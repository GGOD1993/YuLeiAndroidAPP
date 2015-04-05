package com.example.pc.myapplication.adapter;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.view.ViewGroup;

import com.example.pc.myapplication.R;
import com.example.pc.myapplication.activity.parent.ParentMainActivity;

import java.util.List;

public class ParentViewPagerAdapter extends FragmentPagerAdapter {

  private ParentMainActivity activity;
  private List<Fragment> fragmentList;

  private List<String> titleList;

  public ParentViewPagerAdapter(ParentMainActivity activity, FragmentManager fragmentManager,
                                List<Fragment> fragmentList, List<String> titleList) {

    super(fragmentManager);
    this.activity = activity;
    this.fragmentList = fragmentList;
    this.titleList = titleList;
  }

  @Override
  public Fragment getItem(int arg0) {
    return (fragmentList == null || fragmentList.size() == 0)
            ? null : fragmentList.get(arg0);
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
  }

  @Override
  public CharSequence getPageTitle(int position) {
//        return (titleList.size() > position) ? titleList.get(position) : "";

    SpannableStringBuilder ssb = new SpannableStringBuilder("  "+titleList.get(position)); // space added before text
    // for
    Drawable myDrawable = activity.getResources().getDrawable(
            R.mipmap.ic_tab_image);
    myDrawable.setBounds(0, 0, myDrawable.getIntrinsicWidth(),
            myDrawable.getIntrinsicHeight());
    ImageSpan span = new ImageSpan(myDrawable,
            ImageSpan.ALIGN_BASELINE);

    ForegroundColorSpan fcs = new ForegroundColorSpan(activity.getResources().getColor(R.color.mediumslateblue));// 字体颜色设置为绿色
    ssb.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);// 设置图标
    ssb.setSpan(fcs, 1, ssb.length(),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);// 设置字体颜色
    ssb.setSpan(new RelativeSizeSpan(1.2f), 1, ssb.length(),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    return ssb;
  }

  @Override
  public int getCount() {
    return fragmentList == null ? 0 : fragmentList.size();
  }


}
