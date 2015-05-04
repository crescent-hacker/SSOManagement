/**
 * author:cdf
 * date:2014-10-8
 * desc:角色管理js
 */
//---------------------------定义全局变量------------------------------------
var ROLE_ADMIN = (function () {
    /*-----------变量---------------*/
    //新增角色提示语
    var TIP = "输入角色名新增";
    //树形对象
    var TREE = {};
    //树id： 权限树、角色树、可继承角色树
    var TREE_ID = {'TREE_VIEW': 'treeView', 'ROLE_LIST': 'roleList', 'STEP_ROLE_LIST': 'stepRoleList'};
    //树形所有节点
    var TREE_DATA = {};
    /*-----------函数----------------*/
    //获得新增角色表单提示语
    function GET_TIP() {
        return TIP;
    }
    //获得树
    function GET_TREE(ID) {
        return TREE[ID];
    }

    //设置树
    function SET_TREE(ID, V_TREE) {
        TREE[ID] = V_TREE;
    }

    //获得树形所有节点资料,带类型过滤器
    function GET_TREE_DATA(ID, FILTER) {
        if (!FILTER) {
            return TREE_DATA[ID];
        } else {
            var tmpTreeData = [];
            $.each(TREE_DATA[ID], function (index, element) {
                if (element.type != FILTER) {
                    tmpTreeData.push(element);
                }
            });
            return tmpTreeData;
        }
    }

    //设置树形所有节点资料
    function SET_TREE_DATA(ID, V_TREE_DATA) {
        TREE_DATA[ID] = V_TREE_DATA;
    }

    /*-----------返回json----------------*/
    //组装返回闭包
    var returnObj = {
        'GET_TIP': GET_TIP,
        'GET_TREE': GET_TREE,
        'SET_TREE': SET_TREE,
        'GET_TREE_DATA': GET_TREE_DATA,
        'SET_TREE_DATA': SET_TREE_DATA,
        'TREE_VIEW': TREE_ID['TREE_VIEW'],
        'ROLE_LIST': TREE_ID['ROLE_LIST'],
        'STEP_ROLE_LIST': TREE_ID['STEP_ROLE_LIST']
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
                initTree(ROLE_ADMIN.TREE_VIEW, data.treeList, true);
                //进行图标替换
                changeIcon(ROLE_ADMIN.TREE_VIEW, webRoot + "/static/images/icon_document.png", 0);
                changeIcon(ROLE_ADMIN.TREE_VIEW, webRoot + "/static/images/icon_page.png", 1);
                changeIcon(ROLE_ADMIN.TREE_VIEW, webRoot + "/static/images/icon_action.png", 2);
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
 * 获得角色列表
 */
function getRoleList() {
    $.ajax({
        type: "POST",
        async: false,
        url: webRoot + "/role/roleAction!getRoleList.action",
        data: {
            'systemId': $("input[name='systemId']").val()
        },
        dataType: "json",
        success: function (data) {
            if (data.success) {
                initTree(ROLE_ADMIN.ROLE_LIST, data.roleList, false, true, false);
                changeIcon(ROLE_ADMIN.ROLE_LIST, webRoot + "/static/images/icon_person.png");
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
 * 获得角色的多个权限
 */
function getRoleRights(roleId) {
    $.ajax({
        type: "POST",
        async: false,
        url: webRoot + "/role/roleAction!getRoleRights.action",
        data: {
            'roleId': roleId
        },
        dataType: "json",
        success: function (data) {
            if (data.success) {
                var zTree = ROLE_ADMIN.GET_TREE(ROLE_ADMIN.TREE_VIEW);
                //取消选中
                $.each(ROLE_ADMIN.GET_TREE_DATA(ROLE_ADMIN.TREE_VIEW), function (index, element) {
                    zTree.checkNode(zTree.getNodeByParam("id", element.id), false, true);
                });
                //进行自动选中
                $.each(data.roleRightList, function (index, element) {
                    zTree.checkNode(zTree.getNodeByParam("id", element), true, false);
                });
            }
            else {
                alertNew("提示", "查询角色权限失败！", closeMsg);
            }
        },
        error: function (xhr) {
            errorAlert(xhr)
        }
    });
}

/**
 * 增加角色
 */
function addRole() {
    //检查表单非空
    var roleNameSelector = "#roleName";
    if (!checkNull([roleNameSelector],[ROLE_ADMIN.GET_TIP()])) {
        return false;
    }
    //获得权限集
    var accessArray = [];
    $.each(ROLE_ADMIN.GET_TREE(ROLE_ADMIN.TREE_VIEW).getCheckedNodes(), function (index, element) {
        //排除分类目录的节点
        if (element.type != 0) {
            accessArray.push(element.id);
        }
    });
    $.ajax({
        type: "POST",
        url: webRoot + "/role/roleAction!addRole.action",
        data: {
            'systemId': $("input[name='systemId']").val(),
            'name': $("input[name='roleName']").val(),
            'access': accessArray.join(",")
        },
        dataType: "json",
        success: function (data) {
            if (data.success) {
                alertNew("提示", "增加角色成功！", closeMsg);
                //刷新
                queryByCat($("#systemId").val());
            }
            else {
                alertNew("提示", (!data.message) ? "增加角色失败！" : data.message, closeMsg);
            }
        },
        error: function (xhr) {
            errorAlert(xhr)
        }
    });
}

/**
 * 修改角色
 */
function modifyRole() {
    //检查表单非空
    var roleNameSelector = "#roleName";
    if (!checkNull([roleNameSelector])) {
        return false;
    }
    //获得权限集
    var accessArray = [];
    $.each(ROLE_ADMIN.GET_TREE(ROLE_ADMIN.TREE_VIEW).getCheckedNodes(), function (index, element) {
        //排除分类目录的节点
        if (element.type != 0) {
            accessArray.push(element.id);
        }
    });
    //角色id
    var roleId = $("input[name='currentRecord']").val();
    $.ajax({
        type: "POST",
        url: webRoot + "/role/roleAction!modifyRole.action",
        data: {
            'name': $("input[name='roleName']").val(),
            'access': accessArray.join(","),
            'roleId': roleId
        },
        dataType: "json",
        success: function (data) {
            if (data.success) {
                alertNew("提示", "修改角色成功！", closeMsg);
                //重新获得角色列表
                getRoleList();
                //定位和展示修改后的角色节点
                var zTree = ROLE_ADMIN.GET_TREE(ROLE_ADMIN.ROLE_LIST);
                var node = zTree.getNodeByParam("id", roleId);
                zTree.selectNode(node);
                modifyRoleUI(node);
            }
            else {
                alertNew("提示", (!data.message) ? "修改角色失败！" : data.message, closeMsg);
            }
        },
        error: function (xhr) {
            errorAlert(xhr)
        }
    });
}

/**
 * 删除角色
 */
function delRole() {
    //角色id
    var roleId = $("input[name='currentRecord']").val();
    $.ajax({
        type: "POST",
        url: webRoot + "/role/roleAction!delRole.action",
        data: {
            'roleId': roleId
        },
        dataType: "json",
        success: function (data) {
            if (data.success) {
                alertNew("提示", "删除角色成功！", closeMsg);
                //刷新
                queryByCat($("#systemId").val());
            }
            else {
                alertNew("提示", (!data.message) ? "删除角色失败！" : data.message, closeMsg);
            }
        },
        error: function (xhr) {
            errorAlert(xhr)
        }
    });
}

/**
 * 用系统id筛选权限
 * @param systemId
 * @param thisButton
 */
function queryByCat(systemId, thisButton) {
    if (!(!thisButton)) {
        $("button[name='systemButton']").removeClass("active");
        $(thisButton).addClass('active')
    }
    $("input[name='systemId']").val(systemId);
    //初始化加载树
    getTree();
    //初始化加载角色列表
    getRoleList();
    //清空表单并绑定tips
    $("#roleName").val("");
    initSearchBox("#roleName",ROLE_ADMIN.GET_TIP(),addRole);
    $("#currentRecord").val("");
    $("#currentRecordDiv").val("");
    //按钮复位
    showButton(0);
    //展开树按钮复位
    $("#expand").html("+ 权限树");
}

//---------------------------页面初始化------------------------------------
$(document).ready(function () {
    //区域高度调整
    var eDiv = ["#leftTopBlock", "#leftBottomBlock", "#rightBlock", "#leftTopBlockInner", "#leftBottomBlockInner", "#rightBlockInner", ".container"];
    setElementsHeight(eDiv);
    $(window).resize(function () {
        setElementsHeight(eDiv);
    });
    //初始化界面
    queryByCat('6650');
});

//-------------------------树形控件-----------------------------------------
/**
 * 树形控件初始化
 */
function initTree(id, treeNodes, isCheckable, isCallBack, isHideIcon) {
    //设置参数
    var setting = {
        check: {
            enable: isCheckable,
            chkStyle: "checkbox",
            chkboxType: { "Y": "s", "N": "s" }
        },
        treeNodeKey: "id",               //在isSimpleData格式下，当前节点id属性
        treeNodeParentKey: "pId",        //在isSimpleData格式下，当前节点的父节点id属性
        data: {
            simpleData: {
                enable: true
            }
        },
        callback: {
            onClick: zTreeOnClick
        },
        view: {
            showIcon: !isHideIcon
        }
    };
    if (!isCallBack) {
        setting.callback = {};
    }
    //初始化并获得树对象,右键对象
    var zTree = $.fn.zTree.init($("#" + id), setting, treeNodes);
//    zTree.expandAll(true);
    //把树形对象放入全局
    ROLE_ADMIN.SET_TREE(id, zTree);
    ROLE_ADMIN.SET_TREE_DATA(id, treeNodes);
}

/**
 * 树左键点击事件处理方法
 */
function zTreeOnClick(event, treeId, treeNode) {
    //判断是否再点一次取消选择
    if (cancelSelect(event)) {
        return false;
    }
    //记录事件点击div
    $("input[name='currentRecordDiv']").val(event.target.id);
    //展示修改界面
    modifyRoleUI(treeNode);
}

/**
 * 鼠标点击其他地方，隐藏右键菜单
 */
function cancelSelect(event) {
    if (event.target.id == $("input[name='currentRecordDiv']").val()) {
        ROLE_ADMIN.GET_TREE(ROLE_ADMIN.ROLE_LIST).cancelSelectedNode();
        //清空当前选择角色
        $("input[name='currentRecord']").val("");
        //清空当前选择角色DIV
        $("input[name='currentRecordDiv']").val("");
        //清空角色表单并初始化
        $("#roleName").val("");
        initSearchBox("#roleName",ROLE_ADMIN.GET_TIP(),addRole);
        //进行按钮切换
        showButton(0);
        //取消选中权限
        var zTree = ROLE_ADMIN.GET_TREE(ROLE_ADMIN.TREE_VIEW);
        $.each(ROLE_ADMIN.GET_TREE_DATA(ROLE_ADMIN.TREE_VIEW), function (index, element) {
            zTree.checkNode(zTree.getNodeByParam("id", element.id), false, true);
        });
        return true;
    }
    return false;
}

/**
 * 展开/收缩 树
 * @param element 点击的元素对象
 * @param word 显示文字
 */
function expandTree(element, word) {
    var div = $(element);
    var zTree = ROLE_ADMIN.GET_TREE(ROLE_ADMIN.TREE_VIEW)
    if (div.html()[0]=='+') {
        div.html("- " + word);
        zTree.expandAll(true);
    } else {
        div.html("+ " + word);
        zTree.expandAll(false);
    }
}
//-------------------------页面展示辅助方法-----------------------------------------
/**
 * 按钮切换
 * @param type
 */
function showButton(type) {
    if (type == 0) {//显示新增按钮
        $("#addButton").show();
        $("#modifyButton").hide();
    }
    if (type == 1) {//显示修改按钮
        $("#addButton").hide();
        $("#modifyButton").show();
    }
}
/**
 * 更改树的图标
 * @param treeId 树id
 * @param imgPath 图片路径
 * @param selecter 选择符合type值的节点
 */
function changeIcon(treeId, imgPath, selecter) {
    //遍历更改图标
    var zTree = ROLE_ADMIN.GET_TREE(treeId);
    $.each(ROLE_ADMIN.GET_TREE_DATA(treeId), function (index, element) {
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
 * 显示角色修改界面
 * @param treeNode
 */
function modifyRoleUI(treeNode) {
    //获取列表
    getRoleRights(treeNode.id);
    //写入当前选择角色
    $("input[name='currentRecord']").val(treeNode.id);
    //写入角色表单
    $("input[name='roleName']").val(treeNode.name);
    //进行按钮切换
    showButton(1);
}


