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
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.myfutureapp.dashboard.searchJob.SearchJobActivity;
import com.myfutureapp.jobDetail.JobDetailActivity;
import com.myfutureapp.jobDetail.model.InterviewTimeSlotsResponse;
import com.myfutureapp.jobDetail.model.ScheduleInterviewResponse;
import com.myfutureapp.login.DataHolder;
import com.myfutureapp.profile.ui.ProfileActivity;
import com.myfutureapp.util.Helper;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements HomeActivityView {

    RecyclerView id_rv_job_title;
    JobTitleAdapter jobTitleAdapter;
    LinearLayout ongoingApplicationLL;
    OngoingApplicationAdapter ongoingApplicationAdapter;
    RecyclerView id_ongoing_applications;
    RecyclerView id_rv_jobs;
    TextView id_application_status;
    TextView view_more_jobs;
    TextView view_more_online_applications;
    JobsAdapter jobsAdapter;
    String tag_id = "0";
    private HomePresenter homePresenter;
    private boolean dataLoaded = false;
    private int page = 0;
    private TextView noJobs;
    private BottomSheetDialog scheduleBottomDialog;
    private BroadcastReceiver mReceiver, mReceiver1;
    private LinearLayout mainHomeLL;
    private ShimmerFrameLayout shimmerFrameLayout;


    public HomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    @AddTrace(name = "onCreateViewTrace", enabled = true /* optional */)
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        InitUi(view);

        return view;
    }


    private void receiverRegister() {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                page = 0;
                homePresenter.callGetJobsApi(page, tag_id);
            }
        };
        IntentFilter intentFilter = new IntentFilter(getString(R.string.app_name) + "user.bookmark.refresh");
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, intentFilter);

        mReceiver1 = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                homePresenter.callOngoingApplicationsApi();
            }
        };
        IntentFilter intentFilter1 = new IntentFilter(getString(R.string.app_name) + "user.ongoing.refresh");
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver1, intentFilter1);
    }

    public void InitUi(View view) {
        homePresenter = new HomePresenter(getActivity(), this);
        mainHomeLL = view.findViewById(R.id.mainHomeLL);
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout);
        shimmerFrameLayout.startShimmer();
        ongoingApplicationLL = view.findViewById(R.id.ongoingApplicationLL);
        id_application_status = view.findViewById(R.id.id_application_status);
        id_rv_job_title = view.findViewById(R.id.id_rv_job_title);
        id_ongoing_applications = view.findViewById(R.id.id_ongoing_applications);
        id_rv_jobs = view.findViewById(R.id.id_rv_jobs);
        noJobs = view.findViewById(R.id.noJobs);
        TextView id_search = view.findViewById(R.id.id_search);

        if (DataHolder.getInstance().getUserProfileDataresponse != null && DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.applied_opportunity_details != null) {
            if (String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.applied_opportunity_details.applied_count).length() != 0) {
                id_application_status.setText("Applications Left Today : " + DataHolder.getInstance().getUserProfileDataresponse.data.applied_opportunity_details.applied_count
                        + "/" + DataHolder.getInstance().getUserProfileDataresponse.data.applied_opportunity_details.total_count);
                id_application_status.setVisibility(View.VISIBLE);
            } else {
                id_application_status.setVisibility(View.GONE);
            }
        } else {
            id_application_status.setVisibility(View.VISIBLE);
            id_application_status.setText("Applications Left Today : 3/3");
        }
        view_more_online_applications = view.findViewById(R.id.view_more_online_applications);
        view_more_online_applications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DashboardActivity) getActivity()).viewApplication();
            }
        });

        id_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SearchJobActivity.class));
            }
        });
        id_rv_job_title.setHasFixedSize(true);
        id_rv_job_title.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        id_rv_job_title.setItemAnimator(new DefaultItemAnimator());
        id_rv_job_title.setNestedScrollingEnabled(false);

        id_ongoing_applications.setHasFixedSize(false);
        id_ongoing_applications.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        id_ongoing_applications.setItemAnimator(new DefaultItemAnimator());
        id_ongoing_applications.setNestedScrollingEnabled(false);
        id_ongoing_applications.setOverScrollMode(View.OVER_SCROLL_NEVER);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        id_rv_jobs.setHasFixedSize(true);
        id_rv_jobs.setLayoutManager(linearLayoutManager);
        id_rv_jobs.setItemAnimator(new DefaultItemAnimator());
        id_rv_jobs.setNestedScrollingEnabled(false);
        id_rv_jobs.setOverScrollMode(View.OVER_SCROLL_NEVER);
        jobsAdapter = new JobsAdapter(new ArrayList<>(), new JobsAdapter.JobSelectedForDetail() {
            @Override
            public void JobDetailSelected(JobsForYouResponse.JobDetail selectedJob) {
            /*    if (selectedJob != null) {
                                      String title = String.format("%s - %s", selectedJob.company_name, selectedJob.designation);
                    Intent intent = JobDetailActivity.getIntent(requireContext(), selectedJob.id + "", title, selectedJob.compensation_show);
                    startActivity(intent);
                }*/
                boolean[] b = {true, true, true, true};
                Intent intent = new Intent(new Intent(getActivity(), ProfileActivity.class)
                        .putExtra("fragmentToBeLoad", "")
                        .putExtra("loginRequired", "true")
                        .putExtra("jobId", "")
                        .putExtra("askBasicDetails", "1")
                        .putExtra("askGraduationDetails", "1")
                        .putExtra("askPostGraduationDetails", "1")
                        .putExtra("askXEducationDetails", "3")
                        .putExtra("askAdditionalInformation", "1")
                        .putExtra("askAdditionalList", b)
                        .putExtra("askXIIEducationDetails", "3")
                        .putExtra("askWorkExperience", "3"));
                startActivity(intent);
            }

            @Override
            public void JobBookMarkTask(String bookMarkType, JobsForYouResponse.JobDetail jobDetail, int position) {
                homePresenter.callBookMarkApi(bookMarkType, jobDetail, position);
            }
        });
        id_rv_jobs.setAdapter(jobsAdapter);
        NestedScrollView nestedScrollView = (NestedScrollView) view.findViewById(R.id.nestedScrollView);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (oldScrollY > 0) { // only when scrolling up
                    final int visibleThreshold = 4;
                    int lastItem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                    int currentTotalCount = linearLayoutManager.getItemCount();
                    if (currentTotalCount <= lastItem + visibleThreshold && dataLoaded && page != -1) {
                        if (Helper.isNetworkAvailable(getContext())) {
                            dataLoaded = false;
//                            homeViewModel.callGetJobsApi(page, tag_id,getContext());
                            homePresenter.callGetJobsApi(page, tag_id);
                        }
                    }
                }
            }
        });
       /* id_rv_jobs.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                            homePresenter.callGetJobsApi(page, tag_id);
                        }
                    }
                }
            }
        });
*/
        homePresenter.callOngoingApplicationsApi();
        homePresenter.callGetTagsApi();
        homePresenter.callGetJobsApi(page, tag_id);

       /* homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        if (Helper.isNetworkAvailable(getContext())) {
            homeViewModel.callGetJobsApi(page, tag_id,getContext());
            api_Home_data();
        }*/

        receiverRegister();
    }

    private void setView(JobsForYouResponse jobsResponse) {
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
                mainHomeLL.setVisibility(View.VISIBLE);
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
        }
    }


    @Override
    public void setTagsResponse(JobTagsResponse TagsResponse) {
        //   TagsResponse.data

        jobTitleAdapter = new JobTitleAdapter(getActivity(), TagsResponse.data, new JobTitleAdapter.CallApi() {
            @Override
            public void calljobapi(String Tag_id) {
                page = 0;
                tag_id = Tag_id;
                homePresenter.callGetJobsApi(page, Tag_id);
            }
        });
        id_rv_job_title.setAdapter(jobTitleAdapter);
    }

    @Override
    public void setJobsResponse(JobsForYouResponse jobsResponse) {
        //  id_rv_jobs.setNestedScrollingEnabled(true);
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
                mainHomeLL.setVisibility(View.VISIBLE);
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
                ongoingApplicationAdapter = new OngoingApplicationAdapter(getActivity(), jobsResponse.data, new OngoingApplicationAdapter.JobSelectedForDetail() {
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
                ongoingApplicationLL.setVisibility(View.VISIBLE);
            } else {
                ongoingApplicationLL.setVisibility(View.GONE);
            }

        } else {
            ongoingApplicationLL.setVisibility(View.GONE);
        }
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
                homePresenter.callScheduleInterviewApi(s, jobId);
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