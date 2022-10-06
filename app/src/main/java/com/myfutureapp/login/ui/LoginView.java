package com.myfutureapp.login.ui;


import com.myfutureapp.core.ui.MasterView;
import com.myfutureapp.login.model.CreateUserResponse;

public interface LoginView extends MasterView {

    void onResponse(CreateUserResponse createUserResponse);
}