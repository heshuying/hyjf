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
            <div class="left-side">您所在的位置： <a href="${ctx}/">首页</a>  &gt; <a href="${ctx}/hjhplan/initPlanList.do">服务智投</a> &gt;  项目详情</div>
            <div class="right-side"><span>开标时间：</span>${projectDeatil.sendTime}</div>
        </div>
    </section>
    <article class="main-content product">
        <div class="container">
            <!-- start 内容区域 -->
            <div class="product-intr">
                <div class="title">
                	<input type="hidden" name="userBalance" id="userBalance" value="${userBalance}" />
                	<input type="hidden" name="borrowNid" id="borrowNid" value="${projectDeatil.borrowNid }" />
                	<input type="hidden" name="loginFlag" id="loginFlag" value="${loginFlag }" /> <!-- 登录状态 0未登陆 1已登录 -->
                	<input type="hidden" name="openFlag" id="openFlag" value="${openFlag }" /> <!-- 开户状态 0未开户 1已开户 -->
                	<input type="hidden" name="riskFlag" id="riskFlag" value="${riskFlag }" /> <!-- 是否进行过风险测评 0未测评 1已测评 -->
                	<input type="hidden" name="setPwdFlag" id="setPwdFlag" value="${setPwdFlag }" /> <!-- 是否设置过交易密码 0未设置 1已设置 -->
					<span>${projectDeatil.projectName}</span>
                    <div class="contract-box">
                    	<%-- <a href="${ctx}/agreement/diaryServices.do" target="_blank">《借款协议》</a> --%>
						<a href="${ctx}/agreement/goAgreementPdf.do?aliasName=jjfwjkxy" target="_blank">${jjfwjkxy }</a>
						<a href="${ctx}/agreement/goAgreementPdf.do?aliasName=tzfxqrs" target="_blank">${tzfxqrs }</a>
                    </div>
                </div>
                <div class="attr">
                    <div class="attr-item attr1">
                        <span class="val highlight">${projectDeatil.borrowApr}<span class="unit">%</span> </span>
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
                    <div class="list-item">项目编号：${projectDeatil.borrowNid}</div>
                    <div class="list-item">还款方式：${projectDeatil.repayStyle}</div>
                    <div class="list-item"><span class="dark">建议出借人类型：稳健型及以上</span></div>
                </div>
            </div>

            <div class="product-form status">
            	<!-- 投资中 -->
	            <c:if test="${projectDeatil.status eq 11}">
	 	           <!-- 投资中  -->
			       <div class="status-content tzz single"></div>
	            </c:if>
            	<!-- 已还款  -->
                <c:if test="${projectDeatil.status eq 14}">
                	<div class="status-content yhk single"></div>

	                <div class="start-time">满标时间：${projectDeatil.fullTime}</div>
                </c:if>
            	<!-- 还款中  -->
                <c:if test="${projectDeatil.status eq 13}">
	                <div class="status-content hkz single"></div>
	                <div class="start-time">满标时间：${projectDeatil.fullTime}</div>
                </c:if>
            	<!-- 复审中  -->
                <c:if test="${projectDeatil.status eq 12}">
	                <div class="status-content fsz single"></div>
	                <div class="start-time">满标时间：${projectDeatil.fullTime}</div>
                </c:if>
			</div>

            <!-- end 内容区域 -->
        </div>

        <div class="container">
            <section class="content">
                <div class="main-tab">
                    <ul class="tab-tags">
                        <li class="active" panel="0"><a href="javascript:;">项目详情</a></li>
                        <li panel="1"><a href="javascript:;">出借记录</a></li>
                        <li panel="2"><a href="javascript:;">还款计划</a></li>
                        <c:if test="${isDebt eq true}">
                        	<li panel="3"><a href="javascript:;">承接记录</a></li>
                        </c:if>
                    </ul>
                    <ul class="tab-panels">
                        <li class="active" panel="0">
                            <!-- 项目详情 -->
                            <c:if test="${loginFlag eq '1'}"> <!-- 登录-->
                               <c:if test="${viewableFlag  eq '0' }"><!-- 非投资人-->
                                     <div class="unlogin">
                                         <div class="icon"></div>
                                         <p>仅此项目的出借人可查看</p>
                                     </div>
                                </c:if>
                                <c:if test="${viewableFlag  eq '1' }">
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
                                            <th width="35%">出借人</th>
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
                        <c:if test="${isDebt eq true}">
	                        <li panel="3">
		                        <div class="attr-table">
			                        <!-- 承接记录 -->
		                            <p>
		                                <span id="undertakeTotal">承接总人次：</span>&nbsp;&nbsp;
		                                <span id="undertakeTimes">承接金额：元</span>
		                            </p>
		                            <div class="attr-table">
		                                <table cellpadding="0" cellspacing="0">
		                                    <thead>
		                                        <tr>
			                                        <th width="35%">出借人</th>
		                                            <th width="25%">承接金额（元）</th>
		                                            <th width="15%">来源</th>
		                                            <th width="25%">承接时间</th>
		                                        </tr>
		                                    </thead>
		                                    <tbody id="projectUndertakeList">
		                                    </tbody>
		                                </table>
		                                <div class="pages-nav" id="undertake-pagination"></div>
		                            </div>
	                            </div>
	                        </li>
                        </c:if>
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
    <script type="text/javascript" src="${cdn}/dist/js/product/hjh-borrow-detail.js?version=${version}"></script>
</body>
</html>
