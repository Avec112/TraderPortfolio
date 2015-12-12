package no.avec;

import no.avec.domain.Aksje;
import no.avec.service.Traderportfolio;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.ArrayList;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by ronny.ness on 11/12/15.
 */
public class Application {
    public static void main(String[] args) throws SchedulerException {
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();

        JobDetail job = newJob(Traderportfolio.class)
                .withIdentity("job1", "group1")
                .build();

        job.getJobDataMap().put(Traderportfolio.PORTFOLIO, new ArrayList<Aksje>());


        CronTrigger trigger = newTrigger()
                .withIdentity("trigger1", "group1")
//                .withSchedule(cronSchedule("0 0 * * * ?"))
                .withSchedule(cronSchedule("0/30 * * * * ?"))
                .withSchedule(cronSchedule("0 0/30 7-17 ? * MON-FRI"))
                .withSchedule(cronSchedule("0 0 17 ? * SUN"))
                .build();

        // Tell quartz to schedule the job using our trigger
        sched.scheduleJob(job, trigger);

        sched.start();


    }
}
