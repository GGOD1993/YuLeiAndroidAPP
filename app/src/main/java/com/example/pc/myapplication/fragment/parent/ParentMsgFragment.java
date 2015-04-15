package com.example.pc.myapplication.fragment.parent;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.TaskInfo.DiyTaskInfo;
import com.example.pc.myapplication.ViewStyle.SpaceItemDecoration;
import com.example.pc.myapplication.activity.TaskInfoActivity;
import com.example.pc.myapplication.activity.parent.ParentAddDiyTaskActivity;
import com.example.pc.myapplication.activity.parent.ParentAddSystemTaskActivity;
import com.example.pc.myapplication.adapter.ParentRecyclerViewAdapter;
import com.example.pc.myapplication.adapter.RecyclerViewItemClickListener;
import com.example.pc.myapplication.utils.HttpService;
import com.shamanland.fab.FloatingActionButton;
import com.shamanland.fab.ShowHideOnScroll;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

public class ParentMsgFragment extends Fragment implements
        RecyclerViewItemClickListener,
        HttpService.OnGetSendDiyTaskRequestResponseListener{

  //从activity中获得的volley请求队列
  private RequestQueue requestQueue;

  //用于显示任务的列表控件
  private RecyclerView recyclerView;

  //在fragment上浮动的添加任务按钮
  private FloatingActionButton floatingActionButton;

  //下拉刷新控件
  private SwipeRefreshLayout mSwipefreshlayout;

  //activity端实现的接口,用于和activity通信
  private OnMsgFragmentInteractionListener mListener;

  //自定义任务arratlist
  public ArrayList<DiyTaskInfo> taskList;

  //recyclerview适配器
  public ParentRecyclerViewAdapter parentRecyclerViewAdapter;

  //SharedPreference
  private SharedPreferences preferences;

  public static ParentMsgFragment newInstance(RequestQueue requestQueue) {
    ParentMsgFragment fragment = new ParentMsgFragment();
    fragment.requestQueue = requestQueue;
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  public ParentMsgFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    View w = inflater.inflate(R.layout.fragment_parent_msg, container, false);
    taskList = new ArrayList<DiyTaskInfo>();
    preferences = getActivity().getSharedPreferences(AppConstant.PREFERENCE_NAME,0);
    initView(w);
    return w;
  }

  private void initView(View w) {
    recyclerView = (RecyclerView) w.findViewById(R.id.parent_msgfragment_recyclerview);
    floatingActionButton = (FloatingActionButton) w.findViewById(R.id.parent_msgfragment_floatingactionbutton);
    mSwipefreshlayout = (SwipeRefreshLayout) w.findViewById(R.id.parent_msgfragment_swipefreshlayout);
    mSwipefreshlayout.setOnRefreshListener(
            new SwipeRefreshLayout.OnRefreshListener() {
              @Override
              public void onRefresh() {
                String url = AppConstant.GET_SEND_DIY_TASK_URL + "?" + AppConstant.USERNAME + "=" +
                        preferences.getString(AppConstant.FROM_USERID, "");
                HttpService.DoGetSendDiyTaskRequest(Request.Method.GET, url, null, ParentMsgFragment.this);
              }
            }
    );
    floatingActionButton.setSize(FloatingActionButton.SIZE_MINI);
    floatingActionButton.setColor(Color.GRAY);
    floatingActionButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.layout_msgfragment_choosetasktype,null);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).setTitle("任 务 模 式").setView(view).show();
        /**
         * 系统任务
         */
        view.findViewById(R.id.parent_msgfragment_choosetasktype_systemtask).setOnClickListener(
                new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    startActivityForResult(new Intent(getActivity(),
                            ParentAddSystemTaskActivity.class), AppConstant.PARENT_ADDSYSTEMTASK_REQUESTCODE);
                    dialog.cancel();
                  }
                }
        );

        /**
         * 自定义任务
         */
        view.findViewById(R.id.parent_msgfragment_choosetasktype_diytask).setOnClickListener(
                new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    startActivityForResult(new Intent(getActivity(),
                            ParentAddDiyTaskActivity.class), AppConstant.PARENT_ADDDIYTASK_REQUESTCODE);
                    dialog.cancel();
                  }
                }
        );
      }
    });
    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
    recyclerView.setLayoutManager(layoutManager);
    parentRecyclerViewAdapter = new ParentRecyclerViewAdapter(taskList,getActivity(), AppConstant.SEND_TASK_TYPE);
    parentRecyclerViewAdapter.setOnItemClickListener(this);
    recyclerView.addItemDecoration(new SpaceItemDecoration(20));
    ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(parentRecyclerViewAdapter);
    scaleAdapter.setFirstOnly(false);
    AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(scaleAdapter);
    alphaAdapter.setFirstOnly(false);
    recyclerView.setAdapter(alphaAdapter);
    recyclerView.setOnTouchListener(new ShowHideOnScroll(floatingActionButton));
    recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
      }
      @Override
      public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (recyclerView.getChildCount() != 0) {
          if (recyclerView.findViewHolderForPosition(0) != null) {
            mSwipefreshlayout.setEnabled(true);
          }
          else {
            mSwipefreshlayout.setEnabled(false);
          }
        }
      }
    });
  }

  @Override
  public void onItemClick(View view, int position) {
    DiyTaskInfo clickTask = taskList.get(position);
    Intent intent = new Intent(getActivity(), TaskInfoActivity.class);
    intent.putExtra(AppConstant.CLICKED_SEND_TASK, clickTask);
    startActivity(intent);
  }

  @Override
  public void OnGetSendDiyTaskSuccessResponse(JSONArray jsonArray) {
    mSwipefreshlayout.setRefreshing(false);
    JSONObject arrayTask;
    DiyTaskInfo taskInfo;
    ArrayList<DiyTaskInfo> submittedTask = new ArrayList<>();
    ArrayList<DiyTaskInfo> finishedTask = new ArrayList<>();
    taskList.clear();
    for (int i = 0 ; i < jsonArray.length() ; i ++) {
      try{
        arrayTask = (JSONObject) jsonArray.get(i);
        taskInfo = new DiyTaskInfo(
                arrayTask.getString(AppConstant.TO_USERID),
                arrayTask.getString(AppConstant.TASK_ID),
                arrayTask.getString(AppConstant.TASK_REGDATE),
                arrayTask.getString(AppConstant.TASK_CONTENT),
                arrayTask.getString(AppConstant.FROM_USERID)
        );
        switch (arrayTask.getInt(AppConstant.TASK_STATUS)) {
          case AppConstant.STATUS_NEW:
            taskList.add(taskInfo);
            break;

          case AppConstant.STATUS_SUBMITTED:
            submittedTask.add(taskInfo);
            break;

          case AppConstant.STATUS_FINISHED:
            finishedTask.add(taskInfo);
            break;
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    taskList.addAll(submittedTask);
    taskList.addAll(finishedTask);
    parentRecyclerViewAdapter.notifyDataSetChanged();
    submittedTask = null;
    finishedTask = null;
//    JSONObject arrayTask;
//    DiyTaskInfo taskInfo;
//    taskList.clear();
//    for (int i = 0 ; i < jsonArray.length() ; i ++) {
//      try{
//        arrayTask = (JSONObject) jsonArray.get(i);
//        taskInfo = new DiyTaskInfo(
//                arrayTask.getString(AppConstant.TO_USERID),
//                arrayTask.getString(AppConstant.TASK_ID),
//                arrayTask.getString(AppConstant.TASK_REGDATE),
//                arrayTask.getString(AppConstant.TASK_CONTENT),
//                arrayTask.getString(AppConstant.FROM_USERID)
//        );
//        taskList.add(taskInfo);
//        parentRecyclerViewAdapter.notifyDataSetChanged();
//      } catch (Exception e) {
//        e.printStackTrace();
//      }
//    }
  }

  @Override
  public void OnGetSendDiyTaskErrorResponse(String errorResult) {
    showToast(errorResult);
  }

  private void showToast(String string) {
    Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
  }

  public interface OnMsgFragmentInteractionListener {
    // TODO: Update argument type and name
    public void onMsgFragmentInteraction(JSONArray jsonArray);
  }
}
