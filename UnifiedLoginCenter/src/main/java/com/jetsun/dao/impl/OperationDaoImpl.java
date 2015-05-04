package com.jetsun.dao.impl;


import com.jetsun.bean.biz.OperationBean;
import com.jetsun.dao.interfaces.BaseDao;
import com.jetsun.dao.interfaces.OperationDao;
import com.jetsun.utility.StringUtil;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/8/14
 * Desc:登陆dao
 */
public class OperationDaoImpl extends BaseDao implements OperationDao {
    /**
     * 获得权限树
     *
     * @param systemId 系统id
     * @return
     */
    @Override
    public List<Map<String, Object>> getTree(String systemId) {
        String sql = "SELECT OP_ID AS ID,OP_IID AS PID,OP_NAME AS NAME,OP_TYPE AS TYPE FROM OPERATION WHERE RS_ID like ?";
        List<Map<String, Object>> treeList = jdbcTemplate.query(sql, new Object[]{systemId + "%"}, new TreeMapper(new String[]{"TYPE|type"}));
        return treeList;
    }

    /**
     * 根据id获得权限
     * @param opId 权限id
     */
    @Override
    public OperationBean getOperationById(int opId){
        String sql = "SELECT * FROM OPERATION WHERE OP_ID = ?";
        OperationBean operationBean = jdbcTemplate.queryForObject(sql,new Object[]{opId},new OperationMapper());
        return operationBean;
    }

    /**
     * 增加权限
     * @param ob 权限对象
     */
    @Override
    @Transactional(rollbackForClassName = {"RuntimeException", "Exception"})
    public boolean addOperation(OperationBean ob) {
        //查出系统号
        String sql_systemid = "SELECT RS_ID FROM ROLESYSTEM WHERE RS_ID like ?";
        long systemId = jdbcTemplate.queryForObject(sql_systemid,new Object[]{ob.getRsId() + "%"},Long.class);
        //判断系统下的同一个jsp下是否有同一个action名
        String sql_repeat = "SELECT COUNT(1) FROM OPERATION WHERE "+(StringUtil.isNotEmpty(ob.getOpAction())?"OP_ACTION = ?":"OP_ACTION IS NULL")+" AND OP_IID = ? AND RS_ID = ? AND OP_TYPE <> 0";
        List<Object> repeatList = new ArrayList<Object>();
        if (StringUtil.isNotEmpty(ob.getOpAction())){
            repeatList.add(ob.getOpAction());
        }
        repeatList.add(ob.getOpIId());
        repeatList.add(systemId);
        int count = jdbcTemplate.queryForObject(sql_repeat,repeatList.toArray(),Integer.class);
        if(count>0){
            return false;
        }
        //进行插入
        String sql_insert = "INSERT INTO OPERATION VALUES(to_char(?)||to_char(SEQ_OPERATION.NEXTVAL),?,?,?,?,?,?,?)";
         Object[] objects = new Object[]{systemId/1000000000L,systemId,ob.getOpName(),ob.getOpNote(),ob.getOpIId(),ob.getOpAction(),ob.getOpIsPass(),ob.getOpType()};
        int row = jdbcTemplate.update(sql_insert,objects);
        //查出id,提供页面定位
        String sql_id = "SELECT T1.OP_ID FROM OPERATION T1 WHERE "+(StringUtil.isNotEmpty(ob.getOpAction())?"T1.OP_ACTION = ?":"T1.OP_ACTION IS NULL") +" AND T1.OP_IID = ? AND T1.OP_NAME = ? AND T1.RS_ID = ?";
        List<Object> idSqlList = new ArrayList<Object>();
        if (StringUtil.isNotEmpty(ob.getOpAction())){
            idSqlList.add(ob.getOpAction());
        }
        idSqlList.add(ob.getOpIId());
        idSqlList.add(ob.getOpName());
        idSqlList.add(systemId);
        int opId = jdbcTemplate.queryForObject(sql_id,idSqlList.toArray(),Integer.class);
        ob.setOpId(opId);

        return row > 0;
    }

    /**
     * 修改权限
     * @param ob 权限对象
     */
    @Override
    @Transactional(rollbackForClassName = {"RuntimeException", "Exception"})
    public boolean modifyOperation(OperationBean ob) {
        //查出系统号
        String sql_systemid = "SELECT RS_ID FROM ROLESYSTEM WHERE RS_ID like ?";
        long systemId = jdbcTemplate.queryForObject(sql_systemid,new Object[]{ob.getRsId() + "%"},Long.class);
        //进行插入
        String sql_update = "UPDATE OPERATION SET OP_NAME = ?,OP_NOTE = ?,OP_IID = ?,OP_ACTION = ?,OP_ISPASS = ?,OP_TYPE = ? WHERE OP_ID = ? AND RS_ID = ?";
        Object[] objects = new Object[]{ob.getOpName(),ob.getOpNote(),ob.getOpIId(),ob.getOpAction(),ob.getOpIsPass(),ob.getOpType(),ob.getOpId(),systemId};
        int row = jdbcTemplate.update(sql_update,objects);
        return row > 0;
    }

    /**
     * 删除权限
     * @param opIds 权限id
     * @return
     */
    @Override
    @Transactional(rollbackForClassName = {"RuntimeException", "Exception"})
    public boolean delOperation(String opIds) {
        //参数列表
        String[] opIdArr = opIds.split(",");
        List<Object[]> list = new ArrayList<Object[]>();
        for(String opId:opIdArr){
            list.add(new Object[]{Integer.parseInt(opId)});
        }
        //批量删除权限sql
        String sql_del = "DELETE FROM OPERATION WHERE OP_ID = ?";
        //执行删除
        jdbcTemplate.batchUpdate(sql_del,list);
        //批量删除权限角色关联sql
        String sql_unbind = "DELETE FROM ROLEACCESS WHERE OP_ID = ?";
        jdbcTemplate.batchUpdate(sql_unbind,list);
        return true;
    }


    /**
     * 辅助类-把返回的map转成OperationBean对象
     */
    private class OperationMapper implements RowMapper<OperationBean> {

        @Override
        public OperationBean mapRow(ResultSet rs, int rowNum) throws SQLException {
            int opId = rs.getInt("OP_ID");
            long rsId = rs.getLong("RS_ID");
            String opName = rs.getString("OP_NAME");
            String opNote = rs.getString("OP_NOTE");
            int opIId = rs.getInt("OP_IID");
            String opAction = rs.getString("OP_ACTION");
            int opIspass = rs.getInt("OP_ISPASS");
            int opType = rs.getInt("OP_TYPE");
            //组装对象

            return new OperationBean(opId,rsId,opName,opNote,opIId,opAction,opType,opIspass);
        }
    }
}