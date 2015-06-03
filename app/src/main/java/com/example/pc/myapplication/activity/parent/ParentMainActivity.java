package com.example.pc.myapplication.activity.parent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.ViewStyle.ChildSeedInfoView;
import com.example.pc.myapplication.ViewStyle.CircularImage;
import com.example.pc.myapplication.activity.MainActivity;
import com.example.pc.myapplication.adapter.ParentViewPagerAdapter;
import com.example.pc.myapplication.fragment.parent.ParentDonateInfoFragment;
import com.example.pc.myapplication.fragment.parent.ParentSendWishFragment;
import com.example.pc.myapplication.fragment.parent.ParentWishListFragment;
import com.example.pc.myapplication.utils.HttpService;
import com.example.pc.myapplication.utils.RequestQueueController;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.viewpagerindicator.UnderlinePageIndicator;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ParentMainActivity extends FragmentActivity implements
        ParentWishListFragment.OnBabyFragmentInteractionListener,
        ParentSendWishFragment.OnMsgFragmentInteractionListener,
        ParentDonateInfoFragment.OnDynamicFragmentInteractionListener,
        HttpService.OnUpLoadImageRequestResponseListener {

  //记录连续按两次退出
  private long exitTime;

  //SharedPreferences
  private SharedPreferences preferences;

  //侧滑控件
  private ViewPager viewPager;
  //ViewPager适配器
  private ParentViewPagerAdapter adapter;

  //viewpager指示器
  private UnderlinePageIndicator viewPagerIndicator;

  //布局的Header
  private RelativeLayout relativeLayoutHeader;
  //菜单栏按钮
  private ImageView imageViewMenu;
  //头像框
  private CircularImage circularImage;
  //记录签到的当前时间
  private String nowTime;
  //每日签到按钮
  private ImageView imageViewEverydayTask;
  //存储用户更换的头像
  private Bitmap userImage;
  //ImageLoader
  private ImageLoader imageLoader;
  //用于更改头像
  private com.nostra13.universalimageloader.core.ImageLoader loader;

  //存放ViewPager上显示的fragment
  private List<Fragment> fragmentList = new ArrayList<Fragment>();

  //fab相关的布局
  private RapidFloatingActionLayout rapidFloatingActionLayout;
  private RapidFloatingActionButton rapidFloatingActionButton;
  private RapidFloatingActionHelper rapidFloatingActionHelper;

  //用户头像的高度
  private static final int USERIMAGE_HEIGHT = 35;
  //用户头像的宽度
  private static final int USERIMAGE_WIDTH = 35;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_parent_main);
    exitTime = 0;
    Time t = new Time();
    t.setToNow();
    int lastmonth = t.month + 1;
    nowTime = t.year + "年" + lastmonth + "月" + t.monthDay + "日";
    preferences = getSharedPreferences(AppConstant.PREFERENCE_NAME, 0);
    loader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
    loader.init(ImageLoaderConfiguration.createDefault(ParentMainActivity.this));
    imageLoader = new ImageLoader(RequestQueueController.get().getRequestQueue(), new ImageLoader.ImageCache() {
      @Override
      public Bitmap getBitmap(String s) {
        return null;
      }
      @Override
      public void putBitmap(String s, Bitmap bitmap) {
      }
    });
    initView();
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
  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    super.onActivityResult(requestCode, resultCode, intent);
    if (RESULT_OK == resultCode) {
      switch (requestCode) {
        case AppConstant.SELECT_PIC:
        case AppConstant.SELECT_PIC_KITKAT:
          ImageSize targetSize = new ImageSize(USERIMAGE_WIDTH, USERIMAGE_HEIGHT);
          loader.loadImage(intent.getData().toString(), targetSize, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
              userImage = loadedImage;
              HttpService.DoUpLoadImageRequest(
                      userImage, preferences.getString(AppConstant.FROM_USERID, ""), ParentMainActivity.this);
            }
          });
          break;
        case AppConstant.CAMERA_RESULTCODE:
          Bundle bundle = intent.getExtras();
          userImage = (Bitmap) bundle.get(AppConstant.CAMERA_DATA);
          HttpService.DoUpLoadImageRequest(userImage, preferences.getString(AppConstant.FROM_USERID, ""), ParentMainActivity.this);
          break;
      }
    }
  }

  private void initView() {
    viewPagerIndicator = (UnderlinePageIndicator) findViewById(R.id.parent_mainactivity_indicator);
    viewPager = (ViewPager) findViewById(R.id.parent_mainactivity_viewpager);
    relativeLayoutHeader = ((RelativeLayout) findViewById(R.id.parent_mainactivity_header));
    imageViewMenu= ((ImageView) relativeLayoutHeader.findViewById(R.id.parent_mainactivity_header_imageview_menu));
    circularImage = ((CircularImage) relativeLayoutHeader.findViewById(R.id.parent_mainactivity_header_circularimage));
    imageViewEverydayTask= ((ImageView) relativeLayoutHeader.findViewById(R.id.parent_mainactivity_imageview_everydaytask));
    adapter = new ParentViewPagerAdapter(ParentMainActivity.this, getSupportFragmentManager(), fragmentList);

    ChildSeedInfoView childSeedInfoView = new ChildSeedInfoView(ParentMainActivity.this);
    rapidFloatingActionButton = ((RapidFloatingActionButton) findViewById(R.id.child_mainactivity_rapidfloatingactionbutton));
    rapidFloatingActionLayout = ((RapidFloatingActionLayout) findViewById(R.id.child_mainactivity_rapidfloatingactionlayout));
    rapidFloatingActionHelper = new RapidFloatingActionHelper(ParentMainActivity.this, rapidFloatingActionLayout, rapidFloatingActionButton, childSeedInfoView).build();

    rapidFloatingActionLayout.setIsContentAboveLayout(false);
    rapidFloatingActionLayout.setDisableContentDefaultAnimation(true);

    ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(circularImage, R.mipmap.child_funcfragment_setting, R.mipmap.ic_launcher);
    imageLoader.get(preferences.getString(AppConstant.IMG_URL, ""), imageListener);
    fragmentList.add(ParentSendWishFragment.newInstance());
    fragmentList.add(ParentWishListFragment.newInstance());
    fragmentList.add(ParentDonateInfoFragment.newInstance());
    viewPager.setAdapter(adapter);
    viewPagerIndicator.setViewPager(viewPager, 0);
    viewPagerIndicator.setSelectedColor(getResources().getColor(R.color.skyblue));
    if (nowTime.equals(preferences.getString(AppConstant.EVERYDAY_TASK, ""))) {
      imageViewEverydayTask.setImageResource(R.mipmap.parentactivityeveryday_task_done);
    } else imageViewEverydayTask.setImageResource(R.mipmap.parentactivityeveryday_task_normal);

    imageViewMenu.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //实际是菜单,临时用作登出
        preferences.edit().putInt(AppConstant.USER_MODE, 0).apply();
        startActivity(new Intent(ParentMainActivity.this, MainActivity.class));
        finish();
      }
    });

    circularImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final Activity activity = ParentMainActivity.this;
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View viewAddEmplyee = layoutInflater.inflate(R.layout.layout_signup_imagechooser, null);
        new AlertDialog.Builder(activity).setTitle("更 换 头 像").setView(viewAddEmplyee).show();
        viewAddEmplyee.findViewById(R.id.signup_imagechooser_textview_shot).setOnClickListener(
                new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, AppConstant.CAMERA_RESULTCODE);
                  }
                }
        );
        viewAddEmplyee.findViewById(R.id.signup_imagechooser_textview_album).setOnClickListener(
                new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    //打开手机自带的图库，选择图片后将URI返回到onActivityResult
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT)
                      startActivityForResult(intent, AppConstant.SELECT_PIC_KITKAT);
                    else startActivityForResult(intent, AppConstant.SELECT_PIC);
                  }
                }
        );
      }
    });

    imageViewEverydayTask.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String everyDayTaskDoneTime = preferences.getString(AppConstant.EVERYDAY_TASK, "");
        if (!nowTime.equals(everyDayTaskDoneTime)) {
          imageViewEverydayTask.setImageResource(R.mipmap.parentactivityeveryday_task_done);
          Toast.makeText(getApplicationContext(), AppConstant.EVERYDAY_TASK_SUCCESS, Toast.LENGTH_SHORT).show();
          SharedPreferences.Editor editor = preferences.edit();
          editor.putBoolean(AppConstant.EVERYDAY_TASK, true).apply();
          editor.putString(AppConstant.EVERYDAY_TASK, nowTime).apply();
        } else
          showToast(AppConstant.EVERYDAY_TASK_FAILD);
      }
    });
  }

  @Override
  public void OnUpLoadImageSuccessResponse(JSONArray jsonArray) {
    JSONObject codeObject;
    JSONObject msgObject;
    try {
      codeObject = (JSONObject) jsonArray.get(0);
      msgObject = (JSONObject) jsonArray.get(1);
      if (null != codeObject) {
        if (AppConstant.UPLOAD_USER_IMAGE_SUCCESS == codeObject.getInt(AppConstant.RETURN_CODE)) {
          circularImage.setImageBitmap(userImage);

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
  public void OnUpLoadImageErrorResponse(String errorResult) {
    showToast(errorResult);
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
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
