package com.example.pc.myapplication.fragment.child;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class ChildWishFragment extends Fragment
        implements HttpService.OnGetSendDiyTaskRequestResponseListener{

  //下拉刷新控件
  private RefreshableLinearLayout mPullToRefresh;

  //自定义组件
  private ActiveViewGroup activeViewGroup;

  //用于子控件漂浮效果的辅助工具
  private ActiveHelper activeHelper;

  //activity回调接口
  private onChildWishFragmentInteractionListener mListener;

  public static ChildWishFragment newInstance() {
    ChildWishFragment fragment = new ChildWishFragment();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  public ChildWishFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {

    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_child_wish, container, false);
    mPullToRefresh = (RefreshableLinearLayout) view.findViewById(R.id.child_wishfragment_frefreshable_linearlayout);
    activeViewGroup = (ActiveViewGroup) view.findViewById(R.id.child_wishfragment_activeviewgroup);
    activeHelper = new ActiveHelper(activeViewGroup);
    mPullToRefresh.setOnRefreshListener(new RefreshableLinearLayout.PullToRefreshListener() {
      @Override
      public void onRefresh() {
        startGetSendDiyTaskRequest();
      }
    });
    return view;
  }

  private void startGetSendDiyTaskRequest() {
    String url = AppConstant.GET_SEND_DIY_TASK_URL + "?" + AppConstant.USERNAME + "=" +
            getActivity().getSharedPreferences(AppConstant.PREFERENCE_NAME,0)
                    .getString(AppConstant.FROM_USERID, "");

    HttpService.DoGetSendDiyTaskRequest(Request.Method.GET, url, null, ChildWishFragment.this);
  }

  @Override
  public void OnGetSendDiyTaskSuccessResponse(JSONArray jsonArray) {
    mPullToRefresh.finishRefreshing();
    addActiveView(jsonArray);
    activeViewGroup.setMode(AppConstant.ONLAYOUT_MODE_RANDOM);
  }

  @Override
  public void OnGetSendDiyTaskErrorResponse(String errorResult) {
    mPullToRefresh.finishRefreshing();
  }

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
            activeWishView .setBackgroundResource(R.mipmap.ic_launcher);
            break;
          case AppConstant.STATUS_SUBMITTED:
            activeWishView.setBackgroundResource(R.mipmap.sun_loading_blue);
            break;
          case AppConstant.STATUS_FINISHED:
            activeWishView .setBackgroundResource(R.mipmap.ic_tab_image);
            break;
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
      activeViewGroup.addActiveView(activeWishView);
    }
    activeHelper.startWishModeMove();
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      mListener = (onChildWishFragmentInteractionListener) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException(activity.toString()
              + " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  public interface onChildWishFragmentInteractionListener {
    public void onChildWishFragmentInteraction();
  }

}
