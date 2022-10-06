package com.myfutureapp.dashboard.homeProfile.ui;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.myfutureapp.R;
import com.myfutureapp.dashboard.homeProfile.presenter.BasicUserPresenter;
import com.myfutureapp.dashboard.model.UserProfileResponse;
import com.myfutureapp.dashboard.profile.model.ProfileCityListRsponseModel;
import com.myfutureapp.dashboard.profile.model.ProfileStateListResponseModel;
import com.myfutureapp.dashboard.profile.ui.CityListAdapter;
import com.myfutureapp.dashboard.profile.ui.StateListAdapter;
import com.myfutureapp.login.DataHolder;
import com.myfutureapp.profile.model.UserProfileUpDataResponse;
import com.myfutureapp.util.AppConstants;
import com.myfutureapp.util.AppPreferences;
import com.myfutureapp.util.Helper;

import java.util.ArrayList;
import java.util.List;

import static com.myfutureapp.util.AnimationSinglton.collapse;
import static com.myfutureapp.util.AnimationSinglton.expandView;

public class BasicUserFragment extends Fragment implements BasicUserView {

    ImageView basicDetailEditIV;
    private EditText et_Email, et_Whatsapp_Number, et_select_state, et_select_city;
    private boolean basicEditable = true;
    private TextView tv_male, tv_femaile, tv_Others;
    private TextView et_State, et_City;
    private String strGender = "";
    private ImageView iv_state;
    private RecyclerView rv_state, rv_citylist;
    private List<ProfileStateListResponseModel.ProfileStateListDataModel> stateListDataModels = new ArrayList<>();
    private final List<ProfileStateListResponseModel.ProfileStateListDataModel> updatedStateList = new ArrayList<>();
    private List<ProfileCityListRsponseModel.ProfileCityListDataModel> cityListDataModels = new ArrayList<>();
    private final List<ProfileCityListRsponseModel.ProfileCityListDataModel> updatedCityList = new ArrayList<>();
    private String strstateid;
    private StateListAdapter stateListAdapter;
    private CityListAdapter cityListAdapter;
    private boolean searchedState = false;
    private boolean searchedCity = false;
    private BasicUserPresenter basicUserPresenter;
    private ConstraintLayout ll_forstaterecyler;
    private ConstraintLayout ll_forcityrecyler;
    private LinearLayout stateSelectionLL, citySelectionLL, savingCancelBasicDetails, llforgender;
    private TextView editBasicDetail, cancelBasicDetail, saveBasicDetail;
    private String perviousEmail = "", perviousMobile = "", perviousGender = "", perviousState = "", perviousCity = "";
    private View viewemail, viewwhatesapp, viewgender, viewstate, viewcitys;
    private ProgressDialog progressDialog;

    private boolean isLoaded = false;

    public BasicUserFragment() {
    }

    public static BasicUserFragment newInstance(String param1, String param2) {
        BasicUserFragment fragment = new BasicUserFragment();
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            Log.e("isVisibleToUser", "true");
            if (!isLoaded) {
                isLoaded = true;
            }
        } else {
            Log.e("isVisibleToUser", "false");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basic_user, container, false);
        inUi(view);
        progressDialog = new ProgressDialog(getContext());

        return view;

    }

    private void inUi(View view) {
        basicUserPresenter = new BasicUserPresenter(getContext(), BasicUserFragment.this);
        basicDetailEditIV = (ImageView) view.findViewById(R.id.basicDetailEditIV);

        et_Email = (EditText) view.findViewById(R.id.et_Email);
        et_select_state = (EditText) view.findViewById(R.id.et_select_state);
        et_select_city = (EditText) view.findViewById(R.id.et_select_city);
        et_Whatsapp_Number = (EditText) view.findViewById(R.id.et_Whatsapp_Number);

        editBasicDetail = (TextView) view.findViewById(R.id.editBasicDetail);
        tv_male = (TextView) view.findViewById(R.id.tv_male);
        tv_femaile = (TextView) view.findViewById(R.id.tv_femaile);
        tv_Others = (TextView) view.findViewById(R.id.tv_Others);
        et_State = (TextView) view.findViewById(R.id.et_State);
        et_City = (TextView) view.findViewById(R.id.et_City);
        saveBasicDetail = (TextView) view.findViewById(R.id.saveBasicDetail);
        cancelBasicDetail = (TextView) view.findViewById(R.id.cancelBasicDetail);

        iv_state = (ImageView) view.findViewById(R.id.iv_state);

        rv_state = (RecyclerView) view.findViewById(R.id.rv_state);
        rv_citylist = (RecyclerView) view.findViewById(R.id.rv_citylist);

        ll_forstaterecyler = (ConstraintLayout) view.findViewById(R.id.ll_forstaterecyler);
        ll_forcityrecyler = (ConstraintLayout) view.findViewById(R.id.ll_forcityrecyler);

        stateSelectionLL = (LinearLayout) view.findViewById(R.id.stateSelectionLL);
        llforgender = (LinearLayout) view.findViewById(R.id.llforgender);
        citySelectionLL = (LinearLayout) view.findViewById(R.id.citySelectionLL);
        savingCancelBasicDetails = (LinearLayout) view.findViewById(R.id.savingCancelBasicDetails);

        viewemail = (View) view.findViewById(R.id.viewemail);
        viewwhatesapp = (View) view.findViewById(R.id.viewwhatesapp);
        viewgender = (View) view.findViewById(R.id.viewgender);
        viewstate = (View) view.findViewById(R.id.viewstate);
        viewcitys = (View) view.findViewById(R.id.viewcitys);

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        rv_state.setLayoutManager(layoutManager1);
        rv_state.hasFixedSize();
        rv_state.setVerticalScrollBarEnabled(true);
        stateListAdapter = new StateListAdapter(getActivity(), new ArrayList<>(), new StateListAdapter.StateSelected() {
            @Override
            public void stateSelected(ProfileStateListResponseModel.ProfileStateListDataModel profileStateListData) {
                if (searchedState) {
                    strstateid = String.valueOf(profileStateListData.id);
                    et_select_state.setText("");
                } else {
                    strstateid = String.valueOf(profileStateListData.id);
                }
                basicUserPresenter.callProfileCityListApi(strstateid);
                collapse(ll_forstaterecyler);
                et_State.setText(profileStateListData.name);
                //.setVisibility(View.VISIBLE);
                et_City.setText("");
            }
        });
        rv_state.setAdapter(stateListAdapter);

        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        rv_citylist.setLayoutManager(layoutManager2);
        rv_citylist.hasFixedSize();
        rv_citylist.setVerticalScrollBarEnabled(true);
        cityListAdapter = new CityListAdapter(getActivity(), new ArrayList<>(), new CityListAdapter.CitySelected() {
            @Override
            public void citySelected(ProfileCityListRsponseModel.ProfileCityListDataModel cityListDataModel) {
                if (searchedCity) {
                    //      strcityid = String.valueOf(cityListDataModel.id);
                    et_select_city.setText("");
                } else {
                    //     strcityid = String.valueOf(cityListDataModel.id);
                }
                collapse(ll_forcityrecyler);
                et_City.setText(cityListDataModel.name);
//                et_City.setVisibility(View.VISIBLE);

            }
        });
        rv_citylist.setAdapter(cityListAdapter);


        basicDetailEditIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.hideKeyboard(getActivity());
                if (basicEditable) {
                    makeBasicDetailEditable();
                    basicEditable = false;
                    basicDetailEditIV.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_tick_profile));
                } else {
                    checkingIsAnyThingToUpdate();
                }
            }
        });

        cancelBasicDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingUserBasicDetails();
                cancelBasicDetailEditable();
                basicDetailEditIV.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_edit));
                basicEditable = true;

            }
        });
        saveBasicDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkingIsAnyThingToUpdate();
            }
        });
        editBasicDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeBasicDetailEditable();
                basicEditable = false;

            }
        });
        tv_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strGender = "Male";
                Helper.hideKeyboard(getActivity());
                tv_male.setTextColor(getResources().getColor(R.color.white));
                tv_femaile.setTextColor(getResources().getColor(R.color.black));
                tv_Others.setTextColor(getResources().getColor(R.color.black));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    tv_male.setBackgroundResource(R.drawable.black_background_normal);
                    tv_femaile.setBackgroundResource(R.drawable.border_black_white_background);
                    tv_Others.setBackgroundResource(R.drawable.border_black_white_background);
                }
            }
        });
        tv_femaile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strGender = "Female";
                Helper.hideKeyboard(getActivity());
                tv_male.setTextColor(getResources().getColor(R.color.black));
                tv_femaile.setTextColor(getResources().getColor(R.color.white));
                tv_Others.setTextColor(getResources().getColor(R.color.black));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    tv_male.setBackgroundResource(R.drawable.border_black_white_background);
                    tv_femaile.setBackgroundResource(R.drawable.black_background_normal);
                    tv_Others.setBackgroundResource(R.drawable.border_black_white_background);
                }
            }
        });
        tv_Others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strGender = "Other";
                Helper.hideKeyboard(getActivity());
                tv_male.setTextColor(getResources().getColor(R.color.black));
                tv_femaile.setTextColor(getResources().getColor(R.color.black));
                tv_Others.setTextColor(getResources().getColor(R.color.white));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    tv_male.setBackgroundResource(R.drawable.border_black_white_background);
                    tv_femaile.setBackgroundResource(R.drawable.border_black_white_background);
                    tv_Others.setBackgroundResource(R.drawable.black_background_normal);
                }
            }
        });

        //state search
        et_select_state.addTextChangedListener(new TextWatcher() {
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
                    for (int i = 0; i < stateListDataModels.size(); i++) {
                        if (stateListDataModels.get(i).name.toLowerCase().contains(s.toString().toLowerCase())) {
                            updatedStateList.add(stateListDataModels.get(i));
                        }
                    }
                    stateListAdapter.clearAll();
                    stateListAdapter.notifyItem(updatedStateList);
                } else {
                    updatedStateList.clear();
                    searchedState = false;
                    stateListAdapter.clearAll();
                    stateListAdapter.notifyItem(stateListDataModels);
                }
            }
        });
        et_select_city.addTextChangedListener(new TextWatcher() {
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
                    for (int i = 0; i < cityListDataModels.size(); i++) {
                        if (cityListDataModels.get(i).name.toLowerCase().contains(s.toString().toLowerCase())) {
                            updatedCityList.add(cityListDataModels.get(i));
                        }
                    }
                    cityListAdapter.notifyItem(updatedCityList);
                } else {
                    updatedCityList.clear();
                    searchedCity = false;
                    cityListAdapter.notifyItem(cityListDataModels);
                }
            }
        });

        stateSelectionLL.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Helper.hideKeyboard(getActivity());
                return false;
            }
        });
        citySelectionLL.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Helper.hideKeyboard(getActivity());
                return false;
            }
        });
        llforgender.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Helper.hideKeyboard(getActivity());
                return false;
            }
        });
        stateSelectionLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_forstaterecyler.getVisibility() == View.GONE) {
                    if (ll_forcityrecyler.getVisibility() == View.VISIBLE) {
                        collapse(ll_forcityrecyler);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                expandView(ll_forstaterecyler);
                                //    et_State.setVisibility(View.GONE);

                            }
                        }, 500);

                    } else {
                        expandView(ll_forstaterecyler);
                        //  et_State.setVisibility(View.GONE);
                    }
                } else {
                    collapse(ll_forstaterecyler);
                    //  et_State.setVisibility(View.VISIBLE);
                }
            }
        });
        citySelectionLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (strstateid != null) {
                    if (ll_forcityrecyler.getVisibility() == View.GONE) {
                        expandView(ll_forcityrecyler);
//                        et_City.setVisibility(View.GONE);
                    } else {
                        collapse(ll_forcityrecyler);
//                        et_City.setVisibility(View.VISIBLE);
                    }
                } else {
                    showMessage("please select state first");
                }
            }
        });
        settingUserBasicDetails();
        cancelBasicDetailEditable();


    }

    private void checkingIsAnyThingToUpdate() {

        if (et_Email.getText().toString().trim().equalsIgnoreCase(perviousEmail) && et_Whatsapp_Number.getText().toString().trim().equalsIgnoreCase(perviousMobile)
                && et_State.getText().toString().trim().equalsIgnoreCase(perviousState) && et_City.getText().toString().trim().equalsIgnoreCase(perviousCity)
                && strGender.trim().equalsIgnoreCase(perviousGender)) {
            showMessage("Nothing To Update");
            cancelBasicDetailEditable();
            basicDetailEditIV.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_edit));
            basicEditable = true;
            return;
        }
        if (et_Email.getText().toString().trim().equalsIgnoreCase("") && !perviousEmail.equalsIgnoreCase("")) {
            showMessage("Please enter your email.");
            return;
        }
        if (et_Whatsapp_Number.getText().toString().trim().equalsIgnoreCase("") && !perviousMobile.equalsIgnoreCase("")) {
            showMessage("Please enter your whatsapp number.");
            return;
        }

        JsonObject jsonObject = new JsonObject();
        if (et_Email.getText().toString().trim().length() == 0) {
            jsonObject.addProperty("email", "");

        } else if (!Helper.isEmailValid(et_Email.getText().toString())) {
            showMessage("Something wrong with email");
            return;
        } else {
            jsonObject.addProperty("email", et_Email.getText().toString());

        }
        if (et_Whatsapp_Number.getText().toString().trim().length() == 0) {
            jsonObject.addProperty("whatsapp_no", "");

        } else if (et_Whatsapp_Number.getText().toString().length() > 0 && et_Whatsapp_Number.getText().toString().length() < 10) {
            showMessage("Please enter correct number");
            return;
        } else {
            jsonObject.addProperty("whatsapp_no", et_Whatsapp_Number.getText().toString());
        }
        jsonObject.addProperty("state", et_State.getText().toString());
        jsonObject.addProperty("city", et_City.getText().toString());
        jsonObject.addProperty("gender", strGender);
        Log.e("BasicDetails", "details--" + jsonObject.toString());
        progressDialog.setMessage("saving your details");
        progressDialog.setCancelable(false);
        progressDialog.show();
        basicUserPresenter.callUserProfileUploadApi(jsonObject);
    }


    private void settingUserBasicDetails() {
        Helper.hideKeyboard(getActivity());
        if (DataHolder.getInstance().getUserProfileDataresponse != null && DataHolder.getInstance().getUserProfileDataresponse.data != null) {
            if (Helper.isContainValue(DataHolder.getInstance().getUserProfileDataresponse.data.email)) {
                et_Email.setText(DataHolder.getInstance().getUserProfileDataresponse.data.email);
                perviousEmail = DataHolder.getInstance().getUserProfileDataresponse.data.email;
                et_Email.setTextColor(getResources().getColor(R.color.black));
            } else {
                et_Email.setText("");
                et_Email.setHint("None");
            }
            if (Helper.isContainValue(DataHolder.getInstance().getUserProfileDataresponse.data.whatsappNo)) {
                et_Whatsapp_Number.setText(DataHolder.getInstance().getUserProfileDataresponse.data.whatsappNo);
                perviousMobile = DataHolder.getInstance().getUserProfileDataresponse.data.whatsappNo;
            } else {

                et_Whatsapp_Number.setText("");
                et_Whatsapp_Number.setHint("None");
                et_Whatsapp_Number.setTextColor(getResources().getColor(R.color.black));
            }
            if (Helper.isContainValue(DataHolder.getInstance().getUserProfileDataresponse.data.gender)) {
                if (DataHolder.getInstance().getUserProfileDataresponse.data.gender.equalsIgnoreCase("Male")) {
                    tv_male.callOnClick();
                    perviousGender = "Male";
                } else if (DataHolder.getInstance().getUserProfileDataresponse.data.gender.equalsIgnoreCase("Female")) {
                    tv_femaile.callOnClick();
                    perviousGender = "Female";
                } else if (DataHolder.getInstance().getUserProfileDataresponse.data.gender.equalsIgnoreCase("other")) {
                    tv_Others.callOnClick();
                    perviousGender = "other";
                }
            }
            if (DataHolder.getInstance().getUserProfileDataresponse != null && DataHolder.getInstance().getUserProfileDataresponse.data != null) {
                if (DataHolder.getInstance().getUserProfileDataresponse.data.stateData != null && DataHolder.getInstance().getUserProfileDataresponse.data.stateData.name != null) {
                    if (Helper.isContainValue(DataHolder.getInstance().getUserProfileDataresponse.data.stateData.name)) {
                        et_State.setText(DataHolder.getInstance().getUserProfileDataresponse.data.stateData.name);
                        perviousState = DataHolder.getInstance().getUserProfileDataresponse.data.stateData.name;
                        strstateid = DataHolder.getInstance().getUserProfileDataresponse.data.state;
                        basicUserPresenter.callProfileCityListApi(strstateid);
                    }
                } else {
                    et_State.setText("");
                }
                if (DataHolder.getInstance().getUserProfileDataresponse.data.cityData != null && DataHolder.getInstance().getUserProfileDataresponse.data.cityData.name != null) {
                    if (Helper.isContainValue(DataHolder.getInstance().getUserProfileDataresponse.data.cityData.name)) {
                        et_City.setText(DataHolder.getInstance().getUserProfileDataresponse.data.cityData.name);
                        perviousCity = DataHolder.getInstance().getUserProfileDataresponse.data.cityData.name;
                    }
                } else {
                    et_City.setText("");
                }
            }
        }
    }

    private void makeBasicDetailEditable() {
        et_Email.setBackground(getContext().getResources().getDrawable(R.drawable.simple_white_background));
        et_Whatsapp_Number.setBackground(getContext().getResources().getDrawable(R.drawable.simple_white_background));
        citySelectionLL.setBackground(getContext().getResources().getDrawable(R.drawable.simple_white_background));
        stateSelectionLL.setBackground(getContext().getResources().getDrawable(R.drawable.simple_white_background));
        et_State.setTextColor(getContext().getResources().getColor(R.color.black));
        et_City.setTextColor(getContext().getResources().getColor(R.color.black));
        et_Email.setEnabled(true);
        et_Whatsapp_Number.setEnabled(true);
        tv_male.setClickable(true);
        tv_femaile.setClickable(true);
        tv_Others.setClickable(true);
        stateSelectionLL.setClickable(true);
        citySelectionLL.setClickable(true);
        savingCancelBasicDetails.setVisibility(View.VISIBLE);
        editBasicDetail.setVisibility(View.GONE);
        if (stateListDataModels.size() == 0) {
            basicUserPresenter.callProfileStateListApi();
        }

        viewcitys.setVisibility(View.GONE);
        viewstate.setVisibility(View.GONE);
        viewgender.setVisibility(View.GONE);
        viewwhatesapp.setVisibility(View.GONE);
        viewemail.setVisibility(View.GONE);
    }

    private void cancelBasicDetailEditable() {
        et_Email.setBackground(getContext().getResources().getDrawable(R.drawable.simple_cool_grey_background));
        et_Whatsapp_Number.setBackground(getContext().getResources().getDrawable(R.drawable.simple_cool_grey_background));
        stateSelectionLL.setBackground(getContext().getResources().getDrawable(R.drawable.simple_cool_grey_background));
        citySelectionLL.setBackground(getContext().getResources().getDrawable(R.drawable.simple_cool_grey_background));
        et_State.setTextColor(getContext().getResources().getColor(R.color.black));
        et_City.setTextColor(getContext().getResources().getColor(R.color.black));
        et_Email.setEnabled(false);
        et_Whatsapp_Number.setEnabled(false);
        tv_male.setClickable(false);
        tv_femaile.setClickable(false);
        tv_Others.setClickable(false);
        stateSelectionLL.setClickable(false);
        citySelectionLL.setClickable(false);

        savingCancelBasicDetails.setVisibility(View.GONE);
        editBasicDetail.setVisibility(View.VISIBLE);

        viewcitys.setVisibility(View.VISIBLE);
        viewstate.setVisibility(View.VISIBLE);
        viewgender.setVisibility(View.VISIBLE);
        viewwhatesapp.setVisibility(View.VISIBLE);
        viewemail.setVisibility(View.VISIBLE);
    }


    @Override
    public void setStateListResponse(ProfileStateListResponseModel profileStateListResponseModel) {
        if (profileStateListResponseModel != null && profileStateListResponseModel.success && profileStateListResponseModel.data != null) {
            stateListDataModels = profileStateListResponseModel.data;
            stateListAdapter.notifyItem(stateListDataModels);
        }
    }

    @Override
    public void setCityListResponse(ProfileCityListRsponseModel profileCityListRsponseModel) {
        if (profileCityListRsponseModel != null && profileCityListRsponseModel.success && profileCityListRsponseModel.data != null) {
            cityListDataModels.clear();
            cityListDataModels = profileCityListRsponseModel.data;
            cityListAdapter.notifyItem(cityListDataModels);
        }
    }

    @Override
    public void setProfileDataResponse(UserProfileUpDataResponse userProfileUpDataResponse) {
        if (userProfileUpDataResponse != null && userProfileUpDataResponse.success) {
            basicUserPresenter.callUserProfileApi(AppPreferences.getInstance(getContext()).getPreferencesString(AppConstants.STUDENTID));
        }

    }

    @Override
    public void setUserProfileResponse(UserProfileResponse userProfileResponse) {
        if (userProfileResponse != null && userProfileResponse.success) {
            DataHolder.getInstance().getUserProfileDataresponse = userProfileResponse;
            cancelBasicDetailEditable();
            basicEditable = true;
            settingUserBasicDetails();
            basicDetailEditIV.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_edit));
            progressDialog.dismiss();
        }
    }


    @Override
    public void showLoader() {

    }

    @Override
    public void hideLoader() {
        progressDialog.dismiss();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}