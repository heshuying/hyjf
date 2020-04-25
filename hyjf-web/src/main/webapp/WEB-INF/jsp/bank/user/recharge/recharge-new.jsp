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
	<jsp:include page="/subMenu.jsp"></jsp:include>
	<article class="main-content">
	    	<div class="container result">
	    		<!-- start 内容区域 -->
	    		<c:if test="${userType == 0 }">
		    		<div class="recharge-tab">
		    			<div class="tab select">转账充值</div>
		    			<div class="tab">快捷充值</div>
		    		</div>
		    	</c:if>
		    	<c:if test="${userType == 1 }">
					<div class="recharge-tab qiye">
		    			<div >转账充值</div>
		    		</div>
				</c:if>	
				<div class="acount-recharge">
					<c:if test="${isBundCardFlag == 1}"> 
	    			<div class="quick-new">
	    				<div class="notes">请务必使用您尾号${fn:substring(cardNo, fn:length(cardNo)-4, fn:length(cardNo))}的${bank}储蓄卡，向以下账户转入资金。转入后资金将直接到达您在汇盈金服的账户余额中。</div>
	    				<p><img src="${ctx }/dist/images/account/bank@2x.png" />江西银行存管账户信息：</p>
	    				<dl>
	    					<dt>
	    						<div class="e1">收款户名</div>
	    						<div class="e2">收款账号</div>
	    						<div class="e3">收款开户行</div>
	    					</dt>
	    					<dd>
	    						<div class="e1">${rcvAccountName}</div>
	    						<div class="e2">${rcvAccount}</div>
	    						<div class="e3">${rcvOpenBankName}</div>
	    					</dd>
	    				</dl>
	    			</div>
	    			</c:if>
	    			<c:if test="${isBundCardFlag == 0}"> 
	    				<div class="quick-new" >
		    				<a href="${ctx }/bank/web/bindCard/bindCardNew.do" class="go-bindCard"><img src="${ctx }/dist/images/account/bindCard.png" style="width:300px;margin-top:40px" /></a>
		    			</div>
	    			</c:if>
	    			<div class="prompt-recharge-new">
    					<div class="title"><span class="icon iconfont icon-bulb" style="padding-right: 7px;"></span>温馨提示</div>
	    				<p>1.&nbsp;&nbsp;转账充值使用网银转账时，付款账户须与平台绑定银行卡一致，不支持非平台绑定银行卡的网银转账充值功能，即“同卡进出”原则；<br />
2.&nbsp;&nbsp;银行转账时，请选择（城市商业银行）江西银行或者南昌银行；<br />
3.&nbsp;&nbsp;转账充值在工作日17:00前完成操作，当日到账，否则资金最晚将于下个工作日到账；<br />
4.&nbsp;&nbsp;转账充值不符合“同卡进出”原则的，充值资金最长T+1工作日会被退回至付款账户；<br />
5.&nbsp;&nbsp;不支持支付宝、微信等第三方支付平台的转账充值功能。</p>
	    			</div>
	    		</div>
	    		<div class="acount-recharge" style="display: none;">
	    			<c:if test="${isBundCardFlag == 1}">
	    			<form  id="rechargeForm" action="${ctx}/bank/user/directrecharge/page.do" method="post">
	    				<input type="hidden" name="paymentAuthStatus" id="paymentAuthStatus" value="${paymentAuthStatus}" /> 
	    				<input type="hidden" name="tokenCheck" id="tokenCheck" value="" />
		                <input type="hidden" name="tokenGrant" id="tokenGrant" value="${tokenGrant}" />
		                <input type="hidden" name="rechargeType" id="rechargeType" value="0" />
		                <input type="hidden" name="cardId" id="cardId" value="${cardId}" />
		                <input type="hidden" name="isSetPassword " id='isSetPassword' value='${isSetPassword}'>
		                <input type="hidden" name="paymentAuthOn " id='paymentAuthOn' value='${paymentAuthOn}'>
		                <input type="hidden" name="bankName" id="bankName" value="${bank}">
		                <input type="hidden" name="roleId" id="roleId" value="${roleId}" />
		    			<ul>
		    				<li class="avail">
		    					<div class="prompt">可用金额：</div>
		    					<div class="info">
		    						<span>${userBalance}</span>元
		    					</div>
		    				</li>
		    				<c:if test="${isBundCardFlag == 1 }">
		    				<input type="hidden" name="smsSeq" id="smsSeq" value="" />
			    				<li class="card" >
			    					<div class="prompt">${bank}：</div>
			    					<div class="info">
			    						<div>${cardNo}</div>
			    						<c:if test="${!empty concatAllQuota}">
			    							<span style="color:#C0C0C0;" class="note1">限额： ${concatAllQuota }</span>
			    						</c:if>
			    					</div>
			    				</li>
			    				
			    				<c:if test="${userType == 0 }">
				    				<li class="recharge" >
				    					<div class="prompt">充值金额：</div>
				    					<div class="info acount-input"><input type="text" id = "money" placeholder="请输入金额" autocomplete="off" name="money" maxlength="7"/><em>元</em></div>
				    				</li>
				    				
				    				
				    				<li class="recharge">
				    					<div class="prompt">银行预留手机号：</div>
				    					<div class="info acount-input"><input type="text" maxlength="11" onkeyup="value=value.replace(/[^\d]/g,'')" value="${mobile}" placeholder="请输入银行预留手机号" autocomplete="off" name="mobile" id="phoneNum"/></div>
				    				</li>
				    			</c:if>
		    				</c:if>
		    			</ul>
		    			
		    			<c:if test="${isBundCardFlag == 1 and userType == 0}">
		    				<a class="sub">立即充值</a>
		    			</c:if>
		    			
	    			</form>
	    			</c:if>
	    			<c:if test="${isBundCardFlag == 0}"> 
	    				<div class="quick-new" >
		    				<a href="${ctx }/bank/web/bindCard/bindCardNew.do" class="go-bindCard"><img src="${ctx }/dist/images/account/bindCard.png" style="width:300px;margin-top:40px" /></a>
		    			</div>
	    			</c:if>
	    			<div class="prompt-recharge-new">
		    				<div class="title"><span class="icon iconfont icon-bulb" style="padding-right: 7px;"></span>温馨提示</div>
		    				<p>1.&nbsp;&nbsp;充值前，须先确认已绑定银行卡，若未绑定银行卡，请先绑定本人银行卡（仅支持借记卡）；<br />
2.&nbsp;&nbsp;快捷充值须先开通“银联在线支付”功能，若充值时页面提示“错误代码CI77”，则表示未开通该功能；<br />
3.&nbsp;&nbsp;快捷充值单日充值笔数上限为40笔；<br />
4.&nbsp;&nbsp;支付限额请参照 <a target="_blank" href="${ctx}/bank/web/user/recharge/rechargeQuotaLimit.do">支付说明</a>。</p>
	    			</div>
	    			<!-- 若要换绑银行卡，可先解绑再绑定新的银行卡或 <a target="_blank" href='${ctx}/help/index.do?side=hp18&issure=is173'>提交修改申请表</a>； -->
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
    		<a class="btn btn-primary single" id="authInvesPopConfirm">立即授权</a>
    	</div>
    </div>
    <div class="alert" id="setPassword" style="margin-top: -154.5px;width:350px;display: none;">
    	<div class="icon tip"></div>
    	<div class='content prodect-sq'>
    		
    	</div>
    	<div class="btn-group">
    		<a class="btn btn-primary single" id="setPasswordConfirm">立即开通</a>
    	</div>
    </div>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="${ctx}/dist/js/lib/jquery.validate.js"></script>
   	<script src="${ctx}/dist/js/lib/jquery.metadata.js"></script>
    <script type="text/javascript" src="${ctx}/js/bank/user/recharge/recharge-new.js?version=${version}" charset="utf-8"></script>
    <script type="text/javascript" src="${ctx}/js/jquery.cookie.min.js" charset="utf-8"></script>
	<script>setActById("userPandect");</script>
</body>
</html>