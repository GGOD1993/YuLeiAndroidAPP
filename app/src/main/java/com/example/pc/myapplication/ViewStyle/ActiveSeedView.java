package com.example.pc.myapplication.ViewStyle;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.Infos.DiyTaskInfo;

import java.util.Random;


public class ActiveSeedView extends ActiveView implements View.OnClickListener {

  //控件最大偏移量
  private int detaY;

  //控件实际偏移量
  public int actualDetaY = 0;

  public int getDetaY() {
    return detaY;
  }

  public ActiveSeedView(Context context, DiyTaskInfo task) {
    super(context);
    this.setTaskInfo(task);
    this.setOnClickListener(this);
    initParams();
  }

  public ActiveSeedView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public void initParams() {
    random = new Random();
    moveXSpeed = 0;
    moveYSpeed = 1;
    rotateSpeed = 0;
    detaY = random.nextInt(10) + 10;
    if (random.nextInt(100) % 2 == 0) moveXDirection = AppConstant.RIGHT_DIRECTION;
    else moveXDirection = AppConstant.LEFT_DIRECTION;
    if (random.nextInt(100) % 2 == 0) moveYDirection = AppConstant.UP_DIRECTION;
    else moveYDirection = AppConstant.DOWN_DIRECTION;
    if (random.nextInt(100) % 2 == 0) rotateDirection = AppConstant.CLOCKSIDE_DIRECTION;
    else rotateDirection = AppConstant.ANTICLOCKSIDE_DIRECTION;
  }

  @Override
  public void onClick(View v) {
    ActiveSeedView view = ActiveSeedView.this;
    if (view.isSelected()) view.setSelected(false);
    else view.setSelected(true);
  }
}
