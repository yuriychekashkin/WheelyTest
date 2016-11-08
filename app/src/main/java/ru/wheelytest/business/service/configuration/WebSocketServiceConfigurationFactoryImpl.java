package ru.wheelytest.business.service.configuration;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import ru.wheelytest.business.location.LocationMonitor;
import ru.wheelytest.business.network.RPCManager;
import ru.wheelytest.business.network.WebSocketManager;
import ru.wheelytest.business.serialization.GsonConverter;
import ru.wheelytest.business.storage.UserPreferenceStorage;
import ru.wheelytest.business.service.BroadcastSender;
import ru.wheelytest.business.service.EventSender;

/**
 * @author Yuriy Chekashkin
 */
public class WebSocketServiceConfigurationFactoryImpl implements WebSocketServiceConfigurationFactory {

    private final Context context;

    public WebSocketServiceConfigurationFactoryImpl(@NonNull Context context){
        this.context = context;
    }

    @NonNull
    public RPCManager createWebSocketManager(@NonNull RPCManager.WebSocketMessageListener listener) {
        return new WebSocketManager(createHttpClient(), createMessagesConverter(), listener);
    }

    @NonNull
    public GsonConverter createMessagesConverter() {
        return new GsonConverter();
    }

    @NonNull
    public OkHttpClient createHttpClient() {
        return new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.SECONDS)
                .build();
    }

    @NonNull
    public EventSender createBroadcastSender() {
        return new BroadcastSender(context);
    }

    @NonNull
    public UserPreferenceStorage createUserStorage() {
        return new UserPreferenceStorage(context);
    }

    @NonNull
    @Override
    public LocationMonitor createLocationMonitor() {
        return new LocationMonitor(context);
    }
}
