package com.jetsun.remotelogin.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jetsun.remotelogin.bean.CommonVariable;
import com.jetsun.remotelogin.bean.LoginRspKey;
import com.jetsun.remotelogin.bean.SessionKey;
import com.jetsun.remotelogin.bean.UlcProperty;
import com.jetsun.remotelogin.dao.interfaces.LoginHandler;
import com.jetsun.remotelogin.utility.SessionUtil;
import com.jetsun.remotelogin.utility.StringUtil;
import com.jetsun.remotelogin.utility.Validator;
import com.jetsun.remotelogin.utility.property.UlcPropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 过滤器，处理登录用户对jsp等页面的请求。
 * 用户已经登录，转到相应的jsp页面执行，否则提示错误页面。
 *
 * @author cdf
 * @Date 2014-8-14
 */
public class AccessControlFilter implements Filter {
    /**
     * 日志记录器
     */
    private Logger logger = LoggerFactory.getLogger(com.jetsun.remotelogin.filter.AccessControlFilter.class);

    @Override
    public void destroy() {
    }

    /**
     * 主方法-对jsp页面进行过滤
     *
     * @param req
     * @param rsp
     * @param filterChain
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse rsp,
                         FilterChain filterChain) throws IOException, ServletException {
        //初始化
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) rsp;
        Map<String, Object> procMap = null;//存储过程返回map
        HttpSession session = request.getSession();//获得session
        ServletContext servletContext = session.getServletContext();//上下文
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);//Spring上下文
        String ip = (String) session.getAttribute(SessionKey.IP_ADDRESS);
        String webPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();//绝对地址前缀
        CommonVariable.WEB_PATH = webPath;//全局变量，java拼装绝对地址用
        //登陆处理器初始化
        LoginHandler loginHandler = (LoginHandler) ctx.getBean("loginHandler");

        // 取得根目录所对应的绝对路径
        String currentURL = request.getRequestURI();

        //-----------------------------开始特殊页面跳转判断---------------------------------------
        //不需要过滤的页面判断
        boolean isCertErrorJsp = currentURL.contains(UlcPropertyUtil.getProperty(UlcProperty.CERT_ERROR_PAGE));
        boolean isNoRightJsp = currentURL.contains(UlcPropertyUtil.getProperty(UlcProperty.NO_RIGHT_PAGE));
        boolean is404Jsp = currentURL.contains(UlcPropertyUtil.getProperty(UlcProperty.E_404_PAGE));
        boolean isErrorJsp = currentURL.contains(UlcPropertyUtil.getProperty(UlcProperty.ERROR_PAGE));
        boolean isIndexJsp = currentURL.contains(UlcPropertyUtil.getProperty(UlcProperty.INDEX_PAGE));

        //静态文件路径跳转
        if (Validator.isStaticPath(currentURL,request.getContextPath())) {
            filterChain.doFilter(request, response);
            return;
        }

        //安卓远程调用路径跳转
        if ((Boolean)session.getAttribute(SessionKey.IS_ANDROID_INVOKE)) {
            filterChain.doFilter(request, response);
            return;
        }

        //输出日志
        logger.debug("visiting accessfilter：" + currentURL + " userid=" + session.getAttribute(SessionKey.USER_ID) + ";username=" + session.getAttribute(SessionKey.OPER_NAME) + ";ip=" + ip);

        //首页，证书错误页，无权限页，404页，500页，直接跳转
        if (isIndexJsp || isCertErrorJsp || isNoRightJsp || is404Jsp || isErrorJsp) {
            filterChain.doFilter(request, response);
            return;
        }
        //-----------------------------结束特殊页面跳转判断---------------------------------------

        //-----------------------------开始IP判断-------------------------------------------------
        if((Boolean)session.getAttribute(SessionKey.IP_ILLEGAL)){
            response.sendRedirect(request.getContextPath() + UlcPropertyUtil.getProperty(UlcProperty.NO_RIGHT_PAGE));
            return;
        }
        //-----------------------------结束IP判断-------------------------------------------------

        //-----------------------------开始证书判断---------------------------------------
        X509Certificate certificate = (X509Certificate) session.getAttribute(SessionKey.USER_LOGIN_CERTIFICATE);
        //没有登录证书，或登录证书状态不可用，或证书不在数据库中，或证书没有关联用户->则直接跳转到证书错误页面
        if (certificate == null || (Boolean) session.getAttribute(SessionKey.USER_LOGIN_CERT_IS_VALID) == false) {
            response.sendRedirect(request.getContextPath() + UlcPropertyUtil.getProperty(UlcProperty.CERT_ERROR_PAGE));
            return;
        }
        //证书序列号
        String certNo = certificate.getSerialNumber().toString();
        //-----------------------------结束证书判断---------------------------------------

        //-----------------------------开始登录情况判断---------------------------------------
        //如果是登陆页面，则取出用户名放入session，给页面显示用；登陆页面跳过单点登录
        if (currentURL.contains(UlcPropertyUtil.getProperty(UlcProperty.LOGIN_PAGE))) {
            if (session.getAttribute(SessionKey.OPER_NAME) == null) {//用户名为空，第一次取用户名
                Map<String, Object> userMap = loginHandler.getUserNameAndWorkNo(certNo,session);
                if (userMap!=null&&(Boolean) userMap.get(LoginRspKey.SUCCESS)) {//成功用证书获取用户名
                    String operName = (String) userMap.get(LoginRspKey.OPER_NAME);
                    String operNo = (String) userMap.get(LoginRspKey.OPER_NO);
                    session.setAttribute(SessionKey.OPER_NAME, operName);
                    session.setAttribute(SessionKey.OPER_NO, operNo);
                } else {//如果为空则说明当前证书在信任库中，但无绑定用户，相当于无权限
                    response.sendRedirect(request.getContextPath() + UlcPropertyUtil.getProperty(UlcProperty.NO_RIGHT_PAGE));
                    return;
                }
            }
            filterChain.doFilter(request, response);
            return;
        }
        //如果未登录，检查是否SSO登陆
        if (session.getAttribute(SessionKey.USER_ID) == null) {
            //SSO登陆查询
            Map<String, Object> ssoMap = loginHandler.doLoginSSO(certNo, ip,session);
            String code = (ssoMap == null) ? "-99" : (String) ssoMap.get(LoginRspKey.O_ERRORCODE);
            String message = (ssoMap == null) ? "异常" : (String) ssoMap.get(LoginRspKey.O_ERRORMSG);
            logger.debug("SSO login~code=" + code + ";message=" + message);
            //判断SSO登陆情况
            if (ssoMap != null && "0".equals(code)) {
                //SSO登陆成功，把登陆信息放入session
                ssoLoginHandler(ssoMap, session);
            }
        }
        //-----------------------------结束单点登录判断---------------------------------------

        //-----------------------------开始普通页面跳转判断---------------------------------------
        //不用pid进来的jsp页面统统跳404
        if(currentURL.contains(".jsp")){
            response.sendRedirect(request.getContextPath() + UlcPropertyUtil.getProperty(UlcProperty.E_404_PAGE));
            return;
        }
        //进行页面翻译
        String pid = request.getParameter("pid");//页面id
        if ((StringUtil.isEmpty(currentURL) || (request.getContextPath() + "/").equals(currentURL)) && pid != null) {//保证符合http://xxx.com/?pid=xxxx的格式
            currentURL = loginHandler.getUrl(pid,session);//从数据库取出页面路径
            if (currentURL == null) {//找不到pid跳转404
                response.sendRedirect(request.getContextPath() + UlcPropertyUtil.getProperty(UlcProperty.E_404_PAGE));
                return;
            }
        }

        //如果非jsp请求，则直接抛给过滤器链(action)
        if (!currentURL.contains(".jsp")) {
            filterChain.doFilter(request, response);
            return;
        }
        //-----------------------------结束普通页面跳转判断---------------------------------------

        //-----------------------------开始权限判断---------------------------------------
        //未登陆则跳转登陆页面
        if (session.getAttribute(SessionKey.USER_ID) == null) {
            //单点登陆不成功，用户需要重新登陆
            response.sendRedirect(request.getContextPath() + UlcPropertyUtil.getProperty(UlcProperty.LOGIN_PAGE));
            return;
        }
        //提交参数
        String paramStr = setRedirectParam(request);
        //用存储过程进行权限判断
        try {
            procMap = loginHandler.checkUserAuthority((String) session.getAttribute(SessionKey.USER_ID), (String) session.getAttribute(SessionKey.LOGIN_CODE), "1", currentURL, paramStr, ip,currentURL,session);
        } catch (Exception e) {
            logger.error("filter checkAuthority failed", e);
        }
        if(procMap==null){
            logger.debug("call checkAuthority error,procMap null");
            response.sendRedirect(request.getContextPath() + UlcPropertyUtil.getProperty(UlcProperty.NO_RIGHT_PAGE));
            return;
        }
        String code = (String) procMap.get(LoginRspKey.O_ERRORCODE);
        logger.debug("checkAuthority result：" + code + "~" + procMap.get(LoginRspKey.O_ERRORMSG));

        if(!code.equals("0")) {
            //该用户已被别的ip登陆
            if (code.equals("-10")) {
                logger.info("ip:" + ip + " user has been kicked out by other ip user in the same account.");
                response.sendRedirect(request.getContextPath() + UlcPropertyUtil.getProperty(UlcProperty.LOGIN_PAGE)+"?c");
                return;
            }
            //没权限，跳转错误页
            if (code.equals("-1")) {
                response.sendRedirect(request.getContextPath() + UlcPropertyUtil.getProperty(UlcProperty.NO_RIGHT_PAGE));
                return;
            }
            //剩下的都跳去404
            response.sendRedirect(request.getContextPath() + UlcPropertyUtil.getProperty(UlcProperty.E_404_PAGE));
            return;
        }
        //-----------------------------结束权限判断---------------------------------------

        //记录当前访问页面
        session.setAttribute(SessionKey.CURRENT_PAGE,currentURL);
        // jsp内部跳转
        request.getRequestDispatcher(currentURL).forward(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    /**
     * 辅助方法-拼装未登陆前的参数列表到session里
     */
    private String setRedirectParam(HttpServletRequest request) {

        StringBuffer sTemp = new StringBuffer();
        boolean bFirst = true;

        Enumeration<String> enu = request.getParameterNames();
        if (enu.hasMoreElements()) {
            while (enu.hasMoreElements()) {
                if (bFirst == false) {
                    sTemp.append(",");
                } else {
                    bFirst = false;
                }
                String paraName = enu.nextElement();
                sTemp.append(paraName).append("=").append(request.getParameter(paraName));
            }
        }

        String retStr = sTemp.toString();
        int limit = 1000;
        if(retStr.length()>limit){
            retStr = retStr.substring(0,limit);
        }
        return retStr;
    }

    /**
     * 辅助方法-把sso登陆查询信息放入session
     */
    private void ssoLoginHandler(Map<String, Object> ssoMap, HttpSession session) throws IOException {
        //取出存储过程返回结果
        String sJson = (String) ssoMap.get(LoginRspKey.O_STRS_JSON);
        //读取存储过程返回的json
        Map<String, String> loginInfoMap = new ObjectMapper().readValue(sJson, HashMap.class);
        //把sso登陆信息放入session
        SessionUtil.loginSSOSessionHandler(loginInfoMap, session);
    }
}
