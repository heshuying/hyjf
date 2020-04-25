<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>转账失败 - 汇盈金服官网</title>
	   	<%@ include file="/head.jsp"%>	
	   	<link rel="stylesheet" type="text/css" href="${cdn}/css/footer-adjust.css" />
	</head>
	<body>
	<%@ include  file="/header.jsp"%>
		<div class="touzi_bg">
			<div class="TZs_ban biankuang">
				<div class="touziDBan TZs_banner">
					<div class="TZs_left">
						<img src="${cdn}/img/fault.jpg">
					</div>
				    <div class="TZs_right TZd_right">
				    	<span>转账失败！</span>
				    	<p>${message}</p>
				    </div>
				</div>
			</div>
			<div class="s_bot">
				<p>&copy;汇盈金服 All rights reserved| 惠众商务顾问 (北京) 有限公司 | 京ICP备 13050958 号 | 公安安全备案证：37021313127</p>
			</div>
		</div>
		<%@ include  file="/footer.jsp"%>
	</body>
</html>
