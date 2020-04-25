<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
		<title>维护通知</title>
		<style type="text/css">
			*{margin:0 auto;padding:0 auto}
			.maintenanceImg img{display: block;width:100%}
			.maintenanceTxt{color: #404040;font-family: "微软雅黑";}
			.maintenanceTxt p{text-indent: 2em;}
			.maintenanceTxt p:first-child{text-indent: 0em;}
			@media only screen and (max-width:480px ) {
				.maintenanceTxt{padding:25px;font-size: 15px;line-height:27px}
			}
			@media only screen and (min-width:480px ) {
				.maintenanceTxt{padding:40px;font-size: 17px;line-height:35px}
			}
			.colorRed{color: red;}
		</style>
	</head>
	<body>
	    <div class="maintenance">
	   	    <div class="maintenanceImg">
	   		    <img src="${ctx}/images/tzjoptimize/weihu-announce.jpg" alt="" />
	   	    </div>
	   	    <div class="maintenanceTxt">
	   	    	<p>尊敬的汇盈金服用户，</p>
	   	    	<p>您好！本平台于<span class="colorRed">${rangeStart }</span>至<span class="colorRed">${rangeEnd }</span>对接江西银行资金存管，进行数据迁移与系统升级。受此影响，期间汇盈金服平台将暂停与存管相关的一系列业务，包括但不限于开户、充值、提现、投资、还款、修改密码等，<strong>期间APP端与微官网无法正常使用</strong>。</p>
	   	        <p>试技术对接进度将会提前或延迟业务恢复，请关注官网公告。</p>
	   	        <p> 给您带来不便，敬请谅解！</p>
	   	    </div>
	   </div>
	</body>
</html>