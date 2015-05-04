package com.jetsun.utility;

/**
 * Title:        工商局年检系统
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      Talent
 * @author SHI
 * @version 1.0
 */

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

//import utility.DBConnectionManager;
//import java.sql.Connection;
//import java.text.*;
//import java.util.Vector;
//import java.sql.*;
//import utility.*;

public class CommAPI {

    public CommAPI() {
    }


    /**
     * 设置显示格式
     */
    public static String formatDouble(double d)
    {
        DecimalFormat df = new DecimalFormat("###,##0.00");
        return df.format(d);
    }
    public static String formatDouble(double d,String sFormat)
    {

        if (sFormat.equals("")) sFormat="###,##0.00";

        DecimalFormat df = new DecimalFormat(sFormat);
        return df.format(d);
    }

    //负数
    public static String formatDouble_minus(String value)
    {
        if(value!=null){
            if (!value.equals("")) {
                double aaa = Double.parseDouble(value);
                if (aaa < 0) {
                    value = "-" + formatDouble(Double.parseDouble(value.substring(1, value.length()-1)), "#,##0.00");
                }else{
                    value = formatDouble(Double.parseDouble(value), "#,##0.00");
                }
            }
        }else{
            value="";
        }
        return value;
    }




    /**
     * 设置显示格式(传入double)
     * @param d
     * @return
     */
    public static String formatInt(double d)
    {
        DecimalFormat df = new DecimalFormat("###,###");
        return df.format(d);
    }

    /**
     * 设置显示格式(传入String)
     */
    public static String formatInt(String d)
    {
        if (d==null||d.equals(""))return "";
        DecimalFormat df = new DecimalFormat("###,###");
        return df.format(Double.parseDouble(d));
    }


    /**
     * 四舍五入，两位精度
     */
    public static double doubleRound(double d,int scale)
    {
        BigDecimal bd = new BigDecimal(d);
        bd.setScale(scale, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

    /**
     * 设置日期格式
     */
    public static String formatDate(String s,String format)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String fdate = "";

        try{
            fdate = sdf.format(java.sql.Date.valueOf(s));
        }
        catch (Exception e){
            fdate = s;
            System.out.println(fdate);

        }

        return fdate;
    }

    /**
     * 功能：    将8位日期格式化成strFormat格式
     * 日期：    20021211
     * 描述：
     * 入口：    strValue    8位或10位日期值
     * 出口：    转换后的日期格式
     * 异常：    如果是系统异常,则抛出错误
     *
     */
    public String FormatDate(String strDate,String strFormat)
    {
        int i;
        String strTemp = "";
        String strReturnString = "";
        try{

            if(strDate.length()==8){
                strDate = strDate.substring(0,4) + "-" + strDate.substring(4,6) + "-" + strDate.substring(6,8);
            }
            strReturnString = formatDate(strDate,strFormat);

        }

        catch(Exception e){
            strReturnString = "";
        }

        return strReturnString;

    }

    public static String getCurDate()
    {
        String strTemp = null;

        SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMdd");
        strTemp  = formatter.format(new Date());

        return strTemp;
    }
    public static String getWeek()
    {
        Date dt = new Date();
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
        {
            w = 0;
        }
        return weekDays[w];
    }

    public static String getChinaDate()
    {
        String todate = new SimpleDateFormat("yyyy年MM月dd日").format(new Date());//今天的是几月几日
        return todate;
    }
    public static String getCurTime()
    {
        String strTemp = null;

        SimpleDateFormat formatter = new SimpleDateFormat ("HH:mm:ss");
        strTemp  = formatter.format(new Date());

        return strTemp;
    }


    public Properties loadProperties(String strFileName)
    /**
     * 通过流载入属性信息
     */
    {
        InputStream is = getClass().getResourceAsStream(strFileName);
        Properties props = new Properties();
        try {
            props.load(is);
        }
        catch (Exception e) {
            System.err.println("不能读取控制属性文件,请确保 " + strFileName + " 文件在CLASSPATH指定的路径中");
        }
        return props;
    }


    public String ChangMoneyFormat(String strmoney)
/*
  *将金额的"#,##0.00"形式变成"###0.00"形式
*/
    {
        int i,j;
        String strM="";
        for (i=0;i<=strmoney.length()-1;i++)
        {
            if (!strmoney.substring(i,i+1).equals(","))strM=strM + strmoney.substring(i,i+1);
        }
        return strM;
    }
    /**
     * 将金额数值变成大写形式
     * @param strLow  小写的金额数值
     * @return        大写的金额数值
     * @throws Exception
     * @author  Winsun
     */


    public String ChangeToCaps(String strLow) throws Exception {

        String strMatrix[]={"元","拾","佰","仟","万","拾","佰","仟","亿","拾","佰","仟"};
        String strCaps[]={"零","壹","贰","叁","肆","伍","陆","柒","捌","玖"};

        BigDecimal decTemp;
        int i,j,k;
        String strNumber="";
        String strResult="";
        String strTemp="";
        char cTemp;
        String strNegative="";

        if (strLow.substring(0,1).equals("-")) {
            strNegative="负";
            strLow=strLow.substring(1);
        }

        //格式化输入的数值
        decTemp = new BigDecimal(strLow);

        DecimalFormat df = new DecimalFormat("###0.00");

        strLow = df.format(decTemp);

        //将数值倒转排列，如：234567.89改成98.765432
        i = strLow.length();
        strTemp = strLow;
        strLow="";

        for (j=0;j<i;j++) {
            strLow = strLow + String.valueOf(strTemp.charAt(i-j-1));
        }

        //取元以上位置处理
        strLow = strLow.substring(3);
        i = strLow.length();

        //按位入数，当个、万、亿位为0时压缩0。
        for (j=0;j<i;j++) {
            strNumber = String.valueOf(strLow.charAt(j));
            k = Integer.parseInt(strNumber);
            if (strNumber.endsWith("0")) {
                if (j==0 || j==4 || j==8) {
                    strResult = strMatrix[j] + strResult;
                } else {
                    strResult = strCaps[k] + strResult ;
                }
            } else {
                k = Integer.parseInt(strNumber);
                strResult = strCaps[k] + strMatrix[j] + strResult;
            }
        }

        //压缩0
        i = strResult.length();
        strTemp = strResult;
        strResult = "";
        for (j=0;j<i-1;j++) {
            if (strTemp.charAt(j)=='零') {
                cTemp = strTemp.charAt(j+1);
                if (cTemp=='零' || strMatrix[0].equals(String.valueOf(cTemp)) ||
                        strMatrix[4].equals(String.valueOf(cTemp)) || strMatrix[8].equals(String.valueOf(cTemp))) {
                    strResult = strResult;
                } else {
                    strResult = strResult + strTemp.charAt(j);
                }
            } else {
                strResult = strResult + strTemp.charAt(j);
            }
        }
        strResult = strResult + strTemp.charAt(i-1);


        //处理万位均为0的情况
        i = strResult.length();
        strTemp = strResult;
        strResult = "";
        for (j=0;j<i-1;j++) {
            if (strMatrix[4].equals(String.valueOf(strTemp.charAt(j))) &&
                    strMatrix[8].equals(String.valueOf(strTemp.charAt(j-1)))) {
                strResult = strResult;
            } else {
                strResult = strResult + strTemp.charAt(j);
            }
        }
        strResult = strResult + strTemp.charAt(i-1);


        if (strResult.length()==0) {
            strResult = strCaps[0] + strResult;
        }

        strLow = df.format(decTemp);
        strLow = strLow.substring(strLow.length()-2);

        if (strLow.equals("00")) {
            strResult = strResult + "整";
        } else {

            k = Integer.parseInt(String.valueOf(strLow.charAt(0)));
            if (strLow.charAt(0)=='0') {
                strResult = strResult + strCaps[k];
            } else {
                strResult = strResult + strCaps[k] + "角";
            }

            if (strLow.charAt(1)=='0') {
            } else {
                k = Integer.parseInt(String.valueOf(strLow.charAt(1)));
                strResult = strResult + strCaps[k] + "分";
            }

        }

        return strNegative + strResult;
    }

    public static String GetItem(String strInput,String strBreak,long lngIndex)
    {
  /*
    *   功能:从strInput中截取以strBreak为分隔符第lngIndex次出现的字符串.
    *   编写:HJQ
    *   日期:2003-04-14
    *
  */
        int x1,x2,i;
        String strTemp="";
        long num=lngIndex;
        String str1=strInput;
        String str2=strBreak;
        x1=0;

        for (i=1;i<=num;i++){
            x1=str1.indexOf(str2);
            if (i==num && x1 != -1) {
                strTemp=str1.substring(0,x1);
                break;
            }
            if (x1==-1 && i==num){
                strTemp=str1.substring(0,str1.length());
                break;
            }
            if (x1==-1 && i < num){
                strTemp="";
                break;
            }
            if (i > num){
                strTemp="";
                break;
            }
            str1=str1.substring(x1+str2.length(),str1.length());
        }

        return strTemp;

    }

    public static void main(String strArgs[]) {
        System.out.println(getCurDate());
    }
}