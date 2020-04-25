<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<form id="fm" name="fm" action='${bankForm.action }' method="post">
	<c:if test='${!empty bankForm.allParams }'>
		<c:forEach items='${bankForm.allParams }' var="record" begin="0" step="1" varStatus="status">
			<input type="hidden" name='${record.key }' id='${record.key }' value='${record.value}' />
		</c:forEach>
	</c:if>
</form>
<script>
document.getElementById("fm").submit();
</script>