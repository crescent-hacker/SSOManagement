package com.jetsun.dao.interfaces;

import com.jetsun.bean.common.EventModel;
import com.jetsun.bean.common.SessionKey;
import com.jetsun.utility.Pagination;
import com.opensymphony.xwork2.ActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/7/8
 * Desc:基础dao
 */
public abstract class BaseDao {
    /**
     * 日志记录器
     */
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * Spring JDBC 模型
     */
    protected JdbcTemplate jdbcTemplate;

    /**
     * 设置数据源
     */
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * 分页对象
     */
    protected Pagination pagination;

    /**
     * 获得分页sql
     *
     * @param sql
     * @param objArr
     * @return
     */
    protected String getPaginStr(String sql, Object[] objArr) {
        //初始化
        Map<String, Object> session = ActionContext.getContext().getSession();
        EventModel event = (EventModel) session.get(SessionKey.EVENT_MODEL);
        if(event == null) {
            logger.debug("我擦，event空空如也！");
        }
        String countSql = Pagination.getCountSql(sql);
        //创建分页对象
        int pageCount = jdbcTemplate.queryForObject(countSql, objArr, Integer.class);
        pagination = new Pagination(pageCount, (Integer) event.get("pageIndex"));
        //获取分页参数并放入模型
        event.put("totalPages", this.pagination.getTotalPages());
        event.put("records", this.pagination.getRecords());
        return pagination.getPaginSql(sql);
    }

    /**
     * 获得分页sql
     *
     * @param sql
     * @return
     */
    protected String getPaginStr(String sql) {
        //初始化
        Map<String, Object> session = ActionContext.getContext().getSession();
        EventModel event = (EventModel) session.get(SessionKey.EVENT_MODEL);
        String countSql = Pagination.getCountSql(sql);
        //创建分页对象
        int pageCount = jdbcTemplate.queryForObject(countSql, Integer.class);
        pagination = new Pagination(pageCount, (Integer) event.get("pageIndex"));
        //获取分页参数并放入模型
        event.put("totalPages", this.pagination.getTotalPages());
        event.put("records", this.pagination.getRecords());
        return pagination.getPaginSql(sql);
    }


    /**
     * 获取分页对象
     *
     * @return
     */
    public Pagination getPagination() {
        return this.pagination;
    }

    /**
     * 辅助类-把返回的List<Map<String,Object>>转成List<List<String>>
     */
    public class ListMapper implements RowMapper<List<String>> {

        @Override
        public List<String> mapRow(ResultSet rs, int rowNum) throws SQLException {
            int count = rs.getMetaData().getColumnCount();
            List<String> list = new ArrayList<String>();
            for (int i = 1; i <= count; i++) {
                list.add(rs.getString(i));
            }
            return list;
        }
    }

    /**
     * 辅助类-把返回的树形列表属性转化成小写，供ztree使用
     */
    public class TreeMapper implements RowMapper<Map<String, Object>> {
        //存储需要额外放入树的映射关系
        private String[] mappingKey = new String[]{};

        /**
         * 行处理方法
         */
        @Override
        public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
            int id = rs.getInt("ID");
            int pid = rs.getInt("PID");
            String name = rs.getString("NAME");
            //组装map
            Map<String, Object> retMap = new HashMap<String, Object>();
            retMap.put("id", id);
            retMap.put("pId", pid);
            retMap.put("name", name);

            //处理额外的映射关系
            for(String key :mappingKey){
                String[] keyMap = key.split("\\|");
                String origin = keyMap[0];
                String target = keyMap[1];
                retMap.put(target,rs.getObject(origin));
            }

            return retMap;
        }

        /**
         * 无参构造函数
         */
        public TreeMapper(){}

        /**
         * 附带额外树节点参数的构造方法
         */
        public TreeMapper(String[] mappingKey){
            this.mappingKey = mappingKey;
        }
    }

}
