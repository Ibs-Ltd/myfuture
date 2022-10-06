package com.myfutureapp.profile.presenter;

import android.content.Context;

import com.myfutureapp.core.NetworkHandler;
import com.myfutureapp.core.NetworkManager;
import com.myfutureapp.core.model.BaseResponse;
import com.myfutureapp.profile.model.StateWithRegionResponse;
import com.myfutureapp.profile.ui.JobLocationFragmentView;
import com.myfutureapp.util.Helper;

import okhttp3.Request;

public class JobLocationFragmentPresenter extends NetworkHandler {

    private JobLocationFragmentView jobLocationFragmentView;
    private final Context context;

    public JobLocationFragmentPresenter(Context context, JobLocationFragmentView jobLocationFragmentView) {
        super(context, jobLocationFragmentView);
        this.jobLocationFragmentView = jobLocationFragmentView;
        this.context = context;
    }

    @Override
    public boolean handleFailure(Request request, Exception ex, String message) {
        if (jobLocationFragmentView != null) {
            jobLocationFragmentView.hideLoader();
            if (Helper.isContainValue(message)) {
                jobLocationFragmentView.showMessage(message);
            }
        }
        return super.handleFailure(request, ex, message);
    }

    @Override
    public boolean handleError(Request request, Error error) {
        if (jobLocationFragmentView != null) {
            jobLocationFragmentView.hideLoader();
            jobLocationFragmentView.showMessage(error.getMessage());
        }
        return super.handleError(request, error);
    }

    @Override
    public boolean handleResponse(Request request, BaseResponse response) {
        if (jobLocationFragmentView != null && response != null) {
            jobLocationFragmentView.hideLoader();
            if (response instanceof StateWithRegionResponse) {
                StateWithRegionResponse stateWithRegionResponse = (StateWithRegionResponse) response;
                jobLocationFragmentView.setStateRegionListResponse(stateWithRegionResponse);
            }

        }
        return true;
    }

    public void onDestroyedView() {
        jobLocationFragmentView = null;
    }

    public void callStateRegionListApi() {
        NetworkManager.getApi(context).getStateWithRegion().enqueue(this);
    }
}