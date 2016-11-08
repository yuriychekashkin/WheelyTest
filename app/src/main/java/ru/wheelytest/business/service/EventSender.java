package ru.wheelytest.business.service;

import java.util.ArrayList;

import ru.wheelytest.model.entity.GpsPoint;

/**
 * @author Yuriy Chekashkin
 */
public interface EventSender {

    void sendGpsPointsBroadcast(ArrayList<GpsPoint> gpsPointArrayList);

    void sendAuthSuccessBroadcast(boolean isAuthSuccess);
}
