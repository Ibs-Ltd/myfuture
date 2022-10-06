package com.myfutureapp.dashboard.application.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.myfutureapp.R;
import com.myfutureapp.dashboard.application.adapter.OldApplicationAdapter;
import com.myfutureapp.dashboard.application.model.OldApplicationResponse;
import com.myfutureapp.dashboard.application.presenter.OldPresenter;
import com.myfutureapp.login.LoginActivity;
import com.myfutureapp.util.AppConstants;
import com.myfutureapp.util.AppPreferences;
import com.myfutureapp.util.Helper;

import java.util.ArrayList;

public class OldFragment extends Fragment implements OldView {

    private RecyclerView oldApplications;
    private boolean dataLoaded = false;
    private int page = 0;
    private OldApplicationAdapter oldApplicationAdapter;
    private OldPresenter oldPresenter;
    private BottomSheetDialog scheduleBottomDialog;
    private TextView noOldApplication;
    private ShimmerFrameLayout shimmerFrameLayout;

    public OldFragment() {
    }

    public static OldFragment newInstance(String param1, String param2) {
        OldFragment fragment = new OldFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_old, container, false);
        inUi(view);
        return view;

    }

    private void inUi(View view) {
        oldPresenter = new OldPresenter(getContext(), OldFragment.this);
        oldApplications = view.findViewById(R.id.oldApplications);
        noOldApplication = view.findViewById(R.id.noOldApplication);
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout);
        oldApplications.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        oldApplications.setLayoutManager(linearLayoutManager);
        oldApplications.setItemAnimator(new DefaultItemAnimator());
        oldApplications.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) { // only when scrolling up
                    final int visibleThreshold = 4;
                    int lastItem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                    int currentTotalCount = linearLayoutManager.getItemCount();
                    if (currentTotalCount <= lastItem + visibleThreshold && dataLoaded && page != -1) {
                        if (Helper.isNetworkAvailable(getContext())) {
                            dataLoaded = false;
                            oldPresenter.callOldApplicationsApi(page);
                        }
                    }
                }
            }
        });

        oldApplicationAdapter = new OldApplicationAdapter(getActivity(), new ArrayList<>());
        oldApplications.setAdapter(oldApplicationAdapter);
        oldApplications.setVisibility(View.GONE);
        LinearLayout loginLL = view.findViewById(R.id.loginLL);
        loginLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), LoginActivity.class).addFlags(Intent.
                        FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });
        if (!AppPreferences.getInstance(getContext()).getPreferencesString(AppConstants.AUTH).equalsIgnoreCase("true")) {
            loginLL.setVisibility(View.VISIBLE);
            oldApplications.setVisibility(View.GONE);
            noOldApplication.setVisibility(View.GONE);
            shimmerFrameLayout.setVisibility(View.GONE);
        } else {
            shimmerFrameLayout.startShimmer();
            oldPresenter.callOldApplicationsApi(page);
            loginLL.setVisibility(View.GONE);

        }
    }

    @Override
    public void setOldApplicationsResponse(OldApplicationResponse oldApplicationResponse) {
        if (oldApplicationResponse != null && oldApplicationResponse.success) {
            if (oldApplicationResponse.data != null && oldApplicationResponse.data.history != null) {
                if (page == 0) {
                    if (oldApplicationResponse.data.history.size() == 0) {
                        noOldApplication.setVisibility(View.VISIBLE);
                        oldApplications.setVisibility(View.GONE);
                    } else {
                        oldApplicationAdapter.notifyItem(oldApplicationResponse.data.history);
                        noOldApplication.setVisibility(View.GONE);
                        oldApplications.setVisibility(View.VISIBLE);
                    }

                    oldApplications.setVisibility(View.VISIBLE);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);

                } else {
                    oldApplicationAdapter.addMoreData(oldApplicationResponse.data.history);
                }
                if (oldApplicationResponse.data.history.size() == 10) {
                    page = page + 1;
                    dataLoaded = true;
                } else {
                    page = -1;
                    dataLoaded = false;
                }
            }
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