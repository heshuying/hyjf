// JavaScript Document
function b(){	
	t = parseInt(x.css('top'));
	y.css('top','36px');	
	x.animate({top: t - 36 + 'px'},'slow');	//36为每个li的高度
	if(Math.abs(t) == h-36){ //36为每个li的高度
		y.animate({top:'0px'},'slow');
		z=x;
		x=y;
		y=z;
	}
	setTimeout(b,3000);//滚动间隔时间 现在是3秒
}
$(document).ready(function(){
	function aa(){
		$('.swap').html($('.news_li').html());
		x = $('.news_li');
		y = $('.swap');
		h = $('.news_li li').length * 36; //36为每个li的高度
		setTimeout(b,3000);//滚动间隔时间 现在是3秒
	}
	setTimeout(aa,800)
	var url = location.origin;
	$(".contact-us-abstract1 a").attr('href',hyjfArr.hyjf+'://jumpH5/?{"url":"'+url+'/hyjf-app/jsp/aboutus/aboutUs2.jsp?version='+versionCode+'"}')
	$(".contact-us-abstract2 a").attr('href',hyjfArr.hyjf+'://jumpH5/?{"url":"'+url+'/hyjf-app/jsp/aboutus/qualification_doc.jsp?version='+versionCode+'"}')
	$(".contact-us-abstract3 a").attr('href',hyjfArr.hyjf+'://jumpH5/?{"url":"'+url+'/hyjf-app/jsp/aboutus/platform_data.jsp?version='+versionCode+'"}')
	$(".contact-us-abstract4 a").attr('href',hyjfArr.hyjf+'://jumpH5/?{"url":"'+url+'/hyjf-app/jsp/aboutus/report_list.jsp?version='+versionCode+'"}')
	$(".contact-us-abstract5 a").attr('href',hyjfArr.hyjf+'://jumpH5/?{"url":"'+url+'/hyjf-app/jsp/aboutus/contact_us.jsp?version='+versionCode+'"}')
	$(".report-link-dec").attr('href',hyjfArr.hyjf+'://jumpH5/?{"url":"'+url+'/hyjf-app/jsp/aboutus/report_Dec.jsp"}')
	$(".report-link-nov").attr('href',hyjfArr.hyjf+'://jumpH5/?{"url":"'+url+'/hyjf-app/jsp/aboutus/report_Nov.jsp"}')
	$(".report-link-3rd").attr('href',hyjfArr.hyjf+'://jumpH5/?{"url":"'+url+'/hyjf-app/jsp/aboutus/report_3rd.jsp"}')
	$(".report-link-oct").attr('href',hyjfArr.hyjf+'://jumpH5/?{"url":"'+url+'/hyjf-app/jsp/aboutus/report_Oct.jsp"}')
	$(".report-link-july").attr('href',hyjfArr.hyjf+'://jumpH5/?{"url":"'+url+'/hyjf-app/jsp/aboutus/report_July.jsp"}')
	$(".report-link-aug").attr('href',hyjfArr.hyjf+'://jumpH5/?{"url":"'+url+'/hyjf-app/jsp/aboutus/report_Aug.jsp"}')
	$(".report-link-half").attr('href',hyjfArr.hyjf+'://jumpH5/?{"url":"'+url+'/hyjf-app/jsp/aboutus/report_Half.jsp"}')
	$(".report-link-may").attr('href',hyjfArr.hyjf+'://jumpH5/?{"url":"'+url+'/hyjf-app/jsp/aboutus/report_May.jsp"}')
	$(".report-link-201701").attr('href',hyjfArr.hyjf+'://jumpH5/?{"url":"'+url+'/hyjf-app/jsp/aboutus/report_201701.jsp"}')
	$(".report-link-201702").attr('href',hyjfArr.hyjf+'://jumpH5/?{"url":"'+url+'/hyjf-app/jsp/aboutus/report_201702.jsp"}')
	$(".report-link-2017-1th").attr('href',hyjfArr.hyjf+'://jumpH5/?{"url":"'+url+'/hyjf-app/jsp/aboutus/report_2017_1th.jsp"}')
	$(".report-link-201704").attr('href',hyjfArr.hyjf+'://jumpH5/?{"url":"'+url+'/hyjf-app/jsp/aboutus/report_201704.jsp"}')
	$(".report-link-201705").attr('href',hyjfArr.hyjf+'://jumpH5/?{"url":"'+url+'/hyjf-app/jsp/aboutus/report_201705.jsp"}')
})
