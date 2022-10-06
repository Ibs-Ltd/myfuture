package com.myfutureapp.login.model;

import com.google.gson.annotations.SerializedName;
import com.myfutureapp.core.model.BaseResponse;

public class OtpVerifyResponse extends BaseResponse {

    @SerializedName("data")
    public OtpVerifyModel data;


    public class OtpVerifyModel {

        @SerializedName("session_key")
        public String session_key;
        @SerializedName("id")
        public int id;
        @SerializedName("agreement")
        public boolean agreement;

        @SerializedName("student_data")
        public StudentData student_data;

        public class StudentData {
            @SerializedName("student_journey_status")
            public String student_journey_status;

            public String getStudent_journey_status() {
                return student_journey_status;
            }

            public void setStudent_journey_status(String student_journey_status) {
                this.student_journey_status = student_journey_status;
            }
        }
    }
}
