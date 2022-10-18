package com.yb.fish.interview;

public class Test {
    public static void main(String[] args) {
        Test test = new Test();
        test.execute(10);
        int i = 1 / 2;
        System.out.println(i);

    }

    public void execute(int i) {
        System.out.println("入栈:"+i);
        if (i <= 0) {
            System.out.println("触发终止条件");
            return;
        }
        execute(--i);
        print(i);
    }

    public void print(int i){
        System.out.println("输出:"+i);
    }
}
