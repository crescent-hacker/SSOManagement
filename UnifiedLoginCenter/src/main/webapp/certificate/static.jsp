<%
    //绝对路径：域名+端口+挂载路径
    String webPath = request.getScheme()+"://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<!--------------------------css文件引入------------------------------------>
<link type="text/css" href="<%=webPath%>/static/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="<%=webPath%>/static/css/style.css"/>
<link rel="stylesheet" type="text/css" href="<%=webPath%>/static/css/style-ie.css"/>
<link rel="stylesheet" type="text/css" href="<%=webPath%>/static/css/popupModel.css">

<!--------------------------js文件引入------------------------------------->
<script type="text/javascript" src="<%=webPath%>/static/js/common/jquery-1.10.2.js"></script>
<script src="<%=webPath%>/static/js/common/page/page.js" type="text/javascript"></script>
<script src="<%=webPath%>/static/js/common/page/pagination.js" type="text/javascript"></script>
<script src="<%=webPath%>/static/js/common/popupModel.js" type="text/javascript"></script>
<script src="<%=webPath%>/static/js/common/search.js" type="text/javascript"></script>
<script src="<%=webPath%>/static/js/common/newAlert.js" type="text/javascript"></script>
<script src="<%=webPath%>/static/js/common/validate.js" type="text/javascript"></script>

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