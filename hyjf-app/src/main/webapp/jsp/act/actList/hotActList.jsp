<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
		<link rel="stylesheet" type="text/css" href="${ctx}/css/page.css"/>
		<title>热门活动</title>
	</head>
	<body class="bg_grey">
			<input type="hidden" name="jumpcommend" id="jumpcommend" value="${jumpcommend}" />
			<input type="hidden" name="version" id="version" value="${version}" />
			<div class="activeOutdatePop"></div>
			<c:forEach items="${listStarting}" var="actStarting">
			<div class="hot-div">
			    <c:if test="${empty actStarting.activityAppUrl }">
					<a href="">
				</c:if>
				 <c:if test="${!empty actStarting.activityAppUrl }">
					<a href="${ctx }${actDetailUrl}${actStarting.activityAppUrl }">
				</c:if>
						<img src="${fileDomainUrl }${!empty actStarting.imgApp?actStarting.imgApp : actStarting.img}"/>
						<!--进行中-->
						<p><span class="color404040 hot-act-span ">${actStarting.title }</span><span class="fr hot-act-imporcess">进行中</span></p>
						<p class="color898989" style="white-space:nowrap;">活动时间:<hyjf:date value="${actStarting.timeStart}"></hyjf:date>~<hyjf:date value="${actStarting.timeEnd}"></hyjf:date></p>
					</a>
			</div>
			</c:forEach>
			<c:forEach items="${listWaiting}" var="actWaiting">
			<div class="hot-div hot-act-not-start-flag">
				<img src="${fileDomainUrl }${!empty actWaiting.imgApp?actWaiting.imgApp : actWaiting.img}"/>
				<p><span class="color404040 hot-act-flag-show">${actWaiting.title }</span><span class="fr hot-act-not-start">未开始</span></p>
				<p class="color898989" style="white-space:nowrap;">活动时间:<hyjf:date value="${actWaiting.timeStart}"></hyjf:date>~<hyjf:date value="${actWaiting.timeEnd}"></hyjf:date></p>
			</div>
			</c:forEach>
			<c:forEach items="${listEnded}" var="actEnded">
			<div class="hot-div hot-act-end-flag">
				<img src="${fileDomainUrl }${!empty actEnded.imgApp?actEnded.imgApp : actEnded.img}"/>
				<p><span class="color404040 hot-act-flag-show">${actEnded.title }</span><span class="fr color898989">已结束</span></p>
				<p class="color898989" style="white-space:nowrap;">活动时间:<hyjf:date value="${actEnded.timeStart}"></hyjf:date>~<hyjf:date value="${actEnded.timeEnd}"></hyjf:date></p>
			</div>
			</c:forEach>
			<p class="hot-act-bottom">更多精彩敬请期待</p>
			<script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
			<script src="${ctx}/js/zepto.min.js" type="text/javascript" charset="utf-8"></script>
			<script>
			//$(".activeOutdatePop").html("<p>活动将在近期开启~</p><p>请关注活动时间~</p>").fadeIn().delay(1500).fadeOut();
			//$(".activeOutdatePop").html("<p>活动刚刚结束~</p><p>请关注其他活动~</p>").fadeIn().delay(1500).fadeOut();
				$(".hot-act-not-start-flag").click(function(){
					$(".activeOutdatePop").html("<p>活动将在近期开启~</p><p>请关注活动时间~</p>").fadeIn().delay(1500).fadeOut();
				});
				
				$(".hot-act-end-flag").click(function(){
					$(".activeOutdatePop").html("<p>活动刚刚结束~</p><p>请关注其他活动~</p>").fadeIn().delay(1500).fadeOut();
				});
			</script>
	</body>
</html>
