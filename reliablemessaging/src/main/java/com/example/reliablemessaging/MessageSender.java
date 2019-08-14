package com.example.reliablemessaging;

import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;

import com.example.reliablemessaging.service.MessageService;

import java.util.Map;

public class MessageSender {
    private static MessageSender instance = null;
    private Context context;

    private MessageSender(Context context) {
        this.context = context;

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
        if (instance == null) new Exception("first call initialize");
        return instance;
    }

    public void sendMessage(Map<String, String> data) {
        Intent intent = new Intent(context, MessageService.class);
        for (Map.Entry<String, String> entry : data.entrySet()) {
            intent.putExtra(BundleKey.URL, entry.getKey());
            intent.putExtra(BundleKey.DATA, entry.getValue());
            context.startService(intent);
        }
    }
}
