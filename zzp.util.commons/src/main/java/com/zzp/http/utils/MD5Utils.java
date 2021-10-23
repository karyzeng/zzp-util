package com.zzp.http.utils;

import java.security.MessageDigest;

/**
 * MD5工具类
 * @author karyzeng
 * @since 2018.08.29
 *
 */
public class MD5Utils {

    /** 
     * MD5加码 生成32位md5码 
     * 
     * @param str 要进行MD5的字符串
     * 
     * @return String
     */ 
    public static String string2MD5(String str) {
		String result = "";
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
			//将str的编码设置为utf-8，避免中文出现MD5不一致的情况
			md5.update(str.getBytes("UTF-8"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte b[] = md5.digest();
		int i;
		StringBuffer buf = new StringBuffer("");
		for (int offset = 0; offset < b.length; offset++) {
			i = b[offset];
			if (i < 0)
				i += 256;
			if (i < 16)
				buf.append("0");
			buf.append(Integer.toHexString(i));
		}
		result = buf.toString();
 
		return result;
	}
  
    /** 
     * 加密解密算法 执行一次加密，两次解密 
     * 
     * @param str表示要加密或者解密的字符串
     * 
     * @return String
     */   
    public static String convertMD5(String str){  
  
        char[] a = str.toCharArray();  
        for (int i = 0; i < a.length; i++){  
            a[i] = (char) (a[i] ^ 't');  
        }  
        String s = new String(a);  
        return s;  
  
    }  
  
    public static void main(String args[]) {  
        String s = new String("粤BAD548");  
        System.out.println("原始：" + s);  
        System.out.println("MD5后：" + string2MD5(s));  
        System.out.println("加密的：" + convertMD5(s));  
        System.out.println("解密的：" + convertMD5(convertMD5(s)));  
    }  
	
}