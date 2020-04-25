<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<title>债权转让购买成功 - 汇盈金服官网</title>
<jsp:include page="/head.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<div class="clearfix"></div>
	<div class="touzi_bg">
		<div class="clearfix"></div>
		<div class="TZs_ban biankuang">
			<div class="clearfix"></div>
			<div class="shSuccess-Ban">
				<h4>${message}</h4>
				<p>
					<strong>汇转让</strong>项目编号：HZR${creditTender.creditNid } 成功购买
					<fmt:formatNumber value="${creditTender.assignCapital }"
						pattern="#,#00.00" />
					元
				</p>
				<div class="shSuccess-click">
					<a href="${ctx}/credit/webcreditlist.do">继续购买</a> <a
						href="${ctx}/bank/user/trade/initTradeList.do">查看交易明细</a>
				</div>
			</div>

		</div>
		<div class="s_bot">
			<p>&copy;汇盈金服 All rights reserved| 惠众商务顾问 (北京) 有限公司 | 京ICP备
				13050958 号 | 公安安全备案证：37021313127</p>
		</div>
	</div>
	<jsp:include page="/footer.jsp"></jsp:include>
</body>
</html>