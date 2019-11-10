package com.example.toby.springbook;

import com.example.toby.springbook.user.dao.TransactionHandler;
import com.example.toby.springbook.user.dao.UserDao;
import com.example.toby.springbook.user.domain.Level;
import com.example.toby.springbook.user.domain.User;
import com.example.toby.springbook.user.service.UserService;
import com.example.toby.springbook.user.service.UserServiceImpl;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserDao userDao;
    @Autowired
    UserService userService;
    @Autowired
    UserServiceImpl userServiceImpl;
    @Autowired
    DataSource dataSource;
    @Autowired
    PlatformTransactionManager transactionManager;
    @Autowired
    MailSender mailSender;

    private List<User> users;

    public UserServiceTest() {
        System.out.println("this is constructor : " + (userDao == null));
    }

    @PostConstruct
    public void init() {
        System.out.println("this is PostConstruct : " + (userDao == null));
    }

    @Test
    public void postTest() {
        System.out.println("postTest !!");
    }

    @Test
    public void bean() {
        Assert.assertThat(this.userService, Matchers.is(Matchers.notNullValue()));
    }

    @Before
    public void setUp() {
        System.out.println("this is Before");
        users = Arrays.asList(new User("bumjin", "park", "p1", Level.BASIC, UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER - 1, 0, "bumjin@gmail.com"),
                new User("joytouch", "kang", "p2", Level.BASIC, UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER, 0, "joytouch@gmail.com"),
                new User("erwins", "shin", "p3", Level.SILVER, 60, UserServiceImpl.MIN_RECCOMEND_FOR_GOLD - 1, "erwins@gmail.com"),
                new User("madnite1", "lee", "p4", Level.SILVER, 60, UserServiceImpl.MIN_RECCOMEND_FOR_GOLD, "madnite1@gmail.com"),
                new User("green", "oh", "p5", Level.GOLD, 100, Integer.MAX_VALUE, "green@gmail.com")
        );
    }

    @After
    public void after() {
        System.out.println("this is After");
    }

    @Test
    @DirtiesContext // 애플리케이션 컨텍스트 공유를 허용하지 않는다. 테스트 중에 변경한 컨텍스트가 뒤의 테스트에 영향을 주지 않게 하기 위해서다.
    public void upgradeLevels() throws Exception {

        UserServiceImpl userServiceImpl = new UserServiceImpl();

        MockUserDao mockUserDao = new MockUserDao(this.users);
        userServiceImpl.setUserDao(mockUserDao);

//        UserDao mockUserDao = Mockito.mock(UserDao.class);
//        Mockito.when(mockUserDao.getAll()).thenReturn(this.users);
//        userServiceImpl.setUserDao(mockUserDao);
//        Mockito.verify(mockUserDao, Mockito.times(2)).update(Mockito.any(User.class)); // update 메소드가 두 번 호출됐는지 확인해라.

        MockMailSender mockMailSender = new MockMailSender();
        userServiceImpl.setMailSender(mockMailSender);

//        MailSender mockMailSender = Mockito.mock(MailSender.class);
//        userServiceImpl.setMailSender(mockMailSender);

        userServiceImpl.upgradeLevels();

//        Mockito.verify(mockUserDao, Mockito.times(2)).update(Mockito.any(User.class));
//        Mockito.verify(mockUserDao, Mockito.times(2)).update(Mockito.any(User.class));
//        Mockito.verify(mockUserDao).update(users.get(1));
//        Assert.assertThat(users.get(1).getLevel(), Matchers.is(Level.SILVER));
//        Mockito.verify(mockUserDao).update(users.get(3));
//        Assert.assertThat(users.get(3).getLevel(), Matchers.is(Level.GOLD));

//        ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
//        Mockito.verify(mockMailSender, Mockito.times(2)).send(mailMessageArg.capture());
//        List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
//        Assert.assertThat(mailMessages.get(0).getTo()[0], Matchers.is(users.get(1).getEmail()));
//        Assert.assertThat(mailMessages.get(1).getTo()[0], Matchers.is(users.get(3).getEmail()));

        List<User> updated = mockUserDao.getUpdated();
        Assert.assertThat(updated.size(), Matchers.is(2));
        checkUserAndLevel(updated.get(0), "joytouch", Level.SILVER);
        checkUserAndLevel(updated.get(1), "madnite1", Level.GOLD);

        List<String> requests = mockMailSender.getRequests();
        Assert.assertThat(requests.size(), Matchers.is(2));
        Assert.assertThat(requests.get(0), Matchers.is(users.get(1).getEmail()));
        Assert.assertThat(requests.get(1), Matchers.is(users.get(3).getEmail()));
    }

    private void checkUserAndLevel(User user, String expectedId, Level expectedLevel) {
        Assert.assertThat(user.getId(), Matchers.is(expectedId));
        Assert.assertThat(user.getLevel(), Matchers.is(expectedLevel));
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());
        if (upgraded) {
            Assert.assertThat(userUpdate.getLevel(), Matchers.is(user.getLevel().nextLevel()));
        } else {
            Assert.assertThat(userUpdate.getLevel(), Matchers.is(user.getLevel()));
        }
    }

    @Test
    public void add() {
        userDao.deleteAll();

        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        Assert.assertThat(userWithLevelRead.getLevel(), Matchers.is(userWithLevel.getLevel()));
        Assert.assertThat(userWithoutLevelRead.getLevel(), Matchers.is(Level.BASIC));
    }

    static class TestUserService extends UserServiceImpl {
        private String id;

        private TestUserService(String id) {
            this.id = id;
        }

        public void upgradeLevel(User user) {
            if (user.getId().equals(this.id)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        }
    }

    static class TestUserServiceException extends RuntimeException {

    }

    @Test
    public void upgradeAllOrNothing() {
        TestUserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(this.userDao);
        testUserService.setMailSender(this.mailSender);

//        UserServiceTx userServiceTx = new UserServiceTx();
//        userServiceTx.setTransactionManager(this.transactionManager);
//        userServiceTx.setUserService(testUserService);
        TransactionHandler txHandler = new TransactionHandler();
        txHandler.setTarget(testUserService);
        txHandler.setTransactionManager(this.transactionManager);
        txHandler.setPattern("upgradeLevels");

        UserService userServiceTx = (UserService) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{UserService.class}, txHandler);

        userDao.deleteAll();
        for (User user : users) userDao.add(user);

        System.out.println("before users.get(1) : " + users.get(1).getLevel());
        try {
            userServiceTx.upgradeLevels();
            Assert.fail("TestUserServiceException expected");
        } catch (TestUserServiceException e) {
            System.out.println("TestUserServiceException");
        }

        System.out.println("after users.get(1) : " + userDao.get(users.get(1).getId()).getLevel());
        checkLevelUpgraded(users.get(1), false);
    }

    static class MockMailSender implements MailSender {
        private List<String> requests = new ArrayList<>();

        public List<String> getRequests() {
            return requests;
        }

        @Override
        public void send(SimpleMailMessage simpleMessage) throws MailException {
            if (simpleMessage.getTo() != null) {
                requests.add(simpleMessage.getTo()[0]);
            }
        }

        @Override
        public void send(SimpleMailMessage... simpleMessages) throws MailException {

        }
    }

    static class MockUserDao implements UserDao {
        private List<User> users; // 업그레드 후보 목록
        private List<User> updated = new ArrayList<User>();

        private MockUserDao(List<User> users) {
            this.users = users;
        }

        public List<User> getUpdated() {
            return this.updated;
        }

        @Override
        public List<User> getAll() {
            return this.users;
        }

        @Override
        public void update(User user) {
            updated.add(user);
        }

        @Override
        public void add(User user) {
            throw new UnsupportedOperationException();
        }

        @Override
        public User get(String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAll() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getCount() {
            throw new UnsupportedOperationException();
        }
    }

    @Test
    @Transactional(readOnly = true)
    @Rollback(value = false) // 테스트 메소드에서의 트랜잭션 애노테이션은 실행 끝나면 무조건 롤백시킴 -> @RollBack 설정 시 롤백안함
    public void transactionSync() {
        userService.add(new User("testset", "ggg", "1234", Level.SILVER, 30, 50, "test@gmail.com"));
    }
}
