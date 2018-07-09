package com.uvanix.cloud.service.util;

import com.uvanix.cloud.service.vo.ScheduleJob;
import org.quartz.*;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.quartz.*;
import org.springframework.util.ClassUtils;

import java.util.Objects;

/**
 * @author uvanix
 * @date 2018/7/6
 */
public final class JobUtils {

    private JobUtils() {
    }

    /**
     * JobBuilder
     * .newJob(JobUtils.newJobInstance(scheduleJob.getJobName()).getClass())
     * .withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup())
     * .withDescription(scheduleJob.getJobDesc())
     * .build();
     *
     * @param jobClassName
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static Job newJobInstance(String jobClassName) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?> clazz = ClassUtils.forName(jobClassName, ClassUtils.getDefaultClassLoader());
        return (Job) clazz.newInstance();
    }

    /**
     * 通过反射调用 Bean的方法
     *
     * @param targetBeanName
     * @param targetMethod
     * @return
     */
    public static MethodInvokingJobDetailFactoryBean createJobDetail(String targetBeanName, String targetMethod) {
        MethodInvokingJobDetailFactoryBean obj = new MethodInvokingJobDetailFactoryBean();
        obj.setTargetBeanName(targetBeanName);
        obj.setTargetMethod(targetMethod);
        obj.setConcurrent(false);
        return obj;
    }

    /**
     * 依赖QuartzJobBean
     *
     * @param jobClass
     * @param scheduleJob
     * @return
     */
    public static JobDetailFactoryBean createJobDetail(Class<? extends QuartzJobBean> jobClass, ScheduleJob scheduleJob) {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(jobClass);
        factoryBean.setDurability(true);
        if (Objects.nonNull(scheduleJob)) {
            factoryBean.setName(scheduleJob.getJobName());
            factoryBean.setGroup(scheduleJob.getJobGroup());
            factoryBean.setDescription(scheduleJob.getJobDesc());
            factoryBean.getJobDataMap().put("scheduleJob", scheduleJob);
        }

        return factoryBean;
    }

    public static SimpleTriggerFactoryBean createSimpleTrigger(JobDetail jobDetail, long pollFrequencyMs) {
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(jobDetail);
        factoryBean.setStartDelay(0L);
        factoryBean.setRepeatInterval(pollFrequencyMs);
        factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        // in case of misfire, ignore all missed triggers and continue :
        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
        return factoryBean;
    }

    public static CronTriggerFactoryBean createCronTrigger(JobDetail jobDetail, String cronExpression) {
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setJobDetail(jobDetail);
        factoryBean.setStartDelay(3);
        factoryBean.setCronExpression(cronExpression);
        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        return factoryBean;
    }

    public static ScheduleJob wrapScheduleJob(ScheduleJob scheduleJob, Scheduler scheduler, JobDetail jobDetail, Trigger trigger) throws SchedulerException {
        ScheduleJob job = (ScheduleJob) jobDetail.getJobDataMap().get("scheduleJob");
        if (Objects.nonNull(job)) {
            BeanUtils.copyProperties(job, scheduleJob);
        }

        scheduleJob.setJobName(jobDetail.getKey().getName());
        scheduleJob.setJobGroup(jobDetail.getKey().getGroup());
        scheduleJob.setTriggerName(trigger.getKey().getName());
        scheduleJob.setTriggerGroup(trigger.getKey().getGroup());

        Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
        scheduleJob.setJobStatus(triggerState.name());
        if (Objects.nonNull(trigger.getNextFireTime())) {
            scheduleJob.setNextFireTime(DateTimeUtils.toString(trigger.getNextFireTime()));
        }
        if (trigger instanceof CronTrigger) {
            CronTrigger cronTrigger = (CronTrigger) trigger;
            scheduleJob.setCronExpression(cronTrigger.getCronExpression());
            scheduleJob.setTimeZoneId(cronTrigger.getTimeZone().getID());
        }

        jobDetail.getJobDataMap().put("scheduleJob", scheduleJob);

        return scheduleJob;
    }
}
