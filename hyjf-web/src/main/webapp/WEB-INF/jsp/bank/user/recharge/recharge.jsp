<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
<script type="text/javascript">
	var isBundCardFlag = "${isBundCardFlag}";
</script>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<article class="main-content">
	    	<div class="container result">
	    		<!-- start 内容区域 -->
	    		<div class="acount-recharge">
	    			<form <c:if test="${isBundCardFlag == 1}"> id="rechargeForm" action="${ctx}/bank/user/directrecharge/page.do"</c:if> 
	    			<c:if test="${isBundCardFlag == 0}"> id="bindCardForm" action="${ctx}/bank/web/bindCard/bindCard.do?pageFrom=recharge" </c:if> method="post">
	    				<input type="hidden" name="paymentAuthStatus" id="paymentAuthStatus" value="${paymentAuthStatus}" /> 
	    				<input type="hidden" name="paymentAuthOn" id="paymentAuthOn" value="${paymentAuthOn}" /> 
	    				<input type="hidden" name="tokenCheck" id="tokenCheck" value="" />
		                <input type="hidden" name="tokenGrant" id="tokenGrant" value="${tokenGrant}" />
		                <input type="hidden" name="rechargeType" id="rechargeType" value="0" />
		                <input type="hidden" name="cardId" id="cardId" value="${cardId}" />
		                <input type="hidden" name="isSetPassword " id='isSetPassword' value='${isSetPassword}'>
		                <input type="hidden" name="bankName" id="bankName" value="${bankName}" />
	    			
		    			<ul>
		    				<li class="avail">
		    					<div class="prompt">可用金额：</div>
		    					<div class="info">
		    						<span>${userBalance}</span>元
		    					</div>
		    				</li>
		    				<c:if test="${isBundCardFlag == 1}">
		    				<li class="type">
		    					<div class="prompt">充值方式：</div>
		    					<div class="info">
		    						<c:if test="${userType == 0 }">
		    							<a data-val=2 class="check offline" >线下充值<span></span></a>
		    							<a data-val=1 class="onlinebank">快捷充值<span></span></a>
			    						<c:if test="${isBundCardFlag == 0}">
			    							<div class="hint" style="display:none;">使用快捷充值前需要先绑定快捷银行卡</div>
			    						</c:if>
		    						</c:if>
			    					<c:if test="${userType == 1 }">
		    							<a data-val=2 class="check offline">线下充值<span></span></a>
		    						</c:if>				
		    					</div>
		    					<input type="hidden" value=1>
		    				</li>
		    				</c:if>
		    				<c:if test="${isBundCardFlag == 1 }">
		    				<input type="hidden" name="smsSeq" id="smsSeq" value="" />
			    				<li class="card" >
			    					<div class="prompt">快捷银行卡：</div>
			    					<div class="info">
			    						<div>${cardNo}</div>
			    						<c:if test="${userType == 0 }">
			    							<a href="javascript:;" class="delete-card" id="unbundling">解绑>></a>
			    						</c:if>
			    						<c:if test="${!empty singleQuota and !empty singleCardQuota and !empty monthCardQuota}">
			    							<span style="color:#C0C0C0;display:none" class="note1">限额： 单笔${singleQuota}， 单日${singleCardQuota}， 单月${monthCardQuota}</span>
			    						</c:if>
			    						<span style="color:#ff5b29;" class="note2">请务必使用该卡作为付款账户进行转账</span>
			    					</div>
			    				</li>
			    				
			    				<c:if test="${userType == 0 }">
				    				<li class="recharge" style="display:none">
				    					<div class="prompt">充值金额：</div>
				    					<div class="info acount-input"><input type="text" id = "money" placeholder="请输入金额" autocomplete="off" name="money" maxlength="7"/><em>元</em></div>
				    				</li>
				    				
				    				
				    				<li class="recharge" style="display:none">
				    					<div class="prompt">银行预留手机号：</div>
				    					<div class="info acount-input"><input type="text" maxlength="11" onkeyup="value=value.replace(/[^\d]/g,'')" value="${mobile}" placeholder="请输入银行预留手机号" autocomplete="off" name="mobile" id="phoneNum"/></div>
				    				</li>
				    			</c:if>
		    				</c:if>
		    				<c:if test="${isBundCardFlag == 0 and userType == 0 and bindType == 0}">
		    				<input type="hidden" name="lastSrvAuthCode" id="smsSeq" value="" />
	    					<li class="recharge">
		    					<div class="prompt">绑定银行卡：</div>
		    					<div class="info acount-input">
		    						<input id="card" name="cardNo" type="text" placeholder="请输入银行卡号" autocomplete="off"  oninput="if(value.length>19)value=value.slice(0,19)" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="19">
		    					</div>
		    				</li>
		    				<li class="recharge">
		    					<div class="prompt">银行预留手机号：</div>
		    					<div class="info acount-input">
		    						<input name="mobile" id="mobile" type="text" onkeyup="value=value.replace(/[^\d]/g,'')" placeholder="请输入银行预留手机号" autocomplete="off"  maxlength="11">
		    					</div>
		    				</li>
		    				<li class="recharge" style="display: list-item;">
		    					<div class="prompt">验证码：</div>
		    					<div class="info acount-input"><input type="text" placeholder="请输入手机验证码" autocomplete="off" maxlength="6" name="smsCode" id="code"><span class="get-code"><em class="rule"></em>获取验证码</span></div>
		    				</li>
		    				</c:if>
		    				<c:if test="${isBundCardFlag == 1}">
		    				<li class="offline" <c:if test="${userType == 1 }">style="display: block" </c:if>>
								<div class="prompt">网银转账：</div>
		    					<div class="info"> 
		    						收款户名：${rcvAccountName}
 									收款帐号：${rcvAccount}
 									收款银行：${rcvOpenBankName}
									<span class="color999">注：</span><br>
									<span class="color999">①银行转账时，请选择（城市商业银行）江西银行或者南昌银行；</span><br>
									<span class="color999">②线下充值在工作日17:00前完成操作，当日到账，否则资金最晚将于下个工作日到账；</span>
		    					</div>
		    				</li>
		    				</c:if>
		    			</ul>
		    			
		    			<c:if test="${isBundCardFlag == 1 and userType == 0}">
		    				<a class="sub" style="display:none">立即充值</a>
		    			</c:if>
		    				
		    			<c:if test="${isBundCardFlag == 0 and userType == 0}">
		    				<a class="sub bindsub">绑定</a>
		    			</c:if>

						<c:if test="${isBundCardFlag == 0 and userType == 0 and bindType == 1}">
							<a class="sub"  style="display:none" href="${bindCardUrl}?urlstatus=recharge">绑定</a>
						</c:if>
		    			
	    			</form>
	    			<div class="reminder">
	    				<h2>温馨提示</h2>
	    				<ul class="reminder-list">
	    					<li>1. 充值前，须先确认已绑定银行卡，若未绑定银行卡，请先绑定本人银行卡（仅支持借记卡）；</li>
		    				<li>2. 快捷充值须先开通“银联在线支付”功能，若充值时页面提示“错误代码CI77”，则表示未开通该功能；</li>
		    				<li>3. 快捷充值单日充值笔数上限为40笔；</li>
							<li>4. 线下充值使用网银转账时，付款账户须与平台绑定银行卡一致，不支持非平台绑定银行卡的网银转账充值功能，即“同卡进出”原则；</li>
							<li>5. 不支持支付宝、微信等第三方支付平台的转账充值功能；</li>
							<li>6. 线下充值不符合“同卡进出”原则的，充值资金最长T+1工作日会被退回至付款账户；</li>
							<li>7. 解绑银行卡的前提是，当前账户余额为“0”，并且无待收投资和待还借款，如不符合前述条件，<br/>&nbsp;&nbsp;&nbsp;请联系客服申请解绑，客服热线：400-900-7878；</li>
							<li>8. 若要换绑银行卡，可先解绑再绑定新的银行卡或 <a target="_blank" href='${ctx}/help/index.do?side=hp18&issure=is173'>提交修改申请表</a>；</li>
							<li>9. 支付限额请参照 <a target="_blank" href="${ctx}/bank/web/user/recharge/rechargeQuotaLimit.do">支付说明</a>。</li>
						</ul>
	    			</div>
	    		</div>
	    		<!-- end 内容区域 --> 
	    	</div>
	    </article>
	    <script type="text/javascript">
		document.cookie = 'authPayUrl='+window.location.href+';path=/'
    </script>
    <!-- 缴费授权返回地址 -->
    <div class="alert" id="authInvesPop" style="margin-top: -154.5px;width:350px;display: none;">
    	<div onclick="utils.alertClose('authInvesPop')" class="close">
    		<span class="iconfont icon-cha"></span>
    	</div>
    	<div class="icon tip"></div>
    	<div class='content prodect-sq'>
    		
    	</div>
    	<div class="btn-group">
    		<div class="btn btn-primary single" id="authInvesPopConfirm">立即开通</div>
    	</div>
    </div>
    <div class="alert" id="setPassword" style="margin-top: -154.5px;width:350px;display: none;">
    	<div class="icon tip"></div>
    	<div class='content prodect-sq'>
    		
    	</div>
    	<div class="btn-group">
    		<div class="btn btn-primary single" id="setPasswordConfirm">立即开通</div>
    	</div>
    </div>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="${cdn}/dist/js/lib/jquery.validate.js"></script>
   	<script src="${cdn}/dist/js/lib/jquery.metadata.js"></script>
    <script type="text/javascript" src="${cdn}/js/bank/user/recharge/recharge.js?version=${version}" charset="utf-8"></script>
    <script type="text/javascript" src="${cdn}/js/jquery.cookie.min.js" charset="utf-8"></script>
</body>
</html>