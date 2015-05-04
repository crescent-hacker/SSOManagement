package com.jetsun.bean.biz;

import com.jetsun.bean.common.SysProperty;
import com.jetsun.utility.property.SysPropertyUtil;

import java.io.FileInputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/9/26
 * Desc:证书bean
 */
public class CertBean {
    //数据库id
    private int ucId;
    //是否启用
    private int isValid;
    //证书别名
    private String certKey;
    //证书显示名称
    private String certName;
    //证书私钥密码
    private static final String privateKeyPwd = "jet-sun.private";
    //证书有效日期
    private String validDay = "3650";
    //证书序列号
    private String certNo;

    /**
     * 构造函数
     *
     * @param certKey  证书别名
     * @param certName 证书显示名称
     */
    public CertBean(String certKey, String certName) {
        this.certKey = certKey;
        this.certName = certName;
    }

    /**
     * 构造函数
     *
     * @param certKey  证书别名
     * @param certName 证书显示名称
     * @param validDay 生效日期
     */
    public CertBean(String certKey, String certName, String validDay) {
        this.certKey = certKey;
        this.certName = certName;
        this.validDay = validDay;
    }

    /**
     * 无参构造函数
     */
    public  CertBean(){}


    public String getCertKey() {
        return certKey;
    }

    public void setCertKey(String certKey) {
        this.certKey = certKey;
    }

    public String getCertName() {
        return certName;
    }

    public void setCertName(String certName) {
        this.certName = certName;
    }

    public String getPrivateKeyPwd() {
        return privateKeyPwd;
    }

    /**
     * p12证书库密码
     */
    public String getCertPwd() {
        return SysPropertyUtil.getProperty(SysProperty.P12_KEYSTORE_PWD);
    }

    public String getValidDay() {
        return validDay;
    }

    public void setValidDay(String validDay) {
        this.validDay = validDay;
    }

    public String getCertNo() {
        return this.certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    /**
     * 从证书文件中获取证书并设置证书号码
     */
    public void setCertNoFromFile() throws Exception {
        String fileName = this.getCertPath();
        CertificateFactory certificate_factory = CertificateFactory.getInstance("X.509");
        FileInputStream file_inputstream = new FileInputStream(fileName);
        X509Certificate x509certificate = (X509Certificate) certificate_factory.generateCertificate(file_inputstream);
        if (x509certificate != null) {
            this.certNo = x509certificate.getSerialNumber().toString();
        }
    }

    public int getUcId() {
        return ucId;
    }

    public void setUcId(int ucId) {
        this.ucId = ucId;
    }

    public int getIsValid() {
        return isValid;
    }

    public void setIsValid(int isValid) {
        this.isValid = isValid;
    }

    /**
     * 辅助方法-获得p12证书路径
     *
     * @return
     */
    public String getP12Path() {
        return SysPropertyUtil.getProperty(SysProperty.CERT_PATH) + certKey + ".p12";
    }

    /**
     * 辅助方法-获得cer证书路径
     *
     * @return
     */
    public String getCertPath() {
        return SysPropertyUtil.getProperty(SysProperty.CERT_PATH) + certKey + ".cer";
    }
}