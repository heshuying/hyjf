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
	<article class="main-content">
    	<div class="container result">
    		<!-- start 内容区域 -->
    		<div class="acount-recharge">
    			<form id="cashForm" action="${ctx}/user/withdraw/cash.do">
   					<input type="hidden" name="tokenCheck" id="tokenCheck" value="" /> 
					<input type="hidden" name="tokenGrant" id="tokenGrant" value="${tokenGrant}" />
					<input type="hidden" id="total" value="${total}" /> 
   					<input type="hidden" name="cardId" id="cardId" value="<c:if test="${hasBindCard==true}"><c:if test="${hasBindQuick==true}">${quickCardId}</c:if><c:if test="${hasBindDefault==true}">${defaultCardId}</c:if><c:if test="${hasBindDefault==false and hasBindQuick== false }">${banks[0].id}</c:if></c:if>"/>
					<ul>
	    				<li class="avail">
	    					<div class="prompt">汇付天下余额：</div>
	    					<div class="info"><span>${total}</span>元</div>
	    				</li>
	    				<li>
	    					<div class="prompt">提现到银行卡：</div>
	    					<div class="card-box">
		    					<c:if test="${hasBindCard == true}">
			    					<c:if test="${!empty banks}">
										<c:forEach items="${banks}" var="bankInfo" begin="0" step="1" varStatus="status">
											<div class="card" data-id="${bankInfo.id}">
												<a class="card-content">
				    							<p class="tit">
				    								<img src="${bankInfo.logo}" />
				    								<span class="bank">${bankInfo.bank}</span>
				    								<c:choose>
														<c:when test="${bankInfo.isDefault == 0 }">
															<span class="type">普通卡</span>
														</c:when>
														<c:otherwise>
															<span class="type">默认提现卡</span>
														</c:otherwise>
													</c:choose>						
				    							</p>
				    							<div class="number">
				    								<c:set var="end" value="${fn:length(bankInfo.cardNo)}"></c:set>
					    							<span><c:out value="${fn:substring(bankInfo.cardNo, 0, 4)}" /></span>
					    							<span>****</span>
					    							<span>****</span>
					    							<span><c:out value="${fn:substring(bankInfo.cardNo, end-4, end)}" /></span>
				    							</div>
				    							<!-- 默认快捷提现卡 -->
				    							<c:if test="${bankInfo.isDefault == 1 or bankInfo.isDefault == 2 }">
				    								<span class="i-duihao"></span>
				    							</c:if>
				    							</a>
			    								<div class="operation">
				    								<c:if test="${banks.size()> 1 and bankInfo.isDefault == 0 and userType == 0 }">
														<a class="delete">删除</a>
													</c:if>
				    							</div>
				    						</div>
										</c:forEach>
									</c:if>
		    					</c:if>
		    					<!-- 未绑卡，或者未绑定快捷卡，且绑定卡的数量小于3才可继续绑卡 -->
								<c:if test="${hasBindCard == false }">
									<a href="${ctx}/bindCard/bindCard.do" class="add">
		    							<span class="iconfont icon-add"></span>
		    							<p>添加银行卡</p>
		    						</a>
								</c:if>
								<c:if test="${hasBindCard == true and hasBindQuick == false and !empty banks and banks.size() < 3}">
									<a href="${ctx}/bindCard/bindCard.do" class="add">
		    							<span class="iconfont icon-add"></span>
		    							<p>添加银行卡</p>
		    						</a>
								</c:if>
	    					</div>
	    				</li>
	    				<li class="recharge">
	    					<div class="prompt"><span class="P-pd2">提现金额：</span></div>
	    					<div class="info acount-input P-left-2em"><input type="text" placeholder="请输入金额" maxlength="11" autocomplete="off" name="money" id="money"/><em>元</em></div>
	    				</li>
	    			</ul>
	    			<a class="sub huifu">申请提现</a>
    			</form>
    			<div class="reminder">
    				<h2>温馨提示</h2>
    				<ul class="reminder-list">
	    				<li>1. 身份认证、账户托管开通、提现银行设置均成功后，才能进行提现操作；</li>
						<li>2. 提现费用为每笔1.00元，费用为汇付天下资金托管收取；</li>
						<li>3. 因汇盈金服无法触及用户资金账户，无法收取用户任何费用，为防止套现，所充资金必须经投标回款后才能提现；</li>
						<li>4. 资金到达用户账户的当日（包括周六/周日/法定节假日）即可发起提现申请；</li>
						<li>5. 工作日（周六/周日/法定节假日均除外）21:00之前申请的提现，当日审核完毕并预计于下一工作日12:00-16:00到账，<br/>
								&nbsp;&nbsp; 否则将于下一工作日审核并预计于再下一工作日12:00-16:00到账，实际到账时间依据账户托管方（第三方支付平台）及提现银行而有所差异。</li>
					</ul>
    			</div>
    		</div>
    		<!-- end 内容区域 --> 
    	</div>
    </article>
   
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="${cdn }/dist/js/lib/jquery.validate.js"></script>
    <script src="${cdn }/dist/js/lib/jquery.metadata.js"></script>
	<script src="${cdn }/dist/js/acc-re-cash/account-cash-huifu.js?version=${version}"></script>
</body>
</html>