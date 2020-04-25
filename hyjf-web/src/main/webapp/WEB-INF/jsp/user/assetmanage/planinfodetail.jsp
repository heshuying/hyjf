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
	<section class="breadcrumbs">
	     <div class="container">
	         <div class="left-side">您所在的位置： <a href="${ctx}/">首页</a> &gt; <a href="${ctx}/user/pandect/pandect.do">账户中心</a> &gt; <a href="${ctx}/user/assetmanage/init.do">资产管理</a> &gt; 订单详情</div>
	     </div>
	 </section>
	<article class="main-content" style="padding-top: 0;">
        <div class="container">
            <!-- start 内容区域 -->   
            <div class="loan-paymentdetails">
                <div class="paymentdetails-top">
                    <div class="list-fl">
                        <div class="list">
                        <div class="title">
                        	<c:if test="${type==0}"> <!-- type标识项目状态  0投资中  1锁定期  2已回款 -->
                        	<p class="fn-left">${planinfo.debtPlanNid }</p>
                            <a class="fn-right value" href="${ctx}/agreement/planinfo.do?planNid=${planinfo.debtPlanNid }&planOrderId=${planinfo.accedeOrderId}" target="_blank">《计划协议》</a> 
                            <ul class="ui-list-title">
                                <li>
                                    <span class="basic-label">加入金额：</span>
                                    <span class="basic-value">${planinfo.accedeAccount }元</span>
                                </li>
                                <li>
                                    <span class="basic-label">历史年回报率：</span>
                                    <span class="basic-value">${planinfo.expectApr }％</span>
                                </li>
                                <li>
                                    
                                </li>
                                <li>
                                    <span class="basic-label">加入时间：</span>
                                    <span class="basic-value">${planinfo.createTime }</span>
                                </li>
                                <li>
                                    <span class="basic-label" style="white-space: nowrap;">锁定期：</span>
                                    <span class="basic-value">${planinfo.debtLockPeriod }</span>
                                </li>
                                <li>
                                    <span class="basic-label">实际退出日期：</span>
                                    <span class="basic-value">— —</span>
                                </li>
                            </ul>
                        	</c:if>
                            <c:if test="${type==1}"> <!-- type标识项目状态  0投资中  1锁定期  2已回款 -->
                        	<p class="fn-left">${planinfo.debtPlanNid }</p>
                            <a class="fn-right value" href="${ctx}/agreement/planinfo.do?planNid=${planinfo.debtPlanNid }&planOrderId=${planinfo.accedeOrderId}" target="_blank">《计划协议》</a> 
                            <ul class="ui-list-title">
                                <li>
                                    <span class="basic-label">加入金额：</span>
                                    <span class="basic-value">${planinfo.accedeAccount }元</span>
                                </li>
                                <li>
                                    <span class="basic-label">历史年回报率：</span>
                                    <span class="basic-value">${planinfo.expectApr }％</span>
                                </li>
                                <li>
                                    
                                </li>
                                <li>
                                    <span class="basic-label">加入时间：</span>
                                    <span class="basic-value">${planinfo.createTimeFen }</span>
                                </li>
                                <li>
                                    <span class="basic-label" style="white-space: nowrap;">锁定期：</span>
                                    <span class="basic-value">${planinfo.debtLockPeriod }</span>
                                </li>
                                <li>
                                    <span class="basic-label">实际退出日期：</span>
                                    <span class="basic-value">— —</span>
                                </li>
                            </ul>
                        	</c:if>
                        	
                        	<c:if test="${type==2}"> <!-- type标识项目状态  0投资中  1锁定期  2已回款 -->
                        	<p class="fn-left">${planinfo.debtPlanNid }</p>
                            <a class="fn-right value" href="${ctx}/agreement/planinfo.do?planNid=${planinfo.debtPlanNid }&planOrderId=${planinfo.accedeOrderId}" target="_blank">《计划协议》</a> 
                            <ul class="ui-list-title">
                                <li>
                                    <span class="basic-label">加入金额：</span>
                                    <span class="basic-value">${planinfo.accedeAccount }元</span>
                                </li>
                                <li>
                                    <span class="basic-label">历史年回报率：</span>
                                    <span class="basic-value">${planinfo.expectApr }％</span>
                                </li>
                                <li>
                                   
                                </li>
                                <li>
                                    <span class="basic-label">加入时间：</span>
                                    <span class="basic-value">${planinfo.createTime }</span>
                                </li>
                                <li>
                                    <span class="basic-label" style="white-space: nowrap;">锁定期：</span>
                                    <span class="basic-value">${planinfo.debtLockPeriod }</span>
                                </li>
                                <li>
                                    <span class="basic-label">实际退出日期：</span>
                                    <span class="basic-value">${planinfo.repayActualTime }</span>
                                </li>
                            </ul>
                        	</c:if>
                        	
                        </div>
                    </div>
                    <div class="bom">
                        <p class="bom-title">资产统计</p>
                        <c:if test="${type==0}"> <!-- type标识项目状态  0投资中  1锁定期  2已回款 -->
                        <ul class="ui-list-title">
                            <li>
                                <span class="basic-label">投资中  待确定...</span>
                            </li>
                        </ul>
                        </c:if>
                        <c:if test="${type==1}"> <!-- type标识项目状态  0投资中  1锁定期  2已回款 -->
                        <ul class="ui-list-title">
                            <li>
                                <span class="basic-label">当前持有资产总计：</span>
                                <span class="basic-value value">${investSum }</span>
                                <span class="basic-label">元</span>
                            </li>
                            <li>
                                <span class="basic-label">历史回报：</span>
                                <span class="basic-value value">${expectIntrest }</span>
                                <span class="basic-label">元</span>
                            </li>
                        </ul>
                        </c:if>
                        <c:if test="${type==2}"> <!-- type标识项目状态  0投资中  1锁定期  2已回款 -->
                        <ul class="ui-list-title">
                            <li>
                                <span class="basic-label">回款金额：</span>
                                <span class="basic-value value">${repayAccountYes }</span>
                                <span class="basic-label">元</span>
                            </li>
                            <li>
                                <span class="basic-label">实际收益：</span>
                                <span class="basic-value value">${factIntrest }</span>
                                <span class="basic-label">元</span>
                            </li>
                        </ul>
                        </c:if>
                    </div>
                    </div>
                    <div class="list-fr">
                        <!-- <img src="http://img.hyjf.com/dist/images/bond/paymentdetail-icon.png"> -->
                        <c:if test="${type==0}"> <!-- type标识项目状态  0投资中  1锁定期  2已回款 -->
                        <span class="being"></span>
                        </c:if>
                        <c:if test="${type==1}"> <!-- type标识项目状态  0投资中  1锁定期  2已回款 -->
                        <span class="lock"></span>
                        </c:if>
                        <c:if test="${type==2}"> <!-- type标识项目状态  0投资中  1锁定期  2已回款 -->
                        <span class="back"></span>
                        </c:if>
                    </div>
                </div>
                <div class="main">
                    <div class="title">持有项目列表</div>
                    <table class="table">
                        <thead>
                            <th class="ui-list-title pl1">计划编号</th>
                            <th class="ui-list-title pl2">项目期限</th>
                            <th class="ui-list-title pl3">投资金额</th>
                            <th class="ui-list-title pl4">投资时间</th>
                            <th class="ui-list-title pl6" style="padding-left: 8px;">操作</th>
                        </thead>
                        <c:if test="${type==0}"> <!-- type标识项目状态  0投资中  1锁定期  2已回款 -->
                        <tbody>
                            <tr>
                                <td class="ui-list-item pl1" rowspan="6">投资中  待确定...</td>
                            </tr>
                        </tbody>
                        
                        </c:if>
                        
                        <c:if test="${type==1}"> <!-- type标识项目状态  0投资中  1锁定期  2已回款 -->
                        <tbody>
                        	<c:choose>
								<c:when test="${empty debtInvestList}">
									<tr><td colspan="6">暂时没有数据记录</td></tr>
								</c:when>
								<c:otherwise>
								<c:forEach items="${debtInvestList }" var="record" begin="0" step="1" varStatus="status">
									<tr>
									<c:if test="${record.type eq '1'}">
									<td class="ui-list-item pl1"><a href="${ctx}/credit/planwebcredittender.do?creditNid=${record.borrowNid }&typeFlag=0" target="_blank">HZR${record.borrowNid }</a></td>
									<td class="ui-list-item pl2"><c:out value="${record.borrowPeriod }"></c:out>天</td>
									</c:if>
									<c:if test="${record.type eq '0'}">
									<td class="ui-list-item pl1"><a href="${ctx}/project/getHtjProjectDetail.do?borrowNid=${record.borrowNid }" target="_blank">${record.borrowNid }</a></td>
									<td class="ui-list-item pl2"><c:out value="${record.borrowPeriod }"></c:out>
									<c:if test="${record.isDay eq '1'}">天</c:if>
										<c:if test="${record.isDay eq '0'}">个月</c:if>
									</td>
									</c:if>
										<td class="ui-list-item pl3"><c:out value="${record.account }"></c:out></td>
										<td class="ui-list-item pl4"><c:out value="${record.createTime }"></c:out></td>
										<td class="ui-list-item pl6">	
										<c:if test="${record.type eq '1'}">
											<a href="${ctx}/agreement/planusercreditcontract.do?creditNid=${record.borrowNid }&creditTenderNid=${record.orderId }" target="_blank" class="value">债转协议</a>
										</c:if>
										<c:if test="${record.type eq '0'}">
									 	<c:if test="${not empty record.status and record.status ne '0' }">
										 	<a href="${ctx}/agreement/goConDetail.do?borrowNid=${record.borrowNid }&nid=${record.orderId }" target="_blank" class="value">投资协议</a>
										</c:if>
										</c:if>
										</td>
									</tr>
								</c:forEach>
								</c:otherwise>
							</c:choose>
                        </tbody>
                        </c:if>
                        <c:if test="${type==2}"> <!-- type标识项目状态  0投资中  1锁定期  2已回款 -->
                        	<c:choose>
								<c:when test="${empty debtInvestList}">
									<tr><td colspan="6">暂时没有数据记录</td></tr>
								</c:when>
								<c:otherwise>
								<c:forEach items="${debtInvestList }" var="record" begin="0" step="1" varStatus="status">
									<tr>
										<c:if test="${record.type eq '1'}">
											<td class="ui-list-item pl1"><a href="${ctx}/credit/planwebcredittender.do?creditNid=${record.borrowNid }&typeFlag=0" target="_blank">HZR${record.borrowNid }</a></td>
											<td class="ui-list-item pl2"><c:out value="${record.borrowPeriod }"></c:out>天</td>
										</c:if>
										<c:if test="${record.type eq '0'}">
											<td class="ui-list-item pl1"><a href="${ctx}/project/getHtjProjectDetail.do?borrowNid=${record.borrowNid }" target="_blank">${record.borrowNid }</a></td>
											<td class="ui-list-item pl2"><c:out value="${record.borrowPeriod }"></c:out>
											<c:if test="${record.isDay eq '1'}">天</c:if>
											<c:if test="${record.isDay eq '0'}">个月</c:if>
											</td>
										</c:if>
										<td class="ui-list-item pl3"><c:out value="${record.account }"></c:out></td>
										<td class="ui-list-item pl4"><c:out value="${record.createTime }"></c:out></td>
										<td class="ui-list-item pl6">
										<c:if test="${record.type eq '1'}">
											<c:if test="${not empty record.status and record.status ne '0' }">
												<a href="${ctx}/agreement/planusercreditcontract.do?creditNid=${record.borrowNid }&creditTenderNid=${record.orderId }" target="_blank" class="value">债转协议</a>
											</c:if>
										</c:if>
										<c:if test="${record.type eq '0'}">
										 	<c:if test="${not empty record.status and record.status ne '0' }">
												<a href="${ctx}/agreement/goConDetail.do?borrowNid=${record.borrowNid }&nid=${record.orderId }" target="_blank" class="value">投资协议</a>		
											</c:if>
										</c:if>
										</td>
									</tr>
								</c:forEach>
								</c:otherwise>
							</c:choose>
						</c:if>
                    </table>
                </div>
            </div>
            <!-- end 内容区域 -->            
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<!-- 设置定位  -->
	<script>setActById("mytender");</script>
</body>
</html>