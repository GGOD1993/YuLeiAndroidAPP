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
  private ListView parent_addsystemtaskactivity_listview;

  private AddSystemTaskListViewAdapter addSystemTaskListViewAdapter;

  //下拉刷新组件
  private SwipeRefreshLayout parent_addsystemtaskactivity_swiperefreshlayout;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_parent_add_system_task);

    systemTaskList = new ArrayList<>();
    requestQueue = RequestQueueController.get().getRequestQueue();

    initViews();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_parent_add_system_task, menu);
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

  private void showToast(String string) {
    Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
  }

  private void initViews() {

    /**
     * listview设置监听器
     */
    parent_addsystemtaskactivity_listview = (ListView)
            findViewById(R.id.parent_addsystemtaskactivity_listview);
    addSystemTaskListViewAdapter = new AddSystemTaskListViewAdapter(getApplicationContext(),systemTaskList);
    parent_addsystemtaskactivity_listview.setAdapter(addSystemTaskListViewAdapter);
    parent_addsystemtaskactivity_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
    parent_addsystemtaskactivity_swiperefreshlayout = (SwipeRefreshLayout)
            findViewById(R.id.parent_addsystemtaskactivity_swiperefreshlayout);
    parent_addsystemtaskactivity_swiperefreshlayout.setOnRefreshListener(
            new SwipeRefreshLayout.OnRefreshListener() {
              @Override
              public void onRefresh() {
                HttpService.DoGetSysTaskRequest(
                        Request.Method.GET,
                        AppConstant.GET_SYS_TASK_URL,
                        null,
                        ParentAddSystemTaskActivity.this
                );
              }
            }
    );
  }

  /**
   * 网络请求处理
   * @param successResult
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
        addSystemTaskListViewAdapter.notifyDataSetChanged();
      }
    } else {
      showToast("There something error~~~~");
    }

    parent_addsystemtaskactivity_swiperefreshlayout.setRefreshing(false);
  }

  @Override
  public void OnGetSysTaskErrorResponse(String errorResult) {
    parent_addsystemtaskactivity_swiperefreshlayout.setRefreshing(false);
    showToast(errorResult);
  }
}
