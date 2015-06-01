package com.example.pc.myapplication.activity.child;

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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.ImageLoader;
import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.ViewStyle.CircularImage;
import com.example.pc.myapplication.adapter.ChildViewpagerAdapter;
import com.example.pc.myapplication.fragment.child.ChildDonateFragment;
import com.example.pc.myapplication.fragment.child.ChildHistoryFragment;
import com.example.pc.myapplication.fragment.child.ChildTaskFragment;
import com.example.pc.myapplication.utils.HttpService;
import com.example.pc.myapplication.utils.RequestQueueController;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.viewpagerindicator.UnderlinePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChildMainActivity extends FragmentActivity
        implements ChildTaskFragment.onChildTaskFragmentInteractionListener,
        ChildHistoryFragment.OnChildHistoryFragmentInteractionListener,
        ChildDonateFragment.OnChildFuncFragmentInteractionListener,
        HttpService.OnUpLoadImageRequestResponseListener {

  //再按一次返回桌面
  private Long exitTime;

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
  //Header上的TextView
  private TextView textViewHeader;
  //Header上的CircularImage
  private CircularImage imageViewHeader;
  //存储用户更换的头像
  private Bitmap userImage;
  //ImageLoader
  private ImageLoader imageLoader;
  //用于更改头像
  private com.nostra13.universalimageloader.core.ImageLoader loader;

  //fragment列表
  private ArrayList<Fragment> fragmentList;


  //用户头像的高度
  private static final int USERIMAGE_HEIGHT = 70;

  //用户头像的宽度
  private static final int USERIMAGE_WIDTH = 70;

  @Override
  public void onChildHistoryFragmentInteraction() {
  }

  @Override
  public void onChildTaskFragmentInteraction() {
  }

  @Override
  public void onChildFuncFragmentInteraction() {
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_child_main);
    exitTime = 0L;
    fragmentList = new ArrayList<>();
    preferences = getSharedPreferences(AppConstant.PREFERENCE_NAME, 0);
    loader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
    loader.init(ImageLoaderConfiguration.createDefault(ChildMainActivity.this));
    imageLoader = new ImageLoader(RequestQueueController.get().getRequestQueue(), new ImageLoader.ImageCache() {
      @Override
      public Bitmap getBitmap(String s) {
        return null;
      }
      @Override
      public void putBitmap(String s, Bitmap bitmap) {
      }
    });
    initViews();
  }

  private void initViews() {
    viewPager = (ViewPager) findViewById(R.id.child_mainactivity_viewpager);
    viewPagerIndicator = (UnderlinePageIndicator) findViewById(R.id.child_mainactivity_indicator);
    relativeLayoutRoot = (RelativeLayout) findViewById(R.id.child_mainactivity_relativelayout_root);
    relativeLayoutHeader = (RelativeLayout) findViewById(R.id.child_mainactivity_header);
    textViewHeader = (TextView) relativeLayoutHeader.findViewById(R.id.child_mainactivity_header_textview);
    imageViewHeader = (CircularImage) relativeLayoutHeader.findViewById(R.id.child_mainactivity_header_circularimage);

    ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(imageViewHeader, R.mipmap.child_funcfragment_setting, R.mipmap.ic_launcher);
    imageLoader.get(preferences.getString(AppConstant.IMG_URL, ""), imageListener);
    fragmentList.add(ChildTaskFragment.newInstance());
    fragmentList.add(ChildHistoryFragment.newInstance());
    fragmentList.add(ChildDonateFragment.newInstance(preferences));
    mAdapter = new ChildViewpagerAdapter(getSupportFragmentManager(), ChildMainActivity.this, fragmentList);
    viewPager.setAdapter(mAdapter);
    viewPagerIndicator.setViewPager(viewPager, 0);
    viewPagerIndicator.setSelectedColor(getResources().getColor(R.color.skyblue));
    viewPagerIndicator.setFadingEdgeLength(2);

    imageViewHeader.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final Activity activity = ChildMainActivity.this;
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
              HttpService.DoUpLoadImageRequest(Request.Method.POST, AppConstant.UPLOAD_USER_IMAGE,
                      userImage, preferences.getString(AppConstant.FROM_USERID, ""), ChildMainActivity.this);
            }
          });
          break;

        case AppConstant.CAMERA_RESULTCODE:
          Bundle bundle = intent.getExtras();
          userImage = (Bitmap) bundle.get(AppConstant.CAMERA_DATA);
          HttpService.DoUpLoadImageRequest(Request.Method.POST, AppConstant.UPLOAD_USER_IMAGE,
                  userImage, preferences.getString(AppConstant.FROM_USERID, ""), ChildMainActivity.this);
          break;
      }
    }
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
  public void OnUpLoadImageSuccessResponse(JSONArray jsonArray) {
    JSONObject codeObject;
    JSONObject msgObject;
    try {
      codeObject = (JSONObject) jsonArray.get(0);
      msgObject = (JSONObject) jsonArray.get(1);
      if (null != codeObject) {
        if (AppConstant.UPLOAD_USER_IMAGE_SUCCESS == codeObject.getInt(AppConstant.RETURN_CODE)) {
          imageViewHeader.setImageBitmap(userImage);

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

  private void showToast(String string) {
    Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
  }
}
