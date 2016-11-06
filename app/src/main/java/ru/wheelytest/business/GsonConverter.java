package ru.wheelytest.business;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import ru.wheelytest.domain.entity.GpsPoint;

/**
 * @author Yuriy Chekashkin
 */
public class GsonConverter implements WebSocketMessagesConverter {

    @Override
    public List<GpsPoint> deserialize(String message) {
        Type type = new TypeToken<List<GpsPoint>>() {}.getType();
        return new Gson().fromJson(message, type);
    }

    @Override
    public String serialize(GpsPoint gpsPoint) {
        return new Gson().toJson(gpsPoint);
    }
}
