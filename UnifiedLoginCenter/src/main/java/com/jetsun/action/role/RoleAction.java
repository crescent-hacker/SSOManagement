package com.jetsun.action.role;

import com.jetsun.action.BaseAction;
import com.jetsun.dao.interfaces.RoleDao;
import com.jetsun.utility.HttpResponser;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RoleAction extends BaseAction {
    @Autowired
    RoleDao roleDao;

    /**
     * 获得角色列表
     */
    public String getRoleList() throws Exception {
        //接收参数
        String systemId = request.getParameter("systemId");
        //调用dao
        List<Map<String,Object>> roleList =  roleDao.getRoleList(systemId);
        //返回map
        Map<String,Object> retMap = new HashMap<String, Object>();
        retMap.put("roleList",roleList );
        HttpResponser.rspToHttpWithMap(response, retMap, true);
        return null;
    }

    /**
     * 获得所有系统的角色列表，返回树状数组
     */
    public String getAllRoleList() throws  Exception{
        //返回map
        Map<String,Object> retMap = new HashMap<String, Object>();
        retMap.put("roleList",roleDao.getAllRoleList());
        HttpResponser.rspToHttpWithMap(response, retMap, true);
        return null;
    }

    /**
     * 获得角色的权限
     */
    public String getRoleRights() throws Exception {
        //获得参数
        String roleId = request.getParameter("roleId");
        //调用dao
        List<String> roleRights = roleDao.getRoleRights(roleId);
        //返回map
        Map<String,Object> retMap = new HashMap<String, Object>();
        retMap.put("roleRightList", roleRights);
        HttpResponser.rspToHttpWithMap(response, retMap, true);
        return null;
    }

    /**
     * 增加角色
     */
    public String addRole() throws Exception {
        //获得参数
        String name = request.getParameter("name");
        String access = request.getParameter("access");
        String systemId = request.getParameter("systemId");
        //初始化返回变量
        boolean isSuccess = false;
        Map<String,Object> retMap = new HashMap<String, Object>();
        //判断是否重名
        boolean notRepeat = !roleDao.hasRole(name);
        if(notRepeat) {
            //调用dao
            isSuccess = roleDao.addRole(name, access, systemId);
        }else{
            retMap.put("message","角色名字已存在，请重新输入。");
        }
        //返回ajax
        HttpResponser.rspToHttpWithMap(response,retMap, isSuccess);
        return null;
    }

    /**
     * 修改角色
     */
    public String modifyRole() throws Exception {
        //获得参数
        String name = request.getParameter("name");
        String access = request.getParameter("access");
        String roleId = request.getParameter("roleId");
        //初始化返回变量
        boolean isSuccess = false;
        Map<String,Object> retMap = new HashMap<String, Object>();
        //判断是否重名
        boolean notRepeat = !roleDao.hasRole(name,roleId);
        if(notRepeat) {
            //调用dao
            isSuccess = roleDao.modifyRole(name, access, roleId);
        }else{
            retMap.put("message","角色名字已存在，请重新输入。");
        }
        //返回ajax
        HttpResponser.rspToHttpWithMap(response,retMap, isSuccess);
        return null;
    }

    /**
     * 删除角色
     */
    public String delRole() throws Exception {
        //获得参数
        String roleId = request.getParameter("roleId");
        //调用dao
        boolean isSuccess;
        String message = null;
        if (roleDao.hasOperator(roleId)) {
            isSuccess = false;
            message = "有用户正在使用该角色，删除失败。";
        } else {
            isSuccess = roleDao.delRole(roleId);
        }
        //返回map
        Map<String,Object> retMap = new HashMap<String, Object>();
        retMap.put("message",message);
        HttpResponser.rspToHttpWithMap(response, retMap, isSuccess);
        return null;
    }
}
