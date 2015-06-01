package com.example.pc.myapplication.ViewStyle;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.pc.myapplication.Infos.DiyTaskInfo;

import java.util.Random;


public class ActiveView extends TextView {

  //运动方向
  protected int moveXDirection;
  protected int moveYDirection;

  //旋转方向
  protected int rotateDirection;

  //运动速度
  protected int moveXSpeed;
  protected int moveYSpeed;

  //旋转的速度
  protected float rotateSpeed;

  //随机数发生器
  protected Random random;

  //View携带的任务信息
  protected DiyTaskInfo taskInfo;

  public ActiveView(Context context) {
    super(context);
  }

  public ActiveView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public DiyTaskInfo getTaskInfo() {
    return taskInfo;
  }

  public void setTaskInfo(DiyTaskInfo taskInfo) {
    this.taskInfo = taskInfo;
  }

  public int getMoveXDirection() {
    return moveXDirection;
  }

  public void setMoveXDirection(int moveXDirection) {
    this.moveXDirection = moveXDirection;
  }

  public int getMoveYDirection() {
    return moveYDirection;
  }

  public void setMoveYDirection(int moveYDirection) {
    this.moveYDirection = moveYDirection;
  }

  public int getMoveXSpeed() {
    return moveXSpeed;
  }

  public int getMoveYSpeed() {
    return moveYSpeed;
  }

  public float getRotateSpeed() {
    return rotateSpeed;
  }

  public int getRotateDirection() {
    return rotateDirection;
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
  }

  /**
   * 初始化view的移动参数
   */
  public void initParams() {
  }
}
