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
	<tiles:putAttribute name="pageTitle" value="手续费管理" />
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
	<style>
	.panel-title { font-family: "微软雅黑" }
	</style>
	</tiles:putAttribute>
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType" value="${empty feeconfigForm.id ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin:0">
			<div class="panel-body" style="margin:0 auto">
				<div class="panel-scroll height-430">
				<form id="mainForm" action="${empty feeconfigForm.id ? 'insertAction' : 'updateAction'}"
						method="post"  role="form" class="form-horizontal">
					<%-- 手续费列表一览 --%>
					<input type="hidden" name="id" id="id" value="${feeconfigForm.id }" />
					<input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
					<input type="hidden" id="success" value="${success}" />
					<div class="form-group">
					<label class="col-sm-2 control-label" for="personalCredit"> <span class="symbol required"></span>银行名称</label>
						<div class="col-sm-10 admin-select">
								<select id="departmentId" name="name" class="form-control" datatype="*" errormsg="请选择银行！" >
									<option value="" disabled>银行名称</option>
									<c:if test="${!empty bankConfig}">
										<c:forEach items="${bankConfig }" var="dep" begin="0" step="1" varStatus="status">
											<c:choose>
												<c:when test="${feeconfigForm.name == dep.name }">
													<option value="${dep.name }"  data-class="fa" selected="selected">${dep.name }</option>
												</c:when>
												<c:otherwise>
													<option value="${dep.name }" data-class="fa">${dep.name }</option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</c:if>
								</select>
							<hyjf:validmessage key="name" label="银行"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="personalCredit"> <span class="symbol required"></span>个人网银充值</label>
						<div class="col-sm-10">
							<input type="text" placeholder="个人网银充值" id="personalCredit" name="personalCredit" value="${feeconfigForm.personalCredit}"  class="form-control"
								maxlength="9" datatype="/^[1-9]+(\.\d+)?$|^-?0(\.\d+)?$|^[1-9]+[0-9]*(\.\d+)?$/" errormsg="请输入正确的数字,1-9位">
							<hyjf:validmessage key="type" label="个人网银充值"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="enterpriseCredit"> <span class="symbol required"></span>企业网银充值</label>
						<div class="col-sm-10">
							<input type="text" placeholder="企业网银充值" id="enterpriseCredit" name="enterpriseCredit" value="${feeconfigForm.enterpriseCredit}"  class="form-control"
								maxlength="9" datatype="/^[1-9]+(\.\d+)?$|^-?0(\.\d+)?$|^[1-9]+[0-9]*(\.\d+)?$/" errormsg="请输入正确的数字,1-9位" >
							<hyjf:validmessage key="type" label="企业网银充值"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="quickPayment"> <span class="symbol required"></span>快捷支付充值</label>
						<div class="col-sm-10">
							<input type="text" placeholder="快捷支付充值" id="quickPayment" name="quickPayment" value="${feeconfigForm.quickPayment}"  class="form-control"
								maxlength="9" datatype="/^[1-9]+(\.\d+)?$|^-?0(\.\d+)?$|^[1-9]+[0-9]*(\.\d+)?$/" errormsg="请输入正确的数字,1-9位" >
							<hyjf:validmessage key="type" label="快捷支付充值"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="directTakeoutPercent"> <span class="symbol required"></span>即时提现千分比</label>
						<div class="col-sm-10">
							<input type="text" placeholder="即时提现千分比" id="directTakeoutPercent" name="directTakeoutPercent" value="${feeconfigForm.directTakeoutPercent}"  class="form-control"
								maxlength="9" datatype="/^[1-9]+(\.\d+)?$|^-?0(\.\d+)?$|^[1-9]+[0-9]*(\.\d+)?$/" errormsg="请输入正确的数字,1-9位" >
							<hyjf:validmessage key="type" label="即时提现千分比"></hyjf:validmessage>
						</div>
						<label class="col-sm-2 control-label" for="directTakeout"> <span class="symbol required"></span>即时提现</label>
						<div class="col-sm-10">
							<input type="text" placeholder="即时提现" id="directTakeout" name="directTakeout" value="${feeconfigForm.directTakeout}"  class="form-control"
								maxlength="9" datatype="/^[1-9]+(\.\d+)?$|^-?0(\.\d+)?$|^[1-9]+[0-9]*(\.\d+)?$/" errormsg="请输入正确的数字,1-9位" >
							<hyjf:validmessage key="type" label="即时提现"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="quickTakeoutPercent"> <span class="symbol required"></span>快速提现千分比</label>
						<div class="col-sm-10">
							<input type="text" placeholder="快速提现" id="quickTakeoutPercent" name="quickTakeoutPercent" value="${feeconfigForm.quickTakeoutPercent}"  class="form-control"
								maxlength="9" datatype="/^[1-9]+(\.\d+)?$|^-?0(\.\d+)?$|^[1-9]+[0-9]*(\.\d+)?$/" errormsg="请输入正确的数字,1-9位" >
							<hyjf:validmessage key="type" label="快速提现"></hyjf:validmessage>
						</div>
						<label class="col-sm-2 control-label" for="quickTakeout"> <span class="symbol required"></span>快速提现</label>
						<div class="col-sm-10">
							<input type="text" placeholder="快速提现" id="quickTakeout" name="quickTakeout" value="${feeconfigForm.quickTakeout}"  class="form-control"
								maxlength="9" datatype="/^[1-9]+(\.\d+)?$|^-?0(\.\d+)?$|^[1-9]+[0-9]*(\.\d+)?$/" errormsg="请输入正确的数字,1-9位" >
							<hyjf:validmessage key="type" label="快速提现"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="normalTakeout"> <span class="symbol required"></span>普通提现</label>
						<div class="col-sm-10">
							<input type="text" placeholder="普通提现" id="normalTakeout" name="normalTakeout" value="${feeconfigForm.normalTakeout}"  class="form-control"
								maxlength="9" datatype="/^[1-9]+(\.\d+)?$|^-?0(\.\d+)?$|^[1-9]+[0-9]*(\.\d+)?$/" errormsg="请输入正确的数字,1-9位" >
							<hyjf:validmessage key="type" label="普通提现"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="remark"> <span class="symbol required"></span>说明</label>
						<div class="col-sm-10">
							<input type="text" placeholder="说明" id="remark" name="remark" value="${feeconfigForm.remark}"  class="form-control"
								datatype="*1-50" errormsg="说明 只只能是字符数字，长度1~50个字符！"maxlength="50" >
							<hyjf:validmessage key="type" label="说明"></hyjf:validmessage>
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
		<script type='text/javascript' src="${webRoot}/jsp/manager/config/feeconfig/feeconfigInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
