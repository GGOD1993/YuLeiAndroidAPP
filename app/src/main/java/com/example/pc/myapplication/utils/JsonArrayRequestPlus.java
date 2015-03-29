package com.example.pc.myapplication.utils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class JsonArrayRequestPlus extends JsonArrayRequest {

    private Map<String, String> sendHeader=new HashMap<String, String>(1);

    public JsonArrayRequestPlus(String url,
                                Response.Listener<JSONArray> listener,
                                Response.ErrorListener errorListener) {

        super(url, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();

        if(headers == null || headers.equals(Collections.emptyMap())) {

            headers = new HashMap<String, String>();
        }

        RequestQueueController.get().addSessionCookie(headers);
        return headers;
    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {

        return super.parseNetworkResponse(response);
    }
}
