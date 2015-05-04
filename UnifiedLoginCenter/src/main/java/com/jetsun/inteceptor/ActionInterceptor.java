package com.jetsun.inteceptor;

import com.jetsun.action.login.LoginAction;
import com.jetsun.action.login.LogoutAction;
import com.jetsun.bean.common.AppKey;
import com.jetsun.bean.common.LoginReqKey;
import com.jetsun.bean.common.LoginRspKey;
import com.jetsun.bean.common.SessionKey;
import com.jetsun.dao.interfaces.LoginDao;
import com.jetsun.utility.HttpResponser;
import com.jetsun.utility.StringUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 拦截器，对action进行权限判断
 *
 * @author cdf
 * @Date 2014-9-16
 */
public class ActionInterceptor extends AbstractInterceptor {

    /**
     * 日志记录器
     */
    private Logger logger = LoggerFactory.getLogger(ActionInterceptor.class);

    private static final long serialVersionUID = -7514844710933705067L;

    @Autowired
    private LoginDao loginDao;


    /**
     * 主方法-对action进行过滤
     */
    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        //初始化
        HttpServletRequest request = ServletActionContext.getRequest();
        Map<String, Object> session = invocation.getInvocationContext().getSession();
        Object action = invocation.getAction();
        Map<String, Object> map = invocation.getInvocationContext().getParameters();
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        String ip = (String)session.get(SessionKey.IP_ADDRESS);
        //获取action参数字符串
        String sOpt_details = getReqParamsStr(map);
        logger.debug("opt_detail=" + sOpt_details);

        //跳过登陆和登出action
        if (action instanceof LoginAction || action instanceof LogoutAction) {
            logger.debug("exit check login, because this is login action.");
            return invocation.invoke();
        }

        //检查当前用户是否已经登录。是，正常执行；否，提示登录页面
        String userId = (String) session.get(SessionKey.USER_ID);
        String loginCode = (String) session.get(SessionKey.LOGIN_CODE);    //登陆代码
        String nameSpace = invocation.getProxy().getNamespace();
        String actionName = ("/".equals(nameSpace)?"/":nameSpace+"/")+invocation.getInvocationContext().getName();
        String currentPage = StringUtil.transNull((String) session.get(SessionKey.CURRENT_PAGE));
        logger.debug("进到到拦截器-----action：" + actionName+" jsp:"+currentPage);

        //如果已经登录，则跳过检查修改密码和pin码的权限
        if (userId != null && (actionName.equals("ChangePW"))) {
            return invocation.invoke();
        }
        //判断是否子系统远程调用登陆:1.检验调用地址
        // （注：子系统中去除此段代码）
        if(invocation.getProxy().getNamespace().contains("remoteLogin")/*&&ip.contains("192.168.0.")*/){
            String systemId = request.getParameter(LoginReqKey.SYSTEM_ID);
            if(systemId==null||!loginDao.isSystemExist(Long.parseLong(systemId))){//系统id不正确
                return "noright";
            }else{
                return invocation.invoke();
            }
        }

        //处理请求
        if (isHttpRequest(request)) {//ajax请求
            Map<String, Object> ajaxMap = ajaxReqHandler(userId, loginCode, actionName, sOpt_details,ip,currentPage);
            if ("0".equals(ajaxMap.get("code"))) {
                logger.debug("权限判断成功:"+actionName);
                return invocation.invoke();//跳转到相应的action
            }
            HttpResponser.rspToHttpWithMap(response, ajaxMap, false);//返回错误信息到ajax
            return null;
        } else {//页面请求
            String retStr = pageReqHandler(userId, loginCode, actionName, sOpt_details,ip,currentPage);//返回跳转信息
            if ("executeAction".equals(retStr)) {
                logger.debug("权限判断成功:"+actionName);
                return invocation.invoke();
            }
            return retStr;
        }
    }

    /**
     * 辅助方法-判断是否ajax的请求
     *
     * @param request 请求对象
     */
    private boolean isHttpRequest(HttpServletRequest request) {
        String header = request.getHeader("X-Requested-With");
        boolean isHttp = (header != null && "XMLHttpRequest".equals(header));
        return isHttp;
    }

    /**
     * 辅助方法-获得请求参数的字符串形式
     *
     * @param map 请求参数map
     */
    private String getReqParamsStr(Map<String, Object> map) {
        //把map转换为字符串
        StringBuffer sb = new StringBuffer();
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            sb.append(entry.getKey().toString());//键
            String[] tmpStrs = (String[]) entry.getValue();//值
            sb.append("=").append(tmpStrs[0]).append(",");//拼装
        }
        //定义返回字符串,删除最后一位逗号
        String retStr = sb.length() > 0 ? sb.deleteCharAt(sb.length() - 1).toString() : "";
        //过长则进行截取
        int limit = 3900;
        if (retStr.length() > limit) {
            retStr = retStr.substring(0, limit);
        }
        return retStr;
    }

    /**
     * 辅助方法-处理ajax请求
     */
    private Map<String, Object> ajaxReqHandler(String userId, String loginCode, String actionName, String sOpt_details,String ip,String currentPage) throws Exception {
        Map<String, Object> returnResult = new HashMap<String, Object>();
        //未登陆，转到登陆页面
        if (userId == null) {
            returnResult.put("code", "-2");
            returnResult.put("message", "未登陆");
            return returnResult;
        }
        //调用存储过程判断权限
        Map<String, Object> procMap = loginDao.checkUserAuthority(userId, loginCode, "2", actionName, sOpt_details,ip,currentPage,AppKey.APP_KEY);//判断权限
        //存储过程返回结果空，没有权限
        if (procMap.isEmpty()) {
            returnResult.put("code", "-1");
            returnResult.put("message", "没有权限");
            return returnResult;
        }

        //权限判断结果
        if (procMap.get("O_ERRORCODE").equals("0")) {//有权限
            returnResult.put("code", "0");
        } else {//无权限
            returnResult.put("code", procMap.get("O_ERRORCODE"));
            returnResult.put("message", procMap.get("O_ERRORMSG"));
        }

        return returnResult;
    }

    /**
     * 辅助方法-处理页面提交请求
     */
    private String pageReqHandler(String userId, String loginCode, String actionName, String sOpt_details,String ip,String currentPage) throws Exception {
        //未登陆，转到登陆页面
        if (userId == null) {
            return "login";
        }
        //调用存储过程判断权限
        Map<String, Object> procMap = loginDao.checkUserAuthority(userId, loginCode, "2", actionName, sOpt_details,ip,currentPage,AppKey.APP_KEY);//判断权限
        //存储过程返回结果空，没有权限
        if (procMap.isEmpty()) {
            return "noright";
        }
        //权限判断结果
        if (procMap.get(LoginRspKey.O_ERRORCODE).equals("0")) {//有权限
            return "executeAction";
        } else {//无权限
            return "noright";
        }
    }
}
