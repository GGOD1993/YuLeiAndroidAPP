package com.example.pc.myapplication.activity.parent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.TaskInfo.DiyTaskInfo;
import com.example.pc.myapplication.adapter.ParentViewPagerAdapter;
import com.example.pc.myapplication.fragment.parent.ParentBabyFragment;
import com.example.pc.myapplication.fragment.parent.ParentDynamicFragment;
import com.example.pc.myapplication.fragment.parent.ParentMsgFragment;
import com.example.pc.myapplication.utils.HttpService;
import com.example.pc.myapplication.utils.RequestQueueController;
import com.example.pc.myapplication.utils.StringPostRequestPlus;
import com.nineoldandroids.view.ViewHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParentMainActivity extends FragmentActivity implements
        ParentBabyFragment.OnBabyFragmentInteractionListener,
        ParentMsgFragment.OnMsgFragmentInteractionListener,
        ParentDynamicFragment.OnDynamicFragmentInteractionListener,
        HttpService.OnSetDiyTaskRequestResponseListener{

  //记录连续按两次退出
  private long exitTime;

  //SharedPreferences
  private SharedPreferences preferences;

  //最底层的父控件
  private DrawerLayout mDrawerLayout;

  //抽屉右侧显示的内容
  private RelativeLayout parentactivity_relativelayout_root;

  private ViewPager viewPager;
  private PagerTabStrip pagerTabStrip;

  //剩余金币数量
  private String nowTime;
  private TextView parentactivity_leftmenu_textview_money;
  private ImageButton parentactivity_relativelayout_actionbar_openleftmenu;
  private ImageButton parentactivity_relativelayout_actionbar_everydaytask;

  //ViewPager的标签
  private List<String> titleList = new ArrayList<String>();

  //存放ViewPager上显示的fragment
  private List<Fragment> fragmentList = new ArrayList<Fragment>();

  //Volley框架的请求队列
  private RequestQueue mQueue;

  private String from_userid;

  //用来暂存当前的自定义任务
  private DiyTaskInfo diyTaskInfo;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_parent_main);

    exitTime = 0;
    preferences = getSharedPreferences(AppConstant.PREFERENCE_NAME,0);
    mQueue = RequestQueueController.get().getRequestQueue();

    from_userid = preferences.getString(AppConstant.FROM_USERID,"");

    Time t = new Time();
    t.setToNow();
    int lastmonth = t.month + 1 ;
    nowTime =  t.year + "年" + lastmonth + "月" + t.monthDay + "日";

    initView();
    initEvents();

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_parent_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onDynamicFragmentInteraction() {

  }

  @Override
  public void onBabyFragmentInteraction() {

  }

  @Override
  public void onMsgFragmentInteraction(JSONArray jsonArray) {

    ParentMsgFragment parentMsgFragment = (ParentMsgFragment) fragmentList.get(0);

    JSONObject arrayTask = null;
    DiyTaskInfo taskInfo = null;

    parentMsgFragment.taskList.clear();

    for (int i = 0 ; i < jsonArray.length() ; i ++) {

      try{
        arrayTask = (JSONObject) jsonArray.get(i);
        taskInfo = new DiyTaskInfo(
                arrayTask.getString("to_userid"),
                arrayTask.getString("taskId"),
                arrayTask.getString("regdate"),
                arrayTask.getString("content")
        );

        parentMsgFragment.taskList.add(taskInfo);
        parentMsgFragment.parentRecyclerViewAdapter.notifyDataSetChanged();

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
      if((System.currentTimeMillis()-exitTime) > 2000){
        showToast("亲~再点一次返回桌面");
        exitTime = System.currentTimeMillis();
      } else {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);
      }
      return true;
    }
    return super.onKeyDown(keyCode, event);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (resultCode) {
      case AppConstant.PARENT_ADDDIYTASK_RESULTCODE:

        if (data != null) {

          diyTaskInfo = (DiyTaskInfo) data.getSerializableExtra("newTask");
          addNewTask(diyTaskInfo);
        }
        break;

      case AppConstant.PARENT_ADDSYSTEMTASK_RESULTCODE:
        if (data != null) {
//                    家电什么
        }
        break;
    }

    super.onActivityResult(requestCode, resultCode, data);
  }

  public void OpenRightMenu(){
    mDrawerLayout.openDrawer(Gravity.START);
    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
            Gravity.START);
  }

  private void initEvents(){
    mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener()
    {
      @Override
      public void onDrawerStateChanged(int newState)
      {
      }

      @Override
      public void onDrawerSlide(View drawerView, float slideOffset)
      {
        View mContent = mDrawerLayout.getChildAt(0);
        float scale = 1 - slideOffset;
        float rightScale = 0.8f + scale * 0.2f;

        if (drawerView.getTag().equals("LEFT"))
        {

          float leftScale = 1 - 0.3f * scale;
          ViewHelper.setScaleX(drawerView, leftScale);
          ViewHelper.setScaleY(drawerView, leftScale);
          ViewHelper.setAlpha(drawerView, 0.6f + 0.4f * (1 - scale));
          ViewHelper.setTranslationX(mContent,
                  drawerView.getMeasuredWidth() * (1 - scale));
          ViewHelper.setPivotX(mContent, 0);
          ViewHelper.setPivotY(mContent,
                  mContent.getMeasuredHeight() / 2);
          ViewHelper.setScaleX(mContent, rightScale);
          ViewHelper.setScaleY(mContent, rightScale);

        } else {
          ViewHelper.setTranslationX(mContent,
                  -drawerView.getMeasuredWidth() * slideOffset);
          ViewHelper.setPivotX(mContent, mContent.getMeasuredWidth());
          ViewHelper.setPivotY(mContent,
                  mContent.getMeasuredHeight() / 2);

          ViewHelper.setScaleX(mContent, rightScale);
          ViewHelper.setScaleY(mContent, rightScale);
        }
      }

      @Override
      public void onDrawerOpened(View drawerView)
      {
      }

      @Override
      public void onDrawerClosed(View drawerView)
      {
      }
    });
  }

  private void initView() {
    mDrawerLayout = (DrawerLayout) findViewById(R.id.parentactivity_drawerlayout_root);
    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
            Gravity.RIGHT);
    mDrawerLayout.setBackground(new BitmapDrawable(AppConstant.readBitMap(getApplicationContext(), R.mipmap.bg_slight_rain_night)));
    parentactivity_relativelayout_root = (RelativeLayout) findViewById(R.id.parentactivity_relativelayout_root);
    parentactivity_relativelayout_root.setBackground(
            new BitmapDrawable(AppConstant.readBitMap(getApplicationContext(), R.mipmap.bg_slight_rain_day))
    );

    parentactivity_leftmenu_textview_money = (TextView) findViewById(R.id.parentactivity_leftmenu_textview_money);
    parentactivity_relativelayout_actionbar_openleftmenu = (ImageButton)
            findViewById(R.id.parentactivity_relativelayout_actionbar_openleftmenu);
    parentactivity_relativelayout_actionbar_everydaytask = (ImageButton)
            findViewById(R.id.parentactivity_relativelayout_actionbar_everydaytask);

    parentactivity_leftmenu_textview_money.setText(String.valueOf(preferences.getInt("leftmoney", 0)));

    if (nowTime.equals(preferences.getString("everytaskdonetime", ""))) {

      parentactivity_relativelayout_actionbar_everydaytask.
              setImageResource(R.mipmap.parentactivityeveryday_task_done);
    }
    else {
      parentactivity_relativelayout_actionbar_everydaytask.
              setImageResource(R.mipmap.parentactivityeveryday_task_normal);
    }

    parentactivity_relativelayout_actionbar_openleftmenu.setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                OpenRightMenu();
              }
            }
    );

    parentactivity_relativelayout_actionbar_everydaytask.setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                String everyDayTaskDoneTime = preferences.getString("everytaskdonetime","");

                if (!nowTime.equals(everyDayTaskDoneTime)) {

                  parentactivity_relativelayout_actionbar_everydaytask.
                          setImageResource(R.mipmap.parentactivityeveryday_task_done);
                  Toast.makeText(getApplicationContext(), "签到成功，奖励50金币O(∩_∩)O~~~", Toast.LENGTH_SHORT).show();

                  parentactivity_leftmenu_textview_money.setText(String.valueOf
                          (Integer.valueOf(parentactivity_leftmenu_textview_money.getText().toString()) + 50));

                  SharedPreferences.Editor editor = preferences.edit();
                  editor.putBoolean("everydaytaskdone", true).apply();
                  editor.putInt("leftmoney", Integer.valueOf(parentactivity_leftmenu_textview_money.getText().toString())).apply();
                  editor.putString("everytaskdonetime", nowTime).apply();
                }
                else {
                  Toast.makeText(getApplicationContext(), "您今天已经签到过咯~~~~(>_<)~~~~ ", Toast.LENGTH_SHORT).show();
                }
              }
            }
    );


    //初始化ViewPager以及标签
    viewPager = (ViewPager) findViewById(R.id.parentactivity_viewpager);
    pagerTabStrip = (PagerTabStrip) findViewById(R.id.parentactivity_pagertabstrip);

    fragmentList.add(ParentMsgFragment.newInstance(mQueue));
    fragmentList.add(ParentBabyFragment.newInstance());
    fragmentList.add(ParentDynamicFragment.newInstance());

    titleList.add("消息");
    titleList.add("宝贝");
    titleList.add("动态");

    pagerTabStrip.setTabIndicatorColorResource(R.color.deepskyblue);
    viewPager.setAdapter(new ParentViewPagerAdapter(ParentMainActivity.this, getSupportFragmentManager(),
            fragmentList, titleList));

  }

  /**
   * 向ParentMsgFragment中添加新的任务
   * @param newTask
   */
  private void addNewTask(DiyTaskInfo newTask) {

    HashMap<String, String> map = new HashMap<>();
    map.put(AppConstant.FROM_USERID, from_userid);
    map.put("content", newTask.getTaskContent());
    map.put("to_userid", newTask.getToUserId());
    map.put("award", newTask.getAward());

    HttpService.DoSetDiyTaskRequest(Request.Method.POST, AppConstant.SET_DIY_TASK_URL, map, ParentMainActivity.this);

  }

  @Override
  public void OnSetDiyTaskSuccessResponse(JSONArray jsonArray) {

    JSONObject codeObject =null;
    JSONObject msgObject = null;
    try{
      codeObject = (JSONObject) jsonArray.get(0);
      msgObject = (JSONObject) jsonArray.get(1);
      if (null != codeObject) {
        if (AppConstant.SET_DIY_TASK_SUCCESS == codeObject.getInt(AppConstant.RETURN_CODE)) {
          ParentMsgFragment parentMsgFragment = (ParentMsgFragment) fragmentList.get(0);
          parentMsgFragment.taskList.add(diyTaskInfo);
          parentMsgFragment.parentRecyclerViewAdapter.notifyDataSetChanged();
        }
      }
      if (null != msgObject) {
        showToast(msgObject.getString(AppConstant.RETURN_MSG));
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void OnSetDiyTaskErrorResponse(String errorResult) {
    showToast(errorResult);
    Log.e("error", errorResult);
  }

  private void showToast(String string) {
    Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
  }
}
