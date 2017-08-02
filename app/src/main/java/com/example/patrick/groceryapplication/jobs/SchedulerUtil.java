package com.example.patrick.groceryapplication.jobs;

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

/**
 * Created by Patrick on 8/1/2017.
 */

public class SchedulerUtil {
    private static final int SCHEDULE_INTERVAL_MINUTES=1440;
    private static final int SYNC_FLEXTIME_SECONDS=60;
    private static final String JOB_TAG="news_job_tag";

    private static boolean sInitialized;

    //Schedules when the database will be refreshed
    synchronized public static void scheduleRefresh(@NonNull final Context context){
        //Prevents scheduling if that has occurred already
        if(sInitialized) return;

        //Uses GooglePlay and Firebase dispatch to help schedule everything
        //The documentation for Firebase dispatcher is on github
        Driver driver=new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher=new FirebaseJobDispatcher(driver);

        Job contraintRefresh=dispatcher.newJobBuilder()
                .setService(FirebaseJob.class)
                .setTag(JOB_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(SCHEDULE_INTERVAL_MINUTES,SCHEDULE_INTERVAL_MINUTES))
                .setReplaceCurrent(true)
                .build();

        dispatcher.schedule(contraintRefresh);
        sInitialized=true;
    }
}
