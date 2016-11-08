package ru.wheelytest.business.network;

import java.util.ArrayList;
import java.util.List;

import ru.wheelytest.domain.entity.GpsPoint;

/**
 * @author Yuriy Chekashkin
 */
public class WebSocketManagerStub implements RPCManager {

    private final WebSocketMessageListener socketListener;
    private boolean isConnected = false;

    public WebSocketManagerStub(WebSocketMessageListener socketListener) {
        this.socketListener = socketListener;
    }

    @Override
    public void send(GpsPoint gpsPoint) {
        List<GpsPoint> points = generateStub(gpsPoint);
        socketListener.onPointsReceived(points);
    }

    @Override
    public void tryConnect(String url) {
        isConnected = true;
        socketListener.onSuccessConnection();
    }

    @Override
    public void disconnect() {
        isConnected = false;
    }

    @Override
    public boolean isConnected() {
        return isConnected;
    }

    private List<GpsPoint> generateStub(GpsPoint gpsPoint) {
        List<GpsPoint> points = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            points.add(new GpsPoint(gpsPoint.getLatitude(), gpsPoint.getLongitude() + 0.5 * i));
        }
        return points;
    }
}
