package com.zzp.string.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @Description fastJson扩展工具类
 * @Author karyzeng
 * @since 2020.10.14
 **/
public class FastJsonUtils {

    /**
     * 过滤json字符串中为""的字段
     * @param jsonStr json字符串
     * @return
     */
    public static String filterBlankValue(String jsonStr) {
        if (StringUtils.isBlank(jsonStr)) {
            return null;
        }
        if (judgeArray(jsonStr)) {
            return arrayHandler(jsonStr);
        } else {
            return objectHandler(jsonStr);
        }
    }

    /**
     * 对象类型json字符串处理方法
     * @param objectStr json对象字符串
     * @return
     */
    private static String objectHandler(String objectStr) {
        if (StringUtils.isBlank(objectStr)) {
            return null;
        }
        JSONObject object = JSONObject.parseObject(objectStr);
        return objectHandler(object);
    }

    /**
     * 对象类型json处理方法
     * @param object json对象
     * @return
     */
    private static String objectHandler(JSONObject object) {
        JSONObject result = new JSONObject();
        for (Map.Entry entry : object.entrySet()) {
            String valueStr = entry.getValue().toString();
            if (StringUtils.isNotBlank(valueStr)) {
                if (judgeArray(valueStr)) {
                    // 数组类型
                    String arrayResult = arrayHandler(valueStr);
                    if (StringUtils.isBlank(arrayResult)) {
                        continue;
                    }
                    // arrayResult为json数组类型字符串，需要将其转换成json数组
                    JSONArray subArr = JSONObject.parseArray(arrayResult);
                    result.put(entry.getKey().toString(), subArr);
                } else {
                    // 普通类型
                    result.put(entry.getKey().toString(), entry.getValue());
                }
            }
        }
        return result.toJSONString();
    }

    /**
     * 数组类型json字符串处理方法
     * @param arrayStr json数组字符串
     * @return
     */
    private static String arrayHandler(String arrayStr) {
        if (StringUtils.isBlank(arrayStr)) {
            return null;
        }
        JSONArray array = JSONObject.parseArray(arrayStr);
        return arrayHandler(array);
    }

    /**
     * 数组类型json处理方法
     * @param array json数组
     * @return
     */
    private static String arrayHandler(JSONArray array) {
        if (array.size() <= 0) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        for (int i = 0; i < array.size(); i++) {
            JSONObject object = array.getJSONObject(i);
            String objectResult = objectHandler(object);
            if (StringUtils.isBlank(objectResult)) {
                continue;
            }
            sb.append(objectResult + ",");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append("]");
        return sb.toString();
    }

    /**
     * 判断value是否为数组类型
     * @param value
     * @return
     */
    private static boolean judgeArray(String value) {
        if (StringUtils.isBlank(value)) {
            throw new RuntimeException("value is must be not null");
        }

        if (value.startsWith("[") && value.endsWith("]")) {
            return true;
        }

        return false;
    }

    /**
     * 对象String类型如果为null则会转换成""
     * @param t 对象实例
     * @param clazz 对象实例的类对象
     * @return
     */
    public static <T> T writeNullStringAsEmpty(T t, Class<T> clazz) {
        if (t == null) {
            return null;
        }

        String jsonStr = JSON.toJSONString(t,
                SerializerFeature.WriteNullStringAsEmpty, // 字符类型字段如果为null,输出为"",而非null
                SerializerFeature.WriteMapNullValue // 是否输出值为null的字段
        );
        return JSON.parseObject(jsonStr, clazz);
    }

}
