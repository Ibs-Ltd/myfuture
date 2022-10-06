package com.myfutureapp.login;

import com.myfutureapp.core.AppUpdate;
import com.myfutureapp.dashboard.model.UserProfileResponse;
import com.myfutureapp.login.model.GetUserDataresponse;

public class DataHolder {

    private static volatile DataHolder instance;
    public GetUserDataresponse getUserDataresponse = new GetUserDataresponse();
    public UserProfileResponse getUserProfileDataresponse = new UserProfileResponse();
    public AppUpdate appUpdate;

    private DataHolder() {
    }

    public synchronized static DataHolder getInstance() {
        if (instance == null) {
            instance = new DataHolder();

        }
        return instance;
    }


}