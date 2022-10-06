package com.myfutureapp.dashboard.home.ui;

import com.myfutureapp.core.ui.MasterView;
import com.myfutureapp.dashboard.home.model.BookMarkResponse;
import com.myfutureapp.dashboard.home.model.JobTagsResponse;
import com.myfutureapp.dashboard.home.model.JobsForYouResponse;
import com.myfutureapp.dashboard.home.model.OngoingApplicationsResponse;
import com.myfutureapp.jobDetail.model.InterviewTimeSlotsResponse;
import com.myfutureapp.jobDetail.model.ScheduleInterviewResponse;

public interface HomeActivityView extends MasterView {

    void setTagsResponse(JobTagsResponse TagsResponse);

    void setJobsResponse(JobsForYouResponse jobsResponse);

    void setOngoingApplicationsResponse(OngoingApplicationsResponse jobsResponse);

    void setInterviewTimeSlotsResponse(InterviewTimeSlotsResponse interviewTimeSlotsResponse, String jobId);

    void setScheduleInterviewResponse(ScheduleInterviewResponse scheduleInterviewResponse);

    void setBookMarkResponse(BookMarkResponse bookMarkResponse, String bookMarkType, int position);
}