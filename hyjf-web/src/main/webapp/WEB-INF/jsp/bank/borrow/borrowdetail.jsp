<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page language="java" import="java.util.Date"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta charset="utf-8" />
<title>${projectDeatil.borrowNid}项目详情- 汇盈金服官网</title>
<link rel="stylesheet" type="text/css" href="${cdn}/css/baguetteBox.min.css" />
<%@ include file="/head.jsp"%>
<script>
	var projectType = "";
</script>
</head>

<body>
	<%@ include file="/header.jsp"%>
	<div class="new-detail-con">
		<input id="type" name="type" type="hidden" value="${projectDeatil.type}" /> 
		<input type="hidden" id="increase" value="${projectDeatil.increaseMoney}" title="递增金额" /> 
		<input id="borrowNid" name="borrowNid" type="hidden" value="${projectDeatil.borrowNid}" /> 
		<input id="isLast" type="hidden" value="${projectDeatil.tenderAccountMin ge InvestAccountInt}" />

		<div class="new-detail-inner">
			<h4>
				<span class="title">项目编号 ：${projectDeatil.type == 13 ? projectDeatil.borrowAssetNumber : projectDeatil.borrowNid} </span> 
				<span class="date"> 
					<span>起投金额：<fmt:formatNumber value="${projectDeatil.tenderAccountMin}" pattern="#,###" />元起投</span> 
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
					
					<div class="con2"  <c:if test="${projectDeatil.borrowExtraYield ne '0.00'}">style="width:230px;"</c:if>>
						<div class="num highlight">${projectDeatil.borrowApr}<span>%</span><c:if test="${projectDeatil.borrowExtraYield ne '0.00'}"><span style="font-size:22px;"> + <div  class="highlight" style="display:inline;">${projectDeatil.borrowExtraYield}</div>%</span></c:if>
						</div>
						<div class="con-title">历史年回报率</div>
					</div>
					<div class="con3" <c:if test="${projectDeatil.borrowExtraYield ne '0.00'}">style="width:145px;"</c:if>>
						<div class="num">${projectDeatil.borrowPeriod}
							<span>${projectDeatil.borrowPeriodType}</span>
						</div>
						<div class="con-title">项目期限</div>
					</div>
				</div>
				<div class="hd2">
					<!-- 定时发标 -->
					<c:if test="${projectDeatil.status eq 10}">
						<input type="hidden" id="userData" data-balance="${userBalance}" data-openflag="${openFlag}">
							<input type="hidden" name="tokenCheck" id="tokenCheck" value="" />
							<input type="hidden" name="tokenGrant" id="tokenGrant" value="${tokenGrant}" />
							<div class="hd2-item con1">
								<input type="hidden" id="projectData" data-total="${projectDeatil.investAccount}" data-tendermax="${projectDeatil.tenderAccountMax}" data-tendermin="${projectDeatil.tenderAccountMin}"/>
								<span class="title">可投金额</span>
								<a href="${ctx}/recharge/rechargePage.do" class="recharge banrecharge" onclick="setCookie()">充值</a>
								<span class="total"> 
									<fmt:formatNumber value="${projectDeatil.investAccount}" pattern="#,###" />
								<span class="total-font">元</span>
								</span>
								
							</div>
							<div class="hd2-item con2">
								<c:if test="${loginFlag eq 1}">
									<!-- 投标开始 -->
									<span class="title">银行存管账户余额</span>
									<span class="balance" id="balance"> 
										<span id="bankBalance"><fmt:formatNumber value="${userBalance}" pattern="#,##0.00" /></span> 元
									</span>
									<span class="syncAccount"><span class="icon iconfont iconfont-refresh"></span></span>
								</c:if>
								<c:if test="${loginFlag eq 0}">
									<!-- 未登录 -->
									<span class="title">银行存管账户余额</span>
									<!-- 未登陆，调用登陆 -->
									<a href="${ctx}/user/login/init.do" class="balance unlogin">登录后查看</a>
								</c:if>
							</div>
							<!-- 等待发标开始-->
							<div class="hd2-item con3">
								<div class="money">
									<!-- 投标未开始 -->
									<input type="text" name="money" id="money" class="money-input" maxlength="9"
							 			placeholder="${projectDeatil.tenderAccountMin}元起投,${projectDeatil.increaseMoney}元递增"
										oncopy="return false" onpaste="return false"
										oncut="return false" oncontextmenu="return false" autocomplete="off"
										<c:if test="${projectDeatil.tenderAccountMin ge InvestAccountInt}"> value="${InvestAccountInt}"   readonly="readonly"</c:if>/>
									<input id="nid" name="nid" type="hidden" value="${projectDeatil.borrowNid}" />
									<c:if test="${loginFlag eq 1}">
										<fmt:formatNumber value="${userBalance}" maxFractionDigits="2" var="balance" />
										<fmt:formatNumber value="1" maxFractionDigits="2" var="balance1" />
										<c:if test="${balance>=balance1}">
											<!-- 已登录，则获取收益-->
											<a href="javascript:;" class="money-btn <c:if test="${projectDeatil.tenderAccountMin lt InvestAccountInt}">available</c:if>">全投</a>
										</c:if>
									</c:if>
								</div>
							</div>
							<div class="hd2-item con4 coupon-txt couponDiv">
								<c:if test="${isThereCoupon==1}">
									<span> 已选择 <span class="num" data-coupon-quota-str="${couponConfig.couponQuotaStr }">${couponConfig.couponQuotaStr }
										<c:if test="${couponConfig.couponType == 1}"> 元  </c:if> 
										<c:if test="${couponConfig.couponType == 2}"> % </c:if>
									    <c:if test="${couponConfig.couponType == 3}"> 元  </c:if>
										</span> 
										<c:if test="${couponConfig.couponType == 1}"> 体验金 </c:if> 
										<c:if test="${couponConfig.couponType == 2}"> 加息券 </c:if> 
										<c:if test="${couponConfig.couponType == 3}"> 代金券 </c:if>
									</span>
									<a href="javascript:;" class="use-coupon highlight" id="useCoupon">重新选择</a>
								</c:if>
								<c:if test="${isThereCoupon==0}">
									<!-- 是vip -->
										<span> 您有 <span class="num">${couponAvailableCount}</span> 张优惠券可用 </span>
										<a href="javascript:;" class="use-coupon highlight" id="useCoupon">选择优惠券</a>
								</c:if>

								<input type="hidden" name="couponGrantId" id="couponGrantId" <c:if test="${isThereCoupon==1}"> value="${couponConfig.userCouponId}"</c:if>/>
								<input type="hidden" name="couponValue" id="couponValue" <c:if test="${isThereCoupon==1}"> value="${couponConfig.couponQuotaStr}"</c:if>/>
								<input type="hidden" name="couponType" id="couponType" data-couponinterest="0" data-confirmcouponincome="${couponConfig.couponInterest}"
									data-confirmcouponarea="${couponConfig.tenderQuotaRange }" data-confirmcoupon="${couponConfig.couponDescribe}" <c:if test="${isThereCoupon==1}"> value="${couponConfig.couponType}"</c:if>/>
							</div>
							<div class="hd2-item con4">
								历史回报： <span class="highlight" id="income"> 0.00</span> 元
							</div>
							<div class="hd2-item con4">
								<div class="appoint-term">
									<div class="checkicon avaliable checked">
										<input type="checkbox" name="termcheck" id="termcheck" checked="checked" />
									</div>
										<div class="terms-box">
											<c:if test="${projectDeatil.type ne 13}">
												<a href="#" onclick="openNew('${ctx}/agreement/goAgreementPdf.do?aliasName=jjfwjkxy')" class="highlight">${jjfwjkxy }</a>
											    <a href="#" onclick="openNew('${ctx}/agreement/goAgreementPdf.do?aliasName=tzfxqrs')" class="highlight">${tzfxqrs }</a>
										    </c:if>
					                        <c:if test="${projectDeatil.type eq 13}">
				                              	<a href="#" onclick="openNew('${ctx}/user/regist/goDetail.do?type=rtb-open-contract&borrowNid=${projectDeatil.borrowNid }')" class="highlight">《产品协议》</a> 
												<a href="#" onclick="openNew('${ctx}/user/regist/goDetail.do?type=rtb-contract&borrowNid=${projectDeatil.borrowNid }')" class="highlight">《平台协议》</a> 
			                              	</c:if>
										</div>
								</div>
							</div>
							<div class="hd2-item con5">
								<c:if test="${projectDeatil.time < nowTime}">
									<!-- 投标未开始 (且当前时间大于应发标时间)-->
									<span href="#" class="confirm-btn disabled">等待投标</span>
								</c:if>
								<c:if test="${projectDeatil.time >= nowTime}">
									<!-- 投标未开始 -->
									<span href="#" class="confirm-btn disabled">${projectDeatil.onTime} 开标</span>
								</c:if>
							</div>
							<!-- 等待发标结束-->
					</c:if>
					<!-- 投标 -->
					<c:if test="${projectDeatil.status eq 11}">
						<input type="hidden" id="projectData" data-total="${projectDeatil.investAccount}" data-tendermax="${projectDeatil.tenderAccountMax}" data-tendermin="${projectDeatil.tenderAccountMin}"/>
						<input type="hidden" id="userData" data-balance="${userBalance}" data-openflag="${openFlag}"/>
						<form action="${ctx}/bank/web/user/tender/invest.do" id="detailForm">
							<input type="hidden" name="tokenCheck" id="tokenCheck" value="" />
							<input type="hidden" name="tokenGrant" id="tokenGrant" value="${tokenGrant}" /> 
							<input type="hidden" id="nid" name="nid" value="${projectDeatil.borrowNid}" />
							<div class="hd2-item con1">
								<span class="title">可投金额</span>
								<a href="${ctx}/bank/web/user/recharge/rechargePage.do" class="recharge banrecharge" onclick="setCookie()">充值</a>
							    <span class="total"> 
									<fmt:formatNumber value="${projectDeatil.investAccount}" pattern="#,###" /> <span>元</span>
								</span>
							</div>
							<div class="hd2-item con2">
								<c:if test="${loginFlag eq 1}">
									<!-- 投标开始 -->
									<span class="title">银行存管账户余额</span>
									<span class="balance" id="balance"> 
										<span id="bankBalance"><fmt:formatNumber value="${userBalance}" pattern="#,##0.00" /></span> 元
									</span>
									<span class="syncAccount"><span class="icon iconfont iconfont-refresh"></span></span>
								</c:if>
								<c:if test="${loginFlag eq 0}">
									<!-- 未登录 -->
									<span class="title">银行存管账户余额</span>
									<!-- 未登陆，调用登陆 -->
									<a href="${ctx}/user/login/init.do" class="balance unlogin">登录后查看</a>
								</c:if>
							</div>
							<div class="hd2-item con3">
								<div class="money">
									<input type="text" name="money" id="money" class="money-input" maxlength="9"
									    placeholder="${projectDeatil.tenderAccountMin}元起投,${projectDeatil.increaseMoney}元递增"
										oncopy="return false" onpaste="return false"
										oncut="return false" oncontextmenu="return false" autocomplete="off"
										<c:if test="${projectDeatil.tenderAccountMin ge InvestAccountInt}"> value="${InvestAccountInt}" readonly="readonly" </c:if>/>
									<c:if test="${loginFlag eq 1}">
										<fmt:formatNumber value="${userBalance}" maxFractionDigits="2" var="balance" />
										<fmt:formatNumber value="1" maxFractionDigits="2" var="balance1" />
										<c:if test="${balance>=balance1}">
											<!-- 已登录，则获取收益-->
											<a href="javascript:;" class="money-btn <c:if test="${projectDeatil.tenderAccountMin lt InvestAccountInt}">available</c:if>">全投</a>
										</c:if>
									</c:if>
								</div>
							</div>
							<div class="hd2-item con4 coupon-txt couponDiv">
								<c:if test="${isThereCoupon==1}">
									<span> 已选择 <span class="num" data-coupon-quota-str="${couponConfig.couponQuotaStr }">${couponConfig.couponQuotaStr }
											<c:if test="${couponConfig.couponType == 1}"> 元  </c:if> 
											<c:if test="${couponConfig.couponType == 2}"> % </c:if> 
											<c:if test="${couponConfig.couponType == 3}"> 元  </c:if>
											</span> 
											<c:if test="${couponConfig.couponType == 1}"> 体验金 </c:if> 
											<c:if test="${couponConfig.couponType == 2}"> 加息券 </c:if> 
											<c:if test="${couponConfig.couponType == 3}"> 代金券 </c:if>
									</span>
									<a href="javascript:;" class="use-coupon highlight" id="useCoupon">重新选择</a>
								</c:if>
								<c:if test="${isThereCoupon==0}">
									<span> 您有 <span class="num">${couponAvailableCount}</span> 张优惠券可用 </span>
									<a href="javascript:;" class="use-coupon highlight" id="useCoupon">选择优惠券</a>
								</c:if>
								<input type="hidden" name="couponGrantId" id="couponGrantId" <c:if test="${isThereCoupon==1}"> value="${couponConfig.userCouponId}"</c:if>/>
								<input type="hidden" name="couponValue" id="couponValue" <c:if test="${isThereCoupon==1}"> value="${couponConfig.couponQuotaStr}"</c:if>/>
								<input type="hidden" name="couponType" id="couponType"
									data-couponinterest="0"
									data-confirmcouponincome="${couponConfig.couponInterest}"
									data-confirmcouponarea="${couponConfig.tenderQuotaRange }"
									data-confirmcoupon="${couponConfig.couponDescribe}"
									<c:if test="${isThereCoupon==1}"> value="${couponConfig.couponType}"</c:if>/>
							</div>
							<div class="hd2-item con4">
								历史回报： <span class="highlight" id="income"> 0.00</span> 元
							</div>
							<div class="hd2-item con4">
								<div class="appoint-term">
									<div class="checkicon avaliable checked">
										<input type="checkbox" name="termcheck" id="termcheck" checked="checked" />
									</div>
									<div class="terms-box">
										<c:if test="${projectDeatil.type ne 13}">
											<a href="#" onclick="openNew('${ctx}/agreement/goAgreementPdf.do?aliasName=jjfwjkxy')" class="highlight">${ jjfwjkxy}</a>
										    <a href="#" onclick="openNew('${ctx}/agreement/goAgreementPdf.do?aliasName=tzfxqrs')" class="highlight">${tzfxqrs }</a>
									    </c:if>
				                        <c:if test="${projectDeatil.type eq 13}">
			                              	<a href="#" onclick="openNew('${ctx}/user/regist/goDetail.do?type=rtb-open-contract&borrowNid=${projectDeatil.borrowNid }')" class="highlight">《产品协议》</a> 
											<a href="#" onclick="openNew('${ctx}/user/regist/goDetail.do?type=rtb-contract&borrowNid=${projectDeatil.borrowNid }')" class="highlight">《平台协议》</a> 
		                              	</c:if>
									</div>
								</div>
							</div>
							<div class="hd2-item con5">
								<!-- 投标开始 -->
								<a href="javascript:;" class="confirm-btn avaliable">确认投资</a>
							</div>
						</form>
					</c:if>
					<!-- 复审中 -->
					<c:if test="${projectDeatil.status eq 12}">
						<div class="hd2-repayment">
							<c:if test="${empty projectDeatil.fullTime}">
								<div class="time">${projectDeatil.endTime}</div>
								<div class="time-title">项目结束时间</div>
							</c:if>
							<c:if test="${not empty projectDeatil.fullTime}">
								<div class="time">${projectDeatil.fullTime}</div>
								<div class="time-title">满标时间</div>
							</c:if>
							<div class="hd2-icon"></div>
						</div>
					</c:if>
					<!-- 还款中 -->
					<c:if test="${projectDeatil.status eq 13}">
						<div class="hd2-rechecking">
							<c:if test="${empty projectDeatil.fullTime}">
								<div class="time">${projectDeatil.endTime}</div>
								<div class="time-title">项目结束时间</div>
							</c:if>
							<c:if test="${not empty projectDeatil.fullTime}">
								<div class="time">${projectDeatil.fullTime}</div>
								<div class="time-title">满标时间</div>
							</c:if>
							<div class="hd2-icon"></div>
						</div>
					</c:if>
				</div>
				<div class="hd3">
					<div class="infor">
						<c:if test="${projectDeatil.type eq 13}">项目来源：${projectDeatil.borrowProjectSource}</c:if>
						<c:if test="${projectDeatil.type ne 13}"><span class="icon-safe"></span> 安全保障计划</c:if>
					</div>
					<div class="infor">项目历史回报： <span class="highlight"> ¥<fmt:formatNumber value="${projectDeatil.borrowInterest}" pattern="#,##0.00" /></span>元 </div>
					<div class="infor">还款方式：${projectDeatil.repayStyle}</div>
					<div class="infor">发标时间：${projectDeatil.sendTime}</div>
					<div class="infor infor-font">温馨提示：市场有风险，投资需谨慎</div>
					<div class="infor infor-font">建议出借者类型：稳健型及以上</div>
					<c:if test="${projectDeatil.type eq 11 and (projectDeatil.bookingStatus eq 0 or projectDeatil.bookingStatus eq 1 or projectDeatil.bookingStatus eq 2 ) and ( projectDeatil.bookingBeginTime ne 0 and projectDeatil.bookingEndTime ne 0)}">
						<div class="infor" id="time"></div>
					</c:if>
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
						<!-- 已登录 -->
						<c:if test="${loginFlag eq 1}">
							<c:if test="${projectDeatil.status eq 10 or projectDeatil.status eq 11 or projectDeatil.status eq 12 }">
								<li panel="2">相关文件</li>
							</c:if>
						</c:if>
						<c:if test="${projectDeatil.status eq 13}">
							<li panel="2">相关文件</li>
						</c:if>
						<!-- 汇消费 -->
						<c:if test="${projectDeatil.type eq 8}">
							<li panel="3">债权信息</li>
						</c:if>
						<li panel="4">还款计划</li>
					</c:if>
					<li panel="5">投资记录</li>
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
										<img alt="项目流程图" src="${ctx}/img/project/hjs_flow.png" />
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
		                            		<td align="center">出借条件</td>
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
		                            	<%-- <tr>
		                            		<td align="center">合同协议</td>
											<td>
												<a href="#"onclick="openNew('${ctx}/user/regist/goDetail.do?type=rtb_contract')" class="highlight">《产品认购协议》</a>
												<a href="#"onclick="openNew('${ctx}/user/regist/goDetail.do?type=rtb_instructions')" class="highlight">《产品说明书》</a>
											</td>
										</tr> --%>
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
											<img alt="项目流程图" src="${ctx}/img/00-01.png">
										</ul>
									</dd>
								</dl>
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<li>所在地区：${borrowInfo.borrowAddress}</li>
											<li>注册资本：<fmt:formatNumber value="${borrowInfo.regCaptial}" pattern="#,###" />元 </li>
											<li>注册时间：${borrowInfo.registTime}</li>
											<c:if test="${ not empty projectDeatil.borrowLevel and projectDeatil.borrowLevel ne '' }">
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" class="levelimg" src="${ctx}/img/${projectDeatil.borrowLevel}.png"></li>
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
											<img alt="项目流程图" src="${ctx}/img/00-01.png">
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
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" class="levelimg" src="${ctx}/img/${projectDeatil.borrowLevel}.png"></li>
											</c:if>
										</ul>
									</dd>
								</dl>
								<c:if test="${borrowInfo.borrowContents ne null and borrowInfo.borrowContents ne '' }">
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
						
						<!-- 汇车贷、  实鑫车  --> 
						<c:if test="${projectDeatil.type eq 3 || projectDeatil.type eq 12}">
							<c:if test="${projectDeatil.comOrPer eq 1}">
								<!--  comOrPer 项目是个人项目还是企业项目 1企业 2个人  -->
								<dl class="new-detail-dl">
									<dt>项目流程</dt>
									<dd>
										<ul>
											<img alt="项目流程图" src="${ctx}/img/00-01.png">
										</ul>
									</dd>
								</dl>
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<li>所在地区：${borrowInfo.borrowAddress}</li>
											<li>注册资本：<fmt:formatNumber value="${borrowInfo.regCaptial}" pattern="#,###" />元</li>
											<li>注册时间：${borrowInfo.registTime}</li>
											<c:if test="${ not empty projectDeatil.borrowLevel and projectDeatil.borrowLevel ne '' }">
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" class="levelimg" src="${ctx}/img/${projectDeatil.borrowLevel}.png"></li>
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
											<img alt="项目流程图" src="${ctx}/img/00-01.png">
										</ul>
									</dd>
								</dl>
								<dl class="new-detail-dl">
									<dt></dt>
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
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" class="levelimg" src="${ctx}/img/${projectDeatil.borrowLevel}.png"></li>
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
															<td><span class="highlight"> ￥<fmt:formatNumber value="${vehiclePledge.evaluationPrice}" pattern="#,###.00" /></span></td>
														</tr>
													</c:forEach>
												</tbody>
											</table>
										</dd>
									</dl>
								</c:if>
								<c:if test="${borrowInfo.borrowContents ne null and borrowInfo.borrowContents ne '' }">
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
											<img alt="项目流程图" src="${ctx}/img/00-01.png">
										</ul>
									</dd>
								</dl>
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<li>所在地区：${borrowInfo.borrowAddress}</li>
											<li>注册资本：<fmt:formatNumber value="${borrowInfo.regCaptial}" pattern="#,###" />元 </li>
											<li>注册时间：${borrowInfo.registTime}</li>
											<c:if test="${ not empty projectDeatil.borrowLevel and projectDeatil.borrowLevel ne '' }">
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" class="levelimg" src="${ctx}/img/${projectDeatil.borrowLevel}.png"></li>
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
											<img alt="项目流程图" src="${ctx}/img/00-01.png">
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
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" class="levelimg" src="${ctx}/img/${projectDeatil.borrowLevel}.png"></li>
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
							<c:if test="${not empty vehiclePledgeList and vehiclePledgeList ne null}">
								<dl class="new-detail-dl">
									<dt>车辆信息</dt>
									<dd>
										<table class="new-detail-tb">
											<thead>
												<tr>
													<th width="270">车辆品牌</th>
													<th width="270">型号</th>
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
														<td><span class="highlight"> ￥<fmt:formatNumber value="${vehiclePledge.evaluationPrice}" pattern="#,###.00" /></span></td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</dd>
								</dl>
							</c:if>

							<c:if test="${!empty riskControl.controlMeasures and riskControl.controlMeasures ne '' and riskControl.controlMeasures ne null}">
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
											<img alt="项目流程图" src="${ctx}/img/00-01.png">
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
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" class="levelimg" src="${ctx}/img/${projectDeatil.borrowLevel}.png"></li>
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
											<img alt="项目流程图" src="${ctx}/img/00-01.png">
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
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" class="levelimg" src="${ctx}/img/${projectDeatil.borrowLevel}.png"></li>
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
							<c:if test="${not empty vehiclePledgeList and vehiclePledgeList ne null}">
								<dl class="new-detail-dl">
									<dt>车辆信息</dt>
									<dd>
										<table class="new-detail-tb">
											<thead>
												<tr>
													<th width="270">车辆品牌</th>
													<th width="270">型号</th>
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
														<td><span class="highlight"> ￥<fmt:formatNumber value="${vehiclePledge.evaluationPrice}" pattern="#,###.00" /></span></td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</dd>
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
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" class="levelimg" src="${ctx}/img/${projectDeatil.borrowLevel}.png"></li>
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
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" class="levelimg" src="${ctx}/img/${projectDeatil.borrowLevel}.png"></li>
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
											<img alt="项目流程图" src="${ctx}/img/00-02.png">
										</ul>
									</dd>
								</dl>
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<li>所在地区：${borrowInfo.borrowAddress}</li>
											<li>注册资本：<fmt:formatNumber value="${borrowInfo.regCaptial}" pattern="#,###" />元 </li>
											<li>注册时间：${borrowInfo.registTime}</li>
													<c:if test="${ not empty projectDeatil.borrowLevel and projectDeatil.borrowLevel ne '' }">
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" class="levelimg" src="${ctx}/img/${projectDeatil.borrowLevel}.png"></li>
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
											<img alt="项目流程图" src="${ctx}/img/00-01.png">
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
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" class="levelimg" src="${ctx}/img/${projectDeatil.borrowLevel}.png"></li>
											</c:if>
										</ul>
									</dd>
								</dl>
							</c:if>
						</c:if> 
						
						<!-- 汇房贷 --> 
						<c:if test="${projectDeatil.type eq 7}">
							<c:if test="${projectDeatil.comOrPer eq 1}">
								<!--  comOrPer 项目是个人项目还是企业项目 1企业 2个人  -->
								<dl class="new-detail-dl">
									<dt>项目流程</dt>
									<dd>
										<ul>
											<img alt="项目流程图" src="${ctx}/img/00-01.png">
										</ul>
									</dd>
								</dl>
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<li>所在地区：${borrowInfo.borrowAddress}</li>
											<li>所属行业：${borrowInfo.borrowIndustry}</li>
											<li>注册资本：<fmt:formatNumber value="${borrowInfo.regCaptial}" pattern="#,###" />元 </li>
											<li>注册时间：${borrowInfo.registTime}</li>
													<c:if test="${ not empty projectDeatil.borrowLevel and projectDeatil.borrowLevel ne '' }">
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" class="levelimg" src="${ctx}/img/${projectDeatil.borrowLevel}.png"></li>
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
											<img alt="项目流程图" src="${ctx}/img/00-01.png">
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
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" class="levelimg" src="${ctx}/img/${projectDeatil.borrowLevel}.png"></li>
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
							<dl class="new-detail-dl">
								<dt>风控措施</dt>
								<dd>
									<ul>
										<li>${riskControl.controlMeasures }</li>
									</ul>
								</dd>
							</dl>
						</c:if> 
						
						<!-- 员工贷--> 
						<c:if test="${projectDeatil.type eq 14}">
							<c:if test="${projectDeatil.comOrPer eq 1}">
								<!--  comOrPer 项目是个人项目还是企业项目 1企业 2个人  -->
								<dl class="new-detail-dl">
									<dt>基础信息</dt>
									<dd>
										<ul>
											<li>所在地区：${borrowInfo.borrowAddress}</li>
											<li>所属行业：${borrowInfo.borrowIndustry}</li>
											<li>注册资本：<fmt:formatNumber value="${borrowInfo.regCaptial}" pattern="#,###" />元 </li>
											<li>注册时间：${borrowInfo.registTime}</li>
													<c:if test="${ not empty projectDeatil.borrowLevel and projectDeatil.borrowLevel ne '' }">
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" class="levelimg" src="${ctx}/img/${projectDeatil.borrowLevel}.png"></li>
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
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" class="levelimg" src="${ctx}/img/${projectDeatil.borrowLevel}.png"></li>
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
										<li>注册资本：<fmt:formatNumber value="${borrowInfo.regCaptial}" pattern="#,###" />元 </li>
										<li>注册时间：${borrowInfo.registTime}</li>
												<c:if test="${ not empty projectDeatil.borrowLevel and projectDeatil.borrowLevel ne '' }">
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" class="levelimg" src="${ctx}/img/${projectDeatil.borrowLevel}.png"></li>
											</c:if>
									</ul>
								</dd>
							</dl>
							<c:if test="${borrowInfo.borrowContents ne null and borrowInfo.borrowContents ne '' }">
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
							</c:if>
						</c:if> 
						
						<!-- 汇资产 --> 
						<c:if test="${projectDeatil.type eq 9}">
							<dl class="new-detail-dl">
								<dt>项目流程</dt>
								<dd>
									<ul>
										<img alt="项目流程图" src="${ctx}/img/00-01.png">
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
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" class="levelimg" src="${ctx}/img/${projectDeatil.borrowLevel}.png"></li>
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
											<li>注册资本：<fmt:formatNumber value="${borrowInfo.regCaptial}" pattern="#,###" />元 </li>
											<li>注册时间：${borrowInfo.registTime}</li>
													<c:if test="${ not empty projectDeatil.borrowLevel and projectDeatil.borrowLevel ne '' }">
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" class="levelimg" src="${ctx}/img/${projectDeatil.borrowLevel}.png"></li>
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
												<li>信用评级：<img alt="${projectDeatil.borrowLevel}" class="levelimg" src="${ctx}/img/${projectDeatil.borrowLevel}.png"></li>
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
						</c:if>
					</li>
					<c:if test="${projectDeatil.status eq 10 or projectDeatil.status eq 11 or projectDeatil.status eq 12 }">
						<c:if test="${loginFlag eq 1}">
							<!-- 汇保贷 -->
							<c:if test="${projectDeatil.type eq 0}">
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
							<!-- 汇典贷 -->
							<c:if test="${projectDeatil.type eq 1}">
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
							<!-- 汇小贷 -->
							<c:if test="${projectDeatil.type eq 2}">
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
									</dl> 
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
									</dl> 
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
									</dl>
									<dl class="new-detail-dl">
										<dt>风控措施</dt>
										<dd>${riskControl.controlMeasures}</dd>
									</dl>
								</li>
							</c:if>
							<!-- 汇小贷 -->
							<c:if test="${projectDeatil.type eq 2}">
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
									</dl> 
									<dl class="new-detail-dl">
										<dt>风控措施</dt>
										<dd>${riskControl.controlMeasures}</dd>
									</dl>
								</li>
							</c:if>
							<!-- 汇租赁 -->
							<c:if test="${projectDeatil.type eq 5}">
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
									</dl> 
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
												<li><a href="${file.fileUrl}" data-caption="${file.fileName}">
													<div> <img src="${file.fileUrl}" alt=""> </div>
													<span class="title">${file.fileName}</span>
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
												<li><a href="${file.fileUrl}" data-caption="${file.fileName}">
													<div><img src="${file.fileUrl}" alt=""></div>
												    <span class="title">${file.fileName}</span>
												</a></li>
											</c:forEach>
										</ul>
									</div>
								</div>
							</li>
						</c:if>
						
						<c:if test="${investFlag ne 1}">
							<li panel="2">
								<div class="new-detail-imgs">
									<div class="new-detail-img-t">相关资料</div>
									<div class="new-detail-img-c">
										<span>只有投资本项目的用户可见详细信息</span>
									</div>
								</div>
							</li>
						</c:if>
					</c:if>
					<c:if test="${projectDeatil.type eq 8}">
						<li panel="3">
							<table class="new-detail-tb">
								<thead id="projectConsumeListHead"></thead>
								<tbody id="projectConsumeList"></tbody>
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
										<td><span class="highlight">￥<fmt:formatNumber value="${repayPlan.repayTotal}" pattern="#,###.00" /></span></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</li>
					<li panel="5">
						<div class="new-detail-tb-t">
							<span>总投标金额 <strong id="investTotal">90,127.00</strong> 元
							</span> <span>出借人次 <strong id="investTimes">10</strong> 人次
							</span>
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
								<dl>
									<dt class="iconfont iconfont-gotop" data-id="">
										<span class="title">6. 投资人风险测评目的和规则是什么？</span>
									</dt>
									<dd>
										<p>为完善对投资者风险承受能力的尽职评估，实现对投资者的等级管理，保障投资者购买合适的产品，根据投资者风险测评的结果，将投资者风险承受能力由低到高分为保守型、稳健型、成长型、进取型四种类型。</p>
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

	<div class="pop-overlayer"></div>
	<div class="pop-box pop-tips-hastitle coupon-pop" id="couponPop">
		<a class="pop-close" href="javascript:void(0);" onclick="popOutWin()"></a>
		<h4 class="pop-title">请选择优惠券</h4>
		<div class="pop-main">
			<div class="pop-txt">
				<div class="coupon-pop-box">
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
						<td>项目编号： ${projectDeatil.type == 13 ? projectDeatil.borrowAssetNumber : projectDeatil.borrowNid}  </td>
						<td>历史年回报率：${projectDeatil.borrowApr}%<c:if test="${projectDeatil.borrowExtraYield ne '0.00'}"> + ${projectDeatil.borrowExtraYield}%</c:if></td>
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

	<c:choose>
		<c:when test="${projectDeatil.type eq 4}">
			<script>
				setActById('hdXSH');
			</script>
		</c:when>
		<c:when test="${projectDeatil.type eq 11}">
			<script>
				setActById('hdZXH');
			</script>
		</c:when>
		<c:when test="${projectDeatil.type eq 13}">
			<script>
				setActById('hdHJS');
			</script>
		</c:when>
		<c:otherwise>
			<script>
				setActById('hdCFH');
			</script>
		</c:otherwise>
	</c:choose>
	<c:if test="${projectDeatil.projectType eq 'HZT' and projectDeatil.type ne 4 and projectDeatil.type ne 11}">
		<script>
			projectType = "${projectDeatil.projectType}";
		</script>
	</c:if>

	<%@ include file="/footer.jsp"%>
	<script type="text/javascript" src="${cdn}/js/jquery.validate.js?version=${version}" charset="utf-8"></script>
	<script type="text/javascript" src="${cdn}/js/messages_cn.js?version=${version}" charset="utf-8"></script>
	<script type="text/javascript" src="${cdn}/js/jquery.metadata.js?version=${version}" charset="utf-8"></script>
	<script type="text/javascript" src="${cdn}/js/baguetteBox.min.js?version=${version}" charset="utf-8"></script>
	<script type="text/javascript" src="${cdn}/js/jquery.cookie.min.js?version=${version}" charset="utf-8"></script>
	<script type="text/javascript" src="${cdn}/js/bank/borrow/borrowdetail.js?version=${version}"></script>
	

	<script>
		<c:if test="${projectDeatil.tenderAccountMin ge InvestAccountInt and projectDeatil.status eq 11}">
		priceChanged($("#money"));
		</c:if>

		$(function() {
			$(".jobs-list dl dt").click(function() {
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