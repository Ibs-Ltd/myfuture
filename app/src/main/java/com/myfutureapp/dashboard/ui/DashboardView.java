package com.myfutureapp.dashboard.ui;

import com.myfutureapp.core.ui.MasterView;
import com.myfutureapp.dashboard.model.UserProfileResponse;

public interface DashboardView extends MasterView {


    void setUserProfileResponse(UserProfileResponse userProfileResponse);
}