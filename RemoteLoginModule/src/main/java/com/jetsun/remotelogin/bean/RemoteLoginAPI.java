package com.jetsun.remotelogin.bean;

import com.jetsun.remotelogin.utility.property.UlcPropertyUtil;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/9/28
 * Desc:登陆中心远程调用地址
 */
public class RemoteLoginAPI {
    public static String isCertExistAPI() {
        return UlcPropertyUtil.getProperty(UlcProperty.REMOTE_LOGIN_ADDR) + "/remoteLogin/isCertExist.action";
    }

    public static String doLoginSSOAPI() {
        return UlcPropertyUtil.getProperty(UlcProperty.REMOTE_LOGIN_ADDR) + "/remoteLogin/doLoginSSO.action";
    }

    public static String doLoginAPI() {
        return UlcPropertyUtil.getProperty(UlcProperty.REMOTE_LOGIN_ADDR) + "/remoteLogin/doLogin.action";
    }

    public static String checkUserAuthorityAPI() {
        return UlcPropertyUtil.getProperty(UlcProperty.REMOTE_LOGIN_ADDR) + "/remoteLogin/checkUserAuthority.action";
    }

    public static String getUserNameAndWorkNoAPI() {
        return UlcPropertyUtil.getProperty(UlcProperty.REMOTE_LOGIN_ADDR) + "/remoteLogin/getUserNameAndWorkNo.action";
    }

    public static String changePasswordAPI() {
        return UlcPropertyUtil.getProperty(UlcProperty.REMOTE_LOGIN_ADDR) + "/remoteLogin/changePassword.action";
    }

    public static String getUrlAPI() {
        return UlcPropertyUtil.getProperty(UlcProperty.REMOTE_LOGIN_ADDR) + "/remoteLogin/getUrl.action";
    }

    public static String logoutAPI() {
        return UlcPropertyUtil.getProperty(UlcProperty.REMOTE_LOGIN_ADDR) + "/remoteLogin/logout.action";
    }
}
