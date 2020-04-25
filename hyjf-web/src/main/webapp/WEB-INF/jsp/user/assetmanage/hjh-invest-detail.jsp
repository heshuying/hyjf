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
	<article class="main-content product-zhitou" style="padding-top: 0;">
        <div class="container">
            <!-- start 内容区域 -->   
            <div class="loan-paymentdetails">
                <div class="paymentdetails-top">
                    <div class="list-fl">
                        <div class="list" style="height:210px">
                        <div class="title">
                        	<input type="hidden" id="type" name="type" value="${type}">
                        	<input type="hidden" id="accedeOrderId" name="accedeOrderId" value="${userHjhInvistDetail.accedeOrderId}">
                        	<c:if test="${type==0}"> <!-- type标识项目状态  0投资中  1锁定期  2已回款 -->
                        	<p class="fn-left">${userHjhInvistDetail.planName }</p>
                           <%--  <a class="fn-right value" href="${ctx}/agreement/hjhInfo.do?planNid=${userHjhInvistDetail.planNid }&accedeOrderId=${userHjhInvistDetail.accedeOrderId}" target="_blank">《投资服务协议》</a> --%>
                            <%-- 临时修改： 计划投资中不展示协议（协议改版有问题）--%>
                            <%--<c:if test="${userHjhInvistDetail.fddStatus ==1}"> --%>
                            	<%--<a class="fn-right value" href="${ctx}/createAgreementPDF/newHjhInvestPDF.do?planNid=${userHjhInvistDetail.planNid }&accedeOrderId=${userHjhInvistDetail.accedeOrderId}" target="_blank">${tzfwxy }</a>--%>
                            <%--</c:if>--%>
                            <ul class="ui-list-title">
                            	<li style="white-space:nowrap">
                                    <span class="basic-label">服务订单编号：</span>
                                    <span class="basic-value" >${userHjhInvistDetail.accedeOrderId}</span>
                                </li>
                                <li>
                                </li>
                                <li>
                                </li>
                                <li>
                                    <span class="basic-label">授权服务金额：</span>
                                    <span class="basic-value">${userHjhInvistDetail.accedeAccount }元</span>
                                </li>
                                <li>
                                    <span class="basic-label">&nbsp;&nbsp;参考年回报率：</span>
                                    <span class="basic-value">${userHjhInvistDetail.planApr }</span>
                                </li>
                                <li>
                                    
                                </li>
                                <li>
                                    <span class="basic-label">授权服务时间：</span>
                                    <span class="basic-value">${userHjhInvistDetail.addTime }</span>
                                </li>
                                <li style="white-space: nowrap;">
                                	<span class="basic-label">&nbsp;&nbsp;服务回报期限：</span>
                                    <span class="basic-value">${userHjhInvistDetail.planPeriod }</span>
                                </li>
                                <li>
                                    
                                </li>
                                <li>
	                                <span class="basic-label">参考回报：</span>
	                                <span class="basic-value">— —</span>
	                            </li>
                            </ul>
                        	</c:if>
                            <c:if test="${type==1}"> <!-- type标识项目状态  0投资中  1锁定期  2已回款 -->
                        	<p class="fn-left">${userHjhInvistDetail.planName }</p>
                            <%-- <a class="fn-right value" href="${ctx}/agreement/hjhInfo.do?planNid=${userHjhInvistDetail.planNid }&accedeOrderId=${userHjhInvistDetail.accedeOrderId}" target="_blank">《投资服务协议》</a>  --%>
                           <c:if test="${userHjhInvistDetail.fddStatus  ==1 }">
                            	<a class="fn-right value" href="${ctx}/createAgreementPDF/newHjhInvestPDF.do?planNid=${userHjhInvistDetail.planNid }&accedeOrderId=${userHjhInvistDetail.accedeOrderId}" target="_blank">${tzfwxy }</a>
                            </c:if>
                            <ul class="ui-list-title">
                            	<li style="white-space:nowrap">
                                    <span class="basic-label">服务订单编号：</span>
                                    <span class="basic-value">${userHjhInvistDetail.accedeOrderId}</span>
                                    
                                </li>
                                <li>
                                	
                                </li>
                                <li>
                                </li>
                                <li>
                                    <span class="basic-label">授权服务金额：</span>
                                    <span class="basic-value">${userHjhInvistDetail.accedeAccount }元</span>
                                </li>
                                <li>
                                    <span class="basic-label">&nbsp;&nbsp;参考年回报率：</span>
                                    <span class="basic-value">${userHjhInvistDetail.planApr }</span>
                                </li>
                                <li>
                                    
                                </li>
                                <li>
                                    <span class="basic-label">授权服务时间：</span>
                                    <span class="basic-value">${userHjhInvistDetail.addTime }</span>
                                </li>
                                <li style="white-space: nowrap;">
                                    <span class="basic-label">&nbsp;&nbsp;服务回报期限：</span>
                                    <span class="basic-value">${userHjhInvistDetail.planPeriod }</span>
                                </li>
                                <li>
                                </li>
	                            <li>
	                                <span class="basic-label">参考回报：</span>
	                                <span class="basic-value value">${userHjhInvistDetail.waitInterest }</span>
	                                <span class="basic-label">元</span>
	                            </li>
                            </ul>
                        	</c:if>
                        	
                        	<c:if test="${type==2}"> <!-- type标识项目状态  0投资中  1锁定期  2已回款 -->
                        	<p class="fn-left">${userHjhInvistDetail.planName }</p>
                            <%-- <a class="fn-right value" href="${ctx}/agreement/hjhInfo.do?planNid=${userHjhInvistDetail.planNid }&accedeOrderId=${userHjhInvistDetail.accedeOrderId}" target="_blank">《投资服务协议》</a>  --%>
                           <c:if test="${userHjhInvistDetail.fddStatus  ==1 }">
                           		<a class="fn-right value" href="${ctx}/createAgreementPDF/newHjhInvestPDF.do?planNid=${userHjhInvistDetail.planNid }&accedeOrderId=${userHjhInvistDetail.accedeOrderId}" target="_blank">${tzfwxy }</a>
                           </c:if>
                            <ul class="ui-list-title">
                            	<li style="white-space:nowrap">
                                    <span class="basic-label">服务订单编号：</span>
                                    <span class="basic-value">${userHjhInvistDetail.accedeOrderId}</span>
                                    
                                </li>
                                <li>
                                	
                                </li>
                                <li>
                                </li>
                                <li>
                                    <span class="basic-label">授权服务金额：</span>
                                    <span class="basic-value">${userHjhInvistDetail.accedeAccount }元</span>
                                </li>
                                <li>
                                    <span class="basic-label">&nbsp;&nbsp;参考年回报率：</span>
                                    <span class="basic-value">${userHjhInvistDetail.planApr }</span>
                                </li>
                                <li>
                                    
                                </li>
                                <li style="white-space: nowrap;">
                                    <span class="basic-label">授权服务时间：</span>
                                    <span class="basic-value">${userHjhInvistDetail.addTime }</span>
                                </li>
                                <li style="white-space: nowrap;">
                                    <span class="basic-label">&nbsp;&nbsp;服务回报期限：</span>
                                    <span class="basic-value">${userHjhInvistDetail.planPeriod }</span>
                                </li>
                                <li>
                                </li>
	                            <li>
	                                <span class="basic-label">已获回报：</span>
	                                <span class="basic-value value">${userHjhInvistDetail.receivedInterest }</span>
	                                <span class="basic-label">元</span>
	                            </li>
                            </ul>
                        	</c:if>
                        	
                        </div>
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
                <div class="container " style="margin-top: 20px;">
                    <section class="content">
                        <div class="main-title">
                            智投服务进度
                        </div>
                        <div class="flow-content plan">
                        	<c:if test="${userHjhInvistDetail.addTimeFormat !=null && userHjhInvistDetail.countInterestTime==null }">
							<div class="process-box process1">
								<span class="process-img"></span>
								<em style="width: 140px;"><img src="/dist/images/zhitou/forward-gray@2x.png"/></em>
								<div class="text" style="">授权服务 <span>${userHjhInvistDetail.addTimeFormat }</span></div>
								<div class="process-text">智能投标,<br/>待匹配标的全部放款。</div>
							</div>
							</c:if>
							<c:if test="${userHjhInvistDetail.addTimeFormat !=null && userHjhInvistDetail.countInterestTime != null }">
							<div class="process-box process1 white">
								<span class="process-img"></span>
								<em style="width: 140px;"><img src="/dist/images/zhitou/forward@2x.png"/></em>
								<div class="text" style="">授权服务 <span>${userHjhInvistDetail.addTimeFormat }</span></div>
								<div class="process-text">智能投标,<br/>待匹配标的全部放款。</div>
							</div>
							</c:if>
							<c:if test="${userHjhInvistDetail.countInterestTime != null && userHjhInvistDetail.endInterestTime == null }">
								<div class="process-box process2">
									<span class="process-img"></span>
									<em style="width: 356px;"><img src="/dist/images/zhitou/forward-gray@2x.png"/></em>
									<div class="text" style="">开始计息<span>${userHjhInvistDetail.countInterestTime }</span></div>
									<div class="process-text">进入服务回报期，<br>资金循环出借，并持续计息。</div>
								</div>
	                        </c:if>
	                         <c:if test="${userHjhInvistDetail.countInterestTime != null && userHjhInvistDetail.endInterestTime != null}">
								<div class="process-box process2 white">
									<span class="process-img"></span>
									<em style="width: 356px;"><img src="/dist/images/zhitou/forward@2x.png"/></em>
									<div class="text" style="">开始计息<span>${userHjhInvistDetail.countInterestTime }</span></div>
									<div class="process-text">进入服务回报期，<br>资金循环出借，并持续计息。</div>
								</div>
							</c:if>
							<c:if test="${userHjhInvistDetail.countInterestTime == null}">
								<div class="process-box process2 gray">
									<span class="process-img"></span>
									<em style="width: 356px;"><img src="/dist/images/zhitou/forward-gray@2x.png"/></em>
									<div class="text" style="color:#999999">开始计息<span>${userHjhInvistDetail.countInterestTime }</span></div>
									<div class="process-text">进入服务回报期，<br>资金循环出借，并持续计息。</div>
								</div>
							</c:if>
							<c:if test="${userHjhInvistDetail.endInterestTime == null}">
								<div class="process-box process3 gray">
									<span class="process-img"></span>
									<em style="width: 169px;"><img src="/dist/images/zhitou/forward-gray@2x.png"/></em>
									<div class="text" style="color:#999999">开始退出<span>${userHjhInvistDetail.endInterestTime } </span></div>
									<div class="process-text">服务回报期结束，<br />发起债转，待转让全部完成。</div>
								</div>
							</c:if>
							<c:if test="${userHjhInvistDetail.repayActualTime == null && userHjhInvistDetail.endInterestTime !=null}">
								<div class="process-box process3">
									<span class="process-img"></span>
									<em style="width: 169px;"><img src="/dist/images/zhitou/forward-gray@2x.png"/></em>
									<div class="text" style="">开始退出<span>${userHjhInvistDetail.endInterestTime }</span></div>
									<div class="process-text">服务回报期结束，<br />发起债转，待转让全部完成。</div>
								</div>
							</c:if>
							<c:if test="${userHjhInvistDetail.repayActualTime != null }">
								<div class="process-box process3 white">
									<span class="process-img"></span>
									<em style="width: 169px;"><img src="/dist/images/zhitou/forward@2x.png"/></em>
									<div class="text" style="">开始退出<span>${userHjhInvistDetail.endInterestTime }</span></div>
									<div class="process-text">服务回报期结束，<br />发起债转，待转让全部完成。</div>
								</div>
							</c:if>
							<c:if test="${userHjhInvistDetail.repayActualTime==null}">
								<div class="process-box process4 gray">
									<span class="process-img"></span>
									<em style="width: 101px;"><img src="/dist/images/zhitou/forward-gray@2x.png"/></em>
									<div class="text" style="color:#999999">退出完成<span>${userHjhInvistDetail.repayActualTime } </span></div>
									<div class="process-text"><br/>系统清算</div>
								</div>
							</c:if>
							<c:if test="${userHjhInvistDetail.repayActualTime!=null && userHjhInvistDetail.repayActualTime==null}">
								<div class="process-box process4">
									<span class="process-img"></span>
									<em style="width: 101px;"><img src="/dist/images/zhitou/forward-gray@2x.png"/></em>
									<div class="text" style="">退出完成<span>${userHjhInvistDetail.repayActualTime } </span></div>
									<div class="process-text"><br/>系统清算</div>
								</div>
							</c:if>
							<c:if test="${userHjhInvistDetail.repayActualTime!=null && userHjhInvistDetail.repayActualTime!=null}">
								<div class="process-box process4 white">
									<span class="process-img"></span>
									<em style="width: 101px;"><img src="/dist/images/zhitou/forward@2x.png"/></em>
									<div class="text" style="">退出完成<span>${userHjhInvistDetail.repayActualTime } </span></div>
									<div class="process-text"><br/>系统清算</div>
								</div>
							</c:if>
							<c:if test="${ userHjhInvistDetail.repayActualTime==null}">
							<div class="process-box process5 gray">
								<div class="text" style="color:#999999">本息回款<span>${userHjhInvistDetail.repayActualTime } </span></div>
								<span class="process-img"></span>
							</div>
							</c:if>
							<c:if test="${ userHjhInvistDetail.repayActualTime!=null}">
							<div class="process-box process5 white">
								<div class="text" style="">本息回款<span>${userHjhInvistDetail.repayActualTime } </span></div>
								<span class="process-img"></span>
							</div>
							</c:if>
                        </div>
                    </section>
                </div>
                <div class="main">
                    <div class="title">持有项目列表</div>
                    <c:if test="${type!=0}"> <!-- type标识项目状态  0投资中  1锁定期  2已回款 -->
                    <table class="table">
                        <thead>
                            <th class="ui-list-title pl1" style="width: 24%;">项目编号</th>
                            <th class="ui-list-title pl2" style="width: 20%;">项目期限</th>
                            <th class="ui-list-title pl3" style="width: 20%;">出借金额</th>
                            <th class="ui-list-title pl4" style="width: 20%;">出借时间</th>
                            <th class="ui-list-title pl6" style="padding-left: 8px;width: 20%;">操作</th>
                        </thead>
                        
                     	<tbody id="projectList">
	                       	<tr>
	                       		<td colspan="6">
	                       			<div class="loading"><div class="icon"><div class="text">Loading...</div></div></div>
	                       		</td>
	                       	</tr>
                        </tbody>
                    </table>
                    <div class="pages-nav" id="new-pagination"></div>
                    </c:if>
                    <c:if test="${type==0}"> <!-- type标识项目状态  0投资中  1锁定期  2已回款 -->
                        <div class="no-data">
                        	出借中 待确认...
                    	</div>
                        
                    </c:if>
                </div>
            </div>
            <!-- end 内容区域 -->            
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="${cdn}/js/user/assetmanage/hjh-invest-detail.js?version=${version}"></script>
	<script>setActById("mytender");</script>
	
</body>
</html>