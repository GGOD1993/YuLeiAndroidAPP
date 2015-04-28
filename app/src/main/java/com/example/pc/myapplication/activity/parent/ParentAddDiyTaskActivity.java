package com.example.pc.myapplication.activity.parent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.TaskInfo.DiyTaskInfo;
import com.example.pc.myapplication.TaskInfo.SystemTaskInfo;
import com.example.pc.myapplication.adapter.StringPickerViewAdapter;

import java.util.ArrayList;

public class ParentAddDiyTaskActivity extends ActionBarActivity implements AbsListView.OnScrollListener{

  //用于存放即将发布的任务
  private DiyTaskInfo diyTaskInfo;

  //选择布置任务对象控件
  private ListView listViewUserId;

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

  //前一状态
  private int preStatus;

  //当前状态
  private int nowStatus = -1;

  //存放String的list
  private ArrayList<String> list;

  //listview选中的item
  private String selectedId = "";

  //listview三种状态
  private static final int SCROLL_STATE_FLING = 2;
  private static final int SCROLL_STATE_IDLE = 0;
  private static final int SCROLL_STATE_TOUCH_SCROLL = 1;

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
    listViewUserId = (ListView) findViewById(R.id.parent_addtaskactivity_listview_userid);
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

    list = new ArrayList<>();
    for (int i = 0; i < 20; i ++) {
      list.add("dada" + i);
    }

    listViewUserId = (ListView) findViewById(R.id.parent_addtaskactivity_listview_userid);
    StringPickerViewAdapter adapter = new StringPickerViewAdapter(getApplicationContext(), list);
    listViewUserId.setAdapter(adapter);
    listViewUserId.setOnScrollListener(ParentAddDiyTaskActivity.this);

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
        diyTaskInfo = new DiyTaskInfo("", "", "", "", "", 0);
        if (selectedId.length() != 0) {
          showToast(selectedId);
          if (editTextTaskName.getText().length() != 0) {
            if (ediTextAward.getText().length() != 0) {
              if (ediTextTaskContent.getText().length() != 0) {
                diyTaskInfo.setToUserId(selectedId);
                diyTaskInfo.setTaskName(editTextTaskName.getText().toString());
                diyTaskInfo.setAward(ediTextAward.getText().toString());
                diyTaskInfo.setTaskContent(ediTextTaskContent.getText().toString());
                diyTaskInfo.setFromUserId(preferences.getString(AppConstant.FROM_USERID, ""));
                Intent data = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelable(AppConstant.NEW_TASK, diyTaskInfo);
                data.putExtras(bundle);
                setResult(AppConstant.PARENT_ADDDIYTASK_RESULTCODE, data);

                closeInputKeyBoard();
                finish();
              }
              else showToast("请输入正确的任务内容");
            }
            else showToast("请输入正确的奖励金额");
          }
          else showToast("请输入正确的任务名称");
        }
        else showToast("请输入正确的对象名称");
      }
    });
  }

  @Override
  public void onScrollStateChanged(AbsListView absListView, int i) {
    if (-1 == nowStatus) nowStatus = i;
    else {
      preStatus = nowStatus;
      nowStatus = i;
    }
    if ((SCROLL_STATE_TOUCH_SCROLL == preStatus
            || SCROLL_STATE_FLING == preStatus)
            && SCROLL_STATE_IDLE == nowStatus)
      rebound();
  }

  @Override
  public void onScroll(AbsListView absListView, int i, int i2, int i3) {
  }

  private void rebound() {
    View firstVisibleChild = listViewUserId.getChildAt(0);
    int childHeight = firstVisibleChild.getHeight();
    if (childHeight / 2 + firstVisibleChild.getTop() < 0) {
      selectedId = String.valueOf(listViewUserId.getItemAtPosition(listViewUserId.getPositionForView(firstVisibleChild) + 1));
      int scrollY = childHeight + firstVisibleChild.getTop();
      listViewUserId.scrollBy(0, scrollY);
    } else {
      selectedId = String.valueOf(listViewUserId.getItemAtPosition(listViewUserId.getPositionForView(firstVisibleChild)));
      int scrollY = firstVisibleChild.getTop();
      listViewUserId.scrollBy(0, scrollY);
    }
  }

  private void closeInputKeyBoard() {
    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    if (imm.isActive()) imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
  }

  private void showToast(String string) {
    Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
  }
}
