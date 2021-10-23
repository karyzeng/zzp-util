package com.zzp.convert.utils;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description 数据工具类
 * @Author karyzeng
 * @since 2020.08.03
 **/
public class ArrayUtils {

    public static void main(String[] args) {
        List<String> list1 = new ArrayList<String>();
        list1.add("1");
        list1.add("2");
        list1.add("3");
        List<String> list2 = new ArrayList<String>();
        list2.add("3");
        list2.add("2");
        System.out.println(distinct(list1, list2).toString());
        System.out.println(notExistList(list1, list2).toString());

        List<String> allList = new ArrayList<String>();
        allList.add("1");
        allList.add("2");
        allList.add("3");
        allList.add("4");
        allList.add("5");
        allList.add("6");
        allList.add("7");
        allList.add("8");
        allList.add("9");
        allList.add("10");
        allList.add("11");
        allList.add("12");
        allList.add("13");
        allList.add("14");
        allList.add("15");
        allList.add("16");
        allList.add("17");
        allList.add("18");
        allList.add("19");
        List<List<String>> subLists = splitList(allList, 10);
        System.out.println(subLists);
    }

    /**
     * 两个集合合并并去重
     * @param list1
     * @param list2
     * @return
     */
    public static List<String> distinct(List<String> list1, List<String> list2) {
        if (CollectionUtils.isEmpty(list1)) {
            return list2;
        }

        if (CollectionUtils.isEmpty(list2)) {
            return list1;
        }

        Set<String> set = new HashSet<String>(list1);
        Set<String> set2 = new HashSet<String>(list2);
        set.addAll(set2);

        List<String> result = new ArrayList<String>(set);

        return result;
    }

    /**
     * 在list1中删除list2存在的内容
     * @param list1
     * @param list2
     * @return
     */
    public static List<String> notExistList(List<String> list1, List<String> list2) {
        if (CollectionUtils.isEmpty(list1)) {
            return null;
        }

        if (CollectionUtils.isEmpty(list2)) {
            return list1;
        }

        list1.removeAll(list2);

        return list1;
    }

    /**
     * 拆分list为多个list列表
     * @param list list
     * @param limit list的最大长度
     * @return
     */
    public static List<List<String>> splitList(List<String> list, int limit) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }

        if (limit <= 0) {
            throw new RuntimeException("limit必须大于0");
        }

        int splitSize = 0;

        if (list.size() % limit == 0) {
            splitSize = list.size() / limit;
        } else {
            splitSize = (list.size() / limit) + 1;
        }

        List<List<String>> splitList = new ArrayList<List<String>>();
        int start = 0;
        int end = 0;
        for (int i = 0; i < splitSize; i++) {
            start = end;
            if ((i + 1) == splitSize) {
                end = list.size();
            } else {
                end += limit;
            }
            List<String> subList = list.subList(start, end);
            splitList.add(subList);
        }

        return splitList;
    }

}
