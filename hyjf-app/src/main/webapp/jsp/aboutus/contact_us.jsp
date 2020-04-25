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
		<title>联系我们</title>
	</head>
	<body class="about-us-body">
	<input type="hidden" name="jumpcommend" id="jumpcommend" value="${jumpcommend}" />
		<div class="specialFont response" >
			<!-- 累计成交开始 -->
				<div class="about-us-contact" style="margin:0 0 15px 0">
					<ul>
						<li>客服电话<span style="font-size:14px"><a href="" style="font-size:14px" class="colorBlue getContactNumber"></a>(9:00-18:00)</span></li>
						<li>客服邮箱<span>info@hyjf.com</span></li>
						<li>微信公众号<span>汇盈金服</span></li>
						<li>官方QQ群<span>97756631</span></li>
					</ul>
				</div>
				<div class="about-us-contact" >
					<ul>
						<li>公司电话（总部）<span><a href="#" style="font-size:14px;" class="colorBlue hy-callCener">021-23570077</a></span></li>
						<li class="about-contact-li" style="height:60px !important">公司地址<span style="text-align:right;line-height:20px;margin-top:10px">上海市长宁区延安西路2299号世贸商城2401-2404室</span></li>
					</ul>
				</div>
		</div>
		<script src="${ctx}/js/doT.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="${ctx}/js/jquery.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="${ctx}/js/common.js" type="text/javascript" charset="utf-8"></script>
		<script src="${ctx}/js/hyjf.js" type="text/javascript" charset="utf-8"></script>
		<script src="${ctx}/js/fill.js" type="text/javascript" charset="utf-8"></script>
		<script>
		$.fillTmplByAjax("/hyjf-app/homepage/getTotalStatics",null, "#about_deal", "#tmpl-data");
		</script>
		<script src="${ctx}/js/jq22.js" type="text/javascript" charset="utf-8"></script>
		<script>
		callCenter("02123570077")
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
					var num = num1+"-"+num2+"-"+num3;
					$(".getContactNumber").prop("href",hyjfArr.hyjf+'://callCenter/?{"number":"'+num+'"}')
					.text(num1+"-"+num2+"-"+num3)
				}
			})
		</script>
	</body>
</html>