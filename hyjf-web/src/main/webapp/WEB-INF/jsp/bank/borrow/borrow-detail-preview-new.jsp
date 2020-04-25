<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html>
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
            <div class="left-side">您所在的位置： <a href="${ctx}/">首页</a> &gt; 
                <c:if test="${projectDeatil.type == 4}"><a href="${ctx}/bank/web/borrow/newBorrowList.do">新手专区</a> &gt;</c:if>
                <c:if test="${projectDeatil.type != 4}"><a href="${ctx}/bank/web/borrow/initBorrowList.do">散标专区</a> &gt;</c:if> 项目详情
            </div>
            <div class="right-side"><span>开标时间：</span>${projectDeatil.sendTime}</div>
        </div>
    </section>
    <article class="main-content product">
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
							<a href="${ctx}/agreement/intermediaryServices.do" target="_blank">出借协议(范本)</a>
						    <a href="${ctx}/agreement/confirmationOfInvestmentRisk.do" target="_blank">风险确认书(范本)</a>
				    	</c:if>
                       <c:if test="${projectDeatil.type eq 13}">
                            <a href="#" onclick="openNew('${ctx}/user/regist/goDetail.do?type=rtb-open-contract&borrowNid=${projectDeatil.borrowNid }')" >《产品协议》</a> 
							<a href="#" onclick="openNew('${ctx}/user/regist/goDetail.do?type=rtb-contract&borrowNid=${projectDeatil.borrowNid }')" >《平台协议》</a> 
                       </c:if>
                    </div>
                </div>
                <div class="attr">
                    <div class="attr-item attr1">
                        <span class="val highlight">${projectDeatil.borrowApr}<span class="unit"> %</span> 
	                         <c:if test="${projectDeatil.borrowExtraYield ne '0.00'}">
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
                    <div class="list-item">项目编号：${projectDeatil.type == 13 ? projectDeatil.borrowAssetNumber : projectDeatil.borrowNid}</div>
                    <div class="list-item">还款方式：${projectDeatil.repayStyle}</div>
                    <div class="list-item"><span class="dark">建议出借者类型：${projectDeatil.investLevel}及以上</span></div>
                </div>
            </div>
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
                <!-- 投资中 -->
	            <c:if test="${projectDeatil.status eq 11}">
					<div class="status-content tzz"></div>
					<div class="start-time">开标时间：${projectDeatil.sendTime}</div>
	            </c:if>
            </div>

            <!-- end 内容区域 -->
        </div>
        <div class="container">
            <section class="content">
                <div class="main-title">
                  	  项目流程图
                </div>
                <div class="flow-content">
                    <div class="item done">
                        <div class="icon icon-sh"></div>审核</div>
                    <div class="line done"></div>
                    <div class="item done">
                        <div class="icon icon-fb"></div>信息发布</div>
                    <div class="line"></div>
                    <div class="item done">
                        <div class="icon icon-tz"></div>出借</div>
                    <div class="line"></div>
                    <div class="item done">
                        <div class="icon icon-jx"></div>计息</div>
                    <div class="line"></div>
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
                        <li panel="1"><a href="javascript:;">出借记录</a></li>
                        <li panel="2"><a href="javascript:;">还款计划</a></li>
                        <li panel="3"><a href="javascript:;">常见问题</a></li>
                    </ul>
                    <ul class="tab-panels">
                        <li class="active" panel="0">
                            <!-- 项目详情 -->
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
		                                    <td width="50%">
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
                        </li>
                        <li panel="1" class="">
                            <!-- 投资记录 -->
                            <p>
                                <span id="investTotal">加入总人次：</span>&nbsp;&nbsp;
                                <span id="investTimes">加入金额：元</span>
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
	<jsp:include page="/footer.jsp"></jsp:include>
	<script type="text/javascript" src="${cdn}/dist/js/lib/jquery.validate.js"></script>
    <script type="text/javascript" src="${cdn}/dist/js/lib/jquery.metadata.js"></script>
    <script type="text/javascript" src="${cdn}/dist/js/product/data-format.js?version=${version}"></script>
    <script type="text/javascript" src="${cdn}/dist/js/lib/jquery.jcountdown.min.js"></script>
    <script type="text/javascript" src="${cdn}/dist/js/product/product-detail.js?version=${version}"></script>
</body>
</html>