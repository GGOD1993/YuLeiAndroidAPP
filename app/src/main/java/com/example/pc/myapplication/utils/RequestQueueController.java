package com.example.pc.myapplication.utils;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.pc.myapplication.AppConstant;

import java.util.Map;

public class RequestQueueController extends Application{
    private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static final String COOKIE_KEY = "Cookie";
    private static final String COOKIE_USERNAME = "username";

    private RequestQueue _requestQuene;
    private SharedPreferences _preferences;
    private static RequestQueueController _instance;

    public static RequestQueueController get() {
        return _instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
        _preferences = getSharedPreferences(AppConstant.PREFERENCE_NAME,0);
        _requestQuene = Volley.newRequestQueue(this);
    }

    public RequestQueue getRequestQueue() {
        return _requestQuene;
    }

    public final void checkSessionCookie(Map<String, String> headers) {

        if (headers.containsKey(SET_COOKIE_KEY)) {

            String cookie = headers.get(SET_COOKIE_KEY);
            if((cookie.length()) > 0 && (!cookie.contains("saeut"))) {
                String[] splitCookie = cookie.split(";");
                String[] splitSessionId = splitCookie[0].split("=");
                cookie = splitSessionId[1];
                SharedPreferences.Editor prefEditor = _preferences.edit();
                prefEditor.putString(COOKIE_USERNAME, cookie);
                prefEditor.apply();
            }
        }
    }

    public final void addSessionCookie(Map<String, String> headers) {

        String sessionId = _preferences.getString(COOKIE_USERNAME, "");
        if (sessionId.length() > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append(COOKIE_USERNAME);
            builder.append("=");
            builder.append(sessionId);
            if (headers.containsKey(COOKIE_KEY)) {
                builder.append("; ");
                builder.append(headers.get(COOKIE_KEY));
            }
            headers.put(COOKIE_KEY, builder.toString());
        }
    }

}
