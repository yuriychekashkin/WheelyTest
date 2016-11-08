package ru.wheelytest.business.service.configuration;

import android.support.annotation.NonNull;

import okhttp3.OkHttpClient;
import ru.wheelytest.business.location.LocationMonitor;
import ru.wheelytest.business.serialization.GsonConverter;
import ru.wheelytest.business.storage.UserPreferenceStorage;
import ru.wheelytest.business.network.RPCManager;
import ru.wheelytest.business.service.EventSender;

/**
 * @author Yuriy Chekashkin
 */
public interface WebSocketServiceConfigurationFactory {

    @NonNull
    RPCManager createWebSocketManager(RPCManager.WebSocketMessageListener listener);

    @NonNull
    GsonConverter createMessagesConverter();

    @NonNull
    OkHttpClient createHttpClient();

    @NonNull
    EventSender createBroadcastSender();

    @NonNull
    UserPreferenceStorage createUserStorage();

    @NonNull
    LocationMonitor createLocationMonitor();
}
