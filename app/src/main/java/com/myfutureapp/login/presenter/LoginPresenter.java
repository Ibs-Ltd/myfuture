package com.myfutureapp.login.presenter;

import android.content.Context;

import com.myfutureapp.core.NetworkHandler;
import com.myfutureapp.core.NetworkManager;
import com.myfutureapp.core.model.BaseResponse;
import com.myfutureapp.login.model.CreateUserResponse;
import com.myfutureapp.login.ui.LoginView;

import okhttp3.Request;

public class LoginPresenter extends NetworkHandler {

    private LoginView loginView;
    private final Context context;

    public LoginPresenter(Context context, LoginView loginView) {
        super(context, loginView);
        this.loginView = loginView;
        this.context = context;
    }

    @Override
    public boolean handleFailure(Request request, Exception ex, String message) {
        if (loginView != null) {
            loginView.hideLoader();
            loginView.showMessage(message);
        }
        return super.handleFailure(request, ex, message);
    }

    @Override
    public boolean handleError(Request request, Error error) {
        if (loginView != null) {
            loginView.hideLoader();
            loginView.showMessage(error.getMessage());
        }
        return super.handleError(request, error);
    }

    @Override
    public boolean handleResponse(Request request, BaseResponse response) {
        if (loginView != null && response != null) {
            loginView.hideLoader();
            if (response instanceof CreateUserResponse) {
                CreateUserResponse createUserResponse = (CreateUserResponse) response;
                loginView.onResponse(createUserResponse);
            }

        }
        return true;
    }

    public void requestOtp(String mobileNumber) {
        NetworkManager.getApi(context).requestOtp(mobileNumber).enqueue(this);
    }

    public void onDestroyedView() {
        loginView = null;
    }
}