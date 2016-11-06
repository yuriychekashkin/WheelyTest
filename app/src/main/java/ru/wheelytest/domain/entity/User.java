package ru.wheelytest.domain.entity;

import java.io.Serializable;

/**
 * @author Yuriy Chekashkin
 */
public class User implements Serializable {

    private final String login;
    private final String password;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
