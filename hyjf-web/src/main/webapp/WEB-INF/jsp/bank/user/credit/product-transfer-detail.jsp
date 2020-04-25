<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="/common.jsp"%>
<html lang="zh-cmn-Hans">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
<script>
	var baseTableData = ${baseTableData};
	var assetsTableData = ${assetsTableData} ;
	var intrTableData = ${intrTableData} ;
	var credTableData = ${credTableData} ;
	var reviewTableData = ${reviewTableData} ;
	var otherTableData = ${otherTableData} ;
</script>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<section class="breadcrumbs">
        <div class="container">
            <div class="left-side">您所在的位置： <a href="${ctx}/">首页</a> &gt;<a href="${ctx}/bank/user/credit/initWebCreditPage.do">债权转让</a> &gt; 项目详情</div>
        </div>
    </section>
    <article class="main-content product">
        <div class="container">
            <!-- start 内容区域 -->
            <div class="product-intr">
                <div class="title">
                    <span>HZR${creditDetail.creditNid} </span>
                    <span class="title-tag-gray">
	                	出借人适当性管理告知
	                	<a class="risk-alt alt1">
	                		<span class="risk-tips">
	                			作为网络借贷的出借人，应当具备出借风险意识，风险识别能力，拥有一定的金融产品出借经验并熟悉互联网金融。请您在出借前，确保了解借款项目的主要风险，同时确认具有相应的风险认知和承受能力，并自行承担出借可能产生的相关损失。
	                		</span>
	                		<i class="icon iconfont icon-zhu "></i>
	                	</a>
	                </span>
                    <div class="contract-box">
                        <a href="${ctx}/agreement/goAgreementPdf.do?aliasName=zqzrxy" target="_blank">${zqzrxy }</a>
                        <a href="${ctx}/agreement/goAgreementPdf.do?aliasName=tzfxqrs" target="_blank">${tzfxqrs }</a>
                    </div>
                    <input type="hidden" name="isCheckUserRole" id="isCheckUserRole" value="${isCheckUserRole}" /> <!-- 是否判断出借人 -->
                    <input type="hidden" name="paymentAuthStatus" id="paymentAuthStatus" value="${paymentAuthStatus}" /><!-- 自动缴费状态 -->
                    <input type="hidden" name="paymentAuthOn" id="paymentAuthOn" value="${paymentAuthOn}" /> 
                    <input type="hidden" name="userBalance" id="userBalance" value="${balance}" />
                    <input type="hidden" name="roleId" id="roleId" value="${roleId}"><!-- 仅限出借人进行投资业务 -->
                	<input type="hidden" name="loginFlag" id="loginFlag" value="${loginFlag }" /> <!-- 登录状态 0未登陆 1已登录 -->
                	<input type="hidden" name="openFlag" id="openFlag" value="${openFlag }" /> <!-- 开户状态 0未开户 1已开户 -->
                	<input type="hidden" name="riskFlag" id="riskFlag" value="${riskFlag }" /> <!-- 是否进行过风险测评 0未测评 1已测评 -->
                	<input type="hidden" name="setPwdFlag" id="setPwdFlag" value="${setPwdFlag }" /> <!-- 是否设置过交易密码 0未设置 1已设置 -->
                	<input type="hidden" name="isUserValid" id="isUserValid" value="${isUserValid }" /> <!-- 是否禁用 1 禁用  0 未禁用 -->
                	<input type="hidden" name="increase" id="increase" value="1" /> <!-- 递增金额 -->
                	<input type="hidden" name="isLast" id="isLast" value="0" /> <!-- 是否最后一笔投资 -->
                	<input type="hidden" id="projectData" data-total="${creditDetail.creditAssignCapital}" data-tendermax="${creditDetail.creditAssignCapital}" data-tendermin="1"/>
                </div>
                <div class="attr transfer">
                    <div class="attr-item attr4">
                        <span class="val highlight">${creditDetail.borrowApr}<span class="unit"> %</span> </span>
                        <span class="key">历史年回报率</span>
                    </div>
                    <div class="attr-item attr5">
                        <span class="val highlight">${creditDetail.creditDiscount}<span class="unit"> %</span> </span>
                        <span class="key">折让率</span>
                    </div>
                    <div class="attr-item attr6">
                        <span class="val">${creditDetail.creditTerm}<span class="unit"> 天</span> </span>
                        <span class="key">剩余期限</span>
                    </div>
                    <div class="attr-item attr7">
                        <span class="val">${creditDetail.creditCapital}<span class="unit"> 元</span></span>
                        <span class="key">出让金额</span>
                    </div>
                </div>
                <div class="list">
                    <div class="list-item">项目编号：<a href="${ctx}/bank/web/borrow/getBorrowDetail.do?borrowNid=${projectDeatil.borrowNid}" class="normal-a highlight">${projectDeatil.borrowNid}</a></div>
                    <div class="list-item">还款方式：
	                     ${projectDeatil.repayStyle}
                    </div>
                    <div class="list-item">债权持有天数：${creditDetail.creditTermHold}天</div>
                    <div class="list-item"><span class="dark">建议出借人类型：${creditDetail.investLevel}及以上</span></div>
                </div>
            </div>
            <form class="product-form transfer" action="${ctx}/bank/user/credit/websubmitcredittenderassign.do" id="productForm" autocomplete="off">
            	<input type="hidden" name="tokenCheck" id="tokenCheck" value="" />
				<input type="hidden" name="tokenGrant" id="tokenGrant" value="${tokenGrant}" /> 
                <input type="hidden" id="borrowNid" name="borrowNid" value="${projectDeatil.borrowNid}">
                <input type="hidden" id="creditNid" name="creditNid" value="${creditDetail.creditNid}">
                <input type="text" name="username" class="ignore fix-auto-fill"/>
                <div class="field">
                    <div class="key">项目剩余：</div>
                    <div class="val"><span class="highlight"><fmt:formatNumber value="${creditDetail.creditAssignCapital}" pattern="#,##0.00"/></span> 元</div>
                </div>
                <div class="field">
                    <div class="key">可用金额：</div>
                    <c:if test="${loginFlag eq 1}">
                    	<c:if test="${openFlag eq 1 }">
	                    	<div class="val"><fmt:formatNumber value="${balance}" pattern="#,##0.00" /> 元</div>
	                    	<a href="${ctx}/bank/web/user/recharge/rechargePage.do" class="link-recharge" onclick="setCookie()">充值</a>
	                    </c:if>
	                    <c:if test="${openFlag eq 0 }">
	                    	<a href="${ctx}/bank/web/user/bankopen/init.do" class="link-recharge" style="float:left;" onclick="setCookie()">开户后查看，立即开户</a>
	                    </c:if>
                    </c:if>
                    <c:if test="${loginFlag eq 0}">
	                    <div class="val">登录后可见</div>
	                    <a href="${ctx}/user/login/init.do" class="link-recharge" onclick="setCookie()">立即登录</a>
                    </c:if>
                </div>
                <div class="field field-input">
                    <div class="input">
                        <input type="text" name="assignCapital" id="assignCapital" placeholder="投标金额应大于1元" oncopy="return false" onpaste="return false" oncut="return false" oncontextmenu="return false" autocomplete="off" maxlength="9" />
                        <div class="btn sm" id="fullyBtn">全投</div>
                    </div>
                </div>
                <div class="field sub">
                    <div class="key dark">垫付利息：</div>
                    <div class="val fl-r" id="assign_interest_advance">0.00 元</div>
                </div>
                <div class="field sub">
                    <div class="key dark">实际支付金额：</div>
                    <div class="val fl-r" id="act_pay_num">0.00 元</div>
                </div>
                <div class="field sub">
                    <div class="key dark">历史回报：</div>
                    <div class="val fl-r" id="income">0.00 元</div>
                </div>
                <div class="field">
                    <div class="btn submit" id="goSubmit">立即承接</div>
                </div>
                <input type="checkbox" name="termcheck" class="form-term-checkbox" id="productTerm">
                <div class="dialog dialog-alert" id="confirmDialog">
                    <div class="title">承接确认</div>
                    <div class="content">
                        <table class="product-confirm-table" cellspacing="0" cellpadding="0">
                            <tr>
                                <td width="140" align="right"><span class="dark">历史年回报率：</span></td>
                                <td><span id="">${creditDetail.borrowApr}%</span></td>
                            </tr>
                            <tr>
                                <td align="right"><span class="dark">折让率：</span></td>
                                <td><span id="">${creditDetail.creditDiscount}%</span></td>
                            </tr>
                            <tr>
                                <td align="right"><span class="dark">认购本金：</span></td>
                                <td><span class="red" id="assign_capital_confirm"></span> 元</td>
                            </tr>
                            <tr>
                                <td align="right"><span class="dark">剩余期限：</span></td>
                                <td id="">${creditDetail.creditTerm}天</td>
                            </tr>
                            <tr>
                                <td align="right"><span class="dark">垫付利息</span></td>
                                <td id="assign_interest_advance_confirm">60元</td>
                            </tr>
                            <tr>
                                <td align="right"><span class="dark">计息方式：</span></td>
                                <td>${projectDeatil.repayStyle}</td>
                            </tr>
                            
                            <tr>
                                <td align="right"><span class="dark">历史回报：</span></td>
                                <td id="income_confirm">0.00 元</td>
                            </tr>
                        </table>
                        
                        <div class="cutline"></div>
                        <table class="product-confirm-table" cellspacing="0" cellpadding="0">
                            <tr>
                                <td width="140" align="right"><span class="dark">实际支付：</span></td>
                                <td><span class="total" id="act_pay_num_confirm">49,234</span> 元</td>
                            </tr>
                        </table>

                        <div class="product-term">
                            <p class="highlight">注：实际支付金额=认购本金*（1-折让率）+垫付利息</p>
                            <div class="term-checkbox"></div>
                            <span>我已阅读并同意  <a href="${ctx}/agreement/goAgreementPdf.do?aliasName=zqzrxy" target="_blank">${zqzrxy }</a>
                        <a href="${ctx}/agreement/goAgreementPdf.do?aliasName=tzfxqrs" target="_blank">${tzfxqrs }</a></span>
                        </div>
                        <div style="padding-left: 50px; color: #999;font-size: 12px;margin-top: 2px;"><img src="${cdn}/dist/images/icon-star.png"  alt="" style="width:10px;margin-left:2px;height: 10px;margin-right:8px;position: relative;top: -13px;"/><span style="display:inline-block">您应在中国大陆操作，因在境外操作导致的后果将由您<br>独自承担。</span></div>
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
                        <div class="icon icon-fb"></div>承接人认购</div>
                    <div class="line done"></div>
                    <div class="item done">
                        <div class="icon icon-tz"></div>支付资金</div>
                    <div class="line done"></div>
                    <div class="item done">
                        <div class="icon icon-jx"></div>计息</div>
                    <div class="line done"></div>
                    <div class="item done">
                        <div class="icon icon-hk"></div>回款</div>
                </div>
            </section>
        </div>
        <div class="container">
            <section class="content">
                <div class="main-tab">
                    <ul class="tab-tags">
                        <li class="active" panel="0"><a href="javascript:;">项目详情</a></li>
                        <li panel="1"><a href="javascript:;">承接记录</a></li>
                        <li panel="2"><a href="javascript:;">还款计划</a></li>
                        <li panel="3"><a href="javascript:;">常见问题</a></li>
                    </ul>
                    <ul class="tab-panels">
                        <li class="active" panel="0">
                           <!-- 项目详情 -->
                            <c:if test="${loginFlag eq '1'}"> <!-- 登录-->
								<c:if test="${borrowType eq 3}"> <!-- 汇资产展示不同 -->
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
                            
	                            <c:if test="${borrowType ne 3}">
	                            	<c:if test="${baseTableData ne '' && baseTableData != null && baseTableData ne '[]'}">
	                            		<div class="attr-title"><span>基础信息</span></div>
		                            	<div class="attr-table" id="baseTable"></div>
	                            	</c:if>

	                            	<c:if test="${assetsTableData ne '' && assetsTableData != null && assetsTableData ne '[]' }">
			                            <div class="attr-title"><span>资产信息</span></div>
			                            <div class="attr-table" id="assetsTable"></div>
	                            	</c:if>

	                            	<c:if test="${intrTableData ne '' && intrTableData != null && intrTableData ne '[]'}">
			                            <div class="attr-title"><span>项目介绍</span></div>
			                            <div class="attr-table" id="intrTable"></div>
	                            	</c:if>

	                            	<c:if test="${credTableData ne '' && credTableData != null && credTableData ne '[]'}">
			                            <div class="attr-title"><span>信用状况</span></div>
			                            <div class="attr-table" id="credTable"></div>
	                            	</c:if>

		                            <c:if test="${reviewTableData ne '' && reviewTableData != null && reviewTableData ne '[]'}">
			                            <div class="attr-title"><span>审核状况</span></div>
			                            <div class="attr-table" id="reviewTable"></div>
	                            	</c:if>
	                            	<c:if test="${otherTableData ne '' && otherTableData != null && otherTableData ne '[]'}">
			                            <div class="attr-title"><span>其他信息（更新于${updateTime }）</span></div>
			                            <div class="attr-table" id="otherTable"></div>
	                            	</c:if>	                            
	                            </c:if>
	                        <div class="attr-note">
                               <p style="color: #999999;">${projectDeatil.borrowMeasuresMea}</p>
                            </div>
                            </c:if>
                            <!-- 未登录状态 -->
                            <c:if test="${loginFlag eq '0' }">                           
	                            <div class="unlogin">
	                                <div class="icon"></div>
	                                <p>请先 <a href="${ctx}/user/login/init.do">登录</a> 或 <a href="${ctx}/user/regist/init.do">注册</a> 后可查看</p>
	                            </div>
                            </c:if>
                        </li>
                        <li panel="1" class="">
                            <!-- 投资记录 -->
						    <p>
						    	<span id="assignTimes">承接总人次 : 0</span>&nbsp;&nbsp;
                                <span id="assignTotal">承接金额 : <fmt:formatNumber value="0" pattern="#,#00.00"/>元</span>
                           	</p>
                            <div class="attr-table">
                                <table cellpadding="0" cellspacing="0">
                                    <thead>
                                        <tr>
                                            <th width="35%">承接人</th>
                                            <th width="25%">承接金额（元）</th>
                                            <th width="15%">来源</th>
                                            <th width="25%">承接时间</th>
                                        </tr>
                                    </thead>
                                   <tbody  id="projectAssignList">
                                    </tbody>
                                </table>
                                <div class="pages-nav" id="assignListPage"></div>
                            </div>
                        </li>
                        <li panel="2" class="">
                            <!-- 还款计划 -->
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
                        <li panel="3">
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
                                            <td>“优选债权”是与地方金融资产交易所合作推出的资产交易类产品，资产持有人通过汇盈金服居间撮合，将资产或其收益权转让给承接方，承接方获取一定数额的出借收益。</td>
                                        </tr>
                                        <tr>
                                            <td width="100%">
                                                <span class="dark">3、“优选债权”的特点是什么？</span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>“优选债权”发布的产品是在地方金融资产交易所挂牌的项目，项目均经过金交所严格的审核，并提供专业的风控措施，保证项目的优质性。承接人可根据自身出借偏好选择适合项目承接。</td>
                                        </tr>
                                        <tr>
                                            <td width="100%">
                                                <span class="dark">4、我可以认购“优选债权”产品吗？</span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>“优选债权”下每支产品的承接人数不能超过200人；承接人持有本人中华人民共和国居民身份证的公民，且年满十八周岁。</td>
                                        </tr>
                                        <tr>
                                            <td width="100%">
                                                <span class="dark">5、认购“优选债权”产品需要收费吗？</span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>承接人暂时无需支付认购费、管理费。</td>
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
	                                                <span class="dark">1、我可以出借吗？</span>
	                                            </td>
	                                        </tr>
	                                        <tr>
	                                            <td>持有本人中华人民共和国居民身份证的公民，且年满十八周岁，都可在汇盈金服网站上进行注册、完成实名认证，成为出借人。</td>
	                                        </tr>
	                                        <tr>
	                                            <td width="100%">
	                                                <span class="dark">2、怎样进行出借？</span>
	                                            </td>
	                                        </tr>
	                                        <tr>
	                                            <td>请您按照以下步骤进行出借：
	                                                <br/> 1. 在汇盈金服网站或手机客户端上进行注册、通过实名认证、成功绑定银行卡；
	                                                <br/> 2. 完成出借人风险测评；
	                                                <br/> 3. 账户充值；
	                                                <br/> 4. 浏览平台借款项目，根据个人风险偏好自主选择项目出借；
	                                                <br/> 5. 确认出借，出借成功。</td>
	                                        </tr>
	                                        <tr>
	                                            <td width="100%">
	                                                <span class="dark">3、出借后是否可以提前退出？</span>
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
	                                            <td>对于所有出借项目，出借方可自主选择进行出借。在项目完成放款之前，出借金额将被冻结；在项目完成放款之后，出借金额将通过江西银行转给借款方；如果在限定时间内未满标，则根据情况将已借款金放款给借款方或原路返还出借方。</td>
	                                        </tr>
	                                        <tr>
	                                            <td width="100%">
	                                                <span class="dark">5、在汇盈金服出借有哪些费用？</span>
	                                            </td>
	                                        </tr>
	                                        <tr>
	                                            <td>在汇盈金服平台进行出借，平台本身不收取出借方任何费用，出借方在充值/提现时江西银行会收取相关手续费。
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
    		<a class="btn btn-primary single" id="authInvesPopConfirm">立即授权</a>
    	</div>
    </div>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="${cdn}/dist/js/lib/jquery.validate.js"></script>
    <script src="${cdn}/dist/js/lib/jquery.metadata.js"></script>
    <script src="${cdn}/dist/js/product/data-format.js?version=${version}"></script>
    <script>
        /*
            详情页浏览
            detail_view_screen
            product_type  产品类型  String [标的详情，计划详情，债转详情]
            project_id  项目编号  String 项目id，计划id，债转id
            project_name  项目名称  String
          */
        sa && sa.track('detail_view_screen',{
            product_type: '债转详情',
            project_id: "HZR${creditDetail.creditNid}"
        })
        /*
        详情页投资
        before_tender
        entrance  入口  String 是从首页还是列表页来
        project_tag 项目标签  String 新手专享，普通标，计划，债转
        project_id  项目编号  String 1001，1003，1004
        project_name  项目名称  String A，B，C
        project_duration  项目期限  Number  1，3，6，12
        duration_unit 期限单位  String 天，月
        project_apr 历史年回报率  Number  0.04，0.12
        discount_apr  折让率 Number  债转有，没有就为空
        project_repayment_type  还款方式  String 按月付息到期还本，等额本息，一次性还本付息

      */
        var customProps = {
            entrance: document.referrer,
            project_tag:"债转",
            project_id:"HZR${creditDetail.creditNid}",
            project_duration: Number("${creditDetail.creditTerm}"),
            duration_unit:"天",
            project_apr: Number("${creditDetail.borrowApr}")/100,
            discount_apr: Number("${creditDetail.creditDiscount}")*100/10000,
            project_repayment_type:"${projectDeatil.repayStyle}"
        }
        document.cookie = 'beforeUrl='+window.location.href+';path=/'
    </script>
    <script src="${cdn}/dist/js/product/product-transfer-detail.js?version=${version}"></script>
	<script type="text/javascript">
    	$('.risk-alt').hover(function(){
    		var alt=$(this).find('.risk-tips');
			$(this).find('.risk-tips').stop().fadeIn(150);
    	},function(){
    		$(this).find('.risk-tips').stop().fadeOut(150);
    	})
    </script>
</body>
</html>