package com.jetsun.dao.interfaces;

import com.jetsun.bean.biz.OperationBean;

import java.util.List;
import java.util.Map;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/9/24
 * Desc:
 */
public interface OperationDao {
    /**
     * 获得权限树
     * @param systemId 系统id
     * @return
     */
    List<Map<String,Object>> getTree(String systemId);

    /**
     * 根据id获得权限
     * @param opId 权限id
     */
    OperationBean getOperationById(int opId);

    /**
     * 增加权限
     * @param operationBean 权限对象
     */
    boolean addOperation(OperationBean operationBean);

    /**
     * 修改权限
     * @param operationBean 权限对象
     */
    boolean modifyOperation(OperationBean operationBean);

    /**
     * 删除权限
     * @param opIds 权限id
     */
    boolean delOperation(String opIds);
}
