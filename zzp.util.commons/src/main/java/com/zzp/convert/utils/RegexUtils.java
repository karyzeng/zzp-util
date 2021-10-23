package com.zzp.convert.utils;

import com.github.houbb.paradise.common.util.StringUtil;

/**
 * @Description 正则匹配工具类
 * @Author karyzeng
 * @since 2019.04.18
 **/
public class RegexUtils {

    public static boolean checkLoginName (String matchStr) {
        String regex = "^[_a-zA-Z0-9]+$";
        if(StringUtil.isNotBlank(matchStr) && matchStr.matches(regex)){
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        String str = "JJKKL_08_ad___";
        System.out.println(checkLoginName(str));
    }

}
