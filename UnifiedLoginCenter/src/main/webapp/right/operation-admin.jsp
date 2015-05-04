<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <title>权限管理</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=8">
    <!----------------------------引入静态文件------------------------------------------>
    <%@include file="static.jsp" %>
    <script type="text/javascript" src="<%=webPath%>/static/js/biz/right/operation-admin.js"></script>
</head>
<body navId="3">
<!---------------------------------导航栏 开始--------------------------------------------->
<jsp:include page="/page/pageHead.jsp" flush="true"/>
<!---------------------------------导航栏 结束--------------------------------------------->

<!---------------------------------正文内容 开始--------------------------------------------->
<div class="container"
     style="margin-top: -30px;width: 1000px;height: 610px;">
    <jsp:include page="/page/pageNavigation.jsp" flush="true">
        <jsp:param name="title" value="系统导航 : 统一登陆系统 > "/>
        <jsp:param name="mainTitle" value="权限管理"/>
    </jsp:include>
    <div class="row clearfix funcNav">
        <div class="col-md-12 column">
            <!------------条件筛选框 开始---------------->
            <br>
            <!--类型选择条-->
            <div id="systemDiv" class="row">
                <div class="col-lg-6" style="width: 600px;width: 580px \9;">
                    权限所属系统：
                    <div class="btn-group" id="systemType">
                        <button class="btn btn-default active" type="button" name="systemButton"
                                onclick="queryByCat('6650',this)">
                            三目管理系统
                        </button>
                        <button class="btn btn-default" type="button" name="systemButton"
                                onclick="queryByCat('5367',this)">
                            省中心管理系统
                        </button>
                        <button class="btn btn-default" type="button" name="systemButton"
                                onclick="queryByCat('8327',this);">
                            地市管理系统
                        </button>
                        <button class="btn btn-default" type="button" name="systemButton"
                                onclick="queryByCat('3210',this);">
                            医院管理系统
                        </button>
                    </div>
                </div>
                <!--搜索框-->
                <div class="col-lg-4" style="width:320px;width: 240px \9">
                    <div class="input-group">
                        <input type="text" class="form-control" name="searchKey" id="searchKey" value=""
                               style="color: #787878;width: 240px \9">
                        <span class="input-group-btn">
                          <button id="searchButton" class="btn btn-primary" type="button" onclick="locateNode()">定位</button>
                        </span>
                    </div>
                </div>
                <!------------------------条件筛选框 结束------------------------>
            </div>
            <br>
        </div>
    </div>
    <!--树形div 开始-->
    <div class="zTreeDemoBackground left borderLine" style="margin-top: 16px;min-height:390px;">
        <div class="h1title" style="margin-left:-15px;margin-top:-5px;width: 274px;text-align: center">
            <span id="expand" style="cursor: pointer" onclick="expandTree(this,'权限树')">+ 权限树</span>
        </div>
        <div >
            <ul id="treeView" class="ztree" style="overflow: auto;height:410px;min-height: 340px;width:250px"></ul>
        </div>
    </div>
    <!--树形div 结束-->

    <!--权限详情div 开始-->
    <div class="right borderLine" style="min-height:390px;margin-top: 16px;width:670px">
        <div class="h1title" style="margin-left: -14px;margin-top:-5px;width: 694px;text-align: center">权限详情</div>
        <div id="operationDetail" style="margin-top: 20px">
        </div>
        <div class="col-lg-12" style="width:50px;float: left;margin-left: 80px;margin-top: 30px;display: none"
             id="addOperationButton">
            <button class="btn btn-primary " type="button" onclick="addOperation();">新增
            </button>
        </div>
        <div class="col-lg-12" style="width:50px;float: left;margin-left: 80px;margin-top: 30px;display: none"
             id="modifyOperactionButton">
            <button class="btn btn-primary " type="button" onclick="modifyOperation();">修改
            </button>
        </div>
    </div>
    <!--权限详情div 结束-->

    <!--弹出层div 开始-->
    <div id="rMenu" class='rMenu'>
        <ul class="ntes-nav-select-list">
            <li id="m_add_sub" onclick="hideRMenu();addOperationUIWithParent('operationDetail');">增加子权限</li>
            <li id="m_add" onclick="hideRMenu();addOperationUI('operationDetail');">增加权限</li>
            <li id="m_del" onclick="hideRMenu();delOperation();">删除权限</li>
        </ul>
    </div>
    <!--弹出层div 结束-->
</div>
<!---------------------------------正文内容 结束--------------------------------------------->

<!---------------------------------隐藏域 开始--------------------------------------------->
<div id="hiddenVar">
    <input type="hidden" name="currentRecord" value="">
    <input type="hidden" name="systemId" value="6650">
</div>
<!---------------------------------隐藏域 结束--------------------------------------------->

<!---------------------------------页脚 开始--------------------------------------------->
<jsp:include page="/page/pageFoot.jsp" flush="true"/>
<!---------------------------------页脚 结束--------------------------------------------->

</body>
</html>

