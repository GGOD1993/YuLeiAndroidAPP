package com.example.pc.myapplication.utils;

import android.graphics.Bitmap;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.pc.myapplication.AppConstant;

import org.json.JSONArray;

import java.util.HashMap;


/**
 * 第二层相当与传输层
 * 也负责了对数据进行处理
 */
public class HttpService {
  /**
   * 登录请求
   */
  private static OnLoginRequestResponseListener mLoginRequestListener;

  public interface OnLoginRequestResponseListener {
    void OnLoginSuccessResponse(JSONArray jsonArray);

    void OnLoginErrorResponse(String errorResult);
  }

  public static void DoLoginRequest(HashMap<String, String> hashMap, OnLoginRequestResponseListener listener) {
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
    HttpApi.DoJsonArrayRequest(Request.Method.POST, AppConstant.LOGIN_IN_URL, hashMap, responseListener, errorListener);
  }

  /**
   * 注册请求
   */
  private static OnSignupRequestResponseListener mSignupRequestListener;

  public interface OnSignupRequestResponseListener {
    void OnSignupSuccessResponse(JSONArray jsonArray);

    void OnSignupErrorResponse(String errorResult);
  }

  public static void DoSignupRequest(HashMap<String, String> hashMap, OnSignupRequestResponseListener listener) {
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
    HttpApi.DoJsonArrayRequest(Request.Method.POST, AppConstant.NEW_USER_URL, hashMap, responseListener, errorListener);
  }

  /**
   * 获得当前用户
   */
  private static OnGetCurrentUserRequestResponseListener mGetCurrentUserListener;

  public interface OnGetCurrentUserRequestResponseListener {
    void OnGetCurrentUserSuccessResponse(JSONArray jsonArray);

    void OnGetCurrentUserErrorResponse(String errorResult);
  }

  public static void DoGetCurrentUserRequest(HashMap<String, String> hashMap, OnGetCurrentUserRequestResponseListener listener) {
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
      HttpApi.DoJsonArrayRequest(Request.Method.GET, AppConstant.GET_CURRENT_USER_URL, null, responseListener, errorListener);
    } else {
      HttpApi.DoJsonArrayRequest(Request.Method.GET, AppConstant.GET_CURRENT_USER_URL, hashMap, responseListener, errorListener);
    }
  }

  /**
   * 登出请求
   */
  private static OnLogoutRequestResponseListener onLogoutRequestResponseListener;

  public interface OnLogoutRequestResponseListener {
    void OnLogoutSuccessResponse(JSONArray jsonArray);

    void OnLogoutErrorResponse(String errorMsg);
  }

  public static void DoLogoutRequest(HashMap<String, String> hashMap, OnLogoutRequestResponseListener listener) {
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

    HttpApi.DoJsonArrayRequest(Request.Method.GET, AppConstant.LOGIN_OUT_URL, hashMap, responseListener, errorListener);

  }

  /**
   * 添加自定义任务请求
   */
  private static OnSetDiyTaskRequestResponseListener onSetDiyTaskRequestResponseListener;

  public interface OnSetDiyTaskRequestResponseListener {
    void OnSetDiyTaskSuccessResponse(JSONArray jsonArray);

    void OnSetDiyTaskErrorResponse(String errorMsg);
  }

  public static void DoSetDiyTaskRequest(HashMap<String, String> hashMap, OnSetDiyTaskRequestResponseListener listener) {
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

    HttpApi.DoJsonArrayRequest(Request.Method.POST, AppConstant.SET_DIY_TASK_URL, hashMap, responseListener, errorListener);
  }

  /**
   * 获取系统任务请求
   */
  private static OnGetSysTaskRequestResponseListener onGetSysTaskRequestResponseListener;

  public interface OnGetSysTaskRequestResponseListener {
    void OnGetSysTaskSuccessResponse(JSONArray jsonArray);

    void OnGetSysTaskErrorResponse(String errorMsg);
  }

  public static void DoGetSysTaskRequest(HashMap<String, String> hashMap, OnGetSysTaskRequestResponseListener listener) {
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

    HttpApi.DoJsonArrayRequest(Request.Method.GET, AppConstant.GET_SYS_TASK_URL, hashMap, responseListener, errorListener);
  }

  /**
   * 获取用户信息请求
   */
  private static OnGetUserInfoRequestResponseListener mGetUserInfoRequestListener;

  public interface OnGetUserInfoRequestResponseListener {
    void OnGetUserInfoSuccessResponse(JSONArray jsonArray);

    void OnGetUserInfoErrorResponse(String errorResult);
  }

  public static void DoGetUserInfoRequest(HashMap<String, String> hashMap, OnGetUserInfoRequestResponseListener listener) {
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
    HttpApi.DoJsonArrayRequest(Request.Method.GET, AppConstant.GET_USER_INFO_URL, hashMap, responseListener, errorListener);
  }

  /**
   * 获取自己要完成任务
   */
  private static OnGetDiyTaskRequestResponseListener mGetDiyTaskRequestListener;

  public interface OnGetDiyTaskRequestResponseListener {
    void OnGetDiyTaskSuccessResponse(JSONArray jsonArray);

    void OnGetDiyTaskErrorResponse(String errorResult);
  }

  public static void DoGetDiyTaskRequest(String url, HashMap<String, String> hashMap, OnGetDiyTaskRequestResponseListener listener) {
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
    HttpApi.DoJsonArrayRequest(Request.Method.GET, url, hashMap, responseListener, errorListener);
  }

  /**
   * 获取自己发出的完成任务
   */
  private static OnGetSendDiyTaskRequestResponseListener mGetSendDiyTaskRequestListener;

  public interface OnGetSendDiyTaskRequestResponseListener {
    void OnGetSendDiyTaskSuccessResponse(JSONArray jsonArray);

    void OnGetSendDiyTaskErrorResponse(String errorResult);
  }

  public static void DoGetSendDiyTaskRequest(String url, HashMap<String, String> hashMap, OnGetSendDiyTaskRequestResponseListener listener) {
    mGetSendDiyTaskRequestListener = listener;
    Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
      @Override
      public void onResponse(JSONArray jsonArray) {
        mGetSendDiyTaskRequestListener.OnGetSendDiyTaskSuccessResponse(jsonArray);
      }
    };

    Response.ErrorListener errorListener = new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {
        mGetSendDiyTaskRequestListener.OnGetSendDiyTaskErrorResponse(volleyError.getMessage());
      }
    };
    HttpApi.DoJsonArrayRequest(Request.Method.GET, url, hashMap, responseListener, errorListener);
  }

  /**
   * 根据任务状态获取任务信息
   */
  private static OnGetTaskByStatusRequestResponseListener mGetTaskByStatusRequestListener;

  public interface OnGetTaskByStatusRequestResponseListener {
    void OnGetHistoryTaskSuccessResponse(JSONArray jsonArray);

    void OnGetHistoryTaskErrorResponse(String errorResult);
  }
  public static void DoGetTaskByStatusRequest(HashMap<String, String> hashMap, OnGetTaskByStatusRequestResponseListener listener) {
    mGetTaskByStatusRequestListener = listener;
    Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
      @Override
      public void onResponse(JSONArray jsonArray) {
        mGetTaskByStatusRequestListener.OnGetHistoryTaskSuccessResponse(jsonArray);
      }
    };
    Response.ErrorListener errorListener = new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {
        mGetTaskByStatusRequestListener.OnGetHistoryTaskErrorResponse(volleyError.getMessage());
      }
    };
    HttpApi.DoJsonArrayRequest(Request.Method.POST, AppConstant.GET_TASK_BY_STATUS, hashMap, responseListener, errorListener);
  }

  
  /**
   * 家长审核完成任务
   */
  private static OnFinishDiyTaskRequestResponseListener mFinishDiyTaskRequestListener;

  public interface OnFinishDiyTaskRequestResponseListener {
    void OnFinishDiyTaskSuccessResponse(JSONArray jsonArray);

    void OnFinishDiyTaskErrorResponse(String errorResult);
  }

  public static void DoFinishDiyTaskRequest(HashMap<String, String> hashMap, OnFinishDiyTaskRequestResponseListener listener) {
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
    HttpApi.DoJsonArrayRequest(Request.Method.POST, AppConstant.FINISH_DIY_TASK_URL, hashMap, responseListener, errorListener);
  }

  /**
   * 孩子提交任务进行审核
   */
  private static OnSubmitDiyTaskRequestResponseListener mSubmitDiyTaskRequestListener;

  public interface OnSubmitDiyTaskRequestResponseListener {
    void OnSubmitDiyTaskSuccessResponse(JSONArray jsonArray);

    void OnSubmitDiyTaskErrorResponse(String errorResult);
  }

  public static void DoSubmitDiyTaskRequest(HashMap<String, String> hashMap, OnSubmitDiyTaskRequestResponseListener listener) {
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
    HttpApi.DoJsonArrayRequest(Request.Method.POST, AppConstant.SUBMIT_DIY_TASK_URL, hashMap, responseListener, errorListener);
  }

  /**
   * 获取慈善项目
   */
  private static OnGetProjectRequestResponseListener mGetProjectRequestResponseListener;

  public interface OnGetProjectRequestResponseListener {
    void OnGetProjectSuccessResponse(JSONArray jsonArray);

    void OnGetProjectErrorResponse(String errorResult);
  }

  public static void DoGetProjectRequest(HashMap<String, String> hashMap, OnGetProjectRequestResponseListener listener) {
    mGetProjectRequestResponseListener = listener;
    Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
      @Override
      public void onResponse(JSONArray jsonArray) {
        mGetProjectRequestResponseListener.OnGetProjectSuccessResponse(jsonArray);
      }
    };

    Response.ErrorListener errorListener = new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {
        mGetProjectRequestResponseListener.OnGetProjectErrorResponse(volleyError.getMessage());
      }
    };
    HttpApi.DoJsonArrayRequest(Request.Method.GET, AppConstant.GET_PROJECT_URL, hashMap, responseListener, errorListener);
  }

  /**
   * 获取孩子提交审查任务
   */
  private static OnCheckDiyTaskRequestResponseListener mCheckDiyTaskRequestListener;

  public interface OnCheckDiyTaskRequestResponseListener {
    void OnCheckDiyTaskSuccessResponse(JSONArray jsonArray);

    void OnCheckDiyTaskErrorResponse(String errorResult);
  }

  public static void DoCheckDiyTaskRequest(int method,
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

  public interface OnUserInvitationRequestResponseListener {
    void OnUserInvitationSuccessResponse(JSONArray jsonArray);

    void OnUserInvitationErrorResponse(String errorResult);
  }

  public static void DoUserInvitationRequest(HashMap<String, String> hashMap, OnUserInvitationRequestResponseListener listener) {
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
    HttpApi.DoJsonArrayRequest(Request.Method.POST, AppConstant.USER_INVITATION_URL, hashMap, responseListener, errorListener);
  }

  /**
   * 拉取添加信息
   */
  private static OnGetInvitationRequestResponseListener mGetInvitationRequestListener;

  public interface OnGetInvitationRequestResponseListener {
    void OnGetInvitationSuccessResponse(JSONArray jsonArray);

    void OnGetInvitationErrorResponse(String errorResult);
  }

  public static void DoGetInvitationRequest(String url, HashMap<String, String> hashMap, OnGetInvitationRequestResponseListener listener) {
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
    HttpApi.DoJsonArrayRequest(Request.Method.GET, url, hashMap, responseListener, errorListener);
  }

  /**
   * 孩子确认添加
   */
  private static OnAddFriendRequestResponseListener mAddFriendRequestListener;

  public interface OnAddFriendRequestResponseListener {
    void OnAddFriendSuccessResponse(JSONArray jsonArray);

    void OnAddFriendErrorResponse(String errorResult);
  }

  public static void DoAddFriendRequest(HashMap<String, String> hashMap, OnAddFriendRequestResponseListener listener) {
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
    HttpApi.DoJsonArrayRequest(Request.Method.POST, AppConstant.ADD_FRIEND_URL, hashMap, responseListener, errorListener);
  }

  /**
   * 拉取孩子列表
   */
  private static OnGetChildrenRequestResponseListener mGetChildrenRequestListener;

  public interface OnGetChildrenRequestResponseListener {
    void OnGetChildrenSuccessResponse(JSONArray jsonArray);

    void OnGetChildrenErrorResponse(String errorResult);
  }

  public static void DoGetChildrenRequest(String url, HashMap<String, String> hashMap, OnGetChildrenRequestResponseListener listener) {
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
    HttpApi.DoJsonArrayRequest(Request.Method.GET, url, hashMap, responseListener, errorListener);
  }

  /**
   * 拉取家长列表
   */
  private static OnGetParentRequestResponseListener mGetParentRequestListener;

  public interface OnGetParentRequestResponseListener {
    void OnGetParentSuccessResponse(JSONArray jsonArray);

    void OnGetParentErrorResponse(String errorResult);
  }

  public static void DoGetParentRequest(String url, HashMap<String, String> hashMap, OnGetParentRequestResponseListener listener) {
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
    HttpApi.DoJsonArrayRequest(Request.Method.GET, url, hashMap, responseListener, errorListener);
  }

  /**
   * 上传头像
   */
  private static OnUpLoadImageRequestResponseListener mUpLoadRequestListener;

  public interface OnUpLoadImageRequestResponseListener {
    void OnUpLoadImageSuccessResponse(JSONArray jsonArray);

    void OnUpLoadImageErrorResponse(String errorResult);
  }

  public static void DoUpLoadImageRequest(Bitmap bitmap, String userId, OnUpLoadImageRequestResponseListener listener) {
    mUpLoadRequestListener = listener;
    Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
      @Override
      public void onResponse(JSONArray jsonArray) {
        mUpLoadRequestListener.OnUpLoadImageSuccessResponse(jsonArray);
      }
    };
    Response.ErrorListener errorListener = new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {
        mUpLoadRequestListener.OnUpLoadImageErrorResponse(volleyError.getMessage());
      }
    };
    HttpApi.DoMultipartRequest(Request.Method.POST, AppConstant.UPLOAD_USER_IMAGE, bitmap, userId, responseListener, errorListener);
  }
}
