<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="意见反馈回复" />
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<div class="panel panel-white no-margin">
			<div class="panel-body">
				<form id="mainForm" action="updateSubmissionsAction"
						method="post"  role="form" class="form-horizontal" >
					<%-- 角色列表一览 --%>
					<input type="hidden" name="submissionsId" id="submissionsId" value="${submissionsAuto.id }" />
					<input type="hidden" id="success" value="${success}" />
					<input type="hidden" id="hasError" value="${hasError}" />
					<div class="panel-scroll height-340 margin-bottom-15">
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="userName">
								<span class="symbol"></span>反馈内容 
							</label>
							<div class="col-xs-10">
								<textarea id="form-field-22" style="height:200px;" readonly="readonly" class="form-control" >${submissionsAuto.content}</textarea>
							</div>
						</div>
						
						<div class="form-group">
							
							<div class="col-xs-offset-4 col-xs-10">
								<div class="radio clip-radio radio-primary ">
									<input type="radio" id="stateOn" name="status" value="0"  datatype="*" ${submissionsAuto.state == '0' ? 'checked' : ''}> <label for="stateOn">未处理 </label> 
									<input type="radio" id="stateOff" name="status" value="1" datatype="*" ${submissionsAuto.state == '1' ? 'checked' : ''}> <label for="stateOff">已处理 </label>
								</div>
							</div>
						</div>
					</div>
					<div class="form-group margin-bottom-0">
						<div class="col-xs-offset-4 col-xs-10">
							<a class="btn btn-o btn-primary fn-Confirm"><i class="fa fa-check"></i> 确 认</a>
							<a class="btn btn-o btn-primary fn-Cancel"><i class="fa fa-close"></i> 取 消</a>
						</div>
					</div>
				</form>
			</div>
		</div>
	</tiles:putAttribute>
	
	<%-- JS全局变量定义、插件 (ignore) --%>
	<tiles:putAttribute name="pageGlobalImport" type="string">
		<!-- Form表单插件 -->
		<%@include file="/jsp/common/pluginBaseForm.jsp"%>
	</tiles:putAttribute>
	
	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type='text/javascript' src="${webRoot}/jsp/submissions/replySubmissions.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
