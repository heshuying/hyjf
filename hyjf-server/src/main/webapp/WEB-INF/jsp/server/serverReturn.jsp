<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<form id="fm" nm="fm" action="${action}" method=post>
	<input type="hidden" name="status" id="status" value="${status}" />
	<input type="hidden" name="secretKey" id="secretKey" value="${secretKey}" />
	<input type="hidden" name="statusDesc" id="statusDesc" value="${statusDesc}" />
	<input type="hidden" name="responseObject" id="responseObject" value="${responseObject}" />
</form>
<script>
document.getElementById("fm").submit();
</script>