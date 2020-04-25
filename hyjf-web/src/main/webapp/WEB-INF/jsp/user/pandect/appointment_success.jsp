<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<title>预约授权-成功</title>
<link rel="stylesheet" type="text/css" href="${cdn}/css/page.css" />
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<div class="shSuccess-Top">
		<span>自动投资授权</span>
	</div>
	<div class="shSuccess-Ban">
		<h4>${message}</h4>
		<div class="shSuccess-click">
			 <c:if test="${appointment eq 0 }">
			 <a href="${ctx}/project/initProjectList.do?projectType=HZT" class="inputBg">我要投资</a>
			 </c:if>
			<c:if test="${appointment eq 1 }">
			 <%--<a href="${ctx}/plan/initPlanList.do" class="inputBg">我要加入计划</a>--%>
				<%--mod by nxl 智投服务修改--%>
				<a href="${ctx}/plan/initPlanList.do" class="inputBg">我要授权服务</a>
			 <a href="${ctx}/project/initProjectList.do?projectType=ZXH" class="inputBg">我要预约</a>
			 </c:if>
			<a href="${ctx}/user/pandect/pandect.do"  class="inputBg">个人中心</a>
		</div>
	</div>

	<jsp:include page="/footer.jsp"></jsp:include>
</body>
</html>