<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
		<link rel="stylesheet" type="text/css" href="${ctx}/css/page.css"/>
		<title>新手引导 </title>
	</head>
	<body class="bg_grey">
			<input type="hidden" name="jumpcommend" id="jumpcommend" value="${jumpcommend}" />
			<!-- <div class="activeOutdatePop">您已注册</div> -->
			<img src="${ctx}/img/newcomer-bg.png" class="newcomer-bg" />
			<a href="#" class="fast-register-a">
				<img src="${ctx}/img/fast-register.png" class="fast-register-img"/>
			</a>
			<a href="#" class="fast-invest-a hy-jumpXSH">
				<img src="${ctx}/img/fast-invest.png" class="fast-invest-img"/>
			</a>
			<div style="padding:0 15px;font-size:10px;text-align:center;">
				<p>© 汇盈金服 All rights reserved  惠众商务顾问（北京）有限公司 </p>
				<p>京ICP备13050958号   公安安全备案证：37021313127</p>
			</div>
			<script src="${ctx}/js/zepto.min.js" type="text/javascript" charset="utf-8"></script>
			<script src="${ctx}/js/common.js" type="text/javascript" charset="utf-8"></script>
			<script src="${ctx}/js/hyjf.js" type="text/javascript" charset="utf-8"></script>
			<script type="text/javascript">
				document.documentElement.style.fontSize = $(document.documentElement).width() / 12.42 + 'px';
				$(window).on('resize', function() {
					document.documentElement.style.fontSize = $(document.documentElement).width() / 12.42 + 'px';
				});
			</script>
			<script>
				var pop = document.querySelector(".activeOutdatePop");
				var img = $(".newcomer-btn-click");
				img.click(function(){
					pop.style.display = "block";
					setTimeout('imgNone()',1500)
				})
				function imgNone(){
					pop.style.display = "none"
				}
			</script>
			<script>
			function getQueryString(name) {
			    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
			    var r = window.location.search.substr(1).match(reg);
			    if (r != null) {
			        return unescape(r[2]);
			    }
			    return null;
			}
			$(function(){
				var token = getQueryString("token");
				if(token){
					$('.fast-register-a').attr('href','"'+hyjfArr.hyjf+'://jumpRegister/?"');
				}else{
					$('.fast-register-a').attr('href','javascript:;');
				}
			})
			</script>
			
	</body>
</html>