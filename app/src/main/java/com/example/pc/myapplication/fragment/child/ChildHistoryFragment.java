package com.example.pc.myapplication.fragment.child;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.R;

public class ChildHistoryFragment extends Fragment {

  //SharedPreference
  private SharedPreferences preferences;

  //activity中实现的回调接口
  private OnChildHistoryFragmentInteractionListener mListener;

  public static ChildHistoryFragment newInstance() {
    ChildHistoryFragment fragment = new ChildHistoryFragment();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  public ChildHistoryFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    View v = inflater.inflate(R.layout.fragment_child_history, container, false);
    preferences = getActivity().getSharedPreferences(AppConstant.PREFERENCE_NAME, 0);
    return v;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      mListener = (OnChildHistoryFragmentInteractionListener) activity;
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

  public interface OnChildHistoryFragmentInteractionListener {
    public void onChildHistoryFragmentInteraction();
  }
}
