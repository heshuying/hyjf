<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/page.css?version=20171108" />
<link rel="stylesheet" href="${ctx}/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" href="${ctx}/css/jquery.bxslider.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/idangerous.swiper.css" />
<title>项目详情</title>
</head>
<body>
	<div class="back_img">
		<div>
			<img src="" alt="" /><span></span>
		</div>
	</div>
	<div class="openPop"></div>
	<div class="openPopDiv">
		<div>
			<h5>开通江西银行托管账户</h5>
			<img src="${ctx}/img/openAccount.png?v=20171127" alt="" class="openAccountImg">
			<p class="tac text_grey">汇盈金服使用江西银行的第三方资金托管服务，投资更安全！</p>
			<span class="openPopDivSpan"><a class="colorOrange" href="">马上开通</a></span>
			<span class="openPopDivClose">稍后开通 </span>
		</div>
	</div>
	<div class="specialFont response detail_container">
		<input type="hidden" name="jumpcommend" id="jumpcommend" value="${jumpcommend}" />
		<input type="hidden" name="platform" id="platform" value="${platform}" />
		<input type="hidden" name="isSetPassword" id="isSetPassword" value="${isSetPassword}" /><!--是否设置交易密码  -->
		<c:if test="${not empty token}">
			<input type="hidden" name="token" id="token" value="${token}" />
			<input type="hidden" name="openAccountUrl" id="openAccountUrl" value="${openAccountUrl}" />
			<input type="hidden" name="mobile" id="mobile" value="${mobile}" />
			<input type="hidden" name="isSetPassword" id="isSetPassword" value="${isSetPassword}" />
		</c:if>
		<c:if test="${not empty consumeUrl}">
			<input type="hidden" name="consumeUrl" id="consumeUrl" value="${consumeUrl}" />
		</c:if>
		<input type="hidden" name="sign" id="sign" value="${sign}" /> 
		<input type="hidden" name="randomString" id="randomString" value="${randomString}" />
		<input type="hidden" name="projectType" id="projectType" value="${projectDeatil.type}" />
		<input type="hidden" name="order" id="order" value="${order}" />
		<!--列表详情头部开始-->
		<div class="detailHead">
			<span class="detailHeadLeft"> 
				<i class="orange"><b>${projectDeatil.borrowApr}</b>%
				<c:if test="${ not empty projectDeatil.borrowExtraYield and  projectDeatil.borrowExtraYield ne 0.00}">
					<span class="detailHeadLeft-span" >+${projectDeatil.borrowExtraYield}%</span>
				</c:if>
				</i>
				<em>历史年回报率</em>
			</span>
			<span class="detailHeadMiddle">
				<i><b>${projectDeatil.borrowPeriod} </b></i> 
				<em>项目期限</em>
			</span> 
			<span class="detailHeadRight"> 
				<i><b>${projectDeatil.investAccount} </b>元</i> 
				<em>项目剩余</em>
			</span>
			<c:if test="${projectDeatil.status eq 10}">
				<span class="startDate"> ${projectDeatil.onTime}开标 </span>
			</c:if>
			<c:if test="${projectDeatil.status ne 10}">
				<span class="process"> <i> <b></b>
				</i> <small>已投资<span id="centNum">${projectDeatil.borrowSchedule}</span>%
				</small>
				</span>
			</c:if>
			<div class="clearBoth"></div>
		</div>
		<!--列表详情头部结束-->
		<div class="grayHeight"></div>
		<!--列表详情内容开始-->
		<div class="detailContent detailContent-mid">
			<ul>
<%-- 				<c:if test="${not empty projectDeatil.projectName}">
				 	<li>
						<span class="left">项目名称：</span>
						<span class="right">${projectDeatil.projectName}</span>
					</li>
				</c:if> --%>
				<li><span class="left">项目编号：</span> <span class="right">
					${projectDeatil.borrowNid }
				</span>
				</li>
				<li><span class="left">融资总额：</span> <span class="right">${projectDeatil.account}元</span>
				</li>
				<%-- 	<c:if test="${projectDeatil.type ne 8}">
					<li>
						<span class="left">授信额度：</span>
						<span class="right">${projectDeatil.userCredit}元</span>
					</li>
					</c:if> --%>
				<li>
					<span class="left">还款方式：</span>
					<span class="right return-type">${projectDeatil.repayStyle }</span>
				</li>
				<c:if test="${projectDeatil.status ne 10}">
					<c:if test="${projectDeatil.status eq 11}">
						<li><span class="left">发标时间：</span> <span class="right">${projectDeatil.sendTime }</span>
						</li>
					</c:if>
					<c:if test="${projectDeatil.status ne 11}">
						<li><span class="left">发标时间：</span> <span class="right">${projectDeatil.sendTime }</span>
						</li>
						<li><span class="left">满标时间：</span> <span class="right">${projectDeatil.fullTime }</span>
						</li>
					</c:if>
				</c:if>
				<li><span class="left">温馨提示：</span> <span class="right">市场有风险，投资需谨慎<br/>历史回报不等于实际收益<br/>建议投资者类型：稳健型及以上</span>
				</li>
			</ul>
			<div class="clearBoth"></div>
		</div>
		<div class="grayHeight"></div>
		<!------------------------------------- 老版本--------------------------------------------------------------->
		<!--tab开始-->
		<div class="tabTitle">
			<span class="active" id="tab1">
					<%--<c:if test="${projectDeatil.type eq 13}">产品详情</c:if>
					<c:if test="${projectDeatil.type ne 13}">项目详情</c:if>--%>
				项目详情
					<i class="detail_line"></i>
			</span>
			<!-- ${projectDeatil.tabOneName }项目详情 -->
			<%-- <c:if test="${loginFlag eq 1 }"> --%>
				<!-- 融通宝展示不同 -->
			<c:if test="${projectDeatil.type ne 13 and projectDeatil.isNew eq 1}">
				<span id="tab3">还款计划</span>
			</c:if>
			<c:if test="${projectDeatil.type ne 13 and projectDeatil.isNew eq 0}">
				<span id="tab3">相关文件</span>
			</c:if>
			<!-- ${projectDeatil.tabThreeName }相关文件 -->
			<span id="tab4">投资记录</span>
			<!-- ${projectDeatil.tabFourName }投资记录 -->
			<span id="tab5">常见问题</span>
			<!-- ${projectDeatil.tabFiveName }常见问题 -->
		</div>
		<div class="tabContents">
			<c:if test="${projectDeatil.type eq 9}"> <!-- 汇资产  -->
				<!--项目信息开始-->
				<div class="tabItem active" id="infoOneId">
					<c:if test="${loginFlag eq 1 and (projectDeatil.status eq 10 or projectDeatil.status eq 11)}">
						<c:if test="${not empty borrowInfo.borrowName || not empty borrowInfo.borrowType || not empty borrowInfo.borrowAddress || not empty borrowInfo.guarantyValue || not empty borrowInfo.assetOrigin ||  not empty projectDeatil.borrowLevel}">
							<div class="item">
								<i class="icon-item"></i>
								<span>基础信息</span>
								<div class="detailContent clearPadding">
									<ul>
										<c:if test="${not empty  borrowInfo.borrowName}">
										<li><span class="left">项目名称：</span> <span class="right">${borrowInfo.borrowName }</span>
										</li>
										</c:if>
										<c:if test="${not empty  borrowInfo.borrowType}">
										<li><span class="left">项目类型：</span> <span class="right">${borrowInfo.borrowType }</span>
										</li>
										</c:if>
										<c:if test="${not empty  borrowInfo.borrowAddress}">
										<li><span class="left">所在地区：</span> <span class="right">${borrowInfo.borrowAddress }</span>
										</li>
										</c:if>
										<c:if test="${not empty  borrowInfo.guarantyValue}">
										<li><span class="left">评估价值：</span> <span class="right">${borrowInfo.guarantyValue }元</span>
										</li>
										</c:if>
										<%-- <li>
										<span class="left">权属类别：</span>
										<span class="right">${borrowInfo.ownershipCategory }</span>
										</li>  --%>
										<c:if test="${not empty  borrowInfo.assetOrigin}">
										<li><span class="left">资产成因：</span> <span class="right">${borrowInfo.assetOrigin }</span>
										</li>
										</c:if>
										<c:if test="${ not empty projectDeatil.borrowLevel}">
											<li><span class="left">信用评级：</span> <span class="right">${projectDeatil.borrowLevel}</span>
											</li>
										</c:if>
									</ul>
									<div class="clearBoth"></div>
								</div>
							</div>
					     </c:if> 
						 <c:if test="${not empty  borrowInfo.attachmentInfo}">
							<div class="item">
								<i class="icon-item"></i> <span>资产信息</span>
								<p>${borrowInfo.attachmentInfo }</p>
							</div>
						 </c:if>
						 <c:if test="${not empty  disposalPlan.disposalPlan}">
							<div class="item">
								<i class="icon-item"></i> <span>处置结果预案</span>
								<p>${disposalPlan.disposalPlan}</p>
							</div>
						</c:if>
						<c:if test="${not empty repayPlanList and projectDeatil.isNew eq 0}">
							<c:if test="${projectDeatil.status ne 15}">
								<div class="item">
									<i class="icon-star"></i>
									<span>还款计划</span>
									<table  cellspacing="0" cellpadding="0" class="detail-car-table">
										<tr>
											<td>时间</td>
											<td>还款金额(元)</td>
											<td>类型</td>
										</tr>
										<c:forEach items="${repayPlanList}" var="item" varStatus="vs">
											<tr>
												<td><c:out value="${item.repayTime}"></c:out></td>
												<td><c:out value="${item.repayTotal}"></c:out></td>
												<td><c:out value="${item.repayType}"></c:out></td>
											</tr>
										</c:forEach>
									</table>
								</div>
							</c:if>
						</c:if>
					</c:if>
					<c:if test="${loginFlag eq 1 and (projectDeatil.status eq 12 or projectDeatil.status eq 13 or projectDeatil.status eq 14) and investFlag eq 1}">
						<c:if test="${not empty borrowInfo.borrowName || not empty borrowInfo.borrowType || not empty borrowInfo.borrowAddress || not empty borrowInfo.guarantyValue || not empty borrowInfo.assetOrigin ||  not empty projectDeatil.borrowLevel}">
							<div class="item">
								<i class="icon-item"></i>
								<span>基础信息</span>
								<div class="detailContent clearPadding">
									<ul>
										<c:if test="${not empty  borrowInfo.borrowName}">
											<li><span class="left">项目名称：</span> <span class="right">${borrowInfo.borrowName }</span>
											</li>
										</c:if>
										<c:if test="${not empty  borrowInfo.borrowType}">
											<li><span class="left">项目类型：</span> <span class="right">${borrowInfo.borrowType }</span>
											</li>
										</c:if>
										<c:if test="${not empty  borrowInfo.borrowAddress}">
											<li><span class="left">所在地区：</span> <span class="right">${borrowInfo.borrowAddress }</span>
											</li>
										</c:if>
										<c:if test="${not empty  borrowInfo.guarantyValue}">
											<li><span class="left">评估价值：</span> <span class="right">${borrowInfo.guarantyValue }元</span>
											</li>
										</c:if>
											<%-- <li>
                                            <span class="left">权属类别：</span>
                                            <span class="right">${borrowInfo.ownershipCategory }</span>
                                            </li>  --%>
										<c:if test="${not empty  borrowInfo.assetOrigin}">
											<li><span class="left">资产成因：</span> <span class="right">${borrowInfo.assetOrigin }</span>
											</li>
										</c:if>
										<c:if test="${ not empty projectDeatil.borrowLevel}">
											<li><span class="left">信用评级：</span> <span class="right">${projectDeatil.borrowLevel}</span>
											</li>
										</c:if>
									</ul>
									<div class="clearBoth"></div>
								</div>
							</div>
						</c:if>
						<c:if test="${not empty  borrowInfo.attachmentInfo}">
							<div class="item">
								<i class="icon-item"></i> <span>资产信息</span>
								<p>${borrowInfo.attachmentInfo }</p>
							</div>
						</c:if>
						<c:if test="${not empty  disposalPlan.disposalPlan}">
							<div class="item">
								<i class="icon-item"></i> <span>处置结果预案</span>
								<p>${disposalPlan.disposalPlan}</p>
							</div>
						</c:if>
						<c:if test="${not empty repayPlanList and projectDeatil.isNew eq 0}">
							<c:if test="${projectDeatil.status ne 15}">
								<div class="item">
									<i class="icon-star"></i>
									<span>还款计划</span>
									<table  cellspacing="0" cellpadding="0" class="detail-car-table">
										<tr>
											<td>时间</td>
											<td>还款金额(元)</td>
											<td>类型</td>
										</tr>
										<c:forEach items="${repayPlanList}" var="item" varStatus="vs">
											<tr>
												<td><c:out value="${item.repayTime}"></c:out></td>
												<td><c:out value="${item.repayTotal}"></c:out></td>
												<td><c:out value="${item.repayType}"></c:out></td>
											</tr>
										</c:forEach>
									</table>
								</div>
							</c:if>
						</c:if>
					</c:if>
					<c:if test="${(projectDeatil.status eq 12 or projectDeatil.status eq 13 or projectDeatil.status eq 14) and investFlag eq 0 }">
						<p class="tac">只有投资本项目的用户可见详细信息</p>
					</c:if>
					<c:if test="${loginFlag eq 0 }">
						<p class="tac">登录用户可见项目详情，<a href="#" class="colorBlue hy-jumpLogin">点击登录</a></p>
					</c:if>
				</div>
			</c:if>
			<c:if test="${projectDeatil.type ne 9}">  <!-- 非汇资产项目  -->
				<!--项目信息开始-->
				<div class="tabItem active" id="infoOneId">
				<!-- 融通宝展示内容 -->
					<c:if test="${projectDeatil.type eq 13}">
							<div class="item">
							<dl class="new-detail-dl">
								<dd>
								<c:if test="${loginFlag eq 1 and (projectDeatil.status eq 10 or projectDeatil.status eq 11)}">
									<table class="new-detail-tb1 detail-car-table" cellpadding="0" cellspacing="0" border="0" >
	                           	      <tbody>
		                                <tr>
		                                    <td align="center" width="40%">项目名称</td>
		                                    <td>
		                                     <c:if test="${empty projectDeatil.borrowAssetNumber}">
											---
											</c:if>
		                                    ${projectDeatil.borrowAssetNumber}
		                                    </td>
		                                </tr>
		                            	<tr>
		                            		<td align="center">项目来源</td>
		                            		<td>
		                            		 <c:if test="${empty projectDeatil.borrowProjectSource}">
											---
											</c:if>
		                            		${projectDeatil.borrowProjectSource}
		                            		</td>
		                            	</tr>
		                            	<tr>
		                            		<td align="center">募集金额</td>
		                            		<td>
		                            		 <c:if test="${empty projectDeatil.borrowAccount}">
											---
											</c:if>
		                            		${projectDeatil.borrowAccount}元
		                            		</td>
		                            	</tr>
		                            	<tr>
		                            		<td align="center">历史年回报率</td>
		                            		<td>
		                            		 <c:if test="${empty projectDeatil.borrowApr && empty projectDeatil.borrowExtraYield}">
											---
											</c:if>
		                            		${projectDeatil.borrowApr}% + ${projectDeatil.borrowExtraYield}%
		                            		</td>
		                            	</tr>
		                            	<tr>
		                            		<td align="center">加入条件</td>
		                            		<td>
	                            			 <c:if test="${empty projectDeatil.tenderAccountMin && empty projectDeatil.increaseMoney}">
												---
												</c:if>
			                            		${projectDeatil.tenderAccountMin}元起投,
			                            		${projectDeatil.increaseMoney}元递增
		                            		</td>
		                            	</tr>
		                            	<tr>
		                            		<td align="center">投资限额</td>
		                            		<td>
		                            		<c:if test="${empty projectDeatil.tenderAccountMax}">
											---
											</c:if>
		                            		${projectDeatil.tenderAccountMax}元
		                            		</td>
		                            	</tr>
		                            	<tr>
		                            		<td align="center">发布时间</td>
		                            		<td>
		                            		<c:if test="${empty projectDeatil.sendTime or projectDeatil.sendTime eq '1970-01-01 08:00'}">
											${projectDeatil.onTime}
											</c:if>
											<c:if test="${not empty projectDeatil.sendTime and projectDeatil.sendTime ne '1970-01-01 08:00' and projectDeatil.sendTime ne ''}">
											${projectDeatil.sendTime}
											</c:if>
		                            		</td>
		                            	</tr>
		                            	<tr>
		                            		<td align="center">起息日期</td>
		                            		<td>
		                            		<c:if test="${empty projectDeatil.borrowInterestTime}">
											---
											</c:if>
		                            		${projectDeatil.borrowInterestTime}
		                            		</td>
		                            	</tr>
		                            	<tr>
		                            		<td align="center">到期日期 </td>
		                            		<td>
		                            		<c:if test="${empty projectDeatil.borrowDueTime}">
											---
											</c:if>
		                            		${projectDeatil.borrowDueTime}
		                            		</td>
		                            	</tr>
		                            	<tr>
		                            		<td align="center">保障方式</td>
		                            		<td>
		                            		<c:if test="${empty projectDeatil.borrowSafeguardWay}">
											---
											</c:if>
		                            		${projectDeatil.borrowSafeguardWay}
		                            		</td>
		                            	</tr>
		                            	<tr>
		                            		<td align="center">收益说明</td>
		                            		<td>
		                            		<c:if test="${empty projectDeatil.borrowIncomeDescription}">
											---
											</c:if>
		                            		${projectDeatil.borrowIncomeDescription}
		                            		</td>
		                            	</tr>
	                          	  </tbody>
	                        	</table>
								</c:if>
								<c:if test="${loginFlag eq 1 and (projectDeatil.status eq 12 or projectDeatil.status eq 13 or projectDeatil.status eq 14) and investFlag eq 1}">
									<table class="new-detail-tb1 detail-car-table" cellpadding="0" cellspacing="0" border="0" >
										<tbody>
										<tr>
											<td align="center" width="40%">项目名称</td>
											<td>
												<c:if test="${empty projectDeatil.borrowAssetNumber}">
													---
												</c:if>
													${projectDeatil.borrowAssetNumber}
											</td>
										</tr>
										<tr>
											<td align="center">项目来源</td>
											<td>
												<c:if test="${empty projectDeatil.borrowProjectSource}">
													---
												</c:if>
													${projectDeatil.borrowProjectSource}
											</td>
										</tr>
										<tr>
											<td align="center">募集金额</td>
											<td>
												<c:if test="${empty projectDeatil.borrowAccount}">
													---
												</c:if>
													${projectDeatil.borrowAccount}元
											</td>
										</tr>
										<tr>
											<td align="center">历史年回报率</td>
											<td>
												<c:if test="${empty projectDeatil.borrowApr && empty projectDeatil.borrowExtraYield}">
													---
												</c:if>
													${projectDeatil.borrowApr}% + ${projectDeatil.borrowExtraYield}%
											</td>
										</tr>
										<tr>
											<td align="center">加入条件</td>
											<td>
												<c:if test="${empty projectDeatil.tenderAccountMin && empty projectDeatil.increaseMoney}">
													---
												</c:if>
													${projectDeatil.tenderAccountMin}元起投,
													${projectDeatil.increaseMoney}元递增
											</td>
										</tr>
										<tr>
											<td align="center">投资限额</td>
											<td>
												<c:if test="${empty projectDeatil.tenderAccountMax}">
													---
												</c:if>
													${projectDeatil.tenderAccountMax}元
											</td>
										</tr>
										<tr>
											<td align="center">发布时间</td>
											<td>
												<c:if test="${empty projectDeatil.sendTime or projectDeatil.sendTime eq '1970-01-01 08:00'}">
													${projectDeatil.onTime}
												</c:if>
												<c:if test="${not empty projectDeatil.sendTime and projectDeatil.sendTime ne '1970-01-01 08:00' and projectDeatil.sendTime ne ''}">
													${projectDeatil.sendTime}
												</c:if>
											</td>
										</tr>
										<tr>
											<td align="center">起息日期</td>
											<td>
												<c:if test="${empty projectDeatil.borrowInterestTime}">
													---
												</c:if>
													${projectDeatil.borrowInterestTime}
											</td>
										</tr>
										<tr>
											<td align="center">到期日期 </td>
											<td>
												<c:if test="${empty projectDeatil.borrowDueTime}">
													---
												</c:if>
													${projectDeatil.borrowDueTime}
											</td>
										</tr>
										<tr>
											<td align="center">保障方式</td>
											<td>
												<c:if test="${empty projectDeatil.borrowSafeguardWay}">
													---
												</c:if>
													${projectDeatil.borrowSafeguardWay}
											</td>
										</tr>
										<tr>
											<td align="center">收益说明</td>
											<td>
												<c:if test="${empty projectDeatil.borrowIncomeDescription}">
													---
												</c:if>
													${projectDeatil.borrowIncomeDescription}
											</td>
										</tr>
										</tbody>
									</table>
								</c:if>
								<c:if test="${loginFlag eq 1 and (projectDeatil.status eq 12 or projectDeatil.status eq 13 or projectDeatil.status eq 14) and investFlag eq 0}">
									<p class="tac">只有投资本项目的用户可见详细信息</p>
								</c:if>
								<c:if test="${loginFlag eq 0 }">
									<p class="tac">登录用户可见项目详情，<a href="#" class="colorBlue hy-jumpLogin">点击登录</a></p>
								</c:if>
								</dd>
							</dl>
							</div>

						</c:if>
					<c:if test="${projectDeatil.type ne 13}">
					<c:if test="${loginFlag eq 1 }">
					  <c:if test="${projectDeatil.isNew eq 0 }">
						<c:if test="${projectDeatil.comOrPer eq 1}"> <!--  comOrPer 项目是个人项目还是企业项目 1企业 2个人  -->
							<c:if test="${not empty borrowInfo.borrowAddress ||not empty borrowInfo.regCaptial ||not empty borrowInfo.registTime || not empty projectDeatil.borrowLevel}">
							<c:if test="${borrowInfo.regCaptial ne null}">
							<div class="item">
								<i class="icon-item"></i> <span>基础信息</span>
								<div class="detailContent clearPadding">
									<ul>
										<c:if test="${not empty  borrowInfo.borrowAddress}">
										<li><span class="left">所在地区：</span> <span class="right">${borrowInfo.borrowAddress}</span>
										</li>
										</c:if>
										<c:if test="${not empty borrowInfo.regCaptial && borrowInfo.regCaptial != '0.00'}">
										<li><span class="left">注册资本：</span> <span class="right">${borrowInfo.regCaptial}元</span>
										</li>
										</c:if>
										<c:if test="${not empty  borrowInfo.registTime}">
										<li><span class="left">注册时间：</span> <span class="right">${borrowInfo.registTime}</span>
										</li>
										</c:if>
										<c:if test="${ not empty projectDeatil.borrowLevel and projectDeatil.borrowLevel ne '' }">
											<li><span class="left">信用评级：</span> <span class="right">${projectDeatil.borrowLevel}</span>
											</li>
										</c:if>
									</ul>
									<div class="clearBoth"></div>
								</div>
							</div>
							</c:if>
							</c:if>
						</c:if>
						<c:if test="${projectDeatil.comOrPer eq 2}"> <!--  comOrPer 项目是个人项目还是企业项目 1企业 2个人  -->
							<c:if test="${not empty borrowInfo.sex ||not empty borrowInfo.age ||not empty borrowInfo.maritalStatus ||not empty borrowInfo.workingCity ||not empty borrowInfo.position || not empty projectDeatil.borrowLevel}">
							<div class="item">
								<i class="icon-item"></i> <span>基础信息</span>
								<div class="detailContent clearPadding">
									<ul>
										<c:if test="${not empty  borrowInfo.sex}">
										<li style="width: 27%;display: inline-block;position: relative;"><span >性别：</span> <span >${borrowInfo.sex}</span>	</li>
										</c:if>
										<c:if test="${not empty  borrowInfo.age}">
										<c:if test="${borrowInfo.age ne 0}"> 
										<li style="width: 25%;display: inline-block;position: relative;"><span >年龄：</span> <span >${borrowInfo.age}</span>	</li>
										</c:if>
										</c:if>
										<c:if test="${not empty  borrowInfo.maritalStatus}">
										<li style="width: 36%;display: inline-block;position: relative;"><span >婚姻状况：</span> <span >${borrowInfo.maritalStatus}</span></li>
										</c:if>
										<c:if test="${not empty  borrowInfo.workingCity}">
										<li><span class="left">工作城市：</span> <span class="right">${borrowInfo.workingCity}</span>
										</li>
										</c:if>
										<c:if test="${not empty  borrowInfo.position}">
										<li><span class="left">职业岗位：</span> <span class="right">${borrowInfo.position}</span>
										</li>
										</c:if>
											<c:if test="${ not empty projectDeatil.borrowLevel and projectDeatil.borrowLevel ne '' }">
												<li><span class="left">信用评级：</span> <span class="right">${projectDeatil.borrowLevel}</span>
												</li>
											</c:if>
									</ul>
									<div class="clearBoth"></div>
								</div>
							</div>
						 </c:if>
					 </c:if>
						<c:if test="${not empty  vehiclePledgeList}">
						<div class="item">
							<i class="icon-item"></i> <span>车辆信息</span>
							 <div>
								<table  cellspacing="0" cellpadding="0" class="detail-car-table">
									<thead>
										<tr>
											<td>车辆品牌</td>
											<td>型号</td>
											<td>产地</td>
											<td>评估价（元）</td>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${vehiclePledgeList}" var="vehiclePledge">
											<tr>
												<td>
												<c:if test="${empty vehiclePledge.brand}">
												-
												</c:if>
												${vehiclePledge.brand}
												</td>
												<td>
												<c:if test="${empty vehiclePledge.model}">
												-
												</c:if>
												${vehiclePledge.model}
												</td>
												<td>
												<c:if test="${empty vehiclePledge.place}">
												-
												</c:if>
												${vehiclePledge.place}
												</td>
												<td>
												<c:if test="${empty vehiclePledge.toprice}">
												-
												</c:if>
												<span class="highlight"> ￥ ${vehiclePledge.toprice} 
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</div>
						</c:if>
						<c:if test="${not empty borrowInfo.borrowContents}">					
						<div class="item">
							<i class="icon-item"></i> <span>项目介绍</span>
							<p>${borrowInfo.borrowContents }</p>
						</div>
						</c:if>
						<c:if test="${not empty riskControl.controlMeasures}">						
						<div class="item">
							<i class="icon-item"></i> <span>风控措施</span>
							<p>${riskControl.controlMeasures }</p>
						</div>
						</c:if>
						<c:if test="${not empty authenList}">
							<%-- <div class="item">
								<i class="icon-checkInfo"></i> <span>认证信息</span>
								<table border="1" cellspacing="0" cellpadding="0">
									<c:forEach items="${authenList}" var="item" varStatus="vs">
										<tr>
											<td><c:out value="${item.authenName}"></c:out></td>
											<td><c:out value="${item.authenTime}"></c:out></td>
											<c:if test="${item.authenStatus eq 0}">
												<td><i class="icon-right colorGreen"></i>认证通过</td>
											</c:if>
											<c:if test="${item.authenStatus eq 1}">
												<td><i class="icon-right colorGreen"></i>未认证通过</td>
											</c:if>
										</tr>
									</c:forEach>
								</table>
							</div> --%>
						</c:if>
						<c:if test="${not empty repayPlanList}">
							<c:if test="${projectDeatil.status ne 15}">
								<div class="item">
									<i class="icon-star"></i> 
									<span>还款计划</span>
									<table  cellspacing="0" cellpadding="0" class="detail-car-table">
										<tr>
											<td>时间</td>
											<td>还款金额(元)</td>
											<td>类型</td>
										</tr>
										<c:forEach items="${repayPlanList}" var="item" varStatus="vs">
											<tr>
												<td><c:out value="${item.repayTime}"></c:out></td>
												<td><c:out value="${item.repayTotal}"></c:out></td>
												<td><c:out value="${item.repayType}"></c:out></td>
											</tr>
										</c:forEach>
									</table>
								</div>
							</c:if>
						</c:if>
					   </c:if>
					   <c:if test="${projectDeatil.isNew eq 1 }">
						   <!-- 用户登陆并且项目状态为投资中-->
						   <c:if test="${loginFlag eq 1 and (projectDeatil.status eq 10 or projectDeatil.status eq 11)}">
								<c:if test="${projectDeatil.comOrPer eq 1}"> <!--  comOrPer 项目是个人项目还是企业项目 1企业 2个人  -->
									<!--企业基础信息 开始-->
									<div class="item">
										<i class="icon-item"></i>
										<span>基础信息</span>
										<div class="detailContent">
											<ul>
												<c:if test="${not empty borrowInfo.companyName }">
													<li>
														<span class="left">融资主体：</span>
														<span class="right">${borrowInfo.companyName }</span>
													</li>
												</c:if>
												<c:if test="${not empty borrowInfo.borrowAddress }">
													<li>
														<span class="left">注册地区：</span>
														<span class="right">${borrowInfo.borrowAddress }</span>
													</li>
												</c:if>
												<c:if test="${not empty borrowInfo.regCaptial }">
													<li>
														<span class="left">注册资本：</span>
														<span class="right">${borrowInfo.regCaptialFormat }元</span>
													</li>
												</c:if>
												<c:if test="${not empty borrowInfo.registTime }">
													<li>
														<span class="left">注册时间：</span>
														<span class="right">${borrowInfo.registTime }</span>
													</li>
												</c:if>
												<c:if test="${not empty borrowInfo.socialCreditCode }">
													<li>
														<span class="left">统一社会信用代码：</span>
														<span class="right">${borrowInfo.socialCreditCodeAsterisked }</span>
													</li>
												</c:if>
												<c:if test="${not empty borrowInfo.registCode }">
													<li>
														<span class="left">注册号：</span>
														<span class="right">${borrowInfo.registCodeAsterisked }</span>
													</li>
												</c:if>
												<c:if test="${not empty borrowInfo.legalPerson }">
													<li>
														<span class="left">法定代表人：</span>
														<span class="right">${borrowInfo.legalPersonAsterisked }</span>
													</li>
												</c:if>
												<c:if test="${not empty borrowInfo.borrowIndustry }">
													<li>
														<span class="left">所属行业：</span>
														<span class="right">${borrowInfo.borrowIndustry }</span>
													</li>
												</c:if>
												<c:if test="${not empty borrowInfo.mainBusiness }">
													<li>
														<span class="left">主营业务：</span>
														<span class="right">${borrowInfo.mainBusiness }</span>
													</li>
												</c:if>
												<c:if test="${not empty projectDeatil.borrowLevel }">
													<li>
														<span class="left">信用评级：</span>
														<span class="right">${projectDeatil.borrowLevel }</span>
													</li>
												</c:if>
											</ul>
											<div class="clearBoth"></div>
										</div>
									</div>
									<!--企业基础信息结束-->
								</c:if>
								<c:if test="${projectDeatil.comOrPer eq 2}"> <!--  comOrPer 项目是个人项目还是企业项目 1企业 2个人  -->
									<!--个人基础信息开始-->
									<div class="item">
										<i class="icon-item"></i>
										<span>基础信息</span>
										<div class="detailContent">
											<ul>
												<c:if test="${not empty borrowInfo.trueName}">
													<li>
														<span class="left">姓名：</span>
														<span class="right">${borrowInfo.trueNameAsterisked}</span>
													</li>
												</c:if>
												<c:if test="${not empty borrowInfo.sex}">
													<li>
														<span class="left">性别：</span>
														<span class="right">${borrowInfo.sex}</span>
													</li>
												</c:if>
												<c:if test="${not empty borrowInfo.age and borrowInfo.age != 0}">
													<li>
														<span class="left">年龄：</span>
														<span class="right">${borrowInfo.age}</span>
													</li>
												</c:if>
												<c:if test="${not empty borrowInfo.maritalStatus}">
													<li>
														<span class="left">婚姻状况：</span>
														<span class="right">${borrowInfo.maritalStatus}</span>
													</li>
												</c:if>
												<c:if test="${not empty borrowInfo.cardNo}">
													<li>
														<span class="left">身份证号：</span>
														<span class="right">${borrowInfo.cardNoAsterisked}</span>
													</li>
												</c:if>
												<c:if test="${not empty borrowInfo.workingCity}">
													<li>
														<span class="left">工作城市：</span>
														<span class="right">${borrowInfo.workingCity}</span>
													</li>
												</c:if>
												<c:if test="${not empty borrowInfo.domicile}">
													<li>
														<span class="left">户籍地：</span>
														<span class="right">${borrowInfo.domicile}</span>
													</li>
												</c:if>
												<c:if test="${not empty borrowInfo.position}">
													<li>
														<span class="left">岗位职业：</span>
														<span class="right">${borrowInfo.position}</span>
													</li>
												</c:if>
												<c:if test="${not empty projectDeatil.borrowLevel }">
													<li>
														<span class="left">信用评级：</span>
														<span class="right">${projectDeatil.borrowLevel }</span>
													</li>
												</c:if>
											</ul>
											<div class="clearBoth"></div>
										</div>
									</div>
									<!--个人基础信息结束-->
								</c:if>
								<c:if test="${not empty mortgageList or not empty vehiclePledgeList}">
								<!--资产信息开始-->
								<div class="item">
									<i class="icon-item"></i>
									<span>资产信息</span>
									<div class="detailContent">
										<ul>
											<c:forEach items="${mortgageList}" var="mortgage">
												<c:if test="${not empty mortgage.propertyType}">
													<li>
														<span class="left">资产类型：</span>
														<span class="right">${mortgage.propertyType}</span>
													</li>
												</c:if>
												<c:if test="${not empty mortgage.grossArea}">
													<li>
														<span class="left">资产面积：</span>
														<span class="right">${mortgage.grossArea }㎡</span>
													</li>
												</c:if>
												<c:if test="${not empty mortgage.housesCnt}">
													<li>
														<span class="left">资产数量：</span>
														<span class="right">${mortgage.housesCnt }</span>
													</li>
												</c:if>
												<c:if test="${not empty mortgage.guarantyValue}">
													<li>
														<span class="left">评估价值：</span>
														<span class="right"><fmt:formatNumber value="${mortgage.guarantyValue }" pattern="#,##0.00" />元</span>
													</li>
												</c:if>
												<c:if test="${not empty mortgage.housesBelong}">
													<li>
														<span class="left">资产所属：</span>
														<span class="right">${mortgage.housesBelong }</span>
													</li>
												</c:if>
											</c:forEach>
											<c:forEach items="${vehiclePledgeList}" var="vehicle">
												<li>
													<span class="left">资产类型：</span>
													<span class="right">车辆</span>
												</li>
												<c:if test="${not empty vehicle.brand }">
													<li>
														<span class="left">车辆品牌：</span>
														<span class="right">${vehicle.brand }</span>
													</li>
												</c:if>
												<c:if test="${not empty vehicle.model }">
													<li>
														<span class="left">车辆型号：</span>
														<span class="right">${vehicle.model }</span>
													</li>
												</c:if>
												<c:if test="${not empty vehicle.place }">
													<li>
														<span class="left">车辆产地：</span>
														<span class="right">${vehicle.place }</span>
													</li>
												</c:if>
												<c:if test="${not empty vehicle.price }">
													<li>
														<span class="left">购买价格：</span>
														<span class="right"><fmt:formatNumber value="${vehicle.price }" pattern="#,##0.00" />元</span>
													</li>
												</c:if>
												<c:if test="${not empty vehicle.toprice }">
													<li>
														<span class="left">评估价值：</span>
														<span class="right"><fmt:formatNumber value="${vehicle.toprice }" pattern="#,##0.00" />元</span>
													</li>
												</c:if>
												<c:if test="${not empty vehicle.number }">
													<li>
														<span class="left">车牌号：</span>
														<span class="right">${vehicle.number }</span>
													</li>
												</c:if>
												<c:if test="${not empty vehicle.registration }">
													<li>
														<span class="left">车辆登记地：</span>
														<span class="right">${vehicle.registration }</span>
													</li>
												</c:if>
												<c:if test="${not empty vehicle.vin }">
													<li>
														<span class="left">车架号：</span>
														<span class="right">${vehicle.vin }</span>
													</li>
												</c:if>
											</c:forEach>
										</ul>
										<div class="clearBoth"></div>
									</div>
								</div>
								<!--资产信息结束-->
								</c:if>
								<!--项目介绍开始-->
								<c:if test="${!empty projectDeatil.borrowContents or !empty projectDeatil.fianceCondition or !empty projectDeatil.financePurpose or !empty projectDeatil.monthlyIncome or !empty projectDeatil.payment or !empty projectDeatil.firstPayment or !empty projectDeatil.secondPayment or !empty projectDeatil.costIntrodution}">
								<div class="item">
									<i class="icon-item"></i>
									<span>项目介绍</span>
									<div class="detailContent">
										<ul>
											<c:if test="${not empty projectDeatil.borrowContents }">
												<li>
													<span class="left">项目信息：</span>
													<span class="right">${projectDeatil.borrowContents }</span>
												</li>
											</c:if>
											<c:if test="${not empty projectDeatil.fianceCondition }">
												<li>
													<span class="left">财务状况：</span>
													<span class="right">${projectDeatil.fianceCondition }</span>
												</li>
											</c:if>
											<c:if test="${!empty projectDeatil.financePurpose }">
												<li>
													<span class="left">融资用途：</span>
													<span class="right">${projectDeatil.financePurpose }</span>
												</li>
											</c:if>
											<c:if test="${!empty projectDeatil.monthlyIncome }">
												<li>
													<span class="left">月薪收入：</span>
													<span class="right">${projectDeatil.monthlyIncome }</span>
												</li>
											</c:if>
											<c:if test="${!empty projectDeatil.payment }">
												<li>
													<span class="left">还款来源：</span>
													<span class="right">${projectDeatil.payment }</span>
												</li>
											</c:if>
											<c:if test="${!empty projectDeatil.firstPayment }">
												<li>
													<span class="left">第一还款来源：</span>
													<span class="right">${projectDeatil.firstPayment }</span>
												</li>
											</c:if>
											<c:if test="${!empty projectDeatil.secondPayment }">
												<li>
													<span class="left">第二还款来源：</span>
													<span class="right">${projectDeatil.secondPayment }</span>
												</li>
											</c:if>
											<c:if test="${!empty projectDeatil.costIntrodution }">
												<li>
													<span class="left">费用说明：</span>
													<span class="right">${projectDeatil.costIntrodution }</span>
												</li>
											</c:if>
										</ul>
										<div class="clearBoth"></div>
									</div>
								</div>
								</c:if>
								<!--项目介绍结束-->
								<!--信用状况开始-->
								<c:if test="${projectDeatil.comOrPer eq 1}">
									<div class="item">
										<i class="icon-item"></i>
										<span>信用状况</span>
										<div class="detailContent">
											<ul>
												<c:if test="${not empty borrowInfo.overdueTimes }">
													<li>
														<span class="left">在平台逾期次数：</span>
														<span class="right">${borrowInfo.overdueTimes }</span>
													</li>
												</c:if>
												<c:if test="${not empty borrowInfo.overdueAmount }">
													<li>
														<span class="left">在平台逾期金额：</span>
														<span class="right"><fmt:formatNumber value="${borrowInfo.overdueAmount }" pattern="#,##0.00" />元</span>
													</li>
												</c:if>
												<c:if test="${not empty projectDeatil.borrowLevel }">
													<li>
														<span class="left">信用评级：</span>
														<span class="right">${projectDeatil.borrowLevel }</span>
													</li>
												</c:if>
												<c:if test="${not empty borrowInfo.litigation }">
													<li>
														<span class="left">涉诉情况：</span>
														<span class="right">${borrowInfo.litigation }</span>
													</li>
												</c:if>
											</ul>
											<div class="clearBoth"></div>
										</div>
									</div>
								</c:if>
								<c:if test="${projectDeatil.comOrPer eq 2}">
									<div class="item">
										<i class="icon-item"></i>
										<span>信用状况</span>
										<div class="detailContent">
											<ul>
												<c:if test="${not empty borrowInfo.overdueTimes }">
													<li>
														<span class="left">在平台逾期次数：</span>
														<span class="right">${borrowInfo.overdueTimes }</span>
													</li>
												</c:if>
												<c:if test="${not empty borrowInfo.overdueAmount }">
													<li>
														<span class="left">在平台逾期金额：</span>
														<span class="right"><fmt:formatNumber value="${borrowInfo.overdueAmount }" pattern="#,##0.00" /></span>
													</li>
												</c:if>
												<c:if test="${not empty projectDeatil.borrowLevel }">
													<li>
														<span class="left">信用评级：</span>
														<span class="right">${projectDeatil.borrowLevel }</span>
													</li>
												</c:if>
												<c:if test="${not empty borrowInfo.litigation }">
													<li>
														<span class="left">涉诉情况：</span>
														<span class="right">${borrowInfo.litigation }</span>
													</li>
												</c:if>
											</ul>
											<div class="clearBoth"></div>
										</div>
									</div>
								</c:if>
								<!--信用状况结束-->
								<!--审核状况企业开始-->
								<c:if test="${projectDeatil.comOrPer eq 1}"> <!--  comOrPer 项目是个人项目还是企业项目 1企业 2个人  -->
									<div class="item">
										<i class="icon-item"></i>
										<span>审核状况</span>
										<div class="uditstatus detailContent">
											<ul>
												<c:if test="${borrowInfo.isCertificate eq 1}">
													<li>
														<span class="left">企业证件：</span>
														<span class="right">已审核<img class="verified-img" src="${ctx}/images/yishenhe.png" alt="" /></span>
													</li>
												</c:if>
												<c:if test="${borrowInfo.isOperation eq 1}">
													<li>
														<span class="left">经营状况：</span>
														<span class="right">已审核<img class="verified-img" src="${ctx}/images/yishenhe.png" alt="" /></span>
													</li>
												</c:if>
												<c:if test="${borrowInfo.isFinance eq 1}">
													<li>
														<span class="left">财务状况：</span>
														<span class="right">已审核<img class="verified-img" src="${ctx}/images/yishenhe.png" alt="" /></span>
													</li>
												</c:if>
												<c:if test="${borrowInfo.isEnterpriseCreidt eq 1}">
													<li>
														<span class="left">企业信用：</span>
														<span class="right">已审核<img class="verified-img" src="${ctx}/images/yishenhe.png" alt="" /></span>
													</li>
												</c:if>
												<c:if test="${borrowInfo.isLegalPerson eq 1}">
													<li>
														<span class="left">法人信用：</span>
														<span class="right">已审核<img class="verified-img" src="${ctx}/images/yishenhe.png" alt="" /></span>
													</li>
												</c:if>
												<c:if test="${borrowInfo.isAsset eq 1}">
													<li>
														<span class="left">资产状况：</span>
														<span class="right">已审核<img class="verified-img" src="${ctx}/images/yishenhe.png" alt="" /></span>
													</li>
												</c:if>
												<c:if test="${borrowInfo.isPurchaseContract eq 1}">
													<li>
														<span class="left">购销合同：</span>
														<span class="right">已审核<img class="verified-img" src="${ctx}/images/yishenhe.png" alt="" /></span>
													</li>
												</c:if>
												<c:if test="${borrowInfo.isSupplyContract eq 1}">
													<li>
														<span class="left">供销合同：</span>
														<span class="right">已审核<img class="verified-img" src="${ctx}/images/yishenhe.png" alt="" /></span>
													</li>
												</c:if>
											</ul>
											<div>
												${projectDeatil.borrowMeasuresMea}
											</div>
											<div class="clearBoth"></div>
										</div>
									</div>
								</c:if>
								<c:if test="${projectDeatil.comOrPer eq 2}"> <!--  comOrPer 项目是个人项目还是企业项目 1企业 2个人  -->
									<div class="item">
										<i class="icon-item"></i>
										<span>审核状况</span>
										<div class="uditstatus detailContent">
											<ul>
												<c:if test="${borrowInfo.isCard eq 1}">
													<li>
														<span class="left">身份证：</span>
														<span class="right">已审核<img class="verified-img" src="${ctx}/images/yishenhe.png" alt="" /></span>
													</li>
												</c:if>
												<c:if test="${borrowInfo.isIncome eq 1}">
													<li>
														<span class="left">收入状况：</span>
														<span class="right">已审核<img class="verified-img" src="${ctx}/images/yishenhe.png" alt="" /></span>
													</li>
												</c:if>
												<c:if test="${borrowInfo.isCredit eq 1}">
													<li>
														<span class="left">信用状况：</span>
														<span class="right">已审核<img class="verified-img" src="${ctx}/images/yishenhe.png" alt="" /></span>
													</li>
												</c:if>
												<c:if test="${borrowInfo.isAsset eq 1}">
													<li>
														<span class="left">资产状况：</span>
														<span class="right">已审核<img class="verified-img" src="${ctx}/images/yishenhe.png" alt="" /></span>
													</li>
												</c:if>
												<c:if test="${borrowInfo.isVehicle eq 1}">
													<li>
														<span class="left">车辆状况：</span>
														<span class="right">已审核<img class="verified-img" src="${ctx}/images/yishenhe.png" alt="" /></span>
													</li>
												</c:if>
												<c:if test="${borrowInfo.isDrivingLicense eq 1}">
													<li>
														<span class="left">行驶证：</span>
														<span class="right">已审核<img class="verified-img" src="${ctx}/images/yishenhe.png" alt="" /></span>
													</li>
												</c:if>
												<c:if test="${borrowInfo.isVehicleRegistration eq 1}">
													<li>
														<span class="left">车辆登记证：</span>
														<span class="right">已审核<img class="verified-img" src="${ctx}/images/yishenhe.png" alt="" /></span>
													</li>
												</c:if>
												<c:if test="${borrowInfo.isMerry eq 1}">
													<li>
														<span class="left">婚姻状况：</span>
														<span class="right">已审核<img class="verified-img" src="${ctx}/images/yishenhe.png" alt="" /></span>
													</li>
												</c:if>
												<c:if test="${borrowInfo.isWork eq 1}">
													<li>
														<span class="left">工作状况：</span>
														<span class="right">已审核<img class="verified-img" src="${ctx}/images/yishenhe.png" alt="" /></span>
													</li>
												</c:if>
												<c:if test="${borrowInfo.isAccountBook eq 1}">
													<li>
														<span class="left">户口本：</span>
														<span class="right">已审核<img class="verified-img" src="${ctx}/images/yishenhe.png" alt="" /></span>
													</li>
												</c:if>
											</ul>
											<div>
												${projectDeatil.borrowMeasuresMea}
											</div>
											<div class="clearBoth"></div>
										</div>
									</div>
								</c:if>
								<!--项目介绍结束-->
						   </c:if>
						   <c:if test="${loginFlag eq 1 and (projectDeatil.status eq 12 or projectDeatil.status eq 13 or projectDeatil.status eq 14) and investFlag eq 1}">
							   <c:if test="${projectDeatil.comOrPer eq 1}"> <!--  comOrPer 项目是个人项目还是企业项目 1企业 2个人  -->
								   <!--企业基础信息 开始-->
								   <div class="item">
									   <i class="icon-item"></i>
									   <span>基础信息</span>
									   <div class="detailContent">
										   <ul>
											   <c:if test="${not empty borrowInfo.companyName }">
												   <li>
													   <span class="left">融资主体：</span>
													   <span class="right">${borrowInfo.companyName }</span>
												   </li>
											   </c:if>
											   <c:if test="${not empty borrowInfo.borrowAddress }">
												   <li>
													   <span class="left">注册地区：</span>
													   <span class="right">${borrowInfo.borrowAddress }</span>
												   </li>
											   </c:if>
											   <c:if test="${not empty borrowInfo.regCaptial }">
												   <li>
													   <span class="left">注册资本：</span>
													   <span class="right">${borrowInfo.regCaptialFormat }元</span>
												   </li>
											   </c:if>
											   <c:if test="${not empty borrowInfo.registTime }">
												   <li>
													   <span class="left">注册时间：</span>
													   <span class="right">${borrowInfo.registTime }</span>
												   </li>
											   </c:if>
											   <c:if test="${not empty borrowInfo.socialCreditCode }">
												   <li>
													   <span class="left">统一社会信用代码：</span>
													   <span class="right">${borrowInfo.socialCreditCodeAsterisked }</span>
												   </li>
											   </c:if>
											   <c:if test="${not empty borrowInfo.registCode }">
												   <li>
													   <span class="left">注册号：</span>
													   <span class="right">${borrowInfo.registCodeAsterisked }</span>
												   </li>
											   </c:if>
											   <c:if test="${not empty borrowInfo.legalPerson }">
												   <li>
													   <span class="left">法定代表人：</span>
													   <span class="right">${borrowInfo.legalPersonAsterisked }</span>
												   </li>
											   </c:if>
											   <c:if test="${not empty borrowInfo.borrowIndustry }">
												   <li>
													   <span class="left">所属行业：</span>
													   <span class="right">${borrowInfo.borrowIndustry }</span>
												   </li>
											   </c:if>
											   <c:if test="${not empty borrowInfo.mainBusiness }">
												   <li>
													   <span class="left">主营业务：</span>
													   <span class="right">${borrowInfo.mainBusiness }</span>
												   </li>
											   </c:if>
											   <c:if test="${not empty projectDeatil.borrowLevel }">
												   <li>
													   <span class="left">信用评级：</span>
													   <span class="right">${projectDeatil.borrowLevel }</span>
												   </li>
											   </c:if>
										   </ul>
										   <div class="clearBoth"></div>
									   </div>
								   </div>
								   <!--企业基础信息结束-->
							   </c:if>
							   <c:if test="${projectDeatil.comOrPer eq 2}"> <!--  comOrPer 项目是个人项目还是企业项目 1企业 2个人  -->
								   <!--个人基础信息开始-->
								   <div class="item">
									   <i class="icon-item"></i>
									   <span>基础信息</span>
									   <div class="detailContent">
										   <ul>
											   <c:if test="${not empty borrowInfo.trueName}">
												   <li>
													   <span class="left">姓名：</span>
													   <span class="right">${borrowInfo.trueNameAsterisked}</span>
												   </li>
											   </c:if>
											   <c:if test="${not empty borrowInfo.sex}">
												   <li>
													   <span class="left">性别：</span>
													   <span class="right">${borrowInfo.sex}</span>
												   </li>
											   </c:if>
											   <c:if test="${not empty borrowInfo.age and borrowInfo.age != 0}">
												   <li>
													   <span class="left">年龄：</span>
													   <span class="right">${borrowInfo.age}</span>
												   </li>
											   </c:if>
											   <c:if test="${not empty borrowInfo.maritalStatus}">
												   <li>
													   <span class="left">婚姻状况：</span>
													   <span class="right">${borrowInfo.maritalStatus}</span>
												   </li>
											   </c:if>
											   <c:if test="${not empty borrowInfo.cardNo}">
												   <li>
													   <span class="left">身份证号：</span>
													   <span class="right">${borrowInfo.cardNoAsterisked}</span>
												   </li>
											   </c:if>
											   <c:if test="${not empty borrowInfo.workingCity}">
												   <li>
													   <span class="left">工作城市：</span>
													   <span class="right">${borrowInfo.workingCity}</span>
												   </li>
											   </c:if>
											   <c:if test="${not empty borrowInfo.domicile}">
												   <li>
													   <span class="left">户籍地：</span>
													   <span class="right">${borrowInfo.domicile}</span>
												   </li>
											   </c:if>
											   <c:if test="${not empty borrowInfo.position}">
												   <li>
													   <span class="left">岗位职业：</span>
													   <span class="right">${borrowInfo.position}</span>
												   </li>
											   </c:if>
											   <c:if test="${not empty projectDeatil.borrowLevel }">
												   <li>
													   <span class="left">信用评级：</span>
													   <span class="right">${projectDeatil.borrowLevel }</span>
												   </li>
											   </c:if>
										   </ul>
										   <div class="clearBoth"></div>
									   </div>
								   </div>
								   <!--个人基础信息结束-->
							   </c:if>
							   <c:if test="${not empty mortgageList or not empty vehiclePledgeList}">
								   <!--资产信息开始-->
								   <div class="item">
									   <i class="icon-item"></i>
									   <span>资产信息</span>
									   <div class="detailContent">
										   <ul>
											   <c:forEach items="${mortgageList}" var="mortgage">
												   <c:if test="${not empty mortgage.propertyType}">
													   <li>
														   <span class="left">资产类型：</span>
														   <span class="right">${mortgage.propertyType}</span>
													   </li>
												   </c:if>
												   <c:if test="${not empty mortgage.grossArea}">
													   <li>
														   <span class="left">资产面积：</span>
														   <span class="right">${mortgage.grossArea }㎡</span>
													   </li>
												   </c:if>
												   <c:if test="${not empty mortgage.housesCnt}">
													   <li>
														   <span class="left">资产数量：</span>
														   <span class="right">${mortgage.housesCnt }</span>
													   </li>
												   </c:if>
												   <c:if test="${not empty mortgage.guarantyValue}">
													   <li>
														   <span class="left">评估价值：</span>
														   <span class="right"><fmt:formatNumber value="${mortgage.guarantyValue }" pattern="#,##0.00" />元</span>
													   </li>
												   </c:if>
												   <c:if test="${not empty mortgage.housesBelong}">
													   <li>
														   <span class="left">资产所属：</span>
														   <span class="right">${mortgage.housesBelong }</span>
													   </li>
												   </c:if>
											   </c:forEach>
											   <c:forEach items="${vehiclePledgeList}" var="vehicle">
												   <li>
													   <span class="left">资产类型：</span>
													   <span class="right">车辆</span>
												   </li>
												   <c:if test="${not empty vehicle.brand }">
													   <li>
														   <span class="left">车辆品牌：</span>
														   <span class="right">${vehicle.brand }</span>
													   </li>
												   </c:if>
												   <c:if test="${not empty vehicle.model }">
													   <li>
														   <span class="left">车辆型号：</span>
														   <span class="right">${vehicle.model }</span>
													   </li>
												   </c:if>
												   <c:if test="${not empty vehicle.place }">
													   <li>
														   <span class="left">车辆产地：</span>
														   <span class="right">${vehicle.place }</span>
													   </li>
												   </c:if>
												   <c:if test="${not empty vehicle.price }">
													   <li>
														   <span class="left">购买价格：</span>
														   <span class="right"><fmt:formatNumber value="${vehicle.price }" pattern="#,##0.00" />元</span>
													   </li>
												   </c:if>
												   <c:if test="${not empty vehicle.toprice }">
													   <li>
														   <span class="left">评估价值：</span>
														   <span class="right"><fmt:formatNumber value="${vehicle.toprice }" pattern="#,##0.00" />元</span>
													   </li>
												   </c:if>
												   <c:if test="${not empty vehicle.number }">
													   <li>
														   <span class="left">车牌号：</span>
														   <span class="right">${vehicle.number }</span>
													   </li>
												   </c:if>
												   <c:if test="${not empty vehicle.registration }">
													   <li>
														   <span class="left">车辆登记地：</span>
														   <span class="right">${vehicle.registration }</span>
													   </li>
												   </c:if>
												   <c:if test="${not empty vehicle.vin }">
													   <li>
														   <span class="left">车架号：</span>
														   <span class="right">${vehicle.vin }</span>
													   </li>
												   </c:if>
											   </c:forEach>
										   </ul>
										   <div class="clearBoth"></div>
									   </div>
								   </div>
								   <!--资产信息结束-->
							   </c:if>
							   <!--项目介绍开始-->
							   <c:if test="${!empty projectDeatil.borrowContents or !empty projectDeatil.fianceCondition or !empty projectDeatil.financePurpose or !empty projectDeatil.monthlyIncome or !empty projectDeatil.payment or !empty projectDeatil.firstPayment or !empty projectDeatil.secondPayment or !empty projectDeatil.costIntrodution}">
								   <div class="item">
									   <i class="icon-item"></i>
									   <span>项目介绍</span>
									   <div class="detailContent">
										   <ul>
											   <c:if test="${not empty projectDeatil.borrowContents }">
												   <li>
													   <span class="left">项目信息：</span>
													   <span class="right">${projectDeatil.borrowContents }</span>
												   </li>
											   </c:if>
											   <c:if test="${not empty projectDeatil.fianceCondition }">
												   <li>
													   <span class="left">财务状况：</span>
													   <span class="right">${projectDeatil.fianceCondition }</span>
												   </li>
											   </c:if>
											   <c:if test="${!empty projectDeatil.financePurpose }">
												   <li>
													   <span class="left">融资用途：</span>
													   <span class="right">${projectDeatil.financePurpose }</span>
												   </li>
											   </c:if>
											   <c:if test="${!empty projectDeatil.monthlyIncome }">
												   <li>
													   <span class="left">月薪收入：</span>
													   <span class="right">${projectDeatil.monthlyIncome }</span>
												   </li>
											   </c:if>
											   <c:if test="${!empty projectDeatil.payment }">
												   <li>
													   <span class="left">还款来源：</span>
													   <span class="right">${projectDeatil.payment }</span>
												   </li>
											   </c:if>
											   <c:if test="${!empty projectDeatil.firstPayment }">
												   <li>
													   <span class="left">第一还款来源：</span>
													   <span class="right">${projectDeatil.firstPayment }</span>
												   </li>
											   </c:if>
											   <c:if test="${!empty projectDeatil.secondPayment }">
												   <li>
													   <span class="left">第二还款来源：</span>
													   <span class="right">${projectDeatil.secondPayment }</span>
												   </li>
											   </c:if>
											   <c:if test="${!empty projectDeatil.costIntrodution }">
												   <li>
													   <span class="left">费用说明：</span>
													   <span class="right">${projectDeatil.costIntrodution }</span>
												   </li>
											   </c:if>
										   </ul>
										   <div class="clearBoth"></div>
									   </div>
								   </div>
							   </c:if>
							   <!--项目介绍结束-->
							   <!--信用状况开始-->
							   <c:if test="${projectDeatil.comOrPer eq 1}">
								   <div class="item">
									   <i class="icon-item"></i>
									   <span>信用状况</span>
									   <div class="detailContent">
										   <ul>
											   <c:if test="${not empty borrowInfo.overdueTimes }">
												   <li>
													   <span class="left">在平台逾期次数：</span>
													   <span class="right">${borrowInfo.overdueTimes }</span>
												   </li>
											   </c:if>
											   <c:if test="${not empty borrowInfo.overdueAmount }">
												   <li>
													   <span class="left">在平台逾期金额：</span>
													   <span class="right"><fmt:formatNumber value="${borrowInfo.overdueAmount }" pattern="#,##0.00" />元</span>
												   </li>
											   </c:if>
											   <c:if test="${not empty projectDeatil.borrowLevel }">
												   <li>
													   <span class="left">信用评级：</span>
													   <span class="right">${projectDeatil.borrowLevel }</span>
												   </li>
											   </c:if>
											   <c:if test="${not empty borrowInfo.litigation }">
												   <li>
													   <span class="left">涉诉情况：</span>
													   <span class="right">${borrowInfo.litigation }</span>
												   </li>
											   </c:if>
										   </ul>
										   <div class="clearBoth"></div>
									   </div>
								   </div>
							   </c:if>
							   <c:if test="${projectDeatil.comOrPer eq 2}">
								   <div class="item">
									   <i class="icon-item"></i>
									   <span>信用状况</span>
									   <div class="detailContent">
										   <ul>
											   <c:if test="${not empty borrowInfo.overdueTimes }">
												   <li>
													   <span class="left">在平台逾期次数：</span>
													   <span class="right">${borrowInfo.overdueTimes }</span>
												   </li>
											   </c:if>
											   <c:if test="${not empty borrowInfo.overdueAmount }">
												   <li>
													   <span class="left">在平台逾期金额：</span>
													   <span class="right"><fmt:formatNumber value="${borrowInfo.overdueAmount }" pattern="#,##0.00" /></span>
												   </li>
											   </c:if>
											   <c:if test="${not empty projectDeatil.borrowLevel }">
												   <li>
													   <span class="left">信用评级：</span>
													   <span class="right">${projectDeatil.borrowLevel }</span>
												   </li>
											   </c:if>
											   <c:if test="${not empty borrowInfo.litigation }">
												   <li>
													   <span class="left">涉诉情况：</span>
													   <span class="right">${borrowInfo.litigation }</span>
												   </li>
											   </c:if>
										   </ul>
										   <div class="clearBoth"></div>
									   </div>
								   </div>
							   </c:if>
							   <!--信用状况结束-->
							   <!--审核状况企业开始-->
							   <c:if test="${projectDeatil.comOrPer eq 1}"> <!--  comOrPer 项目是个人项目还是企业项目 1企业 2个人  -->
								   <div class="item">
									   <i class="icon-item"></i>
									   <span>审核状况</span>
									   <div class="uditstatus detailContent">
										   <ul>
											   <c:if test="${borrowInfo.isCertificate eq 1}">
												   <li>
													   <span class="left">企业证件：</span>
													   <span class="right">已审核<img class="verified-img" src="${ctx}/images/yishenhe.png" alt="" /></span>
												   </li>
											   </c:if>
											   <c:if test="${borrowInfo.isOperation eq 1}">
												   <li>
													   <span class="left">经营状况：</span>
													   <span class="right">已审核<img class="verified-img" src="${ctx}/images/yishenhe.png" alt="" /></span>
												   </li>
											   </c:if>
											   <c:if test="${borrowInfo.isFinance eq 1}">
												   <li>
													   <span class="left">财务状况：</span>
													   <span class="right">已审核<img class="verified-img" src="${ctx}/images/yishenhe.png" alt="" /></span>
												   </li>
											   </c:if>
											   <c:if test="${borrowInfo.isEnterpriseCreidt eq 1}">
												   <li>
													   <span class="left">企业信用：</span>
													   <span class="right">已审核<img class="verified-img" src="${ctx}/images/yishenhe.png" alt="" /></span>
												   </li>
											   </c:if>
											   <c:if test="${borrowInfo.isLegalPerson eq 1}">
												   <li>
													   <span class="left">法人信用：</span>
													   <span class="right">已审核<img class="verified-img" src="${ctx}/images/yishenhe.png" alt="" /></span>
												   </li>
											   </c:if>
											   <c:if test="${borrowInfo.isAsset eq 1}">
												   <li>
													   <span class="left">资产状况：</span>
													   <span class="right">已审核<img class="verified-img" src="${ctx}/images/yishenhe.png" alt="" /></span>
												   </li>
											   </c:if>
											   <c:if test="${borrowInfo.isPurchaseContract eq 1}">
												   <li>
													   <span class="left">购销合同：</span>
													   <span class="right">已审核<img class="verified-img" src="${ctx}/images/yishenhe.png" alt="" /></span>
												   </li>
											   </c:if>
											   <c:if test="${borrowInfo.isSupplyContract eq 1}">
												   <li>
													   <span class="left">供销合同：</span>
													   <span class="right">已审核<img class="verified-img" src="${ctx}/images/yishenhe.png" alt="" /></span>
												   </li>
											   </c:if>
										   </ul>
										   <div>
												   ${projectDeatil.borrowMeasuresMea}
										   </div>
										   <div class="clearBoth"></div>
									   </div>
								   </div>
							   </c:if>
							   <c:if test="${projectDeatil.comOrPer eq 2}"> <!--  comOrPer 项目是个人项目还是企业项目 1企业 2个人  -->
								   <div class="item">
									   <i class="icon-item"></i>
									   <span>审核状况</span>
									   <div class="uditstatus detailContent">
										   <ul>
											   <c:if test="${borrowInfo.isCard eq 1}">
												   <li>
													   <span class="left">身份证：</span>
													   <span class="right">已审核<img class="verified-img" src="${ctx}/images/yishenhe.png" alt="" /></span>
												   </li>
											   </c:if>
											   <c:if test="${borrowInfo.isIncome eq 1}">
												   <li>
													   <span class="left">收入状况：</span>
													   <span class="right">已审核<img class="verified-img" src="${ctx}/images/yishenhe.png" alt="" /></span>
												   </li>
											   </c:if>
											   <c:if test="${borrowInfo.isCredit eq 1}">
												   <li>
													   <span class="left">信用状况：</span>
													   <span class="right">已审核<img class="verified-img" src="${ctx}/images/yishenhe.png" alt="" /></span>
												   </li>
											   </c:if>
											   <c:if test="${borrowInfo.isAsset eq 1}">
												   <li>
													   <span class="left">资产状况：</span>
													   <span class="right">已审核<img class="verified-img" src="${ctx}/images/yishenhe.png" alt="" /></span>
												   </li>
											   </c:if>
											   <c:if test="${borrowInfo.isVehicle eq 1}">
												   <li>
													   <span class="left">车辆状况：</span>
													   <span class="right">已审核<img class="verified-img" src="${ctx}/images/yishenhe.png" alt="" /></span>
												   </li>
											   </c:if>
											   <c:if test="${borrowInfo.isDrivingLicense eq 1}">
												   <li>
													   <span class="left">行驶证：</span>
													   <span class="right">已审核<img class="verified-img" src="${ctx}/images/yishenhe.png" alt="" /></span>
												   </li>
											   </c:if>
											   <c:if test="${borrowInfo.isVehicleRegistration eq 1}">
												   <li>
													   <span class="left">车辆登记证：</span>
													   <span class="right">已审核<img class="verified-img" src="${ctx}/images/yishenhe.png" alt="" /></span>
												   </li>
											   </c:if>
											   <c:if test="${borrowInfo.isMerry eq 1}">
												   <li>
													   <span class="left">婚姻状况：</span>
													   <span class="right">已审核<img class="verified-img" src="${ctx}/images/yishenhe.png" alt="" /></span>
												   </li>
											   </c:if>
											   <c:if test="${borrowInfo.isWork eq 1}">
												   <li>
													   <span class="left">工作状况：</span>
													   <span class="right">已审核<img class="verified-img" src="${ctx}/images/yishenhe.png" alt="" /></span>
												   </li>
											   </c:if>
											   <c:if test="${borrowInfo.isAccountBook eq 1}">
												   <li>
													   <span class="left">户口本：</span>
													   <span class="right">已审核<img class="verified-img" src="${ctx}/images/yishenhe.png" alt="" /></span>
												   </li>
											   </c:if>
										   </ul>
										   <div>
												   ${projectDeatil.borrowMeasuresMea}
										   </div>
										   <div class="clearBoth"></div>
									   </div>
								   </div>
							   </c:if>
							   <!--项目介绍结束-->
						   </c:if>
					   </c:if>
						</c:if>
						<c:if test="${(projectDeatil.status eq 12 or projectDeatil.status eq 13 or projectDeatil.status eq 14) and investFlag eq 0 }">
							<p class="tac">只有投资本项目的用户可见详细信息</p>
						</c:if>
					<c:if test="${loginFlag eq 0 }">
						<p class="tac">登录用户可见项目详情，<a href="#" class="colorBlue hy-jumpLogin">点击登录</a></p>
					</c:if>
					</c:if>
				</div>
			</c:if>
			<!--项目信息结束-->
			<!--还款计划开始-->
			<c:if test="${projectDeatil.type ne 13 and projectDeatil.isNew eq 1}">
				<div class="tabItem" id="infoThreeId">
					<c:if test="${not empty repayPlanList}">
						<c:if test="${projectDeatil.status ne 15}">
							<div class="item">
								<i class="icon-star"></i> 
								<span>还款计划</span>
								<table  cellspacing="0" cellpadding="0" class="detail-car-table">
									<tr>
										<td>时间</td>
										<td>还款金额(元)</td>
										<td>类型</td>
									</tr>
									<c:forEach items="${repayPlanList}" var="item" varStatus="vs">
										<tr>
											<td><c:out value="${item.repayTime}"></c:out></td>
											<td><c:out value="${item.repayTotal}"></c:out></td>
											<td><c:out value="${item.repayType}"></c:out></td>
										</tr>
									</c:forEach>
								</table>
							</div>
						</c:if>
					</c:if>
				</div>
			</c:if>
			<!--相关文件开始-->
			<c:if test="${projectDeatil.type ne 13 and projectDeatil.isNew eq 0}">
				<div class="tabItem" id="infoThreeId">
				<c:if test="${loginFlag eq 1 }">
					<!--轮播开始-->
					<div class="swiper-container">
					<c:choose>
						<c:when test="${empty fileList}">
							<p class="tac">无相关文件</p>
						</c:when>
						<c:when test="${fileVisible eq 0}">
							<p class="tac">只有投资本项目的用户可见相关文件</p>
						</c:when>
						<c:otherwise>
							<ul class="swiper-wrapper">
								<c:forEach items="${fileList}" var="item" varStatus="vs">
									<li class="swiper-slide"><img class="sliderImg"
										src="${item.fileUrl}">
									<p class="img_name">${item.fileName}</p></li>
								</c:forEach>
							</ul>
						</c:otherwise>
					</c:choose>
					</div>
					<div class="pagination"></div>
					<p>&nbsp;</p>
					<!--轮播结束-->
				</c:if>
				<c:if test="${loginFlag eq 0 }">
					<p class="tac">登录用户可见相关文件，<a href="#" class="colorBlue hy-jumpLogin">点击登录</a></p>
				</c:if>
				</div>
			</c:if>
			<!--相关文件结束-->
			<!--还款计划结束-->
			<!--投资记录开始-->
			<div class="tabItem" id="infoFourId">
				<div class="tac">
					<img src="${ctx}/img/loadingImg.gif" alt="" class="loadingImg">
				</div>
			</div>
			<!--投资记录结束-->

			<!-- 常见问题开始 -->
			<div class="tabItem">
					<c:if test="${projectDeatil.type eq 13}">
					<div class="jobs-list">
					<dl>
							<dt class="iconfont iconfont-gotop" data-id="">
								<span class="title">1、什么是地方金融资产交易所？ </span>
							</dt>
							<dd>
								<p>地方金融资产交易所是由地方省委省政府批准设立，并由地方政府金融办监管的，为金融产品提供登记、托管、交易和结算等提供场所设施和服务的组织机构。</p>
							</dd>
						</dl>
						<dl>
							<dt class="iconfont iconfont-gotop" data-id="">
								<span class="title">2、什么是“优选债权”？ </span>
							</dt>
							<dd>
								<p>“优选债权”是与地方金融资产交易所合作推出的资产交易类产品，资产持有人通过汇盈金服居间撮合，将资产或其收益权转让给出借方，出借方获取一定数额的投资收益。</p>
							</dd>
						</dl>
						<dl>
							<dt class="iconfont iconfont-gotop" data-id="">
								<span class="title">3、“优选债权”的特点是什么？</span>
							</dt>
							<dd>
								<p>“优选债权”发布的产品是在地方金融资产交易所挂牌的项目，项目均经过金交所严格的审核，并提供专业的风控措施，保证项目的优质性。出借人可根据自身投资偏好选择适合项目投资。</p>
							</dd>
						</dl>
						<dl>
							<dt class="iconfont iconfont-gotop" data-id="">
								<span class="title">4、我可以认购“优选债权”产品吗？</span>
							</dt>
							<dd>
								<p>“优选债权”下每支产品的出借人数不能超过200人；出借人持有本人中华人民共和国居民身份证的公民，且年满十八周岁。</p>
							</dd>
						</dl>
						<dl>
							<dt class="iconfont iconfont-gotop" data-id="">
								<span class="title">5、认购“优选债权”产品需要收费吗？ </span>
							</dt>
							<dd>
								<p>出借人暂时无需支付认购费、管理费。</p>
							</dd>
						</dl>
						<dl>
						<dt class="iconfont iconfont-gotop" data-id="">
							<span class="title">6.出借人风险测评目的和规则是什么？</span>
						</dt>
						<dd>
							<p>为完善对出借人风险承受能力的尽职评估，实现对出借人的等级管理，保障出借人购买合适的产品，根据出借人风险测评的结果，将出借人风险承受能力由低到高分为保守型、稳健型、成长型、进取型四种类型。</p>
						</dd>
					</dl>
					</div>
					</c:if>
					<c:if test="${projectDeatil.type ne 13}">
					<div class="jobs-list">
					<dl>
						<dt class="iconfont iconfont-gotop" data-id="">
							<span class="title">1、我可以投资吗？</span>
						</dt>
						<dd>
							<p>持有本人中华人民共和国居民身份证的公民，且年满十八周岁，都可在汇盈金服网站上进行注册、完成实名认证，成为出借人。</p>
						</dd>
					</dl>

					<dl>
						<dt class="iconfont iconfont-gotop" data-id="">
							<span class="title">2、怎样进行投资？</span>
						</dt>
						<dd>
							<p>请您按照以下步骤进行投资：</p>
							<p>①. 在汇盈金服网站或手机客户端上进行注册、通过实名认证、成功绑定银行卡；</p>
							<p>②. 完成出借人风险测评；</p>
							<p>③. 账户充值；</p>
							<p>④. 浏览平台借款项目，根据个人风险偏好自主选择项目投资；</p>
							<p>⑤. 确认投资，投资成功。</p>
						</dd>
					</dl>

					<dl>
						<dt class="iconfont iconfont-gotop" data-id="">
							<span class="title">3、投资后是否可以提前退出？</span>
						</dt>
						<dd>
							<p>①. 平台产品暂不支持提前回款申请。</p>
							<p>②. “债权”项目中除“优选债权”外，都支持债权转让。</p>
						</dd>
					</dl>

					<dl>
						<dt class="iconfont iconfont-gotop" data-id="">
							<span class="title">4、为何投标后会显示资金冻结？</span>
						</dt>
						<dd>
							<p>对于所有投资项目，出借方可自主选择进行投资。在项目完成放款之前，投资金额将被冻结；在项目完成放款之后，投资金额将通过江西银行转给借款方；如果在限定时间内未满标，则根据情况将已借款金放款给借款方或原路返还出借方。</p>
						</dd>
					</dl>

					<dl>
						<dt class="iconfont iconfont-gotop" data-id="">
							<span class="title">5、在汇盈金服投资有哪些费用？</span>
						</dt>
						<dd>
							<p>在汇盈金服平台进行投资，平台本身不收取出借方任何费用，出借方在充值/提现时江西银行会收取相关手续费。</p>
							<p>**特别提示：提现的手续费的计算方式为1元/笔</p>
						</dd>
					</dl>
					<dl>
						<dt class="iconfont iconfont-gotop" data-id="">
							<span class="title">6、出借人风险测评目的和规则是什么？</span>
						</dt>
						<dd>
							<p>为完善对出借方风险承受能力的尽职评估，实现对出借方的等级管理，保障出借方购买合适的产品，根据出借方风险测评的结果，将出借方风险承受能力由低到高分为保守型、稳健型、成长型、进取型四种类型。</p>
						</dd>
					</dl>
				</div>
					</c:if>
				
			</div>
			<!-- 常见问题结束 -->
		</div>
		<div class="grayHeight"></div>
		<!--tab结束-->
		<!--列表详情内容结束-->
		<!------------------------------------- 老版本结束--------------------------------------------------------------->
		<!--列表详情底部开始-->
		<div class="foot-left">
			<a href=""><img src="${ctx}/img/cal.png" /></a>
		</div>
		<div class="foot bg_grey_new">
			<!-- 定时发标 -->
			<c:if test="${projectDeatil.status eq 10}">
				<span>${projectDeatil.onTime}开标</span>
			</c:if>
			<!-- 投资中 -->
			<c:if test="${projectDeatil.status eq 11}">
				<span class="btn_bg_color textWhite"><a id="investNow"
					class="textWhite hy-jumpH5">立即投资</a></span>
			</c:if>
			<!-- 复审中 -->
			<c:if test="${projectDeatil.status eq 12}">
				<span>复审中</span>
			</c:if>
			<!-- 还款中 -->
			<c:if test="${projectDeatil.status eq 13}">
				<span>还款中</span>
			</c:if>
			<!-- 已还款 -->
			<c:if test="${projectDeatil.status eq 14}">
				<span>已还款</span>
			</c:if>
			<!-- 已流标 -->
			<c:if test="${projectDeatil.status eq 15}">
				<span>已流标</span>
			</c:if>
		</div>
		<!--列表详情底部结束-->
	</div>
	<script src="${ctx}/js/doT.min.js" type="text/javascript"
		charset="utf-8"></script>
	<script src="${ctx}/js/jquery.min.js" type="text/javascript"
		charset="utf-8"></script>
	<script src="${ctx}/js/common.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctx}/js/hyjf.js" type="text/javascript" charset="utf-8"></script>	
	<script src="${ctx}/js/fill.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctx}/js/idangerous.swiper.min.js" type="text/javascript"
		charset="utf-8"></script>
	<script src="${ctx}/js/jquery.bxslider.js" type="text/javascript"
		charset="utf-8"></script>

	<script type="text/javascript">
		//设置计算器URL
		$(function(){
				var urlCurrent = window.location.host+"/hyjf-app/jsp/cal.jsp";
				var types = ["按月计息，到期还本还息","按天计息，到期还本还息","先息后本","等额本息","等额本金","按月计息，到期还本息","按天计息，到期还本息"]
				var percent = parseInt($(".detailHeadLeft b").text());
				var time = parseInt($(".detailHeadMiddle").text());
				var type = $.trim($(".return-type").text())
				for(var i = 0;i<5;i ++){
					if(types[i]==type){
						var calType = i;
					}
				}
				if(!calType){
					for(var j = 0;j<2;j ++){
						if(types[j+5]==type){
							var calType = j;
						}
					}
				}
				$(".foot-left a").prop("href",hyjfArr.hyjf+'://jumpH5/?{"url":"http://'+urlCurrent+'?percent='+percent+'&time='+time+'&calType='+calType+'"}')
				//设置图片居中
				var leftFootHeight = ($(".foot-left").height()-25)/2;
				$(".foot-left img").css("margin-top",leftFootHeight+"px")
				//设置计算器背景色
				if($(".foot span").hasClass("btn_bg_color")){
					$(".foot-left").css("background","#eb6100")
				}else{
					$(".foot-left").css("background","#999999")
				}
		})
        var mySwiper = new Swiper('.swiper-container',{
        	autoplay : 3000,//可选选项，自动滑动
        	loop : true,//可选选项，开启循环
        	speed:1000,
        	pagination : '.pagination',
        	paginationClickable :true
        	})
    </script>
	<script id="tmpl-fourData" type="text/x-dot-template">
				{{? it.investList!=0}}
					<div class="item" id="moreTable2">
						<table border="0" cellspacing="0" cellpadding="0" class="borderNone" >
							<tr>
								<th></th>
								<th>用户名</th>
								<th>投资金额（元）</th>
								<th>投资时间</th>
							</tr>
							
							{{~ it.investList : v : index}}
								<tr>
									<td class="padding-0">{{? v.vipId!=0}}<img src="${ctx}/img/project-vip.png" alt="vip" class="project-vip-img"/>{{?}}</td>
            						<td>{{=v.userName}}</td>            						<td>{{=v.account}}</td>
            						<td>{{=v.investTime}}</td>
        						</tr>
							{{~}}
							
						</table>
						{{if(it.investListTotal>10){}}
						<div class="loadMore" >加载更多...</div>
						{{}else{}}
						<div></div>
						{{}}}
					</div>
					{{??}}<p class="tac">无投资记录</p>
				{{?}}
	
	</script>
	<script>
	//loadmore ajax
	//no.2 load more	
		 $("#infoTwoId").delegate(".loadMore","click",function(){
				var $page = Math.floor(($("#moreTable1 table tr").length-1)/10)+1;
				var $borrowNid = "${projectDeatil.borrowNid}";	
				var $platform="${platform}";
				var $token="${token}";
				var $sign="${sign}";
				var $randomString="${randomString}";
				var $order="${order}";
				var $consumeUrl=$("#consumeUrl").val();
				 if($token){
					data = {borrowNid:$borrowNid,page:$page,platform:$platform,token:$token,sign:$sign,randomString:$randomString,order:$order}
				}else{
					data ={borrowNid:$borrowNid,page:$page,platform:$platform,sign:$sign,randomString:$randomString,order:$order}
				} 
				 $.ajax({
						type:"get",
				 		url:$consumeUrl,
						data:{borrowNid:$borrowNid,page:$page,platform:$platform,sign:$sign,randomString:$randomString,order:$order},
						datatype:"json",
						success:function(data){
							var totalPage = Math.floor(data.consumeTotal/10)+1;
							if(!data){
								$("#moreTable1 .loadMore").hide();
							}else if(data&&data.status=="0"){
								var str="";
								for(var i=0;i<data.consumeList.length;i++){
									str=str+"<tr><td>"+data.consumeList[i].name+"</td><td>"+data.consumeList[i].idCard+"</td><td>"+data.consumeList[i].account+"</td></tr>";
								}
								$("#moreTable1 table").append(str);
								if(totalPage==$page){
									$("#moreTable1 .loadMore").hide();
								}
							}						
						} 		
				}) 
			}) 
	</script>
	<script>
	//no.4 load more
	$("#infoFourId").delegate(".loadMore","click",function(){
		var $page = Math.floor(($("#moreTable2 table tr").length-1)/10)+1;
		var $borrowNid = "${projectDeatil.borrowNid}";
		var $platform="${platform}";
		var $token="${token}"; 
		var $sign="${sign}";
		var $randomString="${randomString}";
		var $order="${order}";
			 if($token){		
				data = {borrowNid:$borrowNid,page:$page,platform:$platform,token:$token,sign:$sign,randomString:$randomString,order:$order};
			}else{
				data ={borrowNid:$borrowNid,page:$page,platform:$platform,sign:$sign,randomString:$randomString,order:$order};
			} 
			 $.ajax({
					type:"get",
			 		url:"${projectDeatil.tabFourUrl}",
					data:data,
					datatype:"json",
					success:function(data){
						var totalPage = Math.floor(data.investListTotal/10)+1;
						if(!data){
							$("#moreTable2 .loadMore").hide();
						}else if(data&&data.status=="0"){
							var str="";
							var proImg = "";
							for(var i=0;i<data.investList.length;i++){
								/*加判断  */
								+data.investList[i].vipId===0?proImg ='': proImg = '<img src="${ctx}/img/project-vip.png" alt="vip" class="project-vip-img"/>';
								str=str+"<tr><td>"+proImg+"</td><td>"+data.investList[i].userName+"</td><td>"+data.investList[i].account+"</td><td>"+data.investList[i].investTime+"</td></tr>";		
							}
							$("#moreTable2 table").append(str);
							if(totalPage==$page){
								$("#moreTable2 .loadMore").hide();
							}
						}						
					} 		
			}) 
		})
	</script>
	<script>
	//增加href中 borrowNid
	function addId(){
		var $borrowNid = "${projectDeatil.borrowNid}";
		var $order="${order}";
		var $token="${token}"; 
	    var $platform="${platform}";
	    var $sign="${sign}";
	    var $randomString="${randomString}";
	    var $mobile = $("#mobile").val();
	    var $openAccountUrl = $("#openAccountUrl").val();
	    var url = "${projectDeatil.investUrl}";
	    var projectType = +$("#projectType").val();
	    var isSetPassword = $("#isSetPassword").val();
	    //
	    //console.log(url+'/?{\"url":\"'+$openAccountUrl+"/?token="+$token+"&order="+$order+"&platform="+$platform+"&sign="+$sign+"&randomString="+$randomString+"&mobile="+$mobile+'"}')
	   	//如果未设置交易密码 跳到设置交易密码页面
	    if(url.indexOf("jumpLogin")!=-1){
	    		$("#investNow").prop("href",url+"/?");
	   	}else if(url.indexOf("jumpH5")!=-1){    	  //未开户
		    	var $nHref = url+'/?{"url":"'+$openAccountUrl+"?token="+$token+"&order="+$order+"&platform="+$platform+"&sign="+$sign+"&randomString="+$randomString+"&mobile="+$mobile+'"}';
		    	$(".openPopDivSpan a").prop("href",$nHref);
				$("#investNow").on("click",function(){
					$(".openPopDiv,.openPop").show();
				})
				$(".openPopDivClose").on("click",function(){
					$(".openPopDiv,.openPop").hide();
				})
	    }
	    else if(isSetPassword == 0){       //设置交易密码
	   		jumpH5("/hyjf-app/bank/user/transpassword/setPassword.do?sign="+$sign+"&token="+$token,0,1)
	    }else if(projectType===13){
		    var $newHref = "${projectDeatil.investUrl}"+"/?{"+"\"borrowNid\":\""+ $borrowNid+"\",\"type\":\"RTB\"}";
		    $("#investNow").prop("href",$newHref);
	    }else{
	    	 var $newHref = "${projectDeatil.investUrl}"+"/?{"+"\"borrowNid\":\""+ $borrowNid+"\"}";
		     $("#investNow").prop("href",$newHref);
	    }
	}
	addId();
	</script>
	<script>
	var $token=$("#token").val();
	$("#tab4").click(
			function(){
				if($token){
					$.fillTmplByAjax("${projectDeatil.tabFourUrl}",{"borrowNid":"${projectDeatil.borrowNid }","platform":"${platform}","token":"${token}","sign":"${sign}","randomString":"${randomString}","order":"${order}"}, "#infoFourId", "#tmpl-fourData");		
				}else{
					$.fillTmplByAjax("${projectDeatil.tabFourUrl}",{"borrowNid":"${projectDeatil.borrowNid }","platform":"${platform}","sign":"${sign}","randomString":"${randomString}","order":"${order}"}, "#infoFourId", "#tmpl-fourData");
				}
			}
	);
	
	</script>
</body>
<script src="${ctx}/js/util.js" type="text/javascript" charset="utf-8"></script>
	<script>
	$(function() {
		$(".jobs-list dl dt").click(
				function() {
					var dt = $(this);
					var dl = dt.parent("dl");
					var otherdl = dl.siblings("dl");
					var dd = dt.next("dd");
					if (dd.is(":hidden")) {
						dd.slideDown(500);
						dl.addClass("active");
						otherdl.removeClass("active")
								.children("dd:visible").slideUp(500);
					} else {
						dd.slideUp(500);
						dl.removeClass("active");
					}
				})
	});
	
	</script>
</html>