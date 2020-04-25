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
	<tiles:putAttribute name="pageTitle" value="团队管理" />
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
			value="${empty contentjobsForm.id ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin: 0">
			<div class="panel-body" style="margin: 0 auto">
				<div class="panel-scroll height-430">
					<form id="mainForm"
						action="${empty contentjobsForm.id ? 'insertAction' : 'updateAction'}"
						method="post" role="form" class="form-horizontal">
						<%-- 团队列表一览 --%>
						<input type="hidden" name="id" id="id"
							value="${contentjobsForm.id }" /> <input type="hidden"
							name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" /> <input
							type="hidden" id="success" value="${success}" />
						<div class="form-group">
							<label class="col-sm-2 control-label" for="officeName"> <span
								class="symbol required"></span>职位名称
							</label>
							<div class="col-sm-10">
								<input type="text" placeholder="职位名称" id="officeName"
									name="officeName" value="${contentjobsForm.officeName}"
									class="form-control" datatype="s1-20"
									errormsg="职位名称 只能是字符汉字，长度1~20个字符！">
								<hyjf:validmessage key="type" label="名称"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="place"> <span
								class="symbol required"></span>工作城市
							</label>
							<div class="col-sm-10">
								<input type="text" placeholder="工作城市" id="place" name="place"
									value="${contentjobsForm.place}" class="form-control"
									datatype="s1-30" errormsg="工作城市 只能是字符汉字，长度1~30个字符！">
								<hyjf:validmessage key="type" label="工作地点"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="email"> <span
								class="symbol required"></span>简历投递邮箱
							</label>
							<div class="col-sm-10">
								<input type="text" placeholder="简历投递邮箱" id="email" name="email"
									value="${contentjobsForm.email}" class="form-control"
									datatype="*2-20" errormsg="简历投递邮箱 长度2~20个字符！">
								<hyjf:validmessage key="type" label="简历投递邮箱"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="content"> <span
								class="symbol required"></span>职位描述
							</label>
							<div class="col-sm-10">
								<textarea placeholder="职位描述" id="content" name="content"
									class="form-control limited" datatype="*" errormsg="职位描述必填！"
									maxlength="3000">${contentjobsForm.content}</textarea>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="order"> <span
								class="symbol required"></span>招聘人数
							</label>
							<div class="col-sm-10">
								<input type="text" placeholder="招聘人数" id="persons" name="persons"
									value="${contentjobsForm.persons}" class="form-control"
									datatype="n0-3" errormsg="招聘人数只能是数字，长度1-3位数字！" maxlength="3" >
								<hyjf:validmessage key="persons" label="招聘人数"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="order"> <span
								class="symbol required"></span>排序
							</label>
							<div class="col-sm-10">
								<input type="text" placeholder="排序" id="order" name="order"
									value="${contentjobsForm.order}" class="form-control"
									datatype="n1-3" errormsg="排序 只能是数字，长度1-3位数字！" maxlength="3" >
								<hyjf:validmessage key="order" label="排序"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label"> <span
								class="symbol required"></span>状态
							</label>
							<div class="col-sm-10">
								<div class="radio clip-radio radio-primary ">
									<input type="radio" id="statusOn" name="status" datatype="*"
										value="1" class="event-categories"
										${contentjobsForm.status == '1' ? 'checked' : ''}> <label
										for="statusOn"> 启用 </label> <input type="radio" id="statusOff"
										name="status" datatype="*" value="0" class="event-categories"
										${contentjobsForm.status == '0' ? 'checked' : ''}> <label
										for="statusOff"> 关闭 </label>
								</div>
							</div>
							<hyjf:validmessage key="status" label="团队状态"></hyjf:validmessage>
						</div>
						<div class="form-group margin-bottom-0">
							<div class="col-sm-offset-2 col-sm-10">
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

	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
		<script type='text/javascript'
			src="${webRoot}/jsp/manager/content/contentjobs/contentjobsInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
