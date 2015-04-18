package com.example.pc.myapplication.utils;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * 最底层的API
 * 只根据传来的参数创建了Request
 * 并将其加入了队列中
 */
public class HttpApi {

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
