package com.jetsun.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jetsun.bean.common.EventModel;
import com.jetsun.bean.common.LoginRspKey;

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
     * 返回json对象到页面
     * @param ftlPath
     * @param response
     * @param event
     * @throws Exception
     */
    public static void rspHtmlToHttp(String ftlPath,HttpServletResponse response,EventModel event,boolean isSuccess) throws Exception{
        //获取模板html
        String html = FreeMarker.processContent(ftlPath , event);
        //创建返回JSON对象
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put(LoginRspKey.SUCCESS,isSuccess);
        map.put("html",html);
        //写回JSON
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map));
        response.getWriter().flush();
    }
    /**
     * 返回json对象到页面,附带分页条
     * @param ftlPath
     * @param response
     * @param event
     * @throws Exception
     */
    public static void rspHtmlToHttpWithPagin(String ftlPath,HttpServletResponse response,EventModel event,boolean isSuccess) throws Exception{
        //获取模板html
        String html = FreeMarker.processContent(ftlPath , event);
        String paginHtml = FreeMarker.processContent("/WEB-INF/ftl/page/pagination.ftl" , event);
        //创建返回JSON对象
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put(LoginRspKey.SUCCESS,isSuccess);
        map.put("html",html);
        map.put("pagination_html",paginHtml);
        //写回JSON
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map));
        response.getWriter().flush();
    }

    /**
     * 返回对象到ajax
     * @param response
     * @param isSuccess
     * @throws Exception
     */
    public static void rspToHttp(HttpServletResponse response,boolean isSuccess) throws Exception{
        //创建返回JSON对象
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put(LoginRspKey.SUCCESS,isSuccess);
        //写回JSON
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map));
        response.getWriter().flush();
    }

    /**
     * 返回json对象到页面,附带分页条
     * @param response
     * @param event
     * @throws Exception
     */
    public static void rspToHttpWithPagin(HttpServletResponse response,EventModel event,boolean isSuccess) throws Exception{
        //获取模板html
        String paginHtml = FreeMarker.processContent("/WEB-INF/ftl/page/pagination.ftl" , event);
        //创建返回JSON对象
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put(LoginRspKey.SUCCESS,isSuccess);
        map.put("pagination_html",paginHtml);
        //写回JSON
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map));
        response.getWriter().flush();
    }

    /**
     * 返回json对象到页面,附带分页条
     * @param response
     * @param event
     * @throws Exception
     */
    public static void rspToHttpWithMapAndPagin(HttpServletResponse response,Map<String,Object> map,EventModel event,boolean isSuccess) throws Exception{
        //获取模板html
        String paginHtml = FreeMarker.processContent("/WEB-INF/ftl/page/pagination.ftl" , event);
        //创建返回JSON对象
        ObjectMapper mapper = new ObjectMapper();
        map.put(LoginRspKey.SUCCESS,isSuccess);
        map.put("pagination_html",paginHtml);
        //写回JSON
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map));
        response.getWriter().flush();
    }

    /**
     * 返回json对象到页面
     * @throws Exception
     */
    public static void rspToHttpWithMap(HttpServletResponse response,Map<String,Object> map,boolean isSuccess) throws Exception{
        //获取模板html
        //创建返回JSON对象
        ObjectMapper mapper = new ObjectMapper();
        map.put(LoginRspKey.SUCCESS,isSuccess);
        //写回JSON
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map));
        response.getWriter().flush();
    }
    /**
     * 返回json对象到页面
     * @throws Exception
     */
    public static void rspToHttpWithObj(HttpServletResponse response,Object obj) throws Exception{
    	//获取模板html
    	//创建返回JSON对象
    	ObjectMapper mapper = new ObjectMapper();
    	//写回JSON
    	response.setCharacterEncoding("utf-8");
    	response.getWriter().write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
    	response.getWriter().flush();
    }


}
