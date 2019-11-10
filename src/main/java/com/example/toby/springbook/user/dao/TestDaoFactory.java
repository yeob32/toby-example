package com.example.toby.springbook.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

@Configuration
public class TestDaoFactory {

//    @Bean
//    public UserDao userDao() {
//        UserDao userDao = new UserDao();
//        userDao.setDataSource(dataSource());
//        return userDao;
//    }

    @Bean
    public ConnectionMaker connectionMaker() {
        return new DConnectionMaker();
    }

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(org.mariadb.jdbc.Driver.class);
        dataSource.setUrl("jdbc:mariadb");
        dataSource.setUsername("");
        dataSource.setPassword("");

        return dataSource;
    }
}
