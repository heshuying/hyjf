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
                <div class="bond-nav-div"><a href="${ctx}/bank/web/borrow/initBorrowList.do">散标出借</a></div>
	            <div class="bond-nav-div"><a class="bg-bgcolor" href="${ctx}/bank/user/credit/initWebCreditPage.do">债权转让</a></div>
            </div>
        </div>
    <article class="main-content" style="padding-top: 0;">
        <div class="container">
            <!-- start 内容区域 -->  
            <div class="bond-banner">
                <p>主动发起，加速周转</p>
                <h5>
                	债权转让是债权持有人在汇盈金服平台将债权挂出并将所持有的债权转让给受让人的操作。
                	<a class="risk-alt alt2">
                   		<span class="risk-tips" style="height:66px;background-size: 544px 66px;">
                   			指债权持有人通过汇盈金服平台债权转让系统将债权挂出且与承接人签订债权转让协议，将所持有的债权转让给承接人的操作。
                   		</span>
                   		<i class="icon iconfont icon-zhu "></i>
                   	</a>
                   	市场有风险，出借需谨慎。
                   	<a class="risk-alt alt2">
                   		<span class="risk-tips">
                   			出借人应知悉网络借贷活动的风险，具备参与网络借贷活动相适应的出借风险意识、风险识别能力，拥有非保本类金融产品出借的经历，在出借前确保已经了解借款项目信息，确认具有相适应的风险认知和承受能力，谨慎出借并能够承担借贷相关风险。
                   		</span>
                   		<i class="icon iconfont icon-zhu "></i>
                   	</a>
                </h5>
            </div>
             <div class="bond-investlist" id="bond-transferlist">
                
                <div class="bond-list">
                    <div class="bond-thead">
                        <div>
                             <div class="fl trf1">项目编号</div>
                            <div class="fl trf2">历史年回报率</div>
                            <div class="fl trf3">出借期限</div>
                            <div class="fl trf4">折让率</div>
                            <div class="fl trf5">项目金额</div>
                            <div class="fl trf6 fl-padding">进度</div>
                            <div class="fl fl-padding trf7">状态</div>
                        </div>
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
	<script src="${cdn}/dist/js/product/product-list.js"></script>
	<script src="${cdn}/js/listTimer.js?version=${version}"></script>
	<script src="${cdn}/js/bankhzr/webcreditlist.js?version=${version}"></script>
	<!-- 导航栏定位  -->
	<script>setActById("indexDebt");</script>
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