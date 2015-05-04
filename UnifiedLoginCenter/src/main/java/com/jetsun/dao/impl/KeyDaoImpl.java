package com.jetsun.dao.impl;
import com.jetsun.bean.biz.KeyBean;
import com.jetsun.dao.interfaces.BaseDao;
import com.jetsun.dao.interfaces.KeyDao;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2014/11/18.
 */
public class KeyDaoImpl extends BaseDao implements KeyDao{
    String sql_searchKey_inStorage="select id,key_no,certificate_no,model,location,responsible_person,responsible_telephone,sign,inStorage_person,inStorage_date,notes from KEY where clzt!=-1";
    String sql_searchKey_outStorage="select id,key_no,certificate_no,model,location,responsible_person,responsible_telephone,sign,outStorage_person,outStorage_date,notes from KEY where sign='出库' and clzt!=-1";

    String sql_searchKey_storage="select id,key_no,certificate_no,model,location,responsible_person,responsible_telephone,sign,inStorage_person,inStorage_date,notes from KEY where sign='库存' and clzt!=-1";
    String sql_searchKey_loss="select id,key_no,certificate_no,model,location,responsible_person,responsible_telephone,sign,loss_person,loss_date,notes from KEY where sign='挂失' and clzt!=-1";

    String sql_searchById ="select * from KEY WHERE id=? and clzt!=-1";

    String sql_updateSign="update kEY set sign = ? where id = ?";
    String sql_insertKey="insert into KEY (key_no,certificate_no,model,location,responsible_person,responsible_telephone,sign,inStorage_person,inStorage_date,notes,clzt) values (?,?,?,?,?,?,?,?,?,?,1)";
    String sql_updateKey="update KEY SET key_no=?,certificate_no=?,model=?,location=?,responsible_person=?,responsible_telephone=?,sign=?,inStorage_person=?,outStorage_person=?,loss_person=?,inStorage_date=?,outStorage_date=?,loss_date=?,lastModified_date=?,notes=? WHERE ID = ? and clzt!=-1";
    String sql_deleteKey="update KEY set clzt=-1 where id=?";

    public List<Map<String,Object>> searchKey_inStorage(){
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql_searchKey_inStorage,new Object[]{});
        return list;
    }

    public List<Map<String,Object>> searchKey_outStorage(){
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql_searchKey_outStorage,new Object[]{});
        return list;
    }

    public List<Map<String,Object>> searchKey_storage(){
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql_searchKey_storage,new Object[]{});
        return list;
    }

    public List<Map<String,Object>> searchKey_loss(){
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql_searchKey_loss,new Object[]{});
        return list;
    }

    public List<Map<String,Object>> searchById(KeyBean bean){
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql_searchById,new Object[]{bean.getId()});
        return list;
    }

    public boolean updateSign(KeyBean bean){
        jdbcTemplate.update(sql_updateSign,new Object[]{bean.getSign(),bean.getId()});
        return true;
    }

    public boolean insertKey(KeyBean bean){
        jdbcTemplate.update(sql_insertKey,new Object[]{bean.getKey_no(),bean.getCertificate_no(),bean.getModel(),bean.getLocation(),bean.getResponsible_person(),bean.getResponsible_telephone(),"库存",bean.getInStorage_person(),bean.getInStorage_date(),bean.getNotes()});
        return true;
    }
    public boolean updateKey(KeyBean bean){
        jdbcTemplate.update(sql_updateKey,new Object[]{bean.getKey_no(),bean.getCertificate_no(),bean.getModel(),bean.getLocation(),bean.getResponsible_person(),bean.getResponsible_telephone(),bean.getSign(),bean.getInStorage_person(),bean.getOutStorage_person(),bean.getLoss_person(),bean.getInStorage_date(),bean.getOutStorage_date(),bean.getLoss_date(),bean.getLastModified_date(),bean.getNotes(),bean.getId()});
        return true;
    }
    public boolean deleteKey(KeyBean bean){
        jdbcTemplate.update(sql_deleteKey,new Object[]{bean.getId()});
        return true;
    }
    public Object beanToObject(KeyBean bean){
        Object object = new Object();
        return object;
    }
}
