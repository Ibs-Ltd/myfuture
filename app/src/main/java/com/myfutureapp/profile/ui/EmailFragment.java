package com.myfutureapp.profile.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.myfutureapp.R;
import com.myfutureapp.login.DataHolder;
import com.myfutureapp.util.Helper;

public class EmailFragment extends Fragment {
    private static HandleFragmentLoading handleFragmentLoading;
    private EditText nameProfile;
    private TextView saveName;
    private boolean btnEnable = false;
    private View view;

    /*  private  final Pattern sPattern = Pattern.compile();

      private boolean isValid(CharSequence s) {
          return sPattern.matcher(s).matches();
      }*/
    public EmailFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static EmailFragment newInstance(HandleFragmentLoading handleFragmentLoadings) {
        EmailFragment fragment = new EmailFragment();
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
            view = inflater.inflate(R.layout.fragment_email, container, false);

            nameProfile = view.findViewById(R.id.nameProfile);
            saveName = view.findViewById(R.id.saveName);

            nameProfile.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable p0) {
                    if (Helper.isEmailValid(p0.toString())) {
                        saveName.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_orangish_btn_enable));
                        btnEnable = true;
                    } else {
                        saveName.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_greish_btn_disable));
                        btnEnable = false;
                    }
                }
            });

            saveName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (btnEnable) {
                        DataHolder.getInstance().getUserDataresponse.email = nameProfile.getText().toString();
                        handleFragmentLoading.loadingQuizDetailFragment("genderFragment");
                    } else {
                        showMessage("Please Enter Correct email");
                    }
                }
            });

            if (DataHolder.getInstance().getUserProfileDataresponse != null && DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.email != null) {
                nameProfile.setText(DataHolder.getInstance().getUserProfileDataresponse.data.email);
            }
            return view;
        }
        return view;
    }

    private void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}