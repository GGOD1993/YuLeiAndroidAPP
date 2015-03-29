package com.example.pc.myapplication.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.pc.myapplication.R;
import com.example.pc.myapplication.TaskInfo.DiyTaskInfo;

import java.util.List;

public class ParentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder>{

    private Context context;
    private List<DiyTaskInfo> taskList;

    private RecyclerViewItemClickListener recyclerViewItemClickListener;

    public ParentRecyclerViewAdapter(List<DiyTaskInfo> taskList, Context context) {
        super();
        this.taskList = taskList;
        this.context = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // 创建一个View，简单起见直接使用系统提供的布局，就是一个TextView
        View view = View.inflate(viewGroup.getContext(), R.layout.layout_parent_recyclerview_item, null);
        // 创建一个ViewHolder
        RecyclerViewHolder holder = new RecyclerViewHolder(view, recyclerViewItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder viewHolder, int i) {
        // 绑定数据到ViewHolder上
        DiyTaskInfo task = taskList.get(i);
        viewHolder.parent_recyclerview_textview_childid.setText(task.getChildId());
        viewHolder.parent_rectclerview_textview_award.setText(task.getTaskName());
        viewHolder.parent_rectclerview_textview_award.setText((task.getAward()));
        viewHolder.parent_recyclerview_imageview_taskstate.setImageResource(R.mipmap.image_parent_recyclerview_item_doing);

        viewHolder.parent_recyclerview_circularimage_userimage.setImageResource(R.mipmap.ic_launcher);

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void setOnItemClickListener(RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;
    }

}
