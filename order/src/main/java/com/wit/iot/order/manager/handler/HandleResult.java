package com.wit.iot.order.manager.handler;

public class HandleResult {

    public HandleResult(Integer count, String threadName) {
        this.count = count;
        this.threadName = threadName;
    }

    private Integer count;
    private String threadName;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }
}
