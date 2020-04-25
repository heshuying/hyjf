<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
    <article class="main-content" style="padding-top: 0;">
        <div class="container-investsuccess">
        <input type="hidden"  id="userId" name="userId" value="${orgUserId}">
        <input type="hidden"  id="startDate" name="startDate" value="${startDate}">
        <input type="hidden"  id="endDate" name="endDate" value="${endDate}">
        <input type="hidden"  id="password" name="password" value="${password}">
        <input type="hidden"  id="repayTotal" name="repayTotal" value="${repayTotal}">
            <!-- start 内容区域 -->   
                <div class="in-process">
                    <div class="top-attr">
                        <img src="${cdn}/dist/images/utils/in-process@2x.png" width="106">
                        <c:choose>
                            <c:when test="${not empty message}">
                                <p class="top-title value"><strong>还款申请提交失败</strong></p>
                                <p>${message}</p>
                            </c:when>
                            <c:otherwise>
                                <p class="top-title value"><strong>还款申请已提交成功</strong></p>
                                <p>银行处理中，请勿重复操作...</p>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="bom-attr">
                        <a href="javascript:;" id="back" class="open">返回</a>
                        <a href="javascript:;" id="repay" class="open">查看垫付详情</a>
                    </div>
                </div>

            <!-- end 内容区域 -->            
        </div>
        
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
</body>
<script type="text/javascript">
	$(document).ready(function() {
		var userId = $("#userId").val();
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		var password = $("#password").val();
		var repayTotal = $("#repayTotal").val();
		// $.ajax({
		// 	type:"post",
		// 	url:webPath + '/bank/web/user/repay/orgUserStartBatchRepayAction.do?userId=' + userId + '&startDate=' + startDate + '&endDate=' + endDate + '&password=' + password +'&repayTotal=' + repayTotal,
		// 	success:function(data){
		//
		// 	}
		// });
	});
	$("#back").click(function(){
		var userId = $("#userId").val();
		//批量垫付
		location.href = webPath + '/bank/web/user/repay/orgUserBatchRepayPage.do?userId=' + userId;
	});
	
	$("#repay").click(function(){
		var userId = $("#userId").val();
		//还款列表 bank/web/user/repay/userRepayPage.do
		location.href = webPath + '/bank/web/user/repay/userRepayPage.do?userId=' + userId + '&showFlag=1';
	});
	
	
</script>
</html>