<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<title>会员付款 - 汇盈金服官网</title>
<jsp:include page="/head.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<div class="shSuccess-Top">
		<span>会员付款</span>
	</div>
	<div class="shfail-Ban shSuccess-Ban">
			<h4>付款失败</h4>
			<p>
				抱歉, 您的账户付款失败。
				<br>
				<br>
				
			<div class="shSuccess-click">
			    <a href="#" id="returnVip" class="inputBg">返回重试</a>
			</div>
		</div>
	<jsp:include page="/footer.jsp"></jsp:include>
</body>
<script src="${cdn}/js/drag.js" type="text/javascript" charset="utf-8"></script>
<script src="${cdn}/js/jquery.validate.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${cdn}/js/jquery.metadata.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
~function(){
	$("#returnVip").click(function(){
		window.location.href = webPath + "/vip/apply/init.do";
	});
}();
</script>
</html>