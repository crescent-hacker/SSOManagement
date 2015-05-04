var alert_showtimes = 0;
/**
 * 弹出提示窗体（含确认按钮）
 * @param title 标题
 * @param str 内容
 * @param callback1 回调函数
 */
function alertNew(title, str, callback1) {
    var msgw, msgh, bordercolor;

    alert_showtimes++;
    if (alert_showtimes > 1) {
        return;
    }
    msgw = 483; //提示窗口的宽度
    msgh = 213; //提示窗口的高度
    bordercolor = "#336699"; //提示窗口的边框颜色
    titlecolor = "#99CCFF"; //提示窗口的标题颜色

    var sWidth, sHeight;
    sWidth = getViewportWidth();
    sHeight = getViewportHeight();

    var bgObj = document.createElement("div");
    bgObj.setAttribute('id', 'msgbgDiv');
    bgObj.style.position = "absolute";
    bgObj.style.top = "0";
    bgObj.style.background = "#777";
    bgObj.style.filter = "progid:DXImageTransform.Microsoft.Alpha(style=3,opacity=25,finishOpacity=75";
    bgObj.style.opacity = "0.6";
    bgObj.style.left = "0";
    bgObj.style.width = sWidth + "px";
    bgObj.style.height = sHeight + "px";
    bgObj.style.zIndex = 10000;
    document.body.appendChild(bgObj);
    var msgObj = document.createElement("div");
    msgObj.setAttribute("id", "msgDiv");
    msgObj.setAttribute("align", "center");
    msgObj.style.position = "absolute";
    msgObj.style.background = "url(" + webRoot + "/static/images/bg_alert.png)";
    msgObj.style.font = "12px/1.6em Verdana, Geneva, Arial, Helvetica, sans-serif";
    //msgObj.style.border = "1px solid " + bordercolor;
    msgObj.style.width = msgw + "px";
    msgObj.style.height = msgh + "px";
    msgObj.style.top = (document.documentElement.scrollTop + (sHeight - msgh) / 2) + "px";
    msgObj.style.left = (sWidth - msgw) / 2 + "px";
    msgObj.style.zIndex = 10001;
    var close = document.createElement("img");
    close.setAttribute("id", "msgClose");
    close.src = "" + webRoot + "/static/images/btn_alertClose.png";
    close.style.cursor = "pointer";
    close.onclick = closeMsg;
    close.style.marginLeft = "420px";
    close.onerror = changePicAlert;
    document.body.appendChild(msgObj);
    document.getElementById("msgDiv").appendChild(close);
    var txt = document.createElement("p");
    txt.setAttribute("id", "msgTxt");
    txt.style.font = "20px 'microsoft yahei'";
    txt.style.marginTop = "55px";
    txt.style.marginLeft = "150px";
    txt.style.marginLeft = "130px";
    txt.style.textAlign = "left";
    txt.style.width = "350px";
    txt.style.color = "#0097aa";
    txt.innerHTML = str;
    document.getElementById("msgDiv").appendChild(txt);
    var button = document.createElement("input");
    button.setAttribute("id", "msgButton");
    button.style.marginLeft = "350px";
    button.style.marginTop = "60px";
    button.type = "button";
    button.style.width = "83px";
    button.style.height = "31px";
    button.style.background = "url(" + webRoot + "/static/images/btn_alertOK.png)";
    button.style.border = "0";
    button.style.cursor = "pointer";
    button.onclick = callback1;
    document.getElementById("msgDiv").appendChild(button);
    var tit = document.createElement("p");
    tit.setAttribute("id", "msgTitle");
    tit.style.font = "20px 'microsoft yahei'";
    tit.style.marginTop = "-200px";
    tit.style.marginLeft = "-100px";
    tit.style.color = "#fff";
    tit.style.textAlign = "left";
    tit.innerHTML = title;
    tit.style.width = "350px";
    document.getElementById("msgDiv").appendChild(tit);
    $("#msgButton").focus();
}

/**
 * 弹出询问窗体（含确认和取消按钮）
 * @param title 标题
 * @param str 内容
 * @param callback1 确认按钮回调函数
 * @param callback2 取消按钮回调函数
 */
function confirmNew(title, str, callback1, callback2) {
    var msgw, msgh, bordercolor;

    alert_showtimes++;
    if (alert_showtimes > 1) {
        return;
    }
    msgw = 483; //提示窗口的宽度
    msgh = 213; //提示窗口的高度
    bordercolor = "#336699"; //提示窗口的边框颜色
    titlecolor = "#99CCFF"; //提示窗口的标题颜色

    var sWidth, sHeight;
    sWidth = getViewportWidth();
    sHeight = getViewportHeight();

    var bgObj = document.createElement("div");
    bgObj.setAttribute('id', 'msgbgDiv');
    bgObj.style.position = "absolute";
    bgObj.style.top = "0";
    bgObj.style.background = "#777";
    bgObj.style.filter = "progid:DXImageTransform.Microsoft.Alpha(style=3,opacity=25,finishOpacity=75";
    bgObj.style.opacity = "0.6";
    bgObj.style.left = "0";
    bgObj.style.width = sWidth + "px";
    bgObj.style.height = sHeight + "px";
    bgObj.style.zIndex = 10000;
    document.body.appendChild(bgObj);
    var msgObj = document.createElement("div");
    msgObj.setAttribute("id", "msgDiv");
    msgObj.setAttribute("align", "center");
    msgObj.style.position = "absolute";
    msgObj.style.background = "url(" + webRoot + "/static/images/bg_alert.png)";
    msgObj.style.font = "12px/1.6em Verdana, Geneva, Arial, Helvetica, sans-serif";
    //msgObj.style.border = "1px solid " + bordercolor;
    msgObj.style.width = msgw + "px";
    msgObj.style.height = msgh + "px";
    msgObj.style.top = (document.documentElement.scrollTop + (sHeight - msgh) / 2) + "px";
    msgObj.style.left = (sWidth - msgw) / 2 + "px";
    msgObj.style.zIndex = 10001;
    var close = document.createElement("img");
    close.setAttribute("id", "msgClose");
    close.src = "" + webRoot + "/static/images/btn_alertClose.png";
    close.style.cursor = "pointer";
    close.onclick = closeMsg;
    close.style.marginLeft = "420px";
    close.onerror = changePicAlert;
    document.body.appendChild(msgObj);
    document.getElementById("msgDiv").appendChild(close);
    var txt = document.createElement("p");
    txt.setAttribute("id", "msgTxt");
    txt.style.font = "20px 'microsoft yahei'";
    txt.style.marginTop = "55px";
    txt.style.marginLeft = "150px";
    txt.style.marginLeft = "130px";
    txt.style.textAlign = "left";
    txt.style.width = "350px";
    txt.style.color = "#0097aa";
    txt.innerHTML = str;
    document.getElementById("msgDiv").appendChild(txt);
    var button = document.createElement("input");
    button.setAttribute("id", "msgButton");
    button.style.marginLeft = "270px";
    button.style.marginTop = "60px";
    button.type = "button";
    button.style.width = "83px";
    button.style.height = "31px";
    button.style.background = "url(" + webRoot + "/static/images/btn_alertOK.png)";
    button.style.border = "0";
    button.style.cursor = "pointer";
    button.onclick = callback1;
    document.getElementById("msgDiv").appendChild(button);
    var cancel = document.createElement("input");
    cancel.setAttribute("id", "msgCancel");
    cancel.style.marginLeft = "10px";
    //cancel.style.marginTop = "70px";
    cancel.type = "button";
    cancel.style.width = "83px";
    cancel.style.height = "31px";
    cancel.style.background = "url(" + webRoot + "/static/images/btn_alertCancel.png)";
    cancel.style.border = "0";
    cancel.style.cursor = "pointer";
    cancel.onclick = callback2;
    document.getElementById("msgDiv").appendChild(cancel);
    var tit = document.createElement("p");
    tit.setAttribute("id", "msgTitle");
    tit.style.font = "20px 'microsoft yahei'";
    tit.style.marginTop = "-200px";
    tit.style.marginLeft = "-100px";
    tit.style.color = "#fff";
    tit.style.textAlign = "left";
    tit.innerHTML = title;
    tit.style.width = "350px";
    document.getElementById("msgDiv").appendChild(tit);
    $("#msgButton").focus();
}

function changePicAlert() {
    document.getElementById("msgClose").src = "static/images/btn_alertClose.png";
    document.getElementById("msgButton").style.background = "url(static/images/btn_alertOK.png)";
    document.getElementById("msgDiv").style.background = "url(static/images/bg_alert.png)";
    document.getElementById("msgCancel").style.background = "url(static/images/btn_alertCancel.png)";
}

function closeMsg() {
    alert_showtimes--;
    if (alert_showtimes == 0) {
        document.body.removeChild(document.getElementById("msgbgDiv"));
        document.getElementById("msgDiv").removeChild(document.getElementById("msgClose"));
        document.getElementById("msgDiv").removeChild(document.getElementById("msgButton"));
        //document.getElementById("msgDiv").removeChild(document.getElementById("msgCancel"));
        document.body.removeChild(document.getElementById("msgDiv"));
    }
}

function getViewportHeight() {
    if (window.innerHeight != window.undefined) return window.innerHeight;
    if (document.compatMode == 'CSS1Compat') return document.documentElement.clientHeight;
    if (document.body) return document.body.clientHeight;

    return window.undefined;
}
function getViewportWidth() {
    var offset = 17;
    var width = null;
    if (window.innerWidth != window.undefined) return window.innerWidth;
    if (document.compatMode == 'CSS1Compat') return document.documentElement.clientWidth;
    if (document.body) return document.body.clientWidth;
}

/**
 * session过期时报错，服务器及json解释错误时报错
 */
function errorAlert(xhr) {
    if (xhr.status == 500) {
        alertNew("提示", "登陆超时，请重新登陆！", function () {
            window.location.href = webRoot + "/index.jsp";
        });
    } else {
        alertNew("提示", "网络连接出错，请稍后再试。", closeMsg);
    }
}