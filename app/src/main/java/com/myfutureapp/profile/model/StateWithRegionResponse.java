package com.myfutureapp.profile.model;

import com.google.gson.annotations.SerializedName;
import com.myfutureapp.core.model.BaseResponse;

import java.util.List;

public class StateWithRegionResponse extends BaseResponse {


    @SerializedName("data")
    public List<StateRegionResponse> data;


    public class StateRegionResponse {

        @SerializedName("region")
        public String region;
        @SerializedName("state")
        public List<StateResponse> state;
        public boolean citySelected = false;

        public class StateResponse {
            @SerializedName("name")
            public String name;
            @SerializedName("id")
            public int id;
            public boolean citySelected = false;

            @SerializedName("selected_url")
            public String selected_url;
            @SerializedName("unselected_url")
            public String unselected_url;

        }
    }

}
