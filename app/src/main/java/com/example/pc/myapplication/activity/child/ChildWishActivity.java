package com.example.pc.myapplication.activity.child;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.TaskInfo.DiyTaskInfo;
import com.example.pc.myapplication.ViewStyle.ActiveGameView;
import com.example.pc.myapplication.ViewStyle.ActiveSeedView;
import com.example.pc.myapplication.ViewStyle.ActiveViewGroup;
import com.example.pc.myapplication.utils.ActiveHelper;
import com.example.pc.myapplication.utils.CountTimeAsyncTask;
import com.example.pc.myapplication.utils.HttpService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class ChildWishActivity extends SwipeBackActivity
        implements HttpService.OnGetDiyTaskRequestResponseListener,
        CountTimeAsyncTask.OnAsyncTaskCompleteListener{

  //随机数发生器
  private Random rand = new Random();

  //布局的header
  private RelativeLayout header;

  //header title
  private TextView textViewHeaderTitle;

  //title
  private TextView textViewTitle;

  //倒计时异步任务
  private CountTimeAsyncTask asyncTask;

  //开始按钮
  private ImageButton imageButtonStart;

  //返回按钮
  private ImageButton imageButtonBack;

  //心愿背包
  private ImageButton imageButtonWishBag;

  //漂浮心愿的ViewGroup
  private ActiveViewGroup activeViewGroup;

  //漂浮助手
  private ActiveHelper activeHelper;

  //任务袋
  private ArrayList<DiyTaskInfo> taskBag;

  //接受ActiveGameView发送来的广播
  private ActiveGameViewBroadcastReciver broadcastReciver;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_child_wish);
    initViews();
  }

  @Override
  protected void onStart() {
    broadcastReciver = new ActiveGameViewBroadcastReciver();
    IntentFilter intentFilter = new IntentFilter(AppConstant.BROADCAST_MOVE_TO_WISH_BAG);
    registerReceiver(broadcastReciver, intentFilter);
    super.onStart();
  }

  @Override
  protected void onStop() {
    unregisterReceiver(broadcastReciver);
    if (null != asyncTask) asyncTask.cancel(true);
    activeHelper.stopMove();
    super.onStop();
  }

  /**
   * 初始化控件
   */
  private void initViews() {
    header = (RelativeLayout) findViewById(R.id.child_wishactivity_header);
    textViewTitle = (TextView) findViewById(R.id.child_wishactivity_textview_starttext);
    imageButtonStart = (ImageButton) findViewById(R.id.child_wishactivity_imagebutton_start);
    imageButtonBack = (ImageButton) findViewById(R.id.child_wishactivity_imagebutton_back);
    imageButtonWishBag = (ImageButton) findViewById(R.id.child_wishactivity_imagebutton_wishbag);
    activeViewGroup = (ActiveViewGroup) findViewById(R.id.child_wishactivity_activeviewgroup);
    imageButtonStart = ((ImageButton) findViewById(R.id.child_wishactivity_imagebutton_start));
    imageButtonBack = ((ImageButton) findViewById(R.id.child_wishactivity_imagebutton_back));
    textViewHeaderTitle = (TextView) header.findViewById(R.id.child_mainactivity_header_textview);
    activeHelper = new ActiveHelper(activeViewGroup);

    textViewHeaderTitle.setText("心 愿 种 子");
    imageButtonStart.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String url = AppConstant.GET_DIY_TASK_URL + "?" + AppConstant.USERNAME + "=" +
                getApplicationContext().getSharedPreferences(AppConstant.PREFERENCE_NAME, 0)
                        .getString(AppConstant.FROM_USERID, "");
        HttpService.DoGetDiyTaskRequest(Request.Method.GET, url, null, ChildWishActivity.this);
      }
    });
    imageButtonBack.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
    startInitAnim();
  }

  /**
   * 初始化动画
   */
  private void startInitAnim() {
    YoYo.with(Techniques.FadeIn).duration(2000).playOn(textViewTitle);
    YoYo.with(Techniques.FadeIn).duration(2000).playOn(imageButtonStart);
    YoYo.with(Techniques.FadeIn).duration(2000).playOn(imageButtonBack);
  }

  /**
   * 消失动画
   */
  private void goneAnim() {
    YoYo.with(Techniques.FadeOut).duration(2000).playOn(textViewTitle);
    YoYo.with(Techniques.FadeOut).duration(2000).playOn(imageButtonStart);
    YoYo.with(Techniques.FadeOut).duration(2000).playOn(imageButtonBack);
    imageButtonStart.setClickable(false);
    imageButtonBack.setClickable(false);
    imageButtonWishBag.setVisibility(View.VISIBLE);
    YoYo.with(Techniques.FadeIn).duration(2000).playOn(imageButtonWishBag);
  }

  /**
   * 开始倒计时
   */
  private void startDownCountTime() {
    asyncTask = new CountTimeAsyncTask(textViewHeaderTitle, activeViewGroup, ChildWishActivity.this);
    asyncTask.execute();
  }

  /**
   * 添加控件
   */
  private void addActiveViews(JSONArray jsonArray) {
    activeViewGroup.setMode(AppConstant.ONLAYOUT_MODE_FROM_DOWN);
    activeViewGroup.removeAllViews();
    ActiveGameView activeGameView;
    JSONObject object;
    Context context = getApplicationContext();
    int count = jsonArray.length();
    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    for (int i = 0; i < count; i++) {
      activeGameView = new ActiveGameView(context);
      activeGameView.setLayoutParams(layoutParams);
      try{
        object = jsonArray.getJSONObject(i);
        activeGameView.setTaskInfo(new DiyTaskInfo(
                object.getString(AppConstant.TO_USERID),
                object.getString(AppConstant.TASK_ID),
                object.getString(AppConstant.TASK_REGDATE),
                object.getString(AppConstant.TASK_CONTENT),
                object.getString(AppConstant.FROM_USERID),
                0));
        switch (object.getInt(AppConstant.TASK_STATUS)) {
          case AppConstant.STATUS_NEW:
            activeGameView.setBackgroundResource(R.mipmap.ic_launcher);
            break;
          case AppConstant.STATUS_SUBMITTED:
            activeGameView .setBackgroundResource(R.mipmap.sun_loading_blue);
            break;
          case AppConstant.STATUS_FINISHED:
            activeGameView.setBackgroundResource(R.mipmap.ic_tab_image);
            break;
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
      activeViewGroup.addActiveView(activeGameView);
    }
  }

  /**
   * 网络清求响应
   * @param jsonArray
   */
  @Override
  public void OnGetDiyTaskSuccessResponse(JSONArray jsonArray) {
    addActiveViews(jsonArray);
    goneAnim();
    startDownCountTime();
    activeHelper.startGameModeMove();
    taskBag = new ArrayList<>();
  }

  @Override
  public void OnGetDiyTaskErrorResponse(String errorResult) {
    showToast(errorResult);
  }

  @Override
  public void asyncTaskComplete() {
    final Context context = getApplicationContext();
    textViewHeaderTitle.setText("捕 获 到 的 种 子");
    activeViewGroup.removeAllViews();
    activeViewGroup.setMode(AppConstant.ONLAYOUT_MODE_WISH_BAG);
    for (DiyTaskInfo task : taskBag) {
      ActiveSeedView view = new ActiveSeedView(context, task);
      Log.e("YSpeed",view.getMoveYSpeed() + "");
      view.setBackgroundResource(R.mipmap.test1);
      int spec = rand.nextInt(200) + 200;
      ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(spec, spec);
      view.setLayoutParams(layoutParams);
      YoYo.with(Techniques.Pulse).interpolate(new AccelerateDecelerateInterpolator()).duration(1000).playOn(view);
      activeViewGroup.addActiveView(view);
    }
    activeHelper.startSeedModeMove();
  }

  private void showToast(String string) {
    Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
  }

  /**
   * 广播接收器
   */
  class ActiveGameViewBroadcastReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      YoYo.with(Techniques.Shake).duration(500).playOn(imageButtonWishBag);
      taskBag.add((DiyTaskInfo) intent.getParcelableExtra(AppConstant.CLICKED_GAME_WISH_TASK));
    }
  }
}

