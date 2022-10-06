package com.myfutureapp.dashboard.profile.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.JsonObject;
import com.myfutureapp.R;
import com.myfutureapp.dashboard.model.UserProfileResponse;
import com.myfutureapp.dashboard.profile.model.AddEditWorkExperienceResponse;
import com.myfutureapp.dashboard.profile.model.DeleteWorkExperinceModel;
import com.myfutureapp.dashboard.profile.model.ProfileCityListRsponseModel;
import com.myfutureapp.dashboard.profile.model.ProfileStateListResponseModel;
import com.myfutureapp.dashboard.profile.model.UpdateLocationPreferenceResponse;
import com.myfutureapp.dashboard.profile.presenter.ProfileFragmentPresenter;
import com.myfutureapp.login.DataHolder;
import com.myfutureapp.profile.model.UserProfileUpDataResponse;
import com.myfutureapp.util.AppConstants;
import com.myfutureapp.util.AppPreferences;
import com.myfutureapp.util.DateUtil;
import com.myfutureapp.util.Helper;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class EmploymentDetailsActivity extends AppCompatActivity implements ProfileFragmentView, View.OnClickListener {

    ImageView ivbackEmploymentDetails;
    TextView toolbarTitle;
    ImageView ivDelete;
    RelativeLayout rlfor;
    TextView tvName;
    RelativeLayout rlforCompany;
    TextView tvFullTime;
    TextView tvPartTime;
    TextView tvIntern;
    RelativeLayout rlforSalaryperAnnumCTC;
    TextView tvDateStartMonth;
    ImageView ivStartMonthcalander;
    RelativeLayout rlforStartDate;
    TextView tvStartMonthDate;
    TextView tvDateEndMonth;
    ImageView ivEndMonthcalander;
    RelativeLayout rlforEndDate;
    TextView tvEndMonthDate;
    CircularProgressButton tvSAVE;
    EditText etDesignation;
    EditText etCompany;
    EditText etSalaryperAnnumCTC;
    LinearLayout currentlyWorkingLL;
    LinearLayout jobTypeLL;
    RelativeLayout endMonthRL;
    CheckBox currentWorkingCheckBox;
    String strStartMonth = "", strEndMonth = "", strEndMonthHolder = "", strSalaryperAnnumCTC = "", strDesignation, strCompany, strJobtype = "", strId;
    String designation = "", company = "", role = "", salary = "", start_date = "", end_date = ""/*, job_description = ""*/, id = "";
    long endYearMilliSecs = 0, startYearMilliSecs = 0, currentDateTime = 0;
    int startYear = 0, startMonth = 0;
    private ProfileFragmentPresenter profileFragmentPresenter;
    private boolean edit = false;
    private boolean btnenable = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employment_details);
        bindViews();
        setClickListener();
        profileFragmentPresenter = new ProfileFragmentPresenter(EmploymentDetailsActivity.this, EmploymentDetailsActivity.this);
        if (getIntent() != null) {
            designation = getIntent().getStringExtra("designation");
            company = getIntent().getStringExtra("company");
            role = getIntent().getStringExtra("role");
            salary = getIntent().getStringExtra("salary");
            start_date = getIntent().getStringExtra("start_date");
            end_date = getIntent().getStringExtra("end_date");

//            job_description = getIntent().getStringExtra("job_description");
            id = getIntent().getStringExtra("id");
        }
        handlingTextChangeListener();
        ivDelete.setOnClickListener(v -> deletePopup());
        if (id != null) {
            setTextDetail();
            edit = true;
            strJobtype = role;
            if (end_date != null) {
                try {
                    String[] splitDate = end_date.split("-");
                    String myDate = splitDate[0] + "-" + splitDate[1] + "-" + 01;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = sdf.parse(myDate);
                    endYearMilliSecs = date.getTime();
                    tvDateEndMonth.setText(DateUtil.mnthName(endYearMilliSecs) + " " + DateUtil.yearCalender(endYearMilliSecs));
//                        tvDateEndMonth.setText(selectedYear + "-" + (selectedMonth + 1) + "-" + dayOfMonth);
                    strEndMonth = myDate;
                    strEndMonthHolder = myDate;

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                currentWorkingCheckBox.setChecked(true);
                endMonthRL.setEnabled(false);
                rlforEndDate.setEnabled(false);
                rlforEndDate.setBackground(getBaseContext().getResources().getDrawable(R.drawable.emp_detail_disable_background));
                tvEndMonthDate.setTextColor(getBaseContext().getResources().getColor(R.color.cool_grey1));

            }

            try {
                String[] splitDate = start_date.split("-");
                String myDate = splitDate[0] + "-" + splitDate[1] + "-" + 01;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = sdf.parse(myDate);
                startYearMilliSecs = date.getTime();
                tvDateStartMonth.setText(DateUtil.mnthName(startYearMilliSecs) + " " + DateUtil.yearCalender(startYearMilliSecs));
//                    tvDateStartMonth.setText(selectedYear + "-" + (selectedMonth + 1) + "-" + dayOfMonth);
                strStartMonth = myDate;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ivDelete.setVisibility(View.VISIBLE);
        }
        jobTypeLL.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Helper.hideKeyboard(EmploymentDetailsActivity.this);
                return false;
            }
        });
        rlforStartDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Helper.hideKeyboard(EmploymentDetailsActivity.this);
                return false;
            }
        });
        rlforEndDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Helper.hideKeyboard(EmploymentDetailsActivity.this);
                return false;
            }
        });
        currentlyWorkingLL.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Helper.hideKeyboard(EmploymentDetailsActivity.this);
                return false;
            }
        });
        currentlyWorkingLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentWorkingCheckBox.isChecked()) {
                    currentWorkingCheckBox.setChecked(false);
                    endMonthRL.setEnabled(true);
                    rlforEndDate.setEnabled(true);
                    tvEndMonthDate.setTextColor(getBaseContext().getResources().getColor(R.color.black));
                    rlforEndDate.setBackground(getBaseContext().getResources().getDrawable(R.drawable.emp_detail_background));
                    if (strEndMonthHolder.length() > 0) {
                        strEndMonth = strEndMonthHolder;
                    }
                    checkingButtonIsEnabledOrNot();
//                    endMonthRL.setVisibility(View.VISIBLE);

                } else {
                    currentWorkingCheckBox.setChecked(true);
                    endMonthRL.setEnabled(false);
                    rlforEndDate.setEnabled(false);
                    rlforEndDate.setBackground(getBaseContext().getResources().getDrawable(R.drawable.emp_detail_disable_background));
                    tvEndMonthDate.setTextColor(getBaseContext().getResources().getColor(R.color.cool_grey1));
                    checkingButtonIsEnabledOrNot();
//                    endMonthRL.setVisibility(View.GONE);
                }
            }
        });

    }

    private void bindViews() {
        ivbackEmploymentDetails = findViewById(R.id.iv_backEmploymentDetails);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        ivDelete = findViewById(R.id.iv_delete);
        rlfor = findViewById(R.id.rlfor);
        tvName = findViewById(R.id.tv_name);
        rlforCompany = findViewById(R.id.rlforCompany);
        tvFullTime = findViewById(R.id.tv_Full_Time);
        tvPartTime = findViewById(R.id.tv_Part_Time);
        tvIntern = findViewById(R.id.tv_Intern);
        tvDateStartMonth = findViewById(R.id.tv_date_Start_Month);
        rlforSalaryperAnnumCTC = findViewById(R.id.rlforSalaryper_Annum_CTC);
        tvStartMonthDate = findViewById(R.id.tv_StartMonthDate);
        ivStartMonthcalander = findViewById(R.id.iv_StartMonthcalander);
        rlforStartDate = findViewById(R.id.rlforStartDate);
        tvStartMonthDate = findViewById(R.id.tv_StartMonthDate);
        tvDateEndMonth = findViewById(R.id.tv_date_End_Month);
        ivEndMonthcalander = findViewById(R.id.iv_EndMonthcalander);
        rlforEndDate = findViewById(R.id.rlforEndDate);
        tvEndMonthDate = findViewById(R.id.tv_EndMonthDate);
        tvSAVE = findViewById(R.id.tv_SAVE);
        etDesignation = findViewById(R.id.et_Designation);
        etCompany = findViewById(R.id.et_Company);
        etSalaryperAnnumCTC = findViewById(R.id.et_Salaryper_Annum_CTC);
        currentlyWorkingLL = findViewById(R.id.currentlyWorkingLL);
        jobTypeLL = findViewById(R.id.jobTypeLL);
        endMonthRL = findViewById(R.id.endMonthRL);
        currentWorkingCheckBox = findViewById(R.id.currentWorkingCheckBox);
    }


    private void mthYearDialog(String message, String type) {
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(EmploymentDetailsActivity.this, new MonthPickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(int selectedMonth, int selectedYear) {
                if (type.equalsIgnoreCase("start")) {

                    try {
                        startYear = selectedYear;
                        startMonth = selectedMonth;
                        String myDate = selectedYear + "-" + (selectedMonth + 1) + "-" + 01;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = sdf.parse(myDate);
                        startYearMilliSecs = date.getTime();
                        tvDateStartMonth.setText(DateUtil.mnthName(startYearMilliSecs) + " " + DateUtil.yearCalender(startYearMilliSecs));
                        if (id != null) {
                            settingTextViewColor(tvDateStartMonth);
                        }
//                    tvDateStartMonth.setText(selectedYear + "-" + (selectedMonth + 1) + "-" + dayOfMonth);
                        strStartMonth = myDate;
                        Log.e("dateselected", "start" + selectedMonth + "          s  " + selectedYear);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        String myDate = selectedYear + "-" + (selectedMonth + 1) + "-" + 01;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = sdf.parse(myDate);
                        endYearMilliSecs = date.getTime();
                        tvDateEndMonth.setText(DateUtil.mnthName(endYearMilliSecs) + " " + DateUtil.yearCalender(endYearMilliSecs));
//                        tvDateEndMonth.setText(selectedYear + "-" + (selectedMonth + 1) + "-" + dayOfMonth);
                        if (id != null) {
                            settingTextViewColor(tvDateEndMonth);
                        }
                        strEndMonth = myDate;
                        strEndMonthHolder = myDate;

                        Log.e("dateselected", "end" + selectedMonth + "          s  " + selectedYear);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                checkingButtonIsEnabledOrNot();
            }
        }, Calendar.YEAR, Calendar.MONTH);
        builder.setActivatedMonth(Calendar.JULY)
                .setMinYear(1950)
                .setActivatedYear(currentYear)
                .setMaxYear(currentYear)
                .setMinMonth(Calendar.JANUARY)
                .setTitle(message)
                .setMonthRange(Calendar.JANUARY, Calendar.DECEMBER)

                /*    .setOnMonthChangedListener(new MonthPickerDialog.OnMonthChangedListener() {
                        @Override
                        public void onMonthChanged(int selectedMonth) {
                            if (type.equalsIgnoreCase("start")) {
                                startMnth = selectedMonth;
                            } else {
                                endMnth = selectedMonth;
                            }

                        }
                    }).setOnYearChangedListener(new MonthPickerDialog.OnYearChangedListener() {
                @Override
                public void onYearChanged(int year) {
                    if (type.equalsIgnoreCase("start")) {
                        startYear = year;
                    } else {
                        endYear = year;
                    }
                }
            })*/;
        if (type.equalsIgnoreCase("end")) {
            builder.setMonthAndYearRange(Calendar.JANUARY, Calendar.DECEMBER, startYear, currentYear);
        }
        builder.build().show();
    }

    private void settingTextViewColor(TextView tv) {
        tv.setTextColor(getBaseContext().getResources().getColor(R.color.black_four));
    }

    private void handlingTextChangeListener() {
        etDesignation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (id != null && !designation.equalsIgnoreCase(s.toString().trim())) {

                    settingEditTextColor(etDesignation);
                }
                checkingButtonIsEnabledOrNot();
            }
        });
        etCompany.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (id != null && !company.equalsIgnoreCase(s.toString().trim())) {
                    settingEditTextColor(etCompany);
                }
                checkingButtonIsEnabledOrNot();
            }
        });
        etSalaryperAnnumCTC.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (id != null && !salary.equalsIgnoreCase(s.toString().trim())) {
                    settingEditTextColor(etSalaryperAnnumCTC);
                }
                checkingButtonIsEnabledOrNot();
            }
        });
      /*  etJobDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkingButtonIsEnabledOrNot();
            }
        });*/
    }

    private void settingEditTextColor(EditText et) {
        et.setTextColor(getBaseContext().getResources().getColor(R.color.black_four));
    }

    private void checkingButtonIsEnabledOrNot() {
        if (etDesignation.getText().toString().trim().length() != 0 && etCompany.getText().toString().trim().length() != 0
                && etSalaryperAnnumCTC.getText().toString().trim().length() != 0 /*&& etJobDescription.getText().toString().trim().length() != 0
         */ && strJobtype.trim().length() != 0 && strStartMonth.trim().length() != 0 && (currentWorkingCheckBox.isChecked() || strEndMonth.trim().length() != 0)) {
            tvSAVE.setBackground(getBaseContext().getResources().getDrawable(R.drawable.rounded_orangish_btn_enable));
            btnenable = true;
        } else {
            tvSAVE.setBackground(getBaseContext().getResources().getDrawable(R.drawable.rounded_greish_btn_disable));
            btnenable = false;
        }
    }

    public void setTextDetail() {
        etDesignation.setText(designation);
        etCompany.setText(company);
        etSalaryperAnnumCTC.setText(salary);
//        etJobDescription.setText(job_description);
        tvDateStartMonth.setText(start_date);
        tvDateEndMonth.setText(end_date);
        if (role.equalsIgnoreCase("internship")) {
            tvIntern.callOnClick();
        } else if (role.equalsIgnoreCase("full time")) {
            tvFullTime.callOnClick();
        } else if (role.equalsIgnoreCase("part time")) {
            tvPartTime.callOnClick();
        }

    }

    public void validateDetails() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int mnth = cal.get(Calendar.MONTH);
        String myDate = year + "-" + (mnth + 1) + "-" + 01;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(myDate);

            currentDateTime = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        strDesignation = etDesignation.getText().toString().trim();
        strCompany = etCompany.getText().toString().trim();
        strSalaryperAnnumCTC = etSalaryperAnnumCTC.getText().toString().trim();
//        strJobDescription = etJobDescription.getText().toString().trim();
        if (!Helper.isContainValue(strDesignation)) {
            showMessage("Enter Designation");
            return;
        }
        if (!Helper.isContainValue(strCompany)) {
            showMessage("Enter Company name");
            return;
        }
        if (!Helper.isContainValue(strSalaryperAnnumCTC)) {
            showMessage("Enter Salary");
            return;
        }
      /*  if (!Helper.isContainValue(strJobDescription)) {
            showMessage("Enter job description");
            return;
        }*/
        if (!Helper.isContainValue(strJobtype)) {
            showMessage("Select role");
            return;
        }
        if (!Helper.isContainValue(strStartMonth)) {
            showMessage("Select start date");
            return;
        }
        if (currentDateTime < startYearMilliSecs) {
            showMessage("start month will be less than current month");
            return;
        }

        if (!currentWorkingCheckBox.isChecked()) {
            if (!Helper.isContainValue(strEndMonth)) {
                showMessage("Select end date");
                return;
            }
            if (endYearMilliSecs < startYearMilliSecs) {
                showMessage("end month cannot less than start month");
                return;
            }
            if (currentDateTime < endYearMilliSecs) {
                showMessage("end month will be less than current month");
                return;
            }
        }
        tvSAVE.startAnimation();

        JsonObject jsonObject = new JsonObject();
        if (edit) {
            jsonObject.addProperty("id", id);
        }
        jsonObject.addProperty("designation", strDesignation);
        jsonObject.addProperty("company", strCompany);
        jsonObject.addProperty("role", strJobtype);
        jsonObject.addProperty("salary", strSalaryperAnnumCTC);
        jsonObject.addProperty("start_date", strStartMonth);
        if (currentWorkingCheckBox.isChecked()) {
            jsonObject.addProperty("end_date", "");
            jsonObject.addProperty("currently_working", "1");

        } else {
            jsonObject.addProperty("end_date", strEndMonth);
            jsonObject.addProperty("currently_working", "0");
        }
//        jsonObject.addProperty("job_description", strJobDescription);
        profileFragmentPresenter.callEditWorkExperienceUploadApi(jsonObject);

    }

    private void deletePopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                profileFragmentPresenter.callDeleteWorkExperinceApi(id);
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void setClickListener() {
        tvSAVE.setOnClickListener(this);
        tvFullTime.setOnClickListener(this);
        tvPartTime.setOnClickListener(this);
        tvIntern.setOnClickListener(this);
        ivbackEmploymentDetails.setOnClickListener(this);
        rlforStartDate.setOnClickListener(this);
        rlforEndDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlforStartDate:

                mthYearDialog("Start Month", "start");
              /*  final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog picker = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tvDateStartMonth.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                strStartMonth = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
//                                DateUtil.convertDateIntoUtc(String.valueOf(dayOfMonth),String.valueOf(month+1),String.valueOf(year))
                                checkingButtonIsEnabledOrNot();
                            }
                        }, year, month, day);
                picker.show();*/

                break;
            case R.id.rlforEndDate:

                mthYearDialog("End Month", "end");
               /* final Calendar cldr1 = Calendar.getInstance();
                int day1 = cldr1.get(Calendar.DAY_OF_MONTH);
                int month1 = cldr1.get(Calendar.MONTH);
                int year1 = cldr1.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog picker1 = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tvDateEndMonth.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                strEndMonth = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                //eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                checkingButtonIsEnabledOrNot();
                            }
                        }, year1, month1, day1);
                picker1.show();*/
                break;
            case R.id.tv_SAVE:
                if (btnenable) {
                    Helper.hideKeyboard(EmploymentDetailsActivity.this);
                    validateDetails();
                }
                break;
            case R.id.iv_backEmploymentDetails:
                onBackPressed();
                break;
            case R.id.tv_Full_Time:
                Helper.hideKeyboard(EmploymentDetailsActivity.this);
                strJobtype = "Full Time";
                tvFullTime.setTextColor(getResources().getColor(R.color.white));
                tvPartTime.setTextColor(getResources().getColor(R.color.black));
                tvIntern.setTextColor(getResources().getColor(R.color.black));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    tvFullTime.setBackgroundResource(R.drawable.orange_background);
                    tvPartTime.setBackgroundResource(R.drawable.border_black_white_background);
                    tvIntern.setBackgroundResource(R.drawable.border_black_white_background);
                }
                checkingButtonIsEnabledOrNot();
                break;
            case R.id.tv_Part_Time:
                Helper.hideKeyboard(EmploymentDetailsActivity.this);
                strJobtype = "Part Time";
                tvFullTime.setTextColor(getResources().getColor(R.color.black));
                tvPartTime.setTextColor(getResources().getColor(R.color.white));
                tvIntern.setTextColor(getResources().getColor(R.color.black));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    tvFullTime.setBackgroundResource(R.drawable.border_black_white_background);
                    tvPartTime.setBackgroundResource(R.drawable.orange_background);
                    tvIntern.setBackgroundResource(R.drawable.border_black_white_background);
                }
                checkingButtonIsEnabledOrNot();
                break;
            case R.id.tv_Intern:
                strJobtype = "Internship";
                Helper.hideKeyboard(EmploymentDetailsActivity.this);
                tvFullTime.setTextColor(getResources().getColor(R.color.black));
                tvPartTime.setTextColor(getResources().getColor(R.color.black));
                tvIntern.setTextColor(getResources().getColor(R.color.white));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    tvFullTime.setBackgroundResource(R.drawable.border_black_white_background);
                    tvPartTime.setBackgroundResource(R.drawable.border_black_white_background);
                    tvIntern.setBackgroundResource(R.drawable.orange_background);
                }
                checkingButtonIsEnabledOrNot();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void setProfileDataResponse(UserProfileUpDataResponse userProfileUpDataResponse) {

    }

    @Override
    public void setWorkExperienceResponse(AddEditWorkExperienceResponse addEditWorkExperienceResponse) {
        if (addEditWorkExperienceResponse != null && addEditWorkExperienceResponse.success) {
            showMessage(addEditWorkExperienceResponse.message);
            if (AppPreferences.getInstance(getBaseContext()).getPreferencesString(AppConstants.AUTH).equalsIgnoreCase("true")) {
                profileFragmentPresenter.callUserProfileApi(AppPreferences.getInstance(getBaseContext()).getPreferencesString(AppConstants.STUDENTID));
            }
        }
    }

    @Override
    public void setUpdateLocationPreferenceResponse(UpdateLocationPreferenceResponse addEditWorkExperienceResponse) {

    }

    @Override
    public void setStateListResponse(ProfileStateListResponseModel profileStateListResponseModel) {

    }

    @Override
    public void setCityListResponse(ProfileCityListRsponseModel profileCityListRsponseModel) {

    }

    @Override
    public void setDeleteWorkExperinceResponse(DeleteWorkExperinceModel deleteWorkExperinceModel) {
        if (deleteWorkExperinceModel != null && deleteWorkExperinceModel.success) {
            showMessage(deleteWorkExperinceModel.message);
            if (AppPreferences.getInstance(getBaseContext()).getPreferencesString(AppConstants.AUTH).equalsIgnoreCase("true")) {
                profileFragmentPresenter.callUserProfileApi(AppPreferences.getInstance(getBaseContext()).getPreferencesString(AppConstants.STUDENTID));
            }
        }
    }

    @Override
    public void setUserProfileResponse(UserProfileResponse userProfileResponse) {
        if (userProfileResponse != null && userProfileResponse.success) {
            DataHolder.getInstance().getUserProfileDataresponse = userProfileResponse;
            finish();
            Intent intent = new Intent();
            intent.setAction(getString(R.string.app_name) + "user.workExperince.refresh");
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

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
        Toast.makeText(EmploymentDetailsActivity.this, message, Toast.LENGTH_SHORT).show();

    }
}
