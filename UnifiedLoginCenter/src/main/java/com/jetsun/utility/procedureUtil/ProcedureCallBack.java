package com.jetsun.utility.procedureUtil;

import oracle.jdbc.OracleTypes;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.RowMapper;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/7/17
 * Desc: 存储过程回调类
 */
public class ProcedureCallBack implements CallableStatementCallback {
    /**
     * out开始位置
     */
    private int outPosition;
    /**
     * 返回参数的名字
     */
    private String[] outParamKey;
    /**
     * out类型
     */
    Integer[] outType;

    /**
     * 存储过程回调函数
     * @param cs CallableStatement
     * @return Map<String,Object>
     * @throws java.sql.SQLException
     */
    public Map<String,Object> doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
        //返回对象
        Map<String,Object> retMap = new HashMap<String, Object>();
        //执行查询
        cs.execute();
        //结果集
        ResultSet rs;
        for (int i = 0; i < outType.length; i++) {
            //out的序号
            int index = outPosition+i+1;
            //开始处理
            if(outType[i] == OracleTypes.CURSOR){//处理游标类型
                //初始化
                List<Map<String,Object>> resultsList = new ArrayList<Map<String,Object>>();//游标的多行列表
                rs = (ResultSet) cs.getObject(index);// 获取游标一行的值
                CallBackRowMapper rowMapper = new CallBackRowMapper();//组装每行数据的匹配器
                //用自己传入的匹配器进行每行的数据组装
                for (int j = 0; rs.next(); j++) {
                    Map<String,Object> obj = rowMapper.mapRow(rs, j);
                    resultsList.add(obj);
                }
                //放入返回map
                retMap.put(outParamKey[i],resultsList);
                //关闭结果集
                rs.close();
            }else{//非游标类型
                retMap.put(outParamKey[i], cs.getObject(index));
            }
        }

        cs.close();
        //返回
        return retMap;
    }


    /**
     * 构造函数
     */
    public ProcedureCallBack(int outPosition, String[] outParamKey,Integer[] outType) {
        this.outPosition = outPosition;
        this.outParamKey = outParamKey;
        this.outType = outType;
    }

    /**
     * 回调的游标列表处理类
     */
    private class CallBackRowMapper implements RowMapper<Map<String, Object>> {
        @Override
        public Map<String, Object> mapRow(ResultSet resultSet, int index) throws SQLException {
            //返回map
            Map<String,Object> retMap = new HashMap<String, Object>();
            //获取字段名称
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int count = rsmd.getColumnCount();
            for (int i = 0; i < count; i++) {
                String name = rsmd.getColumnName(i + 1);
                retMap.put(name,resultSet.getObject(name));
            }
            return retMap;
        }
    }
}
