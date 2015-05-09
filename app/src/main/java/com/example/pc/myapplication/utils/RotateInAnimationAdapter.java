package com.example.pc.myapplication.utils;

import android.animation.Animator;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import jp.wasabeef.recyclerview.animators.adapters.AnimationAdapter;

/**
 * Created by dada on 15-5-10.
 */
public class RotateInAnimationAdapter extends AnimationAdapter {

  public RotateInAnimationAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
    super(adapter);
  }

  @Override
  protected Animator[] getAnimators(View view) {
//    return new ObjectAnimator.ofFloat(view, )
    return null;
  }
}
