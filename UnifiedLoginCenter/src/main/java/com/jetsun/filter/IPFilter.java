package com.jetsun.filter;

import com.jetsun.bean.common.SessionKey;
import com.jetsun.utility.StringUtil;
import com.jetsun.utility.property.IPPropertyUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/9/1
 * Desc:进行真实ip判断
 */
public class IPFilter implements Filter {
    private Logger logger = LoggerFactory.getLogger(IPFilter.class);

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;

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
        session.setAttribute(SessionKey.IP_ADDRESS, ip);

        if (!checkIp(ip)) {
            logger.debug("IP为" + ip + "的用户没有系统进入权限");
            session.setAttribute(SessionKey.IP_ILLEGAL, true);
        } else {
//            logger.debug("IP为"+ip+"的用户通过ip检测");
            session.setAttribute(SessionKey.IP_ILLEGAL, false);
        }

        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

    /**
     * 辅助方法-检验ip
     */
    private boolean checkIp(String remoteIp) {
        //检查模式
        int mode = Integer.parseInt(IPPropertyUtil.getProperty(IPPropertyUtil.IP_CHECK_MODE));
        if (mode == 0) {//通配模式
            String patternStr = IPPropertyUtil.getProperty(IPPropertyUtil.IP_CHECK_PATTERN);
            if (StringUtil.isEmpty(patternStr)) {//为空不检查
                return true;
            }
            String[] patterns = patternStr.split("\\|");
            for (String pattern : patterns) {//逐个通配符进行匹配
                if (remoteIp.startsWith(pattern)) {
                    return true;
                }
            }
        }
        if (mode == 1) {//白名单模式
            String ipStr = IPPropertyUtil.getProperty(IPPropertyUtil.IP_WHITE_LIST);
            if (StringUtil.isEmpty(ipStr)) {//为空则不允许任何ip进入
                return false;
            }
            String[] ips = ipStr.split("\\|");
            for (String ip : ips) {//逐个通配符进行匹配
                if (remoteIp.equals(ip)) {
                    return true;
                }
            }
        }
        return false;
    }
}
