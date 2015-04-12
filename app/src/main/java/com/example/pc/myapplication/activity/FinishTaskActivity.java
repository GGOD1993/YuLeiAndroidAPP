package com.example.pc.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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

  private DiyTaskInfo clickTask;

  private EditText editTextChildId;
  private EditText editTextTaskname;
  private EditText editTextAward;
  private EditText editTextTaskcontent;

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


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_award_task, menu);
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

    editTextChildId = (EditText) findViewById(R.id.parent_taskinfoactivity_edittext_childid);
    editTextTaskname = (EditText) findViewById(R.id.parent_taskinfoactivity_edittext_taskname);
    editTextAward = (EditText) findViewById(R.id.parent_taskinfoactivity_editext_award);
    editTextTaskcontent = (EditText) findViewById(R.id.parent_taskinfoactivity_editext_taskcontent);
    submit = (ImageButton) findViewById(R.id.parent_finishtaskactivity_imagebutton_submit);
    cancel = (ImageButton) findViewById(R.id.parent_finishtaskactivity_imagebutton_cancel);

    editTextChildId.setText(clickTask.getFromUserId());
    editTextTaskname.setText(clickTask.getTaskName());
    editTextAward.setText(clickTask.getAward());
    editTextTaskcontent.setText(clickTask.getTaskContent());

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
    JSONObject codeObject =null;
    JSONObject msgObject = null;
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
