package com.example.pc.myapplication.TaskInfo;

import java.io.Serializable;

public class DiyTaskInfo implements Serializable {

    private String award;
    private String taskName;
    private String childId;
    private String taskContent;

    public DiyTaskInfo(String childId, String taskName, String award, String taskContent) {

        this.award = award;
        this.taskName = taskName;
        this.childId = childId;
        this.taskContent = taskContent;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
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
