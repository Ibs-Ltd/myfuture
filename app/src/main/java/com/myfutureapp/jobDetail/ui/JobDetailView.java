package com.myfutureapp.jobDetail.ui;

import com.myfutureapp.core.ui.MasterView;
import com.myfutureapp.dashboard.home.model.BookMarkResponse;
import com.myfutureapp.dashboard.model.UserProfileResponse;
import com.myfutureapp.jobDetail.model.ApplyOpportunityResponse;
import com.myfutureapp.jobDetail.model.EventApplyResponse;
import com.myfutureapp.jobDetail.model.InterviewTimeSlotsResponse;
import com.myfutureapp.jobDetail.model.JobDetailResponse;
import com.myfutureapp.jobDetail.model.ScheduleInterviewResponse;

public interface JobDetailView extends MasterView {


    void setOpportunityDetailResponse(JobDetailResponse jobDetailResponse);

    void setInterviewTimeSlotsResponse(InterviewTimeSlotsResponse interviewTimeSlotsResponse);

    void setApplyOpportunityResponse(ApplyOpportunityResponse applyOpportunityResponse);

    void setScheduleInterviewResponse(ScheduleInterviewResponse scheduleInterviewResponse);

    void setBookMarkResponse(BookMarkResponse bookMarkResponse, String bookMarkType, int position);

    void setUserProfileResponse(UserProfileResponse userProfileResponse);

    void setEventOnOpportunityResponse(EventApplyResponse eventApplyResponse);
}
