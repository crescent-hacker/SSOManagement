package com.jetsun.remotelogin.filter;

import com.jetsun.remotelogin.bean.SessionKey;
import com.jetsun.remotelogin.utility.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/11/18
 * Desc:判断是否安卓调用
 */
public class AndroidFilter implements Filter {
    private Logger logger = LoggerFactory.getLogger(AndroidFilter.class);

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpSession session = request.getSession();

        //ip
        String ip = (String) session.getAttribute(SessionKey.IP_ADDRESS);

        //判断是否安卓远程调用
        boolean isAndroidInvoke = Validator.isAndroidInvoke(ip, request.getRequestURI(), request.getContextPath());

        session.setAttribute(SessionKey.IS_ANDROID_INVOKE, isAndroidInvoke);
        if(isAndroidInvoke) {
            logger.debug("android invoke~result:" + isAndroidInvoke);
        }
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }


}
