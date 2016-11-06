package ru.wheelytest.service;

import android.app.IntentService;
import android.content.Intent;

/**
 * @author Yuriy Chekashkin
 */
public class WebSocketService extends IntentService {

    private static final String WEB_SOCKET_SERVICE = "WEB_SOCKET_SERVICE";

    public WebSocketService() {
        super(WEB_SOCKET_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
