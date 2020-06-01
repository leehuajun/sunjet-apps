package com.sunjet.quartz;

import com.sunjet.service.admin.UserService;
import com.sunjet.utils.common.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 定时任务运行工厂类
 * <p>
 * Created by lhj on 16/6/20.
 */
//@Component("sampleJob")
public class SampleJob implements Executor {
    @Autowired
    private UserService userService;

//    @Override
//    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        ScheduleJobEntity scheduleJob = (ScheduleJobEntity) jobExecutionContext.getMergedJobDataMap().get("scheduleJob");
//        System.out.println("任务名称 = " + scheduleJob.getJobName());
//        System.out.println("用户数量 = " + userService.findAll().size());
//    }

    @Override
    public void doExecute() {
        LoggerUtil.getLogger().info("用户数量 = " + userService.findAll().size());
    }
}
