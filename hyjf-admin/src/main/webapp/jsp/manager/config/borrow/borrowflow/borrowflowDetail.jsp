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
	<tiles:putAttribute name="pageTitle" value="添加流程设置" />
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
		<c:set var="jspEditType" value="${empty borrowflowForm.id ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin:0">
			<div class="panel-body">
				<p class="text-small margin-bottom-20">
					在这里可以添加和修改流程。
				</p>
				<hr/>
				<div class="panel-scroll height-535">
					<form id="mainForm" class="form-horizontal" action="${empty borrowflowForm.id ? 'insertAction' : 'updateAction'}" method="post"  role="form" class="form-horizontal" >
						<%-- 角色列表一览 --%>
						<input type="hidden" name="id" id="id" value="${borrowflowForm.id }" />
						<input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
						<input type="hidden" id="success" value="${success}" />
						<div class="form-group">
							<label class="col-sm-2 control-label" for="projectType">项目类型 <span class="symbol required"></span></label>
							<c:if test="${ empty borrowflowForm.id }">
								<div class="col-sm-4">
									<select id="borrowCd" name="borrowCd" class="form-select2" datatype="*" nullmsg="未指定项目类型！"  style="width:100%" data-placeholder="请选择项目类型...">
										<option value=""></option>
										<c:forEach items="${borrowProjectTypeList }" var="projectType" begin="0" step="1" >
											<option value="${projectType.borrowCd }"
												<c:if test="${projectType.borrowCd eq borrowflowForm.borrowCd}">selected="selected"</c:if>>
												<c:out value="${projectType.borrowName }"></c:out></option>
										</c:forEach>
									</select>
								</div>
							</c:if>
							<c:if test="${ !empty borrowflowForm.id }">
								<div class="col-sm-4">
									<select id="borrowCd" name="borrowCd" class="form-select2" style="width:100%" disabled="disabled" data-placeholder="请选择项目类型...">
										<option value=""></option>
										<c:forEach items="${borrowProjectTypeList }" var="projectType" begin="0" step="1" >
											<option value="${projectType.borrowCd }"
												<c:if test="${projectType.borrowCd eq borrowflowForm.borrowCd}">selected="selected"</c:if>>
												<c:out value="${projectType.borrowName }"></c:out></option>
										</c:forEach>
									</select>
									<input type="hidden" name="projectType" value="${borrowflowForm.borrowCd}" />
								</div>
							</c:if>
							<hyjf:validmessage key="projectType" label="项目类型"></hyjf:validmessage>
						</div>
						
						<div class="form-group">
							<label class="col-sm-2 control-label" for="instCode">资产来源 <span class="symbol required"></span></label>
							<c:if test="${ empty borrowflowForm.id }">
								<div class="col-sm-4">
									<select id="instCode" name="instCode" class="form-select2" datatype="*" nullmsg="未指定资产来源！"  style="width:100%" data-placeholder="请选择资产来源...">
                						<option value=""></option>
                						<c:forEach items="${hjhInstConfigList }" var="inst" begin="0" step="1">
                							<option value="${inst.instCode }"
                								<c:if test="${inst.instCode eq borrowflowForm.instCode}">selected="selected"</c:if>>
                								<c:out value="${inst.instName }"></c:out>
                							</option>
                						</c:forEach>
									</select>
								</div>
							</c:if>
							<c:if test="${ !empty borrowflowForm.id }">
								<div class="col-sm-4">
									<select id="instCode" name="instCode" class="form-select2" style="width:100%" disabled="disabled" data-placeholder="请选择资产来源...">
                						<option value=""></option>
                						<c:forEach items="${hjhInstConfigList }" var="inst" begin="0" step="1">
                							<option value="${inst.instCode }"
                								<c:if test="${inst.instCode eq borrowflowForm.instCode}">selected="selected"</c:if>>
                								<c:out value="${inst.instName }"></c:out>
                							</option>
                						</c:forEach>
									</select>
									<input type="hidden" name="instCode" value="${borrowflowForm.instCode}" />
								</div>
							</c:if>
							<hyjf:validmessage key="instCode" label="资产来源"></hyjf:validmessage>
						</div>
						
						<div class="form-group">
							<label class="col-sm-2 control-label" for="assetType">产品类型 <span class="symbol required"></span></label>
							<c:if test="${ empty borrowflowForm.id }">
								<div class="col-sm-4">
									<select id="assetType" name="assetType" class="form-select2" datatype="*" nullmsg="未指定产品类型！"  style="width:100%" data-placeholder="请选择产品类型...">
                						<option value=""></option>
                						<c:forEach items="${assetTypeList }" var="assetTypeList" begin="0" step="1">
                							<option value="${assetTypeList.assetType }"
                								<c:if test="${assetTypeList.assetType eq borrowflowForm.assetType}">selected="selected"</c:if>>
                								<c:out value="${assetTypeList.assetTypeName }"></c:out>
                							</option>
                						</c:forEach>
									</select>
								</div>
							</c:if>
							<c:if test="${ !empty borrowflowForm.id }">
								<div class="col-sm-4">
									<select id="assetType" name="assetType" class="form-select2" style="width:100%" disabled="disabled" data-placeholder="请选择产品类型...">
                						<option value=""></option>
                						<c:forEach items="${assetTypeList }" var="assetTypeList" begin="0" step="1">
                							<option value="${assetTypeList.assetType }"
                								<c:if test="${assetTypeList.assetType eq borrowflowForm.assetType}">selected="selected"</c:if>>
                								<c:out value="${assetTypeList.assetTypeName }"></c:out>
                							</option>
                						</c:forEach>
									</select>
									<input type="hidden" name="assetType" value="${borrowflowForm.assetType}" />
								</div>
							</c:if>
							<hyjf:validmessage key="assetType" label="产品类型"></hyjf:validmessage>
						</div>
						
						<%-- <div class="form-group">
							<label class="col-sm-2 control-label" for="isAssociatePlan"> 
								<span class="symbol required"></span>是否关联计划
							</label>
							<div class="col-sm-10 ">
								<div class="radio clip-radio radio-primary ">
									<input type="radio" id="isAssociatePlanOn" name="isAssociatePlan" value="1" datatype="*" ${ empty borrowflowForm.isAssociatePlan || borrowflowForm.isAssociatePlan eq 1 ? 'checked' : ''}> <label for="isAssociatePlanOn"> 是 </label>
									<input type="radio" id="isAssociatePlanOff" name="isAssociatePlan" value="0"  datatype="*" ${ ( borrowflowForm.isAssociatePlan eq 0 ) ? 'checked' : ''}> <label for="isAssociatePlanOff"> 否 </label> 
									<hyjf:validmessage key="isAssociatePlan" label="是否关联计划"></hyjf:validmessage>
								</div>
							</div>
						</div> --%>
						
						<div class="form-group">
							<label class="col-sm-2 control-label" for="autoAdd"> 
								<span class="symbol required"></span>自动录入标的
							</label>
							<div class="col-sm-10 ">
								<div class="radio clip-radio radio-primary ">
									<input type="radio" id="autoAddOn" name="autoAdd" value="1" datatype="*" ${ empty borrowflowForm.autoAdd || borrowflowForm.autoAdd eq 1 ? 'checked' : ''}> <label for="autoAddOn"> 是 </label>
									<input type="radio" id="autoAddOff" name="autoAdd" value="0"  datatype="*" ${ ( borrowflowForm.autoAdd eq 0 ) ? 'checked' : ''}> <label for="autoAddOff"> 否 </label> 
									<hyjf:validmessage key="autoAdd" label="自动录入标的"></hyjf:validmessage>
								</div>
							</div>
						</div>

						<%--<div class="form-group">
							<label class="col-sm-2 control-label" for="applicant"> 
								<span class="symbol"></span>项目申请人
							</label>
							<div class="col-sm-10">
								<input type="text" placeholder="项目申请人" id="applicant" name="applicant" value="<c:out value="${borrowflowForm.applicant}" />"   class="form-control"
									datatype="*0-20" errormsg="项目申请人不能超过20个字符！" maxlength="20" >
								<hyjf:validmessage key="applicant" label="项目申请人"></hyjf:validmessage>
							</div>
						</div>--%>

						<div class="form-group">
							<label class="col-sm-2 control-label" for="repayOrgName"> 
								<span class="symbol"></span>担保机构
							</label>
							<div class="col-sm-10">
								<input type="text" placeholder="担保机构" id="repayOrgName" name="repayOrgName" value="<c:out value="${borrowflowForm.repayOrgName}" />"   class="form-control"
									datatype="*0-30" errormsg="担保机构不能超过30个字符！" maxlength="30" >
								<hyjf:validmessage key="repayOrgName" label="担保机构"></hyjf:validmessage>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-2 control-label" for="autoRecord"> 
								<span class="symbol required"></span>银行备案
							</label>
							<div class="col-sm-10 ">
								<div class="radio clip-radio radio-primary ">
									<input type="radio" id="autoRecordOn" name="autoRecord" value="1" datatype="*" ${ empty borrowflowForm.autoRecord || borrowflowForm.autoRecord eq 1 ? 'checked' : ''}> <label for="autoRecordOn"> 自动 </label>
									<input type="radio" id="autoRecordOff" name="autoRecord" value="0"  datatype="*" ${ ( borrowflowForm.autoRecord eq 0 ) ? 'checked' : ''}> <label for="autoRecordOff"> 手动 </label> 
									<hyjf:validmessage key="autoRecord" label="银行备案"></hyjf:validmessage>
								</div>
							</div>
						</div>

						<%--<div class="form-group">
							<label class="col-sm-2 control-label" for="autoBail">
								<span class="symbol required"></span>确认保证金
							</label>
							<div class="col-sm-10 ">
								<div class="radio clip-radio radio-primary ">
									<input type="radio" id="autoBailOn" name="autoBail" value="1" datatype="*" ${ empty borrowflowForm.autoBail || borrowflowForm.autoBail eq 1 ? 'checked' : ''}> <label for="autoBailOn"> 自动 </label>
									<input type="radio" id="autoBailOff" name="autoBail" value="0"  datatype="*" ${ ( borrowflowForm.autoBail eq 0 ) ? 'checked' : ''}> <label for="autoBailOff"> 手动 </label>
									<hyjf:validmessage key="autoBail" label="确认保证金"></hyjf:validmessage>
								</div>
							</div>
						</div>--%>

						<div class="form-group">
							<label class="col-sm-2 control-label" for="autoAudit"> 
								<span class="symbol required"></span>初审
							</label>
							<div class="col-sm-10 ">
								<div class="radio clip-radio radio-primary ">
									<input type="radio" id="autoAuditOn" name="autoAudit" value="1" datatype="*" ${ empty borrowflowForm.autoAudit || borrowflowForm.autoAudit eq 1 ? 'checked' : ''}> <label for="autoAuditOn"> 自动 </label>
									<input type="radio" id="autoAuditOff" name="autoAudit" value="0"  datatype="*" ${ ( borrowflowForm.autoAudit eq 0 ) ? 'checked' : ''}> <label for="autoAuditOff"> 手动 </label> 
									<hyjf:validmessage key="autoAudit" label="初审"></hyjf:validmessage>
								</div>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-2 control-label" for="autoReview"> 
								<span class="symbol required"></span>复审
							</label>
							<div class="col-sm-10 ">
								<div class="radio clip-radio radio-primary ">
									<input type="radio" id="autoReviewOn" name="autoReview" value="1" datatype="*" ${ empty borrowflowForm.autoReview || borrowflowForm.autoReview eq 1 ? 'checked' : ''}> <label for="autoReviewOn"> 自动 </label>
									<input type="radio" id="autoReviewOff" name="autoReview" value="0"  datatype="*" ${ ( borrowflowForm.autoReview eq 0 ) ? 'checked' : ''}> <label for="autoReviewOff"> 手动 </label> 
									<hyjf:validmessage key="autoReview" label="复审"></hyjf:validmessage>
								</div>
							</div>
						</div>

						<%--<div class="form-group">
							<label class="col-sm-2 control-label" for="autoSendMinutes"> 
								<span class="symbol"></span>拆标自动发标时间间隔</label>
							<div class="col-sm-9">
								<div class="input-group">
									<input type="text" placeholder="拆标自动发标时间间隔" id="autoSendMinutes" name="autoSendMinutes" value="<c:out value="${borrowflowForm.autoSendMinutes}"></c:out>" class="form-control"
										datatype="n0-11"  errormsg="拆标自动发标时间间隔必须为数字！" maxlength="11" />
									<span class="input-group-addon">分钟</span>
								</div>
								<hyjf:validmessage key="autoSendMinutes" label="拆标自动发标时间间隔"></hyjf:validmessage>
							</div>
						</div>--%>

						<%--<div class="form-group">
							<label class="col-sm-2 control-label" for="autoReviewMinutes"> 
								<span class="symbol"></span>自动复审时间间隔</label>
							<div class="col-sm-9">
								<div class="input-group">
									<input type="text" placeholder="自动复审时间间隔" id="autoReviewMinutes" name="autoReviewMinutes" value="<c:out value="${borrowflowForm.autoReviewMinutes}"></c:out>" class="form-control"
										datatype="n0-11"  errormsg="自动复审时间间隔必须为数字！" maxlength="11" />
									<span class="input-group-addon">分钟</span>
								</div>
								<hyjf:validmessage key="autoReviewMinutes" label="自动复审时间间隔"></hyjf:validmessage>
							</div>
						</div>--%>

						<div class="form-group">
							<label class="col-sm-2 control-label" for="remark"> <span class="symbol"></span>说明 </label>
							<div class="col-sm-10">
								<textarea placeholder="" id="remark" name="remark" class="form-control limited" 
									datatype="*0-50" errormsg="说明不能超过50个字符！" maxlength="50" ignore="ignore"><c:out value="${borrowflowForm.remark}"></c:out></textarea>
								<hyjf:validmessage key="remark" label="说明"></hyjf:validmessage>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-2 control-label" for="isOpen"> 
								<span class="symbol required"></span>项目状态
							</label>
							<div class="col-sm-10 ">
								<div class="radio clip-radio radio-primary ">
									<input type="radio" id="stateOn" name="isOpen" value="1"  datatype="*" ${ ( empty borrowflowForm.isOpen || borrowflowForm.isOpen eq 1 ) ? 'checked' : ''}> <label for="stateOn"> 启用 </label> 
									<input type="radio" id="stateOff" name="isOpen" value="0" datatype="*" ${ borrowflowForm.isOpen eq 0 ? 'checked' : ''}> <label for="stateOff"> 禁用 </label>
									<hyjf:validmessage key="isOpen" label="项目状态"></hyjf:validmessage>
								</div>
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
		<script type='text/javascript' src="${webRoot}/jsp/manager/config/borrow/borrowflow/borrowflowDetail.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
