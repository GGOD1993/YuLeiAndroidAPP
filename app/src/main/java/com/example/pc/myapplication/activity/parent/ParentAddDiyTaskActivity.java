package com.example.pc.myapplication.activity.parent;

import android.content.Context;
import android.content.Intent;
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


    private DiyTaskInfo diyTaskInfo;

    private EditText parent_addtaskactivity_edittext_childid;
    private EditText parent_addtaskactivity_edittext_taskname;
    private EditText parent_addtaskactivity_editext_award;
    private EditText parent_addtaskactivity_editext_taskcontent;
    private ImageButton parent_addtaskactivity_imagebutton_submit;
    private ImageButton parent_addtaskactivity_imagebutton_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_add_diy_task);

        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_parent_add_task, menu);
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

    private void initView() {

        parent_addtaskactivity_edittext_childid = (EditText) findViewById(R.id.parent_addtaskactivity_edittext_childid);
        parent_addtaskactivity_edittext_taskname = (EditText) findViewById(R.id.parent_addtaskactivity_edittext_taskname);
        parent_addtaskactivity_editext_award = (EditText) findViewById(R.id.parent_addtaskactivity_editext_award);
        parent_addtaskactivity_editext_taskcontent = (EditText) findViewById(R.id.parent_addtaskactivity_editext_taskcontent);

        parent_addtaskactivity_imagebutton_submit = (ImageButton) findViewById(R.id.parent_addtaskactivity_imagebutton_submit);
        parent_addtaskactivity_imagebutton_cancel = (ImageButton) findViewById(R.id.parent_addtaskactivity_imagebutton_cancel);

        /**
         * 针对从系统任务选择的任务
         */
        Intent intent = getIntent();
        if (intent.getSerializableExtra(AppConstant.CLICKED_SYSTEM_TASK) != null) {

            SystemTaskInfo newSystemTask = (SystemTaskInfo) intent.getSerializableExtra(AppConstant.CLICKED_SYSTEM_TASK);
            parent_addtaskactivity_editext_taskcontent.setText(newSystemTask.getTaskContent());
            parent_addtaskactivity_edittext_taskname.setText(newSystemTask.getTaskId());
        }

        parent_addtaskactivity_imagebutton_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(AppConstant.PARENT_ADDDIYTASK_RESULTCODE, null);
                closeInputKeyBoard();
                finish();
            }
        });

        parent_addtaskactivity_imagebutton_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                diyTaskInfo = new DiyTaskInfo("", "", "", "");

                if (parent_addtaskactivity_edittext_childid.getText().length() != 0) {
                    if (parent_addtaskactivity_edittext_taskname.getText().length() != 0) {
                        if (parent_addtaskactivity_editext_award.getText().length() != 0) {
                            if (parent_addtaskactivity_editext_taskcontent.getText().length() != 0) {

                                diyTaskInfo.setChildId(parent_addtaskactivity_edittext_childid.getText().toString());
                                diyTaskInfo.setTaskName(parent_addtaskactivity_edittext_taskname.getText().toString());
                                diyTaskInfo.setAward(parent_addtaskactivity_editext_award.getText().toString());
                                diyTaskInfo.setTaskContent(parent_addtaskactivity_editext_taskcontent.getText().toString());
                                Intent data = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("newTask", diyTaskInfo);
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
