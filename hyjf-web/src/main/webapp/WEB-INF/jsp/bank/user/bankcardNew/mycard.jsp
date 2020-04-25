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
	    			<c:if test="${bindType == 0}">
		    			<div class="quick-new" >
		    				<a href="${ctx }/bank/web/bindCard/bindCardNew.do" class="go-bindCard"><img src="${ctx }/dist/images/account/bindCard.png" style="width:300px;margin-top:40px" /></a>
		    			</div>
	    			</c:if>
	    			<c:if test="${bindType == 1}">
	    			<input type="hidden" name="cardId" id="cardId" value="${cardId }" />
		    			<div class="quick-new">
	    					<div class="bank-card">
	    						<p class="bank-name"><img src="${bankicon }" alt="" />${bankname }</p>
	    						<div class="cardid">${bankcard}</div>
	    						<span id='unbundling'>解绑</span>
	    					</div>
	    				</div>
	    			</c:if>
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
	<script src="${ctx}/dist/js/lib/jquery.validate.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctx}/dist/js/lib/jquery.metadata.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctx}/dist/js/lib/moment.min.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript" charset="utf-8">
	var stageNum; //暂存要删除的银行卡
	$(document).ready(function() {
		$("#unbundling").click(function(){
			//删除银行卡操作
			stageNum = $('#cardId').val();
			var content = "您确认要解绑这张银行卡吗？";
			utils.alert({
				id:"deleteCardDialog",
				type:"confirm",
				content:content,
				fnconfirm:function(){
                    /*
					  解绑提交
					  unbind_submit
					  @params bank_name 银行名称  String  eg:招商银行，工商银行，建设银行，农业银行
					  @params unbind_time 解绑时间  Date
					*/
                    sa && sa.track('unbind_submit',{
                        //entrance: document.referrer,
                        bank_name: "${bankname}",
                        unbind_time: moment().format('YYYY-MM-DD HH:mm:ss.SSS')
                    })
					var card=$('#cardId').val()
					window.location.href=webPath+'/bank/web/deleteCardPage/deleteCardPage.do?cardId='+card
				}
			});
		});
		
	});</script>
	<script>setActById("userPandect");</script>
</body>
</html>