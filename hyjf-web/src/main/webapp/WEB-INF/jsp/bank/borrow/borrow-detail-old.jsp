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
	<section class="breadcrumbs">
        <div class="container">
            <div class="left-side">您所在的位置： <a href="${ctx}/">首页</a> &gt; 
                <c:if test="${projectDeatil.type == 4}"><a href="${ctx}/bank/web/borrow/newBorrowList.do">新手专区</a> &gt;</c:if>
                <c:if test="${projectDeatil.type != 4}"><a href="${ctx}/bank/web/borrow/initBorrowList.do">散标专区</a> &gt;</c:if> 项目详情
            </div>
            <div class="right-side"><span>开标时间：</span>${projectDeatil.sendTime} </div>
        </div>
    </section>
	<article class="main-content product">
	<input type="hidden" name="paymentAuthStatus" id="paymentAuthStatus" value="${paymentAuthStatus}" /><!-- 自动缴费状态 -->
		<input type="hidden" name="userBalance" id="userBalance" value="${userBalance}" />
		<input type="hidden" name="roleId" id="roleId" value="${roleId}"><!-- 仅限出借人进行投资业务 -->
       	<input type="hidden" name="borrowNid" id="borrowNid" value="${projectDeatil.borrowNid }" />
       	<input type="hidden" name="loginFlag" id="loginFlag" value="${loginFlag }" /> <!-- 登录状态 0未登陆 1已登录 -->
       	<input type="hidden" name="openFlag" id="openFlag" value="${openFlag }" /> <!-- 开户状态 0未开户 1已开户 -->
       	<input type="hidden" name="riskFlag" id="riskFlag" value="${riskFlag }" /> <!-- 是否进行过风险测评 0未测评 1已测评 -->
       	<input type="hidden" name="setPwdFlag" id="setPwdFlag" value="${setPwdFlag }" /> <!-- 是否设置过交易密码 0未设置 1已设置 -->
       	<input type="hidden" name="increase" id="increase" value="${projectDeatil.increaseMoney}" /> <!-- 递增金额 -->
       	<c:if test="${projectDeatil.status eq 11}">
       		<input type="hidden" name="isLast" id="isLast" value="${projectDeatil.tenderAccountMin ge InvestAccountInt}" /> <!-- 是否最后一笔投资 -->
       	</c:if>
       	<c:if test="${projectDeatil.status ne 11}">
       		<input type="hidden" name="isLast" id="isLast" value="false" /> <!-- 是否最后一笔投资 -->
       	</c:if>
       	<input type="hidden" id="projectData" data-total="${projectDeatil.investAccount}" data-tendermax="${projectDeatil.tenderAccountMax}" data-tendermin="${projectDeatil.tenderAccountMin}"/>
        <div class="container">
            <!-- start 内容区域 -->
            <div class="product-intr">
                <div class="title">
                    <span>${projectDeatil.borrowNid}</span>
                    <c:if test="${projectDeatil.type == 4}">
 	                  	 <div class="title-tag">新手专享</div>
                    </c:if>
                    <c:if test="${projectDeatil.type == 11}">
 	                  	 <div class="title-tag">尊享债权</div>
                    </c:if>
                    <c:if test="${projectDeatil.type == 13}">
 	                  	 <div class="title-tag">优选债权</div>
                    </c:if>                    
                    <div class="contract-box">
                        <c:if test="${projectDeatil.type ne 13}">
							<a href="${ctx}/agreement/intermediaryServices.do" target="_blank">投资协议(范本)</a>
							<a href="${ctx}/agreement/confirmationOfInvestmentRisk.do" target="_blank">风险确认书(范本)</a>
				    	</c:if>
                        <c:if test="${projectDeatil.type eq 13}">                       	
							<a href="#" onclick="openNew('${ctx}/agreement/RTBProductAgreement.do?borrowNid=${projectDeatil.borrowNid }')" >《产品协议》</a> <!-- (融通宝)温州金融资产交易中心股份有限公司个人会员服务协议 -->
							<a href="#" onclick="openNew('${ctx}/agreement/RTBPlatformAgreement.do?borrowNid=${projectDeatil.borrowNid }')" >《平台协议》</a> <!-- (融通宝)汇盈金服平台服务协议 -->
                        </c:if>
                    </div>
                </div>
                <div class="attr">
                    <div class="attr-item attr1">
                        <span class="val highlight">${projectDeatil.borrowApr}<span class="unit"> %</span> 
	                         <c:if test="${projectDeatil.isIncrease=='true'}">
	                         	<div class="poptag-pos">
		                            <div class="poptag">+${projectDeatil.borrowExtraYield}%</div>
		                        </div>
	                         </c:if>
	                        </span>
                        <span class="key">历史年回报率</span>
                    </div>
                    <div class="attr-item attr2">
                        <span class="val">${projectDeatil.borrowPeriod}<span class="unit"> ${projectDeatil.borrowPeriodType}</span> </span>
                        <span class="key">项目期限</span>
                    </div>
                    <div class="attr-item attr3">
                        <span class="val"><fmt:formatNumber value="${projectDeatil.borrowAccount}" pattern="#,###" /><span class="unit"> 元</span></span>
                        <span class="key">项目金额</span>
                    </div>
                </div>
                <div class="list">
                    <div class="list-item"><i class="safe"></i>安全保障计划</div>
                    <div class="list-item">项目编号：${projectDeatil.borrowNid}</div>
                    <div class="list-item">还款方式：${projectDeatil.repayStyle}</div>
                    <div class="list-item"><span class="dark">建议投资者类型：${projectDeatil.investLevel}及以上</span></div>
                </div>
            </div>
            
            <c:if test="${projectDeatil.status ne 11}">
	            <div class="product-form status">
	            	<!-- 定时发标 -->
	                <c:if test="${projectDeatil.status eq 10}">
		                <div class="status-content">
		                    <div class="title">
		                       	 开标倒计时
		                    </div>
		                    <div class="timer">
		                        <div id="counterTimer" class="counter" data-date="${projectDeatil.time}" data-now="${nowTime}">
		                            <div class="countdown"></div>
		                        </div>
		                    </div>
		                </div>
		                <div class="start-time">开标时间：${projectDeatil.onTime}</div>
	                </c:if>
	            	<!-- 已还款  -->
	                <c:if test="${projectDeatil.status eq 14}">
		                <div class="status-content yhk"></div>
		                <div class="start-time">满标时间：${projectDeatil.fullTime}</div>
	                </c:if>
	            	<!-- 还款中  -->
	                <c:if test="${projectDeatil.status eq 13}">
		                <div class="status-content hkz"></div>
		                <div class="start-time">满标时间：${projectDeatil.fullTime}</div>
	                </c:if>
	            	<!-- 复审中  -->
	                <c:if test="${projectDeatil.status eq 12}">
		                <div class="status-content fsz"></div>
		                <div class="start-time">满标时间：${projectDeatil.fullTime}</div>
	                </c:if>
	            	<!-- 复审中  -->
	                <c:if test="${projectDeatil.status eq 15}">
		                <div class="status-content ylb"></div>
		                <div class="start-time">满标时间：${projectDeatil.fullTime}</div>
	                </c:if>
	            </div>
            </c:if>
            <!-- 投资中  老标无投资中数据-->
            <c:if test="${projectDeatil.status eq 11}">   
            <form class="product-form" action="${ctx }/bank/web/user/tender/invest.do" id="productForm" method="post" autocomplete="off">
 	           	<input type="hidden" name="tokenCheck" id="tokenCheck" value="" />
				<input type="hidden" name="tokenGrant" id="tokenGrant" value="${tokenGrant}" /> 
				<input type="hidden" name="nid" id="nid" value="${projectDeatil.borrowNid }" />
				<input type="text" name="username" class="ignore fix-auto-fill"/>
                <div class="field">
                    <div class="key">项目剩余：</div>
                    <div class="val"><span class="highlight"><fmt:formatNumber value="${projectDeatil.investAccount}" pattern="#,##0.00" /></span> 元</div>
                </div>
                <div class="field">
                    <div class="key">可用金额：</div>
                    <c:if test="${loginFlag eq '1' }">
	                    <div class="val"><fmt:formatNumber value="${userBalance}" pattern="#,##0.00" /> 元</div>
	                    <a href="${ctx}/bank/web/user/recharge/rechargePage.do" class="link-recharge" onclick="setCookie()">充值</a>
                    </c:if>
                    <c:if test="${loginFlag eq '0' }">
	                    <div class="val">登录后可见</div>
	                    <a href="${ctx}/user/login/init.do" class="link-recharge" onclick="setCookie()">立即登录</a>
                    </c:if>
                </div>
                <div class="field">
                    <div class="input">
                        <input type="text" name="money" id="money" maxlength="10"  placeholder="${projectDeatil.tenderAccountMin}元起投，${projectDeatil.increaseMoney}元递增" 
                        oncopy="return false" onpaste="return false" oncut="return false" oncontextmenu="return false" autocomplete="off" <c:if test="${projectDeatil.tenderAccountMin ge InvestAccountInt}"> value="${InvestAccountInt}" readonly="readonly"</c:if> />
                        <div class="btn sm<c:if test="${projectDeatil.tenderAccountMin ge InvestAccountInt}"> btn-disabled</c:if>" <c:if test="${InvestAccountInt gt projectDeatil.tenderAccountMin}"> id="fullyBtn"</c:if> >全投</div>
                    </div>
                </div>
                <div class="field sub">
                    <div class="key dark">优惠券：</div>
                    <div class="val fl-r">
                    <c:if test="${isThereCoupon==1}">
						<a href="javascript:;" class="link-coupon" id="goCoupon"><span>
							${couponConfig.couponQuotaStr }
							<c:if test="${couponConfig.couponType == 1}"> 元  </c:if><c:if test="${couponConfig.couponType == 1}"> 体验金 </c:if> 
							<c:if test="${couponConfig.couponType == 2}"> % </c:if><c:if test="${couponConfig.couponType == 2}"> 加息券 </c:if> 
						    <c:if test="${couponConfig.couponType == 3}"> 元  </c:if><c:if test="${couponConfig.couponType == 3}"> 代金券 </c:if>
						</span>
						</a>
					</c:if>
					<c:if test="${isThereCoupon==0}">
						<!-- 是vip -->
							<a href="javascript:;" class="link-coupon" id="goCoupon"><span> 您有 <span class="num">${couponAvailableCount}</span> 张优惠券可用 </span>
							</a>
					</c:if>
                    
                    </div>
                    <input type="hidden" name="couponGrantId" id="couponGrantId" <c:if test="${isThereCoupon==1}"> value="${couponConfig.userCouponId}"</c:if>/>
                    <input type="hidden" name="coupon" id="couponInput" value="" 
 						<c:if test="${isThereCoupon == 1}">
	 						data-type="${couponConfig.couponType}" data-count="${couponAvailableCount}" data-val="${couponConfig.couponQuota}"
	 						data-id="${couponConfig.userCouponId}" data-txt="${couponConfig.couponName}" data-interest="${couponConfig.couponInterest}"
 						</c:if>
                     	<c:if test="${isThereCoupon == 0}">
	 						data-type="" data-count="0" data-id="" data-txt="请选择优惠券" data-val="" data-interest=""
 						</c:if> />
                </div>
                <div class="field sub">
                    <div class="key dark">历史回报：</div>
                    <div class="val fl-r" id="income">
                    <c:if test="${couponConfig != null}">${couponConfig.couponInterest} </c:if>
                    <c:if test="${couponConfig == null}">0.00</c:if>元
                   	</div>
                </div>
                <div class="field">
                    <div class="btn submit" id="goSubmit">立即投资</div>
                </div>
                <input type="checkbox" name="termcheck" class="form-term-checkbox" id="productTerm" checked="">
                <div class="dialog dialog-alert" id="confirmDialog">
                    <div class="title">投资确认</div>
                    <div class="content">
                        <table class="product-confirm-table" cellspacing="0" cellpadding="0">
                            <tr>
                                <td width="140" align="right"><span class="dark">项目编号：</span></td>
                                <td><span id="prodNum">${projectDeatil.borrowNid}</span></td>
                            </tr>
                            <tr>
                                <td align="right"><span class="dark">历史年回报率：</span></td>
                                <td><span id="">${projectDeatil.borrowApr}%<c:if test="${projectDeatil.borrowExtraYield ne '0.00'}"> + ${projectDeatil.borrowExtraYield}%</c:if></span></td>
                            </tr>
                            <tr>
                                <td align="right"><span class="dark">投资期限：</span></td>
                                <td>${projectDeatil.borrowPeriod}${projectDeatil.borrowPeriodType}</td>
                            </tr>
                            <tr>
                                <td align="right"><span class="dark">计息方式：</span></td>
                                <td>${projectDeatil.repayStyle}</td>
                            </tr>
                            <tr>
                                <td align="right"><span class="dark">投资金额：</span></td>
                                <td><span class="red" id="confirmValue">0.00</span> 元</td>
                            </tr>
                            <tr>
                                <td align="right"><span class="dark">优惠券：</span></td>
                                <td id="confirmCouponName">无</td>
                            </tr>
                            <tr>
                                <td align="right"><span class="dark">历史回报：</span></td>
                                <td id="confirmInterest">0.00 </td>
                                <input type="hidden" name="totalInterest" id ="totalInterest" value=""  />
                            </tr>
                        </table>
                        <div class="cutline"></div>
                        <table class="product-confirm-table" cellspacing="0" cellpadding="0">
                            <tr>
                                <td width="140" align="right"><span class="dark">实际支付：</span></td>
                                <td><span class="total" id="confirmRealMoney">0.00</span> 元</td>
                            </tr>
                        </table>
                        <div class="product-term">
                            <div class="term-checkbox checked"></div>
                            <span>我已同意并阅读  
                            <a href="${ctx}/agreement/RTBProductAgreement.do" target="_blank">《产品协议》</a>
                            <a href="${ctx}/agreement/RTBPlatformAgreement.do" target="_blank">《平台协议》</a></span>
                        </div>
                        <div style="padding-left: 50px; color: #fc0920;font-size: 12px;margin-top: 2px;"><img src="${cdn}/dist/images/icon-star.png"  alt="" style="width:10px;margin-left:2px;height: 10px;margin-right:8px;position: relative;top: -13px;"/><span style="display:inline-block">您应在中国大陆操作，因在境外操作导致的后果将由您<br>独自承担。</span></div>
                    </div>
                    <div class="btn-group">
                        <div class="btn btn-default md" id="confirmDialogCancel">取 消</div>
                        <div class="btn btn-primary md" id="confirmDialogConfirm">确 认</div>
                    </div>
                </div>
                <div class="dialog dialog-alert dialog-coupon" id="couponDialog">
                    <div class="title">请选择优惠券</div>
                    <div class="content">
                        <div class="alert-coupon-content">
							<div id="available"></div>
                            <div class="cutline"></div>
                            <div id="unavailable"></div>
                        </div>
                    </div>
                    <div class="btn-group">
                        <div class="btn btn-default md" id="couponDialogCancel">取 消</div>
                        <div class="btn btn-primary md" id="couponDialogConfirm">确 认</div>
                    </div>
                </div>
            </form>
            </c:if>  
            <!-- end 内容区域 -->     
        </div>
        <div class="container">
            <section class="content">
                <div class="main-tab">
                    <ul class="tab-tags">
                        
						<!-- 融通宝展示不同 -->
						<li panel="0" class="active">
							<a href="javascript:;">
								<c:if test="${projectDeatil.type eq 13}">项目详情</c:if>
								<c:if test="${projectDeatil.type ne 13}">风控信息</c:if>
							</a>
						</li>
						<!-- 融通宝展示不同 -->
						<c:if test="${projectDeatil.type ne 13}">
							<!-- 已登录 -->
							<c:if test="${loginFlag eq 1}">
								<c:if test="${projectDeatil.status eq 10 or projectDeatil.status eq 11 or projectDeatil.status eq 12 }">
									<li panel="1"><a href="javascript:;">相关文件</a></li>
								</c:if>
							</c:if>
							<c:if test="${projectDeatil.status eq 13}">
								<li panel="1"><a href="javascript:;">相关文件</a></li>
							</c:if>
							<!-- 汇消费 -->
							<c:if test="${projectDeatil.type eq 8}">
								<li panel="5"><a href="javascript:;">债权信息</a></li>
							</c:if>
							<li panel="2"><a href="javascript:;">还款计划</a></li>
						</c:if>
						<li panel="3"><a href="javascript:;">投资记录</a></li>
						<li panel="4"><a href="javascript:;">常见问题</a></li>

                    </ul>
                    <ul class="tab-panels">
                    <li panel="0" class="active">
                    	<!-- 登录状态 -->
                       	<c:if test="${loginFlag eq '1' }"> 
							<c:choose>
								<c:when test="${projectDeatil.type eq 13 && projectDeatil.isNew == 0}">
									<div class="attr-title"><span>项目流程</span></div>
									<div class="attr-table"><img alt="项目流程图" src="${cdn}/img/project/hjs_flow.png"></div>
								</c:when>
								<c:when test="${projectDeatil.type eq 13 && projectDeatil.isNew == 1}">
									<div class="attr-title"><span> 项目流程图</span></div>
					                <div class="attr-table">
						                <div class="flow-content">
						                    <div class="item done">
						                        <div class="icon icon-sh"></div>审核</div>
						                    <div class="line done"></div>
						                    <div class="item done">
						                        <div class="icon icon-fb"></div>信息发布</div>
						                    <div class="line done"></div>
						                    <div class="item done">
						                        <div class="icon icon-tz"></div>投资</div>
						                    <div class="line done"></div>
						                    <div class="item done">
						                        <div class="icon icon-jx"></div>计息</div>
						                    <div class="line done"></div>
						                    <div class="item done">
						                        <div class="icon icon-hk"></div>回款</div>
						                </div>
						            </div>
								</c:when>
								<c:when test="${projectDeatil.type eq 6}">
									<div class="attr-title"><span>项目流程</span></div>
									<div class="attr-table"><img alt="项目流程图" src="${cdn}/img/00-02.png"></div>
								</c:when>
								<c:when test="${projectDeatil.type ne 13 && projectDeatil.type ne 6}">
									<div class="attr-title"><span>项目流程</span></div>
									<div class="attr-table"><img alt="项目流程图" src="${cdn}/dist/images/product/product_old_flow.png"></div>
								</c:when>
							</c:choose>
							
	                        <!-- 融通宝展示内容 -->
							<c:if test="${projectDeatil.type eq 13}">
								<!--投资中-->
								<c:if test="${loginFlag eq '1' and (projectDeatil.status eq 10 or projectDeatil.status eq 11) }">
										<div class="attr-title"><span>项目信息</span></div>
										<div class="attr-table">
										<table cellspacing="0" cellpadding="0">
										<tbody>
												<tr>
													<td colspan="2" width="100%">
														<span class="key">项目名称</span>
														<span class="val">${projectDeatil.borrowAssetNumber}</span>
													</td>
												</tr>
												<tr>
													<td colspan="2" width="100%">
														<span class="key">项目来源</span>
														<span class="val">${projectDeatil.borrowProjectSource}</span>
													</td>
												</tr>
												<tr>
													<td colspan="2" width="100%">
														<span class="key">募集金额</span>
														<span class="val"><fmt:formatNumber value="${projectDeatil.borrowAccount}" pattern="#,###" />元</span>
													</td>
												</tr>
												<tr>
													<td colspan="2" width="100%">
														<span class="key">历史年回报率</span>
														<span class="val">${projectDeatil.borrowApr}% + ${projectDeatil.borrowExtraYield}%</span>
													</td>
												</tr>
												<tr>
													<td colspan="2" width="100%">
														<span class="key">加入条件</span>
														<span class="val">
															<fmt:formatNumber value="${projectDeatil.tenderAccountMin}" pattern="#,###" />元起投,
															<fmt:formatNumber value="${projectDeatil.increaseMoney}" pattern="#,###" />元递增
														</span>
													</td>
												</tr>
												<tr>
													<td colspan="2" width="100%">
														<span class="key">投资限额</span>
														<span class="val"><fmt:formatNumber value="${projectDeatil.tenderAccountMax}" pattern="#,###" />元</span>
													</td>
												</tr>
												<tr>
													<td colspan="2" width="100%">
														<span class="key">发布时间</span>
														<span class="val">${projectDeatil.sendTime}</span>
													</td>
												</tr>
												<tr>
													<td colspan="2" width="100%">
														<span class="key">起息日期</span>
														<span class="val">${projectDeatil.borrowInterestTime}</span>
													</td>
												</tr>
												<tr>
													<td colspan="2" width="100%">
														<span class="key">到期日期</span>
														<span class="val">${projectDeatil.borrowDueTime}</span>
													</td>
												</tr>
												<tr>
													<td colspan="2" width="100%">
														<span class="key">保障方式</span>
														<span class="val">${projectDeatil.borrowSafeguardWay}</span>
													</td>
												</tr>
												<tr>
													<td colspan="2" width="100%">
														<span class="key">收益说明</span>
														<span class="val">${projectDeatil.borrowIncomeDescription}</span>
													</td>
												</tr>
										</tbody>
										</table>
										</div>
								</c:if>
								<!-- 未登录状态 -->
								<c:if test="${loginFlag eq '0' }">
									<div class="unlogin">
										<div class="icon"></div>
										<p>请先 <a href="${ctx}/user/login/init.do">登录</a> 或 <a href="${ctx}/user/regist/init.do">注册</a> 后可查看</p>
									</div>
								</c:if>
								<c:if test="${ loginFlag eq '1' and (projectDeatil.status eq 12 or projectDeatil.status eq 13 or projectDeatil.status eq 14) }">
									<c:if test="${investFlag eq 1}">
										<div class="attr-title"><span>项目信息</span></div>
										<div class="attr-table">
										<table cellspacing="0" cellpadding="0">
											<tbody>
											<tr>
												<td colspan="2" width="100%">
													<span class="key">项目名称</span>
													<span class="val">${projectDeatil.borrowAssetNumber}</span>
												</td>
											</tr>
											<tr>
												<td colspan="2" width="100%">
													<span class="key">项目来源</span>
													<span class="val">${projectDeatil.borrowProjectSource}</span>
												</td>
											</tr>
											<tr>
												<td colspan="2" width="100%">
													<span class="key">募集金额</span>
													<span class="val"><fmt:formatNumber value="${projectDeatil.borrowAccount}" pattern="#,###" />元</span>
												</td>
											</tr>
											<tr>
												<td colspan="2" width="100%">
													<span class="key">历史年回报率</span>
													<span class="val">${projectDeatil.borrowApr}% + ${projectDeatil.borrowExtraYield}%</span>
												</td>
											</tr>
											<tr>
												<td colspan="2" width="100%">
													<span class="key">加入条件</span>
													<span class="val">
															<fmt:formatNumber value="${projectDeatil.tenderAccountMin}" pattern="#,###" />元起投,
															<fmt:formatNumber value="${projectDeatil.increaseMoney}" pattern="#,###" />元递增
														</span>
												</td>
											</tr>
											<tr>
												<td colspan="2" width="100%">
													<span class="key">投资限额</span>
													<span class="val"><fmt:formatNumber value="${projectDeatil.tenderAccountMax}" pattern="#,###" />元</span>
												</td>
											</tr>
											<tr>
												<td colspan="2" width="100%">
													<span class="key">发布时间</span>
													<span class="val">${projectDeatil.sendTime}</span>
												</td>
											</tr>
											<tr>
												<td colspan="2" width="100%">
													<span class="key">起息日期</span>
													<span class="val">${projectDeatil.borrowInterestTime}</span>
												</td>
											</tr>
											<tr>
												<td colspan="2" width="100%">
													<span class="key">到期日期</span>
													<span class="val">${projectDeatil.borrowDueTime}</span>
												</td>
											</tr>
											<tr>
												<td colspan="2" width="100%">
													<span class="key">保障方式</span>
													<span class="val">${projectDeatil.borrowSafeguardWay}</span>
												</td>
											</tr>
											<tr>
												<td colspan="2" width="100%">
													<span class="key">收益说明</span>
													<span class="val">${projectDeatil.borrowIncomeDescription}</span>
												</td>
											</tr>
											</tbody>
										</table>
									</div>
									</c:if>
									<c:if test="${investFlag eq '0' }">
										<div class="unlogin">
											<div class="icon"></div>
											<p>仅此项目的投资者可查看</p>
										</div>
									</c:if>
								</c:if>
							</c:if>


							<!-- 汇保贷、汇典贷、汇小贷 、汇车贷、  实鑫车 、新手汇 、尊享汇、汇房贷 --> 
							<c:if test="${projectDeatil.type eq 0 || projectDeatil.type eq 1 || projectDeatil.type eq 2 ||
							              projectDeatil.type eq 3 || projectDeatil.type eq 4 || projectDeatil.type eq 5 ||
							              projectDeatil.type eq 6 || projectDeatil.type eq 7 || projectDeatil.type eq 14 || 
							              projectDeatil.type eq 10 || projectDeatil.type eq 11 || projectDeatil.type eq 12}">     
								<!--  comOrPer 项目是个人项目还是企业项目 1企业 2个人  -->
									<div class="attr-title"><span>基础信息</span></div>
		                            <div class="attr-table">
										<c:if test="${projectDeatil.comOrPer eq 1}">
											<table cellspacing="0" cellpadding="0">
			                          	  	<tbody>
				                                <tr>
				                                    <td width="50%">
				                                    	<span class="key">所在地区</span>
				                                    	<span class="val">${borrowInfo.borrowAddress}</span>
				                                    </td>
				                                    <td width="50%">
				                                    	<span class="key">注册资本</span>
				                                    	<span class="val"><fmt:formatNumber value="${borrowInfo.regCaptial}" pattern="#,###" />元</span>
				                                    </td>
				                                </tr>
				                            	<tr>
				                                    <td  width="50%">
				                                    	<span class="key">注册时间</span>
				                                    	<span class="val">${borrowInfo.registTime}</span>
				                                    </td>
				                                    <td width="50%">
				                                    	<span class="key">信用评级</span>
				                                    	<span class="val">${projectDeatil.borrowLevel}</span>
				                                    </td>		                                    
				                            	</tr>
				                            	<c:if test="${projectDeatil.type eq 11 || projectDeatil.type eq 5 || projectDeatil.type eq 7 
				                            		|| projectDeatil.type eq 14 || projectDeatil.type eq 10 }"> 
					                            	<tr>
					                                    <td  width="50%">
					                                    	<span class="key">所属行业</span>
					                                    	<span class="val">${borrowInfo.borrowIndustry}</span>
					                                    </td>
					                            	</tr>
				                            	</c:if>
			                         	  	 </tbody>
				                       		 </table>
			                       		 </c:if>
			                       		<c:if test="${projectDeatil.comOrPer eq 2}"><!--  comOrPer 项目是个人项目还是企业项目 1企业 2个人  -->
											<table cellspacing="0" cellpadding="0">
			                          	  	<tbody>
				                                <tr>
				                                    <td width="50%">
				                                    	<span class="key">性别</span>
				                                    	<span class="val">${borrowInfo.sex}</span>
				                                    </td>
				                                    <td width="50%">
				                                    	<span class="key">年龄</span>
				                                    	<span class="val">${borrowInfo.age}</span>
				                                    </td>
				                                </tr>
				                            	<tr>
				                                    <td  width="50%">
				                                    	<span class="key">婚姻状况</span>
				                                    	<span class="val">${borrowInfo.maritalStatus}</span>
				                                    </td>
				                                    <td width="50%">
				                                    	<span class="key">工作城市</span>
				                                    	<span class="val">${borrowInfo.workingCity}</span>
				                                    </td>		                                    
				                            	</tr>
				                            	<tr>
				                                    <td  width="50%">
				                                    	<span class="key">岗位职业</span>
				                                    	<span class="val">${borrowInfo.position}</span>
				                                    </td>
				                                    <td width="50%">
				                                    	<span class="key">信用评级</span>
				                                    	<span class="val">${projectDeatil.borrowLevel}</span>
				                                    </td>		                                    
				                            	</tr>
			                         	  	 </tbody>
				                       		 </table>		                       		
			                       		</c:if>
									</div>	
									<!-- 汇车贷、  实鑫车 、新手汇、尊享汇 --> 
									<c:if test="${ projectDeatil.type eq 3 || projectDeatil.type eq 12 || projectDeatil.type eq 4 || 
									 			   projectDeatil.type eq 11}">   
										<c:if test="${not empty vehiclePledgeList and vehiclePledgeList ne null}">
											<div class="attr-title"><span>车辆信息</span></div>
				                            <div class="attr-table">
			                          	  		<c:forEach items="${vehiclePledgeList}" varStatus="status"  var="vehiclePledge">
			                          	  		<c:if test="${status.index gt 0}"><div class="cutline"></div></c:if>
			                          	  		<table cellspacing="0" cellpadding="0">
					                          	  	<tbody>
							                                <tr>
							                                    <td width="50%">
							                                    	<span class="key">车辆品牌</span>
							                                    	<span class="val">${vehiclePledge.vehicleBrand}</span>
							                                    </td>
							                                    <td width="50%">
							                                    	<span class="key">品牌型号</span>
							                                    	<span class="val">${vehiclePledge.vehicleModel}</span>
							                                    </td>
							                                </tr>
							                                <tr>
							                                    <td width="50%">
							                                    	<span class="key">产地</span>
							                                    	<span class="val">${vehiclePledge.place}</span>
							                                    </td>
							                                    <td width="50%">
							                                    	<span class="key">评估价值</span>
							                                    	<span class="val"><fmt:formatNumber value="${vehiclePledge.evaluationPrice}" pattern="#,###.00" /></span>
							                                    </td>
							                                </tr>
							                                </tbody>
						                        	</table>
					                             </c:forEach>
											</div>	
										</c:if>
									</c:if>
									
									<c:if test="${projectDeatil.type eq 0 || projectDeatil.type eq 1 || projectDeatil.type eq 2 ||
							              projectDeatil.type eq 3 || projectDeatil.type eq 12 || projectDeatil.type eq 4 || 
							              projectDeatil.type eq 11 || projectDeatil.type eq 5 || projectDeatil.type eq 7 ||
							              projectDeatil.type eq 14 || projectDeatil.type eq 10}">
								        <c:if test="${borrowInfo.borrowContents ne null and borrowInfo.borrowContents ne '' }">	
											<div class="attr-title"><span>项目介绍</span></div>
				                            <div class="attr-note" style="color:#7F7F80;">
					                       		 ${borrowInfo.borrowContents}
											</div>	
										</c:if>
										<c:if test="${!empty riskControl.controlMeasures and projectDeatil.type ne 10 }">
											<div class="attr-title"><span>风控措施</span></div>
				                            <div class="attr-note" style="color:#7F7F80;">
					                              ${riskControl.controlMeasures}
											</div>
										</c:if>	
							        </c:if>
							        
							        <!-- 供应贷  只有企业借款展示项目介绍--> 
									<c:if test="${projectDeatil.type eq 6}">
								        <c:if test="${borrowInfo.borrowContents ne null and borrowInfo.borrowContents ne '' }">	
											<div class="attr-title"><span>项目介绍</span></div>
				                            <div class="attr-note" style="color:#7F7F80;">
					                       		 ${borrowInfo.borrowContents}
											</div>	
										</c:if>
										<c:if test="${!empty riskControl.controlMeasures}">
											<div class="attr-title"><span>风控措施</span></div>
				                            <div class="attr-note" style="color:#7F7F80;">
					                              ${riskControl.controlMeasures}
											</div>
										</c:if>									
									</c:if>
							</c:if> 
	
							<!-- 汇消费 --> 
							<c:if test="${projectDeatil.type eq 8}">
								<div class="attr-title"><span>基础信息</span></div>
		                           <div class="attr-table">
									<table cellspacing="0" cellpadding="0">
		                         	  	<tbody>
		                                <tr>
		                                    <td width="50%">
		                                    	<span class="key">所在地区</span>
		                                    	<span class="val">${borrowInfo.borrowAddress}</span>
		                                    </td>
		                                    <td width="50%">
		                                    	<span class="key">注册资本</span>
		                                    	<span class="val"><fmt:formatNumber value="${borrowInfo.regCaptial}" pattern="#,###" />元</span>
		                                    </td>
		                                </tr>
		                            	<tr>
		                                    <td  width="50%">
		                                    	<span class="key">注册时间</span>
		                                    	<span class="val">${borrowInfo.registTime}</span>
		                                    </td>
		                                    <td width="50%">
		                                    	<span class="key">信用评级</span>
		                                    	<span class="val">${projectDeatil.borrowLevel}</span>
		                                    </td>		                                    
		                            	</tr>
		                            	<tr>
		                                    <td  width="50%">
		                                    	<span class="key">所属行业</span>
		                                    	<span class="val">${borrowInfo.borrowIndustry}</span>
		                                    </td>
		                            	</tr>
		                        	  	 </tbody>
		                       		 </table>
								 </div>	
	 					         <c:if test="${borrowInfo.borrowContents ne null and borrowInfo.borrowContents ne '' }">	
									<div class="attr-title"><span>项目介绍</span></div>
		                            <div class="attr-note" style="color:#7F7F80;">
			                       		 ${borrowInfo.borrowContents}
									</div>	
								 </c:if>
	 					         <c:if test="${borrowInfo.accountContents ne null and borrowInfo.accountContents ne '' }">	
									<div class="attr-title"><span>财务状况</span></div>
		                            <div class="attr-note" style="color:#7F7F80;">
			                       		 ${borrowInfo.accountContents}
									</div>	
								 </c:if>
							</c:if> 
							
							<!-- 汇资产 --> 
							<c:if test="${projectDeatil.type eq 9}">
								
								<div class="attr-title"><span>基础信息</span></div>
		                           <div class="attr-table">
									<table cellspacing="0" cellpadding="0">
		                         	  	<tbody>
		                                <tr>
		                                    <td width="50%">
		                                    	<span class="key">项目名称</span>
		                                    	<span class="val">${borrowInfo.borrowName}</span>
		                                    </td>
		                                    <td width="50%">
		                                    	<span class="key">项目类型</span>
		                                    	<span class="val">${borrowInfo.borrowType}</span>
		                                    </td>
		                                </tr>
		                            	<tr>
		                                    <td  width="50%">
		                                    	<span class="key">所在地区</span>
		                                    	<span class="val">${borrowInfo.borrowAddress}</span>
		                                    </td>
		                                    <td width="50%">
		                                    	<span class="key">评估价值</span>
		                                    	<span class="val">${borrowInfo.guarantyValue}</span>
		                                    </td>		                                    
		                            	</tr>
		                            	<tr>
		                                    <td width="50%">
		                                    	<span class="key">资产成因</span>
		                                    	<span class="val">${borrowInfo.assetOrigin}</span>
		                                    </td>
		                                    <td  width="50%">
		                                    	<span class="key">信用评级</span>
		                                    	<span class="val">${projectDeatil.borrowLevel}</span>
		                                    </td>
		                            	</tr>
		                        	  	 </tbody>
		                       		 </table>
								 </div>	
	
								<div class="attr-title"><span>资产信息</span></div>
	                            <div class="attr-note" style="color:#7F7F80;">
		                       		 ${borrowInfo.attachmentInfo}
								</div>	
								<div class="attr-title"><span>处置结果预案</span></div>
	                            <div class="attr-note" style="color:#7F7F80;">
		                       		 ${disposalPlan.disposalPlan}
								</div>	
							</c:if>
						</c:if>
						<!-- 未登录状态 -->
                        <c:if test="${loginFlag eq '0' }">                           
                          	<div class="unlogin">
                              	<div class="icon"></div>
                              	<p>请先 <a href="${ctx}/user/login/init.do">登录</a> 或 <a href="${ctx}/user/regist/init.do">注册</a> 后可查看</p>
                          	</div>
                        </c:if>
					</li>
                       
                        <li panel="1">
                            <!-- 相关文件 -->
                            <div class="new-detail-img-c">
			                    <c:if test="${projectDeatil.status eq 10 or projectDeatil.status eq 11 or projectDeatil.status eq 12 }">
									<c:if test="${loginFlag eq 1}">
									     <ul>
										    <c:forEach items="${fileList}" var="file">
												<li><a href="${file.fileUrl}" data-caption="${file.fileName}">
													<div> <img src="${file.fileUrl}" alt=""> </div>
													<span class="title">${file.fileName}</span>
												</a></li>
											</c:forEach>                               
		                                 </ul>
									</c:if>
									<!-- 未登录状态 -->
			                        <c:if test="${loginFlag eq '0' }">                           
			                          	<div class="unlogin">
			                              	<div class="icon"></div>
			                              	<p>请先 <a href="${ctx}/user/login/init.do">登录</a> 或 <a href="${ctx}/user/regist/init.do">注册</a> 后可查看</p>
			                          	</div>
			                        </c:if>
								</c:if>
	                            <c:if test="${projectDeatil.status eq 13 }">
	                            	<c:if test="${investFlag eq 1}">
		                                <ul>
										    <c:forEach items="${fileList}" var="file">
												<li><a href="${file.fileUrl}" data-caption="${file.fileName}">
													<div> <img src="${file.fileUrl}" alt=""> </div>
													<span class="title">${file.fileName}</span>
												</a></li>
											</c:forEach>                               
		                                 </ul>
		                            </c:if>
		                            <c:if test="${investFlag eq 0}">
		                                <span>只有投资本项目的用户可见详细信息</span>
		                            </c:if>
	                            </c:if>
                            </div>
                        </li>
                        <li panel="2" class="">
                            <!-- 还款计划 -->
                            <p><span class="dark" style="padding-left:70px;">还款时间在标的放款后生成</span></p>
                            <div class="attr-table">
                                <table cellpadding="0" cellspacing="0">
                                    <thead>
                                        <tr>
                                            <th width="35%">还款时间</th>
                                            <th width="25%">还款期数</th>
                                            <th width="15%">类型</th>
                                            <th width="25%">还款金额</th>
                                        </tr>
                                    </thead>
                                    <tbody>
										<c:forEach items="${repayPlanList}" var="repayPlan">
											<tr>
												<td><span class="dark">${repayPlan.repayTime}</span></td>
												<td>第${repayPlan.repayPeriod}期</td>
												<td>${repayPlan.repayType}</td>
												<td><fmt:formatNumber value="${repayPlan.repayTotal}" pattern="#,###.00" /></td>
											</tr>
										</c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </li>
                        <li panel="3" class="">
                            <!-- 投资记录 -->
                            <p>
                                <span id="investTimes">加入总人次： 358</span>&nbsp;&nbsp;
                                <span id="investTotal">加入金额：5737000元</span>
                            </p>
                            <div class="attr-table">
                                <table cellpadding="0" cellspacing="0">
                                    <thead>
                                        <tr>
                                            <th width="35%">投资人</th>
                                            <th width="25%">加入金额（元）</th>
                                            <th width="15%">来源</th>
                                            <th width="25%">加入时间</th>
                                        </tr>
                                    </thead>
                                    <tbody id="projectInvestList">
                                    </tbody>
                                </table>
                                <div class="pages-nav" id="invest-pagination"></div>
                            </div>
                        </li>
						<c:if test="${projectDeatil.type eq 8}"><!-- 汇消费债权列表 -->
							<li panel="5">
	                            <div class="attr-table">
	                                <table cellpadding="0" cellspacing="0">
	                                    <thead>
	                                        <tr>
	                                            <th width="35%">姓名</th>
	                                            <th width="40%">身份证</th>
	                                            <th width="25%">融资金额</th>
	                                        </tr>
	                                    </thead>
	                                    <tbody id="projectConsumeList">
	                                    </tbody>
	                                </table>
	                                <div class="pages-nav" id="consume-pagination"></div>
	                            </div>
							</li>
						</c:if>
                        <li panel="4">
                            <!-- 常见问题 -->
                            <div class="attr-table">
	                           	<c:if test="${projectDeatil.type eq 13}">  <!-- 融通宝展示 常见问题 -->
		                        	<table cellpadding="0" cellspacing="0">
	                                    <tbody>
	                                        <tr>
	                                            <td width="100%">
	                                                <span class="dark">1、什么是地方金融资产交易所？</span>
	                                            </td>
	                                        </tr>
	                                        <tr>
	                                            <td>地方金融资产交易所是由地方省委省政府批准设立，并由地方政府金融办监管的，为金融产品提供登记、托管、交易和结算等提供场所设施和服务的组织机构。</td>
	                                        </tr>
	                                        <tr>
	                                            <td width="100%">
	                                                <span class="dark">2、什么是“优选债权”？</span>
	                                            </td>
	                                        </tr>
	                                        <tr>
	                                            <td>“优选债权”是与地方金融资产交易所合作推出的资产交易类产品，资产持有人通过汇盈金服居间撮合，将资产或其收益权转让给出借方，出借方获取一定数额的投资收益。</td>
	                                        </tr>
	                                        <tr>
	                                            <td width="100%">
	                                                <span class="dark">3、“优选债权”的特点是什么？</span>
	                                            </td>
	                                        </tr>
	                                        <tr>
	                                            <td>“优选债权”发布的产品是在地方金融资产交易所挂牌的项目，项目均经过金交所严格的审核，并提供专业的风控措施，保证项目的优质性。出借人可根据自身投资偏好选择适合项目投资。</td>
	                                        </tr>
	                                        <tr>
	                                            <td width="100%">
	                                                <span class="dark">4、我可以认购“优选债权”产品吗？</span>
	                                            </td>
	                                        </tr>
	                                        <tr>
	                                            <td>“优选债权”下每支产品的出借人数不能超过200人；出借人持有本人中华人民共和国居民身份证的公民，且年满十八周岁。</td>
	                                        </tr>
	                                        <tr>
	                                            <td width="100%">
	                                                <span class="dark">5、认购“优选债权”产品需要收费吗？</span>
	                                            </td>
	                                        </tr>
	                                        <tr>
	                                            <td>出借人暂时无需支付认购费、管理费。</td>
	                                        </tr>
	                                        <tr>
	                                            <td width="100%">
	                                                <span class="dark">6、出借人风险测评目的和规则是什么？</span>
	                                            </td>
	                                        </tr>
	                                        <tr>
	                                            <td>为完善对出借人风险承受能力的尽职评估，实现对出借人的等级管理，保障出借人购买合适的产品，根据出借人风险测评的结果，将出借人风险承受能力由低到高分为保守型、稳健型、成长型、进取型四种类型。</td>
	                                        </tr>
	                                    </tbody>
	                                </table>
		                        </c:if>
	                           	<c:if test="${projectDeatil.type ne 13}">
		                        	<table cellpadding="0" cellspacing="0">
	                                    <tbody>
	                                        <tr>
	                                            <td width="100%">
	                                                <span class="dark">1、我可以投资吗？</span>
	                                            </td>
	                                        </tr>
	                                        <tr>
	                                            <td>持有本人中华人民共和国居民身份证的公民，且年满十八周岁，都可在汇盈金服网站上进行注册、完成实名认证，成为出借人。</td>
	                                        </tr>
	                                        <tr>
	                                            <td width="100%">
	                                                <span class="dark">2、怎样进行投资？</span>
	                                            </td>
	                                        </tr>
	                                        <tr>
	                                            <td>请您按照以下步骤进行投资：
	                                                <br/> 1. 在汇盈金服网站或手机客户端上进行注册、通过实名认证、成功绑定银行卡；
	                                                <br/> 2. 完成出借人风险测评；
	                                                <br/> 3. 账户充值；
	                                                <br/> 4. 浏览平台借款项目，根据个人风险偏好自主选择项目投资；
	                                                <br/> 5. 确认投资，投资成功。</td>
	                                        </tr>
	                                        <tr>
	                                            <td width="100%">
	                                                <span class="dark">3、投资后是否可以提前退出？</span>
	                                            </td>
	                                        </tr>
	                                        <tr>
	                                            <td>1. 平台产品暂不支持提前回款申请。
	                                                <br/> 2. “债权”项目中除“优选债权”外，都支持债权转让。</td>
	                                        </tr>
	                                        <tr>
	                                            <td width="100%">
	                                                <span class="dark">4、为何投标后会显示资金冻结？</span>
	                                            </td>
	                                        </tr>
	                                        <tr>
	                                            <td>对于所有投资项目，出借方可自主选择进行投资。在项目完成放款之前，投资金额将被冻结；在项目完成放款之后，投资金额将通过江西银行转给借款方；如果在限定时间内未满标，则根据情况将已借款金放款给借款方或原路返还出借方。</td>
	                                        </tr>
	                                        <tr>
	                                            <td width="100%">
	                                                <span class="dark">5、在汇盈金服投资有哪些费用？</span>
	                                            </td>
	                                        </tr>
	                                        <tr>
	                                            <td>在汇盈金服平台进行投资，平台本身不收取出借方任何费用，出借方在充值/提现时江西银行会收取相关手续费。
	                                                <br/>**特别提示：提现的手续费的计算方式为1元/笔
	                                            </td>
	                                        </tr>
	                                        <tr>
	                                            <td width="100%">
	                                                <span class="dark">6、出借人风险测评目的和规则是什么？</span>
	                                            </td>
	                                        </tr>
	                                        <tr>
	                                            <td>为完善对出借方风险承受能力的尽职评估，实现对出借方的等级管理，保障出借方购买合适的产品，根据出借方风险测评的结果，将出借方风险承受能力由低到高分为保守型、稳健型、成长型、进取型四种类型。</td>
	                                        </tr>
	                                    </tbody>
	                                </table>
		                        </c:if>                           
                            </div>
                        </li>
                    </ul>
                </div>
            </section>
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
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="${cdn}/dist/js/lib/baguetteBox.min.js"></script>
	<script src="${cdn}/dist/js/lib/jquery.validate.js"></script>
    <script src="${cdn}/dist/js/lib/jquery.metadata.js"></script>
    <script type="text/javascript" src="${cdn}/dist/js/lib/jquery.jcountdown.min.js"></script>
    <script src="${cdn}/dist/js/product/product-detail.js?version=${version}"></script>
</body>
</html>