package com.jetsun.remotelogin.bean;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/8/14
 * Desc:登陆常量-请求
 * Note:中心登陆系统修改此文件后必须同步到各个子系统
 */
public class LoginReqKey {
    /**
     * 系统id
     */
    public static final String SYSTEM_ID = "SYSTEM_ID";
    /**
     * 证书序列号
     */
    public static final String CERT_NO = "CERT_NO";
    /**
     * 密码
     */
    public static final String PASSWORD = "PASSWORD";
    /**
     * 新密码
     */
    public static final String NEW_PASSWORD = "NEW_PASSWORD";
    /**
     * 用户id
     */
    public static final String USER_ID = "USER_ID";
    /**
     * 登陆流水号
     */
    public static final String LOGIN_CODE = "LOGIN_CODE";
    /**
     * 检查类型
     *  1--jsp;
     *  2--action;
     */
    public static final String CHECK_TYPE = "CHECK_TYPE";
    /**
     * 操作字符串（jsp地址或action地址）
     */
    public static final String OPERATION_KEY = "OPERATION_KEY";

    /**
     * 操作详情（提交请求的参数）
     */
    public static final String OPT_DETAILS = "OPT_DETAILS";

    /**
     * 用户浏览器访问子系统的ip地址
     */
    public static final String IP_ADDRESS = "IP_ADDRESS";

    /**
     * 页面ID
     */
    public static final String PID = "PID";

    /**
     * 当前jsp
     */
    public static final String CURRENT_PAGE = "CURRENT_PAGE";
}
