package com.example.toby.springbook.push;

public class PushService implements Push {
    @Override
    public void send() {
        System.out.println("this is " + this.getClass());
    }
}
