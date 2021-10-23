package com.zzp.sort.utils;

/**
 * @Description 直接插入排序
 * @Author karyzeng
 * @since 2019.07.26
 **/
public class DirectInsertSortUtils {

    /**
     * 直接插入排序
     *
     * 从未排序好的序列中找出一个数，插入到已排序的序列中的合适位置，默认第一个数为已排序好
     *
     * 时间复杂度：最坏O(n^2)，最好O(n)，平均O(n^2)
     * 空间复杂度：O(n)
     *
     * @param array 要排序的数组
     * @param sortType 排序类型
     * @return
     */
    public static int[] directInsertSort(int[] array, String sortType) {
        if (array != null && array.length > 0) {
            int l = 1;// 默认第一个数为已排序好的列表，那么已排序好的列表长度默认为1
            int i = 1;// 未排序好的列表起始位置为1
            for (; i < array.length; i++) {
                for (int j = 0; j < l; j++) {
                    if (SortTypeEnum.ASC.getCode().equals(sortType)) {
                        if (array[i] < array[j]) {
                            // 升序排序
                            int temp = array[j];
                            array[j] = array[i];
                            array[i] = temp;
                        }
                    } else {
                        if (array[i] > array[j]) {
                            // 升序排序
                            int temp = array[j];
                            array[j] = array[i];
                            array[i] = temp;
                        }
                    }
                }
                l++;
            }
            return array;
        }
        return null;
    }

    public static void main(String[] args) {
        int[] array = ArrayUtils.randomArray(1000, 40);
        directInsertSort(array, SortTypeEnum.DESC.getCode());
        ArrayUtils.print(array);
    }

}
