package com.zzp.sort.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * Map排序
 * 
 * @author karyzeng
 * @since 2018.08.29
 *
 */
public class MapSortUtils {

    public static void main(String[] args) {

        Map<String, String> map = new TreeMap<String, String>();

        map.put("KFC", "zz");
        map.put("WNBA", "g");
        map.put("NBA", "a");
        map.put("CBA", "d");

        Map<String, String> resultMap = sortMapByKey(map);    //按Key进行排序
        
        System.out.println("按key进行排序");
        for (Map.Entry<String, String> entry : resultMap.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        
        Map<String, String> valueMap = sortMapByValue(map);    //按value进行排序

        System.out.println("按value进行排序");
        for (Map.Entry<String, String> entry : valueMap.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }
    
    /**
     * 使用 Map按key进行排序
     * 
     * @param map
     * 
     * @return Map<String, String>
     */
    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<String, String> sortMap = new TreeMap<String, String>(new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }
    
    /**
     * 使用 Map按value进行排序
     * 
     * @param map
     * 
     * @return Map<String, String>
     */
    public static Map<String, String> sortMapByValue(Map<String, String> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<String, String> sortedMap = new LinkedHashMap<String, String>();
        List<Map.Entry<String, String>> entryList = new ArrayList<Map.Entry<String, String>>(oriMap.entrySet());
        Collections.sort(entryList, new MapValueComparator());

        Iterator<Map.Entry<String, String>> iter = entryList.iterator();
        Map.Entry<String, String> tmpEntry = null;
        while (iter.hasNext()) {
            tmpEntry = iter.next();
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
        }
        return sortedMap;
    }
}

class MapKeyComparator implements Comparator<String>{

    @Override
    public int compare(String str1, String str2) {
        return str1.compareTo(str2);
    }
}

class MapValueComparator implements Comparator<Map.Entry<String, String>> {

    @Override
    public int compare(Entry<String, String> me1, Entry<String, String> me2) {
        return me1.getValue().compareTo(me2.getValue());
    }
}