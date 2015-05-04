package com.jetsun.action.right;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jetsun.action.BaseAction;
import com.jetsun.bean.biz.OperationBean;
import com.jetsun.dao.interfaces.OperationDao;
import com.jetsun.utility.HttpResponser;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/9/29
 * Desc:权限action
 */
public class OperationAction extends BaseAction {
    @Autowired
    private OperationDao operationDao;

    /**
     * 获得权限树
     */
    public String getTree() throws Exception {
        String systemId = request.getParameter("systemId");
        //调用dao
        List<Map<String, Object>> treeList = operationDao.getTree(systemId);
        //返回结果到ajax
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("treeList", treeList);
        HttpResponser.rspToHttpWithMap(response, map, true);
        return null;
    }

    /**
     * 增加权限
     */
    public String addOperation() throws Exception  {
        //接收参数
        String json = request.getParameter("addJson").replace("'","\"");
        //拼装bean
        OperationBean operationBean = new ObjectMapper().readValue(json,OperationBean.class);
        //调用dao
        boolean isSuccess = operationDao.addOperation(operationBean);
        //把插入记录的id返回页面，定位用
        returnMap.put("opId",operationBean.getOpId());
        //返回ajax
        HttpResponser.rspToHttpWithMap(response,returnMap,isSuccess);

        return null;
    }

    /**
     * 修改权限
     */
    public String modifyOperation() throws Exception  {
        //接收参数
        String json = request.getParameter("updateJson").replace("'","\"");
        //拼装bean
        OperationBean operationBean = new ObjectMapper().readValue(json,OperationBean.class);
        //调用dao
        boolean isSuccess = operationDao.modifyOperation(operationBean);
        //返回ajax
        HttpResponser.rspToHttp(response,isSuccess);

        return null;
    }

    /**
     * 删除权限
     */
    public String delOperation() throws Exception {
        //获取提交参数
        String opIds = request.getParameter("opIds");
        //调用dao
        boolean isSuccess = operationDao.delOperation(opIds);
        //返回ajax
        HttpResponser.rspToHttp(response,isSuccess);

        return null;

    }

    /**
     * 根据权限id获得权限详情
     */
    public String getOperationById() throws Exception {
        //获取提交参数
        int opId = Integer.parseInt(request.getParameter("opId"));
        //调用dao
        OperationBean operationBean = operationDao.getOperationById(opId);
        //返回ajax
        HttpResponser.rspToHttpWithObj(response,operationBean);

        return null;
    }

}
