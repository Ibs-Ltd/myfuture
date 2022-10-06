package com.myfutureapp.profile.jobLocationPrefferd;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myfutureapp.R;
import com.myfutureapp.profile.jobLocationPrefferd.adapter.LocationCityAdapter;
import com.myfutureapp.profile.model.StateWithRegionResponse;

import java.util.List;

public class FragmentEastArea extends Fragment {


    public static List<StateWithRegionResponse.StateRegionResponse.StateResponse> stateResponses;
    RecyclerView eastAreaRV;
    private LocationCityAdapter locationCityAdapter;
    private BroadcastReceiver mReceiver;

    public FragmentEastArea() {
    }

    public static FragmentEastArea newInstance(List<StateWithRegionResponse.StateRegionResponse.StateResponse> stateResponse) {
        FragmentEastArea fragment = new FragmentEastArea();
        stateResponses = stateResponse;
        return fragment;
    }

    private void receiverRegister() {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (stateResponses != null) {
                    for (int i = 0; i < stateResponses.size(); i++) {
                        stateResponses.get(i).citySelected = false;
                    }
                    locationCityAdapter.notifyDataSetChanged();
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter(getString(R.string.app_name) + "user.all.India.Select");
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_east_area, container, false);

        eastAreaRV = view.findViewById(R.id.eastAreaRV);
        eastAreaRV.setHasFixedSize(true);
        GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
        eastAreaRV.setLayoutManager(mLayoutManager);
        eastAreaRV.setItemAnimator(new DefaultItemAnimator());
        locationCityAdapter = new LocationCityAdapter(getContext(), stateResponses);
        eastAreaRV.setAdapter(locationCityAdapter);
        receiverRegister();

        return view;
    }
}