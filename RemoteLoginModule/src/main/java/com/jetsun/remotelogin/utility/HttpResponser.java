package com.jetsun.remotelogin.utility;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/7/1
 * Desc:
 */
public class HttpResponser {

    /**
     * 返回对象到ajax
     * @param response
     * @param isSuccess
     * @throws Exception
     */
    public static void rspToAjax(HttpServletResponse response,boolean isSuccess) throws Exception{
        //创建返回JSON对象
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("success",isSuccess);
        //写回JSON
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map));
        response.getWriter().flush();
    }

    /**
     * 返回json对象到页面
     * @throws Exception
     */
    public static void rspToAjaxWithMap(HttpServletResponse response,Map<String,Object> map,boolean isSuccess) throws Exception{
        //获取模板html
        //创建返回JSON对象
        ObjectMapper mapper = new ObjectMapper();
        map.put("success",isSuccess);
        //写回JSON
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map));
        response.getWriter().flush();
    }
    /**
     * 返回json对象到页面
     * @throws Exception
     */
    public static void rspToAjaxWithObj(HttpServletResponse response,Object obj) throws Exception{
    	//获取模板html
    	//创建返回JSON对象
    	ObjectMapper mapper = new ObjectMapper();
    	//写回JSON
    	response.setCharacterEncoding("utf-8");
    	response.getWriter().write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
    	response.getWriter().flush();
    }


}
