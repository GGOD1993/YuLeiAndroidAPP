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
   * 注册请求
   */
  private static OnSignupRequestResponseListener mSignupRequestListener;
  public static interface OnSignupRequestResponseListener {
    public void OnSignupSuccessResponse(JSONArray jsonArray);
    public void OnSignupErrorResponse(String errorResult);
  }
  public static void DoSignupRequest (int method,
                                      String url,
                                      HashMap<String, String> hashMap,
                                      OnSignupRequestResponseListener listener
                                      ) {
    mSignupRequestListener = listener;
    Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
      @Override
      public void onResponse(JSONArray jsonArray) {
        mSignupRequestListener.OnSignupSuccessResponse(jsonArray);
      }
    };

    Response.ErrorListener errorListener = new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {
        mSignupRequestListener.OnSignupErrorResponse(volleyError.getMessage());
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

  /**
   * 获取用户信息请求
   */
  private static OnGetUserInfoRequestResponseListener mGetUserInfoRequestListener;
  public static interface OnGetUserInfoRequestResponseListener {
    public void OnGetUserInfoSuccessResponse(JSONArray jsonArray);
    public void OnGetUserInfoErrorResponse(String errorResult);
  }
  public static void DoGetUserInfoRequest (int method,
                                           String url,
                                           HashMap<String, String> hashMap,
                                           OnGetUserInfoRequestResponseListener listener
                                      ) {
    mGetUserInfoRequestListener = listener;
    Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
      @Override
      public void onResponse(JSONArray jsonArray) {
        mGetUserInfoRequestListener.OnGetUserInfoSuccessResponse(jsonArray);
      }
    };

    Response.ErrorListener errorListener = new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {
        mGetUserInfoRequestListener.OnGetUserInfoErrorResponse(volleyError.getMessage());
      }
    };
    HttpApi.DoJsonArrayRequest(method, url, hashMap, responseListener, errorListener);
  }

  /**
   *孩子获取任务
   */
  private static OnGetDiyTaskRequestResponseListener mGetDiyTaskRequestListener;
  public static interface OnGetDiyTaskRequestResponseListener {
    public void OnGetDiyTaskSuccessResponse(JSONArray jsonArray);
    public void OnGetDiyTaskErrorResponse(String errorResult);
  }
  public static void DoGetDiyTaskRequest (int method,
                                          String url,
                                          HashMap<String, String> hashMap,
                                          OnGetDiyTaskRequestResponseListener listener
                                         ) {
    mGetDiyTaskRequestListener = listener;
    Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
      @Override
      public void onResponse(JSONArray jsonArray) {
        mGetDiyTaskRequestListener.OnGetDiyTaskSuccessResponse(jsonArray);
      }
    };

    Response.ErrorListener errorListener = new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {
        mGetDiyTaskRequestListener.OnGetDiyTaskErrorResponse(volleyError.getMessage());
      }
    };
    HttpApi.DoJsonArrayRequest(method, url, hashMap, responseListener, errorListener);
  }

  /**
   *家长审核完成任务
   */
  private static OnFinishDiyTaskRequestResponseListener mFinishDiyTaskRequestListener;
  public static interface OnFinishDiyTaskRequestResponseListener {
    public void OnFinishDiyTaskSuccessResponse(JSONArray jsonArray);
    public void OnFinishDiyTaskErrorResponse(String errorResult);
  }
  public static void DoFinishDiyTaskRequest (int method,
                                             String url,
                                             HashMap<String, String> hashMap,
                                             OnFinishDiyTaskRequestResponseListener listener
  ) {
    mFinishDiyTaskRequestListener = listener;
    Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
      @Override
      public void onResponse(JSONArray jsonArray) {
        mFinishDiyTaskRequestListener.OnFinishDiyTaskSuccessResponse(jsonArray);
      }
    };

    Response.ErrorListener errorListener = new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {
        mFinishDiyTaskRequestListener.OnFinishDiyTaskErrorResponse(volleyError.getMessage());
      }
    };
    HttpApi.DoJsonArrayRequest(method, url, hashMap, responseListener, errorListener);
  }

  /**
   *孩子提交任务进行审核
   */
  private static OnSubmitDiyTaskRequestResponseListener mSubmitDiyTaskRequestListener;
  public static interface OnSubmitDiyTaskRequestResponseListener {
    public void OnSubmitDiyTaskSuccessResponse(JSONArray jsonArray);
    public void OnSubmitDiyTaskErrorResponse(String errorResult);
  }
  public static void DoSubmitDiyTaskRequest (int method,
                                             String url,
                                             HashMap<String, String> hashMap,
                                             OnSubmitDiyTaskRequestResponseListener listener
  ) {
    mSubmitDiyTaskRequestListener = listener;
    Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
      @Override
      public void onResponse(JSONArray jsonArray) {
        mSubmitDiyTaskRequestListener.OnSubmitDiyTaskSuccessResponse(jsonArray);
      }
    };

    Response.ErrorListener errorListener = new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {
        mSubmitDiyTaskRequestListener.OnSubmitDiyTaskErrorResponse(volleyError.getMessage());
      }
    };
    HttpApi.DoJsonArrayRequest(method, url, hashMap, responseListener, errorListener);
  }

  /**
   *捐款
   */
  private static OnDonateRequestResponseListener mDonateRequestListener;
  public static interface OnDonateRequestResponseListener {
    public void OnDonateSuccessResponse(JSONArray jsonArray);
    public void OnDonateErrorResponse(String errorResult);
  }
  public static void DoDonateRequest (int method,
                                      String url,
                                      HashMap<String, String> hashMap,
                                      OnDonateRequestResponseListener listener
  ) {
    mDonateRequestListener = listener;
    Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
      @Override
      public void onResponse(JSONArray jsonArray) {
        mDonateRequestListener.OnDonateSuccessResponse(jsonArray);
      }
    };

    Response.ErrorListener errorListener = new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {
        mDonateRequestListener.OnDonateErrorResponse(volleyError.getMessage());
      }
    };
    HttpApi.DoJsonArrayRequest(method, url, hashMap, responseListener, errorListener);
  }

  /**
   *获取公司列表
   */
  private static OnGetCompanyRequestResponseListener mGetCompanyRequestListener;
  public static interface OnGetCompanyRequestResponseListener {
    public void OnGetCompanySuccessResponse(JSONArray jsonArray);
    public void OnGetCompanyErrorResponse(String errorResult);
  }
  public static void DoGetCompanyRequest (int method,
                                          String url,
                                          HashMap<String, String> hashMap,
                                          OnGetCompanyRequestResponseListener listener
  ) {
    mGetCompanyRequestListener = listener;
    Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
      @Override
      public void onResponse(JSONArray jsonArray) {
        mGetCompanyRequestListener.OnGetCompanySuccessResponse(jsonArray);
      }
    };

    Response.ErrorListener errorListener = new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {
        mGetCompanyRequestListener.OnGetCompanyErrorResponse(volleyError.getMessage());
      }
    };
    HttpApi.DoJsonArrayRequest(method, url, hashMap, responseListener, errorListener);
  }

  /**
   *获取孩子提交审查任务
   */
  private static OnCheckDiyTaskRequestResponseListener mCheckDiyTaskRequestListener;
  public static interface OnCheckDiyTaskRequestResponseListener {
    public void OnCheckDiyTaskSuccessResponse(JSONArray jsonArray);
    public void OnCheckDiyTaskErrorResponse(String errorResult);
  }
  public static void DoCheckDiyTaskRequest (int method,
                                            String url,
                                            HashMap<String, String> hashMap,
                                            OnCheckDiyTaskRequestResponseListener listener
  ) {
    mCheckDiyTaskRequestListener = listener;
    Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
      @Override
      public void onResponse(JSONArray jsonArray) {
        mCheckDiyTaskRequestListener.OnCheckDiyTaskSuccessResponse(jsonArray);
      }
    };

    Response.ErrorListener errorListener = new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {
        mCheckDiyTaskRequestListener.OnCheckDiyTaskErrorResponse(volleyError.getMessage());
      }
    };
    HttpApi.DoJsonArrayRequest(method, url, hashMap, responseListener, errorListener);
  }
  /**
   * 邀请添加孩子
   */
  private static OnUserInvitationRequestResponseListener mUserInvitationRequestListener;
  public static interface OnUserInvitationRequestResponseListener {
    public void OnUserInvitationSuccessResponse(JSONArray jsonArray);
    public void OnUserInvitationErrorResponse(String errorResult);
  }
  public static void DoUserInvitationRequest (int method,
                                              String url,
                                              HashMap<String, String> hashMap,
                                              OnUserInvitationRequestResponseListener listener
  ) {
    mUserInvitationRequestListener = listener;
    Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
      @Override
      public void onResponse(JSONArray jsonArray) {
        mUserInvitationRequestListener.OnUserInvitationSuccessResponse(jsonArray);
      }
    };

    Response.ErrorListener errorListener = new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {
        mUserInvitationRequestListener.OnUserInvitationErrorResponse(volleyError.getMessage());
      }
    };
    HttpApi.DoJsonArrayRequest(method, url, hashMap, responseListener, errorListener);
  }
  /**
   *拉取添加信息
   */
  private static OnGetInvitationRequestResponseListener mGetInvitationRequestListener;
  public static interface OnGetInvitationRequestResponseListener {
    public void OnGetInvitationSuccessResponse(JSONArray jsonArray);
    public void OnGetInvitationErrorResponse(String errorResult);
  }
  public static void DoGetInvitationRequest (int method,
                                             String url,
                                             HashMap<String, String> hashMap,
                                             OnGetInvitationRequestResponseListener listener
  ) {
    mGetInvitationRequestListener = listener;
    Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
      @Override
      public void onResponse(JSONArray jsonArray) {
        mGetInvitationRequestListener.OnGetInvitationSuccessResponse(jsonArray);
      }
    };

    Response.ErrorListener errorListener = new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {
        mGetInvitationRequestListener.OnGetInvitationErrorResponse(volleyError.getMessage());
      }
    };
    HttpApi.DoJsonArrayRequest(method, url, hashMap, responseListener, errorListener);
  }

	/**
	 * 孩子确认添加
	 */
  private static OnAddFriendRequestResponseListener mAddFriendRequestListener;
  public static interface OnAddFriendRequestResponseListener {
    public void OnAddFriendSuccessResponse(JSONArray jsonArray);
    public void OnAddFriendErrorResponse(String errorResult);
  }
  public static void DoAddFriendRequest (int method,
                                         String url,
                                         HashMap<String, String> hashMap,
                                         OnAddFriendRequestResponseListener listener
  ) {
    mAddFriendRequestListener = listener;
    Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
      @Override
      public void onResponse(JSONArray jsonArray) {
        mAddFriendRequestListener.OnAddFriendSuccessResponse(jsonArray);
      }
    };

    Response.ErrorListener errorListener = new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {
        mAddFriendRequestListener.OnAddFriendErrorResponse(volleyError.getMessage());
      }
    };
    HttpApi.DoJsonArrayRequest(method, url, hashMap, responseListener, errorListener);
  }
	/**
	 * 拉取孩子列表
	 */
  private static OnGetChildrenRequestResponseListener mGetChildrenRequestListener;
  public static interface OnGetChildrenRequestResponseListener {
    public void OnGetChildrenSuccessResponse(JSONArray jsonArray);
    public void OnGetChildrenErrorResponse(String errorResult);
  }
  public static void DoGetChildrenRequest (int method,
                                         String url,
                                         HashMap<String, String> hashMap,
                                         OnGetChildrenRequestResponseListener listener
  ) {
    mGetChildrenRequestListener = listener;
    Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
      @Override
      public void onResponse(JSONArray jsonArray) {
        mGetChildrenRequestListener.OnGetChildrenSuccessResponse(jsonArray);
      }
    };

    Response.ErrorListener errorListener = new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {
        mGetChildrenRequestListener.OnGetChildrenErrorResponse(volleyError.getMessage());
      }
    };
    HttpApi.DoJsonArrayRequest(method, url, hashMap, responseListener, errorListener);
  }
	/**
	 * 拉取家长列表
	 */
  private static OnGetParentRequestResponseListener mGetParentRequestListener;
  public static interface OnGetParentRequestResponseListener {
    public void OnGetParentSuccessResponse(JSONArray jsonArray);
    public void OnGetParentErrorResponse(String errorResult);
  }
  public static void DoGetParentRequest (int method,
                                         String url,
                                         HashMap<String, String> hashMap,
                                         OnGetParentRequestResponseListener listener
  ) {
    mGetParentRequestListener = listener;
    Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
      @Override
      public void onResponse(JSONArray jsonArray) {
        mGetParentRequestListener.OnGetParentSuccessResponse(jsonArray);
      }
    };

    Response.ErrorListener errorListener = new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {
        mGetParentRequestListener.OnGetParentErrorResponse(volleyError.getMessage());
      }
    };
    HttpApi.DoJsonArrayRequest(method, url, hashMap, responseListener, errorListener);
  }
}
