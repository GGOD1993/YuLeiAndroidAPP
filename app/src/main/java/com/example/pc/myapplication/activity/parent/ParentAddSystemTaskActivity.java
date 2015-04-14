package com.example.pc.myapplication.activity.parent;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.TaskInfo.SystemTaskInfo;
import com.example.pc.myapplication.adapter.AddSystemTaskListViewAdapter;
import com.example.pc.myapplication.utils.HttpService;
import com.example.pc.myapplication.utils.RequestQueueController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParentAddSystemTaskActivity extends ActionBarActivity
        implements HttpService.OnGetSysTaskRequestResponseListener {

  //Volley请求队列
  private RequestQueue requestQueue;

  //系统任务数组
  private ArrayList<SystemTaskInfo> systemTaskList;

  //系统任务列表
  private ListView listView;

  private AddSystemTaskListViewAdapter adapter;

  //下拉刷新组件
  private SwipeRefreshLayout swipeRefreshLayout;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_parent_add_system_task);
    systemTaskList = new ArrayList<>();
    requestQueue = RequestQueueController.get().getRequestQueue();
    initViews();
  }

  private void initViews() {

    /**
     * listview设置监听器
     */
    listView = (ListView)
            findViewById(R.id.parent_addsystemtaskactivity_listview);
    adapter = new AddSystemTaskListViewAdapter(getApplicationContext(),systemTaskList);
    listView.setAdapter(adapter);
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        SystemTaskInfo clickedTask = systemTaskList.get(position);
        Intent intent = new Intent(ParentAddSystemTaskActivity.this, ParentAddDiyTaskActivity.class);
        intent.putExtra(AppConstant.CLICKED_SYSTEM_TASK, clickedTask);
        startActivity(intent);
        finish();
      }
    });

    /**
     * 给下拉刷新加监听器
     */
    swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.parent_addsystemtaskactivity_swiperefreshlayout);
    swipeRefreshLayout.setOnRefreshListener(
            new SwipeRefreshLayout.OnRefreshListener() {
              @Override
              public void onRefresh() {
                HttpService.DoGetSysTaskRequest(Request.Method.GET, AppConstant.GET_SYS_TASK_URL, null, ParentAddSystemTaskActivity.this);
              }
            }
    );
  }

  /**
   * 网络请求处理
   * @param jsonArray
   */
  @Override
  public void OnGetSysTaskSuccessResponse(JSONArray jsonArray) {

    if (jsonArray != null) {
      if (jsonArray.toString().contains("1409")) {
        showToast("Not login please login");
      } else {

        JSONObject object = null;
        systemTaskList.clear();
        for (int i = 0 ; i < jsonArray.length() ; i ++) {

          try{
            object = (JSONObject) jsonArray.get(i);
            systemTaskList.add(new SystemTaskInfo(
                    object.getString("taskId"),
                    object.getString("taskContent")
            ));
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }
        adapter.notifyDataSetChanged();
      }
    } else {
      showToast("There something error~~~~");
    }

    swipeRefreshLayout.setRefreshing(false);
  }

  @Override
  public void OnGetSysTaskErrorResponse(String errorResult) {
    swipeRefreshLayout.setRefreshing(false);
    showToast(errorResult);
  }

  private void showToast(String string) {
    Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
  }
}
