package com.example.toby.springbook.user.dao;

import com.example.toby.springbook.user.domain.User;

import java.util.List;

public interface UserDao {
    void add(User user);
    User get(String id);
    List<User> getAll();
    void deleteAll();
    int getCount();

    public void update(User user);
}
