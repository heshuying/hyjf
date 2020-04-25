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
		<c:set var="jspEditType" value="${modifyFlag eq 'N' ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin:0">
			<div class="panel-body">
				<p class="text-small margin-bottom-20">
					在这里可以添加和修改项目类型。
				</p>
				<hr/>
				<div class="panel-scroll height-535">
				<form id="mainForm" action="${modifyFlag eq 'N' ? 'insertAction' : 'updateAction'}"
						method="post"  role="form" class="form-horizontal" >
					<%-- 角色列表一览 --%>
					<input type="hidden" name="modifyFlag" id="modifyFlag" value="${modifyFlag}" />
					<input type="hidden" id="success" value="${success}" />
					<c:if test="${ modifyFlag eq 'N'}" >
						<div class="form-group">
							<label class="col-sm-2 control-label" >
								<span class="symbol required"></span>项目类型 :
							</label>
							<div class="col-sm-9">
								<select id="borrowProjectType" name="borrowProjectType" class="form-control underline form-select2" style="width:70% " datatype="*" data-placeholder="请选择项目类型">
									<c:forEach items="${projectTypeList }" var="projectType" begin="0" step="1" varStatus="status">
										<option value="${projectType.nameCd }" <c:if test="${projectType.nameCd eq projecttypeForm.borrowProjectType }">selected="selected"</c:if> >
											<c:out value="${projectType.name }"></c:out>
										</option>
									</c:forEach>
								</select>
								<hyjf:validmessage key="borrowProjectType" label="项目类型"></hyjf:validmessage>
							</div>
						</div>
					</c:if>

					<c:if test="${ modifyFlag eq 'E'}" >
						<div class="form-group">
							<label class="col-sm-2 control-label" >
								<span class="symbol required"></span>项目类型 :
							</label>
							<div class="col-sm-9">
								<select class="form-control underline form-select2" style="width:70% " datatype="*" data-placeholder="请选择项目类型" disabled="disabled">
									<c:forEach items="${projectTypeList }" var="projectType" begin="0" step="1" varStatus="status">
										<option value="${projectType.nameCd }" <c:if test="${projectType.nameCd eq projecttypeForm.borrowProjectType }">selected="selected"</c:if> >
											<c:out value="${projectType.name }"></c:out>
										</option>
									</c:forEach>
								</select>
								<hyjf:validmessage key="borrowProjectType" label="项目类型"></hyjf:validmessage>
							</div>
						</div>
						<input type="hidden" name="borrowProjectType" id="borrowProjectType" value="${projecttypeForm.borrowProjectType }" />
					</c:if>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="borrowCd"> 
							<span class="symbol required"></span>编号:
						</label>
						<div class="col-sm-10">
							<input type="text" placeholder="项目编号" id="borrowCd" name="borrowCd" value="<c:out value="${projecttypeForm.borrowCd}" />"   class="form-control"
								datatype="*1-20" errormsg="项目编号不能超过20个字符！" maxlength="20" <c:if test="${ modifyFlag eq 'N'}" > ajaxurl="checkAction" </c:if> <c:if test="${ modifyFlag eq 'E'}" > readonly="readonly" </c:if>  >
							<hyjf:validmessage key="borrowClass" label="项目编号"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="borrowClass"> 
							<span class="symbol required"></span>项目编号前缀:
						</label>
						<div class="col-sm-10">
							<input type="text" placeholder="项目前缀" id="borrowClass" name="borrowClass" value="<c:out value="${projecttypeForm.borrowClass}" />"   class="form-control"
								datatype="*1-20" errormsg="项目前缀不能超过20个字符！" maxlength="20" >
							<hyjf:validmessage key="borrowClass" label="项目编号"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="borrowName"> 
							<span class="symbol required"></span>名称 :
						</label>
						<div class="col-sm-10">
							<input type="text" placeholder="名称" id="borrowName" name="borrowName" value="<c:out value="${projecttypeForm.borrowName}" />"  class="form-control"
								datatype="*1-20" errormsg="名称不能超过20个字符！" maxlength="20" />
							<hyjf:validmessage key="borrowName" label="名称"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="investStart"> <span class="symbol required"></span>起投值</label>
						<div class="col-sm-10">
							<div class="input-group">
								<input type="text" placeholder="起投值" id="investStart" name="investStart" value="<c:out value="${projecttypeForm.investStart}" />"   class="form-control"
									datatype="n1-10" errormsg="起投值 只能是数字，长度1~10个字符！" >
								<span class="input-group-addon">元</span>
							</div>
							<hyjf:validmessage key="investStart" label="起投值"></hyjf:validmessage>
						</div>
						</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="investEnd"> <span class="symbol required"></span>最大值</label>
						<div class="col-sm-10">
							<div class="input-group">
								<input type="text" placeholder="最大值" id="investEnd" name="investEnd" value="<c:out value="${projecttypeForm.investEnd}" />"   class="form-control"
									datatype="n1-10" errormsg="最大值 只能是数字，长度1~10个字符！" >
								<span class="input-group-addon">元</span>
							</div>
							<hyjf:validmessage key="investEnd" label="最大值"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="increaseMoney"> <span class="symbol required"></span>递增金额</label>
						<div class="col-sm-10">
							<div class="input-group">
								<input type="text" placeholder="递增金额" id="increaseMoney" name="increaseMoney" value="<c:out value="${projecttypeForm.increaseMoney}" />"   class="form-control"
									datatype="n1-10" errormsg="递增金额 只能是数字，长度1~10个字符！" >
								<span class="input-group-addon">元</span>
							</div>
							<hyjf:validmessage key="increaseMoney" label="递增金额"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="investUserType"> 
							<span class="symbol required"></span>出借用户:
						</label>
						<div class="col-sm-10">
							<select id="investUserType" name="investUserType" style="width:70% " class="form-control underline form-select2" datatype="*" errormsg="请选择出借用户！" >
								<option value="" disabled>选择出借用户</option>
								<c:if test="${!empty investUsers}">
									<c:forEach items="${investUsers }" var="investUser" begin="0" step="1" varStatus="status">
										<c:choose>
											<c:when test="${projecttypeForm.investUserType == investUser.nameCd }">
												<option value="${investUser.nameCd }" selected="selected">${investUser.name }</option>
											</c:when>
											<c:otherwise>
												<option value="${investUser.nameCd }" >${investUser.name }</option>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</c:if>
							</select>
							<hyjf:validmessage key="investUserType" label="出借用户"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="methodName"> <span class="symbol required"></span>还款方式</label>
						<div class="col-sm-10">
				  			<c:forEach items="${repayStyles}" var="record" begin="0" step="1" varStatus="status">
					  			<div class="checkbox clip-check check-primary checkbox-inline">
									<input type="checkbox" name="methodName" id="checkbox${ record.nid }" value="<c:out value="${record.nid }" />"
										<c:forEach items="${repayNames}" var="record2" begin="0" step="1" varStatus="status2">
											<c:if test="${ ( record.nid eq record2.repayMethod ) }" >checked="checked"</c:if>
										</c:forEach>
									<c:if test="${status.last }">datatype="*"</c:if>
									>
									<label for="checkbox${ record.nid }">
										<c:out value="${record.name }" />
									</label>
								</div>
							</c:forEach>
						</div>
						<hyjf:validmessage key="methodName" label="还款方式"></hyjf:validmessage>
						</div>
					<div  class="form-group">
								<label class="col-sm-2 control-label" for="investUserType"> 
							<span></span>可用券:
						</label>
						<div class="col-sm-10">
									<div class="checkbox clip-check check-primary checkbox-inline">
										<input type="checkbox" id="tzptCheckAll" value="all" <c:if test="${( projecttypeForm.interestCoupon eq '1' and projecttypeForm.tasteMoney eq '1') }">checked</c:if> />
										<label for="tzptCheckAll">全部</label>
									</div>
									<div class="checkbox clip-check check-info checkbox-inline">
										<input type="checkbox" name="interestCoupon" id="checkboxPC" value="1" class="tzptCheckbox" <c:if test="${( projecttypeForm.interestCoupon eq '1' ) }">checked</c:if> />
										<label for="checkboxPC">加息券</label>
									</div>
									<div class="checkbox clip-check check-info checkbox-inline">
										<input type="checkbox" name="tasteMoney" id="checkboxWGW" value="1" class="tzptCheckbox" <c:if test="${( projecttypeForm.tasteMoney eq '1' ) }">checked</c:if> />
										<label for="checkboxWGW">体验金</label>
									</div>
									</div>
							</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="increaseInterestFlag">
							<span class="symbol required"></span>加息开关
						</label>
						<div class="col-sm-10 ">
							<div class="radio clip-radio radio-primary ">
								<input type="radio" id="increaseInterestOn" name="increaseInterestFlag" value="1" datatype="*" <c:if test="${ ( projecttypeForm.increaseInterestFlag eq '1' ) }" >checked="checked"</c:if> > <label for="increaseInterestOn"> 启用 </label>
								<input type="radio" id="increaseInterestOff" name="increaseInterestFlag" value="0"  datatype="*" <c:if test="${ ( projecttypeForm.increaseInterestFlag eq '0' ) or ( empty projecttypeForm.increaseInterestFlag ) }" >checked="checked"</c:if> ><label for="increaseInterestOff"> 禁用 </label>
								<hyjf:validmessage key="increaseInterestFlag" label="加息开关"></hyjf:validmessage>
							</div>
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label" for="status">
							<span class="symbol required"></span>项目状态
						</label>
						<div class="col-sm-10 ">
							<div class="radio clip-radio radio-primary ">
								<input type="radio" id="stateOn" name="status" value="0"  datatype="*" <c:if test="${ ( projecttypeForm.status eq '0' ) or ( empty projecttypeForm.status ) }" >checked="checked"</c:if> ><label for="stateOn"> 启用 </label>
								<input type="radio" id="stateOff" name="status" value="1" datatype="*" <c:if test="${ ( projecttypeForm.status eq '1' ) }" >checked="checked"</c:if> > <label for="stateOff"> 禁用 </label>
								<hyjf:validmessage key="status" label="项目状态"></hyjf:validmessage>
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
		<script type='text/javascript' src="${webRoot}/jsp/manager/config/borrow/projecttype/projecttypeInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
