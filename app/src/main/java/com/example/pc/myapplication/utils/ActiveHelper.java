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

  /**
   * 运行在主线程中的Handler
   */
  Handler mHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      switch(msg.what) {
        case AppConstant.START_MOVE:

          list = activeViewGroup.getChildArrayList();
          for (ActiveView child : list) {
            dealWithChild(child);
          }
          activeViewGroup.invalidate();

          if (!isSpecGet) {
            activeViewGroupHeight = activeViewGroup.getHeight();
            activeViewGroupWidth = activeViewGroup.getWidth();
            isSpecGet = true;
          }
          break;

        case AppConstant.STOP_MOVE:
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
  public void prepareMove() {

    task = new TimerTask() {
      @Override
      public void run() {
        Message message = new Message();
        message.what = AppConstant.START_MOVE;
        mHandler.sendMessage(message);
      }
    };
  }

  /**
   * 开始移动
   */
  public void startMove() {
    if (!isTaskRun) {
      prepareMove();
      timer.schedule(task, 1000, AppConstant.MOVE_INTERVAL_TIME);
      isTaskRun = true;
    }
  }

  /**
   * 停止控件的移动
   */
  public void stopMove() {
    mHandler.sendEmptyMessage(AppConstant.STOP_MOVE);
  }

  /**
   * 处理控件的位置
   * @param child
   */
  private void dealWithChild(ActiveView child) {
    checkWithChild(child);
    moveWithChild(child);
  }

  /**
   * 检测移动方向
   * @param child
   */
  private void checkWithChild(ActiveView child) {
    if (child.getRight() > activeViewGroupWidth - child.getMoveXSpeed()) {
      child.setMoveXDirection(AppConstant.LEFT_DIRECTION);
    }
    if (child.getLeft() < child.getMoveXSpeed()) {
      child.setMoveXDirection(AppConstant.RIGHT_DIRECTION);
    }
    if (child.getTop() < child.getMoveYSpeed()) {
      child.setMoveYDirection(AppConstant.DOWN_DIRECTION);
    }
    if (child.getBottom() > activeViewGroupHeight - child.getMoveYSpeed()) {
      child.setMoveYDirection(AppConstant.UP_DIRECTION);
    }
  }

  /**
   * 改变控件位置
   * @param child
   */
  private void moveWithChild(ActiveView child) {

    if (AppConstant.RIGHT_DIRECTION == child.getMoveXDirection()) {
      child.setLeft(child.getLeft() + child.getMoveXSpeed());
      child.setRight(child.getRight() + child.getMoveXSpeed());
    } else {
      child.setLeft(child.getLeft() - child.getMoveXSpeed());
      child.setRight(child.getRight() - child.getMoveXSpeed());
    }

    if (AppConstant.UP_DIRECTION == child.getMoveYDirection()) {
      child.setTop(child.getTop() - child.getMoveYSpeed());
      child.setBottom(child.getBottom() - child.getMoveYSpeed());
    } else {
      child.setTop(child.getTop() + child.getMoveYSpeed());
      child.setBottom(child.getBottom() + child.getMoveYSpeed());
    }
  }
}
