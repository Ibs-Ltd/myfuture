package com.myfutureapp.dashboard.home.model;

import com.google.gson.annotations.SerializedName;
import com.myfutureapp.core.model.BaseResponse;

import java.util.List;

public class JobTagsResponse extends BaseResponse {
    @SerializedName("data")
    public List<JobTagsdetail> data;

    public JobTagsResponse() {
    }

    public class JobTagsdetail {
        @SerializedName("id")
        public String id;

        @SerializedName("name")
        public String name;
/*
        @SerializedName("status")
        public String status;

        @SerializedName("created")
        public String created;

        @SerializedName("modified")
        public String modified;*/


    }

}
