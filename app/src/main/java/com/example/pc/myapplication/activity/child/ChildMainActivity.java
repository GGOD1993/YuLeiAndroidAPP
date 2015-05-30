package com.example.pc.myapplication.activity.child;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.ViewStyle.CircularImage;
import com.example.pc.myapplication.adapter.ChildViewpagerAdapter;
import com.example.pc.myapplication.fragment.child.ChildFuncFragment;
import com.example.pc.myapplication.fragment.child.ChildMsgFragment;
import com.example.pc.myapplication.fragment.child.ChildWishFragment;
import com.example.pc.myapplication.utils.RequestQueueController;
import com.viewpagerindicator.UnderlinePageIndicator;

import java.util.ArrayList;

public class ChildMainActivity extends FragmentActivity
        implements ChildWishFragment.onChildWishFragmentInteractionListener,
        ChildMsgFragment.OnChildMsgFragmentInteractionListener,
        ChildFuncFragment.OnChildFuncFragmentInteractionListener{

  //再按一次返回桌面
  private Long exitTime;

  //ImageLoader
  private ImageLoader imageLoader;

  //SharedPreferences
  private SharedPreferences preferences;

  //viewpager
  private ViewPager viewPager;

  //viewpager适配器
  private ChildViewpagerAdapter mAdapter;

  //viewpager指示器
  private UnderlinePageIndicator viewPagerIndicator;

  //根布局
  private RelativeLayout relativeLayoutRoot;

  //布局的Header
  private RelativeLayout relativeLayoutHeader;
  //Header上的CircularImage
  private CircularImage imageViewHeader;
  //Header上的TextView
  private TextView textViewHeader;

  //fragment列表
  private ArrayList<Fragment> fragmentList;

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
    initViews();
  }

  private void initViews() {
    viewPager = (ViewPager) findViewById(R.id.child_mainactivity_viewpager);
    viewPagerIndicator = (UnderlinePageIndicator) findViewById(R.id.child_mainactivity_indicator);
    relativeLayoutRoot = (RelativeLayout) findViewById(R.id.child_mainactivity_relativelayout_root);
    relativeLayoutHeader = (RelativeLayout) findViewById(R.id.child_mainactivity_header);
    textViewHeader = (TextView) relativeLayoutHeader.findViewById(R.id.child_mainactivity_header_textview);
    imageViewHeader = (CircularImage) relativeLayoutHeader.findViewById(R.id.child_mainactivity_header_circularimage);
    fragmentList = new ArrayList<>();
    imageLoader = new ImageLoader(RequestQueueController.get().getRequestQueue(), new ImageLoader.ImageCache() {
      @Override
      public Bitmap getBitmap(String s) {
        return null;
      }
      @Override
      public void putBitmap(String s, Bitmap bitmap) {
      }
    });

    ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(imageViewHeader, R.mipmap.child_funcfragment_setting, R.mipmap.ic_launcher);
    imageLoader.get(preferences.getString(AppConstant.IMG_URL, ""), imageListener);
    fragmentList.add(ChildWishFragment.newInstance());
    fragmentList.add(ChildMsgFragment.newInstance());
    fragmentList.add(ChildFuncFragment.newInstance(preferences));
    mAdapter = new ChildViewpagerAdapter(getSupportFragmentManager(), ChildMainActivity.this, fragmentList);
    viewPager.setAdapter(mAdapter);
    viewPagerIndicator.setViewPager(viewPager, 0);
    viewPagerIndicator.setSelectedColor(getResources().getColor(R.color.indianred));
    viewPagerIndicator.setFadingEdgeLength(2);
    relativeLayoutRoot.setBackground(new BitmapDrawable(AppConstant.readBitMap(getApplicationContext(), R.mipmap.child_mainactivity_background)));
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

  private void showToast(String string) {
    Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
  }
}
