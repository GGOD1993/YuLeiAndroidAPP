package com.example.pc.myapplication.activity.parent;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.StringRequest;
import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.activity.MainActivity;
import com.example.pc.myapplication.utils.HttpService;
import com.example.pc.myapplication.utils.RequestQueueController;
import com.example.pc.myapplication.utils.StringPostRequestPlus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ParentMoreSettingActivity extends Activity implements
        HttpService.OnRequestResponseListener,
        HttpService.OnLogoutRequestResponseListener{

    private RequestQueue requestQueue;
    private SharedPreferences preferences;

    private RelativeLayout nowuser;
    private RelativeLayout parentactivity_moresetting_relativelayout_signout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_more_setting);

        preferences = getSharedPreferences(AppConstant.PREFERENCE_NAME,0);

        requestQueue = RequestQueueController.get().getRequestQueue();

        nowuser = (RelativeLayout) findViewById(R.id.nowuser);
        nowuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HttpService.DoRequest(
                        null,
                        ParentMoreSettingActivity.this,
                        AppConstant.GET_CURRENT_USER_URL,
                        Request.Method.GET);
            }
        });

        parentactivity_moresetting_relativelayout_signout = (RelativeLayout) findViewById(R.id.parentactivity_moresetting_relativelayout_signout);
        parentactivity_moresetting_relativelayout_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HttpService.DoLogoutRequest(
                        AppConstant.LOGIN_OUT_URL,
                        ParentMoreSettingActivity.this
                );
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_parent_more_setting, menu);
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
     * 网络请求结果处理
     * @param successResult
     */
    @Override
    public void OnRequestSuccessResponse(String successResult) {
        if (successResult != null) {
            showToast(successResult);
        } else {
            showToast("There something error~~~~");
        }
    }

    @Override
    public void OnRequestErrorResponse(String errorResult) {
        showToast(errorResult);
    }

    @Override
    public void OnLogoutSuccessRespoonse(JSONArray successJsonArray) {

        try {
            JSONObject codeObject = (JSONObject) successJsonArray.get(0);
            JSONObject msgObject = (JSONObject) successJsonArray.get(1);
            int code= codeObject.getInt("code");
            if (AppConstant.LOGOUT_SUCCESS == code) {
                showToast(msgObject.getString("msg"));
                startActivity(new Intent(ParentMoreSettingActivity.this, MainActivity.class));
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnLogoutErrorRespoonse(String errorMsg) {
        showToast(errorMsg);
    }

    private void showToast(String string) {
        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
    }
}
