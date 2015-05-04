/*$(document).ready(function(){
 searchInit();
 });*/


/**
 * bind behavior to searchBox
 */
function searchInit(id) {
    var searchKeyBox = $("#"+id);
    var sugBox = $("#sugBox");
    //mouse behaviour:1.focus 2.blur
    searchKeyBox.focus(function () {
        searchSuggest(sugBox, searchKeyBox);
    });
    searchKeyBox.blur(function () {
        sugBox.hide();
    });
    //keyboard behaviour:1.keydown 2.keyup
    searchKeyBox.keydown(function (event) {
        keyboardAction(event, sugBox, searchKeyBox);
    });
//    searchKeyBox.change(function(){
//        searchSuggest(sugBox, searchKeyBox);
//    });
    searchKeyBox.on('input', function (event) {
        searchSuggest(sugBox, searchKeyBox);
    });
}

/**
 * action of keydown or keyup
 */
function keyboardAction(event, sugBox, searchKeyBox) {
    if (event.which == 27) {//esc,hide the sugBox
        sugBox.hide();
        return;
    }
    if (event.which == 40) {//down
        var curLi = sugBox.find(".bdsug-s");
        if (sugBox.css("display") == "none") {//if sugBox is hidden,show sugBox
            sugBox.show();
            return;
        }
        if (curLi.length == 0) {//if sugBox is visible and no li is focused,focus on li[0]
            var li = sugBox.find("li").eq(0);
            li.addClass("bdsug-s");
            return;
        }
        //in normal situation,focus on the next element
        curLi.removeClass("bdsug-s");
        curLi.next().addClass("bdsug-s");
        return;
    }
    if (event.which == 38) {//up
        var activeElm = sugBox.find(".bdsug-s");
        activeElm.removeClass("bdsug-s");
        activeElm.prev().addClass("bdsug-s");
        return;
    }
    if (event.which == 13) {//enter
        var activeElm = sugBox.find(".bdsug-s");
        var text = activeElm.html();
        var searchText = text.replace(/<b>/gi, "").replace(/<\/b>/gi, "");
        searchKeyBox.val(searchText);
        searchSuggest(sugBox, searchKeyBox);
        sugBox.hide();
        return;
    }
}

/**
 * do search
 */
function searchSuggest(sugBox, searchKeyBox) {
    //init data
    var searchValue = searchKeyBox.val();
    var content_html = "";

    try {
        var liArr = doSearchSug();//get data from db
    } catch (e) {
        alert('请重写doSearchSug()函数！');
        return;
    }
    //combine all the data to sugBox
    for (var i = 0; i < liArr.length; i++) {
        var value = ("" + liArr[i]).replace(searchValue, "</b>" + searchValue + "<b>");
        content_html += '<li class="bdsug-overflow"><b>' + value + '</b></li>';
    }
    if (liArr.length != 0) {
        var html = "<ul>" + content_html + "</ul>";
        //fill the search result to suggest box
        sugBox.html(html);
        initSugLi(sugBox, searchKeyBox);
        sugBox.show();
    } else {
        sugBox.hide();
        sugBox.html("");
    }
}

/**
 * init sugBox li
 */
function initSugLi(sugBox, searchKeyBox) {
    var li = sugBox.find("li");
    //init mouse event,including 1.mouseover 2.mouseleave 3.onclick
    li.mouseover(function () {
        $(this).addClass("bdsug-s");
    });

    li.mouseleave(function () {
        $(this).removeClass("bdsug-s");
    });

    li.mousedown(function () {
        var text = $(this).html();
        var searchText = text.replace(/<b>/gi, "").replace(/<\/b>/gi, "");
        searchKeyBox.val(searchText);
        sugBox.hide();
    });

}

/**
 * for user to implement
 * @return an array of search result
 */
/*function doSearchSug(){
 var testArray = [12,45,2342,65,567,678,567,8787,567567,787,342,"awrwer","adasd","asdavv","asdasd"];
 return testArray;
 }*/

/**
 * init search Box for tips and hotkey
 */
function initSearchBox(selector,tips,callback) {
    var search = $(selector);
    search.val(tips);
    search.focus(function () {
        if ($(this).val() == tips) {
            $(this).val("");
        }
        $(this).css("color", "black");
    });
    search.blur(function () {
        if ($(this).val() == "") {
            $(this).val(tips);
            $(this).css("color", "#787878");
        }
    });
    if(!(!callback)){
        search.keyup(function (e) {
            if (e.which == 13) {
                callback();
            }
        });
    }
}