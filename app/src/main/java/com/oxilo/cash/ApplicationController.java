package com.oxilo.cash;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.oxilo.cash.volley.MyVolley;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import com.path.android.jobqueue.log.CustomLogger;

/**
 * Created by ericbasendra on 01/12/15.
 */
public class ApplicationController extends Application {
    public static final int LONG_DELAY = 3500; // 3.5 seconds
    public static final int SHORT_DELAY = 2000; // 2 seconds

    /**
     * A singleton instance of the application class for easy access in other places
     */
    private static ApplicationController sInstance;

    /** A Job Manager Instance
     *
     */
    private JobManager jobManager;

    @Override
    public void onCreate() {
        super.onCreate();

        // initialize the singleton
        sInstance = this;
        MyVolley.init(sInstance);

        configureJobManager();
    }

    /**
     * @return ApplicationController singleton instance
     */
    public static synchronized ApplicationController getInstance() {
        return sInstance;
    }

    public JobManager getJobManager() {
        return jobManager;
    }

    private void configureJobManager() {
        Configuration configuration = new Configuration.Builder(this)
                .customLogger(new CustomLogger() {
                    private static final String TAG = "JOBS";
                    @Override
                    public boolean isDebugEnabled() {
                        return true;
                    }


                    @Override
                    public void d(String text, Object... args) {
                        Log.d(TAG, String.format(text, args));
                    }


                    @Override
                    public void e(Throwable t, String text, Object... args) {
                        Log.e(TAG, String.format(text, args), t);
                    }


                    @Override
                    public void e(String text, Object... args) {
                        Log.e(TAG, String.format(text, args));
                    }
                })
                .minConsumerCount(1)//always keep at least one consumer alive
                .maxConsumerCount(3)//up to 3 consumers at a time
                .loadFactor(3)//3 jobs per consumer
                .consumerKeepAlive(120)//wait 2 minute
                .build();
        jobManager = new JobManager(this, configuration);
    }

}
