package ru.wheelytest.business.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import ru.wheelytest.R;
import ru.wheelytest.business.location.LocationMonitor;
import ru.wheelytest.business.network.RPCManager;
import ru.wheelytest.business.service.configuration.WebSocketServiceConfigurationFactory;
import ru.wheelytest.business.service.configuration.WebSocketServiceConfigurationFactoryImpl;
import ru.wheelytest.business.storage.UserStorage;
import ru.wheelytest.model.entity.GpsPoint;
import ru.wheelytest.model.entity.User;

/**
 * @author Yuriy Chekashkin
 */
public class WebSocketService extends Service implements RPCManager.WebSocketMessageListener, LocationMonitor.LocationListener {

    public static final String EXTRA_USER = "EXTRA_USER";

    public static final String EXTRA_BROADCAST_GPS_POINTS = "EXTRA_BROADCAST_GPS_POINTS";
    public static final String EXTRA_BROADCAST_AUTH_SUCCESS = "EXTRA_BROADCAST_AUTH_SUCCESS";
    public static final String EXTRA_SERVICE_ACTION = "EXTRA_SERVICE_ACTION";

    public static final int SERVICE_ACTION_START = 101;
    public static final int SERVICE_ACTION_STOP = 102;

    public static final String TAG = "WebSocketService";

    private RPCManager webSocketManager;
    private UserStorage userStorage;
    private EventSender broadcastSender;
    private LocationMonitor locationMonitor;
    private User user;

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

    @Override
    public void onCreate() {
        startForeground(101, new Notification());
        super.onCreate();
        WebSocketServiceConfigurationFactory factory = new WebSocketServiceConfigurationFactoryImpl(this);

        webSocketManager = factory.createWebSocketManager(this);
        userStorage = factory.createUserStorage();
        broadcastSender = factory.createBroadcastSender();
        locationMonitor = factory.createLocationMonitor();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int action = intent.getIntExtra(EXTRA_SERVICE_ACTION, -1);
        switch (action) {
            case SERVICE_ACTION_START:
                user = (User) intent.getSerializableExtra(EXTRA_USER);
                startMonitoring();
                break;

            case SERVICE_ACTION_STOP:
                stopMonitoring();
                stopSelf();
                break;

        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationUpdate(GpsPoint point) {
        webSocketManager.send(point);
        Log.d(TAG, "Location updated");
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

    private void startMonitoring() {
        String url = getString(R.string.websocket_url, user.getLogin(), user.getPassword());
        webSocketManager.tryConnect(url);
    }

    private void stopMonitoring() {
        locationMonitor.stop();
        webSocketManager.disconnect();
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SERVICE_ACTION_START, SERVICE_ACTION_STOP})
    public @interface ServiceActions {
    }
}
