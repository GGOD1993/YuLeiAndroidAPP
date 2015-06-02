package com.example.pc.myapplication.fragment.child;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pc.myapplication.Infos.DonateProjectInfo;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.ViewStyle.SpaceItemDecoration;
import com.example.pc.myapplication.adapter.ProjectRecyclerViewAdapter;
import com.example.pc.myapplication.utils.HttpService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

public class ChildDonateFragment extends Fragment implements
        HttpService.OnGetProjectRequestResponseListener{

  //列表
  private RecyclerView recyclerView;

  //下拉刷新
  private SwipeRefreshLayout refreshLayout;

  //RecyclerView的Adapter
  private ProjectRecyclerViewAdapter adapter;

  //捐赠项目的列表
  private ArrayList<DonateProjectInfo> projectList;

  //回调的监听
  private OnChildFuncFragmentInteractionListener mListener;

  public static ChildDonateFragment newInstance() {
    ChildDonateFragment fragment = new ChildDonateFragment();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }
  public ChildDonateFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_child_donate, container, false);

    projectList = new ArrayList<>();
    initView(view);
    return view;
  }

  private void initView(View v) {
    recyclerView = ((RecyclerView) v.findViewById(R.id.child_donatefragment_recyclerview));
    refreshLayout = ((SwipeRefreshLayout) v.findViewById(R.id.child_donatefragment_swiperefreshlayout));

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
    recyclerView.setLayoutManager(linearLayoutManager);
    adapter = new ProjectRecyclerViewAdapter(projectList, ChildDonateFragment.this);
    recyclerView.addItemDecoration(new SpaceItemDecoration(30));
    ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(adapter);
    scaleAdapter.setFirstOnly(false);
    AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(scaleAdapter);
    alphaAdapter.setFirstOnly(false);
    recyclerView.setAdapter(alphaAdapter);
    refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        HttpService.DoGetProjectRequest(null, ChildDonateFragment.this);
      }
    });
  }

  @Override
  public void OnGetProjectSuccessResponse(JSONArray jsonArray) {
    refreshLayout.setRefreshing(false);
    JSONObject arrayTask;
    DonateProjectInfo projectInfo;
    projectList.clear();
    for (int i = 0; i < jsonArray.length(); i++) {
      try {
        arrayTask = (JSONObject) jsonArray.get(i);
        projectInfo = new DonateProjectInfo();
        projectList.add(projectInfo);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    adapter.notifyDataSetChanged();
  }

  @Override
  public void OnGetProjectErrorResponse(String errorResult) {
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
      mListener = (OnChildFuncFragmentInteractionListener) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException(activity.toString()
              + " must implement OnFragmentInteractionListener");
    }
  }
  @Override
  public void onDetach() {
    mListener = null;
    super.onDetach();
  }
  public interface OnChildFuncFragmentInteractionListener {
    public void onChildFuncFragmentInteraction();
  }
}
