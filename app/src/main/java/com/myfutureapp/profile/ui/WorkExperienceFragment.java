package com.myfutureapp.profile.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.myfutureapp.R;
import com.myfutureapp.login.DataHolder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkExperienceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkExperienceFragment extends Fragment {

    private static HandleFragmentLoading handleFragmentLoading;
    private View view;

    public WorkExperienceFragment() {
        // Required empty public constructor
    }

    public static WorkExperienceFragment newInstance(HandleFragmentLoading handleFragmentLoadings) {
        WorkExperienceFragment fragment = new WorkExperienceFragment();
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
            view = inflater.inflate(R.layout.fragment_work_experience, container, false);
            inUi(view);
            return view;
        }
        return view;
    }

    private void inUi(View view) {
        LinearLayout yesWork = view.findViewById(R.id.yesWork);
        LinearLayout noWork = view.findViewById(R.id.noWork);
        TextView yesTv = view.findViewById(R.id.yesTV);
        TextView NOTV = view.findViewById(R.id.NOTV);
        SeekBar seekBar = view.findViewById(R.id.seekBar);
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        yesWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yesWork.setBackground(getActivity().getResources().getDrawable(R.drawable.orange_btn));
                yesTv.setTextColor(getActivity().getResources().getColor(R.color.white));
                NOTV.setTextColor(getActivity().getResources().getColor(R.color.warm_gray));
                noWork.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_btn));
                handleFragmentLoading.loadingQuizDetailFragment("workYearExperienceFragment");
            }
        });
        noWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yesWork.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_btn));
                noWork.setBackground(getActivity().getResources().getDrawable(R.drawable.orange_btn));
                yesTv.setTextColor(getActivity().getResources().getColor(R.color.warm_gray));
                NOTV.setTextColor(getActivity().getResources().getColor(R.color.white));
                DataHolder.getInstance().getUserDataresponse.work_experience = "fresher";
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
        });
    }
}