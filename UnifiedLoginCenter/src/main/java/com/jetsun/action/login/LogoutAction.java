package com.jetsun.action.login;

import com.jetsun.action.BaseAction;
import com.jetsun.bean.common.SessionKey;
import com.jetsun.dao.interfaces.LoginDao;
import com.jetsun.utility.HttpResponser;
import com.opensymphony.xwork2.ActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.cert.X509Certificate;
import java.util.Map;

public class LogoutAction extends BaseAction {
    @Autowired
    LoginDao loginDao;
    public String execute() throws Exception {
        /**
         * 功能：用户注销
         */
        Map<String, Object> hmSession = ActionContext.getContext().getSession();
        //清除登陆中心登陆状态
        X509Certificate certificate = (X509Certificate) hmSession.get(SessionKey.USER_LOGIN_CERTIFICATE);
        boolean isSuccess = loginDao.logout(certificate.getSerialNumber().toString());
        //清除本机session
        hmSession.clear();

        HttpResponser.rspToHttp(response, isSuccess);

        return null;
    }
}
