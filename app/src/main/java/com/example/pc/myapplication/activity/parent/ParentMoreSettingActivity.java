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
import com.example.pc.myapplication.utils.RequestQueueController;
import com.example.pc.myapplication.utils.StringPostRequestPlus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ParentMoreSettingActivity extends Activity {

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
                Response.Listener<String> jsonArrayListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                           showToast(response);
                        } else {
                            showToast("There something error~~~~");
                        }
                    }
                };

                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showToast(error.getMessage());
                    }
                };

                StringPostRequestPlus getCurrentUserRequest = new StringPostRequestPlus(
                        Request.Method.GET,
                        AppConstant.GET_CURRENT_USER_URL,
                        jsonArrayListener,
                        errorListener);

                requestQueue.add(getCurrentUserRequest);
            }
        });

        parentactivity_moresetting_relativelayout_signout = (RelativeLayout) findViewById(R.id.parentactivity_moresetting_relativelayout_signout);
        parentactivity_moresetting_relativelayout_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject codeObject = jsonArray.getJSONObject(0);
                            JSONObject msgObject = jsonArray.getJSONObject(1);

                            int code = Integer.valueOf(codeObject.getString("code"));
                            String msg = msgObject.getString("msg");

                            switch (code) {
                                case AppConstant.LOGOUT_SUCCESS:
                                    showToast(msg);

                                    startActivity(new Intent(ParentMoreSettingActivity.this, MainActivity.class));
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
                    StringRequest logOutRequest = new StringRequest(
                            Request.Method.GET,
                            AppConstant.LOGIN_OUT_URL,
                            listener,
                            errorListener);
                    requestQueue.add(logOutRequest);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    private void showToast(String string) {
        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
    }
}
