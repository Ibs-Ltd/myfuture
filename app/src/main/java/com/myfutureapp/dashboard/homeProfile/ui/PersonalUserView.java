package com.myfutureapp.dashboard.homeProfile.ui;

import com.myfutureapp.core.ui.MasterView;
import com.myfutureapp.dashboard.model.UserProfileResponse;
import com.myfutureapp.profile.model.UserProfileUpDataResponse;

public interface PersonalUserView extends MasterView {

    void setProfileDataResponse(UserProfileUpDataResponse userProfileUpDataResponse);

    void setUserProfileResponse(UserProfileResponse userProfileResponse);
}
