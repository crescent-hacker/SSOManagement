package com.jetsun.remotelogin.dao.interfaces;

import java.util.Map;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/8/14
 * Desc:登陆dao
 */
public interface LoginHandler {
    /**
     * 查询证书是否存在
     */
    public boolean isCertExist(String certNo,Object session) ;

    /**
     * 登陆并获取权限
     */
    public Map<String, Object> doLogin(String certNo, String password, String ip,Object session);

    /**
     * SSO登陆并获取权限
     */
    public Map<String, Object> doLoginSSO(String certNo, String ip,Object session) ;

    /**
     * 检查权限
     */
    Map<String, Object> checkUserAuthority(String strUserid, String strlogincode, String stype, String strOperationKey, String strOpt_details, String ip, String currentPage,Object session) ;

    /**
     * 根据证书序号拿用户名
     */
    Map<String, Object> getUserNameAndWorkNo(String certNo,Object session);

    /**
     * 修改密码
     */
    Map<String, Object> changePassword(String oldPassword, String newPassword, int userId,Object session) ;

    /**
     * 根据pid获得页面
     */
    String getUrl(String pid,Object session);

    /**
     * 登出
     */
    boolean logout(String certNo,Object session);

}
