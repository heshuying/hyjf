<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<title>出借成功 - 汇盈金服官网</title>
<%@ include file="/head.jsp"%>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<article class="main-content" style="padding-top: 0;">
        <div class="container-investsuccess" style="position: relative;padding-bottom: 12%;">
            <!-- start 内容区域 -->   
                <div class="invest-success">
                    <div class="top-attr">
                        <img src="${cdn}/dist/images/result/success-img2@2x.png" width="126">
                        <p class="top-title value">${investDesc}</p>
                        <%-- <p>
							<c:if test="${not empty plan and plan eq '1'}">
								汇添金计划编号：<span><c:out value="${planNid}"></c:out></span>
				    		</c:if>
				    		<c:if test="${empty plan}">
						    	<c:if test="${projectType eq 13 }">汇金理财项目编号：<span><c:out value="${borrowAssetNumber}"></c:out></span> </c:if>
						    	<c:if test="${projectType ne 13 }"><c:out value="${projectTypeName}"></c:out></span> 项目编号：<span><c:out value="${borrowNid}"></c:out></span> </c:if>
				    		</c:if>
						</p> --%>
                    </div>
                    <div class="mid-attr">
                        <ul>
                            <li>
                                <div><span>出借金额：</span><span class="value">${account }</span><span> 元</span></div>
                            </li>
                            <li>
                                <div><span>历史回报：</span><span class="value">${interest } </span><span> 元</span></div>
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
							<a href="${ctx}/bank/web/borrow/getBorrowDetail.do?borrowNid=${borrowNid}" class="open">返回项目详情</a>
						</c:if>
						<c:if test="${not empty plan and plan eq '1'}">
							<a href="${ctx}/plan/getPlanDetail.do?planNid=${planNid}" class="open">返回项目详情</a>
				    	</c:if>                 
                    </div>
                </div>
                <!-- <div class="invest-bom-bg"> </div> -->
                <img src="${cdn}/dist/images/gongzhonghao-banner@2x.png?v=20171123" alt="" id="gzhImg" />
            <!-- end 内容区域 -->            
        </div>
        
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>

</body>
</html>