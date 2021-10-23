package com.zzp.lambda.utils;

import com.google.common.base.Splitter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description Java 8 Stream Util
 * @Author karyzeng
 * @since 2020.12.30
 **/
public class StreamUtils {

    public static void main(String[] args) {
        String str = "1,2,3";
        List<Integer> list = convertIntList(str, ",");
        List<Integer> evenList = filterEvenNumber(list);
        System.out.println(evenList);

        String str1 = "5,6,7,10";
        String str2 = "9,10,11";
        List<Integer> mergeList = mergeList(convertIntList(str1, ","), convertIntList(str2, ","), true);
        System.out.println(mergeList);

//        List<User> users = new ArrayList<User>();
//        User user1 = new User();
//        user1.setName("xxx");
//        users.add(user1);
//
//        User user2 = new User();
//        user2.setName("zzp");
//        users.add(user2);

//        List<String> userNames = convert(users);
//        System.out.println(StringUtils.join(userNames, ","));

    }

    /**
     * 将str字符串根据separator分隔符分割成List<Integer>
     * @param str 字符串
     * @param separator 分隔符
     * @return
     */
    public static List<Integer> convertIntList(String str, String separator) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        return Splitter.on(separator).splitToStream(str).map(Integer::valueOf).collect(Collectors.toList());
    }

    /**
     * 将list列表中的偶数过滤出来
     * @param list
     * @return
     */
    public static List<Integer> filterEvenNumber(List<Integer> list) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.stream().filter(x -> x % 2 == 0).collect(Collectors.toList());
    }

    /**
     * 合并两个list
     * @param list1
     * @param list2
     * @param distinct true表示去重，false表示不去重
     * @return
     */
    public static List<Integer> mergeList(List<Integer> list1, List<Integer> list2, boolean distinct) {
        if (CollectionUtils.isEmpty(list1)) {
            return list2;
        }

        if (CollectionUtils.isEmpty(list2)) {
            return list1;
        }

        if (distinct) {
            return Stream.of(list1, list2).flatMap(Collection::stream).distinct().collect(Collectors.toList());
        }
        return Stream.of(list1, list2).flatMap(Collection::stream).collect(Collectors.toList());
    }

//    /**
//     * 对List进行排序
//     * @param users user列表
//     * @param sortType 排序类型 ASC为升序，DESC为降序，如果为null则不排序
//     * @return
//     */
//    public static List<User> sort(List<User> users, String sortType) {
//        if (CollectionUtils.isEmpty(users)) {
//            return users;
//        }
//
//        if ("ASC".equals(sortType)) {
//            users.sort((a, b) -> a.getAge() - b.getAge());
//        } else if ("DESC".equals(sortType)) {
//            users.sort((a, b) -> b.getAge() - a.getAge());
//        }
//
//        return users;
//    }

//    /**
//     * 获取users中所有元素的name并转成List
//     * @param users user列表
//     * @return
//     */
//    public static List<String> convert(List<User> users) {
//        if (CollectionUtils.isEmpty(users)) {
//            return null;
//        }
//
//        // Collectors.toList()转换成List，Collectors.toSet()转换成Set，等等等等
//        return users.stream().map(user -> user.getName()).collect(Collectors.toList());
//    }

//    /**
//     * 差集list1 - list2
//     * @param list1
//     * @param list2
//     * @return
//     */
//    public static List<User> differenceSet(List<User> list1, List<User> list2) {
//        // 差集 (list1 - list2)
//        List<User> reduce1 = list1.stream().filter(item -> !list2.contains(item)).collect(Collectors.toList());
//        System.out.println("---得到差集 reduce1 (list1 - list2)---");
//        reduce1.parallelStream().forEach(System.out::println);
//        return null;
//    }

}
