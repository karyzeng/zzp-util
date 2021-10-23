package com.zzp.sort.utils;


/**
 * @Description 快速排序
 * @Author karyzeng
 * @since 2019.07.26
 **/
public class QuickSortUtils {

    /**
     * 递归排序，当start等于end的时候结束递归
     *
     * 时间复杂度：最优O(nlogn)，最差O(n^2)，平均O(nlogn)
     * 空间复杂度：O(n)
     *
     * @param array 要排序的数组
     * @param start 数组排序开始下标
     * @param end 数据排序结束下标
     */
    private static int divide(int[] array, int start, int end, String sortType) {
        int base = array[end];// end最后一个元素作为基准
        while (start < end) {
            if (SortTypeEnum.ASC.getCode().equals(sortType)) {
                while (start < end && array[start] <= base)
                    start++;
            } else {
                while (start < end && array[start] >= base)
                    start++;
            }

            if (start < end) {
                int temp = array[start];
                array[start] = array[end];
                array[end] = temp;
                end--;
            }

            if (SortTypeEnum.ASC.getCode().equals(sortType)) {
                while (start < end && array[end] >= base)
                    end--;
            } else{
                while (start < end && array[end] <= base)
                    end--;
            }

            if (start < end) {
                int temp = array[start];
                array[start] = array[end];
                array[end] = temp;
                start++;
            }
        }
        return end;
    }

    private static void sort(int[] array, int start, int end, String sortType) {
        if (array == null || array.length <= 0) {
            throw new ArrayIndexOutOfBoundsException("数组不能为空");
        }

        if (start > end) {
            return;
        }

        int divide = divide(array, start, end, sortType);
        sort(array, start, divide - 1, sortType);
        sort(array, divide + 1, end, sortType);
    }

    public static void main(String[] args) {
        int[] array = ArrayUtils.randomArray(1000, 40);
        ArrayUtils.print(array);
        sort(array, 0, array.length - 1, SortTypeEnum.DESC.getCode());
        ArrayUtils.print(array);
    }

}
