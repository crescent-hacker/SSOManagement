<%@ page import="com.jetsun.bean.common.SessionKey" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String webPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    response.sendRedirect(webPath+"/?pid=91");
//    String message;
//    if (session.getAttribute(SessionKey.USER_ID) != null) {
//        message = "用户已登陆，登陆用户为:<b>" + session.getAttribute(SessionKey.OPER_NAME)+"</b>";
//    } else {
//        message = "用户尚未登陆!"+"<a href='"+webPath+"/cert_login.jsp'>登陆</a>";
//    }
%>
<%--<div style="position:absolute;left:50%;top:50%;width:300px;height:300px;margin-top:-150px;margin-left: -150px">--%>
    <%--<%=message%><br>--%>
    <%--<ol>--%>
        <%--<li><a href="<%=webPath%>/?pid=91">用户管理</a></li>--%>
        <%--<li><a href="<%=webPath%>/?pid=92">证书管理</a></li>--%>
        <%--<li><a href="<%=webPath%>/?pid=93">权限管理</a></li>--%>
        <%--<li><a href="<%=webPath%>/?pid=94">角色管理</a></li>--%>
    <%--</ol>--%>
<%--</div>--%>