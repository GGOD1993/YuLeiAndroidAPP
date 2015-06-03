package com.example.pc.myapplication.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.Infos.DiyTaskInfo;
import com.example.pc.myapplication.R;

import java.util.HashMap;
import java.util.List;

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewHolder> {

  //上下文的引用
  private Context context;

  //任务列表
  private List<DiyTaskInfo> taskList;

  //用来区别是发出的任务还是接受到的任务
  private int type;

  public TaskRecyclerViewAdapter(List<DiyTaskInfo> taskList, Context context, int type) {
    super();
    this.taskList = taskList;
    this.context = context;
    this.type = type;
  }

  @Override
  public TaskRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
    View view = View.inflate(viewGroup.getContext(), R.layout.layout_parent_recyclerview_item, null);
    TaskRecyclerViewHolder holder = new TaskRecyclerViewHolder(view);
    return holder;
  }

  @Override
  public void onBindViewHolder(final TaskRecyclerViewHolder viewHolder,int i) {
    // 绑定数据到ViewHolder上
    final DiyTaskInfo task = taskList.get(i);
    if (AppConstant.SEND_TASK_TYPE == type) viewHolder.textViewUserId.setText(task.getToUserId());
    else viewHolder.textViewUserId.setText(task.getFromUserId());
    switch (task.getTaskStatus()) {
      case AppConstant.STATUS_NEW:
        viewHolder.textViewTaskStatus.setText(AppConstant.STATUS_NEW_STRING);
        break;
      case AppConstant.STATUS_SUBMITTED:
        viewHolder.textViewTaskStatus.setText(AppConstant.STATUS_SUBMITTED_STRING);
        break;

      case AppConstant.STATUS_FINISHED:
        viewHolder.textViewTaskStatus.setText(AppConstant.STATUS_FINISHED_STRING);
        break;
    }
    viewHolder.textViewTaskName.setText(task.getTaskName());
    viewHolder.textViewAwrad.setText((task.getAward()));
    viewHolder.circularImageUserImage.setImageResource(R.mipmap.ic_launcher);
    viewHolder.textViewTaskContent.setText(task.getTaskContent());
    viewHolder.imageButtonSubmit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
         if (AppConstant.SEND_TASK_TYPE == type) {
           switch (task.getTaskStatus()) {
             case AppConstant.STATUS_NEW:
               showToast("发出的心愿还未提交,请耐心等待~~~");
               break;
             case AppConstant.STATUS_SUBMITTED:
               HashMap<String, String> map = new HashMap<>();
               map.put(AppConstant.TASK_ID, task.getTaskName());
               map.put(AppConstant.FROM_USERID, task.getFromUserId());
//               HttpService.DoFinishDiyTaskRequest(map, (ParentMsgFragment) fragment);
               break;

             case AppConstant.STATUS_FINISHED:
               showToast("该心愿已经完成~~~");
               break;
           }
        } else {
           switch (task.getTaskStatus()) {
             case AppConstant.STATUS_NEW:
               HashMap<String, String> map = new HashMap<>();
               map.put(AppConstant.TASK_ID, task.getTaskName());
               map.put(AppConstant.TO_USERID, task.getToUserId());
//               HttpService.DoSubmitDiyTaskRequest(map, (ParentBabyFragment) fragment);
               break;
             case AppConstant.STATUS_SUBMITTED:
               showToast("该心愿已经提交,请耐心等待~~~");
               break;
             case AppConstant.STATUS_FINISHED:
               showToast("该心愿已经完成~~~");
               break;
           }
        }
      }
    });
  }

  @Override
  public int getItemCount() {
    return taskList.size();
  }

  private void showToast(String string) {
    Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
  }
}
