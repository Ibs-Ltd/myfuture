package com.myfutureapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.gson.Gson;
import com.myfutureapp.core.AppUpdate;
import com.myfutureapp.dashboard.DashboardActivity;
import com.myfutureapp.enrollment.EnrollmentTermAndConditionActivity;
import com.myfutureapp.login.DataHolder;
import com.myfutureapp.login.LoginActivity;
import com.myfutureapp.util.AppConstants;
import com.myfutureapp.util.AppPreferences;

public class SplashActivity extends AppCompatActivity {

    private FirebaseRemoteConfig firebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchRemoteConfig();

    }

    private void fetchRemoteConfig() {
        // Fetch singleton FirebaseRemoteConfig object
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(10800)
                .build();
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        //setting the default values for the UI
        firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
        setRemoteConfigValues();
    }

    private void setRemoteConfigValues() {
        String appUpdate = firebaseRemoteConfig.getString(AppConstants.UPDATE_AVAILABLE);
        DataHolder.getInstance().appUpdate = new Gson().fromJson(appUpdate, AppUpdate.class);
        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()) {
                    String appUpdate = firebaseRemoteConfig.getString(AppConstants.UPDATE_AVAILABLE);
                    Log.e("appupdateData", appUpdate);
                    DataHolder.getInstance().appUpdate = new Gson().fromJson(appUpdate, AppUpdate.class);
                }
                if (!AppPreferences.getInstance(getBaseContext()).getPreferencesString(AppConstants.AUTH).equalsIgnoreCase("true")) {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
                }
                finish();
            }
        });
    }
}