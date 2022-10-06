package com.myfutureapp.login.presenter;

import android.content.Context;

import com.myfutureapp.core.NetworkHandler;
import com.myfutureapp.core.NetworkManager;
import com.myfutureapp.core.model.BaseResponse;
import com.myfutureapp.dashboard.model.UserProfileResponse;
import com.myfutureapp.login.model.CreateUserResponse;
import com.myfutureapp.login.model.OtpVerifyResponse;
import com.myfutureapp.login.ui.OtpView;
import com.myfutureapp.util.AppConstants;
import com.myfutureapp.util.AppPreferences;
import com.myfutureapp.util.Helper;

import java.util.HashMap;

import okhttp3.Request;

public class OtpVerifyPresenter extends NetworkHandler {

    private OtpView otpView;
    private final Context context;

    public OtpVerifyPresenter(Context context, OtpView otpView) {
        super(context, otpView);
        this.otpView = otpView;
        this.context = context;
    }

    @Override
    public boolean handleFailure(Request request, Exception ex, String message) {
        if (otpView != null) {
            otpView.hideLoader();
            if (Helper.isContainValue(message)) {
                otpView.showMessage(message);
            } else {
                otpView.showMessage("OTP entered is incorrect");
            }
        }
        return super.handleFailure(request, ex, message);
    }

    @Override
    public boolean handleError(Request request, Error error) {
        if (otpView != null) {
            otpView.hideLoader();
            otpView.showMessage(error.getMessage());
        }
        return super.handleError(request, error);
    }

    @Override
    public boolean handleResponse(Request request, BaseResponse response) {
        if (otpView != null && response != null) {
            otpView.hideLoader();
            if (response instanceof OtpVerifyResponse) {
                OtpVerifyResponse otpVerifyResponse = (OtpVerifyResponse) response;
                otpView.setOtpVerifyResponse(otpVerifyResponse);
            } else if (response instanceof CreateUserResponse) {
                CreateUserResponse createUserResponse = (CreateUserResponse) response;
                otpView.onResponse(createUserResponse);
            } else if (response instanceof UserProfileResponse) {
                UserProfileResponse userProfileResponse = (UserProfileResponse) response;
                otpView.setUserProfileResponse(userProfileResponse);
            }

        }
        return true;
    }

    public void onDestroyedView() {
        otpView = null;
    }

    public void callVerifyOtpApi(String mobileNumber, String otp) {
        NetworkManager.getApi(context).verifyOtp(mobileNumber, otp).enqueue(this);
    }

    public void requestOtp(String mobileNumber) {
        NetworkManager.getApi(context).requestOtp(mobileNumber).enqueue(this);
    }


    public void callUserProfileApi(String studentId) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        NetworkManager.getApi(context).getUserProfile(headerMap, studentId).enqueue(this);
    }
}