package com.example.pc.myapplication.utils;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.pc.myapplication.AppConstant;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;


public class HttpService {

    private static OnRequestResponseListener mListener;

    public static interface OnRequestResponseListener {
        public void OnRequestSuccessResponse(String successResult);
        public void OnRequestErrorResponse(String errorResult);
    }
    public static void DoRequest(HashMap<String,String> hashMap,
                                 OnRequestResponseListener listener,
                                 String url,
                                 int method) {
        mListener = listener;
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mListener.OnRequestSuccessResponse(response);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mListener.OnRequestErrorResponse(volleyError.getMessage());
            }
        };

        HttpApi.Request(hashMap, responseListener, errorListener, url, method);
    }
}
