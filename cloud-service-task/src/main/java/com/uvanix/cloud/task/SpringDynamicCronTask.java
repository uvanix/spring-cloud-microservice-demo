package com.uvanix.cloud.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

/**
 * Spring动态周期定时任务
 * 在不停应用的情况下更改任务执行周期
 *
 * @author Yvan
 * @date 2018/7/5
 */
@Component
@EnableScheduling
public class SpringDynamicCronTask implements SchedulingConfigurer {

    private final Logger logger = LoggerFactory.getLogger(SpringDynamicCronTask.class);
    public static String cron;
    public static String cron2;

    public SpringDynamicCronTask(@Value("${task.cron.test}") String cron,
                                 @Value("${task.cron.test2}") String cron2) {
        SpringDynamicCronTask.cron = cron;
        SpringDynamicCronTask.cron2 = cron2;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(
                () -> logger.debug("dynamicCronTask is running... cron: {}", cron),
                triggerContext -> new CronTrigger(cron).nextExecutionTime(triggerContext)
        );

        taskRegistrar.addTriggerTask(
                () -> logger.debug("dynamicCronTask is running... cron2: {}", cron2),
                triggerContext -> new CronTrigger(cron2).nextExecutionTime(triggerContext)
        );
    }
}
