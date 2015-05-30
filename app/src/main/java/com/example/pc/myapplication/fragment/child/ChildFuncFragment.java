package com.example.pc.myapplication.fragment.child;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.ImageLoader;
import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.ViewStyle.CircularImage;
import com.example.pc.myapplication.activity.child.ChildDiaryActivity;
import com.example.pc.myapplication.activity.child.ChildDonateActivity;
import com.example.pc.myapplication.activity.child.ChildSettingActivity;
import com.example.pc.myapplication.activity.child.ChildWishActivity;
import com.example.pc.myapplication.utils.HttpService;
import com.example.pc.myapplication.utils.RequestQueueController;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChildFuncFragment extends Fragment implements
        HttpService.OnUpLoadImageRequestResponseListener{

  //sharedPreference
  private SharedPreferences preferences;

  //头像框
  private CircularImage circularImageUserImage;

  //存储用户更换的头像
  private Bitmap userImage;

  //ImageLoader
  private ImageLoader imageLoader;

  //用于更改头像
  private com.nostra13.universalimageloader.core.ImageLoader loader;

  //用户名
  private TextView textViewUsername;

  //显示剩余金钱数
  private TextView textViewMoney;

  //心愿按钮
  private ImageButton imageButtonWish;

  //日记按钮
  private ImageButton imageButtonDiary;

  //捐赠按钮
  private ImageButton imageButtonDonate;

  //更多设置按钮
  private ImageButton imageButtonSetting;

  //回调的监听
  private OnChildFuncFragmentInteractionListener mListener;

  //用户头像的高度
  private static final int USERIMAGE_HEIGHT = 70;

  //用户头像的宽度
  private static final int USERIMAGE_WIDTH = 70;

  //Activity返回码
  public static final int RESULT_OK           = -1;

  public static ChildFuncFragment newInstance(SharedPreferences preferences) {
    ChildFuncFragment fragment = new ChildFuncFragment();
    fragment.preferences = preferences;
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  public ChildFuncFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_child_func, container, false);
    loader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
    loader.init(ImageLoaderConfiguration.createDefault(getActivity()));
    imageLoader = new ImageLoader(RequestQueueController.get().getRequestQueue(), new ImageLoader.ImageCache() {
      @Override
      public Bitmap getBitmap(String s) {
        return null;
      }
      @Override
      public void putBitmap(String s, Bitmap bitmap) {
      }
    });
    initView(view);
    return view;
  }

  private void initView(View w) {
    circularImageUserImage = (CircularImage) w.findViewById(R.id.child_funcfragment_circularimage_userimage);
    textViewUsername = (TextView) w.findViewById(R.id.child_funcfragment_textview_username);
    textViewMoney = (TextView) w.findViewById(R.id.child_funcfragment_textview_money);
    imageButtonWish = (ImageButton) w.findViewById(R.id.child_funcfragment_imagebutton_wish);
    imageButtonDiary = (ImageButton) w.findViewById(R.id.child_funcfragment_imagebutton_diary);
    imageButtonDonate = (ImageButton) w.findViewById(R.id.child_funcfragment_imagebutton_donate);
    imageButtonSetting = (ImageButton) w.findViewById(R.id.child_funcfragment_imagebutton_setting);

    ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(circularImageUserImage, R.mipmap.child_funcfragment_setting, R.mipmap.ic_launcher);
    imageLoader.get(preferences.getString(AppConstant.IMG_URL, ""), imageListener);
    circularImageUserImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final Activity activity = getActivity();
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
//                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT)
                      startActivityForResult(intent, AppConstant.SELECT_PIC_KITKAT);
                    else startActivityForResult(intent, AppConstant.SELECT_PIC);
                  }
                }
        );
      }
    });

    textViewUsername.setText(preferences.getString(AppConstant.FROM_USERID, ""));
    textViewMoney.setText(String.valueOf(preferences.getInt(AppConstant.LEFT_MONEY, 0)));
    imageButtonWish.setBackground(
            new BitmapDrawable(AppConstant.readBitMap(getActivity(), R.mipmap.child_funcfragment_wish)));
    imageButtonWish.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getActivity(), ChildWishActivity.class);
        startActivity(intent);
      }
    });
    imageButtonDiary.setBackground(
            new BitmapDrawable(AppConstant.readBitMap(getActivity(), R.mipmap.child_funcfragment_diary)));
    imageButtonDiary.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getActivity(), ChildDiaryActivity.class);
        startActivity(intent);
      }
    });

    imageButtonDonate.setBackground(
            new BitmapDrawable(AppConstant.readBitMap(getActivity(), R.mipmap.child_funcfragment_donate)));
    imageButtonDonate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getActivity(), ChildDonateActivity.class);
        startActivity(intent);
      }
    });

    imageButtonSetting.setBackground(
            new BitmapDrawable(AppConstant.readBitMap(getActivity(), R.mipmap.child_funcfragment_setting)));
    imageButtonSetting.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getActivity(), ChildSettingActivity.class);
        startActivity(intent);
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
                      userImage, preferences.getString(AppConstant.FROM_USERID, ""), ChildFuncFragment.this);
            }
          });
          break;

        case AppConstant.CAMERA_RESULTCODE:
          Bundle bundle = intent.getExtras();
          userImage = (Bitmap) bundle.get(AppConstant.CAMERA_DATA);
          HttpService.DoUpLoadImageRequest(Request.Method.POST, AppConstant.UPLOAD_USER_IMAGE,
                  userImage, preferences.getString(AppConstant.FROM_USERID, ""), ChildFuncFragment.this);
          break;
      }
    }
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
          circularImageUserImage.setImageBitmap(userImage);

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
    Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      mListener = (OnChildFuncFragmentInteractionListener) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException(activity.toString()
              + " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    mListener = null;
    super.onDetach();
  }

  public interface OnChildFuncFragmentInteractionListener {
    public void onChildFuncFragmentInteraction();
  }
}
