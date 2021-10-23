package com.zzp.convert.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * 转换工具类
 * 
 * @author karyzeng
 * @since 2018.09.07
 *
 */
public class ConvertUtils {
	
	private static SimpleDateFormat smft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 将各种类型的数据转换成用原生SQL的insert语句可以用的value
	 * 
	 * @param t 
	 * 
	 * @return 如果t为null，则返回"NULL"对应SQL的NULL，如果不为空，则将其转换为"'value'"的格式
	 */
	public static <T> String convertInsertParam(T t) {
		if (t != null) {
			Class clazz = t.getClass();
			if (clazz.equals(String.class) || clazz.equals(Integer.class)) {
				String value = t.toString();
				return "'" + value + "'";
			} else if (clazz.equals(Date.class)) {
				Date value = (Date) t;
				return "'" + smft.format(value) + "'";
			}
		}
		return "NULL";
	}
	
	/**
	 * 1、去除字符串的空格，制表符，换行等符号
	 * 2、将中文逗号"，"转换成","
	 * 
	 * @param str
	 * @return String
	 */
	public static String trimSpace(String str) {
    	if (str == null) return null;
		String tempVal = str.trim();
		tempVal = tempVal.replaceAll("，", ",");//处理中文逗号
		tempVal = tempVal.replaceAll("\\r|\\n|\\s|\\t", "");//去掉换行和字符串中间空格
		return tempVal;
	}

	/**
	 * 将导入数据的错误信息格式化为list
	 *
	 * @param result
	 *
	 * @return List<Map<String, Object>>
	 */
	public static List<Map<String, Object>> convertImportResult(String result) {
		String[] rows = result.split("<br><br>");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (rows != null && rows.length > 0) {
			for (int i = 0;i < rows.length;i++) {
				if (StringUtils.isNotBlank(rows[i])) {
					String[] cols = rows[i].split("<br>");
					if (cols != null && cols.length > 0) {
						String key = cols[0].substring(0, cols[0].indexOf(","));
						StringBuffer value = new StringBuffer();
						for (int j = 0;j < cols.length;j++) {
							if (StringUtils.isNotBlank(cols[j])) {
								value.append(cols[j].substring(cols[j].indexOf(":") + 1, cols[j].length()) + ";");
							}
						}
						Map<String, Object> map = new HashMap<String, Object>();
						map.put(key, value);
						list.add(map);
					}
				}
			}
		}
		return list;
	}

	/**
	 * 将字符串中的html标签替换成空字符串
	 *
	 * @param result
	 *
	 * @return String
	 */
	public static String replaceHtmlLabel(String result) {
		if (result.contains("<br>")) {
			return result.replaceAll("<br>", "");
		}
		return result;
	}

	public static void main(String[] args) {
//		int a = 12;
//		String b = "zzz";
//		Date date = new Date();
//		Integer d = new Integer(56);
//		System.out.println(convertInsertParam(a));
//		System.out.println(convertInsertParam(b));
//		System.out.println(convertInsertParam(date));
//		System.out.println(convertInsertParam(d));
//		System.out.println(convertInsertParam(null));
//
//		//=========================
//		System.out.println(trimSpace("  dd  zzz  dddgg   				"));
//		String name = null;
////		System.out.println(name.hashCode());
//		System.out.println(Objects.hashCode(name));
//        int[] array = new int[10];
//        System.out.println(array.length);

//		String result = "第2行,第2列:内地车牌不允许为空<br>第2行,第3列:是否中港车不允许为空<br>第2行,第5列:车型不允许为空<br>第2行,第6列:载量不允许为空<br>第2行,第7列:是否海关备案车辆不允许为空<br>第2行,第8列:是否危险品运输备案车辆不允许为空<br>第2行,第9列:是否装GPS不允许为空<br><br>第3行,第2列:内地车牌不允许为空<br>第3行,第3列:是否中港车不允许为空<br>第3行,第5列:车型不允许为空<br>第3行,第6列:载量不允许为空<br>第3行,第7列:是否海关备案车辆不允许为空<br>第3行,第8列:是否危险品运输备案车辆不允许为空<br>第3行,第9列:是否装GPS不允许为空<br><br>";
//		System.out.println(convertResult(result));

//		String result = "请选择正确的模板导入!";
//		System.out.println(replaceHtmlLabel(result));
		Queue<Integer> queue = new PriorityQueue<Integer>();
		queue.add(1);
		queue.add(6);
		queue.add(3);
		queue.add(9);
		queue.add(11);
		System.out.println("添加完毕");
		queue.remove(9);

	}
	
}
