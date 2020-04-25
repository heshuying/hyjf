$(function(){
/*------------------------------------------活动一开始----------------------------------------------*/
//	两个活动tab切换活动
	$('.newYear-tab ul li').click(function(){
		var idx= $(this).index();
		$(this).addClass('tabActive').siblings().removeClass('tabActive');
		$('.newYear-cont>div:eq('+idx+')').addClass('contActive').siblings().removeClass('contActive');
	});


//	手机输入框输入时文字颜色变化
    $('.invite-frid').focus(function(){
    	$(this).css({"color":"#d5564a","border-color":"#d5564a"});
    	$(this).attr('placeholder','');
    });
    $('.invite-frid').blur(function(){
    	$(this).css({"color":"#4c4c4c","border-color":"#b7b7b7"});
    	$(this).attr('placeholder','');
    });
//	关闭弹层
    $('.close-btn').click(function(){
     	$(this).parents('.tips-close').hide();
    });
    $('.newYear-tips-7').click(function(){
     	$(this).hide();
    });

    
});
/*------------------------------------------活动一结束----------------------------------------------*/