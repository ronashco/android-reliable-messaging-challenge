package com.example.reliablemessaging;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.PersistableBundle;

import com.example.reliablemessaging.service.MessageJobService;

import java.util.Map;

class MessageSender {
    private static MessageSender instance = null;

    private JobInfo.Builder builder;
    private JobScheduler jobScheduler;

    private MessageSender(Context context) {
        ComponentName serviceComponent = new ComponentName(context, MessageJobService.class);
        builder = new JobInfo.Builder(0, serviceComponent);
        builder.setOverrideDeadline(3 * 1000);
        jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
    }

    public static void initialize(Context context) {
        if (instance == null) {
            synchronized (MessageSender.class) {
                if (instance == null) {
                    instance = new MessageSender(context);
                }
            }
        }
    }

    public static MessageSender getInstance() {
        if (instance == null) new Exception("fist call initialize");
        return instance;
    }

    public void sendMessage(Map<String, String> data) {
        for (Map.Entry<String, String> entry : data.entrySet()) {
            PersistableBundle bundle = new PersistableBundle();
            bundle.putString(BundleKey.URL, entry.getKey());
            bundle.putString(BundleKey.DATA, entry.getValue());
            builder.setExtras(bundle);
            jobScheduler.schedule(builder.build());
        }

    }
}