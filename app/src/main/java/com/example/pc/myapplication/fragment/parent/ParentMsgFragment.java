package com.example.pc.myapplication.fragment.parent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.R;

import org.json.JSONArray;

public class ParentMsgFragment extends Fragment {

  //activity端实现的接口,用于和activity通信
  private OnMsgFragmentInteractionListener mListener;

  //SharedPreference
  private SharedPreferences preferences;

  public static ParentMsgFragment newInstance() {
    ParentMsgFragment fragment = new ParentMsgFragment();
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
    preferences = getActivity().getSharedPreferences(AppConstant.PREFERENCE_NAME,0);
    initView(w);
    return w;
  }

  private void initView(View w) {
  }

  private void showToast(String string) {
    Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
  }

  public interface OnMsgFragmentInteractionListener {
    public void onMsgFragmentInteraction(JSONArray jsonArray);
  }
}
