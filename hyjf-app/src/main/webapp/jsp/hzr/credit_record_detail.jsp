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
		<title>转让详情</title>
	</head>
	<body class="bg_grey">
		<div class="specialFont  cal-main">
				<section class="new-form-item  bg_white my-invest-header">
					<h5>${tenderCreditRecordInfo.bidNid}</h5>
					<div class="my-invest-header-div">
                		<span>历史年回报率<i class="hyjf-color">${tenderCreditRecordInfo.bidApr}<span class="font13">%</span></i></span>
                    	<span>期限<i class="hyjf-color">${tenderCreditRecordInfo.creditTerm}</i>天</span>
               		</div>
				</section>
                <section class="new-form-item  bg_white my-invest-item">
                	<div>
                		<span>转让本金(元)</span>
                    	<span class="hyjf-color format-num">${tenderCreditRecordInfo.creditCapital}</span>
               		</div>
               		<div>
                		<span>本金折让率</span>
                    	<span>${tenderCreditRecordInfo.creditDiscount}%</span>
               		</div>
               		<div>
                		<span>本金转让价格(元)</span>
                    	<span class="hyjf-color format-num">${tenderCreditRecordInfo.creditPrice}</span>
               		</div>
               		
                </section>
                <section class="new-form-item  bg_white my-invest-item">
                	<div>
                		<span>累计转让</span>
                    	<span class="format-num">${tenderCreditRecordInfo.creditCapitalAssigned}(${tenderCreditRecordInfo.borrowSchedule}%)</span>
               		</div>
               		<div>
                		<span>服务费(元)</span>
                    	<span class="format-num">${tenderCreditRecordInfo.creditFee}</span>
               		</div>
                	<div>
                		<span>实际到账金额(元)</span>
                    	<span class="hyjf-color format-num">${tenderCreditRecordInfo.assignPay}</span>
               		</div>
               		<div>
                		<span>包括收益(元)</span>
                    	<span class="hyjf-color format-num">${tenderCreditRecordInfo.creditInterestAssigned}</span>
               		</div>
               		<div>
               			<a href="${tenderCreditRecordInfo.transferDetailUrl}">
               			<span class="text_blue">查看转让明细</span>
						<span class="hzr-arrow-right"> &gt;</span>
						</a>
               		</div>
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
    </body>
</html>