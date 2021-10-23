package com.zzp.http.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 获得sha1加密的sign工具类
 * @author karyzeng
 * @since 2018.10.13
 * 
 */
public class SHA1SignUtils {
	
	/**
	 * 创建签名
	 * @param appid 请求应用ID
	 * @param appSecret 秘钥
	 * @param time   请求的时间
	 * @return
	 */
	public static String createSignature(String appid, String appSecret, String time) {
		MessageDigest md = null;
		String sign = null;
		try {
			StringBuilder buffer = new StringBuilder();
			buffer.append(appid).append(appSecret).append(time);
			md = MessageDigest.getInstance("SHA-1");
			// 将三个参数字符串拼接成一个字符串进行 shal 加密
			byte[] digest = md.digest(buffer.toString().getBytes());
			sign = byteToStr(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		// 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
		return sign;
	}

	/**
	 * 将字节数组转换为十六进制字符串
	 * 
	 * @param digest
	 * @return
	 */
	private static String byteToStr(byte[] digest) {
		String strDigest = "";
		for (int i = 0; i < digest.length; i++) {
			strDigest += byteToHexStr(digest[i]);
		}
		return strDigest;
	}

	/**
	 * 将字节转换为十六进制字符串
	 * 
	 * @param b
	 * @return
	 */
	private static String byteToHexStr(byte b) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] tempArr = new char[2];
		tempArr[0] = Digit[(b >>> 4) & 0X0F];
		tempArr[1] = Digit[b & 0X0F];
		String s = new String(tempArr);
		return s;
	}
	
	public static void main(String[] args) {
		String str = SHA1SignUtils.createSignature("bc23903891", "0e246f103676460287884a09568b3af2", "2018-10-15 10:10:03");
		System.out.println(str);
	}

}
