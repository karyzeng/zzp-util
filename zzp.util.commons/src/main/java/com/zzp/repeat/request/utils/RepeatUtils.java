package com.zzp.repeat.request.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * 重复请求工具类
 * 
 * @author karyzeng
 * @since 2018.09.03
 *
 */
public class RepeatUtils {

	/**
	 * 重复请求方法
	 * 
	 * @param request 请求接口实现类
	 * @param repeat 重复请求次数
	 * @param type 请求类型
	 * 
	 * @return String
	 */
	public String repeatRequest(Request request, int repeat, int type) {
		String result = null;
		for (int i = 0;i < repeat;i++) {
			result = request.requestMethod();
			if (isSuccess(result, type)) {
				break;
			} else {
				System.out.println("请求" + type + "失败" + (i + 1) + "次");
			}
		}
		return result;
	}
	
	/**
	 * 根据请求的返回值和类型来判断请求是否请求成功
	 * 
	 * @param result 请求返回值
	 * @param type 请求类型
	 * 
	 * @return boolean true表示请求成功，false表示请求失败
	 */
	private boolean isSuccess(String result, int type) {
		JSONObject resultJson = JSONObject.parseObject(result);
		if (type == RequestTypeEnum.YL.getType()) {
			String code = resultJson.getJSONObject("result").getString("code");
			return code.equals("1") ? true : false;
		} else if (type == RequestTypeEnum.BD.getType()) {
			String status = resultJson.getString("status");
			return status.equals("0") ? true : false;
		}
		return false;
	}
	
}
