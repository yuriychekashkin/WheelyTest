package ru.wheelytest.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.wheelytest.R;
import ru.wheelytest.business.storage.UserPreferenceStorage;
import ru.wheelytest.business.storage.UserStorage;
import ru.wheelytest.service.BroadcastSender;
import ru.wheelytest.domain.entity.GpsPoint;
import ru.wheelytest.service.WebSocketService;

/**
 * @author Yuriy Chekashkin
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mapView;

    private UserStorage userStorage;

    private List<GpsPoint> points;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            points = intent.getParcelableArrayListExtra(WebSocketService.EXTRA_BROADCAST_GPS_POINTS);
            if (isMapReady()) {
                updatePointsView();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        registerNewDataReceiver();
        userStorage = new UserPreferenceStorage(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterNewDataReceiver();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapView = googleMap;
        mapView.animateCamera(CameraUpdateFactory.zoomTo(8));
        if (hasData()) {
            updatePointsView();
        }
    }

    @OnClick(R.id.disconnect)
    public void onDisconnectClick() {
        unregisterNewDataReceiver();
        userStorage.clear();
        WebSocketService.stop(this);
        finish();
    }

    private void updatePointsView() {
        mapView.clear();

        LatLng latLng = null;
        for (GpsPoint point : points) {
            latLng = createLatLngFromGpsPoint(point);
            mapView.addMarker(new MarkerOptions().position(latLng));
        }

        if (latLng != null) {
            mapView.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    private LatLng createLatLngFromGpsPoint(GpsPoint point) {
        return new LatLng(point.getLatitude(), point.getLongitude());
    }

    private boolean hasData() {
        return points != null;
    }

    private boolean isMapReady() {
        return mapView != null;
    }

    private void registerNewDataReceiver() {
        registerReceiver(broadcastReceiver, new IntentFilter(BroadcastSender.BROADCAST_ACTION_NEW_DATA));
    }

    private void unregisterNewDataReceiver() {
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e){
            // receiver not registered - do nothing
        }
    }
}
