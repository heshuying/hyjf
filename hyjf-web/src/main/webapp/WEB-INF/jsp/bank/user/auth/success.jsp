<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<title>成功 - 汇盈金服官网</title>
<jsp:include page="/head.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<jsp:include page="/subMenu.jsp"></jsp:include>
	<article class="main-content">
        <div class="container result">
            <div class="register-content result-content">
            	<div class="register-top P-mtp60">
            		<img src="${cdn }/dist/images/result/register-success@2x.png" width="390" alt="" />
            	</div>
            	<div class="register-success result-mid">
            		<h3>${message}</h3>
            	</div>           	           	
            	<p class="count-down"><a href="${ctx}/user/pandect/pandect.do">返回用户中心</a>，<span>5</span>秒后自动跳转</p>           	           	   
            </div>
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script>    
	$(function(){
		var time=5
		setInterval(function(){
			time--
			$('.count-down span').text(time)
		},1000)
		setTimeout("location.href='${ctx}/user/pandect/pandect.do'",5000)
	})
	</script>		
</body>
</html>