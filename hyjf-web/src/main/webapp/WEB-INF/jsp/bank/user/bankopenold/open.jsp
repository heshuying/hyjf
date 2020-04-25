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
				<form action="" method="get" id="bankOpenForm">
					<input id="logOrderId" name="logOrderId" type="hidden" value="${logOrderId }">
					<div class="kaihu-cont">
						<div class="kh-item">
							<lable for="trueName" class="kh-lable">姓名</lable>
							<input type="text" name="trueName" id="trueName" class="kaihu-input" placeholder="请输入真实姓名" oninput="if(value.length>20)value=value.slice(0,20)"/>
						</div>
						<div class="kh-item">
							<lable for="idNo" class="kh-lable">身份证</lable>
							<input type="text" name="idNo" id="idNo" class="kaihu-input" placeholder="身份证号应与姓名保持一致" oninput="if(value.length>18)value=value.slice(0,18)" />
						</div>
						<div class="kh-item">
							<lable for="cardNo" class="kh-lable">银行卡</lable>
							<input type="text" name="cardNo" id="cardNo" class="kaihu-input" placeholder="银行卡应与姓名保持一致" oninput="if(value.length>19)value=value.slice(0,19)" />
						    <a class="kh-message" target="_blank" href="${ctx}/contentarticle/getSecurityPage.do?pageType=recharge-limit">支持快捷充值银行卡</a>
						</div>
						<div class="kh-item">
							<lable for="mobile" class="kh-lable">手机号</lable>
							<input type="text" name="mobile" id="mobile" class="kaihu-input <c:if test="${not empty mobile}">ignore</c:if>" value="${mobile}"  <c:if test="${not empty mobile}"> readonly="readonly" value="${mobile }"  style="color:#535154;border-color: #d6d6d6;" </c:if> placeholder="请输入手机号" oninput="if(value.length>11)value=value.slice(0,11)" />
						</div>
						<div class="kh-item kh-yz">
							<lable for="smsCode" class="kh-lable">验证码</lable>
							<input type="text" name="smsCode" id="smsCode" class="yz-input kaihu-input" placeholder="请输入验证码" oninput="if(value.length>6)value=value.slice(0,6)" />
							<input type="button" value="获取" class="get-btn" />
						</div>
						<div class="kh-item kh-agree">
							<input type="checkbox" name=bankLow id="bankLow" checked="checked" class="check-btn" />
							<label class="kaihu-agree" for="bankLow">我已阅读并同意<a href="${ctx}/agreement/goAgreementPdf.do?aliasName=khxy" target="_blank">${khxy }</a> <a href="${ctx}/agreement/goAgreementPdf.do?aliasName=yhsqxy" target="_blank">${yhsqxy }</a></label>
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
	<script type="text/javascript" src="${cdn}/js/bank/user/bankopen/open-old.js?version=${version}" charset="utf-8"></script>
</body>
</html>