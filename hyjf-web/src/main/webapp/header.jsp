<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<style>
.navicon-animate{
    animation: navicon 2s infinite;
}
@keyframes navicon{
    from {
        top:20px;
    }
    8% {
        top:16px;
    }
	15% {
		top:16px;
	}
    23% {
        top: 22px;
    }
	26%{
		top: 20px;
	}
    to{
        top: 20px;
    }
}
</style>
<section class="top-bar">
	<div class="container-box">
<%-- 		<div class="clearfloat saoma">
	    	<!--第一个为IOS二维码，第二个为Android验证码，一开始默认显示IOS验证码-->
	    	<img src="${ctx}/dist/images/app-android.png?version=${version}" class="show" />
	    	<div class="edition">
	    		<p>扫描下载APP</p>
	    		<a href="https://itunes.apple.com/cn/app/hui-ying-jin-fu/id1044961717?mt=8" target="_blank" class="show">IOS 版</a>
	    		<a href="http://img.hyjf.com/data/download/com.huiyingdai.apptest_wangye.apk" target="_blank">Android 版</a>
	    	</div>
	    </div> --%>
	    <div class="container">
	        <div class="top-left"><span class="icon iconfont icon-dianhua"></span> 客服电话：400-900-7878（服务时间 9:00-18:00）</div>
	        <nav class="top-right">
	            <a href="${ctx}/aboutus/fxrisk.do" itemid="tb0">风险教育</a>
	            <a href="${ctx}/aboutus/aboutus.do" itemid="tb1">关于我们</a>
	            <a href="${ctx}/contentarticle/getSecurityPage.do?pageType=service" target="_blank">服务中心</a>

	            <a href="${ctx}/aboutus/mobileDownload.do" itemid="tb3" class="tb3 tc-main" target="_blank"> <span class="icon iconfont icon-phone"></span>手机端下载</a>
				<span style="font-size: 13px;margin-left: 20px;color: #999;">市场有风险，出借需谨慎</span>
	        </nav>
	    </div>
    </div>
</section>
<header id="header">
	<div class="nav-main">
        <div class="container">
            <a href="${ctx}/" class="logo" itemid="hd1"><img src="${cdn}/dist/images/logo.png?v=20171123"  width="130" alt="汇盈金服" /></a>
            <nav class="nav-right">
                <ul>
                    <li id="indexHome"><a href="${ctx}/" itemid="nv1">首页</a></li>
                    <li id="indexPlan" style="position: relative;padding: 0 0 0 26px"><a href="${ctx}/hjhplan/initPlanList.do" itemid="nv4" ><img src="${cdn}/dist/images/icon-plan@2x.jpg" class="navicon-animate" alt=""  height="20"  style="margin-right:10px;position: absolute;top: 20px;left:20px;" />智投服务</a></li>
                    <li id="indexNew"><a href="${ctx}/bank/web/borrow/newBorrowList.do" itemid="nv3">新手专区</a></li>
                    <li id="indexDebt"><a href="${ctx}/bank/web/borrow/initBorrowList.do" itemid="nv2">散标专区</a></li>
                    <li id="indexMessage"><a href="${ctx}/aboutus/getInformation.do" itemid="nv6">信息披露</a></li>
                </ul>
				<c:choose>
				    <c:when test="${cookie['hyjfUsername'].value == null || cookie['hyjfUsername'].value == ''}">
				    	<div class="user-nav">
		                    <a id="login" href="${ctx}/user/regist/init.do" itemid="nv8"  class="action-link">注册</a>
		                    <span id="line">|</span>
		                    <a id="register" href="${ctx}/user/login/init.do" itemid="nv9">登录</a>
		                </div>
				    </c:when>
				    <c:otherwise>
                       <div class="user-nav">
		                    <a href="${ctx}/user/pandect/pandect.do" id="face" class="hd-face">
				   				<img id="faceUrl" src="${cdn}/img/default.png" alt="" width="30" height="30">
		                    </a>
		                    <a href="${ctx}/user/pandect/pandect.do" id="username"  class="hd-user"></a>
		                    <a href="${ctx}/user/login/logout.do" id="logout"  class="hd-logout" title="退出登录">
		                        <span class="iconfont icon-tuichu"></span>
		                    </a>
		                </div>
				    </c:otherwise>
				</c:choose>
            </nav>
        </div>
    </div>
</header>
<script type="text/javascript">
function getCookie(cname)
{
  var name = cname + "=";
  var ca = document.cookie.split(';');
  for(var i=0; i<ca.length; i++)
  {
    var c = ca[i].trim();
    if (c.indexOf(name)==0) return c.substring(name.length,c.length);
  }
  return "";
}

var iconurl = getCookie("iconurl");
if(iconurl){
	iconurl = decodeURIComponent(decodeURIComponent(iconurl));
	var faceUrl = document.getElementById("faceUrl");
	faceUrl.src = iconurl;
}
var hyjfUsername = getCookie("hyjfUsername");
if(hyjfUsername){
	hyjfUsername = decodeURIComponent(hyjfUsername);
	hyjfUsername = decodeURIComponent(hyjfUsername);
	var username = document.getElementById("username");
	username.innerHTML = hyjfUsername;
}
</script>
	
