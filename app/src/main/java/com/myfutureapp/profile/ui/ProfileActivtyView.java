package com.myfutureapp.profile.ui;

import com.myfutureapp.core.ui.MasterView;
import com.myfutureapp.jobDetail.model.ApplyOpportunityResponse;
import com.myfutureapp.profile.model.UserProfileUpDataResponse;

public interface ProfileActivtyView extends MasterView {

    void setProfileDataResponse(UserProfileUpDataResponse userProfileUpDataResponse);

    void setApplyOpportunityResponse(ApplyOpportunityResponse applyOpportunityResponse);
}
