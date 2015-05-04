<%@ page contentType="text/html; charset=UTF-8" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page import="com.jetsun.bean.common.SessionKey" %>
<%
    String webPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=8"/>
    <title>登录</title>
    <script>var webRoot = "<%=webPath%>";</script>
    <script src="<%=webPath%>/static/js/common/jquery-1.10.2.js" type="text/javascript"></script>
    <!--表格样式-->
    <script src="<%=webPath%>/static/js/common/jquery.cookie.js" type="text/javascript"></script>
    <link rel="stylesheet" type="text/css" href="<%=webPath%>/static/css/style.css">
    <script src="<%=webPath%>/static/js/common/cert_login.js" type="text/javascript"></script>
    <script src="<%=webPath%>/static/js/common/logout.js" type="text/javascript"></script>
    <script src="<%=webPath%>/static/js/common/newAlert.js" type="text/javascript"></script>
    <script src="<%=webPath%>/static/js/common/crypt/md5.js" type="text/javascript"></script>
    <script type="text/javascript">var userNo = '<%=session.getAttribute(SessionKey.OPER_NO)%>';</script>
</head>
<body style="background: rgb(223, 223, 223)">
<div style="height: 79px;background:#005e84;">
    <div style="height: 138px;width: 180px; float: left;"></div>
    <div style="float: left;">
        <div style="height: 79px;width: 338px; float: left; background-image: url('<%=webPath%>/static/images/bg_head.png');"></div>
        <!-- <div id="hSystemName" style="margin-left: -100px;margin-top: 20px;float: left;">"三目"管理系统</div> -->
        <div id="hSystemName" style="margin-left: -100px;margin-top: 20px;float: left;font-size: 20px;font-family: simhei;">
        	广东省医疗保险<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;结算服务管理平台</div>
        <div style="float: left;height: 35px;margin-left: 450px;margin-top: 20px;font-size: 30px;color: #ffffff">统一登陆平台</div>
    </div>
</div>
<div style="position: absolute;left: 50%;top: 50%;margin-left: -513px;margin-top: -200px;">
    <div style="height: 400px;width: 1027px; float: left;">
        <img src="<%=webPath%>/static/images/bg_login.png" style="height: 400px">
    </div>
    <div style=" height: 333px;width: 398px; float: left;margin-top: 60px;margin-left: -400px;">

        <div style="margin-top: 50px; float: left;margin-left: 40px;"><input id="username" name="username" size="28px;"
                                                                             value="<%=session.getAttribute(SessionKey.OPER_NAME)%>"
                                                                             disabled
                                                                             style="border:1px solid #b4b4b4;height: 45px;line-height:45px; font-size: 18px;width: 250px;padding-left: 35px;width:210px \9;"/>
        </div>
        <div style="margin-top: 60px; float: left;margin-left: -240px;"><img
                src="<%=webPath%>/static/images/bg_username.png" style="height: 27px;"/></div>
        <div style="margin-top: 20px; float: left;margin-left: 40px;"><input id="password" type="password"
                                                                             name="password" size="28px;"
                                                                             style="border:1px solid #b4b4b4;height: 45px;line-height:43px \9; font-size: 30px;font-size: 18px \9; width: 250px;padding-left: 35px;width:210px \9;"/>
        </div>
        <div style="margin-top: 30px; float: left;margin-left: -240px;"><img
                src="<%=webPath%>/static/images/bg_password.png" style="height: 27px;"/></div>

        <%--<div style="margin-top: 20px; margin-left: 40px; width: 200px; height: 42px; float: left;">--%>
        <%--<input type="checkbox" id="check">--%>
        <%--<input type="hidden" value="1" id="setusername" name="setusername"/>--%>
        <%--<a style="color: #9e9e9e; font-size: 16px;">记住用户名</a>--%>
        <%--</div>--%>
        <%----%>
        <div style="margin-top: 40px; margin-left: 40px; width: 3180px; height: 45px; float: left;">
            <img id="landbutton" onmouseover="loginover()" onmouseout="loginout()" onmousedown="logindown()"
                 onmouseup="loginover();" onclick="cert_loginclick()" src="<%=webPath%>/static/images/btn_login.png"/>
        </div>
    </div>
</div>
<jsp:include page="page/pageFoot.jsp" flush="true"/>
</body>
</html>