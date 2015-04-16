package com.example.pc.myapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
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
        HttpService.OnLoginRequestResponseListener {

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

    initActivity();
  }

  /**
   * 初始化登陆界面
   */
  private void initActivity() {
    setContentView(R.layout.layout_signin);
    preferences = getSharedPreferences(AppConstant.PREFERENCE_NAME,0);
    buttonSignIn = (Button) findViewById(R.id.signin_button_signin);
    buttonSignUp= (Button) findViewById(R.id.signin_button_signup);
    editTextUsername = (EditText) findViewById(R.id.signin_edittext_username);
    editTextPassword = (EditText) findViewById(R.id.signin_edittext_password);
    checkBoxAutoSignIn = (CheckBox) findViewById(R.id.signin_checkbox_autosignin);
    checkBoxMemoryPassword = (CheckBox) findViewById(R.id.signin_checkbox_memorypassword);
    circularImage = (CircularImage) findViewById(R.id.signin_circularimage_userimage);
    root = (RelativeLayout) findViewById(R.id.signin_relativelayout_root);
    circularImage.setImageResource(R.mipmap.ic_launcher);
    editTextUsername.setText(preferences.getString(AppConstant.AUTO_SIGNIN_USERNAME,""));
    editTextPassword.setText(preferences.getString(AppConstant.AUTO_SIGNIN_PASSWORD,""));
    if (preferences.getBoolean("autosignin",false)) {
      checkBoxAutoSignIn.setChecked(true);
//            dealWithUserInfo();
    }
    if (preferences.getBoolean("memorypassword",false)) {
      checkBoxMemoryPassword.setChecked(true);
    }
    root.setBackground(
            new BitmapDrawable(AppConstant.readBitMap(getApplicationContext(), R.mipmap.skin_bg_player_x)));
    /**
     * 登陆按钮
     */
    buttonSignIn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (checkBoxMemoryPassword.isChecked()) {
          preferences.edit().putString(AppConstant.AUTO_SIGNIN_USERNAME,editTextUsername.getText().toString()).apply();
          preferences.edit().putString(AppConstant.AUTO_SIGNIN_PASSWORD,editTextPassword.getText().toString()).apply();
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
          preferences.edit().putBoolean("autosignin",true).apply();
          checkBoxMemoryPassword.setChecked(true);
        } else {
          preferences.edit().putBoolean("autosignin",false).apply();
          checkBoxMemoryPassword.setChecked(false);
        }
      }
    });

    checkBoxMemoryPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) preferences.edit().putBoolean("memorypassword",true).apply();
        else preferences.edit().putBoolean("memorypassword",false).apply();
      }
    });
  }

  /**
   * 根据不同的用户对象(mode变量)来初始化activity
   */
  private void chooseMode() {
    mode = preferences.getInt(AppConstant.USER_MODE,0);
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
          Intent intent = new Intent(MainActivity.this,ChildMainActivity.class);
          intent.putExtra(AppConstant.FROM_USERID,fromUserId);
          editor.putString(AppConstant.FROM_USERID,fromUserId).apply();
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
          Intent intent = new Intent(MainActivity.this,ParentMainActivity.class);
          intent.putExtra(AppConstant.FROM_USERID,fromUserId);
          editor.putString(AppConstant.FROM_USERID,fromUserId).apply();
          startActivity(intent);
          finish();
        }
      });
    }
    else {
      int mode = preferences.getInt(AppConstant.USER_MODE,0);
      if (AppConstant.MODE_CHILD == mode) {
        Intent intent = new Intent(MainActivity.this,ChildMainActivity.class);
        intent.putExtra(AppConstant.FROM_USERID,fromUserId);
        preferences.edit().putString(AppConstant.FROM_USERID,fromUserId).apply();
        startActivity(intent);
        finish();
      }
      else if (AppConstant.MODE_PARENT == mode) {
        Intent intent = new Intent(MainActivity.this,ParentMainActivity.class);
        intent.putExtra(AppConstant.FROM_USERID,fromUserId);
        preferences.edit().putString(AppConstant.FROM_USERID,fromUserId).apply();
        startActivity(intent);
        finish();
      }
      else {
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
        preferences.edit().putString(AppConstant.FROM_USERID,fromUserId).apply();
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put(AppConstant.USERNAME,editTextUsername.getText().toString());
        hashMap.put(AppConstant.PASSWORD,editTextPassword.getText().toString());
        HttpService.DoLoginRequest(Request.Method.POST, AppConstant.LOGIN_IN_URL, hashMap, MainActivity.this);
      } else {
        showToast("请输入正确的密码");
      }
    } else {
      showToast("请输入正确的用户名");
    }
  }

  /**
   * 登录请求的信息处理
   */
  public void OnLoginSuccessResponse(JSONArray jsonArray){
    JSONObject codeObject;
    JSONObject msgObject;
    JSONObject moneyObject;
    try{
      codeObject = (JSONObject) jsonArray.get(0);
      msgObject = (JSONObject) jsonArray.get(1);
      moneyObject = (JSONObject) jsonArray.get(2);
      if (null != codeObject) {
      }
      if (null != msgObject) {
        showToast(msgObject.getString(AppConstant.RETURN_MSG));
      }
      if (null != moneyObject) {
        preferences.edit().putInt(AppConstant.LEFT_MONEY, moneyObject.getInt(AppConstant.MONEY)).apply();
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    chooseMode();
  }

  public void OnLoginErrorResponse(String errorMsg) {
    showToast(errorMsg);
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

  private void showToast(String string) {
    Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
  }
}
