package com.myfutureapp.dashboard.searchJob;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myfutureapp.R;
import com.myfutureapp.dashboard.home.adapter.JobsAdapter;
import com.myfutureapp.dashboard.home.model.BookMarkResponse;
import com.myfutureapp.dashboard.home.model.JobsForYouResponse;
import com.myfutureapp.dashboard.searchJob.presenter.SearchJobPresenter;
import com.myfutureapp.dashboard.searchJob.ui.SearchActivityView;
import com.myfutureapp.jobDetail.JobDetailActivity;
import com.myfutureapp.util.Helper;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SearchJobActivity extends AppCompatActivity implements SearchActivityView {

    private SearchJobPresenter searchJobPresenter;
    private JobsAdapter jobsAdapter;
    private boolean dataLoaded = false;
    private int page = 0;
    private final String searchText = "";
    private TextView nojobText;
    private RecyclerView searchJobs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_job);

        searchJobPresenter = new SearchJobPresenter(SearchJobActivity.this, SearchJobActivity.this);


        TextView toolbarTitle = findViewById(R.id.toolbarTitle);
        ImageView hambarIV = findViewById(R.id.hambarIV);
        EditText serachTextET = findViewById(R.id.serachTextET);
        nojobText = findViewById(R.id.nojobText);
        hambarIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        serachTextET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    nojobText.setText("Please search Text");
                    nojobText.setVisibility(View.VISIBLE);
                    searchJobs.setVisibility(View.GONE);
                    serachTextET.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                } else {
                    page = 0;
                    searchJobPresenter.callGetJobsApi(page, "0", s.toString());

                    serachTextET.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawable(R.drawable.ic_cross_black), null);
                }
            }
        });

        serachTextET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                nojobText.setText("Please search Text");
                nojobText.setVisibility(View.VISIBLE);
                searchJobs.setVisibility(View.GONE);
                serachTextET.setText("");
                serachTextET.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                return false;
            }
        });
        serachTextET.addTextChangedListener(
                new TextWatcher() {
                    private final long DELAY = 1000; // milliseconds
                    private Timer timer = new Timer();

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void afterTextChanged(final Editable s) {
                        timer.cancel();
                        timer = new Timer();
                        timer.schedule(
                                new TimerTask() {
                                    @Override
                                    public void run() {
                                        if (s.length() == 0) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    nojobText.setText("Please search Text");
                                                    nojobText.setVisibility(View.VISIBLE);
                                                    searchJobs.setVisibility(View.GONE);

                                                }
                                            });
                                        } else {
                                            page = 0;
                                            searchJobPresenter.callGetJobsApi(page, "0", s.toString());
                                        }   // TODO: do what you need here (refresh list)
                                        // you will probably need to use runOnUiThread(Runnable action) for some specific actions (e.g. manipulating views)
                                    }
                                },
                                DELAY
                        );
                    }
                }
        );

        searchJobs = findViewById(R.id.searchJobs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false);
        searchJobs.setHasFixedSize(true);
        searchJobs.setLayoutManager(linearLayoutManager);
        searchJobs.setItemAnimator(new DefaultItemAnimator());
        jobsAdapter = new JobsAdapter(new ArrayList<>(), new JobsAdapter.JobSelectedForDetail() {
            @Override
            public void JobDetailSelected(JobsForYouResponse.JobDetail selectedJob) {
                if (selectedJob != null) {
                    String title = String.format("%s - %s", selectedJob.company_name, selectedJob.designation);
                    Intent intent = JobDetailActivity.getIntent(SearchJobActivity.this, selectedJob.id + "", title, selectedJob.compensation_show);
                    startActivity(intent);
                }
            }

            @Override
            public void JobBookMarkTask(String bookMarkType, JobsForYouResponse.JobDetail jobDetail, int position) {
                searchJobPresenter.callBookMarkApi(bookMarkType, jobDetail, position);
            }
        });
        searchJobs.setAdapter(jobsAdapter);
        searchJobs.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) { // only when scrolling up
                    final int visibleThreshold = 4;
                    int lastItem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                    int currentTotalCount = linearLayoutManager.getItemCount();
                    if (currentTotalCount <= lastItem + visibleThreshold && dataLoaded && page != -1) {
                        if (Helper.isNetworkAvailable(SearchJobActivity.this)) {
                            dataLoaded = false;
                            searchJobPresenter.callGetJobsApi(page, "0", searchText);
                        }
                    }
                }
            }
        });

//        searchJobPresenter.callGetJobsApi(page, "0", searchText);


    }

    @Override
    public void setJobsResponse(JobsForYouResponse jobsResponse) {
        //  id_rv_jobs.setNestedScrollingEnabled(true);
        if (jobsResponse != null && jobsResponse.success) {
            if (page == 0) {
                if (jobsResponse.data.size() == 0) {
                    nojobText.setText("No Opportunity found ");
                    nojobText.setVisibility(View.VISIBLE);
                    searchJobs.setVisibility(View.GONE);
                } else {
                    jobsAdapter.notifyItem(jobsResponse.data);
                    nojobText.setVisibility(View.GONE);
                    searchJobs.setVisibility(View.VISIBLE);
                }

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

    @Override
    public void setBookMarkResponse(BookMarkResponse bookMarkResponse, String bookMarkType, int position) {
        if (bookMarkResponse != null && bookMarkResponse.success) {
            jobsAdapter.notifyItemPosition(position, Integer.parseInt(bookMarkType));
        }
    }

}
