package com.jetsun.action.certificate;

import com.jetsun.action.BaseAction;
import com.jetsun.bean.biz.CertBean;
import com.jetsun.bean.common.SysProperty;
import com.jetsun.dao.interfaces.CertDao;
import com.jetsun.utility.CertGenerator;
import com.jetsun.utility.DownloadUtil;
import com.jetsun.utility.HttpResponser;
import com.jetsun.utility.property.SysPropertyUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/9/29
 * Desc:证书action
 */
public class CertificateAction extends BaseAction {
    @Autowired
    private CertDao certDao;

    /**
     * 获得证书列表
     */
    public String getCertList() throws Exception {
        String isValid = request.getParameter("isValid");
        String certName = request.getParameter("searchKey");
        //调用dao
        List<List<String>> certList = certDao.getCertList(isValid, certName);
        //返回结果到ajax
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("certList", certList);
        HttpResponser.rspToHttpWithMapAndPagin(response, map, event, true);
        return null;
    }

    /**
     * 获得未绑定用户的证书列表
     */
    public String getUnbindCertList() throws Exception {
        String certName = request.getParameter("certName");
        //调用dao
        List<String> certList = certDao.getUnbindCertList(certName);
        //返回结果到ajax
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("certList", certList);
        HttpResponser.rspToHttpWithMap(response, map, true);
        return null;
    }

    /**
     * 增加一个证书
     */
    public String addCert() throws Exception {
        //获取提交参数
        int userId = Integer.parseInt(request.getParameter("userId"));//用户id
        String ukeyNo = request.getParameter("ukeyNo");//usbkey序号
        String certKey = request.getParameter("certKey");//证书别名
        String certName = request.getParameter("certName");//证书显示名称
        String validDay = request.getParameter("validDay");//证书有效期
        //组装bean
        CertBean certBean = new CertBean(certKey, certName, validDay);//组装bean
        //生成证书
        CertGenerator certGenerator = new CertGenerator();
        certGenerator.genCertificate(certBean);
        //从生成的证书文件中获得序列号
        certBean.setCertNoFromFile();
        //调用dao
        boolean isSuccess = certDao.addCert(userId, ukeyNo, certBean);
        //返回结果
        HttpResponser.rspToHttp(response, isSuccess);
        return null;
    }

    /**
     * 设置证书状态
     */
    public String setCertStatus() throws Exception {
        boolean isValid = request.getParameter("isValid").equals("1");//是否启用
        int ucId = Integer.parseInt(request.getParameter("ucId"));//证书id
        //调用dao
        boolean isSuccess = certDao.setCertStatus(isValid, ucId);
        //返回结果
        HttpResponser.rspToHttp(response, isSuccess);
        return null;
    }

    /**
     * 解绑证书
     */
    public String cancelRelation() throws Exception {
        int userId = Integer.parseInt(request.getParameter("userId"));//用户id
        //调用dao
        boolean isSuccess = certDao.cancelRelation(userId);
        //返回结果
        HttpResponser.rspToHttp(response, isSuccess);
        return null;
    }

    /**
     * 绑定证书
     */
    public String createRelation() throws Exception {
        int userId = Integer.parseInt(request.getParameter("userId"));//用户id
        String certName = request.getParameter("certName");//证书id
        //调用dao
        boolean isSuccess = certDao.createRelation(userId,certName);
        //返回结果
        HttpResponser.rspToHttp(response, isSuccess);
        return null;
    }

    /**
     * 下载客户端证书
     */
    public String downloadCert() throws Exception{
        //获得文件名
        int ucId = Integer.parseInt(request.getParameter("ucId"));
        //根据ucId获得文件路径
        CertBean certBean = certDao.getCertById(ucId);
        //拼装全路径
        File file = new File(certBean.getP12Path());
        //下载
        if (file.exists()) {
            //进行下载
            DownloadUtil.download(response, certBean.getP12Path(), certBean.getCertName()+".p12");
        }else {
            response.getWriter().print("<div style='font-size:25px;position: absolute;top: 40%;left: 23%;'>The request resource no longer exists, please contact the administrator.</div>");
        }
        return null;
    }

    /**
     * 下载服务器证书
     */
    public String downloadServerCert() throws Exception{
        //拼装全路径
        String path = SysPropertyUtil.getProperty(SysProperty.SERVER_CERT_ZIP);
        File file = new File(path);
        //下载
        if (file.exists()) {
            //进行下载
            DownloadUtil.download(response, path, "证书安装包.zip");
        }else {
            response.getWriter().print("<div style='font-size:25px;position: absolute;top: 40%;left: 23%;'>The request resource no longer exists, please contact the administrator.</div>");
        }
        return null;
    }
}
