package com.uvanix.cloud.config;

import org.quartz.*;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.*;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

/**
 * @author uvanix
 * @date 2018/7/6
 */
@Configuration
public class QuartzConfig {

    @Bean
    public Scheduler schedulerFactoryBean(JobFactory jobFactory,
                                          MethodInvokingJobDetailFactoryBean testJob,
                                          SimpleTriggerFactoryBean testTrigger,
                                          JobDetailFactoryBean loggerJob,
                                          CronTriggerFactoryBean loggerTrigger) throws Exception {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setOverwriteExistingJobs(true);
        // 添加自定义的jobFactory支持继承QuartzJobBean和实现quartz的Job接口来定义job
        factory.setJobFactory(jobFactory);

        // 自定义配置
        factory.setQuartzProperties(quartzProperties());
        factory.afterPropertiesSet();

        Scheduler scheduler = factory.getScheduler();
        scheduler.setJobFactory(jobFactory);

        // 添加job
        // 使用 MethodInvokingJobDetailFactoryBean 在使用数据库存储job时会出现如下bug 需改spring源码待处理
        // Caused by: java.io.NotSerializableException: Unable to serialize JobDataMap for insertion into database because the value of property 'methodInvoker' is not serializable:
//        scheduler.scheduleJob(testJob.getObject(), testTrigger.getObject());
        CronTrigger loggerTriggerObject = loggerTrigger.getObject();
        TriggerKey loggerTriggerObjectKey = loggerTriggerObject.getKey();
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(loggerTriggerObjectKey);
        if (Objects.isNull(trigger)) {
            JobDetail loggerJobObject = loggerJob.getObject();
            JobDetail loggerJobDetail = scheduler.getJobDetail(loggerJobObject.getKey());
            if (Objects.isNull(loggerJobDetail)) {
                scheduler.scheduleJob(loggerJobObject, loggerTriggerObject);
            }
        } else {
            scheduler.rescheduleJob(loggerTriggerObjectKey, loggerTriggerObject);
        }

        scheduler.start();
        return scheduler;
    }

    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext) {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    public final class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory implements ApplicationContextAware {

        private transient AutowireCapableBeanFactory beanFactory;

        @Override
        public void setApplicationContext(final ApplicationContext context) {
            beanFactory = context.getAutowireCapableBeanFactory();
        }

        @Override
        protected Object createJobInstance(final TriggerFiredBundle bundle) throws Exception {
            final Object job = super.createJobInstance(bundle);
            beanFactory.autowireBean(job);
            return job;
        }
    }
}
