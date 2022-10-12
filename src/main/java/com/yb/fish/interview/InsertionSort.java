package com.yb.fish.interview;

import java.util.Arrays;

/**
 * 插入排序
 *
 * @author bing
 * @version 1.0
 * @create 11/10/2022
 **/
public class InsertionSort {
    /**
     * 思路：
     * 1.将待排序数组分为两部分，已排序部分和未排序部分，初始的已排序数组只有第一个元素;
     * 2.操作是将未排序部分的元素取出，在已排序部分找到合适的位置并插入;
     * 3.直到未排序区间没有值结束;
     *
     * @param arr
     */
    public static void insertionSort(int arr[]) {

        for (int i = 1; i < arr.length; i++) {
            //保存当前索引位置的值;
            int temp = arr[i];
            int j = 0;
            /**
             * 1.用前面的值和当前的值作比较；
             * 2.如果当前的值大于前面的值,直接插入；
             * 3.反之，前面的值和后面的值交互位置;
             */
            for (j = i - 1; j >= 0 && arr[j] > temp; j--) {
                arr[j + 1] = arr[j];//移动
            }
            //当前位置
            arr[j + 1] = temp;//插入

        }
        System.out.println(Arrays.toString(arr));

    }


    public static void main(String[] args) {
        int arr[] = {9, 2, 88, 3, 5, 16, 7};
        insertionSort(arr);
    }
}
