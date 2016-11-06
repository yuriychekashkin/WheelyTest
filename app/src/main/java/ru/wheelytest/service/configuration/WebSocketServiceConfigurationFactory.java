package ru.wheelytest.service.configuration;

import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import ru.wheelytest.business.BroadcastSender;
import ru.wheelytest.business.GsonConverter;
import ru.wheelytest.business.storage.UserPreferenceStorage;
import ru.wheelytest.service.WebSocketManager;

/**
 * @author Yuriy Chekashkin
 */
public interface WebSocketServiceConfigurationFactory {

    @NonNull
    public WebSocketManager createWebSocketManager(WebSocketManager.WebSocketMessageListener listener);

    @NonNull
    public GsonConverter createMessagesConverter();

    @NonNull
    public OkHttpClient createHttpClient();

    @NonNull
    public BroadcastSender createBroadcastSender();

    @NonNull
    public UserPreferenceStorage createUserStorage();
}
