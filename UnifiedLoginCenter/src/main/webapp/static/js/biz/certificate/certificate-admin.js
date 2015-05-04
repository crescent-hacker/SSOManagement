/**
 * author:cdf
 * date:2014-9-30
 * desc:证书管理js
 */
//---------------------------定义全局变量------------------------------------
var CERT_ADMIN = (function () {
    /*-----------变量---------------*/
    //搜索提示语
    var TIP = "输入证书名称搜索";
    //表格对象
    var GRID = {};
    //表格id
    var GRID_ID = "gridbox";
    /*-----------函数----------------*/
    //获得搜索提示语
    function GET_TIP() {
        return TIP;
    }
    //获得表格
    function GET_GRID() {
        return GRID;
    }
    //获得表格
    function SET_GRID(V_GRID) {
        GRID = V_GRID;
    }
    //获得表格
    function GET_GRID_ID() {
        return GRID_ID;
    }
    /*-----------返回json----------------*/
    //组装返回闭包
    var returnObj = {
        'GET_TIP': GET_TIP,
        'GET_GRID': GET_GRID,
        'SET_GRID': SET_GRID,
        'GET_GRID_ID': GET_GRID_ID
    };
    return returnObj;
})();


//-------------------------------------业务处理代码----------------------------------
/**
 * 展示证书列表
 */
function showCertTable() {
    var searchKey = ($("input[name='searchKey']").val() == CERT_ADMIN.GET_TIP()) ? "" : $("input[name='searchKey']").val();

    $.ajax({
        type: "POST",
        url: webRoot + "/certificate/certificateAction!getCertList.action",
        data: {
            'isValid': $("input[name='isValid']").val(),
            'searchKey': $.trim(searchKey),
            'pageIndex': $("input[name='pageIndex']").val()
        },
        dataType: "json",
        success: function (data) {
            if (data.success) {
                var grid = CERT_ADMIN.GET_GRID();
                grid.clearAll(false);
                grid.parse(data.certList, "", "jsarray");
//                setGridHeadColor("rgb(2, 117, 173)",grid.getColumnsNum(),CERT_ADMIN.GET_GRID_ID());
                setGridHeadColor("#0099cc",grid.getColumnsNum(),CERT_ADMIN.GET_GRID_ID());
                //分页
                $("#pagination_bar").html(data.pagination_html);
            }
            else {
                alertNew("提示", "查询证书列表失败！", closeMsg);
            }
        },
        error: function (xhr) {
            errorAlert(xhr)
        }
    });
}

/**
 * 获取ukey
 */
function GetUkeySerial()
{
    var isOpen = false;
    var isSelectCert = false;
    var isVerifyUserpin = false;
    var ret = FTUCtrl.IsOnline;  //检查USBKEY是否已经连接，是所有操作的第一步
    if(ret == 0)
    {
        //alert("成功检测到ePass3003");
        isOpen = true;
        //tokenlabel.value = FTUCtrl.GetTokenLabel;    //获取USBKEY的名称，最长32位
        //tokenserial.value = FTUCtrl.GetTokenSerial;  //获取USBKEY的序列号，16位
        return FTUCtrl.GetTokenSerial;
    }
    else
    {
        alert("没有检测到ePass3003");
        isOpen = false;
        return;
    }
}

/**
 * 添加证书
 */
function addCert() {
    var promptCallBack = function(){
        var certKeySelector = "#certKey";
        var certNameSelector = "#certName";
        var validDaySelector = "#validDay";
        if(!checkNull([certKeySelector,certNameSelector,validDaySelector])){
            return false;
        }
        $.ajax({
            type: "POST",
            async:false,
            url: webRoot + "/certificate/certificateAction!addCert.action",
            data: {
                'userId': 0,
                'ukeyNo': GetUkeySerial(),
                'certKey': $(certKeySelector).val(),
                'certName': $(certNameSelector).val(),
                'validDay': $(validDaySelector).val()
            },
            dataType: "json",
            success: function (data) {
                if (data.success) {
                    killPopup();
                    showCertTable();
                    alertNew("提示", "创建证书成功！", closeMsg);
                }
                else {
                    alertNew("提示", "创建证书失败！", closeMsg)
                }
            },
            error: function (xhr) {
                errorAlert(xhr)
            }
        });
    };

    var html = "<div><ul class='modal-body-title'>证书别名</ul><ul class='modal-body-value-manage' style='width:400px;'><input type='text' id='certKey' class='model-body-input' style='width:400px;'  /></ul></div>";
        html+= "<div><ul class='modal-body-title'>证书名称</ul><ul class='modal-body-value-manage' style='width:400px;'><input type='text' id='certName' class='model-body-input' style='width:400px;'  /></ul></div>";
        html+= "<div><ul class='modal-body-title'>有效日期</ul><ul class='modal-body-value-manage' style='width:400px;'><input type='text' id='validDay' class='model-body-input' style='width:400px;' value='3650' /></ul></div>";
    showPopup4("创建证书", html, "600", "310", "创建证书", promptCallBack, "85");
}

/**
 * 设置启用状态
 * @param isValid
 * @param ucId
 */
function setStatus(isValid,ucId){
    $.ajax({
        type: "POST",
        url: webRoot + "/certificate/certificateAction!setCertStatus.action",
        data: {
            'isValid': isValid,
            'ucId': ucId
        },
        dataType: "json",
        success: function (data) {
            if (data.success) {
                showCertTable();
            }
            else {
                alertNew("提示", "设置证书状态失败！", closeMsg);
            }
        },
        error: function (xhr) {
            errorAlert(xhr)
        }
    });
}

/**
 * 用证书状态筛选证书列表
 * @param isValid
 * @param thisButton
 */
function queryByCat(isValid, thisButton) {
    $("input[name='pageIndex']").val(1);
    $("button[name='isValidButton']").removeClass("active");
    $(thisButton).addClass('active')
    $("input[name='isValid']").val(isValid);
    showCertTable();
}


//---------------------------初始化------------------------------------
$(document).ready(function () {
    var eDiv = [".container", "#gridbox"];
    setElementsHeight(eDiv);
    $(window).resize(function () {
        setElementsHeight(eDiv);
    });
    //搜索按钮绑定
    $("#searchButton").click(function () {
        $("input[name='pageIndex']").val(1);
        showCertTable();
    });
    initSearchBox("#searchKey", CERT_ADMIN.GET_TIP(), function () {
        $("#searchButton").click();
    });
    //初始化表格
    initGrid();
    //页面加载后按默认条件查询
    showCertTable();
    setNavigation(3);
});

//分页调用函数
function goPageFunction() {
    showCertTable();
}
//-----------------------表格函数--------------------------------------
/**
 * 表格初始化
 */
function initGrid() {
    var mygrid = new dhtmlXGridObject(CERT_ADMIN.GET_GRID_ID());
    mygrid.setImagePath(webRoot + "/static/css/dhtmlXGrid/imgs/");
    var gridHead = "证书别名,证书名,证书状态,证书路径,UKEY序列号,已绑定用户,操作";
    mygrid.setHeaderNew(gridHead);
    mygrid.attachHeaderNew(gridHead);
    mygrid.setInitWidths("118,135,80,330,100,125,111");
    mygrid.setColAlign("left,left,left,left,left,left,center");
    mygrid.setColTypes("ro,ro,ro,ro,ro,ro,ro");
    mygrid.init();
    mygrid.setSkin("dhx_skyblue");// light
    mygrid.detachHeader(0);
//    $(".xhdr").width("98%");
    //把表格装入全局变量
    CERT_ADMIN.SET_GRID(mygrid);
}

//----------------------------页面公共函数-----------------------------------

/***
 * 表头设置颜色
 * @param color 颜色
 * @param num 数目
 * @param id 表格ID
 */
function setGridHeadColor(color,num,id) {
    for (var i = 0; i < num; i++) {
        $("#"+id+" td:eq(" + i + ")").css("background", color);
        $("#"+id+" td:eq(" + i + ")").css("color", "#fff");
    }
}