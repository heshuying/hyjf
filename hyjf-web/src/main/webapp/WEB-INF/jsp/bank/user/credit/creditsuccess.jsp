<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<article class="main-content">
        <div class="container result">
            <div class="result-content">
            	<div class="result-top">
            		<img src="${cdn}/dist/images/result/success-img@2x.png" width="126" alt="" />
            	</div>
            	<div class="result-mid">
            		<h3>恭喜您,债权转让申请提交成功！</h3>
            		<%-- <p>债转编号：<span>HZR${creditTender.creditNid }</span></p> --%>
            	</div>
            	<div class="result-add">
            		<div class="result-left">
            			<p>
            				转让本金：
            				<span><fmt:formatNumber value="${borrowCredit.creditCapital }" pattern="#,#00.00"/><span></span></span>
            				元
            			</p>
            			<p>
            				截止时间：
            				<span class="zqs_Time">${creditEndTime }</span>
            			</p>
            		</div>
            		<div class="result-right">
            			<p>
            				转让价格：
            				<span><fmt:formatNumber value="${borrowCredit.creditPrice }" pattern="#,#00.00"/></span>
            				元
            			</p>
            		</div>
            	    <div class="clearboth"></div>
            	</div>
            	<div class="jkgl-btn result-btn">
            		<a href="${ctx}/user/assetmanage/init.do?currentTab='myCreditListTab'" class="register-btn import" itemid="bt2">查看详情</a>
            	</div>
            </div>
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
</body>
</html>