package com.example.pc.myapplication.ViewStyle;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.R;

import java.util.ArrayList;
import java.util.Random;


public class ActiveViewGroup extends ViewGroup {

  private Random random = new Random();

  private int mode = AppConstant.ONLAYOUT_MODE_NONE;

  private ArrayList<ActiveView> arrayList = new ArrayList<>();

  public ActiveViewGroup(Context context) {
    super(context);
  }

  public ActiveViewGroup(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public int getMode() {
    return mode;
  }

  public void setMode(int mode) {
    this.mode = mode;
  }

  /**
   * 计算所有ChildView的宽度和高度 然后根据ChildView的计算结果，设置自己的宽和高
   */
  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
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
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    int cCount = getChildCount();
    int mTotalHeight = 0;
    int cWidth;
    int cHeight;
    switch (mode) {
      case AppConstant.ONLAYOUT_MODE_NONE:

        break;
      case AppConstant.ONLAYOUT_MODE_RANDOM:
        for (int i = 0; i < cCount; i++) {
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
        break;
      case AppConstant.ONLAYOUT_MODE_FROM_DOWN:
        for (int i = 0; i < cCount; i++) {
          int parentHeight = getMeasuredHeight();
          int parentWidth = getMeasuredWidth();
          View childView = getChildAt(i);
          cWidth = childView.getMeasuredWidth();
          cHeight = childView.getMeasuredHeight();
          l = random.nextInt(parentWidth - cWidth) + 1;
          t = random.nextInt(parentHeight) + 1 + parentHeight;
          childView.layout(l, t, l + cWidth, t + cHeight);
        }
        break;
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
    for (; i < getChildCount(); i++) {
      activeView = (ActiveView) getChildAt(i);
      if (activeView.getTaskInfo().getTaskName().equals(taskId)) {
        break;
      }
    }
    removeActiveViewAt(i);
    arrayList.remove(i);
  }

  /**
   * 根据任务状态更换标示
   * @param taskId
   */
  public void changeActiveViewBgByTask(String taskId) {
    int i = 0;
    ActiveView activeView;
    for (; i < getChildCount(); i++) {
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
    for (int i = 0; i < getChildCount(); i++) {
      arrayList.add((ActiveView) getChildAt(i));
    }
    return arrayList;
  }
}
