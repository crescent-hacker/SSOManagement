package com.jetsun.remotelogin.utility;

import com.jetsun.remotelogin.bean.SessionKey;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/10/24
 * Desc:处理session信息
 */
public class SessionUtil {
    /**
     * 处理登陆的session信息
     * @param loginInfoMap 登陆信息
     * @param session 会话
     */
    public static void loginSessionHandler(Map<String, String> loginInfoMap,Map<String,Object> session){
        //参数放入session
        session.put(SessionKey.LOGIN_CODE, loginInfoMap.get("LU_ID"));
        session.put(SessionKey.USER_ID, loginInfoMap.get("USERID"));
        session.put(SessionKey.OPER_NAME, loginInfoMap.get("OPER_NAME"));
        session.put(SessionKey.ROLE_ACCESS, loginInfoMap.get("ROLE_ACCESS"));
        session.put(SessionKey.AREA_ID, loginInfoMap.get("AREA_ID"));
        session.put(SessionKey.AREA_NO, loginInfoMap.get("AREA_NO"));
        session.put(SessionKey.HSPT_ID, loginInfoMap.get("HSPT_ID"));
        session.put(SessionKey.HSPT_NO, loginInfoMap.get("HSPT_NO"));
        session.put(SessionKey.AREA_NAME, loginInfoMap.get("AREA_NAME"));
        session.put(SessionKey.HSPT_NAME, loginInfoMap.get("HSPT_NAME"));
        session.put(SessionKey.OPER_NO, loginInfoMap.get("OPER_NO"));
        session.put(SessionKey.OPER_LEVEL, loginInfoMap.get("OPER_LEVEL"));
        //判断是否管理员(这段代码只有三目有用)
        int areaId = Integer.parseInt(loginInfoMap.get("AREA_ID"));
        int hsptId = Integer.parseInt(loginInfoMap.get("HSPT_ID"));
        boolean isAdmin = areaId * hsptId == 0;//省市的地市医院id含0，相乘等于0，为管理员
        session.put(SessionKey.IS_ADMIN,isAdmin);
    }

    /**
     * 处理SSO登陆的session信息
     * @param loginInfoMap SSO登陆信息
     * @param session 会话
     */
    public static void loginSSOSessionHandler(Map<String, String> loginInfoMap,HttpSession session){
        //参数放入session
        session.setAttribute(SessionKey.LOGIN_CODE, loginInfoMap.get("LU_ID"));
        session.setAttribute(SessionKey.USER_ID, loginInfoMap.get("USERID"));
        session.setAttribute(SessionKey.OPER_NAME, loginInfoMap.get("OPER_NAME"));
        session.setAttribute(SessionKey.ROLE_ACCESS, loginInfoMap.get("ROLE_ACCESS"));
        session.setAttribute(SessionKey.AREA_ID, loginInfoMap.get("AREA_ID"));
        session.setAttribute(SessionKey.AREA_NO, loginInfoMap.get("AREA_NO"));
        session.setAttribute(SessionKey.HSPT_ID, loginInfoMap.get("HSPT_ID"));
        session.setAttribute(SessionKey.HSPT_NO, loginInfoMap.get("HSPT_NO"));
        session.setAttribute(SessionKey.AREA_NAME, loginInfoMap.get("AREA_NAME"));
        session.setAttribute(SessionKey.HSPT_NAME, loginInfoMap.get("HSPT_NAME"));
        session.setAttribute(SessionKey.OPER_NO, loginInfoMap.get("OPER_NO"));
        session.setAttribute(SessionKey.OPER_LEVEL, loginInfoMap.get("OPER_LEVEL"));
        //判断是否管理员(这段代码只有三目有用)
        int areaId = Integer.parseInt(loginInfoMap.get("AREA_ID"));
        int hsptId = Integer.parseInt(loginInfoMap.get("HSPT_ID"));
        boolean isAdmin = areaId * hsptId == 0;//省市的地市医院id含0，相乘等于0，为管理员
        session.setAttribute(SessionKey.IS_ADMIN,isAdmin);
    }

    /**
     * 清除登陆信息
     * @param session 会话
     */
    public static void clearSession(HttpSession session) {
        session.removeAttribute(SessionKey.LOGIN_CODE);
        session.removeAttribute(SessionKey.USER_ID);
        session.removeAttribute(SessionKey.OPER_NAME);
        session.removeAttribute(SessionKey.ROLE_ACCESS);
        session.removeAttribute(SessionKey.OPER_NO);
        session.removeAttribute(SessionKey.AREA_ID);
        session.removeAttribute(SessionKey.AREA_NO);
        session.removeAttribute(SessionKey.HSPT_ID);
        session.removeAttribute(SessionKey.HSPT_NO);
        session.removeAttribute(SessionKey.OPER_LEVEL);
        session.removeAttribute(SessionKey.AREA_NAME);
        session.removeAttribute(SessionKey.HSPT_NAME);
        session.removeAttribute(SessionKey.IS_ADMIN);
    }
}
