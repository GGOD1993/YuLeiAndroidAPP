package com.example.pc.myapplication.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pc.myapplication.R;
import com.example.pc.myapplication.ViewStyle.CircularImage;

public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

  //根视图
  public RelativeLayout relativeLayoutHeader;

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

  //每一项的点击监听
  private RecyclerViewItemClickListener mListener;

  public RecyclerViewHolder(View itemView,  RecyclerViewItemClickListener mListener) {
    super(itemView);
    relativeLayoutHeader = (RelativeLayout) itemView.findViewById(R.id.parent_recyclerview_relativelayout_header);
    textViewUserId = (TextView) itemView.findViewById(R.id.parent_recyclerview_textview_childid);
    textViewTaskName = (TextView) itemView.findViewById(R.id.parent_recyclerview_textview_briefinfo);
    textViewAwrad = (TextView) itemView.findViewById(R.id.parent_rectclerview_textview_award);
    circularImageUserImage = (CircularImage) itemView.findViewById(R.id.parent_recyclerview_circularimage_userimage);
    textViewTaskContent = (TextView) itemView.findViewById(R.id.parent_recyclerview_textview_taskcontent);
    imageButtonClose = (ImageButton) itemView.findViewById(R.id.parent_recyclerview_imagebutteon_close);
    this.mListener = mListener;
    itemView.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    if(mListener != null) mListener.onItemClick(v,getPosition());
  }
}
