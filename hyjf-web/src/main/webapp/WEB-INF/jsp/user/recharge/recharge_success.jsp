<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>充值成功 - 汇盈金服官网</title>
		<jsp:include page="/head.jsp"></jsp:include>
	</head>
	<body>
		<jsp:include page="/header.jsp"></jsp:include>
		<article class="main-content">
	        <div class="container result">
	            <div class="result-content">
	            	<div class="chongzhi-top">
	            		<img src="${cdn}/dist/images/result/chognzhi-success@2x.png" width="181" alt="" />
	            	</div>
	            	<div class="result-mid">
	            		<h3>恭喜您，充值成功！</h3>
	            		<p>您已成功向账户充值
	            			<span class="colorRed">${balance}</span>
	            		元</p>
	            	</div>
	            	<div class="result-btn">
	            		<div class="result-left">
	            			<a id="befurl" href="javascript:void(0)" class="import" itemid="bt1">返回</a>
	            		</div>
	            		<div class="result-right">
	            			<a href="${ctx}/bank/user/trade/initTradeList.do" itemid="bt2">查看详情</a>
	            		</div>
	            		 <div class="clearboth"></div>
	            	</div>
	            </div>
	        </div>
	    </article>
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