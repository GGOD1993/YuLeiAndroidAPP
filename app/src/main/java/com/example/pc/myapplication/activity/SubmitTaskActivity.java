package com.example.pc.myapplication.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.TaskInfo.DiyTaskInfo;
import com.example.pc.myapplication.fragment.parent.ParentMsgFragment;
import com.example.pc.myapplication.utils.HttpService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SubmitTaskActivity extends ActionBarActivity
        implements HttpService.OnSubmitDiyTaskRequestResponseListener{

  //头部的文本框
  private TextView textViewHeader;

  //对象id
  private TextView textViewToUserId;

  //任务名称
  private TextView textViewTaskName;

  //任务金额
  private TextView textViewAward;

  //任务内容
  private TextView textViewContent;

  //提交按钮
  private ImageButton submit;

  //取消按钮
  private ImageButton cancel;

  private DiyTaskInfo taskToBeSubmit;



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_submit_task);

    initViews();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_submit_task, menu);
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

  private void initViews() {
    textViewToUserId = (TextView) findViewById(R.id.submittaskactivity_textview_touserid);
    textViewTaskName = (TextView) findViewById(R.id.parent_taskinfoactivity_textview_taskname);
    textViewAward = (TextView) findViewById(R.id.submittaskactivity_textview_award);
    textViewContent = (TextView) findViewById(R.id.submittaskactivity_textview_taskcontent);
    textViewHeader = (TextView) findViewById(R.id.child_mainactivity_header_textview);
    submit = (ImageButton) findViewById(R.id.submittaskactivity_imagebutton_submit);
    cancel = (ImageButton) findViewById(R.id.submittaskactivity_imagebutton_cancel);

    Intent intent = getIntent();
    taskToBeSubmit = (DiyTaskInfo) intent.getSerializableExtra(AppConstant.TASK_TO_BE_SUBMIT);
    textViewToUserId.setText(taskToBeSubmit.getToUserId());
    textViewTaskName.setText(taskToBeSubmit.getTaskName());
    textViewAward.setText(taskToBeSubmit.getAward());
    textViewContent.setText(taskToBeSubmit.getTaskContent());

    submit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startSubmitTask();
      }
    });

    cancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
  }

  /**
   * 发出提交任务请求
   */
  private void startSubmitTask() {
    HashMap<String, String> map = new HashMap<>(2);
    map.put(AppConstant.TASK_ID, taskToBeSubmit.getTaskName());
    map.put(AppConstant.TO_USERID, taskToBeSubmit.getToUserId());
    HttpService.DoSubmitDiyTaskRequest(Request.Method.POST, AppConstant.SUBMIT_DIY_TASK_URL, map, SubmitTaskActivity.this);
  }

  /**
   * 处理网络请求返回的信息
   * @param jsonArray
   */
  @Override
  public void OnSubmitDiyTaskSuccessResponse(JSONArray jsonArray) {
    JSONObject codeObject =null;
    JSONObject msgObject = null;
    try{
      codeObject = (JSONObject) jsonArray.get(0);
      msgObject = (JSONObject) jsonArray.get(1);
      if (null != codeObject) {
      }
      if (null != msgObject) {
        showToast(msgObject.getString(AppConstant.RETURN_MSG));
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    Intent broadcastIntent = new Intent(AppConstant.BROADCAST_REMOVEVIEW);
    broadcastIntent.putExtra(AppConstant.TASK_ID, taskToBeSubmit.getTaskName());
    sendBroadcast(broadcastIntent);

    finish();
  }

  @Override
  public void OnSubmitDiyTaskErrorResponse(String errorResult) {
    showToast(errorResult);
    finish();
  }

  private void showToast(String string) {
    Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
  }
}
