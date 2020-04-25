<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<link rel="stylesheet" type="text/css" href="${ctx}/css/style.css"/>
<title></title>
</head>
<body>
<div class="hbxTop">
			<img src="${ctx}/images/huilingtou_01.jpg"/>
			<img src="${ctx}/images/huilingtou_02.jpg"/>
			<img src="${ctx}/images/huilingtou_03.jpg"/>
		</div>
		
		<div class="hbxMid height375">
			<div class="Mid_text">
				<span>什么是领投汇</span>
				<div class="xian"></div>
				<p>领投汇=“领投+跟投”模式。是汇盈金服搭建的专业项目信息服务通道，让合格投资者作为跟投人跟着知名的领投投资人一起向领投人发掘出来的优质投资项目进行投资。由领投人进行发起投资以及投后管理，跟投人跟随领投人进行投资，从而分享到专业投资人的眼光。</p>
			</div>
		</div>
		
		<div class="hbxFoot">
			<div class="Mid_text">
				<span>为什么选择领投汇</span>
				<div class="xian"></div>
			</div>
		</div>
		<ul class="hbxList">
			<li>
				<div class="hbxLeft">
					<img src="${ctx}/images/lingtouhui_01.jpg"/>
				</div>
				<div class="hbxRight">
					<span>领投人</span>
					<p>只有具备成功投资经验的人，才能成为汇领投的领投人。</p>
				</div>
			</li>
			<div class="clearBoth"></div>
			<li>
				<div class="hbxLeft">
					<span>跟投人</span>
					<p>跟投人只需跟随领投人进行投资，从而分享到专业投资人的眼光。</p>
					
				</div>
				<div class="hbxRight">
					<img src="${ctx}/images/lingtouhui_02.jpg"/>
				</div>
			</li>
			<div class="clearBoth"></div>
			<li class="huilingtouS">
				<div class="hbxLeft" style="padding-right: 30px;">
					<img src="${ctx}/images/lingtouhui_03.jpg"/>
				</div>
				<span>领投汇特色</span>
				<p>领投人进行挖掘优质投资项目进行投资，并持续关注所投资企业的成长，如企业发展顺利，通过其专业能力选择适当时机以适当方式实现退出、实现收益。而跟投人可以保留分红权、收益权，同时将其他股东权益委托给领投人代为行使，轻松收获股权投资的高收益。</p>
			</li>
		</ul>
		<img src="${ctx}/images/more.jpg" class="lingtouhuiMore"/>
</body>
</html>