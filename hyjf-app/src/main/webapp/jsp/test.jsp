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
		<title></title>
	</head>
<body class="coupon-detail-body">
		<div class="detailHead">
                <span class= "detailHeadLeft">
					<i class="orange"><b>
					11
					</b>%</i>
					<em>历史年回报率</em>
				</span>
				<span class= "detailHeadMiddle">
					<i>
					<b>
					3
					</b></i>
					<em>项目期限</em>
				</span>
                <span class= "detailHeadRight">
					<i><b>
					${projectDeatil.investAccount}
					</b>元</i>
					<em>项目剩余</em>
				</span>
				<c:if test="${projectDeatil.status eq 10}">
				<span class="startDate">
					${projectDeatil.onTime}开标
					</span>
				</c:if>
				<c:if test="${projectDeatil.status ne 10}">
				<span class="process">
					<i>
						<b></b>
					</i>
					<small>已投资<span id= "centNum">${projectDeatil.borrowSchedule}</span>%</small>
				</span>
				</c:if>
				<div class="clearBoth"></div>
				<span class="right return-type">等额本息</span>
			</div>
		<img src="img/coupon-outdate.png"/ class="coupon-detail-outdate">
		<div class="specialFont response ">
			<div class="coupon-detail-cell">
				<div class="coupon-detail-header">
					<i class="coupon-icon">加息券</i>
					<em>5.00</em>
					<i>%</i>
				</div>
				<p><span>券名称：</span>注册用户专享体验金</p>
				<p><span>编号：</span>YHQ</p>
				<p><span>有效期：</span>2016.03.01-2016.05.31</p>
			</div>
			<div class="coupon-detail-cell">
				<p><span>金额限制：</span>不限</p>
				<p><span>期限限制：</span>适用于6个月</p>
				<p><span>使用终端：</span>2016.03.01-2016.05.31</p>
				<p><span>适用项目：</span>汇直投</p>
			</div>
			<!-- 体验金 -->
			<div class="coupon-detail-cell">
				<div>
					<span>项目标题：</span><span class="coupon-detail-cell-right">临沂市临沂市临沂市临沂市临沂市临沂市临沂市临沂市临沂市临沂市临沂市临沂市</span>
				</div>
				<p><span>项目期限：</span>6个月</p>
				<p><span>年化收益：</span>15.00%</p>
				<p><span>还款方式：</span>汇直投</p>
			</div>
			<div class="coupon-detail-cell">
				<p><span>券来源：</span>不限</p>
				<p><span>备注：</span>融资方还款后</p>
				<p><span>已得收益：</span>10.00元<i class="coupon-staus-outdate coupon-staus-outdate-adjust">已过期</i></p>
				<table  cellspacing="0" cellpadding="0" border="0px">
					<tr class="coupon-detail-table-header">
						<td>收益(元)</td>
						<td>状态</td>
						<td>汇款日期</td>
					</tr>
					<tr>
						<td>10.00</td>
						<td class="coupon-staus-unclaimed">未领取</td>
						<td>2016.03.03</td>
					</tr>
					<tr>
						<td>10.00</td>
						<td class="coupon-staus-outdate">已过期</td>
						<td>2016.03.03</td>
					</tr>
				</table>
			</div>
			<div class="foot-left"><a href=""><img src="${ctx}/img/cal.png"/></a></div>
			<div class="foot bg_grey_new">
				<span class="btn_bg_color textWhite"><a id="investNow"  class="textWhite">立即投资</a></span>
			</div>
		</div>
		<script type="text/javascript" src="${ctx}/js/jquery.min.js" ></script>
		<script>
			$(function(){
				var urlCurrent = window.location.host+"/hyjf-app/jsp/cal.jsp";
				var types = ["按月计息，到期还本付息","按天计息，到期还本付息","先息后本","等额本息","等额本金"]
				var percent = parseInt($(".detailHeadLeft b").text());
				var time = parseInt($(".detailHeadMiddle").text());
				var type = $.trim($(".return-type").text())
				for(var i = 0;i<types.length;i ++){
					if(types[i]==type){
						var calType = i;
					}
				}
				$(".foot-left a").prop("href","cal.jsp?percent="+percent+"&time="+time+"&calType="+calType)
			})
		</script>
</body>
</html>