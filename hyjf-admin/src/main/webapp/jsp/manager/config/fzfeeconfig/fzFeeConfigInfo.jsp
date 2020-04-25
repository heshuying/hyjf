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
	<tiles:putAttribute name="pageTitle" value="手续费分账管理" />
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
		<c:set var="jspEditType" value="${empty fzfeeconfigForm.id ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin:0">
			<div class="panel-body">
				<p class="text-small margin-bottom-20">
					在这里可以添加和修改手续费分账。
				</p>
				<hr/>
				<div class="panel-scroll height-535">
					<form id="mainForm" class="form-horizontal" action="${empty fzfeeconfigForm.id ? 'insertAction' : 'updateAction'}" method="post"  role="form" class="form-horizontal" >
						<%-- 角色列表一览 --%>
						<input type="hidden" name="id" id="id" value="${fzfeeconfigForm.id }" />
						<input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
						<input type="hidden" id="success" value="${success}" />
						<%-- 分账来源 --%>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="feeConfSource">分账来源 <span class="symbol required"></span></label>
							<div class="col-sm-4">
								<select id="feeConfSource" name="feeConfSource" class="form-select2" datatype="*" nullmsg="未指定分账来源！" style="width: 100%" data-placeholder="请选择分账来源...">
									<option value=""></option>
									<option value="1">全部</option>
									<option value="2">服务费</option>
									<option value="3">管理费</option>
								</select>
							</div>
							<hyjf:validmessage key="feeConfSource" label="分账来源"></hyjf:validmessage>
						</div>
						<%-- 分账来源 end --%>
						<%-- 服务费分账比例 --%>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="serviceFeeFzRate"> <span class="symbol required"></span>服务费分账比例:</label>
							<div class="col-sm-9">
								<div class="input-group">
									<input type="text" placeholder="服务费分账比例" id="serviceFeeFzRate" name="serviceFeeFzRate" value="<c:out value="${fzfeeconfigForm.serviceFeeFzRate}"></c:out>" class="form-control"
										datatype="/^\d{1}(\.\d{1,4})?$/"  errormsg="服务费分账比例必须为数字，整数部分不能超过1位，小数部分不能超过4位！" maxlength="6" />
									<span class="input-group-addon">率</span>
								</div>
								<hyjf:validmessage key="serviceFeeFzRate" label="服务费分账比例"></hyjf:validmessage>
							</div>
						</div>
						<%-- 服务费分账比例 end--%>
						<%-- 管理费分账比例 --%>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="manageFeeFzRate"> <span class="symbol required"></span>管理费分账比例:</label>
							<div class="col-sm-9">
								<div class="input-group">
									<input type="text" placeholder="管理费分账比例" id="manageFeeFzRate" name="manageFeeFzRate" value="<c:out value="${fzfeeconfigForm.manageFeeFzRate}"></c:out>" class="form-control"
										datatype="/^\d{1,2}(\.\d{1,10})?$/"  errormsg="管理费分账比例必须为数字，整数部分不能超过2位，小数部分不能超过10位！" maxlength="13" />
									<span class="input-group-addon">率</span>
								</div>
								<hyjf:validmessage key="manageFeeFzRate" label="管理费分账比例"></hyjf:validmessage>
							</div>
						</div>
						<%-- 管理费分账比例 end --%>
						<%-- 分账类型 --%>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="feeConfType">分账类型 <span class="symbol required"></span></label>
							<div class="col-sm-4">
								<select id="feeConfType" name="feeConfType" class="form-select2" datatype="*" nullmsg="未指定分账类型！" style="width: 100%" data-placeholder="请选择分账类型...">
									<option value=""></option>
									<option value="1">按出借人分账</option>
									<option value="2">按项目分账</option>
								</select>
							</div>
							<hyjf:validmessage key="feeConfType" label="分账类型"></hyjf:validmessage>
						</div>
						<%-- 分账类型 end --%>
						
						<%-- 出借人分公司 --%>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="branchId">出借人分公司 <span class="symbol required"></span></label>
							<div class="col-sm-4">
								<select id="branchId" name="branchId" class="form-select2" datatype="*" nullmsg="未指定出借人分公司！" style="width: 100%" data-placeholder="请选择出借人分公司...">
									<option value=""></option>
									<c:forEach items="${branchCompanyList }" var="branchCompany" begin="0" step="1" varStatus="status">
										<option value="${borrowProjectType.borrowClass }" data-cd="${borrowProjectType.borrowCd }"
											<c:if test="${borrowProjectType.borrowClass eq fzfeeconfigForm.projectType}">selected="selected"</c:if>>
											<c:out value="${borrowProjectType.borrowName }"></c:out></option>
									</c:forEach>
								</select>
								<input type="hidden" name="branchName" value="${fzfeeconfigForm.branchName}" />
							</div>
							<hyjf:validmessage key="branchId" label="出借人分公司"></hyjf:validmessage>
						</div>
						<%-- 出借人分公司 end --%>
						<%-- 适用项目类型 --%>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="projectType">适用项目类型 <span class="symbol required"></span></label>
							<div class="col-sm-10 checkbox clip-check check-primary inline" style="padding-left: 15px;">
								<input type="checkbox" id="pt-all" name="projectTypeAll" checked="checked" value="-1"><label for="pt-all">全部类型</label>
								<c:forEach items="${projectTypes }" var="pt" begin="0" step="1" varStatus="status">
									<input type="checkbox" id="pt-${status.index }" datatype="*" name="projectType" value="${pt.borrowCd}" 
									<c:forEach items="${selectedProjectList }" var="sp" begin="0" step="1"> <c:if test="${pt.borrowCd eq sp.borrowCd}"> checked="checked" </c:if> </c:forEach> />
									<label for="pt-${status.index }">${pt.borrowName }</label>
								</c:forEach>
							</div>
							<hyjf:validmessage key="projectType" label="项目类型"></hyjf:validmessage>
						</div>
						<%-- 适用项目类型 end --%>
						<%-- 资产来源 --%>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="instCode">资产来源 <span class="symbol required"></span></label>
							<c:if test="${ empty fzfeeconfigForm.id }">
								<div class="col-sm-10 checkbox clip-check check-primary inline" style="padding-left: 15px;">
									<input type="checkbox" id="pt-all" name="projectTypeAll" checked="checked" value="-1"><label for="pt-all">全部类型</label>
									<c:forEach items="${projectTypes }" var="pt" begin="0" step="1" varStatus="status">
										<input type="checkbox" id="pt-${status.index }" datatype="*" name="projectType" value="${pt.borrowCd}" 
										<c:forEach items="${selectedProjectList }" var="sp" begin="0" step="1"> <c:if test="${pt.borrowCd eq sp.borrowCd}"> checked="checked" </c:if> </c:forEach> />
										<label for="pt-${status.index }">${pt.borrowName }</label>
									</c:forEach>
								</div>
							</c:if>
							<hyjf:validmessage key="instCode" label="资产来源"></hyjf:validmessage>
						</div>
						<%-- 资产来源 end --%>
						<%-- 产品类型 --%>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="assetType">产品类型 <span class="symbol required"></span></label>
							<c:if test="${ empty fzfeeconfigForm.id }">
								<div class="col-sm-4">
									<select id="assetType" name="assetType" class="form-select2" datatype="*" nullmsg="未指定产品类型！"  style="width:100%" data-placeholder="请选择产品类型...">
                						<option value=""></option>
                						<c:forEach items="${assetTypeList }" var="assetType" begin="0" step="1">
                							<option value="${assetType.assetType }"
                								<c:if test="${assetType.assetType eq fzfeeconfigForm.assetType}">selected="selected"</c:if>>
                								<c:out value="${assetType.assetTypeName }"></c:out>
                							</option>
                						</c:forEach>
									</select>
								</div>
							</c:if>
							<c:if test="${ !empty fzfeeconfigForm.id }">
								<div class="col-sm-4">
									<select id="assetType" name="assetType" class="form-select2" style="width:100%" disabled="disabled" data-placeholder="请选择产品类型...">
                						<option value=""></option>
                						<c:forEach items="${assetTypeList }" var="assetType" begin="0" step="1">
                							<option value="${assetType.assetType }"
                								<c:if test="${assetType.assetType eq fzfeeconfigForm.assetType}">selected="selected"</c:if>>
                								<c:out value="${assetType.assetTypeName }"></c:out>
                							</option>
                						</c:forEach>
									</select>
									<input type="hidden" name="assetType" value="${fzfeeconfigForm.assetType}" />
								</div>
							</c:if>
							<hyjf:validmessage key="assetType" label="产品类型"></hyjf:validmessage>
						</div>
						<%-- 产品类型 end --%>
						<%-- 项目状态 --%>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="projectState"> 
								<span class="symbol required"></span>项目状态
							</label>
							<div class="col-sm-10 ">
								<div class="radio clip-radio radio-primary ">
									<input type="radio" id="projectStateOn" name="projectState" value="0"  datatype="*" ${ ( empty fzfeeconfigForm.projectState || fzfeeconfigForm.projectState eq 0 ) ? 'checked' : ''}> <label for="projectStateOn"> 启用 </label> 
									<input type="radio" id="projectStateOff" name="projectState" value="1" datatype="*" ${ fzfeeconfigForm.projectState eq 1 ? 'checked' : ''}> <label for="projectStateOff"> 禁用 </label>
									<hyjf:validmessage key="projectState" label="项目状态"></hyjf:validmessage>
								</div>
							</div>
						</div>
						<%-- 项目状态 end --%>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="feeConfRemark"> <span class="symbol"></span>说明 </label>
							<div class="col-sm-10">
								<textarea placeholder="" id="feeConfRemark" name="feeConfRemark" class="form-control limited" 
									datatype="*1-255" errormsg="说明不能超过255个字符！" maxlength="255" ignore="ignore"><c:out value="${fzfeeconfigForm.feeConfRemark}"></c:out></textarea>
								<hyjf:validmessage key="feeConfRemark" label="说明"></hyjf:validmessage>
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
