package com.myfutureapp.profile.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.myfutureapp.R;
import com.myfutureapp.login.DataHolder;

import java.util.ArrayList;

public class AdditionalFragment extends Fragment {
    private static HandleFragmentLoading handleFragmentLoading;
    boolean laptopSelected = false, WifiSelected = false, LicenceSelected = false, TwoWheelerSelected = false;
    private LinearLayout laptopRequiredLL, yesLaptopLL, noLaptopLL;
    private ImageView yesLaptopIV, noLaptopIV;
    private TextView yesLaptopTV, noLaptopTV;
    private LinearLayout wifiRequiredLL, yesWifiLL, noWifiLL;
    private ImageView yesWifiIV, noWifiIV;
    private TextView yesWifiTV, noWifiTV;
    private LinearLayout licenseRequiredLL, yesLicenseLL, noLicenseLL;
    private ImageView yesLicenseIV, noLicenseIV;
    private TextView yesLicenseTV, noLicenseTV;
    private LinearLayout twoWheelerRequiredLL, yesTwoWheelerLL, noTwoWheelerLL;
    private ImageView yesTwoWheelerIV, noTwoWheelerIV;
    private TextView yesTwoWheelerTV, noTwoWheelerTV;
    private boolean btnEnable = false;
    private View view;
    private TextView saveAdditionalDetails;
    private ArrayList<Boolean> list;

    /*  private  final Pattern sPattern = Pattern.compile();

      private boolean isValid(CharSequence s) {
          return sPattern.matcher(s).matches();
      }*/
    public AdditionalFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static AdditionalFragment newInstance(HandleFragmentLoading handleFragmentLoadings) {
        AdditionalFragment fragment = new AdditionalFragment();
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
            view = inflater.inflate(R.layout.fragment_additional, container, false);

            laptopRequiredLL = (LinearLayout) view.findViewById(R.id.laptopRequiredLL);
            yesLaptopLL = (LinearLayout) view.findViewById(R.id.yesLaptopLL);
            noLaptopLL = (LinearLayout) view.findViewById(R.id.noLaptopLL);
            yesLaptopIV = (ImageView) view.findViewById(R.id.yesLaptopIV);
            noLaptopIV = (ImageView) view.findViewById(R.id.noLaptopIV);
            yesLaptopTV = (TextView) view.findViewById(R.id.yesLaptopTV);
            noLaptopTV = (TextView) view.findViewById(R.id.noLaptopTV);

            wifiRequiredLL = (LinearLayout) view.findViewById(R.id.wifiRequiredLL);
            yesWifiLL = (LinearLayout) view.findViewById(R.id.yesWifiLL);
            noWifiLL = (LinearLayout) view.findViewById(R.id.noWifiLL);
            yesWifiIV = (ImageView) view.findViewById(R.id.yesWifiIV);
            noWifiIV = (ImageView) view.findViewById(R.id.noWifiIV);
            yesWifiTV = (TextView) view.findViewById(R.id.yesWifiTV);
            noWifiTV = (TextView) view.findViewById(R.id.noWifiTV);

            licenseRequiredLL = (LinearLayout) view.findViewById(R.id.licenseRequiredLL);
            yesLicenseLL = (LinearLayout) view.findViewById(R.id.yesLicenseLL);
            noLicenseLL = (LinearLayout) view.findViewById(R.id.noLicenseLL);
            yesLicenseIV = (ImageView) view.findViewById(R.id.yesLicenseIV);
            noLicenseIV = (ImageView) view.findViewById(R.id.noLicenseIV);
            yesLicenseTV = (TextView) view.findViewById(R.id.yesLicenseTV);
            noLicenseTV = (TextView) view.findViewById(R.id.noLicenseTV);


            twoWheelerRequiredLL = (LinearLayout) view.findViewById(R.id.twoWheelerRequiredLL);
            yesTwoWheelerLL = (LinearLayout) view.findViewById(R.id.yesTwoWheelerLL);
            noTwoWheelerLL = (LinearLayout) view.findViewById(R.id.noTwoWheelerLL);
            yesTwoWheelerIV = (ImageView) view.findViewById(R.id.yesTwoWheelerIV);
            noTwoWheelerIV = (ImageView) view.findViewById(R.id.noTwoWheelerIV);
            yesTwoWheelerTV = (TextView) view.findViewById(R.id.yesTwoWheelerTV);
            noTwoWheelerTV = (TextView) view.findViewById(R.id.noTwoWheelerTV);


            saveAdditionalDetails = (TextView) view.findViewById(R.id.saveAdditionalDetails);


            handlingClick();
            settingView();
            return view;
        }
        return view;
    }

    private void settingView() {
        if (!((ProfileActivity) getActivity()).askAdditionalList[0]) {
            laptopRequiredLL.setVisibility(View.GONE);
            laptopSelected = true;
        }
        if (!((ProfileActivity) getActivity()).askAdditionalList[1]) {
            wifiRequiredLL.setVisibility(View.GONE);
            WifiSelected = true;
        }
        if (!((ProfileActivity) getActivity()).askAdditionalList[2]) {
            licenseRequiredLL.setVisibility(View.GONE);
            LicenceSelected = true;
        }
        if (!((ProfileActivity) getActivity()).askAdditionalList[3]) {
            twoWheelerRequiredLL.setVisibility(View.GONE);
            TwoWheelerSelected = true;
        }
    }

    private void handlingClick() {
        yesLaptopLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                laptopSelected = true;
                DataHolder.getInstance().getUserDataresponse.ask_laptop = "1";
                settingYesLayout(yesLaptopLL, yesLaptopIV, yesLaptopTV, noLaptopLL, noLaptopIV, noLaptopTV);
            }
        });
        noLaptopLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                laptopSelected = true;
                DataHolder.getInstance().getUserDataresponse.ask_laptop = "0";
                settingNoLayout(noLaptopLL, noLaptopIV, noLaptopTV, yesLaptopLL, yesLaptopIV, yesLaptopTV);
            }
        });
        yesWifiLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiSelected = true;
                DataHolder.getInstance().getUserDataresponse.ask_wifi = "1";
                settingYesLayout(yesWifiLL, yesWifiIV, yesWifiTV, noWifiLL, noWifiIV, noWifiTV);

            }
        });
        noWifiLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiSelected = true;
                DataHolder.getInstance().getUserDataresponse.ask_wifi = "0";
                settingNoLayout(noWifiLL, noWifiIV, noWifiTV, yesWifiLL, yesWifiIV, yesWifiTV);
            }
        });
        yesLicenseLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LicenceSelected = true;
                DataHolder.getInstance().getUserDataresponse.ask_driving_license = "1";
                settingYesLayout(yesLicenseLL, yesLicenseIV, yesLicenseTV, noLicenseLL, noLicenseIV, noLicenseTV);

            }
        });
        noLicenseLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LicenceSelected = true;
                DataHolder.getInstance().getUserDataresponse.ask_driving_license = "0";
                settingNoLayout(noLicenseLL, noLicenseIV, noLicenseTV, yesLicenseLL, yesLicenseIV, yesLicenseTV);
            }
        });

        yesTwoWheelerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwoWheelerSelected = true;
                DataHolder.getInstance().getUserDataresponse.ask_2_wheeler = "1";
                settingYesLayout(yesTwoWheelerLL, yesTwoWheelerIV, yesTwoWheelerTV, noTwoWheelerLL, noTwoWheelerIV, noTwoWheelerTV);

            }
        });
        noTwoWheelerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwoWheelerSelected = true;
                DataHolder.getInstance().getUserDataresponse.ask_2_wheeler = "0";
                settingNoLayout(noTwoWheelerLL, noTwoWheelerIV, noTwoWheelerTV, yesTwoWheelerLL, yesTwoWheelerIV, yesTwoWheelerTV);
            }
        });

        saveAdditionalDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnEnable) {
                    handleFragmentLoading.loadingQuizDetailFragment("finished");
                } else {
                    showMessage("Please answers the questions");
                }
            }
        });
    }

    private void setBtnEnable() {
        if (laptopSelected && WifiSelected && LicenceSelected && TwoWheelerSelected) {
            saveAdditionalDetails.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_orangish_btn_enable));
            btnEnable = true;
        } else {
            saveAdditionalDetails.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_greish_btn_disable));
            btnEnable = false;
        }
    }

    private void settingYesLayout(LinearLayout clickedLayout, ImageView clickedIV, TextView clickedTV, LinearLayout unclickedLayout, ImageView unClickedIV, TextView unClickedTV) {
        clickedLayout.setBackgroundResource(R.drawable.green_btn);
        clickedIV.setVisibility(View.VISIBLE);
        clickedTV.setTextColor(getContext().getResources().getColor(R.color.white));
        unclickedLayout.setBackgroundResource(R.drawable.grey_btn_1);
        unClickedIV.setVisibility(View.GONE);
        unClickedTV.setTextColor(getContext().getResources().getColor(R.color.warm_gray));
        setBtnEnable();
    }

    private void settingNoLayout(LinearLayout clickedLayout, ImageView clickedIV, TextView clickedTV, LinearLayout unclickedLayout, ImageView unClickedIV, TextView unClickedTV) {
        clickedLayout.setBackgroundResource(R.drawable.red_btn);
        clickedIV.setVisibility(View.VISIBLE);
        clickedTV.setTextColor(getContext().getResources().getColor(R.color.white));
        unclickedLayout.setBackgroundResource(R.drawable.grey_btn_1);
        unClickedIV.setVisibility(View.GONE);
        unClickedTV.setTextColor(getContext().getResources().getColor(R.color.warm_gray));
        setBtnEnable();
    }

    private void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}