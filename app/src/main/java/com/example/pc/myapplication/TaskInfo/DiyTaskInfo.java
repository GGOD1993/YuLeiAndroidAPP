package com.example.pc.myapplication.TaskInfo;

import java.io.Serializable;

public class DiyTaskInfo implements Serializable {

  private String award;
  private String taskName;
  private String toUserId;
  private String taskContent;

  public DiyTaskInfo(String toUserId, String taskName, String award, String taskContent) {

    this.award = award;
    this.taskName = taskName;
    this.toUserId = toUserId;
    this.taskContent = taskContent;
  }

  public String getToUserId() {
    return toUserId;
  }

  public void setToUserId(String toUserId) {
    this.toUserId = toUserId;
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
}
