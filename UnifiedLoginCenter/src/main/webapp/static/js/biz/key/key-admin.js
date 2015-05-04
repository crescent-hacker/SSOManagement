/**
 * Created by Administrator on 2014/11/18.
 */

//---------------------------定义全局变量------------------------------------
//用于存储当前操作的一些参数
/**
 * grid1，2,3,4对应所有key，库存，出库，挂失4张表格。
 * gridSelect对应当前选中的表格，如选中grid1，那么gridSelect就等于grid1。
 * count等于表格数量。
 * stopdeg:未使用
 * id_grid1,2,3,4和ind_grid1,2,3,4.是当点击表格时候，对应单元格的信息。如点击库存（对应2）表格第3行4列，那么id_grid2=3,ind_grid=3;
 * pageX，pageY代表点击事件时鼠标所在的坐标为（pageX,pageY）
 * data存储的是当前选中的表格中的所有数据。
 * @type {{grid1: null, grid2: null, grid3: null, grid4: null, gridSelect: null, count: number, stopdeg: null, id_grid1: null, id_grid2: null, id_grid3: null, id_grid4: null, ind_grid1: null, ind_grid2: null, ind_grid3: null, ind_grid4: null, pageX: null, pageY: null, data: null}}
 */
var global={
    grid1:null,
    grid2:null,
    grid3:null,
    grid4:null,
    gridSelect:null,
    count:4,
    stopdeg:null,
    id_grid1:null,
    id_grid2:null,
    id_grid3:null,
    id_grid4:null,
    ind_grid1:null,
    ind_grid2:null,
    ind_grid3:null,
    ind_grid4:null,
    pageX:null,
    pageY:null,
    data:null
}

//与后台交互传输的数据bean
var bean={
    id:-1,
    key_no:null,
    certificate_no:null,
    model:null,
    location:null,
    responsible_person:null,
    responsible_telephone:null,
    sign:null,
    inStorage_person:null,
    outStorage_person:null,
    loss_person:null,
    inStorage_date:null,
    outStorage_date:null,
    loss_date:null,
    lastModified_date:null,
    notes:null,
    clzt:-1
}

var bean_copy={
    id:-1,
    key_no:null,
    certificate_no:null,
    model:null,
    location:null,
    responsible_person:null,
    responsible_telephone:null,
    sign:null,
    inStorage_person:null,
    outStorage_person:null,
    loss_person:null,
    inStorage_date:null,
    outStorage_date:null,
    loss_date:null,
    lastModified_date:null,
    notes:null,
    clzt:-1
}

//-------------------------------------业务处理代码----------------------------------
$(document).ready(function(){
    initGrid();
    initGridEvent();
    global.gridSelect=global.grid1;
    $("#grid1").css("z-index","1");
    getGridData(1);
    initSearchBox("#searchKey", '输入key号搜索', function () {
        $("#searchButton").click();
    });

});
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
 * 获取当前日期时间
 * @returns {string}
 */
function getDate(){
    var date = new Date();
    var date_str = date.toLocaleString();
    return date_str;
}
/**
 * 将bean初始化，还原为最初值
 */
function beanToNull(){
    bean.id=-1;
    bean.key_no=null;
    bean.certificate_no=null;
    bean.model=null;
    bean.location=null;
    bean.responsible_person=null;
    bean.responsible_telephone=null;
    bean.sign=null;
    bean.inStorage_person=null,
    bean.outStorage_person=null,
    bean.loss_person=null,
    bean.inStorage_date=null,
    bean.outStorage_date=null,
    bean.loss_date=null,
    bean.lastModified_date=null,
    bean.notes=null,
    bean.clzt=-1
}

function beanCopyToNull(){
    bean_copy.id=-1;
    bean_copy.key_no=null;
    bean_copy.certificate_no=null;
    bean_copy.model=null;
    bean_copy.location=null;
    bean_copy.responsible_person=null;
    bean_copy.responsible_telephone=null;
    bean_copy.sign=null;
    bean_copy.inStorage_person=null,
    bean_copy.outStorage_person=null,
    bean_copy.loss_person=null,
    bean_copy.inStorage_date=null,
    bean_copy.outStorage_date=null,
    bean_copy.loss_date=null,
    bean_copy.lastModified_date=null,
    bean_copy.notes=null,
    bean_copy.clzt=-1
}

/**
 * 打开入库的操作板
 * @param button
 */
function openInsertDiv(button){
    $("#mask").css("display","block");
    $("#insertDiv").css("display","block");
    $("#insertDiv").css("opacity",0.5);
    $("#insertDiv").css("top","-100%");
    $("#insertDiv").animate({top:"20%",opacity:"1"},800,function(){
        $("input[name='insert_key_no']").val(GetUkeySerial());
        $("input[name='insert_inStorage_date']").val(getDate());
    });
}
/**
 * 关闭入库的操作板
 * @param button
 */
function closeInsertDiv(button){
    $("#insertDiv").css("opacity",1);
    $("#insertDiv").css("top","20%");
    $("#insertDiv").animate({top:"-100%",opacity:"0.5"},800,function(){
        $("input[name='insert_key_no']").val("");
        $("input[name='insert_certificate_no']").val("");
        $("input[name='insert_model']").val("");
        $("input[name='insert_location']").val("");
        $("input[name='insert_responsible_person']").val("");
        $("input[name='insert_responsible_telephone']").val("");
        $("input[name='insert_inStorage_person']").val("");
        $("input[name='insert_inStorage_date']").val("");
        $("input[name='insert_notes']").val("");
        $("#insertDiv").css("display","none");
        $("#mask").css("display","none");
    });
}
/**
 * 传输插入的数据到后台
 */
function insertKey(){
    bean.key_no=$("input[name='insert_key_no']").val();
    bean.certificate_no=$("input[name='insert_certificate_no']").val();
    bean.model=$("input[name='insert_model']").val();
    bean.location=$("input[name='insert_location']").val();
    bean.responsible_person=$("input[name='insert_responsible_person']").val();
    bean.responsible_telephone=$("input[name='insert_responsible_telephone']").val();
    bean.sign="库存";
    bean.inStorage_person=$("input[name='insert_inStorage_person']").val();
    bean.inStorage_date=$("input[name='insert_inStorage_date']").val();
    bean.notes=$("input[name='insert_notes']").val();
    sentToServlet("insert",bean);
    beanToNull();
}

/**
 * 打开修改的操作板
 * @param button
 */
function openUpdateDiv(button){
    var id=getId();
    bean.id=id;
    sentToServletAndGetList("searchById",bean);
}

function closeUpdateDiv(button){
    $("#updateDiv").css("opacity",1);
    $("#updateDiv").css("top","20%");
    $("#updateDiv").animate({top:"-100%",opacity:"0.5"},800,function(){
        $("#mask").css("display","none");
        $("#updateDiv").css("display","none");
    });
    $("input[name='update_id']").val("");
    $("input[name='update_key_no']").val("");
    $("input[name='update_certificate_no']").val("");
    $("input[name='update_model']").val("");
    $("input[name='update_location']").val("");
    $("input[name='update_responsible_person']").val("");
    $("input[name='update_responsible_telephone']").val("");
    $("select[name='update_sign']").val("");
    $("input[name='update_inStorage_person']").val("");
    $("input[name='update_outStorage_person']").val("");
    $("input[name='update_loss_person']").val("");
    $("input[name='update_inStorage_date']").val("");
    $("input[name='update_outStorage_date']").val("");
    $("input[name='update_loss_date']").val("");
    $("input[name='update_notes']").val("");
    beanToNull();

}
/**
 * 传输修改的数据到后台
 */
function updateKey(){
    bean.key_no=$("input[name='update_key_no']").val();
    bean.certificate_no=$("input[name='update_certificate_no']").val();
    bean.model=$("input[name='update_model']").val();
    bean.location=$("input[name='update_location']").val();
    bean.responsible_person=$("input[name='update_responsible_person']").val();
    bean.responsible_telephone=$("input[name='update_responsible_telephone']").val();
    bean.sign=$("select[name='update_sign']").val();
    bean.inStorage_person=$("input[name='update_inStorage_person']").val();
    bean.outStorage_person=$("input[name='update_outStorage_person']").val();
    bean.loss_person=$("input[name='update_loss_person']").val();
    bean.inStorage_date=$("input[name='update_inStorage_date']").val();
    bean.outStorage_date=$("input[name='update_outStorage_date']").val();
    bean.loss_date=$("input[name='update_loss_date']").val();
    bean.notes=$("input[name='update_notes']").val();
    sentToServlet("update",bean);
}
/**
 * 将要删除的数据传输至后台
 */
function deleteKey(){
    confirmNew("删除操作","确认删除?",function(){
        beanToNull();
        bean.id=getId();
        sentToServlet("delete",bean);
        closeMsg();
    },closeMsg);
}

/**
 * 获取点击表格行后的id属性（id=n，该id不是指表格的第n行，而是指当前点击的行数据，列名为id的数据值为n）
 * @returns {*}
 */
function getId(){
    if(global.gridSelect==null){
        alertNew("提示","未选中表格",closeMsg);
        return;
    }
    var id_select=null;
    var id=null;
    if(global.grid1==global.gridSelect) {
        id_select=global.id_grid1;
        if(id_select==null) {alertNew("提示","未选中要修改的信息",closeMsg); return;}
        id=global.grid1.cells(id_select,0).getValue();
    }
    if(global.grid2==global.gridSelect) {
        id_select=global.id_grid2;
        if(id_select==null) {alertNew("提示","未选中要修改的信息",closeMsg); return;}
        id=global.grid2.cells(id_select,0).getValue();
    }
    if(global.grid3==global.gridSelect) {
        id_select=global.id_grid3;
        if(id_select==null) {alertNew("提示","未选中要修改的信息",closeMsg); return;}
        id=global.grid3.cells(id_select,0).getValue();
    }
    if(global.grid4==global.gridSelect) {
        id_select=global.id_grid4;
        if(id_select==null) {alertNew("提示","未选中要修改的信息",closeMsg); return;}
        id=global.grid4.cells(id_select,0).getValue();
    }
    return id;
}

/**
 * 获取当前操作的表格（共有4张表格）
 * @returns {number}
 */
function getGridNum(){
    if(global.grid1==global.gridSelect){ return 1;}
    if(global.grid2==global.gridSelect){ return 2;}
    if(global.grid3==global.gridSelect){ return 3;}
    if(global.grid4==global.gridSelect){ return 4;}
}

/**
 * 将bean数据传至后台对应的actionName
 * @param actionName
 * @param bean1
 */
function sentToServlet(actionName,bean1){
    $.ajax({
        type: "POST",
        url: webRoot + "/key/keyAction!work.action",
        data: {
            'actionName':actionName,
            'bean':bean1
        },
        dataType: "json",
        success: function (data) {
            if (data.success) {
                closeInsertDiv();
                closeUpdateDiv();
                getGridData(getGridNum());
            }
            else {
                alertNew("提示", "操作失败！", closeMsg);
            }
        },
        error: function (xhr) {
            errorAlert(xhr)
        }
    });
}

/**
 * 获取当前选中的表格行数据的详细内容至修改面板
 * @param actionName  对应后台java的actionName
 * @param bean1 对应要修改的bean信息
 */
function sentToServletAndGetList(actionName,bean1){
    $.ajax({
        type: "POST",
        url: webRoot + "/key/keyAction!work.action",
        data: {
            'actionName':actionName,
            'bean':bean1
        },
        dataType: "json",
        success: function (data) {
            if (data.success) {
                var list = data.keyList;
                bean.id=list[0][0];
                bean.key_no=list[0][1];
                bean.certificate_no=list[0][2];
                bean.model=list[0][3];
                bean.location=list[0][4];
                bean.responsible_person=list[0][5];
                bean.responsible_telephone=list[0][6];
                bean.sign=list[0][7];
                bean.inStorage_person=list[0][8];
                bean.outStorage_person=list[0][9];
                bean.loss_person=list[0][10];
                bean.inStorage_date=list[0][11];
                bean.outStorage_date=list[0][12];
                bean.loss_date=list[0][13];
                bean.lastModified_date=list[0][14];
                bean.notes=list[0][15];
                bean.clzt=list[0][16];
                $("input[name='update_id']").val(bean.id);
                $("input[name='update_key_no']").val(bean.key_no);
                $("input[name='update_certificate_no']").val(bean.certificate_no);
                $("input[name='update_model']").val(bean.model);
                $("input[name='update_location']").val(bean.location);
                $("input[name='update_responsible_person']").val(bean.responsible_person);
                $("input[name='update_responsible_telephone']").val(bean.responsible_telephone);
                $("select[name='update_sign']").val(bean.sign);
                $("input[name='update_inStorage_person']").val(bean.inStorage_person);
                $("input[name='update_outStorage_person']").val(bean.outStorage_person);
                $("input[name='update_loss_person']").val(bean.loss_person);
                $("input[name='update_inStorage_date']").val(bean.inStorage_date);
                $("input[name='update_outStorage_date']").val(bean.outStorage_date);
                $("input[name='update_loss_date']").val(bean.loss_date);
                $("input[name='update_notes']").val(bean.notes);
                $("#updateDiv").css("display","block");
                $("#updateDiv").css("opacity",0.7);
                $("#updateDiv").css("top","-100%");
                $("#updateDiv").animate({top:"20%",opacity:"1"},800,function(){});
                $("#mask").css("display","block");
            }
            else {
                alertNew("提示", "获取数据失败！", closeMsg);
            }
        },
        error: function (xhr) {
            errorAlert(xhr);
        }
    });
}


/**
 * 按钮点击后执行的操作，此处的按钮对应key操作的4个button
 * @param button
 */
function buttonClick(button){
    //点击button时候会改变button样式
    $("button").removeClass("active");
    $(button).addClass("active");
    $("#grid1").css("z-index","0");
    $("#grid2").css("z-index","0");
    $("#grid3").css("z-index","0");
    $("#grid4").css("z-index","0");
    global.id_grid1=null;
    global.id_grid2=null;
    global.id_grid3=null;
    global.id_grid4=null;
    global.ind_grid1=null;
    global.ind_grid2=null;
    global.ind_grid3=null;
    global.ind_grid4=null;
    var buttonName = $(button).attr("name");
    if(buttonName=="inStorageButton"){
        $("#grid1").css("z-index","1");
        getGridData(1);
    }else if(buttonName=="outStorageButton"){
        $("#grid2").css("z-index","1");
        getGridData(2);
    }else if(buttonName=="storageButton"){
        $("#grid3").css("z-index","1");
        getGridData(3);
    }else if(buttonName=="lossButton"){
        $("#grid4").css("z-index","1");
        getGridData(4);
    }
}

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

/**
 * 初始化表格，对应jsp中的gridbox
 */
function initGrid() {

    var grid1 = new dhtmlXGridObject('grid1');
    grid1.setImagePath(webRoot + "/static/css/dhtmlXGrid/imgs/");
    //11个参数:id,key_no,certificate_no,model,location,responsible_person,responsible_telephone,sign,inStorage_person,inStorage_date,notes
    //id,key号,型号,证书号,key所在地,key负责人,key负责人电话,当前状态,入库经手人,入库日期,备注
    grid1.setHeader("id,key号,证书号,型号,key所在地,key负责人,key负责人电话,当前状态,入库经手人,入库日期,备注");
    grid1.setInitWidths("0,100,100,100,100,100,120,100,100,100,100");
    grid1.setColTypes("ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro");
    grid1.setSkin("dhx_skyblue");
    grid1.detachHeader(0);
    grid1.init();
    setGridHeadColor("#0099cc",grid1.getColumnsNum(),'grid1');

    var grid2 = new dhtmlXGridObject('grid2');
    grid2.setImagePath(webRoot + "/static/css/dhtmlXGrid/imgs/");
    //id,key_no,certificate_no,model,location,responsible_person,responsible_telephone,sign,outStorage_person,outStorage_date,notes
    //id,key号,证书号,型号,key所在地,key负责人,key负责人电话,当前状态,出库经手人,出库日期,备注
    grid2.setHeader("id,key号,证书号,型号,key所在地,key负责人,key负责人电话,当前状态,出库经手人,出库日期,备注");
    grid2.setInitWidths("0,100,100,100,100,100,120,100,100,100,100");
    grid2.setColTypes("ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro");
    grid2.setSkin("dhx_skyblue");
    grid2.init();
    setGridHeadColor("#0099cc",grid1.getColumnsNum(),'grid2');

    var grid3 = new dhtmlXGridObject('grid3');
    grid3.setImagePath(webRoot + "/static/css/dhtmlXGrid/imgs/");
    //id,key_no,certificate_no,model,location,responsible_person,responsible_telephone,sign,inStorage_person,inStorage_date,notes
    //id,key号,证书号,型号,key所在地,key负责人,key负责人电话,当前状态,库存管理人,库存日期,备注
    grid3.setHeader("id,key号,证书号,型号,key所在地,key负责人,key负责人电话,当前状态,入库经手人,入库日期,备注");
    grid3.setInitWidths("0,100,100,100,100,100,120,100,100,100,100");
    grid3.setColTypes("ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro");
    grid3.setSkin("dhx_skyblue");
    grid3.init();
    setGridHeadColor("#0099cc",grid1.getColumnsNum(),'grid3');

    var grid4 = new dhtmlXGridObject('grid4');
    grid4.setImagePath(webRoot + "/static/css/dhtmlXGrid/imgs/");
    //id,key_no,certificate_no,model,location,responsible_person,responsible_telephone,sign,loss_person,loss_date,notes
    //id,key号,证书号,型号,key所在地,key负责人,key负责人电话,当前状态,挂失注册人,挂失日期,备注
    grid4.setHeader("id,key号,证书号,型号,key所在地,key负责人,key负责人电话,当前状态,挂失注册人,挂失日期,备注");
    grid4.setInitWidths("0,100,100,100,100,100,120,100,100,100,100");
    grid4.setColTypes("ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro");
    grid4.setSkin("dhx_skyblue");
    grid4.init();
    setGridHeadColor("#0099cc",grid1.getColumnsNum(),'grid4');


    global.grid1=grid1;
    global.grid2=grid2;
    global.grid3=grid3;
    global.grid4=grid4;

/*    transform($("#gridbox"),1000,0,0,0,0,0,-268);
    transform($("#grid1"),1000,0,0,0,0,0,215);
    transform($("#grid2"),1000,90,0,0,0,0,215);
    transform($("#grid3"),1000,180,0,0,0,0,215);
    transform($("#grid4"),1000,270,0,0,0,0,215);

   rotateXAction($("#gridbox"),0,1,90,function(){
        $("#grid1").animate({width:"100%",height:"100%",left:"0%",top:"0%"},1000,function(){});
        $("#grid2").animate({width:"100%",height:"100%",left:"0%",top:"0%"},1000,function(){});
        $("#grid3").animate({width:"100%",height:"100%",left:"0%",top:"0%"},1000,function(){});
        $("#grid4").animate({width:"100%",height:"100%",left:"0%",top:"0%"},1000,function(){});
    });*/
    //setGridHeadColor
}
/**
 * 初始化4张表格事件
 */
function initGridEvent(){
    global.grid1.attachEvent("onRowSelect",function(id,ind){
        global.id_grid1=id;
        global.ind_grid1=ind;
        openSignDiv(ind)
    });
    global.grid2.attachEvent("onRowSelect",function(id,ind){
        global.id_grid2=id;
        global.ind_grid2=ind;
        openSignDiv(ind)
    });
    global.grid3.attachEvent("onRowSelect",function(id,ind){
        global.id_grid3=id;
        global.ind_grid3=ind;
        openSignDiv(ind)
    });
    global.grid4.attachEvent("onRowSelect",function(id,ind){
        global.id_grid4=id;
        global.ind_grid4=ind;
        openSignDiv(ind)
    });
    $(document).bind({"mousemove":function(e){

        },
        "click":function(e){
            $("#signDiv").css("display","none");
        },
        "mousedown":function(e){
            e=e||window.event;
            global.pageX= e.pageX;
            global.pageY= e.pageY;
        }
    });
}
/**
 * 当选中表格的key状态时，打开修改状态的div
 * @param ind 表格的列。ind=7 代表ind第七列
 */
function openSignDiv(ind){
    $("#signDiv").css("display","none");
    if(ind!=7)  return;
    $("#signDiv").css("display","block");
    $("#signDiv").css("top", global.pageY);
    $("#signDiv").css("left", global.pageX);
}

/**
 * 传输要修改的行的状态至后台处理
 * @param sign 代表key状态，分别是出库，库存，挂失
 */
function changeSign(sign){
    var id=getId();
    beanToNull();
    bean.id=id;
    bean.sign=sign;
    var actionName = "updateSign";
    $.ajax({
        type: "POST",
        url: webRoot + "/key/keyAction!work.action",
        data: {
            'actionName':actionName,
            'bean':bean
        },
        dataType: "json",
        success: function (data) {
            if (data.success) {
                getGridData(getGridNum());
                beanToNull();
            }
            else {
                alertNew("提示", "修改key状态失败！", closeMsg);
            }
        },
        error: function (xhr) {
            errorAlert(xhr)
        }
    });
}

/**
 * 根据gridNum，从后台获取相对应的表格数据
 * @param gridNum  1对应全部key信息，2对应出库key，3对应库存key，4对应挂失key
 */
function getGridData(gridNum){
    var actionName=null,grid=null;
    switch (gridNum){
        case 1:actionName="search_inStorage"; global.gridSelect=global.grid1;break;
        case 2:actionName="search_outStorage"; global.gridSelect=global.grid2;break;
        case 3:actionName="search_storage"; global.gridSelect=global.grid3;break;
        case 4:actionName="search_loss"; global.gridSelect=global.grid4;break;
    }
    $.ajax({
        type: "POST",
        url: webRoot + "/key/keyAction!work.action",
        data: {
            'actionName':actionName
        },
        dataType: "json",
        success: function (data) {
            if (data.success) {
                global.gridSelect.clearAll(false);
                global.gridSelect.parse(data.keyList, "", "jsarray");
                global.data=data.keyList;
            }
            else {
                alertNew("提示", "查询入库列表失败！", closeMsg);
            }
        },
        error: function (xhr) {
            errorAlert(xhr)
        }
    });
}

/**
 * 根据搜索框信息筛选key号码
 * @param button 未使用，代表点击的按钮对象
 */
function searchKey(button){
    var content = $("input[name='searchKey']").val();
    global.gridSelect.clearAll(false);
    global.gridSelect.parse(global.data, "", "jsarray");
    global.gridSelect.filterBy(1,content,true);
}

function copyKey(){
    if($("#updateDiv").css("display")=="block") {
        bean_copy.id = $("input[name='update_id']").val();
        bean_copy.key_no = $("input[name='update_key_no']").val();
        bean_copy.certificate_no = $("input[name='update_certificate_no']").val();
        bean_copy.model = $("input[name='update_model']").val();
        bean_copy.location = $("input[name='update_location']").val();
        bean_copy.responsible_person = $("input[name='update_responsible_person']").val();
        bean_copy.responsible_telephone = $("input[name='update_responsible_telephone']").val();
        bean_copy.sign = $("select[name='update_sign']").val();
        bean_copy.inStorage_person = $("input[name='update_inStorage_person']").val();
        bean_copy.outStorage_person = $("input[name='update_outStorage_person']").val();
        bean_copy.loss_person = $("input[name='update_loss_person']").val();
        bean_copy.inStorage_date = $("input[name='update_inStorage_date']").val();
        bean_copy.outStorage_date = $("input[name='update_outStorage_date']").val();
        bean_copy.loss_date = $("input[name='update_loss_date']").val();
        bean_copy.notes = $("input[name='update_notes']").val();
    }else if($("#insertDiv").css("display")=="block"){
        bean_copy.key_no=$("input[name='insert_key_no']").val();
        bean_copy.certificate_no=$("input[name='insert_certificate_no']").val();
        bean_copy.model=$("input[name='insert_model']").val();
        bean_copy.location=$("input[name='insert_location']").val();
        bean_copy.responsible_person=$("input[name='insert_responsible_person']").val();
        bean_copy.responsible_telephone=$("input[name='insert_responsible_telephone']").val();
        bean_copy.sign=$("select[name='insert_sign']").val();
        bean_copy.inStorage_person=$("input[name='insert_inStorage_person']").val();
        bean_copy.inStorage_date=$("input[name='insert_inStorage_date']").val();
        bean_copy.notes=$("input[name='insert_notes']").val();
    }
    $("input[name='copy_id']").val(bean_copy.id);
    $("input[name='copy_key_no']").val(bean_copy.key_no);
    $("input[name='copy_certificate_no']").val(bean_copy.certificate_no);
    $("input[name='copy_model']").val(bean_copy.model);
    $("input[name='copy_location']").val(bean_copy.location);
    $("input[name='copy_responsible_person']").val(bean_copy.responsible_person);
    $("input[name='copy_responsible_telephone']").val(bean_copy.responsible_telephone);
    $("select[name='copy_sign']").val(bean_copy.sign);
    $("input[name='copy_inStorage_person']").val(bean_copy.inStorage_person);
    $("input[name='copy_outStorage_person']").val(bean_copy.outStorage_person);
    $("input[name='copy_loss_person']").val(bean_copy.loss_person);
    $("input[name='copy_inStorage_date']").val(bean_copy.inStorage_date);
    $("input[name='copy_outStorage_date']").val(bean_copy.outStorage_date);
    $("input[name='copy_loss_date']").val(bean_copy.loss_date);
    $("input[name='copy_notes']").val(bean_copy.notes);
}

function pasteKey(){
    if($("#updateDiv").css("display")=="block") {
        bean_copy.id = $("input[name='update_id']").val();
        bean_copy.key_no = $("input[name='update_key_no']").val();
        bean_copy.certificate_no = $("input[name='update_certificate_no']").val();
        bean_copy.model = $("input[name='update_model']").val();
        bean_copy.location = $("input[name='update_location']").val();
        bean_copy.responsible_person = $("input[name='update_responsible_person']").val();
        bean_copy.responsible_telephone = $("input[name='update_responsible_telephone']").val();
        bean_copy.sign = $("select[name='update_sign']").val();
        bean_copy.inStorage_person = $("input[name='update_inStorage_person']").val();
        bean_copy.outStorage_person = $("input[name='update_outStorage_person']").val();
        bean_copy.loss_person = $("input[name='update_loss_person']").val();
        bean_copy.inStorage_date = $("input[name='update_inStorage_date']").val();
        bean_copy.outStorage_date = $("input[name='update_outStorage_date']").val();
        bean_copy.loss_date = $("input[name='update_loss_date']").val();s
        bean_copy.notes = $("input[name='update_notes']").val();
    }else if($("#insertDiv").css("display")=="block"){
        bean_copy.key_no=$("input[name='insert_key_no']").val();
        bean_copy.certificate_no=$("input[name='insert_certificate_no']").val();
        bean_copy.model=$("input[name='insert_model']").val();
        bean_copy.location=$("input[name='insert_location']").val();
        bean_copy.responsible_person=$("input[name='insert_responsible_person']").val();
        bean_copy.responsible_telephone=$("input[name='insert_responsible_telephone']").val();
        bean_copy.sign=$("select[name='insert_sign']").val();
        bean_copy.inStorage_person=$("input[name='insert_inStorage_person']").val();
        bean_copy.inStorage_date=$("input[name='insert_inStorage_date']").val();
        bean_copy.notes=$("input[name='insert_notes']").val();
    }
}

/**
 * 翻转X轴，未使用
 * @param JQueryObject
 * @param initdeg
 * @param v
 * @param stopdeg
 * @param callback
 */
function rotateXAction(JQueryObject,initdeg,v,stopdeg,callback){
    JQueryObject.animate({width:"+=0"},1,function(){
        //transform($("#gridbox"),1000,0,0,0,0,0,0);
        transform($("#grid1"),1000,(0+initdeg+v)%360,0,0,0,0,-215);
        transform($("#grid2"),1000,(90+initdeg+v)%360,0,0,0,0,-215);
        transform($("#grid3"),1000,(180+initdeg+v)%360,0,0,0,0,-215);
        transform($("#grid4"),1000,(270+initdeg+v)%360,0,0,0,0,-215);
        v++;
        rotateXAction(JQueryObject,initdeg,v,stopdeg,callback);
    });
}

/**
 * 修改div对应的3d状态，未使用
 * @param JQueryObject
 * @param per
 * @param rotX
 * @param rotY
 * @param rotZ
 * @param transX
 * @param transY
 * @param transZ
 * @returns {{JQueryObject: *, per: *, rotX: *, rotY: *, rotZ: *, transX: *, transY: *, transZ: *}}
 */
function transform(JQueryObject,per,rotX,rotY,rotZ,transX,transY,transZ){
    JQueryObject.css("-webkit-transform","perspective("+per+"px) rotateX("+rotX+"deg) rotateY("+rotY+"deg) rotateZ("+rotZ+"deg) translate3d("+transX+"px,"+transY+"px,"+transZ+"px)");    /* for Chrome || Safari */
    JQueryObject.css("-moz-transform","perspective("+per+"px) rotateX("+rotX+"deg) rotateY("+rotY+"deg) rotateZ("+rotZ+"deg) translate3d("+transX+"px,"+transY+"px,"+transZ+"px)");       /* for Firefox */
    JQueryObject.css("-ms-transform", "perspective("+per+"px) rotateX("+rotX+"deg) rotateY("+rotY+"deg) rotateZ("+rotZ+"deg) translate3d("+transX+"px,"+transY+"px,"+transZ+"px)");        /* for IE */
    JQueryObject.css("-o-transform","perspective("+per+"px) rotateX("+rotX+"deg) rotateY("+rotY+"deg) rotateZ("+rotZ+"deg) translate3d("+transX+"px,"+transY+"px,"+transZ+"px)");         /* for Opera */
    var map={"JQueryObject":JQueryObject,"per":per,"rotX":rotX,"rotY":rotY,"rotZ":rotZ,"transX":transX,"transY":transY,"transZ":transZ};
    return map;
}