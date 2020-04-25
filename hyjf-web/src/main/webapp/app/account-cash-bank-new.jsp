<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
	    		<!-- start 内容区域 -->
	    		<div class="acount-recharge">
	    			<form id="cashForm">
	    				<input type="hidden" name="bankType" id="bankType" value="7"/>
		    			<ul>
		    				<li class="avail">
		    					<div class="prompt">可用金额：</div>
		    					<div class="info"><span>541.00</span>元</div>
		    				</li>
		    				<li class="card">
		    					<div class="prompt">提现银行卡：</div>
		    					<div class="info">
		    						<div>建设银行&nbsp;&nbsp;622&nbsp;****&nbsp;****&nbsp;****&nbsp;0652</div>
		    						<a href="#">解绑>></a>
		    						<input type="hidden" value=123456>
		    					</div>
		    				</li>
		    				<li class="poundage">
		    					<div class="prompt">手续费：</div>
		    					<div class="info">
		    						1.00&nbsp;元
		    					</div>
		    				</li>
		    				<li class="recharge">
		    					<div class="prompt">提现金额：</div>
		    					<div class="info acount-input"><input type="text" placeholder="请输入金额" autocomplete="off" name="money" id="money"/><em>元</em></div>
		    				</li>
		    				<li class="bank">
		    					<div class="prompt">开户行号：</div>
		    					<div class="info acount-input"><input type="text"  placeholder="请输入开户行号" autocomplete="off" name="khBankId"/></div>
		    					<a class="choice" href="#">选择开户行号</a>
		    				</li>
		    				<li class="to-accout">
		    					<div class="prompt">到账金额：</div>
		    					<div class="info"><span>0.00</span>&nbsp;元</div>
		    				</li>
		    			</ul>
		    			<a class="sub">申请提现</a>
	    			</form>
	    			<div class="reminder">
	    				<h2>温馨提示</h2>
	    				<ul class="reminder-list">
		    				<li>1. 身份认证、账户托管开通、提现银行设置均成功后，才能进行提现操作；</li>
							<li>2. 提现费用：手续费：1.00元/笔；</li>
							<li>3. 提现手续费由江西银行收取，具体费用以江西银行为准；</li>
							<li>4. 用户绑定银行卡作为快捷卡后，只可以提现到快捷卡；选择常用银行卡作为快捷卡；</li>
							<li> 5. 提现限额<br>
     							工作日9:00-16:30无提现金额限制。<br>
    							其余时间<br>
     							快捷银行卡为中行，工行，单笔限额5万，<br>
    							其余快捷卡，单笔限额20万；</li>
							<li>6. 绑定银行卡时，绑定的银行卡地开户信息，需要与存管账户的用户信息保持一致；</li>
							<li>7. 解绑银行卡时，需要当前存管账户系统余额为0，且没有未结清的标的时(注：清算标的有约两小时的处理时间，22:00后的清算将在次日的8:00后统一清算，请耐心等待)，才可以进行解绑；</li>
							<li>8. 严禁利用充值功能进行信用卡套现、转账、洗钱等行为，一经发现将冻结账号。</li>
						</ul>
	    			</div>
	    		</div>
	    		<!-- end 内容区域 --> 
	    	</div>
	    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="../dist/js/acc-re-cash/account-cash.js"></script>
</body>
</html>