package com.myfutureapp.dashboard.bookmark.ui;

import com.myfutureapp.core.ui.MasterView;
import com.myfutureapp.dashboard.home.model.BookMarkResponse;
import com.myfutureapp.dashboard.home.model.JobsForYouResponse;

public interface BookMarkView extends MasterView {

    void setJobsResponse(JobsForYouResponse jobsForYouResponse);

    void setBookMarkResponse(BookMarkResponse bookMarkResponse, String bookMarkType, int position);

}