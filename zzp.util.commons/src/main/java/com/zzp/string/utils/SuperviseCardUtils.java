package com.zzp.string.utils;

/**
 * @Description 原产地证工具类
 * @Author karyzeng
 * @since 2021.07.21
 **/
public class SuperviseCardUtils {

    public static String getPreTradeAgreeCode(String superviseCardValue) {
        // 获得"<"的下标
        int start = superviseCardValue.indexOf("<");
        // 获得">"的下标
        int end = superviseCardValue.indexOf(">");
        return superviseCardValue.substring(start + 1, end);
    }

    public static String getOriCertType(String superviseCardValue) {
        // 获得">"的下标
        int index = superviseCardValue.indexOf(">");
        String oriCertType = superviseCardValue.substring(index + 1, index + 2);
        if (!oriCertType.equals("C") && !oriCertType.equals("D")) {
            return null;
        }
        return oriCertType;
    }

    public static String getCertOriCode(String superviseCardValue) {
        // 获得">"的下标
        int index = superviseCardValue.indexOf(">");
        if (getOriCertType(superviseCardValue) == null) {
            // 表示不存在原产地证明类型
            index += 1;
        } else {
            // 表示存在原产地证明类型
            index += 2;
        }
        return superviseCardValue.substring(index, superviseCardValue.length());
    }

    public static void main(String[] args) {
        String superviseCardValue = "<01>TE123";
        String preTradeAgreeCode = getPreTradeAgreeCode(superviseCardValue);
        String oriCertType = getOriCertType(superviseCardValue);
        String certOriCode = getCertOriCode(superviseCardValue);
        System.out.println("完成");
    }

}
