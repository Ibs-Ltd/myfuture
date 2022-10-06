package com.myfutureapp.dashboard.application.model;

import com.google.gson.annotations.SerializedName;
import com.myfutureapp.core.model.BaseResponse;

import java.util.List;


public class OldApplicationResponse extends BaseResponse {
    @SerializedName("data")
    public OldApplicationModel data;

    public class OldApplicationModel {

        @SerializedName("history")
        public List<HistoryModel> history;

        public class HistoryModel {

            @SerializedName("company_name")
            public String company_name;

            @SerializedName("id")
            public int id;

            @SerializedName("company_id")
            public int company_id;

            @SerializedName("designation")
            public String designation;

            @SerializedName("web_link")
            public String web_link;

            @SerializedName("rejection")
            public Rejection rejection;

            public class Rejection {

                @SerializedName("row")
                public String row;
                @SerializedName("color")
                public String color;
                @SerializedName("date")
                public String date;
            }
        }
    }
}
