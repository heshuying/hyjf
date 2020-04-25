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
	    		<div class="recharge-tab qiye">
	    			<div >我的银行卡</div>
	    		</div>
	    		<!-- start 内容区域 -->
	    		<div class="acount-recharge">
	    			<form action="" method="post" id="cashForm" >
	    			    <input type="hidden" name="tokenCheck" id="tokenCheck" value="" />
		                <input type="hidden" name="tokenGrant" id="tokenGrant" value="${tokenGrant}" />
		                <input type="hidden" name="rechargeType" id="rechargeType" value="0" />
		                <input type="hidden" name="lastSrvAuthCode" id="smsSeq" value="" />
		    			<ul class="recharge-new">
							<c:if test="${bindType == 0}">
								<li class="avail">
				   					<div class="prompt">姓名：</div>
				   					<div class="info">
				   						<i class="normal">${truename}</i>
				   					</div>
				   				</li>
				   				<li class="avail">
				   					<div class="prompt">身份证号：</div>
				   					<div class="info">
				   						<i class="normal">${idcard }</i>
				   					</div>
				   				</li>
								<li class="recharge" style="margin-bottom: 37px;">
									<div class="prompt">绑定银行卡：</div>
									<div class="info acount-input">
										<input id="card" name="cardNo" type="text" placeholder="请输入银行卡号" autocomplete="off" oninput="if(value.length>19)value=value.slice(0,19)">
									</div>
									<a class="tishi" style="color:#48B7FE;" target="_blank" href="${ctx}/bank/web/user/recharge/rechargeQuotaLimit.do">支持银行及限额说明</a>
								</li>

								<li class="recharge"  style="margin-bottom: 37px;">
									<div class="prompt">银行预留手机号：</div>
									<div class="info acount-input">
										<input name="mobile" id="mobile" type="text" onkeyup="value=value.replace(/[^\d]/g,'')" placeholder="请输入银行预留手机号" autocomplete="off">
									</div>
								</li>
								<li class="recharge"  style="display: list-item;margin-bottom: 35px;">
									<div class="prompt">验证码：</div>
									<div class="info acount-input"><input type="text" placeholder="请输入手机验证码" autocomplete="off" maxlength="6" name="smsCode" id="code"><span class="get-code"><em class="rule"></em>获取验证码</span></div>
								</li>
							</c:if>
							<c:if test="${bindType == 1}">
								<li class="avail">
				   					<div class="prompt">姓名：</div>
				   					<div class="info">
				   						<i class="normal">${truename}</i>
				   					</div>
				   				</li>
				   				<li class="avail">
				   					<div class="prompt">身份证号：</div>
				   					<div class="info">
				   						<i class="normal">${idcard }</i>
				   					</div>
				   				</li>
							</c:if>
		    			</ul>
						<c:if test="${bindType == 0}">
							<a class="sub disable"  id = "sub">绑定</a><!-- 申请提现 -->
						</c:if>
						<c:if test="${bindType == 1}">
							<a class="sub" href="${bindCardUrl}?urlstatus=withdraw" id = "subnew">绑定</a><!-- 申请提现 -->
						</c:if>
	    			</form>
	    			<div class="prompt-recharge-new">
		    				<div class="title"><span class="icon iconfont icon-bulb" style="padding-right: 7px;"></span>温馨提示</div>
		    				<p>1.&nbsp;&nbsp;绑定银行卡时，绑定的银行卡的开户信息，需要与存管账户的用户信息保持一致；<br />
2.&nbsp;&nbsp;若要换绑银行卡，可先解绑再绑定新的银行卡，或  <a target="_blank" href='${ctx}/help/index.do?side=hp18&issure=is173'>提交修改申请表</a>；<br />
3.&nbsp;&nbsp;解绑银行卡的前提是，当前账户余额为“0”，并且无待收出借和待还借款，如不符合前述条件，请联系客服申请解绑，客服热线：400-900-7878。</p>
	    			</div>
	    		</div>
	    		<!-- end 内容区域 --> 
	    	</div>
	    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="${ctx}/dist/js/lib/jquery.validate.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctx}/dist/js/lib/jquery.metadata.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctx}/dist/js/acc-re-cash/bind-card-new.js?version=${version}"></script>
	<script>setActById("userPandect");</script>
</body>
</html>