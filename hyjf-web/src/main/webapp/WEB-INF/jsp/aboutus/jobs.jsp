<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
		<jsp:include page="/head.jsp"></jsp:include>
	</head>
	
	<body>
		<jsp:include page="/header.jsp"></jsp:include>
		<jsp:include page="/subMenu-aboutUS.jsp"></jsp:include>
		<article class="main-content">
	        <div class="container">
	            <section class="about-detial content">
	                <div class="main-title">
	                    招贤纳士
	                </div>
	                <div class="main-tab jion-us">
	                    <p>我们是在金融和互联网领域有着丰富经验的一支团队！我们是完美主义者，是理想主义者，也是发明家。我们不断完善产品和流程，始终以精益求精为己任，我们的每一项工作都将充满挑战。只要你积极乐观，聪明向上，勇于创新，工作勤奋，敢于担当，请加入我们！</p>
	                    <p>请发送您的简历至&nbsp;<a href="mailto:hr@hyjf.com" class="colorRed" itemid="lt">hr@hyjf.com</a> ，标题请注明所申请职位。</p>
	                    <div class="jobs-list">
		                    <c:forEach items="${jobList}" var="list">
		                     	<dl>
			                    	<dt itemid="lt1">
			                    		<span class="title">${list.officeName}(${list.persons}人)</span>
			                    		<span class="address">
			                    			${list.place}
			                    			<i class="icon iconfont icon-addition"></i>
			                    		</span>
			                    	</dt>
			                    	<dd>
			                    		<p>${list.content}</p>
			                    	</dd>
			                    </dl>
			                 </c:forEach>  
	                    </div>
	                </div>
	            </section>
	        </div>
	    </article>
		<jsp:include page="/footer.jsp"></jsp:include>
		<script src="${cdn}/dist/js/lib/baguetteBox.min.js"></script>
		<script src="${cdn}/dist/js/about/about.js?version=${version}"></script>
		<!-- 设置定位  -->
  		<script>setActById("aboutJobs");</script>
  		<!-- 设置定位  -->
	    <script>setActById("indexMessage");</script>
	</body>
</html>