package com.example.pc.myapplication.TaskInfo;

import android.os.Parcel;
import android.os.Parcelable;


public class DiyTaskInfo implements Parcelable{

  private String award;
  private String taskName;
  private String toUserId;
  private String fromUserId;
  private String taskContent;
  private int taskStatus;

  public DiyTaskInfo(String toUserId, String taskName, String award, String taskContent, String fromUserId, int taskStatus) {
    this.award = award;
    this.taskName = taskName;
    this.toUserId = toUserId;
    this.taskContent = taskContent;
    this.fromUserId = fromUserId;
    this.taskStatus = taskStatus;
  }

  public int getTaskStatus() {
    return taskStatus;
  }

  public void setTaskStatus(int taskStatus) {
    this.taskStatus = taskStatus;
  }

  public String getToUserId() {
    return toUserId;
  }

  public void setToUserId(String toUserId) {
    this.toUserId = toUserId;
  }

  public String getFromUserId() {
    return fromUserId;
  }

  public void setFromUserId(String fromUserId) {
    this.fromUserId = fromUserId;
  }

  public String getTaskName() {
    return taskName;
  }

  public void setTaskName(String taskName) {
    this.taskName = taskName;
  }

  public String getAward() {
    return award;
  }

  public void setAward(String award) {
    this.award = award;
  }

  public String getTaskContent() {
    return taskContent;
  }

  public void setTaskContent(String taskContent) {
    this.taskContent = taskContent;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(toUserId);
    dest.writeString(taskName);
    dest.writeString(award);
    dest.writeString(taskContent);
    dest.writeString(fromUserId);
    dest.writeInt(taskStatus);
  }

  public static final Parcelable.Creator<DiyTaskInfo> CREATOR = new Creator<DiyTaskInfo>() {

    @Override
    public DiyTaskInfo createFromParcel(Parcel source) {
      return new DiyTaskInfo(source.readString(), source.readString(), source.readString(), source.readString(), source.readString(), source.readInt());
    }

    @Override
    public DiyTaskInfo[] newArray(int size) {
      return new DiyTaskInfo[size];
    }
  };
}
