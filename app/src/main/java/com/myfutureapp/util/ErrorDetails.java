package com.myfutureapp.util;

/**
 * Created by Deepak Chaudhary on 20-11-2020.
 */
public class ErrorDetails {
        Boolean isError;
        String message;

    public ErrorDetails(Boolean isError, String message) {
        this.isError = isError;
        this.message = message;
    }

        public Boolean getError() {
        return isError;
    }

        public void setError(Boolean error) {
        isError = error;
    }

        public String getMessage() {
        return message;
    }

        public void setMessage(String message) {
        this.message = message;
    }
    }

