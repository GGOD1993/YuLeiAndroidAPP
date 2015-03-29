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

    //屏幕上的组件
    private CircularImage child_funcfragment_circularimage_userimage;
    private TextView child_funcfragment_textview_username;
    private TextView child_funcfragment_textview_money;
    private ImageButton child_funcfragment_imagebutton_wish;
    private ImageButton child_funcfragment_imagebutton_diary;
    private ImageButton child_funcfragment_imagebutton_donate;
    private ImageButton child_funcfragment_imagebutton_setting;

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

        child_funcfragment_circularimage_userimage = (CircularImage) w.findViewById(R.id.child_funcfragment_circularimage_userimage);
        child_funcfragment_circularimage_userimage.setImageResource(R.mipmap.ic_launcher);

        child_funcfragment_textview_username = (TextView) w.findViewById(R.id.child_funcfragment_textview_username);
        child_funcfragment_textview_username.setText(preferences.getString(AppConstant.FROM_USERID, ""));

        child_funcfragment_textview_money = (TextView) w.findViewById(R.id.child_funcfragment_textview_money);

        child_funcfragment_imagebutton_wish = (ImageButton) w.findViewById(R.id.child_funcfragment_imagebutton_wish);
        child_funcfragment_imagebutton_wish.setBackground(
                new BitmapDrawable(AppConstant.readBitMap(getActivity(), R.mipmap.child_funcfragment_wish)));
        child_funcfragment_imagebutton_wish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChildWishActivity.class);
                startActivity(intent);
            }
        });


        child_funcfragment_imagebutton_diary = (ImageButton) w.findViewById(R.id.child_funcfragment_imagebutton_diary);
        child_funcfragment_imagebutton_diary.setBackground(
                new BitmapDrawable(AppConstant.readBitMap(getActivity(), R.mipmap.child_funcfragment_diary)));
        child_funcfragment_imagebutton_diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChildDiaryActivity.class);
                startActivity(intent);
            }
        });

        child_funcfragment_imagebutton_donate = (ImageButton) w.findViewById(R.id.child_funcfragment_imagebutton_donate);
        child_funcfragment_imagebutton_donate.setBackground(
                new BitmapDrawable(AppConstant.readBitMap(getActivity(), R.mipmap.child_funcfragment_donate)));
        child_funcfragment_imagebutton_donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChildDonateActivity.class);
                startActivity(intent);
            }
        });

        child_funcfragment_imagebutton_setting = (ImageButton) w.findViewById(R.id.child_funcfragment_imagebutton_setting);
        child_funcfragment_imagebutton_setting.setBackground(
                new BitmapDrawable(AppConstant.readBitMap(getActivity(), R.mipmap.child_funcfragment_setting)));
        child_funcfragment_imagebutton_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChildSettingActivity.class);
                startActivity(intent);
            }
        });


    }

}
