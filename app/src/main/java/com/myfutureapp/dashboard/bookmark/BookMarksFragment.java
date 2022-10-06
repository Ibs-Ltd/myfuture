package com.myfutureapp.dashboard.bookmark;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.myfutureapp.R;
import com.myfutureapp.dashboard.bookmark.presenter.BookMarkPresenter;
import com.myfutureapp.dashboard.bookmark.ui.BookMarkView;
import com.myfutureapp.dashboard.home.adapter.JobsAdapter;
import com.myfutureapp.dashboard.home.model.BookMarkResponse;
import com.myfutureapp.dashboard.home.model.JobsForYouResponse;
import com.myfutureapp.jobDetail.JobDetailActivity;
import com.myfutureapp.login.LoginActivity;
import com.myfutureapp.util.AppConstants;
import com.myfutureapp.util.AppPreferences;
import com.myfutureapp.util.Helper;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookMarksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class BookMarksFragment extends Fragment implements BookMarkView {

    RecyclerView id_bookmark_jobs;
    private BookMarkPresenter bookMarkPresenter;
    private JobsAdapter jobsAdapter;
    private boolean dataLoaded = false;
    private int page = 0;
    /**
     * Using this receiver to update jobs from other pages if users bookmark any job
     */

    private final BroadcastReceiver jobUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            page = 0;
            bookMarkPresenter.callBookMarksJobsApi(page, "0", "");
        }
    };
    private TextView noBookMark;
    private ShimmerFrameLayout shimmerFrameLayout;

    public BookMarksFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static BookMarksFragment newInstance(String param1, String param2) {
        BookMarksFragment fragment = new BookMarksFragment();
        return fragment;
    }

    private void receiverRegister() {
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(requireContext());

        IntentFilter intentFilter = new IntentFilter(getString(R.string.app_name) + "user.bookmark.refresh");
        manager.registerReceiver(jobUpdateReceiver, intentFilter);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_marks, container, false);
        bookMarkPresenter = new BookMarkPresenter(getContext(), BookMarksFragment.this);
        InitUi(view);
        return view;
    }

    public void InitUi(View view) {
        id_bookmark_jobs = view.findViewById(R.id.id_bookmark_jobs);
        noBookMark = view.findViewById(R.id.noBookMark);
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout);
        LinearLayout loginLL = view.findViewById(R.id.loginLL);
        loginLL.setOnClickListener(v -> startActivity(new Intent(getContext(), LoginActivity.class).addFlags(Intent.
                FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        id_bookmark_jobs.setHasFixedSize(true);
        id_bookmark_jobs.setLayoutManager(linearLayoutManager);
        id_bookmark_jobs.setItemAnimator(new DefaultItemAnimator());
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
                bookMarkPresenter.callBookMarkApi(bookMarkType, jobDetail, position);

            }


        });
        id_bookmark_jobs.setAdapter(jobsAdapter);
        id_bookmark_jobs.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                            bookMarkPresenter.callBookMarksJobsApi(page, "0", "");
                        }
                    }
                }
            }
        });

        if (!AppPreferences.getInstance(getContext()).getPreferencesString(AppConstants.AUTH).equalsIgnoreCase("true")) {
            loginLL.setVisibility(View.VISIBLE);
            shimmerFrameLayout.setVisibility(View.GONE);
            id_bookmark_jobs.setVisibility(View.GONE);
            noBookMark.setVisibility(View.GONE);
            shimmerFrameLayout.setVisibility(View.GONE);
        } else {
            shimmerFrameLayout.setVisibility(View.GONE);
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.startShimmer();
            bookMarkPresenter.callBookMarksJobsApi(page, "0", "");
            loginLL.setVisibility(View.GONE);
            receiverRegister();
        }


    }


    @Override
    public void setBookMarkResponse(BookMarkResponse bookMarkResponse, String bookMarkType, int position) {
        if (bookMarkResponse != null && bookMarkResponse.success) {

            int size = jobsAdapter.notifyBookMarkPosition(position, Integer.parseInt(bookMarkType));
            if (size == 0) {
                id_bookmark_jobs.setVisibility(View.GONE);
                noBookMark.setVisibility(View.VISIBLE);

            }
            Intent intent = new Intent();
            intent.setAction(getString(R.string.app_name) + "user.bookmark.refresh");
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);


        }
    }

    @Override
    public void setJobsResponse(JobsForYouResponse jobsResponse) {
        if (jobsResponse != null && jobsResponse.success) {
            if (page == 0) {
                if (jobsResponse.data.size() == 0) {
                    noBookMark.setVisibility(View.VISIBLE);
                    id_bookmark_jobs.setVisibility(View.GONE);
                } else {
                    jobsAdapter.notifyItem(jobsResponse.data);
                    noBookMark.setVisibility(View.GONE);
                    id_bookmark_jobs.setVisibility(View.VISIBLE);
                }
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
    public void showLoader() {

    }

    @Override
    public void hideLoader() {

    }

    @Override
    public void showMessage(String message) {

    }
}