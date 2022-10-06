package com.myfutureapp.dashboard.application;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;
import com.myfutureapp.R;
import com.myfutureapp.dashboard.application.ui.OldFragment;
import com.myfutureapp.dashboard.application.ui.OnGoingFragment;

public class ApplicationsFragment extends Fragment {
    private TabLayout tabLayout;
    private Fragment onGoingFragment, oldFragment, activeFragment;
    private boolean isLoaded = false;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            Log.e("isVisibleToUser", "true");
            if (!isLoaded) {
                FragmentManager fragmentManager = getChildFragmentManager();
                onGoingFragment = new OnGoingFragment();
                oldFragment = new OldFragment();
                fragmentManager.beginTransaction().add(R.id.frame_container, onGoingFragment, "onGoingFragment").show(onGoingFragment).commitAllowingStateLoss();
                fragmentManager.beginTransaction().add(R.id.frame_container, oldFragment, "applicationFragment").hide(oldFragment).commitAllowingStateLoss();
                activeFragment = onGoingFragment;
                tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        if (tab.getPosition() == 0) {
                            replaceFragment(onGoingFragment);
                        } else if (tab.getPosition() == 1) {
                            replaceFragment(oldFragment);
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
                tabLayout.getTabAt(0);
                isLoaded = true;
            }
        } else {
            Log.e("isVisibleToUser", "false");
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_applications, container, false);
        inUi(root);
        return root;
    }

    private void inUi(View view) {
        setUserVisibleHint(false);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Ongoing"));
        tabLayout.addTab(tabLayout.newTab().setText("History"));

    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.hide(activeFragment).show(fragment).commit();
        activeFragment = fragment;
    }
}