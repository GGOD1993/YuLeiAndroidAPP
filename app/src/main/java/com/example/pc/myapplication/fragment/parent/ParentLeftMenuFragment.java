package com.example.pc.myapplication.fragment.parent;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.pc.myapplication.R;
import com.example.pc.myapplication.activity.parent.ParentMoreSettingActivity;

public class ParentLeftMenuFragment extends Fragment{

    private RelativeLayout parentactivity_leftmenu_relativelayout_moresetting;
    private RelativeLayout parentactivity_leftmenu_relativelayout_addtask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View v = inflater.inflate(R.layout.fragment_parent_left_menu, container, false);

        initView(v);
        return v;
    }

    private void initView(View v) {

        parentactivity_leftmenu_relativelayout_moresetting = (RelativeLayout)
                v.findViewById(R.id.parentactivity_leftmenu_relativelayout_moresetting);
        parentactivity_leftmenu_relativelayout_moresetting
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(getActivity(), ParentMoreSettingActivity.class));
                    }
                });

        parentactivity_leftmenu_relativelayout_addtask = (RelativeLayout)
                v.findViewById(R.id.parentactivity_leftmenu_relativelayout_addtask);
        parentactivity_leftmenu_relativelayout_addtask
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        startActivity(new Intent(getActivity(), ParentAddTaskActivity.class));

                    }
                });
    }

}
