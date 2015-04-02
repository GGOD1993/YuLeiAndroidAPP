package com.example.pc.myapplication.utils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class JsonArrayRequestPlus extends Request<JSONArray>{

    private final Response.Listener<JSONArray> mListener;

    public JsonArrayRequestPlus(int method,
                                String url,
                                Response.Listener<JSONArray> listener,
                                Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
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

    @Override
    protected void deliverResponse(JSONArray jsonArray) {
        mListener.onResponse(jsonArray);
    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {

        RequestQueueController.get().checkSessionCookie(response.headers);

        try {
            String jsonString =
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONArray(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }
}
