<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>投资失败 - 汇盈金服官网</title>
	   	<jsp:include page="/head.jsp"></jsp:include>
	</head>
	<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<article class="main-content" style="padding-top: 0;">
        <div class="container-investsuccess">
            <!-- start 内容区域 -->   
                <div class="invest-success">
                    <div class="top-attr">
                        <img src="${cdn }/dist/images/result/failure-img@2x.png" width="125">
                        <p class="top-title">${investDesc}</p>
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
