<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
		<meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
		<link rel="stylesheet" type="text/css" href="${ctx}/css/page.css?v=1"/>
		<link rel="stylesheet" href="${ctx}/css/font-awesome.min.css">
		<title>持有详情</title>
	</head>
	<body class="bg_grey">
		<div class="specialFont  cal-main">
				<section class="new-form-item  bg_white my-invest-header">
					<h5>
						${couponTender.borrowName}
					</h5>
					<div class="my-invest-header-div">
						<c:choose>
							<c:when test="${couponTender.label == '2'}">
								<span>加息收益率 <i class="hyjf-color">${couponTender.expectApr}</i></span>
							</c:when>
							<c:otherwise>
								<span>历史年回报率 <i class="hyjf-color">${couponTender.expectApr}</i></span>
							</c:otherwise>

						</c:choose>

						<span>锁定期限<i class="hyjf-color">${couponTender.lockPeriod}</i></span>
               		</div>
				</section>
				 <section class="new-form-item  bg_white my-invest-item">

               		<!-- 收益模块 -->
					 <c:choose>
					 <c:when test="${couponTender.label == '2' || couponTender.label == '0'}">
						<c:choose>
						 <c:when test="${type == '1'}">
							 <div>
								 <span>投资本金(元)</span>
								 <span class="hyjf-color format-num">${couponTender.account}</span>
							 </div>
						 </c:when>
							<c:otherwise>
								<div>
									<span>加入本金(元)</span>
									<span class="hyjf-color format-num">${couponTender.account}</span>
								</div>
							</c:otherwise>
						</c:choose>
					 </c:when>
					 <c:otherwise>
						 <div>
							 <span>用券金额(元)</span>
							 <span class="hyjf-color format-num">${couponTender.couponAmount}</span>
						 </div>
					 </c:otherwise>

					 </c:choose>


					 <c:choose>
						 <c:when test="${couponTender.label == '1'}">
							 <div style="overflow:inherit;position:relative">
								 <!-- 收益期限 -->
								 <span>收益期限<img class="profitimg-btn" src="${ctx}/img/date-help.png" alt="" style="display:inline-block"/></span>
								 <div  class="profit-formula">
									 <div class="arrow-top"></div>
									 <p>收益=券面值*项目年化收益率/365*收益期限</p>
								 </div>
								 <span>${couponTender.couponProfitTime}</span>
							 </div>
						 </c:when>
					 </c:choose>

	               	<div>
	                		<span>优惠券编号</span>
	                    	<span>${couponTender.couponCode}</span>
	               	</div>
               		<div>
                			<span>支付时间</span>
                    		<span>${couponTender.repayTime}</span>
               		</div>
                </section>
                <section class="new-form-item  bg_white my-invest-item">


					<c:choose>
						<c:when test="${type == '1'}">
							<div>
								<span>待收收益(元)</span>
								<span>${couponTender.interest}</span>
							</div>
							<div>
								<span>计划应还时间</span>
								<span>${couponTender.recoverTime}</span>
							</div>
						</c:when>
						<c:otherwise>
							<div>
								<span>实际收益(元)</span>
								<span>${couponTender.interest}</span>
							</div>
							<div>
								<span>回款时间</span>
								<span>${couponTender.recoverTime}</span>
							</div>
						</c:otherwise>
					</c:choose>

                </section>

		</div>
	<script type="text/javascript" src="${ctx}/js/jquery.min.js" ></script>
   <script type="text/javascript">
   $(".format-num").each(function(){
   	var _self = $(this);
   	_self.text(formatNum(_self.text()))
   })
   
   	function formatNum(strNum) {
		//return strNum;
		if (strNum.length <= 3) {
			return strNum;
		}
		if (!/^(\+|-)?(\d+)(\.\d+)?$/.test(strNum)) {
			return strNum;
		}
		var a = RegExp.$1, b = RegExp.$2, c = RegExp.$3;
		var re = new RegExp();
		re.compile("(\\d)(\\d{3})(,|$)");
		while (re.test(b)) {
			b = b.replace(re, "$1,$2$3");
		}
		return a + "" + b + "" + c;
	}
   </script>
   <script type="text/javascript">
		$('.profitimg-btn').click(function(){
			$('.profit-formula').fadeIn().delay(5000).fadeOut();
		})
		
		$('.toggle-list').click(function(){
			$('#cyxmList').toggle(0);
			if($("#cyxmList").css("display")=="none"){
				$(this).text("展开");
			}else{
				$(this).text("收起");
			}
		})
	</script>
   
    </body>
</html>