package com.jetsun.action.operator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jetsun.action.BaseAction;
import com.jetsun.bean.biz.CertBean;
import com.jetsun.bean.biz.UserBean;
import com.jetsun.dao.impl.CommonDao;
import com.jetsun.dao.interfaces.CertDao;
import com.jetsun.dao.interfaces.OperatorDao;
import com.jetsun.dao.interfaces.RoleDao;
import com.jetsun.utility.CertGenerator;
import com.jetsun.utility.HttpResponser;
import com.jetsun.utility.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OperatorAction extends BaseAction {
    @Autowired
    private OperatorDao operatorDao;
    @Autowired
    private CommonDao commonDao;
    @Autowired
    private CertDao certDao;
    @Autowired
    private RoleDao roleDao;

    /**
     * 查看用户列表（带搜索）
     */
    public String getUserList() throws Exception {
        String isValid = request.getParameter("isValid");
        String userName = request.getParameter("searchKey");
        //调用dao
        List<List<String>> userList = operatorDao.getUserList(isValid, userName);
        //返回结果到ajax
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userList", userList);
        HttpResponser.rspToHttpWithMapAndPagin(response, map, event, true);
        return null;
    }

    /**
     * 设置用户状态
     */
    public String setUserStatus() throws Exception {
        boolean isValid = request.getParameter("isValid").equals("1");//是否启用
        int userId = Integer.parseInt(request.getParameter("userId"));//用户id
        //调用dao
        boolean isSuccess = operatorDao.setUserStatus(isValid, userId);
        //返回结果
        HttpResponser.rspToHttp(response, isSuccess);
        return null;
    }

    /**
     * 获得医院和地市列表
     */
    public String getCommonList() throws Exception {
        //返回结果到ajax
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("areaList", commonDao.getAreaList());
        map.put("hsptList", commonDao.getHospitalList());
        HttpResponser.rspToHttpWithMapAndPagin(response, map, event, true);
        return null;
    }

    /**
     * 添加用户
     */
    public String addUser() throws Exception {
        //接收参数
        String json = request.getParameter("addJson").replace("'","\"");
        String roles = request.getParameter("roles");
        String ukeyNo = request.getParameter("ukeyNo");
        try{
        //拼装bean
        UserBean userBean = new ObjectMapper().readValue(json,UserBean.class);
        //调用dao插入用户
        boolean isSuccess = operatorDao.addUser(userBean);
        //绑定角色
        roleDao.bindUserAndRoles(userBean.getUserId(),roles);
        //证书处理
        if(userBean.getIsAddCert()==1){//按用户信息新建证书
            CertBean certBean = new CertBean(userBean.getOperNo(),userBean.getOperName());
            //生成证书
            CertGenerator certGenerator = new CertGenerator();
            certGenerator.genCertificate(certBean);
            //从生成的证书文件中获得序列号
            certBean.setCertNoFromFile();
            //创建证书
            boolean isCertSuccess = certDao.addCert(userBean.getUserId(),ukeyNo,certBean);
            logger.debug("新建用户创建证书结果~"+isCertSuccess);
        }else{//根据已有证书进行绑定
            boolean isCertSuccess = certDao.createRelation(userBean.getUserId(),userBean.getCertName());
            logger.debug("新建用户绑定证书结果~"+isCertSuccess);
        }
        //返回结果
        HttpResponser.rspToHttp(response, isSuccess);
        } catch (Exception e) {
            HttpResponser.rspToHttp(response,false);
        }
        return null;
    }

    /**
     * 修改用户
     */
    public String modifyUser() throws Exception {
        //接收参数
        String json = request.getParameter("updateJson").replace("'","\"");
        String roles = request.getParameter("roles");
        int resetPwd = Integer.parseInt(request.getParameter("resetPwd"));
        //拼装bean
        UserBean userBean = new ObjectMapper().readValue(json,UserBean.class);
        //调用dao插入用户
        boolean isSuccess = operatorDao.modifyUser(userBean,resetPwd==1);
        //绑定角色
        roleDao.bindUserAndRoles(userBean.getUserId(),roles);
        //证书处理
        if(StringUtil.isNotEmpty(userBean.getCertName())){//根据已有证书进行绑定
            boolean isCertSuccess = certDao.createRelation(userBean.getUserId(),userBean.getCertName());
            logger.debug("用户绑定证书结果~"+isCertSuccess);
        }

        //返回结果
        HttpResponser.rspToHttp(response,isSuccess);
        return null;
    }

    /**
     * 根据用户id获得角色
     */
    public String getRolesByUserid() throws Exception{
        //用户id
        String userId = request.getParameter("userId");
        //返回结果到ajax
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("roleList", roleDao.getRolesByUserid(userId));
        HttpResponser.rspToHttpWithMap(response, map, true);
        return null;
    }
}
