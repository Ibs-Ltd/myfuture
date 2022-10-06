package com.myfutureapp.profile.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myfutureapp.R;
import com.myfutureapp.login.DataHolder;
import com.myfutureapp.profile.adapter.GraduationAdapter;
import com.myfutureapp.profile.model.GraduationCourseResponse;
import com.myfutureapp.profile.presenter.PostGraduationFragmentPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostGraduationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostGraduationFragment extends Fragment implements PostGraduationFragmentView {

    private static HandleFragmentLoading handleFragmentLoading;
    View contineView;
    TextView contineOther;
    EditText otherText;
    ScrollView scrollViewLL;
    ProgressBar pgBr;
    GraduationAdapter graduationAdapter;
    List<GraduationCourseResponse.GraduationCourseModel> courseModels;
    private RecyclerView postGraducationRV;
    private LinearLayout notDoneYet;
    private TextView notDoneTV;
    private View view;

    public PostGraduationFragment() {
        // Required empty public constructor
    }

    public static PostGraduationFragment newInstance(HandleFragmentLoading handleFragmentLoadings) {
        PostGraduationFragment fragment = new PostGraduationFragment();
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
            view = inflater.inflate(R.layout.fragment_post_graduation, container, false);
            inUi(view);
            return view;
        }
        return view;
    }

    private void inUi(View view) {
        PostGraduationFragmentPresenter postGraduationFragmentPresenter = new PostGraduationFragmentPresenter(getContext(), this);
        SeekBar seekBar = view.findViewById(R.id.seekBar);
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        scrollViewLL = view.findViewById(R.id.scrollViewLL);
        pgBr = view.findViewById(R.id.pgBr);
        notDoneTV = view.findViewById(R.id.notDoneTV);
        postGraducationRV = view.findViewById(R.id.postGraducationRV);
        postGraducationRV.setNestedScrollingEnabled(false);
        postGraducationRV.setHasFixedSize(true);
        GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        postGraducationRV.setLayoutManager(mLayoutManager);
        postGraducationRV.setItemAnimator(new DefaultItemAnimator());
        postGraduationFragmentPresenter.callPostGraduationCourseApi();

        contineView = view.findViewById(R.id.contineView);
        contineOther = view.findViewById(R.id.contineOther);
        otherText = view.findViewById(R.id.otherText);
        notDoneYet = view.findViewById(R.id.notDoneYet);
        otherText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                otherText.requestFocus();
                return false;
            }
        });
        notDoneYet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otherText.setVisibility(View.GONE);
                contineView.setVisibility(View.GONE);
                contineOther.setVisibility(View.GONE);
                notDoneYet.setBackground(getContext().getResources().getDrawable(R.drawable.orange_btn));
                notDoneTV.setTextColor(getContext().getResources().getColor(R.color.white));
                for (int i = 0; i < courseModels.size(); i++) {
                    courseModels.get(i).isSelected = false;
                }
                graduationAdapter.notifyItem(courseModels);
                DataHolder.getInstance().getUserDataresponse.pg_degree = "None";
                DataHolder.getInstance().getUserDataresponse.pg_specialisation = null;
                DataHolder.getInstance().getUserDataresponse.pg_year = null;
                DataHolder.getInstance().getUserDataresponse.pg_gpa_max = null;
                DataHolder.getInstance().getUserDataresponse.pg_percentage = null;
                DataHolder.getInstance().getUserDataresponse.pg_gpa = null;
                handleFragmentLoading.loadingQuizDetailFragment("workExperienceFragment");
            }
        });

        contineOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (otherText.getText().toString().length() > 0) {
                    DataHolder.getInstance().getUserDataresponse.pg_degree = otherText.getText().toString();
                    DataHolder.getInstance().getUserDataresponse.pg_specialisation = null;
                    handleFragmentLoading.loadingQuizDetailFragment("postGraduationYearFragment");
                } else {
                    showMessage("enter course name");
                }

            }
        });


    }

    @Override
    public void setPostGraduationCourseResponse(GraduationCourseResponse graduationCourseResponse) {
        if (graduationCourseResponse != null && graduationCourseResponse.success) {
            pgBr.setVisibility(View.GONE);
            scrollViewLL.setVisibility(View.VISIBLE);
            courseModels = new ArrayList<>();
            for (int i = 0; i < graduationCourseResponse.data.size(); i++) {
                if (graduationCourseResponse.data.get(i).course_name.equalsIgnoreCase("Not Done Yet")) {
                    notDoneYet.setVisibility(View.VISIBLE);
                } else {
                    courseModels.add(graduationCourseResponse.data.get(i));
                }
            }
            graduationAdapter = new GraduationAdapter(getContext(), courseModels, new GraduationAdapter.CourseSelected() {
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
                            Intent intent = new Intent();
                            intent.setAction(getString(R.string.app_name) + "user.postGraduation.specialisation");
                            intent.putStringArrayListExtra("courseDetail", graduationCourseModel.specialization);
                            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
                            DataHolder.getInstance().getUserDataresponse.pg_degree = graduationCourseModel.course_name;
                            DataHolder.getInstance().getUserDataresponse.pg_specialisation = null;
                            handleFragmentLoading.loadingQuizDetailFragmentWithSpecialisation("specialisationPostGraduationFragment", graduationCourseModel.specialization);
                        } else {
                            DataHolder.getInstance().getUserDataresponse.pg_degree = graduationCourseModel.course_name;
                            DataHolder.getInstance().getUserDataresponse.pg_specialisation = null;
                            handleFragmentLoading.loadingQuizDetailFragment("postGraduationYearFragment");
                        }
                    }
                    notDoneTV.setTextColor(getContext().getResources().getColor(R.color.warm_gray));
                    notDoneYet.setBackground(getContext().getResources().getDrawable(R.drawable.grey_btn));
                }

            });
            postGraducationRV.setAdapter(graduationAdapter);
            postGraducationRV.setVisibility(View.VISIBLE);

            if (DataHolder.getInstance().getUserProfileDataresponse != null && DataHolder.getInstance().getUserProfileDataresponse.data != null
                    && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null
                    && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgDegree != null
                    && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgDegree.length() > 0) {
                if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgDegree.equalsIgnoreCase("None")) {
                    notDoneYet.setBackground(getContext().getResources().getDrawable(R.drawable.orange_btn));
                    notDoneTV.setTextColor(getContext().getResources().getColor(R.color.white));
                    for (int i = 0; i < courseModels.size(); i++) {
                        courseModels.get(i).isSelected = false;
                    }
                    graduationAdapter.notifyItem(courseModels);
                }
                for (int i = 0; i < graduationCourseResponse.data.size(); i++) {
                    if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgDegree.equalsIgnoreCase(graduationCourseResponse.data.get(i).course_name)) {
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

    }
}