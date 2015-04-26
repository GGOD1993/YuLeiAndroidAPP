package com.example.pc.myapplication.activity.child;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.ViewStyle.ActiveGameView;
import com.example.pc.myapplication.ViewStyle.ActiveView;
import com.example.pc.myapplication.ViewStyle.ActiveViewGroup;
import com.example.pc.myapplication.utils.ActiveHelper;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class ChildWishActivity extends SwipeBackActivity {

  //布局的header
  private RelativeLayout header;

  //title
  private TextView title;

  //开始按钮
  private ImageButton imageButtonStart;

  //返回按钮
  private ImageButton imageButtonBack;

  //心愿背包
  private ImageButton imageButtonWishBag;

  //漂浮心愿的ViewGroup
  private ActiveViewGroup activeViewGroup;

  //漂浮助手
  private ActiveHelper activeHelper;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_child_wish);
    initViews();
  }

  /**
   * 初始化控件
   */
  private void initViews() {
    header = (RelativeLayout) findViewById(R.id.child_wishactivity_header);
    title = (TextView) findViewById(R.id.child_wishactivity_textview_starttext);
    imageButtonStart = (ImageButton) findViewById(R.id.child_wishactivity_imagebutton_start);
    imageButtonBack = (ImageButton) findViewById(R.id.child_wishactivity_imagebutton_back);
    imageButtonWishBag = (ImageButton) findViewById(R.id.child_wishactivity_imagebutton_wishbag);
    activeViewGroup = (ActiveViewGroup) findViewById(R.id.child_wishactivity_activeviewgroup);
    imageButtonStart = ((ImageButton) findViewById(R.id.child_wishactivity_imagebutton_start));
    imageButtonBack = ((ImageButton) findViewById(R.id.child_wishactivity_imagebutton_back));
    activeHelper = new ActiveHelper(activeViewGroup);

    ((TextView) header.findViewById(R.id.child_mainactivity_header_textview)).setText("心 愿 种 子");
    imageButtonStart.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        goneAnim();
        addActiveViews();
        activeHelper.startGameModeMove();
      }
    });
    imageButtonBack.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
    startInitAnim();
  }

  /**
   * 初始化动画
   */
  private void startInitAnim() {
    YoYo.with(Techniques.FadeIn).duration(2000).playOn(title);
    YoYo.with(Techniques.FadeIn).duration(2000).playOn(imageButtonStart);
    YoYo.with(Techniques.FadeIn).duration(2000).playOn(imageButtonBack);
  }

  /**
   * 消失动画
   */
  private void goneAnim() {
    YoYo.with(Techniques.FadeOut).duration(2000).playOn(title);
    YoYo.with(Techniques.FadeOut).duration(2000).playOn(imageButtonStart);
    YoYo.with(Techniques.FadeOut).duration(2000).playOn(imageButtonBack);
    imageButtonWishBag.setVisibility(View.VISIBLE);
    YoYo.with(Techniques.FadeIn).duration(2000).playOn(imageButtonWishBag);
  }

  /**
   * 添加控件
   */
  private void addActiveViews() {
    activeViewGroup.setMode(AppConstant.ONLAYOUT_MODE_FROM_DOWN);
    activeViewGroup.removeAllViews();
    ActiveGameView activeGameView;
    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    for (int i = 0; i < 10; i++) {
      activeGameView = new ActiveGameView(getApplicationContext());
      activeGameView.setLayoutParams(layoutParams);
      activeGameView.setImageResource(R.mipmap.ic_launcher);
      activeViewGroup.addActiveView(activeGameView);
    }
  }
}
