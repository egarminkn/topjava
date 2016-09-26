package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.List;
import java.util.Arrays;

public class UsersUtil {
    public static final List<User> USERS = Arrays.asList(
        new User(null, "Петя", "petya@mail.ru", "passw0rd", Role.ROLE_USER),
        new User(null, "Вася", "vasya@mail.ru", "passw1rd", Role.ROLE_ADMIN)
    );
}