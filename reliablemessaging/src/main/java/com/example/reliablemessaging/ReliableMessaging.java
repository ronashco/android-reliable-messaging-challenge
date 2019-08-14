package com.example.reliablemessaging;

import android.content.Context;

import java.util.Map;

public class ReliableMessaging {

    public static void initialize(Context context) {
//        MessageSenderForLollipop.initialize(context);
        MessageSender.initialize(context);
    }

    public static void sendMessage(Map<String, String> data) {
//       MessageSenderForLollipop.getInstance().sendMessage(data);
        MessageSender.getInstance().sendMessage(data);
    }

}
