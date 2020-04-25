<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="" scope="request"></c:set>

<!-- 画面功能路径(ignore) -->
<tiles:insertTemplate template="/jsp/layout/mainLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="标签管理" />
		
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType"
			value="${empty labelForm.id ? '添加' : '修改'}"></c:set>
		<!-- start -->
		<div class="container-fluid container-fullw">
			<div class="row">
				<div class="col-md-12">
					<h2></h2>
					<p>
						<i class="ti-info-alt text-primary"></i> 请在这里填写您本次提交标签的基本信息.
					</p>
					<hr>
					<form id="mainForm"	action="${empty labelForm.id ? 'insertAction' : 'updateAction'}" method="post" role="form" >
					<%-- 文章列表一览 --%>
						<input type="hidden" name="id" id="id"
							value="${labelForm.id }" /> <input type="hidden"
							name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" /> <input
							type="hidden" id="success" value="${success}" />
							
						<!-- 新加字段 -->
					<div class="row">
					
						<div class="col-md-10">
							<div class="row">
								<div class="col-md-10 col-lg-6">
									<div class="form-group">
										<label class="control-label">
											标签名称<span class="symbol required"></span>
										</label>
										<input type="text" <%-- <c:if test="${labelForm.engineId !=null}">readonly="readonly;"</c:if> --%> name="labelName" id="labelName" datatype="*1-20" data-placeholder="请填写标签名称..." nullmsg="未填写标签名称" class="form-control input-sm" value="<c:out value="${labelForm.labelName }"></c:out>" maxlength="20"  />
										<hyjf:validmessage key="labelName" label="标签名称"></hyjf:validmessage>
									</div>
									<div class="row">
										<div class="col-sm-4">
											<div class="form-group">
												<label class="control-label" for="labelTermStart">
													标的期限
												</label>
												<input type="text" placeholder="最小期限" name="labelTermStart" datatype="n0-11" id="labelTermStart" class="form-control input-sm"  value="<c:out value="${labelForm.labelTermStart }"></c:out>" maxlength="50" />
											<hyjf:validmessage key="labelTermStart" label="标的期限"></hyjf:validmessage>
											</div>
											
										</div>
										<div class="col-sm-4">
											<div class="form-group">
												<label class="control-label" >
												&nbsp
												</label>
												<input type="text" placeholder="最大期限" name="labelTermEnd" datatype="n0-11" id="labelTermEnd" class="form-control input-sm" value="<c:out value="${labelForm.labelTermEnd }"></c:out>" maxlength="50" />
												
											</div>
										</div>
										<div class="col-sm-4">
											<div class="form-group">
												<label class="control-label" >
												&nbsp
												</label>
												<div class="clip-radio radio-primary">
													<input type="radio" value="日" name="labelTermType" id="labelTermTypeDay" <c:if test="${ ( labelForm.labelTermType eq '日' ) or ( empty labelForm.labelTermType)}">checked</c:if>>
													<label for="labelTermTypeDay">日</label>
													<input type="radio" value="月" name="labelTermType" id="labelTermTypeMon" <c:if test="${ ( labelForm.labelTermType eq '月' )  }">checked</c:if>>
													<label for="labelTermTypeMon">月</label>
												</div>
											</div>
										</div>
										
									</div>
									<div class="row">
										<div class="col-sm-6">
											<div class="form-group">
												<label class="control-label">
													标的实际利率
												</label>
												<input type="text" placeholder="标的实际利率最小范围" name="labelAprStart" datatype="/^\d{0,10}(\.\d{0,2})?$/"  errormsg="必须为数字，可以有小数" id="labelAprStart" class="form-control input-sm"  value="<c:out value="${labelForm.labelAprStart }"></c:out>" maxlength="50" />
											<hyjf:validmessage key="labelAprStart" label="标的实际利率"></hyjf:validmessage>
											</div>
										</div>
										<div class="col-sm-6">
											<label class="control-label">
												&nbsp
											</label>
											<div class="input-group">
												<input type="text" placeholder="标的实际利率最大范围" name="labelAprEnd" id="labelAprEnd" datatype="/^\d{0,10}(\.\d{0,2})?$/"  errormsg="必须为数字，可以有小数" class="form-control input-sm" value="<c:out value="${labelForm.labelAprEnd }"></c:out>" maxlength="50" />
												<span class="input-group-addon">%</span>
											</div>
										</div>
										
									</div>
								<div class="form-group">
										<label class="control-label">
											还款方式 <span class="symbol required"></span>
										</label>
										<div>
											<select id="borrowStyle" name="borrowStyle" class="form-select2" style="width:100%" id="sel-hkfs"
													data-placeholder="请选还款方式..." data-allow-clear="true" datatype="*"  nullmsg="未指定还款方式">
												<option value=""></option>
												<c:forEach items="${borrowStyleList }" var="borrowStyle" begin="0" step="1" varStatus="status">
													<option value="${borrowStyle.nid }" data-cd="${borrowStyle.name }"
														<c:if test="${borrowStyle.nid eq labelForm.borrowStyle}">selected="selected"</c:if>>
														<c:out value="${borrowStyle.name}"></c:out>
													</option>
												</c:forEach>
											</select>
											<input type="hidden" id="borrowStyleName" name="borrowStyleName" value="${labelForm.borrowStyleName}" />
										</div>
									</div>
									<div class="row">
										<div class="col-sm-6">
											<div class="form-group">
												<label class="control-label">
													标的实际支付金额
												</label>
												<input type="text" placeholder="标的实际最小金额" name="labelPaymentAccountStart" datatype="/^\d{0,8}(\.\d{0,2})?$/"  errormsg="长度不能超过8位，且必须为数字" id="labelPaymentAccountStart" class="form-control input-sm"  value="<c:out value="${labelForm.labelPaymentAccountStart }"></c:out>" maxlength="50" />
											<hyjf:validmessage key="labelPaymentAccountEnd" label="标的实际支付金额"></hyjf:validmessage>
											</div>
										</div>
										<div class="col-sm-6">
											<label class="control-label">
												&nbsp
											</label>
											<div class="input-group">
												<input type="text" placeholder="标的实际最大金额" name="labelPaymentAccountEnd" datatype="/^\d{0,8}(\.\d{0,2})?$/"  errormsg="长度不能超过8位，且必须为数字" id="labelPaymentAccountEnd" class="form-control input-sm" value="<c:out value="${labelForm.labelPaymentAccountEnd }"></c:out>" maxlength="50" />
												<span class="input-group-addon">元</span>
											</div>
										</div>										
									</div>
								<div class="form-group">
									<label class="control-label">
										资产来源
									</label>
										<div>
											<select id="instCode" name="instCode" class="form-select2"  nullmsg="未指定资产来源！"  style="width:100%" data-placeholder="请选择资产来源...">
		                						<option value=""></option>
		                						<c:forEach items="${hjhInstConfigList }" var="inst" begin="0" step="1">
		                							<option value="${inst.instCode }" data-cd="${inst.instName }"
		                								<c:if test="${inst.instCode eq labelForm.instCode}">selected="selected"</c:if>>
		                								<c:out value="${inst.instName }"></c:out>
		                							</option>
		                						</c:forEach>
											</select>
											<input type="hidden" id="instName" name="instName" value="${labelForm.instName}" />
										</div>
									<hyjf:validmessage key="instCode" label="资产来源"></hyjf:validmessage>
								</div>
						
								<div class="form-group">
										<label class="control-label">
											产品类型
										</label>
									<div>
										<select id="assetType" name="assetType" class="form-select2"  nullmsg="未指定产品类型！"  style="width:100%" data-placeholder="请选择产品类型...">
	                						<option value=""></option>
	                						<c:forEach items="${assetTypeList }" var="type" begin="0" step="1">
	                							<option value="${type.assetType }" data-cd="${type.assetTypeName }"
	                								<c:if test="${type.assetType eq labelForm.assetType}">selected="selected"</c:if>>
	                								<c:out value="${type.assetTypeName }"></c:out>
	                							</option>
	                						</c:forEach>
										</select>
										<input type="hidden" id="assetTypeName" name="assetTypeName" value="${labelForm.assetTypeName}" />
									</div>
									<hyjf:validmessage key="assetType" label="产品类型"></hyjf:validmessage>
								</div>		
								<div class="form-group">
									<label class="control-label">
										项目类型
									</label>
										<div>
											<select id="projectType" name="projectType" class="form-select2"  nullmsg="未指定项目类型！"  style="width:100%" data-placeholder="请选择项目类型...">
												<option value=""></option>
												<c:forEach items="${borrowProjectTypeList }" var="borrowProjectType" begin="0" step="1" varStatus="status">
													<option value="${borrowProjectType.borrowCd }" data-cn="${borrowProjectType.borrowCd }" data-cd="${borrowProjectType.borrowName }"
														<c:if test="${borrowProjectType.borrowCd eq labelForm.projectType}">selected="selected"</c:if>>
														<c:out value="${borrowProjectType.borrowName }"></c:out></option>
												</c:forEach>
											</select>
											<input type="hidden" id="projectTypeName" name="projectTypeName" value="${labelForm.projectTypeName}" />
										</div>
									<hyjf:validmessage key="projectType" label="项目类型"></hyjf:validmessage>
								</div>
								<div class="form-group">
										<label class="control-label">
											标的是否发生债转 <span class="symbol required"></span>
										</label>
										<div class="clip-radio radio-primary">
											<input type="radio" datatype="*" value="0" name="isCredit" id="isCreditNo" <c:if test="${ (( labelForm.isCredit eq '0' ) or ( empty labelForm.isCredit)) and (!empty labelForm.id)   }">checked</c:if>>
											<label for="isCreditNo">否</label>
											<input type="radio" datatype="*" value="1" name="isCredit" id="isCreditYes" <c:if test="${ ( labelForm.isCredit eq '1' )  }">checked</c:if>>
											<label for="isCreditYes">是</label>
										</div>
									</div>
									<div class="row" id="creditSumMaxDiv" style="display:<c:if test="${ ( labelForm.isCredit eq '0' ) or ( empty labelForm.isCredit) }">none;</c:if>">
										<div class="col-sm-6">
											<div class="form-group">
												<label class="control-label">
													债转次数不超过
												</label>
												<input type="text" name="creditSumMax" datatype="n0-11" id="creditSumMax"  class="form-control input-sm"  value="<c:out value="${labelForm.creditSumMax }"></c:out>" maxlength="50" />
											</div>
										</div>
									</div>	
									<div class="form-group">
										<label class="control-label">
											标的是否逾期 <span class="symbol required"></span>
										</label>
										<div class="clip-radio radio-primary">
											<input type="radio" datatype="*" value="0" name="isLate" id="isLateNo" <c:if test="${ (( labelForm.isLate eq '0' ) or ( empty labelForm.isLate)) and (!empty labelForm.id)  }">checked</c:if>>
											<label for="isLateNo">否</label>
											<input type="radio" datatype="*" value="1" name="isLate" id="isLateYes" <c:if test="${ ( labelForm.isLate eq '1' )  }">checked</c:if>>
											<label for="isLateYes">是</label>
										</div>
									</div>	
									<div class="row">
										<div class="col-sm-6">
											<div class="form-group">
												<label class="control-label">
													推送时间节点(格式：00:00:00)
												</label>
												<input type="text"  placeholder="开始时间" id="pushTimeStartString" name="pushTimeStartString"
												class="form-control input-sm" value="<c:out value="${labelForm.pushTimeStartString }"></c:out>">																																																										
											<hyjf:validmessage key="pushTimeStart" label="推送时间节点"></hyjf:validmessage>
											</div>
										</div>
										<div class="col-sm-6">
											<label class="control-label">
												&nbsp
											</label>
											<input type="text"  placeholder="结束时间" id="pushTimeEndString" name="pushTimeEndString"
												class="form-control input-sm" value="<c:out value="${labelForm.pushTimeEndString }"></c:out>">
										</div>
									</div>	
									
									<div class="row">
										<div class="col-sm-6">
											<div class="form-group">
												<label class="control-label">
													剩余天数
												</label>
												<input type="text" placeholder="剩余最小天数" name="remainingDaysStart" datatype="n0-11" id="remainingDaysStart"  class="form-control input-sm"  value="<c:out value="${labelForm.remainingDaysStart }"></c:out>" maxlength="50" />
												<hyjf:validmessage key="remainingDaysStart" label="剩余天数"></hyjf:validmessage>
											</div>
										</div>
										<div class="col-sm-6">
											<label class="control-label">
												&nbsp
											</label>
											<div class="input-group">
												<input type="text" placeholder="剩余最大天数" name="remainingDaysEnd" datatype="n0-11" id="remainingDaysEnd" class="form-control input-sm" value="<c:out value="${labelForm.remainingDaysEnd }"></c:out>" maxlength="50" />
												<span class="input-group-addon">日</span>
											</div>
										</div>
									</div>	
									<div class="form-group">
										<label class="control-label">
											 状态 <span class="symbol required"></span>
										</label>
										<div class="clip-radio radio-primary">
											<input type="radio" datatype="*"value="0" name="labelState" id="labelStateNo" <c:if test="${ (( labelForm.labelState eq '0' ) or ( empty labelForm.labelState))  and (!empty labelForm.id) }">checked</c:if>>
											<label for="labelStateNo">停用</label>
											<input type="radio" datatype="*" value="1" name="labelState" id="labelStateYes" <c:if test="${ ( labelForm.labelState eq '1' )  }">checked</c:if>>
											<label for="labelStateYes">启用</label>
										</div>
									</div>						
									
								</div>
							</div>
						</div>
					</div>
							
						<div class="row">
							<div class="col-md-12">
								<div>
									<span class="symbol required"></span>必须填写的项目
									<hr>
								</div>
							</div>
						</div>
						
						<div class="row">
							<div class="col-md-8">
								<p>
									点击【提交保存】按钮，保存当前的填写的资料。
								</p>
							</div>
							<div class="col-md-4">
								<a class="btn btn-primary fn-Confirm  pull-right"><i
									class="fa fa-check"></i> 提交保存</a>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
		<!-- end -->
			
	</tiles:putAttribute>

	<%-- 画面的CSS (ignore) --%>
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
		<link href="${themeRoot}/vendor/plug-in/bootstrap-ladda/ladda-themeless.min.css" rel="stylesheet" media="screen">
	<style>
	.table-heads .table-striped {
		margin-bottom: 0;
	}
	.table-body {
		position: relative;
	}
	.table-body .vertical-align-top {
		vertical-align: top!important;
	}
	.table-body .table-striped {
		border-top: none;
		margin-bottom: 0;
	}
	.table-body .table-striped tr:first-child td {
		border-top: 0;
	}
	.table-body .form-group {
		margin-bottom: 0;
	}
	.thumbnails-wrap {
		background: #f5f5f5;
		border: 1px solid #ccc;
		padding: 3px;
		display: inline-block;
	}
	.thumbnails-wrap img {
		min-width: 35px;
		max-width: 70px;
		height: 22px;
	}
	.popover {
		max-width: 500px;
	}
	.popover img {
		max-width: 460px;
	}
	.clip-radio{
		margin-top:4px
	}
	</style>
	</tiles:putAttribute>

	<%-- JS全局变量定义、插件 (ignore) --%>
	<tiles:putAttribute name="pageGlobalImport" type="string">
		<!-- Form表单插件 -->
		<%@include file="/jsp/common/pluginBaseForm.jsp"%>
		<script src="${themeRoot}/vendor/plug-in/bootstrap-fileinput/jasny-bootstrap.js"></script>
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
			<!-- The File Upload validation plugin -->
	</tiles:putAttribute>
	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
		<script type='text/javascript'
			src="${webRoot}/jsp/manager/label/hjhlabelInfo.js"></script>
			<script type="text/javascript">
		//日历选择器																						
		$('.datepicker').datepicker({																				
			autoclose: true,																			
			todayHighlight: true																			
		});																		
		// 日历范围限制																				
		/* $('#start-date').on("change", function(evnet, d) {																				
			d = $('#end-date').datepicker("getDate"),																			
			d && $('#start-date').datepicker("setEndDate", d)																			
		}),																				
		$('#end-date').on("change", function(evnet, d) {																				
			d = $('#start-date').datepicker("getDate"),																			
			d && $('#end-date').datepicker("setStartDate", d)																			
		});	 */																			

</script>
	</tiles:putAttribute>
</tiles:insertTemplate>
