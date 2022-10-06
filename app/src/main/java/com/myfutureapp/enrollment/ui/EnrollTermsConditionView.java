package com.myfutureapp.enrollment.ui;

import com.myfutureapp.core.ui.MasterView;
import com.myfutureapp.enrollment.model.EnrollUserResponse;
import com.myfutureapp.jobDetail.model.ApplyOpportunityResponse;
import com.myfutureapp.jobDetail.model.InterviewTimeSlotsResponse;
import com.myfutureapp.jobDetail.model.ScheduleInterviewResponse;

public interface EnrollTermsConditionView extends MasterView {

    void setEnrollStatusUpdateResponse(EnrollUserResponse enrollUserResponse);

    void setInterviewTimeSlotsResponse(InterviewTimeSlotsResponse interviewTimeSlotsResponse);

    void setApplyOpportunityResponse(ApplyOpportunityResponse applyOpportunityResponse);

    void setScheduleInterviewResponse(ScheduleInterviewResponse scheduleInterviewResponse);

}