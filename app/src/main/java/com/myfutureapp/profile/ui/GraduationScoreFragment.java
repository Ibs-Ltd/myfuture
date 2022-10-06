package com.myfutureapp.profile.ui;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.myfutureapp.R;
import com.myfutureapp.login.DataHolder;
import com.myfutureapp.util.Helper;

import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GraduationScoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GraduationScoreFragment extends Fragment {

    private static HandleFragmentLoading handleFragmentLoading;
    private final Pattern sPattern = Pattern.compile("[0-9]+(\\.[0-9]{1,2})?%?");
    //    private TextView markTV;
    private LinearLayout cgpaScore, percentScore;
    private boolean cgpaSelected = false;
    private final String cgpaStr = "_._/10";
    private final String perStr = "00.00%/100";
    private final String exactCgpa = "";
    private final String exactPer = "";
    private TextView graduationCourseTV, graduationCourseSpecialTV, graduationCourseYearTV;
    private BroadcastReceiver mReceiver;
    private boolean btnEnable = false;
    private LinearLayout cgpaLL, percentageLL;
    private EditText cgpaMarks, cgpaMax, percMarks;
    private TextView continueGraduuationScore;
    private View view;

    public GraduationScoreFragment() {
        // Required empty public constructor
    }

    public static GraduationScoreFragment newInstance(HandleFragmentLoading handleFragmentLoadings) {
        GraduationScoreFragment fragment = new GraduationScoreFragment();
        handleFragmentLoading = handleFragmentLoadings;
        return fragment;
    }

    private void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_graduation_score, container, false);
            inUi(view);
            return view;
        }
        return view;
    }

    private boolean isValid(CharSequence s) {
        return sPattern.matcher(s).matches();
    }

    private void buttonEnable() {
       /* if (selectCity.length() <= 0) {
            contineJobLogin.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_greish_btn_disable));
            btnEnable = false;
        } else {
            contineJobLogin.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_dark_black_btn));
            btnEnable = true;
        }*/
    }

    private void inUi(View view) {
        graduationCourseTV = (TextView) view.findViewById(R.id.graduationCourseTV);
        graduationCourseSpecialTV = (TextView) view.findViewById(R.id.graduationCourseSpecialTV);
        graduationCourseYearTV = (TextView) view.findViewById(R.id.graduationCourseYearTV);
        continueGraduuationScore = (TextView) view.findViewById(R.id.continueGraduuationScore);

        SeekBar seekBar = view.findViewById(R.id.seekBar);
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });


        //   markTV = (TextView) view.findViewById(R.id.markTV);
        cgpaScore = (LinearLayout) view.findViewById(R.id.cgpaScore);
        percentScore = (LinearLayout) view.findViewById(R.id.percentScore);

        percentageLL = (LinearLayout) view.findViewById(R.id.percentageLL);
        cgpaLL = (LinearLayout) view.findViewById(R.id.cgpaLL);

        cgpaMarks = (EditText) view.findViewById(R.id.cgpaMarks);
        cgpaMax = (EditText) view.findViewById(R.id.cgpaMax);
        percMarks = (EditText) view.findViewById(R.id.percMarks);

        cgpaScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cgpaScore.setBackground(getContext().getResources().getDrawable(R.drawable.orange_btn_right_curve));
                percentScore.setBackground(getContext().getResources().getDrawable(R.drawable.grey_btn_left_curve));
                cgpaSelected = true;
                cgpaLL.setVisibility(View.VISIBLE);
                percentageLL.setVisibility(View.GONE);
                if (cgpaMarks.getText().toString().length() >= 1) {
                    continueGraduuationScore.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_dark_black_btn));
                    btnEnable = true;
                } else {
                    continueGraduuationScore.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_greish_btn_disable));
                    btnEnable = false;
                }
                //    markTV.setText(cgpaStr);
            }
        });
        percentScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                percentScore.setBackground(getContext().getResources().getDrawable(R.drawable.orange_btn_left_curve));
                cgpaScore.setBackground(getContext().getResources().getDrawable(R.drawable.grey_btn_right_curve));
                cgpaSelected = false;

                cgpaLL.setVisibility(View.GONE);
                percentageLL.setVisibility(View.VISIBLE);
                if (percMarks.getText().toString().length() >= 1) {
                    continueGraduuationScore.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_dark_black_btn));
                    btnEnable = true;
                } else {
                    continueGraduuationScore.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_greish_btn_disable));
                    btnEnable = false;
                }
            }
        });

        cgpaMarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable p0) {
                if (p0.toString().trim().length() >= 1) {
                    continueGraduuationScore.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_dark_black_btn));
                    btnEnable = true;
                } else {
                    continueGraduuationScore.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_greish_btn_disable));
                    btnEnable = false;
                }
            }
        });
        percMarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable p0) {
                if (p0.toString().trim().length() >= 1) {
                    continueGraduuationScore.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_dark_black_btn));
                    btnEnable = true;
                } else {
                    continueGraduuationScore.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_greish_btn_disable));
                    btnEnable = false;
                }
            }
        });


        String nextAction = checkingFragmentAvailble();
        if (nextAction.equalsIgnoreCase("finished")) {
            continueGraduuationScore.setText("Complete");
        }
        continueGraduuationScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnEnable) {
                    if (cgpaSelected) {
                        if (cgpaMarks.getText().toString().length() != 0 && cgpaMax.getText().toString().length() != 0) {
                            float cgpa = Float.parseFloat(cgpaMarks.getText().toString());
                            float cgpaMaxValue = Float.parseFloat(cgpaMax.getText().toString());
                            if (cgpa > cgpaMaxValue) {
                                showMessage("cgpa will less than cgpa max");
                            } else if (cgpaMaxValue > 12) {
                                showMessage("cgpaMax will not be greater than 12");
                            } else if (cgpa < 1 || cgpa > cgpaMaxValue) {
                                showMessage("cgpa value will not be zero");
                            } else if (!isValid(cgpaMarks.getText().toString())) {
                                showMessage("cgpa is incorrect");
                            } else {
                                DataHolder.getInstance().getUserDataresponse.graduation_gpa = cgpaMarks.getText().toString();
                                DataHolder.getInstance().getUserDataresponse.graduation_gpa_max = cgpaMax.getText().toString();
                                DataHolder.getInstance().getUserDataresponse.graduation_percentage = null;
//                            checkingFragmentAvailble();
                                handleFragmentLoading.loadingQuizDetailFragment(nextAction);

                            }

                        } else {
                            showMessage("Enter Marks");

                        }
                    } else {

                        if (percMarks.getText().toString().length() != 0) {
                            float perMarks = Float.parseFloat(percMarks.getText().toString());
                            if (perMarks > 0 && perMarks <= 100) {
                                DataHolder.getInstance().getUserDataresponse.graduation_gpa = null;
                                DataHolder.getInstance().getUserDataresponse.graduation_gpa_max = null;
                                DataHolder.getInstance().getUserDataresponse.graduation_percentage = percMarks.getText().toString();
                                handleFragmentLoading.loadingQuizDetailFragment(nextAction);
//                            checkingFragmentAvailble();
                            } else {
                                showMessage("enter correct percentage");
                            }
                        } else {
                            showMessage("enter correct percentage");
                        }
                    }

                }
            }
        });

        if (DataHolder.getInstance().getUserProfileDataresponse != null && DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null
                && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.gradutaionGpa != null) {
            cgpaMarks.setText(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.gradutaionGpa);
            cgpaScore.callOnClick();
        }
        if (DataHolder.getInstance().getUserProfileDataresponse != null && DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null
                && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationGpaMax != null) {
            cgpaMax.setText(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationGpaMax);
        }
        if (DataHolder.getInstance().getUserProfileDataresponse != null && DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null
                && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationPercentage != null) {
            percMarks.setText(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationPercentage);
            percentageLL.callOnClick();
        }
        settingValue();


    }

    private String checkingFragmentAvailble() {
        if (!((ProfileActivity) getActivity()).askPostGraduationDetails.equalsIgnoreCase("3")) {
            return "postGraduationFragment";
        } else if (!((ProfileActivity) getActivity()).askWorkExperience.equalsIgnoreCase("3")) {
            return "workExperienceFragment";
        } else if (!((ProfileActivity) getActivity()).askXIIEducationDetails.equalsIgnoreCase("3")) {
            return "gradeTwelveFragment";
        } else if (!((ProfileActivity) getActivity()).askXEducationDetails.equalsIgnoreCase("3")) {
            return "gradeTenthFragment";
        } else if (!((ProfileActivity) getActivity()).askAdditionalInformation.equalsIgnoreCase("3")) {
            return "additionalFragment";
        } else {
            return "finished";
        }
    }

    private void settingValue() {
        if (Helper.isContainValue(DataHolder.getInstance().getUserDataresponse.graduation_degree)) {
            graduationCourseTV.setText(DataHolder.getInstance().getUserDataresponse.graduation_degree);
        }
        if (Helper.isContainValue(DataHolder.getInstance().getUserDataresponse.graduation_specialisation)) {
            graduationCourseSpecialTV.setText(DataHolder.getInstance().getUserDataresponse.graduation_specialisation);
            graduationCourseSpecialTV.setVisibility(View.VISIBLE);
        } else {
            graduationCourseSpecialTV.setVisibility(View.GONE);
        }
        if (Helper.isContainValue(DataHolder.getInstance().getUserDataresponse.graduation_year)) {
            graduationCourseYearTV.setText(DataHolder.getInstance().getUserDataresponse.graduation_year);
        }
    }


}