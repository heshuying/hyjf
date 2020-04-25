<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="/head.jsp"></jsp:include>
<title>提现确认 - 汇盈金服官网</title></head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<article class="main-content">
	    	<div class="container result">
	    		<!-- start 内容区域 -->
	    		<div class="account-set-up-2">
	    			<p class="title">提现</p>
	    			<div class="set-content">
		    			<form action="${ctx}/bank/web/withdraw/cash.do" id="telForm1">
			    			<input type="hidden" name="tokenCheck" id="tokenCheck" value="" /> 
							<input type="hidden" name="tokenGrant" id="tokenGrant" value="${tokenGrant}" />
							<input type="hidden" name="isSetPassWord" id="isSetPassWord" value="${isSetPassWord}" />
							<input type="hidden" name="total" id="total" value="${total}" />
							<input type="hidden" name="bankType" id="bankType" value="${bankType}"/>
							<input type="hidden" name="withdrawmoney" id="withdrawmoney" value="${withdrawmoney}" />
							<input type="hidden" name="widCard" id="widCard" value="${widCard}" />
							<input type="hidden" name="payAllianceCode" id="payAllianceCode" value="${payAllianceCode}" />
		    				<label class="no-border">
		    					<span class="tit">手机号</span>
		    					<span>${phoneNum }</span>
		    					<input type="hidden" name="newRegPhoneNum" id="telnum1" value="${phoneNum }"/>
		    					<input type="hidden" name="validCodeType" id="validCodeType" value="TPL_SMS_WITHDRAW"/>
		    				</label>
		    				<label>
		    					<span class="tit">手机验证</span>
		    					<input type="text" name="withdrawCode" maxlength="8" class="code" placeholder="请输入验证码"/>
		    					<span class="get-code"><em class="rule"></em>获取验证码</span>
		    				</label>
		    				<a class="sub">申请提现</a>
		    				<div style="left:0" class="reminder">
		    					<h3><span class="icon iconfont icon-bulb"></span>温馨提示：<span class="word">若手机号码停用，请联系客服解决</span></h3>
		    				</div>
		    			</form>
	    			</div>
			    </div>
	    		<!-- end 内容区域 --> 
	    	</div>
	    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="${cdn}/js/jquery.validate.js" type="text/javascript" charset="utf-8"></script>
	<script src="${cdn}/js/jquery.metadata.js" type="text/javascript" charset="utf-8"></script>
	<script src="${cdn}/dist/js/acc-set/withdraw-confirm.js" type="text/javascript" charset="utf-8"></script>
</body>
</html>