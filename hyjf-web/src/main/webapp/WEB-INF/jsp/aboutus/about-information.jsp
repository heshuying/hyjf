<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
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
            <section class="content">
                <div class="main-title">
                    信息披露
                </div>
                <div class="main-tab jion-us">
                    <p>《网络借贷信息中介机构业务活动管理暂行办法》第四条、第九条、第三十条、第三十一条均对于网络借贷信息中介机构的信息披露义务进行了规定。根据《中国互联网金融协会信息披露自律管理规范》及《中国互联网金融协会标准：互联网金融信息披露——个体网络借贷》（T/NIFA 1-2016）的规定，汇盈金服平台从从业机构信息、平台运营信息（请见首页平台数据页面）、项目信息（请见具体借款标的页面）、风险教育四个方面进行信息披露，供用户查看监督。</p>
                    <div class="jobs-list">
                    	<a class="info-link" href="${ctx}/aboutus/getCommitment.do">信披声明</a>
                        <a class="info-link" href="${ctx}/aboutus/getRecord.do">备案信息</a>
                     	<a class="info-link" href="${ctx}/aboutus/getBasic.do">组织信息</a>
                     	<%-- 跳转内容未定
                     	<a class="info-link" href="">审核信息</a>
                     	--%>
                     	<a class="info-link" href="${ctx}/aboutus/getEvent.do">重大事项信息</a>
                     	<a class="info-link" href="${ctx}/aboutus/searchKnowReportList.do">网贷知识</a>
                     	<a class="info-link" href="${ctx}/agreement/privacyClause.do">隐私权保护规则</a>
                     	<a class="info-link" href="${ctx}/aboutus/auditInfo.do">审核信息</a>
                    </div>
                </div>
            </section>
        </div>
    </article>

	<jsp:include page="/footer.jsp"></jsp:include>
	<!-- 设置定位  -->
	<script>setActById("aboutInformation");</script>
	<!-- 导航栏定位  -->
	<script>setActById("indexMessage");</script>
	
</body>
</html>