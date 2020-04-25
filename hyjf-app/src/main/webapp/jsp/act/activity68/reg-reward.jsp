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
		<link rel="stylesheet" href="${ctx}/css/font-awesome.min.css">
		<title>汇盈金服</title>
	</head>
	<body class="bg_grey reg-reward">
		<div class="activeOutdatePop"></div>
		<!-- 1 -->
		<c:if test="${status==0}">
		<section class="reg-reward-top">
			<img src="${ctx}/img/reg-reward-top.jpg"/>
			<!--红包提示图片  reg-reward后的数字1为 未领取 2为领取 3为老用户-->
			<img src="${ctx}/img/reg-reward-1.jpg"/>
		</section>
		<section class="reg-reward-steps">
			<!--步骤图片  1为step1 2为step2 3为step3-->
			<img src="${ctx}/img/reg-reward-step-1.png"/>
			<div class="reg-reward-steps-div">
				<div class="reg-reward-step-1">
					<h5>简单注册</h5>
					<p>开启财富之旅</p>
					<span><a href="${registerUrl}">立即注册</a></span>
				</div>
				<div class="reg-reward-step-2">
					<h5>红包到账</h5>
					<p>68元现金红包到手啦</p>
					<span><a href="">立即查看</a></span>
				</div>
			</div>
		</section>
		</c:if>
		<!-- 2 -->
		<c:if test="${status==1}">
		<section class="reg-reward-top">
			<img src="${ctx}/img/reg-reward-top.jpg"/>
			<!--红包提示图片  reg-reward后的数字1为 未领取 2为领取 3为老用户-->
			<img src="${ctx}/img/reg-reward-2.jpg"/>
		</section>
		<section class="reg-reward-steps">
			<!--步骤图片  1为step1 2为step2 3为step3-->
			<img src="${ctx}/img/reg-reward-step-2.png"/>
			<div class="reg-reward-steps-div">
				<div class="reg-reward-step-1-2 reg-reward-step-1">
					<h5>简单注册</h5>
					<p>开启财富之旅</p>
					<span class="hidden"><a href="">立即注册</a></span>
				</div>
				<div class="reg-reward-step-1">
					<h5>红包到账</h5>
					<p>68元红包到手啦</p>
					<c:if test="${versionStatus==0}">
						<span><a href='${newUserUrl}'>立即查看</a></span>
					</c:if>
					<c:if test="${versionStatus==1}">
						<span><a href='${newUserUrl}{"push":"1"}'>立即查看</a></span>
					</c:if>
					
					
				</div>
			</div>
		</section>
		</c:if>
		<!-- 3 -->
		<c:if test="${status==2}">
		<section class="reg-reward-top">
			<img src="${ctx}/img/reg-reward-top.jpg"/>
			<!--红包提示图片  reg-reward后的数字1为 未领取 2为领取 3为老用户-->
			<img src="${ctx}/img/reg-reward-3.jpg"/>
		</section>
		<section class="reg-reward-steps">
			<!--步骤图片  1为step1 2为step2 3为step3-->
			<img src="${ctx}/img/reg-reward-step-2.png"/>
			<div class="reg-reward-steps-div">
				<div class="reg-reward-step-1-2 reg-reward-step-1">
					<h5>简单注册</h5>
					<p>开启财富之旅</p>
					<span class="hidden"><a href="">立即注册</a></span>
				</div>
				<div class="reg-reward-step-3">
					<h5>老用户不能参加此活动</h5>
					<span><a href="${oldUserUrl}">去投资</a></span>
				</div>
			</div>
		</section>
		</c:if>
		<c:if test="${versionStatus==0}">
			<a href="${XSHUrl}" class="reg-rewar-warp-a">
		</c:if>
		<c:if test="${versionStatus==1}">
			<a href='${XSHUrl}{"push":"1"}' class="reg-rewar-warp-a">
		</c:if>
		<section class="reg-reward-recommend bg_white">
			<div class="reg-reward-recommend-title">
				为您推荐
			</div>
			<img src="${ctx}/img/reg-reward-touxiang.png">
			<p>新手汇</p>
			<span></span>
			<div class="reg-reward-recommend-item">
				<div class="reg-reward-recommend-item-1">
					<p class="hyjf-color font25">11.00%</p>
					<span class="color898989 font12">历史年回报率</span>
				</div>
				<div class="reg-reward-recommend-item-2 ">
					<p class="font25">30天</p>
					<span class="color898989 font12">项目期限</span>
				</div>
				<div class="reg-reward-recommend-item-3">
					
						<div class="circle" style="left:0">
							<div class="pie_left"><div class="invest-list-left"></div></div>
							<div class="pie_right"><div class="invest-list-right"></div></div>
							<div class="mask"><p>立即体验</p><span>90</span></div>
						</div>
					
				</div>
			</div>
			<div class="clearBoth"></div>
		</section>
		</a>
		<section class="bg_white reg-reward-rules">
			<div class="reg-reward-recommend-title">
				活动规则
			</div>
			<h5>活动时间:</h5>
			<p>2016年9月19日起，2016年10月18日止。</p>
			<h5>活动内容:</h5>
			<p>凡于活动期内成功注册汇盈金服的用户，即可免费领取68元现金红包。</p>
			<h5>奖励发放:</h5>
			<p>现金红包将于用户注册成功后发放至用户汇盈金服账户，登陆后于“优惠券”中查看。</p>
			<h5>注:</h5>
			<p>1.68元现金红包中包含18元、20元、30元代金券各一张，均自发放之日起30日内有效，过期作废。</p>
			<p>2.18元代金券单笔投资达1000元可用，20元代金券单笔投资达2000元可用，30元代金券单笔投资达5000元可用。</p>
			<p>3.本活动代金券仅限投资汇直投项目使用。</p>
		</section>
		<img src="${ctx}/img/reg-reward-bot.jpg"/>
	<script type="text/javascript" src="${ctx}/js/jquery.min.js" ></script>
	 <script type="text/javascript">
	 	$('.circle').each(function(index, el) {
				var num = $(this).find('span').text() * 3.6;
				if (num<=180) {
					$(this).find('.invest-list-right').css('transform', "rotate(" + num + "deg)");
				} else {
					$(this).find('.invest-list-right').css('transform', "rotate(180deg)");
					$(this).find('.invest-list-left').css('transform', "rotate(" + (num - 180) + "deg)");
				};
			});
	 </script>
     
    </body>
</html>