//窗口自适应
document.documentElement.style .fontSize = $(document.documentElement).width() /12.42 + 'px';
$(window).on( 'resize', function () {document.documentElement. style.fontSize = $(document.documentElement).width() /12.42 + 'px';});
//推荐星弹层
$(function(){
	$('.get-start').click(function(){
        $('.getStart').show();
    });
    $('.use-start').click(function(){
        $('.useStart').show();
    });
    $('.get-start-rule img').click(function(){
        $('.popup-window').hide()
    })
});
//获得/使用推荐星箭头控制
$(function(){
	var arrows = $('.arrows-btn img');
	var coupop = $('.record-coupop');
	$('.arrows-btn img').click(function(){
		var coupop = $(this).parents('.get-record').siblings('.record-coupop');
		var s = $(this);
		if(coupop.is(":hidden")){
			coupop.slideDown(200);
			$(this).addClass('arrows-back');
		}else{
			coupop.slideUp(200);
			$(this).removeClass('arrows-back');
			}
		});
});

