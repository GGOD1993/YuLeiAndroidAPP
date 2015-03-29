package com.example.pc.myapplication.utils;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.example.pc.myapplication.AppConstant;
import java.util.HashMap;
import java.util.Map;

public class HttpApi {

    public static void Request(HashMap<String,String> hashMap,
                                      Response.Listener<String> listener,
                                      Response.ErrorListener errorListener,
                                      String url,
                                      int method) {

        final HashMap<String,String> map = hashMap;
        try{
            StringPostRequestPlus stringRequest = new StringPostRequestPlus(
                    method,
                    url,
                    listener, errorListener){

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return map;
                }
            };
            RequestQueueController.get().getRequestQueue().add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
