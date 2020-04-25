<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="确定已交保证金" />
	
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">	
		<form id="mainForm" method="post">
            <input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
			<input type="hidden" name=borrowNid id="borrowNid" value="${ fireInfo.borrowNid}"/>			
			<input type="hidden" name=borrowPreNid id="borrowPreNid" value="${ fireInfo.borrowPreNid}"/>
			<input type="hidden" id="success" value="${success}" />
		</form>
		<div class="row bg-white">
			<div class="col-sm-12">
				<div class="panel panel-white">
					<div class="panel-body panel-table">
						<table class="center" style="width:95%">
							<tr>
								<td align="right" class="tlabel">项目编号：</td>
								<td align="left"> <c:out value="${ fireInfo.borrowNid}" /></td>
							</tr>
							<tr>
								<td align="right" class="tlabel">项目名称：</td>
								<td align="left"> <c:out value="${ fireInfo.name}" /></td>
							</tr>
							<tr>
								<td align="right" class="tlabel">借款人：</td>
								<td align="left"> <c:out value="${ userName}" /></td>
							</tr>
							<tr>
								<td align="right" class="tlabel">借款金额：</td>
								<td align="left"> <fmt:formatNumber pattern="#,##0.00#" value="${fireInfo.account}" /></td>
							</tr>
							<tr>
								<td align="right" class="tlabel">出借利率：</td>
								<td align="left"> <c:out value="${ fireInfo.borrowApr}" />%</td>
							</tr>
							<tr>
								<td align="right" class="tlabel">借款期限：</td>
								<td align="left"> <c:out value="${ fireInfo.borrowPeriod}" /><c:if test="${ fireInfo.borrowStyle eq 'endday' }">天</c:if><c:if test="${ fireInfo.borrowStyle ne 'endday' }">个月</c:if></td>
							</tr>
							<tr>
								<td align="right" class="tlabel">保证金金额：</td>
								<td align="left"> <fmt:formatNumber pattern="#,##0.00#" value="${accountBail}" />元</td>
							</tr>
						</table>
						<hr>
						<div class="form-group margin-bottom-0" align="center">
							<div class="col-sm-offset-2 col-sm-10">
								<a class="btn btn-o btn-primary fn-Confirm"><i class="fa fa-check"></i> 已交保证金</a>
								<a class="btn btn-o btn-primary fn-Cancel"><i class="fa fa-close"></i> 未交保证金</a>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</tiles:putAttribute>
	
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
	<style>
	.panel-table .tlabel {
		height: 30px;
		font-weight: 700;
		width: 35%;
	}
	</style>
	</tiles:putAttribute>
	
	<%-- JS全局变量定义、插件 (ignore) --%>
	<tiles:putAttribute name="pageGlobalImport" type="string">
		<!-- Form表单插件 -->
		<%@include file="/jsp/common/pluginBaseForm.jsp"%>
	</tiles:putAttribute>

	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type='text/javascript' src="${webRoot}/jsp/manager/borrow/borrowfirst/borrowbail.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
