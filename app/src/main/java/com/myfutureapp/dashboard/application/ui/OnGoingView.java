package com.myfutureapp.dashboard.application.ui;

import com.myfutureapp.core.ui.MasterView;
import com.myfutureapp.dashboard.home.model.OngoingApplicationsResponse;
import com.myfutureapp.jobDetail.model.InterviewTimeSlotsResponse;
import com.myfutureapp.jobDetail.model.ScheduleInterviewResponse;

public interface OnGoingView extends MasterView {

    void setOngoingApplicationsResponse(OngoingApplicationsResponse jobsResponse);

    void setInterviewTimeSlotsResponse(InterviewTimeSlotsResponse interviewTimeSlotsResponse, String jobId);

    void setScheduleInterviewResponse(ScheduleInterviewResponse scheduleInterviewResponse);

}
