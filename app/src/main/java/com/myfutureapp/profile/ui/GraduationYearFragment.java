package com.myfutureapp.profile.ui;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.myfutureapp.R;
import com.myfutureapp.login.DataHolder;
import com.myfutureapp.util.Helper;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GraduationYearFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GraduationYearFragment extends Fragment {

    private static HandleFragmentLoading handleFragmentLoading;
    private TextView yearTV;
    private BroadcastReceiver mReceiver;
    private TextView graduationCourseTV, graduationCourseSpecialTV;
    private View view;
    private boolean btnEnable = false;

    public GraduationYearFragment() {
        // Required empty public constructor
    }

    public static GraduationYearFragment newInstance(HandleFragmentLoading handleFragmentLoadings) {
        GraduationYearFragment fragment = new GraduationYearFragment();
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
            view = inflater.inflate(R.layout.fragment_graduation_year, container, false);
            inUi(view);
            return view;
        }
        return view;
    }

    private void inUi(View view) {
        graduationCourseTV = (TextView) view.findViewById(R.id.graduationCourseTV);
        graduationCourseSpecialTV = (TextView) view.findViewById(R.id.graduationCourseSpecialTV);
        yearTV = (TextView) view.findViewById(R.id.yearTV);
        SeekBar seekBar = view.findViewById(R.id.seekBar);
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });


        if (Helper.isContainValue(DataHolder.getInstance().getUserDataresponse.graduation_degree)) {
            graduationCourseTV.setText(DataHolder.getInstance().getUserDataresponse.graduation_degree);
        }

        if (Helper.isContainValue(DataHolder.getInstance().getUserDataresponse.graduation_specialisation)) {
            graduationCourseSpecialTV.setText(DataHolder.getInstance().getUserDataresponse.graduation_specialisation);
            graduationCourseSpecialTV.setVisibility(View.VISIBLE);
        } else {
            graduationCourseSpecialTV.setVisibility(View.GONE);
        }


        TextView continueGraduuationYear = (TextView) view.findViewById(R.id.continueGraduuationYear);
        continueGraduuationYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnEnable) {
                    if (yearTV.getText().toString().length() == 4) {
                        int years = Integer.valueOf(yearTV.getText().toString());
                        Calendar cal = Calendar.getInstance();
                        int year = cal.get(Calendar.YEAR) + 5;
                        if (years > 1975 && years <= year) {
                            DataHolder.getInstance().getUserDataresponse.graduation_year = String.valueOf(years);
                            handleFragmentLoading.loadingQuizDetailFragment("graduationScoreFragment");
                        } else {
                            showMessage("Please add correct year");
                        }
                    } else {
                        showMessage("Please add correct year");
                    }
                }
            }
        });

        if (DataHolder.getInstance().getUserProfileDataresponse != null && DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null
                && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationYear != null) {
            yearTV.setText(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationYear);
        }
        yearTV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable p0) {
                if (p0.toString().trim().length() == 4) {
                    continueGraduuationYear.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_dark_black_btn));
                    btnEnable = true;
                } else {
                    continueGraduuationYear.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_greish_btn_disable));
                    btnEnable = false;
                }
            }
        });
    }


}