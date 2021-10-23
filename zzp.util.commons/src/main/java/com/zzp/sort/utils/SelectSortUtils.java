package com.zzp.sort.utils;

/**
 * 选择排序
 * @author karyzeng
 * @since 2019.08.01
 */
public class SelectSortUtils {

    /**
     * 直接选择排序
     *
     * 时间复杂度：最坏O(n^2)，最优O(n^2)，平均O(n^2)
     * 空间复杂度：O(n)
     *
     * @param array
     * @param sortType
     */
    private static void compareAndSwap(int[] array, String sortType) {
        for (int i = 0; i < array.length - 1; i++) {
            int index = i;
            for (int j = i + 1; j < array.length; j++) {
                if (SortTypeEnum.ASC.getCode().equals(sortType)) {
                    if (array[j] < array[index]) {
                        index = j;
                    }
                } else {
                    if (array[j] > array[index]) {
                        index = j;
                    }
                }

            }
            if (i != index) {
                int temp = array[i];
                array[i] = array[index];
                array[index] = temp;
            }
        }
    }

    private static int[] sort(int[] array, String sortType){
        if (array == null || array.length <= 0) {
            throw new ArrayIndexOutOfBoundsException("数组不能为空");
        }
        if (array.length == 1) {
            return array;
        }
        compareAndSwap(array, sortType);
        return array;
    }

    public static void main(String[] args) {
        int[] array = ArrayUtils.randomArray(1000, 40);
        ArrayUtils.print(array);
        sort(array, SortTypeEnum.DESC.getCode());
        ArrayUtils.print(array);
    }

}
