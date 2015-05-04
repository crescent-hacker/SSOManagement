package com.jetsun.dao.impl;


import com.jetsun.bean.common.LoginRspKey;
import com.jetsun.dao.interfaces.BaseDao;
import com.jetsun.dao.interfaces.LoginDao;
import com.jetsun.utility.procedureUtil.ProcedureUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/8/14
 * Desc:登陆dao
 */
public class LoginDaoImpl extends BaseDao implements LoginDao {
    /**
     * 证书是否存在
     *
     * @param certNo
     * @return
     */
    @Override
    public boolean isCertExist(String certNo) {
        String sql = "SELECT COUNT(1) FROM OPERATOR T1,USERCERTIFICATE T2 WHERE T1.USERID = T2.USERID AND T1.OPER_STATE = '1' AND T2.IS_VALID = '1' AND T2.CERT_NO = ?";
        int count = jdbcTemplate.queryForObject(sql, new Object[]{certNo}, Integer.class);
        return count > 0;
    }

    /**
     * 登陆并获取权限
     */
    @Override
    @Transactional(rollbackForClassName = {"RuntimeException", "Exception"})
    public Map<String, Object> doLogin(String certNo, String password, String ip, long systemId) throws Exception {
        return ProcedureUtil.execProcRetMap(this.jdbcTemplate, "DO_LOGIN_CERT", new Object[]{certNo, password, ip, systemId}, new String[]{LoginRspKey.O_STRS_JSON, LoginRspKey.O_ERRORCODE, LoginRspKey.O_ERRORMSG});
    }

    /**
     * SSO登陆并获取权限
     */
    @Override
    public Map<String, Object> doLoginSSO(String certNo, String ip, long systemId) {
        Map<String, Object> retMap = null;
        try {
            retMap = ProcedureUtil.execProcRetMap(this.jdbcTemplate, "DO_LOGIN_SSO", new Object[]{certNo, ip, systemId}, new String[]{LoginRspKey.O_STRS_JSON, LoginRspKey.O_ERRORCODE, LoginRspKey.O_ERRORMSG});
        } catch (Exception e) {
            logger.error("SSO登陆出现异常", e);
        } finally {
            return retMap;
        }
    }

    /**
     * 检查权限
     */
    @Override
    @Transactional(rollbackForClassName = {"RuntimeException", "Exception"})
    public Map<String, Object> checkUserAuthority(String userId, String loginCode, String checkType, String operationKey, String optDetails, String ip,String currentPage,long systemId) throws Exception {
        //in参数列表 检查类型 1：页面名称，2：action名称
        Object[] objects = new Object[]{userId, loginCode, checkType, operationKey, optDetails, ip,currentPage,systemId};
        return ProcedureUtil.execProcRetMap(this.jdbcTemplate, "DO_CHECK_USER_AUTHORITY", objects, new String[]{LoginRspKey.O_ERRORCODE, LoginRspKey.O_ERRORMSG});
    }

    /**
     * 根据证书序号拿用户名
     */
    @Override
    public Map<String, Object> getUserNameAndWorkNo(String certNo) {
        String sql = " SELECT T1.OPER_NAME,T1.OPER_NO FROM OPERATOR T1,USERCERTIFICATE T2 WHERE T1.USERID = T2.USERID AND T1.OPER_STATE='1' AND T2.IS_VALID = '1' AND T2.CERT_NO = ?";

        Map<String, Object> retMap = null;
        try {
            retMap = jdbcTemplate.queryForMap(sql, new Object[]{certNo});
        } catch (Exception e) {
            logger.error("获取用户名和用户编码错误", e);
        } finally {
            return retMap;
        }
    }

    /**
     * 修改密码
     */
    @Override
    @Transactional(rollbackForClassName = {"RuntimeException", "Exception"})
    public Map<String, Object> changePassword(String oldPassword, String newPassword, int userId) {
        //修改结果信息，修改结果代码
        String message;
        boolean isSuccess = false;
        Map<String, Object> retMap = new HashMap<String, Object>();

        //判断旧密码是否正确
        String sql = "SELECT COUNT(1) FROM OPERATOR WHERE OPER_PWD = ? AND USERID = ?";
        int count = jdbcTemplate.queryForObject(sql, new Object[]{oldPassword, userId}, Integer.class);
        boolean isOldPwdRight = count > 0;

        //进行密码修改
        if (isOldPwdRight) {
            String sql_update = "UPDATE OPERATOR SET OPER_PWD= ?,OPER_LASTTIME=SYSDATE WHERE USERID=?";
            int rows = jdbcTemplate.update(sql_update, new Object[]{newPassword, userId});
            //修改是否成功
            isSuccess = rows > 0;
            message = isSuccess ? "密码修改成功,请重新登陆。" : "密码修改失败，请稍后再试。";
        } else {
            message = "原始密码错误，请重新输入。";
        }
        //装入返回map
        retMap.put(LoginRspKey.SUCCESS, isSuccess);
        retMap.put(LoginRspKey.MESSAGE, message);

        return retMap;
    }

    /**
     * 根据pid获得页面
     */
    @Override
    public String getUrl(int pid, long systemId) {
        String sql = "SELECT T1.OP_ACTION FROM OPERATION T1,ROLESYSTEM T2 WHERE T1.RS_ID = T2.RS_ID AND T1.OP_ID = ? AND T2.RS_ID = ? AND T2.RS_STATE = '1' AND T1.OP_TYPE = 1 ";
        String url = null;
        try {
            url = jdbcTemplate.queryForObject(sql, new Object[]{pid, systemId}, String.class);
        } catch (DataAccessException e) {
            logger.debug("找不到pid为" + pid + "的页面", e);
        } finally {
            return url;
        }
    }

    /**
     * 系统id是否正确
     */
    @Override
    public boolean isSystemExist(long systemId) {
        String sql = "SELECT COUNT(1) FROM ROLESYSTEM WHERE RS_ID = ?";
        int count = jdbcTemplate.queryForObject(sql, new Object[]{systemId}, Integer.class);
        return count > 0;
    }

    /**
     * 远程登出，不再享有SSO
     */
    @Override
    @Transactional(rollbackForClassName = {"RuntimeException", "Exception"})
    public boolean logout(String certNo) {
        String sql_id = "SELECT T1.USERID FROM OPERATOR T1,USERCERTIFICATE T2 WHERE T1.USERID = T2.USERID AND T2.CERT_NO = ?";
        int userId = jdbcTemplate.queryForObject(sql_id, new Object[]{certNo},Integer.class);
        String sql = "UPDATE LOGONUSER SET LU_TIME = NULL WHERE USERID = ?";
        int count = jdbcTemplate.update(sql, new Object[]{userId});
        return count > 0;
    }
}