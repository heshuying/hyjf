<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ page session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
		<link rel="stylesheet" type="text/css" href="${ctx}/css/page.css"/>
		<link rel="stylesheet" href="${ctx}/css/font-awesome.min.css">
		<title>福利大礼包</title>
	</head>
	<body>
		<!-- 过期提醒 -->
		<div class="activeOutdatePop"></div>
		<input type="hidden" id="sign" value="${sign}">
		<input type="hidden" id="platform" value="${platform}">
		<div class="specialFont response bg-grey ">
			<%-- <div class="active-item" data-number="1">
				<img src="${ctx}/img/active1.png" alt="活动" data-number="1">
				<p>注册专享  注册成功即送10000元投资体验金 <span>进行中</span></p>
				<p>注册成功即到账，可在优惠券中查看</p>
			</div> --%>
			<div class="active-item">
				<img src="${ctx}/img/active2.jpg" alt="活动" data-number="2">
				<p>全民加息券  首次测评成功即送加息券 <span>进行中</span></p>
				<p>新老用户均可参与，测评成功即到账</p>
			</div>
			<div class="active-rule">
				<h5>活动规则：</h5>
				<p>1、活动时间：自本页面公布日起，长期有效，结束时间请关注平台公告；</p>
				<p>2、加息券：新、老用户在活动期间首次完成测评，即送<span>1张</span>加息券；在活动开始前完成测评的用户，系统自动补发<span>1张</span>加息券；所有优惠券可在“我的账户->我的优惠券”中查看；</p>
				<p>3、优惠券发放时间：成功参与活动后，即刻发放；</p>
				<p>4、优惠券投资范围：仅限于投资汇直投和汇消费项目；</p>
				<p>5、优惠券收益领取规则：项目到期后，加息券收益自动发放；</p>
				<p>6、优惠券订单，平台不提供对应的居间协议；</p>
				<p>7、优惠券券仅限个人使用，不可转赠；优惠券有时效限制，请用户合理安排使用时间；</p>
				<p>8、活动解释权在法律允许的范围内归汇盈金服所有。</p>
				<p>注：本活动与苹果公司等应用平台无关；</p>
			</div>
			
		</div>
		<script src="${ctx}/js/jquery.min.js" type="text/javascript" charset="utf-8"></script>
		<script charset="utf-8">
		var showTip = function(){
			var _self = $(this)
			var number = _self.index()+1;
			var platform = $("#platform").val();
			var sign = $("#sign").val();
			/* alert(number);
			alert(platform);
			alert(sign); */
			var url="";
			var activeId="";
/* 			if(number==1){
				url="${ctx}/coupon/registerActiveCheck";
				activeId=35
			} */
			if(number==1){
				url="${ctx}/coupon/evaluateActiveCheck";
				activeId=25
			}
			$.ajax({
				type:"get",
				data:{"sign":sign,"platform":platform,"activeId":activeId},
				url:url,
				async:false,
				datatype:"json",
				success:function(data){	
					if(data.error===1){ //未登录/未注册，点击跳转至登录页面
						var activeOutdatePop = $(".activeOutdatePop")
						activeOutdatePop.text(data.msg).show().delay(3000).fadeOut();
					}else if(data.error==0){
						window.location.href='hyjf://jumpLogin/?';
					}else if(data.error==3){
						window.location.href="${ctx}/financialAdvisor/init?sign="+sign+"&platform="+platform;
					}
				} 
			});
		}
		$(".active-item").on("click",showTip);
		</script>
	</body>
</html>