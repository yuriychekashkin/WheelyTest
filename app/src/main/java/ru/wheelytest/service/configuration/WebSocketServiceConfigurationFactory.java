package ru.wheelytest.service.configuration;

import android.support.annotation.NonNull;

import okhttp3.OkHttpClient;
import ru.wheelytest.business.LocationMonitor;
import ru.wheelytest.service.BroadcastSender;
import ru.wheelytest.business.serialization.GsonConverter;
import ru.wheelytest.business.storage.UserPreferenceStorage;
import ru.wheelytest.service.WebSocketManager;

/**
 * @author Yuriy Chekashkin
 */
public interface WebSocketServiceConfigurationFactory {

    @NonNull
    WebSocketManager createWebSocketManager(WebSocketManager.WebSocketMessageListener listener);

    @NonNull
    GsonConverter createMessagesConverter();

    @NonNull
    OkHttpClient createHttpClient();

    @NonNull
    BroadcastSender createBroadcastSender();

    @NonNull
    UserPreferenceStorage createUserStorage();

    @NonNull
    LocationMonitor createLocationMonitor();
}
