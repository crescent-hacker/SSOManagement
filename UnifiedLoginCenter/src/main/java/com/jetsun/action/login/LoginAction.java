package com.jetsun.action.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jetsun.action.BaseAction;
import com.jetsun.bean.common.AppKey;
import com.jetsun.bean.common.LoginRspKey;
import com.jetsun.bean.common.SessionKey;
import com.jetsun.bean.common.SysProperty;
import com.jetsun.dao.interfaces.LoginDao;
import com.jetsun.utility.HttpResponser;
import com.jetsun.utility.SessionUtil;
import com.jetsun.utility.property.SysPropertyUtil;
import com.opensymphony.xwork2.Action;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/9/15
 * Desc:权限管理中心登陆action
 */
public class LoginAction extends BaseAction implements Action {
    @Autowired
    private LoginDao loginDao;

    public String login() throws Exception {
        /**
         * 功能：检查用户的证书是否在数据库中，在则直接查询出用户userid
         */
        //初始化参数
        Map<String, Object> procMap;//储存过程返回map
        boolean isSuccess = false;//登陆是否成功
        Map<String, Object> ajaxMap = new HashMap<String, Object>();//返回前端的map

        //获得证书
        X509Certificate cert = (X509Certificate) session.get(SessionKey.USER_LOGIN_CERTIFICATE);
        //取得证书序列号
        String certNo = cert.getSerialNumber().toString();
        //获得登陆密码
        String password = request.getParameter("password");
        //获得ip
        String ip = (String) session.get(SessionKey.IP_ADDRESS);
        //登陆取权限
        procMap = loginDao.doLogin(certNo, password, ip, AppKey.APP_KEY);
        //取出存储过程返回结果
        String sJson = (String) procMap.get(LoginRspKey.O_STRS_JSON);
        String errorCode = (String) procMap.get(LoginRspKey.O_ERRORCODE);
        String errorMsg = (String) procMap.get(LoginRspKey.O_ERRORMSG);

        logger.info("证书登陆：ERROR_CODE~" + errorCode + "~ERROR_MSG~" + errorMsg);

        if ("0".equals(errorCode)) {//登陆成功
            Map<String, String> loginInfoMap = new ObjectMapper().readValue(sJson, HashMap.class);
            //参数放入session
            SessionUtil.loginSessionHandler(loginInfoMap,session);
            //设置ajax的返回参数
            ajaxMap.put("url", SysPropertyUtil.getProperty(SysProperty.INDEX_PAGE));
            //登陆结果
            isSuccess = true;
        }
        if ("-1".equals(errorCode)) {//证书错误
            ajaxMap.put("url", SysPropertyUtil.getProperty(SysProperty.CERT_ERROR_PAGE));
            isSuccess = false;
        }
        if ("-2".equals(errorCode)) {//证书存在但密码错误
            isSuccess = false;
        }
        if ("-3".equals(errorCode)) {//系统未启用
            isSuccess = false;
        }
        //返回前端
        HttpResponser.rspToHttpWithMap(response, ajaxMap, isSuccess);

        return null;
    }

    public String changePW() throws Exception {
        //初始化
        //请求参数
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        int userId = Integer.parseInt((String) session.get(SessionKey.USER_ID));

        Map<String, Object> retMap = loginDao.changePassword(oldPassword, newPassword, userId);

        HttpResponser.rspToHttpWithMap(response, retMap, (Boolean) retMap.get(LoginRspKey.SUCCESS));
        return null;
    }
}
