package com.jetsun.filter;

import com.jetsun.bean.common.SessionKey;
import com.jetsun.bean.common.SysProperty;
import com.jetsun.dao.interfaces.LoginDao;
import com.jetsun.utility.SessionUtil;
import com.jetsun.utility.property.SysPropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/8/7
 * Desc:获得用户证书并进行处理
 */
public class ClientCertFilter implements Filter {
    private Logger logger = LoggerFactory.getLogger(ClientCertFilter.class);

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws ServletException, IOException {
        //初始化
        HttpServletRequest request = (HttpServletRequest) req;
        HttpSession session = request.getSession();
        boolean isCertValid = false;

        //远程调用不检查证书（子系统无此代码）
        if(((HttpServletRequest) req).getRequestURI().startsWith(request.getContextPath()+ SysPropertyUtil.getProperty(SysProperty.REMOTE_LOGIN_PREFIX))){
            chain.doFilter(request, resp);
            return;
        }

        //判断是否已经存在证书
        X509Certificate sessionCert = (X509Certificate) session.getAttribute(SessionKey.USER_LOGIN_CERTIFICATE);

        //获得证书链
        X509Certificate certChain[] = (X509Certificate[]) request.getAttribute("javax.servlet.request.X509Certificate");
        // 请求中是否有证书
        if (certChain == null || certChain.length == 0) {
            logger.debug("当前用户没有提交任何证书！");
        } else {//证书存在
            try {
                X509Certificate cert = certChain[certChain.length - 1];
                //检查是否有效，无效抛出exception
                cert.checkValidity();
                //判断当前证书跟session是否相同，相同则直接抛给过滤连，不相同则为更改了用户
                if (sessionCert != null && cert.getSerialNumber().equals(sessionCert.getSerialNumber())) {
                    isCertValid = true;
                    chain.doFilter(req, resp);
                    return;
                }
                //未登陆或者切换了证书->清除session
                SessionUtil.clearSession(session);
                //把证书放入session
                session.setAttribute(SessionKey.USER_LOGIN_CERTIFICATE, cert);

                //dao初始化
                ServletContext servletContext = session.getServletContext();//上下文
                ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);//Spring上下文
                LoginDao loginDao = (LoginDao) ctx.getBean("loginDao");//dao初始化

                //查库检查证书是否存在和关联了用户
                isCertValid = loginDao.isCertExist(cert.getSerialNumber().toString());

            } catch (CertificateExpiredException e) {
                //证书已过期
                logger.error("证书已经过期", e);
            } catch (CertificateNotYetValidException e) {
                //证书未生效
                logger.error("证书未生效", e);
            } finally {
                session.setAttribute(SessionKey.USER_LOGIN_CERT_IS_VALID, isCertValid);
            }
        }

        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
