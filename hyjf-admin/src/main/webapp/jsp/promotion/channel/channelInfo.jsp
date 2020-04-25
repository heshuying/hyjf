<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="推广管理" />
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
	<style>
	.panel-title { font-family: "微软雅黑" }
	</style>
	<link href="${themeRoot}/vendor/plug-in/select2/select2.min.css" rel="stylesheet" media="screen">
	</tiles:putAttribute>
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType" value="${empty channelForm.utmId ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin:0">
			<div class="panel-body" style="margin:0 auto">
				<div class="panel-scroll height-430">
				<form id="mainForm" action="${empty channelForm.utmId ? 'insertAction' : 'updateAction'}"
						method="post"  role="form" class="form-horizontal">
					<%-- 银行列表一览 --%>
					<input type="hidden" name="utmId" id="utmId" value="<c:out value="${channelForm.utmId }"/>" />
					<input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
					<input type="hidden" id="success" value="${success}" />

					<div id="chargeTimeDiv" class="form-group">
						<label class="col-sm-3 control-label" for="sourceId"> 
							<span class="symbol required"></span>渠道(utm_source):
						</label>
						<div class="col-sm-8">
							<select id="projectType" name="sourceId" class="form-select2" style="width:100%" data-placeholder="请选择项目类型..." datatype="*">
								<c:forEach items="${utmPlatList }" var="record" begin="0" step="1" varStatus="status">
									<option value="${record.sourceId }"
										<c:if test="${record.sourceId eq channelForm.sourceId}">selected="selected"</c:if>>
										<c:out value="${record.sourceName }"></c:out></option>
								</c:forEach>
							</select>
							<hyjf:validmessage key="sourceId" label="渠道(utm_source)"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label" for="utmMedium"> 
							推广方式(utm_medium):
						</label>
						<div class="col-sm-8">
							<input type="text" id="utmMedium" name="utmMedium" value="<c:out value="${channelForm.utmMedium}"></c:out>"
									class="form-control" datatype="*1-50" errormsg="推广方式(utm_medium)应该输入1~50个字符！" maxlength="50" ignore="ignore" />
							<hyjf:validmessage key="utmMedium" label="推广方式(utm_medium)"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label" for="utmContent"> 
							推广单元(utm_content):
						</label>
						<div class="col-sm-8">
							<input type="text" id="utmContent" name="utmContent" value="<c:out value="${channelForm.utmContent}"></c:out>"
									class="form-control" datatype="*1-50" errormsg="推广单元(utm_content)应该输入1~50个字符！" maxlength="50" ignore="ignore" />
							<hyjf:validmessage key="utmContent" label="推广单元(utm_content)"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label" for="utmCampaign"> 
							推广计划(utm_campaign):
						</label>
						<div class="col-sm-8">
							<input type="text" id="utmCampaign" name="utmCampaign" value="<c:out value="${channelForm.utmCampaign}"></c:out>"
									class="form-control" datatype="*1-50" errormsg="推广计划(utm_campaign)应该输入1~50个字符！" maxlength="50" ignore="ignore" />
							<hyjf:validmessage key="utmCampaign" label="推广计划(utm_campaign)"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label" for="utmTerm"> 
							关键字(utm_term):
						</label>
						<div class="col-sm-8">
							<input type="text" id="utmTerm" name="utmTerm" value="<c:out value="${channelForm.utmTerm}"></c:out>"
									ignore="ignore" class="form-control" datatype="*1-50" errormsg="关键字(utm_term)应该输入1~50个字符！" maxlength="50" />
							<hyjf:validmessage key="utmTerm" label="关键字(utm_term)"></hyjf:validmessage>
							<hyjf:validmessage key="sourceId-utmTerm" label=""></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label" for="utmReferrer"> 
							推荐人:
						</label>
						<div class="col-sm-8">
							<input type="text" id="utmReferrer" name="utmReferrer" datatype="*" ignore="ignore" value="<c:out value="${utmReferrer}"></c:out>" class="form-control" ajaxurl="checkAction" />
							<hyjf:validmessage key="utmReferrer" label="推荐人"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label" for="linkAddress"> <span class="symbol required"></span>链接地址 </label>
						<div class="col-sm-9">
							<input type="text" placeholder="链接地址" id="linkAddress" name="linkAddress" value="<c:out value="${channelForm.linkAddress}"/>" maxlength="250" class="form-control" datatype="*1-250" errormsg="备注 只能是字符汉字，长度1~250个字符！" ignore="ignore" >
							<hyjf:validmessage key="linkAddress" label="链接地址"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label" > 状态 </label>
						<div class="col-sm-9">
							<div class="radio clip-radio radio-primary ">
								<input type="radio" id="statusOn" name="status" datatype="*" value="0" class="event-categories" ${ ( ( channelForm.status eq '0' ) or ( empty channelForm.status ) ) ? 'checked' : ''}> <label for="statusOn">  启用
								</label>
								<input type="radio" id="statusOff" name="status" datatype="*" value="1" class="event-categories" ${ channelForm.status eq '1' ? 'checked' : ''}> <label for="statusOff">  禁用
								</label>
							</div>
						</div>
						<hyjf:validmessage key="status" label="状态"></hyjf:validmessage>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label" for="remark"> 备注 </label>
						<div class="col-sm-9">
							<input type="text" placeholder="备注" id="remark" name="remark" value="<c:out value="${channelForm.remark}"/>" maxlength="100" class="form-control" datatype="*1-100" errormsg="备注 只能是字符汉字，长度1~100个字符！" ignore="ignore" >
							<hyjf:validmessage key="remark" label="备注"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group" style="${empty channelForm.utmId ? 'display:none;' : ''}">
						<label class="col-sm-3 control-label" for="utmReferrer"> 
							推广链接:
						</label>
						<div class="col-sm-9">
							<pre><c:out value="${url }"></c:out></pre>
						</div>
					</div>
					<div class="form-group margin-bottom-0">
						<div class="col-sm-offset-3 col-sm-9">
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
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/selectFx/classie.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/selectFx/selectFx.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/select2/select2.min.js"></script>
		<script type='text/javascript' src="${webRoot}/jsp/promotion/channel/channelInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
