package com.jetsun.dao.interfaces;

import java.util.Map;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/8/14
 * Desc:登陆dao
 */
public interface LoginDao {
    /**
     * 查询证书是否存在
     */
    boolean isCertExist(String certNo);

    /**
     * 登陆并获取权限
     */
    Map<String, Object> doLogin(String certNo, String password, String ip, long systemId) throws Exception;

    /**
     * SSO登陆并获取权限
     */
    Map<String, Object> doLoginSSO(String certNo, String ip, long systemId);

    /**
     * 检查权限
     */
    Map<String, Object> checkUserAuthority(String userId, String loginCode, String checkType, String operationKey, String optDetails, String ip,String currentPage,long systemId) throws Exception;

    /**
     * 根据证书序号拿用户名
     */
    Map<String, Object> getUserNameAndWorkNo(String certNo);

    /**
     * 修改密码
     */
    Map<String, Object> changePassword(String oldPassword, String newPassword, int userId);

    /**
     * 根据pid获得页面
     */
    String getUrl(int pid, long systemId);

    /**
     * 系统id是否正确
     */
    boolean isSystemExist(long systemId);

    /**
     * 远程登出，不再享有SSO
     */
    boolean logout(String certNo);
}
