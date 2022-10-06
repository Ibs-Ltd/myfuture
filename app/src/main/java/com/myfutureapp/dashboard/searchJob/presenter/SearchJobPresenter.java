package com.myfutureapp.dashboard.searchJob.presenter;

import android.content.Context;

import com.myfutureapp.core.NetworkHandler;
import com.myfutureapp.core.NetworkManager;
import com.myfutureapp.core.model.BaseResponse;
import com.myfutureapp.dashboard.home.model.BookMarkResponse;
import com.myfutureapp.dashboard.home.model.JobsForYouResponse;
import com.myfutureapp.dashboard.searchJob.ui.SearchActivityView;
import com.myfutureapp.util.AppConstants;
import com.myfutureapp.util.AppPreferences;
import com.myfutureapp.util.Helper;

import java.util.HashMap;

import okhttp3.Request;

public class SearchJobPresenter extends NetworkHandler {

    private SearchActivityView searchActivityView;
    private final Context context;
    private String bookMarkType;
    private int position;


    public SearchJobPresenter(Context context, SearchActivityView searchActivityView) {
        super(context, searchActivityView);
        this.searchActivityView = searchActivityView;
        this.context = context;
    }

    @Override
    public boolean handleFailure(Request request, Exception ex, String message) {
        if (searchActivityView != null) {
            searchActivityView.hideLoader();
            if (Helper.isContainValue(message)) {
                searchActivityView.showMessage(message);
            }
        }
        return super.handleFailure(request, ex, message);
    }

    @Override
    public boolean handleError(Request request, Error error) {
        if (searchActivityView != null) {
            searchActivityView.hideLoader();
            searchActivityView.showMessage(error.getMessage());
        }
        return super.handleError(request, error);
    }

    @Override
    public boolean handleResponse(Request request, BaseResponse response) {
        if (searchActivityView != null && response != null) {
            searchActivityView.hideLoader();
            if (response instanceof JobsForYouResponse) {
                JobsForYouResponse jobsForYouResponse = (JobsForYouResponse) response;
                searchActivityView.setJobsResponse(jobsForYouResponse);
            } else if (response instanceof BookMarkResponse) {
                BookMarkResponse bookMarkResponse = (BookMarkResponse) response;
                searchActivityView.setBookMarkResponse(bookMarkResponse, bookMarkType, position);
            }
        }
        return true;
    }

    public void onDestroyedView() {
        searchActivityView = null;
    }


    public void callGetJobsApi(int page, String tag_id, String searchText) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        NetworkManager.getApi(context).getJobsForYou(headerMap, 0, tag_id, page, searchText).enqueue(this);
    }


    public void callBookMarkApi(String bookMarkType, JobsForYouResponse.JobDetail jobDetail, int position) {
        this.bookMarkType = bookMarkType;
        this.position = position;
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        NetworkManager.getApi(context).callBookmarkOpportunity(headerMap, String.valueOf(jobDetail.id), bookMarkType).enqueue(this);
    }
}