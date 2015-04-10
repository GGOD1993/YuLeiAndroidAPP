package com.example.pc.myapplication.fragment.child;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.ViewStyle.ActiveView;
import com.example.pc.myapplication.ViewStyle.ActiveViewGroup;
import com.example.pc.myapplication.ViewStyle.RefreshableLinearLayout;
import com.example.pc.myapplication.utils.ActiveHelper;
import com.example.pc.myapplication.utils.HttpService;

import org.json.JSONArray;

public class ChildMsgFragment extends Fragment implements
        HttpService.OnGetDiyTaskRequestResponseListener{

  private Button add;
  private Button delete;
  private Button start;
  private Button stop;

  private ActiveViewGroup activeViewGroup;

  private ActiveHelper activeHelper;

  private RefreshableLinearLayout refreshableLinearLayout;

  private OnChildMsgFragmentInteractionListener mListener;

  public static ChildMsgFragment newInstance() {
    ChildMsgFragment fragment = new ChildMsgFragment();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  public ChildMsgFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    View v = inflater.inflate(R.layout.fragment_child_msg, container, false);
    add = (Button) v.findViewById(R.id.add);
    delete = (Button) v.findViewById(R.id.delete);
    start = (Button) v.findViewById(R.id.start);
    stop = (Button) v.findViewById(R.id.stop);
    activeViewGroup = (ActiveViewGroup) v.findViewById(R.id.activeViewGroup);
    activeHelper = new ActiveHelper(activeViewGroup);
    refreshableLinearLayout = (RefreshableLinearLayout) v.findViewById(R.id.refreshable_linearlayout);

    refreshableLinearLayout.setOnRefreshListener(new RefreshableLinearLayout.PullToRefreshListener() {
      @Override
      public void onRefresh() {
          //存放下拉刷新需要执行的内容
        refreshableLinearLayout.finishRefreshing();
      }
    });


    add.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ActiveView activeView = new ActiveView(getActivity());
        activeView.setImageResource(R.mipmap.ic_launcher);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        activeView.setLayoutParams(layoutParams);
        activeViewGroup.addActiveView(activeView);
      }
    });
    delete.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (activeViewGroup.getChildCount() != 0) {
          activeViewGroup.removeActiveViewAt(activeViewGroup.getChildCount() - 1);
        }
      }
    });
    start.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        activeHelper.startMove();
      }
    });
    stop.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        activeHelper.stopMove();
      }
    });
    return v;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      mListener = (OnChildMsgFragmentInteractionListener) activity;
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

  public interface OnChildMsgFragmentInteractionListener {
    // TODO: Update argument type and name
    public void onChildMsgFragmentInteraction();
  }

  public void startMoveActiveView() {
    activeHelper.stopMove();
  }

  public void stopMoveActiveView() {
    activeHelper.stopMove();
  }

  private void postGetDiyTaskRequest() {

    String url = AppConstant.GET_DIY_TASK_URL + "?username=" +
            getActivity().getSharedPreferences(AppConstant.PREFERENCE_NAME,0)
                    .getString(AppConstant.FROM_USERID, "");

    HttpService.DoGetDiyTaskRequest(Request.Method.GET, url, null, ChildMsgFragment.this);
  }

  @Override
  public void OnGetDiyTaskSuccessResponse(JSONArray jsonArray) {

  }

  @Override
  public void OnGetDiyTaskErrorResponse(String errorResult) {

  }
}
