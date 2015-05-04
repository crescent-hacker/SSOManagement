<%@ page contentType="text/html; charset=UTF-8" import="java.util.*" %>
<%
    String webPath = request.getScheme()+"://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=8" />
<title>找不到该页</title>
<link rel="stylesheet" type="text/css" href="<%=webPath%>/static/css/style.css">
<script type="text/javascript" src="<%=webPath%>/static/js/common/jquery-1.10.2.js"></script>
</head>
<body style="background-color: #ecedef;">
<table border="0" width="100%" id="table1" cellspacing="0" cellpadding="0" height="80%">
  <tr>
    <td valign="middle">
<div align='center'>
<div><img src="<%=webPath%>/static/images/page_not_found.png" style="height:628px;margin-top: 20px;"/></div>
<div style="margin-top:-300px;margin-left: 150px;">
<div style="margin-left: -150px;margin-bottom: 20px;"><span style="font-size: 67px;font-style: italic;margin-right: 10px;">Sorry!</span><span style="font-size: 52px;">你访问的页面不存在!</span></div>
<div style="margin-bottom: 20px;"><span style="font-size: 26px;">我们会尽快查找原因，提供您所需要的页面。</span><br></div>
<div style="margin-bottom: 20px;"><span style="font-size: 22px;"><a style="color: #FF7A00;text-decoration: none;" id="ahref" href="<%=webPath%>/index.jsp">返回首页</a></span></div>
</div>
</div>
</td>
  </tr>
</table>
</body> 
</html>