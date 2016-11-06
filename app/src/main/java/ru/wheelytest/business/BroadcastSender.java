package ru.wheelytest.business;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import ru.wheelytest.domain.entity.GpsPoint;

import static ru.wheelytest.service.WebSocketService.EXTRA_BROADCAST_AUTH_SUCCESS;
import static ru.wheelytest.service.WebSocketService.EXTRA_BROADCAST_GPS_POINTS;

/**
 * @author Yuriy Chekashkin
 */
public class BroadcastSender {

    private final Context context;

    public BroadcastSender(Context context){
        this.context = context;
    }

    public void sendGpsPointsBroadcast(ArrayList<GpsPoint> gpsPointArrayList) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.putParcelableArrayListExtra(EXTRA_BROADCAST_GPS_POINTS, gpsPointArrayList);
        context.sendBroadcast(broadcastIntent);
    }

    public void sendAuthSuccessBroadcast(boolean isAuthSuccess) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.putExtra(EXTRA_BROADCAST_AUTH_SUCCESS, isAuthSuccess);
        context.sendBroadcast(broadcastIntent);
    }
}
