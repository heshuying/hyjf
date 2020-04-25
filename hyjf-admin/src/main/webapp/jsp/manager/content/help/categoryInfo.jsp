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
	<tiles:putAttribute name="pageTitle" value="问题分类" />
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
		<c:set var="jspEditType" value="${empty infoForm.id ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin: 0">
			<div class="panel-body" style="margin: 0 auto">
				<div class="panel-scroll height-430">
					<form id="mainForm"
						action="${empty infoForm.id ? 'addAction' : 'updateAction'}"
						method="post" role="form" class="form-horizontal">
						<%-- 文章列表一览 --%>
						<input type="hidden" name="id" id="id" value="${infoForm.id }" />
						<input type="hidden" name="pageToken"
							value="${sessionScope.RESUBMIT_TOKEN}" /> <input type="hidden"
							id="success" value="${success}" />

						<c:if test="${categoryLevel==1 }">
							<!-- categoryLevel==1添加一级菜单 -->
							<input type="hidden" name="pid" id="pid" value="0" />
							<input type="hidden" name="level" id="level" value="0" />
							<div class="form-group">
								<label class="col-sm-2 control-label" for="title"> <span
									class="symbol required"></span>问题分类
								</label>
								<div class="col-sm-10">
									<input type="text" placeholder="分类名称" id="title" name="title"
										class="form-control" value="${infoForm.title}" maxlength="20"
										datatype="*1-20" errormsg="分类名称的长度不能大于20个字符！">
								</div>
							</div>
						</c:if>


						<c:if test="${categoryLevel==2 }">
							<input type="hidden" name="level" id="level" value="1" />
							<!-- categoryLevel==2添加二级菜单 -->
							<div class="form-group">
								<label class="col-sm-2 control-label" for="pid">问题分类</label>
								<div class="col-sm-10">
									<select name="pid" id="pid"
										class="form-control underline form-select2" datatype="*"
										errormsg="必须选择一级菜单!">
										<option value="">--全部--</option>
										<c:forEach items="${parentCategorys }" var="record" begin="0"
											step="1" varStatus="status">
											<option value="${record.id }"
												<c:if test="${record.id==infoForm.pid}">selected="selected"</c:if>>${record.title }</option>
										</c:forEach>
									</select>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label" for="title"> <span
									class="symbol required"></span>问题子分类
								</label>
								<div class="col-sm-10">
									<input type="text" placeholder="分类名称" id="title" name="title"
										class="form-control" value="${infoForm.title}" maxlength="20"
										datatype="*1-20" errormsg="分类名称的长度不能大于20个字符！">
								</div>
							</div>
						</c:if>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="sort"> <span
								class="symbol required"></span>排序
							</label>
							<div class="col-sm-10">
								<c:choose>
									<c:when test="${empty infoForm.id}">
										<input type="text" placeholder="排序" id="sort" name="sort"
											class="form-control" maxlength="20" datatype="n1-3"
											errormsg="排序 只能是数字，长度1-3位数字！" maxlength="3">
										<hyjf:validmessage key="sort" label="排序"></hyjf:validmessage>
									</c:when>
									<c:otherwise>
										<input type="text" placeholder="排序" id="sort" name="sort"
											value="${infoForm.sort}" maxlength="20" datatype="n1-3"
											errormsg="排序 只能是数字，长度1-3位数字！" maxlength="3">
									</c:otherwise>
								</c:choose>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="code"> <span
								class="symbol required"></span>分类描述
							</label>
							<div class="col-sm-10">
								<c:choose>
									<c:when test="${empty infoForm.id}">
										<textarea rows="12" cols="10" placeholder="分类描述" id="code"
											name="code" class="form-control" maxlength="200"
											datatype="*1-200" errormsg="分类描述不能为空！"></textarea>
										<hyjf:validmessage key="code" label="分类描述 "></hyjf:validmessage>
									</c:when>
									<c:otherwise>
										<textarea rows="12" cols="10" placeholder="分类描述" id="code"
											name="code" class="form-control" maxlength="200"
											datatype="*1-200" errormsg="分类描述不能为空！">${infoForm.code}</textarea>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="hide"> <span
								class="symbol required"></span>状态
							</label>
							<div class="col-sm-10">
								<c:choose>
									<c:when test="${empty infoForm.id}">
										<input type="radio" name="hide" value="0" checked="true" />启用
											<input type="radio" name="hide" value="1" />关闭
									</c:when>
									<c:otherwise>
										<input type="radio" name="hide" value="0"
											<c:if test="${infoForm.hide==0}">checked="true"</c:if> />启用
											<input type="radio" name="hide" value="1"
											<c:if test="${infoForm.hide ==1}">checked="true"</c:if> />关闭
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
			src="${webRoot}/jsp/manager/content/help/categoryInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
