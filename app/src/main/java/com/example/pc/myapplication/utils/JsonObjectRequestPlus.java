package com.example.pc.myapplication.utils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class JsonObjectRequestPlus extends JsonObjectRequest {

    private Map<String, String> sendHeader=new HashMap<>(1);

    public JsonObjectRequestPlus(int method,
                                 String url,
                                 JSONObject jsonObject,
                                 Response.Listener<JSONObject> listener,
                                 Response.ErrorListener errorListener) {

        super(method, url, jsonObject, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();

        if(headers == null || headers.equals(Collections.emptyMap())) {

            headers = new HashMap<>();
        }

        RequestQueueController.get().addSessionCookie(headers);
        return headers;
    }
}
