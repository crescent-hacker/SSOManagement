package com.jetsun.dao.impl;

import com.jetsun.bean.common.TablesConstant;
import com.jetsun.dao.interfaces.BaseDao;
import com.jetsun.utility.CacheUtil;

import java.util.List;
import java.util.Map;


/**
 * Company: jetsun
 * Author: dick
 * Date: 7/20/14
 * Desc:获取常量列表，诸如剂型，限制类型等
 */
public class CommonDao extends BaseDao {
    /**
     * 缓存对象
     */
    private static CacheUtil<List<Map<String,Object>>> cache;
    static {
         cache = new CacheUtil(24*60*60);
    }

    /**
     * 获得统筹区列表
     */
    public List<Map<String,Object>> getAreaListFromDb(){
        //获得AREA的名字和ID列表
        String sql_area = "SELECT AREA_ID AS ID,AREA_NAME AS NAME,AREA_TYPE AS TYPE,0 AS PID FROM "+TablesConstant.INSURANCE_SCHEMA+"AREA WHERE AREA_NAME IS NOT NULL ORDER BY AREA_NAME ASC";
        List<Map<String,Object>> map_area = jdbcTemplate.query(sql_area,new TreeMapper(new String[]{"TYPE|type"}));
        return map_area;
    }
    public List<Map<String,Object>> getAreaList(){
        String key = "map_area";
        List<Map<String,Object>> map_area = cache.get(key);
        if(map_area==null){
            map_area = getAreaListFromDb();
            cache.put(key,map_area);
        }
        return map_area;
    }
    /**
     * 获得医院列表
     */
    public List<Map<String,Object>> getHospitalListFromDb(){
        //获得HOSPITAL的名字和ID列表
        String sql_hospital = "SELECT HSPT_ID AS ID,HSPT_NAME AS NAME,AREA_ID AS PID FROM "+TablesConstant.INSURANCE_SCHEMA+"HOSPITAL WHERE HSPT_NAME IS NOT NULL ORDER by AREA_ID,HSPT_NAME";
        List<Map<String,Object>> map_hospital = jdbcTemplate.query(sql_hospital,new TreeMapper());
        return map_hospital;
    }
    public List<Map<String,Object>> getHospitalList(){
        String key = "map_hospital";
        List<Map<String,Object>> map_hospital = cache.get(key);
        if(map_hospital==null){
            map_hospital = getHospitalListFromDb();
            cache.put(key,map_hospital);
        }
        return map_hospital;
    }
}
