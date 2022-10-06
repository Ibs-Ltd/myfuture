package com.myfutureapp.jobDetail.model;

import com.google.gson.annotations.SerializedName;
import com.myfutureapp.core.model.BaseResponse;
import com.myfutureapp.dashboard.home.model.JobsForYouResponse.JobDetail;

import java.util.ArrayList;
import java.util.List;

public class JobDetailResponse extends BaseResponse {

    @SerializedName("data")
    public JobDetailModel data;


    public class JobDetailModel {

        @SerializedName("progress_obj")
        public List<ProgressObject> progress_obj;
        @SerializedName("basic")
        public BasicDetails basic;


        @SerializedName("eligibility_creteria")
        public List<EligibilityCreteria> eligibility_creteria;

        @SerializedName("similar_roles")
        public List<JobDetail> similar_roles;

        @SerializedName("company_data")
        public CompanyData company_data;

        public class ProgressObject {
            @SerializedName("date")
            public String date;
            @SerializedName("row")
            public String row;
            @SerializedName("is_done")
            public int is_done;
            @SerializedName("show_schedule_button")
            public int show_schedule_button;
        }

        public class BasicDetails {
            @SerializedName("role_show")
            public String role_show;
            @SerializedName("designation")
            public String designation;
            @SerializedName("compensation")
            public String compensation;
            @SerializedName("vacancies")
            public int vacancies;
            @SerializedName("instructions")
            public String instructions;
            @SerializedName("application_deadline")
            public String application_deadline;
            @SerializedName("compensation_show")
            public String compensation_show;
            @SerializedName("ask_ug_details")
            public String ask_ug_details;
            @SerializedName("ask_pg_details")
            public String ask_pg_details;
            @SerializedName("ask_work_exp")
            public String ask_work_exp;
            @SerializedName("ask_xii_score")
            public String ask_xii_score;
            @SerializedName("ask_x_score")
            public String ask_x_score;
            @SerializedName("ask_laptop")
            public String ask_laptop;
            @SerializedName("ask_wifi")
            public String ask_wifi;
            @SerializedName("ask_driving_license")
            public String ask_driving_license;
            @SerializedName("ask_2_wheeler")
            public String ask_2_wheeler;

            @SerializedName("ask_preference")
            public Boolean ask_preference;
            @SerializedName("showLocation")
            public ArrayList<String> showLocation;
        }

        public class CompanyData {
            @SerializedName("description")
            public String description;
            @SerializedName("name")
            public String name;
            @SerializedName("logo")
            public String logo;
        }

        public class EligibilityCreteria {

            @SerializedName("data")
            public List<EligibilityObject> data;
          /*  @SerializedName("key")
            public String key;
            @SerializedName("value")
            public String value;*/
        }

        public class EligibilityObject {
            @SerializedName("key")
            public String key;
            @SerializedName("value")
            public String value;

        }

    }
}
