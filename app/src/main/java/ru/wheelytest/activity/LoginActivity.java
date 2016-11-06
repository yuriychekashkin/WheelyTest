package ru.wheelytest.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.wheelytest.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        userStorage = new UserPreferenceStorage(this);

        if (userStorage.hasUser()){
            startMapActivity();
        }
    }

    @OnClick(R.id.connect)
    public void onConnectClick() {
        Intent serviceIntent = new Intent(this, WebSocketService.class);
        startService(serviceIntent);
    }

    private void startMapActivity() {
        startActivity(new Intent(this, MapsActivity.class));
        finish();
    }
}
