package com.myfutureapp.dashboard.homeProfile.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myfutureapp.R;
import com.myfutureapp.dashboard.model.UserProfileResponse;
import com.myfutureapp.dashboard.profile.ProfileWorkExperienceAdapter;
import com.myfutureapp.dashboard.profile.ui.EmploymentDetailsActivity;
import com.myfutureapp.login.DataHolder;

import java.util.ArrayList;

public class WorkExperinceUserFragment extends Fragment {

    private RecyclerView workExperinceRV;
    private ProfileWorkExperienceAdapter profileWorkExperienceAdapter;
    private TextView noWorkExperinceAddedYet;
    private TextView addNewWorkExperince;
    private BroadcastReceiver mReceiver;


    public WorkExperinceUserFragment() {
    }

    public static WorkExperinceUserFragment newInstance(String param1, String param2) {
        WorkExperinceUserFragment fragment = new WorkExperinceUserFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wrok_experince_user, container, false);
        inUi(view);
        receiverRegister();
        return view;

    }

    private void receiverRegister() {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (DataHolder.getInstance().getUserProfileDataresponse != null && DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.work_experience_data != null) {
                    if (DataHolder.getInstance().getUserProfileDataresponse.data.work_experience_data.size() != 0) {
                        profileWorkExperienceAdapter.notifyItem(DataHolder.getInstance().getUserProfileDataresponse.data.work_experience_data);
                        noWorkExperinceAddedYet.setVisibility(View.GONE);
                        workExperinceRV.setVisibility(View.VISIBLE);
                    } else {
                        noWorkExperinceAddedYet.setVisibility(View.VISIBLE);
                        workExperinceRV.setVisibility(View.GONE);
                    }
                } else {
                    noWorkExperinceAddedYet.setVisibility(View.VISIBLE);
                    workExperinceRV.setVisibility(View.GONE);
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter(getString(R.string.app_name) + "user.workExperince.refresh");
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, intentFilter);

    }

    private void inUi(View view) {

        noWorkExperinceAddedYet = view.findViewById(R.id.noWorkExperinceAddedYet);
        addNewWorkExperince = view.findViewById(R.id.addNewWorkExperince);

        workExperinceRV = view.findViewById(R.id.workExperinceRV);
        workExperinceRV.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        workExperinceRV.setLayoutManager(linearLayoutManager);
        workExperinceRV.setItemAnimator(new DefaultItemAnimator());
        workExperinceRV.setNestedScrollingEnabled(false);
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
        workExperinceRV.setAdapter(profileWorkExperienceAdapter);

        if (DataHolder.getInstance().getUserProfileDataresponse != null && DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.work_experience_data != null) {
            if (DataHolder.getInstance().getUserProfileDataresponse.data.work_experience_data.size() != 0) {
                profileWorkExperienceAdapter.notifyItem(DataHolder.getInstance().getUserProfileDataresponse.data.work_experience_data);
                noWorkExperinceAddedYet.setVisibility(View.GONE);
                workExperinceRV.setVisibility(View.VISIBLE);
            } else {
                noWorkExperinceAddedYet.setVisibility(View.VISIBLE);
                workExperinceRV.setVisibility(View.GONE);
            }
        } else {
            noWorkExperinceAddedYet.setVisibility(View.VISIBLE);
            workExperinceRV.setVisibility(View.GONE);
        }
        addNewWorkExperince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EmploymentDetailsActivity.class);
                startActivity(intent);
            }
        });
    }


}