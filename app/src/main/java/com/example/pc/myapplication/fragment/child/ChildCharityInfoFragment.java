package com.example.pc.myapplication.fragment.child;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.Infos.CharityInfo;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.ViewStyle.SpaceItemDecoration;
import com.example.pc.myapplication.adapter.CharityRecyclerViewAdapter;
import com.example.pc.myapplication.adapter.RecyclerViewItemClickListener;
import com.example.pc.myapplication.utils.HttpService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

public class ChildCharityInfoFragment extends Fragment implements
    HttpService.OnGetCharityRequestResponseListener,
    HttpService.OnDonateRequestListener,
    RecyclerViewItemClickListener {

  //列表
  private RecyclerView recyclerView;

  //下拉刷新
  private SwipeRefreshLayout refreshLayout;

  //RecyclerView的Adapter
  private CharityRecyclerViewAdapter adapter;

  //捐赠项目的列表
  private ArrayList<CharityInfo> charityList;

  private SharedPreferences preferences;

  //回调的监听
  private OnChildFuncFragmentInteractionListener mListener;

  public static ChildCharityInfoFragment newInstance() {
    ChildCharityInfoFragment fragment = new ChildCharityInfoFragment();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  public ChildCharityInfoFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_child_charity_info, container, false);
    preferences = getActivity().getSharedPreferences(AppConstant.PREFERENCE_NAME, 0);
    charityList = new ArrayList<>();
    initView(view);
    return view;
  }

  private void initView(View v) {
    recyclerView = ((RecyclerView) v.findViewById(R.id.child_donatefragment_recyclerview));
    refreshLayout = ((SwipeRefreshLayout) v.findViewById(R.id.child_donatefragment_swiperefreshlayout));

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
    recyclerView.setLayoutManager(linearLayoutManager);
    adapter = new CharityRecyclerViewAdapter(ChildCharityInfoFragment.this, charityList);
    recyclerView.addItemDecoration(new SpaceItemDecoration(40));
    ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(adapter);
    scaleAdapter.setFirstOnly(false);
    AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(scaleAdapter);
    alphaAdapter.setFirstOnly(false);
    recyclerView.setAdapter(alphaAdapter);
    refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        HttpService.DoGetCharityRequest(null, ChildCharityInfoFragment.this);
      }
    });
  }

  @Override
  public void onItemClick(View view, int position) {
    showDonateDialog(position);
  }


  private void showDonateDialog(final int position) {
    final Context context = getActivity();
    final LayoutInflater layoutInflater = LayoutInflater.from(context);
    final View view = layoutInflater.inflate(R.layout.layout_dialog_userinvite, null);
    final EditText editText = (EditText) view.findViewById(R.id.layout_dialog_edittext_tousername);
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setView(view);
    final AlertDialog dialog = builder.create();
    dialog.show();
    ((TextView) view.findViewById(R.id.layout_dialog_userinvite_textview_header)).setText(R.string.donate);
    editText.setHint(R.string.hint_input_number);
    view.findViewById(R.id.layout_dialog_imagebutton_cancel).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
      }
    });
    view.findViewById(R.id.layout_dialog_imagebutton_submit).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (0 != editText.getText().length()) {
          int inputNum = Integer.valueOf(editText.getText().toString());
          int maxNum = Integer.valueOf(preferences.getString(AppConstant.MONEY, ""));
          if ((0 < inputNum) && (inputNum < maxNum)) {
            HashMap<String, String> map = new HashMap<>();
            map.put(AppConstant.USERNAME, preferences.getString(AppConstant.FROM_USERID, ""));
            map.put(AppConstant.SEEDS, String.valueOf(inputNum));
            map.put(AppConstant.CHARITY_NAME, charityList.get(position).getName());
            HttpService.DoDonateRequest(map, ChildCharityInfoFragment.this);
          } else showToast("输入的种子数有误(1 ~ " + maxNum + ")");
        } else showToast("请输入正确的种子数");
      }
    });
  }

  @Override
  public void OnDonateRequestSuccessResponse(JSONArray jsonArray) {
    JSONObject codeObject;
    JSONObject msgObject;
    try {
      codeObject = (JSONObject) jsonArray.get(0);
      msgObject = (JSONObject) jsonArray.get(1);
      if (null != codeObject) {
        switch (codeObject.getInt(AppConstant.RETURN_CODE)) {
          case AppConstant.DONATE_SUCCESS:

            break;
        }
      }
      if (null != msgObject) showToast(msgObject.getString(AppConstant.RETURN_MSG));
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void OnDonateRequestFailedResponse(String errorResult) {
    showToast(errorResult);
  }

  @Override
  public void OnGetCharitySuccessResponse(JSONArray jsonArray) {
    refreshLayout.setRefreshing(false);
    JSONObject project;
    CharityInfo projectInfo;
    charityList.clear();
    for (int i = 0; i < jsonArray.length(); i++) {
      try {
        project = (JSONObject) jsonArray.get(i);
        projectInfo = new CharityInfo(
            project.getString(AppConstant.CHARITY_NAME),
            project.getString(AppConstant.CHARITY_IMG_URL),
            project.getString(AppConstant.CHARITY_BIREF),
            project.getString(AppConstant.CHARITY_CONTACT),
            project.getString(AppConstant.CHARITY_ADDRESS)
        );
        charityList.add(projectInfo);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    adapter.notifyDataSetChanged();
  }

  @Override
  public void OnGetCharityErrorResponse(String errorResult) {
    refreshLayout.setRefreshing(false);
    showToast(errorResult);
  }

  private void showToast(String string) {
    Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      mListener = (OnChildFuncFragmentInteractionListener) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException(activity.toString()
          + " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    mListener = null;
    super.onDetach();
  }

  public interface OnChildFuncFragmentInteractionListener {
    public void onChildFuncFragmentInteraction();
  }
}
