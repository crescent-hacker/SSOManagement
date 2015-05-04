package com.jetsun.remotelogin.filter;

import com.jetsun.remotelogin.bean.SessionKey;
import com.jetsun.remotelogin.utility.StringUtil;
import com.jetsun.remotelogin.utility.Validator;
import com.jetsun.remotelogin.utility.property.IPPropertyUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/9/1
 * Desc:进行真实ip判断
 */
public class IPFilter implements Filter {
    private Logger logger = LoggerFactory.getLogger(com.jetsun.remotelogin.filter.IPFilter.class);

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String ip = request.getHeader("Cdn-Src-Ip");

//        logger.debug("Cdn-Src-Ip:" + ip);

        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {

            ip = request.getHeader("X-Forwarded-For");

//            logger.debug("X-Forwarded-For:" + ip);

        }

        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {

            ip = request.getHeader("Proxy-Client-IP");

//            logger.debug("Proxy-Client-IP:" + ip);

        }

        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {

            ip = request.getHeader("WL-Proxy-Client-IP");

//            logger.debug("WL-Proxy-Client-IP:" + ip);

        }

        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {

            ip = request.getHeader("HTTP_CLIENT_IP");

//            logger.debug("HTTP_CLIENT_IP:" + ip);

        }

        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {

            ip = request.getHeader("HTTP_X_FORWARDED_FOR");

//            logger.debug("HTTP_X_FORWARDED_FOR:" + ip);

        }

        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {

            ip = request.getRemoteAddr();

//            logger.debug("getRemoteAddr:" + ip);

        }

        HttpSession session = request.getSession();
        //用户ip
        session.setAttribute(SessionKey.IP_ADDRESS,ip);

        if(Validator.checkIp(ip)){
            session.setAttribute(SessionKey.IP_ILLEGAL,false);
        }else {
            logger.debug("IP:"+ip+" user has no right to enter system.");
            session.setAttribute(SessionKey.IP_ILLEGAL,true);
        }

        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }



}
