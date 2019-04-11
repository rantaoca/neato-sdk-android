package com.neatorobotics.sdk.android.example.robots

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

import com.bumptech.glide.Glide
import com.neatorobotics.sdk.android.clients.Resource
import com.neatorobotics.sdk.android.example.R
import com.neatorobotics.sdk.android.models.CleaningMap
import com.neatorobotics.sdk.android.models.Robot
import com.neatorobotics.sdk.android.models.RobotState
import com.neatorobotics.sdk.android.models.updateRobotState
import com.neatorobotics.sdk.android.robotservices.cleaning.cleaningService
import com.neatorobotics.sdk.android.robotservices.findme.findMeService
import com.neatorobotics.sdk.android.robotservices.scheduling.schedulingService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

import java.util.ArrayList
import java.util.HashMap

class RobotCommandsActivityFragment : Fragment() {

    // coroutines
    private var myJob: Job = Job()
    private var uiScope: CoroutineScope = CoroutineScope(Dispatchers.Main + myJob)


    protected var robot: Robot? = null

    private var houseCleaning: Button? = null
    private var mapCleaning: Button? = null
    private var spotCleaning: Button? = null
    private var pauseCleaning: Button? = null
    private var stopCleaning: Button? = null
    private var resumeCleaning: Button? = null
    private var returnToBaseCleaning: Button? = null
    private var findMe: Button? = null
    private var enableDisableScheduling: Button? = null
    private var scheduleEveryWednesday: Button? = null
    private var getScheduling: Button? = null
    private var maps: Button? = null

    private var mapImageView: ImageView? = null

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("ROBOT", robot)
    }

    private fun restoreState(inState: Bundle) {
        robot = inState.getParcelable("ROBOT")
        updateUIButtons()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_robot_commands, container, false)

        houseCleaning = rootView.findViewById<View>(R.id.houseCleaning) as Button
        spotCleaning = rootView.findViewById<View>(R.id.spotCleaning) as Button
        mapCleaning = rootView.findViewById<View>(R.id.mapCleaning) as Button
        pauseCleaning = rootView.findViewById<View>(R.id.pauseCleaning) as Button
        stopCleaning = rootView.findViewById<View>(R.id.stopCleaning) as Button
        resumeCleaning = rootView.findViewById<View>(R.id.resumeCleaning) as Button
        returnToBaseCleaning = rootView.findViewById<View>(R.id.returnToBaseCleaning) as Button
        findMe = rootView.findViewById<View>(R.id.findMe) as Button
        enableDisableScheduling = rootView.findViewById<View>(R.id.enableDisableScheduling) as Button
        scheduleEveryWednesday = rootView.findViewById<View>(R.id.wednesdayScheduling) as Button
        getScheduling = rootView.findViewById<View>(R.id.getScheduling) as Button
        maps = rootView.findViewById<View>(R.id.maps) as Button
        mapImageView = rootView.findViewById<View>(R.id.mapImage) as ImageView

        spotCleaning!!.setOnClickListener { executeSpotCleaning() }

        houseCleaning!!.setOnClickListener { executeHouseCleaning() }

        mapCleaning!!.setOnClickListener { executeMapCleaning() }

        pauseCleaning!!.setOnClickListener { executePause() }

        stopCleaning!!.setOnClickListener { executeStop() }

        returnToBaseCleaning!!.setOnClickListener { executeReturnToBase() }

        resumeCleaning!!.setOnClickListener { executeResumeCleaning() }

        findMe!!.setOnClickListener { executeFindMe() }

        enableDisableScheduling!!.setOnClickListener { enableDisableScheduling() }

        scheduleEveryWednesday!!.setOnClickListener { scheduleEveryWednesday() }

        getScheduling!!.setOnClickListener { getScheduling() }

        maps!!.setOnClickListener { getMaps() }

        return rootView
    }

    private fun updateUIButtons() {
        if (robot != null && robot!!.state != null) {
            houseCleaning!!.isEnabled = robot!!.state!!.isStartAvailable
            spotCleaning!!.isEnabled = robot!!.state!!.isStartAvailable
            pauseCleaning!!.isEnabled = robot!!.state!!.isPauseAvailable
            stopCleaning!!.isEnabled = robot!!.state!!.isStopAvailable
            resumeCleaning!!.isEnabled = robot!!.state!!.isResumeAvailable
            returnToBaseCleaning!!.isEnabled = robot!!.state!!.isGoToBaseAvailable
        } else {
            houseCleaning!!.isEnabled = false
            spotCleaning!!.isEnabled = false
            pauseCleaning!!.isEnabled = false
            stopCleaning!!.isEnabled = false
            resumeCleaning!!.isEnabled = false
            returnToBaseCleaning!!.isEnabled = false
        }
    }

    private fun executePause() {

        uiScope.launch {
            val result = robot?.cleaningService?.pauseCleaning(robot!!)
            when(result?.status) {
                Resource.Status.ERROR -> Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
            }
            robot?.state = result?.data
            updateUIButtons()
        }
    }

    private fun executeResumeCleaning() {
        uiScope.launch {
            val result = robot?.cleaningService?.resumeCleaning(robot!!)
            when(result?.status) {
                Resource.Status.ERROR -> Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
            }
            robot?.state = result?.data
            updateUIButtons()
        }
    }

    private fun executeHouseCleaning() {
        uiScope.launch {
            val result = robot?.cleaningService?.startCleaning(robot!!)
            when(result?.status) {
                Resource.Status.ERROR -> Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
            }
            robot?.state = result?.data
            updateUIButtons()
        }
    }

    private fun executeMapCleaning() {
        // TODO
    }

    private fun executeReturnToBase() {
        uiScope.launch {
            val result = robot?.cleaningService?.returnToBase(robot!!)
            when(result?.status) {
                Resource.Status.ERROR -> Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
            }
            robot?.state = result?.data
            updateUIButtons()
        }
    }

    private fun executeFindMe() {
        uiScope.launch {
            val result = robot?.findMeService?.findMe(robot!!)
            when(result?.status) {
                Resource.Status.ERROR -> Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun enableDisableScheduling() {
        if (robot != null) {
            if (robot!!.state!!.isScheduleEnabled) {
                uiScope.launch {
                    val result = robot?.schedulingService?.disableSchedule(robot!!)
                    when(result?.status) {
                        Resource.Status.ERROR -> Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                    }
                    robot?.updateRobotState()
                    updateUIButtons()
                }
            } else {
                uiScope.launch {
                    val result = robot?.schedulingService?.enableSchedule(robot!!)
                    when(result?.status) {
                        Resource.Status.ERROR -> Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                    }
                    robot?.updateRobotState()
                    updateUIButtons()
                }
            }
        }
    }

    private fun scheduleEveryWednesday() {
        if (robot != null) {
            val everyWednesday = ScheduleEvent()
            everyWednesday.mode = RobotConstants.ROBOT_CLEANING_MODE_TURBO
            everyWednesday.day = 3//0 is Sunday, 1 Monday and so on
            everyWednesday.startTime = "15:00"

            val events = ArrayList<ScheduleEvent>()
            events.add(everyWednesday)
            robot!!.setSchedule(events, object : NeatoCallback<Void>() {
                override fun done(result: Void) {
                    super.done(result)
                    updateUIButtons()
                    Toast.makeText(context, "Yay! Schedule programmed.", Toast.LENGTH_SHORT).show()
                }

                override fun fail(error: NeatoError) {
                    super.fail(error)
                    updateUIButtons(error)
                    Toast.makeText(context, "Oops! Impossible to set schedule.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun getScheduling() {
        uiScope.launch {
            val result = robot?.schedulingService?.getSchedule(robot!!)
            when(result?.status) {
                Resource.Status.SUCCESS -> {
                    Toast.makeText(
                        context,
                        "The robot has " + (result.data?.events?.size?:0) + " scheduled events.",
                        Toast.LENGTH_SHORT
                    ).show()                }
                Resource.Status.ERROR -> Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
            }
            robot?.updateRobotState()
            updateUIButtons()
        }
    }

    private fun getMapDetails(mapId: String) {
        if (robot != null) {
            robot!!.getMapDetails(mapId, object : NeatoCallback<CleaningMap>() {
                override fun done(map: CleaningMap) {
                    super.done(map)
                    showMapImage(map.url)
                }

                override fun fail(error: NeatoError) {
                    super.fail(error)
                    Toast.makeText(context, "Oops! Impossible to get map details.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun showMapImage(url: String) {
        Glide.with(this).load(url).into(mapImageView!!)
    }

    private fun getMaps() {
        if (robot != null) {
            //check if the robot support this service
            if (robot!!.hasService("maps")) {
                robot!!.getMaps(object : NeatoCallback<List<CleaningMap>>() {
                    override fun done(maps: List<CleaningMap>?) {
                        super.done(maps)
                        updateUIButtons()
                        if (maps != null && maps.isNotEmpty()) {
                            // now you can get a map id and retrieve the map details
                            // to download the map image use the map "url" property
                            // this second call is needed because the map urls expire after a while
                            getMapDetails(maps[0].id)

                        } else {
                            Toast.makeText(
                                context,
                                "No maps available yet. Complete at least one house cleaning to view maps.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun fail(error: NeatoError) {
                        super.fail(error)
                        updateUIButtons()
                        Toast.makeText(context, "Oops! Impossible to get robot maps.", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(context, "The robot doesn't support this service.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun executeStop() {
        if (robot != null) {
            robot!!.stopCleaning(object : NeatoCallback<RobotState>() {
                override fun done(result: RobotState) {
                    super.done(result)
                    updateUIButtons()
                }

                override fun fail(error: NeatoError) {
                    super.fail(error)
                    updateUIButtons(error)
                }
            })
        }
    }

    private fun executeSpotCleaning() {
        if (robot != null) {
            val params = HashMap<String, String>()
            params[RobotConstants.CLEANING_MODE_KEY] = RobotConstants.ROBOT_CLEANING_MODE_ECO.toString() + ""
            params[RobotConstants.CLEANING_AREA_SPOT_HEIGHT_KEY] =
                RobotConstants.ROBOT_CLEANING_SPOT_SIZE_LARGE.toString() + ""
            params[RobotConstants.CLEANING_AREA_SPOT_WIDTH_KEY] =
                RobotConstants.ROBOT_CLEANING_SPOT_SIZE_LARGE.toString() + ""

            robot!!.startSpotCleaning(params, object : NeatoCallback<RobotState>() {
                override fun done(result: RobotState) {
                    super.done(result)
                    updateUIButtons()
                }

                override fun fail(error: NeatoError) {
                    super.fail(error)
                    updateUIButtons(error)
                }
            })
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            restoreState(savedInstanceState)
        }
    }

    fun injectRobot(r: Robot) {
        this.robot = r
        updateUIButtons()
    }

    fun reloadRobotState() {
        uiScope.launch {
            robot?.updateRobotState()
            updateUIButtons()
        }
    }
}
