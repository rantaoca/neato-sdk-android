package com.neatorobotics.sdk.android.example.robots

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem

import com.neatorobotics.sdk.android.example.R
import com.neatorobotics.sdk.android.models.Robot
import kotlinx.android.synthetic.main.activity_robot_commands.*

class RobotCommandsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_robot_commands)
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Reloading robot state...", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            val fragment =
                supportFragmentManager.findFragmentById(R.id.robotCommandFragment) as RobotCommandsActivityFragment?
            fragment?.reloadRobotState()
        }

        val extras = intent.extras
        if (extras != null && savedInstanceState == null) {
            val robot = extras.getParcelable<Robot>("ROBOT")!!
            //Inject robot class into fragment
            val fragment =
                supportFragmentManager.findFragmentById(R.id.robotCommandFragment) as RobotCommandsActivityFragment?
            fragment?.injectRobot(robot)
        }
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(menuItem)
    }
}
