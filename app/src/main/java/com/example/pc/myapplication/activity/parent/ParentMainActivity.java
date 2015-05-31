package com.example.pc.myapplication.activity.parent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.format.Time;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.TaskInfo.DiyTaskInfo;
import com.example.pc.myapplication.adapter.ParentViewPagerAdapter;
import com.example.pc.myapplication.fragment.parent.ParentBabyFragment;
import com.example.pc.myapplication.fragment.parent.ParentDynamicFragment;
import com.example.pc.myapplication.fragment.parent.ParentMsgFragment;
import com.example.pc.myapplication.utils.HttpService;
import com.example.pc.myapplication.utils.RequestQueueController;
import com.nineoldandroids.view.ViewHelper;
import com.viewpagerindicator.UnderlinePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

  //侧滑控件
  private ViewPager viewPager;

  //viewpager指示器
  private UnderlinePageIndicator viewPagerIndicator;

  //viewpager的父控件
  private RelativeLayout relativeLayoutViewPagerParent;

  //记录签到的当前时间
  private String nowTime;

  //显示钱的控件
  private TextView textViewMoney;

  //打开侧滑菜单按钮
  private ImageButton imageButtonOpenLeftMenu;

  //每日签到按钮
  private ImageButton imageButtonEverydayTask;

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
  public void onDynamicFragmentInteraction() {
  }

  @Override
  public void onBabyFragmentInteraction() {

  }

  @Override
  public void onMsgFragmentInteraction(JSONArray jsonArray) {
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
          diyTaskInfo = data.getParcelableExtra(AppConstant.NEW_TASK);
          addNewTask(diyTaskInfo);
        }
        break;

      case AppConstant.PARENT_ADDSYSTEMTASK_RESULTCODE:
        if (data != null) {
        }
        break;
    }
    super.onActivityResult(requestCode, resultCode, data);
  }

  public void OpenRightMenu(){
    mDrawerLayout.openDrawer(Gravity.START);
    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.START);
  }

  private void initEvents(){
    mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
      @Override
      public void onDrawerStateChanged(int newState) { }

      @Override
      public void onDrawerSlide(View drawerView, float slideOffset)
      {
        View mContent = mDrawerLayout.getChildAt(0);
        float scale = 1 - slideOffset;
        float rightScale = 0.8f + scale * 0.2f;
        if (drawerView.getTag().equals("LEFT")) {
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
          ViewHelper.setTranslationX(mContent, -drawerView.getMeasuredWidth() * slideOffset);
          ViewHelper.setPivotX(mContent, mContent.getMeasuredWidth());
          ViewHelper.setPivotY(mContent, mContent.getMeasuredHeight() / 2);
          ViewHelper.setScaleX(mContent, rightScale);
          ViewHelper.setScaleY(mContent, rightScale);
        }
      }
      @Override
      public void onDrawerOpened(View drawerView) { }
      @Override
      public void onDrawerClosed(View drawerView) { }
    });
  }

  private void initView() {
    mDrawerLayout = (DrawerLayout) findViewById(R.id.parent_mainactivity_drawerlayout_root);
    viewPagerIndicator = (UnderlinePageIndicator) findViewById(R.id.parent_mainactivity_indicator);
    textViewMoney = (TextView) findViewById(R.id.parentactivity_leftmenu_textview_money);
    imageButtonOpenLeftMenu = (ImageButton) findViewById(R.id.parent_mainactivity_relativelayout_actionbar_openleftmenu);
    imageButtonEverydayTask = (ImageButton) findViewById(R.id.parent_mainactivity_relativelayout_actionbar_everydaytask);
    viewPager = (ViewPager) findViewById(R.id.parentactivity_viewpager);
    relativeLayoutViewPagerParent = (RelativeLayout) findViewById(R.id.parent_mainactivity_relativelayout_viewpager);
    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);


    mDrawerLayout.setBackground(new BitmapDrawable(AppConstant.readBitMap(getApplicationContext(), R.mipmap.bg_slight_rain_night)));
//    findViewById(R.id.parent_mainactivity_drawerlayout_root).setBackgroundColor(getResources().getColor(R.color.beige));
//    mDrawerLayout.setBackgroundColor(getResources().getColor(R.color.material_blue_grey_800));

    textViewMoney.setText(String.valueOf(preferences.getInt(AppConstant.LEFT_MONEY, 0)));
    if (nowTime.equals(preferences.getString(AppConstant.EVERYDAY_TASK, ""))) {
      imageButtonEverydayTask.setImageResource(R.mipmap.parentactivityeveryday_task_done);
    } else imageButtonEverydayTask.setImageResource(R.mipmap.parentactivityeveryday_task_normal);
    imageButtonOpenLeftMenu.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                OpenRightMenu();
              }
            }
    );
    imageButtonEverydayTask.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                String everyDayTaskDoneTime = preferences.getString(AppConstant.EVERYDAY_TASK,"");
                if (!nowTime.equals(everyDayTaskDoneTime)) {
                  imageButtonEverydayTask.setImageResource(R.mipmap.parentactivityeveryday_task_done);
                  Toast.makeText(getApplicationContext(), AppConstant.EVERYDAY_TASK_SUCCESS, Toast.LENGTH_SHORT).show();
                  textViewMoney.setText(String.valueOf(Integer.valueOf(textViewMoney.getText().toString()) + 50));
                  SharedPreferences.Editor editor = preferences.edit();
                  editor.putBoolean(AppConstant.EVERYDAY_TASK, true).apply();
                  editor.putInt(AppConstant.LEFT_MONEY, Integer.valueOf(textViewMoney.getText().toString())).apply();
                  editor.putString(AppConstant.EVERYDAY_TASK, nowTime).apply();
                } else Toast.makeText(getApplicationContext(), AppConstant.EVERYDAY_TASK_FAILD, Toast.LENGTH_SHORT).show();
              }
            }
    );
    fragmentList.add(ParentMsgFragment.newInstance());
    fragmentList.add(ParentBabyFragment.newInstance());
    fragmentList.add(ParentDynamicFragment.newInstance());
    viewPager.setAdapter(new ParentViewPagerAdapter(ParentMainActivity.this, getSupportFragmentManager(), fragmentList));
    viewPagerIndicator.setViewPager(viewPager, 0);
  }

  /**
   * 向ParentMsgFragment中添加新的任务
   * @param newTask
   */
  private void addNewTask(DiyTaskInfo newTask) {
    HashMap<String, String> map = new HashMap<>();
    map.put(AppConstant.FROM_USERID, from_userid);
    map.put(AppConstant.TASK_CONTENT, newTask.getTaskContent());
    map.put(AppConstant.TO_USERID, newTask.getToUserId());
    map.put(AppConstant.AWARD, newTask.getAward());
    HttpService.DoSetDiyTaskRequest(Request.Method.POST, AppConstant.SET_DIY_TASK_URL, map, ParentMainActivity.this);
  }

  @Override
  public void OnSetDiyTaskSuccessResponse(JSONArray jsonArray) {
    JSONObject codeObject;
    JSONObject msgObject;
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
      if (null != msgObject) showToast(msgObject.getString(AppConstant.RETURN_MSG));
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void OnSetDiyTaskErrorResponse(String errorResult) {
    showToast(errorResult);
  }

  private void showToast(String string) {
    Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
  }
}
