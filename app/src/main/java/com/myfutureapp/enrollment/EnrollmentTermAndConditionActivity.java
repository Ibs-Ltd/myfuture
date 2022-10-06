package com.myfutureapp.enrollment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.myfutureapp.R;
import com.myfutureapp.enrollment.model.EnrollUserResponse;
import com.myfutureapp.enrollment.presenter.EnrollTermsConditionPresenter;
import com.myfutureapp.enrollment.ui.EnrollTermsConditionView;
import com.myfutureapp.jobApplyLocationSelection.JobApplyLocationSelectActivity;
import com.myfutureapp.jobDetail.SuccessActivity;
import com.myfutureapp.jobDetail.model.ApplyOpportunityResponse;
import com.myfutureapp.jobDetail.model.InterviewTimeSlotsResponse;
import com.myfutureapp.jobDetail.model.ScheduleInterviewResponse;
import com.myfutureapp.termsCondition.TermsConditionActivity;
import com.myfutureapp.util.SimpleSpanBuilder;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.util.ArrayList;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class EnrollmentTermAndConditionActivity extends AppCompatActivity implements EnrollTermsConditionView {

    ArrayList<String> showLocation = new ArrayList<>();
    private TextView tvSalaryMonth, tvTotalpay, tvMonthEMI, tv_name, tv_name2;
    private CircularProgressButton enrollSubmit;
    private CheckBox agreeTremAndCondition;
    private EnrollTermsConditionPresenter enrollTermsConditionPresenter;
    private String jobId, strGetMonthly, strHalfYearly;
    private Spinner interviewTimeSpinner;
    private IndicatorSeekBar indicatorSeekBar;
    private BottomSheetDialog scheduleBottomDialog;
    private String toolbarTitle, salaryDetails;
    private boolean applyJob;
    private boolean askPrefernces = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment_term_and_condition);
        if (getIntent() != null) {
            jobId = getIntent().getStringExtra("jobId");
            showLocation = getIntent().getStringArrayListExtra("locations");
            askPrefernces = getIntent().getBooleanExtra("askPrefernces", false);
            toolbarTitle = getIntent().getStringExtra("toolbarTitle");
            salaryDetails = getIntent().getStringExtra("salaryDetails");
            applyJob = getIntent().getBooleanExtra("applyJob", false);
        }


        SimpleSpanBuilder ssb = new SimpleSpanBuilder();
        ssb.append("After your placement ", new ForegroundColorSpan(Color.BLACK), new RelativeSizeSpan(1.1f));
        ssb.append("you have to pay \n", new RelativeSizeSpan(1.1f));
        ssb.append("the amount ", new ForegroundColorSpan(Color.BLACK), new RelativeSizeSpan(1.1f));
        ssb.append("equal to ", new RelativeSizeSpan(1.1f));
        ssb.append("2 months ", new ForegroundColorSpan(Color.BLACK), new RelativeSizeSpan(1.1f));
        ssb.append("of your salary", new RelativeSizeSpan(1.1f));
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_name.setText(ssb.build());

        SimpleSpanBuilder ssb1 = new SimpleSpanBuilder();
        ssb1.append("I agree to the ", new RelativeSizeSpan(1.1f));
        ssb1.append("terms and use", new ForegroundColorSpan(getBaseContext().getResources().getColor(R.color.orange)), new UnderlineSpan(), new RelativeSizeSpan(1.1f));
        ssb1.append(" and after placement I will pay the amount equal to 2 months of my salary", new RelativeSizeSpan(1.1f));

        TextView termsConditionTv = (TextView) findViewById(R.id.termsConditionTv);
        termsConditionTv.setText(ssb1.build());
        termsConditionTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EnrollmentTermAndConditionActivity.this, TermsConditionActivity.class));
            }
        });


        inUi();
        settingScheduleBottomSheet();
        indicatorSeekBar = (IndicatorSeekBar) findViewById(R.id.seek_bar_indicators);
        tvSalaryMonth = (TextView) findViewById(R.id.tv_salaryMonthly);
        tvTotalpay = (TextView) findViewById(R.id.tv_totalpay);
        tvMonthEMI = (TextView) findViewById(R.id.tv_monthEMI);
        tvMonthEMI = (TextView) findViewById(R.id.tv_monthEMI);
        indicatorSeekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {

                strGetMonthly = String.valueOf(seekParams.progress);
                int text = seekParams.progress;
                int c = text / 1000;
                int d = text % 1000;
                if (seekParams.progress > 10000 && seekParams.progress < 100000) {
                    tvSalaryMonth.setText(c + "," + d + "/month");
                } else if (seekParams.progress == 10000) {
                    tvSalaryMonth.setText("10,000/month");
                } else if (seekParams.progress == 100000) {
                    tvSalaryMonth.setText("1,00,000/month");
                }
                int getMonthly = Integer.parseInt(strGetMonthly);
                strHalfYearly = String.valueOf(getMonthly * 2);
                int getHalfyearly = Integer.parseInt(strHalfYearly);
                int getEMI = getHalfyearly / 12;
                if (getHalfyearly < 75000) {
                    tvMonthEMI.setText(getEMI + "/month");
                    tvTotalpay.setText(strHalfYearly);

                } else {
                    tvMonthEMI.setText(75000 / 12 + "/month");
                    tvTotalpay.setText("75,000");

                }

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
            }
        });
    }

    private void inUi() {
        enrollTermsConditionPresenter = new EnrollTermsConditionPresenter(EnrollmentTermAndConditionActivity.this, EnrollmentTermAndConditionActivity.this);
        agreeTremAndCondition = (CheckBox) findViewById(R.id.agreeTremAndCondition);

        agreeTremAndCondition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    enrollSubmit.setBackground(getBaseContext().getResources().getDrawable(R.drawable.rounded_orangish_btn_enable));
                } else {
                    enrollSubmit.setBackground(getBaseContext().getResources().getDrawable(R.drawable.rounded_greish_btn_disable));
                }
            }
        });

        ImageView hambarIV = (ImageView) findViewById(R.id.hambarIV);
        hambarIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        enrollSubmit = (CircularProgressButton) findViewById(R.id.enrollSubmit);
        enrollSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (agreeTremAndCondition.isChecked()) {
                    enrollSubmit.startAnimation();
                    enrollTermsConditionPresenter.callEnrollStatusUpdateApi();
                } else {
                    ScrollView scrollView = findViewById(R.id.scrollView);
                    scrollView.smoothScrollTo(0, scrollView.getBottom());
                    showMessage("Please Accept Terms And Conditions");
                }

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setWhiteNavigationBar(@NonNull Dialog dialog) {
        Window window = dialog.getWindow();
        if (window != null) {
            DisplayMetrics metrics = new DisplayMetrics();
            window.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            GradientDrawable dimDrawable = new GradientDrawable();
            // ...customize your dim effect here

            GradientDrawable navigationBarDrawable = new GradientDrawable();
            navigationBarDrawable.setShape(GradientDrawable.RECTANGLE);
            navigationBarDrawable.setColor(Color.WHITE);

            Drawable[] layers = {dimDrawable, navigationBarDrawable};

            LayerDrawable windowBackground = new LayerDrawable(layers);
            windowBackground.setLayerInsetTop(1, metrics.heightPixels);

            window.setBackgroundDrawable(windowBackground);
        }
    }

    private void settingScheduleBottomSheet() {
        View view = getLayoutInflater().inflate(R.layout.schedule_interview_bottom_sheet, null);
        scheduleBottomDialog = new BottomSheetDialog(EnrollmentTermAndConditionActivity.this/*, R.style.DialogStyle*/);
        scheduleBottomDialog.setContentView(view/*, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height-400)*/);
        scheduleBottomDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        scheduleBottomDialog.getBehavior().setHideable(true);
        scheduleBottomDialog.setCancelable(false);
        scheduleBottomDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        interviewTimeSpinner = (Spinner) view.findViewById(R.id.interviewTimeSpinner);
        TextView scheduleNow = (TextView) view.findViewById(R.id.scheduleNow);
        TextView scheduleLater = (TextView) view.findViewById(R.id.scheduleLater);


        scheduleLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EnrollmentTermAndConditionActivity.this, SuccessActivity.class));
            }
        });

        scheduleNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = interviewTimeSpinner.getSelectedItem().toString();
                enrollTermsConditionPresenter.callScheduleInterviewApi(s, jobId);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setWhiteNavigationBar(scheduleBottomDialog);
        }

    }

    private void scheduleInterviewPopup() {
        scheduleBottomDialog.show();
    }


    @Override
    public void setEnrollStatusUpdateResponse(EnrollUserResponse enrollUserResponse) {
        if (enrollUserResponse != null && enrollUserResponse.success) {
            enrollTermsConditionPresenter.callApplyOpportunity(jobId);
            enrollSubmit.revertAnimation();
        } else {
            enrollSubmit.revertAnimation();

        }
    }

    @Override
    public void setInterviewTimeSlotsResponse(InterviewTimeSlotsResponse interviewTimeSlotsResponse) {
        if (interviewTimeSlotsResponse != null && interviewTimeSlotsResponse.success) {
            final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, interviewTimeSlotsResponse.data);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            interviewTimeSpinner.setAdapter(spinnerArrayAdapter);
            scheduleInterviewPopup();
        }
    }

    @Override
    public void setApplyOpportunityResponse(ApplyOpportunityResponse applyOpportunityResponse) {
        if (applyOpportunityResponse != null && applyOpportunityResponse.success) {
            if (askPrefernces) {
                Intent intent = new Intent(EnrollmentTermAndConditionActivity.this, JobApplyLocationSelectActivity.class);
                intent.putStringArrayListExtra("locations", showLocation);
                intent.putExtra("jobId", jobId);
                intent.putExtra("toolbarTitle", toolbarTitle);
                intent.putExtra("salaryDetails", salaryDetails);
                startActivity(intent);
            } else {
                startActivity(new Intent(EnrollmentTermAndConditionActivity.this, SuccessActivity.class).putExtra("jobId", jobId));
            }
        } else {
            showMessage(applyOpportunityResponse.message);
        }

    }

    @Override
    public void setScheduleInterviewResponse(ScheduleInterviewResponse scheduleInterviewResponse) {
        if (scheduleInterviewResponse != null && scheduleInterviewResponse.success) {
            startActivity(new Intent(EnrollmentTermAndConditionActivity.this, SuccessActivity.class));
        }
    }


    @Override
    public void showLoader() {

    }

    @Override
    public void hideLoader() {
        enrollSubmit.revertAnimation();

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        enrollSubmit.revertAnimation();

    }
}