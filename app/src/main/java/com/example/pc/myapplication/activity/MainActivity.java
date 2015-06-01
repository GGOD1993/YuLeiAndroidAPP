package com.example.pc.myapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.ViewStyle.CircularImage;
import com.example.pc.myapplication.activity.child.ChildMainActivity;
import com.example.pc.myapplication.activity.parent.ParentMainActivity;
import com.example.pc.myapplication.utils.HttpService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 初始Activity
 */
public class MainActivity extends ActionBarActivity implements
        HttpService.OnLoginRequestResponseListener, View.OnClickListener {

  //再次点击返回桌面
  private long exitTime = 0;
  //家长或孩子的模式参数
  private int mode;

  //登录用户的Id
  public String fromUserId;

  //登录按钮
  private Button buttonSignIn;

  //注册按钮
  private Button buttonSignUp;

  //登录密码
  private EditText editTextPassword;

  //登录用户名
  private EditText editTextUsername;

  //头像框
  private CircularImage circularImage;

  //登录界面的根视图
  private RelativeLayout root;

  //家长端的入口
  private ImageButton imageButtonParentAccess;

  //孩子端的入口
  private ImageButton imageButtonChildAccess;

  //记住密码选项框
  private CheckBox checkBoxMemoryPassword;

  //自动登录选项框
  private CheckBox checkBoxAutoSignIn;

  //存储数据的SharedPreferences
  private SharedPreferences preferences;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.layout_signin);
    initActivity();
  }

  /**
   * 初始化登陆界面
   */
  private void initActivity() {
    preferences = getSharedPreferences(AppConstant.PREFERENCE_NAME, 0);
    buttonSignIn = (Button) findViewById(R.id.signin_button_signin);
    buttonSignUp = (Button) findViewById(R.id.signin_button_signup);
    editTextUsername = (EditText) findViewById(R.id.signin_edittext_username);
    editTextPassword = (EditText) findViewById(R.id.signin_edittext_password);
    checkBoxAutoSignIn = (CheckBox) findViewById(R.id.signin_checkbox_autosignin);
    checkBoxMemoryPassword = (CheckBox) findViewById(R.id.signin_checkbox_memorypassword);
    circularImage = (CircularImage) findViewById(R.id.signin_circularimage_userimage);
    root = (RelativeLayout) findViewById(R.id.signin_relativelayout_root);
    startInitAnim();
    root.setOnClickListener(this);
    circularImage.setImageResource(R.mipmap.ic_launcher);
    editTextUsername.setText(preferences.getString(AppConstant.AUTO_SIGNIN_USERNAME, ""));
    editTextPassword.setText(preferences.getString(AppConstant.AUTO_SIGNIN_PASSWORD, ""));
    if (preferences.getBoolean(AppConstant.AUTO_SIGNIN, false)) checkBoxAutoSignIn.setChecked(true);
    if (preferences.getBoolean(AppConstant.MEMORY_PASSWORD, false))
      checkBoxMemoryPassword.setChecked(true);
    /**
     * 登陆按钮
     */
    buttonSignIn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (checkBoxMemoryPassword.isChecked()) {
          preferences.edit().putString(AppConstant.AUTO_SIGNIN_USERNAME, editTextUsername.getText().toString()).apply();
          preferences.edit().putString(AppConstant.AUTO_SIGNIN_PASSWORD, editTextPassword.getText().toString()).apply();
        }
        dealWithUserInfo();
      }
    });

    /**
     * 注册按钮
     */
    buttonSignUp.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(MainActivity.this, SignupActivity.class));
      }
    });

    /**
     * 自动登陆和记住密码
     */
    checkBoxAutoSignIn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
          preferences.edit().putBoolean(AppConstant.AUTO_SIGNIN, true).apply();
          checkBoxMemoryPassword.setChecked(true);
        } else {
          preferences.edit().putBoolean(AppConstant.AUTO_SIGNIN, false).apply();
          checkBoxMemoryPassword.setChecked(false);
        }
      }
    });
    checkBoxMemoryPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) preferences.edit().putBoolean(AppConstant.MEMORY_PASSWORD, true).apply();
        else preferences.edit().putBoolean(AppConstant.MEMORY_PASSWORD, false).apply();
      }
    });
  }

  /**
   * 根据不同的用户对象(mode变量)来初始化activity
   */
  private void chooseMode() {
    mode = preferences.getInt(AppConstant.USER_MODE, 0);
    if (mode == 0) {
      setContentView(R.layout.activity_main);
      imageButtonParentAccess = (ImageButton) findViewById(R.id.imagebutton_parent_access);
      imageButtonChildAccess = (ImageButton) findViewById(R.id.imagebutton_child_access);
      imageButtonChildAccess.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          SharedPreferences.Editor editor = preferences.edit();
          mode = AppConstant.MODE_CHILD;
          editor.putInt(AppConstant.USER_MODE, mode);
          editor.apply();
          Intent intent = new Intent(MainActivity.this, ChildMainActivity.class);
          intent.putExtra(AppConstant.FROM_USERID, fromUserId);
          editor.putString(AppConstant.FROM_USERID, fromUserId).apply();
          startActivity(intent);
          finish();
        }
      });
      imageButtonParentAccess.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          SharedPreferences.Editor editor = preferences.edit();
          mode = AppConstant.MODE_PARENT;
          editor.putInt(AppConstant.USER_MODE, mode);
          editor.apply();
          Intent intent = new Intent(MainActivity.this, ParentMainActivity.class);
          intent.putExtra(AppConstant.FROM_USERID, fromUserId);
          editor.putString(AppConstant.FROM_USERID, fromUserId).apply();
          startActivity(intent);
          finish();
        }
      });
    } else {
      int mode = preferences.getInt(AppConstant.USER_MODE, 0);
      if (AppConstant.MODE_CHILD == mode) {
        Intent intent = new Intent(MainActivity.this, ChildMainActivity.class);
        intent.putExtra(AppConstant.FROM_USERID, fromUserId);
        preferences.edit().putString(AppConstant.FROM_USERID, fromUserId).apply();
        startActivity(intent);
        finish();
      } else if (AppConstant.MODE_PARENT == mode) {
        Intent intent = new Intent(MainActivity.this, ParentMainActivity.class);
        intent.putExtra(AppConstant.FROM_USERID, fromUserId);
        preferences.edit().putString(AppConstant.FROM_USERID, fromUserId).apply();
        startActivity(intent);
        finish();
      } else {
        //模式出错
      }
    }
  }

  /**
   * 检测用户的输入等情况
   * 并发送给服务器
   */
  private void dealWithUserInfo() {
    if (editTextUsername.getText().length() != 0) {
      if (editTextPassword.getText().length() != 0) {
        fromUserId = editTextUsername.getText().toString();
        preferences.edit().putString(AppConstant.FROM_USERID, fromUserId).apply();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AppConstant.USERNAME, editTextUsername.getText().toString());
        hashMap.put(AppConstant.PASSWORD, editTextPassword.getText().toString());
        HttpService.DoLoginRequest(hashMap, MainActivity.this);
      } else {
        YoYo.with(Techniques.Shake).duration(800).playOn(editTextPassword);
        showToast("请输入正确的密码");
      }
    } else {
      YoYo.with(Techniques.Shake).duration(800).playOn(editTextUsername);
      showToast("请输入正确的用户名");
    }
  }

  /**
   * 登录请求的信息处理
   */
  public void OnLoginSuccessResponse(JSONArray jsonArray) {
    JSONObject codeObject;
    JSONObject msgObject;
    JSONObject moneyObject = null;
    JSONObject imgUrlObject = null;
    Log.e("dada", jsonArray.toString());
    try {
      codeObject = (JSONObject) jsonArray.get(0);
      msgObject = (JSONObject) jsonArray.get(1);
      if (null != codeObject) {
        switch (codeObject.getInt(AppConstant.RETURN_CODE)) {
          case AppConstant.USERNAME_PASSWORD_WRONG:
            YoYo.with(Techniques.Shake).duration(800).playOn(editTextPassword);
            YoYo.with(Techniques.Shake).duration(800).playOn(editTextUsername);
            break;
          case AppConstant.LOGIN_SUCCESS:
            moneyObject = (JSONObject) jsonArray.get(2);
            imgUrlObject = (JSONObject) jsonArray.get(3);
            chooseMode();
            break;
        }
      }
      if (null != msgObject) showToast(msgObject.getString(AppConstant.RETURN_MSG));
      if (null != moneyObject) preferences.edit().putInt(AppConstant.LEFT_MONEY, moneyObject.getInt(AppConstant.MONEY)).apply();
      if (null != imgUrlObject) preferences.edit().putString(AppConstant.IMG_URL, imgUrlObject.getString(AppConstant.IMG_URL).replaceAll("\\\\", "")).apply();
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public void OnLoginErrorResponse(String errorMsg) {
    showToast(errorMsg);
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

  @Override
  public void onClick(View v) {
    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
  }

  /**
   * 开场动画
   */
  private void startInitAnim() {
    YoYo.with(Techniques.FadeIn).duration(1000).playOn(circularImage);
    YoYo.with(Techniques.FadeIn).duration(1000).playOn(editTextUsername);
    YoYo.with(Techniques.FadeIn).duration(1000).playOn(editTextPassword);
    YoYo.with(Techniques.FadeIn).duration(1000).playOn(checkBoxAutoSignIn);
    YoYo.with(Techniques.FadeInUp).duration(1000).playOn(checkBoxMemoryPassword);
    YoYo.with(Techniques.FadeInUp).duration(1000).playOn(buttonSignIn);
    YoYo.with(Techniques.FadeInUp).duration(1000).playOn(buttonSignUp);
  }

  private void showToast(String string) {
    Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
  }
}
