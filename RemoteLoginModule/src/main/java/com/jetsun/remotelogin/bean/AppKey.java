package com.jetsun.remotelogin.bean;

import com.jetsun.remotelogin.utility.property.UlcPropertyUtil;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/9/25
 * Desc:登陆中心应用标识码
 */
public class AppKey {
    public final static long APP_KEY = Long.parseLong(UlcPropertyUtil.getProperty(UlcProperty.APP_KEY));
}
