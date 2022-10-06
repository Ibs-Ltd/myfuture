package com.myfutureapp.profile.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.gson.JsonObject;
import com.myfutureapp.R;
import com.myfutureapp.dashboard.DashboardActivity;
import com.myfutureapp.enrollment.EnrollActivity;
import com.myfutureapp.jobApplyLocationSelection.JobApplyLocationSelectActivity;
import com.myfutureapp.jobDetail.SuccessActivity;
import com.myfutureapp.jobDetail.model.ApplyOpportunityResponse;
import com.myfutureapp.login.DataHolder;
import com.myfutureapp.profile.model.UserProfileUpDataResponse;
import com.myfutureapp.profile.presenter.ProfileActivityPresenter;
import com.myfutureapp.util.Helper;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements HandleFragmentLoading, ProfileActivtyView {

    //    public boolean askBasicDetails = false, askGraduationDetails = false, askPostGraduationDetails = false, askPrimaryEducationDetails = false, askWorkExperience = false;
    public String askBasicDetails, askGraduationDetails, askPostGraduationDetails, askXEducationDetails, askXIIEducationDetails, askWorkExperience, askAdditionalInformation;
    public boolean[] askAdditionalList;
    TextView skip;
    boolean aggrementSigned;
    String toolbarTitle = "", salaryDetails = "";
    boolean ask_preference;
    private FragmentManager fragmentManager;
    private ProfileActivityPresenter profileActivityPresenter;
    private Fragment activeFragment, nameFragment, emailFragment, genderFragment, locationFragment, graduationFragment, specialisationGraduationFragment, yearGraduationFragment,
            graduationScoreFragment, postGraduationFragment, specialisationPostGraduationFragment, postGraduationYearFragment, postGraduationScoreFragment,
            workExperienceFragment, workYearExperienceFragment, gradeTwelveFragment, gradeTenthFragment, jobLocationFragment, additionalFragment;
    private String fragmentToBeLoad, loginRequired, jobId;
    private ProgressDialog progressDialog;
    private Fragment lastFragment;
    private final boolean lastEnabled = false;
    private String setActivityResult = "false";
    private String loadedFragments;
    private ArrayList<String> showLocation = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        if (getIntent() != null) {
            fragmentToBeLoad = getIntent().getStringExtra("fragmentToBeLoad");
            setActivityResult = getIntent().getStringExtra("setActivityResult");

            askBasicDetails = getIntent().getStringExtra("askBasicDetails");
            askBasicDetails = getIntent().getStringExtra("askBasicDetails");
            askGraduationDetails = getIntent().getStringExtra("askGraduationDetails");
            askPostGraduationDetails = getIntent().getStringExtra("askPostGraduationDetails");
            askXEducationDetails = getIntent().getStringExtra("askXEducationDetails");
            askXIIEducationDetails = getIntent().getStringExtra("askXIIEducationDetails");
            askWorkExperience = getIntent().getStringExtra("askWorkExperience");
            askAdditionalInformation = getIntent().getStringExtra("askAdditionalInformation");
            askAdditionalList = getIntent().getBooleanArrayExtra("askAdditionalList");
            aggrementSigned = getIntent().getBooleanExtra("aggrementSigned", true);
            ask_preference = getIntent().getBooleanExtra("ask_preference", true);
            showLocation = getIntent().getStringArrayListExtra("locations");
            toolbarTitle = getIntent().getStringExtra("toolbarTitle");
            salaryDetails = getIntent().getStringExtra("salaryDetails");


            if (setActivityResult == null) {
                setActivityResult = "false";
            }
            loginRequired = getIntent().getStringExtra("loginRequired");
            jobId = getIntent().getStringExtra("jobId");

        }
        inUi();
    }

    private void inUi() {
        profileActivityPresenter = new ProfileActivityPresenter(this, this);
        progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setMessage("loading....");
        progressDialog.setCancelable(true);


        ImageView backArrow = findViewById(R.id.backArrow);
        skip = (TextView) findViewById(R.id.skip);
        if (loginRequired.equalsIgnoreCase("true")) {
            skip.setVisibility(View.INVISIBLE);
        }
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loginRequired.equalsIgnoreCase("true")) {
                    Log.e("loaddedFragment", loadedFragments);
                    if (loadedFragments.equalsIgnoreCase("graduationFragment")
                            || loadedFragments.equalsIgnoreCase("specialisationGraduationFragment")
                            || loadedFragments.equalsIgnoreCase("yearGraduationFragment") || loadedFragments.equalsIgnoreCase("graduationScoreFragment")) {
                        askGraduationDetails = "3";
                    } else if (loadedFragments.equalsIgnoreCase("postGraduationFragment")
                            || loadedFragments.equalsIgnoreCase("specialisationPostGraduationFragment")
                            || loadedFragments.equalsIgnoreCase("postGraduationYearFragment") || loadedFragments.equalsIgnoreCase("postGraduationScoreFragment")) {
                        askPostGraduationDetails = "3";
                    } else if (loadedFragments.equalsIgnoreCase("workExperienceFragment")
                            || loadedFragments.equalsIgnoreCase("workYearExperienceFragment")) {
                        askWorkExperience = "3";
                    } else if (loadedFragments.equalsIgnoreCase("gradeTwelveFragment")) {
                        askXIIEducationDetails = "3";
                    } else if (loadedFragments.equalsIgnoreCase("gradeTenthFragment")) {
                        askXEducationDetails = "3";
                    }
                    if (!askGraduationDetails.equalsIgnoreCase("3")) {
                        loadingQuizDetailFragment("graduationFragment");
                    } else if (!askPostGraduationDetails.equalsIgnoreCase("3")) {
                        loadingQuizDetailFragment("postGraduationFragment");
                    } else if (!askWorkExperience.equalsIgnoreCase("3")) {
                        loadingQuizDetailFragment("workExperienceFragment");
                    } else if (!askXIIEducationDetails.equalsIgnoreCase("3")) {
                        loadingQuizDetailFragment("gradeTwelveFragment");
                    } else if (!askXEducationDetails.equalsIgnoreCase("3")) {
                        loadingQuizDetailFragment("gradeTenthFragment");
                    } else {
                        loadingQuizDetailFragment("finished");
                    }
//                    setFragmentToLoad(loadedFragments,);
                } else {
                    DataHolder.getInstance().getUserDataresponse.student_journey_status = loadedFragments;
                    if (DataHolder.getInstance().getUserDataresponse.graduation_gpa_max == null) {
                        DataHolder.getInstance().getUserDataresponse.graduation_gpa_max = "10";
                    }
                    if (DataHolder.getInstance().getUserDataresponse.pg_gpa_max == null) {
                        DataHolder.getInstance().getUserDataresponse.pg_gpa_max = "10";
                    }
                    if (DataHolder.getInstance().getUserDataresponse.x_gpa_max == null) {
                        DataHolder.getInstance().getUserDataresponse.x_gpa_max = "10";
                    }
                    if (DataHolder.getInstance().getUserDataresponse.xii_gpa_max == null) {
                        DataHolder.getInstance().getUserDataresponse.xii_gpa_max = "10";
                    }
                    hiitingProfileDataApi();
                }

            }
        });
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helper.hideKeyboard(ProfileActivity.this);
                onBackPressed();
            }
        });

//        setFragmentToLoad("nameFragment");

//        setUpFragment();
//        if (fragmentToBeLoad.length() > 1) {
//            lastEnabled = true;
//            setFragmentToLoad(fragmentToBeLoad, new ArrayList<>());
//            loadingQuizDetailFragment(fragmentToBeLoad);
//        } else {
//            lastEnabled = false;
//            lastFragment = activeFragment;
        fragmentManager = getSupportFragmentManager();

//        setFragmentToLoad("additionalFragment", new ArrayList<>());
        if (!askBasicDetails.equalsIgnoreCase("3")) {
            setFragmentToLoad("nameFragment", new ArrayList<>());
        } else if (!askGraduationDetails.equalsIgnoreCase("3")) {
            setFragmentToLoad("graduationFragment", new ArrayList<>());
        } else if (!askPostGraduationDetails.equalsIgnoreCase("3")) {
            setFragmentToLoad("postGraduationFragment", new ArrayList<>());
        } else if (!askWorkExperience.equalsIgnoreCase("3")) {
            setFragmentToLoad("workExperienceFragment", new ArrayList<>());
        } else if (!askXIIEducationDetails.equalsIgnoreCase("3")) {
            setFragmentToLoad("gradeTwelveFragment", new ArrayList<>());
        } else if (!askXEducationDetails.equalsIgnoreCase("3")) {
            setFragmentToLoad("gradeTenthFragment", new ArrayList<>());
        } else if (!askAdditionalInformation.equalsIgnoreCase("3")) {
            setFragmentToLoad("additionalFragment", new ArrayList<>());
        } else {
//                setFragmentToLoad("nameFragment", new ArrayList<>());
        }
//        }
    }

    private void checkingLastFragment() {

       /* if (activeFragment instanceof NameFragment) {
            loadedFragments = "nameFragment";
        } else if (activeFragment instanceof EmailFragment) {
            loadedFragments = "emailFragment";
        } else if (activeFragment instanceof GenderFragment) {
            loadedFragments = "genderFragment";
        } else if (activeFragment instanceof LocationFragment) {
            loadedFragments = "locationFragment";
        } else if (activeFragment instanceof GraduationFragment) {
            loadedFragments = "graduationFragment";
        } else if (activeFragment instanceof SpecialisationGraduationFragment) {
            loadedFragments = "graduationFragment";
        } else if (activeFragment instanceof GraduationYearFragment) {
            loadedFragments = "yearGraduationFragment";
        } else if (activeFragment instanceof GraduationScoreFragment) {
            loadedFragments = "graduationScoreFragment";
        } else if (activeFragment instanceof PostGraduationFragment) {
            loadedFragments = "postGraduationFragment";
        } else if (activeFragment instanceof SpecialisationPostGraduationFragment) {
            loadedFragments = "postGraduationFragment";
        } else if (activeFragment instanceof PostGraduationYearFragment) {
            loadedFragments = "postGraduationYearFragment";
        } else if (activeFragment instanceof PostGraduationScoreFragment) {
            loadedFragments = "postGraduationScoreFragment";
        } else if (activeFragment instanceof WorkExperienceFragment) {
            loadedFragments = "workExperienceFragment";
        } else if (activeFragment instanceof WorkYearExperienceFragment) {
            loadedFragments = "workExperienceFragment";
        } else if (activeFragment instanceof GradeTwelveFragment) {
            loadedFragments = "gradeTwelveFragment";
        } else if (activeFragment instanceof GradeTenthFragment) {
            loadedFragments = "gradeTenthFragment";
        } else if (activeFragment instanceof JobLocationFragment) {
            loadedFragments = "jobLocationFragment";
        } else {
            loadedFragments = "";
        }
        DataHolder.getInstance().getUserDataresponse.student_journey_status = loadedFragments;

        if (DataHolder.getInstance().getUserDataresponse.graduation_gpa_max == null) {
            DataHolder.getInstance().getUserDataresponse.graduation_gpa_max = "10";
        }
        if (DataHolder.getInstance().getUserDataresponse.pg_gpa_max == null) {
            DataHolder.getInstance().getUserDataresponse.pg_gpa_max = "10";
        }
        if (DataHolder.getInstance().getUserDataresponse.x_gpa_max == null) {
            DataHolder.getInstance().getUserDataresponse.x_gpa_max = "10";
        }
        if (DataHolder.getInstance().getUserDataresponse.xii_gpa_max == null) {
            DataHolder.getInstance().getUserDataresponse.xii_gpa_max = "10";
        }
        hiitingProfileDataApi();*/
    }

    @Override
    public void loadingQuizDetailFragmentWithSpecialisation(String type, ArrayList<String> specialization) {
        setFragmentToLoad(type, specialization);
    }

    private void setFragmentToLoad(String type, ArrayList<String> specialization) {
        if (type.equalsIgnoreCase("nameFragment")) {
            if (nameFragment == null) {
                nameFragment = NameFragment.newInstance(ProfileActivity.this);
//                fragmentManager.beginTransaction().add(R.id.frame_container, nameFragment, "nameFragment").hide(nameFragment).commitAllowingStateLoss();
            }
            replaceFragment(nameFragment);
        } else if (type.equalsIgnoreCase("emailFragment")) {
            if (emailFragment == null)
                emailFragment = EmailFragment.newInstance(ProfileActivity.this);
            replaceFragment(emailFragment);
        } else if (type.equalsIgnoreCase("genderFragment")) {
            if (genderFragment == null)
                genderFragment = GenderFragment.newInstance(ProfileActivity.this);
            replaceFragment(genderFragment);
        } else if (type.equalsIgnoreCase("locationFragment")) {
            if (locationFragment == null)
                locationFragment = LocationFragment.newInstance(ProfileActivity.this);
            replaceFragment(locationFragment);
        } else if (type.equalsIgnoreCase("graduationFragment")) {
            if (graduationFragment == null)
                graduationFragment = GraduationFragment.newInstance(ProfileActivity.this);
            replaceFragment(graduationFragment);
        } else if (type.equalsIgnoreCase("specialisationGraduationFragment")) {
            Fragment specialisationGraduationFragment = SpecialisationGraduationFragment.newInstance(ProfileActivity.this, specialization);
            replaceFragment(specialisationGraduationFragment);
        } else if (type.equalsIgnoreCase("yearGraduationFragment")) {
            Fragment yearGraduationFragment = GraduationYearFragment.newInstance(ProfileActivity.this);
            replaceFragment(yearGraduationFragment);
        } else if (type.equalsIgnoreCase("graduationScoreFragment")) {
            Fragment graduationScoreFragment = GraduationScoreFragment.newInstance(ProfileActivity.this);
            replaceFragment(graduationScoreFragment);
        } else if (type.equalsIgnoreCase("postGraduationFragment")) {
            if (postGraduationFragment == null)
                postGraduationFragment = PostGraduationFragment.newInstance(ProfileActivity.this);
            replaceFragment(postGraduationFragment);
        } else if (type.equalsIgnoreCase("specialisationPostGraduationFragment")) {
            Fragment specialisationPostGraduationFragment = SpecialisationPostGraduationFragment.newInstance(ProfileActivity.this, specialization);
            replaceFragment(specialisationPostGraduationFragment);
        } else if (type.equalsIgnoreCase("postGraduationYearFragment")) {
            Fragment postGraduationYearFragment = PostGraduationYearFragment.newInstance(ProfileActivity.this);
            replaceFragment(postGraduationYearFragment);
        } else if (type.equalsIgnoreCase("postGraduationScoreFragment")) {
            Fragment postGraduationScoreFragment = PostGraduationScoreFragment.newInstance(ProfileActivity.this);
            replaceFragment(postGraduationScoreFragment);
        } else if (type.equalsIgnoreCase("workExperienceFragment")) {
            if (workExperienceFragment == null)
                workExperienceFragment = WorkExperienceFragment.newInstance(ProfileActivity.this);
            replaceFragment(workExperienceFragment);
        } else if (type.equalsIgnoreCase("workYearExperienceFragment")) {
            if (workYearExperienceFragment == null)
                workYearExperienceFragment = WorkYearExperienceFragment.newInstance(ProfileActivity.this);
            replaceFragment(workYearExperienceFragment);
        } else if (type.equalsIgnoreCase("gradeTwelveFragment")) {
            if (gradeTwelveFragment == null)
                gradeTwelveFragment = GradeTwelveFragment.newInstance(ProfileActivity.this);
            replaceFragment(gradeTwelveFragment);
        } else if (type.equalsIgnoreCase("gradeTenthFragment")) {
            if (gradeTenthFragment == null)
                gradeTenthFragment = GradeTenthFragment.newInstance(ProfileActivity.this);
            replaceFragment(gradeTenthFragment);
        } else if (type.equalsIgnoreCase("jobLocationFragment")) {
            if (jobLocationFragment == null)
                jobLocationFragment = PreferredJobLocationFragment.newInstance(ProfileActivity.this);
            replaceFragment(jobLocationFragment);
        } else if (type.equalsIgnoreCase("additionalFragment")) {
            if (additionalFragment == null)
                additionalFragment = AdditionalFragment.newInstance(ProfileActivity.this);
            replaceFragment(additionalFragment);
        } else if (type.equalsIgnoreCase("finished")) {
            if (DataHolder.getInstance().getUserDataresponse.graduation_gpa_max == null) {
                DataHolder.getInstance().getUserDataresponse.graduation_gpa_max = "10";
            }
            if (DataHolder.getInstance().getUserDataresponse.pg_gpa_max == null) {
                DataHolder.getInstance().getUserDataresponse.pg_gpa_max = "10";
            }
            if (DataHolder.getInstance().getUserDataresponse.x_gpa_max == null) {
                DataHolder.getInstance().getUserDataresponse.x_gpa_max = "10";
            }
            if (DataHolder.getInstance().getUserDataresponse.xii_gpa_max == null) {
                DataHolder.getInstance().getUserDataresponse.xii_gpa_max = "10";
            }
            hiitingProfileDataApi();
        }
        loadedFragments = type;
       /* if (lastEnabled) {
            lastFragment = activeFragment;
            lastEnabled = false;
        }*/
    }

    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_container, fragment)
                .addToBackStack(fragment.toString()).commit();
    /*    if (activeFragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.hide(activeFragment).show(fragment).commit();
            activeFragment = fragment;
        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.show(fragment).commit();
            activeFragment = fragment;

        }*/
       /* FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment, fragment.toString());
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.commit();*/
    }

    private void setUpFragment() {
        fragmentManager = getSupportFragmentManager();

        nameFragment = NameFragment.newInstance(ProfileActivity.this);
//        emailFragment = EmailFragment.newInstance(ProfileActivity.this);
//        genderFragment = GenderFragment.newInstance(ProfileActivity.this);
//        locationFragment = LocationFragment.newInstance(ProfileActivity.this);
//
//        graduationFragment = GraduationFragment.newInstance(ProfileActivity.this);
//        specialisationGraduationFragment = SpecialisationGraduationFragment.newInstance(ProfileActivity.this);
//        yearGraduationFragment = GraduationYearFragment.newInstance(ProfileActivity.this);
//        graduationScoreFragment = GraduationScoreFragment.newInstance(ProfileActivity.this);
//
//        postGraduationFragment = PostGraduationFragment.newInstance(ProfileActivity.this);
//        specialisationPostGraduationFragment = SpecialisationPostGraduationFragment.newInstance(ProfileActivity.this);
//        postGraduationYearFragment = PostGraduationYearFragment.newInstance(ProfileActivity.this);
//        postGraduationScoreFragment = PostGraduationScoreFragment.newInstance(ProfileActivity.this);
//
//        workExperienceFragment = WorkExperienceFragment.newInstance(ProfileActivity.this);
//        workYearExperienceFragment = WorkYearExperienceFragment.newInstance(ProfileActivity.this);
//
//
//        gradeTwelveFragment = GradeTwelveFragment.newInstance(ProfileActivity.this);
//        gradeTenthFragment = GradeTenthFragment.newInstance(ProfileActivity.this);
//
//        jobLocationFragment = JobLocationFragment.newInstance(ProfileActivity.this);


        fragmentManager.beginTransaction().add(R.id.frame_container, nameFragment, "nameFragment").hide(nameFragment).commitAllowingStateLoss();
//        fragmentManager.beginTransaction().add(R.id.frame_container, emailFragment, "emailFragment").hide(emailFragment).commitAllowingStateLoss();
//        fragmentManager.beginTransaction().add(R.id.frame_container, genderFragment, "genderFragment").hide(genderFragment).commitAllowingStateLoss();
//
//        fragmentManager.beginTransaction().add(R.id.frame_container, locationFragment, "locationFragment").hide(locationFragment).commitAllowingStateLoss();
//
//        fragmentManager.beginTransaction().add(R.id.frame_container, graduationFragment, "graduationFragment").hide(graduationFragment).commitAllowingStateLoss();
//        fragmentManager.beginTransaction().add(R.id.frame_container, specialisationGraduationFragment, "specialisationGraduationFragment").hide(specialisationGraduationFragment).commitAllowingStateLoss();
//        fragmentManager.beginTransaction().add(R.id.frame_container, yearGraduationFragment, "yearGraduationFragment").hide(yearGraduationFragment).commitAllowingStateLoss();
//        fragmentManager.beginTransaction().add(R.id.frame_container, graduationScoreFragment, "graduationScoreFragment").hide(graduationScoreFragment).commitAllowingStateLoss();
//
//        fragmentManager.beginTransaction().add(R.id.frame_container, postGraduationFragment, "postGraduationFragment").hide(postGraduationFragment).commitAllowingStateLoss();
//        fragmentManager.beginTransaction().add(R.id.frame_container, specialisationPostGraduationFragment, "specialisationPostGraduationFragment").hide(specialisationPostGraduationFragment).commitAllowingStateLoss();
//        fragmentManager.beginTransaction().add(R.id.frame_container, postGraduationYearFragment, "postGraduationYearFragment").hide(postGraduationYearFragment).commitAllowingStateLoss();
//        fragmentManager.beginTransaction().add(R.id.frame_container, postGraduationScoreFragment, "postGraduationScoreFragment").hide(postGraduationScoreFragment).commitAllowingStateLoss();
//
//        fragmentManager.beginTransaction().add(R.id.frame_container, workExperienceFragment, "workExperienceFragment").hide(workExperienceFragment).commitAllowingStateLoss();
//        fragmentManager.beginTransaction().add(R.id.frame_container, workYearExperienceFragment, "workYearExperienceFragment").hide(workYearExperienceFragment).commitAllowingStateLoss();
//
//
//        fragmentManager.beginTransaction().add(R.id.frame_container, gradeTwelveFragment, "gradeTwelveFragment").hide(gradeTwelveFragment).commitAllowingStateLoss();
//        fragmentManager.beginTransaction().add(R.id.frame_container, gradeTenthFragment, "gradeTenthFragment").hide(gradeTenthFragment).commitAllowingStateLoss();
//
//        fragmentManager.beginTransaction().add(R.id.frame_container, jobLocationFragment, "jobLocationFragment").hide(jobLocationFragment).commitAllowingStateLoss();
       /* activeFragment = nameFragment;
        if (fragmentToBeLoad.length() > 1) {
            lastEnabled = true;
            loadingQuizDetailFragment(fragmentToBeLoad);
        } else {
            lastEnabled = false;
            lastFragment = activeFragment;
            loadingQuizDetailFragment("nameFragment");

        }*/
    }


    @Override
    public void loadingQuizDetailFragment(String type) {
        Helper.hideKeyboard(ProfileActivity.this);
        setFragmentToLoad(type, new ArrayList<>());
   /*     if (type.equalsIgnoreCase("nameFragment")) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.hide(activeFragment).show(nameFragment).commit();
            activeFragment = nameFragment;
        } else if (type.equalsIgnoreCase("emailFragment")) {
            setFragmentToLoad(type, new ArrayList<>());
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.hide(activeFragment).show(emailFragment).commit();
//            activeFragment = emailFragment;
        } else if (type.equalsIgnoreCase("genderFragment")) {
            setFragmentToLoad(type, new ArrayList<>());
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.hide(activeFragment).show(genderFragment).commit();
//            activeFragment = genderFragment;
        } else if (type.equalsIgnoreCase("locationFragment")) {
            setFragmentToLoad(type, new ArrayList<>());
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.hide(activeFragment).show(locationFragment).commit();
//            activeFragment = locationFragment;
        } else if (type.equalsIgnoreCase("graduationFragment")) {
            setFragmentToLoad(type, new ArrayList<>());
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.hide(activeFragment).show(graduationFragment).commit();
//            activeFragment = graduationFragment;
        } else if (type.equalsIgnoreCase("specialisationGraduationFragment")) {
            setFragmentToLoad(type, new ArrayList<>());
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.hide(activeFragment).show(specialisationGraduationFragment).commit();
//            activeFragment = specialisationGraduationFragment;
        } else if (type.equalsIgnoreCase("yearGraduationFragment")) {
            setFragmentToLoad(type, new ArrayList<>());
//            Intent intent = new Intent();
//            intent.setAction(getString(R.string.app_name) + "user.displayData");
//            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.hide(activeFragment).show(yearGraduationFragment).commit();
//            activeFragment = yearGraduationFragment;
        } else if (type.equalsIgnoreCase("graduationScoreFragment")) {
            setFragmentToLoad(type, new ArrayList<>());
//            Intent intent = new Intent();
//            intent.setAction(getString(R.string.app_name) + "user.displayData");
//            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.hide(activeFragment).show(graduationScoreFragment).commit();
//            activeFragment = graduationScoreFragment;
        } else if (type.equalsIgnoreCase("postGraduationFragment")) {
            setFragmentToLoad(type, new ArrayList<>());
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.hide(activeFragment).show(postGraduationFragment).commit();
//            activeFragment = postGraduationFragment;
        } else if (type.equalsIgnoreCase("specialisationPostGraduationFragment")) {
            setFragmentToLoad(type, new ArrayList<>());
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.hide(activeFragment).show(specialisationPostGraduationFragment).commit();
//            activeFragment = specialisationPostGraduationFragment;
        } else if (type.equalsIgnoreCase("postGraduationYearFragment")) {
            setFragmentToLoad(type, new ArrayList<>());
//            Intent intent = new Intent();
//            intent.setAction(getString(R.string.app_name) + "user.displayData");
//            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.hide(activeFragment).show(postGraduationYearFragment).commit();
//            activeFragment = postGraduationYearFragment;
        } else if (type.equalsIgnoreCase("postGraduationScoreFragment")) {
            setFragmentToLoad(type, new ArrayList<>());
//            Intent intent = new Intent();
//            intent.setAction(getString(R.string.app_name) + "user.displayData");
//            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.hide(activeFragment).show(postGraduationScoreFragment).commit();
//            activeFragment = postGraduationScoreFragment;
        } else if (type.equalsIgnoreCase("workExperienceFragment")) {
            setFragmentToLoad(type, new ArrayList<>());
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.hide(activeFragment).show(workExperienceFragment).commit();
//            activeFragment = workExperienceFragment;
        } else if (type.equalsIgnoreCase("workYearExperienceFragment")) {
            setFragmentToLoad(type, new ArrayList<>());
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.hide(activeFragment).show(workYearExperienceFragment).commit();
//            activeFragment = workYearExperienceFragment;
        } else if (type.equalsIgnoreCase("gradeTwelveFragment")) {
            setFragmentToLoad(type, new ArrayList<>());
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.hide(activeFragment).show(gradeTwelveFragment).commit();
//            activeFragment = gradeTwelveFragment;
        } else if (type.equalsIgnoreCase("gradeTenthFragment")) {
            setFragmentToLoad(type, new ArrayList<>());
//            Intent intent = new Intent();
//            intent.setAction(getString(R.string.app_name) + "user.displayData");
//            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.hide(activeFragment).show(gradeTenthFragment).commit();
//            activeFragment = gradeTenthFragment;
        } else if (type.equalsIgnoreCase("jobLocationFragment")) {
            setFragmentToLoad(type, new ArrayList<>());
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.hide(activeFragment).show(jobLocationFragment).commit();
//            activeFragment = jobLocationFragment;
        } else if (type.equalsIgnoreCase("finished")) {
            setFragmentToLoad(type, new ArrayList<>());
        }*/

      /*  if (lastEnabled) {
            lastFragment = activeFragment;
            lastEnabled = false;
        }*/
    }

    private void hiitingProfileDataApi() {
        progressDialog.show();
        JsonObject jsonObject = new JsonObject();
        try {
            if (DataHolder.getInstance().getUserDataresponse.name != null) {
                jsonObject.addProperty("name", DataHolder.getInstance().getUserDataresponse.name);
            }
            if (DataHolder.getInstance().getUserDataresponse.email != null) {
                jsonObject.addProperty("email", DataHolder.getInstance().getUserDataresponse.email);
            }
            if (DataHolder.getInstance().getUserDataresponse.gender != null) {
                jsonObject.addProperty("gender", DataHolder.getInstance().getUserDataresponse.gender);
            }
            if (DataHolder.getInstance().getUserDataresponse.pincode != null) {
                jsonObject.addProperty("pincode", DataHolder.getInstance().getUserDataresponse.pincode);
            }
            if (DataHolder.getInstance().getUserDataresponse.state != null) {
                jsonObject.addProperty("state", DataHolder.getInstance().getUserDataresponse.state);
            }
            if (DataHolder.getInstance().getUserDataresponse.city != null) {
                jsonObject.addProperty("city", DataHolder.getInstance().getUserDataresponse.city);
            }
            if (DataHolder.getInstance().getUserDataresponse.address != null) {
                jsonObject.addProperty("address", DataHolder.getInstance().getUserDataresponse.address);
            }
            if (DataHolder.getInstance().getUserDataresponse.graduation_degree != null) {
                jsonObject.addProperty("graduation_degree", DataHolder.getInstance().getUserDataresponse.graduation_degree);
            }
            if (DataHolder.getInstance().getUserDataresponse.graduation_specialisation != null) {
                jsonObject.addProperty("graduation_specialisation", DataHolder.getInstance().getUserDataresponse.graduation_specialisation);
            }
            if (DataHolder.getInstance().getUserDataresponse.graduation_year != null) {
                jsonObject.addProperty("graduation_year", DataHolder.getInstance().getUserDataresponse.graduation_year);
            }
            if (DataHolder.getInstance().getUserDataresponse.graduation_percentage != null) {
                jsonObject.addProperty("graduation_percentage", DataHolder.getInstance().getUserDataresponse.graduation_percentage);
            }
            if (DataHolder.getInstance().getUserDataresponse.graduation_gpa != null) {
                jsonObject.addProperty("graduation_gpa", DataHolder.getInstance().getUserDataresponse.graduation_gpa);
            }
            if (DataHolder.getInstance().getUserDataresponse.graduation_gpa_max != null) {
                jsonObject.addProperty("graduation_gpa_max", DataHolder.getInstance().getUserDataresponse.graduation_gpa_max);
            }
            if (DataHolder.getInstance().getUserDataresponse.pg_degree != null) {
                jsonObject.addProperty("pg_degree", DataHolder.getInstance().getUserDataresponse.pg_degree);
            }
            if (DataHolder.getInstance().getUserDataresponse.pg_specialisation != null) {
                jsonObject.addProperty("pg_specialisation", DataHolder.getInstance().getUserDataresponse.pg_specialisation);
            }
            if (DataHolder.getInstance().getUserDataresponse.pg_year != null) {
                jsonObject.addProperty("pg_year", DataHolder.getInstance().getUserDataresponse.pg_year);
            }
            if (DataHolder.getInstance().getUserDataresponse.pg_gpa != null) {
                jsonObject.addProperty("pg_gpa", DataHolder.getInstance().getUserDataresponse.pg_gpa);
            }
            if (DataHolder.getInstance().getUserDataresponse.pg_gpa_max != null) {
                jsonObject.addProperty("pg_gpa_max", DataHolder.getInstance().getUserDataresponse.pg_gpa_max);
            }
            if (DataHolder.getInstance().getUserDataresponse.pg_percentage != null) {
                jsonObject.addProperty("pg_percentage", DataHolder.getInstance().getUserDataresponse.pg_percentage);
            }
            if (DataHolder.getInstance().getUserDataresponse.work_experience != null) {
                jsonObject.addProperty("work_experience", DataHolder.getInstance().getUserDataresponse.work_experience);
            }
            if (DataHolder.getInstance().getUserDataresponse.xii_gpa != null) {
                jsonObject.addProperty("xii_gpa", DataHolder.getInstance().getUserDataresponse.xii_gpa);
            }
            if (DataHolder.getInstance().getUserDataresponse.xii_gpa_max != null) {
                jsonObject.addProperty("xii_gpa_max", DataHolder.getInstance().getUserDataresponse.xii_gpa_max);
            }
            if (DataHolder.getInstance().getUserDataresponse.x_percentage != null) {
                jsonObject.addProperty("xii_percentage", DataHolder.getInstance().getUserDataresponse.xii_percentage);
            }
            if (DataHolder.getInstance().getUserDataresponse.x_gpa != null) {
                jsonObject.addProperty("x_gpa", DataHolder.getInstance().getUserDataresponse.x_gpa);
            }
            if (DataHolder.getInstance().getUserDataresponse.x_gpa_max != null) {
                jsonObject.addProperty("x_gpa_max", DataHolder.getInstance().getUserDataresponse.x_gpa_max);
            }
            if (DataHolder.getInstance().getUserDataresponse.x_percentage != null) {
                jsonObject.addProperty("x_percentage", DataHolder.getInstance().getUserDataresponse.x_percentage);
            }
            if (DataHolder.getInstance().getUserDataresponse.preferred_location != null) {
                jsonObject.addProperty("preferred_location", DataHolder.getInstance().getUserDataresponse.preferred_location);
            }
            if (DataHolder.getInstance().getUserDataresponse.ask_driving_license != null) {
                jsonObject.addProperty("driving_license", DataHolder.getInstance().getUserDataresponse.ask_driving_license);
            }
            if (DataHolder.getInstance().getUserDataresponse.ask_laptop != null) {
                jsonObject.addProperty("laptop", DataHolder.getInstance().getUserDataresponse.ask_laptop);
            }
            if (DataHolder.getInstance().getUserDataresponse.ask_wifi != null) {
                jsonObject.addProperty("broadband", DataHolder.getInstance().getUserDataresponse.ask_wifi);
            }
            if (DataHolder.getInstance().getUserDataresponse.ask_2_wheeler != null) {
                jsonObject.addProperty("two_wheeler", DataHolder.getInstance().getUserDataresponse.ask_2_wheeler);
            }
            if (DataHolder.getInstance().getUserDataresponse.student_journey_status != null) {
                jsonObject.addProperty("student_journey_status", DataHolder.getInstance().getUserDataresponse.student_journey_status);
            }
            profileActivityPresenter.callUserProfileUploadApi(jsonObject);

        } catch (Exception e) {

        }
    }

    @Override
    public void onBackPressed() {
       /* if (activeFragment != lastFragment) {
            if (activeFragment instanceof NameFragment) {
                super.onBackPressed();
            } else if (activeFragment instanceof EmailFragment) {
                fragmentManager.beginTransaction().hide(activeFragment).show(nameFragment).commit();
                activeFragment = nameFragment;
            } else if (activeFragment instanceof GenderFragment) {
                fragmentManager.beginTransaction().hide(activeFragment).show(emailFragment).commit();
                activeFragment = emailFragment;
            } else if (activeFragment instanceof LocationFragment) {
                fragmentManager.beginTransaction().hide(activeFragment).show(genderFragment).commit();
                activeFragment = genderFragment;
            } else if (activeFragment instanceof GraduationFragment) {
                fragmentManager.beginTransaction().hide(activeFragment).show(locationFragment).commit();
                activeFragment = locationFragment;
            } else if (activeFragment instanceof SpecialisationGraduationFragment) {
                fragmentManager.beginTransaction().hide(activeFragment).show(graduationFragment).commit();
                activeFragment = graduationFragment;
            } else if (activeFragment instanceof GraduationYearFragment) {
                if (DataHolder.getInstance().getUserDataresponse.graduation_specialisation != null) {
                    fragmentManager.beginTransaction().hide(activeFragment).show(specialisationGraduationFragment).commit();
                    activeFragment = specialisationGraduationFragment;
                } else {
                    fragmentManager.beginTransaction().hide(activeFragment).show(graduationFragment).commit();
                    activeFragment = graduationFragment;
                }
            } else if (activeFragment instanceof GraduationScoreFragment) {
                fragmentManager.beginTransaction().hide(activeFragment).show(yearGraduationFragment).commit();
                activeFragment = yearGraduationFragment;
            } else if (activeFragment instanceof PostGraduationFragment) {
                fragmentManager.beginTransaction().hide(activeFragment).show(graduationScoreFragment).commit();
                activeFragment = graduationScoreFragment;
            } else if (activeFragment instanceof SpecialisationPostGraduationFragment) {
                fragmentManager.beginTransaction().hide(activeFragment).show(postGraduationFragment).commit();
                activeFragment = postGraduationFragment;
            } else if (activeFragment instanceof PostGraduationYearFragment) {
                if (DataHolder.getInstance().getUserDataresponse.pg_specialisation != null) {
                    fragmentManager.beginTransaction().hide(activeFragment).show(specialisationPostGraduationFragment).commit();
                    activeFragment = specialisationPostGraduationFragment;
                } else {
                    fragmentManager.beginTransaction().hide(activeFragment).show(postGraduationFragment).commit();
                    activeFragment = postGraduationFragment;
                }
            } else if (activeFragment instanceof PostGraduationScoreFragment) {
                fragmentManager.beginTransaction().hide(activeFragment).show(postGraduationYearFragment).commit();
                activeFragment = postGraduationYearFragment;
            } else if (activeFragment instanceof WorkExperienceFragment) {
                if (DataHolder.getInstance().getUserDataresponse.pg_degree == null) {
                    fragmentManager.beginTransaction().hide(activeFragment).show(postGraduationFragment).commit();
                    activeFragment = postGraduationFragment;
                } else {
                    fragmentManager.beginTransaction().hide(activeFragment).show(postGraduationScoreFragment).commit();
                    activeFragment = postGraduationScoreFragment;
                }
            } else if (activeFragment instanceof WorkYearExperienceFragment) {
                fragmentManager.beginTransaction().hide(activeFragment).show(workExperienceFragment).commit();
                activeFragment = workExperienceFragment;
            } else if (activeFragment instanceof GradeTwelveFragment) {
                if (DataHolder.getInstance().getUserDataresponse.work_experience != null && !DataHolder.getInstance().getUserDataresponse.work_experience.equalsIgnoreCase("fresher")) {
                    fragmentManager.beginTransaction().hide(activeFragment).show(workYearExperienceFragment).commit();
                    activeFragment = workYearExperienceFragment;
                } else {
                    fragmentManager.beginTransaction().hide(activeFragment).show(workExperienceFragment).commit();
                    activeFragment = workExperienceFragment;
                }
            } else if (activeFragment instanceof GradeTenthFragment) {
                fragmentManager.beginTransaction().hide(activeFragment).show(gradeTwelveFragment).commit();
                activeFragment = gradeTwelveFragment;
            } else if (activeFragment instanceof JobLocationFragment) {
                fragmentManager.beginTransaction().hide(activeFragment).show(gradeTenthFragment).commit();
                activeFragment = gradeTenthFragment;
            }
        } else {
            if (setActivityResult.equalsIgnoreCase("true")) {
                Intent returnIntent = new Intent();
                setResult(3000, returnIntent);
                finish();
            } else if (setActivityResult.equalsIgnoreCase("profile")) {
                Intent returnIntent = new Intent();
                setResult(5003, returnIntent);
                finish();
            } else {
                startActivity(new Intent(ProfileActivity.this, DashboardActivity.class).addFlags(Intent.
                        FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        }*/
        Log.e("fragmentcount", getSupportFragmentManager().getBackStackEntryCount() + " s" + loadedFragments);
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            if (setActivityResult.equalsIgnoreCase("true")) {
                Intent returnIntent = new Intent();
                setResult(3000, returnIntent);
                finish();
            } else if (setActivityResult.equalsIgnoreCase("profile")) {
                Intent returnIntent = new Intent();
                setResult(5003, returnIntent);
                finish();
            } else {
                startActivity(new Intent(ProfileActivity.this, DashboardActivity.class).addFlags(Intent.
                        FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
            }
            super.onBackPressed();
        } else {
//            getFragmentManager().popBackStackImmediate();
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5005) {
            Intent returnIntent = new Intent();
            setResult(3000, returnIntent);
            finish();
        }
    }

    @Override
    public void setProfileDataResponse(UserProfileUpDataResponse userProfileUpDataResponse) {
        if (userProfileUpDataResponse != null & userProfileUpDataResponse.success) {
            if (!aggrementSigned) {
                startActivityForResult(new Intent(ProfileActivity.this,
                        EnrollActivity.class).putExtra("jobId", jobId)
                        .putExtra("applyJob", true)
                        .putExtra("loginRequired", "false"), 5005);

            } else if (setActivityResult.equalsIgnoreCase("true")) {
                Intent returnIntent = new Intent();
                setResult(3000, returnIntent);
                finish();
            } else if (setActivityResult.equalsIgnoreCase("profile")) {
                profileActivityPresenter.callApplyOpportunity(jobId);
            } else {
                startActivity(new Intent(ProfileActivity.this, DashboardActivity.class).addFlags(Intent.
                        FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
            }
            progressDialog.dismiss();

        }
    }

    @Override
    public void setApplyOpportunityResponse(ApplyOpportunityResponse applyOpportunityResponse) {
        if (applyOpportunityResponse != null && applyOpportunityResponse.success) {
            if (ask_preference) {
                Intent intent = new Intent(ProfileActivity.this, JobApplyLocationSelectActivity.class);
                intent.putStringArrayListExtra("locations", showLocation);
                intent.putExtra("jobId", jobId);
                intent.putExtra("toolbarTitle", toolbarTitle);
                intent.putExtra("salaryDetails", salaryDetails);
                startActivity(intent);
            } else {
                startActivity(new Intent(ProfileActivity.this, SuccessActivity.class).putExtra("jobId", jobId));
            }
        } else {
            showMessage(applyOpportunityResponse.message);
        }
    }

    @Override
    public void showLoader() {
    }

    @Override
    public void hideLoader() {
    }

    @Override
    public void showMessage(String message) {

    }
}