package com.bonc.crawler.quartz.config;

import com.bonc.crawler.quartz.job.CnBlogCrawler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * Quartz定时任务调度配置类
 *
 * @author 兰杰
 * @create 2019-07-31 14:57
 */
@Configuration
public class QuartzConfig {

    /**
     * 创建博客园爬虫任务工厂
     *
     * @return
     */
    @Bean(name = "cnBlogCrawlerJob")
    public JobDetailFactoryBean cnBlogCrawlerJob() {

        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
        //添加任务
        jobDetailFactoryBean.setJobClass(CnBlogCrawler.class);

        return jobDetailFactoryBean;
    }

    /**
     * 创建博客园爬虫任务触发器
     *
     * @param jobDetailFactoryBean 任务工厂
     * @return
     */
    @Bean(name = "cnBlogCrawlerCronTrigger")
    public CronTriggerFactoryBean cnBlogCrawlerCronTrigger(@Qualifier("cnBlogCrawlerJob") JobDetailFactoryBean jobDetailFactoryBean) {

        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();

        cronTriggerFactoryBean.setJobDetail(jobDetailFactoryBean.getObject());
        //设置执行时间，每5分钟执行一次
        cronTriggerFactoryBean.setCronExpression("0 0/5 * * * ?");

        return cronTriggerFactoryBean;
    }

    /**
     * 创建调度器
     *
     * @param cnBlogCrawlerCronTrigger  博客园爬虫任务触发器
     * @param springAdaptableJobFactory 任务工厂适配器
     * @return
     */
    @Bean(name = "schedulerFactoryBean")
    public SchedulerFactoryBean schedulerFactoryBean(@Qualifier("cnBlogCrawlerCronTrigger") CronTriggerFactoryBean cnBlogCrawlerCronTrigger,
                                                     @Qualifier("springAdaptableJobFactory") SpringAdaptableJobFactory springAdaptableJobFactory) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        //设置触发器
        schedulerFactoryBean.setTriggers(cnBlogCrawlerCronTrigger.getObject());

        //设置任务工厂适配器
        schedulerFactoryBean.setJobFactory(springAdaptableJobFactory);

        return schedulerFactoryBean;
    }

}
