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
	<div class="newcomer">
	    <div class="newcomer-Top">
			<div class="newcomer-Top-bg1"></div>
			<div class="newcomer-Top-bg2">
				<h4>不止会投资,更会给您赚钱</h4>
			</div>
			<div class="newcomer-Top-bg3"></div>
		</div>
		<div class="newcomer-Ban">
		    <ul>
		    <li>
		        <div class="newcomer-img1"></div>
		        <h3>资金安全</h3>
		        <p>
		        	十二重安全盾建立强大风险防控体系严格评估相应安全保障措施。
		        </p>
		    </li>
		    <li>
		        <div class="newcomer-img2"></div>
		        <h3>收益稳健</h3>
		        <p>
		        	汇盈金服尊重每一位投资者的未来，提供不同历史年回报率产品信息服务。
		        </p>
		    </li>
		    <li>
		        <div class="newcomer-img3"></div>
		        <h3>流动性强</h3>
		        <p>
		        	平台还提供期限更灵活的产品及债权转让服务。无投资门槛，灵活便捷。
		        </p>
		    </li>
		    <li class="newcomer-flow">
		    	<h3>投资流程</h3>
		    	<div class="newcomer-img4"></div>
		    	<p>
		    		<span>注册</span>
		    		<span>开户</span>
		    		<span>充值</span>
		    		<span>投资</span>
		    		<span>收益</span>
		    	</p>
		    </li>
		    </ul>
		</div>
	    <div class="newcomer-Bot">
		    <p>更多精彩，敬请期待 ...</p>
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