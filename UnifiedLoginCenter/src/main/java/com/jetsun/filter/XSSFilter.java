package com.jetsun.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/9/1
 * Desc:进行跨站脚本和html注入过滤
 */
public class XSSFilter implements Filter {
    private Logger logger = LoggerFactory.getLogger(XSSFilter.class);

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws ServletException, IOException {
        //初始化
        HttpServletRequest request = (HttpServletRequest) req;
        XSSHttpServletRequestWrapper requestWrapper;
        String contentType = request.getContentType();
        if (contentType == null || !contentType.contains("multipart/form-data")) {
            requestWrapper = new XSSHttpServletRequestWrapper(request);
        }else{
            chain.doFilter(request, resp);
            return;
        }

        chain.doFilter(requestWrapper, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
