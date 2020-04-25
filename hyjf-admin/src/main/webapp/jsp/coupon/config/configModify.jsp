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
		<link href="${themeRoot}/vendor/plug-in/select2/select2.min.css"
			rel="stylesheet" media="screen">
	</tiles:putAttribute>
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType"
			value="${empty couponConfigForm.id ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin: 0">
			<div class="panel-body" style="margin: 0 auto">
				<div class="panel-scroll height-430">
					<form id="mainForm" action="updateAction" method="post" role="form"
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
								<div class="form-group">
									<label class="col-sm-2 control-label" for="couponName">
										<span class="symbol required"></span>名称：
									</label>
									<div class="col-sm-9">
										<input type="text" id="couponName" name="couponName"
											value="<c:out value="${couponConfigForm.couponName}"></c:out>"
											class="form-control" datatype="*1-20"
											errormsg="优惠券名称输入1~50个字符！" maxlength="20" />
										<hyjf:validmessage key="couponName" label="名称"></hyjf:validmessage>
									</div>

								</div>

								<%-- <div class="form-group">
									<label class="col-sm-2 control-label" for="couponType">
										优惠券类型： </label>
									<div class="col-sm-9 ">
										<c:choose>
											<c:when test="${couponConfigForm.couponType == '1' }">
												<label class="control-label"> 体验金 </label>
											</c:when>
											<c:otherwise>
												<label class="control-label"> 加息券 </label>
											</c:otherwise>
										</c:choose>

									</div>
								</div> --%>

								<div id="chargeTimeDiv" class="form-group">
									<label class="col-sm-2 control-label" for="couponQuota">
										面值： </label>
									<div class="col-sm-9">
										<label class="control-label"> <c:out
												value="${couponConfigForm.couponQuota}"></c:out>
												<c:if test="${couponConfigForm.couponType == '1' or couponConfigForm.couponType == '3'}">元</c:if>
												<c:if test="${couponConfigForm.couponType == '2' }">%</c:if>
										</label>
									</div>
								</div>

								<c:if test="${couponConfigForm.couponType == 1 }">
									<div id="chargeTimeDiv" class="form-group">
										<label class="col-sm-2 control-label" for="sourceName">
											收益期限： </label>
										<div class="col-sm-9">
											<label class="control-label"> <c:out
													value="${couponConfigForm.couponProfitTime}"></c:out>天
											</label>
	
										</div>
									</div>
								</c:if>
								
								<div id="chargeTimeDiv" class="form-group">
									<label class="col-sm-2 control-label" for="sourceName">
										<span class="symbol required"></span>发行数量：
									</label>
									<div class="col-sm-9">
									<div class="input-group">
										<input type="text" id="couponQuantity" name="couponQuantity"
											value="<c:out value="${couponConfigForm.couponQuantity}"></c:out>"
											class="form-control" datatype="n1-10"
											errormsg="发行数量应该输入1~10位数字信息！" maxlength="10"
											<c:if test="${not empty couponConfigForm.id}">ajaxurl="checkAction?code=${couponConfigForm.couponCode}"</c:if> />
											<span
											class="input-group-addon">张</span>
										<hyjf:validmessage key="couponQuantity" label="发行数量"></hyjf:validmessage>
									</div>
									</div>
								</div>
								<%-- <div class="form-group">
									<label class="col-sm-2 control-label" for="expirationType">
										有效期类型： </label>
									<div class="col-sm-9 ">
										<c:choose>
											<c:when test="${couponConfigForm.expirationType == 1}">
												<label class="control-label"> 截止日 </label>
											</c:when>
											<c:otherwise>
												<label class="control-label"> 有效时长 </label>
											</c:otherwise>
										</c:choose>

									</div>
								</div> --%>
								<c:choose>
									<c:when test="${couponConfigForm.expirationType == 1}">
										<div class="form-group" id="expType-1">
											<label class="col-sm-2 control-label" for="expirationDate">
												有效期： </label>
											<div class="col-sm-4 ">
												<label class="control-label"> <c:out
														value="${couponConfigForm.expirationDateStr}"></c:out>
												</label>

											</div>
										</div>
									</c:when>
									<c:when test="${couponConfigForm.expirationType == 2}">
										<div class="form-group" id="expType-2">
											<label class="col-sm-2 control-label" for="expirationLength">
												有效期： </label>
											<div class="col-sm-4">
												<label class="control-label"> <c:out
														value="${couponConfigForm.expirationLength}"></c:out>&nbsp;个月
												</label>
											</div>
										</div>
									</c:when>
									<c:when test="${couponConfigForm.expirationType == 3}">
										<div class="form-group" id="expType-3">
											<label class="col-sm-2 control-label" for="expirationLengthDay">
												有效期： </label>
											<div class="col-sm-4">
												<label class="control-label"> <c:out
														value="${couponConfigForm.expirationLengthDay}"></c:out>&nbsp;天
												</label>
											</div>
										</div>
									</c:when>
									<c:otherwise>
										<div class="form-group" id="expType-2">
											<label class="col-sm-2 control-label" for="expirationLength">
												有效期： </label>
											<div class="col-sm-4">
												<label class="control-label"> <c:out
														value="${couponConfigForm.expirationLength}"></c:out>&nbsp;个月
												</label>
											</div>
										</div>
									</c:otherwise>
								</c:choose>
								<c:if test="${couponConfigForm.couponType == 1 }">
									<div id="chargeTimeDiv" class="form-group">
										<label class="col-sm-2 control-label" for="sourceName">
											是否与本金共用： </label>
										<div class="col-sm-9">
											<label class="control-label"> <c:out
													value="${couponConfigForm.addFlg == 0 ? '是' : '否'}"></c:out>
											</label>
	
										</div>
									</div>
								</c:if>
								<c:if test="${couponConfigForm.couponType == 1 }">
									<div id="chargeTimeDiv" class="form-group">
										<label class="col-sm-2 control-label" for="sourceName">
											还款时间： </label>
										<div class="col-sm-9">
											<label class="control-label"> <c:out
													value="${couponConfigForm.repayTimeConfig == 1 ? '项目到期' : '收益期限到期'}"></c:out>
											</label>
	
										</div>
									</div>
								</c:if>
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
								<div class="col-sm-9 " style="padding-left: 15px;">
									<label class="control-label"> <c:out
											value="${selectedClientDisplay }"></c:out>
									</label>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label" for="projectType">项目类型：</label>
								<div class="col-sm-9 " style="padding-left: 15px;">
									<label class="control-label"> <c:out
											value="${selectedProjectDisplay }"></c:out>
									</label>

								</div>
							</div>

							<div class="form-group">
								<label class="col-sm-2 control-label"
									for="projectExpirationType">项目期限： </label>
								<div class="col-sm-9 ">
									<c:choose>
										<c:when test="${couponConfigForm.projectExpirationType == 0}">
											<label class="control-label"> 不限 </label>
										</c:when>
										<c:when test="${couponConfigForm.projectExpirationType == 1}">
											<label class="control-label"> <c:out
													value="${couponConfigForm.projectExpirationLength}"></c:out>
												个月
											</label>
										</c:when>
										<c:when test="${couponConfigForm.projectExpirationType == 2}">
											<label class="control-label"> <c:out
													value="${couponConfigForm.projectExpirationLengthMin}"></c:out>&nbsp;个月&nbsp;&nbsp;~&nbsp;&nbsp;
												<c:out
													value="${couponConfigForm.projectExpirationLengthMax}"></c:out>&nbsp;个月
											</label>

										</c:when>
										<c:when test="${couponConfigForm.projectExpirationType == 3}">
											<label class="control-label"> 大于等于&nbsp;<c:out
													value="${couponConfigForm.projectExpirationLength}"></c:out>
												个月
											</label>
										</c:when>
										<c:when test="${couponConfigForm.projectExpirationType == 4}">
											<label class="control-label"> 小于等于&nbsp;<c:out
													value="${couponConfigForm.projectExpirationLength}"></c:out>
												个月
											</label>
										</c:when>
									</c:choose>
								</div>
							</div>

							<div class="form-group">
								<label class="col-sm-2 control-label" for="tenderQuotaType">
									出借金额： </label>
								<div class="col-sm-9 ">
									<c:choose>
										<c:when test="${couponConfigForm.tenderQuotaType == 0}">
											<label class="control-label"> 不限 </label>
										</c:when>
										<c:when test="${couponConfigForm.tenderQuotaType == 1}">
											<label class="control-label"> <c:out
													value="${couponConfigForm.tenderQuotaMin }"></c:out>&nbsp;元&nbsp;&nbsp;~&nbsp;&nbsp;<c:out
													value="${couponConfigForm.tenderQuotaMax }"></c:out>&nbsp;元
											</label>
										</c:when>
										<c:when test="${couponConfigForm.tenderQuotaType == 2}">
											<label class="control-label"> 大于等于&nbsp;<c:out
													value="${couponConfigForm.tenderQuota}"></c:out>&nbsp;元
											</label>
										</c:when>
									</c:choose>

								</div>
							</div>
						</div>
						<hr>
						<div class="panel panel-rounded">
							<div class="panel-heading">
								<h3 class="panel-title">审核信息</h3>
							</div>
							<div class="panel-body">
								<div class="form-group">
									<label class="col-sm-2 control-label">审核人： </label>
									<div class="col-sm-9 ">
										<label class="control-label"> <c:out
												value="${couponConfigForm.auditUserRealname}"></c:out>
										</label>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-2 control-label">审核时间： </label>
									<div class="col-sm-9 ">
										<label class="control-label"> <c:out
												value="${couponConfigForm.auditTimeDisplay}"></c:out>
										</label>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-2 control-label">审核备注： </label>
									<div class="col-sm-9 ">
										<label> <c:out
												value="${couponConfigForm.auditContent}"></c:out>
										</label>
									</div>
								</div>
							</div>
						</div>
						<div class="form-group margin-bottom-0">
							<div class="col-sm-offset-4 col-sm-10">
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
			src="${webRoot}/jsp/coupon/config/configModify.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
