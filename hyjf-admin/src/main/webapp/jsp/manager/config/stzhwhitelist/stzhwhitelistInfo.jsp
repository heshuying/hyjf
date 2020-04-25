<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp"
	flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="管理" />
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
			value="${empty stzfConfigForm.id ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin: 0">
			<div class="panel-body" style="margin: 0 auto">
				<div class="panel-scroll height-430">
					<form id="mainForm"
						action="${empty stzfConfigForm.id ? 'insertAction' : 'updateAction'}"
						method="post" role="form" class="form-horizontal">
						<%-- 列表一览 --%>
						<input type="hidden" name="id" id="id" value="${stzfConfigForm.id }" />
						<input type="hidden"  name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
						<input type="hidden" id="success" value="${success}" />
						<input type="hidden" name="userId" id="userId" value="${stzfConfigForm.userId}" />
						<input type="hidden" name="stUserId" id="stUserId" value="${stzfConfigForm.stUserId}" />
						<div class="form-group">
							<label class="col-sm-2 control-label" for="userName"> 
								<span class="symbol required"></span>机构/个人 用户名：
							</label>
							<div class="col-sm-10">
								<input type="text" placeholder="机构/个人 用户名" id="userName" name="userName" value="<c:out value="${stzfConfigForm.userName}" />"  class="form-control"
									datatype="*1-20" errormsg="机构/个人 用户名不能超过20个字符！" maxlength="20" ajaxurl="stzfWhiteConfigError"/>
								<hyjf:validmessage key="userName" label="机构/个人 用户名"></hyjf:validmessage>
							</div>
						</div>	
						<div class="form-group">
							<label class="col-sm-2 control-label" for="accountid"> 
								<span class="symbol required"></span>电子账号：
							</label>
							<div class="col-sm-10">
								<input type="text" placeholder="电子账号" id="accountid" name="accountid" value="<c:out value="${stzfConfigForm.accountid}" />"  class="form-control"
									datatype="n" errormsg="电子账号只能是数字！" maxlength="20" readonly="readonly" />
								<hyjf:validmessage key="accountid" label="电子账号"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="mobile"> 
								<span class="symbol required"></span>手机号：
							</label>
							<div class="col-sm-10">
								<input type="text" placeholder="手机号" id="mobile" name="mobile" value="<c:out value="${stzfConfigForm.mobile}" />"  class="form-control"
									datatype="m" errormsg="只能输入正确的手机号！" maxlength="20" readonly="readonly" />
								<hyjf:validmessage key="mobile" label="手机号"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="customerName"> 
								<span class="symbol required"></span>名称/姓名：
							</label>
							<div class="col-sm-10">
								<input type="text" placeholder="名称/姓名" id="customerName" name="customerName" value="<c:out value="${stzfConfigForm.customerName}" />"  class="form-control"
									datatype="*1-20" errormsg="名称/姓名不能超过20个字符！" maxlength="20" readonly="readonly" />
								<hyjf:validmessage key="customerName" label="名称/姓名"></hyjf:validmessage>
							</div>
						</div>
						<div   class="form-group">
							<label class="col-sm-2 control-label" for="instname">
								<span class="symbol required"></span>机构名称/姓名:
							</label>
							<div class="col-sm-10 admin-select">
								<select  id="instname" name="instname"
									class="form-select2"  style="width: 100%">
									<option value=""></option>
									<c:forEach items="${stzfConfigForm.regionList}" var="item">
										<option value="${item.instCode}"
												<c:if test="${item.instCode==stzfConfigForm.instcode}">selected</c:if>>${item.instName }</option>
									</c:forEach>
								</select>
							</div>
							<input type="hidden" name="instcode"  id="instcode" value="" >
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="stUserName"> 
								<span class="symbol required"></span>受托支付收款人：
							</label>
							<div class="col-sm-10">
								<input type="text" placeholder="受托支付收款人" id="stUserName" name="stUserName" value="<c:out value="${stzfConfigForm.stUserName}" />"  class="form-control"
									datatype="*1-20" errormsg="收款人不能超过20个字符！" maxlength="20" ajaxurl="stzfWhiteConfigStNameError"/>
								<hyjf:validmessage key="stUserName" label="受托支付收款人"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="stAccountid"> 
								<span class="symbol required"></span>收款人电子账号：
							</label>
							<div class="col-sm-10">
								<input type="text" placeholder="收款人电子账号" id="stAccountid" name="stAccountid" value="<c:out value="${stzfConfigForm.stAccountid}" />"  class="form-control"
									datatype="n" errormsg="收款人电子账号只能是数字！" maxlength="20" readonly="readonly" />
								<hyjf:validmessage key="stAccountid" label="收款人电子账号"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="stMobile"> 
								<span class="symbol required"></span>收款人  手机号：
							</label>
							<div class="col-sm-10">
								<input type="text" placeholder="收款人  手机号" id="stMobile" name="stMobile" value="<c:out value="${stzfConfigForm.stMobile}" />"  class="form-control"
									datatype="m" errormsg="只能输入正确的手机号" maxlength="20" readonly="readonly" />
								<hyjf:validmessage key="stMobile" label="收款人手机号"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="stCustomerName"> 
								<span class="symbol required"></span>收款人名称/姓名：
							</label>
							<div class="col-sm-10">
								<input type="text" placeholder="收款人名称/姓名" id="stCustomerName" name="stCustomerName" value="<c:out value="${stzfConfigForm.stCustomerName}" />"  class="form-control"
									datatype="*1-20" errormsg="收款人名称/姓名不能超过20个字符！" maxlength="20" readonly="readonly" />
								<hyjf:validmessage key="stCustomerName" label="收款人名称/姓名"></hyjf:validmessage>
							</div>
						</div>
						 <div class="form-group">
							<label class="col-sm-2 control-label" for="approvalTime"> 
								<span class="symbol required"></span>审批时间：
							</label>
							<div class="col-sm-10">
							<div class="input-group input-daterange datepicker">
								<!-- <span class="col-sm-10"> -->
									
										<input type="text" name="approvalTime" id="approvalTime" class="form-control" value="${stzfConfigForm.approvalTime}" />
									
								<!-- </span> -->
							</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="approvalName"> 
								<span class="symbol required"></span>审批人：
							</label>
							<div class="col-sm-10">
								<input type="text" placeholder="审批人" id="approvalName" name="approvalName" value="<c:out value="${stzfConfigForm.approvalName}" />"  class="form-control"
									datatype="*1-20" errormsg="审批人不能超过20个字符！" maxlength="20" />
								<hyjf:validmessage key="approvalName" label="审批人"></hyjf:validmessage>
							</div>
						</div>		
						<%-- <c:if test="${not empty instConfigForm.id}">	
							<div class="form-group">
								<label class="col-sm-2 control-label" for="instCode"> 
									资产来源编号 :
								</label>
								<div class="col-sm-10">
									<input type="text" disabled="disabled" placeholder="资产来源编号" id="instCode" name="instCode" value="<c:out value="${instConfigForm.instCode}" />"  class="form-control" />
								</div>
							</div>	
						</c:if>		 --%>		
						<%-- <div class="form-group">
							<label class="col-sm-2 control-label" for="capitalToplimit"> <span class="symbol required"></span>发标额度上限</label>
							<div class="col-sm-10">
								<div class="input-group">
									<input type="text" placeholder="发标额度上限" id="capitalToplimit" name="capitalToplimit" value="<c:out value="${instConfigForm.capitalToplimit}" />"  class="form-control"
										datatype="/^\d{1,10}(\.\d{1,2})?$/" errormsg="发标额度上限必须为数字，整数部分不能超过10位，小数部分不能超过2位！" maxlength="13">
									<span class="input-group-addon">元</span>
								</div>
								<hyjf:validmessage key="capitalToplimit" label="发标额度上限"></hyjf:validmessage>
							</div>
						</div> --%>
						 <div class="form-group">
							<label class="col-sm-2 control-label" for="remark"> <span
								class="symbol required"></span>备注:
							</label>
							<div class="col-sm-10">
								<textarea datatype="*1-80" errormsg="备注不超过80个字符!" placeholder="备注"
									id="remark" name="remark" class="form-control limited">${stzfConfigForm.remark}</textarea>
							</div>
						</div> 
						<div class="form-group">
							<label class="col-sm-2 control-label" for="state"> <span
								class="symbol required"></span>状态:
							</label>
							<div class="col-xs-10">
								<div style="float:left;">
									<input checked="true" type="radio" name="state" datatype="*"
										value="1" class="event-categories"
										${stzfConfigForm.state == '1' ? 'checked' : ''}> <label
										> 启用 </label> <input type="radio" 
										name="state" datatype="*" value="0" class="event-categories"
										${stzfConfigForm.state == '0' ? 'checked' : ''}> <label
										> 禁用 </label>
								</div>
							</div>
							<hyjf:validmessage key="state" label="状态"></hyjf:validmessage>
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
	<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/bootstrap-datepicker/bootstrap-datepicker3.standalone.min.css" rel="stylesheet" media="screen">
		</tiles:putAttribute>
		
		

		
		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>
	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
		<script type="text/javascript" 
			src="${themeRoot}/vendor/plug-in/bootstrap-datepicker/bootstrap-datepicker.min.js"></script>
		<script type='text/javascript'
			src="${webRoot}/jsp/manager/config/stzhwhitelist/stzhwhitelistInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
