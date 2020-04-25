<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
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
	    		<div class="acount-recharge">
	    			<form action="" method="post" id="cashForm">
	    			    <input type="hidden" name="tokenCheck" id="tokenCheck" value="" />
		                <input type="hidden" name="tokenGrant" id="tokenGrant" value="${tokenGrant}" />
		                <input type="hidden" name="rechargeType" id="rechargeType" value="0" />
		                <input type="hidden" name="lastSrvAuthCode" id="smsSeq" value="" />
		    			<ul>
		    				<li class="avail">
		    					<div class="prompt">可用金额：</div>
		    					<div class="info">
		    						<span>${bankAmount }</span>元
		    						<div class="hint">使用提现功能前需要先绑定快捷银行卡</div>
		    					</div>
		    				</li>
							<c:if test="${bindType == 0}">
								<li class="bound-card" style="margin-bottom: 40px;">
									<div class="prompt">绑定银行卡：</div>
									<div class="info acount-input">
										<input id="card" name="cardNo" type="text" placeholder="请输入银行卡号" autocomplete="off" oninput="if(value.length>19)value=value.slice(0,19)">
									</div>
								</li>

								<li class="bound-card">
									<div class="prompt">银行预留手机号：</div>
									<div class="info acount-input">
										<input name="mobile" id="mobile" type="text" onkeyup="value=value.replace(/[^\d]/g,'')" placeholder="请输入银行预留手机号" autocomplete="off">
									</div>
								</li>
								<li class="recharge" style="display: list-item;">
									<div class="prompt">验证码：</div>
									<div class="info acount-input"><input type="text" placeholder="请输入手机验证码" autocomplete="off" maxlength="6" name="smsCode" id="code"><span class="get-code"><em class="rule"></em>获取验证码</span></div>
								</li>
							</c:if>
		    			</ul>
						<c:if test="${bindType == 0}">
							<a class="sub" id = "sub">绑定</a><!-- 申请提现 -->
						</c:if>
						<c:if test="${bindType == 1}">
							<a class="sub" href="${bindCardUrl}?urlstatus=withdraw" id = "subnew">绑定</a><!-- 申请提现 -->
						</c:if>
	    			</form>
	    			<div class="reminder">
	    				<h2>温馨提示</h2>
	    				<ul class="reminder-list">
		    				<li>1. 身份认证、账户存管开通、提现银行设置均成功后，才能进行提现操作；</li>
							<li>2. 提现费用：手续费：1.00元/笔；</li>
							<li class="tc-main">3. 用户每日发起提现上限40次；</li>
							<li>4. 提现手续费由江西银行收取，具体费用以江西银行为准；</li>
							<li>5. 用户绑定银行卡作为快捷卡后，只可以提现到快捷卡；选择常用银行卡作为快捷卡；</li>
							<li>6.到账时间<br>
       							&nbsp;&nbsp;&nbsp;提现资金≤5万，实时到账<br>
       							&nbsp;&nbsp;&nbsp;提现资金＞5万，T+1个工作日到账
           					</li>
							<li>7.提现时间<br>
       							&nbsp;&nbsp;&nbsp;提现资金≤5万，随时可提<br>
       							&nbsp;&nbsp;&nbsp;提现资金＞5万，工作日（9:00-16:30）
           					</li>
							<li>8. 绑定银行卡时，绑定的银行卡的开户信息，需要与存管账户的用户信息保持一致；</li>
							<li>9. 解绑银行卡时，需要当前存管账户系统余额为0，且没有未结清的标的时(注：清算标的有约两小时的处理时间，22:00后的清算将在次日的8:00后统一清算，请耐心等待)，才可以进行解绑；如不符合条件仍需解绑，请联系客服：400-900-7878</li>
							<li>10. 严禁利用充值功能进行信用卡套现、转账、洗钱等行为，一经发现将冻结账号。</li>
						</ul>
						
	    			</div>
	    		</div>
	    		<!-- end 内容区域 --> 
	    	</div>
	    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="${cdn}/dist/js/lib/jquery.validate.js" type="text/javascript" charset="utf-8"></script>
	<script src="${cdn}/dist/js/lib/jquery.metadata.js" type="text/javascript" charset="utf-8"></script>
	<script src="${cdn}/dist/js/acc-re-cash/bind-card.js?version=${version}"></script>
	
</body>
</html>