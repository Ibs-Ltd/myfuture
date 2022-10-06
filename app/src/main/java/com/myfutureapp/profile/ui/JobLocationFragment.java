package com.myfutureapp.profile.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.myfutureapp.R;
import com.myfutureapp.login.DataHolder;
import com.myfutureapp.profile.adapter.DivisionLocationAdapter;
import com.myfutureapp.profile.jobLocationPrefferd.FragmentCentralArea;
import com.myfutureapp.profile.jobLocationPrefferd.FragmentEastArea;
import com.myfutureapp.profile.jobLocationPrefferd.FragmentNorthArea;
import com.myfutureapp.profile.jobLocationPrefferd.FragmentSouthArea;
import com.myfutureapp.profile.jobLocationPrefferd.FragmentWestArea;
import com.myfutureapp.profile.jobLocationPrefferd.adapter.ViewPagerAdapter;
import com.myfutureapp.profile.model.StateWithRegionResponse;
import com.myfutureapp.profile.presenter.JobLocationFragmentPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JobLocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JobLocationFragment extends Fragment implements JobLocationFragmentView {

    private static HandleFragmentLoading handleFragmentLoading;
    static private LinearLayout cityMainLL, cityunselected;
    static private CardView citySelected;
    private static boolean allIndiaSelected = false;
    List<StateWithRegionResponse.StateRegionResponse> stateRegionResponseList = new ArrayList<>();
    DivisionLocationAdapter divisionLocationAdapter;
    private TextView eastArea, northArea, centralArea, westArea, southArea;
    //    private RecyclerView jobLocationRV;
    private TextView contineJobLogin;
    private StateWithRegionResponse stateWithRegionResponses;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private View view;

    public JobLocationFragment() {
        // Required empty public constructor
    }

    public static void settingAllIndiaButton() {
        allIndiaSelected = false;
        cityunselected.setVisibility(View.VISIBLE);
        citySelected.setVisibility(View.GONE);
    }

    public static JobLocationFragment newInstance(HandleFragmentLoading handleFragmentLoadings) {
        JobLocationFragment fragment = new JobLocationFragment();
        handleFragmentLoading = handleFragmentLoadings;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_job_location, container, false);
            inUi(view);
            return view;
        }
        return view;
    }


    private void inUi(View view) {
        JobLocationFragmentPresenter jobLocationFragmentPresenter = new JobLocationFragmentPresenter(getContext(), this);
        SeekBar seekBar = view.findViewById(R.id.seekBar);
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        northArea = view.findViewById(R.id.northArea);
        eastArea = view.findViewById(R.id.eastArea);
        centralArea = view.findViewById(R.id.centralArea);
        westArea = view.findViewById(R.id.westArea);
        southArea = view.findViewById(R.id.southArea);

//        jobLocationRV = view.findViewById(R.id.jobLocationRV);

        cityMainLL = view.findViewById(R.id.cityMainLL);
        cityunselected = view.findViewById(R.id.cityunselected);
        citySelected = view.findViewById(R.id.citySelected);

        contineJobLogin = view.findViewById(R.id.contineJobLogin);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
      /*  jobLocationRV.setNestedScrollingEnabled(false);
        jobLocationRV.setHasFixedSize(true);
        jobLocationRV.setLayoutManager(linearLayoutManager);
        jobLocationRV.setItemAnimator(new DefaultItemAnimator());

        jobLocationRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
*/
        northArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                northArea.setTextColor(getResources().getColor(R.color.black));
                eastArea.setTextColor(getResources().getColor(R.color.warm_gray));
                centralArea.setTextColor(getResources().getColor(R.color.warm_gray));
                westArea.setTextColor(getResources().getColor(R.color.warm_gray));
                southArea.setTextColor(getResources().getColor(R.color.warm_gray));
//                jobLocationRV.scrollToPosition(0);

            }
        });

        eastArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                northArea.setTextColor(getResources().getColor(R.color.warm_gray));
                eastArea.setTextColor(getResources().getColor(R.color.black));
                centralArea.setTextColor(getResources().getColor(R.color.warm_gray));
                westArea.setTextColor(getResources().getColor(R.color.warm_gray));
                southArea.setTextColor(getResources().getColor(R.color.warm_gray));
//                jobLocationRV.scrollToPosition(1);
            }
        });

        centralArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                northArea.setTextColor(getResources().getColor(R.color.warm_gray));
                eastArea.setTextColor(getResources().getColor(R.color.warm_gray));
                centralArea.setTextColor(getResources().getColor(R.color.black));
                westArea.setTextColor(getResources().getColor(R.color.warm_gray));
                southArea.setTextColor(getResources().getColor(R.color.warm_gray));
//                jobLocationRV.scrollToPosition(2);
            }
        });

        southArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                northArea.setTextColor(getResources().getColor(R.color.warm_gray));
                eastArea.setTextColor(getResources().getColor(R.color.warm_gray));
                centralArea.setTextColor(getResources().getColor(R.color.warm_gray));
                westArea.setTextColor(getResources().getColor(R.color.warm_gray));
                southArea.setTextColor(getResources().getColor(R.color.black));
//                jobLocationRV.scrollToPosition(3);
            }
        });

        westArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                northArea.setTextColor(getResources().getColor(R.color.warm_gray));
                eastArea.setTextColor(getResources().getColor(R.color.warm_gray));
                centralArea.setTextColor(getResources().getColor(R.color.warm_gray));
                westArea.setTextColor(getResources().getColor(R.color.black));
                southArea.setTextColor(getResources().getColor(R.color.warm_gray));
//                jobLocationRV.scrollToPosition(4);
            }
        });

        cityMainLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cityunselected.getVisibility() == View.VISIBLE) {
                    allIndiaSelected = true;
                    cityunselected.setVisibility(View.GONE);
                    citySelected.setVisibility(View.VISIBLE);
                    Intent intent = new Intent();
                    intent.setAction(getString(R.string.app_name) + "user.all.India.Select");
                    LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
                } else {
                    settingAllIndiaButton();
                }
            }
        });


        contineJobLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allIndiaSelected) {
                    settingDataToUpdate("-1");
                } else {
                    String selectCity = "";
                    if (FragmentNorthArea.stateResponses != null) {
                        for (int i = 0; i < FragmentNorthArea.stateResponses.size(); i++) {
                            if (FragmentNorthArea.stateResponses.get(i).citySelected) {
                                if (selectCity.length() == 0) {
                                    selectCity = String.valueOf(FragmentNorthArea.stateResponses.get(i).id);
                                } else {
                                    selectCity = selectCity + "," + FragmentNorthArea.stateResponses.get(i).id;
                                }
                            }
                        }
                    }

                    if (FragmentWestArea.stateResponses != null) {
                        for (int i = 0; i < FragmentWestArea.stateResponses.size(); i++) {
                            if (FragmentWestArea.stateResponses.get(i).citySelected) {
                                if (selectCity.length() == 0) {
                                    selectCity = String.valueOf(FragmentWestArea.stateResponses.get(i).id);
                                } else {
                                    selectCity = selectCity + "," + FragmentWestArea.stateResponses.get(i).id;
                                }
                            }
                        }
                    }

                    if (FragmentCentralArea.stateResponses != null) {
                        for (int i = 0; i < FragmentCentralArea.stateResponses.size(); i++) {
                            if (FragmentCentralArea.stateResponses.get(i).citySelected) {
                                if (selectCity.length() == 0) {
                                    selectCity = String.valueOf(FragmentCentralArea.stateResponses.get(i).id);
                                } else {
                                    selectCity = selectCity + "," + FragmentCentralArea.stateResponses.get(i).id;
                                }
                            }
                        }
                    }

                    if (FragmentSouthArea.stateResponses != null) {
                        for (int i = 0; i < FragmentSouthArea.stateResponses.size(); i++) {
                            if (FragmentSouthArea.stateResponses.get(i).citySelected) {
                                if (selectCity.length() == 0) {
                                    selectCity = String.valueOf(FragmentSouthArea.stateResponses.get(i).id);
                                } else {
                                    selectCity = selectCity + "," + FragmentSouthArea.stateResponses.get(i).id;
                                }
                            }
                        }
                    }

                    if (FragmentEastArea.stateResponses != null) {
                        for (int i = 0; i < FragmentEastArea.stateResponses.size(); i++) {
                            if (FragmentEastArea.stateResponses.get(i).citySelected) {
                                if (selectCity.length() == 0) {
                                    selectCity = String.valueOf(FragmentEastArea.stateResponses.get(i).id);
                                } else {
                                    selectCity = selectCity + "," + FragmentEastArea.stateResponses.get(i).id;
                                }
                            }
                        }
                    }
                    Log.e("citySelected", selectCity + "   a");
                    if (selectCity.length() <= 0) {
                        showMessage("Please select city");
                    } else {
                        settingDataToUpdate(selectCity);

                    }
                }


            }
        });

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);

        jobLocationFragmentPresenter.callStateRegionListApi();
    }

    private void settingDataToUpdate(String value) {
        DataHolder.getInstance().getUserDataresponse.preferred_location = value;
        DataHolder.getInstance().getUserDataresponse.student_journey_status = "finished";
        if (!((ProfileActivity) getActivity()).askGraduationDetails.equalsIgnoreCase("3")) {
            handleFragmentLoading.loadingQuizDetailFragment("graduationFragment");
        } else if (!((ProfileActivity) getActivity()).askPostGraduationDetails.equalsIgnoreCase("3")) {
            handleFragmentLoading.loadingQuizDetailFragment("postGraduationFragment");
        } else if (!((ProfileActivity) getActivity()).askWorkExperience.equalsIgnoreCase("3")) {
            handleFragmentLoading.loadingQuizDetailFragment("workExperienceFragment");
        } else if (!((ProfileActivity) getActivity()).askXIIEducationDetails.equalsIgnoreCase("3")) {
            handleFragmentLoading.loadingQuizDetailFragment("gradeTwelveFragment");
        } else if (!((ProfileActivity) getActivity()).askXEducationDetails.equalsIgnoreCase("3")) {
            handleFragmentLoading.loadingQuizDetailFragment("gradeTenthFragment");
        } else {
            handleFragmentLoading.loadingQuizDetailFragment("finished");
        }
    }

    private void viewSelected(int position) {
        if (position == 0) {
            northArea.setTextColor(getResources().getColor(R.color.black));
            eastArea.setTextColor(getResources().getColor(R.color.warm_gray));
            centralArea.setTextColor(getResources().getColor(R.color.warm_gray));
            westArea.setTextColor(getResources().getColor(R.color.warm_gray));
            southArea.setTextColor(getResources().getColor(R.color.warm_gray));
        } else if (position == 1) {
            northArea.setTextColor(getResources().getColor(R.color.warm_gray));
            eastArea.setTextColor(getResources().getColor(R.color.black));
            centralArea.setTextColor(getResources().getColor(R.color.warm_gray));
            westArea.setTextColor(getResources().getColor(R.color.warm_gray));
            southArea.setTextColor(getResources().getColor(R.color.warm_gray));
        } else if (position == 2) {
            northArea.setTextColor(getResources().getColor(R.color.warm_gray));
            eastArea.setTextColor(getResources().getColor(R.color.warm_gray));
            centralArea.setTextColor(getResources().getColor(R.color.black));
            westArea.setTextColor(getResources().getColor(R.color.warm_gray));
            southArea.setTextColor(getResources().getColor(R.color.warm_gray));
        } else if (position == 3) {
            northArea.setTextColor(getResources().getColor(R.color.warm_gray));
            eastArea.setTextColor(getResources().getColor(R.color.warm_gray));
            centralArea.setTextColor(getResources().getColor(R.color.warm_gray));
            westArea.setTextColor(getResources().getColor(R.color.warm_gray));
            southArea.setTextColor(getResources().getColor(R.color.black));
        } else if (position == 4) {
            northArea.setTextColor(getResources().getColor(R.color.warm_gray));
            eastArea.setTextColor(getResources().getColor(R.color.warm_gray));
            centralArea.setTextColor(getResources().getColor(R.color.warm_gray));
            westArea.setTextColor(getResources().getColor(R.color.black));
            southArea.setTextColor(getResources().getColor(R.color.warm_gray));
        }
    }

    @Override
    public void setStateRegionListResponse(StateWithRegionResponse stateWithRegionResponse) {
        if (stateWithRegionResponse != null && stateWithRegionResponse.success) {

            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), stateWithRegionResponse);
            viewPager.setAdapter(viewPagerAdapter);
            tabLayout.setupWithViewPager(viewPager);

          /*  stateWithRegionResponses = stateWithRegionResponse;
            stateRegionResponseList.addAll(stateWithRegionResponse.data);
            divisionLocationAdapter = new DivisionLocationAdapter(getContext(), stateWithRegionResponses.data, new DivisionLocationAdapter.ViewLoaded() {
                @Override
                public void viewLoadedRV(int position) {
                    viewSelected(position);
                }
            });*/
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
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}