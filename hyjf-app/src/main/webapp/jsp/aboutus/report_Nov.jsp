<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>2016年11月运营报告 </title>
		<meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
		<link rel="stylesheet" type="text/css" href="${ctx}/css/animations.css">
		<link rel="stylesheet" type="text/css" href="${ctx}/css/report-index.css">
		<link rel="stylesheet" type="text/css" href="${ctx}/css/page.css"/>
	</head>
	<body class="bg_grey response report-page">
		<div>
			<img src="${ctx}/img/report-up.png" class="reprot-up">
			<div class="page page-1-1 page-current page-nov-1-1">
				<div class="wrap">
					<div class="report-may-1-main">
						<img src="${ctx}/img/report-nov-1-1.png?v=20171123" class="pt-page-moveCircle1"/>
					</div>
				</div>
			</div>
			<div class="page page-2-1 hide">
				<div class="wrap">
					<div class="report-page-may-2-topImg"></div>
					<div class="report-page-may-2-div">
						<p class="colorWhite leftToRight font12 report-page-may-p">累计交易额(元)</p>
						<img src="${ctx}/img/report-nov-2-1.png" class="report-page-may-img leftToRight"/>
						<p class="colorWhite leftToRight font12 report-page-may-p">平台注册人数(人)</p>
						<img src="${ctx}/img/report-nov-2-2.png" class="report-page-may-img leftToRight"/>
						<p class="colorWhite leftToRight font12 report-page-may-p">累计赚取收益(元)</p>
						<img src="${ctx}/img/report-nov-2-3.png" class="report-page-may-img leftToRight"/>
						<p class="colorWhite leftToRight font12 report-page-may-p">风险保证金(元)</p>
						<img src="${ctx}/img/report-nov-2-4.png" class="report-page-may-img leftToRight"/>
					</div>
				</div>
			</div>
			<div class="page page-3-1 hide">
				<div class="wrap">
					<div class="report-page-may-3-topImg"></div>
					<div class="report-page-may-3-div report-page-may-3">
						<img src="${ctx}/img/report-nov-3-1.png" class="report-page-may-img report-page-may-31"/>
						<img src="${ctx}/img/report-nov-3-3.png" class="report-page-may-img report-page-oct-33"/>
						<img src="${ctx}/img/report-nov-3-4.png" class="report-page-may-img report-page-may-34"/>
						<img src="${ctx}/img/report-nov-3-6.png" class="report-page-may-img report-page-oct-36"/>
					</div>
				</div>
			</div>
			<div class="page page-4-1 hide report-page-aug-4">
				<div class="wrap">
					<div class="report-page-aug-4-topImg"></div>
					<img src="${ctx}/img/report-nov-4-1.jpg" class="report-page-aug-4-1"/>
					<img src="${ctx}/img/report-nov-4-2.jpg" class="report-page-aug-4-2"/>
					<img src="${ctx}/img/report-nov-4-3.jpg" class="report-page-aug-4-3"/>
					<img src="${ctx}/img/report-nov-44.jpg" class="report-page-aug-4-4"/>
				</div>
			</div>
			<div class="page page-5-1 hide">
				<div class="wrap">
					<div class="report-page-may-7-topImg"></div>
					<div class="report-page-aug-5-div">
						<img src="${ctx}/img/report-nov-5-1.jpg" class="report-aug-5-1"/>
						<img src="${ctx}/img/report-nov-5-2.jpg" class="report-aug-5-2"/>
					</div>
				</div>
			</div>
			<div class="page page-6-1 hide report-page-aug-6">
				<div class="wrap">
					<div class="report-page-aug-6-topImg"></div>
					<img src="${ctx}/img/report-nov-6-1.jpg" class="report-oct-6-1"/>
					<img src="${ctx}/img/report-nov-6-2.jpg" class="report-oct-6-2"/>
					<img src="${ctx}/img/report-nov-6-3.jpg" class="report-oct-6-3"/>
					<img src="${ctx}/img/report-nov-6-4.jpg" class="report-oct-6-4"/>
					<img src="${ctx}/img/report-nov-6-5.jpg" class="report-oct-6-5"/>
				</div>
			</div>
			<div class="page page-7-1 hide">
				<div class="wrap">
					<div class="report-page-may-10-topImg"></div>
					<img src="${ctx}/img/report-nov-8-1.jpg" class="report-may-11-1 report-aug-8-1"/>
					<img src="${ctx}/img/report-nov-8-2.jpg" class=" report-oct-8-2" />
				</div>
			</div>
			<div class="page page-8-1 hide">
			     <div class="wrap">
			        <div class="report-page-may-11-topImg"></div>
					<img src="${ctx}/img/report-nov-9-1.jpg" class="report-may-11-1" style="width:100%"/>
					<img src="${ctx}/img/report-nov-9-2.jpg" class="report-may-11-2" style="width:100%"/>
					<img src="${ctx}/img/report-nov-9-3.jpg" class="report-may-11-2" style="width:100%"/>
			     </div>
			</div>
		</div>	
		<script src="${ctx}/js/zepto.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="${ctx}/js/report/touch.js" type="text/javascript" charset="utf-8"></script>
		<script src="${ctx}/js/report/index.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript">
		$(function(){
			var length = $(".leftToRight").length;
			for(var i =0;i<length;i++){
				$(".leftToRight").eq(i).css("animation","fadeInLeft 1s ease "+0.2*i+"s 1 both")
			}
			var length1 = $(".leftToRight1").length;
			for(var i =0;i<length1;i++){
				$(".leftToRight1").eq(i).css("animation","fadeInLeft 1s ease "+0.2*i+"s 1 both")
			}
			var length2 = $(".leftToRight2").length;
			for(var i =0;i<length2;i++){
				$(".leftToRight2").eq(i).css("animation","fadeInLeft 1s ease "+0.2*i+"s 1 both")
			}
		})
	</script>
	</body>
</html>