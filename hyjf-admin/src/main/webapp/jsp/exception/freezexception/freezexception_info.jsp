<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="解冻订单" />

	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<shiro:hasPermission name="freezexception:FREEZE">
		<div class="panel panel-white" style="margin: 0">
			<div class="panel-body">
				<div class="panel-scroll height-260">
					<form id="mainForm" action="freezeAction" method="post" role="form" class="form-horizontal">

						<input type="hidden" id="success" value="${success}" />
						<div class="form-group">
							<label class="col-sm-2 control-label" for="trxId">
								<span class="symbol required"></span> 解冻订单号
							</label>
							<div class="col-sm-10">
								<input type="text" name="trxId" id="trxId" class="form-control" maxlength="20" value="${trxId}" datatype="*,/^[a-zA-Z0-9_]+$/" errormsg="解冻订单号只能是英文或者数字，长度1~20位！" />
								<hyjf:validmessage key="trxId"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="notes">
								<span class="symbol required"></span> 解冻备注
							</label>
							<div class="col-sm-10">
								<input type="text" name="notes" id="notes" class="form-control" maxlength="255" value="${notes}" datatype="*1-255" errormsg="解冻备注，长度不能超过1~255位！" />
								<hyjf:validmessage key="notes"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group margin-bottom-0" align="center">
							<div class="col-sm-offset-2 col-sm-10">
								<a class="btn btn-o btn-primary fn-Confirm"><i class="fa fa-check"></i> 确 认</a>
								<a class="btn btn-o btn-primary fn-Cancel"><i class="fa fa-close"></i> 取 消</a>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
		</shiro:hasPermission>
	</tiles:putAttribute>
	
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
	<style>
	.panel-title {
		font-family: "微软雅黑"
	}
	</style>
	</tiles:putAttribute>
	
	<%-- JS全局变量定义、插件 (ignore) --%>	
	<tiles:putAttribute name="pageGlobalImport" type="string">	
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
	</tiles:putAttribute>
	
	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type='text/javascript' src="${webRoot}/jsp/exception/freezexception/freezexception_info.js"></script>
	</tiles:putAttribute>
	
</tiles:insertTemplate>
