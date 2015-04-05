package com.example.pc.myapplication.utils;

import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.pc.myapplication.AppConstant;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 登录时的Post请求
 * 获取并将cookie存储在本地
 */
public class StringPostRequestPlus extends StringRequest {


  public StringPostRequestPlus(int method, String url,
                               Response.Listener<String> listener,
                               Response.ErrorListener errorListener) {
    super(method, url, listener, errorListener);
  }

  @Override
  protected Response<String> parseNetworkResponse(NetworkResponse response) {

    RequestQueueController.get().checkSessionCookie(response.headers);

    return super.parseNetworkResponse(response);
  }

  /**
   * 给Post请求添加Cookie
   *
   * @return
   * @throws AuthFailureError
   */
  @Override
  public Map<String, String> getHeaders() throws AuthFailureError {

    Map<String, String> headers = super.getHeaders();

    if (headers == null
            || headers.equals(Collections.emptyMap())) {
      headers = new HashMap<>();
    }

    RequestQueueController.get().addSessionCookie(headers);

    return headers;
  }
}
