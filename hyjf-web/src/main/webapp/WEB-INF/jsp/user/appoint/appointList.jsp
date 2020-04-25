<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
		<jsp:include page="/head.jsp"></jsp:include>
	</head>
	<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<div class="user-credit-banner">
        <div class="hd-innernav">
            <jsp:include page="/subMenu.jsp"></jsp:include>
        </div>
        <div class="container-1200">
            <h2 class="appoint-tit">
                <span>预约管理</span> 
                <div class="appoint-val">您当前的失信积分：<span id="recordTotal">0</span>分</div>
            </h2>
        </div>
        
    </div>
    <div class="project-tabbing user-credit-tabbing">
        <div class="container-1200">
        	<input type="hidden" id="appointStatus" name="appointStatus">
            <ul class="project-tab">
                <li panel="0" class="active" data-status = "1" >预约中</li>
                <li panel="1" data-status = "0">已预约</li>
            </ul>
            <div class="clearfix"></div>
            <ul class="project-tab-panel">
                <li panel="0" class="active">
                    <table>
                        <tbody id="appointing">
                        </tbody>
                    </table>
                    <div class="link-box">
                    	<a href="${ctx}/user/appoint/appointIntroduce.do" target="_blank" class="highlight">《预约规则介绍》</a>
						<a href="${ctx}/user/appoint/appointPunish.do" target="_blank" class="highlight">《预约投标失信处理办法》</a>
                    </div>
                    <div class="new-pagination" id ="appointing-pagination"> 
                    </div>
                </li>
                <li panel="1">
                    <table>
                        <tbody id="appointed">
                        </tbody>
                    </table>
                    <div class="link-box">
                    	<a href="${ctx}/user/appoint/appointIntroduce.do" target="_blank" class="highlight">《预约规则介绍》</a>
						<a href="${ctx}/user/appoint/appointPunish.do" target="_blank" class="highlight">《预约投标失信处理办法》</a>
					</div>
                    <div class="new-pagination" id ="appointed-pagination"> 
                    </div>
                </li>
            </ul>
            
        </div>
    </div>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script type="text/javascript" src="${cdn}/js/fill.js?version=${version}"></script>
	<script type="text/javascript" src="${cdn}/js/user/appoint/appointlist.js?version=${version}"></script>
	</body>
</html>