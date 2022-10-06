package com.myfutureapp.profile.model;

import com.google.gson.annotations.SerializedName;
import com.myfutureapp.core.model.BaseResponse;

import java.util.ArrayList;
import java.util.List;

public class PostGraduationCourseResponse extends BaseResponse {

    @SerializedName("data")
    public List<GraduationCourseModel> data;

    public class GraduationCourseModel {
        @SerializedName("course_name")
        public String course_name;
        @SerializedName("specialization")
        public ArrayList<String> specialization;

        public boolean isSelected = false;
    }
}
