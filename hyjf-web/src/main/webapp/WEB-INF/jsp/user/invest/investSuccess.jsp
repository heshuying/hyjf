<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>投资成功 - 汇盈金服官网</title>
<jsp:include page="/head.jsp"></jsp:include>

</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<jsp:include page="/subMenu.jsp"></jsp:include>
	<article class="main-content" style="padding-top: 0;">
        <div class="container-investsuccess">
            <!-- start 内容区域 -->   
                <div class="invest-success">
                    <div class="top-attr">
                        <img src="${cdn }/dist/images/result/success-img2@2x.png" width="126">
                        <p class="top-title value">${investDesc}</p>
                    </div>
                    <div class="mid-attr">
                        <ul>
                            <li>
                                <div>
                                	
                                	<c:if test="${not empty plan and plan eq '1'}">
										<span>授权服务金额：</span>
						    		</c:if>
						    		<c:if test="${empty plan}">
										<span>投资金额：</span>
							    	</c:if>
                                	
                                	<span class="value">${account }</span><span> 元</span></div>
                            </li>
                            <li>
                                <div>
                                <c:if test="${not empty plan and plan eq '1'}">
										<span>参考回报：</span>
						    		</c:if>
						    		<c:if test="${empty plan}">
										<span>历史回报：</span>
							    	</c:if>
                                <span class="value"><fmt:formatNumber value="${earnings+couponInterest}" type="number" pattern="#,##0.00" /></span><span> 元</span>
                                </div>
                            </li>
                            
							<c:if test="${couponType == 1}">
								<li class="mid-border">
                                <div>
                                    <span class="icon iconfont icon-piaofang"></span>
                                    <span>优惠券：</span><span class="value">${couponQuota }元</span><span>体验金</span>
                                </div>
                            </li>
							</c:if>
							<c:if test="${couponType == 2}">
								<li class="mid-border">
                                <div>
                                    <span class="icon iconfont icon-piaofang"></span>
                                    <span>优惠券：</span><span class="value">${couponQuota }%</span><span>加息券</span>
                                </div>
                            </li>
							</c:if>
							<c:if test="${couponType == 3}">
								<li class="mid-border">
                                <div>
                                    <span class="icon iconfont icon-piaofang"></span>
                                    <span>优惠券：</span><span class="value">${couponQuota }元</span><span>代金券</span>
                                </div>
                            </li>
							</c:if>
                        </ul>
                    </div>
                    <div class="bom-attr">
                    	
                        <a href="${ctx}/bank/user/trade/initTradeList.do" class="open">查看交易明细</a>
                        <c:if test="${projectType eq 4}">
							<a href="${ctx}/bank/web/borrow/getBorrowDetail.do?borrowNid=${borrowNid}" class="open">返回项目详情</a>
						</c:if>
						<c:if test="${projectType ne 4}">
							<c:if test="${not empty plan and plan eq '1'}">
								<a href="${ctx}/hjhdetail/getPlanDetail.do?planNid=${planNid}" class="open">返回项目详情</a>
				    		</c:if>
				    		<c:if test="${empty plan}">
								<a href="${ctx}/bank/web/borrow/getBorrowDetail.do?borrowNid=${borrowNid}" class="open">返回项目详情</a>
					    	</c:if> 
							
						</c:if>
						                
                    </div>
                </div>
                <div class="invest-bom-bg"> </div>
            <!-- end 内容区域 -->            
        </div>
        
    </article>
   <jsp:include page="/footer.jsp"></jsp:include>
</body>
</html>