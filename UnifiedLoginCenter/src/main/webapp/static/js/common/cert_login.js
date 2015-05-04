$(document).ready(function () {
    $(document).on('keydown', function (event) {
        if (event.which == 13) {
            cert_loginclick();
        }
    });
});

//onmouseover
function loginover() {
    document.getElementById("landbutton").src = webRoot+"/static/images/btn_login_move.png";
}
//onmouseout
function loginout() {
    element = document.getElementById("landbutton");
    if (element.src.match("btn_login_click")) {
        return;
    }
    element.src = webRoot+"/static/images/btn_login.png";
}

function logindown() {
    element = document.getElementById("landbutton");
    element.src = webRoot+'/static/images/btn_login_click.png';
}

function cert_loginclick() {
    var action = webRoot + "/login/loginAction.action";
    var cryptPwd = hex_md5(userNo+$("#password").val());
    $.ajax({
        type: "POST",
        url: action,
        data: {
            'password': cryptPwd
        },
        dataType: "json",
        success: function (data) {
            if (data.success) {//登陆成功
                window.location = webRoot + data.url;
            }
            else {//登陆失败
                if (!data.url) {//密码错误
                    alertNew("提示", "密码错误，请输入正确的用户密码。", closeMsg);
                } else {//证书不存在
                    window.location = webRoot + data.url;
                }
            }
        },
        error: function (xhr) {
            alertNew("提示", "网络连接出错，请稍后再试。！", closeMsg);
        }
    });
}

