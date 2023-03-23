package com.yb.fish.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Test {
    public static void main(String[] args){
        List references = new ArrayList<>();
        references.add(1);references.add(2);
        System.out.println(ThreadLocalRandom.current().nextDouble());
        while (true){

        }
    }
}
