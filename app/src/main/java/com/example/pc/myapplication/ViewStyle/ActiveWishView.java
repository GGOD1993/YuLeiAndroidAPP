package com.example.pc.myapplication.ViewStyle;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;

import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.activity.FinishTaskActivity;
import com.example.pc.myapplication.activity.SubmitTaskActivity;

import java.util.Random;

public class ActiveWishView extends ActiveView implements View.OnClickListener{

  public ActiveWishView(Context context) {
    super(context);
    initParams();
    this.setOnClickListener(this);
  }

  public ActiveWishView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  /**
   * 初始化view的移动参数
   */
  public void initParams() {
    random = new Random();
    moveXSpeed = random.nextInt(AppConstant.TOP_SPEED) + 1;
    moveYSpeed = random.nextInt(AppConstant.TOP_SPEED) + 1;
    rotateSpeed = random.nextFloat() * 3;
    if (random.nextInt(100)%2 == 0) moveXDirection = AppConstant.RIGHT_DIRECTION;
    else moveXDirection = AppConstant.LEFT_DIRECTION;
    if (random.nextInt(100)%2 == 0) moveYDirection = AppConstant.UP_DIRECTION;
    else moveYDirection = AppConstant.DOWN_DIRECTION;
    if (random.nextInt(100)%2 == 0) rotateDirection = AppConstant.CLOCKSIDE_DIRECTION;
    else rotateDirection = AppConstant.ANTICLOCKSIDE_DIRECTION;
  }

  @Override
  public void onClick(View v) {
    if (getTaskInfo().getToUserId().equals(getContext().
            getSharedPreferences(AppConstant.PREFERENCE_NAME, 0).getString(AppConstant.FROM_USERID,""))) {
      Intent intent = new Intent(getContext(), SubmitTaskActivity.class);
      intent.putExtra(AppConstant.CLICKED_RECIVE_TASK, getTaskInfo());
      getContext().startActivity(intent);
    } else {
      Intent intent = new Intent(getContext(), FinishTaskActivity.class);
      intent.putExtra(AppConstant.CLICKED_SEND_TASK, getTaskInfo());
      getContext().startActivity(intent);
    }
  }
}
