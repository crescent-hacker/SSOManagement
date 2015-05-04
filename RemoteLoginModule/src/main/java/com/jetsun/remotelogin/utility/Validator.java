package com.jetsun.remotelogin.utility;

import com.jetsun.remotelogin.bean.UlcProperty;
import com.jetsun.remotelogin.utility.property.IPPropertyUtil;
import com.jetsun.remotelogin.utility.property.UlcPropertyUtil;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/11/18
 * Desc:
 */
public class Validator {
    /**
     * 静态文件路径数组
     */
    private static String[] staticPaths;
    /**
     * ip规则数组
     */
    private static String[] ipRules;
    /**
     * 安卓ip白名单数组
     */
    private static String[] androidIps;

    /**
     * 判断是否静态文件
     * @param currentURL 当前请求路径
     * @param contextPath 请求上下文
     * @return 判断结果
     */
    public static boolean isStaticPath(String currentURL, String contextPath) {
        //首次读取和分割静态路径
        if (staticPaths == null || staticPaths.length == 0) {
            String staticPathStr = UlcPropertyUtil.getProperty(UlcProperty.STATIC_PATH);
            staticPaths = staticPathStr.split("\\|");
        }
        //进行判断
        for (String staticPath : staticPaths) {
            if (currentURL.startsWith(contextPath + staticPath)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检验是否内部安卓调用
     * @param invokeIp 安卓服务端ip
     * @param currentURL 请求地址
     * @param contextPath 上下文
     * @return
     */
    public static boolean isAndroidInvoke(String invokeIp,String currentURL,String contextPath){
        //安卓白名单ip
        String ipStr = UlcPropertyUtil.getProperty(UlcProperty.ANDROID_INVOKE_IP_WHITE_LIST);
        //是否在白名单内
        boolean isPermit = false;
        //是否安卓调用路径
        boolean isAndroidPath = false;

        //首次分配ip策略数组
        if(androidIps==null||androidIps.length==0){
            androidIps = ipStr.split("\\|");
        }
        //为空则不允许任何ip进入
        if(androidIps==null||androidIps.length==0){
            return isPermit;
        }
        //逐个通配符进行ip匹配
        for(String ip:androidIps){
            if(invokeIp.equals(ip)){
                isPermit = true;
                break;
            }
        }
        //判断是否安卓调用路径
        isAndroidPath = currentURL.startsWith(contextPath + UlcPropertyUtil.getProperty(UlcProperty.ANDROID_INVOKE_PATH));

        //返回结果
        return isPermit&&isAndroidPath;
    }

    /**
     * 检验ip
     * @param remoteIp ip
     * @return
     */
    public static boolean checkIp(String remoteIp){
        //检查模式
        int mode = Integer.parseInt(IPPropertyUtil.getProperty(IPPropertyUtil.IP_CHECK_MODE));
        //通配模式
        if(mode == 0){
            String patternStr = IPPropertyUtil.getProperty(IPPropertyUtil.IP_CHECK_PATTERN);
            if(StringUtil.isEmpty(patternStr)){//为空不检查
                return true;
            }
            //首次分配ip策略数组
            if(ipRules==null||ipRules.length==0){
                ipRules = patternStr.split("\\|");
            }
            for(String pattern:ipRules){//逐个通配符进行匹配
                if(remoteIp.startsWith(pattern)){
                    return true;
                }
            }
        }
        //白名单模式
        if(mode == 1){
            String ipStr = IPPropertyUtil.getProperty(IPPropertyUtil.IP_WHITE_LIST);
            if(StringUtil.isEmpty(ipStr)){//为空则不允许任何ip进入
                return false;
            }
            //首次分配ip策略数组
            if(ipRules==null||ipRules.length==0){
                ipRules = ipStr.split("\\|");
            }
            for(String ip:ipRules){//逐个通配符进行匹配
                if(remoteIp.equals(ip)){
                    return true;
                }
            }
        }
        return false;
    }


}
