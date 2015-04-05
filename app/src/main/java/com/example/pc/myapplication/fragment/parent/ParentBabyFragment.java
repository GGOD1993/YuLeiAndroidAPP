package com.example.pc.myapplication.fragment.parent;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pc.myapplication.R;

public class ParentBabyFragment extends Fragment {

  private OnBabyFragmentInteractionListener mListener;

  public static ParentBabyFragment newInstance() {
    ParentBabyFragment fragment = new ParentBabyFragment();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  public ParentBabyFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_parent_baby, container, false);



  }

  // TODO: Rename method, update argument and hook method into UI event
  public void onButtonPressed() {
    if (mListener != null) {
      mListener.onBabyFragmentInteraction();
    }
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      mListener = (OnBabyFragmentInteractionListener) activity;
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

  public interface OnBabyFragmentInteractionListener {
    // TODO: Update argument type and name
    public void onBabyFragmentInteraction();
  }

}
