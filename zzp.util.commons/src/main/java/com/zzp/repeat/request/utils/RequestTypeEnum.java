package com.zzp.repeat.request.utils;

/**
 * 请求类型枚举
 * 
 * @author karyzeng
 * @since 2018.09.03
 *
 */
public enum RequestTypeEnum {

	YL(1, "易流"),
	BD(2, "百度");
	
	private int type;
	
	private String name;
	
	private RequestTypeEnum(int type, String name) {
		this.type = type;
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public String getName() {
		return name;
	}
	
	/**
	 * 根据type来获取name
	 * 
	 * @param type
	 * 
	 * @return String
	 */
	public static String getNameByType(int type) {
		for (RequestTypeEnum typeEnum : RequestTypeEnum.values()) {
			if (typeEnum.getType() == type) {
				return typeEnum.getName();
			}
		}
		return null;
	}

}
