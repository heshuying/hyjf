$(function(){
	var $wWidth = $(window).width();
	var liWidth = $(".rule_nav_cell").width();
	var time = 200;
    $(".rule_content").width($wWidth*3);
    $(".rule_0").width($wWidth);
     $('.rule_nav_cell').click( function(){
           var index=$(this).index();
           var width = $(".rule_0").width();
           if(index===0){
           		$("body").removeClass("bg_white").addClass("bg_grey");
           }else{
           		$("body").removeClass("bg_grey").addClass("bg_white");
           }
          $( this).addClass('rule_nav_selected').siblings().removeClass('rule_nav_selected');
          //$( ".rule_0").eq(index).show().siblings().hide();
          /*new*/
          $( ".rule_0").each(function(){
          	$(".rule_content").animate({marginLeft:-index*width+'px'},time);
          })
          $(".rule_nav_cell span").animate({left:index*liWidth+'px'},time);
          
     });  
});
