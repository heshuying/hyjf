// JavaScript Document
function b(){	
	t = parseInt(x.css('top'));
	y.css('top','56px');	
	x.animate({top: t - 56 + 'px'},'slow');	//56为每个li的高度
	if(Math.abs(t) == h-56){ //56为每个li的高度
		y.animate({top:'0px'},'slow');
		z=x;
		x=y;
		y=z;
	}
	setTimeout(b,3000);//滚动间隔时间 现在是3秒
}
$(document).ready(function(){
	$('.swap_xsh').html($('.news_li_xsh').html());
	x = $('.news_li_xsh');
	y = $('.swap_xsh');
	h = $('.news_li_xsh li').length * 56; //56为每个li的高度
	setTimeout(b,3000);//滚动间隔时间 现在是3秒
	
})
