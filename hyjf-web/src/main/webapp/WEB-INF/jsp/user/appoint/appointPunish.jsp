<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>关于投资人在预约投标成功后取消预约的处理办法 - 汇盈金服官网</title>
		<jsp:include page="/head.jsp"></jsp:include>
	</head>
	<body>
	<jsp:include page="/header.jsp"></jsp:include>
	
	<div class="active-banner" style="background-image:url(${cdn}/img/activebanner.jpg);">
        <h5>关于投资人在预约投标成功后取消预约处理办法</h5>
    </div>
    <div class="announce-detail">
        <div class="ann-detail-con">
            <p class="indent">为了实现对预约投标进行有效管理，系统为每一个进行预约投标的用户设置失信分数（失信分数初始值为0）。   </p>       
            <p class="indent">1.在您确认预约投标后，可在预约截止时间（每个项目的预约截止时间详见网站项目详情页面）之前在“用户账户中心”里手动取消预约投标。具体方法是：进入“用户账户中心”->进入“预约管理”->查看“预约中”项目列表->点击“取消预约”。您每取消一次预约或在预约成功后但系统未执行投标之前关闭了自动投标功能，失信分数将加2分。若您取消预约的次数达到6次，系统会将您纳入失信名单。</p>
            <p class="indent">2.若您被纳入失信名单，自纳入的该日起60天内，您将无法使用预约投标功能。60天后系统会将您从失信名单中移除，并将您的失信分数清零。</p>
            <p class="indent">3.为了维护您在平台的良好信用，鼓励您“诚信预约”。</p>
        </div>
    </div>
    <div class="clearfix"></div>
	<jsp:include page="/footer.jsp"></jsp:include>
	
	</body>
</html>