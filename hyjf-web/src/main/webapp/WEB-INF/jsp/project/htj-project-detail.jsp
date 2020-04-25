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
                <a href="${ctx}/plan/initPlanList.do">汇计划</a> &gt; 项目详情
            </div>
        </div>
    </section>
	<article class="main-content product">
        <div class="container">
            <!-- start 内容区域 -->
            <div class="product-intr">
                <div class="title">
                <input type="hidden" id="borrowNid" value="${projectDeatil.borrowNid}">
                    <span>${projectDeatil.borrowNid}</span><%-- 原${projectDeatil.projectName} --%>
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
							<a href="${ctx}/agreement/intermediaryServices.do" target="_blank">《居间服务借款协议(范本)》 </a>
						    <a href="${ctx}/agreement/confirmationOfInvestmentRisk.do" target="_blank">《投资风险确认书(范本)》</a>
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
<%-- 	                         <c:if test="${projectDeatil.borrowExtraYield ne '0.00'}">
	                         	<div class="poptag-pos">
		                            <div class="poptag">+${projectDeatil.borrowExtraYield}%</div>
		                        </div>
	                         </c:if> --%>
	                        </span>
                        <span class="key">预期年化收益率</span>
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
                    <div class="list-item"><span class="dark">建议投资者类型：稳健型及以上</span></div>
                </div>
            </div>
            
            <div class="product-form status">
            	<div class="status-content hjh single"></div>
            </div>
            <!-- end 内容区域 -->     
        </div>
        <div class="container">
            <section class="content">
                <div class="main-tab">
                    <ul class="tab-tags">
                        
						<!-- 融通宝展示不同 -->
						<li panel="0" class="active">
							<a href="javascript:;">
								<c:if test="${projectDeatil.type eq 13}">产品详情</c:if>
								<c:if test="${projectDeatil.type ne 13}">风控信息</c:if>
							</a>
						</li>
						<!-- 融通宝展示不同 -->
						<c:if test="${projectDeatil.type ne 13}">
							<c:if test="${projectDeatil.status eq 13}">
								<li panel="1"><a href="javascript:;">相关文件</a></li>
							</c:if>
							<!-- 汇消费 -->
							<c:if test="${projectDeatil.type eq 8}">
								<li panel="5"><a href="javascript:;">债权信息</a></li>
							</c:if>
							<li panel="2"><a href="javascript:;">还款计划</a></li>
						</c:if>
						<li panel="3"><a href="javascript:;">加入记录</a></li>
						<li panel="4"><a href="javascript:;">常见问题</a></li>

                    </ul>
                    <ul class="tab-panels">
                    <li panel="0" class="active">
						<div class="attr-title"><span>项目流程</span></div>
                        <div class="attr-table"><img alt="项目流程图" src="${cdn}/dist/images/product/product_old_flow.png"></div>
                        <!-- 融通宝展示内容 -->
						<c:if test="${projectDeatil.type eq 13}">
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
	                                    	<span class="key">预期年化收益率</span>
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
											<table cellspacing="0" cellpadding="0">
			                          	  	<tbody>
			                          	  		<c:forEach items="${vehiclePledgeList}" var="vehiclePledge">
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
					                             </c:forEach>
			                         	   	</tbody>
				                        	</table>
										</div>	
									</c:if>
								</c:if>
								
								<c:if test="${projectDeatil.type eq 0 || projectDeatil.type eq 1 || projectDeatil.type eq 2 ||
						              projectDeatil.type eq 3 || projectDeatil.type eq 12 || projectDeatil.type eq 4 || 
						              projectDeatil.type eq 11 || projectDeatil.type eq 5 || projectDeatil.type eq 7 ||
						              projectDeatil.type eq 14 || projectDeatil.type eq 10 }">
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
								<c:if test="${projectDeatil.type eq 6 and projectDeatil.comOrPer eq 1 }">
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
					</li>
                       
                        <li panel="1">
                            <!-- 相关文件 -->
                            <div class="new-detail-img-c">
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
                            <!-- 加入记录 -->
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
	<script src="${ctx}/dist/js/lib/baguetteBox.min.js"></script>
	<script src="${ctx}/dist/js/lib/jquery.validate.js"></script>
    <script src="${ctx}/dist/js/lib/jquery.metadata.js"></script>
    <script src="${ctx}/dist/js/product/data-format.js"></script>
    <script type="text/javascript" src="${ctx}/dist/js/lib/jquery.jcountdown.min.js"></script>
    <script src="${ctx}/dist/js/product/htj-product-detail.js?version=${version}"></script>
</body>
</html>