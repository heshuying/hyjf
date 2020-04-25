<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>尊享汇预约介绍页 - 汇盈金服官网<</title>
		<jsp:include page="/head.jsp"></jsp:include>
	</head>
	<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<div class="appoint-section">
        <div class="appoint-block appoint1">
            <div class="container-1200">
                <div class="vip-add-btn">
                    <a href="${ctx}/project/initProjectList.do?projectType=ZXH" id="appointBtn" class="vip-add-copy scaleout">我要预约</a>
                    <a href="${ctx}/project/initProjectList.do?projectType=ZXH" class="vip-add ">我要预约</a>
                </div>
            </div>
        </div>
        <div class="appoint-block appoint2">
            <div class="container-1200">
                <p>预约投标是汇盈金服平台为满足您便捷、及时、高效的投资需求而推出的自动投标产品，<br/>遵循“先有项目再行预约”的原则：</p>
            </div>
        </div>
        <div class="appoint-block appoint3">
            <div class="container-1200">
                <div class="item item1"><div class="title">无需等待</div>不必等待项目开放,系统会按照您的预约金额自动投标，省时省心，提升您的体验。</div>
                <div class="item item3"><div class="title">可撤销</div>您可在预约截止时间之前撤销预约，灵活性强。</div>
            </div>
        </div>
        <div class="appoint-block appoint4"></div>
        <div class="appoint-block appoint5">
            <div class="container-1200">
                <p><strong>1.</strong>点击<span>【项目编号】</span>或<span>【我要预约】</span>进入项目详情页面</p>
                <p><strong>2.</strong>查看项目相关信息和<span>《预约投标服务需知》</span>，并自行决定是否预约</p>
                <p><strong>3.</strong>若决定预约，输入预约金额，并勾选<span>“我已阅读并同意《预约投标服务需知》”</span>，点击<span>【确认预约】</span>按钮<br/>后视为预约成功，系统冻结投资人账户中的预约金额</p>
                <p><strong>4.</strong>在预约截止时间到达前可在<span>“账户中心”</span>里撤销预约，系统解冻预约资金</p>
                <p><strong>5.</strong>系统自动完成投标</p>
                <p><strong>6.</strong>预约截止时间到达后不再接受预约申请和取消预约申请</p>
                <p><strong>7.</strong>预约投标不支持使用优惠券</p>
            </div>
        </div>
        <div class="appoint-block appoint6"></div>
    </div>
	<jsp:include page="/footer.jsp"></jsp:include>
	</body>
</html>