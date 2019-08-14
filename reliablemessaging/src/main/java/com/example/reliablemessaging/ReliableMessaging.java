package com.example.reliablemessaging;

import android.content.Context;
import android.os.Build;

import java.util.Map;

public class ReliableMessaging {

    public static void initialize(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            MessageSenderForLollipop.initialize(context);
        else
            MessageSender.initialize(context);
    }

    public static void sendMessage(Map<String, String> data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            MessageSenderForLollipop.getInstance().sendMessage(data);
        else
            MessageSender.getInstance().sendMessage(data);
    }

}
