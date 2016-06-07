package com.neatorobotics.sdk.android.example.robots;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.neatorobotics.sdk.android.NeatoCallback;
import com.neatorobotics.sdk.android.NeatoUser;
import com.neatorobotics.sdk.android.NeatoError;
import com.neatorobotics.sdk.android.example.R;
import com.neatorobotics.sdk.android.NeatoRobot;
import com.neatorobotics.sdk.android.nucleo.RobotCommands;
import com.neatorobotics.sdk.android.nucleo.RobotConstants;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Neato-SDK
 * Created by Marco on 06/05/16.
 * Copyright © 2016 Neato Robotics. All rights reserved.
 **/
public class RobotsFragment extends Fragment {

    private static final String TAG = "RobotsFragment";

    private NeatoUser neatoUser;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeContainer;
    private TextView noRobotsAvailableMessage;

    private ArrayList<NeatoRobot> robots = new ArrayList<>();

    public RobotsFragment() {}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList serializedRobots = new ArrayList();
        for (NeatoRobot robot : robots) {
            serializedRobots.add(robot.serialize());
        }
        outState.putSerializable("ROBOT_LIST",serializedRobots);
    }

    private void restoreState(Bundle inState) {
        robots.clear();
        ArrayList serializedRobots = (ArrayList) inState.getSerializable("ROBOT_LIST");
        for (Object object : serializedRobots) {
            robots.add(NeatoRobot.deserialize(getContext(),(Serializable) object));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        neatoUser = NeatoUser.getInstance(getContext());

        if(savedInstanceState != null) {
            restoreState(savedInstanceState);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_robots, container, false);
        //recycler view
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.robot_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RobotsListAdapter();
        mRecyclerView.setAdapter(mAdapter);

        //swipe to refresh
        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRobots();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.colorPrimaryDark);
        //end swipe to refresh
        noRobotsAvailableMessage = (TextView)rootView.findViewById(R.id.noRobotsAvailableMessage);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState == null) {
            loadRobots();
        }
        mAdapter.notifyDataSetChanged();
    }

    private void loadRobots() {
        neatoUser.loadRobots(new NeatoCallback<ArrayList<NeatoRobot>>(){
            @Override
            public void done(ArrayList<NeatoRobot> result) {
                super.done(result);
                robots.clear();
                robots.addAll(result);
                swipeContainer.setRefreshing(false);
                if(result.size() == 0) {
                    noRobotsAvailableMessage.setVisibility(View.VISIBLE);
                }else {
                    noRobotsAvailableMessage.setVisibility(View.GONE);
                }
                mAdapter.notifyDataSetChanged();

                //request the robot states
                for (NeatoRobot robot : result) {
                    robot.updateRobotState(new NeatoCallback<Void>(){
                        @Override
                        public void done(Void result) {
                            super.done(result);
                            mAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void fail(NeatoError error) {
                            super.fail(error);
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }

            @Override
            public void fail(NeatoError error) {
                super.fail(error);
                swipeContainer.setRefreshing(false);
                if(error == NeatoError.INVALID_TOKEN) {
                    Toast.makeText(getContext(), "Your session is expired.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public class RobotsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            public TextView robotName,robotModel, robotStatus, robotCharge;

            public ItemViewHolder(View v) {
                super(v);
                this.robotName = (TextView) v.findViewById(R.id.robotName);
                this.robotModel = (TextView) v.findViewById(R.id.robotModel);
                this.robotStatus = (TextView) v.findViewById(R.id.robotStatus);
                this.robotCharge = (TextView) v.findViewById(R.id.robotCharge);
                v.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                final NeatoRobot robot = robots.get(position);

                if(robot.getState() != null) {
                    Intent intent = new Intent(getContext(), RobotCommandsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("ROBOT", robot.serialize());
                    startActivity(intent);
                }else {
                    Toast.makeText(getContext(),"No robot state available yet...", Toast.LENGTH_SHORT).show();
                }
            }
        }

        public RobotsListAdapter() {}

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.robot_list_item, parent, false);

            ItemViewHolder vh = new ItemViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ((ItemViewHolder)holder).robotName.setText(robots.get(position).getName());
            ((ItemViewHolder)holder).robotModel.setText(robots.get(position).getModel());
            if(robots.get(position).getState() != null) {
                ((ItemViewHolder) holder).robotCharge.setText(robots.get(position).getState().getCharge() + "%");
                if(robots.get(position).getState().getState() == RobotConstants.ROBOT_STATE_IDLE) {
                    ((ItemViewHolder) holder).robotStatus.setTextColor(getResources().getColor(R.color.colorAccentSecondary));
                    ((ItemViewHolder) holder).robotStatus.setText("ROBOT IDLE");
                }else if(robots.get(position).getState().getState() == RobotConstants.ROBOT_STATE_BUSY){
                    ((ItemViewHolder) holder).robotStatus.setTextColor(getResources().getColor(R.color.yellow));
                    ((ItemViewHolder) holder).robotStatus.setText("ROBOT BUSY");
                }else if(robots.get(position).getState().getState() == RobotConstants.ROBOT_STATE_ERROR){
                    ((ItemViewHolder) holder).robotStatus.setTextColor(getResources().getColor(R.color.colorAccent));
                    ((ItemViewHolder) holder).robotStatus.setText("ROBOT ERROR");
                }
                //TODO you can handle other robot state here if needed
                else {
                    ((ItemViewHolder) holder).robotStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
                    ((ItemViewHolder) holder).robotStatus.setText("OTHER ROBOT STATE");
                }

                ((ItemViewHolder) holder).robotCharge.setTextColor(getResources().getColor(R.color.colorPrimary));
            }else {
                ((ItemViewHolder) holder).robotStatus.setText("Not available or offline");
                ((ItemViewHolder) holder).robotCharge.setText("Battery status not available");
                ((ItemViewHolder) holder).robotStatus.setTextColor(getResources().getColor(R.color.colorAccent));
                ((ItemViewHolder) holder).robotCharge.setTextColor(getResources().getColor(R.color.colorAccent));
            }
        }


        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public int getItemCount() {
            return robots.size();
        }
    }
}