package com.example.pc.myapplication.utils;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class HttpApi {

  /**
   * 字符串请求的网络Api
   * @param hashMap
   * @param listener
   * @param errorListener
   * @param url
   * @param method
   */
  public static void DoStringRequest(int method,
                                     String url,
                                     HashMap<String,String> hashMap,
                                     Response.Listener<String> listener,
                                     Response.ErrorListener errorListener
  ) {

    final HashMap<String,String> map = hashMap;
    try{
      StringPostRequestPlus stringRequest = new StringPostRequestPlus(
              method,
              url,
              listener,
              errorListener){

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

  /**
   * 返回JsonArray的网络请求Api
   * @param method
   * @param url
   * @param requestBody
   * @param listener
   * @param errorListener
   */

  public static void DoJsonArrayRequest(int method,
                                        String url,
                                        HashMap hashMap,
                                        Response.Listener<JSONArray> listener,
                                        Response.ErrorListener errorListener) {
    final HashMap<String,String> map = hashMap;

    try{
      JsonArrayRequestPlus jsonArrayRequestPlus = new JsonArrayRequestPlus(
              method,
              url,
              listener,
              errorListener){
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
          return map;
        }
      };
      RequestQueueController.get().getRequestQueue().add(jsonArrayRequestPlus);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
