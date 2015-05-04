
/**
 * Created by Chendf on 2014/9/17.
 */
$.ajaxSetup({
    dataFilter: function (data, type) {
        var code;
        var dataJson;
        try {//JSON解释错误转到错误页面
            dataJson = eval('(' + data + ')');
        } catch (e) {
//            window.location.href = webRoot + "/error.jsp"
//            alertNew("提示","服务器出错，请稍后再试",closeMsg)
            console.log("服务器出错");
        }
        if (!dataJson||!dataJson.code) {//正常返回
            return data;
        } else {//code存在，登陆超时或权限错误
            code = dataJson.code;
            if (code == '-1') {//无权限
                window.location.href = webRoot + "/noright.jsp"
            }
            if (code == '-2') {//未登陆
                window.location.href = webRoot + "/cert_login.jsp"
            }
            if (code == '-10') {//有别ip登陆了该账号
                window.location.href = webRoot + "/cert_login.jsp?c"
            }
        }
    }
})