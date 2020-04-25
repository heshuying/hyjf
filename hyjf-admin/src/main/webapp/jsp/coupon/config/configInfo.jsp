<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp"
	flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="优惠券管理" />
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
	<style>
		.panel-title {
			font-family: "微软雅黑"
		}
	</style>
	<link href="${themeRoot}/vendor/plug-in/select2/select2.min.css" rel="stylesheet" media="screen">
	</tiles:putAttribute>
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType"
			value="${empty couponConfigForm.id ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin: 0">
			<div class="panel-body" style="margin: 0 auto">
				<div class="panel-scroll height-430">
					<form id="mainForm" action="insertAction" method="post" role="form"
						class="form-horizontal">
						<%-- 银行列表一览 --%>
						<input type="hidden" name="id" id="id"
							value="<c:out value="${couponConfigForm.id }"/>" /> <input
							type="hidden" name="pageToken"
							value="${sessionScope.RESUBMIT_TOKEN}" /> <input type="hidden"
							id="success" value="${success}" />

						<div class="panel panel-rounded">
							<div class="panel-heading">
								<h3 class="panel-title">基本信息</h3>

							</div>
							<div class="panel-body">
								<div id="chargeTimeDiv" class="form-group">
									<label class="col-sm-2 control-label" for="couponName">
										<span class="symbol required"></span>名称：
									</label>
									<div class="col-sm-9">
										<input type="text" id="couponName" name="couponName"
											value="<c:out value="${couponConfigForm.couponName}"></c:out>"
											class="form-control" datatype="*1-20"
											errormsg="优惠券名称输入1~20个字符！" maxlength="20" />
										<hyjf:validmessage key="couponName" label="名称"></hyjf:validmessage>
									</div>
								</div>

								<div class="form-group">
									<label class="col-sm-2 control-label" for="couponType">
										<span class="symbol required"></span>优惠券类型：
									</label>
									<div class="col-sm-9 admin-select">
										<div class="radio clip-radio radio-primary ">
											
												<input type="radio" id="couponTypeExperience" name="couponType"
												datatype="*" value="1" class="event-categories"
												${couponConfigForm.couponType == '1' ? 'checked' : ''} /> <label
												for="couponTypeExperience">体验金 </label>
												
												
												<input type="radio"
												id="couponTypeInterestIncrease" name="couponType" datatype="*" value="2"
												class="event-categories"
												${couponConfigForm.couponType == '2' ? 'checked' : ''} /> <label
												for="couponTypeInterestIncrease">加息券 </label>
												
												<input type="radio" id="couponTypeCash" name="couponType"
												datatype="*" value="3" class="event-categories"
												${couponConfigForm.couponType == '3' ? 'checked' : ''} /> <label
												for="couponTypeCash">代金券 </label>
												
												
												<hyjf:validmessage key="couponType" label="优惠券类型"></hyjf:validmessage>
										</div>
									</div>
								</div>

								<div id="chargeTimeDiv" class="form-group">
									<label class="col-sm-2 control-label" for="couponQuota">
										<span class="symbol required"></span>面值：
									</label>
									<div class="col-sm-4">
										<div class="input-group">
										<!-- datatype="/^([1-9][\d]{0,7})|(([1-9][\d]{0,4}|0)(\.[\d]{1,2}))?$/" -->
										
											<input type="text" id="couponQuota" name="couponQuota"
												value="<c:out value="${couponConfigForm.couponQuota}"></c:out>"
												class="form-control"
												datatype="/^([1-9][\d]{0,7})$|^(([1-9][\d]{0,4}|0)+(.[0-9]{1,2}))?$/"
												
												errormsg="请输入面值格式" maxlength="6" />
												
												<span id="spanOn" style="display:none"
												class="input-group-addon">&nbsp;元</span>
												<span id="spanOff"
												class="input-group-addon">&nbsp;%</span>
											<hyjf:validmessage key="couponQuota" label="面值"></hyjf:validmessage>
										</div>
									</div>
								</div>
								
								<div class="form-group" id="profitTimeArea" style="display: none;">
									<label class="col-sm-2 control-label" for="couponProfitTime">
										<span class="symbol required"></span>收益期限：
									</label>
									<div class="col-sm-4">
										<div class="input-group">
											<input type="text" id="couponProfitTime"
												name="couponProfitTime"
												value="<c:out value="${couponConfigForm.couponProfitTime}"></c:out>"
												class="form-control" datatype="n1-4" maxlength="3" /> <span
											class="input-group-addon">&nbsp;天</span>
											<hyjf:validmessage key="couponProfitTime" label="收益期限"></hyjf:validmessage>
										</div>
									</div>
								</div>
								
								<div id="chargeTimeDiv" class="form-group">
									<label class="col-sm-2 control-label" for="sourceName">
										<span class="symbol required"></span>发行数量：
									</label>
									<div class="col-sm-9">
										<input type="text" id="couponQuantity" name="couponQuantity"
											value="<c:out value="${couponConfigForm.couponQuantity}"></c:out>"
											class="form-control" datatype="n1-10"
											errormsg="发行数量应该输入1~10位数字信息！" maxlength="10"
											<c:if test="${not empty couponConfigForm.id}">ajaxurl="checkAction?code=${couponConfigForm.couponCode}"</c:if> />
										<hyjf:validmessage key="couponQuantity" label="发行数量"></hyjf:validmessage>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-2 control-label" for="expirationType">
										<span class="symbol required"></span>有效期类型：
									</label>
									<div class="col-sm-9 admin-select">
										<select id="expirationType" name="expirationType"
											class="form-select2">
											<c:forEach items="${expType }" var="item">
												<option value="${item.nameCd}">${item.name }</option>
											</c:forEach>
										</select>
									</div>
								</div>
								<div class="form-group" id="expType-1" style="display: none;">
									<label class="col-sm-2 control-label" for="expirationDate">
										<span class="symbol required"></span>有效期截止日：
									</label>
									<div class="col-sm-4 input-daterange datepicker">
										<input type="text" id="expirationDateStr"
											name="expirationDateStr"
											value="${couponConfigForm.expirationDateStr}" datatype="*"
											maxlength="10" class="form-control" />
										<hyjf:validmessage key="expirationDateStr" label="有效期截止日"></hyjf:validmessage>
									</div>
								</div>

								<div class="form-group" id="expType-2" style="display: none;">
									<label class="col-sm-2 control-label" for="expirationLength">
										<span class="symbol required"></span>有效期时长：
									</label>
									<div class="col-sm-4">
										<div class="input-group">
											<input type="text" id="expirationLength"
												name="expirationLength"
												value="<c:out value="${couponConfigForm.expirationLength}"></c:out>"
												class="form-control" datatype="n1-2" maxlength="2" /> <span
											class="input-group-addon">&nbsp;个月</span>
											<hyjf:validmessage key="expirationLength" label="有效期时长"></hyjf:validmessage>
										</div>
									</div>
								</div>
								
								<div class="form-group" id="expType-3" style="display: none;">
									<label class="col-sm-2 control-label" for="expirationLength">
										<span class="symbol required"></span>有效期时长：
									</label>
									<div class="col-sm-4">
										<div class="input-group">
											<input type="text" id="expirationLengthDay"
												name="expirationLengthDay"
												value="<c:out value="${couponConfigForm.expirationLengthDay}"></c:out>"
												class="form-control" datatype="n1-4" maxlength="4" /> <span
											class="input-group-addon">&nbsp;天</span>
											<hyjf:validmessage key="expirationLengthDay" label="有效期时长"></hyjf:validmessage>
										</div>
									</div>
								</div>
								
								<div class="form-group" id= "addFlgArea" style="display: none;">
									<label class="col-sm-2 control-label" for="couponType">
										<span class="symbol required"></span>是否与本金共用：
									</label>
									<div class="col-sm-9 admin-select">
										<div class="radio clip-radio radio-primary ">
												<input type="radio" id="addFlgYes" name="addFlg" datatype="*" value="0" class="event-categories"/> 
												<label for="addFlgYes">是</label>
												
												<input type="radio" id="addFlgNo" name="addFlg" datatype="*" value="1" class="event-categories"/> 
												<label for="addFlgNo">否 </label>
												
												<hyjf:validmessage key="addFlg" label="是否与本金共用"></hyjf:validmessage>
										</div>
									</div>
								</div>

								<div class="form-group" id= "repayTimeConfigArea" style="display: none;">
									<label class="col-sm-2 control-label" for="repayTimeConfig">
										<span class="symbol required"></span>还款时间：
									</label>
									<div class="col-sm-9 admin-select">
										<div class="radio clip-radio radio-primary ">
												<input type="radio" id="repayTimeConfig2" name="repayTimeConfig" datatype="*" value="1" class="event-categories"/> 
												<label for="repayTimeConfig2">项目到期 </label>
												
												<input type="radio" id="repayTimeConfig1" name="repayTimeConfig" datatype="*" value="2" class="event-categories"/> 
												<label for="repayTimeConfig1">收益期限到期</label>
												
												<hyjf:validmessage key="repayTimeConfig" label="还款时间配置"></hyjf:validmessage>
										</div>
									</div>
								</div>
								
								<div class="form-group">
									<label class="col-sm-2 control-label" for="content">描述：
									</label>
									<div class="col-sm-10">
										<textarea id="content" name="content" placeholder="描述"
											class="form-control limited" maxlength="100"><c:out
												value="${couponConfigForm.content}" /></textarea>
										<hyjf:validmessage key="content" label="描述"></hyjf:validmessage>
									</div>
								</div>
							</div>
						</div>
						<hr>
						<div class="panel panel-rounded">
							<div class="panel-heading">
								<h3 class="panel-title">使用范围</h3>

							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label" for="projectType">操作平台：</label>
								<div class="col-sm-9 checkbox clip-check check-primary inline"
									style="padding-left: 15px;">
									<input type="checkbox" id="ct-all" name="couponSystemAll" checked="checked"
										value="-1"><label for="ct-all" >全部平台</label>
									<c:forEach items="${clients }" var="client" begin="0" step="1"
										varStatus="status">
										<input type="checkbox" id="ct-${status.index }" datatype="*" checked="checked"
											name="couponSystem" value="${client.nameCd}"
											<c:forEach items="${selectedClientList }" var="sc" begin="0" step="1"> </c:forEach>>
										<label for="ct-${status.index }">${client.name }</label>
									</c:forEach>
									<hyjf:validmessage key="couponSystem" label="操作平台"></hyjf:validmessage>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label" for="projectType">项目类型：</label>
								<div class="col-sm-10 checkbox clip-check check-primary inline"
									style="padding-left: 15px;">
									<input type="checkbox" id="pt-all" name="projectTypeAll" checked="checked"
										value="-1"><label for="pt-all">全部类型</label>
									<c:forEach items="${couponProjectTypes }" var="pt" begin="0" step="1"
										varStatus="status">
										<input type="checkbox" id="pt-${status.index }" datatype="*" checked="checked"
											name="projectType" value="${pt.borrowCd}"
											<c:forEach items="${selectedProjectList }" var="sp" begin="0" step="1"> </c:forEach> />
										<label for="pt-${status.index }">${pt.borrowName }</label>
									</c:forEach>
									<hyjf:validmessage key="projectType" label="项目类型"></hyjf:validmessage>
								</div>
							</div>

							<div class="form-group">
								<label class="col-sm-2 control-label"
									for="projectExpirationType"> <span
									class="symbol required"></span>项目期限：
								</label>
								<div class="col-sm-9 admin-select">
									<select id="projectExpirationType" name="projectExpirationType"
										class="form-select2">
										<option value="0"
											<c:if test="${couponConfigForm.projectExpirationType == 0}">selected="selected"</c:if>>不限</option>
										<option value="1"
											<c:if test="${couponConfigForm.projectExpirationType == 1}">selected="selected"</c:if>>等于</option>
										<option value="2"
											<c:if test="${couponConfigForm.projectExpirationType == 2}">selected="selected"</c:if>>期限范围</option>
										<option value="3"
											<c:if test="${couponConfigForm.projectExpirationType == 3}">selected="selected"</c:if>>大于等于</option>
										<option value="4"
											<c:if test="${couponConfigForm.projectExpirationType == 4}">selected="selected"</c:if>>小于等于</option>
									</select>
								</div>
							</div>

							<div class="form-group" id="protype-1" style="display: none;">
								<label class="col-sm-2 control-label" for="expirationDate">
									<span class="symbol required"></span>时间段：
								</label>
								<div class="col-sm-3">
								<div class="input-group">
									<input type="text" id="projectExpirationLengthMin"
										name="projectExpirationLengthMin"
										value="<c:out value="${couponConfigForm.projectExpirationLengthMin}"></c:out>"
										class="form-control" datatype="n1-2" maxlength="2" /><span
											class="input-group-addon">个月</span>
									<hyjf:validmessage key="projectExpirationLengthMin"
										label="项目期限时间段"></hyjf:validmessage>
								</div>
								</div>
								<div class="col-sm-1" style="width: 1px;">
									<label style="margin: 0 auto; line-height: center"> ~ </label>
								</div>

								<div class="col-sm-3">
								<div class="input-group">
									<input type="text" id="projectExpirationLengthMax"
										name="projectExpirationLengthMax"
										value="<c:out value="${couponConfigForm.projectExpirationLengthMax}"></c:out>"
										class="form-control" datatype="n1-2" maxlength="2" /><span
											class="input-group-addon">个月</span>
									<hyjf:validmessage key="projectExpirationLengthMax"
										label="项目期限时间段"></hyjf:validmessage>
								</div>
								</div>
							</div>

							<div class="form-group" id="protype-2" style="display: none;">
								<label class="col-sm-2 control-label"
									for="projectExpirationLength"> <span
									class="symbol required"></span>项目期限时长：
								</label>
								<div class="col-sm-4">
									<div class="input-group">
										<input type="text" id="projectExpirationLength"
											name="projectExpirationLength"
											value="<c:out value="${couponConfigForm.projectExpirationLength}"></c:out>"
											class="form-control" datatype="n1-2" maxlength="2" /> <span
											class="input-group-addon">&nbsp;个月</span>
										<hyjf:validmessage key="projectExpirationLength" label="项目期限时长"></hyjf:validmessage>
									</div>
								</div>
							</div>


							<div class="form-group">
								<label class="col-sm-2 control-label" for="tenderQuotaType">
									<span class="symbol required"></span>出借金额：
								</label>
								<div class="col-sm-9 admin-select">
									<select id="tenderQuotaType" name="tenderQuotaType"
										class="form-select2">
										<option value="0"
											<c:if test="${couponConfigForm.tenderQuotaType == 0}">selected="selected"</c:if>>不限</option>
										<option value="1"
											<c:if test="${couponConfigForm.tenderQuotaType == 1}">selected="selected"</c:if>>金额范围</option>
										<option value="2"
											<c:if test="${couponConfigForm.tenderQuotaType == 2}">selected="selected"</c:if>>大于等于</option>
									</select>
								</div>
							</div>

							<div class="form-group" id="tender-1">
								<label class="col-sm-2 control-label" for="tenderQuota">
									<span class="symbol required"></span>金额范围：
								</label>
								<div class="col-sm-3">
									<input type="text" id="tenderQuotaMin" name="tenderQuotaMin"
										value="<c:out value="${couponConfigForm.tenderQuotaMin}"></c:out>"
										class="form-control"
										datatype="/^([1-9][\d]{0,10}|0)?$/"
										errormsg="请输入金额格式" maxlength="10" />
									<hyjf:validmessage key="tenderQuotaMin" label="最小金额"></hyjf:validmessage>
								</div>
								<div class="col-sm-1" style="width: 1px;">
									<label style="margin: 0 auto; line-height: center"> ~ </label>
								</div>
								<div class="col-sm-3">
									<input type="text" id="tenderQuotaMax" name="tenderQuotaMax"
										value="<c:out value="${couponConfigForm.tenderQuotaMax}"></c:out>"
										class="form-control"
										datatype="/^([1-9][\d]{0,10}|0)?$/"
										errormsg="请输入金额格式" maxlength="10" />
									<hyjf:validmessage key="tenderQuotaMax" label="最大金额"></hyjf:validmessage>
								</div>
							</div>

							<div class="form-group" id="tender-2">
								<label class="col-sm-2 control-label" for="tenderQuota">
									<span class="symbol required"></span>金额（大于等于）：
								</label>
								<div class="col-sm-4">
									<div class="input-group">
										<input type="text" id="tenderQuota" name="tenderQuota"
											value="<c:out value="${couponConfigForm.tenderQuota}"></c:out>"
											class="form-control"
											datatype="/^([1-9][\d]{0,10}|0)?$/"
											errormsg="请输入金额格式" maxlength="10" /> <span
											class="input-group-addon">元</span>
										<hyjf:validmessage key="tenderQuota" label="金额"></hyjf:validmessage>
									</div>
								</div>
							</div>
						</div>
				</div>
				<div class="form-group margin-bottom-0">
					<div class="col-sm-offset-4 col-sm-10">
						<a class="btn btn-o btn-primary fn-Confirm"><i
							class="fa fa-check"></i> 提 交</a> <a
							class="btn btn-o btn-primary fn-Cancel"><i
							class="fa fa-close"></i> 返回列表</a>
					</div>
				</div>
				</form>
			</div>
		</div>
		</div>
	</tiles:putAttribute>
	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/selectFx/classie.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/selectFx/selectFx.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/select2/select2.min.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/bootstrap-datepicker/bootstrap-datepicker.min.js"></script>
		<script type='text/javascript'
			src="${webRoot}/jsp/coupon/config/configInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
