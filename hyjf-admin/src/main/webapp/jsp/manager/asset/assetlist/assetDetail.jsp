<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>


<shiro:hasPermission name="assetlist:INFO">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="资产信息" />

		<%-- 画面主面板标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">资产信息</h1>
			<span class="mainDescription">本功能可以查看资产信息。</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
		
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<div class="container">
								<div class="row clearfix">
									<div class="col-md-6 column">
										<!-- 基础信息 -->
										<div class = "clearFloat"></div>
										<div class = "ulStyle width100">
										<div class = "title"><span>基础信息 </span></div>
											<ul>
												<li><span>姓名</span><span class = "fontContent">${detailForm.truename}</span></li>
												<li><span>性别</span><span class = "fontContent">
													<c:if test="${detailForm.sex == 0}"> 保密 </c:if>
													<c:if test="${detailForm.sex == 1}"> 男 </c:if>  
													<c:if test="${detailForm.sex == 2}"> 女 </c:if>
												</span></li>
												<li><span>年龄</span><span class = "fontContent">${detailForm.age}</span></li>
												<li><span>婚姻状况</span><span class = "fontContent">
													<c:if test="${detailForm.marriage == 1}"> 已婚</c:if>  
													<c:if test="${detailForm.marriage == 2}"> 未婚</c:if>
												</span></li>
												<li><span>工作城市</span><span class = "fontContent">${detailForm.workCity}</span></li>
												<li><span>岗位职业</span><span class = "fontContent">${detailForm.position}</span></li>
												<li><span>身份证号</span><span class = "fontContent">${detailForm.idcard}</span></li>
												<li><span>户籍地</span><span class = "fontContent">${detailForm.domicile}</span></li>
												<li><span>信用评级</span><span class = "fontContent">${detailForm.creditLevel}</span></li>
											</ul>
										<div class = "clearFloat"></div>
										<div class = "ulStyle width100">
										<div class = "title"><span>项目介绍 </span></div>
											<ul>
												<li><span>借款用途</span><span class = "fontContent">${detailForm.useage}</span></li>
												<li><span>月薪收入</span><span class = "fontContent">${detailForm.monthlyIncome}</span></li>
												<li><span>第一还款来源</span><span class = "fontContent">${detailForm.firstPayment}</span></li>
												<li><span>第二还款来源</span><span class = "fontContent">${detailForm.secondPayment}</span></li>
												<li><span>费用说明</span><span class = "fontContent">${detailForm.costIntrodution}</span></li>
											</ul>
										<div class = "clearFloat"></div>
										<div class = "ulStyle width100">
										<div class = "title"><span>信用状况</span></div>
											<ul>
												<li><span>在平台逾期次数</span><span class = "fontContent">${detailForm.overdueTimes}</span></li>
												<li><span>在平台逾期金额</span><span class = "fontContent">${detailForm.overdueAmount}元</span></li>
												<li><span>涉诉情况</span><span class = "fontContent">${detailForm.litigation}</span></li>
												<li><span>信用评级</span><span class = "fontContent">${detailForm.creditLevel}</span></li>
											</ul>
										<div class = "clearFloat"></div>
										<div class = "ulStyle width100">
										<div class = "title"><span>审核状况</span></div>
											<ul>
												<li><span>身份证</span><span class = "fontContent">已审核</span></li>
												<li><span>收入状况</span><span class = "fontContent">已审核</span></li>
												<li><span>信用状况</span><span class = "fontContent">已审核</span></li>
												<li><span>婚姻状况</span><span class = "fontContent">已审核</span></li>
												<li><span>工作状况</span><span class = "fontContent">已审核</span></li>
											</ul>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet" media="screen">
			<style>
				hr{margin:10px 0;}
				.well a{color:#7d7d7d;}
				ul{list-style:none;margin:0;padding:0;}
				.imgStyle{display:block;float:left;margin-right:20px;}
				.width100{width:100%}
				.ulStyle{float:left;}
				.ulStyle2{border:1px solid #ddd;width:75%;margin-left:25%;}
				.ulStyle .ulStyle2 li{height:58px;line-height:40px;}
				.ulStyle2 li{width:90%;margin:0 auto;padding:0 5px;}
				.ulStyle2 li:last-child{border-style:none;}
				.ulStyle .title{font-size:18px;color:#363636;}
				.ulStyle ul li{line-height:30px;height:30px;font-size:12px;border-bottom:1px solid #ddd;}
				.ulStyle ul li span,.ulStyle li span{min-width:120px;display:inline-block;}
				.ulStyle ul li span.fontContent{color:#333}
				.clearFloat{height:1px;line-height:1px;clear:both;margin:20px 0;}
				.colorBut{border-radius:5px;height:22px;line-height:22px;text-align:center;min-width:auto !important;padding:0 3px;color:#fff !important;margin:0 3px;}
				.colorOrange{background:#ff6500;}
				.colorBlue{background:#0097d0}
				.colorGreen{background:#619109}
			</style>
			<style>
			.ulStyle ul li{
				line-height: 22px;
				padding:6px 0;
			    min-height: 30px;
			    height:auto;
			}
			.ulStyle ul li span{
				vertical-align:top;
			}
			.ulStyle ul li span:first-child{
				width:30%;
			}
			.ulStyle ul li span:last-child{
				max-width:70%;
			}
			</style>
		</tiles:putAttribute>

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/qrcode/jquery.qrcode.js"></script>
			<script type='text/javascript' src="${themeRoot}/vendor/plug-in/qrcode/qrcode.js"></script>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jstree/jstree.min.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp/manager/users/userlist/userDetail.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
