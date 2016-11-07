package ru.wheelytest.business.serialization;

import java.util.List;

import ru.wheelytest.domain.entity.GpsPoint;

/**
 * @author Yuriy Chekashkin
 */
public interface WebSocketMessagesConverter {
    List<GpsPoint> deserialize(String message);
    String serialize(GpsPoint gpsPoint);
}
