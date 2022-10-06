package com.myfutureapp.jobDetail;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.myfutureapp.R;
import com.myfutureapp.dashboard.DashboardActivity;
import com.myfutureapp.dashboard.home.adapter.JobsAdapter;
import com.myfutureapp.dashboard.home.model.BookMarkResponse;
import com.myfutureapp.dashboard.home.model.JobsForYouResponse;
import com.myfutureapp.dashboard.model.UserProfileResponse;
import com.myfutureapp.enrollment.EnrollActivity;
import com.myfutureapp.jobApplyLocationSelection.JobApplyLocationSelectActivity;
import com.myfutureapp.jobDetail.adapter.JobEligibilityDivisionAdapter;
import com.myfutureapp.jobDetail.adapter.JobStatusAdapter;
import com.myfutureapp.jobDetail.model.ApplyOpportunityResponse;
import com.myfutureapp.jobDetail.model.EventApplyResponse;
import com.myfutureapp.jobDetail.model.InterviewTimeSlotsResponse;
import com.myfutureapp.jobDetail.model.JobDetailResponse;
import com.myfutureapp.jobDetail.model.SamplingDataModel;
import com.myfutureapp.jobDetail.model.ScheduleInterviewResponse;
import com.myfutureapp.jobDetail.presenter.JobDetailPresenter;
import com.myfutureapp.jobDetail.ui.JobDetailView;
import com.myfutureapp.login.DataHolder;
import com.myfutureapp.login.LoginActivity;
import com.myfutureapp.profile.ui.ProfileActivity;
import com.myfutureapp.util.AppConstants;
import com.myfutureapp.util.AppPreferences;
import com.myfutureapp.util.DateUtil;
import com.myfutureapp.util.DeBouncedClickListener;
import com.myfutureapp.util.Helper;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class JobDetailActivity extends AppCompatActivity implements JobDetailView {

    private static final String PARAM_JOB_ID = "job_id";
    private static final String PARAM_JOB_TITLE = "job_title";
    private static final String PARAM_SALARY_COMPENSATION = "salary_compensation";

    boolean[] askAdditionalList = new boolean[4];
    private JobDetailPresenter jobDetailPresenter;
    private LinearLayout jobProgressLL, jobEligibilitiesLL;
    private View contentView;
    private TextView jobCityName, detailsTags, jobRole, jobExperince, jobVacancies, jobExpireDate, aboutCompanyTV, salaryDetails, toolbarTitle;
    private RecyclerView jobProgressRV, jobEligibilitiesRV, similarJobRV;
    private CardView videoUrlCV;
    private TextView applyJob;
    private BottomSheetDialog scheduleBottomDialog;
    private String jobId, jobTitle, salaryCompensation;
    private Spinner interviewTimeSpinner;
    private boolean onlyScheduleInterview = false;
    private JobsAdapter jobEligibilityAdapter;
    private boolean ask_preference = false;
    private ArrayList<String> showLocation = new ArrayList<>();
    private TextView similarHeading;
    private HtmlTextView html_text, htmlAboutCompany;
    private boolean loginDoneByUser = false;
    private LinearLayout deadlineLL;
    private ImageView logo;
    private NestedScrollView nestedScrollView;
    private String askGraduationDetails = "3", askPostGraduationDetails = "3", askXEducationDetails = "3", askXIIEducationDetails = "3", askWorkExperience = "3", askAdditionalInformation;

    public static Intent getIntent(Context context, String jobId, String jobTitle, String salaryCompensation) {
        Intent intent = new Intent(context, JobDetailActivity.class);
        intent.putExtra(PARAM_JOB_ID, jobId);
        intent.putExtra(PARAM_JOB_TITLE, jobTitle);
        intent.putExtra(PARAM_SALARY_COMPENSATION, salaryCompensation);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);
        jobId = getIntent().getStringExtra(PARAM_JOB_ID);
        jobTitle = getIntent().getStringExtra(PARAM_JOB_TITLE);
        salaryCompensation = getIntent().getStringExtra(PARAM_SALARY_COMPENSATION);
        if (jobId == null || jobId.isEmpty()) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            finish();
        }
        inUi();
        settingScheduleBottomSheet();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        jobId = intent.getStringExtra(PARAM_JOB_ID);
        jobTitle = intent.getStringExtra(PARAM_JOB_TITLE);
        salaryCompensation = intent.getStringExtra(PARAM_SALARY_COMPENSATION);
        if (jobId == null || jobId.isEmpty()) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            finish();
        }
        toolbarTitle.setText(jobTitle);
        if (salaryCompensation == null || salaryCompensation.isEmpty())
            findViewById(R.id.subtitle).setVisibility(View.GONE);
        else
            findViewById(R.id.subtitle).setVisibility(View.VISIBLE);
        salaryDetails.setText(salaryCompensation);
        contentView.setVisibility(View.GONE);
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        jobDetailPresenter.callOpportunityDetailApi(jobId);

    }

    @Override
    public void onBackPressed() {
        if (loginDoneByUser) {
            startActivity(new Intent(JobDetailActivity.this, DashboardActivity.class).addFlags(Intent.
                    FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        } else {
            super.onBackPressed();
        }
    }

    private void inUi() {
        jobDetailPresenter = new JobDetailPresenter(JobDetailActivity.this, JobDetailActivity.this);

        ImageView hambarIV = (ImageView) findViewById(R.id.hambarIV);
        hambarIV.setOnClickListener(view -> onBackPressed());

        detailsTags = (TextView) findViewById(R.id.detailsTags);

        jobProgressLL = (LinearLayout) findViewById(R.id.jobProgressLL);
        jobEligibilitiesLL = (LinearLayout) findViewById(R.id.jobEligibilitiesLL);
        contentView = findViewById(R.id.contentView);
        similarHeading = (TextView) findViewById(R.id.similarHeading);

        videoUrlCV = (CardView) findViewById(R.id.videoUrlCV);
        logo = (ImageView) findViewById(R.id.logo);

        jobCityName = (TextView) findViewById(R.id.jobCityName);
        jobRole = (TextView) findViewById(R.id.jobRole);
        jobExperince = (TextView) findViewById(R.id.jobExperince);
        jobVacancies = (TextView) findViewById(R.id.jobVacancies);
        jobExpireDate = (TextView) findViewById(R.id.jobExpireDate);
        deadlineLL = (LinearLayout) findViewById(R.id.deadlineLL);
        aboutCompanyTV = (TextView) findViewById(R.id.aboutCompanyTV);
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        salaryDetails = (TextView) findViewById(R.id.salaryDetails);
        toolbarTitle = (TextView) findViewById(R.id.toolbarTitle);
        applyJob = (TextView) findViewById(R.id.applyJob);
        html_text = (HtmlTextView) findViewById(R.id.html_text);
        htmlAboutCompany = (HtmlTextView) findViewById(R.id.htmlAboutCompany);

        jobProgressRV = (RecyclerView) findViewById(R.id.jobProgressRV);
        jobProgressRV.setHasFixedSize(true);
        jobProgressRV.setNestedScrollingEnabled(false);
        LinearLayoutManager searchLinearLayoutManager = new LinearLayoutManager(JobDetailActivity.this, RecyclerView.VERTICAL, false);
        jobProgressRV.setLayoutManager(searchLinearLayoutManager);
        jobProgressRV.setItemAnimator(new DefaultItemAnimator());

        jobEligibilitiesRV = (RecyclerView) findViewById(R.id.jobEligibilitiesRV);
        jobEligibilitiesRV.setHasFixedSize(true);
        LinearLayoutManager jobEligibilityLinearLayoutManager = new LinearLayoutManager(JobDetailActivity.this, RecyclerView.VERTICAL, false);
        jobEligibilitiesRV.setLayoutManager(jobEligibilityLinearLayoutManager);
        jobEligibilitiesRV.setNestedScrollingEnabled(false);
        jobEligibilitiesRV.setItemAnimator(new DefaultItemAnimator());

        similarJobRV = (RecyclerView) findViewById(R.id.similarJobRV);
        similarJobRV.setHasFixedSize(true);
        LinearLayoutManager similarJobLinearLayoutManager = new LinearLayoutManager(JobDetailActivity.this, RecyclerView.VERTICAL, false);
        similarJobRV.setNestedScrollingEnabled(false);
        similarJobRV.setLayoutManager(similarJobLinearLayoutManager);
        similarJobRV.setItemAnimator(new DefaultItemAnimator());

        jobDetailPresenter.callOpportunityDetailApi(jobId);
        toolbarTitle.setText(jobTitle);
        if (salaryCompensation == null || salaryCompensation.isEmpty())
            findViewById(R.id.subtitle).setVisibility(View.GONE);
        else
            findViewById(R.id.subtitle).setVisibility(View.VISIBLE);
        salaryDetails.setText(salaryCompensation);

        html_text.setOnClickATagListener((widget, spannedText, href) -> false);
        htmlAboutCompany.setOnClickATagListener((widget, spannedText, href) -> false);
        applyJob.setOnClickListener(new DeBouncedClickListener(2000) {
            @Override
            public void onDeBounceClick(View view) {
                applyingTheJobCheckingVariousStep();
            }
        });

        String myJson = inputStreamToString(getApplication().getResources().openRawResource(R.raw.sampling));
        SamplingDataModel quizStructureResponse = new Gson().fromJson(myJson, SamplingDataModel.class);

      /*  applyJob.setOnClickListener(view -> applyingTheJobCheckingVariousStep());
        settingScheduleBottomSheet();*/
    }

    private String inputStreamToString(InputStream inputStream) {
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, bytes.length);
            return new String(bytes);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 2001) {
            jobDetailPresenter.callUserProfileApi(AppPreferences.getInstance(getBaseContext()).getPreferencesString(AppConstants.STUDENTID));
            loginDoneByUser = true;
        } else if (resultCode == 5001) {
            jobDetailPresenter.callUserProfileApi(AppPreferences.getInstance(getBaseContext()).getPreferencesString(AppConstants.STUDENTID));
            loginDoneByUser = true;
        } else if (resultCode == 5003) {
            loginDoneByUser = true;
        } else if (resultCode == 3000) {
            loginDoneByUser = true;
        }
    }

    private void applyingTheJobCheckingVariousStep() {
        if (AppPreferences.getInstance(JobDetailActivity.this).getPreferencesString(AppConstants.AUTH).equalsIgnoreCase("false")) {
            Intent intent = new Intent(JobDetailActivity.this, LoginActivity.class)
                    .putExtra("loginRequired", "true")
                    .putExtra("jobId", jobId)
                    .putExtra("askBasicDetails", "1")
                    .putExtra("askGraduationDetails", askGraduationDetails)
                    .putExtra("askPostGraduationDetails", askPostGraduationDetails)
                    .putExtra("askXEducationDetails", askXEducationDetails)
                    .putExtra("askXIIEducationDetails", askXIIEducationDetails)
                    .putExtra("askAdditionalInformation", askAdditionalInformation)
                    .putExtra("askAdditionalList", askAdditionalList)
                    .putExtra("askWorkExperience", askWorkExperience)
                    .putExtra("setActivityResult", "true");
            startActivityForResult(intent, 2001);
            return;
        }


        if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.studentJourneyStatus != null) {
            if (!askGraduationDetails.equalsIgnoreCase("3") && Helper.checkingGraduation()) {
                askGraduationDetails = "3";
            }
            if (!askPostGraduationDetails.equalsIgnoreCase("3") && Helper.checkingPostGraduation()) {
                askPostGraduationDetails = "3";
            }
            if (!askWorkExperience.equalsIgnoreCase("3") && Helper.checkingWorkExperince()) {
                askWorkExperience = "3";
            }
            if (!askXIIEducationDetails.equalsIgnoreCase("3") && Helper.checkingXII()) {
                askXIIEducationDetails = "3";
            }
            if (!askXEducationDetails.equalsIgnoreCase("3") && Helper.checkingX()) {
                askXEducationDetails = "3";
            }
            if (!askAdditionalInformation.equalsIgnoreCase("3") && Helper.checkingAdditionalInformation()) {
                askAdditionalInformation = "3";
            }

            if (!DataHolder.getInstance().getUserProfileDataresponse.data.student_data.studentJourneyStatus.equalsIgnoreCase("finished")) {
                jobDetailPresenter.callEventApplyOpportunity(jobId);
                return;
            } else if (!askGraduationDetails.equalsIgnoreCase("3") || !askPostGraduationDetails.equalsIgnoreCase("3") || !askWorkExperience.equalsIgnoreCase("3") || !askXEducationDetails.equalsIgnoreCase("3") || !askXIIEducationDetails.equalsIgnoreCase("3")) {
                jobDetailPresenter.callEventApplyOpportunity(jobId);

            } else if (!DataHolder.getInstance().getUserProfileDataresponse.data.agreement) {
                jobDetailPresenter.callEventApplyOpportunity(jobId);
                return;
            } else {
                if (DataHolder.getInstance().getUserProfileDataresponse.data.applied_opportunity_details.applied_count != 0) {
                    jobDetailPresenter.callApplyOpportunity(jobId);
                } else {
                    showMessage("Quota is full today, Apply tomorrow");
                }
            }

        } else {
            jobDetailPresenter.callEventApplyOpportunity(jobId);
            return;
        }

    }

    private void callingUserProfile() {
    }

    @Override
    protected void onDestroy() {
        if (jobDetailPresenter != null) {
            jobDetailPresenter.onDestroyedView();
        }
        super.onDestroy();
    }

    @Override
    public void setOpportunityDetailResponse(JobDetailResponse jobDetailResponse) {
        if (jobDetailResponse != null && jobDetailResponse.success) {
            String compensation;
            if (salaryCompensation == null || salaryCompensation.isEmpty())
                compensation = jobDetailResponse.data.basic.compensation_show;
            else
                compensation = salaryCompensation;

            salaryDetails.setText(compensation);
            findViewById(R.id.subtitle).setVisibility(View.VISIBLE);
            if (Helper.isContainValue(jobDetailResponse.data.basic.application_deadline)) {
                jobExpireDate.setText(DateUtil.convertyyyymmddhhmmsstoddmmyyyy(jobDetailResponse.data.basic.application_deadline));
                deadlineLL.setVisibility(View.VISIBLE);
            } else {
                deadlineLL.setVisibility(View.GONE);
            }
            if (Helper.isContainValue(String.valueOf(jobDetailResponse.data.basic.vacancies))) {
                if (jobDetailResponse.data.basic.vacancies != 0) {
                    jobVacancies.setText("Vacancies: " + jobDetailResponse.data.basic.vacancies);
                }
            }
            if (jobDetailResponse.data.company_data.logo != null) {
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_placeholder);
                Glide.with(getBaseContext()).load(jobDetailResponse.data.company_data.logo).apply(options).into(logo);
            }

            if (Helper.isContainValue(jobDetailResponse.data.basic.role_show)) {
                jobRole.setText(jobDetailResponse.data.basic.role_show);
            }
            if (jobDetailResponse.data != null && jobDetailResponse.data.basic.showLocation != null && jobDetailResponse.data.basic.showLocation.size() > 0) {
                String city = "";
                for (int i = 0; i < jobDetailResponse.data.basic.showLocation.size(); i++) {
                    if (i == jobDetailResponse.data.basic.showLocation.size() - 1) {
                        city = city + jobDetailResponse.data.basic.showLocation.get(i);
                    } else {
                        city = city + jobDetailResponse.data.basic.showLocation.get(i) + ",";
                    }
                }
                jobCityName.setText(city);
            }
            if (jobDetailResponse.data.basic.ask_ug_details != null) {
                askGraduationDetails = jobDetailResponse.data.basic.ask_ug_details;
            }
            if (jobDetailResponse.data.basic.ask_pg_details != null) {
                askPostGraduationDetails = jobDetailResponse.data.basic.ask_pg_details;
            }
            if (jobDetailResponse.data.basic.ask_x_score != null) {
                askXEducationDetails = jobDetailResponse.data.basic.ask_x_score;
            }
            if (jobDetailResponse.data.basic.ask_xii_score != null) {
                askXIIEducationDetails = jobDetailResponse.data.basic.ask_xii_score;
            }
            if (jobDetailResponse.data.basic.ask_work_exp != null) {
                askWorkExperience = jobDetailResponse.data.basic.ask_work_exp;
            }
            String ask_laptop = "3", ask_driving_license = "3", ask_wifi = "3", ask_2_wheeler = "3";
            if (jobDetailResponse.data.basic.ask_laptop != null) {
                ask_laptop = jobDetailResponse.data.basic.ask_laptop;
            }
            if (jobDetailResponse.data.basic.ask_driving_license != null) {
                ask_driving_license = jobDetailResponse.data.basic.ask_driving_license;
            }
            if (jobDetailResponse.data.basic.ask_wifi != null) {
                ask_wifi = jobDetailResponse.data.basic.ask_wifi;
            }
            if (jobDetailResponse.data.basic.ask_2_wheeler != null) {
                ask_2_wheeler = jobDetailResponse.data.basic.ask_2_wheeler;
            }
            if (ask_laptop.equalsIgnoreCase("3") && ask_wifi.equalsIgnoreCase("3")
                    && ask_2_wheeler.equalsIgnoreCase("3") && ask_driving_license.equalsIgnoreCase("3")) {
                askAdditionalInformation = "3";
            } else {
                askAdditionalInformation = "1";
                if (DataHolder.getInstance().getUserProfileDataresponse != null && DataHolder.getInstance().getUserProfileDataresponse.data != null
                        && DataHolder.getInstance().getUserProfileDataresponse.data.personalPossessionsDict != null) {
                    UserProfileResponse.PersonalPossessionsDict personalPossessionsDict = DataHolder.getInstance().getUserProfileDataresponse.data.personalPossessionsDict;

                    askAdditionalList[0] = personalPossessionsDict.laptop == null && !ask_laptop.equalsIgnoreCase("3");
                    askAdditionalList[1] = personalPossessionsDict.broadband == null && !ask_wifi.equalsIgnoreCase("3");
                    askAdditionalList[2] = personalPossessionsDict.driving_license == null && !ask_driving_license.equalsIgnoreCase("3");
                    askAdditionalList[3] = personalPossessionsDict.two_wheeler == null && !ask_2_wheeler.equalsIgnoreCase("3");

                } else {
                    for (int i = 0; i < askAdditionalList.length; i++) {
                        askAdditionalList[i] = true;
                    }

                }
            }
            ask_preference = jobDetailResponse.data.basic.ask_preference;
            if (jobDetailResponse.data.basic.showLocation != null && jobDetailResponse.data.basic.showLocation.size() != 0) {
                showLocation = jobDetailResponse.data.basic.showLocation;
                Log.e("showLocations", String.valueOf(showLocation));

            }


            if (jobDetailResponse.data.eligibility_creteria != null && jobDetailResponse.data.eligibility_creteria.size() > 0) {
                for (int i = 0; i < jobDetailResponse.data.eligibility_creteria.size(); i++) {
                    if (jobDetailResponse.data.eligibility_creteria.get(i) != null && jobDetailResponse.data.eligibility_creteria.get(i).data != null) {
                        for (int j = 0; j < jobDetailResponse.data.eligibility_creteria.get(i).data.size(); j++) {
                            if (jobDetailResponse.data.eligibility_creteria.get(i).data.get(j).key.equalsIgnoreCase("Work Experience")) {
                                jobExperince.setText(jobDetailResponse.data.eligibility_creteria.get(i).data.get(j).value);
                                jobDetailResponse.data.eligibility_creteria.get(i);
                                break;
                            }

                        }
                    }

                }
                if (jobDetailResponse.data.eligibility_creteria.size() != 0) {
                    JobEligibilityDivisionAdapter jobEligibilityAdapter = new JobEligibilityDivisionAdapter(jobDetailResponse.data.eligibility_creteria);
                    jobEligibilitiesRV.setAdapter(jobEligibilityAdapter);
                    jobEligibilitiesLL.setVisibility(View.VISIBLE);

//                    JobEligibilityAdapter jobEligibilityAdapter = new JobEligibilityAdapter(JobDetailActivity.this, jobDetailResponse.data.eligibility_creteria);
//                    jobEligibilitiesRV.setAdapter(jobEligibilityAdapter);
//                    jobEligibilitiesLL.setVisibility(View.VISIBLE);
                } else {
                    jobEligibilitiesLL.setVisibility(View.GONE);
                }
            } else {
                jobEligibilitiesLL.setVisibility(View.GONE);

            }
            if (jobDetailResponse.data.progress_obj != null && jobDetailResponse.data.progress_obj.size() > 0) {
                JobStatusAdapter jobStatusAdapter = new JobStatusAdapter(JobDetailActivity.this, jobDetailResponse.data.progress_obj, new JobStatusAdapter.ScheduleInterview() {
                    @Override
                    public void scheduleInterview() {
                        onlyScheduleInterview = true;
                        jobDetailPresenter.callGetTimeSlotslApi(jobId);
                    }
                });
                jobProgressRV.setAdapter(jobStatusAdapter);
                jobProgressLL.setVisibility(View.VISIBLE);
                applyJob.setVisibility(View.GONE);
            } else {
                jobProgressLL.setVisibility(View.GONE);
                applyJob.setVisibility(View.VISIBLE);
            }


            if (jobDetailResponse.data.similar_roles != null && jobDetailResponse.data.similar_roles.size() > 0) {
                jobEligibilityAdapter = new JobsAdapter(jobDetailResponse.data.similar_roles, new JobsAdapter.JobSelectedForDetail() {
                    @Override
                    public void JobDetailSelected(JobsForYouResponse.JobDetail selectedJob) {
                        String title = String.format("%s - %s", selectedJob.company_name, selectedJob.designation);
                        Intent intent = JobDetailActivity.getIntent(JobDetailActivity.this, selectedJob.id + "", title, selectedJob.compensation_show);
                        startActivity(intent);
                        nestedScrollView.fullScroll(View.FOCUS_UP);
                    }

                    @Override
                    public void JobBookMarkTask(String bookMarkType, JobsForYouResponse.JobDetail jobDetail, int position) {
                        jobDetailPresenter.callBookMarkApi(bookMarkType, jobDetail, position);
                    }
                });
                similarJobRV.setAdapter(jobEligibilityAdapter);
                similarJobRV.setVisibility(View.VISIBLE);
                similarHeading.setVisibility(View.VISIBLE);
            } else {
                similarJobRV.setVisibility(View.GONE);
                similarHeading.setVisibility(View.GONE);

            }

            if (Helper.isContainValue(jobDetailResponse.data.basic.instructions)) {
//                html_text.setRemoveTrailingWhiteSpace(true);
                html_text.setListIndentPx(10);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    detailsTags.setText(Html.fromHtml(jobDetailResponse.data.basic.instructions, Html.FROM_HTML_MODE_COMPACT));
                    html_text.setHtml(jobDetailResponse.data.basic.instructions);
                } else {
                    detailsTags.setText(Html.fromHtml(jobDetailResponse.data.basic.instructions));
                    html_text.setHtml(jobDetailResponse.data.basic.instructions);
                }

                html_text.setVisibility(View.VISIBLE);
//                detailsTags.setVisibility(View.VISIBLE);
            } else {
                html_text.setVisibility(View.GONE);
                detailsTags.setVisibility(View.GONE);
            }
            if (Helper.isContainValue(jobDetailResponse.data.company_data.description)) {
                htmlAboutCompany.setRemoveTrailingWhiteSpace(true);
                htmlAboutCompany.setListIndentPx(1);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    aboutCompanyTV.setText(Html.fromHtml(jobDetailResponse.data.company_data.description, Html.FROM_HTML_MODE_COMPACT));
                    htmlAboutCompany.setHtml(jobDetailResponse.data.company_data.description);
                } else {
                    aboutCompanyTV.setText(Html.fromHtml(jobDetailResponse.data.company_data.description));
                    htmlAboutCompany.setHtml(jobDetailResponse.data.company_data.description);
                }

                htmlAboutCompany.setVisibility(View.VISIBLE);
//                aboutCompanyTV.setVisibility(View.VISIBLE);
            } else {
                htmlAboutCompany.setVisibility(View.GONE);
//                aboutCompanyTV.setVisibility(View.GONE);
            }
            contentView.setVisibility(View.VISIBLE);
            findViewById(R.id.progressBar).setVisibility(View.GONE);
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
            if (ask_preference) {
                Intent intent = new Intent(JobDetailActivity.this, JobApplyLocationSelectActivity.class);
                intent.putExtra("jobId", jobId);
                intent.putStringArrayListExtra("locations", showLocation);
                intent.putExtra("toolbarTitle", toolbarTitle.getText().toString());
                intent.putExtra("salaryDetails", salaryDetails.getText().toString());
                startActivity(intent);
            } else {
                startActivity(new Intent(JobDetailActivity.this, SuccessActivity.class).putExtra("jobId", jobId));
            }
        }
    }

    @Override
    public void setScheduleInterviewResponse(ScheduleInterviewResponse scheduleInterviewResponse) {
        if (scheduleInterviewResponse != null && scheduleInterviewResponse.success) {
            if (onlyScheduleInterview) {
                scheduleBottomDialog.dismiss();
                showMessage(scheduleInterviewResponse.message);
                jobDetailPresenter.callOpportunityDetailApi(jobId);
                onlyScheduleInterview = false;
                Intent intent = new Intent();
                intent.setAction(getString(R.string.app_name) + "user.ongoing.refresh");
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

            } else {
                startActivity(new Intent(JobDetailActivity.this, SuccessActivity.class));
            }
        }
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
        scheduleBottomDialog = new BottomSheetDialog(JobDetailActivity.this/*, R.style.DialogStyle*/);
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
                if (onlyScheduleInterview) {
                    scheduleBottomDialog.dismiss();
                    onlyScheduleInterview = false;
                } else {
                    startActivity(new Intent(JobDetailActivity.this, SuccessActivity.class));
                }
            }
        });

        scheduleNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String s = interviewTimeSpinner.getSelectedItem().toString();
                jobDetailPresenter.callScheduleInterviewApi(s, jobId);
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
    public void setBookMarkResponse(BookMarkResponse bookMarkResponse, String bookMarkType, int position) {
        if (bookMarkResponse != null && bookMarkResponse.success) {
            jobEligibilityAdapter.notifyItemPosition(position, Integer.parseInt(bookMarkType));
            Intent intent = new Intent();
            intent.setAction(getString(R.string.app_name) + "user.bookmark.refresh");
            LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);
            showMessage(bookMarkResponse.message);

        }
    }

    @Override
    public void setUserProfileResponse(UserProfileResponse userProfileResponse) {
        if (userProfileResponse != null && userProfileResponse.success) {
            DataHolder.getInstance().getUserProfileDataresponse = userProfileResponse;
            jobDetailPresenter.callOpportunityDetailApi(jobId);
        }
    }

    @Override
    public void setEventOnOpportunityResponse(EventApplyResponse eventApplyResponse) {
        if (eventApplyResponse != null && eventApplyResponse.success) {
            if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.studentJourneyStatus != null) {
                if (!DataHolder.getInstance().getUserProfileDataresponse.data.student_data.studentJourneyStatus.equalsIgnoreCase("finished")) {

                    startActivityForResult(new Intent(JobDetailActivity.this, ProfileActivity.class)
                                    .putExtra("fragmentToBeLoad", DataHolder.getInstance().getUserProfileDataresponse.data.student_data.studentJourneyStatus)
                                    .putExtra("loginRequired", "true")
                                    .putExtra("jobId", jobId)
                                    .putExtra("askBasicDetails", "1")
                                    .putExtra("askGraduationDetails", askGraduationDetails)
                                    .putExtra("askPostGraduationDetails", askPostGraduationDetails)
                                    .putExtra("aggrementSigned", DataHolder.getInstance().getUserProfileDataresponse.data.agreement)
                                    .putExtra("askXEducationDetails", askXEducationDetails)
                                    .putExtra("askXIIEducationDetails", askXIIEducationDetails)
                                    .putExtra("askWorkExperience", askWorkExperience)
                                    .putExtra("askAdditionalInformation", askAdditionalInformation)
                                    .putExtra("ask_preference", ask_preference)
                                    .putStringArrayListExtra("locations", showLocation)
                                    .putExtra("toolbarTitle", toolbarTitle.getText().toString())
                                    .putExtra("salaryDetails", salaryDetails.getText().toString())
                                    .putExtra("askAdditionalList", askAdditionalList)
                                    .putExtra("setActivityResult", "profile")
                            , 5001);
                } else if (!askGraduationDetails.equalsIgnoreCase("3") || !askPostGraduationDetails.equalsIgnoreCase("3") || !askWorkExperience.equalsIgnoreCase("3") || !askXEducationDetails.equalsIgnoreCase("3") || !askXIIEducationDetails.equalsIgnoreCase("3") || !askAdditionalInformation.equalsIgnoreCase("3")) {
                    startActivityForResult(new Intent(JobDetailActivity.this, ProfileActivity.class)
                                    .putExtra("fragmentToBeLoad", DataHolder.getInstance().getUserProfileDataresponse.data.student_data.studentJourneyStatus)
                                    .putExtra("loginRequired", "true")
                                    .putExtra("jobId", jobId)
                                    .putExtra("askBasicDetails", "3")
                                    .putExtra("aggrementSigned", DataHolder.getInstance().getUserProfileDataresponse.data.agreement)
                                    .putExtra("askGraduationDetails", askGraduationDetails)
                                    .putExtra("askPostGraduationDetails", askPostGraduationDetails)
                                    .putExtra("askXEducationDetails", askXEducationDetails)
                                    .putExtra("ask_preference", ask_preference)
                                    .putStringArrayListExtra("locations", showLocation)
                                    .putExtra("toolbarTitle", toolbarTitle.getText().toString())
                                    .putExtra("salaryDetails", salaryDetails.getText().toString())
                                    .putExtra("askXIIEducationDetails", askXIIEducationDetails)
                                    .putExtra("askWorkExperience", askWorkExperience)
                                    .putExtra("askAdditionalInformation", askAdditionalInformation)
                                    .putExtra("askAdditionalList", askAdditionalList)
                                    .putExtra("setActivityResult", "profile")
                            , 5001);

                } else if (!DataHolder.getInstance().getUserProfileDataresponse.data.agreement) {
                    startActivity(new Intent(JobDetailActivity.this, EnrollActivity.class).putExtra("jobId", jobId).putExtra("loginRequired", "false"));
                }
            } else {
                startActivityForResult(new Intent(JobDetailActivity.this, ProfileActivity.class).putExtra("fragmentToBeLoad", "")
                                .putExtra("loginRequired", "true")
                                .putExtra("jobId", jobId)
                                .putExtra("askBasicDetails", "1")
                                .putExtra("askGraduationDetails", askGraduationDetails)
                                .putExtra("askPostGraduationDetails", askPostGraduationDetails)
                                .putExtra("askXEducationDetails", askXEducationDetails)
                                .putExtra("aggrementSigned", DataHolder.getInstance().getUserProfileDataresponse.data.agreement)
                                .putExtra("ask_preference", ask_preference)
                                .putStringArrayListExtra("locations", showLocation)
                                .putExtra("toolbarTitle", toolbarTitle.getText().toString())
                                .putExtra("salaryDetails", salaryDetails.getText().toString())
                                .putExtra("askXIIEducationDetails", askXIIEducationDetails)
                                .putExtra("askWorkExperience", askWorkExperience)
                                .putExtra("askAdditionalInformation", askAdditionalInformation)
                                .putExtra("askAdditionalList", askAdditionalList)
                                .putExtra("setActivityResult", "profile")
                        , 5001);
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
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}