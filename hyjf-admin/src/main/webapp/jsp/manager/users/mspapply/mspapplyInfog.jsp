<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp"
	flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="安融反欺诈" />

	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType"
			value="${empty mspapplyForm.id ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin: 0">
			<div class="panel-body" style="margin: 0 auto">
				<div class="panel-scroll height-360  ">
					<form id="mainForm" action="shareUser" method="post" role="form"
						class="form-horizontal">

						<div class="container">
							<div class="row">
								<div class="span6 panel-white "
									style="display: inline-block; width: 49%">
									<input type="hidden" name="id" id="id"
										value="${mspapplyForm.id }" /> <input type="hidden"
										name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" /> <input
										type="hidden" id="success" value="${success}" /> <input
										type="hidden" name="applyDate" id="applyDate"
										value="${mspapplyForm.applyDate }" /> <input type="hidden"
										name="configureId" id="configureId"
										value="${mspapplyForm.configureId }" /> <input type="hidden"
										name="shareIdentification" id="shareIdentification" value="1" />
									<div class="panel-heading">
										<H8 class="panel-title">系统信息</H8>
									</div>

									<div class="form-group">
										<label class="col-xs-3 control-label padding-top-5" for="name">
											<span class="symbol required"></span>姓名
										</label>
										<div class="col-xs-9">
											<input type="text" placeholder="请输入借款人姓名" id="name"
												name="name" value="${mspapplyForm.name}"
												class="form-control" disabled="disabled">
										</div>
									</div>
									<div class="form-group">
										<label class="col-xs-3 control-label padding-top-5"
											for="identityCard"> <span class="symbol required"></span>身份证号
										</label>
										<div class="col-xs-9">
											<input type="text" placeholder="身份证号" id="identityCard"
												name="identityCard" value="${mspapplyForm.identityCard}"
												class="form-control" disabled="disabled">
										</div>
									</div>
									<div class="form-group">
										<label class="col-xs-3 control-label padding-top-5"
											for="applyId"><span class="symbol required"></span>业务标识符
										</label>
										<div class="col-xs-9">
											<input type="text" placeholder="业务标识符" id="applyId"
												name="applyId" value="${mspapplyForm.applyId}"
												class="form-control" disabled="disabled">
										</div>
									</div>
									<div class="form-group">
										<label class="col-xs-3 control-label" for="loanType">
											<span class="symbol required"></span>借款用途
										</label>
										<div class="col-xs-9">
											<select name="loanType" class="form-control "
												disabled="disabled">
												<option value=""></option>
												<option value="01"
													<c:if test="${mspapplyForm.loanType=='01'}">selected</c:if>>经营</option>
												<option value="02"
													<c:if test="${mspapplyForm.loanType=='02'}">selected</c:if>>消费</option>
												<option value="99"
													<c:if test="${mspapplyForm.loanType=='99'}">selected</c:if>>其他</option>
											</select>
										</div>

									</div>
									<div class="form-group">
										<label class="col-xs-3 control-label" for="serviceType"><span
											class="symbol required"></span>业务类型 </label>
										<div class="col-xs-9">
											<select name="serviceType" class="form-control  form-select2" style="width: 100%">
												<option value="02"
													<c:if test="${mspapplyForm.serviceType=='02'}">selected</c:if>>一般借贷</option>
												<option value="03"
													<c:if test="${mspapplyForm.serviceType=='03'}">selected</c:if>>消费信贷</option>
												<option value="04"
													<c:if test="${mspapplyForm.serviceType=='04'}">selected</c:if>>循环贷</option>
												<option value="05"
													<c:if test="${mspapplyForm.serviceType=='05'}">selected</c:if>>其他</option>
											</select>
										</div>

									</div>
									<div class="panel-heading">
										<H8 class="panel-title">债权信息</H8>
									</div>
									<div class="form-group">
										<label class="col-xs-3 control-label padding-top-5"
											for="unredeemedMoney">未偿还本金 </label>
										<div class="col-xs-9">
											<input type="text" placeholder="未偿还本金" id="unredeemedMoney"
												name="unredeemedMoney"
												value="${mspapplyForm.unredeemedMoney}" class="form-control">
										</div>
									</div>
									<div class="form-group">
										<label class="col-xs-3 control-label" for="repaymentStatus"><span
											class="symbol required"></span>当前还款状态 </label>
										<div class="col-xs-9 admin-select">
											<select id="repaymentStatus" name="repaymentStatus"
												class="form-control">
												<option value="01"
													<c:if test="${mspapplyForm.repaymentStatus == 01}">selected="selected"</c:if>>正常</option>
												<option value="02"
													<c:if test="${mspapplyForm.repaymentStatus == 02}">selected="selected"</c:if>>逾期中</option>
												<option value="03"
													<c:if test="${mspapplyForm.repaymentStatus == 03}">selected="selected"</c:if>>逾期核销</option>
												<option value="04"
													<c:if test="${mspapplyForm.repaymentStatus == 04}">selected="selected"</c:if>>正常结清</option>
											</select>
										</div>
									</div>
									<div class="form-group">
										<label class="col-xs-3 control-label padding-top-5"
											for="overdueAmount">逾期总金额 </label>
										<div class="col-xs-9">
											<input type="text" placeholder="逾期总金额 " id="overdueAmount"  disabled="disabled"
												name="overdueAmount" value="" class="form-control">
										</div>
									</div>
									<div class="form-group">
										<label class="col-xs-3 control-label padding-top-5"
											>逾期开始日期 </label>

										<div class="col-xs-9  input-daterange datepicker "
											style="width: 75%">
											<input type="text" id="overdueDate" name="overdueDate" disabled="disabled"
												value=""  class="hui form-control"
												maxlength="10" />

										</div>
									</div>
									<div class="form-group">
										<label class="col-xs-3 control-label padding-top-5"
											for="overdueLength"> 逾期时长 </label>
										<div class="col-xs-9">
											<input type="text" placeholder="逾期时长" id="overdueLength" disabled="disabled"
												name="overdueLength" value="" class="form-control">
										</div>
									</div>
									<div class="form-group">
										<label class="col-xs-3 control-label" for="overdueReason">
											逾期原因 </label>
										<div class="col-xs-9 admin-select">
											<select id="overdueReason" name="overdueReason"  disabled="disabled"
												class="form-control form-select2" style="width: 100%">
												<option value=""></option>
												<option value="01">能力下降</option>
												<option value="02">恶意拖欠</option>
												<option value="03">身份欺诈</option>
												<option value="04">逃逸</option>
												<option value="05">犯罪入狱</option>
												<option value="06">疾病</option>
												<option value="07">死亡</option>
												<option value="99">其他</option>
											</select>
										</div>
									</div>
								</div>

								<div class="span6 panel-white "
									style="display: inline-block; width: 49%; vertical-align: top">
									<div class="panel-heading">
										<H8 class="panel-title">审批信息</H8>
									</div>
									<div class="form-group" id="expType-1">
										<label class="col-xs-3 control-label padding-top-5"
											for="approvalDate"> <span class="symbol required"></span>审批日期
										</label>
										<div class="col-xs-9  input-daterange datepicker"
											style="width: 75%">
											<input type="text" id="approvalDate" name="approvalDate"
												value="${mspapplyForm.approvalDate }" datatype="*"
												maxlength="10" />
										</div>
										<hyjf:validmessage key="expirationDateStr" label="审批日期"></hyjf:validmessage>
									</div>

									<div class="form-group">
										<label class="col-xs-3 control-label" for="approvalResult">
											<span class="symbol required"></span>审批结果
										</label>
										<div class="col-xs-9 admin-select">
											<select id="approvalResult" name="approvalResult"
												class="form-control  form-select2" style="width: 100%">
												<option value="01"
													<c:if test="${mspapplyForm.approvalResult == 01}">selected="selected"</c:if>>审批通过</option>
												<option value="02"
													<c:if test="${mspapplyForm.approvalResult == 02}">selected="selected"</c:if>>审批拒绝</option>
												<option value="04"
													<c:if test="${mspapplyForm.approvalResult == 03}">selected="selected"</c:if>>重新审批</option>
												<option value="05"
													<c:if test="${mspapplyForm.approvalResult == 04}">selected="selected"</c:if>>客户取消</option>
											</select>
										</div>
									</div>

									<div class="panel-heading">
										<H8 class="panel-title">合同信息</H8>
									</div>


									<div class="form-group">
										<label class="col-xs-3 control-label padding-top-5"
											for="loanMoney"> <span class="symbol required"></span>合同金额
										</label>
										<div class="col-xs-9">
											<input disabled="disabled" type="text" placeholder="借款金额"
												id="loanMoney" name="loanMoney"
												value="${mspapplyForm.loanMoney}" class="form-control">

										</div>
									</div>


									<div class="form-group" id="expType-1">
										<label class="col-xs-3 control-label padding-top-5"
											for="contractBegin"> <span class="symbol required"></span>合同借款开始日期
										</label>
										<div class="col-xs-9  input-daterange datepicker"
											style="width: 75%">
											<input type="text" id="contractBegin" name="contractBegin"
												value="${mspapplyForm.contractBegin }" 
												maxlength="10"  class="hui form-control"/>

										</div>
										<hyjf:validmessage key="expirationDateStr" label="合同借款开始日期"></hyjf:validmessage>
									</div>


									<div class="form-group" id="expType-1">
										<label class="col-xs-3 control-label padding-top-5"
											for="contractEnd"> <span class="symbol required"></span>合同借款到期日期
										</label>
										<div class="col-xs-9  input-daterange datepicker"
											style="width: 75%">
											<input type="text" id="contractEnd" name="contractEnd"
												value="${mspapplyForm.contractEnd }" 
												maxlength="10" class="hui form-control" />

										</div>
										<hyjf:validmessage key="expirationDateStr" label="合同借款到期日期"></hyjf:validmessage>
									</div>



									<div class="form-group">
										<label class="col-xs-3 control-label" for="guaranteeType">
											<span class="symbol required"></span>担保类型
										</label>
										<div class="col-xs-9 admin-select">
											<select id="guaranteeType" name="guaranteeType"
												class="form-select2 hui form-control" style="width: 100%" >
												<option value="A"
													<c:if test="${mspapplyForm.guaranteeType=='A'}">selected</c:if>>抵押</option>
												<option value="B"
													<c:if test="${mspapplyForm.guaranteeType=='B'}">selected</c:if>>质押</option>
												<option value="C"
													<c:if test="${mspapplyForm.guaranteeType=='C'}">selected</c:if>>担保</option>
												<option value="D"
													<c:if test="${mspapplyForm.guaranteeType=='D'}">selected</c:if>>信用</option>
												<option value="E"
													<c:if test="${mspapplyForm.guaranteeType=='E'}">selected</c:if>>保证</option>
												<option value="Y"
													<c:if test="${mspapplyForm.guaranteeType=='Y'}">selected</c:if>>其他</option>
											</select>
										</div>
									</div>


									<div disabled="disabled" class="form-group">
										<label class="col-xs-3 control-label" for="creditAddress">
											<span class="symbol required"></span>借款城市
										</label>
										<div class="col-xs-9 admin-select">
											<select id="creditAddress" name="creditAddress"
												style="width: 100%" disabled="disabled" class="hui form-control">
												<c:forEach items="${regionList}" var="item">
													<option value="${item.regionId}"
														<c:if test="${item.regionId==mspapplyForm.creditAddress}">selected</c:if>>${item.regionName }</option>
												</c:forEach>
											</select>
										</div>
										<input type="hidden" name="configureId" id="configureId"
											value="">

									</div>


									<div class="form-group">
										<label disabled="disabled"
											class="col-xs-3 control-label padding-top-5"
											for="loanTimeLimit"> <span class="symbol required"></span>还款期数(月)
										</label>
										<div class="col-xs-9">
											<input disabled="disabled" type="text" placeholder="借款期数" class="hui form-control"
												id="loanTimeLimit" name="loanTimeLimit"
												value="${mspapplyForm.loanTimeLimit}">
										</div>
									</div>

								</div>
								<div class="form-group margin-bottom-0">
									<div class="col-xs-offset-5 col-xs-10">
										<a class="btn btn-o btn-primary fn-Confirm"><i
											class="fa fa-check"></i> 确 认</a> <a
											class="btn btn-o btn-primary fn-Cancel"><i
											class="fa fa-close"></i> 取 消</a>
									</div>
								</div>
							</div>

						</div>


					</form>
				</div>
			</div>
		</div>
	</tiles:putAttribute>

	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
		<link
			href="${themeRoot}/vendor/plug-in/bootstrap-fileinput/jasny-bootstrap.min.css"
			rel="stylesheet" media="screen">
		<style>
.purMargin {
	margin: 8px 0;
}

.purMargin input {
	width: 200px;
}
</style>
	</tiles:putAttribute>

	<%-- JS全局变量定义、插件 (ignore) --%>
	<tiles:putAttribute name="pageGlobalImport" type="string">
		<!-- Form表单插件 -->
		<%@include file="/jsp/common/pluginBaseForm.jsp"%>
		<script
			src="${themeRoot}/vendor/plug-in/bootstrap-fileinput/jasny-bootstrap.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/bootstrap-ladda/spin.min.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/bootstrap-ladda/ladda.min.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/tinymce/jquery.tinymce.min.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/My97DatePicker/WdatePicker.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/region-select.js"></script>
		<script type="text/javascript" src="${themeRoot}/assets/js/common.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery-file-upload/vendor/jquery.ui.widget.js"></script>
		<!-- The Templates plugin is included to render the upload/download listings -->
		<%-- 		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/vendor/tmpl.min.js"></script> --%>
		<!-- The Load Image plugin is included for the preview images and image resizing functionality -->
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery-file-upload/vendor/load-image.all.min.js"></script>
		<!-- The Canvas to Blob plugin is included for image resizing functionality -->
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery-file-upload/vendor/canvas-to-blob.min.js"></script>
		<!-- blueimp Gallery script -->
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery-file-upload/vendor/jquery.blueimp-gallery.min.js"></script>
		<!-- The Iframe Transport is required for browsers without support for XHR file uploads -->
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.iframe-transport.js"></script>
		<!-- The basic File Upload plugin -->
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload.js"></script>
		<!-- The File Upload processing plugin -->
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload-process.js"></script>
		<!-- The File Upload image preview & resize plugin -->
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload-image.js"></script>
		<!-- The File Upload audio preview plugin -->
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload-audio.js"></script>
		<!-- The File Upload video preview plugin -->
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload-video.js"></script>
		<!-- The File Upload validation plugin -->
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload-validate.js"></script>
		<!-- The File Upload user interface plugin -->
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload-ui.js"></script>


	</tiles:putAttribute>
	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
		<script type='text/javascript'
			src="${webRoot}/jsp/manager/users/mspapply/mspapplyInfog.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/selectFx/classie.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/selectFx/selectFx.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/select2/select2.min.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/bootstrap-datepicker/bootstrap-datepicker.min.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
