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

import com.example.pc.myapplication.AppConstant;
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

public class ChildCharityInfoFragment extends Fragment implements
        HttpService.OnGetCharityRequestResponseListener{

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

  public static ChildCharityInfoFragment newInstance() {
    ChildCharityInfoFragment fragment = new ChildCharityInfoFragment();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }
  public ChildCharityInfoFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_child_charity_info, container, false);

    projectList = new ArrayList<>();
    initView(view);
    return view;
  }

  private void initView(View v) {
    recyclerView = ((RecyclerView) v.findViewById(R.id.child_donatefragment_recyclerview));
    refreshLayout = ((SwipeRefreshLayout) v.findViewById(R.id.child_donatefragment_swiperefreshlayout));

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
    recyclerView.setLayoutManager(linearLayoutManager);
    adapter = new ProjectRecyclerViewAdapter(getActivity(), projectList);
    recyclerView.addItemDecoration(new SpaceItemDecoration(40));
    ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(adapter);
    scaleAdapter.setFirstOnly(false);
    AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(scaleAdapter);
    alphaAdapter.setFirstOnly(false);
    recyclerView.setAdapter(alphaAdapter);
    refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        HttpService.DoGetCharityRequest(null, ChildCharityInfoFragment.this);
      }
    });
  }

  @Override
  public void OnGetCharitySuccessResponse(JSONArray jsonArray) {
    refreshLayout.setRefreshing(false);
    JSONObject project;
    DonateProjectInfo projectInfo;
    projectList.clear();
    for (int i = 0; i < jsonArray.length(); i++) {
      try {
        project = (JSONObject) jsonArray.get(i);
        projectInfo = new DonateProjectInfo(
                project.getString(AppConstant.CHARITY_NAME),
                project.getString(AppConstant.CHARITY_IMG_URL),
                project.getString(AppConstant.CHARITY_BIREF),
                project.getString(AppConstant.CHARITY_CONTACT),
                project.getString(AppConstant.CHARITY_ADDRESS)
        );
        projectList.add(projectInfo);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    adapter.notifyDataSetChanged();
  }

  @Override
  public void OnGetCharityErrorResponse(String errorResult) {
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
