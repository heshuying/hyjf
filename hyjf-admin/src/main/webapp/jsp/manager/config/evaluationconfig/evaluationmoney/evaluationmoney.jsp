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
	<tiles:putAttribute name="pageTitle" value="限额配置" />
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
				<div class="panel panel-white" style="margin: 0">
					<div class="panel-body" style="margin: 0 auto">
						<div class="panel-scroll height-430">
							<form id="mainForm" action="updateAction" method="post" role="form" class="form-horizontal">
									<%-- 列表一览 --%>
								<input type="hidden" name="id" id="id" value="${evaluationmoneyForm.id }" />
								<input type="hidden"  name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
								<input type="hidden" id="success" value="${success}" />
									<div class="form-group">
										<label class="col-xs-3 control-label" for="validityEvaluationDate">用户测评有效期：</label>
										<div class="col-xs-9">
											<div class="input-group">
												<input type="text" placeholder="用户测评有效期" id="validityEvaluationDate" name="validityEvaluationDate" value="<c:out value="${evaluationmoneyForm.validityEvaluationDate}" />"  class="form-control"
													   datatype="/^\d{1,8}(\.\d{1,2})?$/" errormsg=".测评有效期：设置测评后再次测评间隔时间，初始值为360天！" maxlength="11">
												<span class="input-group-addon">天</span>
											</div>
											<hyjf:validmessage key="personalMaxAmount" label="发标额度上限"></hyjf:validmessage>
										</div>
									</div>
									<div class="form-group">
										<label class="col-xs-3 control-label" for="conservativeEvaluationSingleMoney">保守型单次出借金额限制：</label>
										<div class="col-xs-9">
											<div class="input-group">
												<input type="text" placeholder="保守型单次出借金额限制" id="conservativeEvaluationSingleMoney" name="conservativeEvaluationSingleMoney" value="<c:out value="${evaluationmoneyForm.conservativeEvaluationSingleMoney}" />"  class="form-control"
													   datatype="/^\d{1,8}(\.\d{1,2})?$/" errormsg="整数部分不能超过8位，小数部分不能超过2位！" maxlength="11">
												<span class="input-group-addon">元</span>
											</div>
											<hyjf:validmessage key="personalMaxAmount" label="发标额度上限"></hyjf:validmessage>
										</div>
									</div>
									<div class="form-group">
										<label class="col-xs-3 control-label" for="steadyEvaluationSingleMoney">稳健型单次出借金额限制：</label>
										<div class="col-xs-9">
											<div class="input-group">
												<input type="text" placeholder="稳健型单次出借金额限制" id="steadyEvaluationSingleMoney" name="steadyEvaluationSingleMoney" value="<c:out value="${evaluationmoneyForm.steadyEvaluationSingleMoney}" />"  class="form-control"
													   datatype="/^\d{1,8}(\.\d{1,2})?$/" errormsg="稳健型单次出借金额限制，整数部分不能超过8位，小数部分不能超过2位！" maxlength="11">
												<span class="input-group-addon">元</span>
											</div>
											<hyjf:validmessage key="enterpriseMaxAmount" label="企业最高金额"></hyjf:validmessage>
										</div>
									</div>
									<div class="form-group">
										<label class="col-xs-3 control-label" for="growupEvaluationSingleMoney">成长型单次出借金额限制：</label>
										<div class="col-xs-9">
											<div class="input-group">
												<input type="text" placeholder="成长型单次出借金额限制" id="growupEvaluationSingleMoney" name="growupEvaluationSingleMoney" value="<c:out value="${evaluationmoneyForm.growupEvaluationSingleMoney}" />"  class="form-control"
													   datatype="/^\d{1,8}(\.\d{1,2})?$/" errormsg="成长型单次出借金额限制，整数部分不能超过8位，小数部分不能超过2位！" maxlength="11">
												<span class="input-group-addon">元</span>
											</div>
											<hyjf:validmessage key="enterpriseMaxAmount" label="企业最高金额"></hyjf:validmessage>
										</div>
									</div>
									<div class="form-group">
										<label class="col-xs-3 control-label" for="enterprisingEvaluationSinglMoney">进取型单次出借金额限制：</label>
										<div class="col-xs-9">
											<div class="input-group">
												<input type="text" placeholder="进取型单次出借金额限制" id="enterprisingEvaluationSinglMoney" name="enterprisingEvaluationSinglMoney" value="<c:out value="${evaluationmoneyForm.enterprisingEvaluationSinglMoney}" />"  class="form-control"
													   datatype="/^\d{1,8}(\.\d{1,2})?$/" errormsg="进取型单次出借金额限制，整数部分不能超过8位，小数部分不能超过2位！" maxlength="11">
												<span class="input-group-addon">元</span>
											</div>
											<hyjf:validmessage key="enterpriseMaxAmount" label="企业最高金额"></hyjf:validmessage>
										</div>
									</div>
									<div class="form-group"></div>
									<div class="form-group">
										<label class="col-xs-3 control-label" for="conservativeEvaluationPrincipalMoney">保守型待收本金金额限制：</label>
										<div class="col-xs-9">
											<div class="input-group">
												<input type="text" placeholder="保守型待收本金金额限制" id="conservativeEvaluationPrincipalMoney" name="conservativeEvaluationPrincipalMoney" value="<c:out value="${evaluationmoneyForm.conservativeEvaluationPrincipalMoney}" />"  class="form-control"
													   datatype="/^\d{1,8}(\.\d{1,2})?$/" errormsg="保守型待收本金金额限制，整数部分不能超过8位，小数部分不能超过2位！" maxlength="11">
												<span class="input-group-addon">元</span>
											</div>
											<hyjf:validmessage key="enterpriseMaxAmount" label="企业最高金额"></hyjf:validmessage>
										</div>
									</div>
									<div class="form-group">
										<label class="col-xs-3 control-label" for="steadyEvaluationPrincipalMoney">稳健型待收本金金额限制：</label>
										<div class="col-xs-9">
											<div class="input-group">
												<input type="text" placeholder="稳健型待收本金金额限制" id="steadyEvaluationPrincipalMoney" name="steadyEvaluationPrincipalMoney" value="<c:out value="${evaluationmoneyForm.steadyEvaluationPrincipalMoney}" />"  class="form-control"
													   datatype="/^\d{1,8}(\.\d{1,2})?$/" errormsg="稳健型待收本金金额限制，整数部分不能超过8位，小数部分不能超过2位！" maxlength="11">
												<span class="input-group-addon">元</span>
											</div>
											<hyjf:validmessage key="enterpriseMaxAmount" label="企业最高金额"></hyjf:validmessage>
										</div>
									</div>
									<div class="form-group">
										<label class="col-xs-3 control-label" for="growupEvaluationPrincipalMoney">成长型待收本金金额限制：</label>
										<div class="col-xs-9">
											<div class="input-group">
												<input type="text" placeholder="成长型待收本金金额限制" id="growupEvaluationPrincipalMoney" name="growupEvaluationPrincipalMoney" value="<c:out value="${evaluationmoneyForm.growupEvaluationPrincipalMoney}" />"  class="form-control"
													   datatype="/^\d{1,8}(\.\d{1,2})?$/" errormsg="成长型待收本金金额限制，整数部分不能超过8位，小数部分不能超过2位！" maxlength="11">
												<span class="input-group-addon">元</span>
											</div>
											<hyjf:validmessage key="enterpriseMaxAmount" label="企业最高金额"></hyjf:validmessage>
										</div>
									</div>
									<div class="form-group">
										<label class="col-xs-3 control-label" for="enterprisingEvaluationPrincipalMoney">进取型待收本金金额限制：</label>
										<div class="col-xs-9">
											<div class="input-group">
												<input type="text" placeholder="进取型待收本金金额限制" id="enterprisingEvaluationPrincipalMoney" name="enterprisingEvaluationPrincipalMoney" value="<c:out value="${evaluationmoneyForm.enterprisingEvaluationPrincipalMoney}" />"  class="form-control"
													   datatype="/^\d{1,8}(\.\d{1,2})?$/" errormsg="进取型待收本金金额限制，整数部分不能超过8位，小数部分不能超过2位！" maxlength="11">
												<span class="input-group-addon">元</span>
											</div>
											<hyjf:validmessage key="enterpriseMaxAmount" label="企业最高金额"></hyjf:validmessage>
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
		<script type='text/javascript' src="${webRoot}/jsp/manager/config/evaluationconfig/evaluationmoney/evaluationmoney.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
