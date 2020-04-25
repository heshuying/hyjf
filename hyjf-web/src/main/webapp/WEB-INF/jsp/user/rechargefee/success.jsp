<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>转账成功 - 汇盈金服官网</title>
		<jsp:include page="/head.jsp"></jsp:include>
	</head>
	<body>
	<jsp:include page="/header.jsp"></jsp:include>
	
	<div class="shSuccess-Top">
		    <span>充值手续费转账</span>
		</div>
		<div class="shSuccess-Ban">
			<h4>转账成功!</h4>
			<p>
				转账金额:<span class="shSuccess-cont">${balance}</span>元
		    </p>
			<div class="shSuccess-click">
			    <a id="befurl" href="javascript:void(0)" class="inputBg">返回继续</a>
			    &nbsp;&nbsp;&nbsp;&nbsp;<a href="${ctx}/bank/user/trade/initTradeList.do">查看详情 &gt;&gt;</a>
			</div>
		</div>
	<jsp:include page="/footer.jsp"></jsp:include>
    <script type="text/javascript" src="${cdn}/js/jquery.cookie.min.js" charset="utf-8"></script>
    <script>
	    $(document).ready(
	    		function() {
	    			$("#befurl").click(function(){
		    			window.location.href = webPath + "/user/pandect/pandect.do";
		    		});
	    			
	            	$("#befurl").attr("href",$.cookie("beforeUrl"));
			});
    </script>
	</body>
</html>