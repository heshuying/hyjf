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
	<tiles:putAttribute name="pageTitle" value="预注册修改页面" />
	
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">	
		<form id="mainForm" method="post" action="savepreregistlist">
			<hyjf:message key="message"></hyjf:message>
			<input type="hidden" name=id id="id" value="${preRegist.id}"/>			
			<input type="hidden" name=mobile id="mobile" value="${preRegist.mobile}"/>
			<input type="hidden" id="success" value="${success}" />
			<div class="row bg-white">
				<div class="col-sm-12">
					<div class="panel panel-white">
						<div class="panel-body panel-table">
							<table class="center" style="width:95%">
								<tr>
									<td align="right" class="tlabel">手机号：</td>
									<td align="left"> <c:out value="${preRegist.mobile}" /></td>
								</tr>
								<tr>
									<td align="right" class="tlabel">推荐人：</td>
									<td align="left"><input type="text" name="referrer" id="referrer" value="${preRegist.referrer}"/></td>
								</tr>
								<tr>
									<td align="right" class="tlabel">渠道：</td>
									<td align="left"><input type="text" name="source" id="source" value="${preRegist.source}"/></td>
								</tr>
								<tr>
									<td align="right" class="tlabel">渠道关键字：</td>
									<td align="left"><input type="text" name="utm" id="utm" value="${preRegist.utm}"/></td>
								</tr>
								<tr>
									<td align="right" class="tlabel">备注：</td>
									<td align="left"><input type="text" name="remark" id="remark" value="${preRegist.remark}"/></td>
								</tr>
							</table>
							<hr>
							<div class="form-group margin-bottom-0" align="center">
								<div class="col-sm-offset-2 col-sm-10">
									<a class="btn btn-o btn-primary fn-Confirm"><i class="fa fa-check"></i> 确认</a>
									<a class="btn btn-o btn-primary fn-Cancel"><i class="fa fa-close"></i> 取消</a>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
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
		<script type='text/javascript' src="${webRoot}/jsp/manager/users/preregist/preregistupdate.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
