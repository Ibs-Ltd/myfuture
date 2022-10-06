package com.myfutureapp.jobDetail.model;

import com.google.gson.annotations.SerializedName;
import com.myfutureapp.core.model.BaseResponse;

import java.util.List;

public class InterviewTimeSlotsResponse extends BaseResponse {

    @SerializedName("data")
    public List<String> data;
}
