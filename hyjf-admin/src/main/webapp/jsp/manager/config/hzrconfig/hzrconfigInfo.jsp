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
	<tiles:putAttribute name="pageTitle" value="汇转让设置" />
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
	<style>
	.panel-title { font-family: "微软雅黑" }
	</style>
	</tiles:putAttribute>
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType" value="${empty hzrconfigForm.id ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin:0">
			<div class="panel-body" style="margin:0 auto">
				<div class="panel-scroll height-430">
				<form id="mainForm" action="${empty hzrconfigForm.id ? 'insertAction' : 'updateAction'}"
						method="post"  role="form" class="form-horizontal">
					<%-- 汇转让列表一览 --%>
					<input type="hidden" name="id" id="id" value="${hzrconfigForm.id }" />
					<input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
					<input type="hidden" id="success" value="${success}" />
					<div class="form-group">
						<label class="col-sm-2 control-label" for="code"> <span class="symbol required"></span>编号 </label>
						<div class="col-sm-10">
							<input type="text" placeholder="编号" id="code" name="code" value="<c:out value="${hzrconfigForm.code}" />" class="form-control" <c:if test="${!empty hzrconfigForm.id }" >readonly="readonly"</c:if>
								ajaxurl="checkAction?id=${hzrconfigForm.id }"
								maxlength="20" datatype="/^\w+$/" errormsg="编号 只能由数字、26个英文字母或者下划线组成，长度1~20个字符！" >
							<hyjf:validmessage key="code" label="编号"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="name"> <span class="symbol required"></span>名称 </label>
						<div class="col-sm-10">
							<input type="text" placeholder="名称" id="name" name="name" value="<c:out value="${hzrconfigForm.name}" />"  class="form-control"
								datatype="s1-20" errormsg="汇转让名称 只能是字符汉字，长度1~20个字符！" >
							<hyjf:validmessage key="name" label="名称"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="value"> <span class="symbol required"></span>数值</label>
						<div class="col-sm-10">
							<input type="text" placeholder="数值" id="value" name="value" value="<c:out value="${hzrconfigForm.value}" />"  class="form-control"
								maxlength="20" datatype="/^[1-9]+(\.\d+)?(%)?$|^-?0(\.\d+)?$|^[1-9]+[0-9]*(\.\d+)?(%)?$/" errormsg="数值 只能是数字，长度1~20个数字！" >
							<hyjf:validmessage key="type" label="数值"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="unit"> <span class="symbol required"></span>单位</label>
						<div class="col-sm-10">
							<input type="text" placeholder="单位" id="unit" name="unit" value="<c:out value="${hzrconfigForm.unit}" />"  class="form-control"
								datatype="s1-20" errormsg="汇转让单位 只能是字符汉字，长度1~20个字符！" >
							<hyjf:validmessage key="unit" label="单位"></hyjf:validmessage>
						</div>
					</div>
						<div class="form-group">
						<label class="col-sm-2 control-label" for="description"> 说明 </label>
						<div class="col-sm-10">
							<textarea placeholder="说明" id="remark" name="remark" class="form-control limited"><c:out value="${hzrconfigForm.unit}" /></textarea>
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
	
	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
		<script type='text/javascript' src="${webRoot}/jsp/manager/config/hzrconfig/hzrconfigInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
