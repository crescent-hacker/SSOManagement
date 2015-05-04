package com.jetsun.utility;

import com.jetsun.bean.biz.CertBean;
import com.jetsun.bean.common.SysProperty;
import com.jetsun.utility.property.SysPropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/9/26
 * Desc:证书生成器
 */
public class CertGenerator {
    /**
     * 日志记录器
     */
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    //信任库密码
    private final static String storePassword = SysPropertyUtil.getProperty(SysProperty.KEYSTORE_PWD);
    //信任库地址
    private final static String storePath = SysPropertyUtil.getProperty(SysProperty.TRUST_KEYSTORE_PATH);
    //证书地址
    private final static String certPath = SysPropertyUtil.getProperty(SysProperty.CERT_PATH);

    /**
     * 生成证书
     *
     * @param certBean 证书bean
     * @return 返回p12证书路径
     * @throws Exception
     */
    public String genCertificate(CertBean certBean) throws Exception {
        File p12 = new File(certPath + certBean.getCertKey() + ".p12");
        File cer = new File(certPath + certBean.getCertKey() + ".cer");
        if (p12.exists() || cer.exists()) {
            //如果证书文件存在则先删除后生成
            p12.renameTo(new File(certPath + certBean.getCertKey() + ".p12_bak_" + DateUtil.getTodayByFormat("yyyyMMddHHmmss")));
            cer.renameTo(new File(certPath + certBean.getCertKey() + ".cer_bak_" + DateUtil.getTodayByFormat("yyyyMMddHHmmss")));
            String cmd_del = "keytool -delete -alias " + certBean.getCertKey() + " -keystore " + storePath + " -storepass " + storePassword;
            execCmd(cmd_del);
        }
        //生成p12
        //keytool -genkey -v -alias admin -keyalg RSA -storetype PKCS12 -keystore F:\超级管理员.p12 -dname "CN=admin,OU=jetsun,L=gz,ST=gd,C=cn" -storepass 123456 -keypass 123456 -validity 365
        StringBuffer cmd = new StringBuffer();
        cmd.append("keytool -genkey -v -alias " + certBean.getCertKey() + " -keyalg RSA -keysize 1024 -validity " + certBean.getValidDay() + " -storetype PKCS12 ");
        cmd.append(" -keystore " + certPath + certBean.getCertKey() + ".p12 ");
        cmd.append(" -keypass " + certBean.getPrivateKeyPwd() + " -storepass " + certBean.getCertPwd());
        cmd.append(" -dname CN=" + certBean.getCertName() + ",OU=jetsun,O=cn,L=cn,ST=cn,C=cn");
        execCmd(cmd.toString());
        //导出cer
        //keytool -export -alias admin -keystore F:\超级管理员.p12 -storetype PKCS12 -storepass 123456 -rfc -file F:\超级管理员.cer
        cmd = new StringBuffer();
        cmd.append("keytool -export -alias " + certBean.getCertKey() + " -storetype PKCS12 ");
        cmd.append(" -keystore " + certPath + certBean.getCertKey() + ".p12 ");
        cmd.append(" -storepass " + certBean.getCertPwd());
        cmd.append(" -rfc -file " + certPath + certBean.getCertKey() + ".cer");
        execCmd(cmd.toString());
        //把cer导入信任库
        //keytool -import -v -alias admin -file F:\超级管理员.cer -keystore F:\servertrust.keystore -storepass 123456
        cmd = new StringBuffer();
        cmd.append("keytool -import -v -alias " + certBean.getCertKey());
        cmd.append(" -file " + certPath + certBean.getCertKey() + ".cer");
        cmd.append(" -keystore " + storePath);
        cmd.append(" -storepass " + storePassword);
        cmd.append(" -noprompt");
        execCmd(cmd.toString());

        return certPath + certBean.getCertKey() + ".p12";
    }

    /**
     * 执行系统命令
     *
     * @param command 命令语句
     */
    private void execCmd(String command) {
        logger.debug(command);
        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            process = runtime.exec(command);
            BufferedReader br = new BufferedReader(new InputStreamReader(process
                    .getInputStream()));
            String inline;
            while ((inline = br.readLine()) != null) {
                logger.debug(inline);
            }
            br.close();
        } catch (IOException e) {
            logger.error("执行命令失败，command=" + command);
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        //storePath = "/servertrust.keystore";
        //certPath = "/";
        CertBean certBean = new CertBean("testJetsun", "健迅测试", "3650");
        CertGenerator certGenerator = new CertGenerator();
        String cert = certGenerator.genCertificate(certBean);
        System.out.println(cert);
    }
}