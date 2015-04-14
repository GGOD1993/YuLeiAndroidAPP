package com.example.pc.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.TaskInfo.DiyTaskInfo;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class TaskInfoActivity extends SwipeBackActivity {

  //显示的任务对象
  private DiyTaskInfo clickTask;

  //用户名框
  private TextView textViewUserId;

  //任务内容框
  private TextView textViewTaskContent;

  //任务奖励
  private TextView textViewAward;

  //任务名称
  private TextView textViewTaskName;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_task_info);

    Intent intent = getIntent();
    clickTask = (DiyTaskInfo) intent.getSerializableExtra(AppConstant.CLICKED_SEND_TASK);

    initViews();

  }

  private void initViews() {
    textViewUserId = (TextView) findViewById(R.id.parent_taskinfoactivity_textview_childid);
    textViewTaskName = (TextView) findViewById(R.id.parent_taskinfoactivity_textview_taskname);
    textViewAward = (TextView) findViewById(R.id.parent_taskinfoactivity_textview_award);
    textViewTaskContent = (TextView) findViewById(R.id.parent_taskinfoactivity_textview_taskcontent);

    textViewUserId.setText(clickTask.getToUserId());
    textViewTaskName.setText(clickTask.getTaskName());
    textViewAward.setText(clickTask.getAward());
    textViewTaskContent.setText(clickTask.getTaskContent());
  }
}
