package com.yb.fish.interview;

/**
 * 题目：一共有n节楼梯，每次只能爬1步或者2步，问共有多少种爬行方式？
 *
 * @author bing
 * @version 1.0
 * @create 10/10/2022
 **/
public class Recursion {
    /**
     * 递归操作
     * 1.无论多少节台阶，都只能拆分为1个台阶和2个台阶的爬法;
     * 2.不同点是第一步是1还是2两大分类；
     * 3.后面就是这两大分类拆分成很多1个台阶和2个台阶的爬法的小组合，求之和；
     * @param n
     * @return
     */
    public static int step(int n) {
        if (n == 1) {
            return 1;
        }
        if (n == 2) {
            return 2;
        }
        return step(n - 1) + step(n - 2);
    }

    /**
     * 循环遍历操作
     *
     * @param n
     * @return
     */
    public static int forStep(int n) {
        //如果台阶数为1、2直接返回已知结果；
        if (n == 1) {
            return 1;
        }
        if (n == 2) {
            return 2;
        }
        //为方便索引位置从1开始的，所以数组长度也+1
        int arr[] = new int[n + 1];
        //存储1个台阶有一种走法；
        arr[1] = 1;
        //存储2个台阶有两种走法；
        arr[2] = 2;
        //把台阶做拆分，从3开始;
        for (int i = 3; i < n + 1; i++) {
            /**
             * 这里的处理很有技巧：
             * 1.利用已知容器中1和2的数量，对后面的台阶做分解；
             * 2.计算并存储了3及以后的数据，供后面(4、5、6...)直接使用
             * 例如：n=5，在走到3、4、5的时候各有多少走法，最后把这些求和；
             */
            arr[i] = arr[i - 1] + arr[i - 2];
        }
        return arr[n];

    }

    public static void main(String[] args) {
        System.out.println(step(4));

    }
}
