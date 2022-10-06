package com.myfutureapp.profile.ui;

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

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myfutureapp.R;
import com.myfutureapp.login.DataHolder;
import com.myfutureapp.profile.adapter.DivisionLocationAdapter;
import com.myfutureapp.profile.model.StateWithRegionResponse;
import com.myfutureapp.profile.presenter.JobLocationFragmentPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PreferredJobLocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreferredJobLocationFragment extends Fragment implements JobLocationFragmentView {

    private static HandleFragmentLoading handleFragmentLoading;
    static private LinearLayout cityMainLL, cityunselected;
    static private CardView citySelected;
    private static boolean allIndiaSelected = false;
    List<StateWithRegionResponse.StateRegionResponse> stateRegionResponseList = new ArrayList<>();
    DivisionLocationAdapter divisionLocationAdapter;
    private TextView eastArea, northArea, centralArea, westArea, southArea;
    private RecyclerView jobLocationRV;
    private TextView contineJobLogin;
    private StateWithRegionResponse stateWithRegionResponses;
    private View view;
    private boolean btnEnable = false;

    public PreferredJobLocationFragment() {
        // Required empty public constructor
    }


    public static PreferredJobLocationFragment newInstance(HandleFragmentLoading handleFragmentLoadings) {
        PreferredJobLocationFragment fragment = new PreferredJobLocationFragment();
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
            view = inflater.inflate(R.layout.fragment_preferred_job_location, container, false);
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

        jobLocationRV = view.findViewById(R.id.jobLocationRV);

        cityMainLL = view.findViewById(R.id.cityMainLL);
        cityunselected = view.findViewById(R.id.cityunselected);
        citySelected = view.findViewById(R.id.citySelected);

        contineJobLogin = view.findViewById(R.id.contineJobLogin);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        jobLocationRV.setNestedScrollingEnabled(false);
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


        northArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                northArea.setTextColor(getResources().getColor(R.color.black));
                eastArea.setTextColor(getResources().getColor(R.color.warm_gray));
                centralArea.setTextColor(getResources().getColor(R.color.warm_gray));
                westArea.setTextColor(getResources().getColor(R.color.warm_gray));
                southArea.setTextColor(getResources().getColor(R.color.warm_gray));
                jobLocationRV.scrollToPosition(0);

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
                jobLocationRV.scrollToPosition(1);
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
                jobLocationRV.scrollToPosition(2);
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
                jobLocationRV.scrollToPosition(3);
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
                jobLocationRV.scrollToPosition(4);
            }
        });

        cityMainLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cityunselected.getVisibility() == View.VISIBLE) {
                    contineJobLogin.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_dark_black_btn));
                    btnEnable = true;

                    allIndiaSelected = true;
                    cityunselected.setVisibility(View.GONE);
                    citySelected.setVisibility(View.VISIBLE);
                    for (int i = 0; i < stateRegionResponseList.size(); i++) {
                        for (int j = 0; j < stateRegionResponseList.get(i).state.size(); j++) {
                            stateRegionResponseList.get(i).state.get(j).citySelected = false;
                        }
                    }
                    divisionLocationAdapter.notifyDataSetChanged();
                } else {
                    contineJobLogin.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_greish_btn_disable));
                    btnEnable = false;
                    settingAllIndiaButton();
                }
            }
        });

        String nextAction = checkingFragmentAvailble();
        if (nextAction.equalsIgnoreCase("finished")) {
            contineJobLogin.setText("Complete");
        }
        contineJobLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnEnable) {
                    if (allIndiaSelected) {
                        Log.e("citySelected", allIndiaSelected + "   a");
                        settingDataToUpdate("-1", nextAction);
                    } else {
                        String selectCity = "";
                        for (int i = 0; i < stateRegionResponseList.size(); i++) {
                            for (int j = 0; j < stateRegionResponseList.get(i).state.size(); j++) {
                                if (stateRegionResponseList.get(i).state.get(j).citySelected) {
                                    if (selectCity.length() == 0) {
                                        selectCity = String.valueOf(stateRegionResponseList.get(i).state.get(j).id);
                                    } else {
                                        selectCity = selectCity + "," + stateRegionResponseList.get(i).state.get(j).id;
                                    }
                                }
                            }
                        }

                        Log.e("citySelected", selectCity + "   a");
                        if (selectCity.length() <= 0) {
                            showMessage("Please select city");
                        } else {
                            settingDataToUpdate(selectCity, nextAction);
                        }
                    }
                }
            }
        });


        jobLocationFragmentPresenter.callStateRegionListApi();
    }

    public void settingAllIndiaButton() {
        allIndiaSelected = false;
        cityunselected.setVisibility(View.VISIBLE);
        citySelected.setVisibility(View.GONE);
    }

    private void settingDataToUpdate(String value, String nextAction) {
        DataHolder.getInstance().getUserDataresponse.preferred_location = value;
        DataHolder.getInstance().getUserDataresponse.student_journey_status = "finished";
        handleFragmentLoading.loadingQuizDetailFragment(nextAction);
    }

    private String checkingFragmentAvailble() {
        if (!((ProfileActivity) getActivity()).askGraduationDetails.equalsIgnoreCase("3")) {
            return "graduationFragment";
        } else if (!((ProfileActivity) getActivity()).askPostGraduationDetails.equalsIgnoreCase("3")) {
            return "postGraduationFragment";
        } else if (!((ProfileActivity) getActivity()).askWorkExperience.equalsIgnoreCase("3")) {
            return "workExperienceFragment";
        } else if (!((ProfileActivity) getActivity()).askXIIEducationDetails.equalsIgnoreCase("3")) {
            return "gradeTwelveFragment";
        } else if (!((ProfileActivity) getActivity()).askXEducationDetails.equalsIgnoreCase("3")) {
            return "gradeTenthFragment";
        } else if (!((ProfileActivity) getActivity()).askAdditionalInformation.equalsIgnoreCase("3")) {
            return "additionalFragment";
        } else {
            return "finished";
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
            stateWithRegionResponses = stateWithRegionResponse;
            stateRegionResponseList.addAll(stateWithRegionResponse.data);
            divisionLocationAdapter = new DivisionLocationAdapter(getContext(), stateWithRegionResponses.data, new DivisionLocationAdapter.ViewLoaded() {
                @Override
                public void viewLoadedRV(int position) {
                    Log.e("ositionView", position + "a");
                    viewSelected(position);
                }

                @Override
                public void allIndiaDisable() {
                    String selectCity = "";
                    for (int i = 0; i < stateRegionResponseList.size(); i++) {
                        for (int j = 0; j < stateRegionResponseList.get(i).state.size(); j++) {
                            if (stateRegionResponseList.get(i).state.get(j).citySelected) {
                                if (selectCity.length() == 0) {
                                    selectCity = String.valueOf(stateRegionResponseList.get(i).state.get(j).id);
                                } else {
                                    selectCity = selectCity + "," + stateRegionResponseList.get(i).state.get(j).id;
                                }
                            }
                        }
                    }

                    Log.e("citySelected", selectCity + "   a");

                    if (selectCity.length() <= 0) {
                        contineJobLogin.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_greish_btn_disable));
                        btnEnable = false;
                    } else {
                        contineJobLogin.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_dark_black_btn));
                        btnEnable = true;
                    }
                    settingAllIndiaButton();
                }
            });
            divisionLocationAdapter.setHasStableIds(true);
            jobLocationRV.setAdapter(divisionLocationAdapter);
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