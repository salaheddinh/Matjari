package com.salaheddin.store.models;

import org.json.JSONObject;

public class WebServiceResponse {
    private final int code;
    private final String errorMessage;
    private final JSONObject data;

    public WebServiceResponse(int code, String errorMessage, JSONObject data) {
        this.code = code;
        this.errorMessage = errorMessage;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public JSONObject getData() {
        return data;
    }
}

