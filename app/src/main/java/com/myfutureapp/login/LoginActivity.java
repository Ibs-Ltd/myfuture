package com.myfutureapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.myfutureapp.R;
import com.myfutureapp.dashboard.DashboardActivity;
import com.myfutureapp.login.model.CreateUserResponse;
import com.myfutureapp.login.presenter.LoginPresenter;
import com.myfutureapp.login.ui.LoginView;
import com.myfutureapp.termsCondition.TermsConditionActivity;
import com.myfutureapp.util.AppConstants;
import com.myfutureapp.util.AppPreferences;
import com.myfutureapp.util.Helper;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class LoginActivity extends AppCompatActivity implements LoginView {

    private LoginPresenter loginPresenter;
    private TextView tvErrorTV;
    private String mobileNumber;
    private CircularProgressButton contineLogin;
    private String loginRequired = "", jobId = "";
    private boolean btnEnable = false;
    private String setActivityResult = "false";
    //    private boolean askBasicDetails = false, askGraduationDetails = false, askPostGraduationDetails = false, askPrimaryEducationDetails = false, askWorkExperience = false;
    private String askGraduationDetails = "3", askPostGraduationDetails = "3", askXEducationDetails = "3", askXIIEducationDetails = "3", askWorkExperience = "3", askAdditionalInformation = "3";
    private boolean[] askAdditionalList;
    private boolean isCheckeds = false;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 3000) {
            Intent returnIntent = new Intent();
            setResult(2001, returnIntent);
            finish();
        }
        if (resultCode == 2001) {
            Intent returnIntent = new Intent();
            setResult(2001, returnIntent);
            finish();
        } else if (resultCode == 2003) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginRequired = getIntent().getStringExtra("loginRequired");
        setActivityResult = getIntent().getStringExtra("setActivityResult");
//        askBasicDetails = getIntent().getStringExtra("askBasicDetails");
        askGraduationDetails = getIntent().getStringExtra("askGraduationDetails");
        askPostGraduationDetails = getIntent().getStringExtra("askPostGraduationDetails");
        askXEducationDetails = getIntent().getStringExtra("askXEducationDetails");
        askXIIEducationDetails = getIntent().getStringExtra("askXIIEducationDetails");
        askWorkExperience = getIntent().getStringExtra("askWorkExperience");
        askAdditionalInformation = getIntent().getStringExtra("askAdditionalInformation");
        askAdditionalList = getIntent().getBooleanArrayExtra("askAdditionalList");

        if (askGraduationDetails == null) {

            askGraduationDetails = "3";
            askPostGraduationDetails = "3";
            askXEducationDetails = "3";
            askXIIEducationDetails = "3";
            askWorkExperience = "3";
            askAdditionalInformation = "3";
        }
        if (setActivityResult == null) {
            setActivityResult = "false";
        }
        jobId = getIntent().getStringExtra("jobId");
        if (loginRequired == null) {
            loginRequired = "false";
        }
        if (jobId == null) {
            jobId = "0";
        }
        inui();
    }


    private void inui() {
        loginPresenter = new LoginPresenter(this, this);

        CheckBox agreeTremAndCondition = (CheckBox) findViewById(R.id.agreeTremAndCondition);
        EditText numberLogin = findViewById(R.id.numberLogin);
        TextView skipTV = findViewById(R.id.skipTV);
        if (loginRequired.equalsIgnoreCase("true")) {
            skipTV.setVisibility(View.GONE);
        }
        tvErrorTV = findViewById(R.id.tvErrorTV);
        contineLogin = findViewById(R.id.contineLogin);
        contineLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnEnable) {
                    contineLogin.startAnimation();
                    validate(numberLogin.getText().toString().trim());
                }
            }
        });


        agreeTremAndCondition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && numberLogin.getText().toString().trim().length() == 10) {
                    isCheckeds = true;
                    contineLogin.setBackground(getBaseContext().getResources().getDrawable(R.drawable.rounded_dark_black_btn));
                    btnEnable = true;
                } else {
                    isCheckeds = false;
                    contineLogin.setBackground(getBaseContext().getResources().getDrawable(R.drawable.rounded_greish_btn_disable));
                    btnEnable = false;
                }
            }
        });
        numberLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 10) {
                    contineLogin.setBackground(getBaseContext().getResources().getDrawable(R.drawable.rounded_dark_black_btn));
                    btnEnable = true;
                } else {
                    contineLogin.setBackground(getBaseContext().getResources().getDrawable(R.drawable.rounded_greish_btn_disable));
                    btnEnable = false;
                }
            }
        });
        SpannableString text = new SpannableString("By clicking on “Continue” button, you are accepting our Terms of Use and Privacy Policy.");
        text.setSpan(new ForegroundColorSpan(getBaseContext().getResources().getColor(R.color.orange)), 56, 68, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new ForegroundColorSpan(getBaseContext().getResources().getColor(R.color.orange)), 73, 87, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ClickableSpan teremsAndCondition = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(LoginActivity.this, TermsConditionActivity.class));
            }
        };

        ClickableSpan privacy = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(LoginActivity.this, TermsConditionActivity.class).putExtra("url", true));
            }
        };

        text.setSpan(teremsAndCondition, 56, 68, 0);
        text.setSpan(privacy, 73, 87, 0);


        TextView termsConditionTv = (TextView) findViewById(R.id.termsConditionTv);
        termsConditionTv.setMovementMethod(LinkMovementMethod.getInstance());
        termsConditionTv.setText(text, TextView.BufferType.SPANNABLE);
        termsConditionTv.setSelected(true);
        numberLogin.requestFocus();

        skipTV.setOnClickListener(view -> {
            AppPreferences.getInstance(LoginActivity.this).savePreferencesString(AppConstants.GUID, "109f0d90-6acf-42ce-ab4f-777c0452c603");
            AppPreferences.getInstance(LoginActivity.this).savePreferencesString(AppConstants.AUTH, "false");
            startActivity(new Intent(LoginActivity.this, DashboardActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            );
            finish();
        });


    }

    private void validate(String mobile) {
        if (!Helper.isContainValue(mobile)) {
            tvErrorTV.setVisibility(View.VISIBLE);
            tvErrorTV.setText(getString(R.string.mobile_error));
            contineLogin.revertAnimation();
        } else if (mobile.length() != 10) {
            tvErrorTV.setVisibility(View.VISIBLE);
            tvErrorTV.setText(getString(R.string.mobile_not_valid));
            contineLogin.revertAnimation();
        } else if (!Helper.isNetworkAvailable(this)) {
            showMessage(getString(R.string.please_check_internet_connection));
            contineLogin.revertAnimation();
        } else {
            showLoader();
            tvErrorTV.setVisibility(View.GONE);
            mobileNumber = mobile;
            loginPresenter.requestOtp(mobile);
        }
    }

    @Override
    public void onResponse(CreateUserResponse createUserResponse) {
        if (createUserResponse.success) {
            contineLogin.revertAnimation();
            if (setActivityResult.equalsIgnoreCase("true")) {
                Intent intent = new Intent(LoginActivity.this, OtpVerifyActivity.class)
                        .putExtra("mobile", mobileNumber).putExtra("loginRequired", loginRequired)
                        .putExtra("askBasicDetails", "1")
                        .putExtra("askGraduationDetails", askGraduationDetails)
                        .putExtra("askPostGraduationDetails", askPostGraduationDetails)
                        .putExtra("askXEducationDetails", askXEducationDetails)
                        .putExtra("askXIIEducationDetails", askXIIEducationDetails)
                        .putExtra("askWorkExperience", askWorkExperience)
                        .putExtra("askAdditionalInformation", askAdditionalInformation)
                        .putExtra("askAdditionalList", askAdditionalList)
                        .putExtra("jobId", jobId).putExtra("setActivityResult", setActivityResult);
                startActivityForResult(intent, 3000);
            } else {

                startActivity(new Intent(LoginActivity.this, OtpVerifyActivity.class)
                        .putExtra("mobile", mobileNumber).putExtra("loginRequired", loginRequired)
                        .putExtra("jobId", jobId).putExtra("setActivityResult", "false")
                        .putExtra("askBasicDetails", "1")
                        .putExtra("askGraduationDetails", askGraduationDetails)
                        .putExtra("askPostGraduationDetails", askPostGraduationDetails)
                        .putExtra("askXEducationDetails", askXEducationDetails)
                        .putExtra("askXIIEducationDetails", askXIIEducationDetails)
                        .putExtra("askWorkExperience", askWorkExperience)
                        .putExtra("askAdditionalInformation", askAdditionalInformation)
                        .putExtra("askAdditionalList", askAdditionalList)

                );
            }
        } else {
            contineLogin.revertAnimation();
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
        tvErrorTV.setVisibility(View.VISIBLE);
        tvErrorTV.setText(message);
        contineLogin.revertAnimation();
    }
}