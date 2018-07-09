package com.uvanix.cloud.service.biz;

import com.uvanix.cloud.service.vo.ScheduleJob;
import org.quartz.SchedulerException;
import org.quartz.SchedulerMetaData;

import java.util.List;

/**
 * @author uvanix
 * @date 2018/7/6
 */
public interface ScheduleJobService {

    List<ScheduleJob> getAllJob() throws SchedulerException;

    List<ScheduleJob> getRunningJob() throws SchedulerException;

    void addJob(ScheduleJob scheduleJob) throws SchedulerException, ClassNotFoundException, InstantiationException, IllegalAccessException;

    void pauseJob(ScheduleJob scheduleJob) throws SchedulerException;

    void resumeJob(ScheduleJob scheduleJob) throws SchedulerException;

    boolean stopJob(ScheduleJob scheduleJob) throws SchedulerException;

    void startJob(ScheduleJob scheduleJob) throws SchedulerException;

    boolean deleteJob(ScheduleJob scheduleJob) throws SchedulerException;

    void runJobOnce(ScheduleJob scheduleJob) throws SchedulerException;

    void updateJobCronExpression(ScheduleJob scheduleJob) throws SchedulerException;

    SchedulerMetaData getSchedulerMetaData() throws SchedulerException;
}
