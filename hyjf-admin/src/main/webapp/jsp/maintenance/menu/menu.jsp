<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>
<%-- 画面功能路径 (ignore) --%>
<c:set value="${fn:split('汇盈金服,菜单设置', ',')}" var="functionPaths" scope="request"></c:set>

<%-- <shiro:hasPermission name="admin:VIEW"> --%>
<tiles:insertTemplate template="/jsp/layout/mainLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="菜单设置" />
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
		<link href="${themeRoot}/vendor/plug-in/sweetalert/sweet-alert.css" rel="stylesheet" media="screen">
		<link href="${themeRoot}/vendor/plug-in/sweetalert/ie9.css" rel="stylesheet" media="screen">
		<link href="${themeRoot}/vendor/plug-in/toastr/toastr.min.css" rel="stylesheet" media="screen">
		<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
		<link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet" media="screen">
		<style>
.table-striped .checkbox {
	width: 20px;
	margin-right: 0 !important;
	overflow: hidden
}
</style>
	</tiles:putAttribute>
	<%-- 画面主面板的标题块 --%>
	<tiles:putAttribute name="pageFunCaption" type="string">
		<h1 class="mainTitle">菜单设置</h1>
		<span class="mainDescription">本功能可以增加修改删除菜单。</span>
	</tiles:putAttribute>
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<div class="container-fluid container-fullw bg-white">
			<div class="row">
				<div class="col-md-12">
					<div class="search-classic">
						<%-- 菜单列表一览 --%>
						<div class="col-sm-6">
							<div class="panel panel-white">
								<div class="panel-heading border-light">
									<h4 class="panel-title text-primary" id=""></h4>
									<a class="btn btn-o btn-primary btn-sm fn-Add" data-toggle="tooltip" tooltip-placement="top" data-original-title="添加新菜单"><i class="fa fa-plus"></i> 添加</a>
									<a class="btn btn-o btn-primary btn-sm fn-Modifys" data-toggle="tooltip" tooltip-placement="top" data-original-title="修改菜单信息"><i class="fa fa-pencil"></i> 修改</a>
									<a class="btn btn-o btn-primary btn-sm fn-Deletes" data-toggle="tooltip" tooltip-placement="top" data-original-title="删除菜单"><i class="fa fa-trash-o"></i> 删除</a>
									<a class="btn btn-o btn-primary btn-sm fn-Refresh" data-toggle="tooltip" tooltip-placement="top" data-original-title="刷新列表"><i class="fa fa-refresh"></i> 刷新</a>
									<a class="btn btn-o btn-primary btn-sm fn-Setting" data-toggle="tooltip" tooltip-placement="top" data-original-title="设置权限"><i class="fa fa-wrench"></i> 设置权限</a>
								</div>
								<div class="panel-body">
									<div id="tree_menu" class="tree_menu"></div>
								</div>
							</div>
						</div>
						<div class="col-sm-6">
							<div id="right-panel" class="panel panel-white">
								<div class="panel-heading border-light">
									<h4 class="panel-title text-primary" id="title"></h4>
								</div>
								<div class="panel-body">
									<div class="panel-scroll">
										<form id="mainForm" action="" method="post" role="form" class="form-horizontal">
											<%-- 角色列表一览 --%>
											<input type="hidden" name="ids" id="ids" value="" />
											<input type="hidden" name="menuPuuid" id="menuPuuid" value="${adminMenuForm.menuPuuid }" />
											<input type="hidden" name="menuUuid" id="menuUuid" value="${adminMenuForm.menuUuid }" />
											<input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
											<input type="hidden" name="selectedNode" id="selectedNode" value="${adminMenuForm.selectedNode}" />
											<input type="hidden" name="formHasError" id="formHasError" value="${FORM_HAS_ERROR}" />
											<div class="form-group">
												<label class="col-sm-3 control-label" for="menuName"> 上级菜单</label>
												<div class="col-sm-9">
													<label id="menuPNameSpan" class="control-label text-dark"></label>
												</div>
											</div>
											<div class="form-group">
												<label class="col-sm-3 control-label"> <span class="symbol required"></span> 菜单名称
												</label>
												<div class="col-sm-9">
													<input type="text" id="menuName" name="menuName" class="form-control" value="${adminMenuForm.menuName}" maxlength="20" datatype="s2-20" errormsg="菜单名称只能是数字、字母和汉字，长度1~20个字符！" />
													<hyjf:validmessage key="menuName" label="菜单名称"></hyjf:validmessage>
												</div>
											</div>
											<div class="form-group">
												<label class="col-sm-3 control-label" for="menuCtrl"> <span class="symbol"></span> 权限名称
												</label>
												<div class="col-sm-9">
													<input type="text" id="menuCtrl" name="menuCtrl" class="form-control" value="${adminMenuForm.menuCtrl}" maxlength="20" ignore="ignore"  datatype="s1-20" errormsg="权限名称只能是数字、字母和汉字，长度1~20个字符！" />
													<hyjf:validmessage key="menuCtrl" label="权限名称"></hyjf:validmessage>
												</div>
											</div>
											<div class="form-group">
												<label class="col-sm-3 control-label" for="menuUrl"> <span class="symbol"></span> URL
												</label>
												<div class="col-sm-9">
													<input type="text" id="menuUrl" name="menuUrl" class="form-control" value="${adminMenuForm.menuUrl}" maxlength="255" ignore="ignore"  datatype="*1-255" errormsg="URL的长度不能超过255个字符！" />
													<hyjf:validmessage key="menuUrl" label="URL"></hyjf:validmessage>
												</div>
											</div>
											<div class="form-group">
												<label class="col-sm-3 control-label" for="menuIcon"> <span class="symbol"></span> 图标
												</label>
												<div class="col-sm-9">
													<input type="text" id="menuIcon" name="menuIcon" class="form-control" value="${adminMenuForm.menuIcon}" maxlength="50" ignore="ignore"  datatype="*1-50" errormsg="图标的长度不能超过50个字符！" />
													<hyjf:validmessage key="menuIcon" label="图标"></hyjf:validmessage>
												</div>
											</div>
											<div class="form-group">
												<label class="col-sm-3 control-label"> <span class="symbol"></span>是否隐藏
												</label>
												<div class="col-sm-9 ">
													<div class="radio clip-radio radio-primary ">
														<input type="radio" id="menuHideOn" name="menuHide" value="1" ${adminMenuForm.menuHide == '1' ? 'checked' : ''}> <label for="menuHideOn"> 是 </label>
														<input type="radio" id="menuHideOff" name="menuHide" value="0" ${adminMenuForm.menuHide == '0' ? 'checked' : ''}> <label for="menuHideOff"> 否 </label>
														<hyjf:validmessage key="menuHide" label="是否隐藏"></hyjf:validmessage>
													</div>
												</div>
											</div>
											<div class="form-group">
												<label class="col-sm-3 control-label" for="menuSort"> <span class="symbol"></span>排序
												</label>
												<div class="col-sm-9">
													<input type="text" placeholder="排序" id="menuSort" name="menuSort" value="${adminMenuForm.menuSort}" class="form-control" maxlength="10" ignore="ignore" datatype="n1-10" errormsg="排序的长度不能大于5！">
													<hyjf:validmessage key="menuSort" label="排序"></hyjf:validmessage>
												</div>
											</div>

											<div class="form-group margin-bottom-0">
												<div class="col-sm-offset-3 col-sm-9">
													<a class="btn btn-o btn-primary fn-Confirm"><i class="fa fa-check"></i> 确 认</a>
												</div>
											</div>
										</form>
									</div>
								</div>
							</div>
						</div>
						<br /> <br />
					</div>
				</div>
			</div>
		</div>
	</tiles:putAttribute>
	<%-- 对话框面板 (ignore) --%>
	<tiles:putAttribute name="dialogPanels" type="string">
		<iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border: none; width: 100%; height: 100%"></iframe>
	</tiles:putAttribute>

	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/sweetalert/sweet-alert.min.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/toastr/toastr.min.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jstree/jstree.min.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		<script type='text/javascript' src="${webRoot}/jsp/maintenance/menu/menu.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
<%-- </shiro:hasPermission> --%>
