$(document).ready(function() {
	$("#username").focus();
	$('#username').bind('keydown', function (e) {
        var key = e.which;
        if (key == 13) {
            $("#password").focus();
        }
    });
	$('#password').bind('keydown', function (e) {
        var key = e.which;
        if (key == 13) {
        	loginclick();
        }
    });
	 if(document.all&&navigator.userAgent.indexOf("MSIE 8.0")>0){
	    	$("#username").css("width","210px"); 
	    	$("#password").css("width","210px"); 
	 }
	 var COOKIE_NAME = 'username'; 
    if( $.cookie(COOKIE_NAME)!=undefined){ 
        $("#username").val(  $.cookie(COOKIE_NAME) );  
    }  
    $("#check").click(function(){  
        if(this.checked){  
            $.cookie(COOKIE_NAME, $("#username").val() , { path: '/', expires: 10 });  
        }else{  
            $.cookie(COOKIE_NAME, null, { path: '/' });  //删除cookie  
        }  
    });  
});
	function getepassson(){
		var sno;
		try{
			ePass.OpenDevice(1, "");
	    }catch(e){
	    	alertNew("提示","请插入U-KEY再进行操作！",function(){closeMsg();$("#username").focus(); });
	    	return false;
	    }
	    try{
	    	sno = ePass.GetStrProperty(7, 0, 0);
	    	$("#epasssno").val(sno);
	    	ePass.CloseDevice();
	    }catch(e){
	    	$("#epasssno").val('');
	    	alertNew("提示","插入的U-key无效或损坏！",function(){closeMsg();$("#username").focus(); });
	    	return false;
	    }
	    
	    return true;
	}
	
	function Authentication(spin,slcode){
		var sreturn;
		sreturn="";
		try{
			ePass.OpenDevice(1, "");
	    }catch(e){
	    	alertNew("提示","请插入U-KEY再进行操作！",function(){closeMsg();});
	    	return "";
	    }
	    try{
	    	ePass.VerifyPIN(0, spin);
	    	//ePass.ChangeDir(0x300, 0, "INSURANCE");
	    	ePass.OpenFile(0, 1);
	    	sreturn=ePass.HashToken(1, 2,slcode);
	    	ePass.CloseDevice();
	    }catch(e){
	    	//ePass.CloseDevice();
	    	alertNew("提示","插入的U-key无效或损坏！",function(){closeMsg();});
	    	return "";
	    }
	    return sreturn;
	}
	
	//onclick
	function loginclick(){
		var agcode;
		if (getepassson() == false) return;
	
		$.post("EpassLoginAction.action",{strWorkNo:$("#username").val(),userpw:$("#password").val(),epasssno:$("#epasssno").val()},function(data,status){
			if(data.strErrorNo=="0"){
				//进行双认证
				agcode =Authentication(data.spin,data.slcode);
				var golocation=data.strErrorMsg;
				$.post("EpassAuthenticAtion.action","sauthenticno=" + agcode ,function(data,status){
					if(data.strErrorNo=="0"){
						window.location=golocation;
					}else{
						alertNew("提示",data.strErrorMsg,function(){closeMsg();$("#username").focus(); });
					}
				},"json");
			}else{
				alertNew("错误",data.strErrorMsg,function(){closeMsg();$("#username").focus(); });
			}
		 },"json");
	}
	
	//onmouseover
	function loginover(){
		document.getElementById("landbutton").src="static/images/btn_login_move.png";
	}
	//onmouseout
	function loginout(){
		element = document.getElementById("landbutton");
		if(element.src.match("btn_login_click")){
			return;
		}
		element.src="static/images/btn_login.png";
	}