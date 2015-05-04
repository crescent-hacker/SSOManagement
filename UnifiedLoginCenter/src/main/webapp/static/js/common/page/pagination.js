//----------------------分页函数--------------------------------------
//跳转到第几页
function gotoPage(index){
	
	index = parseInt((""+index).replace(/\D/g,''));
	
    var totalPages = $("input[name='totalPages']").val();
    if(index>totalPages){
        $("input[name='pageIndex']").val(totalPages);
    }else if(index > 0 ){
    	$("input[name='pageIndex']").val(index);
    }else{
    	$("input[name='pageIndex']").val(1);
    }
    goPageFunction();
}
//下一页
function prevPage(){
    var currentPage = $("input[name='pageIndex']").val();
    var prevP = parseInt(currentPage) - 1;
    if(prevP<1){
        return null;
    }
    gotoPage(prevP);
}
//上一页
function nextPage(){
    var currentPage = $("input[name='pageIndex']").val();
    var nextP = parseInt(currentPage) + 1;
    if(nextP>$("input[name='totalPages']").val()){
        return null;
    }
    gotoPage(nextP);
}

