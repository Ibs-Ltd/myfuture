package com.myfutureapp.dashboard.homeProfile.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.JsonObject;
import com.myfutureapp.R;
import com.myfutureapp.dashboard.homeProfile.presenter.AcademicUserPresenter;
import com.myfutureapp.dashboard.model.UserProfileResponse;
import com.myfutureapp.login.DataHolder;
import com.myfutureapp.profile.model.GraduationCourseResponse;
import com.myfutureapp.profile.model.PostGraduationCourseResponse;
import com.myfutureapp.profile.model.UserProfileUpDataResponse;
import com.myfutureapp.util.AppConstants;
import com.myfutureapp.util.AppPreferences;
import com.myfutureapp.util.Helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AcademicUserFragment extends Fragment implements AcademicUserView {

    private RadioGroup tenthRadioGroup, twelveRadioGroup, graduationDoneRadioGroup, graduationMarksRadioGroup, postGraduationDoneRadioGroup, postGraduationMarksRadioGroup;
    private RadioButton tenthCGPA, tenthPercentage, twelveCGPA, twelvePercentage, graduationMarksPercentage, graduationMarksCgpa, garduationNo, garduationYes, postGarduationYes, postGarduationNo, postGraduationMarksCgpa, postGraduationMarksPercentage;
    private boolean academicEditable = true;
    private ImageView academicDetailEditIV;
    private EditText et_tenth, cgpaTenthMax, cgpaTenthMarks;
    private EditText et_twelve, cgpaTwelveMarks, cgpaTwelveMax, etGraduationYear;
    private EditText marksGraduation, cgpaGraduationMarks, cgpaGraduationMax, etGraduationDegree, etGraduationSpecialisationDegree, etPostGraduationSpecialisationDegree, otherGradutionET, otherPostGradutionET;
    private LinearLayout cgpaTenthLL, cgpaTwelveLL, cgpaGraduationLL, graduationDetailsMainLL, graduationSpecialisationLL, graduationDoneLL, savingCancelAcademicDetails, postGraduationSpecialisationLL;
    private LinearLayout postGraduationDetailsMainLL, cgpaPostGraduationLL;
    private Spinner graduationDegree, graduationSpecialisationDegree, graduationYear, postGraduationSpecialisationDegree, postGraduationDegree, postGraduationYear;
    private AcademicUserPresenter academicUserPresenter;
    private final ArrayList<String> graduationNameList = new ArrayList<>();
    private final ArrayList<String> graduationSpecialisationNameList = new ArrayList<>();
    private final ArrayList<String> graduationYearList = new ArrayList<>();
    private final ArrayList<String> postGraduationNameList = new ArrayList<>();
    private final ArrayList<String> postGraduationSpecialisationNameList = new ArrayList<>();
    private final ArrayList<String> postGraduationYearList = new ArrayList<>();
    private final List<GraduationCourseResponse.GraduationCourseModel> graduationCourseModelList = new ArrayList<>();
    private final List<PostGraduationCourseResponse.GraduationCourseModel> postGraduationCourseModelList = new ArrayList<>();
    private TextView tenthTitile, twelveTitile, titleGraduation, editAcademicDetail;
    private View viewbelow10th, viewbelow12th, gradutaionDoneBelow, gradutaionDegreeBelow, gradutaionSpecialisationDegreeBelow, viewbelowGraduationYear, viewbelowPostGraduationMarks, viewbelowGraduationMarks, postGradutaionDoneBelow;
    private EditText etPostGraduationDegree, etPostGraduationYear, cgpaPostGraduationMarks, cgpaPostGraduationMax, marksPostGraduation;
    private View postGradutaionDegreeBelow, postGradutaionSpecialisationDegreeBelow, viewbelowPostGraduationYear;
    private TextView titlePostGraduation, saveAcademicDetail, cancelAcademicDetail;
    private String perviousTenthCgpa = "", perviousTenthCgpaMax = "", perviousTenthPercentage = "", perviousTwelveCgpaMax = "", perviousTwelveCgpa = "",
            perviousTwelvePercentage = "", perviousGraduationCgpaMax = "", perviousGraduationCgpa = "", perviousGraduationPercentage = "", perviousGraduationDegree = "", perviousGraduationSpecialisation = "", perviousGraduationYear = "";

    private String perviousPostGraduationDegree = "", perviousPostGraduationYear = "", perviousPostGraduationSpecialisation = "", perviousPostGraduationCgpa = "", perviousPostGraduationCgpaMax = "", perviousPostGraduationPercentage = "";

    private String newGraduationDegree = "", newGraduationSpecialisationDegree = "", newPostGraduationDegree = "", newPostGraduationSpecialisationDegree = "", newPostGraduationYear = "", newGraduationYear = "";

    private ProgressDialog progressDialog;

    private LinearLayout percentageTenthLL, percentagePostGraduationLL, percentageTwelveLL, percentageGraduationLL, postGraduationDoneLL;
    private EditText percTenthMarks, percPostGraduationMarks, percTwelveMarks, percGraduationMarks;

    private boolean isLoaded = false;

    public AcademicUserFragment() {
    }

    public static AcademicUserFragment newInstance(String param1, String param2) {
        AcademicUserFragment fragment = new AcademicUserFragment();
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            Log.e("isVisibleToUser", "true");
            if (!isLoaded) {
                settingGraduationYear();
                settingPostGraduationYear();

                settingUsersAcademicDetails();
                cancelAcademicDetailEditable();
                academicUserPresenter.callGraduationCourseApi();
                academicUserPresenter.callPostGraduationCourseApi();
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
        View view = inflater.inflate(R.layout.fragment_academic_user, container, false);
        inUi(view);
        progressDialog = new ProgressDialog(getContext());
        return view;

    }

    private void inUi(View view) {
        academicUserPresenter = new AcademicUserPresenter(getContext(), AcademicUserFragment.this);
        academicDetailEditIV = (ImageView) view.findViewById(R.id.academicDetailEditIV);

        tenthTitile = (TextView) view.findViewById(R.id.tenthTitile);
        twelveTitile = (TextView) view.findViewById(R.id.twelveTitile);
        titleGraduation = (TextView) view.findViewById(R.id.titleGraduation);
        editAcademicDetail = (TextView) view.findViewById(R.id.editAcademicDetail);
        titlePostGraduation = (TextView) view.findViewById(R.id.titlePostGraduation);
        saveAcademicDetail = (TextView) view.findViewById(R.id.saveAcademicDetail);
        cancelAcademicDetail = (TextView) view.findViewById(R.id.cancelAcademicDetail);

        tenthRadioGroup = (RadioGroup) view.findViewById(R.id.tenthRadioGroup);
        twelveRadioGroup = (RadioGroup) view.findViewById(R.id.twelveRadioGroup);
        graduationDoneRadioGroup = (RadioGroup) view.findViewById(R.id.graduationDoneRadioGroup);
        postGraduationDoneRadioGroup = (RadioGroup) view.findViewById(R.id.postGraduationDoneRadioGroup);
        graduationMarksRadioGroup = (RadioGroup) view.findViewById(R.id.graduationMarksRadioGroup);
        postGraduationMarksRadioGroup = (RadioGroup) view.findViewById(R.id.postGraduationMarksRadioGroup);

        tenthCGPA = (RadioButton) view.findViewById(R.id.tenthCGPA);
        tenthPercentage = (RadioButton) view.findViewById(R.id.tenthPercentage);
        twelveCGPA = (RadioButton) view.findViewById(R.id.twelveCGPA);
        twelvePercentage = (RadioButton) view.findViewById(R.id.twelvePercentage);
        graduationMarksCgpa = (RadioButton) view.findViewById(R.id.graduationMarksCgpa);
        graduationMarksPercentage = (RadioButton) view.findViewById(R.id.graduationMarksPercentage);
        garduationYes = (RadioButton) view.findViewById(R.id.garduationYes);
        garduationNo = (RadioButton) view.findViewById(R.id.garduationNo);
        postGarduationYes = (RadioButton) view.findViewById(R.id.postGarduationYes);
        postGarduationNo = (RadioButton) view.findViewById(R.id.postGarduationNo);
        postGraduationMarksCgpa = (RadioButton) view.findViewById(R.id.postGraduationMarksCgpa);
        postGraduationMarksPercentage = (RadioButton) view.findViewById(R.id.postGraduationMarksPercentage);


        cgpaTenthLL = (LinearLayout) view.findViewById(R.id.cgpaTenthLL);
        percentageTenthLL = (LinearLayout) view.findViewById(R.id.percentageTenthLL);
        cgpaTwelveLL = (LinearLayout) view.findViewById(R.id.cgpaTwelveLL);
        percentageTwelveLL = (LinearLayout) view.findViewById(R.id.percentageTwelveLL);
        cgpaGraduationLL = (LinearLayout) view.findViewById(R.id.cgpaGraduationLL);
        graduationDetailsMainLL = (LinearLayout) view.findViewById(R.id.graduationDetailsMainLL);
        postGraduationDetailsMainLL = (LinearLayout) view.findViewById(R.id.postGraduationDetailsMainLL);
        postGraduationSpecialisationLL = (LinearLayout) view.findViewById(R.id.postGraduationSpecialisationLL);
        graduationSpecialisationLL = (LinearLayout) view.findViewById(R.id.graduationSpecialisationLL);
        graduationDoneLL = (LinearLayout) view.findViewById(R.id.graduationDoneLL);
        savingCancelAcademicDetails = (LinearLayout) view.findViewById(R.id.savingCancelAcademicDetails);
        cgpaPostGraduationLL = (LinearLayout) view.findViewById(R.id.cgpaPostGraduationLL);
        percentageGraduationLL = (LinearLayout) view.findViewById(R.id.percentageGraduationLL);
        percentagePostGraduationLL = (LinearLayout) view.findViewById(R.id.percentagePostGraduationLL);
        postGraduationDoneLL = (LinearLayout) view.findViewById(R.id.postGraduationDoneLL);

        et_tenth = (EditText) view.findViewById(R.id.et_tenth);
        percTenthMarks = (EditText) view.findViewById(R.id.percTenthMarks);
        cgpaTenthMax = (EditText) view.findViewById(R.id.cgpaTenthMax);
        cgpaTenthMarks = (EditText) view.findViewById(R.id.cgpaTenthMarks);

        et_twelve = (EditText) view.findViewById(R.id.et_twelve);
        percTwelveMarks = (EditText) view.findViewById(R.id.percTwelveMarks);
        cgpaTwelveMarks = (EditText) view.findViewById(R.id.cgpaTwelveMarks);
        cgpaTwelveMax = (EditText) view.findViewById(R.id.cgpaTwelveMax);

        cgpaGraduationMarks = (EditText) view.findViewById(R.id.cgpaGraduationMarks);
        cgpaGraduationMax = (EditText) view.findViewById(R.id.cgpaGraduationMax);
        marksGraduation = (EditText) view.findViewById(R.id.marksGraduation);
        percGraduationMarks = (EditText) view.findViewById(R.id.percGraduationMarks);

        etGraduationDegree = (EditText) view.findViewById(R.id.etGraduationDegree);
        etGraduationYear = (EditText) view.findViewById(R.id.etGraduationYear);
        etGraduationSpecialisationDegree = (EditText) view.findViewById(R.id.etGraduationSpecialisationDegree);
        etPostGraduationSpecialisationDegree = (EditText) view.findViewById(R.id.etPostGraduationSpecialisationDegree);
        otherGradutionET = (EditText) view.findViewById(R.id.otherGradutionET);

        etPostGraduationDegree = (EditText) view.findViewById(R.id.etPostGraduationDegree);
        percPostGraduationMarks = (EditText) view.findViewById(R.id.percPostGraduationMarks);
        etPostGraduationYear = (EditText) view.findViewById(R.id.etPostGraduationYear);
        cgpaPostGraduationMarks = (EditText) view.findViewById(R.id.cgpaPostGraduationMarks);
        cgpaPostGraduationMax = (EditText) view.findViewById(R.id.cgpaPostGraduationMax);
        marksPostGraduation = (EditText) view.findViewById(R.id.marksPostGraduation);
        otherPostGradutionET = (EditText) view.findViewById(R.id.otherPostGradutionET);

        graduationDegree = (Spinner) view.findViewById(R.id.graduationDegree);
        graduationSpecialisationDegree = (Spinner) view.findViewById(R.id.graduationSpecialisationDegree);
        graduationYear = (Spinner) view.findViewById(R.id.graduationYear);

        postGraduationDegree = (Spinner) view.findViewById(R.id.postGraduationDegree);
        postGraduationSpecialisationDegree = (Spinner) view.findViewById(R.id.postGraduationSpecialisationDegree);
        postGraduationYear = (Spinner) view.findViewById(R.id.postGraduationYear);

        viewbelow10th = (View) view.findViewById(R.id.viewbelow10th);
        viewbelow12th = (View) view.findViewById(R.id.viewbelow12th);
        postGradutaionSpecialisationDegreeBelow = (View) view.findViewById(R.id.postGradutaionSpecialisationDegreeBelow);
        gradutaionDoneBelow = (View) view.findViewById(R.id.gradutaionDoneBelow);
        gradutaionDegreeBelow = (View) view.findViewById(R.id.gradutaionDegreeBelow);
        gradutaionSpecialisationDegreeBelow = (View) view.findViewById(R.id.gradutaionSpecialisationDegreeBelow);
        viewbelowGraduationYear = (View) view.findViewById(R.id.viewbelowGraduationYear);
        viewbelowPostGraduationMarks = (View) view.findViewById(R.id.viewbelowPostGraduationMarks);
        viewbelowGraduationMarks = (View) view.findViewById(R.id.viewbelowGraduationMarks);
        postGradutaionDoneBelow = (View) view.findViewById(R.id.postGradutaionDoneBelow);
        postGradutaionDegreeBelow = (View) view.findViewById(R.id.postGradutaionDegreeBelow);
        viewbelowPostGraduationYear = (View) view.findViewById(R.id.viewbelowPostGraduationYear);


        tenthRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.tenthPercentage) {
                    cgpaTenthLL.setVisibility(View.GONE);
                    percentageTenthLL.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.tenthCGPA) {
                    cgpaTenthLL.setVisibility(View.VISIBLE);
                    percentageTenthLL.setVisibility(View.GONE);
                }
                Helper.hideKeyboard(getActivity());
            }
        });
        twelveRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.twelvePercentage) {
                    cgpaTwelveLL.setVisibility(View.GONE);
                    percentageTwelveLL.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.twelveCGPA) {
                    cgpaTwelveLL.setVisibility(View.VISIBLE);
                    percentageTwelveLL.setVisibility(View.GONE);
                }
                Helper.hideKeyboard(getActivity());

            }
        });
        graduationMarksRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.graduationMarksPercentage) {
                    cgpaGraduationLL.setVisibility(View.GONE);
                    percentageGraduationLL.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.graduationMarksCgpa) {
                    cgpaGraduationLL.setVisibility(View.VISIBLE);
                    percentageGraduationLL.setVisibility(View.GONE);
                }
                Helper.hideKeyboard(getActivity());

            }
        });
        postGraduationMarksRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.postGraduationMarksPercentage) {
                    cgpaPostGraduationLL.setVisibility(View.GONE);
                    percentagePostGraduationLL.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.postGraduationMarksCgpa) {
                    cgpaPostGraduationLL.setVisibility(View.VISIBLE);
                    percentagePostGraduationLL.setVisibility(View.GONE);
                }
                Helper.hideKeyboard(getActivity());

            }
        });

        graduationDoneRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.garduationNo) {
                    graduationDetailsMainLL.setVisibility(View.GONE);
                    postGraduationDoneLL.setVisibility(View.GONE);
                    postGradutaionDoneBelow.setVisibility(View.GONE);
                    postGraduationDetailsMainLL.setVisibility(View.GONE);
                    postGarduationNo.setChecked(true);
                } else if (checkedId == R.id.garduationYes) {
                    graduationDetailsMainLL.setVisibility(View.VISIBLE);
                    postGraduationDoneLL.setVisibility(View.VISIBLE);
                    if (DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgDegree != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgDegree.length() > 0) {
                        postGarduationYes.setChecked(true);
                        postGraduationDetailsMainLL.setVisibility(View.VISIBLE);
                    } else {
                        postGarduationNo.setChecked(true);
                        postGraduationDetailsMainLL.setVisibility(View.GONE);
                    }
                    if (academicEditable) {
                        postGradutaionDoneBelow.setVisibility(View.VISIBLE);
                    }
                }
                Helper.hideKeyboard(getActivity());

            }
        });
        postGraduationDoneRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.postGarduationNo) {
                    postGraduationDetailsMainLL.setVisibility(View.GONE);
                } else if (checkedId == R.id.postGarduationYes) {
                    postGraduationDetailsMainLL.setVisibility(View.VISIBLE);
                }
                Helper.hideKeyboard(getActivity());

            }
        });


        academicDetailEditIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (academicEditable) {
                    makeAcademicDetailEditable();
                    academicDetailEditIV.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_tick_profile));
                    academicEditable = false;
                } else {
                    Helper.hideKeyboard(getActivity());
                    checkingIsAnythingToUpdate();
                }
            }
        });

        graduationDegree.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Helper.hideKeyboard(getActivity());
                return false;
            }
        });
        otherGradutionET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                newGraduationDegree = s.toString().trim();
            }
        });
        otherPostGradutionET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                newPostGraduationDegree = s.toString().trim();
            }
        });
        graduationDegree.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    graduationSpecialisationLL.setVisibility(View.GONE);
                    newGraduationDegree = "";
                    newGraduationSpecialisationDegree = "";
                } else {
                    if (graduationCourseModelList.get(position - 1).course_name.equalsIgnoreCase("other")) {
                        graduationSpecialisationLL.setVisibility(View.GONE);
                        otherGradutionET.setVisibility(View.VISIBLE);
                        newGraduationSpecialisationDegree = "";
                        graduationSpecialisationNameList.clear();
                    } else {
                        newGraduationDegree = graduationCourseModelList.get(position - 1).course_name;
                        otherGradutionET.setVisibility(View.GONE);
                        newGraduationSpecialisationDegree = "";
                        graduationSpecialisationNameList.clear();
                        if (graduationCourseModelList.get(position - 1).specialization.size() > 0) {
                            graduationSpecialisationLL.setVisibility(View.VISIBLE);
                            graduationSpecialisationNameList.add("Select Specialisation");
                            graduationSpecialisationNameList.addAll(graduationCourseModelList.get(position - 1).specialization);
                            ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, graduationSpecialisationNameList);
                            graduationSpecialisationDegree.setAdapter(adapter);
                            graduationSpecialisationDegree.setVisibility(View.VISIBLE);
                            if (DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationSpecialisation != null) {
                                for (int i = 0; i < graduationSpecialisationNameList.size(); i++) {
                                    if (graduationSpecialisationNameList.get(i).equalsIgnoreCase(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationSpecialisation)) {
                                        graduationSpecialisationDegree.setSelection(i);
                                        newGraduationSpecialisationDegree = graduationSpecialisationNameList.get(i);
                                        break;
                                    }
                                }
                            }
                        } else {
                            graduationSpecialisationLL.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        graduationSpecialisationDegree.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Helper.hideKeyboard(getActivity());
                return false;
            }
        });
        graduationSpecialisationDegree.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    newGraduationSpecialisationDegree = "";
                } else {
                    newGraduationSpecialisationDegree = graduationSpecialisationNameList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        postGraduationDegree.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Helper.hideKeyboard(getActivity());
                return false;
            }
        });
        postGraduationDegree.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    newPostGraduationDegree = "";
                    newPostGraduationSpecialisationDegree = "";
                    postGraduationSpecialisationLL.setVisibility(View.GONE);

                } else {
                    if (postGraduationNameList.get(position).equalsIgnoreCase("other")) {
                        postGraduationSpecialisationLL.setVisibility(View.GONE);
                        newPostGraduationSpecialisationDegree = "";
                        otherPostGradutionET.setVisibility(View.VISIBLE);
                        postGraduationSpecialisationNameList.clear();
                    } else {
                        newPostGraduationDegree = postGraduationNameList.get(position);
                        otherPostGradutionET.setVisibility(View.GONE);
                        newPostGraduationSpecialisationDegree = "";
                        postGraduationSpecialisationNameList.clear();
                        if (postGraduationCourseModelList.get(position - 1).specialization.size() > 0) {
                            newPostGraduationDegree = postGraduationNameList.get(position);
                            postGraduationSpecialisationLL.setVisibility(View.VISIBLE);
                            postGraduationSpecialisationNameList.add("Select Specialisation");
                            postGraduationSpecialisationNameList.addAll(postGraduationCourseModelList.get(position - 1).specialization);
                            ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, postGraduationSpecialisationNameList);
                            postGraduationSpecialisationDegree.setAdapter(adapter);
                            postGraduationSpecialisationDegree.setVisibility(View.VISIBLE);
                            if (DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgSpecialisation != null) {
                                for (int i = 0; i < postGraduationSpecialisationNameList.size(); i++) {
                                    if (postGraduationSpecialisationNameList.get(i).equalsIgnoreCase(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgSpecialisation)) {
                                        postGraduationSpecialisationDegree.setSelection(i);
                                        newPostGraduationSpecialisationDegree = postGraduationSpecialisationNameList.get(i);
                                        break;
                                    }
                                }
                            }
                        } else {
                            postGraduationSpecialisationLL.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        postGraduationSpecialisationDegree.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Helper.hideKeyboard(getActivity());
                return false;
            }
        });
        postGraduationSpecialisationDegree.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    newPostGraduationSpecialisationDegree = "";
                } else {
                    newPostGraduationSpecialisationDegree = postGraduationSpecialisationNameList.get(position);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        postGraduationYear.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Helper.hideKeyboard(getActivity());
                return false;
            }
        });
        postGraduationYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    newPostGraduationYear = "";
                } else {
                    newPostGraduationYear = postGraduationYearList.get(position);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        graduationYear.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Helper.hideKeyboard(getActivity());
                return false;
            }
        });
        graduationYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    newGraduationYear = "";
                } else {
                    newGraduationYear = graduationYearList.get(position);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cancelAcademicDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingUsersAcademicDetails();
                cancelAcademicDetailEditable();
                academicDetailEditIV.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_edit));
                academicEditable = true;
            }
        });
        saveAcademicDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.hideKeyboard(getActivity());
                checkingIsAnythingToUpdate();
            }
        });
        editAcademicDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeAcademicDetailEditable();
                academicDetailEditIV.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_tick_profile));
                academicEditable = false;
            }
        });

    }

    private void settingUsersAcademicDetails() {
        if (DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null) {
            if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data.xGpa != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.x_gpa_max != null) {
                et_tenth.setText(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.xGpa + "/" + DataHolder.getInstance().getUserProfileDataresponse.data.student_data.x_gpa_max + " CGPA");
                cgpaTenthMarks.setText(String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.xGpa));
                cgpaTenthMax.setText(String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.x_gpa_max));
                tenthCGPA.setChecked(true);
                perviousTenthCgpaMax = DataHolder.getInstance().getUserProfileDataresponse.data.student_data.x_gpa_max;
                perviousTenthCgpa = DataHolder.getInstance().getUserProfileDataresponse.data.student_data.xGpa;
                cgpaTenthLL.setVisibility(View.VISIBLE);
                percentageTenthLL.setVisibility(View.GONE);
            } else if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data.xPercentage != null) {
                tenthPercentage.setChecked(true);
                et_tenth.setText(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.xPercentage + "%");
                percTenthMarks.setText(String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.xPercentage));
                perviousTenthPercentage = DataHolder.getInstance().getUserProfileDataresponse.data.student_data.xPercentage;
                cgpaTenthLL.setVisibility(View.GONE);
                percentageTenthLL.setVisibility(View.VISIBLE);

            } else {
                et_tenth.setText("");
                et_tenth.setHint("None");
                perviousTenthCgpaMax = "10";
                perviousTenthCgpa = "";
                perviousTenthPercentage = "";

            }
        } else {
            et_tenth.setText("");
            et_tenth.setHint("None");
            perviousTenthCgpaMax = "10";
            perviousTenthCgpa = "";
            perviousTenthPercentage = "";
        }
        if (DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null) {
            if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data.gradutaionGpa != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationGpaMax != null) {
                marksGraduation.setText(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.gradutaionGpa + "/" + DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationGpaMax + " CGPA");
                cgpaGraduationMarks.setText(String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.gradutaionGpa));
                cgpaGraduationMax.setText(String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationGpaMax));
                graduationMarksCgpa.setChecked(true);
                perviousGraduationCgpaMax = DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationGpaMax;
                perviousGraduationCgpa = DataHolder.getInstance().getUserProfileDataresponse.data.student_data.gradutaionGpa;
            } else if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationPercentage != null) {
                graduationMarksPercentage.setChecked(true);
                marksGraduation.setText(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationPercentage + "%");
                percGraduationMarks.setText(String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationPercentage));
                perviousGraduationPercentage = DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationPercentage;

            } else {

                graduationMarksRadioGroup.clearCheck();
                marksGraduation.setText("");
                marksGraduation.setHint("None");
                perviousGraduationPercentage = "";
                perviousGraduationCgpaMax = "10";
                perviousGraduationCgpa = "";
                percGraduationMarks.setText("");
                cgpaGraduationMarks.setText("");
                cgpaGraduationMax.setText("10");


            }
        } else {
            graduationMarksRadioGroup.clearCheck();
            marksGraduation.setText("");
            marksGraduation.setHint("None");
            perviousGraduationPercentage = "";
            perviousGraduationCgpaMax = "10";
            perviousGraduationCgpa = "";
        }

        if (DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null) {
            if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data.xiiGpa != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.xii_gpa_max != null) {
                et_twelve.setText(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.xiiGpa + "/" + DataHolder.getInstance().getUserProfileDataresponse.data.student_data.xii_gpa_max + " CGPA");
                cgpaTwelveMarks.setText(String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.xiiGpa));
                cgpaTwelveMax.setText(String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.xii_gpa_max));
                twelveCGPA.setChecked(true);
                perviousTwelveCgpa = DataHolder.getInstance().getUserProfileDataresponse.data.student_data.xiiGpa;
                perviousTwelveCgpaMax = DataHolder.getInstance().getUserProfileDataresponse.data.student_data.xii_gpa_max;

            } else if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data.xiiPercentage != null) {
                twelvePercentage.setChecked(true);
                et_twelve.setText(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.xiiPercentage + "%");
                percTwelveMarks.setText(String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.xiiPercentage));
                perviousTwelvePercentage = DataHolder.getInstance().getUserProfileDataresponse.data.student_data.xiiPercentage;

            } else {
                et_twelve.setText("");
                et_twelve.setHint("None");
                perviousTwelvePercentage = "";
                perviousTwelveCgpa = "";
                perviousTwelveCgpaMax = "10";

            }
        } else {
            et_twelve.setText("");
            et_twelve.setHint("None");

            perviousTwelvePercentage = "";
            perviousTwelveCgpa = "";
            perviousTwelveCgpaMax = "10";
        }

        if (DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationDegree != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationDegree.length() > 0 && !DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationDegree.equalsIgnoreCase("None")) {
            garduationYes.setChecked(true);
            graduationDetailsMainLL.setVisibility(View.VISIBLE);
            postGraduationDoneLL.setVisibility(View.VISIBLE);
            postGradutaionDoneBelow.setVisibility(View.VISIBLE);
            postGraduationDetailsMainLL.setVisibility(View.VISIBLE);
        } else {
            garduationNo.setChecked(true);
            graduationDetailsMainLL.setVisibility(View.GONE);
            gradutaionDoneBelow.setVisibility(View.GONE);
            postGraduationDoneLL.setVisibility(View.GONE);
            postGradutaionDoneBelow.setVisibility(View.GONE);
            postGraduationDetailsMainLL.setVisibility(View.GONE);

        }
        if (DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationDegree != null) {
            etGraduationDegree.setText(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationDegree);
            perviousGraduationDegree = DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationDegree;
        } else {
            etGraduationDegree.setText("");
            etGraduationDegree.setHint("None");
            perviousGraduationDegree = "";
        }
        if (DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationSpecialisation != null) {
            etGraduationSpecialisationDegree.setText(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationSpecialisation);
            perviousGraduationSpecialisation = DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationSpecialisation;
            graduationSpecialisationLL.setVisibility(View.VISIBLE);
            gradutaionSpecialisationDegreeBelow.setVisibility(View.VISIBLE);
        } else {
            graduationSpecialisationLL.setVisibility(View.GONE);
            gradutaionSpecialisationDegreeBelow.setVisibility(View.GONE);
            etGraduationSpecialisationDegree.setText("");
            etGraduationSpecialisationDegree.setHint("None");
            perviousGraduationSpecialisation = "";

        }
        if (DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationYear != null) {
            etGraduationYear.setText(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationYear);
            perviousGraduationYear = DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationYear;
        } else {
            etGraduationYear.setText("");
            etGraduationYear.setHint("None");
            perviousGraduationYear = "";

        }

        if (DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null) {
            if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgGpa != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgGpaMax != null) {
                marksPostGraduation.setText(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgGpa + "/" + DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgGpaMax + " CGPA");
                cgpaPostGraduationMarks.setText(String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgGpa));
                cgpaPostGraduationMax.setText(String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgGpaMax));
                postGraduationMarksCgpa.setChecked(true);
                perviousPostGraduationCgpa = DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgGpa;
                perviousPostGraduationCgpaMax = DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgGpaMax;
            } else if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgPercentage != null) {
                postGraduationMarksPercentage.setChecked(true);
                marksPostGraduation.setText(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgPercentage + "%");
                percPostGraduationMarks.setText(String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgPercentage));
                perviousPostGraduationPercentage = DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgPercentage;

            } else {
                postGraduationMarksRadioGroup.clearCheck();
                marksPostGraduation.setText("");
                marksPostGraduation.setHint("None");
                perviousPostGraduationCgpa = "";
                perviousPostGraduationCgpaMax = "10";
                perviousPostGraduationPercentage = "";


                percPostGraduationMarks.setText("");
                cgpaPostGraduationMarks.setText("");
                cgpaPostGraduationMax.setText("10");
            }
        } else {
            postGraduationMarksRadioGroup.clearCheck();
            marksPostGraduation.setText("");
            marksPostGraduation.setHint("None");

            perviousPostGraduationCgpa = "";
            perviousPostGraduationCgpaMax = "10";
            perviousPostGraduationPercentage = "";
        }


        if (DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgDegree != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgDegree.length() > 0 && !DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgDegree.equalsIgnoreCase("None")) {
            postGarduationYes.setChecked(true);
            postGraduationDetailsMainLL.setVisibility(View.VISIBLE);
        } else {
            postGarduationNo.setChecked(true);
            postGraduationDetailsMainLL.setVisibility(View.GONE);
        }
        if (DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgDegree != null && !DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationDegree.equalsIgnoreCase("None")) {
            etPostGraduationDegree.setText(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgDegree);
            perviousPostGraduationDegree = DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgDegree;
        } else {
            etPostGraduationDegree.setText("");
            etPostGraduationDegree.setHint("None");
            perviousPostGraduationDegree = "";
        }
        if (DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgSpecialisation != null) {
            etPostGraduationSpecialisationDegree.setText(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgSpecialisation);
            perviousPostGraduationSpecialisation = DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgSpecialisation;
            postGraduationSpecialisationLL.setVisibility(View.VISIBLE);
            postGradutaionSpecialisationDegreeBelow.setVisibility(View.VISIBLE);
        } else {
            postGraduationSpecialisationLL.setVisibility(View.GONE);
            postGradutaionSpecialisationDegreeBelow.setVisibility(View.GONE);
            etPostGraduationSpecialisationDegree.setText("");
            etPostGraduationSpecialisationDegree.setHint("None");
            perviousPostGraduationSpecialisation = "";
        }
        if (DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgYear != null) {
            etPostGraduationYear.setText(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgYear);
            perviousPostGraduationYear = DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgYear;
        } else {
            etPostGraduationYear.setText("");
            etPostGraduationYear.setHint("None");
            perviousPostGraduationYear = "";

        }
    }

    private void makeAcademicDetailEditable() {
        tenthTitile.setText("10th Score in");
        tenthRadioGroup.setVisibility(View.VISIBLE);

        titleGraduation.setText("Graduation Marks in");
        graduationMarksRadioGroup.setVisibility(View.VISIBLE);


        titlePostGraduation.setText("Post Graduation Marks in");
        postGraduationMarksRadioGroup.setVisibility(View.VISIBLE);

        twelveTitile.setText("12th Score in");
        twelveRadioGroup.setVisibility(View.VISIBLE);

        garduationYes.setEnabled(true);
        garduationNo.setEnabled(true);
        postGarduationNo.setEnabled(true);
        postGarduationYes.setEnabled(true);
        et_tenth.setEnabled(true);
        et_twelve.setEnabled(true);
        etGraduationDegree.setEnabled(true);
        etGraduationSpecialisationDegree.setEnabled(true);
        etGraduationYear.setEnabled(true);
        marksGraduation.setEnabled(true);
        etPostGraduationDegree.setEnabled(true);
        etPostGraduationSpecialisationDegree.setEnabled(true);
        etPostGraduationYear.setEnabled(true);
        marksPostGraduation.setEnabled(true);


        graduationDoneLL.setVisibility(View.VISIBLE);
        if (DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null) {
            if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data.xGpa != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.x_gpa_max != null) {
                tenthCGPA.setChecked(true);
                cgpaTenthLL.setVisibility(View.VISIBLE);
            } else if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data.xPercentage != null) {
                tenthPercentage.setChecked(true);
                percentageTenthLL.setVisibility(View.VISIBLE);
            } else {
//                tenthPercentage.setChecked(true);
            }
        } else {
//            tenthPercentage.setChecked(true);
        }


        if (DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null) {
            if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data.xiiGpa != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.xii_gpa_max != null) {
                twelveCGPA.setChecked(true);
                cgpaTwelveLL.setVisibility(View.VISIBLE);
            } else if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data.xiiPercentage != null) {
                twelvePercentage.setChecked(true);
                percentageTwelveLL.setVisibility(View.VISIBLE);
            } else {
//                twelvePercentage.setChecked(true);
            }
        } else {
//            twelvePercentage.setChecked(true);
        }

        if (DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null) {
            if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgGpa != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgGpaMax != null) {
                postGraduationMarksCgpa.setChecked(true);
                cgpaPostGraduationLL.setVisibility(View.VISIBLE);
            } else if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgPercentage != null) {
                postGraduationMarksPercentage.setChecked(true);
                percentagePostGraduationLL.setVisibility(View.VISIBLE);

            } else {
            }
        } else {
        }

        if (DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null) {
            if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data.gradutaionGpa != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationGpaMax != null) {
                graduationMarksCgpa.setChecked(true);
                cgpaGraduationLL.setVisibility(View.VISIBLE);
            } else if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationPercentage != null) {
                graduationMarksPercentage.setChecked(true);
                percentageGraduationLL.setVisibility(View.VISIBLE);
            } else {
            }
        } else {
        }
        if (DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationDegree != null && !DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationDegree.equalsIgnoreCase("None")) {
            garduationYes.setChecked(true);
            graduationDetailsMainLL.setVisibility(View.VISIBLE);
        } else {
            garduationNo.setChecked(true);
            graduationDetailsMainLL.setVisibility(View.GONE);
        }

        if (DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationDegree != null && !DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationDegree.equalsIgnoreCase("None")) {
            int flag = 0, pos = -1;
            for (int i = 0; i < graduationNameList.size(); i++) {
                if (graduationNameList.get(i).equalsIgnoreCase(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationDegree)) {
                    graduationDegree.setSelection(i);
                    flag = 1;
                    break;
                }
                if (graduationNameList.get(i).equalsIgnoreCase("other")) {
                    pos = i;
                }
            }
            if (flag == 0) {
                graduationDegree.setSelection(pos);
                newGraduationDegree = otherGradutionET.getText().toString();
                otherGradutionET.setText(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationDegree);

            }
        } else {
            graduationDegree.setSelection(0);
        }
        if (DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationYear != null) {
            for (int i = 0; i < graduationYearList.size(); i++) {
                if (graduationYearList.get(i).equalsIgnoreCase(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationYear)) {
                    graduationYear.setSelection(i);
                    break;
                }
            }
        } else {
            graduationYear.setSelection(0);
        }
        if (DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgDegree != null && !DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgDegree.equalsIgnoreCase("None")) {
            int flag = 0, pos = -1;
            for (int i = 0; i < postGraduationNameList.size(); i++) {
                if (postGraduationNameList.get(i).equalsIgnoreCase(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgDegree)) {
                    postGraduationDegree.setSelection(i);
                    flag = 1;
                    break;
                }
                if (postGraduationNameList.get(i).equalsIgnoreCase("other")) {
                    pos = i;
                }
            }
            if (flag == 0) {
                postGraduationDegree.setSelection(pos);
                newPostGraduationDegree = otherPostGradutionET.getText().toString();
                otherPostGradutionET.setText(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgDegree);
            }
        } else {
            postGraduationDegree.setSelection(0);
        }
        if (DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgYear != null) {
            for (int i = 0; i < postGraduationYearList.size(); i++) {
                if (postGraduationYearList.get(i).equalsIgnoreCase(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgYear)) {
                    postGraduationYear.setSelection(i);
                    break;
                }
            }
        } else {
            postGraduationYear.setSelection(0);
        }

        savingCancelAcademicDetails.setVisibility(View.VISIBLE);
        graduationSpecialisationDegree.setVisibility(View.VISIBLE);
        etPostGraduationSpecialisationDegree.setVisibility(View.GONE);
        postGraduationSpecialisationDegree.setVisibility(View.GONE);
        graduationDegree.setVisibility(View.VISIBLE);
        postGraduationDegree.setVisibility(View.VISIBLE);
        graduationYear.setVisibility(View.VISIBLE);
        etGraduationDegree.setVisibility(View.GONE);
        etPostGraduationDegree.setVisibility(View.GONE);
        etPostGraduationYear.setVisibility(View.GONE);
        editAcademicDetail.setVisibility(View.GONE);
        etGraduationYear.setVisibility(View.GONE);
        etGraduationSpecialisationDegree.setVisibility(View.GONE);
        postGraduationYear.setVisibility(View.VISIBLE);
        marksGraduation.setVisibility(View.GONE);
        marksPostGraduation.setVisibility(View.GONE);
        et_twelve.setVisibility(View.GONE);
        et_tenth.setVisibility(View.GONE);

        viewbelow10th.setVisibility(View.GONE);
        viewbelow12th.setVisibility(View.GONE);
        viewbelowGraduationYear.setVisibility(View.GONE);
        viewbelowPostGraduationMarks.setVisibility(View.GONE);
        viewbelowGraduationMarks.setVisibility(View.GONE);
        gradutaionDoneBelow.setVisibility(View.GONE);
        gradutaionDegreeBelow.setVisibility(View.GONE);
        gradutaionSpecialisationDegreeBelow.setVisibility(View.GONE);

        postGradutaionDoneBelow.setVisibility(View.GONE);
        postGradutaionDegreeBelow.setVisibility(View.GONE);
        viewbelowPostGraduationYear.setVisibility(View.GONE);

        cgpaTenthLL.setBackground(getContext().getResources().getDrawable(R.drawable.simple_white_background));
        et_tenth.setBackground(getContext().getResources().getDrawable(R.drawable.simple_white_background));
        cgpaTwelveLL.setBackground(getContext().getResources().getDrawable(R.drawable.simple_white_background));
        et_twelve.setBackground(getContext().getResources().getDrawable(R.drawable.simple_white_background));
        cgpaGraduationLL.setBackground(getContext().getResources().getDrawable(R.drawable.simple_white_background));
        marksGraduation.setBackground(getContext().getResources().getDrawable(R.drawable.simple_white_background));
        marksPostGraduation.setBackground(getContext().getResources().getDrawable(R.drawable.simple_white_background));
        cgpaPostGraduationLL.setBackground(getContext().getResources().getDrawable(R.drawable.simple_white_background));

    }

    private void cancelAcademicDetailEditable() {
        tenthTitile.setText("10th Score");
        tenthRadioGroup.setVisibility(View.GONE);
        twelveTitile.setText("12th Score");
        twelveRadioGroup.setVisibility(View.GONE);
        twelveTitile.setText("12th Score");
        twelveRadioGroup.setVisibility(View.GONE);
        titleGraduation.setText("Graduation Marks");
        graduationMarksRadioGroup.setVisibility(View.GONE);
        titlePostGraduation.setText("Post Graduation Marks");
        postGraduationMarksRadioGroup.setVisibility(View.GONE);

        cgpaTenthLL.setVisibility(View.GONE);
        cgpaTwelveLL.setVisibility(View.GONE);
        percentageGraduationLL.setVisibility(View.GONE);
        percentagePostGraduationLL.setVisibility(View.GONE);
        percentageTenthLL.setVisibility(View.GONE);
        percentageTwelveLL.setVisibility(View.GONE);

        garduationYes.setEnabled(false);
        garduationNo.setEnabled(false);
        postGarduationNo.setEnabled(false);
        postGarduationYes.setEnabled(false);
        et_tenth.setEnabled(false);
        et_twelve.setEnabled(false);
        etGraduationDegree.setEnabled(false);
        etGraduationSpecialisationDegree.setEnabled(false);
        etGraduationYear.setEnabled(false);
        marksGraduation.setEnabled(false);
        etPostGraduationDegree.setEnabled(false);
        etPostGraduationSpecialisationDegree.setEnabled(false);
        etPostGraduationYear.setEnabled(false);
        marksPostGraduation.setEnabled(false);

        et_tenth.setVisibility(View.VISIBLE);
        et_twelve.setVisibility(View.VISIBLE);
        etGraduationDegree.setVisibility(View.VISIBLE);
        marksPostGraduation.setVisibility(View.VISIBLE);
        cgpaPostGraduationLL.setVisibility(View.GONE);
        etPostGraduationDegree.setVisibility(View.VISIBLE);
        etGraduationSpecialisationDegree.setVisibility(View.VISIBLE);
        etPostGraduationSpecialisationDegree.setVisibility(View.VISIBLE);
        etGraduationYear.setVisibility(View.VISIBLE);
        etPostGraduationYear.setVisibility(View.VISIBLE);
        marksGraduation.setVisibility(View.VISIBLE);
        postGraduationYear.setVisibility(View.GONE);
        cgpaGraduationLL.setVisibility(View.GONE);
        postGraduationDegree.setVisibility(View.GONE);
        postGraduationSpecialisationDegree.setVisibility(View.GONE);
        viewbelow10th.setVisibility(View.VISIBLE);
        viewbelow12th.setVisibility(View.VISIBLE);
        viewbelowGraduationYear.setVisibility(View.VISIBLE);
        viewbelowGraduationMarks.setVisibility(View.VISIBLE);
        viewbelowPostGraduationMarks.setVisibility(View.VISIBLE);
        gradutaionDoneBelow.setVisibility(View.VISIBLE);
        gradutaionDegreeBelow.setVisibility(View.VISIBLE);
        if (DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationDegree != null && !DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationDegree.equalsIgnoreCase("None")) {
            postGradutaionDoneBelow.setVisibility(View.VISIBLE);
        }
        postGradutaionDegreeBelow.setVisibility(View.VISIBLE);
        viewbelowPostGraduationYear.setVisibility(View.VISIBLE);

        editAcademicDetail.setVisibility(View.VISIBLE);

        graduationSpecialisationDegree.setVisibility(View.GONE);
        savingCancelAcademicDetails.setVisibility(View.GONE);
        graduationDegree.setVisibility(View.GONE);
        graduationYear.setVisibility(View.GONE);
        otherGradutionET.setVisibility(View.GONE);
        otherPostGradutionET.setVisibility(View.GONE);
        et_tenth.setBackground(getContext().getResources().getDrawable(R.drawable.simple_cool_grey_background));
        et_twelve.setBackground(getContext().getResources().getDrawable(R.drawable.simple_cool_grey_background));
        etGraduationDegree.setBackground(getContext().getResources().getDrawable(R.drawable.simple_cool_grey_background));
        etGraduationSpecialisationDegree.setBackground(getContext().getResources().getDrawable(R.drawable.simple_cool_grey_background));
        etGraduationYear.setBackground(getContext().getResources().getDrawable(R.drawable.simple_cool_grey_background));
        et_twelve.setBackground(getContext().getResources().getDrawable(R.drawable.simple_cool_grey_background));
        marksGraduation.setBackground(getContext().getResources().getDrawable(R.drawable.simple_cool_grey_background));
        marksPostGraduation.setBackground(getContext().getResources().getDrawable(R.drawable.simple_cool_grey_background));
        cgpaPostGraduationLL.setBackground(getContext().getResources().getDrawable(R.drawable.simple_cool_grey_background));

    }

    private void settingGraduationYear() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        year += 5;

        graduationYearList.add("Select Year");
        postGraduationYearList.add("Select Year");
        for (int i = 0; i < 50; i++) {
            graduationYearList.add(String.valueOf(year - i));
            postGraduationYearList.add(String.valueOf(year - i));
        }
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, graduationYearList);
        graduationYear.setAdapter(adapter);
    }

    private void settingPostGraduationYear() {
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, postGraduationYearList);
        postGraduationYear.setAdapter(adapter);
    }

    private void checkingIsAnythingToUpdate() {
        JsonObject jsonObject = new JsonObject();
        if (tenthRadioGroup.getCheckedRadioButtonId() == R.id.tenthCGPA) {
            if (checkingValidCGPA(cgpaTenthMarks.getText().toString(), cgpaTenthMax.getText().toString())) {
                if (!cgpaTenthMarks.getText().toString().trim().equalsIgnoreCase(perviousTenthCgpa)) {
                    jsonObject.addProperty("x_gpa", cgpaTenthMarks.getText().toString().trim());
                }
                if (!cgpaTenthMax.getText().toString().trim().equalsIgnoreCase(perviousTenthCgpaMax)) {
                    jsonObject.addProperty("x_gpa_max", cgpaTenthMax.getText().toString().trim());
                }

            } else {
                return;
            }
        } else if (tenthRadioGroup.getCheckedRadioButtonId() == R.id.tenthPercentage) {
            if (checkingValidPercentage(percTenthMarks.getText().toString())) {
                if (!percTenthMarks.getText().toString().trim().equalsIgnoreCase(perviousTenthPercentage)) {
                    jsonObject.addProperty("x_percentage", percTenthMarks.getText().toString().trim());
                }
            } else {
                return;
            }
        }
        if (twelveRadioGroup.getCheckedRadioButtonId() == R.id.twelveCGPA) {
            if (checkingValidCGPA(cgpaTwelveMarks.getText().toString(), cgpaTwelveMax.getText().toString())) {
                if (!cgpaTwelveMarks.getText().toString().trim().equalsIgnoreCase(perviousTwelveCgpa)) {
                    jsonObject.addProperty("xii_gpa", cgpaTwelveMarks.getText().toString().trim());
                }
                if (!cgpaTwelveMax.getText().toString().trim().equalsIgnoreCase(perviousTwelveCgpaMax)) {
                    jsonObject.addProperty("xii_gpa_max", cgpaTwelveMax.getText().toString().trim());
                }
            } else {
                return;
            }
        } else if (twelveRadioGroup.getCheckedRadioButtonId() == R.id.twelvePercentage) {
            if (checkingValidPercentage(percTwelveMarks.getText().toString())) {
                if (!percTwelveMarks.getText().toString().trim().equalsIgnoreCase(perviousTwelvePercentage)) {
                    jsonObject.addProperty("xii_percentage", percTwelveMarks.getText().toString().trim());
                }
            } else {
                return;
            }
        }


        if (graduationDoneRadioGroup.getCheckedRadioButtonId() == R.id.garduationYes) {
            if (newGraduationDegree != null && perviousGraduationDegree != null && !newGraduationDegree.equalsIgnoreCase(perviousGraduationDegree)) {
                if (newGraduationDegree.length() > 0) {
                    jsonObject.addProperty("graduation_degree", newGraduationDegree);
                } else {
                    showMessage("Please Select Graduation Degree");
                    return;
                }
            }
            if (graduationSpecialisationNameList.size() > 0) {
                if (newGraduationSpecialisationDegree.length() > 0) {
                    if (!newGraduationSpecialisationDegree.equalsIgnoreCase(perviousGraduationSpecialisation)) {
                        jsonObject.addProperty("graduation_specialisation", newGraduationSpecialisationDegree);
                    }
                } else {
                    showMessage("Please Select Graduation Specialisation");
                    return;
                }
            } else {
                if (!newGraduationSpecialisationDegree.equalsIgnoreCase(perviousGraduationSpecialisation)) {
                    jsonObject.addProperty("graduation_specialisation", newGraduationSpecialisationDegree);
                }
            }

            if (newGraduationYear != null && newGraduationYear.length() > 0) {
                if (!newGraduationYear.equalsIgnoreCase(perviousGraduationYear)) {
                    jsonObject.addProperty("graduation_year", newGraduationYear);
                }
            } else {
                showMessage("Select Graduation year");
                return;
            }

            if (graduationMarksRadioGroup.getCheckedRadioButtonId() == R.id.graduationMarksCgpa) {
                if (checkingValidCGPA(cgpaGraduationMarks.getText().toString(), cgpaGraduationMax.getText().toString())) {
                    if (!cgpaGraduationMarks.getText().toString().trim().equalsIgnoreCase(perviousGraduationCgpa)) {
                        jsonObject.addProperty("graduation_gpa", cgpaGraduationMarks.getText().toString().trim());
                    }
                    if (!cgpaGraduationMax.getText().toString().trim().equalsIgnoreCase(perviousGraduationCgpaMax)) {
                        jsonObject.addProperty("graduation_gpa_max", cgpaGraduationMax.getText().toString().trim());
                    }


                } else {
                    return;
                }
            } else if (graduationMarksRadioGroup.getCheckedRadioButtonId() == R.id.graduationMarksPercentage) {
                if (checkingValidPercentage(percGraduationMarks.getText().toString())) {
                    if (!percGraduationMarks.getText().toString().trim().equalsIgnoreCase(perviousGraduationPercentage)) {
                        jsonObject.addProperty("graduation_percentage", percGraduationMarks.getText().toString().trim());
                    }
                }
            } else {
                showMessage("Enter Graduation Marks");
                return;
            }

        } else if (graduationDoneRadioGroup.getCheckedRadioButtonId() == R.id.garduationNo) {
            if (perviousGraduationDegree != null && perviousGraduationDegree.length() > 0) {
                jsonObject.addProperty("graduation_degree", "None");
                jsonObject.addProperty("graduation_specialisation", "");
                jsonObject.addProperty("graduation_year", "");
                jsonObject.addProperty("graduation_gpa", "");
                jsonObject.addProperty("graduation_gpa_max", "10");
                jsonObject.addProperty("graduation_percentage", "");

                jsonObject.addProperty("pg_degree", "None");
                jsonObject.addProperty("pg_specialisation", "");
                jsonObject.addProperty("pg_year", "");
                jsonObject.addProperty("pg_gpa", "");
                jsonObject.addProperty("pg_gpa_max", "10");
                jsonObject.addProperty("pg_percentage", "");
            }

        }
        if (postGraduationDoneRadioGroup.getCheckedRadioButtonId() == R.id.postGarduationYes) {

            if (newPostGraduationDegree != null && perviousPostGraduationDegree != null && !newPostGraduationDegree.equalsIgnoreCase(perviousPostGraduationDegree)) {
                if (newPostGraduationDegree.length() > 0) {
                    jsonObject.addProperty("pg_degree", newPostGraduationDegree);
                } else {
                    showMessage("Please Select Post Graduation Degree");
                    return;
                }
            }
            if (postGraduationSpecialisationNameList.size() > 0) {
                if (newPostGraduationSpecialisationDegree.length() > 0) {
                    if (!newPostGraduationSpecialisationDegree.equalsIgnoreCase(perviousPostGraduationSpecialisation)) {
                        jsonObject.addProperty("pg_specialisation", newPostGraduationSpecialisationDegree);
                    }
                } else {
                    showMessage("Please Select Post Graduation Specialisation");
                    return;
                }
            } else {
                if (!newPostGraduationSpecialisationDegree.equalsIgnoreCase(perviousPostGraduationSpecialisation)) {
                    jsonObject.addProperty("pg_specialisation", newPostGraduationSpecialisationDegree);
                }
            }
            if (newPostGraduationYear != null && newPostGraduationYear.length() > 0) {
                if (newGraduationYear != null && newGraduationYear.length() > 0 && Integer.parseInt(newPostGraduationYear) < Integer.parseInt(newGraduationYear) + 1) {
                    showMessage("post graduation year can't be less than graduation year");
                    return;
                }
                if (!newPostGraduationYear.equalsIgnoreCase(perviousPostGraduationYear)) {
                    jsonObject.addProperty("pg_year", newPostGraduationYear);
                }
            } else {
                showMessage("Select Post Graduation year");
                return;
            }

            if (postGraduationMarksRadioGroup.getCheckedRadioButtonId() == R.id.postGraduationMarksCgpa) {
                if (checkingValidCGPA(cgpaPostGraduationMarks.getText().toString(), cgpaPostGraduationMax.getText().toString())) {
                    if (!cgpaPostGraduationMarks.getText().toString().trim().equalsIgnoreCase(perviousPostGraduationCgpa)) {
                        jsonObject.addProperty("pg_gpa", cgpaPostGraduationMarks.getText().toString().trim());
                    }
                    if (!cgpaPostGraduationMax.getText().toString().trim().equalsIgnoreCase(perviousPostGraduationCgpaMax)) {
                        jsonObject.addProperty("pg_gpa_max", cgpaPostGraduationMax.getText().toString().trim());
                    }


                } else {
                    return;
                }
            } else if (postGraduationMarksRadioGroup.getCheckedRadioButtonId() == R.id.postGraduationMarksPercentage) {
                if (checkingValidPercentage(percPostGraduationMarks.getText().toString())) {
                    if (!percPostGraduationMarks.getText().toString().trim().equalsIgnoreCase(perviousPostGraduationPercentage)) {
                        jsonObject.addProperty("pg_percentage", percPostGraduationMarks.getText().toString().trim());
                    }
                } else {
                    return;
                }
            } else {
                showMessage("Enter Post Graduation Marks");
                return;
            }

        } else if (postGraduationDoneRadioGroup.getCheckedRadioButtonId() == R.id.postGarduationNo) {
            if (perviousPostGraduationDegree != null && perviousPostGraduationDegree.length() > 0) {
                jsonObject.addProperty("pg_degree", "None");
                jsonObject.addProperty("pg_specialisation", "");
                jsonObject.addProperty("pg_year", "");
                jsonObject.addProperty("pg_gpa", "");
                jsonObject.addProperty("pg_gpa_max", "10");
                jsonObject.addProperty("pg_percentage", "");
            }

        }

        Log.e("acedmicUserData", jsonObject.toString());
        if (jsonObject.size() == 0) {
            showMessage("Nothing To Update");
            cancelAcademicDetailEditable();
            academicEditable = true;
            academicDetailEditIV.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_edit));
            return;
        } else {
            progressDialog.setMessage("saving your details");
            progressDialog.setCancelable(false);
            progressDialog.show();
            academicUserPresenter.callUserProfileUploadApi(jsonObject);
        }

    }

    private boolean checkingValidPercentage(String toString) {
        if (toString.length() != 0) {
            float perMarks = Float.parseFloat(toString.trim());
            if (perMarks > 0 && perMarks <= 100) {
                return true;
            } else {
                showMessage("enter correct percentage");
            }
        } else {
            showMessage("enter correct percentage");
        }
        return false;
    }

    private boolean checkingValidCGPA(String cgpaValue, String cgpaMaxValue) {
        if (cgpaValue.length() != 0 && cgpaMaxValue.length() != 0) {
            float cgpa = Float.parseFloat(cgpaValue);
            float cgpaMaxValues = Float.parseFloat(cgpaMaxValue);
            if (cgpa > cgpaMaxValues) {
                showMessage("cgpa will less than cgpa max");
            } else if (cgpaMaxValues > 12) {
                showMessage("cgpaMax will not be greater than 12");
            } else if (cgpa < 1 || cgpa > cgpaMaxValues) {
                showMessage("cgpa value will not be zero");
            } else if (!Helper.isValidCgpa(cgpaMaxValue)) {
                showMessage("cgpa is incorrect");
            } else {
                return true;
            }

        } else {
            showMessage("Enter correct  Marks");
        }
        return false;
    }

    @Override
    public void setPostGraduationCourseResponse(PostGraduationCourseResponse graduationCourseResponse) {
        if (graduationCourseResponse != null && graduationCourseResponse.success) {
            postGraduationCourseModelList.addAll(graduationCourseResponse.data);
            postGraduationNameList.add("Select Post Graduation");
            for (int i = 0; i < graduationCourseResponse.data.size(); i++) {
                if (!graduationCourseResponse.data.get(i).course_name.equalsIgnoreCase("Not Done Yet")) {
                    postGraduationNameList.add(postGraduationCourseModelList.get(i).course_name);
                }
            }
            ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, postGraduationNameList);
            postGraduationDegree.setAdapter(adapter);

        }
    }

    @Override
    public void setGraduationCourseResponse(GraduationCourseResponse graduationCourseResponse) {
        if (graduationCourseResponse != null && graduationCourseResponse.success) {
            graduationCourseModelList.addAll(graduationCourseResponse.data);
            graduationNameList.add("Select Graduation");
            for (int i = 0; i < graduationCourseResponse.data.size(); i++) {
                graduationNameList.add(graduationCourseModelList.get(i).course_name);
            }
            ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, graduationNameList);
            graduationDegree.setAdapter(adapter);

        }
    }

    @Override
    public void setProfileDataResponse(UserProfileUpDataResponse userProfileUpDataResponse) {
        if (userProfileUpDataResponse != null && userProfileUpDataResponse.success) {
            academicUserPresenter.callUserProfileApi(AppPreferences.getInstance(getContext()).getPreferencesString(AppConstants.STUDENTID));
        }
    }

    @Override
    public void setUserProfileResponse(UserProfileResponse userProfileResponse) {
        if (userProfileResponse != null && userProfileResponse.success) {
            DataHolder.getInstance().getUserProfileDataresponse = userProfileResponse;
            settingUsersAcademicDetails();
            cancelAcademicDetailEditable();
            academicDetailEditIV.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_edit));
            progressDialog.dismiss();
            academicEditable = true;
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