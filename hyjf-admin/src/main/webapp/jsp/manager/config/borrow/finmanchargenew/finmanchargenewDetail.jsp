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
		<c:set var="jspEditType" value="${empty finmanchargeForm.manChargeCd ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin:0">
			<div class="panel-body">
				<p class="text-small margin-bottom-20">
					在这里可以添加和修改项目类型。
				</p>
				<hr/>
				<div class="panel-scroll height-535">
					<form id="mainForm" class="form-horizontal" action="${empty finmanchargeForm.manChargeCd ? 'insertAction' : 'updateAction'}" method="post"  role="form" class="form-horizontal" >
						<%-- 角色列表一览 --%>
						<input type="hidden" name="manChargeCd" id="manChargeCd" value="${finmanchargeForm.manChargeCd }" />
						<input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
						<input type="hidden" id="success" value="${success}" />
						<div class="form-group">
							<label class="col-sm-2 control-label" for="projectType">项目类型 <span class="symbol required"></span></label>
							<c:if test="${ empty finmanchargeForm.manChargeCd }">
								<div class="col-sm-4">
									<select id="projectType" name="projectType" class="form-select2" datatype="*" nullmsg="未指定项目类型！"  style="width:100%" data-placeholder="请选择项目类型...">
										<option value=""></option>
										<c:forEach items="${borrowProjectTypeList }" var="borrowProjectType" begin="0" step="1" varStatus="status">
											<option value="${borrowProjectType.borrowClass }" data-cd="${borrowProjectType.borrowCd }"
												<c:if test="${borrowProjectType.borrowClass eq finmanchargeForm.projectType}">selected="selected"</c:if>>
												<c:out value="${borrowProjectType.borrowName }"></c:out></option>
										</c:forEach>
									</select>
								</div>
							</c:if>
							<c:if test="${ !empty finmanchargeForm.manChargeCd }">
								<div class="col-sm-4">
									<select id="projectType" name="projectType" class="form-select2" style="width:100%" disabled="disabled" data-placeholder="请选择项目类型...">
										<option value=""></option>
										<c:forEach items="${borrowProjectTypeList }" var="borrowProjectType" begin="0" step="1" varStatus="status">
											<option value="${borrowProjectType.borrowClass }" data-cd="${borrowProjectType.borrowCd }"
												<c:if test="${borrowProjectType.borrowClass eq finmanchargeForm.projectType}">selected="selected"</c:if>>
												<c:out value="${borrowProjectType.borrowName }"></c:out></option>
										</c:forEach>
									</select>
									<input type="hidden" name="projectType" value="${finmanchargeForm.projectType}" />
								</div>
							</c:if>
							<hyjf:validmessage key="projectType" label="项目类型"></hyjf:validmessage>
						</div>
						
						<div class="form-group">
							<label class="col-sm-2 control-label" for="instCode">资产来源 <span class="symbol required"></span></label>
							<c:if test="${ empty finmanchargeForm.manChargeCd }">
								<div class="col-sm-4">
									<select id="instCode" name="instCode" class="form-select2" datatype="*" nullmsg="未指定资产来源！"  style="width:100%" data-placeholder="请选择资产来源...">
                						<option value=""></option>
                						<c:forEach items="${hjhInstConfigList }" var="inst" begin="0" step="1">
                							<option value="${inst.instCode }"
                								<c:if test="${inst.instCode eq finmanchargeForm.instCode}">selected="selected"</c:if>>
                								<c:out value="${inst.instName }"></c:out>
                							</option>
                						</c:forEach>
									</select>
								</div>
							</c:if>
							<c:if test="${ !empty finmanchargeForm.manChargeCd }">
								<div class="col-sm-4">
									<select id="instCode" name="instCode" class="form-select2" style="width:100%" disabled="disabled" data-placeholder="请选择资产来源...">
                						<option value=""></option>
                						<c:forEach items="${hjhInstConfigList }" var="inst" begin="0" step="1">
                							<option value="${inst.instCode }"
                								<c:if test="${inst.instCode eq finmanchargeForm.instCode}">selected="selected"</c:if>>
                								<c:out value="${inst.instName }"></c:out>
                							</option>
                						</c:forEach>
									</select>
									<input type="hidden" name="instCode" value="${finmanchargeForm.instCode}" />
								</div>
							</c:if>
							<hyjf:validmessage key="instCode" label="资产来源"></hyjf:validmessage>
						</div>
						
						<div class="form-group">
							<label class="col-sm-2 control-label" for="assetType">产品类型 <span class="symbol required"></span></label>
							<c:if test="${ empty finmanchargeForm.manChargeCd }">
								<div class="col-sm-4">
									<select id="assetType" name="assetType" class="form-select2" datatype="*" nullmsg="未指定产品类型！"  style="width:100%" data-placeholder="请选择产品类型...">
                						<option value=""></option>
                						<c:forEach items="${assetTypeList }" var="assetType" begin="0" step="1">
                							<option value="${assetType.assetType }"
                								<c:if test="${assetType.assetType eq finmanchargeForm.assetType}">selected="selected"</c:if>>
                								<c:out value="${assetType.assetTypeName }"></c:out>
                							</option>
                						</c:forEach>
									</select>
								</div>
							</c:if>
							<c:if test="${ !empty finmanchargeForm.manChargeCd }">
								<div class="col-sm-4">
									<select id="assetType" name="assetType" class="form-select2" style="width:100%" disabled="disabled" data-placeholder="请选择产品类型...">
                						<option value=""></option>
                						<c:forEach items="${assetTypeList }" var="assetType" begin="0" step="1">
                							<option value="${assetType.assetType }"
                								<c:if test="${assetType.assetType eq finmanchargeForm.assetType}">selected="selected"</c:if>>
                								<c:out value="${assetType.assetTypeName }"></c:out>
                							</option>
                						</c:forEach>
									</select>
									<input type="hidden" name="assetType" value="${finmanchargeForm.assetType}" />
								</div>
							</c:if>
							<hyjf:validmessage key="assetType" label="产品类型"></hyjf:validmessage>
						</div>
						
						<c:if test="${ empty finmanchargeForm.manChargeCd }">
							<div class="form-group">
								<label class="col-sm-2 control-label" for="manChargeTimeType" >类型 <span class="symbol required"></span></label>
								<div class="col-sm-9">
									<select id="manChargeTimeType" name="manChargeTimeType" class="form-control underline form-select2" style="width:70% " datatype="*" data-placeholder="请选择类型">
										<option value="">请选择</option>
										<c:forEach items="${enddayMonthList }" var="enddayMonth" begin="0" step="1" varStatus="status">
											<option value="${enddayMonth.nameCd }" <c:if test="${enddayMonth.nameCd eq finmanchargeForm.manChargeTimeType}">selected="selected"</c:if> >
												<c:out value="${enddayMonth.name }"></c:out>
											</option>
										</c:forEach>
									</select>
									<hyjf:validmessage key="manChargeTimeType" label="类型"></hyjf:validmessage>
								</div>
							</div>
						</c:if>
						<c:if test="${ !empty finmanchargeForm.manChargeCd }">
							<div class="form-group">
								<label class="col-sm-2 control-label" for="manChargeTimeType" >类型 <span class="symbol required"></span></label>
								<div class="col-sm-9">
									<select id="manChargeTimeType" name="manChargeTimeType" class="form-control underline form-select2" style="width:70% " datatype="*" data-placeholder="请选择类型" disabled="disabled">
										<option value="">请选择</option>
										<c:forEach items="${enddayMonthList }" var="enddayMonth" begin="0" step="1" varStatus="status">
											<option value="${enddayMonth.nameCd }" <c:if test="${enddayMonth.nameCd eq finmanchargeForm.manChargeTimeType}">selected="selected"</c:if> >
												<c:out value="${enddayMonth.name }"></c:out>
											</option>
										</c:forEach>
									</select>
									<input type="hidden" name="manChargeTimeType" value="${finmanchargeForm.manChargeTimeType}" />
									<hyjf:validmessage key="manChargeTimeType" label="类型"></hyjf:validmessage>
								</div>
							</div>
						</c:if>
						<c:if test="${ empty finmanchargeForm.manChargeCd }">
							<div class="form-group">
								<label class="col-sm-2 control-label" for="manChargeTime">期限:<span class="symbol required"></span></label>
								<div class="col-sm-9">
									<div class="input-group">
										<input type="text" id="manChargeTime" name="manChargeTime" value="<c:out value="${finmanchargeForm.manChargeTime}"></c:out>"
											class="form-control" datatype="n1-10" errormsg="期限应该输入1~10个数字！" maxlength="10"/>
										<span class="input-group-addon">天/月</span>
									</div>
									<hyjf:validmessage key="manChargeTime" label="期限"></hyjf:validmessage>
								</div>
							</div>
						</c:if>
						<c:if test="${ !empty finmanchargeForm.manChargeCd }">
							<div class="form-group">
								<label class="col-sm-2 control-label" for="manChargeTime"><span class="symbol required"></span>期限:</label>
								<div class="col-sm-9">
									<div class="input-group">
										<input type="text" id="manChargeTime" name="manChargeTime" value="<c:out value="${finmanchargeForm.manChargeTime}"></c:out>"  
											class="form-control" datatype="n1-10" errormsg="期限应该输入1~10个数字！" maxlength="10" disabled="disabled"/>
										<span class="input-group-addon">天/月</span>
										<input type="hidden" name="manChargeTime" value="${finmanchargeForm.manChargeTime}" />
									</div>
									<hyjf:validmessage key="manChargeTime" label="期限"></hyjf:validmessage>
								</div>
							</div>
						</c:if>

						<div class="form-group">
							<label class="col-sm-2 control-label" for="autoBorrowApr"> <span class="symbol"></span>自动发标利率:</label>
							<div class="col-sm-9">
								<div class="input-group">
									<input type="text" placeholder="自动发标利率" id="autoBorrowApr" name="autoBorrowApr" value="<c:out value="${finmanchargeForm.autoBorrowApr}"></c:out>" class="form-control"
										datatype="/^\d{1}(\.\d{1,4})?$/"  errormsg="自动发标利率必须为数字，整数部分不能超过1位，小数部分不能超过4位！" maxlength="6" />
									<span class="input-group-addon">率</span>
								</div>
								<hyjf:validmessage key="autoBorrowApr" label="自动发标利率"></hyjf:validmessage>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-2 control-label" for="chargeMode"> 
								<span class="symbol required"></span>服务费收取方式
							</label>
							<div class="col-sm-10 ">
								<div class="radio clip-radio radio-primary ">
									<input type="radio" id="chargeOn" name="chargeMode" value="1"  datatype="*" ${ ( finmanchargeForm.chargeMode eq 1 ) ? 'checked' : ''}> <label for="chargeOn"> 按比例  </label> 
									<input type="radio" id="chargeOff" name="chargeMode" value="2" datatype="*" ${ finmanchargeForm.chargeMode eq 2 ? 'checked' : ''}> <label for="chargeOff"> 按金额 </label>
									<hyjf:validmessage key="chargeMode" label="服务费收取方式"></hyjf:validmessage>
								</div>
							</div>
						</div>

						<%-- upd by LSY START --%>
						<%-- <div class="form-group">
							<label class="col-sm-2 control-label" for="chargeRate"> <span class="symbol required"></span>服务费率:</label>
							<div class="col-sm-9">
								<div class="input-group">
									<input type="text" placeholder="服务费率" id="chargeRate" name="chargeRate" value="<c:out value="${finmanchargeForm.chargeRate}"></c:out>" class="form-control"
										datatype="/^\d{1,2}(\.\d{1,10})?$/"  errormsg="服务费率必须为数字，整数部分不能超过2位，小数部分不能超过10位！" maxlength="13" />
									<span class="input-group-addon">率</span>
								</div>
								<hyjf:validmessage key="chargeRate" label="服务费率"></hyjf:validmessage>
							</div>
						</div> --%>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="chargeRate"> <span class="symbol required"></span>放款服务费率:</label>
							<div class="col-sm-9">
								<div class="input-group">
									<input type="text" placeholder="放款服务费率" id="chargeRate" name="chargeRate" value="<c:out value="${finmanchargeForm.chargeRate}"></c:out>" class="form-control"
										datatype="/^\d{1,2}(\.\d{1,10})?$/"  errormsg="放款服务费率必须为数字，整数部分不能超过2位，小数部分不能超过10位！" maxlength="13" />
									<span class="input-group-addon">率</span>
								</div>
								<hyjf:validmessage key="chargeRate" label="放款服务费率"></hyjf:validmessage>
							</div>
						</div>
						<%-- 
						<div class="form-group">
							<label class="col-sm-2 control-label" for="manChargeRate"> <span class="symbol required"></span>管理费率:</label>
							<div class="col-sm-9">
								<div class="input-group">
									<input type="text" placeholder="管理费率" id="manChargeRate" name="manChargeRate" value="<c:out value="${finmanchargeForm.manChargeRate}"></c:out>" class="form-control"
										datatype="/^\d{1,2}(\.\d{1,10})?$/"  errormsg="管理费率必须为数字，整数部分不能超过2位，小数部分不能超过10位！" maxlength="13" />
									<span class="input-group-addon">率</span>
								</div>
								<hyjf:validmessage key="manChargeRate" label="管理费率"></hyjf:validmessage>
							</div>
						</div> --%>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="manChargeRate"> <span class="symbol required"></span>还款服务费率:</label>
							<div class="col-sm-9">
								<div class="input-group">
									<input type="text" placeholder="还款服务费率" id="manChargeRate" name="manChargeRate" value="<c:out value="${finmanchargeForm.manChargeRate}"></c:out>" class="form-control"
										datatype="/^\d{1,2}(\.\d{1,10})?$/"  errormsg="还款服务费率必须为数字，整数部分不能超过2位，小数部分不能超过10位！" maxlength="13" />
									<span class="input-group-addon">率</span>
								</div>
								<hyjf:validmessage key="manChargeRate" label="还款服务费率"></hyjf:validmessage>
							</div>
						</div>
						<%-- upd by LSY END --%>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="returnRate"> <span class="symbol required"></span>收益差率:</label>
							<div class="col-sm-9">
								<div class="input-group">
									<input type="text" placeholder="收益差率" id="returnRate" name="returnRate" value="<c:out value="${finmanchargeForm.returnRate}"></c:out>" class="form-control"
										datatype="/^\d{1,2}(\.\d{1,10})?$/"  errormsg="收益差率必须为数字，整数部分不能超过2位，小数部分不能超过10位！" maxlength="13" />
									<span class="input-group-addon">率</span>
								</div>
								<hyjf:validmessage key="returnRate" label="收益差率"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="lateInterest"> <span class="symbol required"></span>逾期利率:</label>
							<div class="col-sm-9">
								<div class="input-group">
									<input type="text" placeholder="逾期利率" id="lateInterest" name="lateInterest" value="<c:out value="${finmanchargeForm.lateInterest}"></c:out>" class="form-control"
										datatype="/^\d{1,2}(\.\d{1,10})?$/"  errormsg="逾期利率必须为数字，整数部分不能超过2位，小数部分不能超过10位！" maxlength="13" />
									<span class="input-group-addon">率</span>
								</div>
								<hyjf:validmessage key="lateInterest" label="逾期利率"></hyjf:validmessage>
							</div>
						</div>
						 <%-- <c:if test="${!empty finmanchargeForm.manChargeCd}">  --%>
							<div class="form-group" id="repayTimeConfigArea" <c:if test="${finmanchargeForm.chargeMode eq 1 or empty finmanchargeForm.manChargeCd}">hidden="hidden" style="visible:false;"</c:if>>
								<label class="col-sm-2 control-label" for="serviceFeeTotal"> <span class="symbol required"></span>服务费总额:</label>
								<div class="col-sm-9">
									<div class="input-group">
										<input type="text" placeholder="服务费总额" id="serviceFeeTotal" name="serviceFeeTotal" value="<c:out value="${finmanchargeForm.serviceFeeTotal}"></c:out>" class="form-control"
										datatype="/^\d{0,12}(\.\d{0,2})?$/"  errormsg="服务费总额必须为数字，整数部分不能超过12位，小数部分不能超过2位！" 
											maxlength="15" />
										<span class="input-group-addon">元</span>
									</div>
									<hyjf:validmessage key="serviceFeeTotal" label="服务费总额"></hyjf:validmessage>
								</div>
							</div>
						<%--  </c:if>  --%>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="lateFreeDays"><span class="symbol required"></span>逾期免息天数:</label>
							<div class="col-sm-9">
								<div class="input-group">
									<input type="text" id="lateFreeDays" name="lateFreeDays" value="<c:out value="${finmanchargeForm.lateFreeDays}"></c:out>"  
										class="form-control" datatype="n1-2" errormsg="逾期免息天数应该输入1~2个数字！" maxlength="2" />
									<span class="input-group-addon">天</span>
								</div>
								<hyjf:validmessage key="lateFreeDays" label="逾期免息天数"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="remark"> <span class="symbol"></span>说明 </label>
							<div class="col-sm-10">
								<textarea placeholder="" id="remark" name="remark" class="form-control limited" 
									datatype="*1-255" errormsg="说明不能超过255个字符！" maxlength="255" ignore="ignore"><c:out value="${finmanchargeForm.remark}"></c:out></textarea>
								<hyjf:validmessage key="remark" label="说明"></hyjf:validmessage>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-2 control-label" for=autoRepay> 
								<span class="symbol required"></span>是否自动还款
							</label>
							<div class="col-sm-10 ">
								<div class="radio clip-radio radio-primary ">
									<input type="radio" id="autoOn" name="autoRepay" value="1"  datatype="*" ${ (finmanchargeForm.autoRepay eq 1 ) ? 'checked' : ''}> <label for="autoOn"> 是 </label> 
									<input type="radio" id="autoOff" name="autoRepay" value="2" datatype="*" ${ finmanchargeForm.autoRepay eq 2 ? 'checked' : ''}> <label for="autoOff"> 否 </label>
									<hyjf:validmessage key="autoRepay" label="是否自动还款"></hyjf:validmessage>
								</div>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-2 control-label" for="repayerType"> 
								<span class="symbol required"></span>扣款账户
							</label>
							<div class="col-sm-10 ">
								<div class="radio clip-radio radio-primary ">
									<!-- <input type="radio" id="repayerTypeOn" name="repayerType" value="1"  datatype="*" ${ ( finmanchargeForm.repayerType eq 1 ) ? 'checked' : ''}> <label for="repayerTypeOn"> 担保账户 </label> -->
									<input type="radio" id="repayerTypeOff" name="repayerType" value="2" datatype="*" checked> <label for="repayerTypeOff"> 借款人账户 </label>
									<hyjf:validmessage key="repayerType" label="扣款账户"></hyjf:validmessage>
								</div>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-2 control-label" for="status"> 
								<span class="symbol required"></span>费率状态
							</label>
							<div class="col-sm-10 ">
								<div class="radio clip-radio radio-primary ">
									<input type="radio" id="stateOn" name="status" value="0"  datatype="*" ${ ( empty finmanchargeForm.status || finmanchargeForm.status eq 0 ) ? 'checked' : ''}> <label for="stateOn"> 启用 </label> 
									<input type="radio" id="stateOff" name="status" value="1" datatype="*" ${ finmanchargeForm.status eq 1 ? 'checked' : ''}> <label for="stateOff"> 禁用 </label>
									<hyjf:validmessage key="status" label="费率状态"></hyjf:validmessage>
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
		<script type='text/javascript' src="${webRoot}/jsp/manager/config/borrow/finmanchargenew/finmanchargenewDetail.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
