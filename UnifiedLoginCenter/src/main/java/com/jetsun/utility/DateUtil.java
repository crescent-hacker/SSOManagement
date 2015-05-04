package com.jetsun.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/7/22
 * Desc:日期工具类
 */
public class DateUtil {
    private static Logger logger = LoggerFactory.getLogger(DateUtil.class);

    /**
     * 把字符串转换成util Date
     *
     * @return
     */
    public static java.util.Date transStringToDate(String dateString) throws ParseException {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = sd.parse(dateString);
        return date;
    }

    /**
     * 字符串转sql Date
     */
    public static Date transStringToSqlDate(String dateString) throws ParseException {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = sd.parse(dateString);
        Date dateS = new Date(date.getTime());
        return dateS;
    }

    /**
     * sql Date转字符串
     */
    public static String transDateToStr(Date date) throws ParseException {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sd.format(date);
        return dateStr;
    }

    /**
     * util Date转子字符串
     */
    public static String transDateToStr(java.util.Date date) throws ParseException {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sd.format(date);
        return dateStr;
    }

    /**
     * util Date 转sql Date
     */
    public static Date transDateFromUtilToSql(java.util.Date date) throws ParseException {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        String str = sd.format(date);
        java.util.Date dateN = sd.parse(str);
        Date returnDate = new Date(dateN.getTime());
        return returnDate;
    }

    /**
     * 把date格式进行年月日截断
     */
    public static java.util.Date formatDate(java.util.Date date) throws ParseException {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date dateN;
        String str = sd.format(date);
        dateN = sd.parse(str);
        return dateN;
    }

    /**
     * 把当前日期按格式返回
     */
    public static String getTodayByFormat(String forMatStr) {
        SimpleDateFormat f = new SimpleDateFormat(forMatStr);
        return f.format(new java.util.Date());
    }

    /**
     * 把指定日期按格式返回
     */
    public static String getDateByFormat(String forMatStr, java.util.Date date) {
        SimpleDateFormat f = new SimpleDateFormat(forMatStr);
        return f.format(date);
    }

    /**
     * 返回前n个月的第一天
     *
     * @param prevMonth 前多少个月
     * @return
     */
    public static java.util.Date getMonthFirstDay(int prevMonth){
        //制造日期
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -prevMonth);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        //进行年月日截断
        java.util.Date targetDay = null;
		try {
			targetDay = formatDate(calendar.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return targetDay;
    }

    public static void main(String[] args) throws ParseException {

        System.out.println(DateUtil.getMonthFirstDay(0));
        System.out.println(DateUtil.getDateByFormat("yyyy-MM-dd",DateUtil.getMonthFirstDay(0)));
    }
}
