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
	<tiles:putAttribute name="pageTitle" value="企业信息补录" />
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<div class="panel panel-white" style="margin:0">
				<form id="mainForm" action="companyInfo"
						method="post"  role="form" class="form-horizontal" >
					<%-- 角色列表一览 --%>
					<input type="hidden" name="userId" id="userId" value="${user.userId }" />
					<input type="hidden" id="success" value="" />
					<input type="hidden" id="hasError" value="" />
					<input type="hidden" id="isOpenAccount" value=""/>
					<input type="hidden" id="idType" value="${companyInfo.cardType}"/>
					<input type="hidden" id="userType" value="${user.userType}"/>
					<div class="panel-scroll margin-bottom-15">
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="userName">
								<span class="symbol"></span>用户名 :
							</label>
							<div class="col-xs-10">
								<span class="badge badge-inverse"> ${user.username} </span>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="userName">
								<span class="symbol"></span>用户类型 :
							</label>
							<div class="col-xs-10">
								<c:out value="${user.userType == '1' ? '企业用户':'个人用户'}"></c:out>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="accountId"> 
								<span class="symbol"></span>电子账号 :
							</label>
							<div class="col-xs-10">
								<input type="text" placeholder="电子账号" style="width: 80%;" class="input-sm" id="accountId" name="accountId" value="${bankOpenAccount.account }">
								<a class="btn btn-o btn-primary fn-Company">查询</a>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="companyName">
								<span class="symbol"></span>企业名称 :
							</label>
							<div class="col-xs-10">
								<span id="companyName">${companyInfo.name} </span>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="cardType">
								<span class="symbol"></span>证件类型 :
							</label>
							<div class="col-xs-10">
								<span id="cardType">${companyInfo.idType} </span>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="cardType">
								<span class="symbol"></span>证件号码 :
							</label>
							<div class="col-xs-10">
								<span id="idNo">${companyInfo.idNo}</span>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="mobile">
								<span class="symbol"></span>手机号 :
							</label>
							<div class="col-xs-10">
								<span id="mobile">${user.mobile}</span>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="account">
								<span class="symbol"></span>对公账号 :
							</label>
							<div class="col-xs-10">
								<span id="account">${companyInfo.account}</span>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="buseId">
								<span class="symbol"></span>营业执照编号:
							</label>
							<div class="col-xs-10">
								<input type="text" placeholder="营业执照编号" style="width: 80%;" class="input-sm" id="buseId" name="buseId" value="${companyInfo.busId}">
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="taxId">
								<span class="symbol"></span>税务登记号: 
							</label>
							<div class="col-xs-10">
								<input type="text" placeholder="税务登记号" style="width: 80%;" class="input-sm" id="taxId" name="taxId" value="${companyInfo.taxId}">
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="remark"> 
								<span class="symbol required"></span>修改说明
							</label>
							<div class="col-xs-10">
								<textarea rows="4" placeholder="说明" class="form-control input-sm"
										id="remark" name="remark"  maxlength="50" datatype="*" nullmsg="请填写说明信息" errormsg="说明信息最长为60字符" >${companyInfo.remark}</textarea>
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
	</tiles:putAttribute>
	
	<%-- JS全局变量定义、插件 (ignore) --%>
	<tiles:putAttribute name="pageGlobalImport" type="string">
		<!-- Form表单插件 -->
		<%@include file="/jsp/common/pluginBaseForm.jsp"%>
	</tiles:putAttribute>  
	
	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type='text/javascript' src="${webRoot}/jsp/manager/users/userlist/companyInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
