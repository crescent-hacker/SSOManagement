package com.jetsun.action.login;

import com.jetsun.action.BaseAction;
import com.jetsun.bean.common.LoginReqKey;
import com.jetsun.bean.common.LoginRspKey;
import com.jetsun.dao.interfaces.LoginDao;
import com.jetsun.utility.HttpResponser;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/9/23
 * Desc:各子系统统一登陆的接入入口
 */
public class UnifiedLoginInterface extends BaseAction {

    @Autowired
    LoginDao loginDao;

    /**
     * 证书是否存在
     */
    public String isCertExist() throws Exception {
        //证书
        String certNo = request.getParameter(LoginReqKey.CERT_NO);
        try {
            //判断证书是否存在
            boolean isExist = loginDao.isCertExist(certNo);
            //返回map
            Map<String, Object> retMap = new HashMap<String, Object>();
            retMap.put(LoginRspKey.IS_CERT_EXIST, isExist);
            //响应
            HttpResponser.rspToHttpWithMap(response, retMap, true);
        } catch (Exception e) {
            logger.error("远程调用错误|isCertExist", e);
            HttpResponser.rspToHttp(response, false);
        } finally {
            return null;
        }
    }

    /**
     * 登陆并获取权限
     */
    public String doLogin() throws Exception {
        //证书、密码、系统id、ip
        String certNo = request.getParameter(LoginReqKey.CERT_NO);
        String password = request.getParameter(LoginReqKey.PASSWORD);
        long systemId = Long.parseLong(request.getParameter(LoginReqKey.SYSTEM_ID));
        String ip = request.getParameter(LoginReqKey.IP_ADDRESS);
        try {
            //登陆返回信息
            Map<String, Object> retMap = loginDao.doLogin(certNo, password, ip, systemId);
            //响应
            HttpResponser.rspToHttpWithMap(response, retMap, true);
        } catch (Exception e) {
            logger.error("远程调用错误|doLogin", e);
            HttpResponser.rspToHttp(response, false);
        } finally {
            return null;
        }
    }

    /**
     * 登陆并获取权限
     */
    public String doLoginSSO() throws Exception {
        //证书、密码、系统id、ip
        String certNo = request.getParameter(LoginReqKey.CERT_NO);
        long systemId = Long.parseLong(request.getParameter(LoginReqKey.SYSTEM_ID));
        String ip = request.getParameter(LoginReqKey.IP_ADDRESS);
        try {
            //登陆返回信息
            Map<String, Object> retMap = loginDao.doLoginSSO(certNo, ip, systemId);
            //响应
            HttpResponser.rspToHttpWithMap(response, retMap, true);
        } catch (Exception e) {
            logger.error("远程调用错误|doLoginSSO", e);
            HttpResponser.rspToHttp(response, false);
        } finally {
            return null;
        }
    }

    /**
     * 检查权限
     */
    public String checkUserAuthority() throws Exception {
        //用户id、登陆随机码、检查类型、操作字符串、操作详情、ip、系统id
        String userId = request.getParameter(LoginReqKey.USER_ID);
        String loginCode = request.getParameter(LoginReqKey.LOGIN_CODE);
        String checkType = request.getParameter(LoginReqKey.CHECK_TYPE);
        String operationKey = request.getParameter(LoginReqKey.OPERATION_KEY);
        String optDetails = request.getParameter(LoginReqKey.OPT_DETAILS);
        String ip = request.getParameter(LoginReqKey.IP_ADDRESS);
        String currentPage = request.getParameter(LoginReqKey.CURRENT_PAGE);
        long systemId = Long.parseLong(request.getParameter(LoginReqKey.SYSTEM_ID));

        try {
            //判断权限
            Map<String, Object> retMap = loginDao.checkUserAuthority(userId, loginCode, checkType, operationKey, optDetails, ip, currentPage, systemId);
            //响应
            HttpResponser.rspToHttpWithMap(response, retMap, true);
        } catch (Exception e) {
            logger.error("远程调用错误|checkUserAuthority", e);
            HttpResponser.rspToHttp(response, false);
        } finally {
            return null;
        }
    }


    /**
     * 根据证书序号拿用户名
     */
    public String getUserNameAndWorkNo() throws Exception {
        //证书
        String certNo = request.getParameter(LoginReqKey.CERT_NO);
        //系统id
        long systemId = Long.parseLong(request.getParameter(LoginReqKey.SYSTEM_ID));
        try {
            //获取用户名和用户英文名
            Map<String, Object> retMap = loginDao.getUserNameAndWorkNo(certNo);
            //响应
            HttpResponser.rspToHttpWithMap(response, retMap, true);
        } catch (Exception e) {
            logger.error("远程调用错误|getUserNameAndWorkNo", e);
            HttpResponser.rspToHttp(response, false);
        } finally {
            return null;
        }
    }

    /**
     * 修改密码
     */
    public String changePassword() throws Exception {
        //用户id,旧密码，新密码
        int userId = Integer.parseInt(request.getParameter(LoginReqKey.USER_ID));
        String oldPassword = request.getParameter(LoginReqKey.PASSWORD);
        String newPassword = request.getParameter(LoginReqKey.NEW_PASSWORD);
        //系统id
        long systemId = Long.parseLong(request.getParameter(LoginReqKey.SYSTEM_ID));

        try {
            //返回map
            Map<String, Object> retMap = loginDao.changePassword(oldPassword, newPassword, userId);
            //响应
            HttpResponser.rspToHttpWithMap(response, retMap, (Boolean) retMap.get(LoginRspKey.SUCCESS));
        } catch (Exception e) {
            logger.error("远程调用错误|changePassword", e);
            HttpResponser.rspToHttp(response, false);
        } finally {
            return null;
        }
    }

    /**
     * 查询url
     */
    public String getUrl() throws Exception {
        //页面id
        int pid = Integer.parseInt(request.getParameter(LoginReqKey.PID));
        //系统id
        long systemId = Long.parseLong(request.getParameter(LoginReqKey.SYSTEM_ID));

        try {
            String url = loginDao.getUrl(pid, systemId);
            boolean isSuccess = (url != null);

            Map<String, Object> retMap = new HashMap<String, Object>();
            retMap.put(LoginRspKey.PAGE_URL, url);
            //响应
            HttpResponser.rspToHttpWithMap(response, retMap, isSuccess);
        } catch (Exception e) {
            logger.error("远程调用错误|getUrl", e);
            HttpResponser.rspToHttp(response, false);
        } finally {
            return null;
        }
    }

    /**
     * 登出
     */
    public String logout() throws Exception {
        //证书
        String certNo = request.getParameter(LoginReqKey.CERT_NO);

        try {
            boolean isSuccess = loginDao.logout(certNo);
            //响应
            HttpResponser.rspToHttp(response, isSuccess);
        } catch (Exception e) {
            logger.error("远程调用错误|logout", e);
            HttpResponser.rspToHttp(response, false);
        } finally {
            return null;
        }
    }
}
