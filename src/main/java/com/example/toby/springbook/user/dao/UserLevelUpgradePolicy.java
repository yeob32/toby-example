package com.example.toby.springbook.user.dao;

import com.example.toby.springbook.user.domain.User;

public interface UserLevelUpgradePolicy {
    boolean canUpgradeLevel(User user);
    void upgradeLevel(User user);
}
