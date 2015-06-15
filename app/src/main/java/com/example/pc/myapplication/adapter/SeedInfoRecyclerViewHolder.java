package com.example.pc.myapplication.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.pc.myapplication.R;
import com.example.pc.myapplication.ViewStyle.CircularImage;

public class SeedInfoRecyclerViewHolder extends RecyclerView.ViewHolder {

    public CircularImage image;
    public TextView company;
    public TextView charity;
    public TextView seeds;

    public SeedInfoRecyclerViewHolder(View v) {
        super(v);
        image = ((CircularImage) v.findViewById(R.id.seed_info_recyclerview_circularimage_userimage));
        company = ((TextView) v.findViewById(R.id.seed_info_textview_company));
        charity = ((TextView) v.findViewById(R.id.seed_info_recyclerview_textview_charity_name));
        seeds = ((TextView) v.findViewById(R.id.seed_info_textview_seeds));
    }
}
