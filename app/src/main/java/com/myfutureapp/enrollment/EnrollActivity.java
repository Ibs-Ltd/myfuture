package com.myfutureapp.enrollment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myfutureapp.R;
import com.myfutureapp.dashboard.DashboardActivity;
import com.myfutureapp.enrollment.adapter.ClearedJobAdapter;
import com.myfutureapp.enrollment.model.EnrollUserResponse;
import com.myfutureapp.enrollment.presenter.EnrollPresenter;
import com.myfutureapp.enrollment.ui.EnrollView;
import com.myfutureapp.jobDetail.model.JobDetailResponse;
import com.myfutureapp.util.DeBouncedClickListener;
import com.myfutureapp.util.Helper;
import com.myfutureapp.util.SimpleSpanBuilder;

import java.util.ArrayList;

public class EnrollActivity extends AppCompatActivity implements EnrollView {

    ArrayList<String> showLocation = new ArrayList<>();
    private RecyclerView appliedOppRV;
    private TextView enrollStatus;
    private EnrollPresenter enrollPresenter;
    private String jobId;
    private TextView salaryDetails, toolbarTitle;
    private boolean askPrefernces = false;
    private String loginRequired = "";
    private boolean applyJob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll);
        jobId = getIntent().getStringExtra("jobId");
        applyJob = getIntent().getBooleanExtra("applyJob", false);

        loginRequired = getIntent().getStringExtra("loginRequired");
        inUi();
    }

    private void inUi() {
        enrollPresenter = new EnrollPresenter(EnrollActivity.this, EnrollActivity.this);

        ImageView hambarIV = (ImageView) findViewById(R.id.hambarIV);
        hambarIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        salaryDetails = (TextView) findViewById(R.id.salaryDetails);
        toolbarTitle = (TextView) findViewById(R.id.toolbarTitle);

        SimpleSpanBuilder ssb = new SimpleSpanBuilder();
        ssb.append("Get started now without any ", new RelativeSizeSpan(1.1f));
        ssb.append("upfront payment, ", new ForegroundColorSpan(Color.BLACK), new RelativeSizeSpan(1.1f));
        ssb.append("amount ", new ForegroundColorSpan(Color.BLACK), new RelativeSizeSpan(1.1f));
        ssb.append("pay only ", new RelativeSizeSpan(1.1f));
        ssb.append("2 months ", new ForegroundColorSpan(Color.BLACK), new RelativeSizeSpan(1.1f));
        ssb.append("of your salary after placement", new RelativeSizeSpan(1.1f));

        TextView tv_name = (TextView) findViewById(R.id.tv_name);
        tv_name.setText(ssb.build());
        enrollStatus = (TextView) findViewById(R.id.enrollStatus);

        appliedOppRV = (RecyclerView) findViewById(R.id.appliedOppRV);
        appliedOppRV.setHasFixedSize(true);
        LinearLayoutManager jobEligibilityLinearLayoutManager = new LinearLayoutManager(EnrollActivity.this, RecyclerView.VERTICAL, false);
        appliedOppRV.setLayoutManager(jobEligibilityLinearLayoutManager);
        appliedOppRV.setItemAnimator(new DefaultItemAnimator());
        enrollStatus.setOnClickListener(new DeBouncedClickListener(2000) {
            @Override
            public void onDeBounceClick(View view) {
                Intent intent = new Intent(EnrollActivity.this, EnrollmentTermAndConditionActivity.class);
                intent.putStringArrayListExtra("locations", showLocation);
                intent.putExtra("jobId", jobId);
                intent.putExtra("askPrefernces", askPrefernces);
                intent.putStringArrayListExtra("locations", showLocation);
                intent.putExtra("toolbarTitle", toolbarTitle.getText().toString());
                intent.putExtra("salaryDetails", salaryDetails.getText().toString());
                intent.putExtra("applyJob", applyJob);
                startActivity(intent);
            }
        });

        enrollPresenter.callEnrollUserApi();
        enrollPresenter.callOpportunityDetailApi(jobId);
    }

    @Override
    public void onBackPressed() {
        if (loginRequired.equalsIgnoreCase("true")) {
            startActivity(new Intent(EnrollActivity.this, DashboardActivity.class).addFlags(Intent.
                    FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        } else {
            super.onBackPressed();

        }

        super.onBackPressed();
    }

    @Override
    public void setOpportunityDetailResponse(JobDetailResponse jobDetailResponse) {
        if (jobDetailResponse != null && jobDetailResponse.success) {

            askPrefernces = jobDetailResponse.data.basic.ask_preference;
            if (jobDetailResponse.data.basic.showLocation != null && jobDetailResponse.data.basic.showLocation.size() != 0) {
                showLocation = jobDetailResponse.data.basic.showLocation;
                Log.e("showLocations", String.valueOf(showLocation));
            }
            if (Helper.isContainValue(jobDetailResponse.data.basic.designation)) {
                toolbarTitle.setText(jobDetailResponse.data.company_data.name + " - " + jobDetailResponse.data.basic.designation);
            }
            if (Helper.isContainValue(jobDetailResponse.data.basic.compensation_show)) {
                salaryDetails.setText(jobDetailResponse.data.basic.compensation_show);
            }
        }

    }

    @Override
    public void setEnrollUserResponse(EnrollUserResponse enrollUserResponse) {
        if (enrollUserResponse != null && enrollUserResponse.success) {
            if (enrollUserResponse.data.user_data != null) {
                ClearedJobAdapter clearedJobAdapter = new ClearedJobAdapter(EnrollActivity.this, enrollUserResponse.data.user_data);
                appliedOppRV.setAdapter(clearedJobAdapter);
                appliedOppRV.setVisibility(View.VISIBLE);
            } else {
                appliedOppRV.setVisibility(View.GONE);
            }

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
}