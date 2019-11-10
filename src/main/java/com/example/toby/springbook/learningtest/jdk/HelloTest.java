package com.example.toby.springbook.learningtest.jdk;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.JUnitCore;

import java.lang.reflect.Proxy;

public class HelloTest {
    public static void main(String[] args) {
        JUnitCore.main("com.example.toby.springbook.learningtest.jdk.HelloTest");
    }

    @Test
    public void SimpleProxy() {
        Hello hello = new HelloTarget();
        Assert.assertThat(hello.sayHello("Toby"), Matchers.is("Hello Toby"));
        Assert.assertThat(hello.sayHi("Toby"), Matchers.is("Hi Toby"));
        Assert.assertThat(hello.sayThankYou("Toby"), Matchers.is("Thank You Toby"));

        Hello proxiedHello = new HelloUppercase(new HelloTarget());
        Assert.assertThat(proxiedHello.sayHello("Toby"), Matchers.is("HELLO TOBY"));
        Assert.assertThat(proxiedHello.sayHi("Toby"), Matchers.is("HI TOBY"));
        Assert.assertThat(proxiedHello.sayThankYou("Toby"), Matchers.is("THANK YOU TOBY"));

        Hello proxiedHello2 = (Hello) Proxy.newProxyInstance(Hello.class.getClassLoader(),
                new Class<?>[]{Hello.class},
                new UppercaseHandler(new HelloTarget()));

        Assert.assertThat(proxiedHello2.sayHello("Toby"), Matchers.is("HELLO TOBY"));
        Assert.assertThat(proxiedHello2.sayHi("Toby"), Matchers.is("HI TOBY"));
        Assert.assertThat(proxiedHello2.sayThankYou("Toby"), Matchers.is("THANK YOU TOBY"));
    }
}
