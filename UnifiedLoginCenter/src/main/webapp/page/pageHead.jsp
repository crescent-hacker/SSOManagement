<%@ page contentType="text/html; charset=UTF-8" import="com.jetsun.bean.common.SessionKey" %>
<%
    String username = request.getSession().getAttribute(SessionKey.OPER_NAME) == null ? "" : request.getSession().getAttribute(SessionKey.OPER_NAME).toString();
    String webPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>

<script>
    var webRoot = '<%=webPath%>';
</script>
<!----------------全局公共js start---------------->
<script src="<%=webPath %>/static/js/common/commonSetting.js"></script>
<!----------------全局公共js end---------------->
<!----------------登出js start---------------->
<script src="<%=webPath %>/static/js/common/jquery.cookie.js"></script>
<script src="<%=webPath %>/static/js/common/logout.js"></script>
<!----------------登出js end---------------->

<!--背景层-->
<div id="bg" class="bg-container"></div>
<!--[if IE]>
<script>
    $(document).ready(function(){
    $("#bg").html('<IMG style="POSITION: fixed; WIDTH: 100%; HEIGHT: 768px; TOP: 0px; LEFT: 0px" src="'+webRoot+'/static/images/background_blur.jpg">');
    });
</script>
<![endif]-->

<!--頭部-->
<div id="header">
    <div id="headercontent">
        <div style="height: 79px;width: 338px; background-image: url('<%=webPath %>/static/images/bg_head.png');margin-left: 200px;"
             id="div_head_bg"></div>
        <div style="margin-top: -110px;margin-left: 220px;">
            <div id="hSystemName">统一登陆管理系统</div>
            <div id="hmenu">
                <!-- <div style="float: left;"><img  src="../static/images/login_user.png" border="0" style='height:20px;' onerror="$(this).attr('src','static/images/login_user.png');"/></div> -->
                <div id="loginName" style="float: left;"><%=username%>
                </div>
                <%--<div style="cursor:pointer;float: left;"
                     onclick="window.location='<%=webPath %>/?pid=90010';"><img
                        src="<%=webPath %>/static/images/btn_changePW.png" border="0"
                        style='margin-left: 10px;height:20px;float: left;'
                        onerror="$(this).attr('src','<%=webPath %>/static/images/btn_changePW.png');"/>

                    <div style="float: left;color:#fff;font-size: 13px;height:20px;line-height: 20px;">修改密码</div>
                </div>--%>
                <div style="cursor:pointer;float: left;" onclick="logout()"><img
                        src="<%=webPath %>/static/images/btn_exit.png" border="0"
                        style='margin-left: 10px;height:20px;float: left;'
                        onerror="$(this).attr('src','<%=webPath %>/static/images/btn_exit.png');"/>

                    <div style="float: left;color:#fff;font-size: 13px;height:20px;line-height: 20px;">退出</div>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="hNavigation">
    <div id="Navigation1">
        <a href="<%=webPath%>/?pid=91">
            <div class="btnNavigation" style="margin-left: 530px;" id="btnNavigation1">用户管理</div>
        </a>
        <a href="<%=webPath%>/?pid=92">
            <div class="btnNavigation" id="btnNavigation2">角色管理</div>
        </a>
        <a href="<%=webPath%>/?pid=93">
            <div class="btnNavigation" id="btnNavigation3">权限管理</div>
        </a>
        <a href="<%=webPath%>/?pid=94">
            <div class="btnNavigation" id="btnNavigation4">证书管理</div>
        </a>
        <a href="<%=webPath%>/?pid=95">
            <div class="btnNavigation" id="btnNavigation5">key管理</div>
        </a>
    </div>
    <!-- <div id="Navigation2"></div> -->
</div>
