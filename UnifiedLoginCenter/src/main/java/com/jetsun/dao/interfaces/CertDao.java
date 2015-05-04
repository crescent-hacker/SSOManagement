package com.jetsun.dao.interfaces;

import com.jetsun.bean.biz.CertBean;

import java.util.List;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/8/14
 * Desc:证书dao
 */
public interface CertDao {
    /**
     * 获取证书列表
     * @param isValid 是否有效 0-无效 1-有效 空字符串-全部
     * @param certName 证书名 空字符串-全部
     * @return 证书列表
     */
    List<List<String>> getCertList(String isValid,String certName);
    
    /**未绑定的证书**/
    List<String> getUnbindCertList(String certName);

    /**
     * 增加一个证书
     * @param userId 如果为0则未绑定用户，clzt为默认为1
     * @param ukeyNo ie下才能读取，由页面读取
     * @param certBean 内含certNo，certPath，certName
     */
    boolean addCert(int userId,String ukeyNo,CertBean certBean);

    /**
     * 设置证书为无效，设置的同时解除与用户的绑定
     */
    boolean setCertStatus(boolean isValid,int ucId);

    /**
     * 解绑证书
     */
    boolean cancelRelation(int userId);

    /**
     * 绑定证书
     */
    boolean createRelation(int userId,String certName);

    /**
     * 根据ucId获取证书
     */
    CertBean getCertById(int ucId);
}
