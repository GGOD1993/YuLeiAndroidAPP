package com.example.pc.myapplication.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.pc.myapplication.R;
import com.example.pc.myapplication.ViewStyle.CircularImage;

public class CharityRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public CircularImage circularImage;
    public TextView name;
    public TextView brief;
    public TextView contact;
    public TextView addr;
    private RecyclerViewItemClickListener mListener;

    @Override
    public void onClick(View v) {
        if (null != mListener) {
            mListener.onItemClick(v, getPosition());
        }
    }

    public CharityRecyclerViewHolder(View itemView, RecyclerViewItemClickListener mListener) {
        super(itemView);
        this.mListener = mListener;
        itemView.setOnClickListener(this);
        circularImage = (CircularImage) itemView.findViewById(R.id.charity_recyclerview_circularimage_userimage);
        name = (TextView) itemView.findViewById(R.id.charity_recyclerview_textview_name);
        brief = (TextView) itemView.findViewById(R.id.charity_recyclerview_textview_briefinfo);
        contact = (TextView) itemView.findViewById(R.id.charity_recyclerview_textview_contact);
        addr = (TextView) itemView.findViewById(R.id.charity_recyclerview_textview_addr);
    }
}
