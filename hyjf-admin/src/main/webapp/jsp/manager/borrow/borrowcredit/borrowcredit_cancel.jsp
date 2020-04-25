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
	<tiles:putAttribute name="pageTitle" value="汇转让取消" />
	
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">	
		<form id="mainForm" method="post">
			<input type="hidden" name=creditNid id="creditNid" value="${ creditNid}"/>
			<input type="hidden" id="success" value="${success}" />
		</form>
		<div class="row bg-white">
			<div class="col-sm-12">
				<div class="panel panel-white">
					<div class="panel-body panel-table">
						<table class="center" style="width:95%">
							<tr>
								<td align="right" class="tlabel">出让人：</td>
								<td align="left"> <c:out value="${ userName }" /></td>
							</tr>
							<tr>
								<td align="right" class="tlabel">债权编号：</td>
								<td align="left"> <c:out value="${ creditNid}" /></td>
							</tr>
							<tr>
								<td align="right" class="tlabel">项目编号：</td>
								<td align="left"> <c:out value="${ bidNid}" /></td>
							</tr>
							<tr>
								<td align="right" class="tlabel">债转本金：</td>
								<td align="left"> <fmt:formatNumber value="${creditCapital }" pattern="#,##0.00#" />元</td>
							</tr>
							<tr>
								<td align="right" class="tlabel">出让时间：</td>
								<td align="left"> <c:out value="${ addTime }" /></td>
							</tr>
						</table>
						<hr>
						<div class="form-group margin-bottom-0" align="center">
							<div class="col-sm-offset-2 col-sm-10">
								<a class="btn btn-o btn-primary fn-Confirm"><i class="fa fa-check"></i> 确定取消转让</a>
								<a class="btn btn-o btn-primary fn-Cancel"><i class="fa fa-close"></i> 取消本次操作</a>
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
		<script type='text/javascript' src="${webRoot}/jsp/manager/borrow/borrowcredit/borrowcredit_cancel.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
