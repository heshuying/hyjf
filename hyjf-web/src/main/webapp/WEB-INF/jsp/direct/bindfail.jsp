<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>投资失败 - 汇盈金服官网</title>
	    <jsp:include page="/head.jsp"></jsp:include>
	</head>
	<body>
	<jsp:include page="/header.jsp"></jsp:include>
		<div class="touzi_bg">
			<div class="TZs_ban biankuang">
				<div class="touziDBan TZs_banner">
					<div class="TZs_left"><img src="${cdn}/img/fault.jpg"></div>
				    <div class="TZs_right TZd_right">
				    	<span>定向转账开通失败！</span>
				    	<p>失败原因：${message} <br>
				    	<a href="${ctx}/user/pandect/pandect.do" >返回重试  &gt;&gt;</a>
				    	</p>
				    </div>
				</div>
			</div>
		<div class="s_bot">
			<p>&copy;汇盈金服 All rights reserved| 惠众商务顾问 (北京) 有限公司 | 京ICP备 13050958 号 | 公安安全备案证：37021313127</p>
		</div>
		</div>
		<jsp:include page="/footer.jsp"></jsp:include>	
	</body>
</html>
