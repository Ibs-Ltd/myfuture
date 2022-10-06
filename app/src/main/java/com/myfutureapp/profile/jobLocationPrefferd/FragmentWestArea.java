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

public class FragmentWestArea extends Fragment {

    public static List<StateWithRegionResponse.StateRegionResponse.StateResponse> stateResponses;
    RecyclerView westAreaRV;
    private LocationCityAdapter locationCityAdapter;
    private BroadcastReceiver mReceiver;

    public FragmentWestArea() {
    }

    public static FragmentWestArea newInstance(List<StateWithRegionResponse.StateRegionResponse.StateResponse> stateResponse) {
        FragmentWestArea fragment = new FragmentWestArea();
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
        View view = inflater.inflate(R.layout.fragment_west_area, container, false);

        westAreaRV = view.findViewById(R.id.westAreaRV);
        westAreaRV.setHasFixedSize(true);
        GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
        westAreaRV.setLayoutManager(mLayoutManager);
        westAreaRV.setItemAnimator(new DefaultItemAnimator());
        locationCityAdapter = new LocationCityAdapter(getContext(), stateResponses);
        westAreaRV.setAdapter(locationCityAdapter);
        receiverRegister();

        return view;
    }
}