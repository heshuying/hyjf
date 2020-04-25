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
	<section class="breadcrumbs">
        <div class="container">
            <div class="left-side">您所在的位置：
            	<a href="${ctx}/">首页</a> &gt; <a href="${ctx}/plan/initPlanList.do">智投专区</a></a>&gt; 项目详情</div>
            	<div class="right-side"><span>开标时间：</span>${planDetail.buyBeginTime}</div>
        </div>
    </section>
    <article class="main-content product">
        <div class="container">
            <!-- start 内容区域 -->
            <div class="product-intr">
                <div class="title">
                    <span>${planDetail.planName}</span>
                    <div class="contract-box">
                    	<a href="${ctx}/agreement/goAgreementPdf.do?aliasName=jjfwjkxy" target="_blank">${jjfwjkxy }</a>
                        <a href="${ctx}/agreement/htjInvestmentPlanningServices.do?borrowNid=${planDetail.planNid}" target="_blank">《产品协议》</a>
                        <a href="${ctx}/agreement/RTBPlatformAgreement.do" target="_blank">《平台协议》</a>
                    </div>
                </div>
                <div class="attr">
                    <div class="attr-item attr1">
                        <span class="val highlight">${planDetail.planApr}<span class="unit"> %</span> </span>
                        <span class="key">历史年回报率</span>
                    </div>
                    <div class="attr-item attr2">
                        <span class="val">${planDetail.planPeriod}<span class="unit"> 个月</span> </span>
                        <span class="key">计划期限</span>
                    </div>
                    <div class="attr-item attr3">
                        <span class="val"><fmt:formatNumber value="${planDetail.planAccount }" pattern="#,###" /><span class="unit"> 元</span></span>
                        <span class="key">计划金额</span>
                    </div>
                </div>
                <div class="list">
                    <div class="list-item">起息日：计划进入锁定期后开始计息</div>
                    <div class="list-item">还款方式：到期一次性还本付息</div>
                    <div class="list-item">保障方式：安全保障计划</div>
                    <div class="list-item"><span class="dark">建议投资者类型：稳健型及以上</span></div>
                </div>
            </div>
            
            <!-- planStatus等于0显示timmer -->
         	<c:if test="${planDetail.planStatus eq 0}">
	            <div class="product-form status">
	                <div class="status-content">
	                    <div class="title">
	                       	 开标倒计时
	                    </div>
	                    <div class="timer">
	                        <div id="counterTimer" class="counter" data-date="${planDetail.timer}" data-now="${nowTime}">
	                            <div class="countdown"></div>
	                        </div>
	                    </div>
	                </div>
	                <div class="start-time">开标时间：${planDetail.buyBeginTime}</div>
	            </div>
             </c:if>
             
             <!-- planStatus等于2显示清算中 -->
             <c:if test="${planDetail.planStatus eq 2}">
	            <div class="product-form status">
	            	<!-- 锁定中  -->
		            <div class="status-content sdz"></div> 
	            </div>
            </c:if>
            
            <!-- planStatus等于3显示清算完成 -->
             <c:if test="${planDetail.planStatus eq 3}">
	            <div class="product-form status">
	            	<!-- 已退出  -->
		            <div class="status-content ytc"></div> 
	            </div>
            </c:if>
            
            <!-- planStatus等于1显示 form -->
            <input id="nid1" name="nid1" type="hidden" value="${planDetail.planNid }">
            <c:if test="${planDetail.planStatus eq 1}">
	            <form class="product-form" action="${ctx}/plan/planInvest.do" id="productForm" autocomplete="off">
	            	<input type="hidden" name="tokenCheck" id="tokenCheck" value="">
	                <input type="hidden" name="tokenGrant" id="tokenGrant" value="${tokenGrant}">
	                <input type="hidden" name="loginFlag" id="loginFlag" value="${loginFlag }" /> <!-- 登录状态 0未登陆 1已登录 -->
                	<input type="hidden" name="openFlag" id="openFlag" value="${openFlag }" /> <!-- 开户状态 0未开户 1已开户 -->
                	<input type="hidden" name="riskFlag" id="riskFlag" value="${riskFlag }" /> <!-- 是否进行过风险测评 0未测评 1已测评 -->
                	<input type="hidden" name="setPwdFlag" id="setPwdFlag" value="${setPwdFlag }" /> <!-- 是否设置过交易密码 0未设置 1已设置 -->
	                <input id="nid" name="nid" type="hidden" value="${planDetail.planNid }">
	                <input type="text" name="username" class="ignore fix-auto-fill"/>
	                <div class="field">
	                    <div class="key">项目剩余：</div>
	                    <div class="val"><span class="highlight"><fmt:formatNumber value="${planDetail.planAccountWait }" pattern="#,###.00" /></span> 元</div>
	                </div>             
	                <div class="field">
	                	<div class="key">可用金额：</div>
		                <c:if test="${loginFlag eq '1' }">
		                    <div class="val"><fmt:formatNumber value="${userBalance }" pattern="#,##0.00" /> 元</div>
		                    <a href="${ctx}/bank/web/user/recharge/rechargePage.do" class="link-recharge">充值</a>
		                 </c:if>
	                    <c:if test="${loginFlag eq '0' }">
		                    <div class="val">登录后可见</div>
		                    <a href="${ctx}/user/login/init.do" class="link-recharge" onclick="setCookie()">立即登录</a>
	                    </c:if>
	                </div>                
	                <div class="field">
	                    <div class="input">
	                        <input type="text" name="money" id="money" 
	                        placeholder='<fmt:formatNumber value="${planDetail.debtMinInvestment }" pattern="#,###" />元起投，<fmt:formatNumber value="${planDetail.debtInvestmentIncrement }" pattern="#,###" />元递增' oncopy="return false" onpaste="return false" oncut="return false" oncontextmenu="return false" autocomplete="off" maxlength="9" />
	                        <div class="btn sm" <c:if test="${planDetail.debtMinInvestment lt planDetail.planAccountWait}"> id="fullyBtn"</c:if>>全投</div>
	                        <input type="hidden" id="debtMinInvestment" name="debtMinInvestment" value="${planDetail.debtMinInvestment }">
	                        <input type="hidden" id="debtInvestmentIncrement" name="debtInvestmentIncrement" value="${planDetail.debtInvestmentIncrement }">
	                        <input type="hidden" id="debtMaxInvestment" name="debtMaxInvestment" value="${planDetail.debtMaxInvestment }">
	                        <input type="hidden" id="userBalance" name="userBalance" value="${userBalance}">
	                        <input type="hidden" id="planAccountWait" name="planAccountWait" value="${planDetail.planAccountWait }">
	                       
	                		<input type="hidden" id="increase" name="increase" value="${planDetail.debtInvestmentIncrement }"><!-- 递增金额 -->
	     					<input type="hidden" id="isLast" name="isLast" value="${planDetail.debtMinInvestment ge planDetail.planAccountWait}" /><!-- 是否最后一笔投资 -->
	     					<input type="hidden" id="projectData" name="projectData" data-total="${planDetail.planAccountWait}" data-tendermax="${planDetail.debtMaxInvestment}" data-tendermin="${planDetail.debtMinInvestment}"/>
	    
	                    </div>
	                </div>
	                <div class="field sub">
	                    <div class="key dark">优惠券：</div>
	                    
	                    <input type="hidden" name="couponGrantId" 
		                    value="<c:if test="${isThereCoupon==1}"> ${couponConfig.userCouponId}</c:if>" 
		                    data-type="${couponConfig.couponType}" data-count="${couponAvailableCount}" 
		                    data-id="<c:if test="${isThereCoupon==1}">${couponConfig.userCouponId}</c:if>" 
		                    data-txt="请选择优惠券" id="couponGrantId">
	                    
	                    <c:if test="${isThereCoupon==1}">
							
							<div class="val fl-r"><a href="javascript:;" class="link-coupon" id="goCoupon"><span>${couponConfig.couponQuotaStr }
								<c:if test="${couponConfig.couponType == 1}"> 元  </c:if> 
								<c:if test="${couponConfig.couponType == 2}"> % </c:if>
							    <c:if test="${couponConfig.couponType == 3}"> 元  </c:if>
								<c:if test="${couponConfig.couponType == 1}"> 体验金 </c:if> 
								<c:if test="${couponConfig.couponType == 2}"> 加息券 </c:if> 
								<c:if test="${couponConfig.couponType == 3}"> 代金券 </c:if></span>&gt;</a></div>
						</c:if>
						<c:if test="${isThereCoupon==0}">
							<!-- 是vip -->
								<div class="val fl-r"><a href="javascript:;" class="link-coupon" id="goCoupon"> 您有 ${couponAvailableCount}张优惠券可用&gt;</a></div>
						</c:if>
	                </div>
	                <div class="field sub">
	                    <div class="key dark">历史回报：</div>
	                    <div class="val fl-r" id="income">0.00 元</div>
	                </div>
	                <div class="field">
	                    <div class="btn submit" id="goSubmit">立即投资</div>
	                    <input id="nid" name="nid" type="hidden" value="${planDetail.planNid }">
	                </div>
	                <input type="checkbox" name="termcheck" class="form-term-checkbox" id="productTerm" checked="">
	                <div class="dialog dialog-alert" id="confirmDialog">
	                    <div class="title">投资确认</div>
	                    <div class="content">
	                        <table class="product-confirm-table" cellspacing="0" cellpadding="0">
	                            <tr>
	                                <td width="140" align="right"><span class="dark">项目编号：</span></td>
	                                <td><span id="prodNum">${planDetail.planNid}</span></td>
	                            </tr>
	                            <tr>
	                                <td align="right"><span class="dark">历史年回报率：</span></td>
	                                <td><span id="">${planDetail.planApr}%</span></td>
	                            </tr>
	                            <tr>
	                                <td align="right"><span class="dark">投资期限：</span></td>
	                                <td>${planDetail.planPeriod}个月</td>
	                            </tr>
	                            <tr>
	                                <td align="right"><span class="dark">计息方式：</span></td>
	                                <td>先息后本</td>
	                            </tr>
	                            <tr>
	                                <td align="right"><span class="dark">投资金额：</span></td>
	                                <td><span class="red" id="confirmmoney">49,234.00</span> 元</td>
	                            </tr>
	                            <tr>
	                                <td align="right"><span class="dark">优惠券：</span></td>
	                                <td id="confirmcoupon">1.00%加息券</td>
	                            </tr>
	                            <tr>
	                                <td align="right"><span class="dark">历史回报：</span></td>
	                                <td id='confirmincome'>0.00 元</td>
	                            </tr>
	                        </table>
	                        <div class="cutline"></div>
	                        <table class="product-confirm-table" cellspacing="0" cellpadding="0">
	                            <tr>
	                                <td width="140" align="right"><span class="dark">实际支付：</span></td>
	                                <td><span class="total" id="confirmpaymentmoney">49,234</span> 元</td>
	                            </tr>
	                        </table>
	                        <div class="product-term">
	                            <div class="term-checkbox checked"></div>
	                            <span>我已同意并阅读
                      			    <a href="${ctx}/agreement/transferOfCreditorRight.do" target="_blank">投资协议(范本)</a>
                       				<a href="${ctx}/agreement/confirmationOfInvestmentRisk.do" target="_blank">风险确认书(范本)</a>
                       			</span>
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
                <div class="main-title">
                    计划进度
                </div>
                <div class="flow-content plan">
                	
                    <div class='item <c:if test="${planDetail.planStatus>0}">done</c:if>'>
                        <div class="icon icon-sh"></div>
                        <span>汇计划发布</span>
                        <div class="time">&nbsp;</div>
                        <!--没有文字放&nbsp;占位-->
                    </div>
                    <div class='line <c:if test="${planDetail.planStatus>=1}">done</c:if>'></div>
                    <div class='item <c:if test="${planDetail.planStatus>=1}">done</c:if>'>
                        <div class="icon icon-fb"></div>
                        <span>开始加入</span>
                        <div class="time">${planDetail.buyBeginTimeFormat}</div>
                    </div>
                    <div class='line <c:if test="${planDetail.planStatus>=2}">done</c:if>'></div>
                    <div class='item <c:if test="${planDetail.planStatus>=2}">done</c:if>'>
                        <div class="icon icon-tz"></div>
                        <span>进入锁定期</span>
                        <div class="time">${planDetail.fullExpireTimeFormat}</div>
                    </div>
                    <div class='line <c:if test="${planDetail.planStatus==3}">done</c:if>''></div>
                    <div class='item <c:if test="${planDetail.planStatus==3}">done</c:if>'>
                        <div class="icon icon-jx"></div>
                        <span>到期退出</span>
                        <div class="time">${planDetail.liquidateFactTimeFormat}</div>
                    </div>
                </div>
            </section>
        </div>
        <div class="container">
            <section class="content">
                <div class="main-tab">
                    <ul class="tab-tags">
                        <li class="active" panel="0"><a href="javascript:;">计划介绍</a></li>
                        <li panel="1"><a href="javascript:;" class="investClass" data-page="1">债权列表</a></li>
                        <li panel="2"><a href="javascript:;" class="consumeClass" data-page="1">加入记录</a></li>
                        <li panel="3"><a href="javascript:;">安全保障</a></li>
                    </ul>
                    <input type="hidden">
                    <ul class="tab-panels">
                        <li class="active" panel="0">
                            <div class="attr-table" id="reviewTable">
                                <table cellpadding="0" cellspacing="0">
                                    <tbody>
                                        <tr>
                                            <td width="100%"><span class="key">计划编号</span> <span class="val">${planDetail.planNid}</span></td>
                                        </tr>
                                        <tr>
                                            <td width="100%"><span class="key">计划介绍</span> <span class="val">${planIntroduce.planConcept }</span></td>
                                        </tr>
                                        <tr>
                                            <td><span class="key">计划原理</span> <span class="val">${planIntroduce.planTheory }</span></td>
                                        </tr>
                                        <tr>
                                            <td><span class="key">投资范围</span> <span class="val">请参考债权列表</span></td>
                                        </tr>
                                        <tr>
                                            <td><span class="key">加入条件</span> <span class="val">${planIntroduce.planAccedeMinAccount } 元起，以 ${planIntroduce.planAccedeIncreaseAccount } 元的倍数递增</span></td>
                                        </tr>
                                        <tr>
                                            <td><span class="key">加入上限</span> <span class="val">${planIntroduce.planAccedeMaxAccount }元</span></td>
                                        </tr>
                                        <tr>
                                            <td><span class="key">历史回报</span> <span class="val">平台建议的历史年回报率${planDetail.planApr }％，实际收益以运营情况为准。</span></td>
                                        </tr>
                                        <tr>
                                            <td><span class="key">退出方式</span> <span class="val">系统将通过债权转让自动完成退出，您所持债权转让完成的具体时间，视债权转让市场交易情况而定。</span></td>
                                        </tr>
                                        <tr>
                                            <td><span class="key">退出日期</span> <span class="val">锁定中后${planDetail.planPeriod}个月，计划自动开始清算退出</span></td>
                                        </tr>
                                        <tr>
                                            <td><span class="key">到账时间</span> <span class="val">退出日期后，预计3个工作日内到账，具体到账时间，以实际运营为准。</span></td>
                                        </tr>
                                        <tr>
                                            <td><span class="key">服务费</span> <span class="val">参见《汇添金投资服务协议》</span></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </li>
                        <li panel="1" class="">
                            <!-- 债权列表 -->
                            <p style="padding-left:70px;">本计划投资的项目限汇添金专属资产和转让资产；债权列表动态变化，具体以实际投资为准。</p>
                            <div class="attr-table">
                                <table cellpadding="0" cellspacing="0">
                                	<thead>
                                        <tr>
                                            <th width="35%">项目编号</th>
                                            <th width="25%">历史年回报率</th>
                                            <th width="15%">项目期限</th>
                                            <th width="25%">还款方式</th>
                                        </tr>
                                    </thead>
		                            <tbody id="projectConsumeList">
                                    </tbody>
                                </table>
								<div class="pages-nav" id="consume-pagination"></div>
                            </div>
                        </li>
                        <li panel="2" class="">
                            <!-- 加入记录 -->
                            <p>
                                <span>加入总人次 : <strong  id="investTimes"></strong></span>&nbsp;&nbsp;
                                <span>加入金额 : <strong  id="investTotal"></strong>元</span>
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
                        <li panel="3">
                            <!-- 安全保障 -->
                            <div class="attr-table">
                                <table cellpadding="0" cellspacing="0">
                                    <tbody>
                                        <tr>
                                            <td>1、“汇计划”安全吗？</td>
                                        </tr>
                                        <tr>
                                            <td width="100%">
                                                <span class="dark">汇盈金服以严谨负责的态度对每笔借款进行严格筛选，同时，“汇计划”所对应借款均适用汇盈金服用户利益保障机制。</span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>2、“汇计划”的“锁定期”是什么？</td>
                                        </tr>
                                        <tr>
                                            <td width="100%">
                                                <span class="dark">“汇计划”出借计划具有收益期锁定限制，锁定期内，用户不可以提前退出。</span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>3、加入“汇计划”的用户所获收益处理方式有几种？</td>
                                        </tr>
                                        <tr>
                                            <td width="100%">
                                                <span class="dark">本计划提供两种收益处理方式：循环复投或用户自由支配。计划退出后，用户的本金和收益将返回至其汇盈金服账户中，供用户自行支配。</span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>4、“汇计划”通过何种方式实现自动投标？</td>
                                        </tr>
                                        <tr>
                                            <td width="100%">
                                                <span class="dark">“汇计划”不设立平台级别的中间账户，不归集出借人的资金，而是为出借人开启专属计划账户，所有资金通过该专属计划账户流动。</span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>5、“汇计划”到期后，我如何退出并实现收益?</td>
                                        </tr>
                                        <tr>
                                            <td width="100%">
                                                <span class="dark">“汇计划”到期后，系统将自动进行资金结算，结算完成后的本金及收益将从用户“汇计划”账户自动划转至用户普通账户中，用户在T+3个工作日内收到资金。</span>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </li>
                    </ul>
                </div>
            </section>
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="${cdn}/dist/js/lib/jquery.jcountdown.min.js"></script>
    <script src="${cdn}/dist/js/lib/jquery.validate.js"></script>
    <script src="${cdn}/dist/js/lib/jquery.metadata.js"></script>
    <script src="${cdn}/dist/js/product/product-plan-detail.js?version=${version}"></script>
    
</body>
</html>