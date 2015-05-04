var pmin=5;
$(document).ready(function() {
	setInterval('timeToBack()',1000);
});

function timeToBack(){
	if(pmin==0){
		window.location=$("#ahref").attr("href"); 
	}else{
		pmin-=1;
		$("#pnum").html(pmin);
	}
}