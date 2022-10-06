package com.myfutureapp.jobApplyLocationSelection;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.gson.JsonObject;
import com.myfutureapp.R;
import com.myfutureapp.dashboard.profile.model.UpdateLocationPreferenceResponse;
import com.myfutureapp.dashboard.profile.ui.ChooseOtherCitiesAdapter;
import com.myfutureapp.dashboard.profile.ui.ChoosePreferredCityAdapter;
import com.myfutureapp.jobApplyLocationSelection.model.JobCitySelectModel;
import com.myfutureapp.jobApplyLocationSelection.presenter.JobApplyLocationPresenter;
import com.myfutureapp.jobDetail.SuccessActivity;

import java.util.ArrayList;
import java.util.List;

public class JobApplyLocationSelectActivity extends AppCompatActivity implements JobApplyView {

    List<JobCitySelectModel> updatedList = new ArrayList<>();
    String jobId = "";
    String toolbarTitle = "", salaryDetails = "";
    int height = 0;
    private ChoosePreferredCityAdapter choosePreferredCityAdapter;
    private ChooseOtherCitiesAdapter chooseOtherCitiesAdapter;
    private RecyclerView rv_recyclerOtherCity, rv_recyclerPreferredCity;
    private EditText et_searchLocation;
    private TextView noCitiesFound;
    private final ArrayList<String> showChoosePreferred = new ArrayList<>();
    private final List<JobCitySelectModel> jobCitySelectModels = new ArrayList<>();
    private boolean searched = false;
    private ImageView serachClearIV;
    private CheckBox prefences;
    private JobApplyLocationPresenter jobApplyLocationPresenter;
    private LinearLayout noPreferncesLL;
    private ArrayList<String> showLocation = new ArrayList<>();
    private boolean btnEnable = false, overLayVisible = false;
    private LinearLayout citySelectionLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_apply_location_select);
        if (getIntent() != null) {
            jobId = getIntent().getStringExtra("jobId");
            showLocation = getIntent().getStringArrayListExtra("locations");
            toolbarTitle = getIntent().getStringExtra("toolbarTitle");
            salaryDetails = getIntent().getStringExtra("salaryDetails");
        }

        for (int i = 0; i < showLocation.size(); i++) {
            jobCitySelectModels.add(new JobCitySelectModel(showLocation.get(i), false));
        }

        ImageView hambarIV = (ImageView) findViewById(R.id.hambarIV);
        hambarIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        inUi();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(JobApplyLocationSelectActivity.this, SuccessActivity.class).
                putExtra("jobId", jobId));
    }

    private void inUi() {
        jobApplyLocationPresenter = new JobApplyLocationPresenter(JobApplyLocationSelectActivity.this, JobApplyLocationSelectActivity.this);
        TextView salaryDetailsTV = (TextView) findViewById(R.id.salaryDetails);
        TextView toolbarTitleTV = (TextView) findViewById(R.id.toolbarTitle);

        citySelectionLayout = (LinearLayout) findViewById(R.id.citySelectionLayout);
        if (salaryDetails != null) {
            salaryDetailsTV.setText(salaryDetails);
        }
        if (toolbarTitle != null) {
            toolbarTitleTV.setText(toolbarTitle);
        }
        et_searchLocation = (EditText) findViewById(R.id.et_searchLocation);
        rv_recyclerOtherCity = (RecyclerView) findViewById(R.id.rv_recyclerOtherCity);
        serachClearIV = (ImageView) findViewById(R.id.serachClearIV);
        noCitiesFound = (TextView) findViewById(R.id.noCitiesFound);
        TextView tv_Submit = (TextView) findViewById(R.id.tv_Submit);
        prefences = (CheckBox) findViewById(R.id.prefences);
        noPreferncesLL = (LinearLayout) findViewById(R.id.noPreferncesLL);


        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(getApplication());
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);

        //lowerone
        chooseOtherCitiesAdapter = new ChooseOtherCitiesAdapter(JobApplyLocationSelectActivity.this, new ArrayList<>(), new ChooseOtherCitiesAdapter.CitySelect() {
            @Override
            public void citySelected(String cityName, int position) {
                if (!overLayVisible) {
                    choosePreferredCityAdapter.notifyItem(cityName);
                    tv_Submit.setBackground(getBaseContext().getResources().getDrawable(R.drawable.rounded_orangish_btn_enable));
                    chooseOtherCitiesAdapter.notifyItemPositionList(position);
                    btnEnable = true;
                }
            }
        });
        rv_recyclerOtherCity.setLayoutManager(flexboxLayoutManager);
        rv_recyclerOtherCity.setAdapter(chooseOtherCitiesAdapter);
        chooseOtherCitiesAdapter.notifyItem(jobCitySelectModels);
        rv_recyclerPreferredCity = (RecyclerView) findViewById(R.id.rv_recyclerPreferredCity);

        //uppervla
        choosePreferredCityAdapter = new ChoosePreferredCityAdapter(this, showChoosePreferred, new ChoosePreferredCityAdapter.PreferredCityRemoved() {
            @Override
            public void preferredCityRemoved(String cityRemovedName, int position) {
                if (!overLayVisible) {
                    for (int i = 0; i < jobCitySelectModels.size(); i++) {
                        if (jobCitySelectModels.get(i).ciyName.equalsIgnoreCase(cityRemovedName)) {
                            if (!searched) {
                                chooseOtherCitiesAdapter.notifyItemPosition(i);
                            } else {
                                jobCitySelectModels.get(i).isCitySelected = !jobCitySelectModels.get(i).isCitySelected;
                            }
                            break;
                        }
                    }
                    for (int i = 0; i < updatedList.size(); i++) {
                        if (updatedList.get(i).ciyName.equalsIgnoreCase(cityRemovedName)) {
                            chooseOtherCitiesAdapter.notifyItemPosition(i);
                            break;
                        }
                    }
                    choosePreferredCityAdapter.remove(position);
                    if (choosePreferredCityAdapter.selectCitySize() == 0) {
                        tv_Submit.setBackground(getBaseContext().getResources().getDrawable(R.drawable.rounded_greish_btn_disable));
                        btnEnable = false;
                    }
                }
            }
        });
        rv_recyclerPreferredCity.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv_recyclerPreferredCity.setNestedScrollingEnabled(false);
        rv_recyclerPreferredCity.setAdapter(choosePreferredCityAdapter);
        et_searchLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() != 0) {
                    searched = true;
                    updatedList.clear();
                    for (int i = 0; i < showLocation.size(); i++) {
                        if (showLocation.get(i).toLowerCase().contains(s.toString().trim().toLowerCase())) {
                            updatedList.add(jobCitySelectModels.get(i));
                        }
                    }
                    chooseOtherCitiesAdapter.notifyItem(updatedList);
                    if (updatedList.size() == 0) {
                        noCitiesFound.setVisibility(View.VISIBLE);
                        rv_recyclerOtherCity.setVisibility(View.GONE);
                    } else {
                        noCitiesFound.setVisibility(View.GONE);
                        rv_recyclerOtherCity.setVisibility(View.VISIBLE);
                    }

                    serachClearIV.setVisibility(View.VISIBLE);
                } else {
                    chooseOtherCitiesAdapter.notifyItem(jobCitySelectModels);
                    updatedList.clear();
                    searched = false;
                    serachClearIV.setVisibility(View.GONE);
                }
            }
        });

        serachClearIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_searchLocation.setText("");
            }
        });
        noPreferncesLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefences.isChecked()) {
                    prefences.setChecked(false);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        citySelectionLayout.setForeground(getBaseContext().getResources().getDrawable(R.drawable.black_overlay_opcaity));
                    }

                    overLayVisible = false;
                    if (choosePreferredCityAdapter.selectCitySize() == 0) {
                        tv_Submit.setBackground(getBaseContext().getResources().getDrawable(R.drawable.rounded_greish_btn_disable));
                        btnEnable = false;
                    }
                } else {
                    prefences.setChecked(true);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        citySelectionLayout.setForeground(getBaseContext().getResources().getDrawable(R.drawable.black_overlay));
                    }
                    overLayVisible = true;
                    tv_Submit.setBackground(getBaseContext().getResources().getDrawable(R.drawable.rounded_orangish_btn_enable));
                    btnEnable = true;
                }
            }
        });
        tv_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnEnable) {
                    if (prefences.isChecked()) {
                        startActivity(new Intent(JobApplyLocationSelectActivity.this, SuccessActivity.class).
                                putExtra("jobId", jobId));
                    } else {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("id", jobId);
                        String cities = "";
                        if (showChoosePreferred.size() != 0) {
                            if (showChoosePreferred.size() > 1) {
                                for (int i = 0; i < showChoosePreferred.size(); i++) {
                                    if (i == showChoosePreferred.size() - 1) {
                                        cities += showChoosePreferred.get(i);
                                    } else {
                                        cities = cities + showChoosePreferred.get(i) + ",";
                                    }
                                }
                            } else {
                                cities = showChoosePreferred.get(0);
                            }
                            jsonObject.addProperty("preferences", cities);
                            jobApplyLocationPresenter.callUpdateLocationPreferenceApi(cities, jobId);
                        } else {
                            showMessage("Please Select any preference");
                        }
                    }
                }
            }
        });
    }

    @Override
    public void setUpdateLocationPreferenceResponse(UpdateLocationPreferenceResponse updateLocationPreferenceResponse) {
        if (updateLocationPreferenceResponse != null && updateLocationPreferenceResponse.success) {
            showMessage(updateLocationPreferenceResponse.message);
            startActivity(new Intent(JobApplyLocationSelectActivity.this, SuccessActivity.class).
                    putExtra("jobId", jobId));

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
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}