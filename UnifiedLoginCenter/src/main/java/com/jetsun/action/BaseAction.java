package com.jetsun.action;

import com.jetsun.bean.common.EventModel;
import com.jetsun.bean.common.SessionKey;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/7/4
 * Desc:
 */
public abstract class BaseAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {
    /**
     * 日志记录器
     */
    protected Logger logger = LoggerFactory.getLogger(BaseAction.class);
    /**
     * 请求对象
     */
    protected HttpServletRequest request;
    /**
     * 响应对象
     */
    protected HttpServletResponse response;
    /**
     * 取得session
     */
    protected Map<String, Object> session = ActionContext.getContext().getSession();
    /**
     * freemarker参数模型
     */
    protected EventModel event;

    /**
     * 返回的map
     */
    protected Map<String, Object> returnMap = new HashMap<String, Object>();

    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
        //分页处理
        if (session.get(SessionKey.EVENT_MODEL) == null) {
            event = new EventModel(request.getSession().getServletContext().getRealPath("/"));
        }else {
            event = (EventModel)session.get(SessionKey.EVENT_MODEL);
        }
        if (request.getParameter("pageIndex") != null) {
            int pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
            event.put("pageIndex", pageIndex);
        }
        session.put(SessionKey.EVENT_MODEL, event);
    }
}
