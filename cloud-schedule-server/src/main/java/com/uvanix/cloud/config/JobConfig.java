package com.uvanix.cloud.config;

import com.uvanix.cloud.service.consts.JobTypeEnum;
import com.uvanix.cloud.service.consts.TriggerTypeEnum;
import com.uvanix.cloud.service.job.LoggerJob;
import com.uvanix.cloud.service.util.JobUtils;
import com.uvanix.cloud.service.util.UuidUtils;
import com.uvanix.cloud.service.vo.ScheduleJob;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

/**
 * @author uvanix
 * @date 2018/7/8
 */
@Configuration
public class JobConfig {

    @Bean
    public MethodInvokingJobDetailFactoryBean testJob() {
        return JobUtils.createJobDetail("testJobBean", "test");
    }

    @Bean
    public SimpleTriggerFactoryBean testTrigger() {
        return JobUtils.createSimpleTrigger(testJob().getObject(), 2000);
    }

    @Bean
    public JobDetailFactoryBean loggerJob() {
        ScheduleJob scheduleJob = new ScheduleJob();
        scheduleJob.setJobId(UuidUtils.generateUuid());
        scheduleJob.setJobType(JobTypeEnum.DEFAULT.name() + "_" + LoggerJob.class.getName());
        scheduleJob.setTriggerType(TriggerTypeEnum.CRON_TRIGGER.name());

        return JobUtils.createJobDetail(LoggerJob.class, scheduleJob);
    }

    @Bean
    public CronTriggerFactoryBean loggerTrigger() {
        return JobUtils.createCronTrigger(loggerJob().getObject(), "0/3 * * * * ?");
    }
}
