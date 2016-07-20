package com.zhengweihao.lock.synchronize;

/**
 * Created by ZhengWeihao on 16/7/17.
 */
public class Demo {

    private static final Object syncObj = new Object();

    public synchronized void syncMethod() {
        // do something ..
    }

    public void normalMethod() {
        synchronized (syncObj) {
            // do something ..
        }

        synchronized (syncObj.getClass()) {
            // do something ..
        }
    }
}
