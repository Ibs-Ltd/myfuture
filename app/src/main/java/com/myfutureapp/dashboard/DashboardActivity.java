package com.myfutureapp.dashboard;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.anrwatchdog.ANRWatchDog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.myfutureapp.BuildConfig;
import com.myfutureapp.R;
import com.myfutureapp.core.AppUpdate;
import com.myfutureapp.dashboard.adapter.NavAdapter;
import com.myfutureapp.dashboard.application.ApplicationsFragment;
import com.myfutureapp.dashboard.bookmark.BookMarksActivity;
import com.myfutureapp.dashboard.home.NewHomeFragment;
import com.myfutureapp.dashboard.homeProfile.UserProfileFragment;
import com.myfutureapp.dashboard.model.UserProfileResponse;
import com.myfutureapp.dashboard.presenter.DashboardPresenter;
import com.myfutureapp.dashboard.ui.DashboardView;
import com.myfutureapp.login.DataHolder;
import com.myfutureapp.login.LoginActivity;
import com.myfutureapp.util.AppConstants;
import com.myfutureapp.util.AppPreferences;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity implements DashboardView {

    private ActionBarDrawerToggle toggle;
    private Fragment homeFragment, applicationFragment, activeFragment, userProfileFragment;
    private BottomNavigationView bottomNavigationView;
    private TextView toolbarTitle;
    private DashboardPresenter dashboardPresenter;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private LinearLayout homeBottomBar, applicationBottomBar, profileBottomBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        new ANRWatchDog().start();
        dashboardPresenter = new DashboardPresenter(DashboardActivity.this, DashboardActivity.this);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
//        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        homeBottomBar = bottomNavigationView.findViewById(R.id.homeBottomBar);
        ImageView homeBottomBarIV = bottomNavigationView.findViewById(R.id.homeBottomBarIV);
        TextView homeBottomBarTV = bottomNavigationView.findViewById(R.id.homeBottomBarTV);
        applicationBottomBar = bottomNavigationView.findViewById(R.id.applicationBottomBar);
        ImageView applicationBottomBarIV = bottomNavigationView.findViewById(R.id.applicationBottomBarIV);
        TextView applicationBottomBarTV = bottomNavigationView.findViewById(R.id.applicationBottomBarTV);
        profileBottomBar = bottomNavigationView.findViewById(R.id.profileBottomBar);
        ImageView profileBottomBarIV = bottomNavigationView.findViewById(R.id.profileBottomBarIV);
        TextView profileBottomBarTV = bottomNavigationView.findViewById(R.id.profileBottomBarTV);
        homeBottomBar.setOnClickListener(v -> {
            homeBottomBarIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_orange));
            homeBottomBarTV.setTextColor(getResources().getColor(R.color.orange));
            applicationBottomBarIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_applications_grey));
            applicationBottomBarTV.setTextColor(getResources().getColor(R.color.warm_gray_three));
            profileBottomBarIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_grey));
            profileBottomBarTV.setTextColor(getResources().getColor(R.color.warm_gray_three));
            if (!(activeFragment instanceof NewHomeFragment)) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.hide(activeFragment).show(homeFragment).commit();
                activeFragment = homeFragment;
                setToolbarTitle("Home");
            }
        });
        applicationBottomBar.setOnClickListener(v -> {
            homeBottomBarIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_grey));
            homeBottomBarTV.setTextColor(getResources().getColor(R.color.warm_gray_three));
            applicationBottomBarIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_applications_orange));
            applicationBottomBarTV.setTextColor(getResources().getColor(R.color.orange));
            profileBottomBarIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_grey));
            profileBottomBarTV.setTextColor(getResources().getColor(R.color.warm_gray_three));
            if (!(activeFragment instanceof ApplicationsFragment)) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.hide(activeFragment).show(applicationFragment).commit();
                applicationFragment.setUserVisibleHint(true);
                activeFragment = applicationFragment;
                setToolbarTitle("Applications");
            }
        });
        profileBottomBar.setOnClickListener(v -> {
            homeBottomBarIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_grey));
            homeBottomBarTV.setTextColor(getResources().getColor(R.color.warm_gray_three));
            applicationBottomBarIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_applications_grey));
            applicationBottomBarTV.setTextColor(getResources().getColor(R.color.warm_gray_three));
            profileBottomBarIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_orange));
            profileBottomBarTV.setTextColor(getResources().getColor(R.color.orange));
            if (!(activeFragment instanceof UserProfileFragment)) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.hide(activeFragment).show(userProfileFragment).commit();
                activeFragment = userProfileFragment;
                setToolbarTitle("Profile");
                userProfileFragment.setUserVisibleHint(true);
            }
        });
        addFragments();
        drawer.setVisibility(View.VISIBLE);

        ImageView hambarIV = findViewById(R.id.hambarIV);

        hambarIV.setOnClickListener(view -> drawer.openDrawer(GravityCompat.START));

        navHeaderSetup();

        if (AppPreferences.getInstance(getBaseContext()).getPreferencesString(AppConstants.AUTH).equalsIgnoreCase("true")) {
            dashboardPresenter.callUserProfileApi(AppPreferences.getInstance(getBaseContext()).getPreferencesString(AppConstants.STUDENTID));
        }

        if (DataHolder.getInstance().appUpdate != null && DataHolder.getInstance().appUpdate.version_code > BuildConfig.VERSION_CODE) {
            showAlertDialogForUpdateApp(DataHolder.getInstance().appUpdate);
        }
    }

    private void navHeaderSetup() {
        ArrayList<String> data = new ArrayList<>();
        data.add("Home");
        data.add("Bookmarks");
        data.add("Applications");
        data.add("Profile");
        LinearLayout loginDashboard = findViewById(R.id.loginDashboard);
        if (AppPreferences.getInstance(this).getPreferencesString(AppConstants.AUTH).equalsIgnoreCase("true")) {
            loginDashboard.setVisibility(View.GONE);
            data.add("Sign Out");
        } else {
            loginDashboard.setVisibility(View.VISIBLE);
        }
        ImageView crossNav = findViewById(R.id.crossNav);
        RecyclerView navRV = findViewById(R.id.navRV);
        navRV.setHasFixedSize(false);
        navRV.setLayoutManager(new LinearLayoutManager(DashboardActivity.this, LinearLayoutManager.VERTICAL, false));
        navRV.setItemAnimator(new DefaultItemAnimator());
        NavAdapter navAdapter = new NavAdapter(this, data, new NavAdapter.NavigationSelection() {
            @Override
            public void navigationSelected(String navTitle, int position) {
                if (navTitle.equalsIgnoreCase("Home")) {
                    homeBottomBar.callOnClick();
//                    bottomNavigationView.getMenu().getItem(0).setChecked(true);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.hide(activeFragment).show(homeFragment).commit();
                    activeFragment = homeFragment;
                    setToolbarTitle("Home");
                } else if (navTitle.equalsIgnoreCase("Applications")) {
                    applicationBottomBar.callOnClick();
//                    bottomNavigationView.getMenu().getItem(1).setChecked(true);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.hide(activeFragment).show(applicationFragment).commit();
                    applicationFragment.setUserVisibleHint(true);
                    activeFragment = applicationFragment;
                    setToolbarTitle("Applications");
                } else if (navTitle.equalsIgnoreCase("Bookmarks")) {
                    startActivity(new Intent(getBaseContext(), BookMarksActivity.class));
//                    bottomNavigationView.getMenu().getItem(1).setChecked(true);
//                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                    transaction.hide(activeFragment).show(userProfileFragment).commit();
//                    activeFragment = userProfileFragment;
//                    setToolbarTitle("Profile");
                } else if (navTitle.equalsIgnoreCase("Profile")) {
                    profileBottomBar.callOnClick();
                    //          bottomNavigationView.getMenu().getItem(2).setChecked(true);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.hide(activeFragment).show(userProfileFragment).commit();
                    activeFragment = userProfileFragment;
                    userProfileFragment.setUserVisibleHint(true);
                    setToolbarTitle("Profile");
                } else if (navTitle.equalsIgnoreCase("Sign Out")) {
                    signOut();
                }
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        navRV.setAdapter(navAdapter);

       /* navLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);

            }
        });*/

        loginDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
            }
        });


        crossNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
            }
        });

    }

    private void signOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure want to sign out?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppPreferences.getInstance(getApplication()).clearPreferences();
                DataHolder.getInstance().getUserProfileDataresponse = null;
                startActivity(new Intent(getApplicationContext(), LoginActivity.class).addFlags(Intent.
                        FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finishAffinity();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private void addFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        homeFragment = new NewHomeFragment();
        applicationFragment = new ApplicationsFragment();
//        profileFragment = new ProfileFragment();
        userProfileFragment = new UserProfileFragment();

        fragmentManager.beginTransaction().add(R.id.nav_host_fragment, homeFragment, "homeFragment").show(homeFragment).commitAllowingStateLoss();
        fragmentManager.beginTransaction().add(R.id.nav_host_fragment, applicationFragment, "applicationFragment").hide(applicationFragment).commitAllowingStateLoss();
//        fragmentManager.beginTransaction().add(R.id.nav_host_fragment, profileFragment, "profileFragment").hide(profileFragment).commitAllowingStateLoss();
        fragmentManager.beginTransaction().add(R.id.nav_host_fragment, userProfileFragment, "userProfileFragment").hide(userProfileFragment).commitAllowingStateLoss();

        activeFragment = homeFragment;
        homeBottomBar.callOnClick();
    }

    @Override
    public void onBackPressed() {
        if (activeFragment instanceof NewHomeFragment) {
            super.onBackPressed();
        } else {
            homeBottomBar.callOnClick();
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.hide(activeFragment).show(homeFragment).commit();
//            activeFragment = homeFragment;
//            setToolbarTitle("Home");
//            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        }
    }

    private void setToolbarTitle(String title) {
        toolbarTitle.setText(title);
    }

    public void viewApplication() {
        applicationBottomBar.callOnClick();
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.hide(activeFragment).show(applicationFragment).commit();
//        activeFragment = applicationFragment;
//        setToolbarTitle("Applications");
//        bottomNavigationView.getMenu().getItem(0).setChecked(true);

    }

    @Override
    public void setUserProfileResponse(UserProfileResponse userProfileResponse) {
        if (userProfileResponse != null && userProfileResponse.success) {
            DataHolder.getInstance().getUserProfileDataresponse = userProfileResponse;
            Intent intent = new Intent();
            intent.setAction(getString(R.string.app_name) + "user.basic.data.refresh");
            LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);

        }
    }


    @Override
    public void showLoader() {

    }

    @Override
    public void hideLoader() {

    }

    @Override
    public void showMessage(String message) {

    }

    private void showAlertDialogForUpdateApp(AppUpdate appUpdate) {
        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this);
        android.app.AlertDialog alertDialog = dialog.create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle(appUpdate.title);
        alertDialog.setMessage(appUpdate.message);
        alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "Update",
                (dialog12, which) -> redirectStore());
        if (!appUpdate.force_update) {
            alertDialog.setButton(android.app.AlertDialog.BUTTON_NEGATIVE, "No, thanks",
                    (dialog1, which) -> dialog1.dismiss());
        }

        alertDialog.show();
        alertDialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.orange));
        alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.orange));
    }

    private void redirectStore() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.myFuture"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}