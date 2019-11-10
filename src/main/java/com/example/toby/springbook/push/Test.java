package com.example.toby.springbook.push;

public class Test {

    private Push push;

    public Test(Push push) {
        this.push = push;
    }

    public static void main(String[] args) {
        Push push = new PushService();
        push.send();
        Test test1 = new Test(push);
        test1.push.send();

        Push push2 = new AnotherPushService();
        Test test2 = new Test(push2);
        test2.push.send();

    }
}
