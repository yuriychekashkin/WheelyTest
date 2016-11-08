package ru.wheelytest.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import ru.wheelytest.R;
import ru.wheelytest.business.LocationMonitor;
import ru.wheelytest.business.storage.UserStorage;
import ru.wheelytest.domain.entity.GpsPoint;
import ru.wheelytest.domain.entity.User;
import ru.wheelytest.service.configuration.WebSocketServiceConfigurationFactory;
import ru.wheelytest.service.configuration.WebSocketServiceConfigurationFactoryImpl;

/**
 * @author Yuriy Chekashkin
 */
public class WebSocketService extends IntentService implements WebSocketManager.WebSocketMessageListener, LocationMonitor.LocationListener {

    public static final String EXTRA_USER = "EXTRA_USER";

    public static final String EXTRA_BROADCAST_GPS_POINTS = "EXTRA_BROADCAST_GPS_POINTS";
    public static final String EXTRA_BROADCAST_AUTH_SUCCESS = "EXTRA_BROADCAST_AUTH_SUCCESS";
    public static final String EXTRA_SERVICE_ACTION = "EXTRA_SERVICE_ACTION";

    public static final int SERVICE_ACTION_START = 101;
    public static final int SERVICE_ACTION_STOP = 102;

    private static final String WEB_SOCKET_SERVICE = "WEB_SOCKET_SERVICE";
    private static final String TAG = "WebSocketService";

    private WebSocketManager webSocketManager;
    private UserStorage userStorage;
    private BroadcastSender broadcastSender;
    private User user;
    private LocationMonitor locationMonitor;

    public static void start(@NonNull Context context, User user) {
        Intent intent = createIntent(context, SERVICE_ACTION_START);
        intent.putExtra(EXTRA_USER, user);
        context.startService(intent);
    }

    public static void stop(@NonNull Context context) {
        Intent intent = createIntent(context, SERVICE_ACTION_STOP);
        context.startService(intent);
    }

    @NonNull
    private static Intent createIntent(@NonNull Context context, @ServiceActions int serviceAction) {
        Intent serviceIntent = new Intent(context, WebSocketService.class);
        serviceIntent.putExtra(EXTRA_SERVICE_ACTION, serviceAction);
        return serviceIntent;
    }

    public WebSocketService() {
        super(WEB_SOCKET_SERVICE);
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();
        WebSocketServiceConfigurationFactory factory = new WebSocketServiceConfigurationFactoryImpl(this);

        webSocketManager = factory.createWebSocketManager(this);
        userStorage = factory.createUserStorage();
        broadcastSender = factory.createBroadcastSender();
        locationMonitor = factory.createLocationMonitor();
    }

    @Override
    public void onSuccessConnection() {
        userStorage.saveUser(user);
        broadcastSender.sendAuthSuccessBroadcast(true);
        locationMonitor.start(this);
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
    public void onLocationUpdate(GpsPoint point) {
        webSocketManager.send(point);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int action = intent.getIntExtra(EXTRA_SERVICE_ACTION, -1);
        switch (action) {
            case SERVICE_ACTION_START:
                if (!webSocketManager.isConnected()) {
                    user = (User) intent.getSerializableExtra(EXTRA_USER);
                    String url = getString(R.string.websocket_url, user.getLogin(), user.getPassword());
                    webSocketManager.tryConnect(url);
                }
                break;

            case SERVICE_ACTION_STOP:
                locationMonitor.stop();
                webSocketManager.disconnect();
                stopSelf();
                break;

        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SERVICE_ACTION_START, SERVICE_ACTION_STOP})
    public @interface ServiceActions {
    }
}
