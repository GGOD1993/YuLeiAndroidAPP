package com.example.pc.myapplication.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.ViewStyle.CircularImage;
import com.example.pc.myapplication.utils.HttpService;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SignupActivity extends ActionBarActivity
        implements HttpService.OnSignupRequestResponseListener,
        HttpService.OnUpLoadImageRequestResponseListener {

  //上下文引用
  private Context context;

  //图像加载库
  private ImageLoader imageLoader = null;

  //提交按钮
  private Button buttonSubmmit;

  //取消按钮
  private Button buttonCancel;

  //用户名输入框
  private EditText editTextUsername;

  //密码输入框
  private EditText editTextPassword;

  //密码确认框
  private EditText editTextPasswordMakesure;

  //昵称输入框
  private EditText editTextNickname;

  //邮箱输入框
  private EditText editTextEmail;

  //性别选择框
  private RadioButton radioButtonMale;

  //性别选择框
  private RadioButton radioButtonFemale;

  //头像选择框
  private CircularImage circularImageUserImage;

  //布局的父控件
  private RelativeLayout relativeLayoutRoot;

  //用户头像的高度
  private static final int USERIMAGE_HEIGHT = 70;

  //用户头像的宽度
  private static final int USERIMAGE_WIDTH = 70;

  //是否可以结束该Activity
  private boolean isAbleFinish;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_signup);
    context = getApplicationContext();
    imageLoader = ImageLoader.getInstance();
    isAbleFinish = false;
    initViews();
  }

  /**
   * 初始化布局
   */
  private void initViews() {
    //这句话一定不要掉了
    imageLoader.init(ImageLoaderConfiguration.createDefault(SignupActivity.this));
    editTextUsername = (EditText) findViewById(R.id.signupactivity_edittext_username);
    editTextPassword = (EditText) findViewById(R.id.signupactivity_edittext_password);
    editTextPasswordMakesure = (EditText) findViewById(R.id.signupactivity_edittext_password_makesure);
    editTextNickname = (EditText) findViewById(R.id.signupactivity_edittext_nickname);
    editTextEmail = (EditText) findViewById(R.id.signupactivity_edittext_email);
    radioButtonMale = (RadioButton) findViewById(R.id.signupactivity_radiobutton_male);
    radioButtonFemale = (RadioButton) findViewById(R.id.signupactivity_radiobutton_female);
    buttonSubmmit = (Button) findViewById(R.id.signupactivity_button_submmit);
    buttonCancel = (Button) findViewById(R.id.signupactivity_button_cancel);
    relativeLayoutRoot = (RelativeLayout) findViewById(R.id.signupactivity_relativelayout_1);
    circularImageUserImage = (CircularImage) findViewById(R.id.signupactivity_circularimage_userimage);
    circularImageUserImage.setImageResource(R.mipmap.bg_imagebutton_parent_access);
    circularImageUserImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        LayoutInflater layoutInflater = LayoutInflater.from(SignupActivity.this);
        View viewAddEmplyee = layoutInflater.inflate(R.layout.layout_signup_imagechooser, null);
        new AlertDialog.Builder(SignupActivity.this).setTitle("选 择 头 像").setView(viewAddEmplyee).show();
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
                    /**
                     * 打开手机自带的图库，选择图片后将URI返回到onActivityResult
                     */
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/jpeg");
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT)
                      startActivityForResult(intent, AppConstant.SELECT_PIC_KITKAT);
                    else startActivityForResult(intent, AppConstant.SELECT_PIC);
                  }
                }
        );
      }
    });
    relativeLayoutRoot.setBackground(new BitmapDrawable(AppConstant.readBitMap(context, R.mipmap.bg_login)));
    buttonSubmmit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dealWithUserInfo();
      }
    });
    buttonCancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
  }

  /**
   * 获取到图片的URI后裁剪设置为头像
   *
   * @param requestCode
   * @param resultCode
   * @param intent
   */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    super.onActivityResult(requestCode, resultCode, intent);
    if (RESULT_OK == resultCode) {
      switch (requestCode) {
        case AppConstant.SELECT_PIC:
        case AppConstant.SELECT_PIC_KITKAT:
          ImageSize targetSize = new ImageSize(USERIMAGE_WIDTH, USERIMAGE_HEIGHT);
          imageLoader.loadImage(intent.getData().toString(), targetSize, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
              circularImageUserImage.setImageBitmap(loadedImage);
            }
          });
          break;

        case AppConstant.CAMERA_RESULTCODE:
          Bundle bundle = intent.getExtras();
          Bitmap bitmap = (Bitmap) bundle.get(AppConstant.CAMERA_DATA);
          circularImageUserImage.setImageBitmap(bitmap);
          break;
      }
    }
  }

  private void dealWithUserInfo() {
    int gender;
    if (editTextUsername.getText().length() != 0) {
      if (editTextNickname.getText().length() != 0) {
        if (editTextEmail.getText().length() != 0) {
          if (editTextPassword.getText().length() != 0
                  && editTextPasswordMakesure.getText().length() != 0) {
            if (editTextPassword.getText().toString().
                    equals(editTextPasswordMakesure.getText().toString())) {
              if (radioButtonMale.isChecked()) gender = AppConstant.GENDER_MALE;
              else gender = AppConstant.GENDER_FEMALE;
              HashMap<String, String> map = new HashMap<>();
              map.put(AppConstant.USERNAME, editTextUsername.getText().toString());
              map.put(AppConstant.PASSWORD, editTextPassword.getText().toString());
              map.put(AppConstant.NICKNAME, editTextNickname.getText().toString());
              map.put(AppConstant.EMAIL, editTextEmail.getText().toString());
              map.put(AppConstant.GENDER, String.valueOf(gender));
              HttpService.DoSignupRequest(map, SignupActivity.this);
              circularImageUserImage.setDrawingCacheEnabled(true);
              HttpService.DoUpLoadImageRequest(circularImageUserImage.getDrawingCache(true), editTextUsername.getText().toString(), SignupActivity.this);
              circularImageUserImage.setDrawingCacheEnabled(false);
            } else {
              showToast("两次密码输入不相同");
            }
          } else {
            showToast("请输入正确的密码");
          }
        } else {
          showToast("请输入正确的邮箱");
        }
      } else {
        showToast("请输入正确的昵称");
      }
    } else {
      showToast("请输入正确的用户名");
    }
  }

  @Override
  public void OnSignupSuccessResponse(JSONArray jsonArray) {
    JSONObject codeObject;
    JSONObject msgObject;
    try {
      codeObject = (JSONObject) jsonArray.get(0);
      msgObject = (JSONObject) jsonArray.get(1);
      if (null != codeObject) {
        if (AppConstant.NEW_USER_SUCCESS== codeObject.getInt(AppConstant.RETURN_CODE)) {
          if (isAbleFinish) finish();
          else isAbleFinish = true;
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
  public void OnSignupErrorResponse(String errorResult) {
    showToast(errorResult);
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
          if (isAbleFinish) finish();
          else isAbleFinish = true;
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
    Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
  }
}
