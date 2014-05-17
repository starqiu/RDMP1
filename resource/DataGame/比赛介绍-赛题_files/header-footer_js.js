// ÏÂÀ­²Ëµ¥ 
 KISSY.use("node,sizzle", function (S, Node) {
	var $ = S.all;
   
	$(".h-sq").on("mouseenter", function(){
		$(this).addClass("cur");
		$(".h-menu li:eq(3)").addClass("bgno");
		$(".h-down").show();
		$(".h-downbg").show();		
	});

	$(".h-sq").on("mouseleave",function(){
		$(this).removeClass("cur");
		$(".h-menu li:eq(3)").removeClass("bgno");
		$(".h-down").hide();
		$(".h-downbg").hide();
	});
 });