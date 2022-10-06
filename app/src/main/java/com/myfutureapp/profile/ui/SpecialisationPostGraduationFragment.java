package com.myfutureapp.profile.ui;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myfutureapp.R;
import com.myfutureapp.login.DataHolder;
import com.myfutureapp.profile.adapter.SpecialisationGraduationAdapter;
import com.myfutureapp.profile.model.SpecialisationGraduationModel;
import com.myfutureapp.util.Helper;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SpecialisationPostGraduationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpecialisationPostGraduationFragment extends Fragment {

    private static HandleFragmentLoading handleFragmentLoading;
    private static ArrayList<String> specializations;
    private BroadcastReceiver mReceiver;
    private RecyclerView specialisationPostGraducationRV;
    private TextView postCourseName;
    private View view;

    public SpecialisationPostGraduationFragment() {
        // Required empty public constructor
    }

    public static SpecialisationPostGraduationFragment newInstance(HandleFragmentLoading handleFragmentLoadings, ArrayList<String> specialization) {
        SpecialisationPostGraduationFragment fragment = new SpecialisationPostGraduationFragment();
        handleFragmentLoading = handleFragmentLoadings;
        specializations = specialization;
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
            view = inflater.inflate(R.layout.fragment_specialisation_post_graduation, container, false);
            inUi(view);
            return view;
        }
        return view;
    }

    private void inUi(View view) {
        postCourseName = view.findViewById(R.id.postCourseName);
        specialisationPostGraducationRV = view.findViewById(R.id.specialisationPostGraducationRV);
        specialisationPostGraducationRV.setNestedScrollingEnabled(false);
        specialisationPostGraducationRV.setHasFixedSize(true);
        specialisationPostGraducationRV.setLayoutManager(new LinearLayoutManager(getContext()));
        specialisationPostGraducationRV.setNestedScrollingEnabled(false);
        specialisationPostGraducationRV.setItemAnimator(new DefaultItemAnimator());
        SeekBar seekBar = view.findViewById(R.id.seekBar);
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        settingSpecializationData();
    }


    private void settingSpecializationData() {
        if (Helper.isContainValue(DataHolder.getInstance().getUserDataresponse.pg_degree)) {
            postCourseName.setText(DataHolder.getInstance().getUserDataresponse.pg_degree);
        }
        List<SpecialisationGraduationModel> specialisationGraduationModels = new ArrayList<>();
        for (int i = 0; i < specializations.size(); i++) {
            specialisationGraduationModels.add(new SpecialisationGraduationModel(specializations.get(i), false));
        }
        SpecialisationGraduationAdapter specialisationGraduationAdapter = new SpecialisationGraduationAdapter(getContext(), specialisationGraduationModels, new SpecialisationGraduationAdapter.SpecialisationCourseSelected() {
            @Override
            public void courseChoosen(String subcourseName) {
                if (Helper.isContainValue(subcourseName)) {
                    DataHolder.getInstance().getUserDataresponse.pg_specialisation = subcourseName;
                    handleFragmentLoading.loadingQuizDetailFragment("postGraduationYearFragment");
                } else {
                    showMessage("Please Select Sub-Course");
                }
            }
        });
        specialisationPostGraducationRV.setAdapter(specialisationGraduationAdapter);
        if (DataHolder.getInstance().getUserProfileDataresponse != null && DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgSpecialisation != null) {
            for (int i = 0; i < specialisationGraduationModels.size(); i++) {
                if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgSpecialisation.equalsIgnoreCase(specialisationGraduationModels.get(i).courseName)) {
                    specialisationGraduationAdapter.setPoistionSelected(i);
                    break;
                }
            }
        }

    }


    @Override
    public void onDestroyView() {
        if (mReceiver != null) {
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReceiver);
        }
        super.onDestroyView();
    }

    private void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}