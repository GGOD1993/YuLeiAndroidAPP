package com.example.pc.myapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.ViewStyle.CircularImage;
import com.example.pc.myapplication.activity.child.ChildMainActivity;
import com.example.pc.myapplication.activity.parent.ParentMainActivity;
import com.example.pc.myapplication.utils.RequestQueueController;
import com.example.pc.myapplication.utils.StringPostRequestPlus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 初始Activity
 */
public class MainActivity extends ActionBarActivity {

    //家长或孩子的模式参数
    private int mode;

    //登录用户的Id
    public String from_userid;

    private Button signin_button_signin;
    private Button signin_button_signup;
    private EditText signin_edittext_password;
    private EditText signin_edittext_username;
    private CircularImage circularImage;
    private RelativeLayout signin_relativelayout_root;

    private ImageButton imagebutton_parent_access;
    private ImageButton imagebutton_child_access;

    private CheckBox signin_checkbox_memorypassword;
    private CheckBox signin_checkbox_autosignin;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivity();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    /**
     * 初始化登陆界面
     */
    private void initActivity() {

        setContentView(R.layout.layout_signin);

        preferences = getSharedPreferences(AppConstant.PREFERENCE_NAME,0);

        signin_button_signin = (Button) findViewById(R.id.signin_button_signin);
        signin_button_signup = (Button) findViewById(R.id.signin_button_signup);
        signin_edittext_username = (EditText) findViewById(R.id.signin_edittext_username);
        signin_edittext_password = (EditText) findViewById(R.id.signin_edittext_password);
        signin_checkbox_autosignin = (CheckBox) findViewById(R.id.signin_checkbox_autosignin);
        signin_checkbox_memorypassword = (CheckBox) findViewById(R.id.signin_checkbox_memorypassword);
        circularImage = (CircularImage) findViewById(R.id.signin_circularimage_userimage);
        signin_relativelayout_root = (RelativeLayout) findViewById(R.id.signin_relativelayout_root);

        circularImage.setImageResource(R.mipmap.ic_launcher);

        signin_edittext_username.setText(preferences.getString("auto_sign_username",""));
        signin_edittext_password.setText(preferences.getString("auto_sign_password",""));

        if (preferences.getBoolean("autosignin",false)) {
            signin_checkbox_autosignin.setChecked(true);

//            dealWithUserInfo();

        }

        if (preferences.getBoolean("memorypassword",false)) {

            signin_checkbox_memorypassword.setChecked(true);
        }

        signin_relativelayout_root.setBackground(
                new BitmapDrawable(AppConstant.readBitMap(getApplicationContext(), R.mipmap.skin_bg_player_x)));

        /**
         * 登陆按钮
         */
        signin_button_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (signin_checkbox_memorypassword.isChecked()) {
                    preferences.edit().putString("auto_sign_username",signin_edittext_username.getText().toString()).apply();
                    preferences.edit().putString("auto_sign_password",signin_edittext_password.getText().toString()).apply();
                }
                dealWithUserInfo();
            }
        });

        /**
         * 注册按钮
         */
        signin_button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, SignupActivity.class));
            }
        });

        /**
         * 自动登陆和记住密码
         */
        signin_checkbox_autosignin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    preferences.edit().putBoolean("autosignin",true).apply();
                    signin_checkbox_memorypassword.setChecked(true);

                } else {

                    preferences.edit().putBoolean("autosignin",false).apply();
                    signin_checkbox_memorypassword.setChecked(false);
                }
            }
        });

        signin_checkbox_memorypassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    preferences.edit().putBoolean("memorypassword",true).apply();

                } else {

                    preferences.edit().putBoolean("memorypassword",false).apply();
                }
            }
        });
    }

    /**
     * 根据不同的用户对象(mode变量)来初始化activity
     */
    private void chooseMode() {

        preferences = getPreferences(MODE_PRIVATE);
        mode = preferences.getInt("mode",0);
        if (mode == 0) {

            setContentView(R.layout.activity_main);

            imagebutton_parent_access = (ImageButton) findViewById(R.id.imagebutton_parent_access);
            imagebutton_child_access = (ImageButton) findViewById(R.id.imagebutton_child_access);

            imagebutton_child_access.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharedPreferences.Editor editor = preferences.edit();
                    mode = AppConstant.MODE_CHILD;
                    editor.putInt("mode", mode);
                    editor.apply();

                    Intent intent = new Intent(MainActivity.this,ChildMainActivity.class);
                    intent.putExtra(AppConstant.FROM_USERID,from_userid);
                    editor.putString(AppConstant.FROM_USERID,from_userid).apply();
                    startActivity(intent);
                    finish();
                }
            });

            imagebutton_parent_access.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharedPreferences.Editor editor = preferences.edit();
                    mode = AppConstant.MODE_PARENT;
                    editor.putInt("mode", mode);
                    editor.apply();

                    Intent intent = new Intent(MainActivity.this,ParentMainActivity.class);
                    intent.putExtra(AppConstant.FROM_USERID,from_userid);
                    editor.putString(AppConstant.FROM_USERID,from_userid).apply();
                    startActivity(intent);
                    finish();
                }
            });
        }
        else {
            int mode = preferences.getInt("mode",0);
            if (AppConstant.MODE_CHILD == mode) {
                Intent intent = new Intent(MainActivity.this,ChildMainActivity.class);
                intent.putExtra(AppConstant.FROM_USERID,from_userid);
                preferences.edit().putString(AppConstant.FROM_USERID,from_userid).apply();
                startActivity(intent);
                finish();
            }
            else if (AppConstant.MODE_PARENT == mode) {
                Intent intent = new Intent(MainActivity.this,ParentMainActivity.class);
                intent.putExtra(AppConstant.FROM_USERID,from_userid);
                preferences.edit().putString(AppConstant.FROM_USERID,from_userid).apply();
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

        if (signin_edittext_username.getText().length() != 0) {
            if (signin_edittext_password.getText().length() != 0) {

                final String username = signin_edittext_username.getText().toString();
                final String password = signin_edittext_password.getText().toString();

                from_userid = signin_edittext_username.getText().toString();

                preferences.edit().putString(AppConstant.FROM_USERID,from_userid).apply();

                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject codeObject = jsonArray.getJSONObject(0);
                            JSONObject msgObject = jsonArray.getJSONObject(1);

                            int code = Integer.valueOf(codeObject.getString("code"));
                            String msg = msgObject.getString("msg");
                            showToast(msg);

                            switch (code) {
                                case AppConstant.LOGIN_SUCCESS:
                                    chooseMode();
                                    break;
                            }
                        } catch(JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        showToast(volleyError.getMessage());
                    }
                };

                try{
                    StringPostRequestPlus stringRequest = new StringPostRequestPlus(
                            Request.Method.POST,
                            AppConstant.LOGIN_IN_URL,
                            listener, errorListener){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> map = new HashMap<>();
                            map.put("username", username);
                            map.put("password", password);
                            return map;
                        }
                    };

                    RequestQueueController.get().getRequestQueue().add(stringRequest);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                showToast("请输入正确的密码");
            }
        } else {
            showToast("请输入正确的用户名");
        }
    }

    private void showToast(String string) {
        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
    }
}
