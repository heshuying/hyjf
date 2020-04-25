<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="overflow:hidden">
<head>
<meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="${ctx}/css/club.css">
<title>会员特权</title>
</head>
<body >
	<input type="hidden" id="vipValue" value="${vipValue}">
	<div class="club-level-top">
			<ul class="club-level-top-slider">
				<div class="club-level-top-line">
					<div></div>
					<span></span>
				</div>
				<i></i>
				<c:forEach items="${vipInfoList}" var="record"
						begin="0" step="1" varStatus="status">
					<li class="club-level-top-cell fl">
						<div class="
							<c:if test="${record.vipLevel==vipLevel }">
									 club-level-top-point club-already club-click
							</c:if>
							<c:if test="${record.vipLevel!=vipLevel }">
								club-level-top-point club-not
							</c:if>">
							<img src="${ctx}/img/d${record.vipLevel}.png" class="club-level-top-level"/>
							<c:if test="${record.vipLevel==vipLevel }">
								<img src="${ctx}/img/my-level-yes.png" class="club-level-top-pic"/>
							</c:if>
							<c:if test="${record.vipLevel!=vipLevel }">
								<img src="${ctx}/img/my-level-no.png" class="club-level-top-pic"/>
							</c:if>
						 	
						 	<span>${record.vipName}</span>
						 	<p>${record.vipValue}</p>
						</div>
						<div></div>
					</li>
				</c:forEach>
				<li class="hide"><p>8000000</p></li>
				
			</ul>
		</div>
		<div class="my-club-more">会员特权</div>
		<div class="my-club-content club-level-content">
			<div class="my-club-content-item">
				<a href="hyjf://jumpH5/?{'url':'${host}/jsp/vip/manage/vip_detail_1.jsp'}">
					<img src="${ctx}/img/c1.png" alt="img" />
					<p>欢迎礼包</p>
					<p class="my-club-content-p-2">有券在手畅享加息</p>
				</a>
			</div>
			<div class="my-club-content-item">
				<a href="hyjf://jumpH5/?{'url':'${host}/jsp/vip/manage/vip_detail_2.jsp'}">
					<img src="${ctx}/img/c2.png" alt="img" />
					<p>升级礼包</p>
					<p>任性投资开心赚</p>
				</a>
			</div>
			<div class="my-club-content-item">
				<a href="hyjf://jumpH5/?{'url':'${host}/jsp/vip/manage/vip_detail_3.jsp'}">
					<img src="${ctx}/img/c3.png" alt="img" />
					<p>专属活动</p>
					<p>惊喜连连享不停</p>
				</a>
			</div>
			<div class="my-club-content-item">
				<a href="hyjf://jumpH5/?{'url':'${host}/jsp/vip/manage/vip_detail_4.jsp'}">
					<img src="${ctx}/img/c4.png" alt="img" />
					<p>尊贵标识</p>
					<p>彰显您的与众不同</p>
				</a>
			</div>
			<div class="my-club-content-item">
				<div class="my-club-content-wait">敬请期待</div>
				<a href="hyjf://jumpH5/?{'url':'${host}/jsp/vip/manage/vip_detail_5.jsp'}">
					<img src="${ctx}/img/c5.png" alt="img" />
					<p>加倍积分</p>
					<p>万千好礼轻松换</p>
				</a>
			</div>
			<div class="my-club-content-item">
				<div class="my-club-content-wait">敬请期待</div>
				<a href="hyjf://jumpH5/?{'url':'${host}/jsp/vip/manage/vip_detail_6.jsp'}">
					<img src="${ctx}/img/c6.png" alt="img" />
					<p>生日礼包</p>
					<p>漫漫人生汇盈见证</p>
				</a>
			</div>
		</div>
<script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
<script>
	$(function(){
		//顶部向左偏移偏移
		var alreadyIndex1 = $(".club-already").parent().index()-2;
		var nextV = parseInt($(".club-already").parent().next().find("p").text());
		var nowV = parseInt($(".club-already").find("p").text());
		var myV = parseInt($("#vipValue").val());
		var percentV = (myV-nowV)/(nextV-nowV);
		var addV = percentV*50
		$(".club-level-top").scrollLeft(118*alreadyIndex1);
		$(".club-level-top-line div").css("width",118*alreadyIndex1+60+addV+"px");
		var switchTrail = function(){
			//点击三角变换
			var _self = $(this);
			var alreadyIndex = $(".club-already").parent().index();
			var clickIndex = _self.parent().index();
			$(".club-level-top-slider").find(".club-click").removeClass("club-click")
			
			//_self.parent().siblings().find(".club-level-top-point").removeClass("club-click");
			_self.addClass("club-click");
			//判断点击与目前等级 如果大于当前等级加灰 
			if(clickIndex>alreadyIndex){
				$(".club-level-content").addClass("club-level-content-pop");
			}else{
				$(".club-level-content").removeClass("club-level-content-pop")
			}
			
		}
		$(".club-level-top-point").on("click",switchTrail);
	})
	
</script>
</body>
</html>