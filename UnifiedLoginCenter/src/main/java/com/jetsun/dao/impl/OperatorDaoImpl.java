package com.jetsun.dao.impl;

import com.jetsun.bean.biz.UserBean;
import com.jetsun.bean.common.CommonVariable;
import com.jetsun.bean.common.TablesConstant;
import com.jetsun.dao.interfaces.BaseDao;
import com.jetsun.dao.interfaces.OperatorDao;
import com.jetsun.utility.CharMD5;
import com.jetsun.utility.StringUtil;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 人员管理*
 */
public class OperatorDaoImpl extends BaseDao implements OperatorDao {

    /**
     * 获取用户列表
     *
     * @param isValid  是否有效 0-无效 1-有效 空字符串-全部
     * @param userName 用户名 空字符串-全部
     * @return 用户列表
     */
    @Override
    public List<List<String>> getUserList(String isValid, String userName) {
        String sql = "SELECT OPERATOR.USERID," +
                "CASE WHEN HSPT.HSPT_ID IS NULL AND AREA.AREA_ID=0 THEN 0 WHEN HSPT.HSPT_ID IS NULL AND AREA.AREA_ID!=0 THEN 1 ELSE 2 END TYPE," +
                "CASE WHEN HSPT.HSPT_ID IS NULL THEN AREA_NAME ELSE HSPT_NAME END NAME," +
                "OPER_NO,OPER_NAME,OPER_BEGIN AS USERBEGIN," +
                "OPER_END AS USEREND," +
                "CASE OPER_STATE WHEN '0' THEN '禁用' ELSE '启用' END STATE," +
                "CERT_NAME,UC_ID,OPER_STATE,AREA.AREA_ID,HSPT.HSPT_ID " +
                " FROM OPERATOR LEFT JOIN USERCERTIFICATE ON USERCERTIFICATE.USERID = OPERATOR.USERID LEFT JOIN " + TablesConstant.INSURANCE_SCHEMA + "AREA ON AREA.AREA_ID=OPERATOR.AREA_ID LEFT JOIN " + TablesConstant.INSURANCE_SCHEMA + "HOSPITAL HSPT ON HSPT.HSPT_ID=OPERATOR.HSPT_ID" +
                " WHERE OPERATOR.USERID > 0 AND OPER_STATE LIKE ? AND OPER_NAME LIKE ?";
        //参数数组
        Object[] objects = new Object[]{StringUtil.wrapLike(isValid), StringUtil.wrapLike(userName)};
        //分页sql
        String paginSql = this.getPaginStr(sql, objects);
        List<List<String>> resultsArrayList = jdbcTemplate.query(paginSql, objects, new UserListMapper());
        return resultsArrayList;
    }

    /**
     * 设置证书是否启用（设置不启用同时解除与用户的绑定）
     */
    @Override
    @Transactional(rollbackForClassName = {"RuntimeException", "Exception"})
    public boolean setUserStatus(boolean isValid, int userid) {
        String sql;
        if (isValid) {
            sql = "UPDATE OPERATOR SET OPER_STATE = '1' WHERE USERID = ?";
        } else {
            sql = "UPDATE OPERATOR SET OPER_STATE = '0' WHERE USERID = ?";
        }
        //执行更新
        int row = jdbcTemplate.update(sql, new Object[]{userid});
        //更新状态
        return row > 0;
    }

    /**
     * 增加用户
     * @param u 用户对象
     * @return
     */
    @Override
    @Transactional(rollbackForClassName = {"RuntimeException", "Exception"})
    public boolean addUser(UserBean u) {
        String sql_insert = "INSERT INTO OPERATOR VALUES(SEQ_OPERATOR.NEXTVAL,?,?,?,?,?,?,?,?,0,sysdate)";
        //加密密码，初始密码123456
        String password = CharMD5.MD5(u.getOperNo() + "123456");
        //执行语句
        Object[] objects = new Object[]{u.getHsptId(),u.getAreaId(),u.getOperNo(), u.getOperName(), password, u.getOperBegin(), u.getOperEnd(), u.getOperState()};
        int row = jdbcTemplate.update(sql_insert, objects);
        //查出userid用以绑定证书和绑定角色
        String sql_userid = "SELECT USERID FROM OPERATOR WHERE OPER_NO = ?";
        int userId = jdbcTemplate.queryForObject(sql_userid,new Object[]{u.getOperNo()},Integer.class);
        u.setUserId(userId);
        //返回新建用户情况
        return row > 0;
    }

    /**
     * 修改用户
     * @param u 用户对象
     * @return
     */
    @Override
    @Transactional(rollbackForClassName = {"RuntimeException", "Exception"})
    public boolean modifyUser(UserBean u,boolean isResetPwd) {
        //组装sql
        String sql = "UPDATE OPERATOR SET HSPT_ID = ?,AREA_ID = ?,OPER_NO = ?,OPER_NAME = ?,OPER_STATE = ?,OPER_BEGIN = ?,OPER_END = ?"+(isResetPwd?",OPER_PWD = ?":"")+" WHERE USERID = ?";
        //拼装参数
        List<Object> objectList = new ArrayList<Object>();
        objectList.add(u.getHsptId());
        objectList.add(u.getAreaId());
        objectList.add(u.getOperNo());
        objectList.add(u.getOperName());
        objectList.add(u.getOperState());
        objectList.add(u.getOperBegin());
        objectList.add(u.getOperEnd());
        if(isResetPwd){
            objectList.add(CharMD5.MD5(u.getOperNo()+"123456"));
        }
        objectList.add(u.getUserId());
        //执行sql
        int row = jdbcTemplate.update(sql,objectList.toArray());

        return row>0;
    }

    /**
     * 辅助类-把返回的List<Map<String,Object>>转成List<List<String>>
     */
    private class UserListMapper implements RowMapper<List<String>> {

        @Override
        public List<String> mapRow(ResultSet rs, int rowNum) throws SQLException {
            List<String> list = new ArrayList<String>();
            //结果集获取数据
            String userid = rs.getString("USERID");
            int type = rs.getInt("TYPE");
            String name = rs.getString("NAME");
            String oper_no = rs.getString("OPER_NO");
            String oper_name = rs.getString("OPER_NAME");
            String oper_begin = rs.getString("USERBEGIN");
            String oper_end = rs.getString("USEREND");
            String state = rs.getString("STATE");
            String cert_name = StringUtil.transNull(rs.getString("CERT_NAME"));
            int oper_state = rs.getInt("OPER_STATE");
            int area_id = rs.getInt("AREA_ID");
            int hspt_id = rs.getInt("HSPT_ID");

            //拼接修改界面的字符串
            String[] strings = new String[]{userid,wrap(oper_no),wrap(oper_name),""+oper_state,""+type,""+area_id,""+hspt_id,wrap(cert_name),wrap(oper_begin),wrap(oper_end)};
            //修改用户
            String operation_1 = "<img src='" + CommonVariable.WEB_PATH + "/static/images/btn_AS_Edit.png' style='width:16px;vertical-align: middle;cursor: pointer;' title='修改'" +
                    "onclick='showModifyUserUI(" + StringUtil.join(strings,",")+")'/>";
            //设置用户状态
            String operation_2 = "<img src='" + CommonVariable.WEB_PATH + "/static/images/" + (oper_state == 1 ? "btn_A_Reject.png" : "btn_A_Assent.png") + "' style='margin-left:15px;width:16px;vertical-align: middle;cursor: pointer;' title='" + (oper_state == 1 ? "禁用" : "启用") + "' " +
                    "onclick='setStatus(" + (oper_state == 1 ? 0 : 1) + "," + userid + ")'/>";

            //加入列表
            list.add((type==0)?"省":(type==1?"地市":"医院"));
            list.add(name);
            list.add(oper_no);
            list.add(oper_name);
            list.add(oper_begin);
            list.add(oper_end);
            list.add(state);
            list.add(cert_name);
            list.add(operation_1 + operation_2);
            return list;
        }

        private String wrap(String origin){
            return "\""+origin+"\"";
        }
    }

}
