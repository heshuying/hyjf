<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="${ctx}/css/club.css">
<title></title>
</head>
<body>
	<div class="club-level-top">
			<ul class="club-level-top-slider">
				<div class="club-level-top-line">
					<div></div>
				</div>
				<li class="club-level-top-cell fl">
					<div class="club-level-top-point club-not">
					 	<img src="${ctx}/img/my-level-yes.png" class="club-level-top-pic"/>
					 	<span>白银</span>
					 	<p>0</p>
					</div>
					<div></div>
				</li>
				<li class="club-level-top-cell fl">
					<div class="club-level-top-point club-not">
					 	<img src="${ctx}/img/my-level-no.png" class="club-level-top-pic"/>
					 	<span>黄金</span>
					 	<p>7000</p>
					</div>
					<div></div>
				</li>
				<li class="club-level-top-cell fl">
					<div class="club-level-top-point club-not">
					 	<img src="${ctx}/img/my-level-no.png" class="club-level-top-pic"/>
					 	<span>铂金</span>
					 	<p>17000</p>
					</div>
					<div></div>
				</li>
				<li class="club-level-top-cell fl">
					<div class="club-level-top-point club-not">
					 	<img src="${ctx}/img/my-level-no.png" class="club-level-top-pic"/>
					 	<span>黑金</span>
					 	<p>57000</p>
					</div>
					<div></div>
				</li>
				<li class="club-level-top-cell fl">
				<!-- club-already 与 club-click标识当前等级  -->
					<div class="club-level-top-point club-already club-click">
					 	<img src="${ctx}/img/my-level-no.png" class="club-level-top-pic"/>
					 	<span>钻石</span>
					 	<p>117000</p>
					</div>
					<div></div>
				</li>
				<li class="club-level-top-cell fl">
					<div class="club-level-top-point club-not">
					 	<img src="${ctx}/img/my-level-no.png" class="club-level-top-pic"/>
					 	<span>至尊</span>
					 	<p>807000</p>
					</div>
					<div></div>
				</li>
			</ul>
		</div>
		<div class="my-club-more">会员特权</div>
		<div class="my-club-content club-level-content">
			<div class="my-club-content-item">
				<a href="">
					<img src="${ctx}/img/c1.png" alt="img" />
					<p>欢迎礼包</p>
					<p class="my-club-content-p-2">有券在手唱响加息</p>
				</a>
			</div>
			<div class="my-club-content-item">
				<a href="">
					<img src="${ctx}/img/c2.png" alt="img" />
					<p>升级礼包</p>
					<p>任性投资开心赚</p>
				</a>
			</div>
			<div class="my-club-content-item">
				<a href="">
					<img src="${ctx}/img/c3.png" alt="img" />
					<p>专属活动</p>
					<p>惊喜连连享不停</p>
				</a>
			</div>
			<div class="my-club-content-item">
				<a href="">
					<img src="${ctx}/img/c4.png" alt="img" />
					<p>尊贵标识</p>
					<p>彰显您的与众不同</p>
				</a>
			</div>
			<div class="my-club-content-item">
				<div class="my-club-content-wait">敬请期待</div>
				<a href="">
					<img src="${ctx}/img/c5.png" alt="img" />
					<p>加倍积分</p>
					<p>万千好礼轻松换</p>
				</a>
			</div>
			<div class="my-club-content-item">
				<div class="my-club-content-wait">敬请期待</div>
				<a href="">
					<img src="${ctx}/img/c6.png" alt="img" />
					<p>生日礼包</p>
					<p>慢慢人生汇盈见证</p>
				</a>
			</div>
		</div>
<script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
<script>
	$(function(){
		//顶部向左偏移偏移
		var alreadyIndex1 = $(".club-already").parent().index()-1;
		var nextV = parseInt($(".club-already").parent().next().find("p").text());
		var nowV = parseInt($(".club-already").find("p").text());
		var myV = 400000;
		var percentV = (myV-nowV)/(nextV-nowV);
		var addV = percentV*50
		$(".club-level-top").scrollLeft(118*alreadyIndex1);
		$(".club-level-top-line div").css("width",118*alreadyIndex1+68+addV+"px");
		var switchTrail = function(){
			//点击三角变换
			var _self = $(this);
			var parent = _self.parent().siblings().find(".club-level-top-point").removeClass("club-click");
			var alreadyIndex = $(".club-already").parent().index();
			var clickIndex = _self.parent().index();
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