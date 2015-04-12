package com.example.pc.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.TaskInfo.DiyTaskInfo;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class TaskInfoActivity extends SwipeBackActivity {

  private DiyTaskInfo clickTask;

  private EditText parent_taskinfoactivity_edittext_childId;
  private EditText parent_taskinfoactivity_edittext_taskname;
  private EditText parent_taskinfoactivity_editext_award;
  private EditText parent_taskinfoactivity_editext_taskcontent;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_task_info);

    Intent intent = getIntent();
    clickTask = (DiyTaskInfo) intent.getSerializableExtra(AppConstant.CLICKED_SEND_TASK);

    initViews();

  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_task_info, menu);
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

    parent_taskinfoactivity_edittext_childId = (EditText) findViewById(R.id.parent_taskinfoactivity_edittext_childid);
    parent_taskinfoactivity_edittext_taskname = (EditText) findViewById(R.id.parent_taskinfoactivity_edittext_taskname);
    parent_taskinfoactivity_editext_award = (EditText) findViewById(R.id.parent_taskinfoactivity_editext_award);
    parent_taskinfoactivity_editext_taskcontent = (EditText) findViewById(R.id.parent_taskinfoactivity_editext_taskcontent);

    parent_taskinfoactivity_edittext_childId.setText(clickTask.getToUserId());
    parent_taskinfoactivity_edittext_taskname.setText(clickTask.getTaskName());
    parent_taskinfoactivity_editext_award.setText(clickTask.getAward());
    parent_taskinfoactivity_editext_taskcontent.setText(clickTask.getTaskContent());
  }
}
