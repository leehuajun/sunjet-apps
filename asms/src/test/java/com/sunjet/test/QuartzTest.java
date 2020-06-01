package com.sunjet.test;

import com.sunjet.model.admin.ScheduleJobEntity;
import com.sunjet.repository.admin.ScheduleJobRepository;
import com.sunjet.utils.quartz.SampleJob;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author lhj
 * @create 2016-02-01 下午3:52
 */
public class QuartzTest {
    private ApplicationContext context = null;
//  private static Map<String,ScheduleJob> jobMap = new HashMap<>();
//  static{
//    for(int i=1;i<=5;i++){
//      ScheduleJob job = new ScheduleJob();
//      job.setObjId("10000" + i);
//      job.setJobName("data_import_" + i);
//      job.setJobGroup("dataWork");
//      job.setJobStatus("1");
//      job.setCronExpression("0/5 * * * * ?");
//      jobMap.put(job.getObjId(),job);
//    }
//  }

    @Before
    public void init() {
        context = new ClassPathXmlApplicationContext("classpath:spring/spring-config.xml");
    }

    @After
    public void destroy() {
        context = null;
    }

    @Test
    public void addJob() {
        ScheduleJobRepository scheduleJobRepository = (ScheduleJobRepository) context.getBean("scheduleJobRepository");
        for (int i = 1; i <= 5; i++) {
            ScheduleJobEntity job = new ScheduleJobEntity();
            job.setObjId("10000" + i);
            job.setJobName("data_import_" + i);
            job.setJobGroup("dataWork");
            job.setEnabled(true);
            job.setCronExpression("0/5 * * * * ?");
            job.setJobClass(SampleJob.class.getName());
//      jobMap.put(job.getObjId(),job);
            scheduleJobRepository.save(job);
        }
    }

    @Test
    public void checkContext() {
//    System.out.println(context.getApplicationName());
//    System.out.println(context.getDisplayName());
        System.out.println(context.getBeanDefinitionCount());
        String[] beanNames = context.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            Object obj = context.getBean(beanName);
            System.out.println(beanName + " ---> " + obj.getClass().getTypeName());
        }
    }
}
