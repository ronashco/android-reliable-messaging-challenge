package com.example.reliablemessaging;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

import com.example.reliablemessaging.MessageSender;
import com.example.reliablemessaging.service.MessageJobService;

import java.util.Map;

public class ReliableMessaging {

    public static void initialize(Context context) {
        MessageSender.initialize(context);
    }

    public static void sendMessage(Map<String, String> data) {
       MessageSender.getInstance().sendMessage(data);
    }

}
