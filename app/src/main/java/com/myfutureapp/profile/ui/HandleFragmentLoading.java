package com.myfutureapp.profile.ui;

import java.util.ArrayList;

public interface HandleFragmentLoading {
    void loadingQuizDetailFragment(String type);

    void loadingQuizDetailFragmentWithSpecialisation(String type, ArrayList<String> specialization);
}
