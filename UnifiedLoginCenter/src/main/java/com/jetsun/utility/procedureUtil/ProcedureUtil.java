package com.jetsun.utility.procedureUtil;

import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.CallableStatement;
import java.util.Map;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/8/22
 * Desc:存储过程工具类
 */
public class ProcedureUtil {
    /**
     * 日志记录器
     */
    private static Logger logger = LoggerFactory.getLogger(ProcedureUtil.class);

    /**
     * 调用无返回值的存储过程
     */
    public static boolean execProc(JdbcTemplate jdbcTemplate, String procName, Object[] inParam, Integer[] inType) throws Exception {
        boolean isSuccess = (Boolean) jdbcTemplate.execute(new ProcedureCreator(procName, inParam, inType)
                , new CallableStatementCallback() {
            public Object doInCallableStatement(CallableStatement cs) {
                boolean isSuccess = false;
                try {
                    cs.execute();
                    isSuccess = true;
                } catch (Exception e) {
                    logger.error("调用无返回值存储过程出错", e);
                } finally {
                    return isSuccess;
                }
            }
        });
        return isSuccess;
    }

    /**
     * 调用返回单行值的存储过程，返回一个map(内可含CURSOR类型)
     *
     * @return
     */
    public static Map<String, Object> execProcRetMap(JdbcTemplate jdbcTemplate, String procName, Object[]
            inParam, Integer[] inType,String[] outParamKey,Integer[] outType) throws Exception {
        Map<String, Object> resultMap = (Map<String, Object>) jdbcTemplate.execute(new ProcedureCreator(procName, inParam, inType,outType), new ProcedureCallBack(inParam.length+1,outParamKey,outType));
        logger.debug("procedure run result:"+resultMap.toString());
        return resultMap;
    }

    /**
     * 调用返回单行值的存储过程，返回一个map,不传指定类型数组就默认in和out都是字符串
     *
     * @return
     */
    public static Map<String, Object> execProcRetMap(JdbcTemplate jdbcTemplate, String procName, Object[]
            inParam, String[] outParamKey) throws Exception {
        //初始化
        Integer[] inType = new Integer[inParam.length];
        Integer[] outType = new Integer[outParamKey.length];
        //拼装入参出参类型
        for (int i = 0; i < inParam.length; i++) {
            inType[i] = OracleTypes.VARCHAR;
        }
        for (int i = 0; i <outParamKey.length; i++) {
            outType[i] = OracleTypes.VARCHAR;
        }
        Map<String, Object> resultMap = (Map<String, Object>) jdbcTemplate.execute(new ProcedureCreator(procName, inParam,inType,outType), new ProcedureCallBack(inParam.length,outParamKey,outType));
        logger.debug("procedure run result:"+resultMap.toString());
        return resultMap;
    }

    /**
     * 调用返回单行值的存储过程，返回一个map,不传指定类型数组就默认in都是字符串
     *
     * @return
     */
    public static Map<String, Object> execProcRetMap(JdbcTemplate jdbcTemplate, String procName, Object[]
            inParam, String[] outParamKey,Integer[] outType) throws Exception {
        //初始化
        Integer[] inType = new Integer[inParam.length];
        //拼装入参出参类型
        for (int i = 0; i < inParam.length; i++) {
            inType[i] = OracleTypes.VARCHAR;
        }
        Map<String, Object> resultMap = (Map<String, Object>) jdbcTemplate.execute(new ProcedureCreator(procName, inParam,inType,outType), new ProcedureCallBack(inParam.length,outParamKey,outType));
        logger.debug("procedure run result:"+resultMap.toString());
        return resultMap;
    }

}
