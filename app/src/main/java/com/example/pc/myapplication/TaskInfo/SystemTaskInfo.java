package com.example.pc.myapplication.TaskInfo;

import java.io.Serializable;

public class SystemTaskInfo implements Serializable {

  private String taskId;
  private String taskContent;

  public SystemTaskInfo(String taskId, String taskContent) {
    this.taskId = taskId;
    this.taskContent = taskContent;
  }

  public String getTaskId() {
    return taskId;
  }

  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }

  public String getTaskContent() {
    return taskContent;
  }

  public void setTaskContent(String taskContent) {
    this.taskContent = taskContent;
  }
}
