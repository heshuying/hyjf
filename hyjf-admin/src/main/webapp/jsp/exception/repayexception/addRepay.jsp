<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="会员详情" />
		<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<div class="panel panel-white" style="margin:0">
			<div class="panel-body">
				<form id="mainForm" action="addRepayAction" method="post"  role="form" class="form-horizontal" >
					<%-- 角色列表一览 --%>
					<input type="hidden" name="userId" id="userId" value="${updateAccountForm.userId }" />
					<input type="hidden" id="success" value="${success}" />
					<input type="hidden" id="hasError" value="${hasError}" />
					<div class="panel-scroll height-230 margin-bottom-15">
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="orderId">
								<span class="symbol required"></span>出借订单号
							</label>
							<div class="col-xs-10">
								<input type="text" placeholder="出借订单号" class="form-control input-sm" id="orderId" name="orderId" value="${orderId}" datatype="s18-30">
								<hyjf:validmessage key="orderId" label="出借订单号"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="period"> 
								<span class="symbol  required"></span>还款期数
							</label>
							<div class="col-xs-10">
								<input type="text" placeholder="还款期数" class="form-control input-sm" id="period" name="period" value="${period}" datatype="s1-2">
								<hyjf:validmessage key="period" label="还款期数"></hyjf:validmessage>
							</div>
						</div>
					</div>
					<div class="form-group margin-bottom-0">
						<div class="col-xs-offset-2 col-xs-10">
							<a class="btn btn-o btn-primary fn-Confirm"><i class="fa fa-check"></i> 确 认</a>
							<a class="btn btn-o btn-primary fn-Cancel"><i class="fa fa-close"></i> 取 消</a>
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
		<script type='text/javascript' src="${webRoot}/jsp/exception/repayexception/addRepay.js"></script>
	</tiles:putAttribute>
	
</tiles:insertTemplate>