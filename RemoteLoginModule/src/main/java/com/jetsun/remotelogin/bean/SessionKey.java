package com.jetsun.remotelogin.bean;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/8/14
 * Desc:SESSION 关键字常量
 */
public class SessionKey {
    /**
     * 客户证书 X509Certificate
     */
    public final static String USER_LOGIN_CERTIFICATE = "USER_LOGIN_CERTIFICATE";

    /**
     * 客户证书生效情况 boolean
     */
    public final static String USER_LOGIN_CERT_IS_VALID = "USER_LOGIN_CERT_IS_VALID";
    /**
     * 事件模型，分页用
     */
    public final static String EVENT_MODEL = "EVENT_MODEL";

    /**
     * 用户id
     */
    public final static String USER_ID = "USER_ID";

    /**
     * 登陆代码（从序列中获取）
     */
    public final static String LOGIN_CODE = "LOGIN_CODE";

    /**
     * 用户中文名
     */
    public final static String OPER_NAME = "OPER_NAME";

    /**
     * 登陆权限集
     */
    public final static String ROLE_ACCESS = "ROLE_ACCESS";

    /**
     * 地区
     */
    public final static String AREA_ID = "AREA_ID";
    /**
     * 医院
     */
    public final static String HSPT_ID = "HSPT_ID";
    /**
     * 地区编码
     */
    public final static String AREA_NO = "AREA_NO";
    /**
     * 医院编码
     */
    public final static String HSPT_NO = "HSPT_NO";
    /**
     * 地区名
     */
    public final static String AREA_NAME = "AREA_NAME";
    /**
     * 医院名
     */
    public final static String HSPT_NAME = "HSPT_NAME";

    /**
     * 是否管理员
     */
    public final static String IS_ADMIN = "isadmin";

    /**
     * 用户名
     */
    public final static String OPER_NO = "OPER_NO";

    /**
     * 用户级别 0-省 1-市 2-医院
     */
    public final static String OPER_LEVEL = "OPER_LEVEL";

    /**
     * 本次访问本系统的IP地址
     */
    public final static String IP_ADDRESS = "IP_ADDRESS";

    /**
     * ip是否非法
     */
    public final static String IP_ILLEGAL = "IP_ILLEGAL";

    /**
     * 当前访问jsp
     */
    public final static String CURRENT_PAGE = "CURRENT_PAGE";

    /**
     * 当前session的HTTP_CLIENT
     */
    public final static String HTTP_CLIENT = "HTTP_CLIENT";
    /**
     * 是否安卓调用
     */
    public final static String IS_ANDROID_INVOKE = "IS_ANDROID_INVOKE";
}
