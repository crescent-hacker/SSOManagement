package com.jetsun.dao.interfaces;

import java.util.List;
import java.util.Map;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/9/24
 * Desc:角色dao接口
 */
public interface RoleDao {

    /**
     * 获取角色列表
     */
    List<Map<String,Object>> getRoleList(String systemId);

    /**
     * 获取所有系统的角色列表
     */
    List<Map<String,Object>> getAllRoleList();

    /**
     * 获取角色权限
     */
    List<String> getRoleRights(String id);

    /**
     * 增加角色
     * @param name 名字
     * @param access 权限集
     */
    boolean addRole(String name, String access,String systemId);

    /**
     * 修改角色
     * @param name 名字
     * @param access 权限集
     * @param roleId 角色id
     */
    boolean modifyRole(String name, String access,String roleId);

    /**
     * 删除角色
     * @param id id
     */
    boolean delRole(String id);

    /**
     * 判断是否有用户使用角色
     * @param id id
     */
    boolean hasOperator(String id);

    /**
     * 判断是否有重名
     * @param name 角色名
     */
    public boolean hasRole(String name);

    /**
     * 判断是否有重名,更新用
     * @param name 角色名
     */
    public boolean hasRole(String name,String roleId);

    /**
     * 根据用户id获取角色id集合
     */
    public List<String> getRolesByUserid(String userid);

    /**
     * 设置用户与角色关联
     */
    public boolean bindUserAndRoles(int userId,String roles);
}
