package com.myfutureapp.dashboard.bookmark.presenter;

import android.content.Context;

import com.myfutureapp.core.NetworkHandler;
import com.myfutureapp.core.NetworkManager;
import com.myfutureapp.core.model.BaseResponse;
import com.myfutureapp.dashboard.bookmark.ui.BookMarkView;
import com.myfutureapp.dashboard.home.model.BookMarkResponse;
import com.myfutureapp.dashboard.home.model.JobsForYouResponse;
import com.myfutureapp.util.AppConstants;
import com.myfutureapp.util.AppPreferences;
import com.myfutureapp.util.Helper;

import java.util.HashMap;

import okhttp3.Request;

public class BookMarkPresenter extends NetworkHandler {

    private BookMarkView bookMarkView;
    private final Context context;
    private String bookMarkType;
    private int position;

    public BookMarkPresenter(Context context, BookMarkView bookMarkView) {
        super(context, bookMarkView);
        this.bookMarkView = bookMarkView;
        this.context = context;
    }

    @Override
    public boolean handleFailure(Request request, Exception ex, String message) {
        if (bookMarkView != null) {
            bookMarkView.hideLoader();
            if (Helper.isContainValue(message)) {
                bookMarkView.showMessage(message);
            }
        }
        return super.handleFailure(request, ex, message);
    }

    @Override
    public boolean handleError(Request request, Error error) {
        if (bookMarkView != null) {
            bookMarkView.hideLoader();
            bookMarkView.showMessage(error.getMessage());
        }
        return super.handleError(request, error);
    }

    @Override
    public boolean handleResponse(Request request, BaseResponse response) {
        if (bookMarkView != null && response != null) {
            bookMarkView.hideLoader();
            if (response instanceof JobsForYouResponse) {
                JobsForYouResponse jobsForYouResponse = (JobsForYouResponse) response;
                bookMarkView.setJobsResponse(jobsForYouResponse);
            } else if (response instanceof BookMarkResponse) {
                BookMarkResponse bookMarkResponse = (BookMarkResponse) response;
                bookMarkView.setBookMarkResponse(bookMarkResponse, bookMarkType, position);
            }
        }
        return true;
    }

    public void onDestroyedView() {
        bookMarkView = null;
    }


    public void callBookMarksJobsApi(int page, String tag_id, String searchText) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        NetworkManager.getApi(context).getJobsForYou(headerMap, 1, tag_id, page, searchText).enqueue(this);
    }


    public void callBookMarkApi(String bookMarkType, JobsForYouResponse.JobDetail jobDetail, int position) {
        this.bookMarkType = bookMarkType;
        this.position = position;
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        NetworkManager.getApi(context).callBookmarkOpportunity(headerMap, String.valueOf(jobDetail.id), bookMarkType).enqueue(this);
    }
}