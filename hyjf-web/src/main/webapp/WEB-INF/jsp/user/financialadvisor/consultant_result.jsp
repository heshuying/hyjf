<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>风险测评 - 汇盈金服官网</title>
		<jsp:include page="/head.jsp"></jsp:include>
	</head>
	<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<jsp:include page="/subMenu.jsp"></jsp:include>
	<article class="main-content">
        <div class="container result">
        	<div class="fxcp-title">
            	风险测评
            </div>
            <div class="result-content fxcp-content ">
            	<div class="fxcp-top">
            		您的风险测评结果为：
            		<p class="type">${userEvalationResult.type }</p>
            		<p class="message highlight">${userEvalationResult.type }单笔最高出借金额为${userEvalationResult.revaluationMoney }</p>
            		<p class="message">${userEvalationResult.summary }</p>
            		<p class="continue"><a href="${ctx}/financialAdvisor/questionnaireInit.do" itemid="lt2">再测一次</a><span></span><a href="javascript:;" itemid="lt1" id="befurl">完成</a></p>
            	</div>
            </div>           
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>	
	 <script type="text/javascript" src="${cdn}/js/jquery.cookie.min.js" charset="utf-8"></script>
	    <script>
		    $(document).ready(
		    		function() {
		    		    var bfUrl = $.cookie("beforeUrl");
		    		    var url = (typeof bfUrl !== 'undefined' &&  bfUrl !== '') ? bfUrl : webPath + "/user/pandect/pandect.do"
		    			$("#befurl").click(function(){
			    			window.location.href = url
			    		});
		            	// $("#befurl").attr("href",url);
				});
	    </script>
	</body>
</html>