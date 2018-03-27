$(document).ready(function(){
        $(".news ul li a img").hide();
	$(".news ul li").mouseover(function(){
		$(".news ul li a img").hide();
		$(this).find("img").show();
		});
$(".news ul li").mouseout(function(){
		$(".news ul li a img").hide();
		
		});

});