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
	<div class="bond-nav">
        <div class="bond-nav-container">
            <div class="bond-nav-div"><a class="bg-bgcolor" href="${ctx}/bank/web/borrow/newBorrowList.do">新手专区</a></div>
            <div class="bond-nav-div"><a href="${ctx}/help/fresher.do">新手指引</a></div>
        </div>
    </div>
    <article class="main-content" style="padding-top: 0;">
        <div class="container">
            <!-- start 内容区域 -->  
            <!--<div class="bond-banner">
                 <p>金融优质服务</p>
                 <h5>多重安全保障,为您的资金保驾护航。</h5>
             </div>--> 
             <div class="newzone-banner-bond">
                   <p>新手通道 ，出借专享</p>
            	<h5>新手标是经过汇盈金服对借款人进行信息搜集和资信评估后在汇盈金服平台发布的针对新用户的借款标的统称。市场有风险，出借需谨慎。<a class="risk-alt"><span class="risk-tips">出借人应知悉网络借贷活动的风险，具备参与网络借贷活动相适应的出借风险意识、风险识别能力，拥有非保本类金融产品出借的经历，在出借前确保已经了解借款项目信息，确认具有相适应的风险认知和承受能力，谨慎出借并能够承担借贷相关风险。</span><i class="icon iconfont icon-zhu "></i></a></h5>
             </div>
             <div class="bond-investlist">
                <input id="projectType" name="projectType" type="hidden" value="${projectType}" />
				<input id="borrowClass" name="borrowClass" type="hidden" value="${borrowClass}" />		
                <div class="bond-list">
					<div class="bond-thead">
						<div class="bond-div bd1">项目编号</div>
						<div class="bond-div bd2">历史年回报率</div>
						<div class="bond-div bd3">出借期限</div>
						<div class="bond-div bd4">项目金额</div>
						<div class="bond-div bd5">进度</div>
						<div class="bond-div bd6">状态</div>
					</div>
					<div id="projectList">
                        <div class="loading"><div class="icon"><div class="text">Loading...</div></div></div>
                    </div>
                </div>
                  <!--分页-->
                   <div class="pages-nav" id="new-pagination"></div> 
             </div>   
             
            <!-- end 内容区域 -->            
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="${cdn}/dist/js/product/product-list.js?version=${version}"></script>
	<script src="${cdn}/js/bank/borrow/bond-investlist.js?version=${version}"></script>
	<!-- 导航栏定位  -->
	<script>setActById("indexNew");</script>
	<script type="text/javascript">
    	$('.risk-alt').hover(function(){
    		var alt=$(this).find('.risk-tips');
			$(this).find('.risk-tips').stop().fadeIn(150);
    	},function(){
    		$(this).find('.risk-tips').stop().fadeOut(150);
    	})
    </script>
</body>
</html>