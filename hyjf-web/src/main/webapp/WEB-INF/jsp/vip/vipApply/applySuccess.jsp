<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<title>会员付款 - 汇盈金服官网</title>
<jsp:include page="/head.jsp"></jsp:include>>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<div class="shSuccess-Top">
		<span>会员付款</span>
	</div>
	<div class="shSuccess-Ban">
		<h4>付款成功！</h4>
		<p style="white-space:nowrap;">
			<strong>恭喜您已成功加入汇盈会员Club欢迎礼包已飞入您的账户</strong> 
	    </p>
		<div class="shSuccess-click">
			<a href="#" id="vipDetail" class="inputBg">立即查看</a>
		</div>
	</div>
	<jsp:include page="/footer.jsp"></jsp:include>
</body>
<script src="${cdn}/js/drag.js" type="text/javascript" charset="utf-8"></script>
<script src="${cdn}/js/jquery.validate.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${cdn}/js/jquery.metadata.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
~function(){
	$("#vipDetail").click(function(){
		window.location.href = webPath + "/coupon/getUserCoupons.do";
	});
}();
</script>
</html>