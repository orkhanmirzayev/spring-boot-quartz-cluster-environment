package com.github.heidiks.controller;

import com.github.heidiks.job.SampleJob;
import com.github.heidiks.model.QuartzDateRequest;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.quartz.*;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static org.quartz.JobBuilder.newJob;

@Component
public class QuartzVerticle extends AbstractVerticle {

    private final SchedulerFactoryBean schedulerFactoryBean;

    public QuartzVerticle(SchedulerFactoryBean schedulerFactoryBean) {
        this.schedulerFactoryBean = schedulerFactoryBean;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.post("/quartz").handler(rc -> {
            final QuartzDateRequest quartzDateRequest = rc.getBodyAsJson().mapTo(QuartzDateRequest.class);
            final JobDetail sampleJobDetail = newJob(SampleJob.class)
                    .withIdentity(UUID.randomUUID().toString(),"mygroup")
                    .requestRecovery()
                    .build();
            JobDataMap jobDataMap = sampleJobDetail.getJobDataMap();
            jobDataMap.put("name", "Orkhan");
            jobDataMap.put("surname", "Orkhan");
            jobDataMap.put("age", "32");

            final Trigger simpleTrigger;
            try {
                simpleTrigger = createSimpleTrigger(sampleJobDetail, quartzDateRequest.getStartDate());
                final Scheduler scheduler = schedulerFactoryBean.getScheduler();
                scheduler.scheduleJob(sampleJobDetail, simpleTrigger);
                scheduler.start();
                rc.response().end("ok");
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (SchedulerException e) {
                e.printStackTrace();
            }

        });

        vertx.createHttpServer().requestHandler(router).listen(8092);

    }

    private static Trigger createSimpleTrigger(JobDetail jobDetail, String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        final Date startDate = sdf.parse(date);
        return  TriggerBuilder.newTrigger().forJob(jobDetail).usingJobData(jobDetail.getJobDataMap())
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .startAt(startDate).build();
    }
}
