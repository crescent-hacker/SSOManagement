var isMouseLeave = true;
var selectNavigation = -1;
var isInitOHeigh = false;//是否初始化原始高度
var OHeighArray = new Array();//原始高度数组

$(document).ready(function () {
    $("#btnNavigation1").bind("mouseover", function () {
        isMouseLeave = false;
        changeNavigation(1);
    });
    $("#btnNavigation2").bind("mouseover", function () {
        isMouseLeave = false;
        changeNavigation(2);
    });
    $("#btnNavigation3").bind("mouseover", function () {
        isMouseLeave = false;
        changeNavigation(3);
    });
    $("#btnNavigation4").bind("mouseover", function () {
        isMouseLeave = false;
        changeNavigation(4);
    });
    $("#btnNavigation5").bind("mouseover", function () {
        isMouseLeave = false;
        changeNavigation(5);
    });
    $("#hNavigation").bind("mouseleave", function () {
        if (selectNavigation >= 0) {
            isMouseLeave = true;
            changeNavigation(selectNavigation);
        }
    });
    //设置导航
    setNavigation();
});

/**
 * 设置实际选中导航
 */
function setNavigation() {
    var id = $("body").attr("navId");
    selectNavigation = id;
    changeNavigation(id);
}

/**
 * 设置当前选中导航的样式
 * @param id
 */
function changeNavigation(id) {
    $(".btnNavigationSelect").attr("class", "btnNavigation");
    $("#btnNavigation" + id).attr("class", "btnNavigationSelect");
}

/**设置元素高度**/
function setElementsHeight(eArray) {
    var wHeight = getWinHeight() - 160;
    var mh = 0;//高度差
    var maxheight = 0;
    //选取最大高度
    for (var i = 0; i < eArray.length; i++) {
        if (!isInitOHeigh)OHeighArray[i] = $(eArray[i]).height();
        if (OHeighArray[i] > maxheight)maxheight = OHeighArray[i];
    }
    mh = wHeight - maxheight;
    for (var i = 0; i < eArray.length; i++) {
        var newHeight = OHeighArray[i] + mh;
        $(eArray[i]).height(newHeight);
    }
    isInitOHeigh = true;
}

function getWinHeight() {
    if (window.innerHeight != window.undefined) return window.innerHeight;
    if (document.compatMode == 'CSS1Compat') return document.documentElement.clientHeight;
    if (document.body) return document.body.clientHeight;

    return window.undefined;
}
