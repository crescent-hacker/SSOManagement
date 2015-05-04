/**
 * author:cdf
 * date:2014-9-30
 * desc:用户管理js
 */
//---------------------------定义全局变量------------------------------------
var OPERATOR_ADMIN = (function () {
    /*-----------变量---------------*/
    //搜索提示语
    var TIP = "输入用户名称搜索";
    //表格对象
    var GRID = {};
    //表格id
    var GRID_ID = "gridbox";
    //统筹区列表
    var AREA_LIST = [];
    //医院列表
    var HSPT_LIST = [];
    //树形对象
    var TREE = {};
    //树id
    var TREE_ID = 'treeView';
    //树形所有节点
    var TREE_DATA = [];
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
    //获得树
    function GET_TREE() {
        return TREE;
    }
    //设置树
    function SET_TREE(V_TREE) {
        TREE = V_TREE;
    }
    //获取树id
    function GET_TREE_ID() {
        return TREE_ID;
    }
    //获得树形所有节点资料,带类型过滤器
    function GET_TREE_DATA() {
       return TREE_DATA;
    }
    //设置树形所有节点资料
    function SET_TREE_DATA(V_TREE_DATA) {
        TREE_DATA = V_TREE_DATA;
    }
    /*-----------返回json----------------*/
    //组装返回闭包
    var returnObj = {
        'GET_TIP': GET_TIP,
        'GET_GRID': GET_GRID,
        'SET_GRID': SET_GRID,
        'GET_GRID_ID': GET_GRID_ID,
        'AREA_LIST':AREA_LIST,
        'HSPT_LIST':HSPT_LIST,
        'GET_TREE': GET_TREE,
        'SET_TREE': SET_TREE,
        'GET_TREE_ID': GET_TREE_ID,
        'GET_TREE_DATA': GET_TREE_DATA,
        'SET_TREE_DATA': SET_TREE_DATA
    };
    return returnObj;
})();


//-------------------------------------业务处理代码----------------------------------
/**
 * 展示用户列表
 */
function showUserTable(){
    var searchKey = ($("input[name='searchKey']").val() == OPERATOR_ADMIN.GET_TIP()) ? "" : $("input[name='searchKey']").val();

    $.ajax({
        type: "POST",
        url: webRoot + "/operator/operatorAction!getUserList.action",
        data: {
            'isValid': $("input[name='isValid']").val(),
            'searchKey': $.trim(searchKey),
            'pageIndex': $("input[name='pageIndex']").val()
        },
        dataType: "json",
        success: function (data) {
            if (data.success) {
                var grid = OPERATOR_ADMIN.GET_GRID();
                grid.clearAll(false);
                grid.parse(data.userList, "", "jsarray");
//                setGridHeadColor("rgb(2, 117, 173)",grid.getColumnsNum(),OPERATOR_ADMIN.GET_GRID_ID());
                setGridHeadColor("rgb(0, 153, 204)",grid.getColumnsNum(),OPERATOR_ADMIN.GET_GRID_ID());
                //分页
                $("#pagination_bar").html(data.pagination_html);
            }
            else {
                alertNew("提示", "查询用户列表失败！", closeMsg);
            }
        },
        error: function (xhr) {
            errorAlert(xhr)
        }
    });
}

/**
 * 设置启用状态
 * @param isValid
 * @param ucId
 */
function setStatus(isValid,userId){
    $.ajax({
        type: "POST",
        url: webRoot + "/operator/operatorAction!setUserStatus.action",
        data: {
            'isValid': isValid,
            'userId': userId
        },
        dataType: "json",
        success: function (data) {
            if (data.success) {
                showUserTable();
            }
            else {
                alertNew("提示", "设置用户状态失败！", closeMsg);
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
 * 增加用户
 */
function addUser(){
    //检查非空字段
    if(!checkNull(["#operNo","#operName","#operBegin","#operEnd"])){
        showPage("tab_page1","div_page1");
        return false;
    }
    //拼装bean
    var addJson = {
        operNo:$("#operNo").val(),
        operName:$("#operName").val(),
        operState:$("#operState").val(),
        areaId:$("#areaId").val(),
        hsptId:$("#hsptId").val(),
        isAddCert:$("#isAddCert").val(),
        certName:$("#certName").val(),
        operBegin:$("#operBegin").val(),
        operEnd:$("#operEnd").val()
    }
    //获得权限集
    var roleArray = [];
    $.each(OPERATOR_ADMIN.GET_TREE().getCheckedNodes(), function (index, element) {
        //排除分类目录的节点
        if (element.type != 0) {
            roleArray.push(element.id);
        }
    });
    $.ajax({
        type: "POST",
        async:false,
        url: webRoot + "/operator/operatorAction!addUser.action",
        data: {
            'addJson': json2str(addJson),
            'roles': roleArray.join(","),
            'ukeyNo': GetUkeySerial()
        },
        dataType: "json",
        success: function (data) {
            if (data.success) {
                closePopup();
                alertNew("提示", "添加用户成功！", closeMsg);
                showUserTable();
            }
            else {
                alertNew("提示", "添加用户失败", closeMsg);
            }
        },
        error: function (xhr) {
            errorAlert(xhr)
        }
    });
}

/**
 * 修改用户
 */
function modifyUser(){
    //检查非空字段
    if(!checkNull(["#operNo","#operName","#operBegin","#operEnd"])){
        showPage("tab_page1","div_page1");
        return false;
    }
    //拼装bean
    var updateJson = {
        userId: $("#currentRecord").val(),
        operNo:$("#operNo").val(),
        operName:$("#operName").val(),
        operState:$("#operState").val(),
        areaId:$("#areaId").val(),
        hsptId:$("#hsptId").val(),
        certName:$("#certName").val(),
        operBegin:$("#operBegin").val(),
        operEnd:$("#operEnd").val()
    }
    //获得权限集
    var roleArray = [];
    $.each(OPERATOR_ADMIN.GET_TREE().getCheckedNodes(), function (index, element) {
        //排除分类目录的节点
        if (element.type != 0) {
            roleArray.push(element.id);
        }
    });
    $.ajax({
        type: "POST",
        async:false,
        url: webRoot + "/operator/operatorAction!modifyUser.action",
        data: {
            'updateJson': json2str(updateJson),
            'resetPwd': $("#resetPwd").val(),
            'roles': roleArray.join(",")
        },
        dataType: "json",
        success: function (data) {
            if (data.success) {
                closePopup();
                alertNew("提示", "修改用户成功！", closeMsg);
                showUserTable();
            }
            else {
                alertNew("提示", "修改用户失败", closeMsg);
            }
        },
        error: function (xhr) {
            errorAlert(xhr)
        }
    });
}
/**
 * 获得角色
 */
function getTree() {
    $.ajax({
        type: "POST",
        async:false,
        url: webRoot + "/role/roleAction!getAllRoleList.action",
        data: {},
        dataType: "json",
        success: function (data) {
            if (data.success) {
                //初始化树列表
                initTree(OPERATOR_ADMIN.GET_TREE_ID(),data.roleList,true);
                //处理树的显示
                var zTree = OPERATOR_ADMIN.GET_TREE()
                $.each(OPERATOR_ADMIN.GET_TREE_DATA(),function(index,element){
                    var node = zTree.getNodeByParam("id",element.id);
                    if(element.pId == 0){//系统节点的显示
                        node.nocheck = true;
                        node.icon = webRoot+"/static/images/icon_system.png";
                    }else{//角色节点的显示
                        node.icon = webRoot+"/static/images/icon_person.png";
                    }
                    zTree.updateNode(node);//更新节点状态
                });
            }
            else {
                alertNew("提示", "查询角色列表失败！", closeMsg);
            }
        },
        error: function (xhr) {
            errorAlert(xhr)
        }
    });
}

/**
 * 解绑证书
 */
function unbindCert(userId) {
    $.ajax({
        type: "POST",
        async:false,
        url: webRoot + "/certificate/certificateAction!cancelRelation.action",
        data: {
            "userId":userId
        },
        dataType: "json",
        success: function (data) {
            if (data.success) {
                var parent = $("#certNameDiv").parent();
                parent.html(getFormatSearchInput("用户证书","certName",400,"",false,false));
                $("#unbind").hide();
                searchInit("certName");
                showUserTable();
            }
            else {
                alertNew("提示", "解绑证书失败！", closeMsg);
            }
        },
        error: function (xhr) {
            errorAlert(xhr)
        }
    });
}

/**
 * 获取用户的角色列表
 */
function getRolesByUserid(userId){
    $.ajax({
        type: "POST",
        url: webRoot + "/operator/operatorAction!getRolesByUserid.action",
        data: {
            "userId":userId
        },
        dataType: "json",
        success: function (data) {
            if (data.success) {
                //选中节点
                var zTree = OPERATOR_ADMIN.GET_TREE();
                $.each(data.roleList, function (index, element) {
                    zTree.checkNode(zTree.getNodeByParam("id", element), true, false);
                });
            }
            else {
                alertNew("提示", "查询用户的角色列表失败！", closeMsg);
            }
        },
        error: function (xhr) {
            errorAlert(xhr)
        }
    });
}

/**
 * 以用户状态筛选用户列表
 * @param isValid
 * @param thisButton
 */
function queryByCat(isValid, thisButton) {
    $("input[name='pageIndex']").val(1);
    $("button[name='isValidButton']").removeClass("active");
    $(thisButton).addClass('active')
    $("input[name='isValid']").val(isValid);
    showUserTable();
}

//---------------------------初始化------------------------------------
$(document).ready(function () {
    var eDiv = [".container", "#"+OPERATOR_ADMIN.GET_GRID_ID()];
    setElementsHeight(eDiv);
    $(window).resize(function () {
        setElementsHeight(eDiv);
    });
    //搜索按钮绑定
    $("#searchButton").click(function () {
        $("input[name='pageIndex']").val(1);
        showUserTable();
    });
    initSearchBox("#searchKey", OPERATOR_ADMIN.GET_TIP(), function () {
        $("#searchButton").click();
    });
    //初始化表格
    initGrid();
    //页面加载后按默认条件查询
    goPageFunction();
    //初始化地市医院常量列表
    initCommonList();
});

/**
 * 分页调用函数
 */
function goPageFunction() {
    showUserTable();
}

/**
 * 初始化地市医院列表
 */
function initCommonList(){
    $.ajax({
        type: "POST",
        url: webRoot + "/operator/operatorAction!getCommonList.action",
        data: {},
        dataType: "json",
        success: function (data) {
            if (data.success) {
                OPERATOR_ADMIN.AREA_LIST = data.areaList;
                OPERATOR_ADMIN.HSPT_LIST = data.hsptList;
            }
            else {
                alertNew("提示", "获取常量列表失败！", closeMsg);
            }
        },
        error: function (xhr) {
            errorAlert(xhr)
        }
    });
}

/**
 * @override:重写搜索建议框展示列表函数
 */
function doSearchSug(){
    var retArr = [];
    $.ajax({
        type: "POST",
        async:false,
        url: webRoot + "/certificate/certificateAction!getUnbindCertList.action",
        data: {
            certName:$("#certName").val()
        },
        dataType: "json",
        success: function (data) {
            if (data.success) {
                retArr = data.certList;
            }
            else {
                alertNew("提示", "获取证书列表失败！", closeMsg);
            }
        },
        error: function (xhr) {
            errorAlert(xhr)
        }
    });
    return retArr;
}
//-----------------------表格函数--------------------------------------
/**
 * 表格初始化
 */
function initGrid() {
    var mygrid = new dhtmlXGridObject(OPERATOR_ADMIN.GET_GRID_ID());
    mygrid.setImagePath(webRoot + "/static/css/dhtmlXGrid/imgs/");
    var gridHead = "类型,地区/医院,用户编码,用户名,启用日期,结束日期,状态,证书,操作";
    mygrid.setHeaderNew(gridHead);
    mygrid.attachHeaderNew(gridHead);
    mygrid.setInitWidths("80,182,100,150,100,100,50,116,121");
    mygrid.setColAlign("left,left,left,left,left,left,left,left,center");
    mygrid.setColTypes("ro,ro,ro,ro,ro,ro,ro,ro,ro");
    mygrid.init();
    mygrid.setSkin("dhx_skyblue");// light
    mygrid.detachHeader(0);
    //把表格装入全局变量
    OPERATOR_ADMIN.SET_GRID(mygrid);
}

/***
 * 表头设置颜色
 * @param color 颜色
 * @param num 数目
 * @param id 表格ID
 */
function setGridHeadColor(color,num,id) {
    for (var i = 0; i < num; i++) {
        var target = $("#"+id+" td:eq(" + i + ")");
        target.css("background", color);
        target.css("color", "#fff");
    }
}
//----------------------------页面展示辅助函数------------------------------------------------------
/**
 * 展示新增用户的界面
 */
function showAddUserUI(){
    //获取通用下拉框数组
    var constant = genSelectArray();
    //拼装表单html
    var html = "";
    html+=getFormatInputHtml("用户编码",400,"operNo",400,"",false,false);
    html+=getFormatInputHtml("用户名称",400,"operName",400,"",false,false);
    html+=getFormatSelectHtml("用户状态",400,constant.operStateArr,"operState",1);
    html+=getFormatSelectHtml("用户类型",400,constant.operTypeArr,"operType",0);
    html+=getFormatSelectHtml("地   市",400,OPERATOR_ADMIN.AREA_LIST,"areaId",0,true);
    html+=getFormatSelectHtml("医    院",400,[],"hsptId",0,true,{'isAllow':true,'value':0});
    html+=getFormatSelectHtml("创建证书",400,constant.isAddCert,"isAddCert",1);
    html+=getFormatSearchInput("用户证书","certName",400,"",false,true);
    html+=getFormatDateInput("生效日期","operBegin",sbday,20,false,false);
    html+=getFormatDateInput("失效日期","operEnd",seday,70,false,false);
    html=applendTreeTabHtml(html);

    //展示弹出框
    showPopup4("新增用户",html,640,630,"新增",addUser,390,600,closePopup);
    //激活日期控件
    activeCalendar("operBegin");
    activeCalendar("operEnd");
    //初始化证书搜索框
    searchInit("certName");
    //处理选择列表
    $("#operState").selectmenu().selectmenu("menuWidget").addClass("cbooverflow");
    $("#operType").selectmenu().selectmenu("menuWidget").addClass("cbooverflow");
    $("#areaId").selectmenu().selectmenu("menuWidget").addClass("cbooverflow");
    $("#hsptId").selectmenu().selectmenu("menuWidget").addClass("cbooverflow");
    $("#hsptId").selectmenu( "option", "width", 300);
    $("#isAddCert").selectmenu().selectmenu("menuWidget").addClass("cbooverflow");
    //处理选择框点击绑定事件
    selectHandler();
    //初始化角色树
    getTree();
}

/**
 * 展示修改用户的界面
 */
function showModifyUserUI(userid,operNo,operName,operState,operType,areaId,hsptId,certName,operBegin,operEnd){
    //设置当前修改用户id
    $("#currentRecord").val(userid);
    //获取通用下拉框数组
    var constant = genSelectArray();
    //拼装表单html
    var html = "";
    html+=getFormatInputHtml("用户编码",400,"operNo",400,operNo,false,false);
    html+=getFormatInputHtml("用户名称",400,"operName",400,operName,false,false);
    html+=getFormatSelectHtml("密码复位",400,constant.isResetPwd,"resetPwd",0);
    html+=getFormatSelectHtml("用户状态",400,constant.operStateArr,"operState",operState);
    html+=getFormatSelectHtml("用户类型",400,constant.operTypeArr,"operType",operType);
    html+=getFormatSelectHtml("地   市",400,OPERATOR_ADMIN.AREA_LIST,"areaId",areaId,true);
    html+=getFormatSelectHtml("医    院",400,[],"hsptId",0,true,{'isAllow':true,'value':0});
    if(!certName){//无证书
        html+=getFormatSearchInput("用户证书","certName",400,"",false,false);
    }else{//有证书
        html+=getFormatSearchInput("用户证书","certName",300,certName,true,false);
        html+="<button type='button' class='btn btn-primary' id='unbind' style='height: 26px;margin-top:5px;padding:0 8 0 8' onclick='unbindCert("+userid+")'>解绑证书</button>"
    }
    html+=getFormatDateInput("生效日期","operBegin",operBegin,20,false,false);
    html+=getFormatDateInput("失效日期","operEnd",operEnd,70,false,false);
    html=applendTreeTabHtml(html);

    //展示弹出框
    showPopup4("修改用户",html,640,630,"修改",modifyUser,390,600,closePopup);
    //激活日期控件
    activeCalendar("operBegin");
    activeCalendar("operEnd");
    //初始化证书搜索框
    searchInit("certName");
    //处理选择列表
    $("#operState").selectmenu().selectmenu("menuWidget").addClass("cbooverflow");
    $("#operType").selectmenu().selectmenu("menuWidget").addClass("cbooverflow");
    $("#areaId").selectmenu().selectmenu("menuWidget").addClass("cbooverflow");
    $("#hsptId").selectmenu().selectmenu("menuWidget").addClass("cbooverflow");
    $("#hsptId").selectmenu( "option", "width", 300)
    $("#resetPwd").selectmenu().selectmenu("menuWidget").addClass("cbooverflow");
    //处理选择框点击绑定事件
    selectHandler();
    //初始化点击
    $("#operType-menu").click();
    genHsptListByAreaId(areaId,hsptId);
    //初始化角色树
    getTree();
    //选中角色节点
    getRolesByUserid(userid);
}

/**
 * 下拉列表联动处理
 */
function selectHandler(areaId,hsptId){
    //根据创建证书下拉框控制证书选择框
    $("#isAddCert-menu").click(function(){
        var isAddCert = parseInt($("#isAddCert").val());
        if(isAddCert){
            $("#certNameDiv").hide();$("#certName").val("");
        }else{
            $("#certNameDiv").show();
        }
    });
    //根据用户类型显示统筹区和医院
    $("#operType-menu").click(function(){
        var operType = parseInt($("#operType").val());
        if(operType == 0){//省
            $("#areaIdDiv").hide();$("#areaId").val(0);
            $("#hsptIdDiv").hide();$("#hsptId").val(0);
        }
        if(operType == 1){//地市
            $("#areaIdDiv").show();
            $("#hsptIdDiv").hide();$("#hsptId").val(0);
        }
        if(operType == 2){//医院
            $("#areaIdDiv").show();
            $("#hsptIdDiv").show();
        }
        $("#areaId").selectmenu('refresh');
        $("#hsptId").selectmenu('refresh');
    });
    //根据统筹区更新医院列表,仅当用户类型为医院时生效
    $("#areaId-menu").click(function(){
        if($("#operType").val() == '2'){
            var areaId = parseInt($("#areaId").val());
            genHsptListByAreaId(areaId);
        }
    });
}

/**
 * 附加html-树形切换tab
 * @param html 展示html
 * @returns 加上树形切换标签的html
 */
function applendTreeTabHtml(html){
    var rhtml="";
    rhtml+="<ul class='nav nav-tabs' style='margin-bottom: 10px;'><li class='active' id='tab_page1'><a href='javascript:showPage(\"tab_page1\",\"div_page1\");'>用户信息</a></li><li id='tab_page2'><a href='javascript:showPage(\"tab_page2\",\"div_page2\");'>角色配置</a></li></ul>";
    rhtml+="<div id='div_page1'>";
    rhtml+= html;
    rhtml+="</div>";
    rhtml+="<div id='div_page2' style='display:none;'>";
    rhtml+='<ul id="'+OPERATOR_ADMIN.GET_TREE_ID()+'" class="ztree" style="overflow: auto;height:350px;margin-left: 20px;"></ul>';
    rhtml+="</div>";
    return rhtml;
}

/**
 * 制作下拉框通用数组
 */
function genSelectArray(){
    //用户状态数组
    var operStateArr = [
        {'id': 0, 'name': '禁用'},
        {'id': 1, 'name': '启用'}
    ];
    //用户类型数组
    var operTypeArr = [
        {'id': 0, 'name': '省'},
        {'id': 1, 'name': '市'},
        {'id': 2, 'name': '医院'}
    ];
    //是否
    var isOrNot = [
        {'id': 0, 'name': '否'},
        {'id': 1, 'name': '是'}
    ];
    return {
        'operStateArr':operStateArr,
        'operTypeArr':operTypeArr,
        'isAddCert':isOrNot,
        'isResetPwd':isOrNot
    }
}

/**
 * 根据统筹区更新医院列表
 * @param areaId 统筹区id
 * @param value 选中值
 */
function genHsptListByAreaId(areaId,value){
    var hsptAfterFilter = [];
    $.each(OPERATOR_ADMIN.HSPT_LIST,function(index,element){
        if(element.pId == areaId){
            hsptAfterFilter.push(element);
        }
    });
    $("#hsptId").html(getFormatOptionHtml(hsptAfterFilter,!value?0:value,{'isAllow':true,'value':0}));
    $("#hsptId").selectmenu('refresh');
}

//-------------------------树形控件-----------------------------------------
/**
 * 树形控件初始化
 */
function initTree(id, treeNodes, isCheckable, isHideIcon) {
    //设置参数
    var setting = {
        check: {
            enable: isCheckable,
            autoCheckTrigger:false,
            chkboxType:{"Y":"","N":""},
            chkStyle:"radio",
            nocheckInherit:false,
            chkDisabledInherit:false,
            radioType:"level"
        },
        treeNodeKey: "id",               //在isSimpleData格式下，当前节点id属性
        treeNodeParentKey: "pId",        //在isSimpleData格式下，当前节点的父节点id属性
        data: {
            simpleData: {
                enable: true
            }
        },
        callback:{
            onClick: zTreeOnClick
        },
        view: {
            showIcon: !isHideIcon
        }

    };
    //初始化并获得树对象,右键对象
    var zTree = $.fn.zTree.init($("#" + id), setting, treeNodes);
    zTree.expandAll(true);
    //把树形对象放入全局
    OPERATOR_ADMIN.SET_TREE(zTree);
    OPERATOR_ADMIN.SET_TREE_DATA(treeNodes);
}

/**
 * 树左键点击事件
 */
function zTreeOnClick(event, treeId, treeNode){
    var zTree = OPERATOR_ADMIN.GET_TREE();
    zTree.checkNode(treeNode,!zTree.getNodeByParam("id",treeNode.id).checked,false);
}
//--------------------------------------公共函数--------------------------------------------------
/**
 * 获得格式化的表单html
 * @param key 表单表头名称
 * @param keyWidth 表头宽度
 * @param valueId 表单元素id
 * @param valueWidth 表单元素宽度
 * @param value 表单的值
 * @param disabled true禁用 false启用
 * @param isHide false不隐藏 true隐藏
 */
function getFormatInputHtml(key, keyWidth, valueId, valueWidth, value, disabled,isHide) {
    var html = "<div id='"+valueId+"Div' style='display:"+(!isHide?"block":"none")+"'><ul class='modal-body-title' style='margin-top:8px;'>" + key + "</ul><ul class='modal-body-value-manage' style='width:" + keyWidth + "px;'><input type='text' id='" + valueId + "' class='model-body-input' style='width:" + valueWidth + "px;' value='" + value + "' " + (disabled ? "disabled" : "") + "/></ul></div>";
    return html;
}

/**
 * 获得格式化的选择框div代码
 * @param key 表头名称
 * @param keyWidth 表头宽度
 * @param selectArray 下拉框 html
 * @param selectId 下拉框 id
 * @param dataId 要选中的id
 * @param isHide false不隐藏 true隐藏
 * @param emptyOption 是否允许空选项，格式{'isAllow':true,'value':0}
 */
function getFormatSelectHtml(key, keyWidth, selectArray, selectId, dataId,isHide,emptyOption) {
    //是否选中空选项
    var isEmptySelected = true;
    var emptyOptionStr = "";
    //处理列表，拼装select语句
    var opIIdSelect = "";
    $.each(selectArray, function (index, element) {
        if (element.id == dataId) {
            opIIdSelect += "<OPTION VALUE=" + element.id + " selected>" + element.name + "</OPTION>";
            isEmptySelected = false;
        } else {
            opIIdSelect += "<OPTION VALUE=" + element.id + ">" + element.name + "</OPTION>";
        }
    });
    //空选项处理
    if (!(!emptyOption) && emptyOption.isAllow) {
        emptyOptionStr = "<OPTION VALUE=" + emptyOption.value + (isEmptySelected ? " selected" : " ") + ">无</OPTION>";
    }
    var selectHtml = emptyOptionStr + opIIdSelect;
    var html = "<div id='"+selectId+"Div' style='display:"+(!isHide?"block":"none")+";margin-top:7px;'><ul class='modal-body-title' style='margin-top:8px;'>" + key + "</ul><ul class='modal-body-value-manage' style='width:" + keyWidth + "px;'><select class='model-body-input' id='" + selectId + "'>" + selectHtml + "</select></ul></div>"
    return html;
}

/**
 * 获得格式化的选择框代码
 * @param selectArray 下拉框 html
 * @param emptyOption 是否允许空选项，格式{'isAllow':true,'value':0}
 */
function getFormatOptionHtml(selectArray,dataId, emptyOption) {
    //是否选中空选项
    var isEmptySelected = true;
    var emptyOptionStr = "";
    //处理列表，拼装select语句
    var select = "";
    $.each(selectArray, function (index, element) {
        if (element.id == dataId) {
            select += "<OPTION VALUE=" + element.id + " selected>" + element.name + "</OPTION>";
            isEmptySelected = false;
        } else {
            select += "<OPTION VALUE=" + element.id + ">" + element.name + "</OPTION>";
        }
    });
    //空选项处理
    if (!(!emptyOption) && emptyOption.isAllow) {
        emptyOptionStr = "<OPTION VALUE=" + emptyOption.value + (isEmptySelected ? " selected" : " ") + ">无</OPTION>";
    }
    var selectHtml = emptyOptionStr + select;
    return selectHtml;
}

/**
 * 获得格式化的日期表单
 * @param key 表单名称
 * @param valueId 表单id
 * @param value 表单值
 * @param disabled true禁用 false启用
 * @param isHide false不隐藏 true隐藏
 * @returns html代码
 */
function getFormatDateInput(key,valueId, value,marginLeft, disabled,isHide){
    var html = '<div style="float:left;margin-top:7px;margin-top:0px \\9;margin-left:'+marginLeft+'px;display:'+(isHide?'none':'block')+'" id="'+valueId+'Div" ><label for="'+valueId+'" style="float:left;height: 28px;line-height: 28px;">'+key+'</label>'+
                '<div class="dateinputborder" style="margin-left: 12px"><input type="text" id="'+valueId+'" class="dateinput" value="'+value+'" '+(disabled?'disabled':'')+'/></div></div>';
    return html;
}

/**
 * 获得格式化的搜索框表单
 * @param key 表单名称
 * @param valueId 表单id
 * @param valueWidth 表单宽度
 * @param value 表单值
 * @param disabled true禁用 false启用
 * @param isHide false不隐藏 true隐藏
 * @returns html代码
 */
function getFormatSearchInput(key,valueId,valueWidth,value,disabled,isHide){
    var html = "<div><div id='"+valueId+"Div' style='display:"+(isHide?'none':'block')+"'><ul class='modal-body-title'>"+key+"</ul><ul class='modal-body-value-manage' style='width:"+valueWidth+"px'>" +
        "<div id='searchBox' style='position:relative'><input type='text' name='"+valueId+"' id='"+valueId+"' maxlength='100' class='searchInput' style='width: "+valueWidth+"px;' value='"+value+"' "+(disabled?'disabled':'')+" />" +
        "<div id='sugBox' class='bdsug' style='display: none;width:"+(valueWidth-2)+"px;height:121px;'></div></div></ul></div></div>";

    return html;
}

/**
 * 激活日历控件
 * @param 日期表单id
 */
function activeCalendar(id){
    $( "#"+id ).datepicker({
        showOn: "button",
        buttonImage: webRoot+"/static/images/calendar.gif",
        buttonImageOnly: true,
        numberOfMonths: 3,
        showButtonPanel: true

    });
}

/**
 * 根据id显示标签页
 * @param id 显示标签id
 */
function showPage(id,divId){
    //要显示的标签页和div
    var curTab = $("#"+id);
    var curDiv = $("#"+divId);
    //去除兄弟的选中状态并隐藏div
    curTab.siblings().removeClass("active");
    curDiv.siblings("div").hide();
    //显示选中状态和显示div
    curTab.addClass("active");
    curDiv.show();
}

/**
 * 删除生成下拉框的冗余部件后再关闭窗口
 */
function closePopup(){
   $("select").selectmenu("destroy");
   killPopup();
}