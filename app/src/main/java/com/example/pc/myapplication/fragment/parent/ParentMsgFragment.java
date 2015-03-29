package com.example.pc.myapplication.fragment.parent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.TaskInfo.DiyTaskInfo;
import com.example.pc.myapplication.activity.parent.ParentAddDiyTaskActivity;
import com.example.pc.myapplication.activity.parent.ParentAddSystemTaskActivity;
import com.example.pc.myapplication.activity.TaskInfoActivity;
import com.example.pc.myapplication.adapter.ParentRecyclerViewAdapter;
import com.example.pc.myapplication.adapter.RecyclerViewItemClickListener;
import com.example.pc.myapplication.utils.JsonArrayRequestPlus;
import com.shamanland.fab.FloatingActionButton;
import com.shamanland.fab.ShowHideOnScroll;

import org.json.JSONArray;

import java.util.ArrayList;

public class ParentMsgFragment extends Fragment implements RecyclerViewItemClickListener{

    //从activity中获得的volley请求队列
    private RequestQueue requestQueue;

    //用于显示任务的列表控件
    private RecyclerView recyclerView;

    //在fragment上浮动的添加任务按钮
    private FloatingActionButton parent_msgfragment_floatingactionbutton;

    //下拉刷新控件
    private SwipeRefreshLayout parent_msgfragment_swipefreshlayout;

    //activity端实现的接口,用于和activity通信
    private OnMsgFragmentInteractionListener mListener;

    //自定义任务arratlist
    public ArrayList<DiyTaskInfo> taskList;

    //recyclerview适配器
    public ParentRecyclerViewAdapter parentRecyclerViewAdapter;

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

        View w = inflater.inflate(R.layout.fragment_parent_msg,container,false);

        taskList = new ArrayList<DiyTaskInfo>();

        initView(w);
        return w;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnMsgFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnMsgFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onMsgFragmentInteraction(JSONArray jsonArray);
    }

    private void initView(View w) {

        recyclerView = (RecyclerView) w.findViewById(R.id.parent_msgfragment_recyclerview);
        parent_msgfragment_floatingactionbutton = (FloatingActionButton) w.findViewById(R.id.parent_msgfragment_floatingactionbutton);
        parent_msgfragment_swipefreshlayout = (SwipeRefreshLayout) w.findViewById(R.id.parent_msgfragment_swipefreshlayout);

        parent_msgfragment_swipefreshlayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        postGetDiyTaskRequest();
                    }
                }
        );

        parent_msgfragment_floatingactionbutton.setSize(FloatingActionButton.SIZE_MINI);
        parent_msgfragment_floatingactionbutton.setColor(Color.GRAY);
        parent_msgfragment_floatingactionbutton.setOnClickListener(new View.OnClickListener() {
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
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(false);

        parentRecyclerViewAdapter = new ParentRecyclerViewAdapter(taskList,getActivity());
        parentRecyclerViewAdapter.setOnItemClickListener(this);

        recyclerView.setAdapter(parentRecyclerViewAdapter);
        recyclerView.setOnTouchListener(new ShowHideOnScroll(parent_msgfragment_floatingactionbutton));
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getChildCount() != 0) {
                    if (recyclerView.findViewHolderForPosition(0) != null) {
                        parent_msgfragment_swipefreshlayout.setEnabled(true);
                    }
                    else {
                        parent_msgfragment_swipefreshlayout.setEnabled(false);
                    }
                }
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {

        DiyTaskInfo clickTask = taskList.get(position);
        Intent intent = new Intent(getActivity(), TaskInfoActivity.class);
        intent.putExtra("clickTask", clickTask);
        startActivity(intent);
    }

    private void postGetDiyTaskRequest() {

        String url = AppConstant.GET_DIY_TASK_URL + "?username=" +
                getActivity().getSharedPreferences(AppConstant.PREFERENCE_NAME,0)
                        .getString(AppConstant.FROM_USERID, "");

        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {

                mListener.onMsgFragmentInteraction(jsonArray);
                parent_msgfragment_swipefreshlayout.setRefreshing(false);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                showToast(volleyError.toString());
            }
        };

        JsonArrayRequestPlus getDiyTaskRequest = new JsonArrayRequestPlus(
                url,
                listener,
                errorListener
        );

        requestQueue.add(getDiyTaskRequest);
    }

    private void showToast(String string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
    }
}
