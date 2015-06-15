package com.example.pc.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.pc.myapplication.Infos.DonateDataInfo;
import com.example.pc.myapplication.R;

import java.util.ArrayList;

public class SeedInfoRecyclerViewAdapter extends RecyclerView.Adapter<SeedInfoRecyclerViewHolder>{

    private Context context;
    private ArrayList<DonateDataInfo> donateDataInfos;

    public SeedInfoRecyclerViewAdapter(ArrayList<DonateDataInfo> list, Context context) {
        donateDataInfos = list;
        this.context = context;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return donateDataInfos.size();
    }

    @Override
    public SeedInfoRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.layout_seed_info_recyclerview_item, null);
        SeedInfoRecyclerViewHolder holder = new SeedInfoRecyclerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(SeedInfoRecyclerViewHolder holder, int position) {
        final DonateDataInfo dataInfo = donateDataInfos.get(position);
        holder.charity.setText(dataInfo.getCharity_name());
        holder.company.setText(dataInfo.getCompany());
        holder.seeds.setText(dataInfo.getSeeds());
    }
}
