package com.example.pc.myapplication.adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pc.myapplication.Infos.DonateProjectInfo;
import com.example.pc.myapplication.R;

import java.util.List;

public class ProjectRecyclerViewAdapter extends RecyclerView.Adapter<ProjectRecyclerViewHolder>{

  //上下文的引用
  private Fragment fragment;

  //任务列表
  private List<DonateProjectInfo> projectList;

  //用来区别是发出的任务还是接受到的任务
  private int type;

  public ProjectRecyclerViewAdapter(List<DonateProjectInfo> list, Fragment fragment) {
    super();
    this.projectList = list;
    this.fragment = fragment;
  }

  @Override
  public ProjectRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
    View view = View.inflate(viewGroup.getContext(), R.layout.layout_parent_recyclerview_item, null);
    ProjectRecyclerViewHolder holder = new ProjectRecyclerViewHolder(view);
    return holder;
  }

  @Override
  public void onBindViewHolder(final ProjectRecyclerViewHolder viewHolder,int i) {
    final DonateProjectInfo projectInfo = projectList.get(i);

  }

  @Override
  public int getItemCount() {
    return projectList.size();
  }

  private void showToast(String string) {
    Toast.makeText(fragment.getActivity(), string, Toast.LENGTH_SHORT).show();
  }
}
