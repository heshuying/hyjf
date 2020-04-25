<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
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
	<article class="main-content  p-top-0">
		<c:if  test="${chinapnrHide !=  '2'  }">
			<c:if test="${webViewUser.chinapnrUsrid != null && webViewUser.chinapnrUsrid != ''}">
				<c:if test="${account.balance !='' && account.balance > 2}">
					<div class="broadcast-box">
						<div class="broadcast">
							<i class="i-broadcast"></i>
							<p>平台已关闭汇付天下提现功能，请前往“汇付天下官网“进行提现。</p>
							<a href="${ctx}/user/withdraw/toWithdrawNew.do">操作教程</a>
						</div>
					</div>
				</c:if>
			</c:if>
		</c:if>
			<%--<c:if test="${chinapnrHide != 'true' }">
				<c:if test="${webViewUser.chinapnrUsrid != null && webViewUser.chinapnrUsrid != ''}">
					<c:if test="${account.balance !='' && account.balance > 2}">
						<c:if test="${showEdition == 0 }">
							<div class="broadcast-box">
					    		<div class="broadcast">
						    		<i class="i-broadcast"></i>
						    		<p>平台现已开通<span>江西银行</span>存管服务，原汇付天下账户将无法出借，请您立即提现。</p>
						    		<a href="${ctx}/user/withdraw/toWithdraw.do">立即提现</a>
						    		<p class="balance">汇付天下余额：<fmt:formatNumber value='${account.balance}' pattern='#,#00.00#' />元</p>
					    		</div>
					    	</div>
					    </c:if>
						<c:if test="${chinapnrHide == 'true' }">
							<div class="broadcast-box">
					    		<div class="broadcast">
						    		<i class="i-broadcast"></i>
						    		<p>平台已于2018年12月31日关闭汇付天下提现功能，请前往“汇付天下官网“进行提现。</p>
						    		<a href="${ctx}/user/withdraw/toWithdrawNew.do">操作教程</a>
					    		</div>
					    	</div>
					    </c:if>
			    	</c:if>
				</c:if>
			</c:if>--%>
		    <c:if test="${!webViewUser.bankOpenAccount}">
		    	<div class="broadcast-box" id="light-box">
		            <div class="broadcast">
		                <i class="i-broadcast"></i>
		                <p>您还没有开通<span>江西银行</span>资金存管账户，为了保障您的资金安全，请您立即开户。</p>
		                <a href="${ctx}/bank/web/user/bankopen/init.do">立即开户</a>
		            </div>
		        </div>
		    </c:if>
	    	<div class="container relative">
	    		<!-- start 内容区域 -->
	    		<div class="basic-infor">
	    			<div class="basic">
	    				<a class="head">
		    				<c:choose>
			    				<c:when test="${user.iconurl == null || user.iconurl == ''}">
			    					<img id="faceUrl" style="border-radius:50%;" src="${cdn}/dist/images/acc-set/account-profile@2x.png" width="60" />
			    				</c:when>
	   							<c:otherwise>
									<img id="faceUrl" style="border-radius:50%;" src="${user.iconurl}" width="60" >
								</c:otherwise>
			    			</c:choose>
	    				</a>
	    				<div class="info">
		    				<p class="name">${username}</p>
		    				<c:if test="${isVip}">
		    					<p class="vip"><i class="i-crown"></i><span>${vipName}</span></p>
		    				</c:if>
		    				<div class="function">
		    					<c:if test="${!webViewUser.bankOpenAccount}">
		    						<a class="i-person-info disable state" href="${ctx}/bank/web/user/bankopen/init.do" data-alt="去开户"></a>
		    					</c:if>
		    					<c:if test="${webViewUser.bankOpenAccount}">
		    						<a class="i-person-info state" data-alt="已开户"></a>
		    					</c:if>
		    					
		    					<c:if test="${user.isSetPassword == 0}">
		    						<a class="i-lock disable state" href="${ctx}/bank/user/transpassword/setPassword.do" data-alt="去设置交易密码"></a>
		    					</c:if>
		    					<c:if test="${user.isSetPassword == 1}">
		    						<a class="i-lock state" data-alt="已设置交易密码"></a>
		    					</c:if>
		    					
		    					<c:if test="${bankCard == null && webViewUser.bankOpenAccount}">
		    						<a class="i-card disable" href="${ctx}/bank/web/bindCard/bindCardNew.do"><span class="alt-tit-show">您尚未绑定银行卡，<em>立即绑定</em></span></a>
		    					</c:if>
		    					<c:if test="${bankCard == null && !webViewUser.bankOpenAccount}">
		    						<a class="i-card state disable"  data-alt="您尚未开户，请先开户" href="${ctx}/bank/web/user/bankopen/init.do"></a>
		    					</c:if>
		    					<c:if test="${bankCard != null }">
		    						<a class="i-card state" data-alt="已绑卡" href="${ctx}/bank/web/bindCard/myCardInit.do"></a>
		    					</c:if>
		    					
		    					
		    					<c:if test="${userEvaluationResultFlag == '0'}">
		    						<a class="i-ce disable state" href="${ctx}/financialAdvisor/financialAdvisorInit.do" data-alt="去风险测评"></a>
		    					</c:if>
		    					<c:if test="${userEvaluationResultFlag != '0'}">
		    						<a class="i-ce state" data-alt="已完成风险测评"></a>
		    					</c:if>
		    				</div>
	    				</div>
	    			</div>
	    			<div class="account">
	    				<div class="coupons">
	    					<p><i class="i-coupons"></i>优惠券<span>${couponValidCount}</span>张</p>
	    				</div>
	    				<div class="state">
<%-- 	    				<c:if test="${bankOpenAccount==null}">
	    					<span>存管账户：未开户</span>
	    					<a href="${ctx}/bank/web/user/bankopen/init.do">马上开户</a>
	    				</c:if> --%>
	    				<c:if test="${bankOpenAccount!=null}">
	    					<span>存管账户：${bankOpenAccount.account }</span>
	    				</c:if>
	    					
	    				</div>
	    				<em class="rule"></em>
	    			</div>
	    			<div class="amount">
	    				<p>可用金额（元）</p>
	    				<p class="money" id="bankBalance">
	    					<fmt:formatNumber value='${fn:split(account.bankBalance, ".")[0]}' pattern='#,##0.' /><span class="sm">${fn:split(account.bankBalance, ".")[1]}</span>
    					</p>
	    				<c:if test="${webViewUser.bankOpenAccount}"><a class="refresh"><i class="i-refresh"></i>刷新</a></c:if>
	    				<a class="btn-recharge" href="${ctx}/bank/web/user/recharge/rechargePage.do">充值</a>
	    				<a class="btn-cash" href="${ctx}/bank/web/withdraw/toWithdraw.do">提现</a>
	    			</div>
	    		</div>
	    		<div class="assets">
	    			<div>
	    				<p class="tit">资产总额（元）</p>
	    				<p class="money"><fmt:formatNumber value='${fn:split(account.bankTotal, ".")[0]}' pattern='#,##0.' /><span class="sm">${fn:split(account.bankTotal, ".")[1]}</span></p>
	    				<em class="rule"></em>
	    			</div>
	    			<div>
	    				<p class="tit">冻结金额（元）</p>
	    				<p class="money"><fmt:formatNumber value='${fn:split(account.bankFrost, ".")[0]}' pattern='#,##0.' /><span class="sm">${fn:split(account.bankFrost, ".")[1]}</span></p>
	    				<em class="rule"></em>
	    			</div>
	    			<div>
	    				<p class="tit">累计收益</p>
	    				<p class="money"><fmt:formatNumber value='${fn:split(account.bankInterestSum, ".")[0]}' pattern='#,##0.' /><span class="sm">${fn:split(account.bankInterestSum, ".")[1]}</span></p>
	    			</div>
	    		</div>
	    		<div class="rights-manage overview-box">
	                <div class="rights-top">
	                	<div class="title">
		    				<p><i class="i-card"></i>我的资产</p>
		    			</div>
	                     <div class="rights-main">
	                         <div class="rights-fl">
	                            <div id="main" style="width:300px;height:300px;margin-top: 40px;"></div>   
	                         </div>
	                         <div class="rights-fr">
	                             <p class="p1">资产</p>
	                             <p class="p2">待收金额（元）</p>
	                             <p class="p3">待收本金（元）</p>
	                             <p class="p4">待收收益（元）</p>
	                             <ul class="table-main">
	                                 <li class="p1" style="padding-left: 0;"><span class="icon-weibiaoti"><i></i></span><span class="fr1">散标</span></li>
	                                 <li class="p2"><span class="lg"><fmt:formatNumber value='${fn:split(account.bankAwait, ".")[0]}' pattern='#,##0.' /><span class="sm">${fn:split(account.bankAwait, ".")[1]}</span></li>
	                                 <li class="p3"><span class="lg"><fmt:formatNumber value='${fn:split(account.bankAwaitCapital, ".")[0]}' pattern='#,##0.' /><span class="sm">${fn:split(account.bankAwaitCapital, ".")[1]}</span></li>
	                                 <li class="p4"><span class="lg"><fmt:formatNumber value='${fn:split(account.bankAwaitInterest, ".")[0]}' pattern='#,##0.' /><span class="sm">${fn:split(account.bankAwaitInterest, ".")[1]}</span></li>
	                             </ul>
	                             <ul class="table-main">
	                                 <li class="p1" style="padding-left: 0;"><span class="icon-weibiaoti"><i></i></span><span class="fr1">智投</span></li>
	                                 <li class="p2"><span class="lg"><fmt:formatNumber value='${fn:split(account.planAccountWait, ".")[0]}' pattern='#,##0.' /><span class="sm">${fn:split(account.planAccountWait, ".")[1]}</span></li>
	                                 <li class="p3"><span class="lg"><fmt:formatNumber value='${fn:split(account.planCapitalWait, ".")[0]}' pattern='#,##0.' /><span class="sm">${fn:split(account.planCapitalWait, ".")[1]}</span></li>
	                                 <li class="p4"><span class="lg"><fmt:formatNumber value='${fn:split(account.planInterestWait, ".")[0]}' pattern='#,##0.' /><span class="sm">${fn:split(account.planInterestWait, ".")[1]}</span></li>
	                             </ul>
	                             <ul class="table-main total">
	                                 <li class="p1" style="padding-left: 0;"><span class="fr1">总计</span></li>
	                                 <li class="p2"><span class="lg"><fmt:formatNumber value='${fn:split(account.bankAwait+account.planAccountWait, ".")[0]}' pattern='#,##0.' /><span class="sm">${fn:split(account.bankAwait+account.planAccountWait, ".")[1]}</span></li>
	                                 <li class="p3"><span class="lg"><fmt:formatNumber value='${fn:split(account.bankAwaitCapital+account.planCapitalWait, ".")[0]}' pattern='#,##0.' /><span class="sm">${fn:split(account.bankAwaitCapital+account.planCapitalWait, ".")[1]}</span></li>
	                                 <li class="p4"><span class="lg"><fmt:formatNumber value='${fn:split(account.bankAwaitInterest+account.planInterestWait, ".")[0]}' pattern='#,##0.' /><span class="sm">${fn:split(account.bankAwaitInterest+account.planInterestWait, ".")[1]}</span></li>
	                             </ul>
	                             <input type="hidden" id="accountAwait" value="<fmt:formatNumber value='${account.bankAwait+account.planAccountWait}' pattern='#,##0.00' />">
	                             <input type="hidden" id="bankAccountAwait" value="${account.bankAwait}">
	                             <input type="hidden" id="planAccountWait" value="${account.planAccountWait}">
	                         </div>
	                    </div>
	                </div>
                </div>
	            
	            
	    		
	    		<div class="repayment overview-box">
	    			<div class="title">
	    				<p><i class="i-repayment"></i>近期回款</p>
	    				<%-- <p class='receipt'>待收总额：<span><fmt:formatNumber value="${account.bankAwait+account.planAccountWait}" pattern="#,##0.00" /></span> 元</p> --%>
	    			</div>
	    			<div class="table">
	    				
    					<div class="tit">
    						<div class="t1">回款时间</div>
    						<div class="t2">项目编号/服务名称</div>
    						<div class="t3">参考年回报率</div>
    						<div class="t4">预计回款金额</div>
    						<div class="t5">状态</div>
    					</div>
    					<c:choose>
    						<c:when test="${empty recoverLatestList}">
    							<div class="data-empty"><div class="empty-icon"></div><p class="align-center">暂无回款记录</p></div>
    						</c:when>
    						<c:otherwise>
								<c:forEach items="${recoverLatestList }" var="record" begin="0" step="1" varStatus="status">
									<a class="tr" href="javascript:;">
			    						<div class="t1">
			    						${record.investDate }
			    						<c:if test="${record.hjhType eq 1}">
			    							<span class="alt-el" data-alt="该时间是指智投服务开始退出<br/>时间。实际退出完成时间视<br/>债转市场交易情况而定。"><i class="icon iconfont icon-zhu"></i></span>
			    						</c:if>
			    						</div>
			    						<div class="t2">
				    						<c:choose>
											     <c:when test="${fn:length(record.projectNid) > 11}">
											     	 <c:out value="${fn:substring(record.projectNid, 0, 11)}...." />
											     </c:when>
											     <c:otherwise>
											     	 <c:out value="${record.projectNid }" />
											     </c:otherwise>
										    </c:choose>	
				    						<c:if test="${record.type eq 3}">
				    							<c:choose>
				    								<c:when test="${record.couponType eq 1 }">
				    									<em class="ui-tiyanjin">体验金</em>
				    								</c:when>
				    								<c:when test="${record.couponType eq 2 }">
				    									<em class="ui-jiaxi">加息券</em>
				    								</c:when>
				    								<c:when test="${record.couponType eq 3 }">
				    									<em class="ui-daijinquan">代金券</em>
				    								</c:when>
				    							</c:choose>
				    						</c:if>
											<c:if test="${record.type eq 4}">
												<em class="ui-daijinquan" style="border: 1px solid #3476b9;color: #fff;background: #3476b9;">加息${record.borrowApr }</em>
											</c:if>
			    						</div>
			    						<div class="t3">${record.borrowApr }</div>
			    						<div class="money t4"><fmt:formatNumber value='${record.totalWait}' pattern='0.00' /></div>
			    						<c:if test="${record.exitType eq 1}">
			    							<div class="t5">退出中</div><!-- class="back t5" -->
			    						</c:if>
			    						<c:if test="${record.exitType ne 1}">
			    							<div class="t5">未还款</div><!-- class="back t5" -->
			    						</c:if>
			    						
			    					</a>
								</c:forEach>	
							</c:otherwise>
						</c:choose>
    				</div>
	    		</div>
	    		<!-- end 内容区域 --> 
	    	</div>
	    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="${cdn}/dist/js/lib/echarts.common.min.js"></script>
	<script src="${cdn}/dist/js/lib/jquery.validate.js"></script>
    <script src="${cdn}/dist/js/lib/jquery.metadata.js"></script>
    <script type="text/javascript" src="${cdn}/js/jquery.cookie.min.js" charset="utf-8"></script>
	<script src="${cdn}/dist/js/acc-set/account-overview.js?version=${version}"></script>
	<!-- 设置定位  -->
	<script>
		setActById("userPandect");
		utils.alt('.table .tr .t1 .alt-el');
	</script>
</body>
</html>