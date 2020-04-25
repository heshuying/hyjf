<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
		<meta charset="UTF-8">
		<title>资质文件 </title>
		<meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
		<link rel="stylesheet" type="text/css" href="${ctx}/css/page.css"/>
		<link rel="stylesheet" type="text/css" href="${ctx}/css/idangerous.swiper.new.css"/>
</head>
<body>
	<div class="swiper-container-new" id="qualifyList">
		<ul class="swiper-wrapper">
			<li class="swiper-slide"><img class="sliderImg" src="${ctx}/img/usaEng.jpg"><p class="img_name hyjf-color">美国ISO9001证书英文版</p></li>
			<li class="swiper-slide"><img class="sliderImg" src="${ctx}/img/usaCn.jpg"><p class="img_name hyjf-color">美国ISO9001证书中文版</p></li>
			<li class="swiper-slide"><img class="sliderImg" src="${ctx}/img/yingye.jpg"><p class="img_name hyjf-color">营业执照</p></li>
			<li class="swiper-slide"><img class="sliderImg" src="${ctx}/img/cnEng.jpg"><p class="img_name hyjf-color">中国ISO9001证书英文版</p></li>
			<li class="swiper-slide"><img class="sliderImg" src="${ctx}/img/cnCn.jpg"><p class="img_name hyjf-color">中国ISO9001证书中文版</p></li>
	     </ul>	
	      <p class="tac"></p>
		  <div class="clearBoth"></div>    
	</div>
	<div class="pagination-new"></div>
		<script src="${ctx}/js/jquery.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="${ctx}/js/idangerous.swiper.min.new.js" type="text/javascript" charset="utf-8"></script>
		<script>
		
			/* $.fillTmplByAjax("/hyjf-app/homepage/getCompanyQualify",null, "#qualifyList", "#qualifyList-data") */
		/* $.ajax({
			url:"/hyjf-app/homepage/getCompanyQualify",
			dataType:"json",
			success:function(data){
				var ul = "";
				for(var i=0;i<data.contentQualifyList.length;i++){
					var li = '<li class="swiper-slide"><img class="sliderImg" src="'+data.host+data.contentQualifyList[i].imgurl+'"><p class="img_name hyjf-color">'+data.contentQualifyList[i].name+'</p></li>';
					var ul = ul+li;
				}
				$(".swiper-wrapper").html(ul);
				var mySwiper = new Swiper('.swiper-container-new',{
		        	autoplay : 3000,//可选选项，自动滑动
		        	loop : true,//可选选项，开启循环
		        	speed:1000,
		        	pagination : '.pagination-new',
		        	paginationClickable :true
		        	})
			}
		}) */
		var mySwiper = new Swiper('.swiper-container-new',{
        	autoplay : 3000,//可选选项，自动滑动
        	loop : true,//可选选项，开启循环
        	speed:1000,
        	pagination : '.pagination-new',
        	paginationClickable :true
        	})
		</script>
</body>
</html>