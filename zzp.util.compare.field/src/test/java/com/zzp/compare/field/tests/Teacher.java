package com.zzp.compare.field.tests;

import com.zzp.compare.field.annontation.CompareField;

import java.math.BigDecimal;

/**
 * 教师类
 * @author Gary Zeng
 * @since 2018.07.11
 */
public class Teacher {

	/**
	 * 姓名
	 */
	@CompareField(fieldName = "姓名")
	private String name;
	
	/**
	 * 年龄
	 */
	@CompareField(fieldName = "年龄")
	private Integer age;
	
	/**
	 * 工资
	 */
	@CompareField(fieldName = "工资")
	private BigDecimal amount;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	
	
}
