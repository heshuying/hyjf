<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>注册成功 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<article class="main-content">
        <c:if test="${couponSendCount==null||couponSendCount <= 0 }">
        <div class="container result" style="overflow:hidden">
            <div class="register-content result-content">
            	<input type="hidden" value="${userid }" id="userid"/>
            	<div class="register-top">
            		<img src="${cdn }/dist/images/result/register-success@2x.png" width="390" alt="" />
            	</div>
            	<div class="register-success result-mid">
            		<h3>恭喜,注册成功！</h3>
            	</div>
            	<div class="register-mid">
            		<p>开通江西银行存管账户，享资金安全保障</p>
            	</div>
            	<div class="result-btn">
            		<a href="${ctx}/bank/web/user/bankopen/init.do" class="register-btn import" itemid="bt1">马上开户</a>
            	</div>
            </div>
        </div>
        </c:if>
        <c:if test="${couponSendCount > 0 }">
        <div class="container result">
            <div class="sucess-content" style="padding-bottom:1px">
            	<video src="${ctx }/dist/images/hongbao.mov" autoplay  style="height:284px;width:450px">
            		<img src="${cdn }/dist/images/login/login-success-logo.png" />
            	</video>
            	<p>您已获得<a> 888元 </a>新手礼包，您可在<a href="${ctx}/user/invite/toInvite.do">“我的奖励”</a>中查看</p>
            	<h3>出借前的流程</h3>
            	<img src="/dist/images/login/steps@2x.png" alt="" width="480px"/>
            	<div class="btn-box"><a href="${ctx}/bank/web/user/bankopen/init.do">开通银行存管</a></div>
            </div>
        </div>
        </c:if>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>

	<script type="text/javascript" src="${cdn}/js/jquery.validate.js" charset="utf-8"></script>
    <script type="text/javascript" src="${cdn}/js/messages_cn.js" charset="utf-8"></script>
    <script type="text/javascript" src="${cdn}/js/jquery.metadata.js" charset="utf-8"></script>
	<!-- 参数说明_orderno：注册用户ID，需要替换为注册用户的ID值 -->
	<script>
	!function(w,d,e){
	var _orderno=$("#userid").val();
	var b=location.href,c=d.referrer,f,s,g=d.cookie,h=g.match(/(^|;)\s*ipycookie=([^;]*)/),i=g.match(/(^|;)\s*ipysession=([^;]*)/);if (w.parent!=w){f=b;b=c;c=f;};u='//stats.ipinyou.com/cvt?a='+e('At.GZ.euyr66NZvXwbrHsCaw9YDP')+'&c='+e(h?h[2]:'')+'&s='+e(i?i[2].match(/jump\%3D(\d+)/)[1]:'')+'&u='+e(b)+'&r='+e(c)+'&rd='+(new Date()).getTime()+'&OrderNo='+e(_orderno)+'&e=';
	function _(){if(!d.body){setTimeout(_(),100);}else{s= d.createElement('script');s.src = u;d.body.insertBefore(s,d.body.firstChild);}}_();
	}(window,document,encodeURIComponent);
	</script>
</body>
</html>