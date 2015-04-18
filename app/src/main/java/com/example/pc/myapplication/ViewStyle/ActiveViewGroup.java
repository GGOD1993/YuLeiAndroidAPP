package com.example.pc.myapplication.ViewStyle;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.R;

import java.util.ArrayList;


public class ActiveViewGroup extends ViewGroup{

  private boolean isRefresh = true;

  private ArrayList<ActiveView> arrayList = new ArrayList<>();

  public ActiveViewGroup(Context context) {
    super(context);
  }

  public ActiveViewGroup(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public boolean isRefresh() {
    return isRefresh;
  }

  public void setRefresh(boolean isRefresh) {
    this.isRefresh = isRefresh;
  }

  /**
   * 计算所有ChildView的宽度和高度 然后根据ChildView的计算结果，设置自己的宽和高
   */
  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
    /**
     * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
     */
    int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
    int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
    // 计算出所有的childView的宽和高
    measureChildren(widthMeasureSpec, heightMeasureSpec);
    /**
     * 如果是wrap_content设置为我们计算的值
     * 否则：直接设置为父容器计算的值
     */
    setMeasuredDimension(sizeWidth, sizeHeight);
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b){
    int cCount = getChildCount();
    int mTotalHeight = 0;
    int cWidth;
    int cHeight;

    if (isRefresh) {
      for (int i = 0; i < cCount; i++){
        View childView = getChildAt(i);
        cWidth = childView.getMeasuredWidth();
        cHeight = childView.getMeasuredHeight();
        if (mTotalHeight >= getMeasuredHeight() - cHeight) {
          mTotalHeight = 0;
          l += cWidth;
        }
        childView.layout(l, mTotalHeight, l + cWidth, mTotalHeight + cHeight);
        mTotalHeight += cHeight;
      }
    }
  }

  public void addActiveView(ActiveView view) {

    addView(view);
    arrayList.add(view);
  }

  public void removeActiveViewAt(int position) {
    removeViewAt(position);
  }

  public void removeActiveViewByTaskId(String taskId) {
    int i = 0;
    ActiveView activeView;
    for (;i<getChildCount();i++) {
      activeView = (ActiveView) getChildAt(i);
      if (activeView.getTaskInfo().getTaskName().equals(taskId)) {
        break;
      }
    }
    removeActiveViewAt(i);
    arrayList.remove(i);
  }

  public void changeActiveViewBgByTask(String taskId) {
    int i = 0;
    ActiveView activeView;
    for (;i<getChildCount();i++) {
      activeView = (ActiveView) getChildAt(i);
      if (activeView.getTaskInfo().getTaskName().equals(taskId)) {
        break;
      }
    }
    int taskStatus = arrayList.get(i).getTaskInfo().getTaskStatus();
    switch (taskStatus) {
      case AppConstant.STATUS_NEW:
        getChildAt(i).setBackgroundResource(R.mipmap.ic_launcher);
        break;
      case AppConstant.STATUS_SUBMITTED:
        getChildAt(i).setBackgroundResource(R.mipmap.sun_loading_blue);
        break;
      case AppConstant.STATUS_FINISHED:
        getChildAt(i).setBackgroundResource(R.mipmap.ic_tab_image);
        break;
    }
  }

  public ArrayList<ActiveView> getChildArrayList() {
    arrayList.clear();
    for (int i=0; i < getChildCount() ; i ++) {
      arrayList.add((ActiveView) getChildAt(i));
    }
    return arrayList;
  }
}
