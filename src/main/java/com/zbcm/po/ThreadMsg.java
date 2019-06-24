package com.zbcm.po;

/**
 * Created by zbcm on 2017-09-24.
 * 线程池中线程数量信息
 */
public class ThreadMsg {
    private int activeCount;
    private long taskCount;
    private int completeTaskNum;

    public ThreadMsg(int activeCount, long taskCount, int completeTaskNum) {
        this.activeCount = activeCount;
        this.taskCount = taskCount;
        this.completeTaskNum = completeTaskNum;
    }

    public ThreadMsg() {
    }

    public int getCompleteTaskNum() {
        return completeTaskNum;
    }

    public void setCompleteTaskNum(int completeTaskNum) {
        this.completeTaskNum = completeTaskNum;
    }

    public int getActiveCount() {
        return activeCount;
    }

    public void setActiveCount(int activeCount) {
        this.activeCount = activeCount;
    }

    public long getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(long taskCount) {
        this.taskCount = taskCount;
    }

    @Override
    public String toString() {
        return "活动线程数 >>> activeCount=" + activeCount +
                ", 任务线程数 >> taskCount=" + taskCount;
    }
}
