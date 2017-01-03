package com.neatorobotics.sdk.android.example.robots;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.neatorobotics.sdk.android.NeatoCallback;
import com.neatorobotics.sdk.android.NeatoError;
import com.neatorobotics.sdk.android.NeatoRobot;
import com.neatorobotics.sdk.android.example.R;
import com.neatorobotics.sdk.android.models.Robot;
import com.neatorobotics.sdk.android.models.ScheduleEvent;
import com.neatorobotics.sdk.android.nucleo.RobotConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;

/**
 * A placeholder fragment containing a simple view.
 */
public class RobotCommandsActivityFragment extends Fragment {

    protected NeatoRobot robot;

    private Button houseCleaning, spotCleaning, pauseCleaning,
                    stopCleaning, resumeCleaning, returnToBaseCleaning, findMe,
                    enableDisableScheduling, scheduleEveryWednesday, getScheduling;

    public RobotCommandsActivityFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("ROBOT",robot.serialize());
    }

    private void restoreState(Bundle inState) {
        robot = new NeatoRobot(getContext(),(Robot) inState.getSerializable("ROBOT"));
        updateUIButtons();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_robot_commands, container, false);

        houseCleaning = (Button)rootView.findViewById(R.id.houseCleaning);
        spotCleaning = (Button)rootView.findViewById(R.id.spotCleaning);
        pauseCleaning = (Button)rootView.findViewById(R.id.pauseCleaning);
        stopCleaning = (Button)rootView.findViewById(R.id.stopCleaning);
        resumeCleaning = (Button)rootView.findViewById(R.id.resumeCleaning);
        returnToBaseCleaning = (Button)rootView.findViewById(R.id.returnToBaseCleaning);
        findMe = (Button)rootView.findViewById(R.id.findMe);
        enableDisableScheduling = (Button)rootView.findViewById(R.id.enableDisableScheduling);
        scheduleEveryWednesday = (Button)rootView.findViewById(R.id.wednesdayScheduling);
        getScheduling = (Button)rootView.findViewById(R.id.getScheduling);

        spotCleaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeSpotCleaning();
            }
        });

        houseCleaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeHouseCleaning();
            }
        });

        pauseCleaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executePause();
            }
        });

        stopCleaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeStop();
            }
        });

        returnToBaseCleaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeReturnToBase();
            }
        });

        resumeCleaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeResumeCleaning();
            }
        });

        findMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeFindMe();
            }
        });

        enableDisableScheduling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableDisableScheduling();
            }
        });

        scheduleEveryWednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleEveryWednesday();
            }
        });

        getScheduling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getScheduling();
            }
        });

        return rootView;
    }

    private void updateUIButtons() {
        if(robot != null && robot.getState() != null) {
            houseCleaning.setEnabled(robot.getState().isStartAvailable());
            spotCleaning.setEnabled(robot.getState().isStartAvailable());
            pauseCleaning.setEnabled(robot.getState().isPauseAvailable());
            stopCleaning.setEnabled(robot.getState().isStopAvailable());
            resumeCleaning.setEnabled(robot.getState().isResumeAvailable());
            returnToBaseCleaning.setEnabled(robot.getState().isGoToBaseAvailable());
        }else {
            houseCleaning.setEnabled(false);
            spotCleaning.setEnabled(false);
            pauseCleaning.setEnabled(false);
            stopCleaning.setEnabled(false);
            resumeCleaning.setEnabled(false);
            returnToBaseCleaning.setEnabled(false);
        }
    }

    private void executePause() {
        if(robot != null) {
            robot.pauseCleaning(new NeatoCallback<Void>(){
                @Override
                public void done(Void result) {
                    super.done(result);
                    updateUIButtons();
                }

                @Override
                public void fail(NeatoError error) {
                    super.fail(error);
                    updateUIButtons();
                }
            });
        }
    }

    private void executeResumeCleaning() {
        if(robot != null) {
            robot.resumeCleaning(new NeatoCallback<Void>(){
                @Override
                public void done(Void result) {
                    super.done(result);
                    updateUIButtons();
                }

                @Override
                public void fail(NeatoError error) {
                    super.fail(error);
                    updateUIButtons();
                }
            });
        }
    }

    private void executeHouseCleaning() {
        if(robot != null) {
            String params = String.format(Locale.US,
                            "{\"category\":%d,\"mode\":%d,\"modifier\":%d,\"navigationMode\":%d}",
                            RobotConstants.ROBOT_CLEANING_CATEGORY_HOUSE,
                            RobotConstants.ROBOT_CLEANING_MODE_ECO,
                            RobotConstants.ROBOT_CLEANING_MODIFIER_NORMAL,
                            RobotConstants.ROBOT_EXTRA_CARE_MODE_OFF);

            robot.startCleaning(params, new NeatoCallback<Void>(){
                @Override
                public void done(Void result) {
                    super.done(result);
                    updateUIButtons();
                }

                @Override
                public void fail(NeatoError error) {
                    super.fail(error);
                    updateUIButtons();
                }
            });
        }
    }

    private void executeReturnToBase() {
        if(robot != null) {
            robot.goToBase(new NeatoCallback<Void>(){
                @Override
                public void done(Void result) {
                    super.done(result);
                    updateUIButtons();
                }

                @Override
                public void fail(NeatoError error) {
                    super.fail(error);
                    updateUIButtons();
                }
            });
        }
    }

    private void executeFindMe() {
        if(robot != null) {
            //check if the robot support this service
            if(robot.hasService("findMe")) {
                robot.findMe(new NeatoCallback<Void>() {
                    @Override
                    public void done(Void result) {
                        super.done(result);
                        updateUIButtons();
                    }

                    @Override
                    public void fail(NeatoError error) {
                        super.fail(error);
                        updateUIButtons();
                    }
                });
            }else {
                Toast.makeText(getContext(),"The robot doesn't support this service.",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void enableDisableScheduling() {
        if(robot != null) {
            if(robot.getState().isScheduleEnabled()) {
                robot.disableScheduling(new NeatoCallback<Void>() {
                    @Override
                    public void done(Void result) {
                        super.done(result);
                        updateUIButtons();
                        Toast.makeText(getContext(),"Scheduling disabled!",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void fail(NeatoError error) {
                        super.fail(error);
                        updateUIButtons();
                        Toast.makeText(getContext(),"Impossible to disable scheduling.",Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                robot.enableScheduling(new NeatoCallback<Void>() {
                    @Override
                    public void done(Void result) {
                        super.done(result);
                        updateUIButtons();
                        Toast.makeText(getContext(),"Scheduling enabled!",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void fail(NeatoError error) {
                        super.fail(error);
                        updateUIButtons();
                        Toast.makeText(getContext(),"Impossible to enable scheduling.",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void scheduleEveryWednesday() {
        if(robot != null) {
            ScheduleEvent everyWednesday = new ScheduleEvent();
            everyWednesday.mode = RobotConstants.ROBOT_CLEANING_MODE_TURBO;
            everyWednesday.day = 3;//0 is Sunday, 1 Monday and so on
            everyWednesday.startTime = "15:00";

            ArrayList<ScheduleEvent> events = new ArrayList<>();
            events.add(everyWednesday);
            robot.setSchedule(events,new NeatoCallback<Void>(){
                @Override
                public void done(Void result) {
                    super.done(result);
                    updateUIButtons();
                    Toast.makeText(getContext(),"Yay! Schedule programmed.",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void fail(NeatoError error) {
                    super.fail(error);
                    updateUIButtons();
                    Toast.makeText(getContext(),"Oops! Impossible to set schedule.",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void getScheduling() {
        if(robot != null) {
            robot.getSchedule(new NeatoCallback<ArrayList<ScheduleEvent>>(){
                @Override
                public void done(ArrayList<ScheduleEvent> result) {
                    super.done(result);
                    updateUIButtons();
                    if(result != null) {
                        Toast.makeText(getContext(), "The robot has " + result.size() + " scheduled events.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void fail(NeatoError error) {
                    super.fail(error);
                    updateUIButtons();
                    Toast.makeText(getContext(),"Oops! Impossible to get schedule.",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void executeStop() {
        if(robot != null) {
            robot.stopCleaning(new NeatoCallback<Void>(){
                @Override
                public void done(Void result) {
                    super.done(result);
                    updateUIButtons();
                }

                @Override
                public void fail(NeatoError error) {
                    super.fail(error);
                    updateUIButtons();
                }
            });
        }
    }

    private void executeSpotCleaning() {
        if(robot != null) {
            String params = String.format(Locale.US,
                    "{\"category\":%d,\"mode\":%d,\"modifier\":%d,\"navigationMode\":%d}",
                    RobotConstants.ROBOT_CLEANING_CATEGORY_SPOT,
                    RobotConstants.ROBOT_CLEANING_MODE_ECO,
                    RobotConstants.ROBOT_CLEANING_MODIFIER_NORMAL,
                    RobotConstants.ROBOT_EXTRA_CARE_MODE_OFF);

            robot.startCleaning(params, new NeatoCallback<Void>(){
                @Override
                public void done(Void result) {
                    super.done(result);
                    updateUIButtons();
                }

                @Override
                public void fail(NeatoError error) {
                    super.fail(error);
                    updateUIButtons();
                }
            });
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null) {
            restoreState(savedInstanceState);
        }
    }

    public void injectRobot(Robot robot) {
        this.robot = new NeatoRobot(getContext(),robot);
        updateUIButtons();
    }

    public void reloadRobotState() {
        robot.updateRobotState(new NeatoCallback<Void>(){
            @Override
            public void done(Void result) {
                super.done(result);
                updateUIButtons();
            }

            @Override
            public void fail(NeatoError error) {
                super.fail(error);
                updateUIButtons();
            }
        });
    }
}
