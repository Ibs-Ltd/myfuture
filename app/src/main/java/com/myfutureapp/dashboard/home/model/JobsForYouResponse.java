package com.myfutureapp.dashboard.home.model;

import com.google.gson.annotations.SerializedName;
import com.myfutureapp.core.model.BaseResponse;

import java.util.List;

public class JobsForYouResponse extends BaseResponse {
    @SerializedName("data")
    public List<JobDetail> data;

    public JobsForYouResponse() {
    }

    public class JobDetail {
        @SerializedName("id")
        public int id;
        @SerializedName("company_name")
        public String company_name;
        @SerializedName("logo")
        public String logo;
        @SerializedName("designation")
        public String designation;
        @SerializedName("type_of_role")
        public String type_of_role;

        @SerializedName("no_of_vacancies")
        public String no_of_vacancies;
        @SerializedName("work_experience")
        public String work_experience;


        @SerializedName("deadline")
        public String deadline;
        @SerializedName("city_name")
        public String city_name;

        @SerializedName("compensation")
        public String compensation;
        @SerializedName("role_show")
        public String role_show;
        @SerializedName("compensation_show")
        public String compensation_show;
        @SerializedName("is_bookmarked")
        public int is_bookmarked;

    }

}
