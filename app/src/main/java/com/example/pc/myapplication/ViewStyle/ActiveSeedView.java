package com.example.pc.myapplication.ViewStyle;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.TaskInfo.DiyTaskInfo;

import java.util.Random;


public class ActiveSeedView extends ActiveView implements View.OnClickListener{

  public ActiveSeedView(Context context, DiyTaskInfo task) {
    super(context);
    this.setTaskInfo(task);
    this.setOnClickListener(this);
  }

  public ActiveSeedView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public void initParams() {
    random = new Random();
    moveXSpeed = 0;
    moveYSpeed = 10;
    rotateSpeed = 0;
    if (random.nextInt(100) % 2 == 0) moveXDirection = AppConstant.RIGHT_DIRECTION;
    else moveXDirection = AppConstant.LEFT_DIRECTION;
    if (random.nextInt(100) % 2 == 0) moveYDirection = AppConstant.UP_DIRECTION;
    else moveYDirection = AppConstant.DOWN_DIRECTION;
    if (random.nextInt(100) % 2 == 0) rotateDirection = AppConstant.CLOCKSIDE_DIRECTION;
    else rotateDirection = AppConstant.ANTICLOCKSIDE_DIRECTION;
  }

  @Override
  public void onClick(View v) {
    Log.e("dada",ActiveSeedView.this.getTaskInfo().toString());
  }
}
