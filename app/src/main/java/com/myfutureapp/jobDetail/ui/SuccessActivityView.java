package com.myfutureapp.jobDetail.ui;

import com.myfutureapp.core.ui.MasterView;
import com.myfutureapp.jobDetail.model.InterviewTimeSlotsResponse;
import com.myfutureapp.jobDetail.model.ScheduleInterviewResponse;

public interface SuccessActivityView extends MasterView {

    void setInterviewTimeSlotsResponse(InterviewTimeSlotsResponse interviewTimeSlotsResponse, String jobId);

    void setScheduleInterviewResponse(ScheduleInterviewResponse scheduleInterviewResponse);
}