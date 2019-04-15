/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.example.robots

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
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
import com.neatorobotics.sdk.android.models.*
import kotlinx.android.synthetic.main.fragment_robots.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

import java.util.ArrayList
import java.util.concurrent.atomic.AtomicInteger

class RobotsFragment : Fragment() {

    // coroutines
    private var myJob: Job = Job()
    private var uiScope: CoroutineScope = CoroutineScope(Dispatchers.Main + myJob)

    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null

    private var robots = ArrayList<Robot>()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("ROBOT_LIST", robots)
    }

    private fun restoreState(inState: Bundle) {
        robots = inState.getParcelableArrayList("ROBOT_LIST")?: arrayListOf()

        if (robots.size == 0) {
            noRobotsAvailableMessage.visibility = View.VISIBLE
        } else {
            noRobotsAvailableMessage.visibility = View.GONE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_robots, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //recycler view
        robot_recycler_view.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(activity)
        robot_recycler_view.layoutManager = mLayoutManager
        mAdapter = RobotsListAdapter()
        robot_recycler_view.adapter = mAdapter

        //swipe to refresh
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener { loadRobots() }
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(
            R.color.colorPrimary,
            R.color.colorPrimaryDark,
            R.color.colorPrimaryDark
        )
        //end swipe to refresh

        if (savedInstanceState != null) {
            restoreState(savedInstanceState)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState == null) {
            loadRobots()
        }
        mAdapter!!.notifyDataSetChanged()
    }

    var lock = AtomicInteger(0)
    private fun loadRobots() {
        uiScope.launch {
            val result = NeatoUser.loadRobots()
            if(!isAdded) return@launch

            when(result.status) {
                Resource.Status.SUCCESS -> {
                    swipeContainer?.isRefreshing = false

                    if(lock.get() != 0) return@launch

                    robots.clear()
                    robots.addAll(result.data as List<Robot>)

                    lock.set(robots.size)

                    if (robots.size == 0) {
                        noRobotsAvailableMessage.visibility = View.VISIBLE
                    } else {
                        noRobotsAvailableMessage.visibility = View.GONE
                    }
                    mAdapter?.notifyDataSetChanged()

                    //request the robot states
                    robots.forEach { robot ->
                        robot.updateRobotState()
                        mAdapter?.notifyDataSetChanged()
                        lock.set(lock.get() - 1)
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

                if (robot.state == null || robot.state?.isNotAvailableOrOffline() == true) {
                    Toast.makeText(context, "No robot state available yet...", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(context, RobotCommandsActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                        putExtra("ROBOT", robot)
                    }

                    startActivity(intent)
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
            val robotState = robots[position].state
            if (robotState != null) {

                holder.robotCharge.text = "${robotState.charge}%"
                when {
                    robotState.isNotAvailableOrOffline() -> {
                        holder.apply {
                            robotStatus.text = "Not available or offline"
                            robotCharge.text = "Battery status not available"
                            robotStatus.setTextColor(resources.getColor(R.color.colorAccent))
                            robotCharge.setTextColor(resources.getColor(R.color.colorAccent))
                        }
                    }
                    robotState.iscleaning -> {
                        holder.apply {
                            robotStatus.setTextColor(resources.getColor(R.color.colorAccentSecondary))
                            robotStatus.text = "CLEANING"
                        }
                    }
                    robotState.state == State.IDLE -> {
                        holder.apply {
                            robotStatus.setTextColor(resources.getColor(R.color.colorAccentSecondary))
                            robotStatus.text = "ROBOT IDLE"
                        }
                    }
                    robotState.state == State.BUSY -> {
                        holder.apply {
                            robotStatus.setTextColor(resources.getColor(R.color.yellow))
                            robotStatus.text = "ROBOT BUSY"
                        }
                    }
                    robotState.state == State.ERROR -> {
                        holder.apply {
                            robotStatus.setTextColor(resources.getColor(R.color.colorAccent))
                            robotStatus.text = "ROBOT ERROR"
                        }
                    }
                }//TODO you can handle other robot state here if needed

                holder.robotCharge.setTextColor(resources.getColor(R.color.colorPrimary))
            } else {
                holder.apply {
                    robotStatus.text = "Not available or offline"
                    robotCharge.text = "Battery status not available"
                    robotStatus.setTextColor(resources.getColor(R.color.colorAccent))
                    robotCharge.setTextColor(resources.getColor(R.color.colorAccent))
                }
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

        private const val TAG = "RobotsFragment"
    }
}