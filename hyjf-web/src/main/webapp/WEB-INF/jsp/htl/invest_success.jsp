<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>投资成功 - 汇盈金服官网</title>
	<jsp:include page="/head.jsp"></jsp:include>
		
	</head>
	<body>
	<jsp:include page="/header.jsp"></jsp:include>
		<div class="touzi_bg">
			<div class="TZs_ban biankuang">
				<div class="TZs_banner">
					<div class="TZs_left"><img src="${ctx}/img/sucess01.jpg"></div>
				    <div class="TZs_right">
				    	<span>恭喜您投资成功！</span>
				    	<span>汇天利成功转入， ${amount }元</span>
				    	<p>收益日期 : ${interestDate }
				    		<a href="${ctx}/htl/getUserProdcutInfo.do" target="_blank">查看投资详情  &gt;&gt;</a>
				    	</p>
				    	<a href="${ctx}/htl/moveToInvestPage.do" target="_blank">继续投资 </a>
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