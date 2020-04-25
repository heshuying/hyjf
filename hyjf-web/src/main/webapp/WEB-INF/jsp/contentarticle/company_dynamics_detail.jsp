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
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<jsp:include page="/subMenu-aboutUS.jsp"></jsp:include>
	<article class="main-content">
        <div class="container">
            <section class="about-detial content">
                <div class="main-title">
                    <span class="title-head">${companyDynamics.title}</span>
                    <span class="title-time"><fmt:formatDate value="${companyDynamics.createTime}" pattern="yyyy-MM-dd HH:mm"/></span>
                </div>
                <div class="detial-list">
                	${companyDynamics.content}
			    </div>
			    <div class="detial-btn">
			    	<a href="javascript:void(0)"  itemid="lt">返回列表<i class="iconfont icon-xiayibu63"></i></a>
			    </div>
            </section>
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include> 	
	<!-- 设置定位  -->
	<script>setActById("indexMessage");</script>
	<!-- 设置定位  -->
	<script>setActById("aboutDynamics");</script>
	<script type="text/javascript">
		$('.detial-btn a').click(function(){
			var page=utils.getCookie('companyDyPage')
			if( page !=''){
				location.href='${ctx}/contentarticle/getCompanyDynamicsList.do?page='+page
			}else{
				location.href='${ctx}/contentarticle/getCompanyDynamicsList.do'
			}
		})
	</script>
</body>
</html>