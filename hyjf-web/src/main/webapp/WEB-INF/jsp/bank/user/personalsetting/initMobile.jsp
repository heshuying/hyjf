<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

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
	    		<!-- start 内容区域 -->
	    		<div class="account-set-up-2">
	    			<p class="title">手机认证</p>
	    			<!-- 是否开户 -->
	    			<div class="set-content">
	    			<c:if test="${bankOpenAccount != null && bankOpenAccount.account != ''}">
	            		<!-- 已开户 -->
	            		<input type="hidden" id="is_open_account" value="1" />
					</c:if>
					<c:if test="${bankOpenAccount == null || bankOpenAccount.account == ''}">
						<!-- 未开户 -->
						<input type="hidden" id="is_open_account" value="0" />
					</c:if>
	    			
		    			<form action="" id="telForm1">
		    				<label class="no-border">
		    					<span class="tit">手机号码</span><!-- 需要掩盖4位手机号 -->
		    					<span>${hideMobile }</span>
		    					<input type="hidden" name="mobile" id="telnum1" value="${mobile }"/><!-- 添加一个往JS $('#telForm1 .get-code').click(function()传的值 -->
		    				</label>
		    				
		    				<label>
		    					<span class="tit">手机验证</span>
		    					<input type="text" name="code" maxlength="6" class="code" placeholder="请输入验证码"/>
		    					<span class="get-code"><em class="rule"></em>获取验证码</span>
		    				</label>
		    				
		    				<a class="sub">下一步</a>
		    				<div class="reminder">
		    					<h3><span class="icon iconfont icon-bulb"></span>温馨提示：<br><span>1.若修改手机号失败，可以选择其它方式修改绑定手机号，<a href="${ctx}/help/index.do?side=hp18&issure=is172">点击查看详情</a>。</span><br><span class="word">2.若手机号码停用，请联系客服解决</span></h3>
		    				</div>
		    			</form>
		    			
		    			<form action="" id="telForm2">
		    				<input type="hidden"  name="srvAuthCode" id="srvAuthCode" value=""/>
		    				<input type="hidden" name="tokenCheck" id="tokenCheck" value="" />
			                <input type="hidden" name="tokenGrant" id="tokenGrant" value="${tokenGrant}" />
		    				<p class="tips">原手机号码已通过验证，请填写您的新手机号码。</p>
		    				<label>
		    					<span class="tit">新手机号</span>
		    					<!-- 填写完新手机号后，鼠标离开，触发 验证新手机号 事件 -->
		    					<input type="text" onkeyup="value=value.replace(/[^\d]/g,'')" name="mobile" id="telnum2" placeholder="请输入新手机号"/>
		    				</label>
		    				
		    				<label>
		    					<span class="tit">手机验证</span>
		    					<input type="text" name="code" id="smsCode" maxlength="6" class="code" placeholder="请输入验证码"/>
		    					<span class="get-code"><em class="rule"></em>获取验证码</span>
		    				</label>
		    				
		    				<a class="sub">下一步</a>
		    				<div class="reminder">
		    					<h3><span class="icon iconfont icon-bulb"></span>温馨提示：<br><span>1.若修改手机号失败，可以选择其它方式修改绑定手机号，<a href="${ctx}/help/index.do?side=hp18&issure=is172">点击查看详情</a>。</span><br><span class="word">2.若手机号码停用，请联系客服解决</span></h3>
		    				</div>
		    			</form>
	    			</div>
			    </div>
	    		<!-- end 内容区域 --> 
	    	</div>
	    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script>setActById('accountSet');</script>
	<script src="${cdn }/dist/js/lib/jquery.validate.js"></script>
    <script src="${cdn }/dist/js/lib/jquery.metadata.js"></script>
	<script src="${cdn }/dist/js/acc-set/account-tel-change.js?version=${version}"></script>
</body>
</html>