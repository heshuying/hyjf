<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="余额查询" />
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<div class="panel panel-white no-margin">
			<div class="panel-body">
				<form id="mainForm" action="yueSearchAction"
						method="post"  role="form" class="form-horizontal" >
					
					<input type="hidden" id="success" value="${success}" />
					<input type="hidden" id="hasError" value="${hasError}" />
					<div class="panel-scroll height-340 margin-bottom-15">
						<div class="form-group">
							<label class="col-xs-3 control-label text-right padding-right-0" for="userName">
								<span class="symbol"></span>公司账户余额：
							</label>
							<div class="col-xs-9">
								<fmt:formatNumber value="${companyYue}" type="currency"/> 元
							</div>
						</div>
					</div>
					<div class="form-group margin-bottom-0">
						<div class="col-xs-offset-4 col-xs-10">
							<a class="btn btn-o btn-primary fn-Confirm"><i class="fa fa-check"></i> 刷 新</a>
							<a class="btn btn-o btn-primary fn-Cancel"><i class="fa fa-close"></i> 关 闭</a>
						</div>
					</div>
				</form>
			</div>
		</div>
	</tiles:putAttribute>
	
	<%-- JS全局变量定义、插件 (ignore) --%>
	<tiles:putAttribute name="pageGlobalImport" type="string">
		<!-- Form表单插件 -->
		<%@include file="/jsp/common/pluginBaseForm.jsp"%>
	</tiles:putAttribute>
	
	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type='text/javascript' src="${webRoot}/jsp/finance/web/yueDetails.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
