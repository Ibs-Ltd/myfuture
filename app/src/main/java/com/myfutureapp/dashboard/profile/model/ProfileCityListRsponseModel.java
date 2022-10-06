package com.myfutureapp.dashboard.profile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myfutureapp.core.model.BaseResponse;

import java.util.List;

public class ProfileCityListRsponseModel extends BaseResponse {

    @SerializedName("data")
    @Expose
    public List<ProfileCityListDataModel> data = null;


    public class ProfileCityListDataModel {

        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("state_id")
        @Expose
        public Integer stateId;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("created")
        @Expose
        public String created;
        @SerializedName("modified")
        @Expose
        public String modified;

    }
}
