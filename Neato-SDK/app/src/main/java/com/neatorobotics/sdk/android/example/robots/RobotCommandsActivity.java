package com.neatorobotics.sdk.android.example.robots;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.neatorobotics.sdk.android.example.R;
import com.neatorobotics.sdk.android.models.Robot;

public class RobotCommandsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot_commands);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Reloading robot state...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                RobotCommandsActivityFragment fragment = ((RobotCommandsActivityFragment)getSupportFragmentManager().findFragmentById(R.id.robotCommandFragment));
                if(fragment != null) {
                    fragment.reloadRobotState();
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null && savedInstanceState == null) {
            Robot robot = (Robot)extras.getSerializable("ROBOT");
            //Inject robot class into fragment
            RobotCommandsActivityFragment fragment = ((RobotCommandsActivityFragment)getSupportFragmentManager().findFragmentById(R.id.robotCommandFragment));
            if(fragment != null) {
                fragment.injectRobot(robot);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
