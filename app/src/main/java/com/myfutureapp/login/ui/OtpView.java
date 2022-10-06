package com.myfutureapp.login.ui;


import com.myfutureapp.core.ui.MasterView;
import com.myfutureapp.dashboard.model.UserProfileResponse;
import com.myfutureapp.login.model.CreateUserResponse;
import com.myfutureapp.login.model.OtpVerifyResponse;

public interface OtpView extends MasterView {

    void setOtpVerifyResponse(OtpVerifyResponse otpVerifyResponse);

    void onResponse(CreateUserResponse createUserResponse);

    void setUserProfileResponse(UserProfileResponse userProfileResponse);
}