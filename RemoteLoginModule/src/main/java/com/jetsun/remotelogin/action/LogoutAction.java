package com.jetsun.remotelogin.action;

import com.jetsun.remotelogin.bean.SessionKey;
import com.jetsun.remotelogin.dao.interfaces.LoginHandler;
import com.jetsun.remotelogin.utility.HttpResponser;
import com.opensymphony.xwork2.ActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.cert.X509Certificate;
import java.util.Map;

public class LogoutAction extends BaseAction {
    @Autowired
    LoginHandler loginHandler;

    public String execute() throws Exception {
        /**
         * 功能：用户注销
         *
         */
        Map<String, Object> hmSession = ActionContext.getContext().getSession();
        //清除登陆中心登陆状态
        X509Certificate certificate = (X509Certificate) session.get(SessionKey.USER_LOGIN_CERTIFICATE);
        boolean isSuccess = loginHandler.logout(certificate.getSerialNumber().toString(),session);
        //清除本机session
        hmSession.clear();
        //响应ajax
        HttpResponser.rspToAjax(response, isSuccess);

        return null;
    }
}
