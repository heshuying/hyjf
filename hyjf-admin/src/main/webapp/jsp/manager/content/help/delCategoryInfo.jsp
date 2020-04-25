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
	<tiles:putAttribute name="pageTitle" value="删除问题分类" />
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
		<c:set var="jspEditType" value="删除"></c:set>
		<div class="panel panel-white" style="margin: 0">
			<div class="panel-body" style="margin: 0 auto">
				<h4>删除问题分类</h4>
				<hr />
				<div class="panel-scroll height-430">
					<form id="mainForm" action="delAction" method="post" role="form"
						class="form-horizontal">

						<input type="hidden" name="id" id="id" value="${infoForm.id }" />
						<input type="hidden" name="pid" id="pid" value="${infoForm.pid }" /><input
							type="hidden" name="pageToken"
							value="${sessionScope.RESUBMIT_TOKEN}" /> <input type="hidden"
							id="success" value="${success}" />
						<div class="form-group">
							<label for="type" style="margin-left:20px;"> 分类名称：${infoForm.title} </label>
						</div>

						<div class="form-group">
							<label for="code" style="margin-left:20px;"> 分类描述：${infoForm.code} </label>
						</div>

						<div class="form-group">
							<label for="level" style="margin-left:20px;"> 问题数量：${infoForm.tip} </label>
						</div>
						<hr>
						<div class="form-group">
							<label for="level" style="margin-left:20px;"> 删除选项： </label> <input type="radio"
								name="delType" value="0" onclick="del(0)" checked="true" />分类与问题同时删除
							<input type="radio" name="delType" value="1" onclick="del(1)" />分类删除，问题转移
							<div style="display: none;" id="listDiv" class="col-xs-12">
								<div class="col-xs-12">
									问题分类:<select name="newpid" id="newpid">
										<option value="">--全部--</option>
										<c:forEach items="${parentCategorys }" var="record" begin="0"
											step="1" varStatus="status">
											<c:if test="${record.id!=infoForm.id}">
												<option value="${record.id }">${record.title }</option>
											</c:if>
										</c:forEach>
									</select> 问题子分类:<select name="newid" id="newid">
										<option value="">--全部--</option>
									</select>
								</div>
							</div>
						</div>
						<div class="form-group margin-bottom-0">
							<div class="col-sm-offset-2 col-sm-10">
								<a class="btn btn-o btn-primary fn-Confirm"><i
									class="fa fa-check"></i> 确 认删除</a> <a
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
			src="${webRoot}/jsp/manager/content/help/delCategoryInfo.js"></script>
		<script type='text/javascript'>
			function del(type) {
				if (type == 1) {
					$("#listDiv").css("display", "block");
				} else {
					$("#listDiv").css("display", "none");
				}
			}
		</script>
	</tiles:putAttribute>
</tiles:insertTemplate>
