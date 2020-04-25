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
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
				<div class="panel panel-white" style="margin: 0">
					<div class="panel-body" style="margin: 0 auto">
						<div class="panel-scroll height-430">
							<form id="mainForm" action="updateAction" method="post" role="form" class="form-horizontal">
									<%-- 列表一览 --%>
								<input type="hidden" name="id" id="id" value="${evaluationcheckForm.id }" />
								<input type="hidden"  name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
								<input type="hidden" id="success" value="${success}" />
								<div class="form-group">
									<label class="col-xs-5 control-label">
										散标／债转出借者测评类型校验 ：
									</label>
									<div class="col-xs-6 ">
										<div class="radio clip-radio radio-primary ">
											<input type="radio" id="debtEvaluationTypeCheckOn" name="debtEvaluationTypeCheck" value=1  datatype="*" <c:if test="${ evaluationcheckForm.debtEvaluationTypeCheck eq 1 }" >checked="checked"</c:if> ><label for="debtEvaluationTypeCheckOn"> 开启 </label>
											<input type="radio" id="debtEvaluationTypeCheckOff" name="debtEvaluationTypeCheck" value=0 datatype="*" <c:if test="${ evaluationcheckForm.debtEvaluationTypeCheck ne 1 }" >checked="checked"</c:if> > <label for="debtEvaluationTypeCheckOff"> 关闭 </label>
											<hyjf:validmessage key="status" label="启用状态"></hyjf:validmessage>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-5 control-label">
										智投出借者测评类型校验 ：
									</label>
									<div class="col-xs-6 ">
										<div class="radio clip-radio radio-primary ">
											<input type="radio" id="intellectualEveluationTypeCheckOn" name="intellectualEveluationTypeCheck" value=1  datatype="*" <c:if test="${ evaluationcheckForm.intellectualEveluationTypeCheck eq 1 }" >checked="checked"</c:if> ><label for="intellectualEveluationTypeCheckOn"> 开启 </label>
											<input type="radio" id="intellectualEveluationTypeCheckOff" name="intellectualEveluationTypeCheck" value=0 datatype="*" <c:if test="${ evaluationcheckForm.intellectualEveluationTypeCheck ne 1 }" >checked="checked"</c:if> > <label for="intellectualEveluationTypeCheckOff"> 关闭 </label>
											<hyjf:validmessage key="status" label="启用状态"></hyjf:validmessage>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-5 control-label">
										散标／债转单笔出借金额校验 ：
									</label>
									<div class="col-xs-6 ">
										<div class="radio clip-radio radio-primary ">
											<input type="radio" id="deptEvaluationMoneyCheckOn" name="deptEvaluationMoneyCheck" value=1  datatype="*" <c:if test="${ evaluationcheckForm.deptEvaluationMoneyCheck eq 1 }" >checked="checked"</c:if> ><label for="deptEvaluationMoneyCheckOn"> 开启 </label>
											<input type="radio" id="deptEvaluationMoneyCheckOff" name="deptEvaluationMoneyCheck" value=0 datatype="*" <c:if test="${ evaluationcheckForm.deptEvaluationMoneyCheck ne 1 }" >checked="checked"</c:if> > <label for="deptEvaluationMoneyCheckOff"> 关闭 </label>
											<hyjf:validmessage key="status" label="启用状态"></hyjf:validmessage>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-5 control-label">
										智投单笔出借金额校验 ：
									</label>
									<div class="col-xs-6 ">
										<div class="radio clip-radio radio-primary ">
											<input type="radio" id="intellectualEvaluationMoneyCheckOn" name="intellectualEvaluationMoneyCheck" value=1  datatype="*" <c:if test="${ evaluationcheckForm.intellectualEvaluationMoneyCheck eq 1 }" >checked="checked"</c:if> ><label for="intellectualEvaluationMoneyCheckOn"> 开启 </label>
											<input type="radio" id="intellectualEvaluationMoneyCheckOff" name="intellectualEvaluationMoneyCheck" value=0 datatype="*" <c:if test="${ evaluationcheckForm.intellectualEvaluationMoneyCheck ne 1 }" >checked="checked"</c:if> > <label for="intellectualEvaluationMoneyCheckOff"> 关闭 </label>
											<hyjf:validmessage key="status" label="启用状态"></hyjf:validmessage>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-5 control-label">
										散标／债转待收本金校验 ：
									</label>
									<div class="col-xs-6 ">
										<div class="radio clip-radio radio-primary ">
											<input type="radio" id="deptCollectionEvaluationCheckOn" name="deptCollectionEvaluationCheck" value=1  datatype="*" <c:if test="${ evaluationcheckForm.deptCollectionEvaluationCheck eq 1 }" >checked="checked"</c:if> ><label for="deptCollectionEvaluationCheckOn"> 开启 </label>
											<input type="radio" id="deptCollectionEvaluationCheckOff" name="deptCollectionEvaluationCheck" value=0 datatype="*" <c:if test="${ evaluationcheckForm.deptCollectionEvaluationCheck ne 1 }" >checked="checked"</c:if> > <label for="deptCollectionEvaluationCheckOff"> 关闭 </label>
											<hyjf:validmessage key="status" label="启用状态"></hyjf:validmessage>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-5 control-label">
										智投待收本金校验 ：
									</label>
									<div class="col-xs-6 ">
										<div class="radio clip-radio radio-primary ">
											<input type="radio" id="intellectualCollectionEvaluationCheckOn" name="intellectualCollectionEvaluationCheck" value=1  datatype="*" <c:if test="${ evaluationcheckForm.intellectualCollectionEvaluationCheck eq 1 }" >checked="checked"</c:if> ><label for="intellectualCollectionEvaluationCheckOn"> 开启 </label>
											<input type="radio" id="intellectualCollectionEvaluationCheckOff" name="intellectualCollectionEvaluationCheck" value=0 datatype="*" <c:if test="${ evaluationcheckForm.intellectualCollectionEvaluationCheck ne 1 }" >checked="checked"</c:if> > <label for="intellectualCollectionEvaluationCheckOff"> 关闭 </label>
											<hyjf:validmessage key="status" label="启用状态"></hyjf:validmessage>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-5 control-label">
										投标时校验（二期）：
									</label>
									<div class="col-xs-6">
										<div class="radio clip-radio radio-primary ">
											<input type="radio" id="investmentEvaluationCheckOn" name="investmentEvaluationCheck" value=1  datatype="*" <c:if test="${ evaluationcheckForm.investmentEvaluationCheck eq 1 }" >checked="checked"</c:if> ><label for="investmentEvaluationCheckOn"> 开启 </label>
											<input type="radio" id="investmentEvaluationCheckOff" name="investmentEvaluationCheck" value=0 datatype="*" <c:if test="${ evaluationcheckForm.investmentEvaluationCheck ne 1 }" >checked="checked"</c:if> > <label for="investmentEvaluationCheckOff"> 关闭 </label>
											<hyjf:validmessage key="status" label="启用状态"></hyjf:validmessage>
										</div>
									</div>
								</div>
								<div class="form-group margin-bottom-0">
									<div class="col-xs-offset-5 col-xs-6">
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
			</div>
		</div>
	</tiles:putAttribute>

	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
		<script type='text/javascript' src="${webRoot}/jsp/manager/config/evaluationconfig/evaluationcheck/evaluationcheck.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
