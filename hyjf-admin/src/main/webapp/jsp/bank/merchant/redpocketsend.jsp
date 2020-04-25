<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="修改推荐人" />
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<div class="panel panel-white" style="margin:0">
			<div class="panel-body">
				<form id="mainForm" action="redPocketSendAction" method="post"  role="form" class="form-horizontal" >
					<%-- 角色列表一览 --%>
					<input type="hidden" id="success" value="${success}" />
					<input type="hidden" id="hasError" value="${hasError}" />
					<div class="panel-scroll height-230 margin-bottom-15">
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="userAccount"> 
								<span class="symbol"></span>银行账户：
							</label>
							<div class="col-xs-10">
								<input type="text" placeholder="银行账户" class="form-control input-sm" id="userAccount" name="userAccount" value="${redPocketSendForm.userAccount}"
										datatype="n2-20" errormsg="银行账户只能是数字，长度2~20个字符！" >
							</div>
							<hyjf:validmessage key="userAccount" label="转出子账户"></hyjf:validmessage>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="amount"> 
								<span class="symbol"></span>金额：
							</label>
							<div class="col-xs-10">
								<input type="text" placeholder="金额" class="form-control input-sm" id="amount" name="amount" value="${redPocketSendForm.amount}"
										datatype="n2-20" errormsg="金额只能是数字，长度2~20个字符！" >
							</div>
							<hyjf:validmessage key="amount" label="转出子账户"></hyjf:validmessage>
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
		<script type='text/javascript' src="${webRoot}/jsp/bank/merchant/redpocketsend.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
