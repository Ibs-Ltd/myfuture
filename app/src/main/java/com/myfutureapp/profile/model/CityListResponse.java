package com.myfutureapp.profile.model;

import com.google.gson.annotations.SerializedName;
import com.myfutureapp.core.model.BaseResponse;

import java.util.List;

public class CityListResponse extends BaseResponse {

    @SerializedName("data")
    public List<CityModel> data;
    @SerializedName("id")
    public String id;


    public class CityModel {
        @SerializedName("id")
        public String id;
        @SerializedName("state_id")
        public String state_id;
        @SerializedName("name")
        public String name;
    }
}
