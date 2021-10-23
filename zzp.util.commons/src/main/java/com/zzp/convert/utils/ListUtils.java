package com.zzp.convert.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.beanutils.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * List转换工具
 */
public class ListUtils {

    /**
     * 使用JSON方法转换
     * @param list
     * @param <T>
     * @return
     */
    public static <T> List<Map> convertListMap(List<T> list) {
        String jsonStr = JSON.toJSONString(list);
        List<Map> listMap = JSON.parseArray(jsonStr, Map.class);
        return listMap;
    }

    /**
     * 使用Apache的BeanUtils转换
     * @param list
     * @param <T>
     * @return
     */
    public static <T> List<Map> beanToMaps(List<T> list) {
        if (list != null && list.size() > 0) {
            List<Map> maps = new ArrayList<Map>();
            for (int i = 0;i < list.size(); i++) {
                Map map = beanToMap(list.get(i));
                maps.add(map);
            }
            return maps;
        }
        return null;
    }

    public static <T> Map beanToMap(T t) {
        if (t != null) {
            try {
                Map map = BeanUtils.describe(t);
                return map;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

}
