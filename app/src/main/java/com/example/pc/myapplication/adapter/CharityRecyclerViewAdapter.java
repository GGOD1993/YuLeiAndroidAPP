package com.example.pc.myapplication.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.example.pc.myapplication.Infos.CharityInfo;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.fragment.child.ChildCharityInfoFragment;
import com.example.pc.myapplication.utils.RequestQueueController;

import java.util.List;

public class CharityRecyclerViewAdapter extends RecyclerView.Adapter<CharityRecyclerViewHolder> {

    //上下文引用
    private ChildCharityInfoFragment context;

    //任务列表
    private List<CharityInfo> projectList;

    //ImageLoader
    private ImageLoader imageLoader;

    public CharityRecyclerViewAdapter(ChildCharityInfoFragment context, List<CharityInfo> list) {
        super();
        this.projectList = list;
        this.context = context;
        imageLoader = new ImageLoader(RequestQueueController.get().getRequestQueue(), new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String s) {
                return null;
            }

            @Override
            public void putBitmap(String s, Bitmap bitmap) {
            }
        });
    }

    @Override
    public CharityRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = View.inflate(viewGroup.getContext(), R.layout.layout_recyclerview_charity_item, null);
        CharityRecyclerViewHolder holder = new CharityRecyclerViewHolder(view, context);
        return holder;
    }

    @Override
    public void onBindViewHolder(final CharityRecyclerViewHolder viewHolder, int i) {
        final CharityInfo projectInfo = projectList.get(i);
        viewHolder.name.setText(projectInfo.getName());
        viewHolder.brief.setText(projectInfo.getBrief());
        viewHolder.contact.setText(projectInfo.getContact());
        viewHolder.addr.setText(projectInfo.getAddr());
        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(viewHolder.circularImage, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        imageLoader.get(projectInfo.getImg_url(), imageListener);
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

}
