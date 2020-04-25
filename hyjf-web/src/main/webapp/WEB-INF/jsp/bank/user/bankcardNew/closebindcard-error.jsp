<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-cmn-Hans">

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
	<jsp:include page="/head.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<jsp:include page="/subMenu.jsp"></jsp:include>
	<article class="main-content">
	    	<div class="container result">
	    		<div class="recharge-tab qiye">
	    			<div >我的银行卡</div>
	    		</div>
	    		<!-- start 内容区域 -->
	    		<div class="acount-recharge">
	    			<div class="bind-success">
	    				<img src="${ctx }/dist/images/account/closebind-error-icon@2x.png" alt="" style="height: 100px;margin-top: 60px;"/>
	    				<p style="margin-top: 30px;">${message}</p>
	    				<div>
							<c:if test="${from == null}">
	    					<a href="${ctx}/bank/web/bindCard/myCardInit.do" class="bind-success-btn2">返回</a>
							</c:if>
							<c:if test="${from != null&&from=='mysalf'}">
								<a href="${ctx}/user/safe/init.do" class="bind-success-btn2">返回</a>
							</c:if>
	    				</div>
	    			</div>
	    			<div class="prompt-recharge-new">
		    				<div class="title"><span class="icon iconfont icon-bulb" style="padding-right: 7px;"></span>温馨提示</div>
		    				<p>1.&nbsp;&nbsp;绑定银行卡时，绑定的银行卡的开户信息，需要与存管账户的用户信息保持一致；<br />
2.&nbsp;&nbsp;若要换绑银行卡，可先解绑再绑定新的银行卡，或 <a target="_blank" href='${ctx}/help/index.do?side=hp18&issure=is173'>提交修改申请表</a>；<br />
3.&nbsp;&nbsp;解绑银行卡的前提是，当前账户余额为“0”，并且无待收出借和待还借款，如不符合前述条件，请联系客服申请解绑，客服热线：400-900-7878。</p>
	    			</div>
	    		</div>
	    		<!-- end 内容区域 --> 
	    	</div>
	    	<div class="alert" id="deleteCardDialog" style="margin-top: -130.5px; display: none; "><div class="icon tip"></div><div class="content">您确认要解绑这张银行卡吗？</div><div class="btn-group"><div class="btn" id="deleteCardDialogCancel">取 消</div><div class="btn btn-primary" id="deleteCardDialogConfirm">确 定</div></div></div>
	    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
</body>
</html>