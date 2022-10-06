package com.myfutureapp.core;

import android.content.Context;

public class NetworkManager {

    public static final String BASE_URL_API_STAGING = "https://dev-hub-console-api.sunstone.edu.in/";
    private static final String BASE_URL_API_PRODUCTION = "https://hub-console-api.sunstone.edu.in/";

    public static ApiInterface getApi(Context context) {
        return RetrofitClient.getClient(BASE_URL_API_STAGING, context)
                .create(ApiInterface.class);
    }

   /* public static ApiInterface getRXJAVAApi(Context context) {
        return RetrofitClient.getRXJavaClient(BASE_URL_API_STAGING,context)
                .create(ApiInterface.class);
    }*/

}