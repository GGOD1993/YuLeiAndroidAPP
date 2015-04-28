package com.example.pc.myapplication.utils;

import android.os.AsyncTask;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.ViewStyle.ActiveViewGroup;

public class CountTimeAsyncTask extends AsyncTask<Void, Integer, Void> {

  //剩余时间
  private int resultTime;

  //显示剩余时间的标题
  private TextView title;

  //种子的父控件
  private ActiveViewGroup activeViewGroup;

  //任务完成的回调接口
  private OnAsyncTaskCompleteListener mListener;

  public CountTimeAsyncTask(TextView textViewTitle, ActiveViewGroup activeViewGroup, OnAsyncTaskCompleteListener listener) {
    super();
    this.title = textViewTitle;
    this.activeViewGroup = activeViewGroup;
    this.mListener = listener;
  }

  /**
   * doInBackground之前调用
   */
  @Override
  protected void onPreExecute() {
    super.onPreExecute();
    resultTime = 30;
    title.setText("剩余时间:" + resultTime);
  }

  @Override
  protected Void doInBackground(Void... params) {
    while (resultTime >= 1) {
      try {
        Thread.sleep(1000);
      } catch (Exception e) {
        e.printStackTrace();
      }
      publishProgress(--resultTime);
    }
    return null;
  }

  @Override
  protected void onProgressUpdate(Integer... values) {
    if (AppConstant.ONLAYOUT_MODE_NONE != activeViewGroup.getMode()){
      activeViewGroup.setMode(AppConstant.ONLAYOUT_MODE_NONE);
    }
    if (10 > values[0]) YoYo.with(Techniques.Flash).duration(300).playOn(title);
    title.setText("剩余时间:" + values[0]);
  }

  @Override
  protected void onPostExecute(Void aVoid) {
    mListener.asyncTaskComplete();
    super.onPostExecute(aVoid);
  }

  public interface OnAsyncTaskCompleteListener {
    public void asyncTaskComplete();
  }
}
