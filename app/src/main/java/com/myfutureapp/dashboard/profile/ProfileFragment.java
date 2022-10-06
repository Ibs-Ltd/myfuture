package com.myfutureapp.dashboard.profile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.myfutureapp.R;
import com.myfutureapp.dashboard.model.UserProfileResponse;
import com.myfutureapp.dashboard.profile.model.AddEditWorkExperienceResponse;
import com.myfutureapp.dashboard.profile.model.DeleteWorkExperinceModel;
import com.myfutureapp.dashboard.profile.model.ProfileCityListRsponseModel;
import com.myfutureapp.dashboard.profile.model.ProfileStateListResponseModel;
import com.myfutureapp.dashboard.profile.model.UpdateLocationPreferenceResponse;
import com.myfutureapp.dashboard.profile.presenter.ProfileFragmentPresenter;
import com.myfutureapp.dashboard.profile.ui.CityListAdapter;
import com.myfutureapp.dashboard.profile.ui.EmploymentDetailsActivity;
import com.myfutureapp.dashboard.profile.ui.ProfileFragmentView;
import com.myfutureapp.dashboard.profile.ui.StateListAdapter;
import com.myfutureapp.login.DataHolder;
import com.myfutureapp.login.LoginActivity;
import com.myfutureapp.profile.model.UserProfileUpDataResponse;
import com.myfutureapp.util.AppConstants;
import com.myfutureapp.util.AppPreferences;
import com.myfutureapp.util.OnItemClickEvent;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.myfutureapp.util.AnimationSinglton.collapse;
import static com.myfutureapp.util.AnimationSinglton.expandView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements ProfileFragmentView, OnItemClickEvent, View.OnClickListener {

    CircleImageView ivProfilePic;
    TextView tvUserName;
    TextView tvMbile;
    EditText etEmail;
    EditText etWhatsappNumber;
    TextView tvMale;
    TextView tvFemaile;
    TextView tvOthers;
    TextView etState;
    TextView etCity;
    EditText etClass10thpercent;
    EditText etClass12thpercent;
    EditText etGraduationDegree;
    EditText etGraduationYear;
    EditText etGraduationPercentage;
    EditText etGraduationSpecialisation;
    EditText etPostgraduationDegree;
    EditText etPostgraduationYear;
    EditText etPostgraduationPercentage;
    EditText etPostgraduationSpecialisation;
    EditText etParentsName;
    EditText etParentRelation;
    EditText etparentContactNumber;
    EditText etpermanentAddress;
    RecyclerView rvRecyclerworkexp;
    TextView tvSubmitBasicDetail;
    TextView tvSubmitAcademicDetail;
    TextView tvSubmitPersonalDetail;
    LinearLayout llBasicDetails;
    LinearLayout llAcademicDetails;
    LinearLayout llPersonalDetails;
    LinearLayout llWorkExperience;
    LinearLayout loginLL;
    LinearLayout profileLL;
    RecyclerView.LayoutManager layoutManager, layoutManager1;
    String strUserName, strMbile, strEmail, strWhatsappNumber, strGender, strState, strCity, strClass10thpercent, strClass12thpercent, strGraduationDegree,
            strGraduationYear, strGraduationPercentage, strGraduationSpecialisation, strPostgraduationDegree, strPostgraduationYear, strPostgraduationPercentage, strPostgraduationSpecialisation,
            strParentsName, strParentRelation, strparentContactNumber, strpermanentAddress, strstateid, strstateids, strcityid;
    Spinner spinstate, spincity;
    ImageView iv_statename, iv_cityname;
    String[] stateList;
    String[] cityList;
    boolean state_flag = false;
    boolean city_flag = false;
    List<ProfileStateListResponseModel.ProfileStateListDataModel> updatedStateList = new ArrayList<>();
    List<ProfileCityListRsponseModel.ProfileCityListDataModel> updatedCityList = new ArrayList<>();
    TextView tv_nameofstate;
    List<ProfileStateListResponseModel.ProfileStateListDataModel> statelistG;
    List<ProfileCityListRsponseModel.ProfileCityListDataModel> citrylistG;
    RelativeLayout rlForCityrecyler, rlForStaterecyler;
    RecyclerView rvCitylist, rvState;
    EditText etSelectState, etSelectCity;
    private boolean basicButtonEnable = false, acdemicButtonEnable = false, personalButtonEnable = false;
    private ProfileWorkExperienceAdapter profileWorkExperienceAdapter;
    private ProfileFragmentPresenter profileFragmentPresenter;
    private boolean searchedState = false;
    private boolean searchedCity = false;
    private StateListAdapter stateListAdapter;
    private CityListAdapter cityListAdapter;
    private BroadcastReceiver mReceiver;


    public ProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        bindView(view);
        setClickListeners(view);
        spinstate = (Spinner) view.findViewById(R.id.spinstate);
        spincity = (Spinner) view.findViewById(R.id.spinCity);
        iv_statename = (ImageView) view.findViewById(R.id.iv_state);
        iv_cityname = (ImageView) view.findViewById(R.id.iv_city);
        rlForCityrecyler = (RelativeLayout) view.findViewById(R.id.ll_forcityrecyler);
        rvCitylist = (RecyclerView) view.findViewById(R.id.rv_citylist);
        rvState = (RecyclerView) view.findViewById(R.id.rv_state);
        rlForStaterecyler = (RelativeLayout) view.findViewById(R.id.ll_forstaterecyler);
        etSelectState = (EditText) view.findViewById(R.id.et_select_state);
        etSelectCity = (EditText) view.findViewById(R.id.et_select_city);

        profileFragmentPresenter = new ProfileFragmentPresenter(getContext(), ProfileFragment.this);

        profileFragmentPresenter.callProfileStateListApi();
        receiverRegister();

        /// tv_nameofstate=view.findViewById(R.id.tv_nameofstate);

        if (DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.stateData != null && DataHolder.getInstance().getUserProfileDataresponse.data.stateData.name != null) {
            strstateids = String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.stateData.name);
            etState.setText(strstateids);
        } else {
            strstateids = "Select any state";
        }
        if (DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.cityData != null && DataHolder.getInstance().getUserProfileDataresponse.data.cityData.name != null) {
            strcityid = String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.cityData.name);
            etCity.setText(strcityid);
        } else {
            strstateids = "Select any state";
        }
        if (strstateids != null) {
            profileFragmentPresenter.callProfileCityListApi(strstateids);
        }
        //spinnersetup();
        loginLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), LoginActivity.class).addFlags(Intent.
                        FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });
        etSelectState.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    searchedState = true;
                    updatedStateList.clear();
                    for (int i = 0; i < statelistG.size(); i++) {
                        if (statelistG.get(i).name.toLowerCase().contains(s.toString().toLowerCase())) {
                            updatedStateList.add(statelistG.get(i));
                        }
                    }
                    stateListAdapter.clearAll();
                    stateListAdapter.notifyItem(updatedStateList);
                } else {
                    updatedStateList.clear();
                    searchedState = false;

                    stateListAdapter.clearAll();
                    stateListAdapter.notifyItem(statelistG);
                }
            }
        });

        etSelectCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    searchedCity = true;
                    updatedCityList.clear();
                    for (int i = 0; i < citrylistG.size(); i++) {
                        if (citrylistG.get(i).name.toLowerCase().contains(s.toString().toLowerCase())) {
                            updatedCityList.add(citrylistG.get(i));
                        }
                    }
                    cityListAdapter.clearAll();
                    cityListAdapter.notifyItem(updatedCityList);
                } else {
                    searchedCity = false;
                    updatedCityList.clear();

                    cityListAdapter.clearAll();
                    cityListAdapter.notifyItem(citrylistG);
                }
            }
        });

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        rvState.setLayoutManager(layoutManager1);
        rvState.hasFixedSize();
        rvState.setVerticalScrollBarEnabled(true);

        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        rvCitylist.setLayoutManager(layoutManager2);
        rvCitylist.hasFixedSize();
        rvCitylist.setVerticalScrollBarEnabled(true);
        iv_statename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state_flag == false) {
                    if (city_flag) {
                        collapse(rlForCityrecyler);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                expandView(rlForStaterecyler);

                            }
                        }, 500);

                    } else {
                        expandView(rlForStaterecyler);

                    }
                    // ivCountryDrpDwnIcon.animate().rotation(180).setDuration(400);
                    state_flag = true;
                    city_flag = false;

                } else {
                    collapse(rlForStaterecyler);
                    //  ivCountryDrpDwnIcon.animate().rotation(0).setDuration(400);
                    state_flag = false;


                }
            }
        });


        iv_cityname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (strstateid != null) {
                    if (city_flag == false) {
                        expandView(rlForCityrecyler);
                        // ivCountryDrpDwnIcon.animate().rotation(180).setDuration(400);
                        city_flag = true;

                    } else {
                        collapse(rlForCityrecyler);
                        // ivCountryDrpDwnIcon.animate().rotation(0).setDuration(400);
                        city_flag = false;


                    }
                } else {
                    showMessage("please select state");
                }
            }
        });
        inui();


        return view;
    }

    private void bindView(View view) {
        ivProfilePic = view.findViewById(R.id.iv_profilePic);
        tvUserName = view.findViewById(R.id.tv_userName);
        tvMbile = view.findViewById(R.id.tv_mbile);
        etEmail = view.findViewById(R.id.et_Email);
        etWhatsappNumber = view.findViewById(R.id.et_Whatsapp_Number);
        tvMale = view.findViewById(R.id.tv_male);
        tvFemaile = view.findViewById(R.id.tv_femaile);
        tvOthers = view.findViewById(R.id.tv_Others);
        etState = view.findViewById(R.id.et_State);
        etCity = view.findViewById(R.id.et_City);
        etClass10thpercent = view.findViewById(R.id.et_class10thpercent);
        etClass12thpercent = view.findViewById(R.id.et_class12thpercent);
        etGraduationDegree = view.findViewById(R.id.et_graduation_Degree);
        etGraduationYear = view.findViewById(R.id.et_graduation_Year);
        etGraduationPercentage = view.findViewById(R.id.et_graduation_Percentage);
        etGraduationSpecialisation = view.findViewById(R.id.et_graduation_Specialisation);
        etPostgraduationDegree = view.findViewById(R.id.et_Postgraduation_Degree);
        etPostgraduationYear = view.findViewById(R.id.et_Postgraduation_Year);
        etPostgraduationPercentage = view.findViewById(R.id.et_Postgraduation_Percentage);
        etPostgraduationSpecialisation = view.findViewById(R.id.et_Postgraduation_Specialisation);
        etParentsName = view.findViewById(R.id.et_parentsName);
        etParentRelation = view.findViewById(R.id.et_parentRelation);
        etparentContactNumber = view.findViewById(R.id.etparentContactNumber);
        etpermanentAddress = view.findViewById(R.id.etpermanentAddress);
        rvRecyclerworkexp = view.findViewById(R.id.rv_recyclerworkexp);
        tvSubmitBasicDetail = view.findViewById(R.id.tv_submitBasicDetail);
        tvSubmitAcademicDetail = view.findViewById(R.id.tv_submitAcademicDetail);
        tvSubmitPersonalDetail = view.findViewById(R.id.tv_submitPersonalDetail);
        llBasicDetails = view.findViewById(R.id.llBasic_Details);
        llAcademicDetails = view.findViewById(R.id.llAcademic_Details);
        llPersonalDetails = view.findViewById(R.id.llPersonal_Details);
        llWorkExperience = view.findViewById(R.id.llWork_Experience);
        loginLL = view.findViewById(R.id.loginLL);
        profileLL = view.findViewById(R.id.profileLL);
    }

    private void inui() {
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                basicButtonEnable = true;
                tvSubmitBasicDetail.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_orangish_btn_enable));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etWhatsappNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                basicButtonEnable = true;
                tvSubmitBasicDetail.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_orangish_btn_enable));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etClass10thpercent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                acdemicButtonEnable = true;
                tvSubmitAcademicDetail.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_orangish_btn_enable));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etClass10thpercent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                acdemicButtonEnable = true;
                tvSubmitAcademicDetail.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_orangish_btn_enable));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etClass12thpercent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                acdemicButtonEnable = true;
                tvSubmitAcademicDetail.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_orangish_btn_enable));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if (!AppPreferences.getInstance(getContext()).getPreferencesString(AppConstants.AUTH).equalsIgnoreCase("true")) {
            loginLL.setVisibility(View.VISIBLE);
            profileLL.setVisibility(View.GONE);
        } else {
            setTextDetails();
            validateWorExperience();
            profileLL.setVisibility(View.VISIBLE);
            loginLL.setVisibility(View.GONE);
        }

        etGraduationDegree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                acdemicButtonEnable = true;
                tvSubmitAcademicDetail.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_orangish_btn_enable));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etClass10thpercent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                acdemicButtonEnable = true;
                tvSubmitAcademicDetail.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_orangish_btn_enable));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etGraduationYear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                acdemicButtonEnable = true;
                tvSubmitAcademicDetail.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_orangish_btn_enable));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etGraduationPercentage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                acdemicButtonEnable = true;
                tvSubmitAcademicDetail.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_orangish_btn_enable));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etGraduationSpecialisation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                acdemicButtonEnable = true;
                tvSubmitAcademicDetail.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_orangish_btn_enable));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etPostgraduationDegree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                acdemicButtonEnable = true;
                tvSubmitAcademicDetail.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_orangish_btn_enable));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etPostgraduationYear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                acdemicButtonEnable = true;
                tvSubmitAcademicDetail.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_orangish_btn_enable));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etPostgraduationPercentage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                acdemicButtonEnable = true;
                tvSubmitAcademicDetail.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_orangish_btn_enable));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etPostgraduationSpecialisation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                acdemicButtonEnable = true;
                tvSubmitAcademicDetail.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_orangish_btn_enable));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etParentsName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                personalButtonEnable = true;
                tvSubmitPersonalDetail.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_orangish_btn_enable));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etParentRelation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                personalButtonEnable = true;
                tvSubmitPersonalDetail.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_orangish_btn_enable));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etparentContactNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                personalButtonEnable = true;
                tvSubmitPersonalDetail.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_orangish_btn_enable));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etpermanentAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                personalButtonEnable = true;
                tvSubmitPersonalDetail.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_orangish_btn_enable));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void spinnersetup() {
        spinstate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) spinstate.getSelectedView()).setTextColor(Color.parseColor("#000000"));
                ((TextView) spinstate.getSelectedView()).setTextSize(11);
                ((TextView) spinstate.getSelectedView()).setGravity(Gravity.START);
                // ((TextView) strstateid.getSelectedView()).setGravity(Gravity.START);

                // etState.setText(String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.stateData.name));

                // spinstate.setSelection(i);

                if (!stateList[i].equalsIgnoreCase("Select any state")) {
                    strState = String.valueOf(statelistG.get(i).name);

                    strstateid = String.valueOf(statelistG.get(i).id);
                    if (strstateids.equalsIgnoreCase(strState)) {
                        basicButtonEnable = false;
                        tvSubmitBasicDetail.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_greish_btn_disable));
                    } else {
                        basicButtonEnable = true;
                        tvSubmitBasicDetail.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_orangish_btn_enable));
                    }

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spincity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
                ((TextView) spincity.getSelectedView()).setTextColor(Color.parseColor("#000000"));
                ((TextView) spincity.getSelectedView()).setTextSize(11);
                ((TextView) spincity.getSelectedView()).setGravity(Gravity.START);
                strCity = String.valueOf(citrylistG.get(position).name);
                if (strcityid.equalsIgnoreCase(strCity)) {
                    basicButtonEnable = false;
                    tvSubmitBasicDetail.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_greish_btn_disable));
                } else {
                    basicButtonEnable = true;
                    tvSubmitBasicDetail.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_orangish_btn_enable));
                }
                // strstateid = spincity.getSelectedItem().toString();
                // Toast.makeText(getContext(), "spinstate.getSelectedView().toString()", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    public void validateAcademicDetails() {
        strClass10thpercent = etClass10thpercent.getText().toString().trim();
        strClass12thpercent = etClass12thpercent.getText().toString().trim();
        strGraduationDegree = etGraduationDegree.getText().toString().trim();
        strGraduationYear = etGraduationYear.getText().toString().trim();
        strGraduationPercentage = etGraduationPercentage.getText().toString().trim();
        strGraduationSpecialisation = etGraduationSpecialisation.getText().toString().trim();
        strPostgraduationDegree = etPostgraduationDegree.getText().toString().trim();
        strPostgraduationYear = etPostgraduationYear.getText().toString().trim();
        strPostgraduationPercentage = etPostgraduationPercentage.getText().toString().trim();
        strPostgraduationSpecialisation = etPostgraduationSpecialisation.getText().toString().trim();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("x_percentage", strClass10thpercent);
        jsonObject.addProperty("xii_percentage", strClass12thpercent);
        jsonObject.addProperty("graduation_degree", strGraduationDegree);
        jsonObject.addProperty("graduation_year", strGraduationYear);
        jsonObject.addProperty("graduation_percentage", strGraduationPercentage);
        jsonObject.addProperty("graduation_specialisation", strGraduationSpecialisation);
        jsonObject.addProperty("pg_degree", strPostgraduationDegree);
        jsonObject.addProperty("pg_year", strPostgraduationYear);
        jsonObject.addProperty("pg_percentage", strPostgraduationPercentage);
        jsonObject.addProperty("pg_specialisation", strPostgraduationSpecialisation);
//        profileFragmentPresenter.callUserProfileUploadApi(jsonObject);
    }

    public void validateBasicDetails() {
        strEmail = etEmail.getText().toString().trim();
        strWhatsappNumber = etWhatsappNumber.getText().toString().trim();


        // strState = etState.getText().toString().trim();
        //strCity = etCity.getText().toString().trim();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email", strEmail);
        jsonObject.addProperty("whatsapp_no", strWhatsappNumber);
        jsonObject.addProperty("state", strState);
        jsonObject.addProperty("city", strCity);
        jsonObject.addProperty("gender", strGender);
//        profileFragmentPresenter.callUserProfileUploadApi(jsonObject);
    }

    public void validatePersonalDetails() {
        strParentsName = etParentsName.getText().toString().trim();
        strParentRelation = etParentRelation.getText().toString().trim();
        strparentContactNumber = etparentContactNumber.getText().toString().trim();
        strpermanentAddress = etpermanentAddress.getText().toString().trim();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("parent_name", strParentsName);
        jsonObject.addProperty("parent_relation", strParentRelation);
        jsonObject.addProperty("parent_no", strparentContactNumber);
        jsonObject.addProperty("parent_address", strpermanentAddress);
//        profileFragmentPresenter.callUserProfileUploadApi(jsonObject);
    }

    public void validateWorExperience() {
        profileWorkExperienceAdapter = new ProfileWorkExperienceAdapter(getActivity(), new ArrayList<>(), new ProfileWorkExperienceAdapter.WorkSelect() {
            @Override
            public void workSelect(UserProfileResponse.UserDataModel.WorkExperinceData workExperinceData) {
                Intent intent = new Intent(getActivity(), EmploymentDetailsActivity.class);
                intent.putExtra("designation", workExperinceData.designation);
                intent.putExtra("company", workExperinceData.company);
                intent.putExtra("role", workExperinceData.role);
                intent.putExtra("salary", String.valueOf(workExperinceData.salary));
                intent.putExtra("start_date", workExperinceData.start_date);
                intent.putExtra("end_date", workExperinceData.end_date);
//                intent.putExtra("job_description", workExperinceData.job_description);
                intent.putExtra("id", String.valueOf(workExperinceData.id));
                startActivity(intent);
            }
        });
        layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvRecyclerworkexp.setLayoutManager(layoutManager1);
        rvRecyclerworkexp.setAdapter(profileWorkExperienceAdapter);

        if (DataHolder.getInstance().getUserProfileDataresponse != null && DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.work_experience_data != null) {
            if (DataHolder.getInstance().getUserProfileDataresponse.data.work_experience_data.size() != 0) {
                profileWorkExperienceAdapter.notifyItem(DataHolder.getInstance().getUserProfileDataresponse.data.work_experience_data);
            } else {
                profileWorkExperienceAdapter.notifyItem(new ArrayList<>());

            }
        }
    }

    private void receiverRegister() {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (DataHolder.getInstance().getUserProfileDataresponse != null && DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.work_experience_data != null)
                    profileWorkExperienceAdapter.notifyItem(DataHolder.getInstance().getUserProfileDataresponse.data.work_experience_data);
            }
        };
        IntentFilter intentFilter = new IntentFilter(getString(R.string.app_name) + "user.workExperince.refresh");
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, intentFilter);

    }

    public void setTextDetails() {
        if (DataHolder.getInstance().getUserProfileDataresponse.data != null) {

            if (DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.name != null) {
                tvUserName.setText(String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.name));
            }
            if (DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.mobile != null) {
                tvMbile.setText(String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.mobile));
            }
            if (DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.email != null) {
                etEmail.setText(String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.email));
            }
            if (DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.whatsappNo != null) {
                etWhatsappNumber.setText(String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.whatsappNo));
            }

            if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.xPercentage != null) {
                etClass10thpercent.setText(String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.xPercentage));

            }
            if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.xGpa != null) {
                etClass10thpercent.setText(String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.xGpa));

            }
            if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.xiiPercentage != null) {
                etClass12thpercent.setText(String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.xiiPercentage));
            }
            if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.xiiGpa != null) {
                etClass12thpercent.setText(String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.xiiGpa));
            }
            if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationDegree != null) {
                etGraduationDegree.setText(String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationDegree));
            }
            if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationYear != null) {
                etGraduationYear.setText(String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationYear));
            }
            if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationPercentage != null) {
                etGraduationPercentage.setText(String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationPercentage));
            }
            if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.gradutaionGpa != null) {
                etGraduationPercentage.setText(String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.gradutaionGpa));
            }
            if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationSpecialisation != null) {
                etGraduationSpecialisation.setText(String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.graduationSpecialisation));
            }
            if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgDegree != null) {
                etPostgraduationDegree.setText(String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgDegree));
            }
            if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgYear != null) {
                etPostgraduationYear.setText(String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgYear));
            }
            if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgPercentage != null) {
                etPostgraduationPercentage.setText(String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgPercentage));
            }
            if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgGpa != null) {
                etPostgraduationPercentage.setText(String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgGpa));
            }
            if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgSpecialisation != null) {
                etPostgraduationSpecialisation.setText(String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.pgSpecialisation));
            }
            if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.parentName != null) {
                etParentsName.setText(String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.parentName));
            }
            if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.parentRelation != null) {
                etParentRelation.setText(String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.parentRelation));
            }
            if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.parentNo != null) {
                etparentContactNumber.setText(String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.parentNo));
            }
            if (DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null && DataHolder.getInstance().getUserProfileDataresponse.data.student_data.parentAddress != null) {
                etpermanentAddress.setText(String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.student_data.parentAddress));
            }
            if (DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.gender != null) {
                strGender = String.valueOf(DataHolder.getInstance().getUserProfileDataresponse.data.gender);
                if (strGender.equalsIgnoreCase("Male")) {
                    tvMale.setTextColor(getResources().getColor(R.color.white));
                    tvFemaile.setTextColor(getResources().getColor(R.color.black));
                    tvOthers.setTextColor(getResources().getColor(R.color.black));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        tvMale.setBackgroundResource(R.drawable.black_background_normal);
                        tvFemaile.setBackgroundResource(R.drawable.border_black_white_background);
                        tvOthers.setBackgroundResource(R.drawable.border_black_white_background);
                    }
                } else if (strGender.equalsIgnoreCase("Female")) {
                    tvMale.setTextColor(getResources().getColor(R.color.black));
                    tvFemaile.setTextColor(getResources().getColor(R.color.white));
                    tvOthers.setTextColor(getResources().getColor(R.color.black));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        tvMale.setBackgroundResource(R.drawable.border_black_white_background);
                        tvFemaile.setBackgroundResource(R.drawable.black_background_normal);
                        tvOthers.setBackgroundResource(R.drawable.border_black_white_background);
                    }
                } else if (strGender.equalsIgnoreCase("Other")) {
                    tvMale.setTextColor(getResources().getColor(R.color.black));
                    tvFemaile.setTextColor(getResources().getColor(R.color.black));
                    tvOthers.setTextColor(getResources().getColor(R.color.white));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        tvMale.setBackgroundResource(R.drawable.border_black_white_background);
                        tvFemaile.setBackgroundResource(R.drawable.border_black_white_background);
                        tvOthers.setBackgroundResource(R.drawable.black_background_normal);
                    }
                }
            }
        }
    }


    private void setClickListeners(View view) {
        tvSubmitBasicDetail.setOnClickListener(this);
        tvSubmitPersonalDetail.setOnClickListener(this);
        tvSubmitAcademicDetail.setOnClickListener(this);
        tvMale.setOnClickListener(this);
        tvFemaile.setOnClickListener(this);
        tvOthers.setOnClickListener(this);
        view.findViewById(R.id.iv_Basic_Details).setOnClickListener(this);
        view.findViewById(R.id.iv_Academic_Details).setOnClickListener(this);
        view.findViewById(R.id.iv_Personal_Details).setOnClickListener(this);
        view.findViewById(R.id.iv_Work_Experience).setOnClickListener(this);
        view.findViewById(R.id.tv_addExperience).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_addExperience:
                Intent intent = new Intent(getActivity(), EmploymentDetailsActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_submitBasicDetail:
                if (basicButtonEnable) {
                    validateBasicDetails();
                } else {
                    showMessage("Nothing to update");
                }
                break;
            case R.id.tv_submitAcademicDetail:
                if (acdemicButtonEnable) {
                    validateAcademicDetails();
                } else {
                    showMessage("Nothing to update");
                }
                break;
            case R.id.tv_submitPersonalDetail:
                if (personalButtonEnable) {
                    validatePersonalDetails();
                } else {
                    showMessage("Nothing to update");
                }
                break;
            case R.id.tv_male:
                strGender = "Male";
                tvMale.setTextColor(getResources().getColor(R.color.white));
                tvFemaile.setTextColor(getResources().getColor(R.color.black));
                tvOthers.setTextColor(getResources().getColor(R.color.black));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    tvMale.setBackgroundResource(R.drawable.black_background_normal);
                    tvFemaile.setBackgroundResource(R.drawable.border_black_white_background);
                    tvOthers.setBackgroundResource(R.drawable.border_black_white_background);
                }
                basicButtonEnable = true;
                tvSubmitBasicDetail.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_orangish_btn_enable));

                break;
            case R.id.tv_femaile:
                strGender = "Female";
                tvMale.setTextColor(getResources().getColor(R.color.black));
                tvFemaile.setTextColor(getResources().getColor(R.color.white));
                tvOthers.setTextColor(getResources().getColor(R.color.black));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    tvMale.setBackgroundResource(R.drawable.border_black_white_background);
                    tvFemaile.setBackgroundResource(R.drawable.black_background_normal);
                    tvOthers.setBackgroundResource(R.drawable.border_black_white_background);
                }
                basicButtonEnable = true;
                tvSubmitBasicDetail.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_orangish_btn_enable));

                break;
            case R.id.tv_Others:
                strGender = "Other";
                tvMale.setTextColor(getResources().getColor(R.color.black));
                tvFemaile.setTextColor(getResources().getColor(R.color.black));
                tvOthers.setTextColor(getResources().getColor(R.color.white));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    tvMale.setBackgroundResource(R.drawable.border_black_white_background);
                    tvFemaile.setBackgroundResource(R.drawable.border_black_white_background);
                    tvOthers.setBackgroundResource(R.drawable.black_background_normal);
                }
                basicButtonEnable = true;
                tvSubmitBasicDetail.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_orangish_btn_enable));

                break;

            case R.id.iv_Basic_Details:
                if (llBasicDetails.getVisibility() == View.VISIBLE) {
                    llBasicDetails.setVisibility(View.GONE);
                } else {
                    llBasicDetails.setVisibility(View.VISIBLE);
                }
                llAcademicDetails.setVisibility(View.GONE);
                llPersonalDetails.setVisibility(View.GONE);
                llWorkExperience.setVisibility(View.GONE);
                break;
            case R.id.iv_Academic_Details:
                if (llAcademicDetails.getVisibility() == View.VISIBLE) {
                    llAcademicDetails.setVisibility(View.GONE);
                } else {
                    llAcademicDetails.setVisibility(View.VISIBLE);
                }
                llBasicDetails.setVisibility(View.GONE);
                llPersonalDetails.setVisibility(View.GONE);
                llWorkExperience.setVisibility(View.GONE);
                break;
            case R.id.iv_Personal_Details:
                llBasicDetails.setVisibility(View.GONE);
                llAcademicDetails.setVisibility(View.GONE);
                if (llPersonalDetails.getVisibility() == View.VISIBLE) {
                    llPersonalDetails.setVisibility(View.GONE);
                } else {
                    llPersonalDetails.setVisibility(View.VISIBLE);
                }
                llWorkExperience.setVisibility(View.GONE);
                break;
            case R.id.iv_Work_Experience:

                llBasicDetails.setVisibility(View.GONE);
                llAcademicDetails.setVisibility(View.GONE);
                llPersonalDetails.setVisibility(View.GONE);
                if (llWorkExperience.getVisibility() == View.VISIBLE) {
                    llWorkExperience.setVisibility(View.GONE);
                } else {
                    llWorkExperience.setVisibility(View.VISIBLE);
                }
                break;
        }
    }


    @Override
    public void setProfileDataResponse(UserProfileUpDataResponse userProfileUpDataResponse) {
        if (userProfileUpDataResponse != null && userProfileUpDataResponse.success) {
            showMessage(userProfileUpDataResponse.message);
            if (personalButtonEnable) {
                personalButtonEnable = false;
                tvSubmitPersonalDetail.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_greish_btn_disable));
            }
            if (acdemicButtonEnable) {
                acdemicButtonEnable = false;
                tvSubmitAcademicDetail.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_greish_btn_disable));

            }
            if (basicButtonEnable) {
                basicButtonEnable = false;
                tvSubmitBasicDetail.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_greish_btn_disable));

            }

        }
    }

    @Override
    public void setWorkExperienceResponse(AddEditWorkExperienceResponse addEditWorkExperienceResponse) {

    }

    @Override
    public void setUpdateLocationPreferenceResponse(UpdateLocationPreferenceResponse addEditWorkExperienceResponse) {

    }

    @Override
    public void setStateListResponse(ProfileStateListResponseModel profileStateListResponseModel) {
        if (profileStateListResponseModel != null && profileStateListResponseModel.success) {
            statelistG = profileStateListResponseModel.data;
            if (statelistG != null) {
                stateListAdapter = new StateListAdapter(getActivity(), new ArrayList<>(), new StateListAdapter.StateSelected() {
                    @Override
                    public void stateSelected(ProfileStateListResponseModel.ProfileStateListDataModel profileStateListData) {

                    }
                });
                stateListAdapter.notifyItem(statelistG);

                rvState.setAdapter(stateListAdapter);
//                stateListAdapter.setOnItemClicked(this);


            }
           /* stateList = new String[profileStateListResponseModel.data.size()+1];
            stateList[0]="Select any state";
            for (int i = 0; i < profileStateListResponseModel.data.size(); i++) {
                int c=i+1;
                stateList[c] = (profileStateListResponseModel.data.get(i).name);

            }
            ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1
                    , stateList);
            dayAdapter.setDropDownViewResource(R.layout.date_spinner_item);

            spinstate.setAdapter(dayAdapter);*/


            /*if (strstateids != null) {
                for (int i = 0; i < profileStateListResponseModel.data.size(); i++) {
                    // strstateid=statelistG.get(i).name;
                    if (strstateids.equalsIgnoreCase(profileStateListResponseModel.data.get(i).name)) {
                        spinstate.setSelection(i);
                        break;
                    }
                }
            }*/
            //  }


        }
    }

    @Override
    public void setCityListResponse(ProfileCityListRsponseModel profileCityListRsponseModel) {
        if (profileCityListRsponseModel != null && profileCityListRsponseModel.success) {
            citrylistG = profileCityListRsponseModel.data;
            if (citrylistG != null) {
                cityListAdapter = new CityListAdapter(getActivity(), new ArrayList<>(), new CityListAdapter.CitySelected() {
                    @Override
                    public void citySelected(ProfileCityListRsponseModel.ProfileCityListDataModel cityListDataModel) {

                    }
                });
                cityListAdapter.notifyItem(citrylistG);
                rvCitylist.setAdapter(cityListAdapter);
                //  cityListAdapter.setOnItemClicked(this);
            }
            // cityList = new String[profileCityListRsponseModel.data.size()];
           /* for (int i = 0; i < profileCityListRsponseModel.data.size(); i++) {
                // cityList.add(profileCityListRsponseModel.data.get(i).name);
                cityList[i] = (profileCityListRsponseModel.data.get(i).name);

            }
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item, cityList);//setting the country_array to spinner
            adapter1.setDropDownViewResource(R.layout.date_spinner_item);
            spincity.setAdapter(adapter1);*/

          /*  if (strcityid != null) {
                for (int i = 0; i < profileCityListRsponseModel.data.size(); i++) {
                    // strstateid=statelistG.get(i).name;

                    if (strcityid.equalsIgnoreCase(profileCityListRsponseModel.data.get(i).name)) {
                        spincity.setSelection(i);
                        break;
                    }
                }
            }*/

        }
    }

    @Override
    public void setDeleteWorkExperinceResponse(DeleteWorkExperinceModel deleteWorkExperinceModel) {

    }

    @Override
    public void setUserProfileResponse(UserProfileResponse userProfileResponse) {

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

    @Override
    public void onItemClicked(View view, int position) {
        if (view.getId() == R.id.tv_state_name) {
            basicButtonEnable = true;
            tvSubmitBasicDetail.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_orangish_btn_enable));
            if (searchedState) {
                strState = updatedStateList.get(position).name;
                strstateid = String.valueOf(updatedStateList.get(position).id);
                //collapse(rlForStaterecyler);
                etSelectState.setText("");

            } else {
                strState = statelistG.get(position).name;
                strstateid = String.valueOf(statelistG.get(position).id);
            }
            profileFragmentPresenter.callProfileCityListApi(strstateid);
            collapse(rlForStaterecyler);
            etState.setText(strState);
            etCity.setText("");
            //ivStateDrpDwnIcon.animate().rotation(0).setDuration(400);
            state_flag = false;

            // callApiCityList();

        } else if (view.getId() == R.id.tv_city_name) {
            basicButtonEnable = true;
            tvSubmitBasicDetail.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_orangish_btn_enable));
            if (searchedCity) {
                strcityid = String.valueOf(updatedCityList.get(position).id);
                strCity = updatedCityList.get(position).name;
                etSelectCity.setText("");
            } else {
                strcityid = String.valueOf(citrylistG.get(position).id);
                strCity = citrylistG.get(position).name;

            }
            collapse(rlForCityrecyler);
            etCity.setText(strCity);
            city_flag = false;

        }
    }

 /*   @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) spinstate.getSelectedView()).setTextColor(Color.parseColor("#0000"));
        ((TextView) spinstate.getSelectedView()).setTextSize(11);
        ((TextView) spinstate.getSelectedView()).setGravity(Gravity.CENTER);

        strstateid = spinstate.getSelectedItem().toString();
        Toast.makeText(getContext(), spinstate.getSelectedView().toString(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }*/
}