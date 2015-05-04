<%@ page contentType="text/html; charset=UTF-8" import="java.util.*" %>
<%
String type=request.getParameter("title")==null?"":request.getParameter("title").toString();
String mainTitle=request.getParameter("mainTitle")==null?"":request.getParameter("mainTitle").toString();
 %>
<div id="navigation_bg">
<div id="navigation_name"><%=type %><label class='navigation_current'><%=mainTitle %></label></div>
<div id="navigation_line"></div>
</div>
