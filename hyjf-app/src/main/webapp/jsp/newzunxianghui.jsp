<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html >
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<link rel="stylesheet" href="${ctx}/css/style.css">
<title></title>
</head>
<body style="font-size: 14px;">
<div class="zxh">
        <div class="zxh-banner">
            <h2>只为带给您非凡的客户体验</h2>
            <h4>顶级巅峰礼遇  专业态度  细节至上</h4>
        </div>
        <div class="zxh-container">
            <div class="zxh-item1">
                <div class="zxh-item1-icon"></div>
                <div class="zxh-item1-title">什么是尊享汇</div>
                <div class="zxh-item1-after"></div>
                <div class="zxh-item1-content">特别为合格贵宾投资客户和私人业务客户专设的管家式礼宾服务。开启您心享旅程的私人管家，无论您身在何处，我们都第一时间为您提供贴心的专属服务。</div>
            </div>
        </div>
        <div class="zxh-banner-2">
            尽享尊贵特权  成就品质生活
            <div class="after"></div>
        </div>
        <div class="zxh-b-container">
            <div class="zxh-item1">
                <div class="zxh-item1-icon zxh-b1"></div>
                <div class="zxh-item1-right">
                    <div class="zxh-item1-title">贵宾礼遇</div>
                    <div class="zxh-item1-content">专属尊享汇贵宾投资中心 ，您将享有管家式礼宾服务，将由只属于您的一对一高级客户经理以及资深管理团队悉心服务，助您运筹帷幄。</div>
                </div>
            </div>
            <div class="zxh-item1">
                <div class="zxh-item1-icon zxh-b2"></div>
                <div class="zxh-item1-right">
                    <div class="zxh-item1-title">巅峰服务</div>
                    <div class="zxh-item1-content">当您拥有高额投资时，多元化的投资组合及收益更显重要。加入尊享汇的您在享受优质服务的同时将获得更高收益和更优惠的费率。</div>
                </div>
            </div>
            <div class="zxh-item1">
                <div class="zxh-item1-icon zxh-b3"></div>
                <div class="zxh-item1-right">
                    <div class="zxh-item1-title">专享收益</div>
                    <div class="zxh-item1-content">从欣赏精彩非凡的全球娱乐盛宴，到令人热血沸腾的运动赛事，为挚爱之人选择珍贵且意义非凡的礼物——只需拨打巅峰服务专线，您的专属客户经理将会协助您进行联络和安排。特别的服务，只为特别的您。</div>
                </div>
            </div>
            <div class="zxh-item1">
                <div class="zxh-item1-icon zxh-b4"></div>
                <div class="zxh-item1-right">
                    <div class="zxh-item1-title">全方位投资规划</div>
                    <div class="zxh-item1-content">地位显赫的您当然日理万机，尊享汇为您度身定制的全方位投资规划方案，始终以您的利益为先，助您完善投资组合，提升回报潜力，让您从容坐享高礼遇。</div>
                </div>
            </div>
			
        </div>
        <div class="zxh-more">更多精彩，敬请期待...</div>
    </div>
    <script src="${ctx}/js/jquery.min.js"></script>
    <script>
    	function changeFZ(){
    		var w = $(window).width();
    		setHtmlFz(w/10);
    	}
    	function setHtmlFz(fz){
    		$("html").css("font-size",fz+"px");
    	}
    	window.onload = function(){
    		changeFZ();
    	}
    	window.onresize = function(){
    		changeFZ();
    	}
    </script>
</body>
</html>