package com.jetsun.dao.impl;

import com.jetsun.dao.interfaces.BaseDao;
import com.jetsun.dao.interfaces.RoleDao;
import com.jetsun.utility.StringUtil;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 角色管理
 */
public class RoleDaoImpl extends BaseDao implements RoleDao {

    /**
     * 获得角色列表
     */
    @Override
    public List<Map<String, Object>> getRoleList(String systemId) {
        String sql = "SELECT ROLE_ID AS ID,ROLE_NAME AS NAME,0 AS PID FROM ROLE WHERE RS_ID LIKE ?";
        List<Map<String,Object>> retList = jdbcTemplate.query(sql,new Object[]{systemId+"%"},new TreeMapper());
        return retList;
    }
    
    /**
     * 获得所有系统角色列表
     */
    @Override
    public List<Map<String, Object>> getAllRoleList() {
        String sql = "(SELECT TO_NUMBER(SUBSTR(RS_ID,0,1)) AS PID,ROLE_ID AS ID,ROLE_NAME AS NAME FROM ROLE) UNION (SELECT 0 AS PID,TO_NUMBER(SUBSTR(RS_ID,0,1)) AS ID,RS_INFO AS NAME FROM ROLESYSTEM WHERE TO_NUMBER(SUBSTR(RS_ID,0,1))!=9)";
        List<Map<String,Object>> retList = jdbcTemplate.query(sql,new TreeMapper());
        return retList;
    }

    /**
     * 获得角色列表
     */
    @Override
    public List<String> getRoleRights(String roleId) {
        String sql = "SELECT T1.OP_ID FROM ROLEACCESS T1,OPERATION T2 WHERE T1.OP_ID = T2.OP_ID AND T1.ROLE_ID = ?";
        List<String> retList = jdbcTemplate.queryForList(sql,new Object[]{roleId},String.class);
        return retList;
    }

    /**
     * 增加角色
     * @param name 名字
     * @param access 角色集
     */
    @Override
    @Transactional(rollbackForClassName = {"RuntimeException", "Exception"})
    public boolean addRole(String name, String access,String sysId) {
        //查出系统号
        String sql_systemid = "SELECT RS_ID FROM ROLESYSTEM WHERE RS_ID like ?";
        long systemId = jdbcTemplate.queryForObject(sql_systemid,new Object[]{sysId + "%"},Long.class);
        //执行插入
        String sql_insert = "INSERT INTO ROLE VALUES(TO_CHAR(?)||TO_CHAR(SEQ_ROLE.NEXTVAL),?,?)";
        Object[] objects = new Object[]{systemId/1000000000L,systemId,name};
        int row = jdbcTemplate.update(sql_insert,objects);
        //判断插入是否成功
        if(row<=0){
            return false;
        }
        //查询刚插入的角色id
        String sql_role_id = "SELECT ROLE_ID FROM ROLE WHERE ROLE_NAME = ?";
        int roleId = jdbcTemplate.queryForObject(sql_role_id,new Object[]{name},Integer.class);
        //进行角色角色绑定
        String sql_bind = "INSERT INTO ROLEACCESS VALUES(SEQ_ROLEACCESS.NEXTVAL,?,?)";
        List<Object[]> objList = new ArrayList<Object[]>();
        if(StringUtil.isNotEmpty(access)){
            for(String opId:access.split(",")){
                objList.add(new Object[]{opId,roleId});
            }
        }
        jdbcTemplate.batchUpdate(sql_bind,objList);
        return true;
    }

    /**
     * 修改角色
     * @param name id
     * @param access 角色集
     * @param roleId 角色id
     */
    @Override
    @Transactional(rollbackForClassName = {"RuntimeException", "Exception"})
    public boolean modifyRole(String name, String access,String roleId) {
        //执行更新
        String sql_update = "UPDATE ROLE SET ROLE_NAME = ? WHERE ROLE_ID = ?";
        Object[] objects = new Object[]{name,roleId};
        int row = jdbcTemplate.update(sql_update,objects);
        //判断更新是否成功
        if(row<=0){
            return false;
        }
        //删除角色相关的角色
        String sql_del = "DELETE FROM ROLEACCESS WHERE ROLE_ID = ?";
        jdbcTemplate.update(sql_del,new Object[]{roleId});
        //插入角色相关的角色
        String sql_bind = "INSERT INTO ROLEACCESS VALUES(SEQ_ROLEACCESS.NEXTVAL,?,?)";
        List<Object[]> objList = new ArrayList<Object[]>();
        if(StringUtil.isNotEmpty(access)){
            for(String opId:access.split(",")){
                objList.add(new Object[]{opId,roleId});
            }
        }
        jdbcTemplate.batchUpdate(sql_bind,objList);
        return true;
    }

    /**
     * 删除角色
     * @param roleId 角色id
     * @return
     */
    @Override
    @Transactional(rollbackForClassName = {"RuntimeException", "Exception"})
    public boolean delRole(String roleId) {
        //删除关联的角色
        String sql_unbind = "DELETE FROM ROLEACCESS WHERE ROLE_ID = ?";
        jdbcTemplate.update(sql_unbind,new Object[]{roleId});
        //删除角色
        String sql_del = "DELETE FROM ROLE WHERE ROLE_ID = ?";
        int row = jdbcTemplate.update(sql_del,new Object[]{roleId});
        return row>0;
    }

    /**
     * 用户正在使用不能删除角色
     * @param id id
     */
    @Override
    public boolean hasOperator(String id) {
        String sql = "SELECT COUNT(1) FROM ROLE T1,OPERATORACCESS T2 WHERE T1.ROLE_ID = T2.ROLE_ID AND T1.ROLE_ID = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, Integer.class) > 0;
    }

    /**
     * 判断是否有重名，插入用
     * @param name 角色名
     */
    @Override
    public boolean hasRole(String name) {
        String sql = "SELECT COUNT(1) FROM ROLE WHERE ROLE_NAME = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{name}, Integer.class) > 0;
    }

    /**
     * 判断是否有重名,更新用
     * @param name 角色名
     */
    @Override
    public boolean hasRole(String name,String roleId) {
        String sql = "select count(1) from role where role_name = ? and role_id != ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{name,roleId}, Integer.class) > 0;
    }

    /**
     * 根据用户id获取角色id集合
     * @param userid 用户id
     * @return 权限id数组
     */
    @Override
    public List<String> getRolesByUserid(String userid){
        String sql = "SELECT ROLE_ID FROM OPERATORACCESS WHERE USERID = ?";
        List<String> list =  jdbcTemplate.queryForList(sql,new Object[]{userid},String.class);
        return list;
    }

    /**
     * 设置用户与角色关联
     */
    @Override
    @Transactional(rollbackForClassName = {"RuntimeException", "Exception"})
    public boolean bindUserAndRoles(int userId,String roles){
        //删除该用户已经绑定的角色
        String sql_del = "DELETE FROM OPERATORACCESS WHERE USERID = ?";
        jdbcTemplate.update(sql_del,new Object[]{userId});
        //用户绑定角色
        String sql_bind = "INSERT INTO OPERATORACCESS VALUES(SEQ_OPERATORACCESS.NEXTVAL,?,?)";
        List<Object[]> objList = new ArrayList<Object[]>();
        if(StringUtil.isNotEmpty(roles)){
            for(String roleId:roles.split(",")){
                objList.add(new Object[]{roleId,userId});
            }
        }
        jdbcTemplate.batchUpdate(sql_bind,objList);
        return true;
    }
}
