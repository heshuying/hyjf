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
		<title></title>
	</head>
	<body>
		<input type="hidden" name="jumpcommend" id="jumpcommend" value="${jumpcommend}" />
		<div class="container specialFont response">
			<div class="outcome">
				<div class="outcome_icon"><i class="icon-inprocess text_orange"></i></div>
				<h5>${message }</h5>
			</div>
			<div class="process">
				<div class="process_line btn_bg_color"><a href="#" class="hy-closeView">完成</a></div>
				<div class="process_line btn_bg_color"><a href="" class="getContactNumber">联系客服</a></div>
			</div>
		</div>
		<script src="${ctx}/js/zepto.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="${ctx}/js/common.js" type="text/javascript" charset="utf-8"></script>
		<script src="${ctx}/js/hyjf.js" type="text/javascript" charset="utf-8"></script>
		<script>
			$.ajax({
				type:"get",
				data:null,
				dataType:"json",
				url:"${ctx}/homepage/getServicePhoneNumber",
				success:function(data){
					var dataNumber = data.servicePhoneNumber;
					var num1 = dataNumber.substr(0,3);
					var num2 = dataNumber.substr(3,3);
					var num3 = dataNumber.substr(6);
					$(".getContactNumber").prop("href",hyjfArr.hyjf+'://callCenter/?{"number":"'+data.servicePhoneNumber+'"}')
					
				}
			})
		</script>
	</body>
</html>
