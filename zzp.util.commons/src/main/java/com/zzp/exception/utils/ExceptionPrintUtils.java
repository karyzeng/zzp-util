package com.zzp.exception.utils;

import org.apache.commons.lang3.StringUtils;
import sun.security.action.GetPropertyAction;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.AccessController;

/**
 * @Description 异常转换为字符串
 * @Author zzp
 * @since 2019.06.04
 **/
public class ExceptionPrintUtils {

    /**
     * 将异常信息转换成字符串
     *
     * @param t 异常
     *
     * @return String
     */
    public static String printStackTraceToString(Throwable t) {
        try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw, true);) {
            t.printStackTrace(pw);
            String stackTraceString = sw.getBuffer().toString();
            return stackTraceString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据异常信息字符串来解析出异常的信息
     * @param exceptionStr 异常信息字符串
     * @return
     */
    public static String analysisExceptionMessage(String exceptionStr) {
        if (StringUtils.isNotBlank(exceptionStr)) {
            // 获取当前系统的换行符
            String lineSeparator = (String) AccessController.doPrivileged(new GetPropertyAction("line.separator"));
            String message = exceptionStr.substring(exceptionStr.indexOf(":") + 1, exceptionStr.indexOf(lineSeparator));
            return message.trim();
        }
        return null;
    }

}
