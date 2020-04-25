<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
		<link rel="stylesheet" type="text/css" href="${ctx}/css/page.css"/>
		<title>新手攻略</title>
		<style type="text/css">
			* {
				margin: 0 auto;
				padding: 0 auto
			}
			
			.exclusive img {
				display: block;
				width: 100%
			}
			
			.new-exclusive {
				width: 86.876%;
				margin: 0 auto;
				padding-bottom: 1.12rem;
			}
			
			.new-exclusive>p {
				position: relative;
			}
			
			.new-exclusive>p a {
				display: block;
				width: 3.48rem;
				height: 0.98rem;
				text-align: center;
				line-height: 0.98rem;
				font-size: 0.36rem;
				color: #7d00bc;
				background: #fee21c;
				border-radius: 5px;
				-webkit-border-radius: 5px;
				-moz-border-radius: 5px;
				text-decoration: none;
				position: absolute;
				font-family: "微软雅黑";
			}
			
			.colorpurple {
				background-color: #461182;
			}
			
			.new-exclusive2 {
				margin-top: 0.72rem;
			}
			
			.new-exclusive1 a {
				top: 2.06rem;
				left: 0.79rem
			}
			
			.new-exclusive2 a {
				top: 2.06rem;
				left: 6.49rem
			}
		</style>
	</head>

	<body class="colorpurple">
		<div class="activeOutdatePop"></div>
		<div class="exclusive">
			<img src="${ctx}/images/tzjoptimize/newExclusive01.jpg" alt="" />
			<img src="${ctx}/images/tzjoptimize/newExclusive02.jpg" alt="" />
			<div class="new-exclusive">
				<p class="new-exclusive1">
					<img src="${ctx}/images/tzjoptimize/newExclusive03.jpg" alt="" />
					<c:if test="${!empty regUrl }">
					  <a href="${regUrl }" class="hy-jumpRegister">立即注册领取</a>
					</c:if>
					<c:if test="${empty regUrl }">
						<a href="javascript:void(0)" class="logined">立即注册领取</a>
					</c:if>
				</p>
				<p class="new-exclusive2">
					<img src="${ctx}/images/tzjoptimize/newExclusive04.jpg?version=1" alt="" />
					<a href="${tenderUrl }">立即投资</a>
				</p>
			</div>

			<img src="${ctx}/images/tzjoptimize/newExclusive05.jpg" alt="" />
			<img src="${ctx}/images/tzjoptimize/newExclusive06.jpg" alt="" />
			<img src="${ctx}/images/tzjoptimize/newExclusive07.jpg" alt="" />
			<img src="${ctx}/images/tzjoptimize/newExclusive08.jpg" alt="" />
			<img src="${ctx}/images/tzjoptimize/newExclusive09.jpg" alt="" />
			<img src="${ctx}/images/tzjoptimize/newExclusive10.jpg" alt="" />
			<img src="${ctx}/images/tzjoptimize/newExclusive11.jpg" alt="" />
		</div>
	</body>
	<script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
	<script src="${ctx}/js/common.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctx}/js/hyjf.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript">
		document.documentElement.style.fontSize = $(document.documentElement).width() / 12.42 + 'px';
		$(window).on('resize', function() {
			document.documentElement.style.fontSize = $(document.documentElement).width() / 12.42 + 'px';
		});
		$(".logined").on("click",function(){
			var activeOutdatePop = $(".activeOutdatePop")
			activeOutdatePop.text("该活动仅针对新用户").show().delay(3000).fadeOut(); 
		})
		//判断是否登录
		var token = GetQueryString("token");
		if(token){
			$(".new-exclusive1 a").prop("href","javascript:void(0)")
		}
	</script>

</html>