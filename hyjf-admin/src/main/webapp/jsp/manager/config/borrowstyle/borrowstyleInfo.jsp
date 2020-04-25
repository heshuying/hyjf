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
	<tiles:putAttribute name="pageTitle" value="还款方式管理" />
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
		<style>
.panel-title {
	font-family: "微软雅黑"
}
</style>
	</tiles:putAttribute>
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType"
			value="${empty borrowstyleForm.id ? '添加' : '修改'}"></c:set>


		<div class="panel panel-white" style="margin: 0">
			<div class="panel-body" style="margin: 0 auto">
				<div class="panel-scroll height-430">
					<form id="mainForm"
						action="${empty borrowstyleForm.id ? 'insertAction' : 'updateAction'}"
						method="post" role="form" class="form-horizontal">

						<input type="hidden" name="id" id="id"
							value="${borrowstyleForm.id }" /> <input type="hidden"
							name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" /> <input
							type="hidden" id="success" value="${success}" />
						<div class="widget">
							<div class="widgetcontent nopadding">
								<%-- 还款方式管理 --%>
								<div class="form-group">
									<label class="col-sm-2 control-label" for="nid"> <span
										class="symbol required"></span>nid
									</label>
									<div class="col-sm-10">
										<input type="text" id="nid" name="nid"
											value="${borrowstyleForm.nid}" class="form-control"
											datatype="s1-50" errormsg="NID长度1-50位！">
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-2 control-label" for="name"> <span
										class="symbol required"></span>名称
									</label>
									<div class="col-sm-10">
										<input type="text" placeholder="名称" id="name" name="name"
											value="${borrowstyleForm.name}" class="form-control"
											datatype="*1-50" errormsg="名称长度1-50位！">
										<hyjf:validmessage key="name" label="名称"></hyjf:validmessage>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-2 control-label" for="title"> <span
										class="symbol required"></span>标题
									</label>
									<div class="col-sm-10">
										<input type="text" placeholder="标题" id="title" name="title"
											value="${borrowstyleForm.title}" class="form-control"
											datatype="*1-50" errormsg="标题长度1-50位！">
										<hyjf:validmessage key="title" label="标题"></hyjf:validmessage>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-2 control-label" for="contents"> <span
										class="symbol required"></span>详解
									</label>
									<div class="col-sm-10">
										<input type="text" placeholder="详解" id="contents"
											name="contents" value="${borrowstyleForm.contents}"
											class="form-control">
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-2 control-label" for="remark"> <span
										class="symbol required"></span>备注
									</label>
									<div class="col-sm-10">
										<input type="text" placeholder="备注" id="remark" name="remark"
											value="${borrowstyleForm.remark}" class="form-control">
									</div>
								</div>

								<div class="form-group">
									<label class="col-sm-2 control-label"> <span
										class="symbol required"></span>状态
									</label>
									<div class="col-sm-10">
										<div class="radio clip-radio radio-primary ">
											<input type="radio" id="statusOn" name="status" datatype="*"
												value="0" class="event-categories"
												${borrowstyleForm.status == '0' ? 'checked' : ''}> <label
												for="statusOn"> 启用 </label> <input type="radio"
												id="statusOff" name="status" datatype="*" value="1"
												class="event-categories"
												${borrowstyleForm.status == '1' ? 'checked' : ''}> <label
												for="statusOff"> 关闭 </label>
										</div>
									</div>
									<hyjf:validmessage key="status" label="状态"></hyjf:validmessage>
								</div>
								<div class="form-group margin-bottom-0">
									<div class="col-sm-offset-2 col-sm-10">
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
	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
		<script type='text/javascript'
			src="${webRoot}/jsp/manager/config/borrowstyle/borrowstyleInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
