package com.example.toby.springbook.learningtest.template;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
    public Integer calcSum(String filePath) throws IOException {
        LineCallback<Integer> sumCallback = new LineCallback<Integer>() {
            @Override
            public Integer doSomethingWithLine(String line, Integer value) {
                return value + Integer.parseInt(line);
            }
        };

        return lineReadTemplate(filePath, sumCallback, 0);
    }

    public Integer calcMultiply(String numFilepath) throws IOException {

        LineCallback<Integer> multiplyCallback = new LineCallback<Integer>() {
            @Override
            public Integer doSomethingWithLine(String line, Integer value) {
                return value * Integer.parseInt(line);
            }
        };

        return lineReadTemplate(numFilepath, multiplyCallback, 1);
    }

    public Integer fileReadTemplate(String filePath, BufferedReaderCallback bufferedReaderCallback) throws IOException {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(filePath));
            int result = bufferedReaderCallback.doSomethingWithReader(bufferedReader);

            return result;
        } catch(IOException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if(bufferedReader != null) {
                try {
                    bufferedReader.close();
                }catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

    }

    public <T> T lineReadTemplate(String filePath, LineCallback<T> lineCallback, T initVal) throws IOException {

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(filePath));

            T res = initVal;
            String line = null;
            while((line = bufferedReader.readLine()) != null) {
                res = lineCallback.doSomethingWithLine(line, res);
            }

            return res;
        } catch(IOException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if(bufferedReader != null) {
                try {
                    bufferedReader.close();
                }catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

    }

    public String concatenate(String filepath) throws IOException {
        LineCallback<String> concatenateCallback = new LineCallback<String>() {
            @Override
            public String doSomethingWithLine(String line, String value) {
                return value + line;
            }
        };

        return lineReadTemplate(filepath, concatenateCallback, "");
    }

}
