<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="/head.jsp"></jsp:include>
<link rel="stylesheet" type="text/css" href="${cdn}/css/footer-adjust.css" />
<style>
	.qiye-btn{
		margin-right: 20px;
	}
	.error-content{
		width: 780px;
		margin-left: auto;
		margin-right: auto;
		text-align: center;
		color: #fc1402;
	}
	
</style>
<title>用户注册成功 - 汇盈金服官网</title>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<div class="ktzj_bg">
		<div class="ktzj_ban biankuang hongbao">
			<input type="hidden" value="${userid }" id="userid"/>
			<div class="ktzj_banTop">
				<a href="${ctx }/coupon/getUserCoupons.do"><img src="${cdn}/img/user/zhucehongbao.png" class="hongbao" /> <span class="highlight">恭喜您注册成功！</span></a>
				<p>888元新手礼包已经发放至您账户，前往<span>“个人中心-优惠券”</span>查看使用</p>
				
				<div class="regist-readybox clearfix">
					<div class="title">投资前的准备工作</div>
					
					<div>
					<img src="${cdn}/img/user/readyflow.png"/>
					</div>
				</div>
				<form action="${ctx}/user/openaccount/open.do">
					<input class="inputBgAll" type="submit" value="开通个人账户">
					<div class="clearfix"></div>
					<a href="javascript:;" class="qiye-btn highlight" id="qiyeBtn">&lt;&lt;开通企业账户</a>
					<a href="javascript:;" class="org-btn highlight" id="orgBtn">&lt;&lt;开通担保机构账户</a>
				</form>
			</div>
			
		</div>
		<div class="s_bot hongbao">
			<p class="ktzj_p">&copy;汇盈金服 All rights reserved| 惠众商务顾问 (北京) 有限公司 | 京ICP备 13050958 号 | 公安安全备案证：37021313127</p>
		</div>
	</div>
	<div class="pop-overlayer"></div>
	<div class="pop-box pop-tips-hastitle" id="licencePop">
        <a class="pop-close" href="javascript:void(0);" onclick="popOutWin()"></a>
        <h4 class="pop-title" style="text-align:center;">请输入企业营业执照编号</h4>
        <form class="pop-main" id="licenceForm" action="${ctx}/user/openaccount/openCorp.do">
        	<input id="guarType" name="guarType" type="hidden" value="N">
            <div style="text-align:center;color:#ddd;font-size:14px;">营业执照号码提交后不允许修改，请谨慎填写。</div>
            <div class="pop-txt">
                <input type="text" name="busiCode" id="busiCode" class="ipt-middle" maxlength="20"/>
            </div>
            <div class="btns">
                <button type="submit" class="confirm" id="confirmedBtn">确认</button>
                <a href="javascript:;" class="cancel" onclick="popOutWin()">再想想</a>
            </div>
            <div class="clearfix"></div>
        </form>
    </div>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script type="text/javascript" src="${cdn}/js/jquery.validate.js" charset="utf-8"></script>
    <script type="text/javascript" src="${cdn}/js/messages_cn.js" charset="utf-8"></script>
    <script type="text/javascript" src="${cdn}/js/jquery.metadata.js" charset="utf-8"></script>
	<script>
	
		$("#qiyeBtn").click(function() {
			popUpWin({
				"ele" : "#licencePop"
			});
		});
		$("#orgBtn").click(function() {
			$("#guarType").val("Y");
			popUpWin({
				"ele" : "#licencePop"
			});
		});
		
		var validate = $("#licenceForm").validate({
			"rules" : {
				"busiCode" : {
					"required" : true,
					"minlength" : 15,
					"maxlength" : 20
				}
			},
			"messages" : {
				"busiCode" : {
					"required" : "请填写营业执照编号",
					"minlength" : "营业执照编号最短15位",
					"maxlength" : "营业执照编号最长20位"
				}
			},
			"ignore" : ".ignore",
			"onfocusout" : false,
			submitHandler : function(form) {
				form.submit();
			}
		});
	</script>
	<!--
	参数说明
	_orderno：注册用户ID，需要替换为注册用户的ID值
	-->
	<script>
	!function(w,d,e){
	var _orderno=$("#userid").val();
	var b=location.href,c=d.referrer,f,s,g=d.cookie,h=g.match(/(^|;)\s*ipycookie=([^;]*)/),i=g.match(/(^|;)\s*ipysession=([^;]*)/);if (w.parent!=w){f=b;b=c;c=f;};u='//stats.ipinyou.com/cvt?a='+e('At.GZ.euyr66NZvXwbrHsCaw9YDP')+'&c='+e(h?h[2]:'')+'&s='+e(i?i[2].match(/jump\%3D(\d+)/)[1]:'')+'&u='+e(b)+'&r='+e(c)+'&rd='+(new Date()).getTime()+'&OrderNo='+e(_orderno)+'&e=';
	function _(){if(!d.body){setTimeout(_(),100);}else{s= d.createElement('script');s.src = u;d.body.insertBefore(s,d.body.firstChild);}}_();
	}(window,document,encodeURIComponent);
	</script>
</body>
</html>