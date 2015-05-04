<%@ page contentType="text/html; charset=UTF-8" import="java.util.*" %>
<%
    String webPath = request.getScheme()+"://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=8" />
<title>服务器出错</title>
<link rel="stylesheet" type="text/css" href="<%=webPath%>/static/css/style.css">
</head>
<body style="background-color: #ecedef;">
<table border="0" width="100%" id="table1" cellspacing="0" cellpadding="0" height="80%">
  <tr>
    <td valign="middle">
<div align='center'>
<div><img src="<%=webPath%>/static/images/bg_error.png" style="margin-top: 20px;height: 386px;"/></div>
<div>
<div style="margin-top: 20px;"><span style="font-size: 47px;color: #454545;">哎呦！出错啦！</span></div>
<div style="margin-top: 20px;background-color: #454545;height: 2px;width: 90%;"></div>
<div style="margin-top: 60px;">
<%--<a href="" style="margin-left: -50px;"><img src="<%=webPath%>/static/images/btn_error_refresh.png" style="height: 80%;"/></a>--%>
<%--<a href="<%=webPath%>/index.jsp"><img src="<%=webPath%>/static/images/btn_error_back.png"/></a>--%>
<a href="<%=webPath%>/index.jsp"><img src="<%=webPath%>/static/images/btn_error_back.png"/></a>
</div>
</div>
</div>
</td>
  </tr>
</table>
</body> 
</html>