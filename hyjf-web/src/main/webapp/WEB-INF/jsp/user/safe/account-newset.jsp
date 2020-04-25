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
	    	<div class="container relative">
	    		<!-- start 内容区域 -->
	    		<div class="account-newset">
	    			<div class="top">消息通知</div>
	    			<div class="main">
	    				<form action="" id="newsetForm" method="post">
	    					<div class="mes-set">短信设置</div>
	    					<ul class="ui-list">
	    						<li class="ui-list-title">
	    							<span class="text">充值成功</span>
	    							<span class="right">
	    								<div class="magic-checkbox">
	    									<a class="xuanze <c:if test='${user.rechargeSms==0}'>bg</c:if>" href="javascript:void(0)">
	    										<input type="checkbox" id="rechargeSms" class="checkbox" <c:if test='${user.rechargeSms==0}'>checked="checked"</c:if>>
	    											
	    									</a>
	    									<label>短信</label>
	    								</div>
	    							</span>
	    						</li>
	    						<li class="ui-list-title">
	    							<span class="text">提现成功</span>
	    							<span class="right">
	    								<div class="magic-checkbox">
	    									<a class="xuanze <c:if test='${user.withdrawSms==0}'>bg</c:if>" href="javascript:void(0)">
	    										<input type="checkbox" id="withdrawSms" class="checkbox" <c:if test='${user.withdrawSms==0}'>checked="checked"</c:if>>
	    									</a>
	    									<label>短信</label>
	    								</div>
	    							</span>
	    						</li>
	    						<li class="ui-list-title">
	    							<span class="text">出借成功</span>
	    							<span class="right">
	    								<div class="magic-checkbox">
	    									<a class="xuanze <c:if test='${user.investSms==0}'>bg</c:if>" href="javascript:void(0)">
	    										<input type="checkbox" id="investSms" class="checkbox" <c:if test='${user.investSms==0}'>checked="checked"</c:if>>
	    									</a>
	    									<label>短信</label>
	    								</div>
	    							</span>
	    						</li>
	    						<li class="ui-list-title">
	    							<span class="text">收到回款</span>
	    							<span class="right">
	    								<div class="magic-checkbox">
	    									<a class="xuanze <c:if test='${user.recieveSms==0}'>bg</c:if>" href="javascript:void(0)">
	    										<input type="checkbox" id="recieveSms" class="checkbox" <c:if test='${user.recieveSms==0}'>checked="checked"</c:if>>
	    									</a>
	    									<label>短信</label>
	    								</div>
	    							</span>
	    						</li>
	    						<li class="ui-list-set">邮件设置</li>
	    						<li class="ui-list-title">
	    							<span class="text">出借协议</span>
	    							<span class="right">
	    								<div class="magic-checkbox">
	    									<a class="xuanze <c:if test='${user.isSmtp==0}'>bg</c:if>" href="javascript:void(0)">
	    										<input type="checkbox" id="isSmtp" class="checkbox" <c:if test='${user.isSmtp==0}'>checked="checked"</c:if>>
	    									</a>
	    									<label>邮件</label>
	    								</div>
	    							</span>
	    						</li>
	    					</ul>
	    					<div class="sub">
	    						<p class="success">设置保存成功！</p>
	    						<p class="btn-set">保存设置</p>
	    					</div>
	    				</form>
	    			</div>
	    			<div class="bom">
	    				<p class="newset-text">
	    					<span class="iconfont icon-bulb"></span>
	    					 温馨提示：<span class="dark">勾选相应选项，才会收到对应的短信或邮件通知。</span>
	    				</p>
	    			</div>
	    		</div>
	    		<!-- end 内容区域 --> 
	    	</div>
	    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script>setActById('accountSet');</script>
	<script src="${cdn }/dist/js/acc-set/newset.js?version=${version}"></script>
</body>
</html>