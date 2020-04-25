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
		<title>持有详情</title>
	</head>
	<body class="bg_grey">
		<div class="specialFont  cal-main">
				<section class="new-form-item  bg_white my-invest-header">
					<h5>
					<c:if test="${repayDetail.projectType eq 13}">
					${repayDetail.borrowNid}
					</c:if>
					<c:if test="${repayDetail.projectType ne 13}">
					${repayDetail.borrowNid}
					</c:if>
					</h5>
					<div class="my-invest-header-div">
						<c:choose>
							<c:when test="${repayDetail.couponType eq '2'}">
	                			<span>加息收益率<i class="hyjf-color">${repayDetail.couponQuota}<span class="font13">%</span></i></span>
	                		</c:when>
	                		<c:when test="${repayDetail.projectType eq 13  and not empty investType and investType ne 'null'}">
	                			<span>产品加息率<i class="hyjf-color">${repayDetail.borrowExtraYield}<span class="font13">%</span></i></span>
	                		</c:when>
	                		<c:otherwise>
	                			<span>历史年回报率<i class="hyjf-color">${repayDetail.borrowApr}<span class="font13">%</span></i></span>
	                		</c:otherwise>
                		</c:choose>
                    	<span>期限<i class="hyjf-color">${repayDetail.borrowPeriod}</i>
	                    	<span class="font13">
	                    		<c:if test="${repayDetail.borrowStyle eq 'endday'}">天</c:if>
	                    		<c:if test="${repayDetail.borrowStyle ne 'endday'}">个月</c:if>
	                    	</span>
                    	</span>
               		</div>
				</section>
                <section class="new-form-item  bg_white my-invest-item">
                	<div>
                		<!-- 优惠券以及普通投资 -->
                		<c:if test="${repayDetail.couponType ne '3' and repayDetail.couponType ne '1'}">
                			<span>投资本金(元)</span>
                		</c:if>
                		<!-- 代金券 -->
                		<c:if test="${repayDetail.couponType eq '3' or repayDetail.couponType eq '1'}">
                			<span>用券金额（元）</span>
                		</c:if>
                    	<span class="hyjf-color format-num">${repayDetail.account}</span>
               		</div>
               		<!-- 收益期限 -->
               		<c:if test="${repayDetail.couponType eq '1' and !empty repayDetail.couponProfitTime}">
               		<div style="overflow:inherit;position:relative">
                		<span>收益期限<img class="profitimg-btn" src="${ctx}/img/date-help.png" alt="" style="display:inline-block"/></span>
                		<div  class="profit-formula">
		            		<div class="arrow-top"></div>
		            		<p>收益=券面值*项目年化收益率/365*收益期限</p>
		            	</div>
                    	<span>${repayDetail.couponProfitTime }天</span>
               		</div>
               		</c:if>
               		<c:if test="${repayDetail.couponUserCode != null and repayDetail.couponUserCode != '' }">
	               		<div>
	                		<span>优惠券编号</span>
	                    	<span>${repayDetail.couponUserCode}</span>
	               		</div>
               		</c:if>
               		<div>
                		<span>还款方式</span>
                    	<c:if test="${repayDetail.couponType eq '1'}">
                    		<span>按天计息，到期还本还息</span>
                    	</c:if>
                    	<c:if test="${repayDetail.couponType ne '1'}">
                    		<span>${repayDetail.repayStyle}</span>
                    	</c:if>
               		</div>
               		<div>
                		<span>支付时间</span>
                    	<span>${repayDetail.payTime}</span>
               		</div>
                </section>
                <section class="new-form-item  bg_white my-invest-item">
                	
               		<c:if test="${repayDetail.projectType eq 13  and not empty investType and investType ne 'null'}">
						<div>
							<span>预期加息收益（元）</span>
                			<span class="hyjf-color format-num">${repayDetail.interest}</span>
            			</div>
					</c:if>
					<c:if test="${empty investType or investType eq 'null'}">
						<div>
                		<!-- 代金券 优惠券 -->
                		<c:if test="${repayDetail.couponType eq '1' or repayDetail.couponType eq '2' or repayDetail.couponType eq '3' }">
                			<span>预期用券收益（元）</span>
                		</c:if>
                		<c:if test="${repayDetail.couponType ne '1' and repayDetail.couponType ne '2' and repayDetail.couponType ne '3' }">
                			<span>待收本息(元)</span>
                		</c:if>
                    	<span class="hyjf-color format-num">${repayDetail.accountAll}</span>
               		</div>
               		<c:if test="${repayDetail.couponType ne '1' and repayDetail.couponType ne '2' and repayDetail.couponType ne '3' }">
	               		<div>
	                		<span>待收收益(元)</span>
	                    	<span>${repayDetail.interest }</span>
	               		</div>
               		</c:if>
               		<c:if test="${repayDetail.recoverCapitalYes !=null and repayDetail.recoverCapitalYes ne '' and repayDetail.recoverCapitalYes ne '0.00' }">
	               		<div>
	                		<span>回款金额（元）</span>
	                    	<span>${repayDetail.recoverCapitalYes }</span>
	               		</div>
               		</c:if>
					</c:if>
               		<div>
                		<span>最近应还时间</span>
                    	<span>${repayDetail.repayTime }</span>
               		</div>
                </section>
                <c:if test="${repayDetail.transStatus != 0 }">
                    <section class="new-form-item  bg_white my-invest-item">
	                	<div>
	                		<span class="hyjf-color">已转让(元)</span>
	                    	<span class="hyjf-color format-num">${repayDetail.transAccount}</span>
	               		</div>
	                </section>
                </c:if>
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
	</script>
   
    </body>
</html>