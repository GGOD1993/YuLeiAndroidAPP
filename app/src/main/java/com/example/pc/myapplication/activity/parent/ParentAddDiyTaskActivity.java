package com.example.pc.myapplication.activity.parent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.TaskInfo.DiyTaskInfo;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.TaskInfo.SystemTaskInfo;

public class ParentAddDiyTaskActivity extends ActionBarActivity {

  //用于存放即将发布的任务
  private DiyTaskInfo diyTaskInfo;

  //输入孩子id的控件
  private EditText editTextChildId;

  //输入任务内容的控件
  private EditText editTextTaskName;

  //输入任务奖金的控件
  private EditText ediTextAward;

  //输入任务内容的控件
  private EditText ediTextTaskContent;

  //提交按钮
  private ImageButton imageButtonSubmit;

  //取消按钮
  private ImageButton imageButtonCancel;

  //存储小型数据的SharedPerference
  private SharedPreferences preferences;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_parent_add_diy_task);
    preferences = getSharedPreferences(AppConstant.PREFERENCE_NAME,0);
    initView();
  }

  private void initView() {

    editTextChildId = (EditText) findViewById(R.id.parent_addtaskactivity_edittext_childid);
    editTextTaskName = (EditText) findViewById(R.id.parent_addtaskactivity_edittext_taskname);
    ediTextAward = (EditText) findViewById(R.id.parent_addtaskactivity_editext_award);
    ediTextTaskContent = (EditText) findViewById(R.id.parent_addtaskactivity_editext_taskcontent);

    imageButtonSubmit = (ImageButton) findViewById(R.id.parent_addtaskactivity_imagebutton_submit);
    imageButtonCancel = (ImageButton) findViewById(R.id.parent_addtaskactivity_imagebutton_cancel);

    /**
     * 针对从系统任务选择的任务
     */
    Intent intent = getIntent();
    if (intent.getSerializableExtra(AppConstant.CLICKED_SYSTEM_TASK) != null) {

      SystemTaskInfo newSystemTask = (SystemTaskInfo) intent.getSerializableExtra(AppConstant.CLICKED_SYSTEM_TASK);
      ediTextTaskContent.setText(newSystemTask.getTaskContent());
      editTextTaskName.setText(newSystemTask.getTaskId());
    }

    imageButtonCancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        setResult(AppConstant.PARENT_ADDDIYTASK_RESULTCODE, null);
        closeInputKeyBoard();
        finish();
      }
    });

    imageButtonSubmit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        diyTaskInfo = new DiyTaskInfo("", "", "", "", "");

        if (editTextChildId.getText().length() != 0) {
          if (editTextTaskName.getText().length() != 0) {
            if (ediTextAward.getText().length() != 0) {
              if (ediTextTaskContent.getText().length() != 0) {

                diyTaskInfo.setToUserId(editTextChildId.getText().toString());
                diyTaskInfo.setTaskName(editTextTaskName.getText().toString());
                diyTaskInfo.setAward(ediTextAward.getText().toString());
                diyTaskInfo.setTaskContent(ediTextTaskContent.getText().toString());
                diyTaskInfo.setFromUserId(preferences.getString(AppConstant.FROM_USERID, ""));
                Intent data = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppConstant.NEW_TASK, diyTaskInfo);
                data.putExtras(bundle);
                setResult(AppConstant.PARENT_ADDDIYTASK_RESULTCODE, data);

                closeInputKeyBoard();
                finish();
              }
              else {
                showToast("请输入正确的任务内容");
              }
            }
            else {
              showToast("请输入正确的奖励金额");
            }
          }
          else {
            showToast("请输入正确的任务名称");
          }
        }
        else {
          showToast("请输入正确的对象名称");
        }
      }
    });
  }

  private void closeInputKeyBoard() {

    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    if (imm.isActive()) {
      imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
  }

  private void showToast(String string) {
    Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
  }
}
