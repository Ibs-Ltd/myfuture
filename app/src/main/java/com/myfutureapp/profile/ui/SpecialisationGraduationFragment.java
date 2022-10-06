package com.myfutureapp.profile.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
 * Use the {@link SpecialisationGraduationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpecialisationGraduationFragment extends Fragment {

    private static HandleFragmentLoading handleFragmentLoading;
    private static ArrayList<String> specializations;
    private BroadcastReceiver mReceiver;
    private RecyclerView specialisationGraducationRV;
    private TextView courseName;
    private View view;

    public SpecialisationGraduationFragment() {
        // Required empty public constructor
    }

    public static SpecialisationGraduationFragment newInstance(HandleFragmentLoading handleFragmentLoadings, ArrayList<String> specialization) {
        SpecialisationGraduationFragment fragment = new SpecialisationGraduationFragment();
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
            view = inflater.inflate(R.layout.fragment_specialisation_graduation, container, false);
            inUi(view);
//        receiverRegister();
            return view;
        }
        return view;
    }

    private void inUi(View view) {
        courseName = view.findViewById(R.id.courseName);

        specialisationGraducationRV = view.findViewById(R.id.specialisationGraducationRV);
        specialisationGraducationRV.setNestedScrollingEnabled(false);
        specialisationGraducationRV.setHasFixedSize(true);
        specialisationGraducationRV.setLayoutManager(new LinearLayoutManager(getContext()));
        specialisationGraducationRV.setItemAnimator(new DefaultItemAnimator());
        specialisationGraducationRV.setNestedScrollingEnabled(false);
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
        if (Helper.isContainValue(DataHolder.getInstance().getUserDataresponse.graduation_degree)) {
            courseName.setText(DataHolder.getInstance().getUserDataresponse.graduation_degree);
        }
        List<SpecialisationGraduationModel> specialisationGraduationModels = new ArrayList<>();
        for (int i = 0; i < specializations.size(); i++) {
            specialisationGraduationModels.add(new SpecialisationGraduationModel(specializations.get(i), false));
        }
        SpecialisationGraduationAdapter specialisationGraduationAdapter = new SpecialisationGraduationAdapter(getContext(), specialisationGraduationModels, new SpecialisationGraduationAdapter.SpecialisationCourseSelected() {
            @Override
            public void courseChoosen(String subcourseName) {
                if (Helper.isContainValue(subcourseName)) {
                    DataHolder.getInstance().getUserDataresponse.graduation_specialisation = subcourseName;
                    handleFragmentLoading.loadingQuizDetailFragment("yearGraduationFragment");
                } else {
                    showMessage("Please Select Sub-Course");
                }

            }
        });
        specialisationGraducationRV.setAdapter(specialisationGraduationAdapter);
        if (DataHolder.getInstance().getUserProfileDataresponse != null && DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationSpecialisation != null) {
            for (int i = 0; i < specialisationGraduationModels.size(); i++) {
                if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationSpecialisation.equalsIgnoreCase(specialisationGraduationModels.get(i).courseName)) {
                    specialisationGraduationAdapter.setPoistionSelected(i);
                    break;
                }
            }
        }
    }


    private void receiverRegister() {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Helper.isContainValue(DataHolder.getInstance().getUserDataresponse.graduation_degree)) {
                    courseName.setText(DataHolder.getInstance().getUserDataresponse.graduation_degree);
                }
                ArrayList<String> courseDetail = intent.getStringArrayListExtra("courseDetail");
                List<SpecialisationGraduationModel> specialisationGraduationModels = new ArrayList<>();
                for (int i = 0; i < courseDetail.size(); i++) {
                    specialisationGraduationModels.add(new SpecialisationGraduationModel(courseDetail.get(i), false));
                }
                SpecialisationGraduationAdapter specialisationGraduationAdapter = new SpecialisationGraduationAdapter(getContext(), specialisationGraduationModels, new SpecialisationGraduationAdapter.SpecialisationCourseSelected() {
                    @Override
                    public void courseChoosen(String subcourseName) {
                        if (Helper.isContainValue(subcourseName)) {
                            DataHolder.getInstance().getUserDataresponse.graduation_specialisation = subcourseName;
                            handleFragmentLoading.loadingQuizDetailFragment("yearGraduationFragment");
                        } else {
                            showMessage("Please Select Sub-Course");
                        }

                    }
                });
                specialisationGraducationRV.setAdapter(specialisationGraduationAdapter);
                if (DataHolder.getInstance().getUserProfileDataresponse != null && DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationSpecialisation != null) {
                    for (int i = 0; i < specialisationGraduationModels.size(); i++) {
                        if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationSpecialisation.equalsIgnoreCase(specialisationGraduationModels.get(i).courseName)) {
                            specialisationGraduationAdapter.setPoistionSelected(i);
                            break;
                        }
                    }
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter(getString(R.string.app_name) + "user.graduation.specialisation");
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, intentFilter);
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