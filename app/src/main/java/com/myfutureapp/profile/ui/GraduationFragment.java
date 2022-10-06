package com.myfutureapp.profile.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myfutureapp.R;
import com.myfutureapp.login.DataHolder;
import com.myfutureapp.profile.adapter.GraduationAdapter;
import com.myfutureapp.profile.model.GraduationCourseResponse;
import com.myfutureapp.profile.presenter.GraduationFragmentPresenter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GraduationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GraduationFragment extends Fragment implements GraduationFragmentView {

    private static HandleFragmentLoading handleFragmentLoading;
    View contineView;
    TextView contineOther;
    EditText otherText;
    ScrollView scrollViewLL;
    ProgressBar pgBr;
    View view;
//    private ShimmerFrameLayout shimmerFrameLayout;
    private RecyclerView graducationRV;

    public GraduationFragment() {
        // Required empty public constructor
    }

    public static GraduationFragment newInstance(HandleFragmentLoading handleFragmentLoadings) {
        GraduationFragment fragment = new GraduationFragment();
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
            view = inflater.inflate(R.layout.fragment_graduation, container, false);
            inUi(view);
            return view;
        } else {
            return view;
        }
    }


    private void inUi(View view) {
        contineView = view.findViewById(R.id.contineView);
        contineOther = view.findViewById(R.id.contineOther);
        otherText = view.findViewById(R.id.otherText);
        scrollViewLL = view.findViewById(R.id.scrollViewLL);
        pgBr = view.findViewById(R.id.pgBr);
        GraduationFragmentPresenter graduationFragmentPresenter = new GraduationFragmentPresenter(getContext(), this);
        graducationRV = view.findViewById(R.id.graducationRV);
//        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout);
//        shimmerFrameLayout.startShimmerAnimation();
        graducationRV.setNestedScrollingEnabled(false);
        graducationRV.setHasFixedSize(true);
        GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        graducationRV.setLayoutManager(mLayoutManager);
        graducationRV.setItemAnimator(new DefaultItemAnimator());
        graduationFragmentPresenter.callGraduationCourseApi();
        SeekBar seekBar = view.findViewById(R.id.seekBar);
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        otherText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                otherText.requestFocus();
                return false;
            }
        });
        contineOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (otherText.getText().toString().length() > 0) {
                    DataHolder.getInstance().getUserDataresponse.graduation_degree = otherText.getText().toString();
                    DataHolder.getInstance().getUserDataresponse.graduation_specialisation = null;
                    handleFragmentLoading.loadingQuizDetailFragment("yearGraduationFragment");
                } else {
                    showMessage("please enter course name");
                }

            }
        });

    }


    @Override
    public void setGraduationCourseResponse(GraduationCourseResponse graduationCourseResponse) {
        if (graduationCourseResponse != null && graduationCourseResponse.success) {
            pgBr.setVisibility(View.GONE);
            scrollViewLL.setVisibility(View.VISIBLE);
            GraduationAdapter graduationAdapter = new GraduationAdapter(getContext(), graduationCourseResponse.data, new GraduationAdapter.CourseSelected() {
                @Override
                public void courseChoosen(GraduationCourseResponse.GraduationCourseModel graduationCourseModel) {
                    if (graduationCourseModel.course_name.equalsIgnoreCase("other")) {
                        otherText.setVisibility(View.VISIBLE);
                        contineView.setVisibility(View.VISIBLE);
                        contineOther.setVisibility(View.VISIBLE);
                    } else {
                        otherText.setVisibility(View.GONE);
                        contineView.setVisibility(View.GONE);
                        contineOther.setVisibility(View.GONE);
                        if (graduationCourseModel.specialization.size() != 0) {
//                            settingSpecializationData(graduationCourseModel.specialization, graduationCourseModel.course_name);
//                            Intent intent = new Intent();
//                            intent.setAction(getString(R.string.app_name) + "user.graduation.specialisation");
//                            intent.putStringArrayListExtra("courseDetail", graduationCourseModel.specialization);
//                            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
                            DataHolder.getInstance().getUserDataresponse.graduation_degree = graduationCourseModel.course_name;
                            DataHolder.getInstance().getUserDataresponse.graduation_specialisation = null;
                            handleFragmentLoading.loadingQuizDetailFragmentWithSpecialisation("specialisationGraduationFragment", graduationCourseModel.specialization);
                        } else {
                            DataHolder.getInstance().getUserDataresponse.graduation_degree = graduationCourseModel.course_name;
                            DataHolder.getInstance().getUserDataresponse.graduation_specialisation = null;
                            handleFragmentLoading.loadingQuizDetailFragment("yearGraduationFragment");
                        }
                    }
                }
            });
            graducationRV.setAdapter(graduationAdapter);
//            shimmerFrameLayout.stopShimmerAnimation();
            graducationRV.setVisibility(View.VISIBLE);
//            shimmerFrameLayout.setVisibility(View.GONE);
            if (DataHolder.getInstance().getUserProfileDataresponse != null && DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationDegree != null) {
                for (int i = 0; i < graduationCourseResponse.data.size(); i++) {
                    if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationDegree.equalsIgnoreCase(graduationCourseResponse.data.get(i).course_name)) {
                        graduationAdapter.setPoistionSelected(i);
                        break;
                    }
                }
            }
        }
    }


    @Override
    public void showLoader() {

    }

    @Override
    public void hideLoader() {

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}