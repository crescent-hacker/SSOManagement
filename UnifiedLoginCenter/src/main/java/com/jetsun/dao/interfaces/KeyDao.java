package com.jetsun.dao.interfaces;

import com.jetsun.bean.biz.KeyBean;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2014/11/18.
 */
public interface KeyDao {
    public List<Map<String,Object>> searchKey_inStorage();
    public List<Map<String,Object>> searchKey_outStorage();
    public List<Map<String,Object>> searchKey_storage();
    public List<Map<String,Object>> searchKey_loss();
    public List<Map<String,Object>> searchById(KeyBean bean);
    boolean updateSign(KeyBean keyBean);
    boolean insertKey(KeyBean keyBean);
    boolean updateKey(KeyBean keyBean);
    boolean deleteKey(KeyBean keyBean);
}
