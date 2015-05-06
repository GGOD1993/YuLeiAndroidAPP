package com.example.pc.myapplication.adapter;

/**
 * Created by dada on 15-4-20.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pc.myapplication.R;

import java.util.ArrayList;

/**
 * Created by dada on 15-4-19.
 */
public class StringPickerViewAdapter extends BaseAdapter {

  //存放String的容器
  private ArrayList<String> list;

  //动态映射布局
  private LayoutInflater inflater;

  public StringPickerViewAdapter(Context context, ArrayList<String> list) {
    inflater = LayoutInflater.from(context);
    this.list = list;
  }

  @Override
  public int getCount() {
    if(list != null) return list.size();
    return 0;
  }

  @Override
  public Object getItem(int arg0) {
    return list.get(arg0 % list.size());
  }

  @Override
  public long getItemId(int arg0) {
    return arg0 % list.size();
  }

  @Override
  public View getView(int i, View view, ViewGroup viewGroup) {
    view = inflater.inflate(R.layout.layout_stringpicker_item, null);
    TextView textView = ((TextView) view.findViewById(R.id.textview));
    textView.setText(list.get(i % list.size()));
    return view;
  }
}

