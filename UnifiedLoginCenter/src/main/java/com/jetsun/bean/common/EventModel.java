package com.jetsun.bean.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * freemarker值模型
 */
public class EventModel extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;
    String contexturl;
    HttpSession session;
    HttpServletRequest request;
    HttpServletResponse response;
    private Map<String, Object> requesetMap;
    boolean result;
    private String retMsg = "";
    private int resultCode;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public EventModel() {
        super();
    }
    public EventModel(String contextUrl) {
        this.contexturl = contextUrl;
    }

    public EventModel(
            HttpServletRequest request,
            HttpServletResponse response) {
        super();
        this.request = request;
        this.response = response;
//        initMap();
    }

    private void initMap() {
        requesetMap = new HashMap<String, Object>();       
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }   

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    

    public Map<String, Object> getRequesetMap() {
        if (requesetMap == null) {
            initMap();
        }
        return requesetMap;
    }

    public void setRequesetMap(Map<String, Object> requesetMap) {
        this.requesetMap = requesetMap;
    }

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	public String getContexturl() {
		return contexturl;
	}

	public void setContexturl(String contexturl) {
        this.contexturl = contexturl;
	}


   
}