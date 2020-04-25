<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<form id="fm" nm="fm" action="${callBackForm.callBackAction }" method=post>
	<c:if test="${!empty callBackForm.allParams }">
		<c:forEach items="${callBackForm.allParams }" var="record" begin="0" step="1" varStatus="status">
			<input type="hidden" name="${record.key }" id="${record.key }"value='<c:out value="${record.value}" escapeXml="true" />' />
		</c:forEach>
	</c:if>
</form>
<script type="text/javascript">
document.getElementById("fm").submit();
</script>
