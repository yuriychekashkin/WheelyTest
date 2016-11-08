package ru.wheelytest.service.configuration;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import ru.wheelytest.business.LocationMonitor;
import ru.wheelytest.service.BroadcastSender;
import ru.wheelytest.business.serialization.GsonConverter;
import ru.wheelytest.business.storage.UserPreferenceStorage;
import ru.wheelytest.service.WebSocketManager;

/**
 * @author Yuriy Chekashkin
 */
public class WebSocketServiceConfigurationFactoryImpl implements WebSocketServiceConfigurationFactory {

    private final Context context;

    public WebSocketServiceConfigurationFactoryImpl(@NonNull Context context){
        this.context = context;
    }

    @NonNull
    public WebSocketManager createWebSocketManager(@NonNull WebSocketManager.WebSocketMessageListener listener) {
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
    public BroadcastSender createBroadcastSender() {
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
