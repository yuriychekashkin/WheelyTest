package ru.wheelytest.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.wheelytest.R;
import ru.wheelytest.domain.entity.User;
import ru.wheelytest.service.BroadcastSender;
import ru.wheelytest.business.storage.UserPreferenceStorage;
import ru.wheelytest.business.storage.UserStorage;
import ru.wheelytest.service.WebSocketService;

/**
 * @author Yuriy Chekashkin
 */
public class LoginActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;

    @Bind(R.id.input_login)
    EditText loginInput;

    @Bind(R.id.input_password)
    EditText passwordInput;

    @Bind(R.id.progress_container)
    View progressContainer;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isAuthSuccess = intent.getBooleanExtra(WebSocketService.EXTRA_BROADCAST_AUTH_SUCCESS, false);
            if (isAuthSuccess) {
                startMapActivity();
            } else {
                showConnectError();
            }
            hideProgress();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        UserStorage userStorage = new UserPreferenceStorage(this);

        if (false && userStorage.hasUser()) {
            startMapActivity();
        } else {
            registerConnectReceiver();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterConnectReceiver();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length == 2
                && (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            startService();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @OnClick(R.id.connect)
    public void onConnectClick() {
        if (isLocationPermissionsGranted()) {
            startService();
        } else {
            requestPermissions();
        }
    }

    private void showProgress() {
        progressContainer.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progressContainer.setVisibility(View.GONE);
    }

    private User getUserFromInput() {
        String login = loginInput.getText().toString();
        String password = passwordInput.getText().toString();
        return new User(login, password);
    }

    private void registerConnectReceiver() {
        registerReceiver(broadcastReceiver, new IntentFilter(BroadcastSender.BROADCAST_ACTION_CONNECT));
    }

    private void unregisterConnectReceiver() {
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            // receiver not registered - do nothing
        }
    }

    private void startService() {
        WebSocketService.start(this, getUserFromInput());
        showProgress();
    }

    private void startMapActivity() {
        startActivity(new Intent(this, MapsActivity.class));
    }

    private void showConnectError() {
        Toast.makeText(this, R.string.connect_error, Toast.LENGTH_LONG).show();
    }

    private void requestPermissions() {
        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
    }

    private boolean isLocationPermissionsGranted() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
}
