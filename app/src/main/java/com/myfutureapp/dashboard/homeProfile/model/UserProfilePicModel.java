package com.myfutureapp.dashboard.homeProfile.model;

import com.google.gson.annotations.SerializedName;
import com.myfutureapp.core.model.BaseResponse;

public class UserProfilePicModel extends BaseResponse {

    @SerializedName("data")
    public String data;
}
