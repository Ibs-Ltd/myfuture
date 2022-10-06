package com.myfutureapp.dashboard.profile.ui;

import com.myfutureapp.core.ui.MasterView;
import com.myfutureapp.dashboard.model.UserProfileResponse;
import com.myfutureapp.dashboard.profile.model.AddEditWorkExperienceResponse;
import com.myfutureapp.dashboard.profile.model.DeleteWorkExperinceModel;
import com.myfutureapp.dashboard.profile.model.ProfileCityListRsponseModel;
import com.myfutureapp.dashboard.profile.model.ProfileStateListResponseModel;
import com.myfutureapp.dashboard.profile.model.UpdateLocationPreferenceResponse;
import com.myfutureapp.profile.model.UserProfileUpDataResponse;

public interface ProfileFragmentView extends MasterView {

    void setProfileDataResponse(UserProfileUpDataResponse userProfileUpDataResponse);

    void setWorkExperienceResponse(AddEditWorkExperienceResponse addEditWorkExperienceResponse);

    void setUpdateLocationPreferenceResponse(UpdateLocationPreferenceResponse addEditWorkExperienceResponse);

    void setStateListResponse(ProfileStateListResponseModel profileStateListResponseModel);

    void setCityListResponse(ProfileCityListRsponseModel profileCityListRsponseModel);

    void setDeleteWorkExperinceResponse(DeleteWorkExperinceModel deleteWorkExperinceModel);

    void setUserProfileResponse(UserProfileResponse userProfileResponse);
}
