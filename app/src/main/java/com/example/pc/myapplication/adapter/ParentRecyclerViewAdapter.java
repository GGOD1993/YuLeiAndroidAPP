package com.example.pc.myapplication.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.TaskInfo.DiyTaskInfo;

import java.util.List;

public class ParentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

  //上下文的引用
  private Context context;

  //任务列表
  private List<DiyTaskInfo> taskList;

  //用来区别是发出的任务还是接受到的任务
  private int type;

  //点击事件的回调方法
  private RecyclerViewItemClickListener recyclerViewItemClickListener;

  public ParentRecyclerViewAdapter(List<DiyTaskInfo> taskList, Context context, int type) {
    super();
    this.taskList = taskList;
    this.context = context;
    this.type = type;
  }

  @Override
  public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
    // 创建一个View，简单起见直接使用系统提供的布局，就是一个TextView
    View view = View.inflate(viewGroup.getContext(), R.layout.layout_parent_recyclerview_item, null);
    // 创建一个ViewHolder
    if (i == 0) {
      view.setTag(AppConstant.RECYCLERVIEW_FIRST_TAG);
    }
    RecyclerViewHolder holder = new RecyclerViewHolder(view, recyclerViewItemClickListener);
    return holder;
  }

  @Override
  public void onBindViewHolder(RecyclerViewHolder viewHolder,final int i) {
    // 绑定数据到ViewHolder上
    final DiyTaskInfo task = taskList.get(i);
    if (AppConstant.SEND_TASK_TYPE == type) viewHolder.textViewUserId.setText(task.getToUserId());
    else viewHolder.textViewUserId.setText(task.getFromUserId());
    switch (task.getTaskStatus()) {
      case AppConstant.STATUS_NEW:
        viewHolder.relativeLayoutHeader.setBackgroundColor(R.color.greenyellow);
        break;
      case AppConstant.STATUS_SUBMITTED:
        viewHolder.relativeLayoutHeader.setBackgroundColor(R.color.greenyellow);
        break;

      case AppConstant.STATUS_FINISHED:
        viewHolder.relativeLayoutHeader.setBackgroundColor(R.color.greenyellow);
        break;
    }
    viewHolder.textViewTaskName.setText(task.getTaskName());
    viewHolder.textViewAwrad.setText((task.getAward()));
    viewHolder.circularImageUserImage.setImageResource(R.mipmap.ic_launcher);
    viewHolder.textViewTaskContent.setText(task.getTaskContent());
  }

  @Override
  public int getItemCount() {
    return taskList.size();
  }

  public void setOnItemClickListener(RecyclerViewItemClickListener recyclerViewItemClickListener) {
    this.recyclerViewItemClickListener = recyclerViewItemClickListener;
  }

}
