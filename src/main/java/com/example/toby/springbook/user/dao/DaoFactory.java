package com.example.toby.springbook.user.dao;

import com.example.toby.springbook.user.service.DummyMailSender;
import com.example.toby.springbook.user.service.UserServiceImpl;
import com.example.toby.springbook.user.service.UserServiceTx;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class DaoFactory {

    @Bean
    public UserDao userDao() {
        UserDaoJdbc userDao = new UserDaoJdbc();
        userDao.setDataSource(dataSource());
        return userDao;
    }

    @Bean
    public UserServiceImpl userServiceImpl() {
        UserServiceImpl userService = new UserServiceImpl();
        userService.setUserDao(userDao());
        userService.setMailSender(mailSender());
        return userService;
    }

    @Bean
    public UserServiceTx userService() {
        UserServiceTx userService = new UserServiceTx();
        userService.setTransactionManager(transactionManager());
        userService.setUserService(userServiceImpl());

        return userService;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
//        return new JtaTransactionManager();
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public MailSender mailSender() {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        DummyMailSender mailSender = new DummyMailSender();
//        mailSender.setHost("smtp.gmail.com");
        return mailSender;
    }

    @Bean
    public ConnectionMaker connectionMaker() {
        return new DConnectionMaker();
    }

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(org.mariadb.jdbc.Driver.class);
        dataSource.setUrl("");
        dataSource.setUsername("ksy");
        dataSource.setPassword("");

        return dataSource;
    }
}
