package com.example.pc.myapplication.fragment.child;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.pc.myapplication.R;
import com.example.pc.myapplication.activity.child.ChildWishListActivity;

public class ChildTaskFragment extends Fragment {

  //查看父母发布的心愿入口
  private ImageButton imageButtonParent;

  //科学版块入口
  private ImageButton imageButtonScience;

  //音乐版块入口
  private ImageButton imageButtonMusic;

  //随机入口
  private ImageButton imageButtonRandom;

  //书籍入口
  private ImageButton imageButtonBook;

  //activity回调接口
  private onChildTaskFragmentInteractionListener mListener;

  public static ChildTaskFragment newInstance() {
    ChildTaskFragment fragment = new ChildTaskFragment();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  public ChildTaskFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_child_task, container, false);
    initViews(view);
    return view;
  }

  private void initViews(View v) {
    imageButtonParent = (ImageButton) v.findViewById(R.id.child_taskfragment_imagebutton_parent);
    imageButtonBook = (ImageButton) v.findViewById(R.id.child_taskfragment_imagebutton_book);
    imageButtonMusic = (ImageButton) v.findViewById(R.id.child_taskfragment_imagebutton_music);
    imageButtonRandom = (ImageButton) v.findViewById(R.id.child_taskfragment_imagebutton_random);
    imageButtonScience = (ImageButton) v.findViewById(R.id.child_taskfragment_imagebutton_science);

    imageButtonParent.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getActivity(), ChildWishListActivity.class);
        startActivity(intent);
      }
    });

    imageButtonBook.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showToast("该版块正在开发");
      }
    });

    imageButtonMusic.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showToast("该版块正在开发");
      }
    });

    imageButtonRandom.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showToast("该版块正在开发");
      }
    });

    imageButtonScience.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showToast("该版块正在开发");
      }
    });
  }

  private void showToast(String string) {
    Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
  }


  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      mListener = (onChildTaskFragmentInteractionListener) activity;
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

  public interface onChildTaskFragmentInteractionListener {
    public void onChildTaskFragmentInteraction();
  }

}
