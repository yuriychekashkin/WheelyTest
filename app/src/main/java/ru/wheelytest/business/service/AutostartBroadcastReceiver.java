package ru.wheelytest.business.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ru.wheelytest.business.storage.UserPreferenceStorage;
import ru.wheelytest.business.storage.UserStorage;

/**
 * @author Yuriy Chekashkin
 */
public class AutostartBroadcastReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = "AutostartReceiver";

    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "onReceive autostart " + intent.getAction());
        UserStorage userStorage = new UserPreferenceStorage(context);
        if (userStorage.hasUser()) {
            WebSocketService.start(context, userStorage.getUser());
        }
    }
}
