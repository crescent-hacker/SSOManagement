<%@ page contentType="text/html; charset=UTF-8" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE>
<html lang="zh">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=8">
    <title>角色管理</title>
    <!----------------------------引入静态文件------------------------------------------>
    <%@include file="static.jsp" %>
    <script type="text/javascript" src="<%=webPath %>/static/js/biz/role/role-admin.js"></script>
</head>
<body navId="2">
<!---------------------------------导航栏 开始--------------------------------------------->
<jsp:include page="/page/pageHead.jsp" flush="true"/>
<!---------------------------------导航栏 结束--------------------------------------------->

<!---------------------------------正文内容 开始--------------------------------------------->
<div class="container"
     style="margin-top: -30px;width: 1000px;height: 610px;">
    <jsp:include page="/page/pageNavigation.jsp" flush="true">
        <jsp:param name="title" value="系统导航 : 统一登陆系统 > "/>
        <jsp:param name="mainTitle" value="角色管理"/>
    </jsp:include>
    <div class="row clearfix funcNav">
        <div class="col-md-12 column">
            <!------------条件筛选框 开始---------------->
            <br>
            <!--类型选择条-->
            <div id="systemDiv" class="row">
                <div class="col-lg-12">
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
                <!------------------------条件筛选框 结束------------------------>
            </div>
            <br>
        </div>
    </div>
    <!--可继承角色树形div 开始-->
    <div id="leftTopBlock" class="left borderLine"
         style="height: 190px;text-align: left;margin-left: 0;min-height: 100px;margin-top: 15px">
        <div class="h1title"
             style="margin-bottom:5px;margin-left:-15px;margin-top:-5px;width: 274px;text-align: center">可继承角色列表
        </div>
        <div id="leftTopBlockInner" style="overflow:auto;height:155px;min-height: 70px">
            <ul id="stepRoleList" class="ztree" style="margin-top:0px;"></ul>
        </div>
    </div>
    <!--可继承角色树形div 结束-->

    <!--权限树div 开始-->
    <div id="rightBlock" class="right borderLine" style="width:670px;height: 470px;min-height: 280px;margin-top: 15px">
        <div class="h1title"
             style="margin-bottom:5px;margin-left:-14px;margin-top:-5px;width: 694px;text-align: center">
            <span id="expand" style="cursor: pointer" onclick="expandTree(this,'权限树')">+ 权限树</span>
        </div>
        <div id="rightBlockInner" style="overflow: auto;height:435px;min-height: 240px">
            <ul id="treeView" class="ztree" style="margin-left:30px;margin-top:0px;width: 540px"></ul>
        </div>
    </div>
    <!--权限树div 结束-->

    <!--角色树形div 开始-->
    <div id="leftBottomBlock" class="left borderLine"
         style="height: 200px;text-align: left;margin-left: 0;margin-top:15px;min-height: 100px;">
        <div class="h1title"
             style="margin-bottom:5px;margin-left:-15px;margin-top:-5px;width: 274px;text-align: center">角色列表
        </div>
        <div id="leftBottomBlockInner" style="overflow:auto;height:165px;min-height: 70px">
            <ul id="roleList" class="ztree" style="margin-top:0px"></ul>
        </div>
    </div>
    <!--角色树形div 结束-->

    <!--表单区域div 开始-->
    <div id="opZone" style="width:279px;float: left;margin-top:26px;">
        <div style="float:left;margin-left:0px;margin-right: 10px;">
            <%--<label style="float:left;margin-top:6px;margin-right:5px" for="roleName"></label>--%>
            <input type="text" class="form-control" style="width: 155px;height: 34px;height:20px \9;width:90px \9;"
                   id="roleName" name="roleName"/>
        </div>
        <input id="addButton" onclick="addRole();" type="button" value="  增加" class="btn btn-primary"/>
        <input id="modifyButton" onclick="modifyRole();" type="button" value="  修改" class="btn btn-primary"
               style="display: none"/>
        <input id="delButton" onclick="delRole();" type="button" value="  删除" class="btn btn-default"
               style="background-color: #EAEAEA"/>
    </div>
    <!--表单区域div 结束-->

</div>
<!---------------------------------隐藏域 开始--------------------------------------------->
<div id="hiddenVar">
    <input type="hidden" id="currentRecord" name="currentRecord" value="">
    <input type="hidden" id="currentRecordDiv" name="currentRecordDiv" value="">
    <input type="hidden" id="systemId" name="systemId" value="">
</div>
<!---------------------------------隐藏域 结束--------------------------------------------->

<!---------------------------------页脚 开始--------------------------------------------->
<jsp:include page="/page/pageFoot.jsp" flush="true"/>
<!---------------------------------页脚 结束--------------------------------------------->

</body>
</html>