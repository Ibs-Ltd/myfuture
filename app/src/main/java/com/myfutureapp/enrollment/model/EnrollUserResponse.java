package com.myfutureapp.enrollment.model;

import com.google.gson.annotations.SerializedName;
import com.myfutureapp.core.model.BaseResponse;

import java.util.List;

public class EnrollUserResponse extends BaseResponse {

    @SerializedName("data")
    public EnrollModel data;

    public class EnrollModel {

        @SerializedName("enrollment_quote")
        public String enrollment_quote;
        @SerializedName("user_data")
        public List<EnrollUserDataModel> user_data;

        public class EnrollUserDataModel {
            @SerializedName("name")
            public String name;
            @SerializedName("designation")
            public String designation;
            @SerializedName("company")
            public String company;
            @SerializedName("profile_url")
            public String profile_url;
        }
    }

}
