package com.example.toby.springbook.learningtest.template;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;

public class CalcSumTest {

    public static void main(String[] args) {
        JUnitCore.main("com.example.toby.springbook.learningtest.template.CalcSumTest");
    }

    Calculator calculator;
    String numFilepath;

    @Before
    public void setUp() {
        this.calculator = new Calculator();
        this.numFilepath = "numbers.txt";
    }

    @Test
    public void sumOfNumbers() throws IOException {
        Assert.assertThat(calculator.calcSum(this.numFilepath), is(10));
    }

    @Test
    public void multipleOfNumbers() throws IOException {
        Assert.assertThat(calculator.calcMultiply(this.numFilepath), is(24));
    }

    @Test
    public void concatenateStrings() throws IOException {
        Assert.assertThat(calculator.concatenate(this.numFilepath), is("1234"));
    }
}
