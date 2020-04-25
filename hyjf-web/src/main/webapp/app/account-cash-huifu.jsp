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
	<article class="main-content">
    	<div class="container result">
    		<!-- start 内容区域 -->
    		<div class="acount-recharge">
    			<form id="cashForm">
					<ul>
	    				<li class="avail">
	    					<div class="prompt">汇付天下余额：</div>
	    					<div class="info"><span>541.00</span>元</div>
	    				</li>
	    				<li>
	    					<div class="prompt">提现到银行卡：</div>
	    					<div class="card-box">
	    						<input type="hidden" value="" id="cardId" />
	    						<div class="card" data-id="111">
	    							<a class="card-content">
		    							<p class="tit"><img src="http://img.hyjf.com/dist/images/account/blank_logo.jpg" /><span class="bank">中国工商银行</span><span class="type">普通卡</span></p>
		    							<div class="number">
			    							<span>6222</span>
			    							<span>0034</span>
			    							<span>7008</span>
			    							<span>3266</span>
		    							</div>
		    							<span class="i-duihao"></span>
		    						</a>
		    						<div class="operation">
	    								<a class="delete">删除</a>
	    								<em class="rule"></em>
	    								<a class="shortcut">设为快捷卡</a>
		    						</div>
	    						</div>
	    						<div class="card">
	    							<p class="tit"><img src="http://img.hyjf.com/dist/images/account/blank_logo.jpg" /><span class="bank">中国工商银行</span><span class="type">普通卡</span></p>
	    							<div class="number">
		    							<span>6222</span>
		    							<span>0034</span>
		    							<span>7008</span>
		    							<span>3266</span>
	    							</div>
	    							<div class="operation">
	    								<a class="delete">删除</a>
	    								<em class="rule"></em>
	    								<a class="shortcut">设为快捷卡</a>
	    							</div>
	    							<input type="hidden" name="card" id="" value="456" disabled/>
	    						</div>
	    						<div class="card">
	    							<p class="tit"><img src="http://img.hyjf.com/dist/images/account/blank_logo.jpg" /><span class="bank">中国工商银行</span><span class="type">普通卡</span></p>
	    							<div class="number">
		    							<span>6222</span>
		    							<span>0034</span>
		    							<span>7008</span>
		    							<span>3266</span>
	    							</div>
	    							<div class="operation">
	    								<a class="delete">删除</a>
	    								<em class="rule"></em>
	    								<a class="shortcut">设为快捷卡</a>
	    							</div>
	    							<input type="hidden" name="card" id="" value="789" disabled/>
	    						</div>
	    						<div class="card">
	    							<p class="tit"><img src="http://img.hyjf.com/dist/images/account/blank_logo.jpg" /><span class="bank">中国工商银行</span><span class="type">普通卡</span></p>
	    							<div class="number">
		    							<span>6222</span>
		    							<span>0034</span>
		    							<span>7008</span>
		    							<span>3266</span>
	    							</div>
	    							<div class="operation">
	    								<a class="delete">删除</a>
	    								<em class="rule"></em>
	    								<a class="shortcut">设为快捷卡</a>
	    							</div>
	    							<input type="hidden" name="card" id="" value="111" disabled/>
	    						</div>
	    						<div class="card">
	    							<p class="tit"><img src="http://img.hyjf.com/dist/images/account/blank_logo.jpg" /><span class="bank">中国工商银行</span><span class="type">普通卡</span></p>
	    							<div class="number">
		    							<span>6222</span>
		    							<span>0034</span>
		    							<span>7008</span>
		    							<span>3266</span>
	    							</div>
	    							<div class="operation">
	    								<a class="delete">删除</a>
	    								<em class="rule"></em>
	    								<a class="shortcut">设为快捷卡</a>
	    							</div>
	    							<input type="hidden" name="card" id="" value="222" disabled/>
	    						</div>
	    						<a href="" class="add">
	    							<span class="iconfont icon-add"></span>
	    							<p>添加银行卡</p>
	    						</a>
	    					</div>
	    				</li>
	    				<li class="recharge">
	    					<div class="prompt">提现金额：</div>
	    					<div class="info acount-input"><input type="text" onkeyup="value=value.replace(/[^\d]/g,'')" placeholder="请输入金额" autocomplete="off" name="money" id="money"/><em>元</em></div>
	    				</li>
	    			</ul>
	    			<a class="sub">申请提现</a>
    			</form>
    			<div class="reminder">
    				<h2>温馨提示</h2>
    				<ul class="reminder-list">
	    				<li>1. 投资人充值投资所有项目均不收取充值费用；</li>
						<li>2. 最低充值金额应大于等于 1 元；</li>
						<li>3. 快捷充值当日充值资金不可当日操作取现；</li>
						<li>4. 使用快捷充值的资金，取现时原卡返回；</li>
						<li>5. 充值/提现必须为银行借记卡，不支持存折、信用卡充值；</li>
						<li>6. 汇付天下严禁利用充值功能进行信用卡套现、转账、洗钱等行为，一经发现，将封停账号30天；</li>
						<li>7. 充值期间，请勿关闭浏览器，待充值成功并返回首页后，所充资金才能入账，如有疑问，请联系客服；</li>
						<li>8. 充值需开通银行卡网上支付功能，如有疑问请咨询开户行客服；</li>
						<li>9. 支付限额请参照 <a href="#">支付说明</a>。</li>
					</ul>
    			</div>
    		</div>
    		<!-- end 内容区域 --> 
    	</div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="../dist/js/lib/jquery.validate.js"></script>
    <script src="../dist/js/lib/jquery.metadata.js"></script>
	<script src="../dist/js/acc-re-cash/account-cash.js"></script>
</body>
</html>