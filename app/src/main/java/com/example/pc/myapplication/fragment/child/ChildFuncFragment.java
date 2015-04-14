package com.example.pc.myapplication.fragment.child;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.ViewStyle.CircularImage;
import com.example.pc.myapplication.activity.child.ChildDiaryActivity;
import com.example.pc.myapplication.activity.child.ChildDonateActivity;
import com.example.pc.myapplication.activity.child.ChildSettingActivity;
import com.example.pc.myapplication.activity.child.ChildWishActivity;

public class ChildFuncFragment extends Fragment {

  //sharedPreference
  private SharedPreferences preferences;

  //头像框
  private CircularImage circularImageUserImage;

  //用户名
  private TextView textViewUsername;

  //显示剩余金钱数
  private TextView textViewMoney;

  //心愿按钮
  private ImageButton imageButtonWish;

  //日记按钮
  private ImageButton imageButtonDiary;

  //捐赠按钮
  private ImageButton imageButtonDonate;

  //更多设置按钮
  private ImageButton imageButtonSetting;

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
    // Required empty public constructor
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
    super.onDetach();
    mListener = null;
  }

  public interface OnChildFuncFragmentInteractionListener {
    // TODO: Update argument type and name
    public void onChildFuncFragmentInteraction();
  }

  private void initView(View w) {

    circularImageUserImage= (CircularImage) w.findViewById(R.id.child_funcfragment_circularimage_userimage);
    circularImageUserImage.setImageResource(R.mipmap.ic_launcher);
    circularImageUserImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getActivity().getSharedPreferences(AppConstant.PREFERENCE_NAME,0)
                .edit().putInt(AppConstant.USER_MODE, 0).apply();
        Toast.makeText(getActivity(), "logout success", Toast.LENGTH_SHORT).show();;
      }
    });

    textViewUsername = (TextView) w.findViewById(R.id.child_funcfragment_textview_username);
    textViewUsername.setText(preferences.getString(AppConstant.FROM_USERID, ""));
    textViewMoney = (TextView) w.findViewById(R.id.child_funcfragment_textview_money);
    imageButtonWish = (ImageButton) w.findViewById(R.id.child_funcfragment_imagebutton_wish);
    imageButtonWish.setBackground(
            new BitmapDrawable(AppConstant.readBitMap(getActivity(), R.mipmap.child_funcfragment_wish)));
    imageButtonWish.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getActivity(), ChildWishActivity.class);
        startActivity(intent);
      }
    });

    imageButtonDiary= (ImageButton) w.findViewById(R.id.child_funcfragment_imagebutton_diary);
    imageButtonDiary.setBackground(
            new BitmapDrawable(AppConstant.readBitMap(getActivity(), R.mipmap.child_funcfragment_diary)));
    imageButtonDiary.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getActivity(), ChildDiaryActivity.class);
        startActivity(intent);
      }
    });

    imageButtonDonate = (ImageButton) w.findViewById(R.id.child_funcfragment_imagebutton_donate);
    imageButtonDonate.setBackground(
            new BitmapDrawable(AppConstant.readBitMap(getActivity(), R.mipmap.child_funcfragment_donate)));
    imageButtonDonate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getActivity(), ChildDonateActivity.class);
        startActivity(intent);
      }
    });

    imageButtonSetting = (ImageButton) w.findViewById(R.id.child_funcfragment_imagebutton_setting);
    imageButtonSetting.setBackground(
            new BitmapDrawable(AppConstant.readBitMap(getActivity(), R.mipmap.child_funcfragment_setting)));
    imageButtonSetting.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getActivity(), ChildSettingActivity.class);
        startActivity(intent);
      }
    });
  }

}
