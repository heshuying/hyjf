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
			<div class="account-set-up-2">
				<p class="title">开通银行存管账户</p>
				<div class="set-content">
					<form action="${ctx}/bank/web/user/bankopenencrypt/open.do" method="post" id="bankOpenForm">
						<label>
	    					<span class="tit">姓名</span>
	    					<input type="text" name="trueName" id="trueName" class="kaihu-input" placeholder="请输入真实姓名" oninput="if(value.length>20)value=value.slice(0,20)"/>
	    				</label>
	    				<label>
	    					<span class="tit">手机号</span>
	    					<input type="text" placeholder="请输入银行卡的预留手机号" name="mobile" id="mobile" class="kaihu-input <c:if test="${not empty mobile}">ignore</c:if>" value="${mobile}"  <c:if test="${not empty mobile}"> value="${mobile }"  </c:if> placeholder="请输入手机号" oninput="if(value.length>11)value=value.slice(0,11)" />
	    				</label>
						<a class="kh-message" style="color:#999;white-space:nowrap">*请确保该手机号与开户绑定银行卡的预留手机号一致。</a>
						<div class="kh-item">
							<input type="submit" value="出借人开户" class="kaihu-btn" />
						</div>
						<a class="otherRole" href="/bank/web/user/loanbankopen/init.do">我是借款人 ></a>
					</form>
				</div>
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