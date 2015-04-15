package com.example.pc.myapplication.fragment.parent;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.TaskInfo.DiyTaskInfo;
import com.example.pc.myapplication.activity.FinishTaskActivity;
import com.example.pc.myapplication.activity.SubmitTaskActivity;
import com.example.pc.myapplication.activity.TaskInfoActivity;
import com.example.pc.myapplication.activity.parent.ParentMainActivity;
import com.example.pc.myapplication.adapter.ParentRecyclerViewAdapter;
import com.example.pc.myapplication.adapter.RecyclerViewItemClickListener;
import com.example.pc.myapplication.utils.HttpService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParentBabyFragment extends Fragment
        implements HttpService.OnGetDiyTaskRequestResponseListener, RecyclerViewItemClickListener{

  //fragment所在的activity
  private ParentMainActivity activity;

  //任务列表
  private RecyclerView mRecyclerView;

  //存储任务的arraylist
  private ArrayList<DiyTaskInfo> taskList;

  //下拉刷新的控件
  private SwipeRefreshLayout mPullRefresh;

  //SharedPreference
  private SharedPreferences preferences;

  //recyclerview适配器
  public ParentRecyclerViewAdapter recyclerViewAdapter;

  //和activity通信的回调接口
  private OnBabyFragmentInteractionListener mListener;

  public static ParentBabyFragment newInstance() {
    ParentBabyFragment fragment = new ParentBabyFragment();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  public ParentBabyFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_parent_baby, container, false);
    taskList = new ArrayList<>();
    activity = (ParentMainActivity) getActivity();
    preferences = activity.getSharedPreferences(AppConstant.PREFERENCE_NAME, 0);
    initViews(v);

    return v;
  }

  /**
   * 初始化fragment中的控件
   * @param v
   */
  private void initViews(View v) {

    mRecyclerView = (RecyclerView) v.findViewById(R.id.parent_babyfragment_recyclerview);
    mPullRefresh = (SwipeRefreshLayout) v.findViewById(R.id.parent_babyfragment_swiperefreshlayout);
    LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
    recyclerViewAdapter = new ParentRecyclerViewAdapter(taskList, activity, AppConstant.RECIVE_TASK_TYPE);
    recyclerViewAdapter.setOnItemClickListener(this);
    mRecyclerView.setLayoutManager(layoutManager);
    mRecyclerView.setAdapter(recyclerViewAdapter);

    mPullRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        String url = AppConstant.GET_DIY_TASK_URL + "?" + AppConstant.USERNAME + "=" +
                preferences.getString(AppConstant.FROM_USERID, "");
        HttpService.DoGetDiyTaskRequest(Request.Method.GET, url, null, ParentBabyFragment.this);
      }
    });
  }

  /**
   * recyclerview的点击监听事件处理
   * @param view
   * @param position
   */
  @Override
  public void onItemClick(View view, int position) {
    DiyTaskInfo clickTask = taskList.get(position);
    Intent intent = new Intent(getActivity(), SubmitTaskActivity.class);
    intent.putExtra(AppConstant.CLICKED_SEND_TASK, clickTask);
    startActivity(intent);
  }

  /**
   * 处理网络请求
   * @param jsonArray
   */
  @Override
  public void OnGetDiyTaskSuccessResponse(JSONArray jsonArray) {
    mPullRefresh.setRefreshing(false);
    JSONObject arrayTask;
    DiyTaskInfo taskInfo;
    ArrayList<DiyTaskInfo> submittedTask = new ArrayList<>();
    ArrayList<DiyTaskInfo> finishedTask = new ArrayList<>();
    taskList.clear();
    for (int i = 0 ; i < jsonArray.length() ; i ++) {
      try{
        arrayTask = (JSONObject) jsonArray.get(i);
        taskInfo = new DiyTaskInfo(
                arrayTask.getString(AppConstant.TO_USERID),
                arrayTask.getString(AppConstant.TASK_ID),
                arrayTask.getString(AppConstant.TASK_REGDATE),
                arrayTask.getString(AppConstant.TASK_CONTENT),
                arrayTask.getString(AppConstant.FROM_USERID)
        );
        switch (arrayTask.getInt(AppConstant.TASK_STATUS)) {
          case AppConstant.STATUS_NEW:
            taskList.add(taskInfo);
            break;

          case AppConstant.STATUS_SUBMITTED:
            submittedTask.add(taskInfo);
            break;

          case AppConstant.STATUS_FINISHED:
            finishedTask.add(taskInfo);
            break;
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    taskList.addAll(submittedTask);
    taskList.addAll(finishedTask);
    recyclerViewAdapter.notifyDataSetChanged();
    submittedTask = null;
    finishedTask = null;
  }

  @Override
  public void OnGetDiyTaskErrorResponse(String errorResult) {
    mPullRefresh.setRefreshing(true);
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      mListener = (OnBabyFragmentInteractionListener) activity;
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
  public interface OnBabyFragmentInteractionListener {
    // TODO: Update argument type and name
    public void onBabyFragmentInteraction();
  }

}
