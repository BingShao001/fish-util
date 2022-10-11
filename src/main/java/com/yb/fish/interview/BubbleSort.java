package com.yb.fish.interview;

import java.util.Arrays;

/**
 * 冒泡排序重点在于：
 * 1.相邻两个元素进行比较大小；
 * 2.原地交互值；
 * 3.每次内层循环执行1、2操作，只能使一个最大值有序，所以需要操作n次；
 * 4.之前排序操作后的元素已经有序，本次就不用再比较有序操作（只比较到有序的位置，也就是n-(i+1)之前，但不包括）
 * ，所以内层循环的条件为：j < n - i - 1
 *
 * @author bing
 * @version 1.0
 * @create 10/10/2022
 **/
public class BubbleSort {

    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        //每次只完成一个最大值排序，所以需要操作n次；
        for (int i = 0; i <= n - 1; i++) {
            System.out.println("——————————新一轮开始" + (i + 1) + "——————————");
            //优化点：问一下，结束了吗？
            boolean isEnd = true;
            //之前(包括上一次)排序后，元素已经是有序了(有序的索引位是n-(i+1)开始往后)，不用再进行比较了；
            for (int j = 0; j < n - (i + 1); j++) {
                //需要一个中间变量进行临时存储值，用于相邻值交换；
                int temp = 0;
                if (arr[j] > arr[j + 1]) {
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    //优化点：如果本轮依然有乱序，下一次继续循环
                    isEnd = false;
                }
                System.out.println(Arrays.toString(arr));
            }
            //优化点：如果本轮没有乱序，直接结束循环
            if (isEnd){
                break;
            }
        }
    }

    public static void main(String[] args) {
        int arr[] = {9, 2, 88, 3, 5, 16, 7};
        bubbleSort(arr);
    }
}
