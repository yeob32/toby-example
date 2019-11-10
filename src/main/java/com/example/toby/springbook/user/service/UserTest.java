package com.example.toby.springbook.user.service;

import com.example.toby.springbook.user.domain.Level;
import com.example.toby.springbook.user.domain.User;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTest {
    public static void main(String[] args) {
        JUnitCore.main("com.example.toby.springbook.user.service.UserTest");
    }

    User user;

    @Before
    public void setUp() {
        user = new User();
    }

    @Test
    public void upgradeLevel() {
        Level[] levels = Level.values();
        for (Level level : levels) {
            if (level.nextLevel() == null) {
                continue;
            }

            user.setLevel(level);
            user.upgradeLevel();
            Assert.assertThat(user.getLevel(), Matchers.is(level.nextLevel()));
        }
    }

    @Test(expected = IllegalStateException.class)
    public void cannotUpgradeLevel() {
        Level[] levels = Level.values();
        for (Level level : levels) {
            if(level.nextLevel() != null) {
                continue;
            }

            user.setLevel(level);
            user.upgradeLevel();
        }
    }
}
