<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp"
	flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="返回码按钮" />

	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType"
			value="${empty bankretcodeconfigForm.id ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin: 0">
			<div class="panel-heading">
				<h5 class="panel-title">${jspEditType}返回码信息</h5>
			</div>
			<div class="panel-body">
				<p class="text-small margin-bottom-20">
					在这里可以添加和修改返回码配置，以便于更好的管理系统的应用。</p>
				<hr />
				<div class="panel-scroll height-270">
					<form id="mainForm"
						action="${empty bankretcodeconfigForm.id ? 'insertAction' : 'updateAction'}"
						method="post" role="form" class="form-horizontal">
						<%-- 角色列表一览 --%>
						<input type="hidden" name="id" id="id" value="${bankretcodeconfigForm.id}" />
						
						<input type="hidden" id="success" value="${success}" />
						<div class="form-group">
							<label class="col-sm-2 control-label" for="inputEmail3">
								<span class="symbol required"></span> 接口名
							</label>
							<div class="col-sm-10">
								<input type="text" name="txCode" class="form-control"
									value="${bankretcodeconfigForm.txCode}" datatype="s2-20"
									errormsg="接口名只能长度2~20个字符！" />
								<hyjf:validmessage key="txCode"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="inputPassword3">
								<span class="symbol required"></span> 返回码
							</label>
							<div class="col-sm-10">
								<input type="text" name="retCode" class="form-control"
									${empty bankretcodeconfigForm.id ? '' : 'readonly'}
									value="${bankretcodeconfigForm.retCode}" datatype="s2-20"
									errormsg="返回码只能长度2~20个字符！" />
								<hyjf:validmessage key="retCode"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="inputPassword3">
								<span class="symbol required"></span> 返回描述
							</label>
							<div class="col-sm-10">
								<input type="text" name="retMsg" class="form-control"
									value="${bankretcodeconfigForm.retMsg}" datatype="s2-20"
									errormsg="权限名称只能是数字、字母和汉字，长度2~20个字符！" />
								<hyjf:validmessage key="retMsg"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="inputPassword3">
								描述 </label>
							<div class="col-sm-10">
								<textarea name="errorMsg" maxlength="255"
									placeholder="平台错误的详细描述信息" rows="4" class="form-control"><c:out
										value="${bankretcodeconfigForm.errorMsg}"></c:out></textarea>
							</div>
							<hyjf:validmessage key="errorMsg"></hyjf:validmessage>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5"> <span
								class="symbol required"></span>状态
							</label>
							<div class="col-xs-9">
								<div style="float: left;">
									<input type="radio" name="status" datatype="*" value="1"
										class="event-categories" ${bankretcodeconfigForm.status == '1' ? 'checked' : ''}> <label>
										启用 </label> 
										<input type="radio" name="status" datatype="*" value="0"
										class="event-categories" ${bankretcodeconfigForm.status == '0' ? 'checked' : ''}> <label>
										禁用 </label>
								</div>
							</div>
							<hyjf:validmessage key="status" label="状态"></hyjf:validmessage>
						</div>
						
						<div class="form-group margin-bottom-0">
							<div class="col-sm-offset-2 col-sm-10">
								<a class="btn btn-o btn-primary fn-Confirm"><i
									class="fa fa-check"></i> 确 认</a> <a
									class="btn btn-o btn-primary fn-Cancel"><i
									class="fa fa-close"></i> 取 消</a>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
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
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
	</tiles:putAttribute>

	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type='text/javascript'
			src="${webRoot}/jsp/manager/config/bankretcodeconfig/bankretcodeconfigInfo.js"></script>
	</tiles:putAttribute>

</tiles:insertTemplate>
