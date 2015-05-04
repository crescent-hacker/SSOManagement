<%@ page import="com.jetsun.utility.DateUtil" %>
<%
    //绝对路径：域名+端口+挂载路径
    String webPath = request.getScheme()+"://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    String format = "yyyy-MM-dd";
    String sbday = DateUtil.getDateByFormat(format, DateUtil.getMonthFirstDay(0));
    String seday = DateUtil.getTodayByFormat(format);
%>
<!--------------------------css文件引入------------------------------------>
<link rel="stylesheet" href="<%=webPath%>/static/css/ztreeStyle/zTreeStyle.css">
<link type="text/css" href="<%=webPath%>/static/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="<%=webPath%>/static/css/style.css"/>
<link rel="stylesheet" type="text/css" href="<%=webPath%>/static/css/style-ie.css"/>
<link rel="stylesheet" type="text/css" href="<%=webPath%>/static/css/popupModel.css">
<link rel="stylesheet" type="text/css" href="<%=webPath%>/static/css/search.css">
<link rel="stylesheet" href="<%=webPath%>/static/css/datePicker/all.css">

<!--------------------------js文件引入------------------------------------->
<script type="text/javascript" src="<%=webPath%>/static/js/common/jquery-1.10.2.js"></script>
<script src="<%=webPath%>/static/js/common/page/page.js" type="text/javascript"></script>
<script src="<%=webPath%>/static/js/common/page/pagination.js" type="text/javascript"></script>
<script src="<%=webPath%>/static/js/common/popupModel.js" type="text/javascript"></script>
<script src="<%=webPath%>/static/js/common/search.js" type="text/javascript"></script>
<script src="<%=webPath%>/static/js/common/newAlert.js" type="text/javascript"></script>
<script src="<%=webPath%>/static/js/common/validate.js" type="text/javascript"></script>
<script src="<%=webPath%>/static/js/common/json.js" type="text/javascript"></script>

<!--------------------------dhtmlxgrid表格样式与js------------------------->
<link rel="STYLESHEET" type="text/css" href="<%=webPath%>/static/css/dhtmlXGrid/dhtmlxgrid.css">
<link rel="stylesheet" type="text/css" href="<%=webPath%>/static/css/dhtmlXGrid/dhtmlxgrid_dhx_skyblue.css">
<script src="<%=webPath%>/static/js/common/dhtmlXGrid/dhtmlxcommon.js" type="text/javascript"></script>
<script src="<%=webPath%>/static/js/common/dhtmlXGrid/dhtmlxgrid.js" type="text/javascript"></script>
<script src="<%=webPath%>/static/js/common/dhtmlXGrid/dhtmlxgrid_srnd.js" type="text/javascript"></script>
<script src="<%=webPath%>/static/js/common/dhtmlXGrid/dhtmlxgridcell.js" type="text/javascript"></script>
<script src="<%=webPath%>/static/js/common/dhtmlXGrid/dhtmlXGrid_excell_link.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=webPath%>/static/js/common/bootstrap.min.js"></script>

<!------------------------日期控件与下拉框---------------------------------->
<script src="<%=webPath%>/static/js/common/datePicker/core.js"></script>
<script src="<%=webPath%>/static/js/common/datePicker/widget.js"></script>
<script src="<%=webPath%>/static/js/common/datePicker/position.js"></script>
<script src="<%=webPath%>/static/js/common/datePicker/menu.js"></script>
<script src="<%=webPath%>/static/js/common/datePicker/selectmenu.js"></script>
<script src="<%=webPath %>/static/js/common/datePicker/datepicker.js"></script>
<script src="<%=webPath %>/static/js/common/datePicker/datepicker-zh-TW.js"></script>

<!------------------------ztree的js---------------------------------->
<script src="<%=webPath%>/static/js/common/ztree/jquery.ztree.core-3.5.js"></script>
<script src="<%=webPath%>/static/js/common/ztree/jquery.ztree.excheck-3.5.js"></script>
<script src="<%=webPath%>/static/js/common/ztree/jquery.ztree.exedit-3.5.js"></script>

<script>
    var sbday='<%=sbday%>';
    var seday='<%=seday%>';
</script>

