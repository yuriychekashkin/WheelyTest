package ru.wheelytest.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author Yuriy Chekashkin
 */
public class AutostartBroadcastReceiver extends BroadcastReceiver {

    final String LOG_TAG = "AutostartReceiver";

    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "onReceive autostart " + intent.getAction());
        WebSocketService.start(context);
    }
}
