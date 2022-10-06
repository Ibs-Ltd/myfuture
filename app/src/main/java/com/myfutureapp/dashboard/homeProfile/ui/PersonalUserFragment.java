package com.myfutureapp.dashboard.homeProfile.ui;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.JsonObject;
import com.myfutureapp.R;
import com.myfutureapp.dashboard.homeProfile.presenter.PersonalUserPresenter;
import com.myfutureapp.dashboard.model.UserProfileResponse;
import com.myfutureapp.login.DataHolder;
import com.myfutureapp.profile.model.UserProfileUpDataResponse;
import com.myfutureapp.util.AppConstants;
import com.myfutureapp.util.AppPreferences;
import com.myfutureapp.util.Helper;

public class PersonalUserFragment extends Fragment implements PersonalUserView {

    View viewParentNameBelow, viewParentRelationBelow, viewParentContactBelow, viewParentAddressBelow;
    ImageView personalDetailEditIV;
    private PersonalUserPresenter personalUserPresenter;
    private EditText et_parentsName, etparentContactNumber, etpermanentAddress;
    private TextView editPersonalDetail, tv_father, tv_mother;
    private boolean personalEditable = true;
    private String perviousParentName = "", perviousParentRelation = "", perviousParentContactNumber = "", perviousPermanentAddress = "";
    private String strParentRelation = "";
    private TextView cancelPersonalDetail, savePersonalDetail;
    private ProgressDialog progressDialog;
    private LinearLayout savingCancelPersonalDetails;
    private LinearLayout parentRelationLL;

    private boolean isLoaded = false;

    public PersonalUserFragment() {
    }

    public static PersonalUserFragment newInstance(String param1, String param2) {
        PersonalUserFragment fragment = new PersonalUserFragment();
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            Log.e("isVisibleToUser", "true");
            if (!isLoaded) {
                settingUserPersonalDetails();
                cancelPersonalDetailEditable();
                isLoaded = true;
            }
        } else {
            Log.e("isVisibleToUser", "false");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_user, container, false);
        inUi(view);
        return view;

    }

    private void inUi(View view) {
        personalUserPresenter = new PersonalUserPresenter(getContext(), PersonalUserFragment.this);
        personalDetailEditIV = (ImageView) view.findViewById(R.id.personalDetailEditIV);

        et_parentsName = (EditText) view.findViewById(R.id.et_parentsName);
        etparentContactNumber = (EditText) view.findViewById(R.id.etparentContactNumber);
        etpermanentAddress = (EditText) view.findViewById(R.id.etpermanentAddress);

        editPersonalDetail = (TextView) view.findViewById(R.id.editPersonalDetail);
        tv_father = (TextView) view.findViewById(R.id.tv_father);
        tv_mother = (TextView) view.findViewById(R.id.tv_mother);
        cancelPersonalDetail = (TextView) view.findViewById(R.id.cancelPersonalDetail);
        savePersonalDetail = (TextView) view.findViewById(R.id.savePersonalDetail);

        viewParentNameBelow = (View) view.findViewById(R.id.viewParentNameBelow);
        viewParentRelationBelow = (View) view.findViewById(R.id.viewParentRelationBelow);
        viewParentContactBelow = (View) view.findViewById(R.id.viewParentContactBelow);
        viewParentAddressBelow = (View) view.findViewById(R.id.viewParentAddressBelow);

        parentRelationLL = (LinearLayout) view.findViewById(R.id.parentRelationLL);

        savingCancelPersonalDetails = (LinearLayout) view.findViewById(R.id.savingCancelPersonalDetails);
        parentRelationLL.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Helper.hideKeyboard(getActivity());
                return false;
            }
        });
        tv_father.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strParentRelation = "Father";
                tv_mother.setTextColor(getResources().getColor(R.color.black));
                tv_father.setTextColor(getResources().getColor(R.color.white));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    tv_father.setBackgroundResource(R.drawable.black_background_normal);
                    tv_mother.setBackgroundResource(R.drawable.border_black_white_background);
                }
                Helper.hideKeyboard(getActivity());

            }
        });
        tv_mother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strParentRelation = "Mother";
                tv_father.setTextColor(getResources().getColor(R.color.black));
                tv_mother.setTextColor(getResources().getColor(R.color.white));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    tv_father.setBackgroundResource(R.drawable.border_black_white_background);
                    tv_mother.setBackgroundResource(R.drawable.black_background_normal);
                }
                Helper.hideKeyboard(getActivity());

            }
        });

        personalDetailEditIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.hideKeyboard(getActivity());
                if (personalEditable) {
                    makePersonalDetailEditable();
                    personalDetailEditIV.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_tick_profile));
                    personalEditable = false;
                } else {
                    checkingIsAnyThingToUpdate();
                }
            }
        });
        cancelPersonalDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.hideKeyboard(getActivity());
                settingUserPersonalDetails();
                cancelPersonalDetailEditable();
                personalDetailEditIV.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_edit));
                personalEditable = true;
            }
        });
        savePersonalDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.hideKeyboard(getActivity());
                checkingIsAnyThingToUpdate();
            }
        });
        editPersonalDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePersonalDetailEditable();
                personalDetailEditIV.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_tick_profile));
                personalEditable = false;
            }
        });

    }

    private void checkingIsAnyThingToUpdate() {
        if (et_parentsName.getText().toString().trim().equalsIgnoreCase(perviousParentName) && strParentRelation.trim().equalsIgnoreCase(perviousParentRelation)
                && etparentContactNumber.getText().toString().trim().equalsIgnoreCase(perviousParentContactNumber) && etpermanentAddress.getText().toString().trim().equalsIgnoreCase(perviousPermanentAddress)) {
            showMessage("Nothing To Update");
            cancelPersonalDetailEditable();
            personalEditable = true;
            personalDetailEditIV.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_edit));
            return;
        }
        if (etparentContactNumber.getText().length() != 0 && etparentContactNumber.getText().length() < 10) {
            showMessage("Please enter correct number");
            return;
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("parent_name", et_parentsName.getText().toString().trim());
        jsonObject.addProperty("parent_relation", strParentRelation);
        jsonObject.addProperty("parent_no", etparentContactNumber.getText().toString().trim());
        jsonObject.addProperty("parent_address", etpermanentAddress.getText().toString().trim());
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("saving your details");
        progressDialog.setCancelable(false);
        progressDialog.show();
        personalUserPresenter.callUserProfileUploadApi(jsonObject);

    }

    private void makePersonalDetailEditable() {
        et_parentsName.setBackground(getContext().getResources().getDrawable(R.drawable.simple_white_background));
        etparentContactNumber.setBackground(getContext().getResources().getDrawable(R.drawable.simple_white_background));
        etpermanentAddress.setBackground(getContext().getResources().getDrawable(R.drawable.simple_white_background));
        et_parentsName.setEnabled(true);
        tv_father.setEnabled(true);
        tv_mother.setEnabled(true);
        etparentContactNumber.setEnabled(true);
        etpermanentAddress.setEnabled(true);
        savingCancelPersonalDetails.setVisibility(View.VISIBLE);
        editPersonalDetail.setVisibility(View.GONE);
        viewParentNameBelow.setVisibility(View.GONE);
        viewParentContactBelow.setVisibility(View.GONE);
        viewParentRelationBelow.setVisibility(View.GONE);
        viewParentAddressBelow.setVisibility(View.GONE);
    }

    private void cancelPersonalDetailEditable() {
        et_parentsName.setBackground(getContext().getResources().getDrawable(R.drawable.simple_cool_grey_background));
        etparentContactNumber.setBackground(getContext().getResources().getDrawable(R.drawable.simple_cool_grey_background));
        etpermanentAddress.setBackground(getContext().getResources().getDrawable(R.drawable.simple_cool_grey_background));
        et_parentsName.setEnabled(false);
        tv_father.setEnabled(false);
        tv_mother.setEnabled(false);
        etparentContactNumber.setEnabled(false);
        etpermanentAddress.setEnabled(false);
        savingCancelPersonalDetails.setVisibility(View.GONE);
        editPersonalDetail.setVisibility(View.VISIBLE);
        personalEditable = true;
        viewParentNameBelow.setVisibility(View.VISIBLE);
        viewParentContactBelow.setVisibility(View.VISIBLE);
        viewParentRelationBelow.setVisibility(View.VISIBLE);
        viewParentAddressBelow.setVisibility(View.VISIBLE);
    }

    private void settingUserPersonalDetails() {
        if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.parentName != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.parentName.length() != 0) {
            et_parentsName.setText(String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.parentName));
            perviousParentName = DataHolder.getInstance().getUserProfileDataresponse.data.student_data.parentName;
        } else {
            et_parentsName.setText("");
            et_parentsName.setHint("None");
        }
        if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.parentRelation != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.parentRelation.length() != 0) {
            if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data.parentRelation.equalsIgnoreCase("father")) {
                tv_father.callOnClick();
                perviousParentRelation = "Father";
            } else if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data.parentRelation.equalsIgnoreCase("mother")) {
                tv_mother.callOnClick();
                perviousParentRelation = "Mother";
            }
        }
        if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.parentNo != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.parentNo.length() != 0) {
            etparentContactNumber.setText(String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.parentNo));
            perviousParentContactNumber = DataHolder.getInstance().getUserProfileDataresponse.data.student_data.parentNo;
        } else {
            etparentContactNumber.setText("");
            etparentContactNumber.setHint("None");
        }
        if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.parentAddress != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.parentAddress.length() != 0) {
            etpermanentAddress.setText(String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.parentAddress));
            perviousPermanentAddress = DataHolder.getInstance().getUserProfileDataresponse.data.student_data.parentAddress;
        } else {
            etpermanentAddress.setText("");
            etpermanentAddress.setHint("None");
        }
    }

    @Override
    public void setProfileDataResponse(UserProfileUpDataResponse userProfileUpDataResponse) {
        if (userProfileUpDataResponse != null && userProfileUpDataResponse.success) {
            personalUserPresenter.callUserProfileApi(AppPreferences.getInstance(getContext()).getPreferencesString(AppConstants.STUDENTID));
        }
    }

    @Override
    public void setUserProfileResponse(UserProfileResponse userProfileResponse) {
        if (userProfileResponse != null && userProfileResponse.success) {
            DataHolder.getInstance().getUserProfileDataresponse = userProfileResponse;
            settingUserPersonalDetails();
            cancelPersonalDetailEditable();
            personalDetailEditIV.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_edit));
            progressDialog.dismiss();
        }
    }

    @Override
    public void showLoader() {

    }

    @Override
    public void hideLoader() {
        progressDialog.dismiss();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}