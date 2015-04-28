package com.example.pc.myapplication.fragment.child;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.TaskInfo.DiyTaskInfo;
import com.example.pc.myapplication.ViewStyle.ActiveView;
import com.example.pc.myapplication.ViewStyle.ActiveViewGroup;
import com.example.pc.myapplication.ViewStyle.ActiveWishView;
import com.example.pc.myapplication.ViewStyle.RefreshableLinearLayout;
import com.example.pc.myapplication.utils.ActiveHelper;
import com.example.pc.myapplication.utils.HttpService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChildMsgFragment extends Fragment implements
        HttpService.OnGetDiyTaskRequestResponseListener{

  //SharedPreference
  private SharedPreferences preferences;

  //自定义的viewgroup
  private ActiveViewGroup activeViewGroup;

  //用于子控件漂浮效果的辅助工具
  private ActiveHelper activeHelper;

  //自定义可下拉刷新的linearlayout
  private RefreshableLinearLayout refreshableLinearLayout;

  //activity中实现的回调接口
  private OnChildMsgFragmentInteractionListener mListener;

  //广播接收器
  private RemoveViewReciver reciver;

  public static ChildMsgFragment newInstance() {
    ChildMsgFragment fragment = new ChildMsgFragment();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  public ChildMsgFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    reciver = new RemoveViewReciver();
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(AppConstant.BROADCAST_REMOVEVIEW);
    getActivity().registerReceiver(reciver, intentFilter);

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    View v = inflater.inflate(R.layout.fragment_child_msg, container, false);
    activeViewGroup = (ActiveViewGroup) v.findViewById(R.id.activeViewGroup);
    activeHelper = new ActiveHelper(activeViewGroup);
    preferences = getActivity().getSharedPreferences(AppConstant.PREFERENCE_NAME,0);
    refreshableLinearLayout = (RefreshableLinearLayout) v.findViewById(R.id.refreshable_linearlayout);
    refreshableLinearLayout.setOnRefreshListener(new RefreshableLinearLayout.PullToRefreshListener() {
      @Override
      public void onRefresh() {
        startGetDiyTaskRequest();
      }
    });
    return v;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      mListener = (OnChildMsgFragmentInteractionListener) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException(activity.toString()
              + " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
    getActivity().unregisterReceiver(reciver);
  }

  public void stopMoveActiveView() {
    activeHelper.stopMove();
  }

  /**
   * 开始请求
   */
  private void startGetDiyTaskRequest() {
    String url = AppConstant.GET_DIY_TASK_URL + "?" + AppConstant.USERNAME + "=" +
            getActivity().getSharedPreferences(AppConstant.PREFERENCE_NAME,0)
                    .getString(AppConstant.FROM_USERID, "");
    HttpService.DoGetDiyTaskRequest(Request.Method.GET, url, null, ChildMsgFragment.this);
  }

  /**
   * 处理请求结果
   * @param jsonArray
   */
  @Override
  public void OnGetDiyTaskSuccessResponse(JSONArray jsonArray) {
    refreshableLinearLayout.finishRefreshing();
    addActiveView(jsonArray);
    activeViewGroup.setMode(AppConstant.ONLAYOUT_MODE_RANDOM);
  }

  @Override
  public void OnGetDiyTaskErrorResponse(String errorResult) {
    refreshableLinearLayout.finishRefreshing();
  }

  /**
   * 获取任务后添加activeview
   * @param jsonArray
   */
  private void addActiveView(JSONArray jsonArray) {
    activeViewGroup.removeAllViews();
    ActiveWishView activeWishView;
    JSONObject object;
    int count = jsonArray.length();
    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    for (int i = 0; i < count; i++) {
      activeWishView = new ActiveWishView(getActivity());
      activeWishView.setLayoutParams(layoutParams);
      try{
        object = jsonArray.getJSONObject(i);
        activeWishView.setTaskInfo(new DiyTaskInfo(
                object.getString(AppConstant.TO_USERID),
                object.getString(AppConstant.TASK_ID),
                object.getString(AppConstant.TASK_REGDATE),
                object.getString(AppConstant.TASK_CONTENT),
                object.getString(AppConstant.FROM_USERID),
                0));
        switch (object.getInt(AppConstant.TASK_STATUS)) {
          case AppConstant.STATUS_NEW:
            activeWishView.setBackgroundResource(R.mipmap.ic_launcher);
            break;
          case AppConstant.STATUS_SUBMITTED:
            activeWishView .setBackgroundResource(R.mipmap.sun_loading_blue);
            break;
          case AppConstant.STATUS_FINISHED:
            activeWishView.setBackgroundResource(R.mipmap.ic_tab_image);
            break;
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
      activeViewGroup.addActiveView(activeWishView);
    }
    activeHelper.startWishModeMove();
  }

  /**
   * 广播接收器类
   */
  class RemoveViewReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      String taskId = intent.getStringExtra(AppConstant.TASK_ID);
      activeViewGroup.changeActiveViewBgByTask(taskId);
    }
  }
  public interface OnChildMsgFragmentInteractionListener {
    public void onChildMsgFragmentInteraction();
  }
}
