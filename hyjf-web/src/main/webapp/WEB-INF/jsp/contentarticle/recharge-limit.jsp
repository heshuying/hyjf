<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head lang="en">
<meta charset="UTF-8">
<title>支付说明 - 汇盈金服官网</title>
<jsp:include page="/head.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<article class="main-content">
	<div class="container">
		<section class="about-detial content">
		<div class="main-title">快捷充值限额</div>
			<table class="zhifu-des"  border="0" cellspacing="0" cellpadding="0">
				<thead>
				<tr>
					<td>序号</td>
					<td >支持银行</td>
					<td >交易限额/单笔限额</td>
					<td >日累计限额</td>
					<td >月累计限额</td>
				</tr>
				</thead>

				<tbody id="roleTbody">
				<c:choose>
					<c:when test="${empty date}">
						<tr>
							<td colspan="6">暂时没有数据记录</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach items="${date}" var="bank" begin="0" step="1" varStatus="status">
							<tr>
								<td><c:out value="${ status.index + 1}"></c:out></td>
								<td class="center"><c:out value="${bank.bankName }"></c:out></td>

								<td>
									<c:choose>
										<c:when test="${bank.singleQuota.unscaledValue() == 0}">
											<c:out value="不限"></c:out>
										</c:when>
										<c:otherwise>
											<c:out  value="${(bank.singleQuota)}"/>
											<c:out value="万元"></c:out>
										</c:otherwise>
									</c:choose>
								</td>
								<td>
									<c:choose>
										<c:when test="${bank.singleCardQuota.unscaledValue() == 0}">
											<c:out value="不限"></c:out>
										</c:when>
										<c:otherwise>
											<c:out value="${(bank.singleCardQuota)}"/>
											<c:out value="万元"></c:out>
										</c:otherwise>
									</c:choose>

								</td>
								<td>
									<c:choose>
										<c:when test="${bank.monthCardQuota.unscaledValue() == 0}">
											<c:out value="不限"></c:out>
										</c:when>
										<c:otherwise>
											<c:out  value="${(bank.monthCardQuota)}"/>
											<c:out value="万元"></c:out>
										</c:otherwise>
									</c:choose>

								</td>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
				</tbody>
			</table>
		</section>

	</div>

	</article>


	<jsp:include page="/footer.jsp"></jsp:include>
</body>
</html>