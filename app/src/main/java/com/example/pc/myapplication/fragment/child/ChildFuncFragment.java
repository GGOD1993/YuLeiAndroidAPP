package com.example.pc.myapplication.fragment.child;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pc.myapplication.R;

public class ChildFuncFragment extends Fragment {

  //sharedPreference
  private SharedPreferences preferences;

  //回调的监听
  private OnChildFuncFragmentInteractionListener mListener;

  public static ChildFuncFragment newInstance(SharedPreferences preferences) {
    ChildFuncFragment fragment = new ChildFuncFragment();
    fragment.preferences = preferences;
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }
  public ChildFuncFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_child_func, container, false);
    initView(view);
    return view;
  }

  private void initView(View w) {

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
