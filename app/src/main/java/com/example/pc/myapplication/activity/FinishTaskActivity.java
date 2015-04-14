package com.example.pc.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.TaskInfo.DiyTaskInfo;
import com.example.pc.myapplication.utils.HttpService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class FinishTaskActivity extends SwipeBackActivity
        implements HttpService.OnFinishDiyTaskRequestResponseListener{

  //当前任务对象
  private DiyTaskInfo clickTask;

  //心愿的发布者
  private TextView textViewFromUserId;

  //心愿的名称
  private TextView textViewTaskName;

  //心愿奖励
  private TextView textViewAward;

  //心愿内容
  private TextView textViewTaskContent;

  private ImageButton submit;
  private ImageButton cancel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_finish_task);

    Intent intent = getIntent();
    clickTask = (DiyTaskInfo) intent.getSerializableExtra(AppConstant.CLICKED_CHECK_TASK);
    initViews();
  }

  private void initViews() {

    textViewFromUserId = (TextView) findViewById(R.id.parent_finishtaskactivity_edittext_fromuserid);
    textViewTaskName = (TextView) findViewById(R.id.parent_finishtaskactivity_edittext_taskname);
    textViewAward = (TextView) findViewById(R.id.parent_finishtaskactivity_editext_award);
    textViewTaskContent = (TextView) findViewById(R.id.parent_finishtaskactivity_editext_taskcontent);
    submit = (ImageButton) findViewById(R.id.parent_finishtaskactivity_imagebutton_submit);
    cancel = (ImageButton) findViewById(R.id.parent_finishtaskactivity_imagebutton_cancel);

    textViewFromUserId.setText(clickTask.getFromUserId());
    textViewTaskName.setText(clickTask.getTaskName());
    textViewAward.setText(clickTask.getAward());
    textViewTaskContent.setText(clickTask.getTaskContent());

    submit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        HashMap<String ,String> map = new HashMap<>(2);
        map.put(AppConstant.TASK_ID, clickTask.getTaskName());
        map.put(AppConstant.FROM_USERID, clickTask.getFromUserId());
        HttpService.DoFinishDiyTaskRequest(Request.Method.POST, AppConstant.FINISH_DIY_TASK_URL, map, FinishTaskActivity.this);
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
   * 处理网络请求的结果
   * @param jsonArray
   */
  @Override
  public void OnFinishDiyTaskSuccessResponse(JSONArray jsonArray) {
    JSONObject codeObject;
    JSONObject msgObject;
    try{
      codeObject = (JSONObject) jsonArray.get(0);
      msgObject = (JSONObject) jsonArray.get(1);
      if (null != codeObject) {
        if (AppConstant.FINISH_TASK_SUCCESS == codeObject.getInt(AppConstant.RETURN_CODE)) {
          finish();
          //这里还需要移动当前的任务到已完成的历史任务中去
        }
        showToast(msgObject.getString(AppConstant.RETURN_MSG));
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void OnFinishDiyTaskErrorResponse(String errorResult) {
    showToast(errorResult);
  }

  private void showToast(String string) {
    Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
  }
}
