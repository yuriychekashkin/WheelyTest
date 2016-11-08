package ru.wheelytest.service;

import java.util.ArrayList;

import ru.wheelytest.domain.entity.GpsPoint;

/**
 * @author Yuriy Chekashkin
 */
public interface EventSender {

    void sendGpsPointsBroadcast(ArrayList<GpsPoint> gpsPointArrayList);

    void sendAuthSuccessBroadcast(boolean isAuthSuccess);
}
