package com.myfutureapp.dashboard.home;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.perf.metrics.AddTrace;
import com.myfutureapp.R;
import com.myfutureapp.dashboard.DashboardActivity;
import com.myfutureapp.dashboard.home.adapter.JobTitleAdapter;
import com.myfutureapp.dashboard.home.adapter.JobsAdapter;
import com.myfutureapp.dashboard.home.adapter.OngoingApplicationAdapter;
import com.myfutureapp.dashboard.home.model.BookMarkResponse;
import com.myfutureapp.dashboard.home.model.JobTagsResponse;
import com.myfutureapp.dashboard.home.model.JobsForYouResponse;
import com.myfutureapp.dashboard.home.model.OngoingApplicationsResponse;
import com.myfutureapp.dashboard.home.presenter.HomePresenter;
import com.myfutureapp.dashboard.home.ui.HomeActivityView;
import com.myfutureapp.dashboard.model.UserProfileResponse;
import com.myfutureapp.dashboard.searchJob.SearchJobActivity;
import com.myfutureapp.jobDetail.JobDetailActivity;
import com.myfutureapp.jobDetail.model.InterviewTimeSlotsResponse;
import com.myfutureapp.jobDetail.model.ScheduleInterviewResponse;
import com.myfutureapp.login.DataHolder;
import com.myfutureapp.util.Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NewHomeFragment extends Fragment implements HomeActivityView {

    private Group onGoingGroup;
    private RecyclerView id_ongoing_applications;
    private RecyclerView id_rv_jobs;
    private RecyclerView id_rv_job_title;
    private TextView id_application_status;
    /**
     * Using this receiver to update user profile data
     */

    private final BroadcastReceiver userProfileUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshApplicationText();
        }
    };
    private TextView view_more_online_applications;
    private JobsAdapter jobsAdapter;
    private String tag_id = "0";
    private HomePresenter homePresenter;
    /**
     * Using this receiver to update ongoing application if user schedules or made any changes on
     * Ongoing Application from other page
     */

    private final BroadcastReceiver ongoingApplicationUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            homePresenter.callOngoingApplicationsApi();
        }
    };
    private boolean dataLoaded = false;
    private int page = 0;
    /**
     * Using this receiver to update jobs from other pages if users bookmark any job
     */

    private final BroadcastReceiver jobUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            page = 0;
            homePresenter.callGetJobsApi(page, tag_id);
        }
    };
    private TextView noJobs;
    private BottomSheetDialog scheduleBottomDialog;
    private ConstraintLayout constraintLayoutHome;
    private ShimmerFrameLayout shimmerFrameLayout;
    private ProgressBar progressBar;

    @Override
    @AddTrace(name = "onCreateViewTrace")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_new, container, false);
        initUi(view);
        return view;
    }

    private void receiverRegister() {
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(requireContext());

        IntentFilter intentFilter = new IntentFilter(getString(R.string.app_name) + "user.bookmark.refresh");
        manager.registerReceiver(jobUpdateReceiver, intentFilter);

        IntentFilter intentFilter1 = new IntentFilter(getString(R.string.app_name) + "user.ongoing.refresh");
        manager.registerReceiver(ongoingApplicationUpdateReceiver, intentFilter1);

        IntentFilter intentFilter2 = new IntentFilter(getString(R.string.app_name) + "user.basic.data.refresh");
        manager.registerReceiver(userProfileUpdateReceiver, intentFilter2);
    }

    private void unregisterReceivers() {
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(requireContext());
        manager.unregisterReceiver(jobUpdateReceiver);
        manager.unregisterReceiver(ongoingApplicationUpdateReceiver);
    }

    public void initUi(View view) {
        homePresenter = new HomePresenter(getActivity(), this);
        constraintLayoutHome = view.findViewById(R.id.constraintLayoutHome);
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout);
        progressBar = view.findViewById(R.id.progressBar);
        shimmerFrameLayout.startShimmer();
        onGoingGroup = view.findViewById(R.id.onGoingGroup);
        id_application_status = view.findViewById(R.id.id_application_status);
        id_rv_job_title = view.findViewById(R.id.id_rv_job_title);
        id_ongoing_applications = view.findViewById(R.id.id_ongoing_applications);
        id_rv_jobs = view.findViewById(R.id.id_rv_jobs);
        noJobs = view.findViewById(R.id.noJobs);
        TextView id_search = view.findViewById(R.id.id_search);
        view_more_online_applications = view.findViewById(R.id.view_more_online_applications);
        view_more_online_applications.setOnClickListener(v -> ((DashboardActivity) getActivity()).viewApplication());
        id_ongoing_applications.setItemAnimator(new DefaultItemAnimator());
        id_rv_jobs.setItemAnimator(new DefaultItemAnimator());
        id_rv_job_title.setItemAnimator(new DefaultItemAnimator());
        id_search.setOnClickListener(v -> startActivity(new Intent(getContext(), SearchJobActivity.class)));

        jobsAdapter = new JobsAdapter(new ArrayList<>(), new JobsAdapter.JobSelectedForDetail() {
            @Override
            public void JobDetailSelected(JobsForYouResponse.JobDetail selectedJob) {
                if (selectedJob != null) {
                    String title = String.format("%s - %s", selectedJob.company_name, selectedJob.designation);
                    Intent intent = JobDetailActivity.getIntent(requireContext(), selectedJob.id + "", title, selectedJob.compensation_show);
                    startActivity(intent);
                }
            }

            @Override
            public void JobBookMarkTask(String bookMarkType, JobsForYouResponse.JobDetail jobDetail, int position) {
                homePresenter.callBookMarkApi(bookMarkType, jobDetail, position);
            }
        });
        id_rv_jobs.setAdapter(jobsAdapter);

        NestedScrollView nestedScrollView = (NestedScrollView) view.findViewById(R.id.nestedScrollView);
        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener((ViewTreeObserver.OnScrollChangedListener) () -> {
            View view1 = (View) nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);

            int diff = (view1.getBottom() - (nestedScrollView.getHeight() + nestedScrollView
                    .getScrollY()));

            if (diff == 0 && dataLoaded && page != -1) {
                if (Helper.isNetworkAvailable(getContext())) {
                    dataLoaded = false;
                    progressBar.setVisibility(View.VISIBLE);
                    homePresenter.callGetJobsApi(page, tag_id);
                }
            }
        });
    }

    private void refreshApplicationText() {
        UserProfileResponse profileData = DataHolder.getInstance().getUserProfileDataresponse;
        if (profileData != null && profileData.data != null && profileData.data.applied_opportunity_details != null) {
            if (String.valueOf(profileData.data.applied_opportunity_details.applied_count).length() != 0) {
                int appliedCount = profileData.data.applied_opportunity_details.applied_count;
                int totalCount = profileData.data.applied_opportunity_details.total_count;
                String message = String.format(Locale.getDefault(), "Applications Left Today : %d/%d", appliedCount, totalCount);
                id_application_status.setText(message);
                id_application_status.setVisibility(View.VISIBLE);
            } else {
                id_application_status.setVisibility(View.GONE);
            }
        } else {
            id_application_status.setVisibility(View.VISIBLE);
            id_application_status.setText("Applications Left Today : 3/3");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homePresenter.callOngoingApplicationsApi();
        homePresenter.callGetTagsApi();
        homePresenter.callGetJobsApi(page, tag_id);
    }

    @Override
    public void onResume() {
        super.onResume();
        receiverRegister();
        refreshApplicationText();
    }

    @Override
    public void onDestroyView() {
        unregisterReceivers();
        super.onDestroyView();
    }

    @Override
    public void setTagsResponse(JobTagsResponse TagsResponse) {
        JobTitleAdapter jobTitleAdapter = new JobTitleAdapter(getActivity(), TagsResponse.data, Tag_id -> {
            page = 0;
            tag_id = Tag_id;
            homePresenter.callGetJobsApi(page, Tag_id);
        });
        id_rv_job_title.setAdapter(jobTitleAdapter);
    }

    @Override
    public void setJobsResponse(JobsForYouResponse jobsResponse) {
        if (jobsResponse != null && jobsResponse.success) {
            if (page == 0) {

                if (jobsResponse.data.size() == 0) {
                    noJobs.setVisibility(View.VISIBLE);
                    id_rv_jobs.setVisibility(View.GONE);
                } else {
                    noJobs.setVisibility(View.GONE);
                    id_rv_jobs.setVisibility(View.VISIBLE);
                    jobsAdapter.notifyItem(jobsResponse.data);
                }
                constraintLayoutHome.setVisibility(View.VISIBLE);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
            } else {
                jobsAdapter.addMoreData(jobsResponse.data);
            }
            if (jobsResponse.data.size() == 10) {
                page = page + 1;
                dataLoaded = true;
            } else {
                page = -1;
                dataLoaded = false;
            }

            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void setOngoingApplicationsResponse(OngoingApplicationsResponse jobsResponse) {
        if (jobsResponse != null && jobsResponse.success) {
            if (jobsResponse.data != null && jobsResponse.data.size() >= 3) {
                view_more_online_applications.setVisibility(View.VISIBLE);
            } else {
                view_more_online_applications.setVisibility(View.GONE);
            }
            if (jobsResponse.data.size() > 0) {
                OngoingApplicationAdapter ongoingApplicationAdapter = new OngoingApplicationAdapter(getActivity(), jobsResponse.data, new OngoingApplicationAdapter.JobSelectedForDetail() {
                    @Override
                    public void JobDetailSelected(OngoingApplicationsResponse.OngoingApplicationdetail selectedJob) {
                        if (selectedJob != null) {
                            String title = String.format("%s - %s", selectedJob.company_name, selectedJob.designation);
                            Intent intent = JobDetailActivity.getIntent(requireContext(), selectedJob.id + "", title, "");
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void scheduleInterviewTime(String jobId) {
                        if (jobId != null) {
                            homePresenter.callGetTimeSlotslApi(jobId);
                        }
                    }
                });
                id_ongoing_applications.setAdapter(ongoingApplicationAdapter);
                onGoingGroup.setVisibility(View.VISIBLE);
            } else {
                onGoingGroup.setVisibility(View.GONE);
            }

        } else {
            onGoingGroup.setVisibility(View.GONE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setWhiteNavigationBar(@NonNull Dialog dialog) {
        Window window = dialog.getWindow();
        if (window != null) {
            DisplayMetrics metrics = new DisplayMetrics();
            window.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            GradientDrawable dimDrawable = new GradientDrawable();

            GradientDrawable navigationBarDrawable = new GradientDrawable();
            navigationBarDrawable.setShape(GradientDrawable.RECTANGLE);
            navigationBarDrawable.setColor(Color.WHITE);

            Drawable[] layers = {dimDrawable, navigationBarDrawable};

            LayerDrawable windowBackground = new LayerDrawable(layers);
            windowBackground.setLayerInsetTop(1, metrics.heightPixels);

            window.setBackgroundDrawable(windowBackground);
        }
    }

    private void settingScheduleBottomSheet(String jobId, List<String> data) {
        View view = getLayoutInflater().inflate(R.layout.schedule_interview_bottom_sheet, null);
        scheduleBottomDialog = new BottomSheetDialog(getContext());
        scheduleBottomDialog.setContentView(view);
        scheduleBottomDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        scheduleBottomDialog.getBehavior().setHideable(true);
        scheduleBottomDialog.setCancelable(false);
        scheduleBottomDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Spinner interviewTimeSpinner = (Spinner) view.findViewById(R.id.interviewTimeSpinner);
        TextView scheduleNow = (TextView) view.findViewById(R.id.scheduleNow);
        TextView scheduleLater = (TextView) view.findViewById(R.id.scheduleLater);

        scheduleLater.setOnClickListener(view1 -> scheduleBottomDialog.dismiss());

        scheduleNow.setOnClickListener(view12 -> {
            String s = interviewTimeSpinner.getSelectedItem().toString();
            homePresenter.callScheduleInterviewApi(s, jobId);
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setWhiteNavigationBar(scheduleBottomDialog);
        }

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, data);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        interviewTimeSpinner.setAdapter(spinnerArrayAdapter);
        scheduleInterviewPopup();
    }


    private void scheduleInterviewPopup() {
        scheduleBottomDialog.show();
    }

    @Override
    public void setScheduleInterviewResponse(ScheduleInterviewResponse scheduleInterviewResponse) {
        if (scheduleInterviewResponse != null && scheduleInterviewResponse.success) {
            scheduleBottomDialog.dismiss();
            showMessage(scheduleInterviewResponse.message);
            Intent intent = new Intent();
            intent.setAction(getString(R.string.app_name) + "user.ongoing.refresh");
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
            homePresenter.callOngoingApplicationsApi();

        }
    }

    @Override
    public void setBookMarkResponse(BookMarkResponse bookMarkResponse, String bookMarkType, int position) {
        if (bookMarkResponse != null && bookMarkResponse.success) {
            jobsAdapter.notifyItemPosition(position, Integer.parseInt(bookMarkType));
            showMessage(bookMarkResponse.message);
        }
    }

    @Override
    public void setInterviewTimeSlotsResponse(InterviewTimeSlotsResponse interviewTimeSlotsResponse, String jobId) {
        if (interviewTimeSlotsResponse != null && interviewTimeSlotsResponse.success) {
            settingScheduleBottomSheet(jobId, interviewTimeSlotsResponse.data);
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