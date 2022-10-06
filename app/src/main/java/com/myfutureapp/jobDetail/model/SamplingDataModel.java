package com.myfutureapp.jobDetail.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SamplingDataModel {

    @SerializedName("eligibility_creteria")
    public List<EligibilityCreterias> eligibility_creteria;

    public class EligibilityCreterias {
        @SerializedName("data")
        public List<JobDetailResponse.JobDetailModel.EligibilityCreteria> data;

        public class data {
            @SerializedName("key")
            public String key;
            @SerializedName("value")
            public String value;
        }
    }
}
