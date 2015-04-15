package com.example.pc.myapplication.ViewStyle;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * recyclerView中每个item之间的间距
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
  private int space;

  public SpaceItemDecoration(int space) {
    this.space = space;
  }

  @Override
  public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
    outRect.right = space;
    outRect.left = space;
    outRect.bottom = space;
    // Add top margin only for the first item to avoid double space between items
    if(parent.getChildPosition(view) == 0)
      outRect.top = space;
  }
}
