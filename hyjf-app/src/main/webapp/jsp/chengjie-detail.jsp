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
					<h5>HXD160554244525</h5>
					<div class="my-invest-header-div">
                		<span>历史年回报率<i class="hyjf-color">15.00<span class="font13">%</span></i></span>
                    	<span>期限<i class="hyjf-color">41</i>天</span>
               		</div>
				</section>
                <section class="new-form-item  bg_white my-invest-item">
                	<div>
                		<span>投资本金(元)</span>
                    	<span class="hyjf-color format-num">10000.00</span>
               		</div>
               		<div>
                		<span>折让率</span>
                    	<span>1.0%</span>
               		</div>
               		<div>
                		<span>垫付利息(元)</span>
                    	<span>1.01</span>
               		</div>
               		<div>
                		<span>实际支付(元)</span>
                    	<span class="hyjf-color format-num">1001.10</span>
               		</div>
               		<div>
                		<span>支付时间</span>
                    	<span>2016-06-12</span>
               		</div>
                </section>
                <section class="new-form-item  bg_white my-invest-item">
                	<div>
                		<span>到期预估本息(元)</span>
                    	<span class="hyjf-color format-num">1001.10</span>
               		</div>
               		<div>
                		<span>到期预估收益(元)</span>
                    	<span>1.10</span>
               		</div>
               		<div>
                		<span>累计到账金额(元)</span>
                    	<span>1.10</span>
               		</div>
               		<div>
                		<span>最近应还时间</span>
                    	<span>2016-06-12</span>
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