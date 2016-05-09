package com.neatorobotics.sdk.android.example.robots;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.neatorobotics.sdk.android.NeatoCallback;
import com.neatorobotics.sdk.android.NeatoUser;
import com.neatorobotics.sdk.android.NeatoError;
import com.neatorobotics.sdk.android.example.R;
import com.neatorobotics.sdk.android.example.login.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Neato-SDK
 * Created by Marco on 06/05/16.
 * Copyright © 2016 Neato Robotics. All rights reserved.
 **/
public class RobotsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NeatoUser neatoUser;
    private NavigationView navigationView;

    private String userEmail;

    private void restoreState(Bundle inState) {
        userEmail = inState.getString("EMAIL");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("EMAIL", userEmail);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robots);

        neatoUser = NeatoUser.getInstance(this);

        navigationView = (NavigationView)findViewById(R.id.nav_view);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Drawer layout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Navigation view
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Retrieve user email
        if(savedInstanceState == null) {
            neatoUser.getUserInfo(new NeatoCallback<JSONObject>(){
                @Override
                public void done(JSONObject result) {
                    super.done(result);
                    try {
                        userEmail = result.getString("email");
                    } catch (JSONException e) {e.printStackTrace();}
                    fillUserInfo();
                }

                @Override
                public void fail(NeatoError error) {
                    super.fail(error);
                }
            });
        }else {
            restoreState(savedInstanceState);
            fillUserInfo();
        }
    }

    private void fillUserInfo() {
        if(userEmail!= null) {
            ((TextView)(navigationView.getHeaderView(0).findViewById(R.id.emailText))).setText(userEmail);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        neatoUser.logout(new NeatoCallback<Boolean>(){
            @Override
            public void done(Boolean result) {
                super.done(result);
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void fail(NeatoError error) {
                super.fail(error);
                if(RobotsActivity.this.isFinishing()) return;
                Toast.makeText(getApplicationContext(),"Error during logout",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
