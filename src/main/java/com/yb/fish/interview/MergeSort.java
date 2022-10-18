package com.yb.fish.interview;

import java.util.Arrays;

/**
 * 归并排序(分而治之)O(nlogn)
 * 核心思想：
 * 1.创建一个临时数组
 * 2.递归的将排序数组从中间分层前后两段(相当于分成多个两部分数组)；
 * 3.在递归中进行合并比较排序；
 * 4.合并的核心逻辑：把两个数组起始头进行循环比较，
 * 小的放入临时数组中，大的继续比较，直到其中一个数组遍历完，
 * 剩余没遍历完的数组直接合并到临时数组的后面，进行合并；
 *
 * @author bing
 * @version 1.0
 * @create 14/10/2022
 **/
public class MergeSort {

    //归并排序
    public static int[] sortArray(int[] nums) {
        //临时数组result
        int[] result = new int[nums.length];
        //归并排序
        mergeSort(nums, 0, nums.length - 1, result);
        //此时nums与result相同
        return result;//此时nums与result相同
    }

    // 对 nums 的 [start, end] 区间归并排序
    public static void mergeSort(int[] nums, int start, int end, int[] result) {
        // 只剩下一个数字，停止拆分
        if (start == end) {
            return;
        }
        int middle = (start + end) / 2;
        // 拆分左边区域，并将归并排序的结果保存到 result 的 [start, middle] 区间
        mergeSort(nums, start, middle, result);
        // 拆分右边区域，并将归并排序的结果保存到 result 的 [middle + 1, end] 区间
        mergeSort(nums, middle + 1, end, result);
        // 合并左右区域到 result 的 [start, end] 区间
        merge(nums, start, end, result);
    }

    // 将 nums 的 [start, middle] 和 [middle + 1, end] 区间合并
    public static void merge(int[] nums, int start, int end, int[] result) {
        //分割
        int middle = (start + end) / 2;
        // 数组 1 的首尾位置
        int start1 = start;
        int end1 = middle;
        // 数组 2 的首尾位置
        int start2 = middle + 1;
        int end2 = end;
        // 用来遍历数组的指针
        int index1 = start1;
        int index2 = start2;
        // 结果数组的指针
        int resultIndex = start1;
        //比较插入结果数组
        while (index1 <= end1 && index2 <= end2) {
            if (nums[index1] <= nums[index2]) {
                result[resultIndex++] = nums[index1++];
            } else {
                result[resultIndex++] = nums[index2++];
            }
        }
        // 将剩余数字补到结果数组之后
        //先index1再index2可以保证数组排序的稳定性
        while (index1 <= end1) {
            result[resultIndex++] = nums[index1++];
        }
        while (index2 <= end2) {
            result[resultIndex++] = nums[index2++];
        }
        // 将 result 操作区间的数字拷贝到 arr 数组中，以便下次比较
        for (int i = start; i <= end; i++) {
            nums[i] = result[i];
        }
        System.out.println(Arrays.toString(nums));
    }

    public static void main(String[] args) {
        int arr[] = {9, 2, 88, 3, 5, 16, 7};
        sortArray(arr);
    }
}
