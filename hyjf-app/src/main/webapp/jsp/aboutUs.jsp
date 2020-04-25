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
		<link rel="stylesheet" type="text/css" href="${ctx}/css/main.css"/>
		<link rel="stylesheet" href="${ctx}/css/font-awesome.min.css">
		<title>关于我们</title>
	</head>
	<body class="about-us-body">
		<input type="hidden" name="jumpcommend" id="jumpcommend" value="${jumpcommend}" />
		<input type="hidden" id="version" name="version" value="${version}" />
		<div class="specialFont response" >
			<!-- 累计成交开始 -->
				<div class="hyjf-logo">
					<img src="${ctx}/img/hyjf_logo.png?v=20171123" alt="汇盈金服"  style="width:178px"/>
					<p style="text-align:center;margin-top:10px;font-size: 12px;">美国上市股票代码：SFHD</p>
				</div>
				<div class="about_deal" id="about_deal">
				</div>
				<div class="about-us-contact" >
					<ul>
						<li class="contact-us-abstract contact-us-abstract1"><a href=''>公司简介<span>&gt;</span></a></li>
						<li class="contact-us-abstract contact-us-abstract2"><a href=''>资质文件<span>&gt;</span></a></li>
						<li class="contact-us-abstract contact-us-abstract3"><a href=''>平台数据<span>&gt;</span></a></li>
						<li class="contact-us-abstract contact-us-abstract4"><a href=''>运营报告<span>&gt;</span></a></li>
					</ul>
				</div>
				<div class="about-us-contact" style="margin-top:20px">
					<ul>
						<li class="contact-us-abstract contact-us-abstract5"><a href=''>联系我们<span>&gt;</span></a></li>
						<li>客服电话<span><a style="font-size:14px;" class="colorBlue getContactNumber"></a></span></li>
					</ul>				</div>
				<span class="fr">客服工作时间： 9:00 - 18:00</span>
		</div>
		<script src="${ctx}/js/doT.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="${ctx}/js/jquery.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="${ctx}/js/common.js" type="text/javascript" charset="utf-8"></script>
		<script src="${ctx}/js/hyjf.js" type="text/javascript" charset="utf-8"></script>
		<script src="${ctx}/js/fill.js" type="text/javascript" charset="utf-8"></script>
		<script id="tmpl-data" type="text/x-dot-template">
			<div class="earnUl">
				<ul class="news_li">
					<!--<marquee direction="up" behavior="scroll" scrollamount="2" scrolldelay="100" loop="-1"  height="56"  >-->
						<li class="font42">累计成交<span>&nbsp;{{=it.totalSum}}&nbsp;</span>元</li>
						<li class="font42">累计赚取<span>&nbsp;{{=it.totalInterest}}&nbsp;</span>元</li>
						<li class="font42">风险保证金<span>&nbsp;{{=it.bailTotal}}&nbsp;</span>元</li>
					<!--</marquee>-->
					
				</ul>
				<ul class="swap"></ul>
			</div>
		</script>
		<script>
		$.fillTmplByAjax("/hyjf-app/homepage/getTotalStatics",null, "#about_deal", "#tmpl-data");
		</script>
		<script src="${ctx}/js/jq22.js" type="text/javascript" charset="utf-8"></script>
		<script>
			$.ajax({
				type:"get",
				data:null,
				dataType:"json",
				url:"${ctx}/homepage/getServicePhoneNumber",
				success:function(data){
					var dataNumber = data.servicePhoneNumber;
					if(!dataNumber){$(".getContactNumber").parent().parent().hide()}
					var num1 = dataNumber.substr(0,3);
					var num2 = dataNumber.substr(3,3);
					var num3 = dataNumber.substr(6);
					var num = num1+"-"+num2+"-"+num3
					$(".getContactNumber").prop("href",hyjfArr.hyjf+'://callCenter/?{"number":"'+num+'"}')
					.text(num1+"-"+num2+"-"+num3)
				}
			})
		</script>
	</body>
</html>