package com.zzp.string.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Splitter;
import org.apache.commons.lang3.BooleanUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @Description 字符串工具类
 * @Author Garyzeng
 * @since 2019.12.02
 **/
public class StringUtils {

    /**
     * Java是基于Unicode编码的，因此，一个汉字的长度为1，而不是2
     * “123abc美丽中国”按字节长度计算是14，而按Unicode计算长度是10
     * 可以通过判断每一个字符Ascii码来获得具体的长度。如果是标准的字符，Ascii的范围是0至255，如果是汉字或其他全角字符，Ascii会大于255
     * @param str
     * @return
     */
    public static int strByteLength(String str) {
        int length = 0;
        for(int i = 0; i < str.length(); i++) {
            int ascii = Character.codePointAt(str, i);
            if(ascii >= 0 && ascii <=255)
                length++;
            else
                length += 2;
        }
        return length;
    }

    public static int strByteLengthByStream(String str) {
        try {
            if (str != null && !str.equals("")) {
                byte[] bytes = str.getBytes("GBK");
                return bytes.length;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 生成UUID
     * @return
     */
    public static String UUID() {
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    /**
     * 如果字符串的第一个字符与传入的c相等，则删除第一个字符
     * @param str 原字符串
     * @param c 要删除的字符
     * @return
     */
    public static String deleteFirstChar(String str, String c) {
        validate(str, c);
        StringBuffer strBuffer = new StringBuffer(str);
        String firstChar = strBuffer.substring(0, 1);// 获取第一个字符
        if (firstChar.equals(c)) {
            // 如果第一个字符为"/"的话，则删除该"/"
            strBuffer.deleteCharAt(0);
        }
        return strBuffer.toString();
    }

    /**
     * 如果字符串的最后一个字符与传入的c相等，则删除最后一个字符
     * @param str 原字符串
     * @param c 要删除的字符
     * @return
     */
    public static String deleteLastChar(String str, String c) {
        validate(str, c);
        StringBuffer strBuffer = new StringBuffer(str);
        String lastChar = strBuffer.substring(strBuffer.length() - 1, strBuffer.length());// 获取最后一个字符
        if (lastChar.equals(c)) {
            // 如果最后一个字符为"/"的话，则删除该"/"
            strBuffer.deleteCharAt(strBuffer.length() - 1);
        }
        return strBuffer.toString();
    }

    /**
     * 如果字符串的第一个字符与与传入的c相等，则删除第一个字符
     * @param str 原字符串
     * @param c 要删除的字符
     * @return
     */
    public static String deleteFirstAndLastChar(String str, String c) {
        return deleteLastChar(deleteFirstChar(str, c), c);
    }

    /**
     * 对字符串进行分割之后去重再拼接
     * @param str 字符串
     * @param separator 拼接分隔符
     * @param split 拆分分隔符
     * @return
     */
    private static String distintAndJoinStr(String str, String separator, String split) {
        String[] strArr = str.split(split);
        Set<String> set = new LinkedHashSet<String>();
        for (int i = 0; i < strArr.length; i++) {
            if (org.apache.commons.lang3.StringUtils.isNotBlank(strArr[i])) {
                set.add(strArr[i]);
            }
        }
        return org.apache.commons.lang3.StringUtils.join(set, separator);
    }

    /**
     * 校验str和c
     * @param str
     * @param c
     */
    public static void validate(String str, String c) {
        if (str == null) {
            throw new NullPointerException("str must not null");
        }
        if (c == null) {
            throw new NullPointerException("c must not null");
        }
    }

    /**
     * ascii码转换成对应的字符串
     *
     * @param asciiValue ascii值
     * @return {@link String}
     */
    public static String asciiToString(int asciiValue) {
        char asciiChar = (char) asciiValue;
        return String.valueOf(asciiChar);
    }

    /**
     * 添加字符串到字符串中，且去重
     *
     * @param str       原字符串
     * @param appendStr 附加str
     * @param separator 分隔符
     * @return {@link String}
     */
    public static String append(String str, String appendStr, String separator) {
        return append(str, appendStr, separator, true);
    }

    /**
     * 添加字符串到字符串中
     *
     * @param str       原字符串
     * @param appendStr 附加str
     * @param separator 分隔符
     * @param distinct  是否去重，true表示去重，false表示不去重
     * @return {@link String}
     */
    public static String append(String str, String appendStr, String separator, boolean distinct) {
        return append(str, appendStr, separator, distinct, false);
    }

    /**
     * 添加字符串到字符串中
     *
     * @param str       原字符串
     * @param appendStr 附加str
     * @param separator 分隔符
     * @param distinct  是否去重，true表示去重，false表示不去重
     * @param appendHead 是否添加在头部，true表示是，false表示添加在尾部
     * @return {@link String}
     */
    public static String append(String str, String appendStr, String separator, boolean distinct, boolean appendHead) {
        if (org.apache.commons.lang3.StringUtils.isBlank(str)) {
            return appendStr;
        }

        if (org.apache.commons.lang3.StringUtils.isBlank(appendStr)) {
            return str;
        }

        List<String> list = Splitter.on(separator).splitToList(str);

        List<String> newList = new ArrayList<String>(list);
        if (appendHead) {
            // 添加在头部
            newList.add(0, appendStr);
        } else {
            // 添加在尾部
            newList.add(appendStr);
        }

        if (distinct) {
            // 去重
            Set<String> set = new LinkedHashSet<String>(newList);
            return org.apache.commons.lang3.StringUtils.join(set, separator);
        }

        return org.apache.commons.lang3.StringUtils.join(newList, separator);

    }

    public static void appendBitStr(StringBuffer bf, Boolean sign) {
        if (bf == null) {
            return;
        }

        if (BooleanUtils.isTrue(sign)) {
            bf.append("1");
        } else {
            bf.append("0");
        }
    }

    private static Boolean analysisBitStr(String bitStr, int start, int end) {
        if (org.apache.commons.lang3.StringUtils.isBlank(bitStr)) {
            return null;
        }

        String bit = bitStr.substring(start, end);
        if ("1".equals(bit)) {
            return true;
        } else if ("0".equals(bit)){
            return false;
        }

        return null;
    }


    public static void main(String[] args) {

        JSONObject jsonObject = JSON.parseObject("{\"list\":[{\"name\":\"zzp\"}],\"size\":1}");
        Map<String, Object> map = jsonObject.getInnerMap();
        for (String key : map.keySet()) {
            Object obj = map.get(key);
            if (obj instanceof JSONArray) {
                System.out.println("key:" + key + " is jsonArray，value:" + obj);
            } else {
                System.out.println(obj);
            }
        }

//        String content = "1|1|缶4|34好的没问题安安定定aadded中e是打卡机冷风机aa";
//        System.out.println(content.length());
//        System.out.println(strByteLengthByStream(content));
//        System.out.println(UUID());
//
//        String invoiceNo = "/CK发票号001/";
//        System.out.println(invoiceNo);
//        System.out.println(deleteFirstAndLastChar(invoiceNo, "/"));
//
//        System.out.println(asciiToString(65 + 2 + 0));
//
//        System.out.println(append("金甲卡卡龙；哈勃望眼镜；笔记本", "哈勃望眼镜", "；", true, true));

        StringBuffer sb = new StringBuffer();
        appendBitStr(sb, true);
        appendBitStr(sb, false);
        appendBitStr(sb, null);
        appendBitStr(sb, true);
        appendBitStr(sb, null);

        String str = sb.toString();
        System.out.println(str);

        System.out.println(analysisBitStr(str, 0, 1));
        System.out.println(analysisBitStr(str, 1, 2));
        System.out.println(analysisBitStr(str, 2, 3));
        System.out.println(analysisBitStr(str, 3, 4));
        System.out.println(analysisBitStr(str, 4, 5));

    }

}
