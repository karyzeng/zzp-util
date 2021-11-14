package com.zzp.cron.utils;

import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description Cron表达式工具类
 * @Author Garyzeng
 * @since 2021.11.14
 **/
public class CronExpressionUtils {

    public static void main(String[] args) {
        List<String> cronExpressions = new ArrayList<String>();
        cronExpressions.add("0 0/30 * * * ? *");
        cronExpressions.add("0 0 10 * * ? *");
        cronExpressions.add("0 0 0/1 * * ? *");
        try {
            String dataStr = "2021-11-14 10:00:00";
            Date date = DateUtils.parseDate(dataStr, "yyyy-MM-dd hh:mm:ss");
            for (String cronExpression : cronExpressions) {
                System.out.println(isSatisfiedBy(cronExpression, date));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 指示给定日期是否满足cron表达式。注意，毫秒被忽略了，所以在同一秒的不同毫秒上的两个日期总是有相同的结果。
     *
     * @param cronExpression cron表达式
     * @param date 日期
     * @return boolean
     */
    public static boolean isSatisfiedBy(String cronExpression, Date date) {
        CronExpression cron = null;
        try {
            cron = new CronExpression(cronExpression);
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println(String.format("isSatisfiedBy -> cronExpression parse exception %s", cronExpression));
            return false;
        }
        return cron.isSatisfiedBy(date);
    }

}
