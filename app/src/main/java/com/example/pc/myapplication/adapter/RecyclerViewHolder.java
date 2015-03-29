package com.example.pc.myapplication.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.myapplication.R;
import com.example.pc.myapplication.ViewStyle.CircularImage;

public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView parent_recyclerview_textview_childid;
    public TextView parent_recyclerview_textview_briefinfo;
    public TextView parent_rectclerview_textview_award;
    public ImageView parent_recyclerview_imageview_taskstate;
    public CircularImage parent_recyclerview_circularimage_userimage;

    //每一项的点击监听
    private RecyclerViewItemClickListener mListener;

    public RecyclerViewHolder(View itemView,  RecyclerViewItemClickListener mListener) {
        super(itemView);

        parent_recyclerview_textview_childid = (TextView) itemView.findViewById(R.id.parent_recyclerview_textview_childid);
        parent_recyclerview_textview_briefinfo = (TextView) itemView.findViewById(R.id.parent_recyclerview_textview_briefinfo);
        parent_rectclerview_textview_award = (TextView) itemView.findViewById(R.id.parent_rectclerview_textview_award);
        parent_recyclerview_imageview_taskstate = (ImageView) itemView.findViewById(R.id.parent_recyclerview_imageview_taskstate);
        parent_recyclerview_circularimage_userimage = (CircularImage)  itemView.findViewById(R.id.parent_recyclerview_circularimage_userimage);

        this.mListener = mListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(mListener != null){
            mListener.onItemClick(v,getPosition());
        }
    }
}
