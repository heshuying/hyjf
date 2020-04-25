<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="数据字典" />

	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType" value="${empty formInfo.nameCd ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin: 0">
			<div class="panel-heading">
				<h5 class="panel-title">${jspEditType}数据字典信息</h5>
			</div>
			<div class="panel-body">
				<div class="panel-scroll height-270">
					<form id="mainForm" action="${ modifyFlag eq 'N' ? 'insertAction' : 'updateAction'}"
							method="post" role="form" class="form-horizontal">
						<%-- 角色列表一览 --%>
						<input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
						<input type="hidden" name="modifyFlag" value="${modifyFlag}" />
						
						<input type="hidden" id="success" value="${success}" />
						<div class="form-group">
							<label class="col-sm-2 control-label" for="nameClass">
								<span class="symbol required"></span> 字典区分
							</label>
							<div class="col-sm-10">
								<input type="text" name="nameClass" id="nameClass" class="form-control" maxlength="20" ${ modifyFlag eq 'N' ? '' : 'readonly="readonly"'}
										value="${formInfo.nameClass}" datatype="*,/^[a-zA-Z0-9_]+$/" errormsg="字典区分只能是数字、字母或者下划线，长度1~6位！" />
								<hyjf:validmessage key="nameClass"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="nameCd">
								<span class="symbol required"></span> 字典编号
							</label>
							<div class="col-sm-10">
								<input type="text" name="nameCd" id="nameCd" class="form-control" maxlength="6" ${ modifyFlag eq 'N' ? '' : 'readonly="readonly"'}
									value="${formInfo.nameCd}" datatype="*,/^[a-zA-Z0-9_]+$/" errormsg="字典区分只能是数字、字母或者下划线，长度1~6位！" />
								<hyjf:validmessage key="nameCd"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="name">
								字典名称 </label>
							<div class="col-sm-10">
								<input type="text" name="name" id="name" class="form-control" maxlength="100" 
									value="${formInfo.name}" datatype="*1-100" errormsg="字典名称长度不能超过100个字符！" />
								<hyjf:validmessage key="name"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="other1">
								排序 </label>
							<div class="col-sm-10">
								<input type="text" name="sort" id="sort" class="form-control" maxlength="2" 
									value="${formInfo.sort}" datatype="n1-2" errormsg="排序只能输入数字，长度不能超过2位！" ignore="ignore" />
								<hyjf:validmessage key="sort"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="other1">
								扩展项目1 </label>
							<div class="col-sm-10">
								<input type="text" name="other1" id="other1" class="form-control" maxlength="255" 
									value="${formInfo.other1}" datatype="*1-255" errormsg="扩展项目1长度不能超过255个字符！" ignore="ignore" />
								<hyjf:validmessage key="other1"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="other2">
								扩展项目2 </label>
							<div class="col-sm-10">
								<input type="text" name="other2" id="other2" class="form-control" maxlength="255" 
									value="${formInfo.other2}" datatype="*1-255" errormsg="扩展项目2长度不能超过255个字符！" ignore="ignore" />
								<hyjf:validmessage key="other2"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="other3">
								扩展项目3 </label>
							<div class="col-sm-10">
								<input type="text" name="other3" id="other3" class="form-control" maxlength="255" 
									value="${formInfo.other3}" datatype="*1-255" errormsg="扩展项目3长度不能超过255个字符！" ignore="ignore" />
								<hyjf:validmessage key="other3"></hyjf:validmessage>
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
	<style>
	.panel-title {
		font-family: "微软雅黑"
	}
	</style>
	</tiles:putAttribute>
	
	<%-- JS全局变量定义、插件 (ignore) --%>	
	<tiles:putAttribute name="pageGlobalImport" type="string">	
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
	</tiles:putAttribute>
	
	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type='text/javascript' src="${webRoot}/jsp/maintenance/paramname/paramname_info.js"></script>
	</tiles:putAttribute>
	
</tiles:insertTemplate>
