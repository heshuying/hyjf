<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page language="java" import="java.util.Date" %>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta charset="utf-8" />
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<link rel="stylesheet" type="text/css" href="${cdn}/css/baguetteBox.min.css" />
<%@ include file="/head.jsp"%>
</head>

<body>

	<div class="new-detail-con">
		<input id="type" name="type" type="hidden" value="${projectDeatil.type}" />
	    <input type="hidden" id="increase" value="${projectDeatil.increaseMoney}" title="递增金额" /> 
	    <input id="borrowNid" name="borrowNid" type="hidden" value="${projectDeatil.borrowNid}" />
	    <input id="isLast" type="hidden" value="${projectDeatil.tenderAccountMin ge InvestAccountInt}" />
		<div class="new-detail-inner">
			<h4>
				<span class="title">项目编号 ：${projectDeatil.type == 13 ? projectDeatil.borrowAssetNumber : projectDeatil.borrowNid} </span>
			    <span class="date">
				    <span>起投金额：<fmt:formatNumber value="${projectDeatil.tenderAccountMin}" pattern="#,###" />元起投 </span> 
					<span>递增投资额： ${projectDeatil.increaseMoney}元</span>
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
					<div class="con2"  <c:if test="${projectDeatil.borrowExtraYield gt 0.00}">style="width:230px;"</c:if>>
						<div class="num highlight">${projectDeatil.borrowApr}<span>%</span><c:if test="${projectDeatil.borrowExtraYield gt 0.00}"><span style="font-size:22px;"> + <div  class="highlight" style="display:inline;">${projectDeatil.borrowExtraYield}</div>%</span></c:if>
						</div>
						<div class="con-title">历史年回报率</div>
					</div>
					<div class="con3" <c:if test="${projectDeatil.borrowExtraYield gt 0.00}">style="width:145px;"</c:if>>
						<div class="num">${projectDeatil.borrowPeriod}
							<span>${projectDeatil.borrowPeriodType}</span>
						</div>
						<div class="con-title">项目期限</div>
					</div>
				</div>

				<div class="hd3">
					<div class="infor">
						<c:if test="${projectDeatil.type eq 13}">项目来源：${projectDeatil.borrowProjectSource}</c:if>
						<c:if test="${projectDeatil.type ne 13}"><span class="icon-safe"></span> 安全保障计划</c:if>
					</div>
					<div class="infor">
						项目历史回报： <span class="highlight"> ¥<fmt:formatNumber value="${projectDeatil.borrowInterest}" pattern="#,##0.00" /></span>元
					</div>
					<div class="infor">还款方式：${projectDeatil.repayStyle}</div>
					<div class="infor">发标时间：${projectDeatil.sendTime}</div>
					<div class="infor infor-font">温馨提示：市场有风险，投资需谨慎</div>
					<c:if
						test="${projectDeatil.type eq 11 and (projectDeatil.bookingStatus eq 0 or projectDeatil.bookingStatus eq 1 or projectDeatil.bookingStatus eq 2 ) and ( projectDeatil.bookingBeginTime ne 0 and projectDeatil.bookingEndTime ne 0)}">
						<div class="infor" id="time"></div>
					</c:if>
					<div class="infor infor-font">建议投资者类型：稳健型及以上</div>
				</div>
			</div>

			<div class="new-detail-main">
				<ul class="new-detail-tab">
					<!-- 融通宝展示不同 -->
					<li panel="0" class="active">
						<c:if test="${projectDeatil.type eq 13}">产品详情</c:if>
						<c:if test="${projectDeatil.type ne 13}">风控信息</c:if>
					</li>
					<!-- 融通宝展示不同 -->
					<c:if test="${projectDeatil.type ne 13}">
						<li panel="2">相关文件</li>
						<c:if test="${projectDeatil.type eq 8}">
							<!-- 汇消费 -->
							<li panel="3">债权信息</li>
						</c:if>
					</c:if>
					<li panel="6">常见问题</li>
				</ul>
				<ul class="new-detail-tab-panel">
					<li panel="0" class="active">
					<!-- 融通宝展示内容 -->
						<c:if test="${projectDeatil.type eq 13}">
							<dl class="new-detail-dl">
								<dt>项目流程</dt>
								<dd>
									<ul>
										<img alt="项目流程图" src="${cdn}/img/project/hjs_flow.png" />
									</ul>
								</dd>
							</dl>
							<dl class="new-detail-dl">
								<dd>
									<table class="new-detail-tb1" cellpadding="0" cellspacing="0" border="0">
	                           	      <tbody>
		                                <tr>
		                                    <td align="center" width="210">项目名称</td>
		                                    <td>${projectDeatil.borrowAssetNumber}</td>
		                                </tr>
		                            	<tr>
		                            		<td align="center">项目来源</td>
		                            		<td>${projectDeatil.borrowProjectSource}</td>
		                            	</tr>
		                            	<tr>
		                            		<td align="center">募集金额</td>
		                            		<td><fmt:formatNumber value="${projectDeatil.borrowAccount}" pattern="#,###" />元</td>
		                            	</tr>
		                            	<tr>
		                            		<td align="center">历史年回报率</td>
		                            		<td>${projectDeatil.borrowApr}% + ${projectDeatil.borrowExtraYield}%</td>
		                            	</tr>
		                            	<tr>
		                            		<td align="center">加入条件</td>
		                            		<td>
			                            		<fmt:formatNumber value="${projectDeatil.tenderAccountMin}" pattern="#,###" />元起投,
			                            		<fmt:formatNumber value="${projectDeatil.increaseMoney}" pattern="#,###" />元递增
		                            		</td>
		                            	</tr>
		                            	<tr>
		                            		<td align="center">投资限额</td>
		                            		<td><fmt:formatNumber value="${projectDeatil.tenderAccountMax}" pattern="#,###" />元</td>
		                            	</tr>
		                            	<tr>
		                            		<td align="center">发布时间</td>
		                            		<td>${projectDeatil.sendTime}</td>
		                            	</tr>
		                            	<tr>
		                            		<td align="center">起息日期</td>
		                            		<td>${projectDeatil.borrowInterestTime}</td>
		                            	</tr>
		                            	<tr>
		                            		<td align="center">到期日期 </td>
		                            		<td>${projectDeatil.borrowDueTime}</td>
		                            	</tr>
		                            	<tr>
		                            		<td align="center">保障方式</td>
		                            		<td>${projectDeatil.borrowSafeguardWay}</td>
		                            	</tr>
		                            	<tr>
		                            		<td align="center">收益说明</td>
		                            		<td>${projectDeatil.borrowIncomeDescription}</td>
		                            	</tr>
		                            	<tr>
		                            		<td align="center">合同协议</td>
											<td>
												<a href="#"onclick="openNew('${ctx}/user/regist/goDetail.do?type=rtb_contract')" class="highlight">《产品认购协议》</a>
												<a href="#"onclick="openNew('${ctx}/user/regist/goDetail.do?type=rtb_instructions')" class="highlight">《产品说明书》</a>
											</td>
										</tr>
	                          	  </tbody>
	                        	</table>
								</dd>
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
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" src="${cdn}/img/${projectDeatil.borrowLevel}.png"></li>
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
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" src="${cdn}/img/${projectDeatil.borrowLevel}.png"></li>
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
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" src="${cdn}/img/${projectDeatil.borrowLevel}.png"></li>
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
											<c:if test="${borrowInfo.position ne null and borrowInfo.position ne '' }">
												<li>岗位职业：${borrowInfo.position}</li>
											</c:if>
													<c:if test="${ not empty projectDeatil.borrowLevel and projectDeatil.borrowLevel ne '' }">
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" src="${cdn}/img/${projectDeatil.borrowLevel}.png"></li>
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
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" src="${cdn}/img/${projectDeatil.borrowLevel}.png"></li>
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
											<c:if test="${borrowInfo.position ne null and borrowInfo.position ne '' }">
												<li>岗位职业：${borrowInfo.position}</li>
											</c:if>
													<c:if test="${ not empty projectDeatil.borrowLevel and projectDeatil.borrowLevel ne '' }">
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" src="${cdn}/img/${projectDeatil.borrowLevel}.png"></li>
											</c:if>
										</ul>
									</dd>
								</dl>
							</c:if>
							<c:if test="${borrowInfo.borrowContents ne null and borrowInfo.borrowContents ne '' }">
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
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" src="${cdn}/img/${projectDeatil.borrowLevel}.png"></li>
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
											<c:if test="${borrowInfo.position ne null and borrowInfo.position ne '' }">
												<li>岗位职业：${borrowInfo.position}</li>
											</c:if>
													<c:if test="${ not empty projectDeatil.borrowLevel and projectDeatil.borrowLevel ne '' }">
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" src="${cdn}/img/${projectDeatil.borrowLevel}.png"></li>
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
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" src="${cdn}/img/${projectDeatil.borrowLevel}.png"></li>
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
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" src="${cdn}/img/${projectDeatil.borrowLevel}.png"></li>
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
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" src="${cdn}/img/${projectDeatil.borrowLevel}.png"></li>
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
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" src="${cdn}/img/${projectDeatil.borrowLevel}.png"></li>
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
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" src="${cdn}/img/${projectDeatil.borrowLevel}.png"></li>
											</c:if>
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
											<c:if test="${borrowInfo.position ne null and borrowInfo.position ne '' }">
												<li>岗位职业：${borrowInfo.position}</li>
											</c:if>
													<c:if test="${ not empty projectDeatil.borrowLevel and projectDeatil.borrowLevel ne '' }">
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" src="${cdn}/img/${projectDeatil.borrowLevel}.png"></li>
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
						
						<!-- 员工贷 --> 
						<c:if test="${projectDeatil.type eq 14}">
							<c:if test="${projectDeatil.comOrPer eq 1}">
								<!--  comOrPer 项目是个人项目还是企业项目 1企业 2个人  -->
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
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" src="${cdn}/img/${projectDeatil.borrowLevel}.png"></li>
											</c:if>
										</ul>
									</dd>
								</dl>
							</c:if>
							<c:if test="${projectDeatil.comOrPer eq 2}">
								<!--  comOrPer 项目是个人项目还是企业项目 1企业 2个人  -->
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<li>性别：${borrowInfo.sex}</li>
											<li>年龄：${borrowInfo.age}</li>
											<li>婚姻状况：${borrowInfo.maritalStatus}</li>
											<li>工作城市：${borrowInfo.workingCity}</li>
											<c:if test="${borrowInfo.position ne null and borrowInfo.position ne '' }">
												<li>岗位职业：${borrowInfo.position}</li>
											</c:if>
													<c:if test="${ not empty projectDeatil.borrowLevel and projectDeatil.borrowLevel ne '' }">
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" src="${cdn}/img/${projectDeatil.borrowLevel}.png"></li>
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
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" src="${cdn}/img/${projectDeatil.borrowLevel}.png"></li>
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
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" src="${cdn}/img/${projectDeatil.borrowLevel}.png"></li>
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
											<li>注册资本：<fmt:formatNumber value="${borrowInfo.regCaptial}" pattern="#,###" />元
											</li>
											<li>注册时间：${borrowInfo.registTime}</li>
													<c:if test="${ not empty projectDeatil.borrowLevel and projectDeatil.borrowLevel ne '' }">
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" src="${cdn}/img/${projectDeatil.borrowLevel}.png"></li>
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
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" src="${cdn}/img/${projectDeatil.borrowLevel}.png"></li>
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
							<!-- 汇保贷 -->
							<c:if test="${projectDeatil.type eq 0}">
								<li panel="1">
									<dl class="new-detail-dl">
										<dt>合作机构</dt>
										<dd>${riskControl.agencyIntroduction}</dd>
									</dl> <%-- <c:if test="${!empty riskControl.controlMort}">
										<dl class="new-detail-dl">
											<dt>抵/质押物信息</dt>
											<dd>${riskControl.controlMort}</dd>
										</dl>
									</c:if> --%> 
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
									</dl> <%-- <c:if test="${!empty riskControl.controlMort}">
										<dl class="new-detail-dl">
											<dt>抵/质押物信息</dt>
											<dd>${riskControl.controlMort}</dd>
										</dl>
									</c:if> --%>
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
									</dl> <%-- <c:if test="${!empty riskControl.controlMort}">
										<dl class="new-detail-dl">
											<dt>抵/质押物信息</dt>
											<dd>${riskControl.controlMort}</dd>
										</dl>
									</c:if> --%>
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
									</dl><%--  <c:if test="${!empty riskControl.controlMort}">
										<dl class="new-detail-dl">
											<dt>抵/质押物信息</dt>
											<dd>${riskControl.controlMort}</dd>
										</dl>
									</c:if> --%>
									<dl class="new-detail-dl">
										<dt>风控措施</dt>
										<dd>${riskControl.controlMeasures}</dd>
									</dl>
								</li>
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


							<li panel="2">
								<div class="new-detail-imgs">
									<div class="new-detail-img-t">相关资料</div>
									<div class="new-detail-img-c">
										<ul>
											<c:forEach items="${fileList}" var="file">
												<li><a href="${file.fileUrl}" data-caption="${file.fileName}">
														<div>
															<img src="${file.fileUrl}" alt="">
														</div> 
														<span class="title">${file.fileName}</span>
												</a></li>
											</c:forEach>
										</ul>
									</div>
								</div>
							</li>

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

					<li panel="6">
						<div class="jobs-list">
							<!-- 融通宝展示不同 -->
							<c:if test="${projectDeatil.type eq 13}">
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
										<span class="title">2、 什么是“汇金理财”？ </span>
									</dt>
									<dd>
										<p>“汇金理财”是与地方金融资产交易所合作推出的资产交易类投融资产品，资产持有人通过汇盈金服居间撮合，将资产或其收益权转让给投资人，投资人获取约定的投资收益。</p>
									</dd>
								</dl>
								<dl>
									<dt class="iconfont iconfont-gotop" data-id="">
										<span class="title">3、“汇金理财”的特点是什么？</span>
									</dt>
									<dd>
										<p>“汇金理财”发布的产品是在地方金融资产交易所挂牌的项目，项目均经过金交所严格的审核，并提供专业的风控措施，保证项目的优质性。投资人可根据自身投资偏好选择适合项目投资。</p>
									</dd>
								</dl>
								<dl>
									<dt class="iconfont iconfont-gotop" data-id="">
										<span class="title">4、我可以认购“汇金理财”产品吗？ </span>
									</dt>
									<dd>
										<p>“汇金理财”下每支产品的投资人数不能超过200人；投资者持有本人中华人民共和国居民身份证的公民，且年满十八周岁。</p>
									</dd>
								</dl>
								<dl>
									<dt class="iconfont iconfont-gotop" data-id="">
										<span class="title">5、认购“汇金理财”产品需要收费吗？ </span>
									</dt>
									<dd>
										<p>投资者暂时无需支付认购费、管理费。</p>
									</dd>
								</dl>
							</c:if>
						
						
						
							<c:if test="${projectDeatil.type ne 13}">
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
							</c:if>
						</div>
					</li>
				</ul>
				<!-- panel="0" end -->
			</div>
			<div class="clearfix"></div>
		</div>
	</div>


	<script src="${cdn}/js/jquery.min.js?version=${version}" type="text/javascript" charset="utf-8"></script>
	<script src="${cdn}/js/jquery.placeholder.min.js?version=${version}" type="text/javascript"></script>
	<script src="${cdn}/js/common/common.js?version=${version}" type="text/javascript"></script>
	<script src="${cdn}/js/home/footer.js?version=${version}" type="text/javascript"></script>

	<script type="text/javascript" src="${cdn}/js/jquery.validate.js?version=${version}" charset="utf-8"></script>
	<script type="text/javascript" src="${cdn}/js/messages_cn.js?version=${version}" charset="utf-8"></script>
	<script type="text/javascript" src="${cdn}/js/jquery.metadata.js?version=${version}" charset="utf-8"></script>
	<script type="text/javascript" src="${cdn}/js/baguetteBox.min.js?version=${version}" charset="utf-8"></script>
	<script type="text/javascript" src="${cdn}/js/jquery.cookie.min.js?version=${version}" charset="utf-8"></script>
	<script type="text/javascript" src="${cdn}/js/project/projectPreview.js?version=${version}" ></script>
	
	
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
		})
	</script>
</body>
</html>