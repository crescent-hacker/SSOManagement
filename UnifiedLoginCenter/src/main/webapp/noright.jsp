<%@ page contentType="text/html; charset=UTF-8" import="java.util.*" %>
<%
    String webPath = request.getScheme()+"://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=8" />
<title>没有权限</title>
<link rel="stylesheet" type="text/css" href="<%=webPath%>/static/css/style.css">
<script type="text/javascript" src="<%=webPath%>/static/js/common/jquery-1.10.2.js"></script>
</head>
<body style="background-color: #b9babb;">
<table border="0" width="100%" id="table1" cellspacing="0" cellpadding="0" height="80%">
  <tr>
    <td valign="middle">
<div align='center'>
<div><img src="<%=webPath%>/static/images/bg_role_error.png" style="height:764px;"/></div>
<div style="margin-top:-250px;">
<div style="margin-bottom: 20px;"><span style="font-size: 26px;color: #515151;">抱歉···当前您没有访问权限，请联系管理员</span><br></div>
<div style="margin-bottom: 20px;"><span style="font-size: 22px;color: #515151;"><a style="color: #FF7A00;text-decoration: none;" id="ahref" href="<%=webPath%>/index.jsp">返回首页</a></span></div>
</div>
</div>
</td>
  </tr>
</table>
</body>
</html>