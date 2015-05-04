package com.jetsun.dao.interfaces;

import com.jetsun.bean.biz.UserBean;

import java.util.List;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/9/24
 * Desc:
 */
public interface OperatorDao {
    /**
     * 获取用户列表
     * @param isValid 是否有效 0-无效 1-有效 空字符串-全部
     * @param userName 用户名 空字符串-全部
     * @return 用户列表
     */
    List<List<String>> getUserList(String isValid,String userName);

    /**
     * 设置用户状态
     * @param isValid 1启用 0禁用
     * @param userId 用户id
     * @return 操作结果
     */
    boolean setUserStatus(boolean isValid, int userId);

    /**
     * 增加用户
     * @param userBean 用户对象
     * @return
     */
    boolean addUser(UserBean userBean);

    /**
     * 修改用户
     * @param userBean 用户对象
     * @param isResetPwd 是否重置密码
     */
    boolean modifyUser(UserBean userBean,boolean isResetPwd);
}
