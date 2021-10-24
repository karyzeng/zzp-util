package com.zzp.compare.field.utils;

import com.alibaba.fastjson.JSONObject;
import com.zzp.compare.field.annontation.CompareField;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

/**
 * 比较属性工具类
 * @author zzp
 * @since 2018.05.02
 */
public class CompareUtils {

	/** 
	 * 获取两个对象同名属性内容不相同的列表 
	 * @param class1 对象1 
	 * @param class2 对象2 
	 * @return 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 */  
	public static List<Map<String ,Object>> compareTwoClass(Object oldObj,Object newObj) throws ClassNotFoundException, IllegalAccessException {  
		return compareTwoClassAddPrefix(oldObj, newObj, null);
	}  
	
	/** 
	 * 获取两个对象同名属性内容不相同的列表，并且在nameDescription添加前缀
	 * @param class1 对象1 
	 * @param class2 对象2 
	 * @param prefix 添加在nameDescription的前缀
	 * @return 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 */ 
	public static List<Map<String ,Object>> compareTwoClassAddPrefix(Object class1,Object class2,String prefix) throws ClassNotFoundException, IllegalAccessException {  
	    if(StringUtils.isNotBlank(prefix)){
	    	prefix = "[" + prefix + "]";
	    }else{
	    	prefix = "";
	    }
		List<Map<String,Object>> list=new ArrayList<Map<String, Object>>();  
	    //获取对象的class  
	    Class<?> clazz1 = class1.getClass();  
	    Class<?> clazz2 = class2.getClass();
	    if(!clazz1.equals(clazz2)){
	    	throw new RuntimeException("对比的两个对象不属于同一个类，无法比较！");
	    }
	    //获取对象的属性列表  
	    Field[] field1 = clazz1.getDeclaredFields();  
	    Field[] field2 = clazz2.getDeclaredFields();  
	    //遍历属性列表field1  
	    for(int i = 0;i < field1.length;i++){  
	    	//遍历属性列表field2  
	        for(int j = 0;j < field2.length;j++){  
	        	 //如果field1[i]属性名与field2[j]属性名内容相同  
	             if(field1[i].getName().equals(field2[j].getName())){
	            	 //判断field[i]和field2[j]是否都包含有对比字段注解
	            	 CompareField compareField1 = field1[i].getAnnotation(CompareField.class);
	            	 CompareField compareField2 = field1[i].getAnnotation(CompareField.class);
	            	 if(compareField1 != null && compareField2 != null){
	            		 //判断是否是相同字段的注解
	            		 if(compareField1.fieldName().equals(compareField2.fieldName())){
	            			 field1[i].setAccessible(true);  
			                 field2[j].setAccessible(true);  
			                 //如果field1[i]属性值与field2[j]属性值内容不相同  
			                 if (!compareTwo(field1[i].get(class1), field2[j].get(class2))){  
			                	 Map<String,Object> map2=new HashMap<String, Object>();  
			                	 map2.put("name",field1[i].getName());
			                	 map2.put("nameDescription",prefix+ compareField1.fieldName()); 
			                	 Object oldObj = field1[i].get(class1);
			                	 map2.put("oldVal",oldObj instanceof Date ? date2TimeStamp((Date)oldObj) : oldObj);  
			                	 Object newObj = field2[j].get(class2);
			                	 map2.put("newVal",newObj instanceof Date ? date2TimeStamp((Date)newObj) : newObj);  
			                	 list.add(map2);  
			                 }  
	            		 }
	            	 }
	            	 break;
		         }  
	         }  
	    }  
	  
	    return list;  
	}  
	
	/**  
	 * 对比两个数据是否内容相同  
	 *  
	 * @param  object1,object2  
	 * @return boolean类型  
	 */  
	private static boolean compareTwo(Object object1,Object object2){  
	  
	    if(object1 == null && object2 == null){  
	        return true;  
	    }  
	    if(object1 == null && object2 != null){  
	        return false;  
	    }
	    if(object1 instanceof BigDecimal && object2 instanceof BigDecimal){
	    	BigDecimal bigDecimal1 = (BigDecimal) object1;
	    	BigDecimal bigDecimal2 = (BigDecimal) object2;
	    	if(bigDecimal1.doubleValue() - bigDecimal2.doubleValue() == 0){
	    		return true;
	    	}
	    }else{
	    	if(object1.equals(object2)){  
		        return true;  
		    }
	    }
	    return false;  
	 }  
	
	/**
	 * 将Date转换成时间戳
	 */
	private static long date2TimeStamp(Date date){
		return date.getTime();
	}

	/**
	 * 比较类和json字符串中相同的属性
	 * @param clazz
	 * @param jsonStr
	 * @return
	 */
	public static List<String> compareIdenticalField(Class clazz, String jsonStr) {
		// 将json字符串转换为json对象
		JSONObject jsonObject = JSONObject.parseObject(jsonStr);

		//
		Field[] fields = clazz.getDeclaredFields();
		if (fields != null && fields.length > 0) {
			List<String> result = new ArrayList<String>();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				String fieldName = field.getName();
				Object value = jsonObject.get(fieldName);
				if (value != null && String.valueOf(value).equals("true")) {
					// 表示该字段需要显示
					CompareField compareField = field.getAnnotation(CompareField.class);
					String fieldDescribe = compareField.fieldName();
					result.add("字段名称：" + fieldName + "，字段中文描述：" + fieldDescribe);
				}
			}
			return result;
		}
		return null;
	}

	/**
	 * 将实体列表转换为Map列表
	 * @param list 实体列表
	 * @param <T>
	 * @return
	 */
	public static <T> List<Map> beansToMaps(List<T> list) {
		if (list != null && list.size() > 0) {
			List<Map> listMap = new ArrayList<Map>();
			for (int i = 0; i < list.size(); i++) {
				T t = list.get(i);
				Map map = beanToMap(t);
				if (map != null && map.size() > 0) {
					listMap.add(map);
				}
			}
			return listMap;
		}
		return null;
	}

	/**
	 * 将实体转换为map，不会将为null的属性也进行转换
	 * @param t 实体
	 * @param <T>
	 * @return
	 */
	public static <T> Map beanToMap(T t) {
		Field[] fields = t.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0) {
			Map map = new HashMap<String, Object>();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				field.setAccessible(true);
				String fieldName = field.getName();
				try {
					Object value = field.get(t);
					if (value == null)
						continue;

					if (value instanceof Date) {
						String dateStr = DateFormatUtils.format((Date) value, "yyyy-MM-dd HH:mm:ss");
						map.put(fieldName, dateStr);
						continue;
					}

					map.put(fieldName, value);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return map;
		}
		return null;
	}

	/**
	 * 将实体转换为map，使用apache的BeanUtils，会将为null的属性也进行转换
	 * @param t 实体
	 * @param <T>
	 * @return
	 */
	public static <T> Map beanToMap2(T t) {
		try {
			Map map = BeanUtils.describe(t);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
