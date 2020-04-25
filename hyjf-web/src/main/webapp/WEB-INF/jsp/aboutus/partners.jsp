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
	            <section class="content">
                <div class="main-title">
                    合作伙伴
                </div>
	                <div class="main-tab">
	                    <ul class="tab-tags">
	                        <li class="active" panel="0" itemid="tt0"><a href="#">服务支持</a></li>
	                        <li panel="1" itemid="tt1"><a href="#">法律支持</a></li>
	                        <li panel="3" itemid="tt3"><a href="#">其他机构</a></li>
	                    </ul>
	                    <ul class="tab-panels">
	                    
	                    	<!-- 服务支持 -->
	                        <li class="active" panel="0">
	                        	<c:forEach items="${serviceSupportList }" var="record" begin="0" step="1" varStatus="status">
		                            <dl>
		                                <dt>
		                                    <a href="${record.url}" target="_blank" itemid="tp011">
		                                        <img src="${record.logo}" alt="">
		                                    </a>
		                                </dt>
		                                <dd>
		                                    <a href="${record.url}" target="_blank" itemid="tp012">${record.webname}</a>
		                                    <p>${record.summary}</p>
		                                </dd>
		                            </dl>
	                            </c:forEach>
	                        </li>
	                        
	                     	<!-- 法律支持 -->
	                        <li panel="1">
	                        	<c:forEach items="${lawSupportList }" var="record" begin="0" step="1" varStatus="status">
		                        	<dl>
		                                <dt>
		                                    <a href="${record.url}" target="_blank" itemid="tp111">
		                                        <img src="${record.logo}" alt="">
		                                    </a>
		                                </dt>
		                                <dd>
		                                    <a href="${record.url}" target="_blank" itemid="tp112">${record.webname}</a>
		                                    <p>${record.summary}</p>
		                                </dd>
		                            </dl>
	                     		</c:forEach>
	                        </li>
	                        
	                        <!-- 金融机构 -->
	                        <li panel="2">
	                        	<c:forEach items="${financeOrgList }" var="record" begin="0" step="1" varStatus="status">
		                        	<dl>
		                                <dt>
		                                    <a href="${record.url}" target="_blank" itemid="tp111">
		                                        <img src="${record.logo}" alt="">
		                                    </a>
		                                </dt>
		                                <dd>
		                                    <a href="${record.url}" target="_blank" itemid="tp112">${record.webname}</a>
		                                    <p>${record.summary}</p>
		                                </dd>
		                            </dl>
	                     		</c:forEach>
	                        </li>
	                        
	                       	<!-- 其他机构 -->
	                        <li panel="3">
	                        	<c:forEach items="${otherOrgList }" var="record" begin="0" step="1" varStatus="status">
		                        	<dl>
		                                <dt>
		                                    <a href="${record.url}" target="_blank" itemid="tp111">
		                                        <img src="${record.logo}" alt="">
		                                    </a>
		                                </dt>
		                                <dd>
		                                    <a href="${record.url}" target="_blank" itemid="tp112">${record.webname}</a>
		                                    <p>${record.summary}</p>
		                                </dd>
		                            </dl>
	                     		</c:forEach>
	                        </li>
	                    </ul>
	                </div>
	            </section>
	        </div>
	    </article>
		<jsp:include page="/footer.jsp"></jsp:include>
		<!-- 设置定位  -->
  		<script>setActById("aboutPartners");</script>	
	    <!-- 设置定位  -->
	    <script>setActById("indexMessage");</script>
		<script src="${cdn}/dist/js/lib/baguetteBox.min.js"></script>
		<script src="${cdn}/dist/js/about/about.js?version=${version}"></script>
	</body>
</html>