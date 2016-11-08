package ru.wheelytest.business;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import ru.wheelytest.domain.entity.GpsPoint;

/**
 * @author Yuriy Chekashkin
 */
public class LocationMonitor {

    private static final long TIMER_PERIOD = TimeUnit.MINUTES.toMillis(1);
    private static final long TIMER_FIRST_DELAY = 0;

    private final LocationManager locationManager;
    private final Timer timer;

    private LocationListener locationListener;

    public LocationMonitor(@NonNull Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        timer = new Timer();
    }

    public void start(@NonNull LocationListener locationListener){
        this.locationListener = locationListener;
        timer.schedule(new UpdateLocationTask(), TIMER_FIRST_DELAY, TIMER_PERIOD);
    }

    public void stop(){
        locationListener = null;
        timer.cancel();
    }

    @SuppressWarnings("MissingPermission")
    private GpsPoint getLocation() {
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        return new GpsPoint(location.getLatitude(), location.getLongitude());
    }

    public interface LocationListener {
        void onLocationUpdate(GpsPoint point);
    }

    private class UpdateLocationTask extends TimerTask {

        @Override
        public void run() {
            GpsPoint point = getLocation();
            if (locationListener != null) {
                locationListener.onLocationUpdate(point);
            }
        }
    }
}
