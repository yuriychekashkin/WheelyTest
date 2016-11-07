package ru.wheelytest.business.storage;

import ru.wheelytest.domain.entity.User;

/**
 * @author Yuriy Chekashkin
 */
public interface UserStorage {
    void saveUser(User user);

    User getUser();

    boolean hasUser();

    void clear();
}
