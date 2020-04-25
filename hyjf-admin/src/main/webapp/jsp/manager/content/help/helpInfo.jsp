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
	<tiles:putAttribute name="pageTitle" value="问题" />
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
			value="${empty helpInfoForm.id ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin: 0">
			<div class="panel-body" style="margin: 0 auto">
				<div class="panel-scroll height-430">
					<form id="mainForm"
						action="${empty helpInfoForm.id ? 'helpAddAction' : 'helpUpdateAction'}"
						method="post" role="form" class="form-horizontal">
						<%-- 文章列表一览 --%>
						<input type="hidden" name="id" id="id" value="${helpInfoForm.id }" />
						<input type="hidden" name="pageToken"
							value="${sessionScope.RESUBMIT_TOKEN}" /> <input type="hidden"
							id="success" value="${success}" />
						<div class="form-group">
							<label class="col-sm-2 control-label" for="pcateId">问题分类</label>
							<div class="col-sm-10">
								<select name="pcateId" id="pcateId"
									class="form-control underline form-select2" datatype="*"
									errormsg="必须选择一级菜单!">
									<option value="">--全部--</option>
									<c:forEach items="${parentCategorys }" var="record" begin="0"
										step="1" varStatus="status">
										<option value="${record.id }"
											<c:if test="${record.id==helpInfoForm.pcateId}">selected="selected"</c:if>>${record.title }</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="cateId"> <span
								class="symbol required"></span>问题子分类
							</label>
							<div class="col-sm-10">
								<select name="cateId" id="cateId"
									class="form-control underline form-select2"
									errormsg="必须选择一级菜单!">
									<option value="">--全部--</option>
									<c:forEach items="${childCategorys }" var="record" begin="0"
										step="1" varStatus="status">
										<option value="${record.id }"
											<c:if test="${record.id==helpInfoForm.cateId}">selected="selected"</c:if>>${record.title }</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="title"> <span
								class="symbol required"></span>问题名称
							</label>
							<div class="col-sm-10">
								<c:choose>
									<c:when test="${empty helpInfoForm.id}">
										<input type="text" placeholder="问题名称" id="title" name="title"
											class="form-control" datatype="*1-100"
											errormsg="问题名称的长度不能大于100个字符！">
										<hyjf:validmessage key="title" label="分类名称 "></hyjf:validmessage>
									</c:when>
									<c:otherwise>
										<input type="text" placeholder="问题名称" id="title" name="title"
											class="form-control" value="${helpInfoForm.title}"
											datatype="*1-100" errormsg="问题名称的长度不能大于100个字符！">
									</c:otherwise>
								</c:choose>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-2 control-label" for="content"> 问题内容
							</label>
							<div class="col-sm-10">
								<c:choose>
									<c:when test="${empty helpInfoForm.id}">
										<textarea id="content" name="content" placeholder="问题内容"
											class="ckeditor form-control" cols="10" rows="12"
											maxlength="50000" datatype="*1-50000"
											errormsg="问题内容的长度不能大于500个字符！"></textarea>
										<hyjf:validmessage key="content" label="问题内容"></hyjf:validmessage>
									</c:when>
									<c:otherwise>
										<textarea id="content" name="content" placeholder="问题内容"
											class="form-control" cols="10" rows="12"
											maxlength="50000" datatype="*1-50000"
											errormsg="问题内容的长度不能大于50000个字符！">${helpInfoForm.content}</textarea>
									</c:otherwise>
								</c:choose>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-2 control-label" for="order"> <span
								class="symbol required"></span>排序
							</label>
							<div class="col-sm-10">
								<c:choose>
									<c:when test="${empty helpInfoForm.id}">
										<input type="text" placeholder="排序" id="order" name="order"
											class="form-control" datatype="n1-3"
											errormsg="排序 只能是数字，长度1-3位数字！" maxlength="3">
										<hyjf:validmessage key="helpOrder" label="排序"></hyjf:validmessage>
									</c:when>
									<c:otherwise>
										<input type="text" placeholder="排序" id="order" name="order"
											class="form-control" value="${helpInfoForm.order}"
											datatype="n1-3" errormsg="排序 只能是数字，长度1-3位数字！" maxlength="3">
									</c:otherwise>
								</c:choose>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="status"> <span
								class="symbol required"></span>状态
							</label>
							<div class="col-sm-10">
								<c:choose>
									<c:when test="${empty helpInfoForm.id}">
										<input type="radio" name="status" value="0" checked="true" />关闭
											<input type="radio" name="status" value="1" />启用
									</c:when>
									<c:otherwise>
										<input type="radio" name="status" value="0"
											<c:if test="${helpInfoForm.status==0}">checked="true"</c:if> />关闭
											<input type="radio" name="status" value="1"
											<c:if test="${helpInfoForm.status ==1}">checked="true"</c:if> />启用
									</c:otherwise>
								</c:choose>
							</div>
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
			src="${webRoot}/jsp/manager/content/help/helpInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
