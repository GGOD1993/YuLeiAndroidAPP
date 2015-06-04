package com.example.pc.myapplication.fragment.child;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.Infos.DiyTaskInfo;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.ViewStyle.SpaceItemDecoration;
import com.example.pc.myapplication.adapter.TaskRecyclerViewAdapter;
import com.example.pc.myapplication.utils.HttpService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

public class ChildHistoryFragment extends Fragment implements
        HttpService.OnGetTaskByStatusRequestResponseListener {

  //Preferences
  private SharedPreferences preferences;

  //列表
  private RecyclerView recyclerView;

  //历史任务列表
  private ArrayList<DiyTaskInfo> historyTaskList;

  //下拉刷新
  private SwipeRefreshLayout refreshLayout;

  //RecyclerView的Adapter
  private TaskRecyclerViewAdapter adapter;

  //activity中实现的回调接口
  private OnChildHistoryFragmentInteractionListener mListener;

  public static ChildHistoryFragment newInstance() {
    ChildHistoryFragment fragment = new ChildHistoryFragment();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  public ChildHistoryFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    View v = inflater.inflate(R.layout.fragment_child_history, container, false);
    historyTaskList = new ArrayList<>();
    preferences = getActivity().getSharedPreferences(AppConstant.PREFERENCE_NAME, 0);
    initViews(v);
    return v;
  }

  private void initViews(View v) {
    recyclerView = ((RecyclerView) v.findViewById(R.id.child_historyfragment_recyclerview));
    refreshLayout = ((SwipeRefreshLayout) v.findViewById(R.id.child_historyfragment_swiperefreshlayout));

    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
    recyclerView.setLayoutManager(layoutManager);
    adapter = new TaskRecyclerViewAdapter(historyTaskList, getActivity(), AppConstant.RECIVE_TASK_TYPE);
    recyclerView.addItemDecoration(new SpaceItemDecoration(30));
    ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(adapter);
    scaleAdapter.setFirstOnly(false);
    AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(scaleAdapter);
    alphaAdapter.setFirstOnly(false);
    recyclerView.setAdapter(alphaAdapter);
    refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        HashMap<String, String> map = new HashMap<>();
        map.put(AppConstant.TASK_STATUS, String.valueOf(AppConstant.STATUS_FINISHED));
        map.put(AppConstant.USERNAME, preferences.getString(AppConstant.FROM_USERID, ""));
        HttpService.DoGetTaskByStatusRequest(map, ChildHistoryFragment.this);
      }
    });
  }

  @Override
  public void OnGetHistoryTaskSuccessResponse(JSONArray jsonArray) {
    refreshLayout.setRefreshing(false);
    JSONObject arrayTask;
    DiyTaskInfo taskInfo;
    historyTaskList.clear();
    for (int i = 0; i < jsonArray.length(); i++) {
      try {
        arrayTask = (JSONObject) jsonArray.get(i);
        taskInfo = new DiyTaskInfo(
                arrayTask.getString(AppConstant.TO_USERID),
                arrayTask.getString(AppConstant.TASK_ID),
                arrayTask.getString(AppConstant.TASK_AWARD),
                arrayTask.getString(AppConstant.TASK_CONTENT),
                arrayTask.getString(AppConstant.FROM_USERID),
                AppConstant.STATUS_FINISHED
        );
        historyTaskList.add(taskInfo);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    adapter.notifyDataSetChanged();
  }

  @Override
  public void OnGetHistoryTaskErrorResponse(String errorResult) {
    refreshLayout.setRefreshing(false);
    showToast(errorResult);
  }

  private void showToast(String string) {
    Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      mListener = (OnChildHistoryFragmentInteractionListener) activity;
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
  public interface OnChildHistoryFragmentInteractionListener {
    public void onChildHistoryFragmentInteraction();
  }
}
