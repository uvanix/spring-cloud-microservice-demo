package com.uvanix.cloud.service.biz.impl;

import com.uvanix.cloud.service.biz.ScheduleJobService;
import com.uvanix.cloud.service.consts.JobTypeEnum;
import com.uvanix.cloud.service.consts.TriggerTypeEnum;
import com.uvanix.cloud.service.job.HttpRequestJob;
import com.uvanix.cloud.service.util.DateTimeUtils;
import com.uvanix.cloud.service.util.JobUtils;
import com.uvanix.cloud.service.util.UuidUtils;
import com.uvanix.cloud.service.vo.ScheduleJob;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author uvanix
 * @date 2018/7/6
 */
@Service
public class ScheduleJobServiceImpl implements ScheduleJobService {

    private final Logger logger = LoggerFactory.getLogger(ScheduleJobServiceImpl.class);
    private final Scheduler scheduler;
    private final int port;
    private final String mgmtContextPath;
    private final String infoPath;

    public ScheduleJobServiceImpl(Scheduler scheduler,
                                  @Value("${server.port}") int port,
                                  @Value("${management.context-path}") String mgmtContextPath,
                                  @Value("${endpoints.info.path:/info}") String infoPath) {
        this.scheduler = scheduler;
        this.port = port;
        this.mgmtContextPath = mgmtContextPath;
        this.infoPath = infoPath;
    }

    @Override
    public List<ScheduleJob> getAllJob() throws SchedulerException {
        List<ScheduleJob> jobList = new ArrayList<>();

        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        Set<JobKey> jobKeySet = scheduler.getJobKeys(matcher);
        if (CollectionUtils.isEmpty(jobKeySet)) {
            return jobList;
        }

        for (JobKey jobKey : jobKeySet) {
            ScheduleJob scheduleJob = new ScheduleJob();

            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            ScheduleJob job = (ScheduleJob) jobDetail.getJobDataMap().get("scheduleJob");
            if (Objects.nonNull(job)) {
                BeanUtils.copyProperties(job, scheduleJob);
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobDetail.getKey());
                for (Trigger trigger : triggers) {
                    scheduleJob.setJobStatus(scheduler.getTriggerState(trigger.getKey()).name());
                }
            }

            jobList.add(scheduleJob);
        }

        return jobList;
    }

    @Override
    public List<ScheduleJob> getRunningJob() throws SchedulerException {
        List<ScheduleJob> jobList = new ArrayList<>();

        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        Set<JobKey> jobKeySet = scheduler.getJobKeys(matcher);
        if (CollectionUtils.isEmpty(jobKeySet)) {
            return jobList;
        }

        for (JobKey jobKey : jobKeySet) {
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            for (Trigger trigger : scheduler.getTriggersOfJob(jobDetail.getKey())) {
                if (Trigger.TriggerState.NORMAL.equals(scheduler.getTriggerState(trigger.getKey()))) {
                    ScheduleJob scheduleJob = new ScheduleJob();
                    JobUtils.wrapScheduleJob(scheduleJob, scheduler, jobDetail, trigger);

                    jobList.add(scheduleJob);
                }
            }
        }

        return jobList;
    }

    @Override
    public void addJob(ScheduleJob scheduleJob) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (Objects.nonNull(jobDetail)) {
            throw new ObjectAlreadyExistsException(jobDetail);
        }

        scheduleJob.setJobId(UuidUtils.generateUuid());
        if (StringUtils.isEmpty(scheduleJob.getJobType())) {
            scheduleJob.setJobType(JobTypeEnum.HTTP_REQUEST.name());
        }
        if (StringUtils.isEmpty(scheduleJob.getTriggerType())) {
            scheduleJob.setTriggerType(TriggerTypeEnum.CRON_TRIGGER.name());
        }
        if (StringUtils.isEmpty(scheduleJob.getTriggerName())) {
            scheduleJob.setTriggerName(scheduleJob.getJobName());
        }
        if (StringUtils.isEmpty(scheduleJob.getUrl())) {
            scheduleJob.setUrl("http://localhost:" + port + mgmtContextPath + infoPath);
        }
        // for test
        if (StringUtils.isEmpty(scheduleJob.getCronExpression())) {
            scheduleJob.setCronExpression("0/3 * * * * ?");
        }

        // 这里使用JobBuilder创建的job调用scheduler.unscheduleJob后将会删掉jobDetail，
        // 而使用JobDetailFactoryBean不会从而达到停止job效果，不会出现start后将错过时间的一次性全部执行(resume效果)
//        jobDetail = JobBuilder.newJob(HttpRequestJob.class)
//                .withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup())
//                .withDescription(scheduleJob.getJobDesc())
//                .build();
        JobDetailFactoryBean jobDetailFactoryBean = JobUtils.createJobDetail(HttpRequestJob.class, scheduleJob);
        jobDetailFactoryBean.afterPropertiesSet();
        jobDetail = jobDetailFactoryBean.getObject();

        Date date = scheduler.scheduleJob(jobDetail, this.createCronTrigger(scheduleJob, jobDetail));
        logger.info("add job success {} -> {}", date, scheduleJob);
    }

    @Override
    public void pauseJob(ScheduleJob scheduleJob) throws SchedulerException {
        scheduler.pauseJob(JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup()));
        logger.info("pause job success -> {}", scheduleJob);
    }

    @Override
    public void resumeJob(ScheduleJob scheduleJob) throws SchedulerException {
        // 恢复(resumeJob)会将错过时间的一次性全部执行
        scheduler.resumeJob(JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup()));
        logger.info("resume job success -> {}", scheduleJob);
    }

    @Override
    public boolean stopJob(ScheduleJob scheduleJob) throws SchedulerException {
        if (Objects.isNull(scheduleJob.getTriggerName())) {
            scheduleJob.setTriggerName(scheduleJob.getJobName());
        }

        boolean result = scheduler.unscheduleJob(TriggerKey.triggerKey(scheduleJob.getTriggerName(), scheduleJob.getTriggerGroup()));
        logger.info("stop job success {} -> {}", result, scheduleJob);
        return result;
    }

    @Override
    public void startJob(ScheduleJob scheduleJob) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (Objects.isNull(jobDetail)) {
            throw new SchedulerException("job not exist");
        }
        if (Objects.isNull(scheduleJob.getTriggerName())) {
            scheduleJob.setTriggerName(scheduleJob.getJobName());
        }
        if (Objects.isNull(scheduleJob.getCronExpression())) {
            ScheduleJob job = (ScheduleJob) jobDetail.getJobDataMap().get("scheduleJob");
            scheduleJob.setCronExpression(job.getCronExpression());
        }

        Date date = scheduler.scheduleJob(createCronTrigger(scheduleJob, jobDetail));
        logger.info("start job success {} -> {}", date, scheduleJob);
    }

    @Override
    public boolean deleteJob(ScheduleJob scheduleJob) throws SchedulerException {
        boolean result = scheduler.deleteJob(JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup()));
        logger.info("delete job success {} -> {}", result, scheduleJob);
        return result;
    }

    @Override
    public void runJobOnce(ScheduleJob scheduleJob) throws SchedulerException {
        scheduler.triggerJob(JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup()));
        logger.info("run job once success -> {}", scheduleJob);
    }

    @Override
    public void updateJobCronExpression(ScheduleJob scheduleJob) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        if (Objects.isNull(cronTrigger)) {
            throw new SchedulerException("job not running");
        }

        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());
        cronTrigger = cronTrigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();
        Date date = scheduler.rescheduleJob(triggerKey, cronTrigger);
        logger.info("update job cron expression success {} -> {}", date, scheduleJob);
    }

    @Override
    public SchedulerMetaData getSchedulerMetaData() throws SchedulerException {
        return scheduler.getMetaData();
    }

    private CronTrigger createCronTrigger(ScheduleJob scheduleJob, JobDetail jobDetail) throws SchedulerException {
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());
        TriggerBuilder<CronTrigger> triggerBuilder = TriggerBuilder.<CronTrigger>newTrigger()
                .withIdentity(scheduleJob.getTriggerName(), scheduleJob.getTriggerGroup())
                .withSchedule(cronScheduleBuilder)
                .forJob(jobDetail);
        if (Objects.nonNull(scheduleJob.getStartTime())) {
            triggerBuilder.startAt(DateTimeUtils.parseStringToDate(scheduleJob.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
        }
        if (Objects.nonNull(scheduleJob.getEndTime())) {
            triggerBuilder.startAt(DateTimeUtils.parseStringToDate(scheduleJob.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
        }

        CronTrigger cronTrigger = triggerBuilder.build();
        JobUtils.wrapScheduleJob(scheduleJob, scheduler, jobDetail, cronTrigger);

        return cronTrigger;
    }
}
