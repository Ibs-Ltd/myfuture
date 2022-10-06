package com.myfutureapp.dashboard.profile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myfutureapp.core.model.BaseResponse;

import java.util.List;

public class ProfileStateListResponseModel extends BaseResponse {

    @SerializedName("data")
    @Expose
    public List<ProfileStateListDataModel> data = null;


    public class ProfileStateListDataModel {

        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("name")
        @Expose
        public String name;

    }
}