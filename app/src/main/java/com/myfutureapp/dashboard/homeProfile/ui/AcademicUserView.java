package com.myfutureapp.dashboard.homeProfile.ui;

import com.myfutureapp.core.ui.MasterView;
import com.myfutureapp.dashboard.model.UserProfileResponse;
import com.myfutureapp.profile.model.GraduationCourseResponse;
import com.myfutureapp.profile.model.PostGraduationCourseResponse;
import com.myfutureapp.profile.model.UserProfileUpDataResponse;

public interface AcademicUserView extends MasterView {

    void setProfileDataResponse(UserProfileUpDataResponse userProfileUpDataResponse);

    void setUserProfileResponse(UserProfileResponse userProfileResponse);

    void setGraduationCourseResponse(GraduationCourseResponse graduationCourseResponse);

    void setPostGraduationCourseResponse(PostGraduationCourseResponse graduationCourseResponse);
}
