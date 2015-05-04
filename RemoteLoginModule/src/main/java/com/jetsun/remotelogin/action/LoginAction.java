package com.jetsun.remotelogin.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jetsun.remotelogin.bean.LoginRspKey;
import com.jetsun.remotelogin.bean.SessionKey;
import com.jetsun.remotelogin.bean.UlcProperty;
import com.jetsun.remotelogin.dao.interfaces.LoginHandler;
import com.jetsun.remotelogin.utility.DownloadUtil;
import com.jetsun.remotelogin.utility.HttpResponser;
import com.jetsun.remotelogin.utility.SessionUtil;
import com.jetsun.remotelogin.utility.property.UlcPropertyUtil;
import com.opensymphony.xwork2.Action;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/9/15
 * Desc:证书登陆action
 */
public class LoginAction extends BaseAction implements Action {
    @Autowired
    private LoginHandler loginHandler;

    /**
     * 登陆
     * @return
     * @throws Exception
     */
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
        procMap = loginHandler.doLogin(certNo, password, ip,session);
        //取出存储过程返回结果
        String sJson = (String) procMap.get(LoginRspKey.O_STRS_JSON);
        String errorCode = (String) procMap.get(LoginRspKey.O_ERRORCODE);
        String errorMsg = (String) procMap.get(LoginRspKey.O_ERRORMSG);

        logger.info("cert login：ERROR_CODE~" + errorCode + "~ERROR_MSG~" + errorMsg);

        if ("0".equals(errorCode)) {//登陆成功
            Map<String, String> loginInfoMap = new ObjectMapper().readValue(sJson, HashMap.class);
            //把登陆信息放入session
            SessionUtil.loginSessionHandler(loginInfoMap, session);
            //设置ajax的返回参数
            ajaxMap.put("url", UlcPropertyUtil.getProperty(UlcProperty.INDEX_PAGE));
            //登陆结果
            isSuccess = true;
        }
        if ("-1".equals(errorCode)) {//证书错误
            ajaxMap.put("url", UlcPropertyUtil.getProperty(UlcProperty.CERT_ERROR_PAGE));
            isSuccess = false;
        }
        if ("-2".equals(errorCode)) {//证书存在但密码错误
            isSuccess = false;
        }
        if ("-3".equals(errorCode)) {//系统未启用
            isSuccess = false;
        }
        //返回前端
        HttpResponser.rspToAjaxWithMap(response, ajaxMap, isSuccess);

        return null;
    }

    /**
     * 修改密码
     * @return
     * @throws Exception
     */
    public String changePW() throws Exception {
        //初始化
        //请求参数
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        int userId = Integer.parseInt((String) session.get(SessionKey.USER_ID));
        //修改密码
        Map<String, Object> retMap = loginHandler.changePassword(oldPassword, newPassword, userId,session);

        HttpResponser.rspToAjaxWithMap(response, retMap, (Boolean) retMap.get(LoginRspKey.SUCCESS));
        return null;
    }

    /**
     * 下载服务器证书
     */
    public String downloadServerCert() throws Exception{
        //拼装全路径
        String path = UlcPropertyUtil.getProperty(UlcProperty.SERVER_CERT_ZIP);
        File file = new File(path);
        //下载
        if (file.exists()) {
            //进行下载
            DownloadUtil.download(response, path, "证书安装包.zip");
        }else {
            response.getWriter().print("<div style='font-size:25px;position: absolute;top: 40%;left: 23%;'>The request resource no longer exists, please contact the administrator.</div>");
        }
        return null;
    }

}
