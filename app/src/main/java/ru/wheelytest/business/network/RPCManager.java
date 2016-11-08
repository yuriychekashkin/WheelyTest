package ru.wheelytest.business.network;

import java.util.List;

import ru.wheelytest.domain.entity.GpsPoint;

/**
 * @author Yuriy Chekashkin
 */
public interface RPCManager {
    void send(GpsPoint gpsPoint);

    void tryConnect(String url);

    void disconnect();

    boolean isConnected();

    interface WebSocketMessageListener {
        void onSuccessConnection();

        void onFailedConnection();

        void onPointsReceived(List<GpsPoint> gpsPoints);
    }
}
