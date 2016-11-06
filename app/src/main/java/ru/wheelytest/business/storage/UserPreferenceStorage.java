package ru.wheelytest.business.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import ru.wheelytest.domain.entity.User;

/**
 * @author Yuriy Chekashkin
 */
public class UserPreferenceStorage implements UserStorage {

    private static final String USER_STORAGE_PREFERENCES = "USER_STORAGE_PREFERENCES";
    private static final String LOGIN_PREFERENCE = "LOGIN_PREFERENCE";
    private static final String PASSWORD_PREFERENCE = "PASSWORD_PREFERENCE";

    private final SharedPreferences sharedPreferences;

    public UserPreferenceStorage(@NonNull Context context) {
        sharedPreferences = context.getSharedPreferences(USER_STORAGE_PREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    public void saveUser(User user) {
        saveUser(user.getLogin(), user.getPassword());
    }

    @Override
    public User getUser() {
        if (hasUser()) {
            String login = sharedPreferences.getString(LOGIN_PREFERENCE, null);
            String password = sharedPreferences.getString(PASSWORD_PREFERENCE, null);
            return new User(login, password);
        } else {
            return null;
        }
    }

    @Override
    public boolean hasUser() {
        return sharedPreferences.contains(LOGIN_PREFERENCE) && sharedPreferences.contains(PASSWORD_PREFERENCE);
    }

    public void saveUser(@NonNull String login, @NonNull String password) {
        sharedPreferences.edit()
                .putString(LOGIN_PREFERENCE, login)
                .putString(PASSWORD_PREFERENCE, password)
                .apply();
    }
}
