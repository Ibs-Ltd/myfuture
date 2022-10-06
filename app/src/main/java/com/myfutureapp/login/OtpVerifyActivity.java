package com.myfutureapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.myfutureapp.R;
import com.myfutureapp.dashboard.DashboardActivity;
import com.myfutureapp.dashboard.model.UserProfileResponse;
import com.myfutureapp.login.model.CreateUserResponse;
import com.myfutureapp.login.model.OtpVerifyResponse;
import com.myfutureapp.login.presenter.OtpVerifyPresenter;
import com.myfutureapp.login.ui.OtpView;
import com.myfutureapp.profile.ui.ProfileActivity;
import com.myfutureapp.util.AppConstants;
import com.myfutureapp.util.AppPreferences;
import com.myfutureapp.util.Helper;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class OtpVerifyActivity extends AppCompatActivity implements OtpView {

    private CircularProgressButton verifyOtp;
    private TextView otpStatusTV, resendOtp;
    private EditText otpEditText;
    private OtpVerifyPresenter otpVerifyPresenter;
    private String mobileNumber;
    private String loginRequired = "", jobId = "";
    private int time = 30;
    private boolean resendOtpBtn = false;
    private String setActivityResult = "false";
    private String askBasicDetails, askGraduationDetails, askPostGraduationDetails, askXEducationDetails, askXIIEducationDetails, askWorkExperience, askAdditionalInformation;
    private boolean[] askAdditionalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verify);
        if (getIntent() != null) {
            loginRequired = getIntent().getStringExtra("loginRequired");
            setActivityResult = getIntent().getStringExtra("setActivityResult");
            askBasicDetails = getIntent().getStringExtra("askBasicDetails");
            askGraduationDetails = getIntent().getStringExtra("askGraduationDetails");
            askPostGraduationDetails = getIntent().getStringExtra("askPostGraduationDetails");
            askXEducationDetails = getIntent().getStringExtra("askXEducationDetails");
            askXIIEducationDetails = getIntent().getStringExtra("askXIIEducationDetails");
            askWorkExperience = getIntent().getStringExtra("askWorkExperience");
            askAdditionalInformation = getIntent().getStringExtra("askAdditionalInformation");
            askAdditionalList = getIntent().getBooleanArrayExtra("askAdditionalList");


            if (setActivityResult == null) {
                setActivityResult = "false";
            }
            jobId = getIntent().getStringExtra("jobId");
        }
        inUi();

    }

    private void inUi() {
        mobileNumber = getIntent().getStringExtra("mobile");
        otpVerifyPresenter = new OtpVerifyPresenter(this, this);
        otpStatusTV = findViewById(R.id.otpStatus);
        resendOtp = findViewById(R.id.resendOtp);
        TextView mobilenumberTv = findViewById(R.id.mobilenumberTv);
        TextView updateInfoTV = findViewById(R.id.updateInfoTV);
        mobilenumberTv.setText("+91 " + mobileNumber);

        otpEditText = findViewById(R.id.otpEditText);
        verifyOtp = findViewById(R.id.verifyOtp);
        verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateOtp(otpEditText.getText().toString());
            }
        });
        resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resendOtpBtn) {
                    otpVerifyPresenter.requestOtp(mobileNumber);
                }
            }
        });

        mobilenumberTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        updateInfoTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        otpStatusTV.setVisibility(View.VISIBLE);

        settingTimer();
        otpEditText.requestFocus();
    }


    private void settingTimer() {
        resendOtp.setTextColor(getResources().getColor(R.color.cool_grey));

        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                otpStatusTV.setText("resend otp in " + "0:" + checkDigit(time));
                time--;
            }

            public void onFinish() {
                otpStatusTV.setVisibility(View.GONE);
                resendOtpBtn = true;
                resendOtp.setTextColor(getResources().getColor(R.color.black_light));
            }

        }.start();
    }

    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    private void validateOtp(String otp) {
        if (!Helper.isContainValue(otp)) {
            otpStatusTV.setVisibility(View.VISIBLE);
            otpStatusTV.setText(getString(R.string.otp_error));
        } else if (otp.length() != 6) {
            otpStatusTV.setVisibility(View.VISIBLE);
            otpStatusTV.setText(getString(R.string.otp_not_valid));
        } else if (!Helper.isNetworkAvailable(this)) {
            showMessage(getString(R.string.please_check_internet_connection));
        } else {
            verifyOtp.startAnimation();
            otpStatusTV.setVisibility(View.GONE);
            otpVerifyPresenter.callVerifyOtpApi(mobileNumber, otp);
        }
    }

    @Override
    public void setUserProfileResponse(UserProfileResponse userProfileResponse) {
        if (userProfileResponse != null && userProfileResponse.success) {
            DataHolder.getInstance().getUserProfileDataresponse = userProfileResponse;
            boolean agreement;
            if (loginRequired.equalsIgnoreCase("true")) {
                agreement = userProfileResponse.data.agreement;
            } else {
                agreement = true;
            }
            if (DataHolder.getInstance().getUserProfileDataresponse.data != null &&
                    DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null &&
                    DataHolder.getInstance().getUserProfileDataresponse.data.student_data.studentJourneyStatus != null) {
                if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data.studentJourneyStatus.equalsIgnoreCase("finished")) {
                    if (setActivityResult.equalsIgnoreCase("true")) {
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

                        if (!askGraduationDetails.equalsIgnoreCase("3") || !askPostGraduationDetails.equalsIgnoreCase("3") || !askWorkExperience.equalsIgnoreCase("3") || !askXEducationDetails.equalsIgnoreCase("3") || !askXIIEducationDetails.equalsIgnoreCase("3") || !askAdditionalInformation.equalsIgnoreCase("3")) {
                            callingUserProfile("3", agreement);
                        } else {
                            Intent returnIntent = new Intent();
                            setResult(3000, returnIntent);
                            finish();
                        }
                    } else {
                        startActivity(new Intent(OtpVerifyActivity.this, DashboardActivity.class).addFlags(Intent.
                                FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        finishAffinity();
                    }
                } else {
                    if (setActivityResult.equalsIgnoreCase("true")) {
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

                        Intent intent = new Intent(new Intent(OtpVerifyActivity.this, ProfileActivity.class)
                                .putExtra("fragmentToBeLoad", DataHolder.getInstance().getUserProfileDataresponse.data.student_data.studentJourneyStatus)
                                .putExtra("loginRequired", loginRequired)
                                .putExtra("jobId", jobId)
                                .putExtra("askBasicDetails", askBasicDetails)
                                .putExtra("askGraduationDetails", askGraduationDetails)
                                .putExtra("askPostGraduationDetails", askPostGraduationDetails)
                                .putExtra("askXEducationDetails", askXEducationDetails)
                                .putExtra("aggrementSigned", agreement)
                                .putExtra("askXIIEducationDetails", askXIIEducationDetails)
                                .putExtra("askWorkExperience", askWorkExperience)
                                .putExtra("askAdditionalInformation", askAdditionalInformation)
                                .putExtra("askAdditionalList", askAdditionalList)
                                .putExtra("setActivityResult", "true"));

                        startActivityForResult(intent, 3000);
                    } else {
                        startActivity(new Intent(OtpVerifyActivity.this, ProfileActivity.class)
                                .putExtra("fragmentToBeLoad", DataHolder.getInstance().getUserProfileDataresponse.data.student_data.studentJourneyStatus)
                                .putExtra("loginRequired", "")
                                .putExtra("askBasicDetails", askBasicDetails)
                                .putExtra("askGraduationDetails", askGraduationDetails)
                                .putExtra("askPostGraduationDetails", askPostGraduationDetails)
                                .putExtra("aggrementSigned", agreement)
                                .putExtra("askXEducationDetails", askXEducationDetails)
                                .putExtra("askXIIEducationDetails", askXIIEducationDetails)
                                .putExtra("askWorkExperience", askWorkExperience)
                                .putExtra("askAdditionalInformation", askAdditionalInformation)
                                .putExtra("askAdditionalList", askAdditionalList)
                                .putExtra("jobId", jobId));
                        finishAffinity();
                    }
                }

            } else {
                if (setActivityResult.equalsIgnoreCase("true")) {
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

                    Intent intent = new Intent(new Intent(OtpVerifyActivity.this, ProfileActivity.class)
                            .putExtra("fragmentToBeLoad", "")
                            .putExtra("loginRequired", loginRequired)
                            .putExtra("jobId", jobId)
                            .putExtra("askBasicDetails", "1")
                            .putExtra("askGraduationDetails", askGraduationDetails)
                            .putExtra("aggrementSigned", agreement)
                            .putExtra("askPostGraduationDetails", askPostGraduationDetails)
                            .putExtra("askXEducationDetails", askXEducationDetails)
                            .putExtra("askXIIEducationDetails", askXIIEducationDetails)
                            .putExtra("askWorkExperience", askWorkExperience)
                            .putExtra("askAdditionalInformation", askAdditionalInformation)
                            .putExtra("askAdditionalList", askAdditionalList)
                            .putExtra("setActivityResult", "true"));

                    startActivityForResult(intent, 3000);

                } else {
                    startActivity(new Intent(OtpVerifyActivity.this, ProfileActivity.class)
                            .putExtra("fragmentToBeLoad", "")
                            .putExtra("loginRequired", loginRequired)
                            .putExtra("askBasicDetails", askBasicDetails)
                            .putExtra("askGraduationDetails", askGraduationDetails)
                            .putExtra("aggrementSigned", agreement)
                            .putExtra("askPostGraduationDetails", askPostGraduationDetails)
                            .putExtra("askXEducationDetails", askXEducationDetails)
                            .putExtra("askXIIEducationDetails", askXIIEducationDetails)
                            .putExtra("askWorkExperience", askWorkExperience)
                            .putExtra("askAdditionalInformation", askAdditionalInformation)
                            .putExtra("askAdditionalList", askAdditionalList)
                            .putExtra("jobId", jobId));
                    finishAffinity();
                }
                verifyOtp.revertAnimation();
                verifyOtp.setDrawableBackground(getResources().getDrawable(R.drawable.rounded_dark_black_btn));
            }
        }
    }

    private void callingUserProfile(String askBasicDetailss, boolean agreement) {
        Intent intent = new Intent(new Intent(OtpVerifyActivity.this, ProfileActivity.class)
                .putExtra("fragmentToBeLoad", DataHolder.getInstance().getUserProfileDataresponse.data.student_data.studentJourneyStatus)
                .putExtra("loginRequired", loginRequired)
                .putExtra("jobId", jobId)
                .putExtra("askBasicDetails", askBasicDetailss)
                .putExtra("askGraduationDetails", askGraduationDetails)
                .putExtra("askPostGraduationDetails", askPostGraduationDetails)
                .putExtra("askXEducationDetails", askXEducationDetails)
                .putExtra("askAdditionalInformation", askAdditionalInformation)
                .putExtra("askAdditionalList", askAdditionalList)
                .putExtra("askXIIEducationDetails", askXIIEducationDetails)
                .putExtra("aggrementSigned", agreement)
                .putExtra("askWorkExperience", askWorkExperience));

        startActivityForResult(intent, 3000);
    }

    @Override
    public void onResponse(CreateUserResponse createUserResponse) {
        if (createUserResponse.success) {
            otpStatusTV.setVisibility(View.VISIBLE);
            otpStatusTV.setText(createUserResponse.message);
            time = 30;
            resendOtpBtn = false;
            settingTimer();
            Toast.makeText(this, createUserResponse.message, Toast.LENGTH_SHORT).show();
        } else {

        }
    }

    @Override
    public void setOtpVerifyResponse(OtpVerifyResponse otpVerifyResponse) {
        if (otpVerifyResponse.success) {
            AppPreferences.getInstance(this).savePreferencesString(AppConstants.GUID, otpVerifyResponse.data.session_key);
            AppPreferences.getInstance(this).savePreferencesString(AppConstants.STUDENTID, String.valueOf(otpVerifyResponse.data.id));
            AppPreferences.getInstance(this).savePreferencesString(AppConstants.AUTH, "true");
            otpVerifyPresenter.callUserProfileApi(AppPreferences.getInstance(getBaseContext()).getPreferencesString(AppConstants.STUDENTID));

        } else {
            otpStatusTV.setVisibility(View.VISIBLE);
            otpStatusTV.setText(getString(R.string.otp_not_valid));
            verifyOtp.revertAnimation();
            verifyOtp.setBackgroundResource(R.drawable.rounded_dark_black_btn);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3000) {
            Intent returnIntent = new Intent();
            setResult(2001, returnIntent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (loginRequired.equalsIgnoreCase("true")) {
            Intent returnIntent = new Intent();
            setResult(2003, returnIntent);
            finish();
        } else {
            super.onBackPressed();
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
        otpStatusTV.setVisibility(View.VISIBLE);
        otpStatusTV.setText(message);
        verifyOtp.revertAnimation();
        verifyOtp.setBackgroundResource(R.drawable.rounded_dark_black_btn);

    }
}