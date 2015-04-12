package com.example.pc.myapplication.ViewStyle;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.R;


public class RefreshableLinearLayout extends LinearLayout implements View.OnTouchListener{

  //下拉刷新
  public static final int STATUS_PULL_TO_REFRESH = 0;

  //释放立即刷新
  public static final int STATUS_RELEASE_TO_REFRESH = 1;

  //正在刷新
  public static final int STATUS_REFRESHING = 2;

  //普通状态
  public static final int STATUS_REFRESH_FINISHED = 3;

  //下拉头部回滚速度
  public static final int SCROLL_SPEED = -20;

  //一分钟的毫秒值
  public static final long ONE_MINUTE = 60 * 1000;

  //一小时的毫秒值
  public static final long ONE_HOUR = ONE_MINUTE * 60;

  //一天的毫秒值
  public static final long ONE_DAY = ONE_HOUR * 24;

  //一个月的毫秒值
  public static final long ONE_MONTH = ONE_DAY * 30;

  //一年的毫秒值
  public static final long ONE_YEAR = ONE_MONTH * 12;

  //上次更新的时间字符串的存储键值
  private static final String UPDATE_AT = "update_at";

  //下拉刷新的回调接口
  private PullToRefreshListener mListener;

  //SharedPreference
  private SharedPreferences preferences;

  //下拉头部
  private View header;

  //需要去下拉刷新的content
  private ActiveViewGroup activeViewGroup;

  //刷新时显示的进度条
  private ProgressBar progressBar;

  //下拉刷新的箭头
  private ImageView arrow;

  //下拉刷新的文字描述
  private TextView description;

  //下拉刷新的时间
  private TextView updateAt;

  //下拉头的局部参数
  private MarginLayoutParams headerLayoutParams;

  //上次更新时间的毫秒值
  private long lastUpdateTime;

  //下拉刷新头部的高度
  private int headerHeight;

  //当前的下拉头状态
  private int currentStatus = STATUS_REFRESH_FINISHED;

  //记录上一次状态,以免冲突
  private int lastStatus = currentStatus;

  //手指按下屏幕的纵坐标
  private float yDown;

  //在被判定为滚动之前,用户手指能移动的最大值
  private int touchSlop;

  //是否已经加载过一次layout,只需要加载一次
  private boolean loadOnce;

  public RefreshableLinearLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    preferences = context.getSharedPreferences(AppConstant.PREFERENCE_NAME,0);
    header = LayoutInflater.from(context).inflate(R.layout.layout_pull_to_refresh_header, null, true);
    progressBar = (ProgressBar) header.findViewById(R.id.progress_bar);
    arrow = (ImageView) header.findViewById(R.id.arrow);
    description = (TextView) header.findViewById(R.id.description);
    updateAt = (TextView) header.findViewById(R.id.updated_at);
    touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    updateDescription();
    setOrientation(VERTICAL);
    addView(header,0);
  }

  /**
   * 绑定触摸事件等操作
   * @param changed
   * @param l
   * @param t
   * @param r
   * @param b
   */
  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    super.onLayout(changed, l, t, r, b);
    if (changed && ! loadOnce) {
      headerHeight = -header.getHeight();
      headerLayoutParams = (MarginLayoutParams) header.getLayoutParams();
      headerLayoutParams.topMargin = headerHeight;
      activeViewGroup = (ActiveViewGroup) getChildAt(1);
      activeViewGroup.setOnTouchListener(this);
      loadOnce = true;
    }
  }

  @Override
  public boolean onTouch(View v, MotionEvent event) {

    if (activeViewGroup.isRefresh()) {
      activeViewGroup.setRefresh(false);
    }

    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        yDown = event.getRawY();
        break;
      case MotionEvent.ACTION_MOVE:
        float yMove = event.getRawY();
        int distance = (int) (yMove - yDown);

//          如果手指是下滑状态，并且下拉头是完全隐藏的，就屏蔽下拉事件
        if (distance <= 0 && headerLayoutParams.topMargin <= headerHeight) {
          return false;
        }
        if (distance < touchSlop) {
          return false;
        }
        if (currentStatus != STATUS_REFRESHING) {
          if (headerLayoutParams.topMargin > 0) {
            currentStatus = STATUS_RELEASE_TO_REFRESH;
          } else {
            currentStatus = STATUS_PULL_TO_REFRESH;
          }
          // 通过偏移下拉头的topMargin值，来实现下拉效果
          headerLayoutParams.topMargin = (distance / 2) + headerHeight;
          header.setLayoutParams(headerLayoutParams);
        }
        break;

      case MotionEvent.ACTION_UP:

      default:
        if (currentStatus == STATUS_RELEASE_TO_REFRESH) {
          // 松手时如果是释放立即刷新状态，就去调用正在刷新的任务
          new RefreshingTask().execute();
        } else if (currentStatus == STATUS_PULL_TO_REFRESH) {
          // 松手时如果是下拉状态，就去调用隐藏下拉头的任务
          new HideHeaderTask().execute();
        }
        break;
    }

    // 时刻记得更新下拉头中的信息
    if (currentStatus == STATUS_PULL_TO_REFRESH
            || currentStatus == STATUS_RELEASE_TO_REFRESH) {
      updateHeaderView();
      // 当前正处于下拉或释放状态，要让ListView失去焦点，否则被点击的那一项会一直处于选中状态
      lastStatus = currentStatus;
      // 当前正处于下拉或释放状态，通过返回true屏蔽掉ListView的滚动事件
      return true;
    }
    return true;
  }

  public void setOnRefreshListener(PullToRefreshListener listener) {
    mListener = listener;
  }

  /**
   * 当所有的刷新逻辑完成后，记录调用一下，否则你的ListView将一直处于正在刷新状态。
   */
  public void finishRefreshing() {
    currentStatus = STATUS_REFRESH_FINISHED;
    preferences.edit().putLong(UPDATE_AT, System.currentTimeMillis()).apply();
    new HideHeaderTask().execute();
  }

  /**
   * 更新下拉刷新头中的信息
   */
  private void updateHeaderView() {
    if (lastStatus != currentStatus) {
      if (currentStatus == STATUS_PULL_TO_REFRESH) {
        description.setText(getResources().getString(R.string.pull_to_refresh));
        arrow.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        rotateArrow();
      } else if (currentStatus == STATUS_RELEASE_TO_REFRESH) {
        description.setText(getResources().getString(R.string.release_to_refresh));
        arrow.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        rotateArrow();
      } else if (currentStatus == STATUS_REFRESHING) {
        description.setText(getResources().getString(R.string.refreshing));
        progressBar.setVisibility(View.VISIBLE);
        arrow.clearAnimation();
        arrow.setVisibility(View.GONE);
      }
      updateDescription();
    }
  }

  /**
   * 根据当前的状态来旋转箭头
   */
  private void rotateArrow() {
    float pivotX = arrow.getWidth() / 2f;
    float pivotY = arrow.getHeight() / 2f;
    float fromDegrees = 0f;
    float toDegrees = 0f;
    if (currentStatus == STATUS_PULL_TO_REFRESH) {
      fromDegrees = 180f;
      toDegrees = 360f;
    } else if (currentStatus == STATUS_RELEASE_TO_REFRESH) {
      fromDegrees = 0f;
      toDegrees = 180f;
    }
    RotateAnimation animation = new RotateAnimation(fromDegrees, toDegrees, pivotX, pivotY);
    animation.setDuration(300);
    animation.setFillAfter(true);
    arrow.startAnimation(animation);
  }

  /**
   * 更新文字描述
   */
  private void updateDescription() {
    lastUpdateTime = preferences.getLong(UPDATE_AT, -1);
    long currentTime = System.currentTimeMillis();
    long timePassed = currentTime - lastUpdateTime;
    long timeIntoFormat;
    String updateAtValue;
    if (lastUpdateTime == -1) {
      updateAtValue = getResources().getString(R.string.not_updated_yet);
    } else if (timePassed < 0) {
      updateAtValue = getResources().getString(R.string.time_error);
    } else if (timePassed < ONE_MINUTE) {
      updateAtValue = getResources().getString(R.string.updated_just_now);
    } else if (timePassed < ONE_HOUR) {
      timeIntoFormat = timePassed / ONE_MINUTE;
      String value = timeIntoFormat + "分钟";
      updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
    } else if (timePassed < ONE_DAY) {
      timeIntoFormat = timePassed / ONE_HOUR;
      String value = timeIntoFormat + "小时";
      updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
    } else if (timePassed < ONE_MONTH) {
      timeIntoFormat = timePassed / ONE_DAY;
      String value = timeIntoFormat + "天";
      updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
    } else if (timePassed < ONE_YEAR) {
      timeIntoFormat = timePassed / ONE_MONTH;
      String value = timeIntoFormat + "个月";
      updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
    } else {
      timeIntoFormat = timePassed / ONE_YEAR;
      String value = timeIntoFormat + "年";
      updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
    }
    updateAt.setText(updateAtValue);
  }

  /**
   * 正在刷新的任务，在此任务中会去回调注册进来的下拉刷新监听器。
   *
   * @author guolin
   */
  class RefreshingTask extends AsyncTask<Void, Integer, Void> {

    @Override
    protected Void doInBackground(Void... params) {
      int topMargin = headerLayoutParams.topMargin;
      while (true) {
        topMargin = topMargin + SCROLL_SPEED;
        if (topMargin <= 0) {
          topMargin = 0;
          break;
        }
        publishProgress(topMargin);
        sleep(10);
      }
      currentStatus = STATUS_REFRESHING;
      publishProgress(0);
      if (mListener != null) {
        mListener.onRefresh();
      }
      return null;
    }

    @Override
    protected void onProgressUpdate(Integer... topMargin) {
      updateHeaderView();
      headerLayoutParams.topMargin = topMargin[0];
      header.setLayoutParams(headerLayoutParams);
    }
  }

  /**
   * 隐藏下拉头的任务，当未进行下拉刷新或下拉刷新完成后，此任务将会使下拉头重新隐藏。
   *
   * @author guolin
   */
  class HideHeaderTask extends AsyncTask<Void, Integer, Integer> {

    @Override
    protected Integer doInBackground(Void... params) {
      int topMargin = headerLayoutParams.topMargin;
      while (true) {
        topMargin = topMargin + SCROLL_SPEED;
        if (topMargin <= headerHeight) {
          topMargin = headerHeight;
          break;
        }
        publishProgress(topMargin);
        sleep(10);
      }
      return topMargin;
    }

    @Override
    protected void onProgressUpdate(Integer... topMargin) {
      headerLayoutParams.topMargin = topMargin[0];
      header.setLayoutParams(headerLayoutParams);
    }

    @Override
    protected void onPostExecute(Integer topMargin) {
      headerLayoutParams.topMargin = topMargin;
      header.setLayoutParams(headerLayoutParams);
      currentStatus = STATUS_REFRESH_FINISHED;
    }
  }

  /**
   * 使当前线程睡眠指定的毫秒数。
   *
   * @param time
   * 指定当前线程睡眠多久，以毫秒为单位
   */
  private void sleep(int time) {
    try {
      Thread.sleep(time);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * 刷新回调接口
   */
  public interface PullToRefreshListener{
    void onRefresh();
  }
}
