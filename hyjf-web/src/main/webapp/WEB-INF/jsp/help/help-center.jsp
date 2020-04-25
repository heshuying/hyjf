<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
</head>
<body class="help-center">
	<jsp:include page="/header.jsp"></jsp:include>
	<article class="main-content">
		<div class="container">
			<aside class="side-bar">
				<div class="side-title">帮助中心<i class="icon iconfont icon-bangzhu"></i></div>				
				<div class="side-menu" id="side-list">			
				<c:forEach items="${AllList}" var="list" varStatus="st">
				<dl>				
				<dt itemid="hp${st.index}">${list.HelpCategoryCustomize.title} <i class="iconfont icon-sanjiao"></i></dt>
				<c:forEach items="${list.listsun}" var="listsun" varStatus="dt">
						<dd itemid="hp${st.index}${dt.index + 1}" id="hp${st.index}${dt.index + 1}" ><span>${listsun.title}</span></dd>
						</c:forEach>
				</dl>
				</c:forEach>
				</div>
			</aside>
			<section class="side-tab" id="side-cont">
				<c:forEach items="${AllList}" var="list" varStatus="st">
					<section class="side-tab-item">
						<ul>
						<c:forEach items="${list.listsun}" var="listsun" varStatus="dt">
							<li class="issure-list hide" >
								<div class="issure-list-title">${list.HelpCategoryCustomize.title}-<span>${listsun.title}</span></div>
								<c:forEach items="${listsun.listsunContent}" var="listsunContent" varStatus="dd">
									<ul class="issure-list-cont">
										<li>
											<div class="title" itemid="is${st.index}${dt.index}${dd.index + 1}">
												${listsunContent.title}
												<i class="iconfont icon-add"></i>
											</div>
											<div class="message">${listsunContent.content}</div>
										</li>
									</ul>
								</c:forEach>
							</li>
							</c:forEach>
						</ul>
					</section>
				</c:forEach>
			</section>
		</div>
	</article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script type="text/javascript">
		var indexId = "${indexId}";
	</script>
	<script src="${cdn}/dist/js/about/help.js?version=${version}"></script>
</body>
</html>
