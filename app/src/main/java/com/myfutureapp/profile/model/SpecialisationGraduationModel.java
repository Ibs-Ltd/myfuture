package com.myfutureapp.profile.model;

public class SpecialisationGraduationModel {

    public String courseName;
    public boolean isSelected = false;

    public SpecialisationGraduationModel(String courseName, boolean isSelected) {
        this.courseName = courseName;
        this.isSelected = isSelected;
    }
}
