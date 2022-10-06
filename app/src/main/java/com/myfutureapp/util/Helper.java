package com.myfutureapp.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.myfutureapp.dashboard.model.UserProfileResponse;
import com.myfutureapp.login.DataHolder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {

    public static boolean isContainValue(String value) {
        return (value != null && !TextUtils.isEmpty(value) && !value.equalsIgnoreCase("null"));
    }

    public static boolean isNetworkAvailable(Context mcontext) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null;
    }

    public static boolean isValidEmail(CharSequence target) {

        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private static final Pattern sPattern = Pattern.compile("[0-9]+(\\.[0-9]{1,2})?%?");

    private static final Pattern sPatterncgpa = Pattern.compile("[0-9]+(\\.[0-9]{1,2})?%?");

    public static boolean isValidSgpa(CharSequence s) {
        return sPatterncgpa.matcher(s).matches();
    }

    public static boolean isValidCgpa(CharSequence s) {
        return sPattern.matcher(s).matches();
    }

    public static boolean isEmailValid(String email) {

        String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches())
            return true;
        else
            return false;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }

        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static long getTimeInMillisecondsFromDate(String givenDateString) {
        String dateFormat = "yyyy-MM-dd kk:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            Date mDate = sdf.parse(givenDateString);
            return mDate.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static float convertDpToPixel(Context context, float dp) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float convertPixelsToDp(Context context, float px) {
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static void postDelayThreeSecond(final View v) {
        v.setClickable(false);
        v.postDelayed(new Runnable() {
            @Override
            public void run() {
                v.setClickable(true);
            }
        }, 3000);
    }

    public static boolean checkingBasicDetails() {
        if (DataHolder.getInstance().getUserProfileDataresponse != null && DataHolder.getInstance().getUserProfileDataresponse.data != null
                && DataHolder.getInstance().getUserProfileDataresponse.data != null) {
            UserProfileResponse.UserDataModel student_data = DataHolder.getInstance().getUserProfileDataresponse.data;
            if (student_data.name != null && student_data.email != null && student_data.gender != null && student_data.city != null) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static boolean checkingGraduation() {
        if (DataHolder.getInstance().getUserProfileDataresponse != null && DataHolder.getInstance().getUserProfileDataresponse.data != null
                && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null) {
            UserProfileResponse.UserDataModel.StudentData student_data = DataHolder.getInstance().getUserProfileDataresponse.data.student_data;
            if (student_data.graduationDegree != null) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static boolean checkingPostGraduation() {
        if (DataHolder.getInstance().getUserProfileDataresponse != null && DataHolder.getInstance().getUserProfileDataresponse.data != null
                && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null) {
            UserProfileResponse.UserDataModel.StudentData student_data = DataHolder.getInstance().getUserProfileDataresponse.data.student_data;
            if (student_data.pgDegree != null) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static boolean checkingXII() {
        if (DataHolder.getInstance().getUserProfileDataresponse != null && DataHolder.getInstance().getUserProfileDataresponse.data != null
                && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null) {
            UserProfileResponse.UserDataModel.StudentData student_data = DataHolder.getInstance().getUserProfileDataresponse.data.student_data;
            if (student_data.xiiPercentage != null || student_data.xiiGpa != null) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static boolean checkingX() {
        if (DataHolder.getInstance().getUserProfileDataresponse != null && DataHolder.getInstance().getUserProfileDataresponse.data != null
                && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null) {
            UserProfileResponse.UserDataModel.StudentData student_data = DataHolder.getInstance().getUserProfileDataresponse.data.student_data;
            if (student_data.xPercentage != null || student_data.xPercentage != null) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static boolean checkingWorkExperince() {
        if (DataHolder.getInstance().getUserProfileDataresponse != null && DataHolder.getInstance().getUserProfileDataresponse.data != null
                && DataHolder.getInstance().getUserProfileDataresponse.data.student_data != null) {
            UserProfileResponse.UserDataModel.StudentData student_data = DataHolder.getInstance().getUserProfileDataresponse.data.student_data;
            if (student_data.workExperience != null) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static boolean checkingAdditionalInformation() {
        if (DataHolder.getInstance().getUserProfileDataresponse != null && DataHolder.getInstance().getUserProfileDataresponse.data != null
                && DataHolder.getInstance().getUserProfileDataresponse.data.personalPossessionsDict != null) {
            UserProfileResponse.PersonalPossessionsDict personalPossessionsDict = DataHolder.getInstance().getUserProfileDataresponse.data.personalPossessionsDict;
            if (personalPossessionsDict.broadband == null) {
                return false;
            }
            if (personalPossessionsDict.laptop == null) {
                return false;
            }
            if (personalPossessionsDict.driving_license == null) {
                return false;
            }
            if (personalPossessionsDict.two_wheeler == null) {
                return false;
            }
            return true;
        }
        return false;
    }
}