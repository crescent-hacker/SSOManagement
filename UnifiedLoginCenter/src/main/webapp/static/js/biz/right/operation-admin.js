/**
 * author:cdf
 * date:2014-10-8
 * desc:权限管理js
 */
//---------------------------定义全局变量------------------------------------
var OPERATION_ADMIN = (function () {
    /*-----------变量---------------*/
    //搜索提示语
    var TIP = "输入 pid/权限名称 快速定位";
    //树形对象
    var TREE = {};
    //树id
    var TREE_ID = 'treeView';
    //右键对象
    var RMENU = {};
    //右键id
    var RMENU_ID = 'rMenu';
    //树形所有节点
    var TREE_DATA = [];
    /*-----------函数----------------*/
    //获得搜索提示语
    function GET_TIP() {
        return TIP;
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

    //获得右键菜单
    function GET_RMENU() {
        return RMENU;
    }

    //设置右键菜单
    function SET_RMENU(V_RMENU) {
        RMENU = V_RMENU;
    }

    //获取树id
    function GET_RMENU_ID() {
        return RMENU_ID;
    }

    //获得树形所有节点资料,带类型过滤器
    function GET_TREE_DATA(filter) {
        if (!filter) {
            return TREE_DATA;
        } else {
            var tmpTreeData = [];
            $.each(TREE_DATA, function (index, element) {
                if (element.type != filter) {
                    tmpTreeData.push(element);
                }
            });
            return tmpTreeData;
        }
    }

    //设置树形所有节点资料
    function SET_TREE_DATA(V_TREE_DATA) {
        TREE_DATA = V_TREE_DATA;
    }

    /*-----------返回json----------------*/
    //组装返回闭包
    var returnObj = {
        'GET_TIP': GET_TIP,
        'GET_TREE': GET_TREE,
        'SET_TREE': SET_TREE,
        'GET_TREE_ID': GET_TREE_ID,
        'GET_RMENU': GET_RMENU,
        'SET_RMENU': SET_RMENU,
        'GET_RMENU_ID': GET_RMENU_ID,
        'GET_TREE_DATA': GET_TREE_DATA,
        'SET_TREE_DATA': SET_TREE_DATA
    };
    return returnObj;
})();


//---------------------------------业务处理代码----------------------------------
/**
 * 获得权限树
 */
function getTree() {
    $.ajax({
        type: "POST",
        async: false,
        url: webRoot + "/right/operationAction!getTree.action",
        data: {
            'systemId': $("input[name='systemId']").val()
        },
        dataType: "json",
        success: function (data) {
            if (data.success) {
                initTree(data.treeList);
                //清空工作区
                $("#operationDetail").html("");
                showButton(2);
                //进行图标替换
                changeIcon(webRoot + "/static/images/icon_document.png", 0);
                changeIcon(webRoot + "/static/images/icon_page.png", 1);
                changeIcon(webRoot + "/static/images/icon_action.png", 2);
            }
            else {
                alertNew("提示", "查询权限树失败！", closeMsg);
            }
        },
        error: function (xhr) {
            errorAlert(xhr)
        }
    });
}

/**
 * 根据id获得权限详情,opType用作筛选，传-1查全部
 */
function getOperation(opId) {
    //权限id
    var retData = {};
    $.ajax({
        type: "POST",
        url: webRoot + "/right/operationAction!getOperationById.action",
        async: false,
        data: {
            'opId': opId
        },
        dataType: "json",
        success: function (data) {
            if (!(!data)) {//data不为空则展示
                retData = data;
            }
            else {
                alertNew("提示", "查询权限失败！", closeMsg);
            }
        },
        error: function (xhr) {
            errorAlert(xhr)
        }
    });
    return retData;
}

/**
 * 增加权限
 */
function addOperation() {
    if (!checkNull(["#opName"])) {
        return false;
    }
    var addJson = {
        rsId: $("input[name='systemId']").val(),
        opName: $("#opName").val(),
        opNote: $("#opNote").val(),
        opIId: $("#opIIdSelect").val(),
        opAction: $("#opAction").val(),
        opType: $("#opTypeSelect").val(),
        opIsPass: $("#opIsPassSelect").val()
    };
    $.ajax({
        type: "POST",
        url: webRoot + "/right/operationAction!addOperation.action",
        data: {
            'addJson': json2str(addJson)
        },
        dataType: "json",
        success: function (data) {
            if (data.success) {
                alertNew("提示", "增加权限成功！", closeMsg);
                getTree();
                zTreeOnClick(null, 0, OPERATION_ADMIN.GET_TREE().getNodeByParam("id", data.opId));
            }
            else {
                alertNew("提示", "增加权限失败！", closeMsg);
            }
        },
        error: function (xhr) {
            errorAlert(xhr)
        }
    });
}

/**
 * 修改权限
 */
function modifyOperation() {
    if (!checkNull(["#opName"])) {
        return false;
    }
    var opId = parseInt($("#opId").val());
    var updateJson = {
        rsId: $("input[name='systemId']").val(),
        opName: $("#opName").val(),
        opNote: $("#opNote").val(),
        opIId: $("#opIIdSelect").val(),
        opAction: $("#opAction").val(),
        opType: $("#opTypeSelect").val(),
        opIsPass: $("#opIsPassSelect").val(),
        opId: $("#opId").val()
    };
    $.ajax({
        type: "POST",
        url: webRoot + "/right/operationAction!modifyOperation.action",
        data: {
            'updateJson': json2str(updateJson)
        },
        dataType: "json",
        success: function (data) {
            if (data.success) {
                alertNew("提示", "修改权限成功！", closeMsg);
                getTree();
                zTreeOnClick(null, 0, OPERATION_ADMIN.GET_TREE().getNodeByParam("id", opId));
            }
            else {
                alertNew("提示", "请输入修改数据！", closeMsg);
            }
        },
        error: function (xhr) {
            errorAlert(xhr)
        }
    });
}

/**
 * 删除权限
 */
function delOperation() {
    //获取所有子节点
    var curNode = OPERATION_ADMIN.GET_TREE().getSelectedNodes()[0];
    var childIds = getChildNodes(curNode);
    //二次提示回调函数
    var promptCallback = function () {
        closeMsg();
        $.ajax({
            type: "POST",
            url: webRoot + "/right/operationAction!delOperation.action",
            data: {
                'opIds': childIds
            },
            dataType: "json",
            success: function (data) {
                if (data.success) {
                    alertNew("提示", "删除权限成功！", closeMsg);
                    getTree();
                }
                else {
                    alertNew("提示", "删除权限失败！", closeMsg);
                }
            },
            error: function (xhr) {
                errorAlert(xhr)
            }
        });
    }

    //提示是否二次删除
    alertNew("提示", "确认删除该权限及其所有子权限？", promptCallback);
}

/**
 * 用系统id筛选权限
 * @param systemId
 * @param thisButton
 */
function queryByCat(systemId, thisButton) {
    $("input[name='pageIndex']").val(1);
    $("button[name='systemButton']").removeClass("active");
    $(thisButton).addClass('active')
    $("input[name='systemId']").val(systemId);
    getTree();
    //展开树按钮复位
    $("#expand").html("+ 权限树");
}

//---------------------------页面初始化------------------------------------
$(document).ready(function () {
    //区域高度调整
    var eDiv = [".borderLine", ".container", "#treeView"];
    setElementsHeight(eDiv);
    $(window).resize(function () {
        setElementsHeight(eDiv);
    });
    //初始化加载树
    getTree();
    //初始化定位搜索框
    initSearchBox("#searchKey", OPERATION_ADMIN.GET_TIP(), function () {
        var result = locateNode();
        if (!result) {

        }
    });
});

function locateNode() {
    var isLocateSuccess = false;
    var searchKey = $("#searchKey").val();//搜索值
    var node;//欲定位的节点
    if (validate(searchKey, 'number')) {
        node = OPERATION_ADMIN.GET_TREE().getNodeByParam("id", searchKey);
        //数字
        if (!(!node)) {
            zTreeOnClick(null, null, node);
            isLocateSuccess = true;
        }
    } else {
        //名称
        $.each(OPERATION_ADMIN.GET_TREE_DATA(), function (index, element) {
            if (element.name.indexOf(searchKey) > -1) {
                node = OPERATION_ADMIN.GET_TREE().getNodeByParam("id", element.id);
                zTreeOnClick(null, null, node);
                isLocateSuccess =  true;
                return false;//跳出循环
            }
        });
    }
    //定位成功
    if(isLocateSuccess){
        return true;
    }
    //没有定位到，弹窗提示
    alertNew("提示", "请输入有效的pid或名称", function () {
        closeMsg();
        $("#searchKey").focus();
    });
}

//----------------------------页面展示辅助函数-----------------------------------
/**
 * 显示修改权限界面
 * @param data 权限对象
 * @param divId 展示的div
 */
function modifyOperationUI(data, divId) {
    //父类型资料
    var parentLevel = (data.opIId > 0) ? getOperation(data.opIId).opType : 0;
    //是否检查数组
    var opIsPassArr = [
        {'id': 0, 'name': '是'},
        {'id': 1, 'name': '否'}
    ];
    //是否隐藏表单，当类型为目录时隐藏url和检查项
    var isHideInput = data.opType == 0 ? true : false;
    //拼接表单
    var html = getFormatSelectHtml("父权限", 500, OPERATION_ADMIN.GET_TREE_DATA(2), "opIIdSelect", data.opIId, false, {'isAllow': true, 'value': 0});//TODO,父权限选择表单,分三种情况讨论
    html += getFormatSelectHtml("权限类型", 500, getOpTypeArray(parentLevel), "opTypeSelect", data.opType, 0);
    html += getFormatInputHtml("权限id", 500, "opId", 400, data.opId, 1);
    html += getFormatInputHtml("权限名称", 500, "opName", 400, data.opName, 0);
    html += getFormatInputHtml("权限url", 500, "opAction", 400, data.opAction, 0, isHideInput);
    html += getFormatInputHtml("权限备注", 500, "opNote", 400, data.opNote, 0);
    html += getFormatSelectHtml("是否检查", 500, opIsPassArr, "opIsPassSelect", data.opIsPass, isHideInput);
    //写入元素
    $("#" + divId).html(html);
    //显示按钮
    showButton(1);
    //处理选择列表样式
    $("select").selectmenu().selectmenu("menuWidget").addClass("cbooverflow");
    $("#opIIdSelect-menu").click(function () {
        var pNode = OPERATION_ADMIN.GET_TREE().getNodeByParam("id", $("#opIIdSelect").val());
        var data = getOperation(pNode.id);//父节点资料
        $("#opTypeSelect").html(getFormatOptionHtml("权限类型", 400, getOpTypeArray(data.opType), "opTypeSelect", 0))
        $("select").selectmenu('refresh');
    });
    ctrlInputBySelect("opTypeSelect", "opActionDiv|opIsPassSelectDiv", 0);
}
/**
 * 展示新增权限界面
 * @param data 权限对象
 * @param divId 展示的div
 */
function addOperationUI(divId) {
    //新增节点的权限类型数组
    var opTypeArr = [
        {'id': 0, 'name': '分组目录'},
        {'id': 1, 'name': 'JSP'},
        {'id': 2, 'name': 'ACTION'}
    ];
    //是否检查数组
    var opIsPassArr = [
        {'id': 0, 'name': '是'},
        {'id': 1, 'name': '否'}
    ];
    //拼接表单
    var html = getFormatSelectHtml("父权限", 500, OPERATION_ADMIN.GET_TREE_DATA(2), "opIIdSelect", 0, false, {'isAllow': true, 'value': 0});
    html += getFormatSelectHtml("权限类型", 500, opTypeArr, "opTypeSelect");
    html += getFormatInputHtml("权限名称", 500, "opName", 400, '', 0);
    html += getFormatInputHtml("权限url", 500, "opAction", 400, '', 0, true);
    html += getFormatInputHtml("权限备注", 500, "opNote", 400, '', 0);
    html += getFormatSelectHtml("是否检查", 500, opIsPassArr, "opIsPassSelect", 0, true);
    //写入元素
    $("#" + divId).html(html);
    //展示按钮
    showButton(0);
    //处理选择列表样式
    $("select").selectmenu().selectmenu("menuWidget").addClass("cbooverflow");
    $("#opIIdSelect-menu").click(function () {
        var pNode = OPERATION_ADMIN.GET_TREE().getNodeByParam("id", $("#opIIdSelect").val());
        var data = getOperation(pNode.id);//父节点资料
        $("#opTypeSelect").html(getFormatOptionHtml("权限类型", 400, getOpTypeArray(data.opType), "opTypeSelect", 0))
        $("select").selectmenu('refresh');
    });
    ctrlInputBySelect("opTypeSelect", "opActionDiv|opIsPassSelectDiv", 0);
}

/**
 * 展示增加子权限的界面
 */
function addOperationUIWithParent(divId) {
    //选中父节点
    var curNode = (OPERATION_ADMIN.GET_TREE().getSelectedNodes().length > 0 ? OPERATION_ADMIN.GET_TREE().getSelectedNodes()[0] : null);
    //查询父节点详情
    var data = getOperation(curNode.id);
    //是否检查数组
    var opIsPassArr = [
        {'id': 0, 'name': '是'},
        {'id': 1, 'name': '否'}
    ];
    //是否隐藏表单，当类型为目录时隐藏url和检查项
    var isHideInput = data.opType == 0 ? true : false;
    //拼接表单
    var html = getFormatSelectHtml("父权限", 500, [curNode], "opIIdSelect", data.opId);
    html += getFormatSelectHtml("权限类型", 500, getOpTypeArray(data.opType), "opTypeSelect", 0);
    html += getFormatInputHtml("权限名称", 500, "opName", 400, '', 0);
    html += getFormatInputHtml("权限url", 500, "opAction", 400, '', 0, isHideInput);
    html += getFormatInputHtml("权限备注", 500, "opNote", 400, '', 0);
    html += getFormatSelectHtml("是否检查", 500, opIsPassArr, "opIsPassSelect", 0, isHideInput);
    //写入元素
    $("#" + divId).html(html);
    //展示按钮
    showButton(0);
    //处理选择列表
    $("#opIIdSelect").selectmenu().selectmenu("menuWidget").addClass("cbooverflow");
    $("#opTypeSelect").selectmenu().selectmenu("menuWidget").addClass("cbooverflow");
    $("#opIsPassSelect").selectmenu().selectmenu("menuWidget").addClass("cbooverflow");
    ctrlInputBySelect("opTypeSelect", "opActionDiv|opIsPassSelectDiv", 0);
}

/**
 * 按钮切换
 * @param type
 */
function showButton(type) {
    if (type == 0) {//显示新增按钮
        $("#addOperationButton").show();
        $("#modifyOperactionButton").hide();
    }
    if (type == 1) {//显示修改按钮
        $("#addOperationButton").hide();
        $("#modifyOperactionButton").show();
    }
    if (type == 2) {//按钮消失
        $("#addOperationButton").hide();
        $("#modifyOperactionButton").hide();
    }
}

/**
 * 根据父节点权限类型获得当前节点权限类型选择数组
 * @param type 父节点权限类型
 * @returns {Array}
 */
function getOpTypeArray(type) {
    var opTypeArr = [];
    if (type == 0) {
        opTypeArr = [
            {'id': 0, 'name': '分组目录'},
            {'id': 1, 'name': 'JSP'}
        ];
    }
    if (type == 1) {
        opTypeArr = [
            {'id': 2, 'name': 'ACTION'}
        ];
    }
    return opTypeArr;
}

/**
 * 点击select列表，显示或隐藏表单
 * @param type 下拉列表id
 * @param inputDivIds 表单ids
 * @param value 符合值进行隐藏
 */
function ctrlInputBySelect(selectId, inputDivIds, value) {
    $("#" + selectId + "-menu").click(function () {
        $.each(inputDivIds.split("|"), function (index, element) {
            if ($("#" + selectId).val() == value) {
                $("#" + element).hide();
            } else {
                $("#" + element).show();
            }
        });
    });
}

//-------------------------树形控件-----------------------------------------
/**
 * 树形控件初始化
 */
function initTree(treeNodes) {
    //设置参数
    var setting = {
        checkable: false,
        treeNodeKey: "id",               //在isSimpleData格式下，当前节点id属性
        treeNodeParentKey: "pId",        //在isSimpleData格式下，当前节点的父节点id属性
        data: {
            simpleData: {
                enable: true
            }
        },
        callback: {
            onClick: zTreeOnClick,
            onRightClick: zTreeOnRightClick
        }
    };
    //初始化并获得树对象,右键对象
    var treeDiv = $("#" + OPERATION_ADMIN.GET_TREE_ID());
    var zTree = $.fn.zTree.init(treeDiv, setting, treeNodes);
//    zTree.expandAll(false);
    //把树形对象放入全局
    OPERATION_ADMIN.SET_TREE(zTree);
    OPERATION_ADMIN.SET_TREE_DATA(treeNodes);
    //把右键菜单放入全局
    OPERATION_ADMIN.SET_RMENU($("#" + OPERATION_ADMIN.GET_RMENU_ID()));
    //如果树为空，提示增加权限
    if (treeDiv.html() == '') {
        treeDiv.html('<span style="font-size:13px;margin-left: 65px;margin-top:100px">点击右键增加权限</span>');
    }
}

/**
 * 树左键点击事件处理方法
 */
function zTreeOnClick(event, treeId, treeNode) {
    var zTree = OPERATION_ADMIN.GET_TREE();
    //选中
    zTree.selectNode(treeNode, false);
    //聚焦
    zTree.expandNode(treeNode, true, false, true);
    //获取单个权限详情
    var data = getOperation(treeNode.id);
    //显示修改权限面板
    modifyOperationUI(data, "operationDetail");
}

/**
 * 树右键点击事件处理方法
 */
function zTreeOnRightClick(event, treeId, treeNode) {
    var zTree = OPERATION_ADMIN.GET_TREE();
    if (!treeNode) {
        zTree.cancelSelectedNode();
        showRMenu("root", event.clientX, event.clientY);
    }
    if (treeNode && !treeNode.noR) {
        if (treeNode.newrole && event.target.tagName != "a" && $(event.target).parents("a").length == 0) {
            zTree.cancelSelectedNode();
            showRMenu("root", event.clientX, event.clientY);
        }
        if (treeNode.type == 2) {
            zTree.selectNode(treeNode);
            showRMenu("leaf", event.clientX, event.clientY);
        } else {
            zTree.selectNode(treeNode);
            showRMenu("node", event.clientX, event.clientY);
        }
    }
}

/**
 * 树形图右键弹出窗，设置其弹出的位置和显示的内容
 */
function showRMenu(type, x, y) {
    $("#rMenu ul").show();
    if (type == "root") {
        $("#m_del").hide();
        $("#m_add_sub").hide();
        $("#m_add").show();
    }
    if (type == "leaf") {
        $("#m_del").show();
        $("#m_add").show();
        $("#m_add_sub").hide();
    }
    if (type == "node") {
        $("#m_del").show();
        $("#m_add").show();
        $("#m_add_sub").show();
    }
    OPERATION_ADMIN.GET_RMENU().css({
        "top": y + "px",
        "left": x + "px",
        "visibility": "visible"
    });

    $("body").bind("mousedown", onBodyMouseDown);
}

/**
 * 树形图右键菜单隐藏函数，在右键菜单被点击或在其他位置单击页面时会调用
 */
function hideRMenu() {
    if (OPERATION_ADMIN.GET_RMENU())
        OPERATION_ADMIN.GET_RMENU().css({
            "visibility": "hidden"
        });
    $("body").unbind("mousedown", onBodyMouseDown);
}

/**
 * 鼠标点击其他地方，隐藏右键菜单
 */
function onBodyMouseDown(event) {
    if (!(event.target.id == "rMenu" || $(event.target).parents("#rMenu").length > 0)) {
        OPERATION_ADMIN.GET_RMENU().css({
            "visibility": "hidden"
        });
//        OPERATION_ADMIN.GET_TREE().cancelSelectedNode();
    }
}
/**
 * 根据当前节点获取所有子节点id
 * @param treeNode
 * @returns {string}
 */
function getChildNodes(treeNode) {
    var childNodes = OPERATION_ADMIN.GET_TREE().transformToArray(treeNode);
    var nodes = new Array();
    for (var i = 0; i < childNodes.length; i++) {
        nodes[i] = childNodes[i].id;
    }
    return nodes.join(",");
}

/**
 * 更改树的图标
 * @param treeId 树id
 * @param imgPath 图片路径
 * @param selecter 选择符合type值的节点
 */
function changeIcon(imgPath, selecter) {
    //遍历更改图标
    var zTree = OPERATION_ADMIN.GET_TREE();
    $.each(OPERATION_ADMIN.GET_TREE_DATA(), function (index, element) {
        var node = zTree.getNodeByParam("id", element.id);
        if (!selecter) {//正常替换树所有节点
            node.icon = imgPath;
            zTree.updateNode(node);
        } else {//筛选符合type值的节点
            if (node.type == selecter) {
                node.icon = imgPath;
                zTree.updateNode(node);
            }
        }
    });
}

/**
 * 展开/收缩 树
 * @param element 点击的元素对象
 * @param word 显示文字
 */
function expandTree(element, word) {
    var div = $(element);
    var zTree = OPERATION_ADMIN.GET_TREE()
    if (div.html()[0] == '+') {
        div.html("- " + word);
        zTree.expandAll(true);
    } else {
        div.html("+ " + word);
        zTree.expandAll(false);
    }
}
//----------------------------公共函数-----------------------------------
/**
 * 获得格式化的表单html
 * @param key 表单表头名称
 * @param keyWidth 表头宽度
 * @param valueId 表单元素id
 * @param valueWidth 表单元素宽度
 * @param value 表单的值
 * @param disabled 1禁用 0启用
 * @param isHide false不隐藏 true隐藏
 */
function getFormatInputHtml(key, keyWidth, valueId, valueWidth, value, disabled, isHide) {
    var html = "<div id='" + valueId + "Div' style='display:" + (!isHide ? "block" : "none") + "'><ul class='modal-body-title'>" + key + "</ul><ul class='modal-body-value-manage' style='width:" + keyWidth + "px;'><input type='text' id='" + valueId + "' class='model-body-input' style='width:" + valueWidth + "px;' value='" + value + "' " + (disabled ? "disabled" : "") + "/></ul></div>";
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
function getFormatSelectHtml(key, keyWidth, selectArray, selectId, dataId, isHide, emptyOption) {
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
    var html = "<div id='" + selectId + "Div' style='display:" + (!isHide ? "block" : "none") + "'><ul class='modal-body-title'>" + key + "</ul><ul class='modal-body-value-manage' style='width:" + keyWidth + "px;'><select class='model-body-input' id='" + selectId + "'>" + selectHtml + "</select></ul></div>"
    return html;
}
/**
 * 获得格式化的选择框代码
 * @param selectArray 下拉框 html
 * @param emptyOption 是否允许空选项，格式{'isAllow':true,'value':0}
 */
function getFormatOptionHtml(selectArray, dataId, emptyOption) {
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
