package com.yb.fish.exception;

public class TestMain {
    public static void main(String[] args) {

        try {
            execute("2");
            execute("1");
        } catch (NullPointerException | BusinessException e) {
            e.printStackTrace();
        }
    }

    private static void execute(String str) {
        if ("1".equals(str)){
            throw new BusinessException(666,"BusinessException");
        }else {
            throw new NullPointerException();
        }
    }
}
