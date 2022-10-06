package com.myfutureapp.profile.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.myfutureapp.R;
import com.myfutureapp.login.DataHolder;


public class GenderFragment extends Fragment {

    private static HandleFragmentLoading handleFragmentLoading;
    private String genderSelected;
    private ImageView maleGender, femaleGender, otherGender;
    private LinearLayout maleLL, femaleLL, otherLL;
    private View view;

    public GenderFragment() {
        // Required empty public constructor
    }

    public static GenderFragment newInstance(HandleFragmentLoading handleFragmentLoadings) {
        GenderFragment fragment = new GenderFragment();
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
            view = inflater.inflate(R.layout.fragment_gender, container, false);
            inUi(view);
            return view;
        }
        return view;
    }


    private void inUi(View view) {
        maleGender = view.findViewById(R.id.maleGender);
        femaleGender = view.findViewById(R.id.femaleGender);
        otherGender = view.findViewById(R.id.otherGender);
        maleLL = view.findViewById(R.id.maleLL);
        femaleLL = view.findViewById(R.id.femaleLL);
        otherLL = view.findViewById(R.id.otherLL);

        maleLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionPerformed("male");
            }
        });
        femaleLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionPerformed("female");

            }
        });
        otherLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionPerformed("other");

            }
        });

      /*  if (DataHolder.getInstance().getUserProfileDataresponse != null && DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.gender != null) {
            genderSelected=DataHolder.getInstance().getUserProfileDataresponse.data.gender;
            if (genderSelected.equalsIgnoreCase("male")) {
                maleGender.setImageDrawable(getResources().getDrawable(R.drawable.ic_male_selected));
                maleLL.setBackground(getContext().getResources().getDrawable(R.drawable.male_bg_selected));
                femaleLL.setBackgroundColor(getResources().getColor(R.color.white));
                femaleGender.setImageDrawable(getResources().getDrawable(R.drawable.ic_female_unselected));
                otherLL.setBackgroundColor(getResources().getColor(R.color.white));
                otherGender.setImageDrawable(getResources().getDrawable(R.drawable.ic_other_unselected));

            } else if (genderSelected.equalsIgnoreCase("female")) {
                femaleGender.setImageDrawable(getResources().getDrawable(R.drawable.ic_female_selected));
                femaleLL.setBackground(getContext().getResources().getDrawable(R.drawable.female_bg_selected));
                maleLL.setBackgroundColor(getResources().getColor(R.color.white));
                maleGender.setImageDrawable(getResources().getDrawable(R.drawable.ic_male_unselected));
                otherLL.setBackgroundColor(getResources().getColor(R.color.white));
                otherGender.setImageDrawable(getResources().getDrawable(R.drawable.ic_other_unselected));
            } else if (genderSelected.equalsIgnoreCase("other")) {
                otherLL.setBackground(getResources().getDrawable(R.drawable.other_bg_selected));
                otherGender.setImageDrawable(getResources().getDrawable(R.drawable.ic_other_selected));
                femaleLL.setBackgroundColor(getResources().getColor(R.color.white));
                femaleGender.setImageDrawable(getResources().getDrawable(R.drawable.ic_female_unselected));
                maleLL.setBackgroundColor(getResources().getColor(R.color.white));
                maleGender.setImageDrawable(getResources().getDrawable(R.drawable.ic_male_unselected));
            }
        }
 */
    }

    private void actionPerformed(String genderSelected) {
        if (genderSelected.equalsIgnoreCase("male")) {
            maleGender.setImageDrawable(getResources().getDrawable(R.drawable.ic_male_selected));
            maleLL.setBackground(getContext().getResources().getDrawable(R.drawable.male_bg_selected));
            femaleLL.setBackgroundColor(getResources().getColor(R.color.white));
            femaleGender.setImageDrawable(getResources().getDrawable(R.drawable.ic_female_unselected));
            otherLL.setBackgroundColor(getResources().getColor(R.color.white));
            otherGender.setImageDrawable(getResources().getDrawable(R.drawable.ic_other_unselected));

        } else if (genderSelected.equalsIgnoreCase("female")) {
            femaleGender.setImageDrawable(getResources().getDrawable(R.drawable.ic_female_selected));
            femaleLL.setBackground(getContext().getResources().getDrawable(R.drawable.female_bg_selected));
            maleLL.setBackgroundColor(getResources().getColor(R.color.white));
            maleGender.setImageDrawable(getResources().getDrawable(R.drawable.ic_male_unselected));
            otherLL.setBackgroundColor(getResources().getColor(R.color.white));
            otherGender.setImageDrawable(getResources().getDrawable(R.drawable.ic_other_unselected));
        } else if (genderSelected.equalsIgnoreCase("other")) {
            otherLL.setBackground(getResources().getDrawable(R.drawable.other_bg_selected));
            otherGender.setImageDrawable(getResources().getDrawable(R.drawable.ic_other_selected));
            femaleLL.setBackgroundColor(getResources().getColor(R.color.white));
            femaleGender.setImageDrawable(getResources().getDrawable(R.drawable.ic_female_unselected));
            maleLL.setBackgroundColor(getResources().getColor(R.color.white));
            maleGender.setImageDrawable(getResources().getDrawable(R.drawable.ic_male_unselected));
        }
        DataHolder.getInstance().getUserDataresponse.gender = genderSelected;
        handleFragmentLoading.loadingQuizDetailFragment("locationFragment");

    }
}