package com.sunjet.utils.quartz;

import java.util.Date;

/**
 * @author lhj
 * @create 2016-01-21 下午3:29
 */
public class TestJobImpl implements TestJob {
    @Override
    public void work() {
        System.out.println("date:" + new Date().toString());
    }
}
