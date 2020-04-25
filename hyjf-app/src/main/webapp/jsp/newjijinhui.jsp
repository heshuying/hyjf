<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<link rel="stylesheet" href="${ctx}/css/style.css">
<title></title>
</head>
<body style="font-size: 14px;">
 <div class="jjh">
        <div class="jjh-banner">
            <h2>携手全球与国内领先的基金机构</h2>
            <h4>为境内投资者提供多区域、多种类的基金产品投资服务</h4>
        </div>
        <div class="jjh-container">
            <div class="jjh-item1 jjhi1">
                <div class="jjh-item1-title">专业 <div class="after"></div></div>
                <div class="jjh-item1-content">我们秉持专业的理念与更全面的产品选择为用户提供国内外市场信息，放眼全球资金机遇，满足您不同的投资需求信息与资产配置。</div>
            </div>
            <div class="jjh-item1 pr jjhi2">
                <div class="jjh-item1-title">对接国内基金<div class="after"></div></div>
                <div class="jjh-item1-content">本地市场多类别基金品种，如证券投资基金、货币基金等。我们精选优质基金，为投资者洞悉先机，把握中国股市成长动能。可作为分散投资风险的组合选择之一，丰富您的投资配置。</div>
            </div>
            
        </div>
        <div class="jjh-b-container">
            <div class="jjh-item1">
                <div class="jjh-item1-icon jjh-b1"></div>
                <div class="jjh-item1-right">
                    <div class="jjh-item1-title">股票型基金</div>
                    <div class="jjh-item1-content">60%以上的基金资产投资于股票的，为股票基金；股票型基金依投资标的产业，又可分为各种产业型基金，这类型基金是以股票为主要投资标的。</div>
                </div>
            </div>
            <div class="jjh-item1">
                <div class="jjh-item1-icon jjh-b2"></div>
                <div class="jjh-item1-right">
                    <div class="jjh-item1-title">混合型基金</div>
                    <div class="jjh-item1-content">混合型基金投资范围为具有良好流动性的金融工具，包括国内依法公开发行上市的股票、债券以及中国证监会允许基金投资的其他金融工具。如法律法规或监管机构以后允许基金投资的其他品种，基金管理人在履行适当程序后，可以将其纳入投资范围。</div>
                </div>
            </div>
            <div class="jjh-item1">
                <div class="jjh-item1-icon jjh-b3"></div>
                <div class="jjh-item1-right">
                    <div class="jjh-item1-title">货币型基金</div>
                    <div class="jjh-item1-content">货币基金根据短期利率的变动和市场格局的变化，积极主动地在债券资产和回购资产之间进行动态地资产配置。通过对中长期回购利率波动规律的把握对回购资产进行时间方向上的动态配置策略。同时根据回购利差进行回购品种的配置比例调整。</div>
                </div>
            </div>
        </div>
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