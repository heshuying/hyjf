<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>债权转让申请 - 汇盈金服官网</title>
		<jsp:include page="/head.jsp"></jsp:include>
	</head>
	<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<div class="ZQS_Top hjkS_Top">
		    <span style="padding-top: 42px;">债权转让申请</span>
		</div>
		<div class="ZQS_banner">
			<div class="TZs_left"><img src="${cdn}/img/sucess01.jpg"></div>
		    <div class="ZQS_right" >
				<div class="ZQS_tishi">恭喜您，债权转让申请提交成功</div>
				<div class="zqs-jin">
					<div class="zqs-item">
			            <div class="zqs-text">
			        		转让本金：
			            </div>
			            <div class="zqs-content">
			        	    <fmt:formatNumber value="${borrowCredit.creditCapital }" pattern="#,#00.00"/><span>元</span>
			            </div>
			        </div>
				    <div class="zqs-item">
					    <div class="zqs-text">
			        		转让价格：
			            </div>
			            <div class="zqs-content">
			        	    <fmt:formatNumber value="${borrowCredit.creditPrice }" pattern="#,#00.00"/><span>元</span>
			            </div>
				    </div>
				</div> 
				<div class="zqs-jin">
					<div class="zqs-item">
			            <div class="zqs-text">
			        		截止时间：
			            </div>
			            <div class="zqs-content">
			        	   	<span class="zqs_Time">${creditEndTime }</span>
			            </div>
			            <!-- 
			            <div class="zqs-content">
			        	   	<span class="zqs_Time">11:36</span>
			            </div>
			             -->
			        </div>
				</div> 
				<a href="${ctx}/credit/usercreditlist.do?tab=creditInProgress" class="Inputzhaiquan inputBg">查看详情</a>
		    </div>
		</div>
	
	<jsp:include page="/footer.jsp"></jsp:include>
	</body>
</html>