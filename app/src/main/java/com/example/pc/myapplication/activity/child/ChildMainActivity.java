package com.example.pc.myapplication.activity.child;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.adapter.ChildViewpagerAdapter;
import com.example.pc.myapplication.fragment.child.ChildFuncFragment;
import com.example.pc.myapplication.fragment.child.ChildMsgFragment;
import com.example.pc.myapplication.fragment.child.ChildWishFragment;
import com.viewpagerindicator.UnderlinePageIndicator;

import java.util.ArrayList;


public class ChildMainActivity extends FragmentActivity
        implements ChildWishFragment.onChildWishFragmentInteractionListener,
        ChildMsgFragment.OnChildMsgFragmentInteractionListener,
        ChildFuncFragment.OnChildFuncFragmentInteractionListener{

  //再按一次返回桌面
  private Long exitTime;

  //viewpager
  private ViewPager child_mainactivity_viewpager;

  //viewpager适配器
  private ChildViewpagerAdapter mAdapter;

  //viewpager指示器
  private UnderlinePageIndicator child_mainactivity_indicator;

  //根布局
  private RelativeLayout child_mainactivity_relativelayout_root;

  //fragment列表
  private ArrayList<Fragment> fragmentList;

  //sharedpreference
  private SharedPreferences preferences;

  @Override
  public void onChildMsgFragmentInteraction() {

  }

  @Override
  public void onChildWishFragmentInteraction() {

  }

  @Override
  public void onChildFuncFragmentInteraction() {

  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_child_main);

    exitTime = 0L;
    preferences = getSharedPreferences(AppConstant.PREFERENCE_NAME, 0);

    fragmentList = new ArrayList<>();
    fragmentList.add(ChildMsgFragment.newInstance());
    fragmentList.add(ChildWishFragment.newInstance());
    fragmentList.add(ChildFuncFragment.newInstance(preferences));

    child_mainactivity_viewpager = (ViewPager) findViewById(R.id.child_mainactivity_viewpager);
    child_mainactivity_indicator = (UnderlinePageIndicator) findViewById(R.id.child_mainactivity_indicator);

    mAdapter = new ChildViewpagerAdapter(getSupportFragmentManager(), ChildMainActivity.this, fragmentList);
    child_mainactivity_viewpager.setAdapter(mAdapter);
    child_mainactivity_indicator.setViewPager(child_mainactivity_viewpager, 0);

    child_mainactivity_relativelayout_root = (RelativeLayout) findViewById(R.id.child_mainactivity_relativelayout_root);
    child_mainactivity_relativelayout_root.setBackground(
            new BitmapDrawable(AppConstant.readBitMap(getApplicationContext(), R.mipmap.child_mainactivity_background)));

  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (KeyEvent.KEYCODE_BACK == keyCode
            && event.getAction() == KeyEvent.ACTION_DOWN) {
      if ((System.currentTimeMillis() - exitTime) > 2000) {
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
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_child_main, menu);
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

  private void showToast(String string) {
    Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
  }
}
