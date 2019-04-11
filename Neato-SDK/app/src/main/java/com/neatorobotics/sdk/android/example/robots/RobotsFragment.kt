package com.neatorobotics.sdk.android.example.robots

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import com.neatorobotics.sdk.android.NeatoUser
import com.neatorobotics.sdk.android.clients.Resource
import com.neatorobotics.sdk.android.clients.ResourceState
import com.neatorobotics.sdk.android.example.R
import com.neatorobotics.sdk.android.models.Robot
import com.neatorobotics.sdk.android.models.State
import com.neatorobotics.sdk.android.models.updateRobotState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

import java.util.ArrayList

/**
 * Neato-SDK
 * Created by Marco on 06/05/16.
 * Copyright © 2016 Neato Robotics. All rights reserved.
 */
class RobotsFragment : Fragment() {

    // coroutines
    private var myJob: Job = Job()
    private var uiScope: CoroutineScope = CoroutineScope(Dispatchers.Main + myJob)

    private lateinit var neatoUser: NeatoUser

    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    private var swipeContainer: SwipeRefreshLayout? = null
    private var noRobotsAvailableMessage: TextView? = null

    private var robots = ArrayList<Robot>()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("ROBOT_LIST", robots)
    }

    private fun restoreState(inState: Bundle) {
        robots = inState.getParcelableArrayList("ROBOT_LIST")?: arrayListOf()

        if (robots.size == 0) {
            noRobotsAvailableMessage!!.visibility = View.VISIBLE
        } else {
            noRobotsAvailableMessage!!.visibility = View.GONE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        neatoUser = NeatoUser.getInstance(context!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_robots, container, false)
        //recycler view
        mRecyclerView = rootView.findViewById<View>(R.id.robot_recycler_view) as RecyclerView
        mRecyclerView!!.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(activity)
        mRecyclerView!!.layoutManager = mLayoutManager
        mAdapter = RobotsListAdapter()
        mRecyclerView!!.adapter = mAdapter

        //swipe to refresh
        swipeContainer = rootView.findViewById<View>(R.id.swipeContainer) as SwipeRefreshLayout
        // Setup refresh listener which triggers new data loading
        swipeContainer!!.setOnRefreshListener { loadRobots() }
        // Configure the refreshing colors
        swipeContainer!!.setColorSchemeResources(
            R.color.colorPrimary,
            R.color.colorPrimaryDark,
            R.color.colorPrimaryDark
        )
        //end swipe to refresh
        noRobotsAvailableMessage = rootView.findViewById<View>(R.id.noRobotsAvailableMessage) as TextView

        if (savedInstanceState != null) {
            restoreState(savedInstanceState)
        }

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState == null) {
            loadRobots()
        }
        mAdapter!!.notifyDataSetChanged()
    }

    private fun loadRobots() {

        uiScope.launch {
            val result = neatoUser.loadRobots()
            when(result.status) {
                Resource.Status.SUCCESS -> {
                    robots = ArrayList(result.data!!.robots)
                    swipeContainer?.isRefreshing = false
                    if (robots.size == 0) {
                        noRobotsAvailableMessage!!.visibility = View.VISIBLE
                    } else {
                        noRobotsAvailableMessage!!.visibility = View.GONE
                    }
                    mAdapter!!.notifyDataSetChanged()

                    //request the robot states
                    for (robot in robots) {
                        robot.updateRobotState()
                        mAdapter!!.notifyDataSetChanged()
                    }
                }
                else -> {
                    swipeContainer?.isRefreshing = false
                    if (result.code == ResourceState.HTTP_UNAUTHORIZED) {
                        Toast.makeText(context, "Your session is expired.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    inner class RobotsListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        inner class ItemViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

            var robotName = v.findViewById<TextView>(R.id.robotName)
            var robotModel = v.findViewById<TextView>(R.id.robotModel)
            var robotStatus = v.findViewById<TextView>(R.id.robotStatus)
            var robotCharge = v.findViewById<TextView>(R.id.robotCharge)

            init {
                v.setOnClickListener(this)
            }

            override fun onClick(v: View) {
                val position = adapterPosition
                val robot = robots[position]

                if (robot.state != null) {
                    val intent = Intent(context, RobotCommandsActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                    intent.putExtra("ROBOT", robot)
                    startActivity(intent)
                } else {
                    Toast.makeText(context, "No robot state available yet...", Toast.LENGTH_SHORT).show()
                }
            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): RecyclerView.ViewHolder {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.robot_list_item, parent, false)

            return ItemViewHolder(v)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as ItemViewHolder).robotName.text = robots[position].name
            holder.robotModel.text = robots[position].model
            if (robots[position].state != null) {
                holder.robotCharge.text = "${robots[position].state!!.charge}%"
                when {
                    robots[position].state?.state == State.IDLE -> {
                        holder.robotStatus.setTextColor(resources.getColor(R.color.colorAccentSecondary))
                        holder.robotStatus.text = "ROBOT IDLE"
                    }
                    robots[position].state?.state == State.BUSY -> {
                        holder.robotStatus.setTextColor(resources.getColor(R.color.yellow))
                        holder.robotStatus.text = "ROBOT BUSY"
                    }
                    robots[position].state?.state == State.ERROR -> {
                        holder.robotStatus.setTextColor(resources.getColor(R.color.colorAccent))
                        holder.robotStatus.text = "ROBOT ERROR"
                    }
                    else -> {
                        holder.robotStatus.setTextColor(resources.getColor(R.color.colorPrimary))
                        holder.robotStatus.text = "OTHER ROBOT STATE"
                    }
                }//TODO you can handle other robot state here if needed

                holder.robotCharge.setTextColor(resources.getColor(R.color.colorPrimary))
            } else {
                holder.robotStatus.text = "Not available or offline"
                holder.robotCharge.text = "Battery status not available"
                holder.robotStatus.setTextColor(resources.getColor(R.color.colorAccent))
                holder.robotCharge.setTextColor(resources.getColor(R.color.colorAccent))
            }
        }


        override fun getItemViewType(position: Int): Int {
            return 0
        }

        override fun getItemCount(): Int {
            return robots.size
        }
    }

    companion object {

        private val TAG = "RobotsFragment"
    }
}