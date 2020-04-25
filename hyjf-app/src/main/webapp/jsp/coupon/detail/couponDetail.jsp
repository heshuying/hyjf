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
	    <c:if test="${detail.usedFlag == '已失效'}">
			<img src="${ctx}/img/coupon-outdate.png" class="coupon-outdate-img">
		</c:if>
		<div class="specialFont response">
			<!--item begin-->			
			<div class="detailContent bg_white">
				<ul>
					<li>
						<span class="couponTypeStr">${detail.couponTypeStr}</span>
						<c:if test="${detail.couponTypeStr == '体验金' or detail.couponTypeStr == '代金券'}">
							<span class="right right-black"><c:out value="${detail.couponQuotaOriginal}"></c:out><i>元</i></span>
						</c:if>
						<c:if test="${detail.couponTypeStr == '加息券'}">
							<span class="right right-black"><c:out value="${detail.couponQuotaOriginal}"></c:out><i>%</i></span>
						</c:if>
					</li>
					<li>
						<span class="left">券名称：</span>
						<span class="right">${detail.couponName}</span>
					</li>
					<li>
						<span class="left">编<i class="hiddenText">是</i>号：</span>
						<span class="right">${detail.couponUserCode}</span>
					</li>
					<c:if test="${detail.usedFlag == '未使用' or detail.usedFlag == '已失效'}">
					<li>
						<span class="left">有效期：</span>
						<span class="right">${detail.addTime } ~ ${detail.endTime}</span>
					</li>
					</c:if>
					<c:if test="${detail.usedFlag == '已使用'}">
					<li>
						<span class="left">使用时间：</span>
						<span class="right">${detail.orderDate}</span>
					</li>
					</c:if>
					<c:if test="${detail.couponTypeStr == '体验金'}">
					<li style="position:relative">
						<span class="left">收益期限<img class="profitimg-btn" src="${ctx}/img/date-help.png" alt="" style="display:inline-block"/>：</span>
						<div  class="profit-formula" style="top:33px;left:3px">
		            		<div class="arrow-top"></div>
		            		<p>收益=券面值*项目年化收益率/365*收益期限</p>
		            	</div>
						<span class="right">${detail.couponProfitTime }</span>
					</li>
					</c:if>
				</ul>
				<div class="clearBoth"></div>
			</div>
			<!--item end-->
			<div class="grayHeight"></div>	
			<!--item begin-->			
			<c:if test="${detail.usedFlag == '未使用' or detail.usedFlag == '已失效'}">
			<div class="detailContent bg_white">
				<ul>
					<li>
						<span class="left">金额限制：</span>
						<span class="right"><c:out value="${detail.tenderQuota}"></c:out></span>
					</li>
					<li>
						<span class="left">期限限制：</span>
						<span class="right">${detail.projectExpirationType}</span>
					</li>
					<li>
						<span class="left">适用终端：</span>
						<span class="right">${detail.couponSystem}</span>
					</li>
					<li>
						<span class="left">适用项目：</span>
						<span class="right">${detail.projectType}</span>
					</li>
				</ul>
				<div class="clearBoth"></div>
			</div>
			</c:if>
			
			<c:if test="${detail.usedFlag == '已使用' and detail.tenderType != 3}">
			<div class="detailContent bg_white">
				<ul>
					<li>
						<span class="left">项目编号：</span>
						<span class="right"><c:out value="${detail.borrowNid}"></c:out></span>
					</li>
					<li>
						<span class="left">项目期限：</span>
						<span class="right">${detail.borrowPeriod}</span>
					</li>
					<li>
						<span class="left">年化收益：</span>
						<span class="right">${detail.borrowApr}</span>
					</li>
					<li>
						<span class="left">还款方式：</span>
						<c:if test="${detail.couponTypeStr == '体验金'}">
							<span class="right">按天计息，到期还本还息</span>
						</c:if>
						<c:if test="${detail.couponTypeStr != '体验金'}">
							<span class="right">${detail.borrowStyleName}</span>
						</c:if>
					</li>
				</ul>
				<div class="clearBoth"></div>
			</div>
			</c:if>
			<c:if test="${detail.usedFlag == '已使用' and detail.tenderType == 3}">
			<div class="detailContent bg_white">
				<ul>
					<li>
						<span class="left">项目编号：</span>
						<span class="right"><c:out value="${detail.borrowNid}"></c:out></span>
					</li>
					<li>
						<span class="left">项目期限：</span>
						<span class="right">${detail.lockPeriodView}</span>
					</li>
					<li>
						<span class="left">年化收益：</span>
						<span class="right">${detail.expectAprView}</span>
					</li>
					<li>
						<span class="left">还款方式：</span>
						<c:if test="${detail.couponTypeStr == '体验金'}">
							<span class="right">按天计息，到期还本还息</span>
						</c:if>
						<c:if test="${detail.couponTypeStr != '体验金'}">
							<span class="right">${detail.planStyleName}</span>
						</c:if>
					</li>
				</ul>
				<div class="clearBoth"></div>
			</div>
			</c:if>
			<!--item end-->
			<div class="grayHeight"></div>	
			<!--item begin-->			
			<c:if test="${detail.usedFlag == '未使用' or detail.usedFlag == '已失效'}">
			<div class="detailContent bg_white">
				<ul>
					<li>
						<span class="left">券来源：</span>
						<span class="right"><c:out value="${detail.couponFrom}"></c:out></span>
					</li>
					<li>
						<span class="left">备&nbsp;&nbsp;&nbsp;&nbsp;注：</span>
						<span class="right">${detail.content }</span>
					</li>
				</ul>
				<div class="clearBoth"></div>
			</div>
			</c:if>
			<!--item end-->
			<!--item begin-->		
			<c:if test="${detail.usedFlag == '已使用'}">
			<c:choose>
			    <c:when test="${detail.borrowTenderStatus == '0' || detail.orderStatus == '0' || detail.orderStatus == '2'}">
			      <div class="detailContent bg_white">
					<ul>
						<li>
							<span class="left">历史回报：</span>
							<span class="right"><c:out value="${earnings=='0.00'?'--':earnings }"></c:out></span>
						</li>
						<li>
							<span class="left">项目状态：</span>
							<c:if test="${detail.borrowStatus == '复审中' }">
								<span class="right">复审中</span>
							</c:if>
							<c:if test="${detail.borrowStatus == '投标中' }">
								<span class="right">投标中</span>
							</c:if>
						</li>
					</ul>
					<div class="clearBoth"></div>
				</div>
			    </c:when>
			    <c:otherwise>
					<c:if test="${detail.borrowStyle == 'end' or detail.borrowStyle == 'endday' or detail.planStyle == 'end' or detail.planStyle == 'endday'}">
					<c:forEach items="${couponRecoverlist}" var="item" varStatus="vs">
					<div class="detailContent bg_white">
						<ul>
						    <c:if test="${item.recoverStatus == '未还款'}">
								<li>
									<span class="left">待收收益：</span>
									<span class="right"><c:out value="${item.recoverInterest}"></c:out></span>
								</li>
								<li>
									<span class="left">回款时间：</span>
									<span class="right">${item.recoverTime}</span>
								</li>
							</c:if>
						   <c:if test="${item.recoverStatus == '已还款'}">
								<li>
									<span class="left">已得收益：</span>
									<span class="right"><c:out value="${item.recoverInterest}"></c:out></span>
								</li>
								<li>
									<span class="left">回款时间：</span>
									<span class="right">${item.recoverYestime}</span>
								</li>
							</c:if>
						    <c:if test="${item.recoverStatus == '已还款' and item.receivedFlg == '已过期'}">
								<li>
									<span class="left">待收收益：</span>
									<span class="right"><c:out value="${item.recoverInterest}"></c:out>&nbsp;&nbsp;<span style="color:blue">已过期</span></span>
								</li>
								<li>
									<span class="left">过期时间：</span>
									<span class="right">${item.expTime}</span>
								</li>
							</c:if>
						</ul>
						<div class="clearBoth"></div>
						
					</div>
					</c:forEach>
					</c:if>
					<!--item end-->
					<c:if test="${detail.borrowStyle == 'month' or detail.borrowStyle == 'principal' or detail.borrowStyle == 'endmonth'}">
					<div class="contract_item bg_white">
						<div>
							<div class="contract_item_content">
								<p><span>已得收益 ：</span><c:out value="${receivedMoney}"></c:out></p>
							</div>
						</div>
					
						<c:if test="${not empty couponRecoverlist}">
							<div class="item">
								<table border="0" cellspacing="0" cellpadding="0" class="borderNone">
									<tr>
										<td>收益（元）</td>
										<td>状态</td>
										<td>回款日期</td>
									</tr>
									<c:forEach items="${couponRecoverlist}" var="item" varStatus="vs">
										<tr>
											<td><c:out value="${item.recoverInterest}"></c:out></td>
											<td><c:out value="${item.receivedFlg}"></c:out></td>
											<td><c:out value="${item.recoverTime}"></c:out></td>
										</tr>
									</c:forEach>
								</table>
							</div>
						</c:if>
					</div>
					</c:if>

		    </c:otherwise>
			</c:choose>
			
			</c:if>
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