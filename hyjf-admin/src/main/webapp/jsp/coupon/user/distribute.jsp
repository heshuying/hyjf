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
	<tiles:putAttribute name="pageTitle" value="会员信息修改" />
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<div class="panel panel-white no-margin">
			<div class="panel-body">
				<form id="mainForm" action="distributeAction"
						method="post"  role="form" class="form-horizontal" >
					<%-- 角色列表一览 --%>
					<input type="hidden" name="userId" id="userId" value="${usersUpdateForm.userId }" />
					 <input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
					<input type="hidden" id="success" value="${success}" />
					<input type="hidden" id="hasError" value="${hasError}" />
					<div class="panel-scroll height-340 margin-bottom-15">
						<div class="form-group">
							<label class="col-xs-3 control-label text-right padding-top-5 padding-right-0" for="userRole"> 
								<span class="symbol required"></span>优惠券
							</label>
							<div class="col-xs-9 inline-block admin-select">
								<select id="couponCode" name="couponCode" class="form-control input-sm" datatype="*" errormsg="请选择优惠券！" style="width:100%">
									<option value="">选择优惠券</option>
									<c:if test="${!empty configForm}">
										<c:forEach items="${configForm }" var="config" begin="0" step="1" varStatus="status">
										  <c:if test="${config.isExpiration == 1}">
											<option value="${config.couponCode }" data-class="fa">${config.couponName }</option>
										  </c:if>
										</c:forEach>
									</c:if>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label text-right padding-top-5 padding-right-0" for="userRole"> 
								活动
							</label>
							<div class="col-xs-9 inline-block admin-select">
								<select id="activityId" name="activityId" class="form-control input-sm" style="width:100%">
									<option value="" >选择活动</option>
									<c:if test="${!empty activeListForm}">
										<c:forEach items="${activeListForm }" var="active" begin="0" step="1" varStatus="status">
											<option value="${active.id }" data-class="fa">${active.title }</option>
										</c:forEach>
									</c:if>
								</select>
							</div>
						</div>
						<div class="col-xs-12 margin-bottom-20 margin-left-15">
							<fieldset class="margin-top-15 margin-right-0">
								<legend>
									优惠券信息
								</legend>
								<table class="table table-condensed margin-bottom-0">
									<colgroup>
										<col width="90px"></col>
										<col></col>
									</colgroup>
									<tbody>
										<tr>
											<td>面值</td>
											<td id="couponQuota"></td>
										</tr>
										<tr>
											<td>有效期</td>
											<td id="expirationDate"></td>
										</tr>
										<tr id="profitTimeArea" >
											<td>收益期限</td>
											<td id="profitTime"></td>
										</tr>
										<tr id="addFlgArea" >
											<td>是否共用</td>
											<td id="addFlg"></td>
										</tr>
										<tr>
											<td>适用平台</td>
											<td id="couponSystem"></td>
										</tr>
										<tr>
											<td>适用项目期限</td>
											<td id="projectExpirationLength"></td>
										</tr>
										<tr>
											<td>适用项目类型</td>
											<td id="projectType"></td>
										</tr>
										<tr>
											<td>适用项目金额</td>
											<td id="tenderQuota"></td>
										</tr> 	
									</tbody>
								</table>
							</fieldset>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label text-right padding-top-5 padding-right-0" for="mobile"> 
								<span class="symbol required"></span>用户名
							</label>
							<div class="col-xs-9">
								<input type="text" placeholder="用户名" class="form-control input-sm" id="username" name="username" value=""
										datatype="*" nullmsg="请填写用户名！" ajaxurl="checkUserAction">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label text-right padding-top-5 padding-right-0" for="mobile"> 
								<span class="symbol required"></span>数量
							</label>
							<div class="col-xs-9">
								<input type="text" placeholder="数量" class="form-control input-sm" id="amount" name="amount" value=""
										datatype="*" nullmsg="请填写发放数量！" >
							</div>
						</div>
						<div class="form-group margin-bottom-5">
							<label class="col-xs-3 control-label text-right padding-top-5 padding-right-0" for="content"> 
								<span class="symbol required"></span>备注
							</label>
							<div class="col-xs-9">
								<textarea rows="4" placeholder="备注" id="content" name="content" class="form-control"
										maxlength="60" datatype="*" nullmsg="请填写说明信息" errormsg="说明信息最长为60字符" ></textarea>
							</div>
						</div>
					</div>
					<div class="form-group margin-bottom-0">
						<div class="col-xs-offset-2 col-xs-10">
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
		<script type='text/javascript' src="${webRoot}/jsp/coupon/user/distribute.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
