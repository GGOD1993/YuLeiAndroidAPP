package com.example.pc.myapplication.ViewStyle;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.TaskInfo.DiyTaskInfo;

import java.io.Serializable;
import java.util.Random;

public class ActiveGameView extends ActiveView implements View.OnClickListener, Serializable {

  //运动方向
  private int moveXDirection;
  private int moveYDirection;

  //旋转方向
  private int rotateDirection;

  //运动速度
  private int moveXSpeed;
  private int moveYSpeed;

  //旋转的速度
  private float rotateSpeed;

  //随机数发生器
  private Random random;

  //View携带的任务信息
  private DiyTaskInfo taskInfo;

  public ActiveGameView(Context context) {
    super(context);
    initParams();
    this.setOnClickListener(this);
  }

  public ActiveGameView(Context context, AttributeSet attrs) {
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
  @Override
  public void initParams() {
    random = new Random();
    moveXSpeed = random.nextInt(AppConstant.TOP_SPEED) + 1;
    moveYSpeed = random.nextInt(AppConstant.TOP_SPEED) + 6;
    rotateSpeed = random.nextFloat() * 3;
    if (random.nextInt(100) % 2 == 0) moveXDirection = AppConstant.RIGHT_DIRECTION;
    else moveXDirection = AppConstant.LEFT_DIRECTION;
    moveYDirection = AppConstant.UP_DIRECTION;
    if (random.nextInt(100) % 2 == 0) rotateDirection = AppConstant.CLOCKSIDE_DIRECTION;
    else rotateDirection = AppConstant.ANTICLOCKSIDE_DIRECTION;
  }

  @Override
  public void onClick(final View v) {
    ActiveGameView.this.setClickable(false);
    if (AppConstant.ONLAYOUT_MODE_NONE != ((ActiveViewGroup) getParent()).getMode()) {
      ((ActiveViewGroup) getParent()).setMode(AppConstant.ONLAYOUT_MODE_NONE);
    }
    ActiveGameView.this.animate()
            .x(((ActiveViewGroup) getParent()).getWidth())
            .y(((ActiveViewGroup) getParent()).getHeight())
            .alpha(0).scaleX(0).scaleY(0).setDuration(1000)
            .setInterpolator(new AccelerateDecelerateInterpolator())
            .setListener(new Animator.AnimatorListener() {
              @Override
              public void onAnimationStart(Animator animation) {

              }

              @Override
              public void onAnimationEnd(Animator animation) {
                ActiveGameView.this.setVisibility(GONE);
                Intent intent = new Intent(AppConstant.BROADCAST_MOVE_TO_WISH_BAG);
                intent.putExtra(AppConstant.CLICKED_GAME_WISH_TASK, ActiveGameView.this.getTaskInfo());
                getContext().sendBroadcast(intent);
              }

              @Override
              public void onAnimationCancel(Animator animation) {

              }

              @Override
              public void onAnimationRepeat(Animator animation) {

              }
            });
  }
}