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
            <div class="left-side">您所在的位置： <a href="${ctx}/">首页</a> &gt; <a href="${ctx}/bank/web/borrow/initBorrowList.do">债权</a> &gt; <a href="${ctx}/bank/user/credit/initWebCreditPage.do">债权转让</a> &gt; 项目详情</div>
        </div>
    </section>
    <article class="main-content product">
        <div class="container">
        	
            <!-- start 内容区域 -->
            <div class="product-intr">
                <div class="title">
                    <span>债权转让项目 </span>
                    <input type="hidden" name="creditNid" id="creditNid" value="${creditResult.data.creditTender.creditNid}" />
                    <div class="contract-box">
                        <a href="${ctx}/agreement/transferOfCreditorRight.do">《债权转让协议(范本)》</a>
                        <a href="${ctx}/agreement/confirmationOfInvestmentRisk.do">《风险确认书(范本)》</a>
                    </div>
                	<input type="hidden" name="userBalance" id="userBalance" value="${creditResult.data.balance}" />
                	<input type="hidden" name="borrowNid" id="borrowNid" value="${creditResult.data.borrow.borrowNid}" />
                	<input type="hidden" name="loginFlag" id="loginFlag" value="${loginFlag }" /> <!-- 登录状态 0未登陆 1已登录 -->
                	<input type="hidden" name="openFlag" id="openFlag" value="${openFlag }" /> <!-- 开户状态 0未开户 1已开户 -->
                	<input type="hidden" name="riskFlag" id="riskFlag" value="${riskFlag }" /> <!-- 是否进行过风险测评 0未测评 1已测评 -->
                	<input type="hidden" name="setPwdFlag" id="setPwdFlag" value="${setPwdFlag }" /> <!-- 是否设置过交易密码 0未设置 1已设置 -->
                	<input type="hidden" name="isUserValid" id="isUserValid" value="${isUserValid }" /> <!-- 是否禁用 1 禁用  0 未禁用 -->
                	<input type="hidden" name="increase" id="increase" value="1" /> <!-- 递增金额 -->
                	<input type="hidden" name="isLast" id="isLast" value="0" /> <!-- 是否最后一笔投资 -->
                	<input type="hidden" id="projectData" data-total="${creditResult.data.creditTender.creditAssignCapital}" data-tendermax="${creditResult.data.creditTender.creditAssignCapital}" data-tendermin="1"/>
                    <span>${projectDeatil.projectName}</span>
                </div>
                <div class="attr transfer">
                    <div class="attr-item attr4">
                        <span class="val highlight">${creditResult.data.creditTender.borrowApr}<span class="unit"> %</span> </span>
                        <span class="key">历史年回报率</span>
                    </div>
                    <%-- <div class="attr-item attr5">
                        <span class="val highlight">${creditResult.data.creditTender.creditDiscount}<span class="unit"> %</span> </span>
                        <span class="key">折让率</span>
                    </div> --%>
                    <div class="attr-item attr6">
                        <span class="val">${creditResult.data.creditTender.creditTerm}<span class="unit"> 天</span> </span>
                        <span class="key">剩余期限</span>
                    </div>
                    <div class="attr-item attr7">
                        <span class="val">${creditResult.data.creditTender.creditCapital}<span class="unit"> 元</span></span>
                        <span class="key">出让金额</span>
                    </div>

                </div>
                <div class="list">
                
                <c:if test="${typeFlag eq 0}">
	                <div class="list-item">项目编号：<a href="${ctx}/project/getHtjProjectDetail.do?borrowNid=${creditResult.data.borrow.borrowNid}" class="normal-a highlight">${creditResult.data.borrow.borrowNid}</a></div>
                </c:if>
                
                <c:if test="${typeFlag ne 0}">
	                <div class="list-item">项目编号：<a href="${ctx}/bank/web/borrow/getBorrowDetail.do?borrowNid=${creditResult.data.borrow.borrowNid}" class="normal-a highlight">${creditResult.data.borrow.borrowNid}</a></div>
                </c:if>
                
                    
                    <div class="list-item">还款方式：
	                    <c:if test="${creditResult.data.creditTender.borrowStyle=='month'}">
							等额本息
							</c:if>
							<c:if test="${creditResult.data.creditTender.borrowStyle=='principal'}">
							等额本金
							</c:if>
							<c:if test="${creditResult.data.creditTender.borrowStyle=='end'}">
							按月计息，到期还本还息
							</c:if>
							<c:if test="${creditResult.data.creditTender.borrowStyle=='endday'}">
							按天计息，到期还本还息
							</c:if>
							<c:if test="${creditResult.data.creditTender.borrowStyle=='endmonth'}">
							先息后本
						</c:if>
					</div>
                    <div class="list-item">债权持有天数：${creditResult.data.creditTender.creditTermHold}天</div>
                    <div class="list-item"><span class="dark">建议投资者类型：稳健型及以上</span></div>
                </div>
            </div>
            <form class="product-form transfer" action="${ctx}/bank/user/credit/websubmitcredittenderassign.do" id="productForm">
				<input type="hidden" id="borrowNid" name="borrowNid" value="${creditResult.data.creditTender.borrowNid}">
                <input type="hidden" id="creditNid" name="creditNid" value="${creditResult.data.creditTender.creditNid}">
                
                <c:if test="${typeFlag ne 0}">
                <div class="field">
                    <div class="key">项目剩余：</div>
                    <div class="val"><span class="highlight"><fmt:formatNumber value="${creditResult.data.creditTender.creditAssignCapital}" pattern="#,##0.00"/></span> 元</div>
                </div>
                <div class="field">
                	<c:if test="${creditResult.data.userId!=null && creditResult.data.userId!='' && creditResult.data.userId!=0}">
	                    <div class="key">可用金额：</div>
	                    <c:if test="${openFlag eq 1 }">
	                    	<div class="val"><fmt:formatNumber value="${creditResult.data.balance}" pattern="#,##0.00" /> 元</div>
	                    	<a href="${ctx}/bank/web/user/recharge/rechargePage.do" class="link-recharge" onclick="setCookie()">充值</a>
	                    </c:if>
	                    <c:if test="${openFlag eq 0 }">
	                    	<a href="${ctx}/bank/web/user/bankopen/init.do" class="link-recharge" style="float:left;" onclick="setCookie()">开户后查看，立即开户</a>
	                    </c:if>
                    </c:if>
                    <!-- 用户未登录 -->
                    <c:if test="${creditResult.data.userId==null || creditResult.data.userId=='' || creditResult.data.userId==0}">
	                    <div class="key">可用金额：</div>
	                    <div class="val">0 元</div>
	                    <a href="${ctx}/user/login/init.do" class="balance unlogin">登录后查看</a>
                    </c:if>
                </div>
                <div class="field field-input">
                    <div class="input">
                        <input type="text" name="assignCapital" id="assignCapital" placeholder="投标金额应大于1元" oncopy="return false" onpaste="return false" oncut="return false" oncontextmenu="return false" autocomplete="off"  />
                        <div class="btn sm" id="fullyBtn">全投</div>
                    </div>
                </div>
                <div class="field sub">
                    <div class="key dark">垫付利息：</div>
                    <div class="val fl-r" id = "assign_interest_advance">0.00 元</div>
                </div>
                <div class="field sub">
                    <div class="key dark">实际支付金额：</div>
                    <div class="val fl-r" id = "act_pay_num">0.00 元</div>
                </div>
                <div class="field sub">
                    <div class="key dark">历史回报：</div>
                    <div class="val fl-r" id="income">0.00 元</div>
                </div>
                
                
                
                
                <div class="field">
                    <div class="btn submit" id="goSubmit">立即投资</div>
                </div>
                
                </c:if>
                <c:if test="${typeFlag eq 0}">
	                <div class="product-form status">
		            	<div class="status-content hjh single"></div>
		            </div>
                </c:if>
                
                
                
                
                <input type="checkbox" name="termcheck" class="form-term-checkbox" id="productTerm" checked="">
                <div class="dialog dialog-alert" id="confirmDialog">
                    <div class="title">投资确认</div>
                    <div class="content">
                        <table class="product-confirm-table" cellspacing="0" cellpadding="0">
                            <tr>
                                <td width="140" align="right"><span class="dark">预期年化收益率：</span></td>
                                <td><span>${creditResult.data.creditTender.borrowApr}</span><span class="unit"> %</span></td>
                            </tr>
                            <tr>
                                <td align="right"><span class="dark">折扣率：</span></td>
                                <td><span>${creditResult.data.creditTender.creditDiscount}<span class="unit"> %</span></span></td>
                            </tr>
                            <tr>
                                <td align="right"><span class="dark">认购本金：</span></td>
                                <td><span class="red" id="assign_capital_confirm"></span></td>
                            </tr>
                            <tr>
                                <td align="right"><span class="dark">剩余期限：</span></td>
                                <td>${creditResult.data.creditTender.creditTerm}<span class="unit"> 天</span></td>
                            </tr>
                            <tr>
                                <td align="right"><span class="dark">垫付利息</span></td>
                                <td id="assign_interest_advance_confirm"></td>
                            </tr>
                            <tr>
                                <td align="right"><span class="dark">计息方式：</span></td>
                                <td id="assign_interest_advance_confirm" >
                                	<c:if test="${creditResult.data.creditTender.borrowStyle=='month'}">
										等额本息
									</c:if>
									<c:if test="${creditResult.data.creditTender.borrowStyle=='principal'}">
										等额本金
									</c:if>
									<c:if test="${creditResult.data.creditTender.borrowStyle=='end'}">
										按月计息，到期还本还息
									</c:if>
									<c:if test="${creditResult.data.creditTender.borrowStyle=='endday'}">
										按天计息，到期还本还息
									</c:if>
									<c:if test="${creditResult.data.creditTender.borrowStyle=='endmonth'}">
										先息后本
									</c:if>
                                </td>
                            </tr>
                            <tr>
                                <td align="right"><span class="dark">历史回报：</span></td>
                                <td id="income_confirm">0.00 </td>
                            </tr>
                        </table>
                        
                        <div class="cutline"></div>
                        <table class="product-confirm-table" cellspacing="0" cellpadding="0">
                            <tr>
                                <td width="140" align="right"><span class="dark">实际支付：</span></td>
                                <td><span class="total" id="act_pay_num_confirm"></span> </td>
                            </tr>
                        </table>
                        <div class="product-term">
                            <p class="highlight">注：实际支付金额=认购本金*（1-折扣率）+垫付利息</p>
                            <div class="term-checkbox checked"></div>
                            <span>我已同意并阅读  <a href="${ctx}/agreement/transferOfCreditorRight.do">《债权转让协议》</a><a href="${ctx}/agreement/confirmationOfInvestmentRisk.do">《投资风险确认书》</a></span>
                        </div>
                    </div>
                    <div class="btn-group">
                        <div class="btn btn-default md" id="confirmDialogCancel">取 消</div>
                        <div class="btn btn-primary md" id="confirmDialogConfirm">确 认</div>
                    </div>
                </div>
            </form>
            <!-- end 内容区域 -->
        </div>
        <div class="container">
            <section class="content">
                <div class="main-title">
					项目流程图
                </div>
                <div class="flow-content transfer">
                    <div class="item done">
                        <div class="icon icon-sh"></div>债权人发起转让</div>
                    <div class="line done"></div>
                    <div class="item done">
                        <div class="icon icon-fb"></div>投资人认购</div>
                    <div class="line"></div>
                    <div class="item">
                        <div class="icon icon-tz"></div>支付资金</div>
                    <div class="line"></div>
                    <div class="item">
                        <div class="icon icon-jx"></div>计息</div>
                    <div class="line"></div>
                    <div class="item">
                        <div class="icon icon-hk"></div>回款</div>
                </div>
            </section>
        </div>
        <div class="container">
            <section class="content">
                <div class="main-tab">
                    <ul class="tab-tags">
                        <li class="active" panel="0"><a href="javascript:;">风控信息</a></li>
                        <c:if test="${typeFlag ne 0}">
			                 <li panel="1"><a href="javascript:;">相关文件</a></li>
		                </c:if>
                        <li panel="2"><a href="javascript:;" >还款计划</a></li>
                        <li panel="3"><a href="javascript:;" class="assignClass" data-page="1">投资记录</a></li>
                        <c:if test="${typeFlag ne 0}">
			                 <li panel="4"><a href="javascript:;">常见问题</a></li>
		                </c:if>
                        
                    </ul>
                    <ul class="tab-panels">
                        <li panel="0" class="active">
                            <div class="attr-title"><span>债转说明</span></div>
                            <div class="attr-note" style="color:#7F7F80;">
								此项目为“汇直投”债权转让服务。 
								债权转让达成后，债权拥有者将变更为新投资人，担保公司将继续对借款人的借款承担连带担保责任。                  
							</div>
                            <div class="attr-title"><span>基础信息</span></div>
                            <div class="attr-table">
                                <table cellpadding="0" cellspacing="0">
                                	
                                    <tbody>
                                    	<c:if test="${creditResult.data.creditTender.comOrPer==1}">
                                        <tr>
                                            <td width="50%"><span class="key">所在地区：${borrowInfo.borrowAddress}</span> <span class="val"></span></td>
                                            <td width="50%"><span class="key">抵/注册资本：<fmt:formatNumber value="${borrowInfo.regCaptial}" pattern="#,###" /></span> <span class="val">元</span></td>
                                        </tr>
                                        <tr>
                                            <td><span class="key">注册时间：</span> <span class="val">${borrowInfo.registTime}</span></td>
                                            <td><span class="key">信用评级：</span> <span class="val">${creditResult.data.borrow.borrowLevel}</span></td>
                                        </tr>
                                        </c:if>
                                        <c:if test="${creditResult.data.creditTender.comOrPer==2}">
                                        <tr>
                                            <td width="50%"><span class="key">所在地区：${borrowInfo.workingCity}</span> <span class="val"></span></td>
                                            <td width="50%"><span class="key">抵/注册资本：<fmt:formatNumber value="${borrowInfo.accountContents}" pattern="#,###" /></span> <span class="val">元</span></td>
                                        </tr>
                                        <tr>
                                            <td><span class="key">注册时间：</span> <span class="val"></span></td>
                                            <td><span class="key">信用评级：</span> <span class="val">${creditResult.data.borrow.borrowLevel}</span></td>
                                        </tr>
                                        </c:if>
                                    </tbody>
                                </table>
                            </div>
                            <div class="attr-title"><span>项目介绍</span></div>
                            <div class="attr-note" style="color:#7F7F80;">
								${borrowInfo.borrowContents}                            
							</div>
                            <div class="attr-title"><span>风控措施</span></div>
                            <div class="attr-note" style="color:#7F7F80;">
								${riskControl.controlMeasures }                            
							</div>
                        </li>
                        <li panel="1">
                            <!-- 相关文件 -->
                            <div class="new-detail-img-c">
                                <ul>
                                <c:if test="${creditResult.data.creditTender.status eq 10 or creditResult.data.creditTender.status eq 11 or creditResult.data.creditTender.status eq 12 }">
                                	<c:forEach items="${fileList}" var="borrowFiles">
	                                    <li>
	                                        <a href="${borrowFiles.fileUrl}" data-caption="${borrowFiles.fileName}">
		                                        <div><img src="${borrowFiles.fileUrl}" alt=""></div>
		                                        <span class="title">${borrowFiles.fileName}</span>
	                                        </a>
	                                    </li>
								 	 </c:forEach>
                                </c:if>	
                                <c:if test="${creditResult.data.creditTender.status eq 13 }">
                                	<c:forEach items="${creditResult.data.borrowFiles}" var="borrowFiles">
	                                    <li>
	                                        <a href="${borrowFiles.fileUrl}" data-caption="${borrowFiles.fileName}">
		                                        <div><img src="${borrowFiles.fileUrl}" alt=""></div>
		                                        <span class="title">${borrowFiles.fileName}</span>
	                                        </a>
	                                    </li>
								 	 </c:forEach>
								 </c:if>
                                </ul>
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
                                        <c:forEach items="${creditResult.data.repay.repayPlan}" var="repayPlan">
											<tr>
												<td><span class="dark">${repayPlan.repayTime}</span></td>
												<td>第${repayPlan.repayPeriod}期</td>
												<td>本息</td>
												<td><fmt:formatNumber value="${repayPlan.repayAccount}" pattern="#,###.00" /></td>
											</tr>
										</c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </li>
                        <li panel="3" class="">
                            <!-- 投资记录 -->
							<p>
                                <span id="assignTotal">加入总人次： ${creditResult.data.tender.tenderNum}</span>&nbsp;&nbsp;
                                <span id="assignTimes">加入金额：<fmt:formatNumber value="${creditResult.data.tender.tendTotal}" pattern="#,#00.00"/>元</span>
                            </p>
                            <div class="attr-table">
                                <table cellpadding="0" cellspacing="0">
                                    <thead>
                                        <tr>
                                            <th width="35%">投资人</th>
                                            <th width="25%">承接金额（元）</th>
                                            <th width="15%">来源</th>
                                            <th width="25%">承接时间</th>
                                        </tr>
                                    </thead>
                                    <tbody id="projectAssignList">
                                    </tbody>
                                </table>
                                <div class="pages-nav" id="assignListPage"></div>
                            </div>
                        </li>
                        <li panel="4">
                            <!-- 常见问题 -->
                            <div class="attr-table">
                                <table cellpadding="0" cellspacing="0">
                                    <tbody>
                                        <tr>
                                            <td width="100%">
                                                <span class="dark">1、我可以投资吗？</span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>持有本人中华人民共和国居民身份证的公民，且年满十八周岁，都可在汇盈金服网站上进行注册、完成实名认证，成为投资人。</td>
                                        </tr>
                                        <tr>
                                            <td width="100%">
                                                <span class="dark">2、怎样进行投资？</span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>请您按照以下步骤进行投资：
                                                <br/> 1. 在汇盈金服网站或手机客户端上进行注册、通过实名认证、成功绑定银行卡；
                                                <br/> 2. 账户充值；
                                                <br/> 3. 浏览平台融资项目，根据个人风险偏好自主选择项目投资；
                                                <br/> 4. 确认投资，投资成功。</td>
                                        </tr>
                                        <tr>
                                            <td width="100%">
                                                <span class="dark">3、投资后是否可以提前退出？</span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>1. 平台产品暂不支持提前回款申请。
                                                <br/> 2. 汇直投和尊享汇融资项目支持债权转让。</td>
                                        </tr>
                                        <tr>
                                            <td width="100%">
                                                <span class="dark">4、为何投标后会显示资金冻结？</span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>对于所有投资项目，投资人可自主选择进行投资。在项目完成放款之前，投资金额将被冻结；在项目完成放款之后，投资金额将通过第三方资金托管机构（汇付天下）转给融资方；如果在限定时间内未满标，则根据情况将已融资金放款给融资方或原路返还投资人。
                                            </td>
                                        </tr>
                                        <tr>
                                            <td width="100%">
                                                <span class="dark">5、在汇盈金服投资有哪些费用？</span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>在汇盈金服平台进行投资，平台本身不收取投资人任何费用，投资人在充值/提现时第三方资金托管机构（汇付天下）会收取相关手续费。
                                                <br/> 提现方式： 一般提现 快速取现 即时取现
                                                <br/> 手续费： 1元／笔 1元／笔 ＋ 提现金额的0.05% 1元／笔 ＋ 提现金额的0.05%
                                                <br/> 到账时间：
                                                <br/> 一般提现：提现日后下一个工作日到账（T+1）
                                                <br/> 快速提现：工作日当日14:30前发起，当日到账（T+0）
                                                <br/> 即时提现：提现当日到账（目前只有部分银行支持）
                                                <br/>
                                                <br/> 特别提示：快速提现的手续费的计算方式为1元/笔+提现金额的0.05%，只适用于提现日后的一天为工作日的情况，如果后一天是非工作日，提现的手续费计算方式为1元/笔+提现金额的0.05% x（1+非工作日的天数）。例：周五申请快速提现，手续费=1元/笔+提现金额的0.05% x（1+2）
                                            </td>
                                        </tr>
                                        <tr>
                                            <td width="100%">
                                                <span class="dark">6. 投资人风险测评目的和规则是什么？</span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>为完善对投资者风险承受能力的尽职评估，实现对投资者的等级管理，保障投资者购买合适的产品，根据投资者风险测评的结果，将投资者风险承受能力由低到高分为保守型、稳健型、成长型、进取型四种类型。
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
	<script src="${cdn}/dist/js/lib/jquery.validate.js"></script>
    <script src="${cdn}/dist/js/lib/jquery.metadata.js"></script>
    <script src="${cdn}/dist/js/product/data-format.js"></script>
    <script src="${cdn}/dist/js/product/htj-product-transfer-detail.js"></script>
</body>
</html>