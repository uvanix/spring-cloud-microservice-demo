package com.uvanix.cloud.service.job;

import com.uvanix.cloud.service.biz.LoggerService;
import com.uvanix.cloud.service.util.JobUtils;
import com.uvanix.cloud.service.vo.ScheduleJob;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * @author uvanix
 * @date 2018/7/6
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Component
public class LoggerJob extends QuartzJobBean {

    private final Logger logger = LoggerFactory.getLogger(LoggerJob.class);
    private LoggerService loggerService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");
        try {
            JobUtils.wrapScheduleJob(scheduleJob, context.getScheduler(), context.getJobDetail(), context.getTrigger());
            loggerService.logger(scheduleJob.getJobName());
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Autowired
    public void setLoggerService(LoggerService loggerService) {
        this.loggerService = loggerService;
    }
}
