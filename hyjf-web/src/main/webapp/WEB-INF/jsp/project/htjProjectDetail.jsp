<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page language="java" import="java.util.Date"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta charset="utf-8" />
<title>${projectDeatil.borrowNid}项目详情- 汇盈金服官网</title>
<link rel="stylesheet" type="text/css"
	href="${cdn}/css/baguetteBox.min.css" />
<%@ include file="/head.jsp"%>
<script>
	var projectType = "";
</script>
</head>

<body>
	<%@ include file="/header.jsp"%>
	
	<div class="new-detail-con">
			<input id="type" name="type" type="hidden" value="${projectDeatil.type}"/>
			<input type="hidden" id="increase" value="${projectDeatil.increaseMoney}" title="递增金额" />
		<input id="borrowNid" name="borrowNid" type="hidden" value="${projectDeatil.borrowNid}" />
		<input id="isLast" type="hidden" value="${projectDeatil.tenderAccountMin ge InvestAccountInt}"/>
		

		<div class="new-detail-inner">
				<h4>
				<span class="title">项目编号：${projectDeatil.borrowNid}</span>
				<span class="date">
                    <span>起投金额：<fmt:formatNumber value="${projectDeatil.tenderAccountMin}" pattern="#,###" />元起投</span> 
	           </span>
			</h4>
			<div class="new-detail-hd">
				<div class="hd1">
					<div class="con1">
						<div class="num">
							<fmt:formatNumber value="${projectDeatil.borrowAccount}" pattern="#,###" />
							<span>元</span>
						</div>
						<div class="con-title">项目金额</div>
					</div>
					<div class="con2">
						<div class="num highlight">
							${projectDeatil.borrowApr}<span>%</span>
						</div>
						<div class="con-title">历史年回报率</div>
					</div>
					<div class="con3">
						<div class="num">${projectDeatil.borrowPeriod}
							<span>${projectDeatil.borrowPeriodType}</span>
						</div>
						<div class="con-title">项目期限</div>
					</div>
				</div>
				<div class="hd2">
					<!-- 还款中 -->
						<div class="hd2-htj-zs">
						<!-- 汇添金专属资产的戳  -->
							<div class="hd2-icon"></div>
						</div>
				</div>
				<div class="hd3">
					<div class="infor">
						<span class="icon-safe"></span> 安全保障计划
					</div>
					<div class="infor">
						项目历史回报：
						<span class="highlight">
							¥<fmt:formatNumber value="${projectDeatil.borrowInterest}" pattern="#,##0.00" />
						</span>元
					</div>
					<div class="infor">还款方式：${projectDeatil.repayStyle}</div>
					<div class="infor">发标时间：${projectDeatil.sendTime}</div>
					<div class="infor infor-font">温馨提示：市场有风险，投资需谨慎</div>
					<div class="infor infor-font">建议投资者类型：稳健型及以上</div>
				</div>
			</div>

			<div class="new-detail-main">
				<ul class="new-detail-tab">
					<li panel="0" class="active">风控信息</li>
					<!-- 原 项目信息 -->
					<c:if test="${loginFlag eq 1}">
						<!-- 已登录 -->
						<!--   -->
						<c:if
							test="${projectDeatil.status eq 10 or projectDeatil.status eq 11 or projectDeatil.status eq 12 }">
							<c:if test="${projectDeatil.type ne 9}">
								<!-- 项目不是汇资产项目 -->
								<!-- <li panel="1">风控信息</li> -->
								<li panel="2">相关文件</li>
							</c:if>
							<c:if test="${projectDeatil.type eq 9}">
								<!-- 项目是汇资产项目 -->
								<!-- <li panel="1">处置预案</li> -->
								<li panel="2">相关文件</li>
							</c:if>
						</c:if>
					</c:if>
					<c:if test="${investFlag eq 1}">
						<!-- 用户投资了该项目  -->
						<c:if test="${projectDeatil.status eq 13}">
							<!-- 项目没有全部还款 -->
							<c:if test="${projectDeatil.type ne 9}">
								<!-- <li panel="1">风控信息</li> -->
								<li panel="2">相关文件</li>
							</c:if>
							<c:if test="${projectDeatil.type eq 9}">
								<!-- 汇资产 -->
								<!-- <li panel="1">处置预案</li> -->
								<li panel="2">相关文件</li>
							</c:if>
						</c:if>
					</c:if>
					<c:if test="${projectDeatil.type eq 8}">
						<!-- 汇消费 -->
						<li panel="3">债权信息</li>
					</c:if>
					<li panel="4">还款计划</li>
					<li panel="5">投资记录</li>
					<li panel="6">常见问题</li>

				</ul>
				<ul class="new-detail-tab-panel">
					<li panel="0" class="active">
					<!-- RTB -->
						<c:if test="${projectDeatil.type eq 13}">
						   <c:if test="${borrowInfo.borrowContents ne null and borrowInfo.borrowContents ne '' }">
								<dl class="new-detail-dl">
									<dt>项目介绍</dt>
									<dd>${borrowInfo.borrowContents}</dd>
								</dl>
							</c:if>
							 <c:if test="${!empty riskControl.controlMeasures and riskControl.controlMeasures ne '' and riskControl.controlMeasures ne null}">
								<dl class="new-detail-dl">
									<dt>风控措施</dt>
									<dd>${riskControl.controlMeasures}</dd>
								</dl>
							 </c:if>
							 <dl class="new-detail-dl">
										<dt>相关协议</dt>
										<dd><a href="#"onclick="openNew('${ctx}/user/regist/goDetail.do?type=rtb_contract')" class="highlight">《产品认购协议》</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<a href="#"onclick="openNew('${ctx}/user/regist/goDetail.do?type=rtb_instructions')" class="highlight">《产品说明书》</a></dd>
									</dl>	
						</c:if>
						<!-- 汇保贷、汇典贷、汇小贷 、--> 
						<c:if test="${projectDeatil.type eq 0 ||projectDeatil.type eq 1 ||projectDeatil.type eq 2}">     
							<c:if test="${projectDeatil.comOrPer eq 1}">
								<!--  comOrPer 项目是个人项目还是企业项目 1企业 2个人  -->
								<dl class="new-detail-dl">
									<dt>项目流程</dt>
									<dd>
										<ul>
											<img alt="项目流程图" src="${cdn}/img/00-01.png">
										</ul>
									</dd>
								</dl>
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<li>所在地区：${borrowInfo.borrowAddress}</li>
											<%-- <li>所属行业：${borrowInfo.borrowIndustry}</li> --%>
											<li>注册资本：<fmt:formatNumber
													value="${borrowInfo.regCaptial}" pattern="#,###" />元
											</li>
											<li>注册时间：${borrowInfo.registTime}</li>
													<c:if test="${ not empty projectDeatil.borrowLevel and projectDeatil.borrowLevel ne '' }">
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" class="levelimg" src="${cdn}/img/${projectDeatil.borrowLevel}.png"></li>
											</c:if>
										</ul>
									</dd>
								</dl>
								<dl class="new-detail-dl">
									<dt>项目介绍</dt>
									<dd>
										<ul>
											<li>${borrowInfo.borrowContents}</li>
										</ul>
									</dd>
								</dl>
								<dl class="new-detail-dl">
									<dt>风控措施</dt>
									<dd>
										<ul>
											<li>${riskControl.controlMeasures }</li>
										</ul>
									</dd>
								</dl>
							</c:if>
							<c:if test="${projectDeatil.comOrPer eq 2}">
								<!--  comOrPer 项目是个人项目还是企业项目 1企业 2个人  -->
								<dl class="new-detail-dl">
									<dt>项目流程</dt>
									<dd>
										<ul>
											<img alt="项目流程图" src="${cdn}/img/00-01.png">
										</ul>
									</dd>
								</dl>
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<li>性别：${borrowInfo.sex}</li>
											<li>年龄：${borrowInfo.age}</li>
											<li>婚姻状况：${borrowInfo.maritalStatus}</li>
											<li>工作城市：${borrowInfo.workingCity}</li>
													<c:if test="${ not empty projectDeatil.borrowLevel and projectDeatil.borrowLevel ne '' }">
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" class="levelimg" src="${cdn}/img/${projectDeatil.borrowLevel}.png"></li>
											</c:if>
										</ul>
									</dd>
								</dl>
								<c:if
									test="${borrowInfo.borrowContents ne null and borrowInfo.borrowContents ne '' }">
									<dl class="new-detail-dl">
										<dt>项目介绍</dt>
										<dd>${borrowInfo.borrowContents}</dd>
									</dl>
								</c:if>
								<%-- <c:if test="${borrowInfo.accountContents ne null and borrowInfo.accountContents ne '' }">
								<dl class="new-detail-dl">
									<dt>财务状况</dt>
									<dd>${borrowInfo.accountContents}</dd>
								</dl> 
								</c:if> --%>
								<dl class="new-detail-dl">
									<dt>风控措施</dt>
									<dd>
										<ul>
											<li>${riskControl.controlMeasures }</li>
										</ul>
									</dd>
								</dl>
							</c:if>
						</c:if> 
						
						<!-- 汇车贷、  实鑫车  --> 
						<c:if test="${projectDeatil.type eq 3 || projectDeatil.type eq 12}">
							<c:if test="${projectDeatil.comOrPer eq 1}">
								<!--  comOrPer 项目是个人项目还是企业项目 1企业 2个人  -->
								<dl class="new-detail-dl">
									<dt>项目流程</dt>
									<dd>
										<ul>
											<img alt="项目流程图" src="${cdn}/img/00-01.png">
										</ul>
									</dd>
								</dl>
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<li>所在地区：${borrowInfo.borrowAddress}</li>
											<%-- <li>所属行业：${borrowInfo.borrowIndustry}</li> --%>
											<li>注册资本：<fmt:formatNumber
													value="${borrowInfo.regCaptial}" pattern="#,###" />元
											</li>
											<li>注册时间：${borrowInfo.registTime}</li>
													<c:if test="${ not empty projectDeatil.borrowLevel and projectDeatil.borrowLevel ne '' }">
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" class="levelimg" src="${cdn}/img/${projectDeatil.borrowLevel}.png"></li>
											</c:if>
										</ul>
									</dd>
								</dl>
								<dl class="new-detail-dl">
									<dt>项目介绍</dt>
									<dd>
										<ul>
											<li>${borrowInfo.borrowContents}</li>
										</ul>
									</dd>
								</dl>
								<dl class="new-detail-dl">
									<dt>风控措施</dt>
									<dd>
										<ul>
											<li>${riskControl.controlMeasures }</li>
										</ul>
									</dd>
								</dl>
							</c:if>
							<c:if test="${projectDeatil.comOrPer eq 2 and projectDeatil.type ne '9' and projectDeatil.type ne 9}">
								<!--  comOrPer 项目是个人项目还是企业项目 1企业 2个人  -->
								<dl class="new-detail-dl">
									<dt>项目流程</dt>
									<dd>
										<ul>
											<img alt="项目流程图" src="${cdn}/img/00-01.png">
										</ul>
									</dd>
								</dl>
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<li>性别：${borrowInfo.sex}</li>
											<li>年龄：${borrowInfo.age}</li>
											<li>婚姻状况：${borrowInfo.maritalStatus}</li>
											<li>工作城市：${borrowInfo.workingCity}</li>
											<li>岗位职业：${borrowInfo.position}</li>
													<c:if test="${ not empty projectDeatil.borrowLevel and projectDeatil.borrowLevel ne '' }">
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" class="levelimg" src="${cdn}/img/${projectDeatil.borrowLevel}.png"></li>
											</c:if>
										</ul>
									</dd>
								</dl>
								<c:if
									test="${not empty vehiclePledgeList and vehiclePledgeList ne null}">
									<dl class="new-detail-dl">
										<dt>车辆信息</dt>
										<dd>
											<table class="new-detail-tb">
												<thead>
													<tr>
														<th width="270">车辆品牌</th>
														<th width="270">品牌型号</th>
														<th>产地</th>
														<th>评估价值</th>
													</tr>
												</thead>
												<tbody>
													<c:forEach items="${vehiclePledgeList}" var="vehiclePledge">
														<tr>
															<td>${vehiclePledge.vehicleBrand}</td>
															<td>${vehiclePledge.vehicleModel}</td>
															<td>${vehiclePledge.place}</td>
															<td><span class="highlight"> ￥<fmt:formatNumber
																		value="${vehiclePledge.evaluationPrice}"
																		pattern="#,###.00" /></span></td>
														</tr>
													</c:forEach>
												</tbody>
											</table>
										</dd>
									</dl>
								</c:if>
								<c:if
									test="${borrowInfo.borrowContents ne null and borrowInfo.borrowContents ne '' }">
									<dl class="new-detail-dl">
										<dt>项目介绍</dt>
										<dd>${borrowInfo.borrowContents}</dd>
									</dl>
								</c:if>
								<dl class="new-detail-dl">
									<dt>风控措施</dt>
									<dd>
										<ul>
											<li>${riskControl.controlMeasures }</li>
										</ul>
									</dd>
								</dl>
							</c:if>
						</c:if> 
						
						<!-- 新手标 --> 
						<c:if test="${projectDeatil.type eq 4}">
							<c:if test="${projectDeatil.comOrPer eq 1}">
								<dl class="new-detail-dl">
									<dt>项目流程</dt>
									<dd>
										<ul>
											<img alt="项目流程图" src="${cdn}/img/00-01.png">
										</ul>
									</dd>
								</dl>
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<li>所在地区：${borrowInfo.borrowAddress}</li>
											<%-- <li>所属行业：${borrowInfo.borrowIndustry}</li> --%>
											<li>注册资本：<fmt:formatNumber
													value="${borrowInfo.regCaptial}" pattern="#,###" />元
											</li>
											<li>注册时间：${borrowInfo.registTime}</li>
													<c:if test="${ not empty projectDeatil.borrowLevel and projectDeatil.borrowLevel ne '' }">
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" class="levelimg" src="${cdn}/img/${projectDeatil.borrowLevel}.png"></li>
											</c:if>
										</ul>
									</dd>
								</dl>
							</c:if>
							<c:if test="${projectDeatil.comOrPer eq 2}">
								<dl class="new-detail-dl">
									<dt>项目流程</dt>
									<dd>
										<ul>
											<img alt="项目流程图" src="${cdn}/img/00-01.png">
										</ul>
									</dd>
								</dl>
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<li>性别：${borrowInfo.sex}</li>
											<li>年龄：${borrowInfo.age}</li>
											<li>婚姻状况：${borrowInfo.maritalStatus}</li>
											<li>工作城市：${borrowInfo.workingCity}</li>
													<c:if test="${ not empty projectDeatil.borrowLevel and projectDeatil.borrowLevel ne '' }">
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" class="levelimg" src="${cdn}/img/${projectDeatil.borrowLevel}.png"></li>
											</c:if>
										</ul>
									</dd>
								</dl>
							</c:if>
							<c:if
								test="${borrowInfo.borrowContents ne null and borrowInfo.borrowContents ne '' }">
								<dl class="new-detail-dl">
									<dt>项目介绍</dt>
									<dd>${borrowInfo.borrowContents}</dd>
								</dl>
							</c:if>
							<%-- <c:if test="${borrowInfo.accountContents ne null and borrowInfo.accountContents ne '' }">
							<dl class="new-detail-dl">
								<dt>财务状况</dt>
								<dd>${borrowInfo.accountContents}</dd>
							</dl>
							</c:if> --%>
							<c:if
								test="${not empty vehiclePledgeList and vehiclePledgeList ne null}">
								<dl class="new-detail-dl">
									<dt>车辆信息</dt>
									<dd>
										<table class="new-detail-tb">
											<thead>
												<tr>
													<th width="270">车辆品牌</th>
													<th width="270">型号</th>
													<th>评估价</th>
												</tr>
											</thead>
											<tbody>
												<c:forEach items="${vehiclePledgeList}" var="vehiclePledge">
													<tr>
														<td>${vehiclePledge.vehicleBrand}</td>
														<td>${vehiclePledge.vehicleModel}</td>
														<td><span class="highlight"> ￥<fmt:formatNumber
																	value="${vehiclePledge.evaluationPrice}"
																	pattern="#,###.00" /></span></td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</dd>
								</dl>
							</c:if>

						<%-- 	<c:if
								test="${!empty riskControl.agencyIntroduction and riskControl.agencyIntroduction ne '' and riskControl.agencyIntroduction ne null}">
								<dl class="new-detail-dl">
									<dt>合作机构</dt>
									<dd>${riskControl.agencyIntroduction}</dd>
								</dl>
							</c:if> --%>
							<c:if test="${!empty riskControl.controlMort}">
								<dl class="new-detail-dl">
									<dt>抵/质押物信息</dt>
									<dd>${riskControl.controlMort}</dd>
								</dl>
							</c:if>

							<c:if
								test="${!empty riskControl.controlMeasures and riskControl.controlMeasures ne '' and riskControl.controlMeasures ne null}">
								<dl class="new-detail-dl">
									<dt>风控措施</dt>
									<dd>${riskControl.controlMeasures}</dd>
								</dl>
							</c:if>


						</c:if> 
						
						<!-- 尊享汇 --> 
						<c:if test="${projectDeatil.type eq 11}">
							<c:if test="${projectDeatil.comOrPer eq 1}">
								<dl class="new-detail-dl">
									<dt>项目流程</dt>
									<dd>
										<ul>
											<img alt="项目流程图" src="${cdn}/img/00-01.png">
										</ul>
									</dd>
								</dl>
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<li>所在地区：${borrowInfo.borrowAddress}</li>
											<li>所属行业：${borrowInfo.borrowIndustry}</li>
											<li>注册资本：<fmt:formatNumber
													value="${borrowInfo.regCaptial}" pattern="#,###" />元
											</li>
											<li>注册时间：${borrowInfo.registTime}</li>
													<c:if test="${ not empty projectDeatil.borrowLevel and projectDeatil.borrowLevel ne '' }">
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" class="levelimg" src="${cdn}/img/${projectDeatil.borrowLevel}.png"></li>
											</c:if>
										</ul>
									</dd>
								</dl>
							</c:if>
							<c:if test="${projectDeatil.comOrPer eq 2}">
								<dl class="new-detail-dl">
									<dt>项目流程</dt>
									<dd>
										<ul>
											<img alt="项目流程图" src="${cdn}/img/00-01.png">
										</ul>
									</dd>
								</dl>
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<li>性别：${borrowInfo.sex}</li>
											<li>年龄：${borrowInfo.age}</li>
											<li>婚姻状况：${borrowInfo.maritalStatus}</li>
											<li>工作城市：${borrowInfo.workingCity}</li>
													<c:if test="${ not empty projectDeatil.borrowLevel and projectDeatil.borrowLevel ne '' }">
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" class="levelimg" src="${cdn}/img/${projectDeatil.borrowLevel}.png"></li>
											</c:if>
										</ul>
									</dd>
								</dl>
							</c:if>
							<c:if
								test="${borrowInfo.borrowContents ne null and borrowInfo.borrowContents ne '' }">
								<dl class="new-detail-dl">
									<dt>项目介绍</dt>
									<dd>${borrowInfo.borrowContents}</dd>
								</dl>
							</c:if>
							<%-- <c:if test="${borrowInfo.accountContents ne null and borrowInfo.accountContents ne '' }">
							<dl class="new-detail-dl">
								<dt>财务状况</dt>
								<dd>${borrowInfo.accountContents}</dd>
							</dl>
							</c:if> --%>
							<c:if
								test="${not empty vehiclePledgeList and vehiclePledgeList ne null}">
								<dl class="new-detail-dl">
									<dt>车辆信息</dt>
									<dd>
										<table class="new-detail-tb">
											<thead>
												<tr>
													<th width="270">车辆品牌</th>
													<th width="270">型号</th>
													<th>评估价</th>
												</tr>
											</thead>
											<tbody>
												<c:forEach items="${vehiclePledgeList}" var="vehiclePledge">
													<tr>
														<td>${vehiclePledge.vehicleBrand}</td>
														<td>${vehiclePledge.vehicleModel}</td>
														<td><span class="highlight"> ￥<fmt:formatNumber
																	value="${vehiclePledge.evaluationPrice}"
																	pattern="#,###.00" /></span></td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</dd>
								</dl>
							</c:if>
							<!-- 风控措施 -->
							<%-- <c:if
								test="${!empty riskControl.agencyIntroduction and riskControl.agencyIntroduction ne '' and riskControl.agencyIntroduction ne null}">
								<dl class="new-detail-dl">
									<dt>合作机构</dt>
									<dd>${riskControl.agencyIntroduction}</dd>
								</dl>
							</c:if> --%>
							<c:if test="${!empty riskControl.controlMort}">
								<dl class="new-detail-dl">
									<dt>抵/质押物信息</dt>
									<dd>${riskControl.controlMort}</dd>
								</dl>
							</c:if>

							<c:if
								test="${!empty riskControl.controlMeasures and riskControl.controlMeasures ne '' and riskControl.controlMeasures ne null}">
								<dl class="new-detail-dl">
									<dt>风控措施</dt>
									<dd>${riskControl.controlMeasures}</dd>
								</dl>
							</c:if>
						</c:if> 
						<!-- 汇租赁 --> 
						<c:if test="${projectDeatil.type eq 5}">
							<c:if test="${projectDeatil.comOrPer eq 1}">
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<li>所在地区：${borrowInfo.borrowAddress}</li>
											<li>所属行业：${borrowInfo.borrowIndustry}</li>
											<li>注册资本：<fmt:formatNumber
													value="${borrowInfo.regCaptial}" pattern="#,###" />元
											</li>
											<li>注册时间：${borrowInfo.registTime}</li>
													<c:if test="${ not empty projectDeatil.borrowLevel and projectDeatil.borrowLevel ne '' }">
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" class="levelimg" src="${cdn}/img/${projectDeatil.borrowLevel}.png"></li>
											</c:if>
										</ul>
									</dd>
								</dl>
							</c:if>
							<c:if test="${projectDeatil.comOrPer eq 2}">
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<li>性别：${borrowInfo.sex}</li>
											<li>年龄：${borrowInfo.age}</li>
											<li>婚姻状况：${borrowInfo.maritalStatus}</li>
											<li>工作城市：${borrowInfo.workingCity}</li>
													<c:if test="${ not empty projectDeatil.borrowLevel and projectDeatil.borrowLevel ne '' }">
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" class="levelimg" src="${cdn}/img/${projectDeatil.borrowLevel}.png"></li>
											</c:if>
										</ul>
									</dd>
								</dl>
							</c:if>
							<c:if
								test="${borrowInfo.borrowContents ne null and borrowInfo.borrowContents ne '' }">
								<dl class="new-detail-dl">
									<dt>项目介绍</dt>
									<dd>${borrowInfo.borrowContents}</dd>
								</dl>
							</c:if>
							<c:if
								test="${borrowInfo.accountContents ne null and borrowInfo.accountContents ne '' }">
								<dl class="new-detail-dl">
									<dt>财务状况</dt>
									<dd>${borrowInfo.accountContents}</dd>
								</dl>
							</c:if>
						</c:if> 
						
						<!-- 供应贷 --> 
						<c:if test="${projectDeatil.type eq 6}">
							<c:if test="${projectDeatil.comOrPer eq 1}">
								<!--  comOrPer 项目是个人项目还是企业项目 1企业 2个人  -->
								<dl class="new-detail-dl">
									<dt>项目流程</dt>
									<dd>
										<ul>
											<img alt="项目流程图" src="${cdn}/img/00-02.png">
										</ul>
									</dd>
								</dl>
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<%-- <li>项目名称：${borrowInfo.borrowName}</li> --%>
											<li>所在地区：${borrowInfo.borrowAddress}</li>
											<%-- <li>所属行业：${borrowInfo.borrowIndustry}</li> --%>
											<li>注册资本：<fmt:formatNumber
													value="${borrowInfo.regCaptial}" pattern="#,###" />元
											</li>
											<li>注册时间：${borrowInfo.registTime}</li>
													<c:if test="${ not empty projectDeatil.borrowLevel and projectDeatil.borrowLevel ne '' }">
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" class="levelimg" src="${cdn}/img/${projectDeatil.borrowLevel}.png"></li>
											</c:if>
										</ul>
									</dd>
								</dl>
								<dl class="new-detail-dl">
									<dt>项目介绍</dt>
									<dd>
										<ul>
											<li>${borrowInfo.borrowContents}</li>
										</ul>
									</dd>
								</dl>
								<dl class="new-detail-dl">
									<dt>风控措施</dt>
									<dd>
										<ul>
											<li>${riskControl.controlMeasures }</li>
										</ul>
									</dd>
								</dl>
							</c:if>
							<c:if test="${projectDeatil.comOrPer eq 2}">
								<!--  comOrPer 项目是个人项目还是企业项目 1企业 2个人  -->
								<dl class="new-detail-dl">
									<dt>项目流程</dt>
									<dd>
										<ul>
											<img alt="项目流程图" src="${cdn}/img/00-01.png">
										</ul>
									</dd>
								</dl>
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<li>性别：${borrowInfo.sex}</li>
											<li>年龄：${borrowInfo.age}</li>
											<li>婚姻状况：${borrowInfo.maritalStatus}</li>
											<li>工作城市：${borrowInfo.workingCity}</li>
													<c:if test="${ not empty projectDeatil.borrowLevel and projectDeatil.borrowLevel ne '' }">
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" class="levelimg" src="${cdn}/img/${projectDeatil.borrowLevel}.png"></li>
											</c:if>
										</ul>
									</dd>
								</dl>
							</c:if>
							<%-- <c:if test="${borrowInfo.borrowContents ne null and borrowInfo.borrowContents ne '' }">
							<dl class="new-detail-dl">
								<dt>项目介绍</dt>
								<dd>${borrowInfo.borrowContents}</dd>
							</dl>
							</c:if>
							<c:if test="${borrowInfo.accountContents ne null and borrowInfo.accountContents ne '' }">
							<dl class="new-detail-dl">
								<dt>财务状况</dt>
								<dd>${borrowInfo.accountContents}</dd>
							</dl>
							</c:if> --%>
						</c:if> 
						
						<!-- 汇房贷 --> 
						<c:if test="${projectDeatil.type eq 7}">
							<c:if test="${projectDeatil.comOrPer eq 1}">
								<!--  comOrPer 项目是个人项目还是企业项目 1企业 2个人  -->
								<dl class="new-detail-dl">
									<dt>项目流程</dt>
									<dd>
										<ul>
											<img alt="项目流程图" src="${cdn}/img/00-01.png">
										</ul>
									</dd>
								</dl>
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<li>所在地区：${borrowInfo.borrowAddress}</li>
											<li>所属行业：${borrowInfo.borrowIndustry}</li>
											<li>注册资本：<fmt:formatNumber
													value="${borrowInfo.regCaptial}" pattern="#,###" />元
											</li>
											<li>注册时间：${borrowInfo.registTime}</li>
													<c:if test="${ not empty projectDeatil.borrowLevel and projectDeatil.borrowLevel ne '' }">
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" class="levelimg" src="${cdn}/img/${projectDeatil.borrowLevel}.png"></li>
											</c:if>
										</ul>
									</dd>
								</dl>
							</c:if>
							<c:if test="${projectDeatil.comOrPer eq 2 and projectDeatil.type ne '9' and projectDeatil.type ne 9}">
								<!--  comOrPer 项目是个人项目还是企业项目 1企业 2个人  -->
								<dl class="new-detail-dl">
									<dt>项目流程</dt>
									<dd>
										<ul>
											<img alt="项目流程图" src="${cdn}/img/00-01.png">
										</ul>
									</dd>
								</dl>
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<li>性别：${borrowInfo.sex}</li>
											<li>年龄：${borrowInfo.age}</li>
											<li>婚姻状况：${borrowInfo.maritalStatus}</li>
											<li>工作城市：${borrowInfo.workingCity}</li>
											<li>岗位职业：${borrowInfo.position}</li>
													<c:if test="${ not empty projectDeatil.borrowLevel and projectDeatil.borrowLevel ne '' }">
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" class="levelimg" src="${cdn}/img/${projectDeatil.borrowLevel}.png"></li>
											</c:if>
										</ul>
									</dd>
								</dl>
							</c:if>
							<c:if
								test="${borrowInfo.borrowContents ne null and borrowInfo.borrowContents ne '' }">
								<dl class="new-detail-dl">
									<dt>项目介绍</dt>
									<dd>${borrowInfo.borrowContents}</dd>
								</dl>
							</c:if>
							<dl class="new-detail-dl">
								<dt>风控措施</dt>
								<dd>
									<ul>
										<li>${riskControl.controlMeasures }</li>
									</ul>
								</dd>
							</dl>
						</c:if> 
						
						<!-- 汇消费 --> 
						<c:if test="${projectDeatil.type eq 8}">
							<dl class="new-detail-dl">
								<dt>基础信息</dt>
								<dd>
									<ul>
										<li>所在地区：${borrowInfo.borrowAddress}</li>
										<li>所属行业：${borrowInfo.borrowIndustry}</li>
										<li>注册资本：<fmt:formatNumber
												value="${borrowInfo.regCaptial}" pattern="#,###" />元
										</li>
										<li>注册时间：${borrowInfo.registTime}</li>
												<c:if test="${ not empty projectDeatil.borrowLevel and projectDeatil.borrowLevel ne '' }">
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" class="levelimg" src="${cdn}/img/${projectDeatil.borrowLevel}.png"></li>
											</c:if>
									</ul>
								</dd>
							</dl>
							<c:if
								test="${borrowInfo.borrowContents ne null and borrowInfo.borrowContents ne '' }">
								<dl class="new-detail-dl">
									<dt>项目介绍</dt>
									<dd>${borrowInfo.borrowContents}</dd>
								</dl>
							</c:if>
							<c:if
								test="${borrowInfo.accountContents ne null and borrowInfo.accountContents ne '' }">
								<dl class="new-detail-dl">
									<dt>财务状况</dt>
									<dd>${borrowInfo.accountContents}</dd>
								</dl>
							</c:if>
						</c:if> 
						
						<!-- 汇资产 --> 
						<c:if test="${projectDeatil.type eq 9}">
							<dl class="new-detail-dl">
								<dt>项目流程</dt>
								<dd>
									<ul>
										<img alt="项目流程图" src="${cdn}/img/00-01.png">
									</ul>
								</dd>
							</dl>
							<dl class="new-detail-dl">
								<dt>基础信息</dt>
								<dd>
									<ul>
										<li>项目名称：${borrowInfo.borrowName}</li>
										<li>项目类型：${borrowInfo.borrowType}</li>
										<li>所在地区：${borrowInfo.borrowAddress}</li>
										<li>评估价值：${borrowInfo.guarantyValue}</li>
										<%-- <li>权属类别：${borrowInfo.ownershipCategory}</li> --%>
										<li>资产成因：${borrowInfo.assetOrigin}</li>
												<c:if test="${ not empty projectDeatil.borrowLevel and projectDeatil.borrowLevel ne '' }">
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" class="levelimg" src="${cdn}/img/${projectDeatil.borrowLevel}.png"></li>
											</c:if>
									</ul>
								</dd>
							</dl>
							<dl class="new-detail-dl">
								<dt>资产信息</dt>
								<dd>${borrowInfo.attachmentInfo}</dd>
							</dl>
							<dl class="new-detail-dl">
								<dt>处置结果预案</dt>
								<dd>${disposalPlan.disposalPlan}</dd>
							</dl>
						</c:if> 
						
						<!-- 汇投资 --> 
						<c:if test="${projectDeatil.type eq 10}">
							<c:if test="${projectDeatil.comOrPer eq 1}">
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<li>所在地区：${borrowInfo.borrowAddress}</li>
											<li>所属行业：${borrowInfo.borrowIndustry}</li>
											<li>注册资本：<fmt:formatNumber
													value="${borrowInfo.regCaptial}" pattern="#,###" />元
											</li>
											<li>注册时间：${borrowInfo.registTime}</li>
													<c:if test="${ not empty projectDeatil.borrowLevel and projectDeatil.borrowLevel ne '' }">
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" class="levelimg" src="${cdn}/img/${projectDeatil.borrowLevel}.png"></li>
											</c:if>
										</ul>
									</dd>
								</dl>
							</c:if>
							<c:if test="${projectDeatil.comOrPer eq 2}">
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<li>性别：${borrowInfo.sex}</li>
											<li>年龄：${borrowInfo.age}</li>
											<li>婚姻状况：${borrowInfo.maritalStatus}</li>
											<li>工作城市：${borrowInfo.workingCity}</li>
													<c:if test="${ not empty projectDeatil.borrowLevel and projectDeatil.borrowLevel ne '' }">
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" class="levelimg" src="${cdn}/img/${projectDeatil.borrowLevel}.png"></li>
											</c:if>
										</ul>
									</dd>
								</dl>
							</c:if>
							<c:if
								test="${borrowInfo.borrowContents ne null and borrowInfo.borrowContents ne '' }">
								<dl class="new-detail-dl">
									<dt>项目介绍</dt>
									<dd>${borrowInfo.borrowContents}</dd>
								</dl>
							</c:if>
						</c:if>
					</li>
					<c:if
						test="${projectDeatil.status eq 10 or projectDeatil.status eq 11 or projectDeatil.status eq 12 }">
						<c:if test="${loginFlag eq 1}">
							<!-- 汇保贷 -->
							<c:if test="${projectDeatil.type eq 0}">
								<li panel="1">
									<dl class="new-detail-dl">
										<dt>合作机构</dt>
										<dd>${riskControl.agencyIntroduction}</dd>
									</dl> <c:if test="${!empty riskControl.controlMort}">
										<dl class="new-detail-dl">
											<dt>抵/质押物信息</dt>
											<dd>${riskControl.controlMort}</dd>
										</dl>
									</c:if>
									<dl class="new-detail-dl">
										<dt>风控措施</dt>
										<dd>${riskControl.controlMeasures}</dd>
									</dl>
								</li>
							</c:if>
							<!-- 汇典贷 -->
							<c:if test="${projectDeatil.type eq 1}">
								<li panel="1">
									<dl class="new-detail-dl">
										<dt>合作机构</dt>
										<dd>${riskControl.agencyIntroduction}</dd>
									</dl> <c:if test="${!empty riskControl.controlMort}">
										<dl class="new-detail-dl">
											<dt>抵/质押物信息</dt>
											<dd>${riskControl.controlMort}</dd>
										</dl>
									</c:if>
									<dl class="new-detail-dl">
										<dt>风控措施</dt>
										<dd>${riskControl.controlMeasures}</dd>
									</dl>
								</li>
							</c:if>
							
							<%-- <!-- 融通宝  -->
							<c:if test="${projectDeatil.type eq 13}">
								<li panel="1">
									<dl class="new-detail-dl">
										<dt>合作机构</dt>
										<dd>${riskControl.agencyIntroduction}</dd>
									</dl> <c:if test="${!empty riskControl.controlMort}">
										<dl class="new-detail-dl">
											<dt>抵/质押物信息</dt>
											<dd>${riskControl.controlMort}</dd>
										</dl>
									</c:if>
									<dl class="new-detail-dl">
										<dt>风控措施</dt>
										<dd>${riskControl.controlMeasures}</dd>
									</dl>
								</li>
							</c:if> --%>
							<!-- 汇小贷 -->
							<c:if test="${projectDeatil.type eq 2}">
								<li panel="1">
									<dl class="new-detail-dl">
										<dt>合作机构</dt>
										<dd>${riskControl.agencyIntroduction}</dd>
									</dl> <c:if test="${!empty riskControl.controlMort}">
										<dl class="new-detail-dl">
											<dt>抵/质押物信息</dt>
											<dd>${riskControl.controlMort}</dd>
										</dl>
									</c:if>
									<dl class="new-detail-dl">
										<dt>风控措施</dt>
										<dd>${riskControl.controlMeasures}</dd>
									</dl>
								</li>
							</c:if>
							<!-- 汇车贷、  实鑫车  --> 
							<c:if test="${projectDeatil.type eq 3 || projectDeatil.type eq 12}">
								<li panel="1"><c:if
										test="${!empty riskControl.agencyIntroduction and riskControl.agencyIntroduction ne '' and riskControl.agencyIntroduction ne null}">
										<dl class="new-detail-dl">
											<dt>合作机构</dt>
											<dd>${riskControl.agencyIntroduction}</dd>
										</dl>
									</c:if> <c:if
										test="${!empty riskControl.controlMeasures and riskControl.controlMeasures ne '' and riskControl.controlMeasures ne null}">
										<dl class="new-detail-dl">
											<dt>风控措施</dt>
											<dd>${riskControl.controlMeasures}</dd>
										</dl>
									</c:if></li>
							</c:if>

							<!-- 汇租赁 -->
							<c:if test="${projectDeatil.type eq 5}">
								<li panel="1"><c:if
										test="${!empty riskControl.controlMort}">
										<dl class="new-detail-dl">
											<dt>抵/质押物信息</dt>
											<dd>${riskControl.controlMort}</dd>
										</dl>
									</c:if></li>
							</c:if>
							<!-- 供应贷 -->
							<c:if test="${projectDeatil.type eq 6}">
								<li panel="1">
									<dl class="new-detail-dl">
										<dt>项目优势</dt>
										<dd>${riskControl.agencyIntroduction}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>项目操作流程</dt>
										<dd>${riskControl.operatingProcess}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>风控措施</dt>
										<dd>${riskControl.controlMeasures}</dd>
									</dl>
								</li>
							</c:if>
							<!-- 汇房贷 -->
							<c:if test="${projectDeatil.type eq 7}">
								<li panel="1">
									<dl class="new-detail-dl">
										<dt>合作机构</dt>
										<dd>${riskControl.agencyIntroduction}</dd>
									</dl> <c:if test="${!empty riskControl.controlMort}">
										<dl class="new-detail-dl">
											<dt>抵/质押物信息</dt>
											<dd>${riskControl.controlMort}</dd>
										</dl>
									</c:if>
									<dl class="new-detail-dl">
										<dt>风控措施</dt>
										<dd>${riskControl.controlMeasures}</dd>
									</dl>
								</li>
							</c:if>
							<!-- 汇消费 -->
							<c:if test="${projectDeatil.type eq 8}">
								<li panel="1">
									<dl class="new-detail-dl">
										<dt>合作机构</dt>
										<dd>${riskControl.agencyIntroduction}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>风控措施</dt>
										<dd>${riskControl.controlMeasures}</dd>
									</dl>
								</li>
							</c:if>
							<!-- 汇资产 -->
							<c:if test="${projectDeatil.type eq 9}">
								<li panel="1">
									<dl class="new-detail-dl">
										<dt>售价预估</dt>
										<dd>${disposalPlan.priceEstimate}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>处置周期</dt>
										<dd>${disposalPlan.disposalPeriod}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>处置渠道</dt>
										<dd>${disposalPlan.disposalChannel}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>处置结果预案</dt>
										<dd>${disposalPlan.disposalPlan}</dd>
									</dl>
								</li>
							</c:if>
							<!-- 汇投资 -->
							<c:if test="${projectDeatil.type eq 10}">
								<li panel="1">
									<dl class="new-detail-dl">
										<dt>合作机构</dt>
										<dd>${riskControl.agencyIntroduction}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>风控措施</dt>
										<dd>${riskControl.controlMeasures}</dd>
									</dl>
								</li>
							</c:if>
						</c:if>
					</c:if>

					<c:if test="${projectDeatil.status eq 13 }">
						<c:if test="${investFlag eq 1}">
							<!-- 汇保贷 -->
							<c:if test="${projectDeatil.type eq 0}">
								<li panel="1">
									<dl class="new-detail-dl">
										<dt>合作机构</dt>
										<dd>${riskControl.agencyIntroduction}</dd>
									</dl> <c:if test="${!empty riskControl.controlMort}">
										<dl class="new-detail-dl">
											<dt>抵/质押物信息</dt>
											<dd>${riskControl.controlMort}</dd>
										</dl>
									</c:if>
									<dl class="new-detail-dl">
										<dt>风控措施</dt>
										<dd>${riskControl.controlMeasures}</dd>
									</dl>
								</li>
							</c:if>
							<!-- 汇典贷 -->
							<c:if test="${projectDeatil.type eq 1}">
								<li panel="1">
									<dl class="new-detail-dl">
										<dt>合作机构</dt>
										<dd>${riskControl.agencyIntroduction}</dd>
									</dl> <c:if test="${!empty riskControl.controlMort}">
										<dl class="new-detail-dl">
											<dt>抵/质押物信息</dt>
											<dd>${riskControl.controlMort}</dd>
										</dl>
									</c:if>
									<dl class="new-detail-dl">
										<dt>风控措施</dt>
										<dd>${riskControl.controlMeasures}</dd>
									</dl>
								</li>
							</c:if>
							<%-- <!-- 融通宝 -->
							<c:if test="${projectDeatil.type eq 13}">
								<li panel="1">
									<dl class="new-detail-dl">
										<dt>合作机构</dt>
										<dd>${riskControl.agencyIntroduction}</dd>
									</dl> <c:if test="${!empty riskControl.controlMort}">
										<dl class="new-detail-dl">
											<dt>抵/质押物信息</dt>
											<dd>${riskControl.controlMort}</dd>
										</dl>
									</c:if>
									<dl class="new-detail-dl">
										<dt>风控措施</dt>
										<dd>${riskControl.controlMeasures}</dd>
									</dl>
								</li>
							</c:if> --%>
							
							<!-- 汇小贷 -->
							<c:if test="${projectDeatil.type eq 2}">
								<li panel="1">
									<dl class="new-detail-dl">
										<dt>合作机构</dt>
										<dd>${riskControl.agencyIntroduction}</dd>
									</dl> <c:if test="${!empty riskControl.controlMort}">
										<dl class="new-detail-dl">
											<dt>抵/质押物信息</dt>
											<dd>${riskControl.controlMort}</dd>
										</dl>
									</c:if>
									<dl class="new-detail-dl">
										<dt>风控措施</dt>
										<dd>${riskControl.controlMeasures}</dd>
									</dl>
								</li>
							</c:if>
							
							<!-- 汇车贷、  实鑫车  --> 
							<c:if test="${projectDeatil.type eq 3 || projectDeatil.type eq 12}">
								<li panel="1">
									<dl class="new-detail-dl">
										<dt>合作机构</dt>
										<dd>${riskControl.agencyIntroduction}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>风控措施</dt>
										<dd>${riskControl.controlMeasures}</dd>
									</dl>
								</li>
							</c:if>
							<!-- 新手标 -->
							<c:if test="${projectDeatil.type eq 4||projectDeatil.type eq 11}">
								<li panel="1">
									<dl class="new-detail-dl">
										<dt>合作机构</dt>
										<dd>${riskControl.agencyIntroduction}</dd>
									</dl> <c:if test="${!empty riskControl.controlMort}">
										<dl class="new-detail-dl">
											<dt>抵/质押物信息</dt>
											<dd>${riskControl.controlMort}</dd>
										</dl>
									</c:if>
									<dl class="new-detail-dl">
										<dt>风控措施</dt>
										<dd>${riskControl.controlMeasures}</dd>
									</dl>
								</li>
							</c:if>
							<!-- 汇租赁 -->
							<c:if test="${projectDeatil.type eq 5}">
								<li panel="1"><c:if
										test="${!empty riskControl.controlMort}">
										<dl class="new-detail-dl">
											<dt>抵/质押物信息</dt>
											<dd>${riskControl.controlMort}</dd>
										</dl>
									</c:if></li>
							</c:if>
							<!-- 供应贷 -->
							<c:if test="${projectDeatil.type eq 6}">
								<li panel="1">
									<dl class="new-detail-dl">
										<dt>项目优势</dt>
										<dd>${riskControl.agencyIntroduction}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>项目操作流程</dt>
										<dd>${riskControl.operatingProcess}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>风控措施</dt>
										<dd>${riskControl.controlMeasures}</dd>
									</dl>
								</li>
							</c:if>
							<!-- 汇房贷 -->
							<c:if test="${projectDeatil.type eq 7}">
								<li panel="1">
									<dl class="new-detail-dl">
										<dt>合作机构</dt>
										<dd>${riskControl.agencyIntroduction}</dd>
									</dl> <c:if test="${!empty riskControl.controlMort}">
										<dl class="new-detail-dl">
											<dt>抵/质押物信息</dt>
											<dd>${riskControl.controlMort}</dd>
										</dl>
									</c:if>
									<dl class="new-detail-dl">
										<dt>风控措施</dt>
										<dd>${riskControl.controlMeasures}</dd>
									</dl>
								</li>
							</c:if>
							<!-- 汇消费 -->
							<c:if test="${projectDeatil.type eq 8}">
								<li panel="1">
									<dl class="new-detail-dl">
										<dt>合作机构</dt>
										<dd>${riskControl.agencyIntroduction}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>风控措施</dt>
										<dd>${riskControl.controlMeasures}</dd>
									</dl>
								</li>
							</c:if>
							<!-- 汇资产 -->
							<c:if test="${projectDeatil.type eq 9}">
								<li panel="1">
									<dl class="new-detail-dl">
										<dt>售价预估</dt>
										<dd>${disposalPlan.priceEstimate}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>处置周期</dt>
										<dd>${disposalPlan.disposalPeriod}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>处置渠道</dt>
										<dd>${disposalPlan.disposalChannel}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>处置结果预案</dt>
										<dd>${disposalPlan.disposalPlan}</dd>
									</dl>
								</li>
							</c:if>
							<!-- 汇投资 -->
							<c:if test="${projectDeatil.type eq 10}">
								<li panel="1">
									<dl class="new-detail-dl">
										<dt>合作机构</dt>
										<dd>${riskControl.agencyIntroduction}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>风控措施</dt>
										<dd>${riskControl.controlMeasures}</dd>
									</dl>
								</li>
							</c:if>
						</c:if>
					</c:if>

					<c:if
						test="${projectDeatil.status eq 10 or projectDeatil.status eq 11 or projectDeatil.status eq 12 }">
						<c:if test="${loginFlag eq 1}">
							<li panel="2">
								<div class="new-detail-imgs">
									<div class="new-detail-img-t">相关资料</div>
									<div class="new-detail-img-c">
										<ul>
											<c:forEach items="${fileList}" var="file">
												<li><a href="${file.fileUrl}"
													data-caption="${file.fileName}">
														<div>
															<img src="${file.fileUrl}" alt="">
														</div> <span class="title">${file.fileName}</span>
												</a></li>
											</c:forEach>
										</ul>
									</div>
								</div>
							</li>
						</c:if>
					</c:if>
					<c:if test="${projectDeatil.status eq 13 }">
						<c:if test="${investFlag eq 1}">
							<li panel="2">
								<div class="new-detail-imgs">
									<div class="new-detail-img-t">相关资料</div>
									<div class="new-detail-img-c">
										<ul>
											<c:forEach items="${fileList}" var="file">
												<li><a href="${file.fileUrl}"
													data-caption="${file.fileName}">
														<div>
															<img src="${file.fileUrl}" alt="">
														</div> <span class="title">${file.fileName}</span>
												</a></li>
											</c:forEach>
										</ul>
									</div>
								</div>
							</li>
						</c:if>
					</c:if>
					<c:if test="${projectDeatil.type eq 8}">
						<li panel="3">
							<table class="new-detail-tb">
								<thead id="projectConsumeListHead">
								</thead>
								<tbody id="projectConsumeList">
								</tbody>
							</table>
							<div class="clearfix"></div>
							<div class="new-pagination" id="consume-pagination"></div>
						</li>
					</c:if>
					<li panel="4">
						<table class="new-detail-tb">
							<thead>
								<tr>
									<th width="270">还款时间</th>
									<th width="270">还款期数</th>
									<th>类型</th>
									<th width="430">还款金额</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${repayPlanList}" var="repayPlan">
									<tr>
										<td>${repayPlan.repayTime}</td>
										<td>第${repayPlan.repayPeriod}期</td>
										<td>${repayPlan.repayType}</td>
										<td><span class="highlight">￥<fmt:formatNumber
													value="${repayPlan.repayTotal}" pattern="#,###.00" /></span></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</li>
					<li panel="5">
							<div class="new-detail-tb-t"> 
							<span>总投标金额 <strong id="investTotal">90,127.00</strong> 元 </span>
							<span>加入人次 <strong id="investTimes">10</strong> 人次</span>
						</div>
						<table class="new-detail-tb">
							<thead id="projectInvestListHead">
							</thead>
							<tbody id="projectInvestList">
							</tbody>
						</table>
						<div class="clearfix"></div>
						<div class="new-pagination" id="invest-pagination"></div>
					
					</li>
					<li panel="6">
						<div class="jobs-list">
							<dl>
								<dt class="iconfont iconfont-gotop" data-id="">
									<span class="title">1、我可以投资吗？</span>
								</dt>
								<dd>
									<p>持有本人中华人民共和国居民身份证的公民，且年满十八周岁，都可在汇盈金服网站上进行注册、完成实名认证，成为投资人。</p>
								</dd>
							</dl>

							<dl>
								<dt class="iconfont iconfont-gotop" data-id="">
									<span class="title">2、怎样进行投资？</span>
								</dt>
								<dd>
									<p>请您按照以下步骤进行投资：</p>
									<p>1. 在汇盈金服网站或手机客户端上进行注册、通过实名认证、成功绑定银行卡；</p>
									<p>2. 账户充值；</p>
									<p>3. 浏览平台融资项目，根据个人风险偏好自主选择项目投资；</p>
									<p>4. 确认投资，投资成功。</p>
								</dd>
							</dl>

							<dl>
								<dt class="iconfont iconfont-gotop" data-id="">
									<span class="title">3、投资后是否可以提前退出？</span>
								</dt>
								<dd>
									<p>1. 平台产品暂不支持提前回款申请。</p>
									<p>2. 汇直投和尊享汇融资项目支持债权转让。</p>
								</dd>
							</dl>

							<dl>
								<dt class="iconfont iconfont-gotop" data-id="">
									<span class="title">4、为何投标后会显示资金冻结？</span>
								</dt>
								<dd>
									<p>对于所有投资项目，投资人可自主选择进行投资。在项目完成放款之前，投资金额将被冻结；在项目完成放款之后，投资金额将通过第三方资金托管机构（汇付天下）转给融资方；如果在限定时间内未满标，则根据情况将已融资金放款给融资方或原路返还投资人。</p>
								</dd>
							</dl>

							<dl>
								<dt class="iconfont iconfont-gotop" data-id="">
									<span class="title">5、在汇盈金服投资有哪些费用？</span>
								</dt>
								<dd>
									<p>在汇盈金服平台进行投资，平台本身不收取投资人任何费用，投资人在充值/提现时第三方资金托管机构（汇付天下）会收取相关手续费。</p>
									<p>**特别提示：快速提现的手续费的计算方式为1元/笔+提现金额的0.05%，只适用于提现日后的一天为工作日的情况，如果后一天是非工作日，提现的手续费计算方式为1元/笔+提现金额的0.05%
										x（1+非工作日的天数）。例：周五申请快速提现，手续费=1元/笔+提现金额的0.05% x（1+2）</p>
								</dd>
							</dl>
							<dl>
								<dt class="iconfont iconfont-gotop" data-id="">
									<span class="title">6. 投资人风险测评目的和规则是什么？</span>
								</dt>
								<dd>
									<p>为完善对投资者风险承受能力的尽职评估，实现对投资者的等级管理，保障投资者购买合适的产品，根据投资者风险测评的结果，将投资者风险承受能力由低到高分为保守型、稳健型、成长型、进取型四种类型。</p>
								</dd>
							</dl>
						</div>
					</li>
				</ul>
				<!-- panel="0" end -->
			</div>
			<div class="clearfix"></div>
		</div>
	</div>


	<div class="pop-overlayer"></div>
	<div class="pop-box pop-tips-hastitle coupon-pop" id="couponPop">
		<a class="pop-close" href="javascript:void(0);" onclick="popOutWin()"></a>
		<h4 class="pop-title">请选择优惠券</h4>
		<div class="pop-main">
			<div class="pop-txt">
				<div class="coupon-pop-box">
					<c:if test="${ifVip==0}">
						<p>
							开通会员获取更多优惠券 <a href="javascript:;" class="highlight"
								onclick="openVIP()">&gt;&gt;马上开通</a>
						</p>
					</c:if>
					<ul class="available"></ul>
					<ul class="unavailable"></ul>
				</div>
			</div>
			<div class="btns">
				<a href="javascript:;" class="confirm" onclick="popOutWin()">确定</a>
				<a href="javascript:;" class="cancel" onclick="popOutWin()">取消</a>
			</div>
			<div class="clearfix"></div>
		</div>
	</div>
	<div class="pop-box pop-tips-hastitle confirm-pop" id="confirmPop">
		<a class="pop-close" href="javascript:void(0);" onclick="popOutWin()"></a>
		<h4 class="pop-title" style="text-align: center;">投资确认</h4>
		<div class="pop-main">
			<div class="pop-txt">
				<table width="100%" cellpadding="0" cellspacing="0">
					<tr>
						<th colspan="3">标的信息</th>
					</tr>
					<tr class="tbody">
						<td>项目编号：${projectDeatil.borrowNid}</td>
						<td>历史年回报率：${projectDeatil.borrowApr}%</td>
						<td>项目期限：${projectDeatil.borrowPeriod}${projectDeatil.borrowPeriodType}</td>
					</tr>
					<tr>
						<th colspan="3">投资信息</th>
					</tr>
					<tr class="tbody">
						<td id="confirmValue"></td>
						<td id="confirmIncome"></td>
						<td></td>
					</tr>
					<tr>
						<th colspan="3">优惠券信息</th>
					</tr>
					<tr class="tbody">
						<td id="confirmCoupon"></td>
						<td id="confirmCouponArea"></td>
						<td id="confirmCouponIncome"></td>
					</tr>
				</table>
			</div>
			<div class="btns">
				<a href="javascript:;" class="confirm" onclick="popOutWin()"
					id="confirmedBtn">确认投资</a> <a href="javascript:;" class="cancel"
					onclick="popOutWin()">重新投资</a>
			</div>
			<div class="clearfix"></div>
		</div>
	</div>

	<c:if
		test="${projectDeatil.projectType eq 'HZT' and projectDeatil.type ne 4 and projectDeatil.type ne 11}">
		<script>
			/* setActById('subHZT'); */
			projectType = "${projectDeatil.projectType}";
		</script>
	</c:if>
	<c:if test="${projectDeatil.type eq 8}">
		<!-- <script>
	setActById('subHXF');
	</script> -->
	</c:if>



	<div class="pop-overlayer"></div>
		<script>setActById('subHTJ');</script>    
	<%@ include file="/footer.jsp"%>
	<script type="text/javascript" src="${cdn}/js/project/htjProjectDetail.js?version=${version}" ></script>
	<script type="text/javascript" src="${cdn}/js/jquery.validate.js?version=${version}" charset="utf-8"></script>
	<script type="text/javascript" src="${cdn}/js/messages_cn.js?version=${version}" charset="utf-8"></script>
	<script type="text/javascript" src="${cdn}/js/jquery.metadata.js?version=${version}" charset="utf-8"></script>
	<script type="text/javascript" src="${cdn}/js/baguetteBox.min.js?version=${version}" charset="utf-8"></script>
	<script type="text/javascript" src="${cdn}/js/jquery.cookie.min.js?version=${version}" charset="utf-8"></script>
<script>
	if(checkCookie1("vipbuypop")){
		$(".vip-buy-pop").hide();
	}else if("${ifVip}"==1){
		$(".vip-buy-pop").hide();
	}else{
		$(".vip-buy-pop").show();
		setCookieVip("vipbuypop", "vipbuypop", 3650);
		$("html").css("overflow","hidden")
	}
	function checkCookie1(cname1) {
		var cname = getCookie(cname1);
		if (cname != null && cname != "") {
			return true;
		} else {
			return false;
		}
	}
	
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
	})
	</script>
</body>
</html>