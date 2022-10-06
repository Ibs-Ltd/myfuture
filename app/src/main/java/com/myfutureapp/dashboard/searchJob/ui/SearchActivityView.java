package com.myfutureapp.dashboard.searchJob.ui;

import com.myfutureapp.core.ui.MasterView;
import com.myfutureapp.dashboard.home.model.BookMarkResponse;
import com.myfutureapp.dashboard.home.model.JobsForYouResponse;

public interface SearchActivityView extends MasterView {

    void setJobsResponse(JobsForYouResponse jobsResponse);

    void setBookMarkResponse(BookMarkResponse bookMarkResponse, String bookMarkType, int position);
}