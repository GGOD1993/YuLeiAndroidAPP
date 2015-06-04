package com.example.pc.myapplication.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.example.pc.myapplication.Infos.DonateProjectInfo;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.utils.RequestQueueController;

import java.util.List;

public class ProjectRecyclerViewAdapter extends RecyclerView.Adapter<ProjectRecyclerViewHolder> {

  //上下文引用
  private Context context;

  //任务列表
  private List<DonateProjectInfo> projectList;

  //ImageLoader
  private ImageLoader imageLoader;

  public ProjectRecyclerViewAdapter(Context context, List<DonateProjectInfo> list) {
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
  public ProjectRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
    View view = View.inflate(viewGroup.getContext(), R.layout.layout_recyclerview_charity_item, null);
    ProjectRecyclerViewHolder holder = new ProjectRecyclerViewHolder(view);
    return holder;
  }

  @Override
  public void onBindViewHolder(final ProjectRecyclerViewHolder viewHolder, int i) {
    final DonateProjectInfo projectInfo = projectList.get(i);
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

  private void showToast(String string) {
    Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
  }
}
