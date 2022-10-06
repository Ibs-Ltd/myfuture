package com.myfutureapp.dashboard.homeProfile.ui;

import com.myfutureapp.core.ui.MasterView;
import com.myfutureapp.dashboard.model.UserProfileResponse;
import com.myfutureapp.dashboard.profile.model.ProfileCityListRsponseModel;
import com.myfutureapp.dashboard.profile.model.ProfileStateListResponseModel;
import com.myfutureapp.profile.model.UserProfileUpDataResponse;

public interface BasicUserView extends MasterView {

    void setProfileDataResponse(UserProfileUpDataResponse userProfileUpDataResponse);

    void setStateListResponse(ProfileStateListResponseModel profileStateListResponseModel);

    void setCityListResponse(ProfileCityListRsponseModel profileCityListRsponseModel);

    void setUserProfileResponse(UserProfileResponse userProfileResponse);
}
