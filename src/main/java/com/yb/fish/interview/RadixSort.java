package com.yb.fish.interview;

import java.util.ArrayList;

/**
 * 基数排序
 * 将个位按大小进行排序，并将次序列覆盖原有数组
 * 使用个位排序覆盖后的数组，将十位、百位…按同样方法操作，直到最长位排序结束。
 *
 * @author bing
 * @version 1.0
 * @create 18/10/2022
 **/
public class RadixSort {
    public static void main(String[] args) {
        int[] arr = {12351, 323, 122, 4455, 255, 66};
        radixSort(arr);
        printArr(arr);
    }

    private static void radixSort(int[] arr) {
        //创建10个桶
        ArrayList[] bucket = new ArrayList[10];
        //初始化桶
        for (int i = 0; i < bucket.length; i++) {
            bucket[i] = new ArrayList();
        }
        //获取待排序数组中最大的数
        int max = maxOf(arr);
        //获取最高位
        int numLength = getMaxLength(max);
        //对每一位进行排序
        for (int i = 1; i <= numLength; i++) {
            sort(arr, i, bucket);
        }
    }

    //对待排序数组的第i位进行排序
    private static void sort(int[] arr, int i, ArrayList[] bucket) {
        //入桶
        putIntoBucket(arr, i, bucket);
        //出桶
        getOutOfBucket(arr, bucket);
        //清空桶
        clearAllBucket(bucket);
    }

    private static void getOutOfBucket(int[] arr, ArrayList[] bucket) {
        int k = 0;
        for (int j = 0; j < bucket.length; j++) {
            for (int l = 0; l < bucket[j].size(); l++) {
                int temp = (int) bucket[j].get(l);
                arr[k++] = temp;
            }
        }
    }

    private static void clearAllBucket(ArrayList[] bucket) {
        for (ArrayList m : bucket) {
            m.clear();
        }
    }

    //i:第几位
    private static void putIntoBucket(int[] arr, int i, ArrayList[] bucket) {
        for (int j = 0; j < arr.length; j++) {
            int temp = arr[j];
            switch ((int) (temp / (Math.pow(10, i - 1))) % 10) {
                case 0:
                    bucket[0].add(arr[j]);
                    break;
                case 1:
                    bucket[1].add(arr[j]);
                    break;
                case 2:
                    bucket[2].add(arr[j]);
                    break;
                case 3:
                    bucket[3].add(arr[j]);
                    break;
                case 4:
                    bucket[4].add(arr[j]);
                    break;
                case 5:
                    bucket[5].add(arr[j]);
                    break;
                case 6:
                    bucket[6].add(arr[j]);
                    break;
                case 7:
                    bucket[7].add(arr[j]);
                    break;
                case 8:
                    bucket[8].add(arr[j]);
                    break;
                default:
                    bucket[9].add(arr[j]);
                    break;
            }
        }

    }

    //获取最大位 eg：12345 return 5;
    private static int getMaxLength(int max) {
        int numLength = 0;
        while (max != 0) {
            numLength++;
            max = max / 10;
        }
        return numLength;
    }

    //获取数组最大数
    private static int maxOf(int[] arr) {
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (max < arr[i]) {
                max = arr[i];
            }
        }
        return max;
    }

    public static void printArr(int[] arr) {
        StringBuilder sb = new StringBuilder("[ ");
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i] + " ");
        }
        sb.append("]");
        System.out.println(sb.toString());
    }

}
