package com.myfutureapp.jobApplyLocationSelection.model;

public class JobCitySelectModel {

    public String ciyName;
    public boolean isCitySelected = false;

    public JobCitySelectModel(String s, boolean b) {
        ciyName = s;
        isCitySelected = b;
    }
}
