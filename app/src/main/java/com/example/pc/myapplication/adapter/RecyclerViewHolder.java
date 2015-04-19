package com.example.pc.myapplication.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pc.myapplication.R;
import com.example.pc.myapplication.ViewStyle.CircularImage;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

  //标示任务状态
  public TextView textViewTaskStatus;

  //用户id
  public TextView textViewUserId;

  //任务名称
  public TextView textViewTaskName;

  //任务奖励
  public TextView textViewAwrad;

  //头像框
  public CircularImage circularImageUserImage;

  //任务内容
  public TextView textViewTaskContent;

  //关闭按钮
  public ImageButton imageButtonClose;

  //提交按钮
  public ImageButton imageButtonSubmit;

  public RecyclerViewHolder(View itemView) {
    super(itemView);
    textViewTaskStatus = (TextView) itemView.findViewById(R.id.parent_recyclerview_textview_taskstatus);
    textViewUserId = (TextView) itemView.findViewById(R.id.parent_recyclerview_textview_childid);
    textViewTaskName = (TextView) itemView.findViewById(R.id.parent_recyclerview_textview_briefinfo);
    textViewAwrad = (TextView) itemView.findViewById(R.id.parent_rectclerview_textview_award);
    circularImageUserImage = (CircularImage) itemView.findViewById(R.id.parent_recyclerview_circularimage_userimage);
    textViewTaskContent = (TextView) itemView.findViewById(R.id.parent_recyclerview_textview_taskcontent);
    imageButtonClose = (ImageButton) itemView.findViewById(R.id.parent_recyclerview_imagebutten_close);
    imageButtonSubmit = (ImageButton) itemView.findViewById(R.id.parent_recyclerview_submit);
  }
}
