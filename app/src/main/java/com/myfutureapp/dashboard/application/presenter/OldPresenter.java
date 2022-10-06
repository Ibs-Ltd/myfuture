package com.myfutureapp.dashboard.application.presenter;

import android.content.Context;

import com.myfutureapp.core.NetworkHandler;
import com.myfutureapp.core.NetworkManager;
import com.myfutureapp.core.model.BaseResponse;
import com.myfutureapp.dashboard.application.model.OldApplicationResponse;
import com.myfutureapp.dashboard.application.ui.OldView;
import com.myfutureapp.util.AppConstants;
import com.myfutureapp.util.AppPreferences;
import com.myfutureapp.util.Helper;

import java.util.HashMap;

import okhttp3.Request;

public class OldPresenter extends NetworkHandler {

    private OldView oldView;
    private final Context context;
    private String jobId;

    public OldPresenter(Context context, OldView oldView) {
        super(context, oldView);
        this.oldView = oldView;
        this.context = context;
    }

    @Override
    public boolean handleFailure(Request request, Exception ex, String message) {
        if (oldView != null) {
            oldView.hideLoader();
            if (Helper.isContainValue(message)) {
                oldView.showMessage(message);
            }
        }
        return super.handleFailure(request, ex, message);
    }

    @Override
    public boolean handleError(Request request, Error error) {
        if (oldView != null) {
            oldView.hideLoader();
            oldView.showMessage(error.getMessage());
        }
        return super.handleError(request, error);
    }

    @Override
    public boolean handleResponse(Request request, BaseResponse response) {
        if (oldView != null && response != null) {
            oldView.hideLoader();
            if (response instanceof OldApplicationResponse) {
                OldApplicationResponse oldApplicationResponse = (OldApplicationResponse) response;
                oldView.setOldApplicationsResponse(oldApplicationResponse);
            }

        }
        return true;
    }

    public void onDestroyedView() {
        oldView = null;
    }

    public void callOldApplicationsApi(int page) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        NetworkManager.getApi(context).getOldApplications(headerMap, page).enqueue(this);
    }
}