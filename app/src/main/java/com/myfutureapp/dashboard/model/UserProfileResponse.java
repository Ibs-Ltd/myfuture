package com.myfutureapp.dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myfutureapp.core.model.BaseResponse;

import java.util.List;

public class UserProfileResponse extends BaseResponse {

    @SerializedName("data")
    public UserDataModel data;

    public class UserDataModel {
        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("sunstone_student_id")
        @Expose
        public Object sunstoneStudentId;
        @SerializedName("web_user_id")
        @Expose
        public Object webUserId;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("profile_picture")
        @Expose
        public String profile_picture;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("sunstone_email")
        @Expose
        public Object sunstoneEmail;
        @SerializedName("mobile")
        @Expose
        public String mobile;
        @SerializedName("admission_year")
        @Expose
        public Object admissionYear;
        @SerializedName("enrolment_date")
        @Expose
        public Object enrolmentDate;
        @SerializedName("login_otp")
        @Expose
        public String loginOtp;
        @SerializedName("login_otp_time")
        @Expose
        public String loginOtpTime;
        @SerializedName("session_key")
        @Expose
        public String sessionKey;
        @SerializedName("role")
        @Expose
        public String role;
        @SerializedName("gender")
        @Expose
        public String gender;
        @SerializedName("pincode")
        @Expose
        public String pincode;
        @SerializedName("city")
        @Expose
        public String city;
        @SerializedName("state")
        @Expose
        public String state;
        @SerializedName("country")
        @Expose
        public Integer country;
        @SerializedName("address")
        @Expose
        public String address;
        @SerializedName("aadhar_no")
        @Expose
        public String aadharNo;
        @SerializedName("campus_id")
        @Expose
        public String campusId;
        @SerializedName("college_roll_no")
        @Expose
        public String collegeRollNo;
        @SerializedName("city_preference")
        @Expose
        public String cityPreference;
        @SerializedName("date_of_birth")
        @Expose
        public String dateOfBirth;
        @SerializedName("counseller_id")
        @Expose
        public String counsellerId;
        @SerializedName("profile_completion_status")
        @Expose
        public String profileCompletionStatus;
        @SerializedName("lead_source")
        @Expose
        public String leadSource;
        @SerializedName("utm_source")
        @Expose
        public String utmSource;
        @SerializedName("utm_medium")
        @Expose
        public String utmMedium;
        @SerializedName("utm_campaign")
        @Expose
        public String utmCampaign;
        @SerializedName("npf_user_id")
        @Expose
        public String npfUserId;
        @SerializedName("haptik_flag")
        @Expose
        public String haptikFlag;
        @SerializedName("is_haptik_inactive")
        @Expose
        public String isHaptikInactive;
        @SerializedName("linked_in_url")
        @Expose
        public String linkedInUrl;
        @SerializedName("whatsapp_no")
        @Expose
        public String whatsappNo;
        @SerializedName("alternate_no")
        @Expose
        public String alternateNo;
        @SerializedName("online_student_type")
        @Expose
        public String onlineStudentType;
        @SerializedName("lead_status")
        @Expose
        public String leadStatus;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("email_otp")
        @Expose
        public String emailOtp;
        @SerializedName("created_npf")
        @Expose
        public String createdNpf;
        @SerializedName("created")
        @Expose
        public String created;
        @SerializedName("modified")
        @Expose
        public String modified;
        @SerializedName("student_documents")
        @Expose
        public List<Object> studentDocuments = null;
        @SerializedName("student_certifications")
        @Expose
        public List<Object> studentCertifications = null;
        @SerializedName("city_data")
        @Expose
        public CityData cityData;
        @SerializedName("state_data")
        @Expose
        public StateData stateData;
        @SerializedName("specialisation")
        @Expose
        public String specialisation;

        @SerializedName("agreement")
        public boolean agreement;

        @SerializedName("applied_opportunity_details")
        public AppliedOpportunityDetails applied_opportunity_details;

        @SerializedName("student_data")
        public StudentData student_data;

        @SerializedName("work_experience_data")
        public List<WorkExperinceData> work_experience_data;

        @SerializedName("personal_possessions_dict")
        public PersonalPossessionsDict personalPossessionsDict;

        public class AppliedOpportunityDetails {

            @SerializedName("applied_count")
            public int applied_count;
            @SerializedName("total_count")
            public int total_count;
        }

        public class StudentData {
            @SerializedName("id")
            @Expose
            public Integer id;
            @SerializedName("user_id")
            @Expose
            public Integer userId;
            @SerializedName("graduation_year")
            @Expose
            public String graduationYear;
            @SerializedName("graduation_degree")
            @Expose
            public String graduationDegree;
            @SerializedName("graduation_specialisation")
            @Expose
            public String graduationSpecialisation;
            @SerializedName("graduation_percentage")
            @Expose
            public String graduationPercentage;
            @SerializedName("gradutaion_gpa")
            @Expose
            public String gradutaionGpa;
            @SerializedName("graduation_gpa_max")
            @Expose
            public String graduationGpaMax;
            @SerializedName("graduation_college")
            @Expose
            public String graduationCollege;
            @SerializedName("graduation_university")
            @Expose
            public String graduationUniversity;
            @SerializedName("management_exam_name")
            @Expose
            public String managementExamName;
            @SerializedName("management_exam_score")
            @Expose
            public String managementExamScore;
            @SerializedName("x_year")
            @Expose
            public String xYear;
            @SerializedName("x_percentage")
            @Expose
            public String xPercentage;
            @SerializedName("x_gpa")
            @Expose
            public String xGpa;
            @SerializedName("x_gpa_max")
            @Expose
            public String x_gpa_max;
            @SerializedName("xii_year")
            @Expose
            public String xiiYear;
            @SerializedName("xii_stream")
            @Expose
            public String xiiStream;
            @SerializedName("xii_percentage")
            @Expose
            public String xiiPercentage;
            @SerializedName("xii_gpa")
            @Expose
            public String xiiGpa;
            @SerializedName("xii_gpa_max")
            @Expose
            public String xii_gpa_max;
            @SerializedName("pg_year")
            @Expose
            public String pgYear;
            @SerializedName("pg_degree")
            @Expose
            public String pgDegree;
            @SerializedName("pg_percentage")
            @Expose
            public String pgPercentage;
            @SerializedName("pg_gpa")
            @Expose
            public String pgGpa;
            @SerializedName("pg_gpa_max")
            @Expose
            public String pgGpaMax;
            @SerializedName("pg_specialisation")
            @Expose
            public String pgSpecialisation;
            @SerializedName("pg_college")
            @Expose
            public String pgCollege;
            @SerializedName("pg_university")
            @Expose
            public String pgUniversity;
            @SerializedName("family_income")
            @Expose
            public String familyIncome;
            @SerializedName("work_experience")
            @Expose
            public String workExperience;
            @SerializedName("parent_name")
            @Expose
            public String parentName;
            @SerializedName("parent_no")
            @Expose
            public String parentNo;
            @SerializedName("parent_relation")
            @Expose
            public String parentRelation;
            @SerializedName("parent_address")
            @Expose
            public String parentAddress;
            @SerializedName("parent_aadhar")
            @Expose
            public String parentAadhar;
            @SerializedName("gaurdian_name")
            @Expose
            public String gaurdianName;
            @SerializedName("gaurdian_no")
            @Expose
            public String gaurdianNo;
            @SerializedName("guardian_relationship")
            @Expose
            public String guardianRelationship;
            @SerializedName("local_guardian_name")
            @Expose
            public String localGuardianName;
            @SerializedName("local_guardian_no")
            @Expose
            public String localGuardianNo;
            @SerializedName("local_guardian_relationship")
            @Expose
            public String localGuardianRelationship;
            @SerializedName("local_guardian_address")
            @Expose
            public String localGuardianAddress;
            @SerializedName("schedule_test_time")
            @Expose
            public String scheduleTestTime;
            @SerializedName("se_test_score")
            @Expose
            public String seTestScore;
            @SerializedName("se_test_max_score")
            @Expose
            public String seTestMaxScore;
            @SerializedName("se_pi_score")
            @Expose
            public String sePiScore;
            @SerializedName("se_pi_max_score")
            @Expose
            public String sePiMaxScore;
            @SerializedName("specialisation")
            @Expose
            public String specialisation;
            @SerializedName("category")
            @Expose
            public String category;
            @SerializedName("preferred_location")
            @Expose
            public String preferredLocation;
            @SerializedName("student_journey_status")
            @Expose
            public String studentJourneyStatus;
            @SerializedName("created")
            @Expose
            public String created;
            @SerializedName("modified")
            @Expose
            public String modified;

        }

        public class CityData {

            @SerializedName("name")
            public String name;
        }

        public class StateData {

            @SerializedName("name")
            public String name;
        }

        public class WorkExperinceData {

            @SerializedName("id")
            public int id;
            @SerializedName("company")
            public String company;
            @SerializedName("designation")
            public String designation;
            @SerializedName("role")
            public String role;
            @SerializedName("salary")
            public int salary;
            @SerializedName("currently_working")
            public String currently_working;
            @SerializedName("start_date")
            public String start_date;
            @SerializedName("end_date")
            public String end_date;
            /*   @SerializedName("job_description")
               public String job_description;
            */
            @SerializedName("status")
            public String status;

        }
    }

    public class PersonalPossessionsDict {

        @SerializedName("broadband")
        public Broadband broadband;
        @SerializedName("two_wheeler")
        public TwoWheeler two_wheeler;
        @SerializedName("laptop")
        public Laptop laptop;
        @SerializedName("driving_license")
        public DrivingLicense driving_license;
    }

    public class Broadband {
        @SerializedName("possession_value")
        public String possession_value;
    }

    public class TwoWheeler {
        @SerializedName("possession_value")
        public String possession_value;
    }

    public class Laptop {
        @SerializedName("possession_value")
        public String possession_value;
    }

    public class DrivingLicense {
        @SerializedName("possession_value")
        public String possession_value;
    }
}
