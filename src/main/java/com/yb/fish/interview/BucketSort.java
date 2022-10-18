package com.yb.fish.interview;

import java.util.Arrays;

/**
 * 桶计数排序：用桶来统计数据出现的次数，最后再遍历桶，顺序输出数组
 * 遍历原始数组，找到数组中的最大值
 * 创建一个下标为原始数组中最大值的桶数组,该桶数组的下标代表元素，该数组下标所对应的值代表这个值出现的次数
 * 再次遍历原始数组，得到原数组中存在的各个元素，以及出现的次数
 * 遍历桶数组,外层循环从桶的第一位开始（即下表为零）；内层循环遍历桶数组中下标为i的值出现的次数
 * <p>
 * PS：传统的桶排序：与计数不同，桶的粒度大些，按照一定的规则将原数据均匀的分配到桶里，
 * 然后桶里分别用快排处理桶内排序，最后再合并
 *
 * @author bing
 * @version 1.0
 * @create 18/10/2022
 **/
public class BucketSort {

    public static void bucketSort(int[] num) {

        // 遍历原始数组，找到数组中的最大值
        int max = num[0];
        for (int i = 0; i < num.length; i++) {
            if (num[i] > max) {
                max = num[i];
            }
        }

        // 创建一个下标为原始数组中最大值的桶数组,该桶数组的下标代表元素，该数组下标所对应的值代表这个值出现的次数

        int[] bucketArray = new int[max + 1];

        // 再次遍历原始数组，得到原数组中存在的各个元素，以及出现的次数
        for (int i = 0; i < num.length; i++) {
            bucketArray[num[i]]++;
        }

        // 遍历桶数组,外层循环从桶的第一位开始（即下表为零）；内层循环遍历桶数组中下标为i的值出现的次数
        int index = 0;
        for (int i = 0; i < bucketArray.length; i++) {
            for (int j = 0; j < bucketArray[i]; j++) {
                num[index++] = i;
            }
        }
    }

    public static void main(String[] args) {
        int[] num = new int[] {2, 5, 6, 8, 5, 2, 9, 6};
        bucketSort(num);
        System.out.println(Arrays.toString(num));

    }

}
