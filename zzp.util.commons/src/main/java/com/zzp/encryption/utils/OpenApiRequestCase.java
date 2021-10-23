package com.zzp.encryption.utils;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description OPEN-API调用示例
 * @Author zzp
 * @since 2021.03.30
 **/
public class OpenApiRequestCase {

    public static void main(String[] args) {
        try {
            String appKey = "C1615443407371"; // open提供
            String secret = "e5dfe17ab9e842189d2ac0a724d602cb"; // open提供

            // 业务参数
            Map<String, String> jsonMap = new HashMap<String, String>();
            jsonMap.put("goodsName", "iphoneX");

            // 将业务参数转换成json
            String json = JSON.toJSONString(jsonMap);
            json = URLEncoder.encode(json, "utf-8");

            // 请求参数
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("name", "scm.bomLmgs.standard.import");
            param.put("app_key", appKey);
            param.put("format", "json");
            param.put("data", "%7b%22userName%22%3a+%22ZSOK2021%22%2c%22isUpdate%22%3a+%22false%22%2c%22list%22%3a+%5b%7b%22exgNo%22%3a+%22000-DB607-00010%22%2c%22materialNo%22%3a+%22002%22%2c%22consumeStr%22%3a+%221%22%2c%22tgblLossRateStr%22%3a+%221%22%2c%22intgbLossRateStr%22%3a+%220%22%2c%22erpVersion%22%3a+%22003%22%2c%22cusVersion%22%3a+%221%22%7d%5d%7d");
            param.put("timestamp", "2021-03-30 11:40:02");
            param.put("version", "");

            // 生成sign
            String sign = buildSign(param, secret);

            // 将sign加进请求参数param中
            param.put("sign", sign);

            System.out.println("=====请求参数=====");
            String postJson = JSON.toJSONString(param);
            System.out.println(postJson);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 构建签名
     *
     * @param paramsMap
     *            参数
     * @param secret
     *            密钥
     * @return
     * @throws IOException
     */
    public static String buildSign(Map<String, ?> paramsMap, String secret) throws IOException {
        Set<String> keySet = paramsMap.keySet();
        List<String> paramNames = new ArrayList<String>(keySet);

        Collections.sort(paramNames);

        StringBuilder paramNameValue = new StringBuilder();

        for (String paramName : paramNames) {
            paramNameValue.append(paramName).append(paramsMap.get(paramName));
        }

        String source = secret + paramNameValue.toString() + secret;

        return md5(source);
    }

    /**
     * 生成md5,全部大写
     *
     * @param message
     * @return
     */
    public static String md5(String message) {
        try {
            // 1 创建一个提供信息摘要算法的对象，初始化为md5算法对象
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 2 将消息变成byte数组
            byte[] input = message.getBytes();

            // 3 计算后获得字节数组,这就是那128位了
            byte[] buff = md.digest(input);

            // 4 把数组每一字节（一个字节占八位）换成16进制连成md5字符串
            return byte2hex(buff);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 二进制转十六进制字符串
     *
     * @param bytes
     * @return
     */
    private static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }

    public static String getTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

}
