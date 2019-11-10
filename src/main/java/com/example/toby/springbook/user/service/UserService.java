package com.example.toby.springbook.user.service;

import com.example.toby.springbook.user.domain.User;

public interface UserService {
    void add(User user);
    void upgradeLevels();
}
