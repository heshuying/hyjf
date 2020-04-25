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
	<tiles:putAttribute name="pageTitle" value="查询出借人债权明细" />
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
	<style>
	.panel-title { font-family: "微软雅黑" }
	</style>
	</tiles:putAttribute>
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<%-- <c:set var="jspEditType" value="${empty roleForm.id ? '添加' : '修改'}"></c:set> --%>
		<hyjf:message key="state_error"></hyjf:message>
		<div class="panel panel-white" style="margin:0">
			<div class="panel-body">
				<p class="text-small margin-bottom-20">
					在这里查询出借人债权明细
				</p>
				<hr/>
				<div class="panel-scroll height-430">
				<form id="mainForm" action="debtCheckInfoAction" method="post"  role="form" class="form-horizontal" target="_parent" >
					<input type="hidden" id="userid" name="userid" value="${debtCheckForm.userid}" />	
<%-- 					<input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
					<input type="hidden" id="success" value="${success}" /> --%>
					<input type="hidden" id="borrownid" name="borrownid" value="${debtCheckForm.borrownid}" />	
					<input type="hidden" id="nid" name="nid" value="${debtCheckForm.nid}" />	
					<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="name"> <span
								class="symbol required"></span>时间
							</label>
							<div class="col-xs-3">	
								<div class="input-group">																														
									<input type="text"  placeholder="开始时间" id="startTime" name="startTime"
									class="form-control input-sm datepicker" value="${debtCheckForm.startTime}"
									maxlength="50">																													
									<span class="input-group-addon"><i class="fa fa-calendar"></i></span>																													
								</div>																														
								<hyjf:validmessage key="type" label="开始时间"></hyjf:validmessage>
							</div>
							<div class="col-xs-1" class="form-control">
								~
							</div>
							<div class="col-xs-3">
								<div class="input-group">
									<input type="text"  placeholder="结束时间" id="endTime" name="endTime"
									class="form-control input-sm datepicker" value="${debtCheckForm.endTime}"
									 maxlength="50">																													
									<span class="input-group-addon"><i class="fa fa-calendar"></i></span>
								</div>
								<hyjf:validmessage key="type" label="结束时间"></hyjf:validmessage>
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
			src="${themeRoot}/vendor/plug-in/bootstrap-datepicker/bootstrap-datepicker.min.js"></script>	
		
	</tiles:putAttribute>
	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
		<script type='text/javascript'
			src="${webRoot}/jsp/manager/borrow/borrowinvest/bankaccountcheck.js"></script>
		<script type="text/javascript">
		//日历选择器																						
		$('.datepicker').datepicker({																				
			autoclose: true,																			
			todayHighlight: true																			
		})
		// 日历范围限制																				
		/* $('#statrTime').on("change", function(evnet, d) {																				
			d = $('#endTime').datepicker("getDate"),																			
			d && $('#statrTime').datepicker("setEndDate", d)																			
		}),																				
		$('#endTime').on("change", function(evnet, d) {																				
			d = $('#statrTime').datepicker("getDate"),																			
			d && $('#endTime').datepicker("setStartDate", d)																			
		});	 */																	
		</script>
	</tiles:putAttribute>
</tiles:insertTemplate>
