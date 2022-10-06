package com.myfutureapp.dashboard.application.ui;

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
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.myfutureapp.R;
import com.myfutureapp.dashboard.application.presenter.OnGoingPresenter;
import com.myfutureapp.dashboard.home.adapter.OngoingApplicationAdapter;
import com.myfutureapp.dashboard.home.model.OngoingApplicationsResponse;
import com.myfutureapp.jobDetail.JobDetailActivity;
import com.myfutureapp.jobDetail.model.InterviewTimeSlotsResponse;
import com.myfutureapp.jobDetail.model.ScheduleInterviewResponse;
import com.myfutureapp.login.LoginActivity;
import com.myfutureapp.util.AppConstants;
import com.myfutureapp.util.AppPreferences;
import com.myfutureapp.util.Helper;

import java.util.ArrayList;
import java.util.List;

public class OnGoingFragment extends Fragment implements OnGoingView {

    private RecyclerView id_ongoing_applications;
    private boolean dataLoaded = false;
    private int page = 0;
    private OngoingApplicationAdapter ongoingApplicationAdapter;
    private OnGoingPresenter onGoingPresenter;
    private BottomSheetDialog scheduleBottomDialog;
    private TextView noOngoingApplication;

    private BroadcastReceiver mReceiver;
    private ShimmerFrameLayout shimmerFrameLayout;

    public OnGoingFragment() {
    }

    public static OnGoingFragment newInstance(String param1, String param2) {
        OnGoingFragment fragment = new OnGoingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_on_going, container, false);
        inUi(view);
        return view;

    }

    private void receiverRegister() {

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                page = 0;
                onGoingPresenter.callOngoingApplicationsApi(page);
            }
        };
        IntentFilter intentFilter1 = new IntentFilter(getString(R.string.app_name) + "user.ongoing.refresh");
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, intentFilter1);
    }

    private void inUi(View view) {
        onGoingPresenter = new OnGoingPresenter(getContext(), OnGoingFragment.this);
        noOngoingApplication = view.findViewById(R.id.noOngoingApplication);
        id_ongoing_applications = view.findViewById(R.id.id_ongoing_applications);
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout);
        id_ongoing_applications.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        id_ongoing_applications.setLayoutManager(linearLayoutManager);
        id_ongoing_applications.setItemAnimator(new DefaultItemAnimator());
        id_ongoing_applications.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) { // only when scrolling up
                    final int visibleThreshold = 4;
                    int lastItem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                    int currentTotalCount = linearLayoutManager.getItemCount();
                    if (currentTotalCount <= lastItem + visibleThreshold && dataLoaded && page != -1) {
                        if (Helper.isNetworkAvailable(getContext())) {
                            dataLoaded = false;
                            onGoingPresenter.callOngoingApplicationsApi(page);
                        }
                    }
                }
            }
        });

        ongoingApplicationAdapter = new OngoingApplicationAdapter(getActivity(), new ArrayList<>(), new OngoingApplicationAdapter.JobSelectedForDetail() {
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
                    onGoingPresenter.callGetTimeSlotslApi(jobId);
                }
            }
        });
        id_ongoing_applications.setAdapter(ongoingApplicationAdapter);
        LinearLayout loginLL = view.findViewById(R.id.loginLL);
        loginLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), LoginActivity.class).addFlags(Intent.
                        FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });
        if (!AppPreferences.getInstance(getContext()).getPreferencesString(AppConstants.AUTH).equalsIgnoreCase("true")) {
            loginLL.setVisibility(View.VISIBLE);
            id_ongoing_applications.setVisibility(View.GONE);
            noOngoingApplication.setVisibility(View.GONE);
            shimmerFrameLayout.setVisibility(View.GONE);
        } else {

            shimmerFrameLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.startShimmer();
            onGoingPresenter.callOngoingApplicationsApi(page);
            loginLL.setVisibility(View.GONE);
        }
        receiverRegister();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setWhiteNavigationBar(@NonNull Dialog dialog) {
        Window window = dialog.getWindow();
        if (window != null) {
            DisplayMetrics metrics = new DisplayMetrics();
            window.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            GradientDrawable dimDrawable = new GradientDrawable();
            // ...customize your dim effect here

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
        scheduleBottomDialog = new BottomSheetDialog(getContext()/*, R.style.DialogStyle*/);
        scheduleBottomDialog.setContentView(view/*, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height-400)*/);
        scheduleBottomDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        scheduleBottomDialog.getBehavior().setHideable(true);
        scheduleBottomDialog.setCancelable(false);
        scheduleBottomDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Spinner interviewTimeSpinner = (Spinner) view.findViewById(R.id.interviewTimeSpinner);
        TextView scheduleNow = (TextView) view.findViewById(R.id.scheduleNow);
        TextView scheduleLater = (TextView) view.findViewById(R.id.scheduleLater);


        scheduleLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scheduleBottomDialog.dismiss();
            }
        });

        scheduleNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = interviewTimeSpinner.getSelectedItem().toString();
                onGoingPresenter.callScheduleInterviewApi(s, jobId);
            }
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
    public void setOngoingApplicationsResponse(OngoingApplicationsResponse jobsResponse) {
        if (jobsResponse != null && jobsResponse.success) {
            if (page == 0) {
                if (jobsResponse.data.size() == 0) {
                    noOngoingApplication.setVisibility(View.VISIBLE);
                    id_ongoing_applications.setVisibility(View.GONE);
                } else {
                    ongoingApplicationAdapter.notifyItem(jobsResponse.data);
                    noOngoingApplication.setVisibility(View.GONE);
                    id_ongoing_applications.setVisibility(View.VISIBLE);
                }
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
            } else {
                ongoingApplicationAdapter.addMoreData(jobsResponse.data);
            }
            if (jobsResponse.data.size() == 10) {
                page = page + 1;
                dataLoaded = true;
            } else {
                page = -1;
                dataLoaded = false;
            }
        }
    }


    @Override
    public void setScheduleInterviewResponse(ScheduleInterviewResponse scheduleInterviewResponse) {
        if (scheduleInterviewResponse != null && scheduleInterviewResponse.success) {
            scheduleBottomDialog.dismiss();
            showMessage(scheduleInterviewResponse.message);
//            page = 0;
//            onGoingPresenter.callOngoingApplicationsApi(page);
            Intent intent = new Intent();
            intent.setAction(getString(R.string.app_name) + "user.ongoing.refresh");
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);


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

    }
}