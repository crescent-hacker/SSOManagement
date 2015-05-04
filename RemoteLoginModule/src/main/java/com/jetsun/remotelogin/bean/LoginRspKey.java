package com.jetsun.remotelogin.bean;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/8/14
 * Desc:登陆常量-响应
 * Note:中心登陆系统修改此文件后必须同步到各个子系统
 */
public class LoginRspKey {

    /**
     * 证书是否存在
     */
    public static final String IS_CERT_EXIST = "IS_CERT_EXIST";

    /**
     * 登陆成功后的JSON对象
     * LU_ID--登陆随机码
     * OPER_NAME--用户名
     * USERID--用户id
     * AREA_ID--统筹区id
     * HSPT_ID--医院id
     * ROLE_ACCESS--登陆权限
     * OPER_LEVEL--用户级别：0-省 1-市 2-医院
     * OPER_NO--用户编码
     */
    public static final String O_STRS_JSON = "O_STRS_JSON";

    /**
     * 登陆存储过程返回代码
     *  0--成功;
     * -1--找不证书或证书未启用;
     * -2--证书合法但密码错误;
     */
    public static final String O_ERRORCODE = "O_ERRORCODE";

    /**
     * 登陆存储过程返回信息
     */
    public static final String O_ERRORMSG = "O_ERRORMSG";

    /**
     * 用户名
     */
    public static final String OPER_NAME = "OPER_NAME";

    /**
     * 用户英文名
     */
    public static final String OPER_NO = "OPER_NO";

    /**
     * 修改密码情况
     */
    public static final String SUCCESS = "success";

    /**
     * 修改密码返回信息
     */
    public static final String MESSAGE = "message";

    /**
     * 翻译url
     */
    public static final String PAGE_URL = "PAGE_URL";



}
