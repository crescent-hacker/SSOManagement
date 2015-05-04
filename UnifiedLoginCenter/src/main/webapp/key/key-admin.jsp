<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2014/11/18
  Time: 17:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=8">
    <title>key管理</title>
    <%@include file="static.jsp" %>
    <script>
        var isOpen = false;				 //是否已经连接了USBKEY
        var isSelectCert = false;        //是否选择了证书的标志位
        var isVerifyUserpin = false;	 //是否验证了用户口令
    </script>
    <script type="text/javascript" src="<%=webPath%>/static/js/biz/key/key-admin.js"></script>
</head>
<body navId="5">
<!---------------------------------导航栏 开始--------------------------------------------->
<jsp:include page="/page/pageHead.jsp" flush="true"/>
<!---------------------------------导航栏 结束--------------------------------------------->


<!---------------------------------正文内容 开始--------------------------------------------->
<div class="container" style="margin-top: -30px;width: 1000px;height: 610px;">
    <jsp:include page="/page/pageNavigation.jsp" flush="true">
        <jsp:param name="title" value="系统导航 : 统一登陆系统 > "/>
        <jsp:param name="mainTitle" value="key管理"/>
    </jsp:include>

        <div class="row clearfix funcNav">
            <div class="col-md-12 column" >
                <!------------条件筛选框 开始---------------->
                <br/>
                <!--类型选择条-->
                <div id="isValidDiv" class="row" >
                    <div class="col-lg-3" style="float:left;width:350px;">
                        key查看：
                        <div class="btn-group" id="isValidType">
                                <button class="btn btn-default active" type="button" name="inStorageButton"
                                        onclick="buttonClick(this)">
                                    所有key
                                </button>
                                <button class="btn btn-default" type="button" name="outStorageButton"
                                        onclick="buttonClick(this)">
                                    出库
                                </button>
                                <button class="btn btn-default" type="button" name="storageButton"
                                        onclick="buttonClick(this);">
                                    库存
                                </button>
                                <button class="btn btn-default" type="button" name="lossButton"
                                        onclick="buttonClick(this);">
                                    挂失
                                </button>
                        </div>
                    </div>
                    <div class="col-lg-2 " style="float:left;width:300px">
                            key操作：
                        <div class="btn-group">
                            <button class="btn btn-primary" type="button" name="lossButton"
                                    onclick="openInsertDiv(this);">
                                入库
                            </button>
                            <button class="btn btn-primary" type="button" name="lossButton"
                                    onclick="openUpdateDiv(this);">
                                修改
                            </button>
                            <button class="btn btn-primary" type="button" name="lossButton"
                                    onclick="deleteKey(this);">
                                删除
                            </button>
                        </div>
                    </div>
                    <!--搜索框-->
                    <div class="col-lg-4 " style="width:260px">
                        <div class="input-group">
                            <input type="text" class="form-control" name="searchKey" id="searchKey" value=""
                                   style="color: #787878;width: 150px \9">
                                <span class="input-group-btn">
                                <button id="searchButton" class="btn btn-primary" type="button" onclick="searchKey(this)">搜索</button>
                                </span>
                        </div>
                    </div>
                    <!------------------------条件筛选框 结束------------------------>
                </div>
                <br/>
            </div>
        </div>
    <div id="gridbox" style="width:1000px;height: 430px;border:solid 0px #3099BC;margin-top: 10px">
        <div id="grid1" style="position:absolute;width:1000px;height: 430px;border:solid 0px #3099BC;"></div>
        <div id="grid2" style="position:absolute;width:1000px;height: 430px;border:solid 0px #3099BC;"></div>
        <div id="grid3" style="position:absolute;width:1000px;height: 430px;border:solid 0px #3099BC;"></div>
        <div id="grid4" style="position:absolute;width:1000px;height: 430px;border:solid 0px #3099BC;"></div>
    </div>

</div>
<!---------------------------------正文内容 结束--------------------------------------------->

<style>

    #insertDiv input{
        background-color: aliceblue;
        border-bottom: 2px solid #75a0df ;
        float: right;
    }
    #insertDiv label{
        margin-top: 9px;
        font-size: 18px;
        float: left;
    }
    #updateDiv input{
        background-color: aliceblue;
        border-bottom: 2px solid #75a0df ;
        float: right;
    }
    #updateDiv select{
        background-color: aliceblue;
        border-bottom: 2px solid #75a0df ;
        float: right;
        width: 179px;
    }
    #updateDiv label{
        margin-top: 9px;
        font-size: 18px;
        float: left;
    }
    #copyDiv input{
        background-color: aliceblue;
        border-bottom: 2px solid #75a0df ;
        float: right;
    }
    #copyDiv select{
        background-color: aliceblue;
        border-bottom: 2px solid #75a0df ;
        float: right;
    }
    #copyDiv label{
        font-size: 12px;
        float: left;
    }
</style>
<!---------------------------------隐藏域 开始--------------------------------------------->
<div id="signDiv" style="position:absolute;z-index:3;display: none">
    <button class="btn btn-default btn-primary" type="button" name="lossButton"
            onclick="changeSign('库存')">
        转去库存
    </button>
    </br>
    <%--    <label>操作员:</label>
        <input type="text" class="btn" name="">--%>
    <button class="btn btn-default btn-primary" type="button" name="lossButton"
            onclick="changeSign('出库')">
        转去出库
    </button>
    </br>
    <button class="btn btn-default btn-primary" type="button" name="lossButton"
            onclick="changeSign('挂失')">
        转去挂失
    </button>
    <%--    <label>操作人:</label>
        <input type="text" class="btn" name="">--%>
</div>
<div id="insertDiv" class="row clearfix funcNav" style="position:absolute;display:none;width:60%;height:60%;top:20%;left:20%;z-index:5;border:solid 0px #3099BC;">
    <div id="navigation_bg" style="background-color: #ffb553">
        <%--<div id="navigation_line"></div>--%>
    </div>
    <div style="float:left;margin-left: 10%;width: 320px">
        <div>
            <label>key号:</label>
            <input type="text" class="btn" name="insert_key_no">
            </br></br>
        </div>
        <div>
            <label>证书号:</label>
            <input type="text" class="btn" name="insert_certificate_no">
            </br></br>
        </div>
        <div>
            <label>型号:</label>
            <input type="text" class="btn" name="insert_model">
            </br></br>
        </div>
        <div>
            <label>key所在地:</label>
            <input type="text" class="btn" name="insert_location">
            </br></br>
        </div>
        <div>
            <label>key负责人:</label>
            <input type="text" class="btn" name="insert_responsible_person">
            </br></br>
        </div>
        <div>
            <label>key负责人电话:</label>
            <input type="text" class="btn" name="insert_responsible_telephone">
            </br></br>
        </div>
        <%--    <div>
                <label>当前状态:</label>
                <input type="text" name="insert_sign">
                </br>
            </div>--%>
        <div>
            <label>入库经手人:</label>
            <input type="text" class="btn" name="insert_inStorage_person">
            </br></br>
        </div>
    </div>
    <div style="float:right;margin-right: 10%;width: 320px">
        <div>
            <label>入库日期:</label>
            <input type="text" class="btn" name="insert_inStorage_date">
            </br></br>
        </div>
        <div>
            <label>备注:</label>
            <input type="text" class="btn" name="insert_notes">
            </br></br>
        </div>
        </br>
        <button onclick="insertKey()" class="btn-primary" style="float: right">key入库</button>
        </br></br>
        <button onclick="closeInsertDiv()" class="btn-primary" style="float: right">关闭</button>
    </div>
</div>
<div id="updateDiv" style="font-size:16px;position:absolute;display:none;width:60%;height:60%;top:20%;left:20%;background-color:aliceblue;z-index: 5;border:solid 0px #3099BC;">
    <div id="navigation_bg" style="background-color: #ffb553">
        <%--<div id="navigation_line"></div>--%>
    </div>
    <div style="float:left;margin-left: 10%;width:320px;">
        <div>
            <label>key号:</label>
            <input type="text" class="btn" name="update_key_no">
            </br></br>
        </div>
        <div>
            <label>证书号:</label>
            <input type="text" class="btn" name="update_certificate_no">
            </br></br>
        </div>
        <div>
            <label>型号:</label>
            <input type="text" class="btn" name="update_model">
            </br></br>
        </div>
        <div>
            <label>key所在地:</label>
            <input type="text" class="btn" name="update_location">
            </br></br>
        </div>
        <div>
            <label>key负责人:</label>
            <input type="text" class="btn" name="update_responsible_person">
            </br></br>
        </div>
        <div>
            <label>key负责人电话:</label>
            <input type="text" class="btn" name="update_responsible_telephone">
            </br></br>
        </div>
        <div>
            <label>当前状态:</label>
            <select type="text" class="btn" name="update_sign">
                <option value="出库">出库</option>
                <option value="库存">库存</option>
                <option value="挂失">挂失</option>
            </select>
            </br></br>
        </div>
        <div>
            <label>入库经手人:</label>
            <input type="text" class="btn" name="update_inStorage_person" >
            </br></br>
        </div>
    </div>
    <div style="float:right;margin-right: 10%;width:320px;">
        <div>
            <label>入库日期:</label>
            <input type="text" class="btn" name="update_inStorage_date">
            </br></br>
        </div>
        <div>
            <label>出库经手人:</label>
            <input type="text" class="btn" name="update_outStorage_person" >
            </br></br>
        </div>
        <div>
            <label>出库日期:</label>
            <input type="text" class="btn" name="update_outStorage_date" >
            </br></br>
        </div>
        <div>
            <label>挂失注册人:</label>
            <input type="text" class="btn" name="update_loss_person">
            </br></br>
        </div>
        <div>
            <label>挂失日期:</label>
            <input type="text" class="btn" name="update_loss_date">
            </br></br>
        </div>
        <div>
            <label>备注:</label>
            <input type="text" class="btn" name="update_notes">
            </br></br>
        </div>
        </br>
        <button onclick="updateKey()" class="btn-primary" style="float: right">信息修改保存</button>
        </br></br>
        <button onclick="closeUpdateDiv()" class="btn-primary" style="float: right">关闭</button>
        </br></br>
    </div>
</div>

<div id="copyDiv" style="font-size:12px;position:absolute;display:none;width:60%;height:60%;top:20%;left:20%;background-color:aliceblue;z-index: 5;border:solid 0px #3099BC;">
    <div id="navigation_bg" style="background-color: #ffb553">
        <%--<div id="navigation_line"></div>--%>
    </div>
    <div style="float:left;margin-left: 10%;width:320px;">
        <div>
            <label>key号:</label>
            <input type="text" class="btn" name="copy_key_no">
            </br>
        </div>
        <div>
            <label>证书号:</label>
            <input type="text" class="btn" name="copy_certificate_no">
            </br>
        </div>
        <div>
            <label>型号:</label>
            <input type="text" class="btn" name="copy_model">
            </br>
        </div>
        <div>
            <label>key所在地:</label>
            <input type="text" class="btn" name="copy_location">
            </br>
        </div>
        <div>
            <label>key负责人:</label>
            <input type="text" class="btn" name="copy_responsible_person">
            </br>
        </div>
        <div>
            <label>key负责人电话:</label>
            <input type="text" class="btn" name="copy_responsible_telephone">
            </br>
        </div>
        <div>
            <label>当前状态:</label>
            <select type="text" class="btn" name="copy_sign">
                <option value="出库">出库</option>
                <option value="库存">库存</option>
                <option value="挂失">挂失</option>
            </select>
            </br>
        </div>
        <div>
            <label>入库经手人:</label>
            <input type="text" class="btn" name="copy_inStorage_person" >
            </br>
        </div>
        <div>
            <label>入库日期:</label>
            <input type="text" class="btn" name="copy_inStorage_date">
            </br>
        </div>
        <div>
            <label>出库经手人:</label>
            <input type="text" class="btn" name="copy_outStorage_person" >
            </br></br>
        </div>
        <div>
            <label>出库日期:</label>
            <input type="text" class="btn" name="copy_outStorage_date" >
            </br>
        </div>
        <div>
            <label>挂失注册人:</label>
            <input type="text" class="btn" name="copy_loss_person">
            </br>
        </div>
        <div>
            <label>挂失日期:</label>
            <input type="text" class="btn" name="copy_loss_date">
            </br>
        </div>
        <div>
            <label>备注:</label>
            <input type="text" class="btn" name="copy_notes">
            </br>
        </div>
        <button onclick="copyKey()" class="btn-primary" style="float: right">复制</button>
        </br>
        <button onclick="pasteKey()" class="btn-primary" style="float: right">粘贴</button>
        </br>
        <button onclick="closeCopyDiv()" class="btn-primary" style="float: right">关闭</button>
    </div>
</div>


<div id="mask" style="position:absolute;left:0;top:0;width:100%;height:100%;z-index:2;background-color:black;opacity:0.4;filter:alpha(opacity=40);display: none"></div>
<!---------------------------------隐藏域 结束--------------------------------------------->


<!---------------------------------页脚 开始--------------------------------------------->
<jsp:include page="/page/pageFoot.jsp" flush="true"/>
<!---------------------------------页脚 结束--------------------------------------------->

    <%--<object CLASSID="clsid:C7672410-309E-4318-8B34-016EE77D6B58" CODEBASE="../files/install.cab#Version=0,0,0,1" BORDER="0" VSPACE="0" HSPACE="0" ALIGN="TOP" HEIGHT="0" WIDTH="0" VIEWASTEXT>--%>
        <%--<embed ></embed>--%>
    <%--</object>--%>
    <%--<OBJECT CLASSID="clsid:0272DA76-96FB-449E-8298-178876E0EA89" ID="ePass" Name = "ePass"  VIEWASTEXT>		&lt;%&ndash;//ft_nd_sc.dll&ndash;%&gt;--%>
    <%--</OBJECT>--%>
    <%--<OBJECT CLASSID="clsid:C7672410-309E-4318-8B34-016EE77D6B58" ID="ePass1" Name = "ePass1"  VIEWASTEXT>&lt;%&ndash;	//ft_nd_full.dll&ndash;%&gt;--%>
    <%--</OBJECT>--%>
    <object CLASSID="clsid:85711335-4F90-4ADA-B006-A2FD62C60779" CODEBASE="../files/install3003.cab" BORDER="0" VSPACE="0" HSPACE="0" ALIGN="TOP" HEIGHT="0" WIDTH="0" VIEWASTEXT></object>
    <OBJECT id="FTUCtrl" classid="clsid:85711335-4F90-4ADA-B006-A2FD62C60779" VIEWASTEXT></OBJECT>
</body>
</html>
