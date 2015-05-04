package com.jetsun.remotelogin.dao.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jetsun.remotelogin.bean.AppKey;
import com.jetsun.remotelogin.bean.LoginReqKey;
import com.jetsun.remotelogin.bean.LoginRspKey;
import com.jetsun.remotelogin.bean.RemoteLoginAPI;
import com.jetsun.remotelogin.dao.interfaces.LoginHandler;
import com.jetsun.remotelogin.utility.HttpRequester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/8/14
 * Desc:登陆dao
 */
public class LoginHandlerImpl implements LoginHandler {
    /**
     * 日志记录器
     */
    private Logger logger = LoggerFactory.getLogger(com.jetsun.remotelogin.dao.impl.LoginHandlerImpl.class);

    /**
     * 证书是否存在
     * @param certNo
     * @return
     */
    @Override
    public boolean isCertExist(String certNo,Object session) {
        //远程调用的api
        String api = RemoteLoginAPI.isCertExistAPI();
        //组装参数
        Map<String, String> inputMap = new HashMap<String, String>();
        inputMap.put(LoginReqKey.CERT_NO, certNo);
        inputMap.put(LoginReqKey.SYSTEM_ID, "" + AppKey.APP_KEY);
        Map<String, Object> outputMap;
        boolean isExist = false;
        try {
            //远程调用
            String rJson = HttpRequester.getInstance().httpPost(api, inputMap,session);
            outputMap = new ObjectMapper().readValue(rJson, Map.class);
            isExist = (Boolean) outputMap.get(LoginRspKey.SUCCESS);
        }catch (Exception e){
            logger.error("验证证书异常,certNo="+certNo,e);        }
        return isExist;
    }

    /**
     * 登陆并获取权限
     */
    @Override
    public Map<String, Object> doLogin(String certNo, String password, String ip,Object session)  {
        //远程调用的api
        String api = RemoteLoginAPI.doLoginAPI();
        //组装参数
        Map<String, String> inputMap = new HashMap<String, String>();
        inputMap.put(LoginReqKey.CERT_NO, certNo);
        inputMap.put(LoginReqKey.PASSWORD, password);
        inputMap.put(LoginReqKey.IP_ADDRESS, ip);
        inputMap.put(LoginReqKey.SYSTEM_ID, "" + AppKey.APP_KEY);

        Map<String, Object> outputMap = null;
        try {
            //远程调用
            String rJson = HttpRequester.getInstance().httpPost(api, inputMap,session);
            outputMap = new ObjectMapper().readValue(rJson, Map.class);
        }catch (Exception e){
            logger.error("登陆异常,certNo="+certNo+",ip="+ip,e);
        }
        return outputMap;
    }

    /**
     * SSO登陆并获取权限
     */
    @Override
    public Map<String, Object> doLoginSSO(String certNo, String ip,Object session)  {
        //远程调用的api
        String api = RemoteLoginAPI.doLoginSSOAPI();
        //组装参数
        Map<String, String> inputMap = new HashMap<String, String>();
        inputMap.put(LoginReqKey.CERT_NO, certNo);
        inputMap.put(LoginReqKey.IP_ADDRESS, ip);
        inputMap.put(LoginReqKey.SYSTEM_ID, "" + AppKey.APP_KEY);

        Map<String, Object> outputMap = null;
        try {
            //远程调用
            String rJson = HttpRequester.getInstance().httpPost(api, inputMap,session);
            outputMap = new ObjectMapper().readValue(rJson, Map.class);
        }catch (Exception e){
            logger.error("SSO登陆异常,certNo="+certNo+",ip="+ip,e);
        }
        return outputMap;
    }

    /**
     * 检查权限
     */
    @Override
    public Map<String, Object> checkUserAuthority(String userId, String loginCode, String checkType, String operationKey, String optDetails, String ip,String currentPage,Object session) {
        //远程调用的api
        String api = RemoteLoginAPI.checkUserAuthorityAPI();
        //组装参数
        Map<String, String> inputMap = new HashMap<String, String>();
        inputMap.put(LoginReqKey.USER_ID, userId);
        inputMap.put(LoginReqKey.LOGIN_CODE, loginCode);
        inputMap.put(LoginReqKey.CHECK_TYPE, checkType);
        inputMap.put(LoginReqKey.OPERATION_KEY, operationKey);
        inputMap.put(LoginReqKey.OPT_DETAILS, optDetails);
        inputMap.put(LoginReqKey.IP_ADDRESS, ip);
        inputMap.put(LoginReqKey.CURRENT_PAGE, currentPage);
        inputMap.put(LoginReqKey.SYSTEM_ID, "" + AppKey.APP_KEY);

        Map<String, Object> outputMap = null;
        try {
            //远程调用
            String rJson = HttpRequester.getInstance().httpPost(api, inputMap,session);
            outputMap = new ObjectMapper().readValue(rJson, Map.class);
        }catch (Exception e){
            logger.error("检查权限异常,userId|loginCode|checkType|operationKey|ip|currentJsp="+userId+"|"+loginCode+"|"+checkType+"|"+operationKey+"|"+ip+"|"+currentPage,e);
        }
        return outputMap;
    }

    /**
     * 根据证书序号拿用户名
     */
    @Override
    public Map<String, Object> getUserNameAndWorkNo(String certNo,Object session){
        //远程调用的api
        String api = RemoteLoginAPI.getUserNameAndWorkNoAPI();
        //组装参数
        Map<String, String> inputMap = new HashMap<String, String>();
        inputMap.put(LoginReqKey.CERT_NO, certNo);
        inputMap.put(LoginReqKey.SYSTEM_ID, "" + AppKey.APP_KEY);

        Map<String, Object> outputMap = null;
        try {
            //远程调用
            String rJson = HttpRequester.getInstance().httpPost(api, inputMap,session);
            outputMap = new ObjectMapper().readValue(rJson, Map.class);
        }catch (Exception e){
            logger.error("获得用户名异常,certNo="+certNo,e);
        }
        return outputMap;
    }

    /**
     * 修改密码
     */
    @Override
    public Map<String, Object> changePassword(String oldPassword, String newPassword, int userId,Object session) {
        //远程调用的api
        String api = RemoteLoginAPI.changePasswordAPI();
        //组装参数
        Map<String, String> inputMap = new HashMap<String, String>();
        inputMap.put(LoginReqKey.PASSWORD, oldPassword);
        inputMap.put(LoginReqKey.NEW_PASSWORD, newPassword);
        inputMap.put(LoginReqKey.USER_ID, "" + userId);
        inputMap.put(LoginReqKey.SYSTEM_ID, "" + AppKey.APP_KEY);

        Map<String, Object> outputMap = null;
        try {
            //远程调用
            String rJson = HttpRequester.getInstance().httpPost(api, inputMap,session);
            outputMap = new ObjectMapper().readValue(rJson, Map.class);
        }catch (Exception e){
            logger.error("密码更改异常,userId="+userId,e);
        }
        return outputMap;
    }

    /**
     * 根据pid获得页面
     */
    @Override
    public String getUrl(String pid,Object session) {
        //远程调用的api
        String api = RemoteLoginAPI.getUrlAPI();
        //组装参数
        Map<String, String> inputMap = new HashMap<String, String>();
        inputMap.put(LoginReqKey.PID, pid);
        inputMap.put(LoginReqKey.SYSTEM_ID, "" + AppKey.APP_KEY);
        //返回url
        String url = null;
        try {
            //远程调用
            String rJson = HttpRequester.getInstance().httpPost(api, inputMap,session);
            //解析返回信息
            Map<String, Object> outputMap = new ObjectMapper().readValue(rJson, Map.class);
            if ((Boolean) outputMap.get(LoginRspKey.SUCCESS)) {
                url = (String) outputMap.get(LoginRspKey.PAGE_URL);
                logger.debug("获取url成功，url=" + url);
            }
        } catch (Exception e) {
            logger.error("getUrl失败,pid=" + pid, e);
        }
        return url;
    }

    /**
     * 登出
     */
    @Override
    public boolean logout(String certNo,Object session){
        boolean isSuccess = false;
        //远程调用的api
        String api = RemoteLoginAPI.logoutAPI();
        //组装参数
        Map<String, String> inputMap = new HashMap<String, String>();
        inputMap.put(LoginReqKey.CERT_NO, certNo);
        inputMap.put(LoginReqKey.SYSTEM_ID, "" + AppKey.APP_KEY);
        try {
            //远程调用
            String rJson = HttpRequester.getInstance().httpPost(api, inputMap,session);
            //解析返回信息
            Map<String, Object> outputMap = new ObjectMapper().readValue(rJson, Map.class);
            if ((Boolean) outputMap.get(LoginRspKey.SUCCESS)) {
                logger.debug("logout success" );
                isSuccess = true;
            }
        } catch (Exception e) {
            logger.error("logout failed", e);
        }
        return isSuccess;
    }
}