package com.example.pc.myapplication.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.example.pc.myapplication.activity.parent.ParentMainActivity;

import java.util.List;

public class ParentViewPagerAdapter extends FragmentPagerAdapter {

  //所在的activity引用
  private ParentMainActivity activity;

  //ViewPager中存放的fragment集合
  private List<Fragment> fragmentList;

  public ParentViewPagerAdapter(ParentMainActivity activity, FragmentManager fragmentManager,
                                List<Fragment> fragmentList) {
    super(fragmentManager);
    this.activity = activity;
    this.fragmentList = fragmentList;
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
  public int getCount() {
    return fragmentList == null ? 0 : fragmentList.size();
  }


}
