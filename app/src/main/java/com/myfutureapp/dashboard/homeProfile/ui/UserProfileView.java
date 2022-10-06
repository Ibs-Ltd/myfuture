package com.myfutureapp.dashboard.homeProfile.ui;

import com.myfutureapp.core.ui.MasterView;
import com.myfutureapp.dashboard.homeProfile.model.UserProfilePicModel;
import com.myfutureapp.dashboard.model.UserProfileResponse;
import com.myfutureapp.profile.model.UserProfileUpDataResponse;

public interface UserProfileView extends MasterView {

    void setUserProfilePicResponse(UserProfilePicModel userProfilePicModel);

    void setUserProfileResponse(UserProfileResponse userProfileResponse);

    void setProfileDataResponse(UserProfileUpDataResponse userProfileUpDataResponse);
}
