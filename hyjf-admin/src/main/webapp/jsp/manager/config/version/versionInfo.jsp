<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>
<%-- 画面功能菜单设置开关 --%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="酒店管理" />
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
	<style>
	.panel-title { font-family: "微软雅黑" }
	.admin-select .select2-container { 
		width: 100% !important;
	}
	.admin-select .select2-container--default .select2-selection--single { 
		border-radius: 0px;
		border: 1px solid #BBBAC0 !important;
	}
	.admin-select .select2-container--default .select2-selection--single .select2-selection__rendered, .admin-select .select2-container--default .select2-selection--single { 
		height: 34px;
		line-height:34px;
	}
	.admin-select .select2-container .select2-selection--single .select2-selection__rendered {
		padding-left: 4px;
	}
	</style>
	<link href="${themeRoot}/vendor/plug-in/select2/select2.min.css" rel="stylesheet" media="screen">
	</tiles:putAttribute>
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType" value="${empty versionconfigForm.id ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin:0">
			<div class="panel-body">
				<p class="text-small margin-bottom-20">
					添加/修改版本信息
				</p>
				<hr/>
				<div class="panel-scroll height-535">
				<form id="mainForm" action="${empty versionconfigForm.id ? 'insertAction' : 'updateAction'}"
						method="post"  role="form" class="form-horizontal" >
					<%-- 角色列表一览 --%>
					<input type="hidden" name="id" id="id" value="${versionconfigForm.id }" />
					<input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
					<input type="hidden" id="success" value="${success}" />
					<div class="form-group">
						<label class="col-sm-2 control-label" for="type"> <span class="symbol required"></span>系统名称</label>
						<div class="col-sm-10 admin-select">
							<select id="type" name="type" class="form-control" datatype="*" errormsg="请选择客系统！" <c:if test="${not empty versionconfigForm.id}"> disabled="disabled" </c:if>  >
								<option value="" disabled>选择系统</option>
									<c:forEach items="${versionName}" var="ver" begin="0" step="1" varStatus="status">
										<c:choose>
											<c:when test="${versionconfigForm.type == ver.nameCd }">
												<option value="${ver.nameCd }" data-class="fa" selected="selected">${ver.name }</option>
											</c:when>
											<c:otherwise>
												<option value="${ver.nameCd }" data-class="fa">${ver.name }</option>
											</c:otherwise>
										</c:choose>
									</c:forEach>
							</select>
							<hyjf:validmessage key="type" label="系统名称"></hyjf:validmessage>
						</div>
					</div>	
					
					<div class="form-group">
						<label class="col-sm-2 control-label" for="version"> <span class="symbol required"></span>版本号</label>
						<div class="col-sm-10">
							<input type="text" <c:if test="${not empty versionconfigForm.id}"> disabled="disabled" </c:if>  placeholder="版本号" id="version" name="version" value="${versionconfigForm.version}"  class="form-control"
								datatype="*1-12" maxlength="12">
							<hyjf:validmessage key="version" label="版本号"></hyjf:validmessage>
							<hyjf:validmessage key="type-verdioncode" label=""></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="url"> 更新地址</label>
						<div class="col-sm-10">
							<input type="text" placeholder="更新文件地址" id="url" name="url" value="${versionconfigForm.url}"  class="form-control"
								ignore="ignore"  maxlength="255" >
							<hyjf:validmessage key="url" label="更新地址"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="content"> 版本描述</label>
							<div class="col-xs-10">
								<textarea maxlength="500" placeholder="版本描述" id="content"
									name="content" class="form-control limited">${versionconfigForm.content}</textarea>
								<hyjf:validmessage key="content" label="版本描述"></hyjf:validmessage>
							</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="isupdate"> <span class="symbol required"></span>是否更新</label>
						<div class="col-sm-10 admin-select">
							<select id="isupdate" name="isupdate" class="form-control" datatype="*" errormsg="请选择！" >
									<c:forEach items="${isUpdate}" var="isu" begin="0" step="1" varStatus="status">
										<c:choose>
											<c:when test="${versionconfigForm.isupdate == isu.nameCd }">
												<option value="${isu.nameCd }" data-class="fa" selected="selected">${isu.name }</option>
											</c:when>
											<c:otherwise>
												<option value="${isu.nameCd }" data-class="fa">${isu.name }</option>
											</c:otherwise>
										</c:choose>
									</c:forEach>
							</select>
							<hyjf:validmessage key="isupdate" label="是否更新"></hyjf:validmessage>
						</div>
					</div>	

					<div class="form-group margin-bottom-0">
						<div class="col-sm-offset-2 col-sm-10">
							<a class="btn btn-o btn-primary fn-Confirm"><i class="fa fa-check"></i> 确 认</a>
							<a class="btn btn-o btn-primary fn-Cancel"><i class="fa fa-close"></i> 取 消</a>
						</div>
					</div>
				</form>
				</div>
			</div>
		</div>
	</tiles:putAttribute>
	
	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/selectFx/classie.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/selectFx/selectFx.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/select2/select2.min.js"></script>
		<script type='text/javascript' src="${webRoot}/jsp/manager/config/version/versionInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
