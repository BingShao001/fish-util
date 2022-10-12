package com.yb.fish.interview;

/**
 * 1.每一轮通过循环比对，把最小的数据索引位置记录下来;
 * 2.每一轮比对结束把最小的值交互到最左边；
 *
 * @author bing
 * @version 1.0
 * @create 11/10/2022
 **/
public class SelectSort {

    public static void selectionSort(int[] nums) {
        int length = nums.length;
        //最小值的索引
        int minIndex;
        for (int i = 0; i < length; i++) {
            minIndex = i;
            //比较
            for (int j = i + 1; j < length; j++) {
                //寻找最小的值
                if (nums[j] < nums[minIndex]) {
                    minIndex = j;
                }
            }
            int tem = nums[i];
            nums[i] = nums[minIndex];
            nums[minIndex] = tem;
        }
    }

    public static void main(String[] args) {
        int arr[] = {9, 2, 88, 3, 5, 16, 7};
        selectionSort(arr);
    }

}
