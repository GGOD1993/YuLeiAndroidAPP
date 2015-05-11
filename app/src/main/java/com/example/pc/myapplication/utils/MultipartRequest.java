package com.example.pc.myapplication.utils;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MultipartRequest extends Request<JSONArray> {

  MultipartEntity mMultipartEntity = new MultipartEntity();

  Map<String, String> headers = new HashMap<>();

  private Response.Listener<JSONArray> mListener;

  public MultipartRequest(int method,
                          String url,
                          Response.Listener<JSONArray> mListener,
                          Response.ErrorListener errorListener) {
    super(method, url, errorListener);
    this.mListener = mListener;
  }

  public void addHeader(String key, String value) {
    headers.put(key, value);
  }

  @Override
  public Map<String, String> getHeaders() throws AuthFailureError {
    RequestQueueController.get().addSessionCookie(headers);
    return headers;
  }

  @Override
  public String getBodyContentType() {
    return mMultipartEntity.getContentType().getValue();
  }

  @Override
  public byte[] getBody() {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try {
      // multipart body
      mMultipartEntity.writeTo(bos);
    } catch (IOException e) {
      Log.e("", "IOException writing to ByteArrayOutputStream");
    }
    return bos.toByteArray();
  }

  @Override
  protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
    RequestQueueController.get().checkSessionCookie(response.headers);
    try {
      String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
      return Response.success(new JSONArray(jsonString), HttpHeaderParser.parseCacheHeaders(response));
    } catch (UnsupportedEncodingException e) {
      return Response.error(new ParseError(e));
    } catch (JSONException je) {
      return Response.error(new ParseError(je));
    }
  }

  @Override
  protected void deliverResponse(JSONArray jsonArray) {
    mListener.onResponse(jsonArray);
  }

  public MultipartEntity getMultiPartEntity() {
    return mMultipartEntity;
  }
}
