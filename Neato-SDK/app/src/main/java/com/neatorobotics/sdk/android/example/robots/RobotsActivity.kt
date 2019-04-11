package com.neatorobotics.sdk.android.example.robots

import android.content.Intent
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast

import com.neatorobotics.sdk.android.NeatoUser
import com.neatorobotics.sdk.android.clients.Resource
import com.neatorobotics.sdk.android.example.R
import com.neatorobotics.sdk.android.example.login.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Neato-SDK
 * Created by Marco on 06/05/16.
 * Copyright © 2016 Neato Robotics. All rights reserved.
 */
class RobotsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    // coroutines
    private var myJob: Job = Job()
    private var uiScope: CoroutineScope = CoroutineScope(Dispatchers.Main + myJob)

    private lateinit var neatoUser: NeatoUser
    private lateinit var navigationView: NavigationView

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

        neatoUser = NeatoUser.getInstance(this)

        navigationView = findViewById(R.id.nav_view)

        //Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        //Drawer layout
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        //Navigation view
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        //Retrieve user email
        if (savedInstanceState == null) {

            uiScope.launch {
                val result = neatoUser.getUserInfo()
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
            (navigationView.getHeaderView(0).findViewById<TextView>(R.id.firstNameText)).text = userFirstName
        }
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
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

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun logout() {
        uiScope.launch {
            val result = neatoUser.logout()
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
