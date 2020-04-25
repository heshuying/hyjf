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
	<tiles:putAttribute name="pageTitle" value="银行管理" />
	
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType"
			value="${empty packageconfigForm.id ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin: 0">
			<div class="panel-body" style="margin: 0 auto">
				<div class="panel-scroll height-430">
					<form id="mainForm"
						action="${empty packageconfigForm.id ? 'insertAction' : 'updateAction'}"
						method="post" role="form" class="form-horizontal">
						<%-- 银行列表一览 --%>
						<input type="hidden" name="id" id="id" value="${packageconfigForm.id }" /> 
						<input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" /> 
						<input type="hidden" id="success" value="${success}" />
						<div class="panel-scroll height-340 margin-bottom-15">	
						
							<div class="form-group">
								<label class="col-xs-3 control-label text-right padding-top-5 padding-right-0" for="userRole"> 
									<span class="symbol required"></span>VIP 等级
								</label>
								<div class="col-xs-9 inline-block admin-select">
									<c:if test="${packageconfigForm.id>0}">
										<label class="col-xs-3 control-label padding-top-5 padding-right-0" for="userRole"> 
											${packageconfigForm.vipName}
										</label>
										<input type="hidden" name="vipId" id="vipId" value="${packageconfigForm.vipId }" /> 
									</c:if>
									<c:if test="${packageconfigForm.id==null}">
										<select id="vipId" name="vipId" class="form-control input-sm" 
										datatype="*" errormsg="请选择VIP 等级！" style="width:100%">
											<option value="">选择VIP等级</option>
											<c:if test="${!empty vipInfos}">
												<c:forEach items="${vipInfos }" var="vipInfo" begin="0" step="1" varStatus="status">
												  	<option value="${vipInfo.id }"
														<c:if test="${vipInfo.id eq packageconfigForm.vipId}">selected="selected"</c:if>>
														<c:out value="${vipInfo.vipName }"></c:out>
													</option>
												</c:forEach>
											</c:if>
										</select>
									</c:if>
								</div>
							</div>
							<div class="form-group">
								<label class="col-xs-3 control-label text-right padding-top-5 padding-right-0" for="userRole"> 
									<span class="symbol required"></span>优惠券类型
								</label>
								<div class="col-xs-9 inline-block admin-select">
									<select id="couponType" name="couponType" class="form-control input-sm" 
									datatype="*" errormsg="请选择优惠券！" style="width:100%" onchange="doAction(this.value);">
										<option value="">选择优惠券类型</option>
										<c:if test="${!empty couponType}">
											<c:forEach items="${couponType }" var="type" begin="0" step="1" varStatus="status">
											  	<option value="${type.nameCd }"
													<c:if test="${type.nameCd eq packageconfigForm.couponType}">selected="selected"</c:if>>
													<c:out value="${type.name }"></c:out>
												</option>
											</c:forEach>
										</c:if>
									</select>
								</div>
							</div>
							<div class="form-group">
								<label class="col-xs-3 control-label text-right padding-top-5 padding-right-0" for="userRole"> 
									<span class="symbol required"></span>优惠券名称
								</label>
								<div class="col-xs-9 inline-block admin-select">
									<c:if test="${packageconfigForm.id>0}">
										<select id="couponCode" name="couponCode" class="form-control input-sm" datatype="*" 
										errormsg="请选择优惠券！" style="width:100%">
											<option value="">选择优惠券</option>
											<c:if test="${!empty configList}">
											<c:forEach items="${configList }" var="config" begin="0" step="1" varStatus="status">
											  	<option value="${config.couponCode }"
													<c:if test="${config.couponCode eq packageconfigForm.couponCode}">selected="selected"</c:if>>
													<c:out value="${config.couponName }"></c:out>
												</option>
											</c:forEach>
										</c:if>
										</select>
									</c:if>
									<c:if test="${packageconfigForm.id==null}">
										<select id="couponCode" name="couponCode" class="form-control input-sm" datatype="*" errormsg="请选择优惠券！" style="width:100%">
											<option value="">选择优惠券</option>
										</select>
									</c:if>
									
								</div>
							</div>
							<div class="form-group">
								<label class="col-xs-3 control-label text-right padding-top-5 padding-right-0" for="name"> 
									优惠券面值
								</label>
								<div class="col-xs-7">
									<input type="text" id="couponQuota" value="${packageconfigForm.couponQuota}" readonly="readonly">
								</div>
							</div>
							<div class="form-group">
								<label class="col-xs-3 control-label text-right padding-top-5 padding-right-0" for="auditContent">
									<span class="symbol required"></span>发放数量
								</label>
								<div class="col-xs-7">
									<input type="text" placeholder="发放数量" id="couponQuantity" name="couponQuantity"
										value="${packageconfigForm.couponQuantity}" class="form-control" 
										datatype="/^([1-9][\d]{0,9})$/" errormsg="发放数量请输入正整数！" maxlength="10">
									<hyjf:validmessage key="type" label="发放数量"></hyjf:validmessage>
								</div>
							</div> 
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label text-right padding-top-5 padding-right-0" for="auditContent">
								备注
							</label>
							<div class="col-xs-7">
								<textarea id="auditContent" name="remark" placeholder="备注" 
									class="form-control" maxlength="300" style="resize: none;">${gradeconfigForm.remark}</textarea>
							</div>
						</div>
						<br/>
						<div class="form-group margin-bottom-0">
							<div class="col-xs-offset-2 col-xs-10">
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
	</tiles:putAttribute>

	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
		<link href="${themeRoot}/vendor/plug-in/bootstrap-fileinput/jasny-bootstrap.min.css" rel="stylesheet" media="screen">
		<link href="${themeRoot}/vendor/plug-in/bootstrap-datepicker/bootstrap-datepicker3.standalone.min.css" rel="stylesheet" media="screen">																																										
		
		
		<style>
			.purMargin{
				margin:8px 0;
			}
			.purMargin input{
				width:200px;
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
		<script type="text/javascript" 
			src="${themeRoot}/vendor/plug-in/bootstrap-datepicker/bootstrap-datepicker.min.js"></script>	
	</tiles:putAttribute>
	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
		<script type='text/javascript'
			src="${webRoot}/jsp/manager/vip/packageconfig/packageconfigInfo.js"></script>
		<script type="text/javascript">
		//日历选择器																						
		$('.datepicker').datepicker({																				
			autoclose: true,																			
			todayHighlight: true																			
		}),																				
		// 日历范围限制																				
		$('#start-date').on("change", function(evnet, d) {																				
			d = $('#end-date').datepicker("getDate"),																			
			d && $('#start-date').datepicker("setEndDate", d)																			
		}),																				
		$('#end-date').on("change", function(evnet, d) {																				
			d = $('#start-date').datepicker("getDate"),																			
			d && $('#end-date').datepicker("setStartDate", d)																			
		});	
		
		//选择CI
		function doAction(couponType) {
			jQuery('#couponType').val(couponType); //让第一个下拉框保持显示选中的值
			//alert(v);
			jQuery('#couponCode').html(""); //把ci内容设为空
			var couponCodeSelect = jQuery('#couponCode');
			couponCodeSelect.append('<option value="">选择优惠券</option>');
			
			
			
			//异步请求查询ci列表的方法并返回json数组
			jQuery.ajax({
				url : 'selectCouponListAction',
				type : 'post',
				data : { couponType : couponType },
				dataType : 'json',
				success : function (data) {
					// 单选CI
					 if (data.list && data.list.length > 0) {
							var html = [];
							for (var i = 0; i < data.list.length; i++) {
								html.push('<option value="'+data.list[i].couponCode+'">'+data.list[i].couponName+'</option>');
							}
							couponCodeSelect.append(html.join(''));
						}
					} 
				});
		};

</script>
	</tiles:putAttribute>
</tiles:insertTemplate>
