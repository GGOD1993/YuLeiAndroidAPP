package com.example.pc.myapplication.utils;

import android.os.Handler;
import android.os.Message;

import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.ViewStyle.ActiveView;
import com.example.pc.myapplication.ViewStyle.ActiveViewGroup;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ActiveHelper {

  //移动View的定时任务是否在执行
  private boolean isTaskRun;

  //判断父控件的尺寸是否得到了
  private boolean isSpecGet;

  //定时任务和定时器
  private TimerTask task;
  private Timer timer = new Timer();

  //存放view的列表
  private ArrayList<ActiveView> list = null;

  //父控件及其属性
  private ActiveViewGroup activeViewGroup;
  private int activeViewGroupHeight;
  private int activeViewGroupWidth;

  //两种移动的模式
  private static final int MODE_WISH = 1;
  private static final int MODE_GAME = 2;
  private static final int MODE_SEED = 3;
  private static final int START_MODE_WISH_MOVE = 3;
  private static final int START_MODE_GAME_MOVE = 4;
  private static final int START_MODE_SEED_MOVE = 5;
  private static final int STOP_MOVE = 5;
  private final int MOVE_INTERVAL_TIME = 50;

  /**
   * 运行在主线程中的Handler
   */
  Handler mHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case START_MODE_WISH_MOVE:
          list = activeViewGroup.getChildArrayList();
          for (ActiveView child : list) {
            dealWithWishChild(child);
          }
          activeViewGroup.invalidate();
          if (!isSpecGet) {
            activeViewGroupHeight = activeViewGroup.getHeight();
            activeViewGroupWidth = activeViewGroup.getWidth();
            isSpecGet = true;
          }
          break;
        case START_MODE_GAME_MOVE:
          list = activeViewGroup.getChildArrayList();
          for (ActiveView child : list) {
            dealWithGameChild(child);
          }
          activeViewGroup.invalidate();
          if (!isSpecGet) {
            activeViewGroupHeight = activeViewGroup.getHeight();
            activeViewGroupWidth = activeViewGroup.getWidth();
            isSpecGet = true;
          }
          break;
        case STOP_MOVE:
          if (null != task) {
            task.cancel();
            isTaskRun = false;
          }
          break;
      }
    }
  };


  public ActiveHelper(ActiveViewGroup activeViewGroup) {
    isTaskRun = false;
    isSpecGet = false;
    this.activeViewGroup = activeViewGroup;
  }

  /**
   * 移动开始前的准备工作
   */
  public void prepareMove(int mode) {
    switch (mode) {
      case MODE_WISH:
        task = new TimerTask() {
          @Override
          public void run() {
            Message message = new Message();
            message.what = START_MODE_WISH_MOVE;
            mHandler.sendMessage(message);
          }
        };
        break;
      case MODE_GAME:
        task = new TimerTask() {
          @Override
          public void run() {
            Message message = new Message();
            message.what = START_MODE_GAME_MOVE;
            mHandler.sendMessage(message);
          }
        };
        break;
      case MODE_SEED:
        break;
    }
  }

  /**
   * 开始愿望模式移动
   */
  public void startWishModeMove() {
    if (!isTaskRun) {
      prepareMove(MODE_WISH);
      timer.schedule(task, 1000, MOVE_INTERVAL_TIME);
      isTaskRun = true;
    }
  }

  /**
   * 开始游戏模式移动
   */
  public void startGameModeMove() {
    if (!isTaskRun) {
      prepareMove(MODE_GAME);
      timer.schedule(task, 1000, MOVE_INTERVAL_TIME);
      isTaskRun = true;
    }
  }

  public void startChooseModeMove() {

  }

  /**
   * 停止控件的移动
   */
  public void stopMove() {
    mHandler.sendEmptyMessage(STOP_MOVE);
  }

  /**
   * 处理Wish模式控件的位置
   * @param child
   */
  private void dealWithWishChild(ActiveView child) {
    checkWithWishChild(child);
    moveWithChild(child);
  }

  /**
   * 处理Game模式控件的位置
   * @param child
   */
  private void dealWithGameChild(ActiveView child) {
    checkWithGameChild(child);
    moveWithChild(child);
  }

  /**
   * 处理Float模式控件的位置
   * @param child
   */
  private void dealWithFloatChild(ActiveView child) {
    checkWithFloatChild(child);
    moveWithChild(child);
  }

  /**
   * 检测Wish模式控件移动方向
   * @param child
   */
  private void checkWithWishChild(ActiveView child) {
    if (child.getRight() > activeViewGroupWidth - child.getMoveXSpeed())
      child.setMoveXDirection(AppConstant.LEFT_DIRECTION);
    if (child.getLeft() < child.getMoveXSpeed())
      child.setMoveXDirection(AppConstant.RIGHT_DIRECTION);
    if (child.getTop() < child.getMoveYSpeed()) child.setMoveYDirection(AppConstant.DOWN_DIRECTION);
    if (child.getBottom() > activeViewGroupHeight - child.getMoveYSpeed())
      child.setMoveYDirection(AppConstant.UP_DIRECTION);
  }

  /**
   * 检测Game模式控件的移动方向
   *
   * @param child
   */
  private void checkWithGameChild(ActiveView child) {
    if (child.getRight() > activeViewGroupWidth - child.getMoveXSpeed())
      child.setMoveXDirection(AppConstant.LEFT_DIRECTION);
    if (child.getLeft() < child.getMoveXSpeed())
      child.setMoveXDirection(AppConstant.RIGHT_DIRECTION);
    if (child.getBottom() < 1) {
      child.setTop(activeViewGroupHeight);
      child.setBottom(activeViewGroupHeight + child.getMeasuredHeight());
    }
  }

  /**
   * 检测Float模式控件的移动方向
   * @param child
   */
  private void checkWithFloatChild(ActiveView child) {
    if (child.getMoveYDirection() == AppConstant.DOWN_DIRECTION) child.setMoveYDirection(AppConstant.UP_DIRECTION);
    else child.setMoveYDirection(AppConstant.DOWN_DIRECTION);
  }

  /**
   * 改变控件位置
   *
   * @param child
   */
  private void moveWithChild(ActiveView child) {
    int childLeft = child.getLeft();
    int childRight = child.getRight();
    int childTop = child.getTop();
    int childBottom = child.getBottom();
    int childXSpeed = child.getMoveXSpeed();
    int childYSpeed = child.getMoveYSpeed();
    int childRotateDirection = child.getRotateDirection();
    float childRotateSpeed = child.getRotateSpeed();
    float childRotation = child.getRotation();

    if (AppConstant.RIGHT_DIRECTION == child.getMoveXDirection()) {
      child.setLeft(childLeft + childXSpeed);
      child.setRight(childRight + childXSpeed);
    } else {
      child.setLeft(childLeft - childXSpeed);
      child.setRight(childRight - childXSpeed);
    }
    if (AppConstant.UP_DIRECTION == child.getMoveYDirection()) {
      child.setTop(childTop - childYSpeed);
      child.setBottom(childBottom - childYSpeed);
    } else {
      child.setTop(childTop + childYSpeed);
      child.setBottom(childBottom + childYSpeed);
    }
    if (AppConstant.CLOCKSIDE_DIRECTION == childRotateDirection) {
      if (childRotation < 360 - childRotateSpeed)
        child.setRotation(childRotation + childRotateSpeed);
      else child.setRotation(0);
    } else {
      if (childRotation > childRotateSpeed - 360)
        child.setRotation(childRotation - childRotateSpeed);
      else child.setRotation(0);
    }
  }
}
