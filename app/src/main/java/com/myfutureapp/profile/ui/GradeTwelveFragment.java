package com.myfutureapp.profile.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.myfutureapp.R;
import com.myfutureapp.login.DataHolder;
import com.myfutureapp.util.Helper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GradeTwelveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GradeTwelveFragment extends Fragment {

    private static HandleFragmentLoading handleFragmentLoading;

    private LinearLayout cgpaLL, percentageLL;
    private EditText cgpaMarks, cgpaMax, percMarks;
    private TextView continueGraduuationScore;
    private LinearLayout cgpaScore, percentScore;
    private final String exactPer = "";
    private boolean cgpaSelected = false;
    private boolean btnEnable = false;
    private View view;

    public GradeTwelveFragment() {
        // Required empty public constructor
    }

    public static GradeTwelveFragment newInstance(HandleFragmentLoading handleFragmentLoadings) {
        GradeTwelveFragment fragment = new GradeTwelveFragment();
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
            view = inflater.inflate(R.layout.fragment_twelve_grade, container, false);
            inUi(view);
            return view;
        }
        return view;
    }

    private void inUi(View view) {
        EditText marks = (EditText) view.findViewById(R.id.marks);

        cgpaScore = (LinearLayout) view.findViewById(R.id.cgpaScore);
        percentScore = (LinearLayout) view.findViewById(R.id.percentScore);

        percentageLL = (LinearLayout) view.findViewById(R.id.percentageLL);
        cgpaLL = (LinearLayout) view.findViewById(R.id.cgpaLL);

        cgpaMarks = (EditText) view.findViewById(R.id.cgpaMarks);
        cgpaMax = (EditText) view.findViewById(R.id.cgpaMax);
        percMarks = (EditText) view.findViewById(R.id.percMarks);
        TextView continueGraduuationScore = (TextView) view.findViewById(R.id.continueGraduuationScore);

       /* percMarks.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                 *//*   if (percMarks.getText().toString().length() > 2) {
                        Float perMar = Float.valueOf(percMarks.getText().toString());
                        if (perMar <= 100) {
                            DataHolder.getInstance().getUserDataresponse.graduation_percentage = exactPer;
                            handleFragmentLoading.loadingQuizDetailFragment("postGraduationFragment");
                        } else {
                            showMessage("Please add correct marks");
                        }
                    } else {
                        showMessage("Please add correct marks");
                    }*//*
                    return true;
                }
                return false;
            }
        });*/
       /* cgpaMarks.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    *//*if (cgpaMarks.getText().toString().length() > 2) {
                        Float perMar = Float.valueOf(cgpaMarks.getText().toString());
                        if (perMar <= 100) {
                            DataHolder.getInstance().getUserDataresponse.graduation_percentage = exactPer;
                            handleFragmentLoading.loadingQuizDetailFragment("postGraduationFragment");
                        } else {
                            showMessage("Please add correct marks");
                        }
                    } else {
                        showMessage("Please add correct marks");
                    }*//*
                    return true;
                }
                return false;
            }
        });*/

      /*  cgpaMax.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                  *//*  if (cgpaMax.getText().toString().length() > 2) {
                        Float perMar = Float.valueOf(cgpaMax.getText().toString());
                        if (perMar <= 100) {
                            DataHolder.getInstance().getUserDataresponse.graduation_percentage = exactPer;
                            handleFragmentLoading.loadingQuizDetailFragment("postGraduationFragment");
                        } else {
                            showMessage("Please add correct marks");
                        }
                    } else {
                        showMessage("Please add correct marks");
                    }*//*
                    return true;
                }
                return false;
            }
        });*/

        cgpaScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cgpaScore.setBackground(getContext().getResources().getDrawable(R.drawable.orange_btn_right_curve));
                percentScore.setBackground(getContext().getResources().getDrawable(R.drawable.grey_btn_left_curve));
                cgpaSelected = true;
                cgpaLL.setVisibility(View.VISIBLE);
                percentageLL.setVisibility(View.GONE);
                //    markTV.setText(cgpaStr);
                if (cgpaMarks.getText().toString().length() >= 1) {
                    continueGraduuationScore.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_dark_black_btn));
                    btnEnable = true;
                } else {
                    continueGraduuationScore.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_greish_btn_disable));
                    btnEnable = false;
                }
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
                //  markTV.setText(perStr);
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
                            } else if (!Helper.isValidSgpa(cgpaMarks.getText().toString())) {
                                showMessage("cgpa is incorrect");
                            } else {
                                DataHolder.getInstance().getUserDataresponse.xii_gpa = cgpaMarks.getText().toString();
                                DataHolder.getInstance().getUserDataresponse.xii_gpa_max = cgpaMax.getText().toString();
                                DataHolder.getInstance().getUserDataresponse.xii_percentage = null;
                                handleFragmentLoading.loadingQuizDetailFragment(nextAction);

                            }

                        } else {
                            showMessage("Enter Marks");

                        }
                    } else {

                        if (percMarks.getText().toString().length() != 0) {
                            float perMarks = Float.parseFloat(percMarks.getText().toString());
                            if (perMarks > 0 && perMarks <= 100) {
                                DataHolder.getInstance().getUserDataresponse.xii_gpa = null;
                                DataHolder.getInstance().getUserDataresponse.xii_gpa_max = null;
                                DataHolder.getInstance().getUserDataresponse.xii_percentage = percMarks.getText().toString();
                                handleFragmentLoading.loadingQuizDetailFragment(nextAction);
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
                && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.xiiGpa != null) {
            cgpaMarks.setText(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.xiiGpa);
            cgpaScore.callOnClick();
        }
        if (DataHolder.getInstance().getUserProfileDataresponse != null && DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null
                && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.xii_gpa_max != null) {
            cgpaMax.setText(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.xii_gpa_max);
        }
        if (DataHolder.getInstance().getUserProfileDataresponse != null && DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null
                && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.xiiPercentage != null) {
            percMarks.setText(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.xiiPercentage);
            percentageLL.callOnClick();
        }
      /*  continueGraduuationScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (marks.getText().toString().length() != 0) {
                    float perMarks = Float.parseFloat(marks.getText().toString());
                    if (perMarks>0&&perMarks <= 100) {
                        DataHolder.getInstance().getUserDataresponse.xii_percentage = marks.getText().toString();
                        handleFragmentLoading.loadingQuizDetailFragment("gradeTenthFragment");
                    } else {
                        showMessage("enter correct percentage");
                    }
                } else {
                    showMessage("enter correct percentage");
                }


            }
        });*/

    }

    private String checkingFragmentAvailble() {
        if (!((ProfileActivity) getActivity()).askXEducationDetails.equalsIgnoreCase("3")) {
            return "gradeTenthFragment";
        } else if (!((ProfileActivity) getActivity()).askAdditionalInformation.equalsIgnoreCase("3")) {
            return "additionalFragment";
        } else {
            return "finished";
        }
    }
}