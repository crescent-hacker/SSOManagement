/**
 * 登出函数
 */
function logout() {
    alertNew("登出提示", "确定退出？(切换用户需关闭浏览器)", function () {
        //清除session
        var url = webRoot + "/login/logoutAction.action";

        $.ajax({
            url: url,
            async: false, // 注意此处需要同步
            type: "POST",
            dataType: "json",
            success: function (data, status) {
                if (data.success) {
                    clear_auth();
                    $('body').html('<div style="align:center;text-align:center;font-size: 25px;margin-top:10px">请重新选择证书</div>');
                    window.location = webRoot + "/cert_login.jsp";
                }
            }
        });
    });

}

function createXMLObject() {
    try {
        if (window.XMLHttpRequest) {
            xmlhttp = new XMLHttpRequest();
        }
        // code for IE5、IE6
        else if (window.ActiveXObject) {
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
    }
    catch (e) {
        xmlhttp = false;
    }
    return xmlhttp;
}

function clear_auth() {
    try {
        if (navigator.userAgent.indexOf("MSIE") > 0)         //IE浏览器实现注销功能
        {
            document.execCommand("ClearAuthenticationCache");
        }
        else if (isFirefox = navigator.userAgent.indexOf("Firefox") > 0)       //Firefox实现注销功能
        {
            var xmlhttp = createXMLObject();
            xmlhttp.open("GET", ".force_logout_offer_login_mozilla", true, "logout", "logout");
            xmlhttp.send("");
            xmlhttp.abort();
        }
        else       //Google Chrome等浏览器实现注销功能
        {
            var xmlHttp = false;
            if (window.XMLHttpRequest) {
                xmlHttp = new XMLHttpRequest();
            }
            xmlHttp.open("GET", "./", false, "logout", "logout");
            xmlHttp.send(null);
            xmlHttp.abort();
        }
    }
    catch (e) {
        alert("there was an error");
        return false;
    }
}