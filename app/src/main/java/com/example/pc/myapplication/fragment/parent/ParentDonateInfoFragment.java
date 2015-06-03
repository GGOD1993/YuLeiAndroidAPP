package com.example.pc.myapplication.fragment.parent;

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
import com.example.pc.myapplication.adapter.ProjectRecyclerViewAdapter;
import com.example.pc.myapplication.utils.HttpService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParentDonateInfoFragment extends Fragment implements
        HttpService.OnGetProjectRequestResponseListener {

  private RecyclerView recyclerView;

  private ArrayList<DonateProjectInfo> projectList;

  private ProjectRecyclerViewAdapter adapter;

  private SwipeRefreshLayout refreshLayout;

  private OnDynamicFragmentInteractionListener mListener;

  public static ParentDonateInfoFragment newInstance() {
    ParentDonateInfoFragment fragment = new ParentDonateInfoFragment();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  public ParentDonateInfoFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_parent_donate_info, container, false);
    projectList = new ArrayList<>();
    initViews(v);
    return v;
  }

  private void initViews(View v) {
    recyclerView = ((RecyclerView) v.findViewById(R.id.parent_donateinfofragment_recyclerview));
    refreshLayout = ((SwipeRefreshLayout) v.findViewById(R.id.parent_donateinfofragment_swiperefreshlayout));

    adapter = new ProjectRecyclerViewAdapter(projectList, ParentDonateInfoFragment.this);
    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(adapter);

    refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        HttpService.DoGetProjectRequest(null, ParentDonateInfoFragment.this);
      }
    });
  }

  @Override
  public void OnGetProjectSuccessResponse(JSONArray jsonArray) {
    refreshLayout.setRefreshing(false);
    JSONObject project;
    DonateProjectInfo projectInfo;
    projectList.clear();
    for (int i = 0; i < jsonArray.length(); i++) {
      try {
        project = (JSONObject) jsonArray.get(i);
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
      mListener = (OnDynamicFragmentInteractionListener) activity;
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
  public interface OnDynamicFragmentInteractionListener {
    public void onDynamicFragmentInteraction();
  }

}
