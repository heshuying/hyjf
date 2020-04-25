<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<style>
.container-1300{
	position:relative;
}
.top-bar .right-side{
	position:absolute;
	right:0;
	width:236px;
}
.earnUlS{
	height:36px !important;
	line-height:35px !important;
	margin-top:-27px;
}
.swapH,.news_liS{
	text-align:right !important;
}
</style>
<div class="top-bar">
	<div class="container-1192">
		<div class="phone"><div class="iconfont iconfont-tel"></div>客服电话：400-900-7878（服务时间  9:00-18:00）</div>
		<div class="right-side"> 
			<span class="mobile-client ">
				<span><div class="iconfont iconfont-phone"></div>手机客户端</span>
				<div class="tool-popup">
			    	<div class="tool-pop-title">
			    		扫描下载客户端
			    	</div>
			    	<img src="${cdn}/img/qrcode.jpg" alt="" width="150" height="150">
			    </div>
			</span>
			  &nbsp;|&nbsp;			  
			  <div class="earnUl earnUlS">
				<ul class="news_li news_liS news_liH">
						<li><span>市场有风险 投资需谨慎</span></li>
						<li><span>历史回报不等于实际收益</span></li>
				</ul>
				<ul class="swap swapH"></ul>
			 </div>
			  
		</div>
	</div>
</div>
<header id="header" class="mall-header">
	<div class="container-1192">
		<h1 class="hd-logo mall-logo">
			<a href="${ctx}/" title="汇盈金服">
				<img src="${cdn}/img/mall/mall_logo.jpg" width="209" height="35" alt="汇盈金服" />
			</a>
		</h1>
		<div class="hd-login">
			<a id="login" href="${ctx}/user/login/init.do" class="action-link">登录</a>
			<div class="cut-line"></div>
			<a id="register" href="${ctx}/user/regist/init.do" href="#" class="action-link">注册</a>
			<!--  登录后-->
			<a href="${ctx}/user/pandect/pandect.do" id="face" style="display: none;"  class="hd-face">
				<img id="faceUrl" src="" alt="" width="40" height="40" />
			</a> 
			<a href="${ctx }/user/pandect/pandect.do" id="username" style="display: none;" class="hd-user"></a> 
			<a href="${ctx}/user/login/logout.do" id="logout" style="display: none;" href="#" class="iconfont iconfont-logout hd-logout"></a>
		</div>
		<ul class="hd-nav" id="hdNav">
                <li id="mallIndex"><a href="#">商城首页</a></li>
                <li id="mallList"><a href="#">商品列表</a></li>
                <li id="mallDraw"><a href="#">积分抽奖</a></li>
                <li id="mallSource"><a href="#">我的积分</a></li>
                <li id="mallIntr"><a href="#">积分介绍</a></li>
        </ul>
	</div>
	<script src="${cdn}/js/jquery.min.js" type="text/javascript" charset="utf-8"></script>
	<script>
		/*
		 * 原生脚本
		 * 需要在li加上ID
		 * 
		 */
		function setActById(id){
			document.getElementById(id).className = "active";
		}
		
		function vipClub(){
			
			if("${isVip}" == "true"){
				// 是vip 跳转到vip管理
				window.location.href="${ctx}/vip/manage/init.do";
			}else{
				// 不是vip 跳转到vip申请
				window.location.href="${ctx}/vip/apply/init.do";
			}
		}
		
		
		function bH(){	
			th = parseInt(xh.css('top'));
			yh.css('top','36px');	
			xh.animate({top: th - 36 + 'px'},'slow');	//36为每个li的高度
			if(Math.abs(th) == hh-36){ //36为每个li的高度
				yh.animate({top:'0px'},'slow');
				zh=xh;
				xh=yh;
				yh=zh;
			}
			setTimeout(bH,3000);//滚动间隔时间 现在是3秒
		}
		$(document).ready(function(){
			$('.swapH').html($('.news_liH').html());
			xh = $('.news_liH');
			yh = $('.swapH');
			hh = $('.news_liH li').length * 36; //36为每个li的高度
			setTimeout(bH,3000);//滚动间隔时间 现在是3秒
			
		})
		
	</script>
</header>