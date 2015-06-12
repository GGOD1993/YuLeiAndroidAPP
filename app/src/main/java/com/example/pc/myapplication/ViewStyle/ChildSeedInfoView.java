package com.example.pc.myapplication.ViewStyle;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.pc.myapplication.R;
import com.wangjie.rapidfloatingactionbutton.contentimpl.viewbase.RapidFloatingActionContentViewBase;

public class ChildSeedInfoView extends RapidFloatingActionContentViewBase {

  //上下文引用
  private Context context;

  //圆形展开的自定义view
  private RelativeLayout contentView;

  public ChildSeedInfoView(Context context) {
    super(context);
    this.context = context;
  }

  public ChildSeedInfoView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public ChildSeedInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public ChildSeedInfoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @NonNull
  @Override
  protected View getContentView() {
    contentView = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.layout_seed_info, null);
    return contentView;
  }
}
