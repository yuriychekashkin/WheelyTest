package ru.wheelytest.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.wheelytest.R;
import ru.wheelytest.service.BroadcastSender;
import ru.wheelytest.business.storage.UserPreferenceStorage;
import ru.wheelytest.business.storage.UserStorage;
import ru.wheelytest.service.WebSocketService;

/**
 * @author Yuriy Chekashkin
 */
public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.input_login)
    EditText loginInput;

    @Bind(R.id.input_password)
    EditText passwordInput;

    private UserStorage userStorage;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isAuthSuccess = intent.getBooleanExtra(WebSocketService.EXTRA_BROADCAST_AUTH_SUCCESS, false);
            if (isAuthSuccess){
                startMapActivity();
            } else {
                showConnectError();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        userStorage = new UserPreferenceStorage(this);

        if (userStorage.hasUser()){
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

    @OnClick(R.id.connect)
    public void onConnectClick() {
        WebSocketService.start(this);
    }

    private void registerConnectReceiver() {
        registerReceiver(broadcastReceiver, new IntentFilter(BroadcastSender.BROADCAST_ACTION_CONNECT));
    }

    private void unregisterConnectReceiver(){
        unregisterReceiver(broadcastReceiver);
    }

    private void startMapActivity() {
        startActivity(new Intent(this, MapsActivity.class));
    }

    private void showConnectError() {
        Toast.makeText(this, R.string.connect_error, Toast.LENGTH_LONG).show();
    }
}
