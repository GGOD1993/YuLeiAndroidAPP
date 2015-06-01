package com.example.pc.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pc.myapplication.R;
import com.example.pc.myapplication.Infos.SystemTaskInfo;

import java.util.ArrayList;

class ListViewHolder{
  public TextView textViewTaskId;
  public TextView textViewTaskContent;
}

public class AddSystemTaskListViewAdapter extends BaseAdapter {

  //上下文的引用
  private Context context;

  //列表中每一项的容器
  private ListViewHolder listViewHolder;

  //存放当前获取到的任务列表
  private ArrayList<SystemTaskInfo> systemTaskList;

  public AddSystemTaskListViewAdapter(Context context, ArrayList<SystemTaskInfo> systemTaskList) {
    this.context = context;
    this.systemTaskList = systemTaskList;
  }

  @Override
  public int getCount() {
    return systemTaskList.size();
  }

  @Override
  public Object getItem(int position) {
    return position;
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    listViewHolder = null;
    if (null == convertView) {
      listViewHolder = new ListViewHolder();
      convertView = LayoutInflater.from(context).inflate(R.layout.layout_parent_listview_item, null);
      listViewHolder.textViewTaskId= (TextView) convertView.findViewById(R.id.parent_addsystemtaskactivity_listview_taskid);
      listViewHolder.textViewTaskContent= (TextView) convertView.findViewById(R.id.parent_addsystemtaskactivity_listview_taskcontent);
      convertView.setTag(listViewHolder);
    } else listViewHolder = (ListViewHolder) convertView.getTag();
    SystemTaskInfo taskInfo = systemTaskList.get(position);
    listViewHolder.textViewTaskId.setText(taskInfo.getTaskId());
    listViewHolder.textViewTaskContent.setText(taskInfo.getTaskContent());
    return convertView;
  }
}
