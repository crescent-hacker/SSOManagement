package com.jetsun.dao.impl;


import com.jetsun.bean.biz.CertBean;
import com.jetsun.bean.common.CommonVariable;
import com.jetsun.dao.interfaces.BaseDao;
import com.jetsun.dao.interfaces.CertDao;
import com.jetsun.utility.StringUtil;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/8/14
 * Desc:登陆dao
 */
@Transactional(rollbackForClassName = {"RuntimeException", "Exception"})
public class CertDaoImpl extends BaseDao implements CertDao {

    /**
     * 获取证书列表
     *
     * @param isValid  是否有效 0-无效 1-有效 空字符串-全部
     * @param certName 证书名 空字符串-全部
     * @return 证书列表
     */
    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public List<List<String>> getCertList(String isValid, String certName) {
        String sql = "SELECT UC_ID,CERT_KEY,CERT_NAME,IS_VALID,CERT_PATH,UKEY_NO,OPER_NAME " +
                "FROM USERCERTIFICATE T1 LEFT JOIN OPERATOR T2 ON T1.USERID = T2.USERID " +
                "WHERE IS_VALID LIKE ? AND CERT_NAME LIKE ? AND T1.USERID >= 0 ORDER BY OPER_NAME ASC";
        //参数数组
        Object[] objects = new Object[]{StringUtil.wrapLike(isValid), StringUtil.wrapLike(certName)};
        //分页sql
        String paginSql = this.getPaginStr(sql, objects);
        List<List<String>> certList = jdbcTemplate.query(paginSql, objects, new CertListMapper());

        return certList;
    }

    /**
     * 获得未绑定用户的证书
     * @return 证书列表
     */
    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public List<String> getUnbindCertList(String certName) {
        String sql = "SELECT CERT_NAME FROM USERCERTIFICATE where USERID =0 and IS_VALID=1 and CERT_NAME LIKE ?";
        //分页sql
        List<String> certList = jdbcTemplate.queryForList(sql,new Object[]{StringUtil.wrapLike(certName)},String.class);

        return certList;
    }

    /**
     * 增加一个证书,增加完马上提供下载
     *
     * @param userId   如果为0则未绑定用户，IS_VALID为默认为1
     * @param ukeyNo   ie下才能读取，由页面读取
     * @param certBean 内含certKey,certName,validDay
     */
    @Override
    public boolean addCert(int userId, String ukeyNo, CertBean certBean) {
        String sql = "INSERT INTO USERCERTIFICATE(UC_ID,USERID,UKEY_NO,CERT_NO,CERT_PATH,IS_VALID,CERT_NAME,CERT_KEY)" +
                " VALUES(SEQ_USERCERTIFICATE.NEXTVAL,?,?,?,?,?,?,?)";
        Object[] objects = new Object[]{userId, ukeyNo, certBean.getCertNo(), certBean.getP12Path(), "1", certBean.getCertName(), certBean.getCertKey()};
        //执行插入
        int row = jdbcTemplate.update(sql, objects);
        //插入状态
        return row > 0;
    }

    /**
     * 设置证书是否启用（设置不启用同时解除与用户的绑定）
     */
    @Override
    public boolean setCertStatus(boolean isValid, int ucId) {
        String sql;
        if (isValid) {
            sql = "UPDATE USERCERTIFICATE SET IS_VALID = '1' WHERE UC_ID = ?";
        } else {
            sql = "UPDATE USERCERTIFICATE SET IS_VALID = '0', USERID = 0 WHERE UC_ID = ?";
        }
        //执行更新
        int row = jdbcTemplate.update(sql, new Object[]{ucId});
        //更新状态
        return row > 0;
    }

    /**
     * 解绑证书
     */
    @Override
    public boolean cancelRelation(int userId) {
        String sql = "UPDATE USERCERTIFICATE SET USERID = 0 WHERE USERID = ?";
        //执行更新
        int row = jdbcTemplate.update(sql, new Object[]{userId});
        //更新状态
        return row > 0;
    }

    /**
     * 绑定证书
     */
    @Override
    public boolean createRelation(int userId, String certName) {
        String sql = "UPDATE USERCERTIFICATE SET USERID = ? WHERE CERT_NAME = ? AND USERID = 0";
        //执行更新
        int row = jdbcTemplate.update(sql, new Object[]{userId, certName});
        //更新状态
        return row > 0;
    }

    /**
     * 根据ucId获取证书全路径
     */
    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public CertBean getCertById(int ucId) {
        String sql = "SELECT * FROM USERCERTIFICATE WHERE UC_ID = ?";
        //证书路径
        CertBean certBean = jdbcTemplate.queryForObject(sql, new Object[]{ucId},new CertMapper());
        //返回证书路径
        return certBean;
    }

    /**
     * 辅助类-把返回的List<Map<String,Object>>转成List<List<String>>
     */
    private class CertListMapper implements RowMapper<List<String>> {

        @Override
        public List<String> mapRow(ResultSet rs, int rowNum) throws SQLException {
            List<String> list = new ArrayList<String>();
//            UC_ID,CERT_KEY,CERT_NAME,IS_VALID,CERT_PATH,UKEY_NO,OPER_NAME
//            证书别名,证书名,证书状态,证书路径,UKEY序列号,已绑定用户,操作
            String uc_id = rs.getString("UC_ID");
            String cert_key = rs.getString("CERT_KEY");
            String cert_name = rs.getString("CERT_NAME");
            int is_valid = rs.getInt("IS_VALID");
            String cert_path = rs.getString("CERT_PATH");
            String ukey_no = rs.getString("UKEY_NO");
            String oper_name = rs.getString("OPER_NAME");
            String operation_1 = "<img src='" + CommonVariable.WEB_PATH + "/static/images/"+(is_valid==1?"btn_A_Reject.png":"btn_A_Assent.png")+"' style='width:16px;vertical-align: middle;cursor: pointer;' " +
                    "onclick='setStatus(\"" + (is_valid==1?0:1) + "\",\"" + uc_id + "\")' title='"+(is_valid==1?"禁用":"启用")+"' />";
            String operation_2 = "<a href='"+CommonVariable.WEB_PATH+"/certificate/certificateAction!downloadCert.action?ucId="+uc_id+"' target='_blank' title='下载证书'><img src='" + CommonVariable.WEB_PATH + "/static/images/btn_download.png' style='margin-left:20px;width:16px;vertical-align: middle;cursor: pointer;' /></a>";
            //加入列表
            list.add(cert_key);
            list.add(cert_name);
            list.add(is_valid == 1? "启用" : "禁用");
            list.add(cert_path);
            list.add(ukey_no);
            list.add(oper_name);
            list.add(operation_1+operation_2);
            return list;
        }
    }


    /**
     * 辅助类-把返回的Map<String,Object>转成CertBean
     */
    private class CertMapper implements RowMapper<CertBean> {

        @Override
        public CertBean mapRow(ResultSet rs, int rowNum) throws SQLException {
            CertBean certBean = new CertBean();
            //证书别名,证书名,证书状态,证书路径,UKEY序列号,已绑定用户,操作
            int uc_id = rs.getInt("UC_ID");
            String cert_key = rs.getString("CERT_KEY");
            String cert_name = rs.getString("CERT_NAME");
            int is_valid = rs.getInt("IS_VALID");
            String cert_no = rs.getString("CERT_NO");
            //组装bean
            certBean.setUcId(uc_id);
            certBean.setCertKey(cert_key);
            certBean.setCertName(cert_name);
            certBean.setIsValid(is_valid);
            certBean.setCertNo(cert_no);
            //返回bean
            return certBean;
        }
    }

}