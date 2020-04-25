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
		<title>优惠券详情</title>
	</head>
	<body class="bg_grey couponDetailBody">
		<img src="${ctx}/img/coupon-outdate.png" class="coupon-outdate-img">
		<div class="specialFont response">
			<!--item begin-->			
			<div class="detailContent bg_white">
				<!-- 券详细区域 -->
				<ul>
					<li>
						<span class="couponTypeStr">代金券</span>
						<span class="right right-black"><i>￥</i><c:out value="550.00"></c:out></span>
					</li>
					<li>
						<span class="left">券名称：</span>
						<span class="right">注册用户专享代金券注册用户专享代金券</span>
					</li>
					<li>
						<span class="left">编<i class="hiddenText">是</i>号：</span>
						<span class="right">YHQ160322021564</span>
					</li>
					<li>
						<span class="left">有效期：</span>
						<span class="right">2016.03.01 ~ 2016.05.31</span>
					</li>
					<li>
						<span class="left">使用时间：</span>
						<span class="right">2016.03.01 12：12</span>
					</li>
					<li>
						<span class="left">收益期限：</span>
						<span class="right">3天</span>
					</li>
				</ul>
				<div class="clearBoth"></div>
			</div>
			<!--item end-->
			<div class="grayHeight"></div>	
			<!--item begin-->		
			<!-- 未使用展示 -->	
			<div class="detailContent bg_white">
				<ul>
					<li>
						<span class="left">金额限制：</span>
						<span class="right"><c:out value="不限"></c:out></span>
					</li>
					<li>
						<span class="left">期限限制：</span>
						<span class="right">适用于6个月的项目</span>
					</li>
					<li>
						<span class="left">使用终端：</span>
						<span class="right">苹果APP、安卓APP</span>
					</li>
					<li>
						<span class="left">适用产品：</span>
						<span class="right">汇直投/汇消费项目</span>
					</li>
				</ul>
				<div class="clearBoth"></div>
			</div>
			<!-- 已使用展示 -->
			<div class="detailContent bg_white">
				<ul>
					<li>
						<span class="left">项目编号：</span>
						<span class="right"><c:out value="HXD20160603012"></c:out></span>
					</li>
					<li>
						<span class="left">项目期限：</span>
						<span class="right">6个月</span>
					</li>
					<li>
						<span class="left">年化收益：</span>
						<span class="right">15.00%</span>
					</li>
					<li>
						<span class="left">还款方式：</span>
						<span class="right">按月计息，到期还本还息</span>
					</li>
				</ul>
				<div class="clearBoth"></div>
			</div>
			<!--item end-->
			<div class="grayHeight"></div>	
			<!--item begin-->	
			<!-- 未使用跟已过期的情况下使用		 -->
			<div class="detailContent bg_white">
				<ul>
					<li>
						<span class="left">券来源：</span>
						<span class="right"><c:out value="追梦如初 心安礼得"></c:out></span>
					</li>
					<li>
						<span class="left">备&nbsp;&nbsp;&nbsp;&nbsp;注：</span>
						<span class="right">融资方还款后，收益将自动发放到您的汇付天下账户</span>
					</li>
				</ul>
				<div class="clearBoth"></div>
			</div>
			<!--item end-->
			<!--item begin-->	
			<!-- 已使用情况下展示	 -->
			<div class="detailContent bg_white">
				<ul>
						<!-- 已使用->a、项目状态：复审中（未放款） -->
						<li>
							<span class="left">历史回报：</span>
							<span class="right"><c:out value="10.00元"></c:out></span>
						</li>
						<li>
							<span class="left">项目状态：</span>
							<span class="right">复审中</span>
						</li>
				   		<!-- 已使用->项目状态：还款中 -->
						<li>
							<span class="left">待收收益：</span>
							<span class="right"><c:out value="10.00元"></c:out></span>
						</li>
						<li>
							<span class="left">回款时间：</span>
							<span class="right">2016.04.14</span>
						</li>
						<!-- 已使用->项目状态：已还款 -->
						<li>
							<span class="left">已得收益：</span>
							<span class="right"><c:out value="10.00元"></c:out></span>
						</li>
						<li>
							<span class="left">回款时间：</span>
							<span class="right">2016.04.14</span>
						</li>
				    		<!-- 已使用->项目状态：已还款已过期 -->
						<li>
							<span class="left">待收收益：</span>
							<span class="right"><c:out value="10.00元"></c:out></span>
						</li>
						<li>
							<span class="left">过期时间：</span>
							<span class="right">2016.04.14</span>
						</li>
					</ul>
					<div class="clearBoth"></div>	
				</div>
				<!--item end-->
				<div class="contract_item bg_white">
					<!-- 项目状态：还款中/已还款 -->
					<div>
						<div class="contract_item_content">
							<p><span>已得收益 ：</span><c:out value="10.00元"></c:out></p>
						</div>
					</div>
					<div class="item">
						<table border="0" cellspacing="0" cellpadding="0" class="borderNone">
							<tr>
								<td>收益（元）</td>
								<td>状态</td>
								<td>回款日期</td>
							</tr>
							<tr>
								<td><c:out value="10.00"></c:out></td>
								<td><c:out value="已领取"></c:out></td>
								<td><c:out value="2016.03.17"></c:out></td>
							</tr>
							<tr>
								<td><c:out value="10.00"></c:out></td>
								<td><c:out value="已领取"></c:out></td>
								<td><c:out value="2016.03.17"></c:out></td>
							</tr>
							<tr>
								<td><c:out value="10.00"></c:out></td>
								<td><c:out value="已领取"></c:out></td>
								<td><c:out value="2016.03.17"></c:out></td>
							</tr>
						</table>
					</div>
				</div>
			<!--item end-->
			
		</div>
		<script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
		<script type="text/javascript">
			$('.profitimg-btn').click(function(){
				$('.profit-formula').fadeIn().delay(5000).fadeOut();
			})
		</script>
	</body>
</html>