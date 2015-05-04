<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <title>证书管理</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=8">
    <!----------------------------引入静态文件------------------------------------------>
    <%@include file="static.jsp" %>
    <script type="text/javascript" src="<%=webPath%>/static/js/biz/certificate/certificate-admin.js"></script>
</head>
<body navId="4">
<!---------------------------------导航栏 开始--------------------------------------------->
<jsp:include page="/page/pageHead.jsp" flush="true"/>
<!---------------------------------导航栏 结束--------------------------------------------->

<!---------------------------------正文内容 开始--------------------------------------------->
<div class="container"
     style="margin-top: -30px;width: 1000px;height: 610px;">
    <jsp:include page="/page/pageNavigation.jsp" flush="true">
        <jsp:param name="title" value="系统导航 : 统一登陆系统 > "/>
        <jsp:param name="mainTitle" value="证书管理"/>
    </jsp:include>
    <div class="row clearfix funcNav">
        <div class="col-md-12 column">
            <!------------条件筛选框 开始---------------->
            <br>
            <!--类型选择条-->
            <div id="isValidDiv" class="row">
                <div class="col-lg-3" style="float:left;width:250px">
                    证书状态：
                    <div class="btn-group" id="isValidType">
                        <button class="btn btn-default" type="button" name="isValidButton"
                                onclick="queryByCat('',this)">
                            全部
                        </button>
                        <button class="btn btn-default active" type="button" name="isValidButton"
                                onclick="queryByCat('1',this)">
                            启用
                        </button>
                        <button class="btn btn-default" type="button" name="isValidButton"
                                onclick="queryByCat('0',this);">
                            禁用
                        </button>
                    </div>
                </div>
                <div class="col-lg-2" style="float: left;width:230px">
                    <button class="btn btn-primary " type="button" name="addCertButton" onclick="addCert();">新增
                    </button>

                    <a class="btn btn-primary" style="margin-left: 10px" name="downServCert" href='<%=webPath%>/certificate/certificateAction!downloadServerCert.action' target="_blank">下载服务端证书
                    </a>
                </div>
                <!--搜索框-->
                <div class="col-lg-4" style="width:420px">
                    <div class="input-group">
                        <input type="text" class="form-control" name="searchKey" id="searchKey" value=""
                               style="color: #787878;width: 340px \9">
                        <span class="input-group-btn">
                          <button id="searchButton" class="btn btn-primary" type="button">搜索</button>
                        </span>
                    </div>
                </div>
                <!------------------------条件筛选框 结束------------------------>

            </div>
            <br>
        </div>

    </div>
    <!--表格展示div 开始-->
    <div id="gridbox" style="width:1000px;height: 430px;border:solid 0px #3099BC;margin-top: 10px"></div>
    <!--表格展示div 结束-->

    <!--分页div 开始-->
    <div id="pagination_bar" style="margin-top: 0px;"></div>
    <!--分页div 结束-->

    <!--弹出层div 开始-->
    <!--弹出层div 结束-->
</div>
<!---------------------------------正文内容 结束--------------------------------------------->

<!---------------------------------隐藏域 开始--------------------------------------------->
<div id="hiddenVar">
    <input type="hidden" name="currentRecord" value="">
    <input type="hidden" name="pageIndex" value="1">
    <input type="hidden" name="isValid" value="1">
</div>
<!---------------------------------隐藏域 结束--------------------------------------------->

<!---------------------------------页脚 开始--------------------------------------------->
<jsp:include page="/page/pageFoot.jsp" flush="true"/>
<!---------------------------------页脚 结束--------------------------------------------->

    <object CLASSID="clsid:C7672410-309E-4318-8B34-016EE77D6B58" CODEBASE="../files/install.cab#Version=0,0,0,1" BORDER="0" VSPACE="0" HSPACE="0" ALIGN="TOP" HEIGHT="0" WIDTH="0" VIEWASTEXT>
        <embed ></embed>
    </object>
    <OBJECT CLASSID="clsid:0272DA76-96FB-449E-8298-178876E0EA89" ID="ePass" Name = "ePass"  VIEWASTEXT>		<%--//ft_nd_sc.dll--%>
    </OBJECT>
    <OBJECT CLASSID="clsid:C7672410-309E-4318-8B34-016EE77D6B58" ID="ePass1" Name = "ePass1"  VIEWASTEXT><%--	//ft_nd_full.dll--%>
    </OBJECT>

</body>
</html>

