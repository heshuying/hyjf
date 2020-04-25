<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<meta charset="utf-8" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/head.jsp"%>
<style type="text/css">
.hd-innernavS{
	top:118px;
}
.hzt-banner{
	height:150px;
}
.hzt-banner h4{
	padding-top:80px;
}
.hzt-banner{
	background-position:center -1px;
	position:relative;
}

</style>
</head>

<body>
	<%@ include file="/header.jsp"%>
	<c:if test="${projectType eq 'XSH'}"> 
		<div class="hzt-banner" style="background-image:url(${cdn}/img/newxsh_banner.jpg?version=${version})">
	        <div id="subMenu" class="hd-innernav">
	            <ul class="subnav-inner">
	                <li class="active"><a href="${ctx}/project/initProjectList.do?projectType=XSH">新手汇</a></li>
			        <li><a href="${ctx}/xsh/xshPage.do">新手指引</a></li>
	            </ul>
	        </div>
	    </div>
	</c:if>
	<c:if test="${projectType eq 'ZXH'}"> 
		<div class="hzt-banner" style="background-image:url(${cdn}/img/newzxh_banner.jpg?version=${version});">
	        <div id="subMenu" class="hd-innernav">
	            <ul class="subnav-inner">
	                <li class="active"><a href="${ctx}/project/initProjectList.do?projectType=ZXH">尊享汇</a></li>
			        <li><a href="${ctx}/zxh/init.do">关于尊享汇</a></li>
	            </ul>
	        </div>
	    </div>
	</c:if>
	<%-- <c:if test="${projectType ne 'XSH' and  projectType ne 'ZXH'}"> 
	
		<div id="subMenu" class="hd-innernav" style="url(/hyjf-web/img/newxsh_banner.jpg?version=111)">
            <ul class="subnav-inner">
                <li class="active"><a href="${ctx}/project/initProjectList.do?projectType=HZT">汇直投</a></li>
		        <li><a href="${ctx}/project/initProjectList.do?projectType=HXF">汇消费</a></li>
            </ul>
        </div>
        
		<ul class="htl-subnav">
			<li>
				<a href="${ctx}/htl/getHtlInfo.do">
					<!-- <span class="htl-icon htl-icon1"></span> -->
					<span class="title">汇添金</span>
				</a>
			</li>
			<li <c:if test="${projectType eq 'HZT'}"> class="active"</c:if>>
				<a href="${ctx}/project/initProjectList.do?projectType=HZT"> 
					<!-- <span class="htl-icon htl-icon2"></span> -->
					<span class="title">汇直投</span>
				</a>
			</li>
			<li <c:if test="${projectType eq 'HXF'}"> class="active"</c:if>>
				<a href="${ctx}/project/initProjectList.do?projectType=HXF">
					<!-- <span class="htl-icon htl-icon3"></span> -->
					<span class="title">汇消费</span>
				</a>
			</li>
			<li>
				<a href="${ctx}/credit/webcreditlist.do"> 
					<span class="htl-icon htl-icon4"></span>
					<span class="title">汇转让</span>
				</a>
			</li>
		</ul>
	</c:if> --%>
	<c:if test="${projectType eq 'HZT'}">
	
		<div id="subMenu" class="hd-innernav hd-innernavS">
            <ul class="subnav-inner">
                <li class="active"><a href="${ctx}/project/initProjectList.do?projectType=HZT">汇直投</a></li>
		        <li><a href="${ctx}/project/initProjectList.do?projectType=HXF">汇消费</a></li>
            </ul>
        </div>
	
		<div class="hzt-banner" style="background-image: url(${cdn}/img/hztbg1.jpg?version=${version});">
			<h4>为您养成良好的投资习惯</h4>
		</div>
	</c:if>
	<c:if test="${projectType eq 'HXF'}">
	
		<div id="subMenu" class="hd-innernav hd-innernavS">
            <ul class="subnav-inner">
                <li><a href="${ctx}/project/initProjectList.do?projectType=HZT">汇直投</a></li>
		        <li class="active"><a href="${ctx}/project/initProjectList.do?projectType=HXF">汇消费</a></li>
            </ul>
        </div>
	
		<div class="hzt-banner" style="background-image: url(${cdn}/img/hxfbg1.jpg?version=${version});">
			<h4>资金流动更加灵活</h4>
		</div>
	</c:if>
	
	<!-- 汇金理财/汇金所/融通宝 -->
	<c:if test="${projectType eq 'RTB'}">
	<div class="rtb-banner" style="background-image:url(${cdn}/img/project/rtb-banner.jpg?version=${version});">
		<div id="subMenu" class="hd-innernav">
            <ul class="subnav-inner">
                <li class="active"><a href="${ctx}/project/initProjectList.do?projectType=RTB">汇金理财</a></li>
                <li><a href="${ctx}/project/rtbintr.do">关于汇金理财</a></li>
            </ul>
        </div>
        <div class="reb-intr-entry">
        	<a href="${ctx}/project/rtbintr.do"><img  class="scaleout" src="${cdn}/img/project/rtb-entry.png"/></a>
        </div>
	</div>     
	</c:if>
	<div class="new-listing">
	<br/>
	<br/>
		<input id="projectType" name="projectType" type="hidden" value="${projectType}" />
		<ul id="projectList">
		</ul>
		<div class="clearfix"></div>
		<div id="new-pagination" class="new-pagination"></div>
	</div>
	<script>
		<c:if test="${projectType eq 'XSH'}">setActById('hdXSH');</c:if>
		<c:if test="${projectType ne 'XSH' and projectType ne 'ZXH' and projectType ne 'RTB'}">setActById('hdCFH');</c:if>
		<c:if test="${projectType eq 'RTB'}">setActById('hdHJS');</c:if>
		/* <c:if test="${projectType eq 'HZT'}">setActById('subHZT');</c:if> */
		/* <c:if test="${projectType eq 'HXF'}">setActById('subHXF');</c:if> */
		<c:if test="${projectType eq 'ZXH'}">setActById('hdZXH');</c:if>
	</script>
	<%@ include file="/footer.jsp"%>
</body>
<script src="${cdn}/js/listTimer.js?version=${version}"></script>
<script src="${cdn}/js/project/projectList.js?version=${version}" type="text/javascript"></script>

</html>