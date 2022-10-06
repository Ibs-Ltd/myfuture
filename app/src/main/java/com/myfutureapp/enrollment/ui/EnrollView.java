package com.myfutureapp.enrollment.ui;

import com.myfutureapp.core.ui.MasterView;
import com.myfutureapp.enrollment.model.EnrollUserResponse;
import com.myfutureapp.jobDetail.model.JobDetailResponse;

public interface EnrollView extends MasterView {

    void setEnrollUserResponse(EnrollUserResponse enrollUserResponse);

    void setOpportunityDetailResponse(JobDetailResponse jobDetailResponse);
}