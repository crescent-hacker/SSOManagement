package com.jetsun.utility.procedureUtil;

import org.springframework.jdbc.core.CallableStatementCreator;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/7/17
 * Desc: 存储过程创建类
 */
public class ProcedureCreator implements CallableStatementCreator {
    /**
     * 存储过程名
     */
    private String procName;
    /**
     * 输入参数
     */
    Object[] inParam;
    /**
     * 输入类型
     */
    Integer[] inType;
    /**
     * 输出类型
     */
    Integer[] outType;

    /**
     * 构造方法-单行返回值（OUT可含CURSOR类型）
     *
     * @param procName
     * @param inParam
     * @param inType
     * @param outType
     */
    public ProcedureCreator(String procName, Object[] inParam, Integer[] inType, Integer[] outType) throws Exception {
        if ((inParam.length != inType.length)) {
            throw new Exception("参数与参数类型长度不一致");
        }
        this.procName = procName;
        this.inParam = inParam;
        this.inType = inType;
        this.outType = outType;
    }

    /**
     * 构造方法-游标返回多行
     *
     * @param procName
     * @param inParam
     * @param inType
     */
    public ProcedureCreator(String procName, Object[] inParam, Integer[] inType) throws Exception {
        if ((inParam.length != inType.length)) {
            throw new Exception("参数与参数类型长度不一致");
        }
        this.procName = procName;
        this.inParam = inParam;
        this.inType = inType;
    }

    /**
     * 创建存储过程函数
     *
     * @param con
     * @return
     * @throws java.sql.SQLException
     */
    public CallableStatement createCallableStatement(Connection con) throws SQLException {
        //preStatement字符串
        StringBuffer params = new StringBuffer("");
        //参数总长度
        int len = inParam.length + (outType!=null?outType.length:0);
        //拼接参数字符串
        if (len > 0) {
            params.append("?");
        }
        for (int i = 1; i < len; i++) {
            params.append(",?");
        }
        //调用存储过程的的SQL
        String storeProc = "{call " + procName + "(" + params.toString() + ")}";
        CallableStatement cs = con.prepareCall(storeProc);
        // 设置输入参数的值
        for (int i = 0; i < inParam.length; i++) {
            cs.setObject(i + 1, inParam[i]);
        }
        // 注册输出参数的类型
        if(outType!=null) {
            for (int i = inParam.length; i < len; i++) {
                cs.registerOutParameter(i + 1, outType[i - inParam.length]);
            }
        }

        return cs;
    }
}





