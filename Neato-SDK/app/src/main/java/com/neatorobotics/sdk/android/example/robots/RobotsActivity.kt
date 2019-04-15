/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.example.robots

import android.content.Intent
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.navigation.NavigationView

import com.neatorobotics.sdk.android.NeatoUser
import com.neatorobotics.sdk.android.clients.Resource
import com.neatorobotics.sdk.android.example.R
import com.neatorobotics.sdk.android.example.login.LoginActivity
import kotlinx.android.synthetic.main.activity_robots.*
import kotlinx.android.synthetic.main.app_bar_robots.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RobotsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    // coroutines
    private var myJob: Job = Job()
    private var uiScope: CoroutineScope = CoroutineScope(Dispatchers.Main + myJob)

    private var userFirstName: String? = null

    private fun restoreState(inState: Bundle) {
        userFirstName = inState.getString("USER_FIRST_NAME")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("USER_FIRST_NAME", userFirstName)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_robots)

        //Toolbar
        setSupportActionBar(toolbar)

        //Drawer layout
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        //Navigation view
        nav_view.setNavigationItemSelectedListener(this)

        //Retrieve user email
        if (savedInstanceState == null) {

            uiScope.launch {
                val result = NeatoUser.getUserInfo()
                when(result.status) {
                    Resource.Status.SUCCESS -> {
                        userFirstName = result.data?.first_name?:""
                        fillUserInfo()
                    }else -> {
                        Toast.makeText(applicationContext, result.message?:"error", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        } else {
            restoreState(savedInstanceState)
            fillUserInfo()
        }
    }

    private fun fillUserInfo() {
        if (userFirstName != null) {
            (nav_view.getHeaderView(0).findViewById<TextView>(R.id.firstNameText)).text = userFirstName
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.nav_logout) {
            logout()
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun logout() {
        uiScope.launch {
            val result = NeatoUser.logout()
            if(isFinishing) return@launch

            when (result.status) {
                Resource.Status.SUCCESS -> {
                    val intent = Intent(applicationContext, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else -> {
                    Toast.makeText(applicationContext, "Error during logout", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
