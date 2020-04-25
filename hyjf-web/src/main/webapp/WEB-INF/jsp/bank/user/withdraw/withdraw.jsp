<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
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
	    		<c:if test="${roleId eq '1' }">
	    			<form action="${ctx}/bank/web/withdraw/cash.do" method="" id="cashForm" class="cashForm">
	    		</c:if>
	    		<c:if test="${roleId ne '1' }">
	    			<form action="${ctx}/bank/web/withdraw/cash.do" method="" id="cashForm" class="cashForm">
	    		</c:if>
	    			<input type="hidden" name="paymentAuthStatus" id="paymentAuthStatus" value="${paymentAuthStatus}" /> 
	    			<input type="hidden" name="paymentAuthOn" id="paymentAuthOn" value="${paymentAuthOn}" /> 
	    			<input type="hidden" name="tokenCheck" id="tokenCheck" value="" /> 
					<input type="hidden" name="tokenGrant" id="tokenGrant" value="${tokenGrant}" />
					<input type="hidden" name="isSetPassWord" id="isSetPassWord" value="${isSetPassWord}" />
					<input type="hidden" name="total" id="total" value="${total}" />
					<input type="hidden" name="bankBalance" id="bankBalance" value="${bankBalance}" />
					<input type="hidden" name="bankType" id="bankType" value="${bankType}"/>
					<input type="hidden" name="bankName" id="bankName" value="${bankName}" />
					<input type="hidden" name="roleId" id="roleId" value="${roleId}" />
		    			<ul class="P-input-w250">
		    				<li class="avail">
		    					<div class="prompt">可用金额：</div>
		    					<div class="info"><span>${total}</span>元
		    						<c:if test="${empty banks}">
		    							<div class="hint">使用提现功能前需要先绑定快捷银行卡</div>
		    						</c:if>
		    					</div>
		    					
		    				</li>
		    				<li class="card">
		    					<div class="prompt">提现银行卡：</div>
		    					<div class="info">
		    						<c:if test="${!empty banks}">
										<c:forEach items="${banks}" var="bankInfo" begin="0" step="1" varStatus="status">
											<input type="hidden" name="widCard" id="${bankInfo.cardNo}" value="${bankInfo.cardNo}" />
											<input type="hidden" name="isBindCard" id="isBindCard" value="1" />
											<input type="hidden" name="bankId" id="bankId" value="${bankInfo.bankId }">
											<div class="item" data-num="${bankInfo.id}">
												<div class="widCards P-font-size16">
													${bankInfo.bank }&nbsp;&nbsp;${bankInfo.cardNoInfo}
												</div>
											</div>
										</c:forEach>
									</c:if>
									<c:if test="${userType == 0 and empty banks}">
										<input type="hidden" name="isBindCard" id="isBindCard" value="0" />
									 <i class="widAvailableAmount widSetOther" onclick="location.href='${ctx }/bank/web/bindCard/index.do'">设置提现银行卡</i>
									</c:if>
		    					</div>
		    				</li>
		    				<li class="poundage">
		    					<div class="prompt">手续费：</div>
		    					<div class="info">
		    						${feeWithdraw }&nbsp;元
		    					</div>
		    				</li>
		    				<li class="recharge">
		    					<div class="prompt">提现金额：</div>
		    					<div class="info acount-input">
		    						<input type="hidden" id="total" value="${total}" />
		    						<input type="text" placeholder="请输入金额" autocomplete="off" name="withdrawmoney" maxlength="12" id="withdrawmoney"/><em>元</em>
		    					</div>
		    				</li>
		    				<c:if test="${userType == 0}">
		    				<li class="bank">
		    					<div class="prompt">开户行号：</div>
		    					<div class="info acount-input"><input type="text" placeholder="请输入开户行号" autocomplete="off" name="payAllianceCode" value="${payAllianceCode }"/><a class="choice" target="_blank" href="http://www.lianhanghao.com/">查询开户行号&nbsp;>></a></div>
		    				</li>
		    				</c:if>
		    				<c:if test="${userType == 1}">
		    				<li class="recharge" >
		    					<div class="prompt">开户行号：</div>
		    					<div class="info acount-input"><input type="text" placeholder="请输入开户行号" autocomplete="off" name="payAllianceCode" value="${payAllianceCode }"/><a class="choice" target="_blank" href="http://www.lianhanghao.com/">查询开户行号&nbsp;>></a></div>
		    				</li>
		    				</c:if>
		    				<li class="to-accout">
		    					<div class="prompt">到账金额：</div>
		    					<div class="info"><span>0.00</span>元</div>
		    				</li>
		    			</ul>
		    			<%--<c:if test="${roleId eq '1' }">
			    			<a class="sub">下一步</a>
			    		</c:if>
			    		<c:if test="${roleId ne '1' }">--%>
			    			<a class="sub">申请提现</a>
			    		<%--</c:if>--%>
		    			
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
	    <!-- 缴费授权返回地址 -->
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
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="${cdn}/js/findPwd.js?version=${version}" type="text/javascript" charset="utf-8"></script>
	<script src="${cdn}/js/jquery.validate.js" type="text/javascript" charset="utf-8"></script>
	<script src="${cdn}/js/jquery.metadata.js?version=${version}" type="text/javascript" charset="utf-8"></script>
	<script src="${cdn}/js/user/bankwithdraw/withdraw.js?version=${version}" type="text/javascript" charset="utf-8"></script>
	<script>setActById("userPandect");</script>
</body>
</html>