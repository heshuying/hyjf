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
            <div class="left-side">您所在的位置： <a href="${ctx}/">首页</a> &gt;<a href="${ctx}/bank/user/credit/initWebCreditPage.do">债权转让</a> &gt; 项目详情</div>
        </div>
    </section>
    <article class="main-content product">
        <div class="container">
            <!-- start 内容区域 -->
            <div class="product-intr">
                <div class="title">
                    <span>HZR${creditDetail.creditNid} </span>
                    <div class="contract-box">
                        <a href="${ctx}/agreement/goAgreementPdf.do?aliasName=zqzrxy" target="_blank">${zqzrxy }</a>
                        <a href="${ctx}/agreement/goAgreementPdf.do?aliasName=fxqrs" target="_blank">${ fxqrs}</a>
                    </div>
                    <input type="hidden" name="paymentAuthStatus" id="paymentAuthStatus" value="${paymentAuthStatus}" /><!-- 自动缴费状态 -->
                	<input type="hidden" name="userBalance" id="userBalance" value="${balance}" />
                	<input type="hidden" name="loginFlag" id="loginFlag" value="${loginFlag }" /> <!-- 登录状态 0未登陆 1已登录 -->
                	<input type="hidden" name="openFlag" id="openFlag" value="${openFlag }" /> <!-- 开户状态 0未开户 1已开户 -->
                	<input type="hidden" name="riskFlag" id="riskFlag" value="${riskFlag }" /> <!-- 是否进行过风险测评 0未测评 1已测评 -->
                	<input type="hidden" name="setPwdFlag" id="setPwdFlag" value="${setPwdFlag }" /> <!-- 是否设置过交易密码 0未设置 1已设置 -->
                	<input type="hidden" name="isUserValid" id="isUserValid" value="${isUserValid }" /> <!-- 是否禁用 1 禁用  0 未禁用 -->
                	<input type="hidden" name="increase" id="increase" value="1" /> <!-- 递增金额 -->
                	<input type="hidden" name="isLast" id="isLast" value="0" /> <!-- 是否最后一笔投资 -->
                	<input type="hidden" id="projectData" data-total="${creditDetail.creditAssignCapital}" data-tendermax="${creditDetail.creditAssignCapital}" data-tendermin="1"/>
                   <%-- 	<span>${projectDeatil.projectName}</span>  --%>
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
                    <div class="list-item"><span class="dark">建议投资者类型：稳健型及以上</span></div>
                </div>
            </div>
            <form class="product-form transfer" action="${ctx}/bank/user/credit/websubmitcredittenderassign.do" id="productForm" autocomplete="off">
				<input type="hidden" id="borrowNid" name="borrowNid" value="${projectDeatil.borrowNid}">
                <input type="hidden" id="creditNid" name="creditNid" value="${creditDetail.creditNid}">
                <input type="hidden" name="tokenCheck" id="tokenCheck" value="" />
				<input type="hidden" name="tokenGrant" id="tokenGrant" value="${tokenGrant}" /> 
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
                    <!-- 用户未登录 -->
                    <c:if test="${loginFlag eq 0}">
	                    <div class="val">登录后可见</div>
	                    <a href="${ctx}/user/login/init.do"  class="link-recharge" onclick="setCookie()">立即登录</a>
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
                <input type="checkbox" name="termcheck" class="form-term-checkbox" id="productTerm" checked="">
                <div class="dialog dialog-alert" id="confirmDialog">
                    <div class="title">投资确认</div>
                    <div class="content">
                        <table class="product-confirm-table" cellspacing="0" cellpadding="0">
                            <tr>
                                <td width="140" align="right"><span class="dark">历史年回报率：</span></td>
                                <td><span>${creditDetail.borrowApr}</span><span class="unit"> %</span></td>
                            </tr>
                            <tr>
                                <td align="right"><span class="dark">折让率：</span></td>
                                <td><span>${creditDetail.creditDiscount}<span class="unit"> %</span></span></td>
                            </tr>
                            <tr>
                                <td align="right"><span class="dark">认购本金：</span></td>
                                <td><span class="red" id="assign_capital_confirm"></span></td>
                            </tr>
                            <tr>
                                <td align="right"><span class="dark">剩余期限：</span></td>
                                <td>${creditDetail.creditTerm}<span class="unit"> 天</span></td>
                            </tr>
                            <tr>
                                <td align="right"><span class="dark">垫付利息</span></td>
                                <td id="assign_interest_advance_confirm"></td>
                            </tr>
                            <tr>
                                <td align="right"><span class="dark">计息方式：</span></td>
                                <td id="assign_interest_advance_confirm" >${projectDeatil.repayStyle}</td>
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
                            <p class="highlight">注：实际支付金额=认购本金*（1-折让率）+垫付利息</p>
                            <div class="term-checkbox checked"></div>
                            <span>我已同意并阅读  <a href="${ctx}/agreement/goAgreementPdf.do?aliasName=zqzrxy" target="_blank">${zqzrxy }</a><a href="${ctx}/agreement/goAgreementPdf.do?aliasName=tzfxqrs" target="_blank">${tzfxqrs }</a></span>
                        </div>
                        <div style="padding-left: 50px; color: #fc0920;font-size: 12px;margin-top: 2px;"><img src="${cdn}/dist/images/icon-star.png"  alt="" style="width:10px;margin-left:2px;height: 10px;margin-right:8px;position: relative;top: -13px;"/><span style="display:inline-block">您应在中国大陆操作，因在境外操作导致的后果将由您<br>独自承担。</span></div>
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
						<li panel="3"><a href="javascript:;">投资记录</a></li>
						<li panel="4"><a href="javascript:;">常见问题</a></li>
                    </ul>
                    <ul class="tab-panels">
                        <li panel="0" class="active">
							<!-- 登录状态 -->
                            <c:if test="${loginFlag eq '1' }">
	                            <div class="attr-title"><span>债转说明</span></div>
	                            <div class="attr-note" style="color:#7F7F80;">
									此项目为“汇直投”债权转让服务。 
									债权转让达成后，债权拥有者将变更为新投资人，担保公司将继续对借款人的借款承担连带担保责任。                  
								</div>
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
			                                    	<span class="key">历史年回报率</span>
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
					                                	<c:if test="${!empty orrowInfo.borrowAddress}">
						                                    <td width="50%"> 
						                                    	<span class="key">所在地区</span>
						                                    	<span class="val">${borrowInfo.borrowAddress}</span>
						                                    </td>
					                                    </c:if>
					                                    <c:if test="${!empty borrowInfo.regCaptial}">
						                                    <td width="50%">
						                                    	<span class="key">注册资本</span>
						                                    	<span class="val"><fmt:formatNumber value="${borrowInfo.regCaptial}" pattern="#,###" />元</span>
						                                    </td>
					                                    </c:if>
					                                </tr>
					                            	<tr>
					                            		<c:if test="${!empty borrowInfo.registTime}">
						                                    <td  width="50%">
						                                    	<span class="key">注册时间</span>
						                                    	<span class="val">${borrowInfo.registTime}</span>
						                                    </td>
					                                    </c:if>
					                                    <c:if test="${!empty projectDeatil.borrowLevel}">
						                                    <td width="50%">
						                                    	<span class="key">信用评级</span>
						                                    	<span class="val">${projectDeatil.borrowLevel}</span>
						                                    </td>	
					                                    </c:if>	                                    
					                            	</tr>
					                            	<c:if test="${projectDeatil.type eq 11 || projectDeatil.type eq 5 || projectDeatil.type eq 7 
					                            		|| projectDeatil.type eq 14 || projectDeatil.type eq 10 }"> 
					                            		<c:if test="${!empty borrowInfo.borrowIndustry}">
							                            	<tr>
							                                    <td  width="50%">
							                                    	<span class="key">所属行业</span>
							                                    	<span class="val">${borrowInfo.borrowIndustry}</span>
							                                    </td>
							                            	</tr>
						                            	</c:if>
					                            	</c:if>
				                         	  	 </tbody>
					                       		 </table>
				                       		 </c:if>
				                       		<c:if test="${projectDeatil.comOrPer eq 2}"><!--  comOrPer 项目是个人项目还是企业项目 1企业 2个人  -->
												<table cellspacing="0" cellpadding="0">
				                          	  	<tbody>
					                                <tr>
					                                	<c:if test="${!empty borrowInfo.sex}">
						                                    <td width="50%">
						                                    	<span class="key">性别</span>
						                                    	<span class="val">${borrowInfo.sex}</span>
						                                    </td>
					                                    </c:if>
					                                    <c:if test="${!empty borrowInfo.age}">
						                                    <td width="50%">
						                                    	<span class="key">年龄</span>
						                                    	<span class="val">${borrowInfo.age}</span>
						                                    </td>
					                                    </c:if>
					                                </tr>
					                            	<tr>
					                            		<c:if test="${!empty borrowInfo.maritalStatus}">
						                                    <td  width="50%">
						                                    	<span class="key">婚姻状况</span>
						                                    	<span class="val">${borrowInfo.maritalStatus}</span>
						                                    </td>
					                                    </c:if>
					                                    <c:if test="${!empty borrowInfo.workingCity}">
						                                    <td width="50%">
						                                    	<span class="key">工作城市</span>
						                                    	<span class="val">${borrowInfo.workingCity}</span>
						                                    </td>		  
					                                    </c:if>                                  
					                            	</tr>
					                            	<tr>
					                            		<c:if test="${!empty borrowInfo.position}">
						                                    <td  width="50%">
						                                    	<span class="key">岗位职业</span>
						                                    	<span class="val">${borrowInfo.position}</span>
						                                    </td>
					                                    </c:if>
					                                    <c:if test="${!empty projectDeatil.borrowLevel}">
						                                    <td width="50%">
						                                    	<span class="key">信用评级</span>
						                                    	<span class="val">${projectDeatil.borrowLevel}</span>
						                                    </td>		   
					                                    </c:if>                                 
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
				                          	  		<c:forEach items="${vehiclePledgeList}" varStatus="status"  var="vehiclePledge">
				                          	  		<c:if test="${status.index gt 0}"><div class="cutline"></div></c:if>
				                          	  		<table cellspacing="0" cellpadding="0">
						                          	  	<tbody>
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
								                                </tbody>
							                        	</table>
						                             </c:forEach>
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
			                                	<c:if test="${!empty borrowInfo.borrowAddress}">
				                                    <td width="50%">
				                                    	<span class="key">所在地区</span>
				                                    	<span class="val">${borrowInfo.borrowAddress}</span>
				                                    </td>
			                                    </c:if>
			                                    <c:if test="${!empty borrowInfo.regCaptial}">
				                                    <td width="50%">
				                                    	<span class="key">注册资本</span>
				                                    	<span class="val"><fmt:formatNumber value="${borrowInfo.regCaptial}" pattern="#,###" />元</span>
				                                    </td>
			                                    </c:if>
			                                </tr>
			                            	<tr>
			                            		<c:if test="${!empty borrowInfo.registTime}">
				                                    <td  width="50%">
				                                    	<span class="key">注册时间</span>
				                                    	<span class="val">${borrowInfo.registTime}</span>
				                                    </td>
			                                    </c:if>
			                                    <c:if test="${!empty projectDeatil.borrowLevel}">
				                                    <td width="50%">
				                                    	<span class="key">信用评级</span>
				                                    	<span class="val">${projectDeatil.borrowLevel}</span>
				                                    </td>	
			                                    </c:if>	                                    
			                            	</tr>
			                            	<tr>
			                            		<c:if test="${!empty borrowInfo.borrowIndustry}">
				                                    <td  width="50%">
				                                    	<span class="key">所属行业</span>
				                                    	<span class="val">${borrowInfo.borrowIndustry}</span>
				                                    </td>
			                                    </c:if>
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
			                                	<c:if test="${!empty borrowInfo.borrowName}">
				                                    <td width="50%">
				                                    	<span class="key">项目名称</span>
				                                    	<span class="val">${borrowInfo.borrowName}</span>
				                                    </td>
			                                    </c:if>
			                                    <c:if test="${!empty borrowInfo.borrowType}">
				                                    <td width="50%">
				                                    	<span class="key">项目类型</span>
				                                    	<span class="val">${borrowInfo.borrowType}</span>
				                                    </td>
			                                    </c:if>
			                                </tr>
			                            	<tr>
			                            		<c:if test="${!empty borrowInfo.borrowAddress}">
				                                    <td  width="50%">
				                                    	<span class="key">所在地区</span>
				                                    	<span class="val">${borrowInfo.borrowAddress}</span>
				                                    </td>
			                                    </c:if>
			                                    <c:if test="${!empty borrowInfo.guarantyValue}">
				                                    <td width="50%">
				                                    	<span class="key">评估价值</span>
				                                    	<span class="val">${borrowInfo.guarantyValue}</span>
				                                    </td>		    
			                                    </c:if>                                
			                            	</tr>
			                            	<tr>
			                            		<c:if test="${!empty borrowInfo.assetOrigin}">
				                                    <td width="50%">
				                                    	<span class="key">资产成因</span>
				                                    	<span class="val">${borrowInfo.assetOrigin}</span>
				                                    </td>
			                                    </c:if>
			                                    <c:if test="${!empty projectDeatil.borrowLevel}">
				                                    <td  width="50%">
				                                    	<span class="key">信用评级</span>
				                                    	<span class="val">${projectDeatil.borrowLevel}</span>
				                                    </td>
			                                    </c:if>
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
                            </c:if>
							<!-- 未登录状态 -->
                            <c:if test="${loginFlag eq '0' }">                           
	                            <div class="unlogin">
	                                <div class="icon"></div>
	                                <p>请先 <a href="${ctx}/user/login/init.do">登录</a> 或 <a href="${ctx}/user/regist/init.do">注册</a> 后可查看</p>
	                            </div>
                            </c:if>
                        </li>
                        <li panel="1">
                            <!-- 相关文件 -->
                            <div class="new-detail-img-c">
	                            <!-- 登录状态 -->
	                            <c:if test="${loginFlag eq '1' }">                           
		                            <ul>
	                                	<c:forEach items="${borrowFiles}" var="borrowFiles">
		                                    <li>
		                                        <a href="${borrowFiles.fileUrl}" data-caption="${borrowFiles.fileName}">
			                                        <div><img src="${borrowFiles.fileUrl}" alt=""></div>
			                                        <span class="title">${borrowFiles.fileName}</span>
		                                        </a>
		                                    </li>
									 	 </c:forEach>
	                                </ul>
	                            </c:if>
	                            <!-- 未登录状态 -->
	                            <c:if test="${loginFlag eq '0' }">                           
		                            <div class="unlogin">
		                                <div class="icon"></div>
		                                <p>请先 <a href="${ctx}/user/login/init.do">登录</a> 或 <a href="${ctx}/user/regist/init.do">注册</a> 后可查看</p>
		                            </div>
	                            </c:if>
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
                        <li panel="3" class="">
                            <!-- 投资记录 -->
						    <p>
						    	<span id="assignTimes">承接总人次 : 0</span>&nbsp;&nbsp;
						    	<span id="assignTotal">承接金额 : <fmt:formatNumber value="0" pattern="#,#00.00"/>元</span>
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
                                    <tbody  id="projectAssignList">
                                    </tbody>
                                </table>
                                <div class="pages-nav" id="assignListPage"></div>
                            </div>
                        </li>
                        <li panel="4">
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
    		<div class="btn btn-primary single" id="authInvesPopConfirm">立即开通</div>
    	</div>
    </div>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="${cdn}/dist/js/lib/jquery.validate.js"></script>
    <script src="${cdn}/dist/js/lib/jquery.metadata.js"></script>
    <script src="${cdn}/dist/js/product/data-format.js?version=${version}"></script>
    <script src="${cdn}/dist/js/product/product-transfer-detail.js?version=${version}"></script>
</body>
</html>