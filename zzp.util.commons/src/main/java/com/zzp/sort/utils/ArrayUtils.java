package com.zzp.sort.utils;

/**
 * @Description 数组工具类
 * @Author karyzeng
 * @since 2019.07.26
 **/
public class ArrayUtils {

    /**
     * 打印数组
     * @param array
     */
    public static void print(int[] array){
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + " ");
        }
        System.out.println("");
    }

    /**
     * 生成长度为length的整形数组，其元素最大不超过max
     * @param max
     * @param length
     * @return
     */
    public static int[] randomArray(int max, int length) {
        int[] array = new int[length];
        for (int i = 0; i < length; i++) {
            array[i] = randomInt(max);
        }
        return array;
    }

    /**
     * 产生[0, max)之间的随机整数
     * @param max
     * @return
     */
    public static int randomInt(int max) {
        int random = (int) (Math.random() * max);
        return random;
    }

    public static void main(String[] args) {
        int[] array = randomArray(100, 40);
        print(array);
    }

}
