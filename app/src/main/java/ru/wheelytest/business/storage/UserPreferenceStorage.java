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
    private static final String PREFERENCE_LOGIN = "PREFERENCE_LOGIN";
    private static final String PREFERENCE_PASSWORD = "PREFERENCE_PASSWORD";

    private final SharedPreferences sharedPreferences;

    public UserPreferenceStorage(@NonNull Context context) {
        sharedPreferences = context.getSharedPreferences(USER_STORAGE_PREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    public void saveUser(User user) {
        sharedPreferences.edit()
                .putString(PREFERENCE_LOGIN, user.getLogin())
                .putString(PREFERENCE_PASSWORD, user.getPassword())
                .apply();
    }

    @Override
    public User getUser() {
        if (hasUser()) {
            String login = sharedPreferences.getString(PREFERENCE_LOGIN, null);
            String password = sharedPreferences.getString(PREFERENCE_PASSWORD, null);
            return new User(login, password);
        } else {
            return null;
        }
    }

    @Override
    public boolean hasUser() {
        return sharedPreferences.contains(PREFERENCE_LOGIN) && sharedPreferences.contains(PREFERENCE_PASSWORD);
    }

    @Override
    public void clear() {
        sharedPreferences.edit().clear().apply();
    }
}
