package com.example.pc.myapplication.activity.child;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
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
import com.example.pc.myapplication.ViewStyle.ActiveView;
import com.example.pc.myapplication.ViewStyle.ActiveViewGroup;
import com.example.pc.myapplication.utils.ActiveHelper;
import com.example.pc.myapplication.utils.CountTimeAsyncTask;
import com.example.pc.myapplication.utils.HttpService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class ChildWishActivity extends SwipeBackActivity
        implements HttpService.OnGetDiyTaskRequestResponseListener,
        HttpService.OnSetDiyTaskRequestResponseListener,
        HttpService.OnGetParentRequestResponseListener,
        CountTimeAsyncTask.OnAsyncTaskCompleteListener {

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

  //preference
  private SharedPreferences preferences;

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
    preferences = getSharedPreferences(AppConstant.PREFERENCE_NAME, 0);
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
    imageButtonWishBag.setBackgroundResource(R.mipmap.wish_bag);
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
      try {
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
            activeGameView.setBackgroundResource(R.mipmap.sun_loading_blue);
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
   *
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
      view.setBackgroundResource(R.drawable.child_seed_textview_style);
      view.setText(task.getTaskContent());
      view.setTextColor(getResources().getColor(R.color.indianred));
      view.setGravity(Gravity.CENTER);
      int spec = rand.nextInt(200) + 200;
      view.setTextSize(TypedValue.COMPLEX_UNIT_PX, 70 * spec / 400);
      ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(spec, spec);
      view.setLayoutParams(layoutParams);
      YoYo.with(Techniques.Pulse).interpolate(new AccelerateDecelerateInterpolator()).duration(1000).playOn(view);
      activeViewGroup.addActiveView(view);
    }
    activeHelper.startSeedModeMove();

    imageButtonWishBag.setBackgroundResource(R.drawable.parent_addtaskactivity_imagebutton_submit_style);
    imageButtonWishBag.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        initParentFromNetwork();
      }
    });
  }

  /**
   * 从网络获取关系
   */
  private void initParentFromNetwork() {
    String url = AppConstant.GET_PARENT_URL + "?" + AppConstant.USERID + "=" +
            preferences.getString(AppConstant.FROM_USERID, "");
    HttpService.DoGetParentRequest(Request.Method.GET, url, null, ChildWishActivity.this);
  }

  /**
   * 从缓存获取关系
   */
  private void initParentFromCache() {
    HashSet<String> set = ((HashSet) preferences.getStringSet(AppConstant.PREFERENCE_LINKED_PARENT, null));
    if (null != set) showChooseToUserDialog(set);
    else showToast("貌似出了点问题诶.....");
  }

  /**
   * 弹出选择发送对象的对话框
   *
   * @param set
   */
  private void showChooseToUserDialog(HashSet<String> set) {
    Iterator<String> iterator = set.iterator();
    String mom = "暂时还木有绑定噢~";
    String father = mom;
    if (iterator.hasNext()) {
      mom = iterator.next();
      if (iterator.hasNext()) {
      father = iterator.next();
      }
    }
    View v = getLayoutInflater().inflate(R.layout.layout_child_choose_parent, null);
    AlertDialog.Builder builder = new AlertDialog.Builder(ChildWishActivity.this);
    builder.setView(v);
    ((TextView) v.findViewById(R.id.child_wishactivity_dialog_textview_username_father)).setText(father);
    ((TextView) v.findViewById(R.id.child_wishactivity_dialog_textview_username_mom)).setText(mom);

    v.findViewById(R.id.child_wishactivity_dialog_relativelayout_mom).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (((TextView) v.findViewById(R.id.child_wishactivity_dialog_textview_username_mom)).getText().toString().contains("木有"))
          showToast("暂时还木有和麻麻绑定噢~");
        else sendDiyTasks(((TextView) v.findViewById(R.id.child_wishactivity_dialog_textview_username_mom)).getText().toString());
      }
    });

    v.findViewById(R.id.child_wishactivity_dialog_relativelayout_father).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (((TextView) v.findViewById(R.id.child_wishactivity_dialog_textview_username_father)).getText().toString().contains("木有"))
          showToast("暂时还木有和粑粑绑定噢~");
        else sendDiyTasks(((TextView) v.findViewById(R.id.child_wishactivity_dialog_textview_username_father)).getText().toString());
      }
    });
    builder.show();
  }

  /**
   * 发送自定义任务
   * @param toUserId
   */
  private void sendDiyTasks(String toUserId) {
    DiyTaskInfo task;
    HashMap<String,String> map;
    String fromUserId = preferences.getString(AppConstant.FROM_USERID, "");
    for (ActiveView view : activeViewGroup.getChildArrayList()) {
      if (view.isSelected()) {
        task = view.getTaskInfo();
        map = new HashMap<>();
        map.put(AppConstant.FROM_USERID, fromUserId);
        map.put(AppConstant.TASK_CONTENT, task.getTaskContent());
        map.put(AppConstant.AWARD, task.getAward());
        map.put(AppConstant.TO_USERID, toUserId);
        HttpService.DoSetDiyTaskRequest(Request.Method.POST, AppConstant.SET_DIY_TASK_URL, map, ChildWishActivity.this);
      }
    }
  }

  @Override
  public void OnGetParentSuccessResponse(JSONArray jsonArray) {
    JSONObject object;
    HashSet<String> set = new HashSet<>();
    try {
      for (int i = 0; i < jsonArray.length(); i++) {
        object = (JSONObject) jsonArray.get(i);
        if (null != object) {
          String name = object.getString(AppConstant.PARENT);
          set.add(name);
        }
      }
      showChooseToUserDialog(set);
      preferences.edit().putStringSet(AppConstant.PREFERENCE_LINKED_PARENT, set).apply();
    } catch (JSONException e) {
      e.printStackTrace();
      initParentFromCache();
    }
  }

  @Override
  public void OnGetParentErrorResponse(String errorResult) {
    initParentFromCache();
  }

  @Override
  public void OnSetDiyTaskErrorResponse(String errorMsg) {
  }

  @Override
  public void OnSetDiyTaskSuccessResponse(JSONArray jsonArray) {
    showToast(jsonArray.toString());
    finish();
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

