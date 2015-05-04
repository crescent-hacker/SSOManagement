var Popup_showtimes=0;
var max_z_index = 110;
/**
 * 弹出窗体
 * @param name 标题
 * @param html 内容
 * @param width 宽度
 * @param height 高度
 */
function showPopup(name,html,width,height,detailsHeight) {  
	var msgw, msgh, bordercolor;
	Popup_showtimes++;
    if (Popup_showtimes>1){
    	return;
    }
    bordercolor = "#336699"; //提示窗口的边框颜色
    titlecolor = "#99CCFF"; //提示窗口的标题颜色
    
    var sWidth, sHeight;
    sWidth = getViewportWidth();
    sHeight = getViewportHeight();
    
    var bgObj = document.createElement("div");
    bgObj.setAttribute('id', 'bgDiv');
    bgObj.style.position = "absolute";
    bgObj.style.top = "0";
    bgObj.style.background = "#777";
    bgObj.style.filter = "progid:DXImageTransform.Microsoft.Alpha(style=3,opacity=25,finishOpacity=75";
    bgObj.style.opacity = "0.6";
    bgObj.style.left = "0";
    bgObj.style.width = sWidth + "px";
    bgObj.style.height = sHeight + "px";
    bgObj.style.zIndex = 1000;
    document.body.appendChild(bgObj);
    var popUp = document.createElement("div");
    popUp.setAttribute("id", "popupcontent");
    popUp.setAttribute("class", "popupcontent");
    var h=200;
    if(detailsHeight!=undefined)h=detailsHeight;
    popUp.style.top =200+200-detailsHeight+ "px";  
	popUp.style.left = (document.body.clientWidth/ 2 -width/2) +"px";
	popUp.style.width = width + "px";
	popUp.style.height = height + "px";
	popUp.style.visibility = "visible";
	popUp.style.zIndex = 1001;
    document.body.appendChild(popUp);
    $("#popupcontent").html("<div class='modal-dialog'>"+
    "<div class='modal-content'>"+
      "<div class='modal-header'><div class='modal-title-bg'><h4 class='modal-title' id='myModalLabel'>"+name+"</h4></div><img src='"+webRoot+"/static/images/bg_popupModel.png'  class='modal-header-bg'/></div>"+
      "<div class='modal-body' id='details' style='height: "+h+"px;'>"+html+"</div>"+
      "<div class='modal-footer'><button type='button' class='btn btn-default' onclick='killPopup();' >关闭</button></div>"+
      "</div></div>");
}  

/**
 * 弹出窗体(表格切换)
 * @param name 明细的标题
 * @param name2 表格的标题
 * @param html 明细的内容
 * @param width 宽度
 * @param height 高度
 * @param Header 表格的表头
 * @param Widths 表格的列宽度
 * @param Align 表格的列排序
 * @param Types 表格的列类型
 * @param gridData 表格的数据
 * @param htmlHeight 内容的高度
 */
function showPopup2(name,name2,html,width,height,Header,Widths,Align,Types,gridData,htmlHeight) {  
	var msgw, msgh, bordercolor;
	Popup_showtimes++;
    if (Popup_showtimes>1){
    	return;
    }
    bordercolor = "#336699"; //提示窗口的边框颜色
    titlecolor = "#99CCFF"; //提示窗口的标题颜色
    
    var sWidth, sHeight;
    sWidth = getViewportWidth();
    sHeight = getViewportHeight();
    
    var bgObj = document.createElement("div");
    bgObj.setAttribute('id', 'bgDiv');
    bgObj.style.position = "absolute";
    bgObj.style.top = "0";
    bgObj.style.background = "#777";
    bgObj.style.filter = "progid:DXImageTransform.Microsoft.Alpha(style=3,opacity=25,finishOpacity=75";
    bgObj.style.opacity = "0.6";
    bgObj.style.left = "0";
    bgObj.style.width = sWidth + "px";
    bgObj.style.height = sHeight + "px";
    document.body.appendChild(bgObj);
    var popUp = document.createElement("div");
    popUp.setAttribute("id", "popupcontent");
    popUp.setAttribute("class", "popupcontent");
    var h=200;
    if(htmlHeight!=undefined)h=(document.body.clientHeight/ 2 -height/2);
    popUp.style.top = h +"px";  
	popUp.style.left = (document.body.clientWidth/ 2 -width/2) +"px";
	popUp.style.width = width + "px";
	popUp.style.height = height + "px";
	popUp.style.visibility = "visible";
    document.body.appendChild(popUp);
    var tempHeight=200;
    if(htmlHeight!=undefined)tempHeight=htmlHeight;
    $("#popupcontent").html("<div class='modal-dialog'>"+
    "<div class='modal-content'>"+
      "<div class='modal-header'><div class='modal-title-bg'><h4 class='modal-title' id='myModalLabel'>"+name+"</h4><h4 class='modal-title' id='myModalLabel2' style='display:none;'>"+name2+"</h4></div><img src='"+webRoot+"/static/images/bg_popupModel.png'  class='modal-header-bg'/></div>"+
      "<div class='modal-body' id='details' style='height: "+tempHeight+"px;'>"+html+"</div>"+
      "<div class='modal-body' id='details2' style='border:0;height: "+(tempHeight+40)+"px;display:none;'></div>"+
      "<div class='modal-footer'><button type='button' class='btn btn-primary' id='back' onclick='showdetails();'>"+name2+"</button><button type='button' class='btn btn-primary' id='back2' onclick='showdetails();'  style='display:none;'>"+name+"</button><button type='button' class='btn btn-default' onclick='killPopup();' >关闭</button></div>"+
      "</div></div>");
    mygrid2 = new dhtmlXGridObject('details2');
	mygrid2.setImagePath(webRoot+"static/css/dhtmlXGrid/imgs/");
	mygrid2.setHeaderNew(Header);
	mygrid2.setInitWidths(Widths);
	mygrid2.setColAlign(Align);
	mygrid2.setColTypes(Types);
	mygrid2.init();
	mygrid2.setSkin("dhx_skyblue");// light
	mygrid2.parse(gridData, "", "jsarray");
	setPopGridHeadColor("#465261");
    setPopGridColumnColor("#fff", "#d1d4d7");
	d1show=true;
	$('#details2 .ev_dhx_skyblue').each(function(){    
    	$(this).bind("mousemove",function(){
    		//alert($(this).index());
    		try 
    		 { 
    			onDetails2Mousemove($(this).index());
    		 } 
    		 catch (e) 
    		 { 
    		 } 
    	});
    });
	$('#details2 .odd_dhx_skyblue').each(function(){    
    	$(this).bind("mousemove",function(){ 
    		//alert($(this).index());
    		try 
    		 { 
    			onDetails2Mousemove($(this).index());
    		 } 
    		 catch (e) 
    		 { 
    		 } 
    	});
    });
	$('#details2').bind("mouseover",function(){ hideTag();});
}

var d1show=true;
//切换内容
function showdetails(){
	if(d1show){
		$("#details2").show();
		$("#details").hide();
		$("#myModalLabel2").show();
		$("#myModalLabel").hide();
		$("#back").hide();
		$("#back2").show();
		d1show=false;
	}else{
		$("#details").show();
		$("#details2").hide();
		$("#myModalLabel").show();
		$("#myModalLabel2").hide();
		$("#back2").hide();
		$("#back").show();
		d1show=true;
	}
}


/**
 * 弹出窗体(用于增加或修改)
 * @param name 标题
 * @param html 内容
 * @param width 宽度
 * @param height 高度
 * @param btnname 按钮名称
 * @param callback 回调函数
 */
function showPopup3(name,html,width,height,btnname,callback) {  
	var msgw, msgh, bordercolor;
	Popup_showtimes++;
    if (Popup_showtimes>1){
    	return;
    }
    bordercolor = "#336699"; //提示窗口的边框颜色
    titlecolor = "#99CCFF"; //提示窗口的标题颜色
    
    var sWidth, sHeight;
    sWidth = getViewportWidth();
    sHeight = getViewportHeight();
    
    var bgObj = document.createElement("div");
    bgObj.setAttribute('id', 'bgDiv');
    bgObj.style.position = "absolute";
    bgObj.style.top = "0";
    bgObj.style.background = "#808080";
    bgObj.style.filter = "progid:DXImageTransform.Microsoft.Alpha(style=3,opacity=25,finishOpacity=75";
    bgObj.style.opacity = "0.6";
    bgObj.style.left = "0";
    bgObj.style.width = sWidth + "px";
    bgObj.style.height = sHeight + "px";
    bgObj.style.zIndex = 5000;
    document.body.appendChild(bgObj);
    var popUp = document.createElement("div");
    popUp.setAttribute("id", "popupcontent");
    popUp.setAttribute("class", "popupcontent");
    popUp.style.top = "200px";  
	popUp.style.left = (document.body.clientWidth/ 2 -width/2) +"px";
	popUp.style.width = width + "px";
	popUp.style.height = (height=="auto")?"auto":height+"px";
	popUp.style.visibility = "visible";
    popUp.style.zIndex = 9999;
    document.body.appendChild(popUp);
    var html = "<div class='modal-dialog'>"+
        "<div class='modal-content'>"+
        "<div class='modal-header'><div class='modal-title-bg'><h4 class='modal-title' id='myModalLabel'>"+name+"</h4></div><img src='"+webRoot+"/static/images/bg_popupModel.png'  class='modal-header-bg'/></div>"+
        "<div class='modal-body' id='details' style='width: 200px'>"+html+"</div>"+
        "<div class='modal-footer'><button type='button' class='btn btn-primary' id='back'>"+btnname+"</button><button type='button' class='btn btn-default' onclick='killPopup();' >关闭</button></div>"+
        "</div></div>";
    $("#popupcontent").html(html);
    $("#back").click(callback);
} 
/**
 * 弹出窗体(用于自定义内容高度)
 * @param name 标题
 * @param html 内容
 * @param width 宽度
 * @param height 高度
 * @param btnname 按钮名称
 * @param callback 回调函数
 * @param detailsHeight 内容高度
 * @param detailsWidth 内容宽度
 * @param closePopup 关闭窗口函数，不传默认关闭窗口
 */
function showPopup4(name,html,width,height,btnname,callback,detailsHeight,detailsWidth,closePopup) {
	var msgw, msgh, bordercolor;
	Popup_showtimes++;
    if (Popup_showtimes>1){
    	return;
    }
    bordercolor = "#336699"; //提示窗口的边框颜色
    titlecolor = "#99CCFF"; //提示窗口的标题颜色

    var sWidth, sHeight;
    sWidth = getViewportWidth();
    sHeight = getViewportHeight();

    var bgObj = document.createElement("div");
    bgObj.setAttribute('id', 'bgDiv');
    bgObj.style.position = "absolute";
    bgObj.style.top = "0";
    bgObj.style.background = "#808080";
    bgObj.style.filter = "progid:DXImageTransform.Microsoft.Alpha(style=3,opacity=25,finishOpacity=75";
    bgObj.style.opacity = "0.6";
    bgObj.style.left = "0";
    bgObj.style.width = sWidth + "px";
    bgObj.style.height = sHeight + "px";
    bgObj.style.zIndex = max_z_index+=1;
    document.body.appendChild(bgObj);
    var popUp = document.createElement("div");
    popUp.setAttribute("id", "popupcontent");
    popUp.setAttribute("class", "popupcontent");
    popUp.style.top = (document.body.clientHeight/ 2 -height/2)+"px";
	popUp.style.left = (document.body.clientWidth/ 2 -width/2) +"px";
	popUp.style.width = width + "px";
	popUp.style.height = (height=="auto")?"auto":height+"px";
	popUp.style.visibility = "visible";
    popUp.style.zIndex = max_z_index+=1;
    document.body.appendChild(popUp);
    var h=250;
    if(detailsHeight!=undefined)h=detailsHeight;
    var dialogStyle="";
    if(detailsWidth!=undefined)dialogStyle="style='width:"+detailsWidth+"px;'"
    var html = "<div class='modal-dialog' "+dialogStyle+">"+
        "<div class='modal-content'>"+
        "<div class='modal-header'><div class='modal-title-bg'><h4 class='modal-title' id='myModalLabel'>"+name+"</h4></div><img src='"+webRoot+"/static/images/bg_popupModel.png'  class='modal-header-bg'/></div>"+
        "<div class='modal-body' id='details' style='height:"+h+"px '>"+html+"</div>"+
        "<div class='modal-footer'><button type='button' class='btn btn-primary' id='back'>"+btnname+"</button><button type='button' class='btn btn-default' id='closePopup' >关闭</button></div>"+
        "</div></div>";
    $("#popupcontent").html(html);
    $("#back").click(callback);
    if(!closePopup){
        $("#closePopup").click(killPopup);
    }else{
        $("#closePopup").click(closePopup);
    }
}

/**
 * 弹出窗体(只有表格)
 * @param name 标题名称
 * @param btnname 按钮名称
 * @param callback 按钮回调函数
 * @param width 宽度
 * @param height 高度
 * @param Header 表格的表头
 * @param Widths 表格的列宽度
 * @param Align 表格的列排序
 * @param Types 表格的列类型
 * @param gridData 表格的数据
 * @param htmlHeight 内容的高度
 */
function showPopup5(name,btnname,callback,width,height,Header,Widths,Align,Types,gridData,htmlHeight) {  
	var msgw, msgh, bordercolor;
	Popup_showtimes++;
    if (Popup_showtimes>1){
    	return;
    }
    bordercolor = "#336699"; //提示窗口的边框颜色
    titlecolor = "#99CCFF"; //提示窗口的标题颜色
    
    var sWidth, sHeight;
    sWidth = getViewportWidth();
    sHeight = getViewportHeight();
    
    var bgObj = document.createElement("div");
    bgObj.setAttribute('id', 'bgDiv');
    bgObj.style.position = "absolute";
    bgObj.style.top = "0";
    bgObj.style.background = "#777";
    bgObj.style.filter = "progid:DXImageTransform.Microsoft.Alpha(style=3,opacity=25,finishOpacity=75";
    bgObj.style.opacity = "0.6";
    bgObj.style.left = "0";
    bgObj.style.width = sWidth + "px";
    bgObj.style.height = sHeight + "px";
    document.body.appendChild(bgObj);
    var popUp = document.createElement("div");
    popUp.setAttribute("id", "popupcontent");
    popUp.setAttribute("class", "popupcontent");
    var h=200;
    if(htmlHeight!=undefined)h=(document.body.clientHeight/ 2 -height/2);
    popUp.style.top = h +"px";  
	popUp.style.left = (document.body.clientWidth/ 2 -width/2) +"px";
	popUp.style.width = width + "px";
	popUp.style.height = height + "px";
	popUp.style.visibility = "visible";
    document.body.appendChild(popUp);
    var tempHeight=200;
    if(htmlHeight!=undefined)tempHeight=htmlHeight;
    $("#popupcontent").html("<div class='modal-dialog'>"+
    "<div class='modal-content'>"+
      "<div class='modal-header'><div class='modal-title-bg'><h4 class='modal-title' id='myModalLabel'>"+name+"</h4></div><img src='"+webRoot+"/static/images/bg_popupModel.png'  class='modal-header-bg'/></div>"+
      "<div class='modal-body' id='details2' style='border:0;height: "+(tempHeight+40)+"px;'></div>"+
      "<div class='modal-footer'><button type='button' class='btn btn-primary' id='back' onclick='showdetails();'>"+btnname+"</button><button type='button' class='btn btn-default' onclick='killPopup();' >关闭</button></div>"+
      "</div></div>");
    mygrid2 = new dhtmlXGridObject('details2');
	mygrid2.setImagePath(webRoot+"/static/css/dhtmlXGrid/imgs/");
	mygrid2.setHeaderNew(Header);
	mygrid2.setInitWidths(Widths);
	mygrid2.setColAlign(Align);
	mygrid2.setColTypes(Types);
	mygrid2.init();
	mygrid2.setSkin("dhx_skyblue");// light
	mygrid2.parse(gridData, "", "jsarray");
	setPopGridHeadColor("#465261");
    setPopGridColumnColor("#fff", "#d1d4d7");
	 $("#back").click(callback);
}

/**
 * 弹出透明层
 * @param index 索引
 * @param zindex 
 * @param opacity 不透明度
 */
var layerArray=new Array();
function showLayer(index,zindex,opacity) {
	if(!(layerArray[index]==undefined||layerArray[index]==0))return;
	var msgw, msgh, bordercolor;
    bordercolor = "#336699"; //提示窗口的边框颜色
    titlecolor = "#99CCFF"; //提示窗口的标题颜色

    var sWidth, sHeight;
    sWidth = getViewportWidth();
    sHeight = getViewportHeight();

    var bgObj = document.createElement("div");
    bgObj.setAttribute('id', 'bgLayer'+index);
    bgObj.style.position = "absolute";
    bgObj.style.top = "0";
    bgObj.style.background = "#808080";
    bgObj.style.filter = "progid:DXImageTransform.Microsoft.Alpha(style=3,opacity="+opacity+",finishOpacity="+opacity;
    bgObj.style.opacity =opacity==0?"0":"0.6";
    bgObj.style.left = "0";
    bgObj.style.width = sWidth + "px";
    bgObj.style.height = sHeight + "px";
    bgObj.style.zIndex = zindex;
    document.body.appendChild(bgObj);
    layerArray[index]="1";
}


/**
 * 关闭透明层
 * @param index 索引
 */
function killLayer(index){  
	document.body.removeChild(document.getElementById('bgLayer'+index));
	layerArray[index]="0";
}  

  /*
   * 关闭窗体
   */
function killPopup(){  
	Popup_showtimes--;
	if (Popup_showtimes==0){
		document.body.removeChild(document.getElementById("bgDiv"));
	    document.body.removeChild(document.getElementById("popupcontent"));
	}
    max_z_index = 110;
}  

var isCreateTag=false;
function showTag(divId,strhtml) {
	if(isCreateTag){
		$("#popTag").show();
		$("#popTag").html(strhtml);
	}else{
		isCreateTag=true;
		var popUp = document.createElement("div");
	    popUp.setAttribute("id", "popTag");
	    popUp.setAttribute("class", "borderSquare");
	    popUp.style.position = "absolute";
	    popUp.style.top = "200px";
		popUp.style.left = (document.body.clientWidth/ 2 +$(divId).outerWidth(true)/2)-50 +"px";
	    popUp.style.zIndex = 9999;
	    popUp.style.background='#3f6ca7';
	    popUp.style.color='#fff';
	    popUp.style.visibility = "visible";
	    popUp.style.width =  "300px";
	    popUp.style.height = "300px";
	    document.body.appendChild(popUp);
	    $("#popTag").html(strhtml);
	}
}

function showTag2(strhtml,top,left,width,height,background,fontcolor,isVCenter) {
	if(isCreateTag){
		$("#popTag").show();
		$("#popTag").html(strhtml);
		$("#popTag").css("top",top);
		$("#popTag").css("left",left);
		$("#popTag").css("width",width);
		$("#popTag").css("height",height);
		if(isVCenter){
			$("#popTag").css("line-height",height+"px");
        }else{
        	$("#popTag").css("line-height","normal");
        }
	}else{
		isCreateTag=true;
		var popUp = document.createElement("div");
	    popUp.setAttribute("id", "popTag");
	    popUp.setAttribute("class", "borderSquare");
	    popUp.style.position = "absolute";
	    popUp.style.top = top+"px";
		popUp.style.left = left +"px";
	    popUp.style.zIndex = 50;
	    popUp.style.background=background;
	    popUp.style.color=fontcolor;
	    popUp.style.visibility = "visible";
	    popUp.style.width =  width+"px";
	    popUp.style.height = height+"px";
        if(isVCenter){
            popUp.style.lineHeight = height+"px";
        }else{
            popUp.style.lineHeight = "normal";
        }
	    document.body.appendChild(popUp);
	    $("#popTag").html(strhtml);
	}
}

function hideTag() {
	if(isCreateTag){
		$("#popTag").hide();
	}
}

  
function getViewportHeight() {
	if (window.innerHeight!=window.undefined) return window.innerHeight;
	if (document.compatMode=='CSS1Compat') return document.documentElement.clientHeight;
	if (document.body) return document.body.clientHeight; 

	return window.undefined; 
}
function getViewportWidth() {
	var offset = 17;
	var width = null;
	if (window.innerWidth!=window.undefined) return window.innerWidth; 
	if (document.compatMode=='CSS1Compat') return document.documentElement.clientWidth; 
	if (document.body) return document.body.clientWidth; 
}

/***
 * 表格设置颜色
 * @param color1 颜色1
 * @param color2 颜色2
 */
function setPopGridColumnColor(color1, color2) {
    $(".ev_dhx_skyblue").each(function () {
        for (var i = 0; i < mygrid2.getColumnsNum(); i++) {
            $(this).find("td:eq(" + i + ")").css("background-color", color1);
        }
    });
    $(".odd_dhx_skyblue").each(function () {
        for (var i = 0; i < mygrid2.getColumnsNum(); i++) {
            $(this).find("td:eq(" + i + ")").css("background-color", color2);
        }
    });
}

/***
 * 表头设置颜色
 * @param color 颜色
 */
function setPopGridHeadColor(color) {
    for (var i = 0; i < mygrid2.getColumnsNum(); i++) {
        $("#details2 td:eq(" + i + ")").css("background", color);
        $("#details2 td:eq(" + i + ")").css("color", "#fff");
    }
}