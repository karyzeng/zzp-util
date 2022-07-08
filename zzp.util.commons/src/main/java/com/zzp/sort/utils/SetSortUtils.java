package com.zzp.sort.utils;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * @Description Set集合排序
 * @Author karyzeng
 * @since 2020.07.31
 **/
public class SetSortUtils {

    public static void main(String[] args) {
        Set<Integer> set = new HashSet<Integer>();
        set.add(1);
        set.add(3);
        System.out.println(sortLambda(set, "desc").toString());
    }

    /**
     * 排序
     * @param set set集合
     * @param sortType 排序类型
     * @return
     */
    public static Set sort(Set set, String sortType) {
        Set<Integer> sortSet = new TreeSet<Integer>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                // compare方法返回值大于0，会交换前后两个数位置
                // compare方法返回值小于等于0，位置不交换
                if (sortType.equals("desc")) {
                    return o2.compareTo(o1);//降序排序
                } else if (sortType.equals("asc")) {
                    return o1.compareTo(o2);//升序排序
                } else {
                    // 默认升序排序
                    return o1.compareTo(o2);//升序排序
                }
            }
        });
        sortSet.addAll(set);
        return sortSet;
    }

    /**
     * lambda排序
     * @param set set集合
     * @param sortType 排序类型
     * @return
     */
    public static Set sortLambda(Set set, String sortType) {
        Set<Integer> sortSet = new TreeSet<Integer>((o1, o2) -> (sortType.equals("desc") ? o2.compareTo(o1) : (sortType.equals("asc") ? o1.compareTo(o2) : o1.compareTo(o2))));
        sortSet.addAll(set);
        return sortSet;
    }

}
