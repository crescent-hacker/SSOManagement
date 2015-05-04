package com.jetsun.action.key;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jetsun.action.BaseAction;
import com.jetsun.bean.biz.KeyBean;
import com.jetsun.dao.interfaces.KeyDao;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * Created by Administrator on 2014/11/18.
 */
public class KeyAction extends BaseAction {
    @Autowired
    private KeyDao keyDao;

    /**
     * jsp页面数据传输格式应该类似
     * $.ajax{
     *     type:"POST"
     *     url:...!work.action
     *     data:{
     *         actionName:...传输去work()中的对应部分
     *         bean:...要传输的bean数据
     *     }
     *     success:function(data){
     *
     *     }
     * }
     *
     * @return response返还数据，success和装有数据的List<List<String>>
     */
    public String work(){
        String actionName=request.getParameter("actionName").toString();
        if (actionName.equals("insert")) {
            KeyBean bean = changeToBean(request);
            keyDao.insertKey(bean);
            backToJsp();
        }
        else if (actionName.equals("update")) {
            KeyBean bean = changeToBean(request);
            keyDao.updateKey(bean);
            backToJsp();
        }
        else if (actionName.equals("delete")){
            KeyBean bean = changeToBean(request);
            keyDao.deleteKey(bean);
            backToJsp();
        }
        else if (actionName.equals("searchById")){
            KeyBean bean = changeToBean(request);
            List<List<String>> list =changeToList(keyDao.searchById(bean));
            backToJsp(list);
        }
        else if (actionName.equals("search_inStorage")){
            List<List<String>> list =changeToList(keyDao.searchKey_inStorage());
            backToJsp(list);
        }
        else if (actionName.equals("search_outStorage")){
            List<List<String>> list =changeToList(keyDao.searchKey_outStorage());
            backToJsp(list);
        }
        else if (actionName.equals("search_storage")){
            List<List<String>> list =changeToList(keyDao.searchKey_storage());
            backToJsp(list);
        }
        else if (actionName.equals("search_loss")){
            List<List<String>> list =changeToList(keyDao.searchKey_loss());
            backToJsp(list);
        }
        else if (actionName.equals("updateSign")){
            KeyBean bean = changeToBean(request);
            keyDao.updateSign(bean);
            backToJsp();
        }
        return null;
    }

    /**
     * 不带数据返回jsp
     * 返回参数success
     */
    private void backToJsp(){
        Map<String,Object> data = new HashMap<String, Object>();
        data.put("success",true);
        response.setCharacterEncoding("utf-8");
        ObjectMapper mapper = new ObjectMapper();
        try {
            response.getWriter().write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data));
            response.getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 带Object数据返回jsp
     * 返回参数success
     */
    private void backToJsp(Object list) {
        Map<String,Object> data = new HashMap<String, Object>();
        data.put("success",true);
        data.put("keyList",list);
        response.setCharacterEncoding("utf-8");
        ObjectMapper mapper = new ObjectMapper();
        try {
            response.getWriter().write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data));
            response.getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 把数据结构为List<Map<String,Object>>的数据转换为List<List<String>>
     * @param data
     * @return
     */
    public List<List<String>> changeToList(List<Map<String,Object>> data){
        List<List<String>> list1 = new ArrayList<List<String>>();
        for(int i=0;i<data.size();i++) {
            List<String> list2 = new ArrayList<String>();
            Map<String,Object> map = (Map) data.get(i);
            Iterator iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
                Object obj=map.get(iterator.next());
                if(obj!=null){
                    list2.add(obj.toString());
                }else{
                    list2.add("");
                }
            }
            list1.add(list2);
        }
        return list1;
    }

    /**
     * 把request带来的json中含bean[]的数据转化为KeyBean类型
     * @param request
     * @return
     */
    private KeyBean changeToBean(HttpServletRequest request){
        Map<String,String[]> map = request.getParameterMap();
        KeyBean bean = new KeyBean();
        Enumeration e = request.getParameterNames();
        while (e.hasMoreElements()){
            String thisName = e.nextElement().toString();
            String thisValue = request.getParameter(thisName);
            //去掉bean[]
            if(thisName.length()>5) {
                thisName = thisName.substring(5, thisName.length() - 1);
                bean.setBean(thisName, thisValue);
            }
        }
        return bean;
    }
}
