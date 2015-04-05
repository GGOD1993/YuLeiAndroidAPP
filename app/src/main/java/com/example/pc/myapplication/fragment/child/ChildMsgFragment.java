package com.example.pc.myapplication.fragment.child;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.pc.myapplication.R;
import com.example.pc.myapplication.ViewStyle.ActiveView;
import com.example.pc.myapplication.ViewStyle.ActiveViewGroup;
import com.example.pc.myapplication.utils.ActiveHelper;

public class ChildMsgFragment extends Fragment {

  private Button add;
  private Button delete;
  private Button start;
  private Button stop;

  private ActiveViewGroup activeViewGroup;

  private ActiveHelper activeHelper;


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

}
