package com.zzp.object.utils;


import java.lang.reflect.Field;

/**
 * @Description 对象字段工具类
 * @Author karyzeng
 * @since 2020.07.24
 **/
public class ObjectFieldUtils {

    public static void main(String[] args) {
//        User user = new User();
//        user.setAge(11);
        System.out.println(fieldNotEmpty(null));
    }

    /**
     * 判断对象的所有字段是否不为空
     * @param obj
     * @return boolean 存在不为空的字段返回true，否则返回false
     */
    public static boolean fieldNotEmpty(Object obj) {
        try {
            Class clazz = obj.getClass();
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.get(obj) != null) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断对象的所有字段是否为空
     * @param obj
     * @return boolean 存在不为空的字段返回false，否则返回true
     */
    public static boolean fieldEmpty(Object obj) {
        try {
            Class clazz = obj.getClass();
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.get(obj) != null) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

}
