package com.myfutureapp.core;

import com.google.gson.JsonObject;
import com.myfutureapp.dashboard.application.model.OldApplicationResponse;
import com.myfutureapp.dashboard.home.model.BookMarkResponse;
import com.myfutureapp.dashboard.home.model.JobTagsResponse;
import com.myfutureapp.dashboard.home.model.JobsForYouResponse;
import com.myfutureapp.dashboard.home.model.OngoingApplicationsResponse;
import com.myfutureapp.dashboard.homeProfile.model.UserProfilePicModel;
import com.myfutureapp.dashboard.model.UserProfileResponse;
import com.myfutureapp.dashboard.profile.model.AddEditWorkExperienceResponse;
import com.myfutureapp.dashboard.profile.model.DeleteWorkExperinceModel;
import com.myfutureapp.dashboard.profile.model.ProfileCityListRsponseModel;
import com.myfutureapp.dashboard.profile.model.ProfileStateListResponseModel;
import com.myfutureapp.dashboard.profile.model.UpdateLocationPreferenceResponse;
import com.myfutureapp.enrollment.model.EnrollUserResponse;
import com.myfutureapp.jobDetail.model.ApplyOpportunityResponse;
import com.myfutureapp.jobDetail.model.EventApplyResponse;
import com.myfutureapp.jobDetail.model.InterviewTimeSlotsResponse;
import com.myfutureapp.jobDetail.model.JobDetailResponse;
import com.myfutureapp.jobDetail.model.ScheduleInterviewResponse;
import com.myfutureapp.login.model.CreateUserResponse;
import com.myfutureapp.login.model.OtpVerifyResponse;
import com.myfutureapp.profile.model.CityListResponse;
import com.myfutureapp.profile.model.GraduationCourseResponse;
import com.myfutureapp.profile.model.PostGraduationCourseResponse;
import com.myfutureapp.profile.model.StateWithRegionResponse;
import com.myfutureapp.profile.model.UserProfileUpDataResponse;

import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {


    // Used for signup process
    @FormUrlEncoded
    @POST("sdp/requestOtp")
    Call<CreateUserResponse> requestOtp(@Field("mobile") String mobile);

    // For verify the otp
    @FormUrlEncoded
    @POST("sdp/verifyOtp")
    Call<OtpVerifyResponse> verifyOtp(@Field("mobile") String mobile, @Field("otp") String otp);

    // For graduation courses list
    @GET("sdp/fetchGraduationDegreeList")
    Call<GraduationCourseResponse> graduationCourse();

    // For post Graduation courses list
    @GET("api/fetchPostGraduationList")
    Call<GraduationCourseResponse> postGraduationCourse();

    // For post Graduation courses list
    @GET("api/fetchPostGraduationList")
    Call<PostGraduationCourseResponse> postGraduationCourses();

    // For getting list of city
    @GET("api/fetchCityList")
    Call<CityListResponse> cityList();

    // For getting list of city
    @GET("api/fetchStateWithRegion")
    Call<StateWithRegionResponse> getStateWithRegion();

    // For getting list of city
    @POST("sdp/updateUserData")
    Call<UserProfileUpDataResponse> userProfileUploadApi(@HeaderMap HashMap<String, String> token, @Body JsonObject jsonObject);

    @GET("student/fetchOpportunity")
    Call<JobDetailResponse> opportunityDetail(@HeaderMap HashMap<String, String> token, @Query("id") String id, @Query("app_type") String app_type);

    //For getting tags
    @GET("sdp/getOpportunityTags")
    Call<JobTagsResponse> getTags(@HeaderMap HashMap<String, String> token);

    @GET("sdp/getOpportunity")
    Call<JobsForYouResponse> getJobsForYou(@HeaderMap HashMap<String, String> token, @Query("type") int type, @Query("tag_id") String tag_id, @Query("page") int page, @Query("search") String search);

    @GET("sdp/getOpportunity")
    Observable<JobsForYouResponse> getJobsForYou1(@HeaderMap HashMap<String, String> token, @Query("type") int type, @Query("tag_id") String tag_id, @Query("page") int page, @Query("search") String search);

    //For ongoing Applications
    @GET("student/fetchOnGoingApplications")
    Call<OngoingApplicationsResponse> getOngoingApplications(@HeaderMap HashMap<String, String> token, @Query("is_home") String is_home, @Query("page") String page);

    //For getting Old Applications
    @GET("student/fetchAcceptedOffers")
    Call<OldApplicationResponse> getOldApplications(@HeaderMap HashMap<String, String> token, @Query("page") int page);

    //For getting enroll user list
    @GET("sdp/getEnrollmentUserData")
    Call<EnrollUserResponse> getEnrolledUser(@HeaderMap HashMap<String, String> token);

    //For update enroll status
    @POST("sdp/signAgreement")
    Call<EnrollUserResponse> enrollStatusUpdate(@HeaderMap HashMap<String, String> token);

    //For getting interview time slots
    @GET("sdp/getTimeSlots")
    Call<InterviewTimeSlotsResponse> getInterviewTimeSlots(@HeaderMap HashMap<String, String> token, @Query("id") String id);

    //For getting studentData
    @GET("student/fetchStudentProfile")
    Call<UserProfileResponse> getUserProfile(@HeaderMap HashMap<String, String> token, @Query("id") String id);

    //For apply opportunities
    @GET("student/applyForOpportunities")
    Call<ApplyOpportunityResponse> applyOpportunity(@HeaderMap HashMap<String, String> token, @Query("id") String id, @Query("flag") String flag);

    //For apply opportunities
    @POST("student/insertUserEligibleRow")
    Call<EventApplyResponse> applyEventEntering(@HeaderMap HashMap<String, String> token, @Body JsonObject jsonObject);

    //For schedule Interview
    @POST("sdp/scheduleInterview")
    Call<ScheduleInterviewResponse> scheduleInterview(@HeaderMap HashMap<String, String> token, @Body JsonObject jsonObject);

    //For bookmark and unbookmark job
    @POST("sdp/bookmarkOpportunity")
    Call<BookMarkResponse> callBookmarkOpportunity(@HeaderMap HashMap<String, String> token, @Query("id") String id, @Query("type") String type);

    //For updating profile
    @POST("sdp/updateUserData")
    Call<UserProfileUpDataResponse> userProfileUpdateApi(@HeaderMap HashMap<String, String> token, @Body JsonObject jsonObject);

    //For updating profile pic
    @Multipart
    @POST("sdp/uploadProfilePicture")
    Call<UserProfilePicModel> userProfilePicUpdateApi(@HeaderMap HashMap<String, String> token, @Part MultipartBody.Part filePart);

    //For Add Edit Work Experience
    @POST("student/addEditWorkExperience")
    Call<AddEditWorkExperienceResponse> addEditWorkExperienceUpdate(@HeaderMap HashMap<String, String> token, @Body JsonObject jsonObject);

    //For update opportunities
    @GET("student/updateLocationPreference")
    Call<UpdateLocationPreferenceResponse> updateLocationPreference(@HeaderMap HashMap<String, String> token, @Query("preferences") String preferences, @Query("id") String id);

    //For state list
    @GET("api/fetchStateList")
    Call<ProfileStateListResponseModel> profilestatelist();

    //For city list
    @GET("api/fetchCityFromState")
    Call<ProfileCityListRsponseModel> profilecitylist(@Query("state_id") String id);

    //For delete work experince
    @GET("student/deleteWorkExperience")
    Call<DeleteWorkExperinceModel> deleteWorkExperince(@HeaderMap HashMap<String, String> token, @Query("id") String id);
}
