<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta charset="utf-8" />
    <title>HZR${creditResult.data.creditTender.creditNid}项目详情 - 汇盈金服官网</title>
    
    <link rel="stylesheet" type="text/css" href="${cdn}/css/baguetteBox.min.css" />
    <jsp:include page="/head.jsp"></jsp:include>
</head>

<body>
<jsp:include page="/header.jsp"></jsp:include>
<!-- 投资页面引导会员投资弹层 -->
	<%-- <div class="vip-buy-pop hide">
		<div class="vip-buy-pop-back"></div>
		<div class="vip-buy-pop-div">
			<div class="vip-buy-close"></div>
			<div class="vip-buy-buy"><a href="${returl}"></a></div>
		</div>
	</div>
	<div class="vip-buy-header"><a href="${returl}"></a></div> --%>
    <div class="new-detail-con">
        <div class="new-detail-inner">
            <h4>
                <span class="title">债权转让项目 HZR${creditResult.data.creditTender.creditNid}</span>
                <span class="date">
                    <span>发布时间：${fn:substring(creditResult.data.creditTender.creditTime, 0, 10)}</span> 
	           </span>
            </h4>
            <div class="new-detail-hd">
                <div class="hd1 zhuanrang">
                    <div class="con1">
                        <div class="num"><fmt:formatNumber value="${creditResult.data.creditTender.creditCapital}" pattern="#,##0"/><span>元</span></div>
                        <div class="con-title">项目金额</div>
                    </div>
                    <div class="con2">
                        <div class="num highlight">${creditResult.data.creditTender.borrowApr}<span>%</span> </div>
                        <div class="con-title">历史年回报率</div>
                    </div>
                    <div class="con34">
                        <div class="num">${creditResult.data.creditTender.creditTerm}<span>天</span></div>
                        <div class="con-title">剩余期限</div>
                    </div>
                </div>
                <div class="hd2">
						<div class="hd2-htj-zs">
						<!-- 汇添金专属资产的戳  -->
							<div class="hd2-icon"></div>
						</div>
                </div>
                <div class="hd3 zhuanrang">
                    <div class="infor">原借款项目：<a href="${ctx}/project/getHtjProjectDetail.do?borrowNid=${creditResult.data.borrow.borrowNid}" class="highlight">${creditResult.data.borrow.borrowNid}</a></div>
                    <div class="infor">还款方式：
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
                    <div class="infor">发标时间：${creditResult.data.borrow.addtime}</div>
                    <div class="infor"><span id="time" data-time="${creditResult.data.creditTender.creditTimeInt}"></span> <span class="icon-info"><em>注：原债权人可随时终止转让 。</em></span></div>
                    <div class="infor infor-font">温馨提示：市场有风险，投资需谨慎</div>
                    <div class="infor infor-font">建议投资者类型：稳健型及以上</div>
                </div>
            </div>
            <div class="new-detail-main">
                
				<ul class="new-detail-tab">
					<li panel="0" class="active">项目信息</li>
<%-- 							<c:if test="${creditResult.data.creditTender.type ne 9}">
								<li panel="1">风控信息</li>
							</c:if>
							<c:if test="${creditResult.data.creditTender.type eq 9}">
								<li panel="1">处置预案</li>
							</c:if>
					<li panel="2">相关文件</li>
 --%>					<li panel="3">还款计划</li>
					<li panel="4">投资记录</li>
					
				</ul>
				<ul class="new-detail-tab-panel">
					<li panel="0" class="active">
						<dl class="new-detail-dl">
                            <dt>债转说明</dt>
                            <dd>此项目为“汇添金”债权转让服务。  <br />债权转让达成后，债权拥有者将变更为新投资人，担保公司将继续对借款人的借款承担连带担保责任。</dd>
                            <%-- <dd>${creditResult.data.borrow.name}</dd> --%>
                        </dl>
						<!-- 汇保贷 --> 
						<c:if test="${creditResult.data.creditTender.type eq 0}">
							<c:if test="${creditResult.data.creditTender.comOrPer eq 1}">
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<li>所在地区：${borrowInfo.borrowAddress}</li>
											<li>所属行业：${borrowInfo.borrowIndustry}</li>
											<li>注册资本：<fmt:formatNumber value="${borrowInfo.regCaptial}" pattern="#,###" />元</li>
											<li>注册时间：${borrowInfo.registTime}</li>
														<c:if test="${ not empty creditResult.data.borrow.borrowLevel and creditResult.data.borrow.borrowLevel ne '' }">
												<li>信用评级：<img alt="${creditResult.data.borrow.borrowLevel}" src="${cdn}/img/${creditResult.data.borrow.borrowLevel}.png"></li>
											</c:if>
										</ul>
									</dd>
								</dl>
							</c:if>
							<c:if test="${creditResult.data.creditTender.comOrPer eq 2}">
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<li>性别：${borrowInfo.sex}</li>
											<li>年龄：${borrowInfo.age}</li>
											<li>婚姻状况：${borrowInfo.maritalStatus}</li>
											<li>工作城市：${borrowInfo.workingCity}</li>
												<c:if test="${ not empty creditResult.data.borrow.borrowLevel and creditResult.data.borrow.borrowLevel ne '' }">
												<li>信用评级：<img alt="${creditResult.data.borrow.borrowLevel}" src="${cdn}/img/${creditResult.data.borrow.borrowLevel}.png"></li>
											</c:if>
										</ul>
									</dd>
								</dl>
							</c:if>
							<dl class="new-detail-dl">
								<dt>项目描述</dt>
								<dd>${borrowInfo.borrowContents}</dd>
							</dl>
							<dl class="new-detail-dl">
								<dt>财务状况</dt>
								<dd>${borrowInfo.accountContents}</dd>
							</dl>
						</c:if> 
						<!-- 汇典贷 --> 
						<c:if test="${creditResult.data.creditTender.type eq 1}">
							<c:if test="${creditResult.data.creditTender.comOrPer eq 1}">
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<li>所在地区：${borrowInfo.borrowAddress}</li>
											<li>所属行业：${borrowInfo.borrowIndustry}</li>
											<li>注册资本：<fmt:formatNumber value="${borrowInfo.regCaptial}" pattern="#,###" />元</li>
											<li>注册时间：${borrowInfo.registTime}</li>
												<c:if test="${ not empty creditResult.data.borrow.borrowLevel and creditResult.data.borrow.borrowLevel ne '' }">
												<li>信用评级：<img alt="${creditResult.data.borrow.borrowLevel}" src="${ctx}/img/${creditResult.data.borrow.borrowLevel}.png"></li>
											</c:if>
										</ul>
									</dd>
								</dl>
							</c:if>
							<c:if test="${creditResult.data.creditTender.comOrPer eq 2}">
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<li>性别：${borrowInfo.sex}</li>
											<li>年龄：${borrowInfo.age}</li>
											<li>婚姻状况：${borrowInfo.maritalStatus}</li>
											<li>工作城市：${borrowInfo.workingCity}</li>
												<c:if test="${ not empty creditResult.data.borrow.borrowLevel and creditResult.data.borrow.borrowLevel ne '' }">
												<li>信用评级：<img alt="${creditResult.data.borrow.borrowLevel}" src="${ctx}/img/${creditResult.data.borrow.borrowLevel}.png"></li>
											</c:if>
										</ul>
									</dd>
								</dl>
							</c:if>
							<dl class="new-detail-dl">
								<dt>项目描述</dt>
								<dd>${borrowInfo.borrowContents}</dd>
							</dl>
							<dl class="new-detail-dl">
								<dt>财务状况</dt>
								<dd>${borrowInfo.accountContents}</dd>
							</dl>
						</c:if> 
						<!-- 汇小贷 --> 
						<c:if test="${creditResult.data.creditTender.type eq 2}">
							<c:if test="${creditResult.data.creditTender.comOrPer eq 1}">
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<li>所在地区：${borrowInfo.borrowAddress}</li>
											<li>所属行业：${borrowInfo.borrowIndustry}</li>
											<li>注册资本：<fmt:formatNumber value="${borrowInfo.regCaptial}" pattern="#,###" />元</li>
											<li>注册时间：${borrowInfo.registTime}</li>
												<c:if test="${ not empty creditResult.data.borrow.borrowLevel and creditResult.data.borrow.borrowLevel ne '' }">
												<li>信用评级：<img alt="${creditResult.data.borrow.borrowLevel}" src="${ctx}/img/${creditResult.data.borrow.borrowLevel}.png"></li>
											</c:if>
										</ul>
									</dd>
								</dl>
							</c:if>
							<c:if test="${creditResult.data.creditTender.comOrPer eq 2}">
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<li>性别：${borrowInfo.sex}</li>
											<li>年龄：${borrowInfo.age}</li>
											<li>婚姻状况：${borrowInfo.maritalStatus}</li>
											<li>工作城市：${borrowInfo.workingCity}</li>
												<c:if test="${ not empty creditResult.data.borrow.borrowLevel and creditResult.data.borrow.borrowLevel ne '' }">
												<li>信用评级：<img alt="${creditResult.data.borrow.borrowLevel}" src="${ctx}/img/${creditResult.data.borrow.borrowLevel}.png"></li>
											</c:if>
										</ul>
									</dd>
								</dl>
							</c:if>
							<dl class="new-detail-dl">
								<dt>项目描述</dt>
								<dd>${borrowInfo.borrowContents}</dd>
							</dl>
							<dl class="new-detail-dl">
								<dt>财务状况</dt>
								<dd>${borrowInfo.accountContents}</dd>
							</dl>
						</c:if> 
						<!-- 汇车贷 --> 
						<c:if test="${creditResult.data.creditTender.type eq 3}">
							<c:if test="${creditResult.data.creditTender.comOrPer eq 1}">
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<li>所在地区：${borrowInfo.borrowAddress}</li>
											<li>所属行业：${borrowInfo.borrowIndustry}</li>
											<li>注册资本：<fmt:formatNumber value="${borrowInfo.regCaptial}" pattern="#,###" />元 </li>
											<li>注册时间：${borrowInfo.registTime}</li>
												<c:if test="${ not empty creditResult.data.borrow.borrowLevel and creditResult.data.borrow.borrowLevel ne '' }">
												<li>信用评级：<img alt="${creditResult.data.borrow.borrowLevel}" src="${ctx}/img/${creditResult.data.borrow.borrowLevel}.png"></li>
											</c:if>
										</ul>
									</dd>
								</dl>
							</c:if>
							<c:if test="${creditResult.data.creditTender.comOrPer eq 2}">
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<li>性别：${borrowInfo.sex}</li>
											<li>年龄：${borrowInfo.age}</li>
											<li>婚姻状况：${borrowInfo.maritalStatus}</li>
											<li>工作城市：${borrowInfo.workingCity}</li>
												<c:if test="${ not empty creditResult.data.borrow.borrowLevel and creditResult.data.borrow.borrowLevel ne '' }">
												<li>信用评级：<img alt="${creditResult.data.borrow.borrowLevel}" src="${ctx}/img/${creditResult.data.borrow.borrowLevel}.png"></li>
											</c:if>
										</ul>
									</dd>
								</dl>
							</c:if>
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
													<td>
														<span class="highlight"> ￥<fmt:formatNumber value="${vehiclePledge.evaluationPrice}" pattern="#,###.00" /></span>
													</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</dd>
							</dl>
							<dl class="new-detail-dl">
								<dt>项目描述</dt>
								<dd>${borrowInfo.borrowContents}</dd>
							</dl>
						</c:if> 
						<!-- 新手标 --> 
						<c:if test="${creditResult.data.creditTender.type eq 4}">
							<c:if test="${creditResult.data.creditTender.comOrPer eq 1}">
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<li>所在地区：${borrowInfo.borrowAddress}</li>
											<li>所属行业：${borrowInfo.borrowIndustry}</li>
											<li>注册资本：<fmt:formatNumber value="${borrowInfo.regCaptial}" pattern="#,###" />元</li>
											<li>注册时间：${borrowInfo.registTime}</li>
											<c:if test="${ not empty creditResult.data.borrow.borrowLevel and creditResult.data.borrow.borrowLevel ne '' }">
												<li>信用评级：<img alt="${creditResult.data.borrow.borrowLevel}" src="${ctx}/img/${creditResult.data.borrow.borrowLevel}.png"></li>
											</c:if>
										</ul>
									</dd>
								</dl>
							</c:if>
							<c:if test="${creditResult.data.creditTender.comOrPer eq 2}">
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<li>性别：${borrowInfo.sex}</li>
											<li>年龄：${borrowInfo.age}</li>
											<li>婚姻状况：${borrowInfo.maritalStatus}</li>
											<li>工作城市：${borrowInfo.workingCity}</li>
											<c:if test="${ not empty creditResult.data.borrow.borrowLevel and creditResult.data.borrow.borrowLevel ne '' }">
												<li>信用评级：<img alt="${creditResult.data.borrow.borrowLevel}" src="${ctx}/img/${creditResult.data.borrow.borrowLevel}.png"></li>
											</c:if>
										</ul>
									</dd>
								</dl>
							</c:if>
							<dl class="new-detail-dl">
								<dt>项目描述</dt>
								<dd>${borrowInfo.borrowContents}</dd>
							</dl>
							<dl class="new-detail-dl">
								<dt>财务状况</dt>
								<dd>${borrowInfo.accountContents}</dd>
							</dl>
						</c:if> 
						<!-- 汇租赁 --> 
						<c:if test="${creditResult.data.creditTender.type eq 5}">
							<c:if test="${creditResult.data.creditTender.comOrPer eq 1}">
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<li>所在地区：${borrowInfo.borrowAddress}</li>
											<li>所属行业：${borrowInfo.borrowIndustry}</li>
											<li>注册资本：<fmt:formatNumber value="${borrowInfo.regCaptial}" pattern="#,###" />元</li>
											<li>注册时间：${borrowInfo.registTime}</li>
											<c:if test="${ not empty creditResult.data.borrow.borrowLevel and creditResult.data.borrow.borrowLevel ne '' }">
												<li>信用评级：<img alt="${creditResult.data.borrow.borrowLevel}" src="${ctx}/img/${creditResult.data.borrow.borrowLevel}.png"></li>
											</c:if>
										</ul>
									</dd>
								</dl>
							</c:if>
							<c:if test="${creditResult.data.creditTender.comOrPer eq 2}">
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<li>性别：${borrowInfo.sex}</li>
											<li>年龄：${borrowInfo.age}</li>
											<li>婚姻状况：${borrowInfo.maritalStatus}</li>
											<li>工作城市：${borrowInfo.workingCity}</li>
											<c:if test="${ not empty creditResult.data.borrow.borrowLevel and creditResult.data.borrow.borrowLevel ne '' }">
												<li>信用评级：<img alt="${creditResult.data.borrow.borrowLevel}" src="${ctx}/img/${creditResult.data.borrow.borrowLevel}.png"></li>
											</c:if>
										</ul>
									</dd>
								</dl>
							</c:if>
							<dl class="new-detail-dl">
								<dt>项目描述</dt>
								<dd>${borrowInfo.borrowContents}</dd>
							</dl>
							<dl class="new-detail-dl">
								<dt>财务状况</dt>
								<dd>${borrowInfo.accountContents}</dd>
							</dl>
						</c:if> 
						<!-- 供应贷 --> 
						<c:if test="${creditResult.data.creditTender.type eq 6}">
							<c:if test="${creditResult.data.creditTender.comOrPer eq 1}">
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<li>所在地区：${borrowInfo.borrowAddress}</li>
											<li>所属行业：${borrowInfo.borrowIndustry}</li>
											<li>注册资本：<fmt:formatNumber value="${borrowInfo.regCaptial}" pattern="#,###" />元</li>
											<li>注册时间：${borrowInfo.registTime}</li>
											<c:if test="${ not empty creditResult.data.borrow.borrowLevel and creditResult.data.borrow.borrowLevel ne '' }">
												<li>信用评级：<img alt="${creditResult.data.borrow.borrowLevel}" src="${ctx}/img/${creditResult.data.borrow.borrowLevel}.png"></li>
											</c:if>
										</ul>
									</dd>
								</dl>
							</c:if>
							<c:if test="${creditResult.data.creditTender.comOrPer eq 2}">
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<li>性别：${borrowInfo.sex}</li>
											<li>年龄：${borrowInfo.age}</li>
											<li>婚姻状况：${borrowInfo.maritalStatus}</li>
											<li>工作城市：${borrowInfo.workingCity}</li>
											<c:if test="${ not empty creditResult.data.borrow.borrowLevel and creditResult.data.borrow.borrowLevel ne '' }">
												<li>信用评级：<img alt="${creditResult.data.borrow.borrowLevel}" src="${ctx}/img/${creditResult.data.borrow.borrowLevel}.png"></li>
											</c:if>
										</ul>
									</dd>
								</dl>
							</c:if>
							<dl class="new-detail-dl">
								<dt>项目描述</dt>
								<dd>${borrowInfo.borrowContents}</dd>
							</dl>
							<dl class="new-detail-dl">
								<dt>财务状况</dt>
								<dd>${borrowInfo.accountContents}</dd>
							</dl>
						</c:if> 
						<!-- 汇房贷 --> 
						<c:if test="${creditResult.data.creditTender.type eq 7}">
							<c:if test="${creditResult.data.creditTender.comOrPer eq 1}">
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<li>所在地区：${borrowInfo.borrowAddress}</li>
											<li>所属行业：${borrowInfo.borrowIndustry}</li>
											<li>注册资本：<fmt:formatNumber value="${borrowInfo.regCaptial}" pattern="#,###" />元 </li>
											<li>注册时间：${borrowInfo.registTime}</li>
											<c:if test="${ not empty creditResult.data.borrow.borrowLevel and creditResult.data.borrow.borrowLevel ne '' }">
												<li>信用评级：<img alt="${creditResult.data.borrow.borrowLevel}" src="${ctx}/img/${creditResult.data.borrow.borrowLevel}.png"></li>
											</c:if>
										</ul>
									</dd>
								</dl>
							</c:if>
							<c:if test="${creditResult.data.creditTender.comOrPer eq 2}">
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<li>性别：${borrowInfo.sex}</li>
											<li>年龄：${borrowInfo.age}</li>
											<li>婚姻状况：${borrowInfo.maritalStatus}</li>
											<li>工作城市：${borrowInfo.workingCity}</li>
											<c:if test="${ not empty creditResult.data.borrow.borrowLevel and creditResult.data.borrow.borrowLevel ne '' }">
												<li>信用评级：<img alt="${creditResult.data.borrow.borrowLevel}" src="${ctx}/img/${creditResult.data.borrow.borrowLevel}.png"></li>
											</c:if>
										</ul>
									</dd>
								</dl>
							</c:if>
							<dl class="new-detail-dl">
								<dt>项目描述</dt>
								<dd>${borrowInfo.borrowContents}</dd>
							</dl>
						</c:if> 
						<!-- 汇消费 --> 
						<c:if test="${creditResult.data.creditTender.type eq 8}">
							<dl class="new-detail-dl">
								<dt>基础信息</dt>
								<dd>
									<ul>
										<li>所在地区：${borrowInfo.borrowAddress}</li>
										<li>所属行业：${borrowInfo.borrowIndustry}</li>
										<li>注册资本：<fmt:formatNumber value="${borrowInfo.regCaptial}" pattern="#,###" />元</li>
										<li>注册时间：${borrowInfo.registTime}</li>
										<c:if test="${ not empty creditResult.data.borrow.borrowLevel and creditResult.data.borrow.borrowLevel ne '' }">
												<li>信用评级：<img alt="${creditResult.data.borrow.borrowLevel}" src="${ctx}/img/${creditResult.data.borrow.borrowLevel}.png"></li>
											</c:if>
									</ul>
								</dd>
							</dl>
							<dl class="new-detail-dl">
								<dt>项目描述</dt>
								<dd>${borrowInfo.borrowContents}</dd>
							</dl>
							<dl class="new-detail-dl">
								<dt>财务状况</dt>
								<dd>${borrowInfo.accountContents}</dd>
							</dl>
						</c:if> 
						<!-- 汇资产 --> 
						<c:if test="${creditResult.data.creditTender.type eq 9}">
							<dl class="new-detail-dl">
								<dt>基础信息</dt>
								<dd>
									<ul>
										<li>项目名称：${borrowInfo.borrowName}</li>
										<li>项目类型：${borrowInfo.borrowType}</li>
										<li>所在地区：${borrowInfo.borrowAddress}</li>
										<li>评估价值：${borrowInfo.guarantyValue}</li>
										<li>权属类别：${borrowInfo.ownershipCategory}</li>
										<li>资产成因：${borrowInfo.assetOrigin}</li>
										<c:if test="${ not empty creditResult.data.borrow.borrowLevel and creditResult.data.borrow.borrowLevel ne '' }">
												<li>信用评级：<img alt="${creditResult.data.borrow.borrowLevel}" src="${ctx}/img/${creditResult.data.borrow.borrowLevel}.png"></li>
											</c:if>
									</ul>
								</dd>
							</dl>
							<dl class="new-detail-dl">
								<dt>资产信息</dt>
								<dd>${borrowInfo.attachmentInfo}</dd>
							</dl>
						</c:if> 
						<!-- 汇投资 --> 
						<c:if test="${creditResult.data.creditTender.type eq 10}">
							<c:if test="${creditResult.data.creditTender.comOrPer eq 1}">
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<li>所在地区：${borrowInfo.borrowAddress}</li>
											<li>所属行业：${borrowInfo.borrowIndustry}</li>
											<li>注册资本：<fmt:formatNumber value="${borrowInfo.regCaptial}" pattern="#,###" />元</li>
											<li>注册时间：${borrowInfo.registTime}</li>
											<c:if test="${ not empty creditResult.data.borrow.borrowLevel and creditResult.data.borrow.borrowLevel ne '' }">
												<li>信用评级：<img alt="${creditResult.data.borrow.borrowLevel}" src="${ctx}/img/${creditResult.data.borrow.borrowLevel}.png"></li>
											</c:if>
										</ul>
									</dd>
								</dl>
							</c:if>
							<c:if test="${creditResult.data.creditTender.comOrPer eq 2}">
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<li>性别：${borrowInfo.sex}</li>
											<li>年龄：${borrowInfo.age}</li>
											<li>婚姻状况：${borrowInfo.maritalStatus}</li>
											<li>工作城市：${borrowInfo.workingCity}</li>
											<c:if test="${ not empty creditResult.data.borrow.borrowLevel and creditResult.data.borrow.borrowLevel ne '' }">
												<li>信用评级：<img alt="${creditResult.data.borrow.borrowLevel}" src="${ctx}/img/${creditResult.data.borrow.borrowLevel}.png"></li>
											</c:if>
										</ul>
									</dd>
								</dl>
							</c:if>
							<dl class="new-detail-dl">
								<dt>项目描述</dt>
								<dd>${borrowInfo.borrowContents}</dd>
							</dl>
						</c:if>
					</li>
					<c:if test="${creditResult.data.creditTender.status eq 10 or creditResult.data.creditTender.status eq 11 or creditResult.data.creditTender.status eq 12 }">
							<!-- 汇保贷 -->
							<c:if test="${creditResult.data.creditTender.type eq 0}">
								<li panel="1">
									<dl class="new-detail-dl">
										<dt>合作机构</dt>
										<dd>${riskControl.partner}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>抵/质押物信息</dt>
										<dd>${riskControl.controlMort}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>风控措施</dt>
										<dd>${riskControl.controlMeasures}</dd>
									</dl>
								</li>
							<!-- 汇典贷 -->
							<c:if test="${creditResult.data.creditTender.type eq 1}">
								<li panel="1">
									<dl class="new-detail-dl">
										<dt>合作机构</dt>
										<dd>${riskControl.partner}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>抵/质押物信息</dt>
										<dd>${riskControl.controlMort}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>风控措施</dt>
										<dd>${riskControl.controlMeasures}</dd>
									</dl>
								</li>
							</c:if>
							<!-- 汇小贷 -->
							<c:if test="${creditResult.data.creditTender.type eq 2}">
								<li panel="1">
									<dl class="new-detail-dl">
										<dt>合作机构</dt>
										<dd>${riskControl.partner}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>抵/质押物信息</dt>
										<dd>${riskControl.controlMort}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>风控措施</dt>
										<dd>${riskControl.controlMeasures}</dd>
									</dl>
								</li>
							</c:if>
							<!-- 汇车贷 -->
							<c:if test="${creditResult.data.creditTender.type eq 3}">
								<li panel="1">
									<dl class="new-detail-dl">
										<dt>合作机构</dt>
										<dd>${riskControl.partner}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>风控措施</dt>
										<dd>${riskControl.controlMeasures}</dd>
									</dl>
								</li>
							</c:if>
							<!-- 新手标 -->
							<c:if test="${creditResult.data.creditTender.type eq 4}">
								<li panel="1">
									<dl class="new-detail-dl">
										<dt>合作机构</dt>
										<dd>${riskControl.partner}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>抵/质押物信息</dt>
										<dd>${riskControl.controlMort}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>风控措施</dt>
										<dd>${riskControl.controlMeasures}</dd>
									</dl>
								</li>
							</c:if>
							<!-- 汇租赁 -->
							<c:if test="${creditResult.data.creditTender.type eq 5}">
								<li panel="1">
									<dl class="new-detail-dl">
										<dt>抵/质押物信息</dt>
										<dd>${riskControl.controlMort}</dd>
									</dl>
								</li>
							</c:if>
							<!-- 供应贷 -->
							<c:if test="${creditResult.data.creditTender.type eq 6}">
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
							<c:if test="${creditResult.data.creditTender.type eq 7}">
								<li panel="1">
									<dl class="new-detail-dl">
										<dt>合作机构</dt>
										<dd>${riskControl.partner}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>抵/质押物信息</dt>
										<dd>${riskControl.controlMort}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>风控措施</dt>
										<dd>${riskControl.controlMeasures}</dd>
									</dl>
								</li>
							</c:if>
							<!-- 汇消费 -->
							<c:if test="${creditResult.data.creditTender.type eq 8}">
								<li panel="1">
									<dl class="new-detail-dl">
										<dt>合作机构</dt>
										<dd>${riskControl.partner}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>风控措施</dt>
										<dd>${riskControl.controlMeasures}</dd>
									</dl>
								</li>
							</c:if>
							<!-- 汇资产 -->
							<c:if test="${creditResult.data.creditTender.type eq 9}">
								<li panel="1">
										<dl class="new-detail-dl">
										<dt>售价预估</dt>
										<dd>${disposalPlan.priceEstimate}</dd>
										<dt>处置周期</dt>
										<dd>${disposalPlan.disposalPeriod}</dd>
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
							<c:if test="${creditResult.data.creditTender.type eq 10}">
								<li panel="1">
									<dl class="new-detail-dl">
										<dt>合作机构</dt>
										<dd>${riskControl.partner}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>风控措施</dt>
										<dd>${riskControl.controlMeasures}</dd>
									</dl>
								</li>
							</c:if>
						</c:if>
					</c:if>
					<c:if test="${creditResult.data.creditTender.status eq 13 }">
					
							<!-- 汇保贷 -->
							<c:if test="${creditResult.data.creditTender.type eq 0}">
								<li panel="1">
									<dl class="new-detail-dl">
										<dt>合作机构</dt>
										<dd>${riskControl.partner}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>抵/质押物信息</dt>
										<dd>${riskControl.controlMort}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>风控措施</dt>
										<dd>${riskControl.controlMeasures}</dd>
									</dl>
								</li>
							</c:if>
							<!-- 汇典贷 -->
							<c:if test="${creditResult.data.creditTender.type eq 1}">
								<li panel="1">
									<dl class="new-detail-dl">
										<dt>合作机构</dt>
										<dd>${riskControl.partner}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>抵/质押物信息</dt>
										<dd>${riskControl.controlMort}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>风控措施</dt>
										<dd>${riskControl.controlMeasures}</dd>
									</dl>
								</li>
							</c:if>
							<!-- 汇小贷 -->
							<c:if test="${creditResult.data.creditTender.type eq 2}">
								<li panel="1">
									<dl class="new-detail-dl">
										<dt>合作机构</dt>
										<dd>${riskControl.partner}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>抵/质押物信息</dt>
										<dd>${riskControl.controlMort}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>风控措施</dt>
										<dd>${riskControl.controlMeasures}</dd>
									</dl>
								</li>
							</c:if>
							<!-- 汇车贷 -->
							<c:if test="${creditResult.data.creditTender.type eq 3}">
								<li panel="1">
									<dl class="new-detail-dl">
										<dt>合作机构</dt>
										<dd>${riskControl.partner}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>风控措施</dt>
										<dd>${riskControl.controlMeasures}</dd>
									</dl>
								</li>
							</c:if>
							<!-- 新手标 -->
							<c:if test="${creditResult.data.creditTender.type eq 4}">
								<li panel="1">
									<dl class="new-detail-dl">
										<dt>合作机构</dt>
										<dd>${riskControl.partner}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>抵/质押物信息</dt>
										<dd>${riskControl.controlMort}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>风控措施</dt>
										<dd>${riskControl.controlMeasures}</dd>
									</dl>
								</li>
							</c:if>
							<!-- 汇租赁 -->
							<c:if test="${creditResult.data.creditTender.type eq 5}">
								<li panel="1">
									<dl class="new-detail-dl">
										<dt>抵/质押物信息</dt>
										<dd>${riskControl.controlMort}</dd>
									</dl>
								</li>
							</c:if>
							<!-- 供应贷 -->
							<c:if test="${creditResult.data.creditTender.type eq 6}">
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
							<c:if test="${creditResult.data.creditTender.type eq 7}">
								<li panel="1">
									<dl class="new-detail-dl">
										<dt>合作机构</dt>
										<dd>${riskControl.partner}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>抵/质押物信息</dt>
										<dd>${riskControl.controlMort}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>风控措施</dt>
										<dd>${riskControl.controlMeasures}</dd>
									</dl>
								</li>
							</c:if>
							<!-- 汇消费 -->
							<c:if test="${creditResult.data.creditTender.type eq 8}">
								<li panel="1">
									<dl class="new-detail-dl">
										<dt>合作机构</dt>
										<dd>${riskControl.partner}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>风控措施</dt>
										<dd>${riskControl.controlMeasures}</dd>
									</dl>
								</li>
							</c:if>
						
							<!-- 汇资产 -->
							<c:if test="${creditResult.data.creditTender.type eq 9}">
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
							<c:if test="${creditResult.data.creditTender.type eq 10}">
								<li panel="1">
									<dl class="new-detail-dl">
										<dt>合作机构</dt>
										<dd>${riskControl.partner}</dd>
									</dl>
									<dl class="new-detail-dl">
										<dt>风控措施</dt>
										<dd>${riskControl.controlMeasures}</dd>
									</dl>
								</li>
							</c:if>
						</c:if>
					<c:if test="${creditResult.data.creditTender.status eq 10 or creditResult.data.creditTender.status eq 11 or creditResult.data.creditTender.status eq 12 }">
							<li panel="2">
								<div class="new-detail-imgs">
									<div class="new-detail-img-t">相关资料</div>
									<div class="new-detail-img-c">
										<ul>
											<c:forEach items="${fileList}" var="file">
												<li>
													<a href="${file.fileUrl}" data-caption="${file.fileName}"> 
														<div><img src="${file.fileUrl}" alt=""></div>
														<span class="title">${file.fileName}</span>
													</a>
												</li>
											</c:forEach>
										</ul>
									</div>
								</div>
							</li>
					</c:if>
					<c:if test="${creditResult.data.creditTender.status eq 13 }">
							<li panel="2">
						<div class="new-detail-imgs">
                            <div class="new-detail-img-t">相关资料</div>
                            <div class="new-detail-img-c">
                                <ul>
                                  <c:forEach items="${creditResult.data.borrowFiles}" var="borrowFiles">
                                    <li>
                                        <a href="${borrowFiles.fileUrl}" data-caption="${borrowFiles.fileName}"><div><img src="${borrowFiles.fileUrl}" alt=""></div><span class="title">${borrowFiles.fileName}</span></a>
                                    </li>
								  </c:forEach>
                                </ul>
                            </div>
                        </div>
							</li>
					</c:if>
					
				   <li panel="3">
                        <%-- <div class="new-detail-tb-t">
                            已还本息 <strong><fmt:formatNumber value="${creditResult.data.repay.repayed}" pattern="#,#00.00"/></strong> 元 待还本息 <strong><fmt:formatNumber value="${creditResult.data.repay.redayRepay}" pattern="#,#00.00"/></strong> 元
                        </div> --%>
                        <table class="new-detail-tb" id="repayTable">
                            <thead>
                                <tr>
                                    <th width="270">还款时间</th>
                                    <th>类型</th>
                                    <th width="430">还款金额</th>
                                </tr>
                            </thead>
                            <tbody>
	                            <c:if test="${creditResult.data.creditTender.borrowStyle=='endmonth'}">
	                              <c:forEach items="${creditResult.data.repay.repayPlan}" var="recoverPlan">
	                              	  <c:if test="${recoverPlan.repayInterest>0.00 and recoverPlan.repayCapital>0.00}">
		                                <tr>
		                                    <td>${recoverPlan.repayTime}</td>
		                                    <td>本息</td>
		                                    <td><span class="highlight">￥${recoverPlan.repayAccount}</span></td>
		                                </tr>
	                              	  </c:if>
	                              	  <c:if test="${recoverPlan.repayInterest eq '0.00' or recoverPlan.repayCapital eq '0.00'}">
			                              <c:if test="${recoverPlan.repayInterest>0.00}">
			                                <tr>
			                                    <td>${recoverPlan.repayTime}</td>
			                                    <td>利息</td>
			                                    <td><span class="highlight">￥${recoverPlan.repayInterest}</span></td>
			                                </tr>
			                              </c:if>
			                              <c:if test="${recoverPlan.repayCapital>0.00}">
			                                <tr>
			                                    <td>${recoverPlan.repayTime}</td>
			                                    <td>本金</td>
			                                    <td><span class="highlight">￥${recoverPlan.repayCapital}</span></td>
			                                </tr>
			                              </c:if>
	                              	  </c:if>
	                              </c:forEach>
	                             </c:if>
	                             <c:if test="${creditResult.data.creditTender.borrowStyle!='endmonth'}">
	                              <c:forEach items="${creditResult.data.repay.repayPlan}" var="recoverPlan">
		                              <c:if test="${recoverPlan.repayInterest>0.00}">
		                                <tr>
		                                    <td>${recoverPlan.repayTime}</td>
		                                    <td>利息</td>
		                                    <td><span class="highlight">￥${recoverPlan.repayInterest}</span></td>
		                                </tr>
		                              </c:if>
		                              <c:if test="${recoverPlan.repayCapital>0.00}">
		                                <tr>
		                                    <td>${recoverPlan.repayTime}</td>
		                                    <td>本金</td>
		                                    <td><span class="highlight">￥${recoverPlan.repayCapital}</span></td>
		                                </tr>
		                              </c:if>
	                              </c:forEach>
	                             </c:if>
                            </tbody>
                        </table>
                        <div class="new-pagination" id="repayPage"> 
                        	<!-- 分页栏模板 -->  
                        	${creditResult.data.repay.repayPage.webPaginator}
                        </div>
                    </li>
                    <li panel="4">
                        <div class="new-detail-tb-t">
                            总投标金额 <strong>
                     <c:choose>
						<c:when test="${creditResult.data.tender.tendTotal ne null && creditResult.data.tender.tendTotal ne 0 && creditResult.data.tender.tendTotal ne ''}">
						    <fmt:formatNumber value="${creditResult.data.tender.tendTotal}" pattern="#,#00.00"/></strong> 元 加入人次 <strong>${creditResult.data.tender.tenderNum}
						</c:when>
						<c:otherwise>
						    <fmt:formatNumber value="0" pattern="#,#00.00"/></strong> 元 加入人次 <strong>0
						</c:otherwise>
					</c:choose> 
                            </strong> 人次
                        </div>
                        
                          <c:choose>
						<c:when test="${creditResult.data.tender.tendTotal ne null && creditResult.data.tender.tendTotal ne 0 && creditResult.data.tender.tendTotal ne ''}">
						   <table class="new-detail-tb" id="tenderTable">
                            <thead>
                                <tr>
                                    <th>用户名</th>
                                    <th width="310">投标金额</th>
                                    <th width="370">投标时间</th>
                                    <th width="210">投标来源</th>
                                </tr>
                            </thead>
                            <tbody>
                              <c:forEach items="${creditResult.data.tender.tenders}" var="tenders">
                                <tr>
                                    <td>${tenders.addip}</td>
                                    <td><span class="highlight">￥${tenders.assignCapital}</span></td>
                                    <td>${tenders.createUserName}</td>
                                    <td>
                                    <c:if test="${tenders.client==0}">
                                    	PC
                                    </c:if>
                                    <c:if test="${tenders.client==1}">
                                    	微信
                                    </c:if>
                                    <c:if test="${tenders.client==2}">
                                    	安卓APP
                                    </c:if>
                                    <c:if test="${tenders.client==3}">
                                    	IOS APP
                                    </c:if>
                                    <c:if test="${tenders.client==3}">
                                    	其他
                                    </c:if>
                                    </td>
                                </tr>
							  </c:forEach>
                            </tbody>
                        </table>
                        <div class="new-pagination" id="tenderPage"> 
                        	<!-- 分页栏模板 -->  
                        	${creditResult.data.tender.tenderPage.webPaginator}
                        </div>
						</c:when>
						<c:otherwise>
						<div class="new-detail-tb-t" style="width:100%;text-align:center;margin:30px 0;">
							未查询到数据
						</div>
						</c:otherwise>
					</c:choose> 
                     
                    </li>
				</ul>
            </div>
            <div class="clearfix"></div>
        </div>
    </div>
    <div class="settlement_mask"></div>
    <div class="settlement js_zr js_zra">
        <a class="zr_close js_close" href="javascript:void(0)">×</a>
        <div class="qr_main">
            <h3>提示信息</h3>
            <h4 class="zr_pay">实际支付:<span class="act_pay_num highlight"></span>元</h4>
            <dl class="zr_pay_main">
                <dd>
                    <label>认购本金</label><span class="assign_capital highlight"></span>元</dd>
                <dd>
                    <label>垫付利息</label><span class="assign_interest_advance highlight"></span>元</dd>
                <dd>
                    <label>折价率</label><span class="discount_rate highlight">0.0</span>%</dd>
                
            </dl>
            <dl>
                <dd>
                    <label>实际支付</label><span class="pay_num"></span></dd>
                <dd><a href="${ctx}/help/index.do" target="_blank" class="highlight">帮助中心</a></dd>
            </dl>
            <div class="btns">
                <button type="button" id="submitBtn">确定</button>
                <button type="button" class="btn_close">取消</button>
            </div>
        </div>
    </div>
    <script>/* setActById('hdCFH'); */setActById('subHTJ');</script>
    <jsp:include page="/footer.jsp"></jsp:include>
    <script type="text/javascript" src="${cdn}/js/jquery.validate.js" charset="utf-8"></script>
    <script type="text/javascript" src="${cdn}/js/messages_cn.js" charset="utf-8"></script>
    <script type="text/javascript" src="${cdn}/js/jquery.metadata.js" charset="utf-8"></script>
    <script type="text/javascript" src="${cdn}/js/baguetteBox.min.js" charset="utf-8"></script>
    
    <script>
    $(".new-detail-main").click(function(e){
        var _self = $(e.target);
        if(_self.is("li")){
            var idx = _self.attr("panel");
            var panel = _self.parent().next(".new-detail-tab-panel");
            _self.siblings("li.active").removeClass("active");
            _self.addClass("active");
            panel.children("li.active").removeClass("active");
            panel.children("li[panel="+idx+"]").addClass("active");
        }
    });
    $(".zhuanrang").find(".icon-info").mouseover(function(){
        showInfo($(this));
    }).mouseleave(function(){
        hideInfo($(this));
    });
    function showInfo(obj){
        obj.children("em").fadeIn();
    }
    function hideInfo(obj){
        obj.children("em").hide();
    }
    
    var now = new Date("<%=new java.util.Date()%>");
    now =now.getTime()-1000*60*60*14;
    var box = $("#time");
    var timedata = box.data("time")*1000;
    var currTimmer;
    doTimer();
	function doTimer(){
		var current = box.data("current") || parseInt(now);
        var ts = timedata - current; //计算剩余的毫秒数  
        if (ts >= 0) {
            timer(box, ts, current);
        } else {
            clearInterval(currTimmer);
            //倒计时为0时执行
        }
        if(!currTimmer){
        	currTimmer = setInterval(function() {
                doTimer();
            }, 1000);
        }
	}
    //倒计时
    function timer(box, ts, current) {
        /*
         * @param box 存放倒计时容器
         * @param ts 剩余时间
         * @param current 当前时间
         */
        var dd = parseInt(ts / 1000 / 60 / 60 / 24, 10); //计算剩余的天数  
        var hh = parseInt(ts / 1000 / 60 / 60, 10); //计算剩余的小时数  
        var mm = parseInt(ts / 1000 / 60 % 60, 10); //计算剩余的分钟数  
        var ss = parseInt(ts / 1000 % 60, 10); //计算剩余的秒数  
        //dd = checkTime(dd);
        hh = checkTime(hh);
        mm = checkTime(mm);
        //ss = checkTime(ss);
        if (ts >= 0) {
            box.data("current", current + 1000); //更新倒计时
            box.html("剩余时间：<strong>" + hh + "</strong> 小时 <strong>" + mm + "</strong> 分");
        }
    }

    function checkTime(i) {
        if (i < 10) {
            i = "0" + i;
        }
        return i;
    }
    
  	//弹出框关闭
    $('.js_close,.btn_close').click(function(){
        closePop();
    })

    //问号点击
    $("#pay_detail,#confirmBtn").click(function(){
    	var assignCapital = parseFloat(jQuery("#assignCapital").val()).toFixed(2);
    	var maxAssignCapital = parseFloat(jQuery("#maxAssignCapital").val());
    	var balance = parseFloat(jQuery("#balance").val());
    	var term = $("#termcheck");
    	if(term.prop("checked") == false){
    		term.parent().parent().append('<span id="termcheck-error" class="error">请先阅读并同意汇盈金服投资协议</span>')
    	}else if(assignCapital>=1 && assignCapital<=maxAssignCapital && assignCapital<=balance && !$(this).hasClass("isdisabled")){
    		openPop();
    	}else{
    		if($("#ajaxErr").length){
				$("#ajaxErr").text("请输入正确的金额");
			}else{
				$("#assignCapital").parent().parent().append("<span id='ajaxErr' class='error'>请输入正确的金额</span>");
			}
    	} 
    });
    function openPop(){
        /*打开弹窗*/
        $('.settlement_mask').fadeIn();
        $('.js_zr').fadeIn();
    }
    function closePop(){
        /*关闭弹窗*/
        $('.settlement_mask').fadeOut();
        $('.js_zr').fadeOut();
    }
    </script>
</body>
<script type="text/javascript">
	var timmer;
	$(document).ready(function(){
		//确认投资点击事件
		$('#submitBtn').click(function(){
			var assignCapital = parseFloat(jQuery("#assignCapital").val());
			if(assignCapital<=parseFloat(jQuery("#maxAssignCapital").val()) && jQuery("#creditNid").val()!=null && jQuery("#creditNid").val()!=0){
				if(jQuery("#creditNid").val()!=null && jQuery("#creditNid").val()!=""){
					jQuery("#detialForm").submit();
				}else{
					if($("#ajaxErr").length){
						$("#ajaxErr").text("无法完成提交,请刷新页面后重试!");
					}else{
						$("#assignCapital").parent().parent().append("<span id='ajaxErr' class='error'>无法完成提交,请刷新页面后重试!</span>");
					}
				}
			}else{
				if($("#ajaxErr").length){
					$("#ajaxErr").text("无法完成提交,请刷新页面后重试!");
				}else{
					$("#assignCapital").parent().parent().append("<span id='ajaxErr' class='error'>无法完成提交,请刷新页面后重试!</span>");
				}
			}
    	})
		//输入投资金额后历史回报提示
		jQuery("#assignCapital").keyup(function(){
			var _self = $(this);
			var val = _self.val();
			if(!checkOnlyNumber(val)){
				_self.val(parseInt(val.replace(/[^\d]/g,'')));
				_self.val("");
				return;
		   }
			if(timmer){
		        clearTimeout(timmer);
		    }
		    timmer = setTimeout(function(){
				var assignCapital = $("#assignCapital").val();
				if(assignCapital!=null && assignCapital>=1){
					$("#assignCapital").val(parseInt(assignCapital));
					jQuery.ajax({
						type: "POST",
						async: "async",
						url: "${ctx}/credit/webcheckcredittenderassign.do",
						contentType:"application/x-www-form-urlencoded;charset=utf-8",
						dataType: "json",
						data: {
							"creditNid":jQuery("#creditNid").val(),
							"assignCapital":assignCapital
						},
						success: function(data){
							if(data.resultFlag==0){
								$("#ajaxErr").remove();
								//历史回报
								jQuery("#income").html(data.data.tenderToCreditAssign.assignInterest);
								//实际支付
								jQuery(".act_pay_num").html(data.data.tenderToCreditAssign.assignPay);
								//弹出浮层数据提示
								jQuery(".assign_capital").html(data.data.tenderToCreditAssign.assignCapital);
								jQuery(".assign_interest_advance").html(data.data.tenderToCreditAssign.assignInterestAdvance);
								jQuery(".discount_rate").html(data.data.tenderToCreditAssign.creditDiscount);
								jQuery(".pay_num").html(data.data.tenderToCreditAssign.assignPayText);	
								$("#pay_detail,#confirmBtn").removeClass("isdisabled");
								
							}else{
								if($("#ajaxErr").length){
									$("#ajaxErr").text(data.msg);
									
								}else{
									$("#assignCapital").parent().parent().append("<span id='ajaxErr' class='error'>"+data.msg+"</span>");
								}
								if(data.resultType == "1"){
									$("#pay_detail,#confirmBtn").addClass("isdisabled");
								}else{
									$("#pay_detail,#confirmBtn").removeClass("isdisabled");
								}
								//jQuery("#assignCapital").val("");
							}
						}
					});
				}else{
					$("#assignCapital").val("");
					//历史回报
					jQuery("#income").html("0.00");
					//实际支付
					jQuery(".act_pay_num").html("0.00");
				}
		    },500);
		})
		//全投点击事件
		jQuery("#assignCapitalAll").click(function(){
			var assignCapital = 0;
			var balance = parseFloat(jQuery("#balance").val());
			if((balance+'').indexOf(".")!=-1){
				balance=(balance+'').substring(0, (balance+'').indexOf("."));
			}
			var maxAssignCapital = parseFloat(jQuery("#maxAssignCapital").val());
			if(balance>maxAssignCapital){
				jQuery("#assignCapital").val(maxAssignCapital);
				assignCapital = maxAssignCapital;
			}else{
				jQuery("#assignCapital").val(balance);
				assignCapital = balance;
			}
			jQuery.ajax({
				type: "POST",
				async: "async",
				url: "${ctx}/credit/webcheckcredittenderassign.do",
				contentType:"application/x-www-form-urlencoded;charset=utf-8",
				dataType: "json",
				data: {
					"creditNid":jQuery("#creditNid").val(),
					"assignCapital":assignCapital
				},
				success: function(data){
					$("#ajaxErr").remove();
					if(data.resultFlag==0){
						//历史回报
						jQuery("#income").html(data.data.tenderToCreditAssign.assignInterest);
						//实际支付
						jQuery(".act_pay_num").html(data.data.tenderToCreditAssign.assignPay);
						//弹出浮层数据提示
						jQuery(".assign_capital").html(data.data.tenderToCreditAssign.assignCapital);
						jQuery(".assign_interest_advance").html(data.data.tenderToCreditAssign.assignInterestAdvance);
						jQuery(".discount_rate").html(data.data.tenderToCreditAssign.creditDiscount);
						jQuery(".pay_num").html(data.data.tenderToCreditAssign.assignPayText);	
						$("#pay_detail,#confirmBtn").removeClass("isdisabled");
						
					}else{
						if($("#ajaxErr").length){
							$("#ajaxErr").text(data.msg);
						}else{
							$("#assignCapital").parent().parent().append("<span id='ajaxErr' class='error'>"+data.msg+"</span>");
						}
						if(data.resultType == "1"){
							$("#pay_detail,#confirmBtn").addClass("isdisabled");
						}else{
							$("#pay_detail,#confirmBtn").removeClass("isdisabled");
						}
						//jQuery("#assignCapital").val("");
					}
				}
			});
		})
		
	})
	function checkOnlyNumber(val){
	    /*检测是否是数字*/
	    var reg = /^[0-9]*$/;
	    if(reg.test(val)){
	        return true;
	    }
	    return false;
	}
	//还款计划分页
	function repayPage(page, limit, param){
		if(page!=null && page!="" && page!=0){
			jQuery.ajax({
				type: "POST",
				async: "async",
				url: "${ctx}/credit/planwebcreditrepaypage.do",
				contentType:"application/x-www-form-urlencoded;charset=utf-8",
				dataType: "json",
				data: {
					"borrowNid":jQuery("#borrowNid").val(),
					"currPage":page,
					"limitPage":limit
				},
				success: function(data){
				  var pagStr = "";
				  for(var i=0;i<data.data.repayPlan.length;i++){
					  pagStr += "<tr><td>"+data.data.repayPlan[i].repayTime+"</td><td>利息</td><td><span class=\"highlight\">￥"+data.data.repayPlan[i].repayInterest+"</span></td></tr>";
					  pagStr += "<tr><td>"+data.data.repayPlan[i].repayTime+"</td><td>本金</td><td><span class=\"highlight\">￥"+data.data.repayPlan[i].repayCapital+"</span></td></tr>"
				  }
				  jQuery("#repayTable tbody").html(pagStr);
				  jQuery("#repayPage").html(data.data.repayPage.webPaginator);
				}
			});
		}
	};
	//投资记录分页
	function tenderPage(page, limit, param){
		if(page!=null && page!="" && page!=0){
			jQuery.ajax({
				type: "POST",
				url: "${ctx}/credit/planwebcredittenderpage.do",
				contentType:"application/x-www-form-urlencoded;charset=utf-8",
				dataType: "json",
				data: {
					"creditNid":jQuery("#creditNid").val(),
					"currPage":page,
					"limitPage":limit
				},
				success: function(data){
					var pagStr = "";
					for(var i=0;i<data.data.tenders.length;i++){
						pagStr += "<tr>";
						pagStr += "<td>"+data.data.tenders[i].addip+"</td>";
						pagStr += "<td><span class=\"highlight\">￥"+data.data.tenders[i].assignCapital+"</span></td>";
						pagStr += "<td>"+data.data.tenders[i].createUserName+"</td>";
						if(data.data.tenders[i].client==0){
							pagStr +="<td>PC</td>";
						}
						if(data.data.tenders[i].client==1){
							pagStr += "<td>微信</td>";
						}
						if(data.data.tenders[i].client==2){
							pagStr += "<td>安卓 APP</td>";
						}
						if(data.data.tenders[i].client==3){
							pagStr += "<td>IOS APP</td>";
						}
						if(data.data.tenders[i].client==4){
							pagStr += "<td>其他</td>";
						}
						pagStr += "</tr>";
					}
					jQuery("#tenderTable tbody").html(pagStr);
					jQuery("#tenderPage").html(data.data.tenderPage.webPaginator);
				}
			});
		}
	};
	$(document).click(function(e){
		if($(e.target).attr("id") == "ajaxErr"){
			$(e.target).remove();
		}
	});
	//vip 购买弹层
	/* function setCookieVip(c_name, value, expiredays) {
		var exdate = new Date();
		exdate.setDate(exdate.getDate() + expiredays)
		document.cookie = c_name + "=" + escape(value) +";path=/"+ ((expiredays == null) ? "" : ";expires=" + exdate.toGMTString())
	}
	$(".vip-buy-close").click(function(){
		$(".vip-buy-pop-div").animate({top:"-62px"},600).animate({backgroundSize:"42%"},400,function(){
			$(".vip-buy-pop").fadeOut();
			$("html").css("overflow","auto");
		})
		
	})
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
	} */
	$(".appoint-term .checkicon").click(function(){
		if($(this).hasClass("avaliable")){
			if(!$(".appoint-term .checkicon").hasClass("checked")){
				$(this).addClass("checked");
				$(this).children("input").prop("checked",true).valid();
				$("#termcheck-error").remove();
			}else{
				$(this).removeClass("checked");
				$(this).children("input").prop("checked",false).valid();
			}
		}
	});
	$(".appoint-term").delegate("#termcheck-error","click",function(){
		$(this).remove();
	});
	function openNew(url) {
		window.open(url);
	}
</script>
</html>