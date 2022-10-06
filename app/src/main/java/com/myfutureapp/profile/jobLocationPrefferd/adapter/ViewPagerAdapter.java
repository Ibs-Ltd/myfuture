package com.myfutureapp.profile.jobLocationPrefferd.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.myfutureapp.profile.jobLocationPrefferd.FragmentCentralArea;
import com.myfutureapp.profile.jobLocationPrefferd.FragmentEastArea;
import com.myfutureapp.profile.jobLocationPrefferd.FragmentNorthArea;
import com.myfutureapp.profile.jobLocationPrefferd.FragmentSouthArea;
import com.myfutureapp.profile.jobLocationPrefferd.FragmentWestArea;
import com.myfutureapp.profile.model.StateWithRegionResponse;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public StateWithRegionResponse stateWithRegionResponse;

    public ViewPagerAdapter(FragmentManager fm, StateWithRegionResponse stateWithRegionResponse) {
        super(fm);
        this.stateWithRegionResponse = stateWithRegionResponse;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = FragmentNorthArea.newInstance(stateWithRegionResponse.data.get(0).state);
        } else if (position == 1) {
            fragment = FragmentEastArea.newInstance(stateWithRegionResponse.data.get(1).state);
        } else if (position == 2) {
            fragment = FragmentCentralArea.newInstance(stateWithRegionResponse.data.get(2).state);
        } else if (position == 3) {
            fragment = FragmentSouthArea.newInstance(stateWithRegionResponse.data.get(3).state);
        } else if (position == 4) {
            fragment = FragmentWestArea.newInstance(stateWithRegionResponse.data.get(4).state);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0) {
            title = "North";
        } else if (position == 1) {
            title = "East";
        } else if (position == 2) {
            title = "Central";
        } else if (position == 3) {
            title = "South";
        } else if (position == 4) {
            title = "West";
        }
        return title;
    }
}