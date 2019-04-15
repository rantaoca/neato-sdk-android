/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

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
import com.neatorobotics.sdk.android.clients.nucleo.Nucleo
import com.neatorobotics.sdk.android.example.R
import com.neatorobotics.sdk.android.models.*
import com.neatorobotics.sdk.android.robotservices.cleaning.CleaningParams
import com.neatorobotics.sdk.android.robotservices.cleaning.cleaningService
import com.neatorobotics.sdk.android.robotservices.findme.findMeService
import com.neatorobotics.sdk.android.robotservices.housecleaning.houseCleaningService
import com.neatorobotics.sdk.android.robotservices.maps.mapService
import com.neatorobotics.sdk.android.robotservices.scheduling.schedulingService
import com.neatorobotics.sdk.android.robotservices.spotcleaning.spotCleaningService
import kotlinx.android.synthetic.main.fragment_robot_commands.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RobotCommandsActivityFragment : Fragment() {

    // coroutines
    private var myJob: Job = Job()
    private var uiScope: CoroutineScope = CoroutineScope(Dispatchers.Main + myJob)

    private var robot: Robot? = null
    
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
        
        return inflater.inflate(R.layout.fragment_robot_commands, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        spotCleaning.setOnClickListener { executeSpotCleaning() }
        houseCleaning.setOnClickListener { executeHouseCleaning() }
        mapCleaning.setOnClickListener { executeMapCleaning() }
        pauseCleaning.setOnClickListener { executePause() }
        stopCleaning.setOnClickListener { executeStop() }
        returnToBaseCleaning.setOnClickListener { executeReturnToBase() }
        resumeCleaning.setOnClickListener { executeResumeCleaning() }
        findMe.setOnClickListener { executeFindMe() }
        enableDisableScheduling.setOnClickListener { enableDisableScheduling() }
        wednesdayScheduling.setOnClickListener { scheduleEveryWednesday() }
        getScheduling!!.setOnClickListener { getScheduling() }
        maps.setOnClickListener { getMaps() }
    }

    private fun updateUIButtons() {
        if (robot != null && robot?.state != null) {
            houseCleaning.isEnabled = robot?.state?.isStartAvailable?:false
            spotCleaning.isEnabled = robot?.state?.isStartAvailable?:false
            pauseCleaning.isEnabled = robot?.state?.isPauseAvailable?:false
            stopCleaning.isEnabled = robot?.state?.isStopAvailable?:false
            resumeCleaning.isEnabled = robot?.state?.isResumeAvailable?:false
            returnToBaseCleaning.isEnabled = robot?.state?.isGoToBaseAvailable?:false
        } else {
            houseCleaning.isEnabled = false
            spotCleaning.isEnabled = false
            pauseCleaning.isEnabled = false
            stopCleaning.isEnabled = false
            resumeCleaning.isEnabled = false
            returnToBaseCleaning.isEnabled = false
        }
    }

    private fun executePause() {

        uiScope.launch {
            val result = robot?.cleaningService?.pauseCleaning(robot!!)
            if(!isAdded) return@launch
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
            if(!isAdded) return@launch
            when(result?.status) {
                Resource.Status.ERROR -> Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
            }
            robot?.state = result?.data
            updateUIButtons()
        }
    }

    private fun executeHouseCleaning() {
        uiScope.launch {

            val params = CleaningParams(category = CleaningCategory.HOUSE, mode = CleaningMode.TURBO)

            val result = robot?.houseCleaningService?.startCleaning(robot!!, params)
            if(!isAdded) return@launch
            when(result?.status) {
                Resource.Status.ERROR -> Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
            }
            robot?.state = result?.data
            updateUIButtons()
        }
    }

    private fun executeMapCleaning() {

        // first check if the robot support map cleaning service
        if (robot?.mapService?.isFloorPlanSupported == true) {

            uiScope.launch {
                val params = CleaningParams(category = CleaningCategory.MAP, mode = CleaningMode.TURBO)

                val result = robot?.houseCleaningService?.startCleaning(robot!!, params)
                when (result?.status) {
                    Resource.Status.ERROR -> Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                }
                robot?.state = result?.data
                updateUIButtons()
            }
        }else showNotSupportedService()
    }

    private fun executeReturnToBase() {
        uiScope.launch {
            val result = robot?.cleaningService?.returnToBase(robot!!)
            if(!isAdded) return@launch
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
            if(!isAdded) return@launch
            when(result?.status) {
                Resource.Status.ERROR -> Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                null -> showNotSupportedService()
            }
        }
    }

    private fun enableDisableScheduling() {
        if (robot != null) {
            if (robot?.state?.isScheduleEnabled == true) {
                uiScope.launch {
                    val result = robot?.schedulingService?.disableSchedule(robot!!)
                    if(!isAdded) return@launch
                    when(result?.status) {
                        Resource.Status.SUCCESS -> Toast.makeText(context, "Successfully enabled/disabled schedule", Toast.LENGTH_SHORT).show()
                        else -> Toast.makeText(context, result?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                uiScope.launch {
                    val result = robot?.schedulingService?.enableSchedule(robot!!)
                    if(!isAdded) return@launch
                    when(result?.status) {
                        Resource.Status.SUCCESS -> Toast.makeText(context, "Successfully enabled/disabled schedule", Toast.LENGTH_SHORT).show()
                        else -> Toast.makeText(context, result?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun scheduleEveryWednesday() {

        uiScope.launch {
            val everyWednesday = ScheduleEvent().apply {
                mode = CleaningMode.TURBO
                day = 3//0 is Sunday, 1 Monday and so on
                startTime = "15:00"
            }
            val robotSchedule = RobotSchedule(true, arrayListOf(everyWednesday))
            val result = robot?.schedulingService?.setSchedule(robot!!, robotSchedule)
            if(!isAdded) return@launch
            when(result?.status) {
                Resource.Status.SUCCESS -> {
                    Toast.makeText(context, "Yay! Schedule programmed.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(context, "Oops! Impossible to set schedule: "+result?.message, Toast.LENGTH_SHORT).show()
                }
            }
            robot?.updateRobotState()
            updateUIButtons()
        }
    }

    private fun getScheduling() {
        uiScope.launch {
            val result = robot?.schedulingService?.getSchedule(robot!!)
            if(!isAdded) return@launch
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

        uiScope.launch {
            val result = robot?.mapService?.getCleaningMap(robot!!, mapId)
            if(!isAdded) return@launch
            when(result?.status) {
                Resource.Status.SUCCESS -> {
                    showMapImage(result.data?.url?:"")
                }
                null -> showNotSupportedService()
                else -> {
                    Toast.makeText(context, "Oops! Impossible to get map details.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showMapImage(url: String) {
        Glide.with(this).load(url).into(mapImage)
    }

    private fun getMaps() {

        uiScope.launch {
            val result = robot?.mapService?.getCleaningMaps(robot!!)
            if(!isAdded) return@launch
            when(result?.status) {
                Resource.Status.SUCCESS -> {
                    if (result.data != null && result.data?.isNotEmpty() == true) {
                        // now you can get a map id and retrieve the map details
                        // to download the map image use the map "url" property
                        // this second call is needed because the map urls expire after a while
                        getMapDetails(result.data!![0].id?:"")

                    } else {
                        Toast.makeText(
                            context,
                            "No maps available yet. Complete at least one house cleaning to view maps.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                null -> showNotSupportedService()
                else -> {
                    Toast.makeText(context, "Oops! Impossible to get robot maps.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun executeStop() {
        uiScope.launch {
            val result = robot?.cleaningService?.stopCleaning(robot!!)
            if(!isAdded) return@launch
            when(result?.status) {
                Resource.Status.ERROR -> Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
            }
            robot?.state = result?.data
            updateUIButtons()
        }
    }

    private fun executeSpotCleaning() {
        uiScope.launch {

            val params = CleaningParams(category = CleaningCategory.SPOT, mode = CleaningMode.TURBO, modifier = CleaningModifier.DOUBLE)

            val result = robot?.spotCleaningService?.startCleaning(robot!!, params)
            if(!isAdded) return@launch
            when(result?.status) {
                Resource.Status.ERROR -> Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
            }
            robot?.state = result?.data
            updateUIButtons()
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

    private fun showNotSupportedService() {
        Toast.makeText(context, "Service non supported", Toast.LENGTH_SHORT).show()
    }
}
