package com.uvanix.cloud.service.job;

import com.uvanix.cloud.service.util.JobUtils;
import com.uvanix.cloud.service.vo.ScheduleJob;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

/**
 * @author uvanix
 * @date 2018/7/8
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Component
public class HttpRequestJob extends QuartzJobBean {

    private final Logger logger = LoggerFactory.getLogger(HttpRequestJob.class);
    private RestTemplate restTemplate;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");
        try {
            JobUtils.wrapScheduleJob(scheduleJob, context.getScheduler(), context.getJobDetail(), context.getTrigger());

            String result = restTemplate.getForObject(scheduleJob.getUrl(), String.class);
            logger.info("http request job {} {} result -> {}", scheduleJob.getJobName(), LocalDateTime.now(), result);
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
