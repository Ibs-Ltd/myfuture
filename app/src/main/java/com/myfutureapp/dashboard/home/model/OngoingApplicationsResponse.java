package com.myfutureapp.dashboard.home.model;

import com.google.gson.annotations.SerializedName;
import com.myfutureapp.core.model.BaseResponse;

import java.util.List;

public class OngoingApplicationsResponse extends BaseResponse {

    @SerializedName("data")
    public List<OngoingApplicationdetail> data;

    public OngoingApplicationsResponse() {
    }

    public class OngoingApplicationdetail {

        @SerializedName("company_name")
        public String company_name;
        @SerializedName("opportunity_applied_datetime")
        public String opportunity_applied_datetime;

        @SerializedName("id")
        public int id;

        @SerializedName("designation")
        public String designation;


        @SerializedName("show_schedule_button")
        public int show_schedule_button;
        @SerializedName("nextStep")
        public NextStep nextStep;

        public class NextStep {
            @SerializedName("row")
            public String row;
            @SerializedName("color")
            public String color;

        }
    }

}
