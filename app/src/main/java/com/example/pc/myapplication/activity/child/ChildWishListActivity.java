package com.example.pc.myapplication.activity.child;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.Infos.DiyTaskInfo;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.ViewStyle.SpaceItemDecoration;
import com.example.pc.myapplication.adapter.TaskRecyclerViewAdapter;
import com.example.pc.myapplication.utils.HttpService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class ChildWishListActivity extends SwipeBackActivity implements
        HttpService.OnGetDiyTaskRequestResponseListener {


  private TaskRecyclerViewAdapter adapter;
  private RecyclerView recyclerView;
  private ArrayList<DiyTaskInfo> taskList;

  private RelativeLayout header;
  private TextView textViewHeadeline;
  private TextView textViewBack;

  private SwipeRefreshLayout refreshLayout;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_child_wish_list);
    taskList = new ArrayList<>();
    initViews();
  }

  private void initViews() {
    recyclerView = (RecyclerView) findViewById(R.id.child_wishlistactivity_recyclerview);
    header = (RelativeLayout) findViewById(R.id.child_wishlistactivity_header);
    refreshLayout = (SwipeRefreshLayout) findViewById(R.id.child_wishlistactivity_swiperefreshlayout);
    textViewHeadeline = (TextView) findViewById(R.id.child_otheractivity_header_textview_headline);
    textViewBack = (TextView) findViewById(R.id.child_otheractivity_header_textview_back);

    textViewHeadeline.setText("父 母 心 愿");
    textViewBack.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
    recyclerView.setLayoutManager(layoutManager);
    adapter = new TaskRecyclerViewAdapter(taskList, getApplicationContext(), AppConstant.RECIVE_TASK_TYPE);
    recyclerView.addItemDecoration(new SpaceItemDecoration(30));
    ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(adapter);
    scaleAdapter.setFirstOnly(false);
    AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(scaleAdapter);
    alphaAdapter.setFirstOnly(false);
    recyclerView.setAdapter(alphaAdapter);
    refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        String url = AppConstant.GET_DIY_TASK_URL + "?" + AppConstant.USERNAME + "=" +
                getSharedPreferences(AppConstant.PREFERENCE_NAME, 0)
                        .getString(AppConstant.FROM_USERID, "");
        HttpService.DoGetDiyTaskRequest(url, null, ChildWishListActivity.this);
      }
    });
  }

  @Override
  public void OnGetDiyTaskSuccessResponse(JSONArray jsonArray) {
    refreshLayout.setRefreshing(false);
    JSONObject object;
    DiyTaskInfo taskInfo;
    ArrayList<DiyTaskInfo> submittedTask = new ArrayList<>();
    ArrayList<DiyTaskInfo> finishedTask = new ArrayList<>();
    int count = jsonArray.length();
    for (int i = 0; i < count; i++) {
      try{
        object = jsonArray.getJSONObject(i);
        taskInfo = new DiyTaskInfo(
                object.getString(AppConstant.TO_USERID),
                object.getString(AppConstant.TASK_ID),
                object.getString(AppConstant.TASK_REGDATE),
                object.getString(AppConstant.TASK_CONTENT),
                object.getString(AppConstant.FROM_USERID),
                0);

        switch (object.getInt(AppConstant.TASK_STATUS)) {
          case AppConstant.STATUS_NEW:
            taskInfo.setTaskStatus(AppConstant.STATUS_NEW);
            taskList.add(taskInfo);
            break;
          case AppConstant.STATUS_SUBMITTED:
            taskInfo.setTaskStatus(AppConstant.STATUS_SUBMITTED);
            submittedTask.add(taskInfo);
            break;
          case AppConstant.STATUS_FINISHED:
            taskInfo.setTaskStatus(AppConstant.STATUS_FINISHED);
            finishedTask.add(taskInfo);
            break;
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    taskList.addAll(submittedTask);
    taskList.addAll(finishedTask);
    recyclerView.scrollToPosition(taskList.size());
    submittedTask = null;
    finishedTask = null;
    adapter.notifyDataSetChanged();
  }

  @Override
  public void OnGetDiyTaskErrorResponse(String errorResult) {
    refreshLayout.setRefreshing(false);
    showToast(errorResult);
  }

  private void showToast(String string) {
    Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
  }
}
