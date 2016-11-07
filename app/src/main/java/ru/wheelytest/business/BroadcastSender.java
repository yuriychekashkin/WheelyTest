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

    public static final String BROADCAST_ACTION_CONNECT = "BROADCAST_ACTION_CONNECT";
    public static final String BROADCAST_ACTION_NEW_DATA = "BROADCAST_ACTION_NEW_DATA";

    private final Context context;

    public BroadcastSender(Context context){
        this.context = context;
    }

    public void sendGpsPointsBroadcast(ArrayList<GpsPoint> gpsPointArrayList) {
        Intent broadcastIntent = new Intent(BROADCAST_ACTION_NEW_DATA);
        broadcastIntent.putParcelableArrayListExtra(EXTRA_BROADCAST_GPS_POINTS, gpsPointArrayList);
        context.sendBroadcast(broadcastIntent);
    }

    public void sendAuthSuccessBroadcast(boolean isAuthSuccess) {
        Intent broadcastIntent = new Intent(BROADCAST_ACTION_CONNECT);
        broadcastIntent.putExtra(EXTRA_BROADCAST_AUTH_SUCCESS, isAuthSuccess);
        context.sendBroadcast(broadcastIntent);
    }
}
