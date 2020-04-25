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
	<tiles:putAttribute name="pageTitle" value="添加项目类型" />
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
		<c:set var="jspEditType" value="${empty accountconfigForm.ids ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin:0">
			<div class="panel-body">
				<p class="text-small margin-bottom-20">
					在这里可以添加和修改项目类型。
				</p>
				<hr/>
				<div class="panel-scroll height-535">
					<form id="mainForm" class="form-horizontal" action="${empty accountconfigForm.ids ? 'insertAction' : 'updateAction'}" method="post"  role="form" class="form-horizontal" >
						<%-- 角色列表一览 --%>
						<input type="hidden" name="ids" id="ids" value="${accountconfigForm.ids }" />
						<input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
						<input type="hidden" id="success" value="${success}" />
						<div class="form-group">
							<label class="col-xs-2 control-label" for="subAccountName"><span class="symbol required"></span>子账户名称:</label>
							<div class="col-xs-10">
								<div class="input-group">
									<input type="text" placeholder="子账户名称" id="subAccountName" style="width: 300px;" name="subAccountName" ajaxurl="checkAction?ids=<c:out value="${accountconfigForm.ids}"></c:out>" value="<c:out value="${accountconfigForm.subAccountName}"></c:out>" class="input-group" datatype="*" maxlength="20" />
									<hyjf:validmessage key="subAccountName" label="子账户名称"></hyjf:validmessage>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="subAccountType" ><span class="symbol required"></span>子账户类型: </label>
							<div class="col-sm-9">
								<select id="subAccountType" name="subAccountType" class="form-control underline form-select2" style="width:300px; " datatype="*" data-placeholder="请选择类型">
									<option value="">请选择</option>
									<c:forEach items="${subaccountType }" var="record" begin="0" step="1" varStatus="status">
										<option value="${record.nameCd }" <c:if test="${record.nameCd eq accountconfigForm.subAccountType}">selected="selected"</c:if> >
											<c:out value="${record.name }"></c:out>
										</option>
									</c:forEach>
								</select>
								<hyjf:validmessage key="subAccountType" label="子账户类型"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="subAccountCode"><span class="symbol required"></span>子账户代号:</label>
							<div class="col-sm-9">
								<div class="input-group">
									<input type="text" id="subAccountCode" style="width: 300px;" name="subAccountCode" ajaxurl="checkAction?ids=<c:out value="${accountconfigForm.ids}"></c:out>" value="<c:out value="${accountconfigForm.subAccountCode}"></c:out>"
										class="form-control" datatype="*" maxlength="20"/>
								</div>
								<hyjf:validmessage key="subAccountCode" label="子账户代号"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="transferIntoFlg"><span class="symbol required"></span>子账户转入:</label>
							<div class="col-sm-10 ">
								<div class="radio clip-radio radio-primary ">
									<input type="radio" id="transferIntoFlgstateOn" name="transferIntoFlg" value="1" datatype="*" ${ accountconfigForm.transferIntoFlg eq 1 ? 'checked' : ''}> <label for="transferIntoFlgstateOn"> 支持 </label>
									<input type="radio" id="transferIntoFlgstateOff" name="transferIntoFlg" value="0"  datatype="*" ${ ( empty accountconfigForm.transferIntoFlg || accountconfigForm.transferIntoFlg eq 0 ) ? 'checked' : ''}> <label for="transferIntoFlgstateOff"> 不支持 </label> 
									<hyjf:validmessage key="transferIntoFlg" label="子账户转出"></hyjf:validmessage>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="transferOutFlg"><span class="symbol required"></span>子账户转出:</label>
							<div class="col-sm-10 ">
								<div class="radio clip-radio radio-primary">
									<input type="radio" id="transferOutFlgstateOn" name="transferOutFlg" value="1" datatype="*" ${ accountconfigForm.transferOutFlg eq 1 ? 'checked' : ''}> <label for="transferOutFlgstateOn"> 支持 </label>
									<input type="radio" id="transferOutFlgstateOff" name="transferOutFlg" value="0"  datatype="*" ${ ( empty accountconfigForm.transferOutFlg || accountconfigForm.transferOutFlg eq 0 ) ? 'checked' : ''}> <label for="transferOutFlgstateOff"> 不支持 </label>
									<hyjf:validmessage key="transferOutFlg" label="子账户转出"></hyjf:validmessage>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="sort"><span class="symbol required"></span>排序:</label>
							<div class="col-sm-9">
								<div class="input-group">
									<input type="text" id="sort" style="width: 300px;" name="sort" value="<c:out value="${accountconfigForm.sort}"></c:out>"
										class="form-control" datatype="n" placeholder="0或正整数" maxlength="2"/>
								</div>
								<hyjf:validmessage key="sort" label="排序"></hyjf:validmessage>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-2 control-label" for="purpose"><span class="symbol"></span>用途 :</label>
							<div class="col-sm-9">
								<textarea placeholder="" id="purpose" name="purpose" style="width: 350px;" class="form-control limited" 
									datatype="*1-50" errormsg="说明不能超过50个字符！" maxlength="50" ignore="ignore"><c:out value="${accountconfigForm.purpose}"></c:out></textarea>
								<hyjf:validmessage key="purpose" label="用途"></hyjf:validmessage>
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
		<script type='text/javascript' src="${webRoot}/jsp/manager/config/borrow/finmanchargenew/finmanchargenewDetail.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
