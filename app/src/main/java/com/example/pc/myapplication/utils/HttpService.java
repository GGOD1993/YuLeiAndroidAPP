package com.example.pc.myapplication.utils;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.pc.myapplication.AppConstant;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;


public class HttpService {

    /**
     * 登录请求
     */
    private static OnLoginRequestResponseListener mLoginRequestListener;
    public static interface OnLoginRequestResponseListener {
        public void OnLoginSuccessResponse(JSONArray jsonArray);
        public void OnLoginErrorResponse(String errorResult);
    }
    public static void DoLoginRequest (int method,
                                       String url,
                                       HashMap<String,String> hashMap,
                                       OnLoginRequestResponseListener listener
                                       ) {
        mLoginRequestListener = listener;
        Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                mLoginRequestListener.OnLoginSuccessResponse(jsonArray);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mLoginRequestListener.OnLoginErrorResponse(volleyError.getMessage());
            }
        };
        HttpApi.DoJsonArrayRequest(method, url, hashMap, responseListener, errorListener);
    }

    /**
     * 获得当前用户
     */
    private static OnGetCurrentUserRequestResponseListener mGetCurrentUserListener;
    public static interface OnGetCurrentUserRequestResponseListener {
        public void OnGetCurrentUserSuccessResponse(JSONArray jsonArray);
        public void OnGetCurrentUserErrorResponse(String errorResult);
    }
    public static void DoGetCurrentUserRequest (int method,
                                                String url,
                                                HashMap<String,String> hashMap,
                                                OnGetCurrentUserRequestResponseListener listener
                                                ) {
        mGetCurrentUserListener = listener;
        Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                mGetCurrentUserListener.OnGetCurrentUserSuccessResponse(jsonArray);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mGetCurrentUserListener.OnGetCurrentUserErrorResponse(volleyError.getMessage());
            }
        };

        if (hashMap == null) {
            HttpApi.DoJsonArrayRequest(method, url, null, responseListener, errorListener);

        } else {
            HttpApi.DoJsonArrayRequest(method, url, hashMap, responseListener, errorListener);
        }
    }

    /**
     * 登出请求
     */
    private static OnLogoutRequestResponseListener onLogoutRequestResponseListener;
    public static interface OnLogoutRequestResponseListener {
        public void OnLogoutSuccessResponse(JSONArray jsonArray);
        public void OnLogoutErrorResponse (String errorMsg);
    }
    public static void DoLogoutRequest (int method,
                                        String url,
                                        HashMap<String,String> hashMap,
                                        OnLogoutRequestResponseListener listener) {
        onLogoutRequestResponseListener = listener;
        Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                onLogoutRequestResponseListener.OnLogoutSuccessResponse(jsonArray);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                onLogoutRequestResponseListener.OnLogoutErrorResponse(volleyError.getMessage());
            }
        };

        HttpApi.DoJsonArrayRequest(method, url, hashMap, responseListener, errorListener);

    }

    /**
     * 添加自定义任务请求
     */
    private static OnSetDiyTaskRequestResponseListener onSetDiyTaskRequestResponseListener;
    public static interface OnSetDiyTaskRequestResponseListener {
        public void OnSetDiyTaskSuccessResponse(JSONArray jsonArray);
        public void OnSetDiyTaskErrorResponse (String errorMsg);
    }
    public static void DoSetDiyTaskRequest (int method,
                                        String url,
                                        HashMap<String,String> hashMap,
                                        OnSetDiyTaskRequestResponseListener listener) {
        onSetDiyTaskRequestResponseListener = listener;
        Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                onSetDiyTaskRequestResponseListener.OnSetDiyTaskSuccessResponse(jsonArray);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                onSetDiyTaskRequestResponseListener.OnSetDiyTaskErrorResponse(volleyError.getMessage());
            }
        };

        HttpApi.DoJsonArrayRequest(method, url, hashMap, responseListener, errorListener);
    }

    /**
     * 获取系统任务请求
     */
    private static OnGetSysTaskRequestResponseListener onGetSysTaskRequestResponseListener;
    public static interface OnGetSysTaskRequestResponseListener {
        public void OnGetSysTaskSuccessResponse(JSONArray jsonArray);
        public void OnGetSysTaskErrorResponse (String errorMsg);
    }
    public static void DoGetSysTaskRequest (int method,
                                            String url,
                                            HashMap<String,String> hashMap,
                                            OnGetSysTaskRequestResponseListener listener) {
        onGetSysTaskRequestResponseListener = listener;
        Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                onGetSysTaskRequestResponseListener.OnGetSysTaskSuccessResponse(jsonArray);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                onGetSysTaskRequestResponseListener.OnGetSysTaskErrorResponse(volleyError.getMessage());
            }
        };

        HttpApi.DoJsonArrayRequest(method, url, hashMap, responseListener, errorListener);
    }

    /**
     * 父母获取发出的心愿
     */
    private static OnGetTaskRequestResponseListener onGetTaskRequestResponseListener;
    public static interface OnGetTaskRequestResponseListener {
        public void OnGetTaskSuccessResponse(JSONArray jsonArray);
        public void OnGetTaskErrorResponse (String errorMsg);
    }
    public static void DoGetTaskRequest (int method,
                                            String url,
                                            HashMap<String,String> hashMap,
                                            OnGetTaskRequestResponseListener listener) {
        onGetTaskRequestResponseListener = listener;
        Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                onGetTaskRequestResponseListener.OnGetTaskSuccessResponse(jsonArray);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                onGetTaskRequestResponseListener.OnGetTaskErrorResponse(volleyError.getMessage());
            }
        };

        HttpApi.DoJsonArrayRequest(method, url, hashMap, responseListener, errorListener);
    }
}
