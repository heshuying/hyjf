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
	<jsp:include page="/subMenu.jsp"></jsp:include>
	<article class="main-content">
    <div class="container result">
	<div class="account-set-up-2">
		<p class="title">设置紧急联系人</p>
		<div class="set-content">
   			<form action="" id="contactForm">
   				<label>
	   				<div class="select-box">
	   					<span class="tit">关系</span>
	   					<div class="new-form-item">
	   						<c:if test="${checkRelationName == ''}">
	   							<h4 class="new-form-input color-999">请选择 </h4>
	   						</c:if>
							<c:if test="${checkRelationName != ''}">
	   							<h4 class="new-form-input"> ${checkRelationName}</h4>
	   						</c:if>
							<div class="new-form-hdselect">
								<ul id="relationUl" for="relationId">
									<c:forEach items="${relationList}" var="contact" begin="0" step="1" varStatus="status">
										<li value="${contact.nameCd}">${contact.name}</li>
									</c:forEach>
								</ul>
							</div>
							<i class="iconfont icon-ananzuiconv265"></i>
						</div>
						<input type="hidden" name="relationId" id="relationId" value="${checkRelationId}" />
	   				</div>
	   			</label>
   				<label>
   					<span class="tit">姓名</span>
   					<input type="text" name="rlName" id="rlName" maxlength="4" placeholder="请输入联系人姓名" value="${usersContract.rlName }"/>
   				</label>
   				<label>
   					<span class="tit">电话</span>
   					<input type="text" name="rlPhone" id="rlPhone" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="11" placeholder="请输入联系人手机号" value="${usersContract.rlPhone }"/>
   				</label>
   				<a class="sub">提交</a>
   			</form>
		</div>
    </div>
    </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script>setActById('accountSet');</script>
	<script src="${cdn}/dist/js/lib/jquery.validate.js"></script>
	<script src="${cdn}/dist/js/lib/jquery.metadata.js"></script>
	<script src="${cdn}/dist/js/lib/customForm.js"></script>
	<script src="${cdn}/dist/js/acc-set/account-contact.js?version=${version}"></script>
</body>
</html>