package com.example.pc.myapplication.ViewStyle;

import android.content.Context;
import android.util.AttributeSet;

import com.example.pc.myapplication.TaskInfo.DiyTaskInfo;


public class ActiveSeedView extends ActiveView {

  public ActiveSeedView(Context context, DiyTaskInfo task) {
    super(context);
    this.setTaskInfo(task);
  }

  public ActiveSeedView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }



}
