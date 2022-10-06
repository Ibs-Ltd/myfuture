package com.myfutureapp.profile.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.myfutureapp.R;
import com.myfutureapp.login.DataHolder;


public class WorkYearExperienceFragment extends Fragment {

    private static HandleFragmentLoading handleFragmentLoading;
    private View view;
    private TextView year0_1, year1_2, year2_4, year4_5, year_5;

    public WorkYearExperienceFragment() {
        // Required empty public constructor
    }

    public static WorkYearExperienceFragment newInstance(HandleFragmentLoading handleFragmentLoadings) {
        WorkYearExperienceFragment fragment = new WorkYearExperienceFragment();
        handleFragmentLoading = handleFragmentLoadings;
        return fragment;
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
            view = inflater.inflate(R.layout.fragment_work_year_experience, container, false);
            inUi(view);
            return view;
        }
        return view;
    }

    private void inUi(View view) {
        year0_1 = view.findViewById(R.id.year0_1);
        year1_2 = view.findViewById(R.id.year1_2);
        year2_4 = view.findViewById(R.id.year2_4);
        year4_5 = view.findViewById(R.id.year4_5);
        year_5 = view.findViewById(R.id.year_5);
        SeekBar seekBar = view.findViewById(R.id.seekBar);
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        year0_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year0_1.setBackground(getActivity().getResources().getDrawable(R.drawable.orange_btn));
                year1_2.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_btn));
                year2_4.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_btn));
                year4_5.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_btn));
                year_5.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_btn));
                year0_1.setTextColor(getActivity().getResources().getColor(R.color.white));
                year1_2.setTextColor(getActivity().getResources().getColor(R.color.warm_gray));
                year2_4.setTextColor(getActivity().getResources().getColor(R.color.warm_gray));
                year4_5.setTextColor(getActivity().getResources().getColor(R.color.warm_gray));
                year_5.setTextColor(getActivity().getResources().getColor(R.color.warm_gray));
//                NOTV.setTextColor(getActivity().getResources().getColor(R.color.warm_gray));

                setExperienceValue("0-1");
            }
        });
        year1_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                year0_1.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_btn));
                year1_2.setBackground(getActivity().getResources().getDrawable(R.drawable.orange_btn));
                year2_4.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_btn));
                year4_5.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_btn));
                year_5.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_btn));
                year0_1.setTextColor(getActivity().getResources().getColor(R.color.warm_gray));
                year1_2.setTextColor(getActivity().getResources().getColor(R.color.white));
                year2_4.setTextColor(getActivity().getResources().getColor(R.color.warm_gray));
                year4_5.setTextColor(getActivity().getResources().getColor(R.color.warm_gray));
                year_5.setTextColor(getActivity().getResources().getColor(R.color.warm_gray));
                setExperienceValue("1-2");
            }
        });
        year2_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year0_1.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_btn));
                year1_2.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_btn));
                year2_4.setBackground(getActivity().getResources().getDrawable(R.drawable.orange_btn));
                year4_5.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_btn));
                year_5.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_btn));
                year0_1.setTextColor(getActivity().getResources().getColor(R.color.warm_gray));
                year1_2.setTextColor(getActivity().getResources().getColor(R.color.warm_gray));
                year2_4.setTextColor(getActivity().getResources().getColor(R.color.white));
                year4_5.setTextColor(getActivity().getResources().getColor(R.color.warm_gray));
                year_5.setTextColor(getActivity().getResources().getColor(R.color.warm_gray));
                setExperienceValue("2-4");
            }
        });
        year4_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                year0_1.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_btn));
                year1_2.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_btn));
                year2_4.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_btn));
                year4_5.setBackground(getActivity().getResources().getDrawable(R.drawable.orange_btn));
                year_5.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_btn));
                year0_1.setTextColor(getActivity().getResources().getColor(R.color.warm_gray));
                year1_2.setTextColor(getActivity().getResources().getColor(R.color.warm_gray));
                year2_4.setTextColor(getActivity().getResources().getColor(R.color.warm_gray));
                year4_5.setTextColor(getActivity().getResources().getColor(R.color.white));
                year_5.setTextColor(getActivity().getResources().getColor(R.color.warm_gray));
                setExperienceValue("4-5");
            }
        });
        year_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year0_1.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_btn));
                year1_2.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_btn));
                year2_4.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_btn));
                year4_5.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_btn));
                year_5.setBackground(getActivity().getResources().getDrawable(R.drawable.orange_btn));
                year0_1.setTextColor(getActivity().getResources().getColor(R.color.warm_gray));
                year1_2.setTextColor(getActivity().getResources().getColor(R.color.warm_gray));
                year2_4.setTextColor(getActivity().getResources().getColor(R.color.warm_gray));
                year4_5.setTextColor(getActivity().getResources().getColor(R.color.warm_gray));
                year_5.setTextColor(getActivity().getResources().getColor(R.color.white));
                setExperienceValue("+5");
            }
        });

    }

    private void setExperienceValue(String s) {
        DataHolder.getInstance().getUserDataresponse.work_experience = s;
        if (!((ProfileActivity) getActivity()).askXIIEducationDetails.equalsIgnoreCase("3")) {
            handleFragmentLoading.loadingQuizDetailFragment("gradeTwelveFragment");
        } else if (!((ProfileActivity) getActivity()).askXEducationDetails.equalsIgnoreCase("3")) {
            handleFragmentLoading.loadingQuizDetailFragment("gradeTenthFragment");
        } else if (!((ProfileActivity) getActivity()).askAdditionalInformation.equalsIgnoreCase("3")) {
            handleFragmentLoading.loadingQuizDetailFragment("additionalFragment");
        } else {
            handleFragmentLoading.loadingQuizDetailFragment("finished");
        }
    }

}