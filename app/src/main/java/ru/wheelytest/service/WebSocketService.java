package ru.wheelytest.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import ru.wheelytest.R;
import ru.wheelytest.business.BroadcastSender;
import ru.wheelytest.business.storage.UserStorage;
import ru.wheelytest.domain.entity.GpsPoint;
import ru.wheelytest.domain.entity.User;
import ru.wheelytest.service.configuration.WebSocketServiceConfigurationFactory;
import ru.wheelytest.service.configuration.WebSocketServiceConfigurationFactoryImpl;

/**
 * @author Yuriy Chekashkin
 */
public class WebSocketService extends IntentService implements WebSocketManager.WebSocketMessageListener {

    public static final String EXTRA_USER = "EXTRA_USER";
    public static final String EXTRA_BROADCAST_GPS_POINTS = "EXTRA_BROADCAST_GPS_POINTS";
    public static final String EXTRA_BROADCAST_AUTH_SUCCESS = "EXTRA_BROADCAST_AUTH_SUCCESS";
    public static final String EXTRA_SERVICE_ACTION = "EXTRA_SERVICE_ACTION";

    public static final String SERVICE_ACTION_START = "SERVICE_ACTION_START";
    public static final String SERVICE_ACTION_STOP = "SERVICE_ACTION_STOP";


    private static final String WEB_SOCKET_SERVICE = "WEB_SOCKET_SERVICE";


    private WebSocketManager webSocketManager;
    private UserStorage userStorage;
    private BroadcastSender broadcastSender;
    private User user;

    public WebSocketService() {
        super(WEB_SOCKET_SERVICE);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        WebSocketServiceConfigurationFactory factory = new WebSocketServiceConfigurationFactoryImpl(this);

        webSocketManager = factory.createWebSocketManager(this);
        userStorage = factory.createUserStorage();
        broadcastSender = factory.createBroadcastSender();
    }

    @Override
    public void onSuccessConnection() {
        userStorage.saveUser(user);
        broadcastSender.sendAuthSuccessBroadcast(true);
    }

    @Override
    public void onFailedConnection() {
        user = null;
        broadcastSender.sendAuthSuccessBroadcast(false);
    }

    @Override
    public void onPointsReceived(List<GpsPoint> gpsPoints) {
        broadcastSender.sendGpsPointsBroadcast((ArrayList<GpsPoint>) gpsPoints);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getStringExtra(EXTRA_SERVICE_ACTION);
        switch (action) {
            case SERVICE_ACTION_START:
                if (!userStorage.hasUser()) {
                    user = (User) intent.getSerializableExtra(EXTRA_USER);
                    String url = getString(R.string.websocket_url, user.getLogin(), user.getPassword());
                    webSocketManager.tryConnect(url);
                }

            case SERVICE_ACTION_STOP:
                webSocketManager.disconnect();

            default:
                throw new IllegalArgumentException("Unknown type of service action. Use WebSocketService.ServiceActions");
        }
    }


    @Retention(RetentionPolicy.SOURCE)
    @StringDef({SERVICE_ACTION_START, SERVICE_ACTION_STOP})
    public @interface ServiceActions {}
}
