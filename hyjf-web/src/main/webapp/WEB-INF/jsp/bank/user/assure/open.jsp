<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<article class="main-content">
		<div class="container result">
			<!-- start 内容区域 -->
			<div class="fxcp-title">
				开通银行存管账户
			</div>
			<div class="kaihu-tabs">
				<form action="${ctx}/bank/web/user/assurebankopen/open.do" method="post" id="bankOpenForm">

					<div class="kaihu-cont">
						<div class="kh-item">
							<lable for="trueName" class="kh-lable">姓名</lable>
							<input type="text" name="trueName" id="trueName" class="kaihu-input" placeholder="请输入真实姓名" oninput="if(value.length>20)value=value.slice(0,20)"/>
						</div>
						<div class="kh-item">
							<lable for="mobile" class="kh-lable">手机号</lable>
							<input type="text" placeholder="请输入银行卡的预留手机号" name="mobile" id="mobile" class="kaihu-input <c:if test="${not empty mobile}">ignore</c:if>" value="${mobile}"  <c:if test="${not empty mobile}"> value="${mobile }"  </c:if> placeholder="请输入手机号" oninput="if(value.length>11)value=value.slice(0,11)" />
						</div>
						<div class="kh-item">
							<input type="submit" value="开通银行存管账户" class="kaihu-btn" />
						</div>
					</div>
				</form>
			</div>
			<!-- end 内容区域 -->
		</div>
	</article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="${cdn}/js/drag.js" type="text/javascript" charset="utf-8"></script>
	<script src="${cdn}/dist/js/lib/jquery.validate.js"></script>
	<script src="${cdn}/dist/js/lib/jquery.metadata.js"></script>
	<script type="text/javascript" src="${cdn}/js/bank/user/bankopen/open.js?version=${version}" charset="utf-8"></script>
</body>
</html>