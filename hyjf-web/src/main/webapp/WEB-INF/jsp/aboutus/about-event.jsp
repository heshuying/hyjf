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
            <section class="content" style="padding-bottom: 28px;">
                <div class="main-title">
                   	重大事项信息
                </div>
                <div class="main-tab jion-us">
                    <div class="event">
                    	<p>公司减资、合并、分立、解散或申请破产： <span>无</span></p>
                    	<p>公司依法进入破产程序： <span>无</span></p>
                    	<p>公司被责令停业、整顿、关闭： <span>无</span></p>
                    	<p>公司涉及重大诉讼、仲裁，或涉嫌违法违规被有权机关调查，或受到刑事处罚、重大行政处罚： <span>无</span></p>
                    	<p>公司法定代表人、实际控制人、主要负责人、董事、监事、高级管理人员涉及重大诉讼、仲裁，或涉嫌违法违纪被有权机关调查，或受到刑事处罚、重大行政处罚，或被采取强制措施： <span>无</span></p>
                    	<p>公司主要或者全部业务陷入停顿： <span>无</span></p>
                    	<p>存在欺诈、损害出借人利益等其他影响网络借贷信息中介机构经营活动的重大事项： <span>无</span></p>
                    </div>
                    <div class="go-back">
                    	<a href="${ctx}/aboutus/getInformation.do">返回列表<i class="xiayibu"></i></a>
                    </div>
                </div>
            </section>
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="${cdn}/dist/js/lib/baguetteBox.min.js"></script>
	<script src="${cdn}/dist/js/about/about.js"></script>
	<!-- 设置定位  -->
	<script>setActById("aboutInformation");</script>
	<!-- 导航栏定位  -->
	<script>setActById("indexMessage");</script>
</body>
</html>