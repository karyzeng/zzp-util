package com.zzp.compare.field.annontation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @ClassName CompareField 
 * @Description 字段比较注解
 * @author GaryZeng
 *
 */
@Retention(RetentionPolicy.RUNTIME) // 在运行时可以获取
@Target(value = {ElementType.METHOD, ElementType.TYPE,ElementType.FIELD}) // 作用到类，方法，接口和字段上等
public @interface CompareField {

	/**
	 * 字段名称
	 * 
	 * @return
	 */
	String fieldName() default "";
	
}
