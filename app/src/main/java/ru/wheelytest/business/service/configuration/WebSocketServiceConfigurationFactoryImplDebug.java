package ru.wheelytest.business.service.configuration;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import ru.wheelytest.business.network.RPCManager;
import ru.wheelytest.business.network.WebSocketManagerStub;
import ru.wheelytest.model.entity.GpsPoint;
import ru.wheelytest.business.service.BroadcastSender;
import ru.wheelytest.business.service.EventSender;

/**
 * @author Yuriy Chekashkin
 */
public class WebSocketServiceConfigurationFactoryImplDebug extends WebSocketServiceConfigurationFactoryImpl {

    private final Context context;

    public WebSocketServiceConfigurationFactoryImplDebug(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @NonNull
    @Override
    public RPCManager createWebSocketManager(@NonNull RPCManager.WebSocketMessageListener listener) {
        return new WebSocketManagerStub(listener);
    }

    @NonNull
    @Override
    public EventSender createBroadcastSender() {
        return new BroadcastSender(context) {
            @Override
            public void sendGpsPointsBroadcast(ArrayList<GpsPoint> gpsPointArrayList) {
                // do nothing
            }
        };
    }
}
