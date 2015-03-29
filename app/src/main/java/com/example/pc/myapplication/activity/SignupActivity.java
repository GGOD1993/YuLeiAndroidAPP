package com.example.pc.myapplication.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.ViewStyle.CircularImage;
import com.example.pc.myapplication.utils.RequestQueueController;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends ActionBarActivity{

    private Context context;

    private ImageLoader imageLoader = null;

    private Button signup_button_submmit;
    private Button signup_button_cancel;
    private EditText signup_edittext_username;
    private EditText signup_edittext_password;
    private EditText signup_edittext_password_makesure;
    private EditText signup_edittext_nickname;
    private EditText signup_edittext_email;
    private RadioButton signup_radiobutton_male;
    private RadioButton signup_radiobutton_female;

    private CircularImage signup_circularimage_userimage;
    private RelativeLayout signup_relativelayout_1;

    private static final int USERIMAGE_HEIGHT = 70;
    private static final int USERIMAGE_WIDTH = 70;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        context = getApplicationContext();

        imageLoader = ImageLoader.getInstance();

        //这句话一定不要掉了
        imageLoader.init(ImageLoaderConfiguration.createDefault(SignupActivity.this));

        signup_edittext_username = (EditText) findViewById(R.id.signup_edittext_username);
        signup_edittext_password = (EditText) findViewById(R.id.signup_edittext_password);
        signup_edittext_password_makesure = (EditText) findViewById(R.id.signup_edittext_password_makesure);
        signup_edittext_nickname = (EditText) findViewById(R.id.signup_edittext_nickname);
        signup_edittext_email = (EditText) findViewById(R.id.signup_edittext_email);
        signup_radiobutton_male = (RadioButton) findViewById(R.id.signup_radiobutton_male);
        signup_radiobutton_female = (RadioButton) findViewById(R.id.signup_radiobutton_female);

        signup_circularimage_userimage = (CircularImage) findViewById(R.id.signup_circularimage_userimage);
        signup_circularimage_userimage.setImageResource(R.mipmap.bg_imagebutton_parent_access);
        signup_circularimage_userimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater layoutInflater = LayoutInflater.from(SignupActivity.this);
                View viewAddEmplyee = layoutInflater.inflate(R.layout.layout_signup_imagechooser,null);
                new AlertDialog.Builder(SignupActivity.this).setTitle("选 择 头 像").setView(
                        viewAddEmplyee).show();
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
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                    startActivityForResult(intent, AppConstant.SELECT_PIC_KITKAT);
                                } else {
                                    startActivityForResult(intent, AppConstant.SELECT_PIC);
                                }
                            }
                        }
                );
            }
        });

        signup_relativelayout_1 = (RelativeLayout) findViewById(R.id.signup_relativelayout_1);
        signup_relativelayout_1.setBackground(new BitmapDrawable(
                AppConstant.readBitMap(context, R.mipmap.bg0_fine_night)));

        signup_button_submmit = (Button) findViewById(R.id.signup_button_submmit);
        signup_button_submmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dealWithUserInfo();
            }
        });

        signup_button_cancel = (Button) findViewById(R.id.signup_button_cancel);
        signup_button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signup, menu);
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
                    imageLoader.loadImage(intent.getData().toString(), targetSize, new SimpleImageLoadingListener(){
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            signup_circularimage_userimage.setImageBitmap(loadedImage);
                        }
                    });
                    break;

                case AppConstant.CAMERA_RESULTCODE:
                    Bundle bundle= intent.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    signup_circularimage_userimage.setImageBitmap(bitmap);
                    break;
            }
        }
    }

    private void dealWithUserInfo() {

        if (signup_edittext_username.getText().length() != 0) {
            if (signup_edittext_nickname.getText().length() != 0) {
                if (signup_edittext_email.getText().length() != 0) {
                    if (signup_edittext_password.getText().length() != 0
                            && signup_edittext_password_makesure.getText().length() != 0) {
                        if (signup_edittext_password.getText().toString().
                                equals(signup_edittext_password_makesure.getText().toString())) {

                            final int gender;
                            if (signup_radiobutton_male.isChecked())
                                gender = 1;
                            else
                                gender = 0;
                            final String username = signup_edittext_username.getText().toString();
                            final String password = signup_edittext_password.getText().toString();
                            final String nickname = signup_edittext_nickname.getText().toString();
                            final String email = signup_edittext_email.getText().toString();

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
                                            case AppConstant.NEW_USER_SUCCESS:
                                                finish();
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
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstant.NEW_USER_URL,  listener, errorListener) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> map = new HashMap<>();
                                        map.put("username", username);
                                        map.put("password", password);
                                        map.put("nickname", nickname);
                                        map.put("email", email);
                                        map.put("gender", String.valueOf(gender));
                                        return map;
                                    }
                                };
                                RequestQueueController.get().getRequestQueue().add(stringRequest);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else {
                            showToast("两次密码输入不相同");
                        }
                    } else {
                        showToast("请输入正确的密码");
                    }
                }else {
                    showToast("请输入正确的邮箱");
                }
            } else {
                showToast("请输入正确的昵称");
            }
        } else {
            showToast("请输入正确的用户名");
        }
    }

    private void showToast(String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }
}
